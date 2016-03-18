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

package com.ibm.xasdi_bridge.log;

import java.io.IOException;

import com.ibm.xasdi_bridge.rt.log.LogReader;

/**
 * A LogSet object reads a header of a set of log files. 
 * @author yamamoto
 *
 */
public class LogSet {
	LogReader reader = null;

	/**
	 * Get a LogSet object corresponding to a set of log files 
	 * @param filename
	 * @return LogSet object
	 * @throws IOException
	 */
	public static LogSet read(String filename) throws IOException {
		return new LogSet(filename);
	}
	
	LogSet(String fname) throws IOException {
		reader = new LogReader(fname);
	}
	
	/**
	 * Get a region id
	 * @return ID of belonging region
	 */
	public long getRegionID() {
		return reader.getRegionID();
	}
	
	/**
	 * Get a serial number
	 * @return Serial number of log
	 */
	public int getSerialNumber() {
		return reader.getSerialNumber();
	}
	
	/**
	 * Check whether unread log file exists
	 * @return true if a unread log file exists
	 */
	public boolean hasNext() {
		try {
			return reader.hasNext();
		} catch(IOException e) {
			return false;
		}
	}
	
	/**
	 * Move a next log file
	 * @return Next log file
	 */
	public Log next() {
		try {
			return reader.next();
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
