/*
 *  This file is part of the XASDI project (http://x10-lang.org/xasdi/).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2018.
 */

package com.ibm.xasdi_bridge.rt.log;

import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import com.ibm.xasdi_bridge.log.ColumnType;
import com.ibm.xasdi_bridge.log.Log;
import com.ibm.xasdi_bridge.log.LogDefinition;
import com.ibm.xasdi_bridge.log.LogFilter;
import com.ibm.xasdi_bridge.log.Logger;
import com.ibm.xasdi_bridge.util.HPHashMap;
import com.ibm.xasdi_bridge.util.IndexMap;

/**
 * @author yamamoto
 *
 */
public class LogWriter {
	static final java.util.logging.Logger javaLogger = java.util.logging.Logger.getLogger("XASDILOG");

	static final int DEFSIZE = 1024*1024; //1MB
	static class FreeRecords {
		LogImpl free = null;
	}
	
	static class LogOutputStream extends ByteArrayOutputStream {
		int limit;
		LogOutputStream(int s) {
			super(s);
			limit = s;
		}
		
		public int capacity() {
			return limit;
		}
	}

	static class LogWriterEntry {
		LogWriter parent;
		HPHashMap map;
		LogWriterEntry(LogWriter lw) {
			parent = lw;
			map = new HPHashMap();
		}
	}
	
	static LogWriterEntry[] logWriterTable = null;
	
	public static void addLogWriter(long regionid, LogWriter lw) {
		if (logWriterTable == null) {
			logWriterTable = new LogWriterEntry[(int)regionid+1];
			logWriterTable[(int)regionid] = new LogWriterEntry(lw);
		} else {
			if (logWriterTable.length >= regionid+1) {
				logWriterTable[(int)regionid] = new LogWriterEntry(lw);
			} else {
				LogWriterEntry[] h = new LogWriterEntry[(int)regionid+1];
				System.arraycopy(logWriterTable,0,h,0,logWriterTable.length);
				h[(int)regionid] = new LogWriterEntry(lw);
				logWriterTable = h;
			}
		}
	}
	
	public static LogWriter getLogWriter(long regionid) {
		LogWriterEntry e = logWriterTable[(int)regionid];

		if(e == null) {
			javaLogger.info("LogWriterEntry is null: regionid=" + regionid);
			System.exit(1);
		}
		return e.parent;
	}
	
