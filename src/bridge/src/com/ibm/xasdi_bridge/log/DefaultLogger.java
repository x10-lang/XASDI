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

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import com.ibm.xasdi_bridge.rt.log.LogWriter;
import com.ibm.xasdi_bridge.simulator.World;
import com.ibm.xasdi_bridge.util.IndexMap;

/**
 * DefaultLogger is used if no logger is registered.
 * @author yamamoto
 */
public class DefaultLogger implements Logger {
	
	private static final int DEFENTRY = 1000;
	
	private File file;
	private IndexMap definitions = new IndexMap(DEFENTRY);
	private LogFilter filter = null;
	
	/**
	 * Sets the log file.
	 * @param file log file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Gets the log file.
	 * @return log file
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Get a log object of the log record which is defined with a given id.
	 * Throw an exception if World has not been started yet.
	 * @param id
	 */
	public Log getFreeLog(int id) throws IllegalStateException {
		if (!World.world().isAtWork()) {
			throw new IllegalStateException("not packed");
		}
		LogDefinition def = (LogDefinition)definitions.get(id);
		LogWriter logger = LogWriter.getLogWriter(def.getRegionID());
		if (logger == null) return null;
		return logger.getRecord(id);
	}
	
	/**
	 * Add a log definition.
	 * Throw an exception if World has already been started.
	 * @param def 
	 */
	public void addLogDefinition(LogDefinition def) throws IllegalStateException {

		synchronized(definitions) {
			definitions.put(def.getID(),def);
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("[");
		ColumnType[] ct = def.getColumnTypes();
		for(int i=0;i<ct.length;i++) {
			pw.println("    " + i + "/" + ct[i]);
		}
		pw.println("]");

		com.ibm.xasdi_bridge.fd.engine.Log.info("DefaultLogger","A log is defined:[" + def.getID() + "] " + sw.toString());
	}
	
	/**
	 * Get a log definition of a given id. 
	 * Throw an exception if World has not been started yet.
	 * @param id
	 * @return a log definition object
	 */
	public LogDefinition getLogDefinition(int id) {
		if (!World.world().isAtWork()) {
			throw new IllegalStateException("not packed");
		}
		return (LogDefinition)definitions.get(id);
	}
	
	/**
	 * Get an iterator object containing log definitions
	 * Throw an exception if World has not been started yet.
	 * @return an iterator object
	 */
	public Iterator <Object> getLogDefinitions() {
		if (!World.world().isAtWork()) {
			throw new IllegalStateException("not packed");
		}
		return definitions.iterator();
	}
	
	/**
	 * Get the number of log definitions
	 * @return the number of log definitions
	 */
	public int getNumberOfLogDefinitions() {
		return definitions.size();
	}
	
	/**
	 * Disable a log of a given id
	 * @param id
	 */
	public void disableLog(int id) {
		if (World.world().isAtWork()) {
			throw new IllegalStateException("not packed");
		}
		LogDefinition def = (LogDefinition)definitions.get(id);;
		if (def == null) {
			return;
		}
		def.logging(false);
	}
	
	/**
	 * Disable all logs
	 */
	public void disableAllLogs() {
		if (World.world().isAtWork()) {
			throw new IllegalStateException("not packed");
		}
		Iterator <Object> it = definitions.iterator();
		while(it.hasNext()) {
			LogDefinition def = (LogDefinition)it.next();
			def.logging(false);
		}
	}
	
	/**
	 * Enable a log of a given id
	 * @param id
	 */
	public void enableLog(int id) {
		if (World.world().isAtWork()) {
			throw new IllegalStateException("not packed");
		}
		LogDefinition def = (LogDefinition)definitions.get(id);;
		if (def == null) {
			return;
		}
		def.logging(true);
	}
	
	/**
	 * Enable all logs
	 */
	public void enableAllLogs() {
		if (World.world().isAtWork()) {
			throw new IllegalStateException("not packed");
		}
		Iterator <Object> it = definitions.iterator();
		while(it.hasNext()) {
			LogDefinition def = (LogDefinition)it.next();
			def.logging(true);
		}
	}
	
	/**
	 * Check whether a log of a given id is enabled
	 * @param id
	 * @return true if a log of a given id is enabled
	 */
	public boolean isEnabled(int id) {
		LogDefinition def = (LogDefinition)definitions.get(id);;
		if (def == null) {
			return false;
		}
		return def.isEnabled();
	}
	
	/**
	 * Check whether a log of a given id is disabled
	 * @param id
	 * @return true if a log of a given id is disabled
	 */
	public boolean isDisabled(int id) {
		LogDefinition def = (LogDefinition)definitions.get(id);;
		if (def == null) {
			return true;
		}
		return def.isDisabled();
	}

	/**
	 * Set a log filter
	 * @param filter a log filter
	 */
	public void setLogFilter(LogFilter filter) {
		this.filter = filter;
	}
	
	/**
	 * Get a log filter
	 * @return a log filter
	 */
	public LogFilter getLogFilter() {
		return filter;
	}
}
