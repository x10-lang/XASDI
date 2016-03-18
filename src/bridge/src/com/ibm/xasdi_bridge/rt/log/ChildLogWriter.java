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

import java.io.IOException;
import java.util.Vector;


/**
 * @author yamamoto
 *
 */
public class ChildLogWriter extends LogWriter {
	LogWriter parent;
	Vector <LogImpl> logs = new Vector<LogImpl>();
	
	public ChildLogWriter(long regionID, int simulationCount, int bufferSize, LogWriter parent) throws IOException {
		super(parent.logger,regionID,simulationCount,bufferSize,null);
		this.parent = parent;
	}
	
	public ChildLogWriter(long regionID, int simulationCount, LogWriter parent) throws IOException {
		super(parent.logger,regionID,simulationCount,null);
		this.parent = parent;
	}

	public void close() {
		try {
			flush();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public LogWriter getParent() {
		return parent;
	}
	
	void write(LogImpl log) throws IOException {

		logs.add(log);
	}
	
	public void flush() throws IOException {
		for(int i=0;i<logs.size();i++) {
			LogImpl log = logs.elementAt(i);
			parent.write(log);
			release(log);
		}
		logs.removeAllElements();
	}
}