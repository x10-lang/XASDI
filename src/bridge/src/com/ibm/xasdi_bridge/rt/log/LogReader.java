/*
 *  This file is part of the XASDI project (http://x10-lang.org/xasdi/).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2016.
 */

package com.ibm.xasdi_bridge.rt.log;

import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.ibm.xasdi_bridge.log.ColumnType;
import com.ibm.xasdi_bridge.log.Log;
import com.ibm.xasdi_bridge.log.LogDefinition;
import com.ibm.xasdi_bridge.util.IndexMap;

/**
 * @author yamamoto
 *
 */
public class LogReader {
	String fileName;
	long regionID;
	int serialNumber;
	IndexMap definition = null;
	ObjectInputStream in = null;
	
	public LogReader(String fname) throws IOException {
		fileName = fname;
		readHeader(new FileInputStream(fileName));
	}
	
	public long getRegionID() {
		return regionID;
	}
	
	public int getSerialNumber() {
		return serialNumber;
	}
	
	void readHeader(FileInputStream fin) throws IOException {
		in = new ObjectInputStream(fin);
		regionID = in.readLong();
		serialNumber = in.readInt();
		int numOfDef = in.readInt();
		definition = new IndexMap(numOfDef);
		for(int i=0;i<numOfDef;i++) {
			int id = in.readInt();
			int n = in.readInt();
			LogDefinition def = new LogDefinition(id,regionID);
			for(int j=0;j<n;j++) {
				int t = in.readInt();
				ColumnType type = ColumnType.getFromInt(t);
				def.addColumn(type);
			}
			definition.put(def.getID(),def);
		}
	}
	
	public boolean hasNext() throws IOException {
		if (in.available() < 8) {
			return false;
		}
		return true;
	}
	
	
	public Log next() throws IOException {
		if (in.available() < 8) {
			return null;
		}
		int id = in.readInt();
		int n = in.readInt();
		
		LogDefinition def = (LogDefinition)definition.get(id);
		if (def == null) {
			throw new IOException("no log definition:" + id);
		} else {
			LogImpl rec = new LogImpl(def);
			rec.activate();
			for(int i=0;i<n;i++) {
				ColumnType t = def.getColumnType(i);
				if (t == ColumnType.BOOLEAN) {
					boolean v = in.readBoolean();
					rec.setBoolean(i,v);
				} else if (t == ColumnType.BYTE) {
					byte v = in.readByte();
					rec.setByte(i,v);
				} else if (t == ColumnType.DOUBLE) {
					double v = in.readDouble();
					rec.setDouble(i,v);
				} else if (t == ColumnType.FLOAT) {
					float v = in.readFloat();
					rec.setFloat(i,v);
				} else if (t == ColumnType.INT) {
					int v = in.readInt();
					rec.setInt(i,v);
				} else if (t == ColumnType.LONG) {
					long v = in.readLong();
					rec.setLong(i,v);
				} else if (t == ColumnType.OBJECT) {
					try {
						Object v = in.readObject();
						rec.setObject(i,v);
					} catch(ClassNotFoundException cne) {
						throw new IOException(cne.getMessage());
					}
				} else if (t == ColumnType.SHORT) {
					short v = in.readShort();
					rec.setShort(i,v);
				} else if (t == ColumnType.STRING) {
					String v = readString();
					rec.setString(i,v);
				}
			}
			return rec;
		}
	}
	
	private String readString() throws IOException {
		int len = in.readInt();
		if (len == 0) {
			return null;
		}
		byte[] b = new byte[len];
		in.read(b);
		String str = new String(b);
		return str;
	}
}
