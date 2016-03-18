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

import java.util.Properties;

public interface LogFilter {
	/**
	 * Set properties
	 * @param prop
	 */
	void setProperties(Properties prop);
	
	/**
	 * Invoked when a simulator starts
	 * @param serverName
	 */
	void start(String serverName);
	
	/**
	 * Invoked when a log is written
	 * If return value is null, a log will not
	 * be written into a log file
	 * @param log
	 * @return a log which should be written into a log file
	 */
	Log write(Log log);
	
	/**
	 * Invoked when this log filter is closed.
	 */
	void close();
}