	public static LogWriter getChildLogWriter(long regionid) {
		Thread th = Thread.currentThread();
		
		LogWriterEntry e = logWriterTable[(int)regionid];
		LogWriter lw = (LogWriter)e.map.get(th);
		if (lw == null) {
			try {
				LogWriter child = e.parent.getChildLogWriter();
				e.map.put(th,child);
				return child;
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return lw;
	}
	
	public static void removeAllLogWriters() {
		logWriterTable = null;
	}
	
	long regionID;
	IndexMap freeRecords = null;
	LogOutputStream bout;
	ObjectOutputStream out;
	File file;
	int serialNumber = 0;
	int simulationCount;
	boolean written = false;
	ChildLogWriter[] children = null;
	Logger logger;
	int bufferSize;
	LogFilter filter = null;
	
	public LogWriter(Logger logger, long regionID, int simulationCount, int bufferSize, File file) throws IOException {
		this.logger = logger;
		this.regionID = regionID;
		this.freeRecords = new IndexMap(10);
		this.bout = new LogOutputStream(bufferSize);
		this.out = new ObjectOutputStream(bout);
		this.file = file;
		this.simulationCount = simulationCount;
		this.bufferSize = bufferSize;
		filter = logger.getLogFilter();
		writeHeader();
	}
	
	public LogWriter(Logger logger, long regionID, int simulationCount, File file) throws IOException {
		this(logger, regionID, simulationCount, DEFSIZE,file);
	}
	
	public LogFilter getLogFilter() {
		return filter;
	}
	
	public LogWriter getParent() {
		return this;
	}
	
	public synchronized ChildLogWriter getChildLogWriter() throws IOException {
		ChildLogWriter clog = new ChildLogWriter(regionID,0,bufferSize,this);
		if (children == null) {
			children = new ChildLogWriter[1];
			children[0] = clog;
		} else {
			ChildLogWriter[] n = new ChildLogWriter[children.length+1];
			System.arraycopy(children,0,n,0,children.length);
			n[children.length] = clog;
			children = n;
		}
		return clog;
	}
	
	public ChildLogWriter[] getChildLogWriters() {
		return children;
	}
	
	public void flushChildLogWriters() throws IOException {
		for(int i=0;i<children.length;i++) {
			children[i].flush();
		}
	}
	
	public void close() {
		if(children != null){
			for(int i=0;i<children.length;i++) {
				children[i].close();
			}
		}
		writeToFile();
		if (filter != null) {
			filter.close();
		}
	}

	public Log getRecord(int id) {
		LogDefinition def = logger.getLogDefinition(id);
		if (def == null) {
			return null;
		}
		FreeRecords fr = (FreeRecords)freeRecords.get(id);
		if (fr == null) {
			fr = new FreeRecords();
			freeRecords.put(id,fr);
		}
		
		LogImpl rec;
		if (fr.free == null) {
			rec = new LogImpl(def);
		} else {
			rec = fr.free;
			fr.free = rec.next;
		}
		rec.activate();
		return rec;
	}
	
	synchronized void write(LogImpl log) throws IOException {
		if (filter != null) {
			Log l = filter.write(log);
			if (l == null) {
				return;
			}
			log = (LogImpl)l;
		}
		written = true;
		
		int len = 8; // for header
		for(int i=0;i<log.entries.length;i++) {
			len += log.entries[i].getLength();
		}
		ensureCapacity(len);
		out.writeInt(log.definition.getID());
		out.writeInt(log.entries.length);
		for(int i=0;i<log.entries.length;i++) {
			log.entries[i].write(out);
		}
		out.flush();
	}
	
	void release(LogImpl rec) {
		rec.deactivate();
		int id = rec.definition.getID();
		FreeRecords fr = (FreeRecords)freeRecords.get(id);
		if (fr == null) return;
		rec.next = fr.free;
		fr.free = rec;
	}
	
	private void writeHeader() throws IOException {
		out.writeLong(regionID);
		out.writeInt(serialNumber);
		out.writeInt(logger.getNumberOfLogDefinitions());
		Iterator <Object> it = logger.getLogDefinitions();
		while(it.hasNext()) {
			LogDefinition def = (LogDefinition)it.next();
			int n = def.getNumberOfColumns();
			out.writeInt(def.getID());
			out.writeInt(n);
			for(int i=0;i<n;i++) {
				ColumnType type = def.getColumnType(i);
				out.writeInt(type.intValue());
			}
		}
	}

	private void flush() throws IOException {
		writeToFile();
		out.close();
		bout.reset();
		out = new ObjectOutputStream(bout);
		writeHeader();
		written = false;
	}
	
	private void ensureCapacity(int len) throws IOException {
		if (bout.capacity() <= bout.size() + len) {
			flush();
		}
	}
	
	private synchronized void writeToFile() {
		try {
			if (written == false) {
				javaLogger.info("Not written");
				return;
			}
			out.flush();
			File f;				//	avoid from conflicting file
			do{
				f = new File(file.getAbsolutePath() + "_" + regionID + "_" + simulationCount + "_" + serialNumber + ".log");
				serialNumber++;
			}while(!f.createNewFile());
			javaLogger.info("write log " + f.toString());
			com.ibm.xasdi_bridge.fd.engine.Log.info("LogWriter","Write logs into file:" + f);
			FileOutputStream fout = new FileOutputStream(f, true);
			bout.writeTo(fout);
			fout.flush();
			fout.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
