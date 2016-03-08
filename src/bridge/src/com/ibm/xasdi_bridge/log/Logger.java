/*
 *  This file is part of the XASDI project (https://github.com/x10-lang/XASDI).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2016.
 */

package com.ibm.xasdi_bridge.log;

import java.io.File;
import java.util.Iterator;

/**
 * A Logger object is used to write log records. 
 * @author yamamoto
 */
public interface Logger {
	/**
	 * Sets the log file.
	 * @param file the log file
	 */
	void setFile(File file);
    
	/**
	 * Gets the log file.
	 * @return log file
	 */
	File getFile();
    
	/**
	 * Gets a reusable log entry.
	 * @param id 
	 * @return a log entry
	 * @throws IllegalStateException
	 */
	Log getFreeLog(int id) throws IllegalStateException;
    
	/**
	 * Registers a log definition (schema).
	 * @param def a log definition
	 * @throws IllegalStateException
	 */
	void addLogDefinition(LogDefinition def) throws IllegalStateException;
	
	/**
	 * Gets a log definition (schema) registerd to this logger.
	 * @param id ID of a log definition
	 * @return log definition (schema)
	 */
	LogDefinition getLogDefinition(int id);
	
	/**
	 * Gets the list of log definitions (schemata) registered to this logger. 
	 * @return list of log definitions (schemata)
	 */
	Iterator <Object> getLogDefinitions();
    
	/**
	 * Gets the number of log definitions registered to this logger.
	 * @return number of log definitions
	 */
	int getNumberOfLogDefinitions();
    
	/**
	 * Disable the specified type of logs.
	 * @param id 
	 */
	void disableLog(int id);
	
	/**
	 * Disable all logs
	 *
	 */
	void disableAllLogs();
	
	/**
	 * Enable the specified type of logs
	 * @param id
	 */
	void enableLog(int id);
	
	/**
	 * Enable all logs
	 *
	 */
	void enableAllLogs();
	
	/**
	 * Check whether the specified log type is enabled
	 * @param id
	 * @return true if the log type is enabled
	 */
	boolean isEnabled(int id);
	
	/**
	 * Check whether the specified log type is disabled
	 * @param id
	 * @return true if the log type is disabled
	 */
	boolean isDisabled(int id);
	
	/**
	 * Set a log filter
	 * @param filter
	 */
	public void setLogFilter(LogFilter filter);
	
	/**
	 * Get a log filter 
	 * @return a log filter
	 */
	public LogFilter getLogFilter();
}
