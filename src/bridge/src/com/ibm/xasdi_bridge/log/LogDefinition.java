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

/**
 * A LogDefinition object defines a schema of a log record.
 * 
 * @see Log
 * @author yamamoto
 */
public class LogDefinition {
	private int id;
	private ColumnType[] types = null;
	private long regionID;
	private boolean logging = true;
	
	/**
	 * Creates a LogDefinition for the specified
	 * {@link com.ibm.xasdi_bridge.simulator.Region}.
	 * @param id ID of this log definition 
	 * @param regionid ID of a {@link com.ibm.xasdi_bridge.simulator.Region}
	 */
	public LogDefinition(int id, long regionid) {
		this.id = id;
		this.regionID = regionid;
	}
	
	/**
	 * Gets ID of the region.
	 * @return region ID
	 */
	public long getRegionID() {
		return regionID;
	}
	
	/**
	 * Adds a column definition.
	 * @param t column type
	 */
	public void addColumn(ColumnType t) {
		if (types == null) {
			types = new ColumnType[1];
			types[0] = t;
		} else {
			ColumnType[] tt = new ColumnType[types.length+1];
			System.arraycopy(types,0,tt,0,types.length);
			tt[types.length] = t;
			types = tt;
		}
	}

	/**
	 * Gets the ID of this log definition (metadata).
	 * @return ID of this log definition (metadata)
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Gets type of the i-th column. 
	 * @param i column number (starting from 0)
	 * @return type of the specified column
	 */
	public ColumnType getColumnType(int i) {
		return types[i];
	}
	
	/**
	 * Get an array of ColumnType
	 * @return an array of ColumnType
	 */
	public ColumnType[] getColumnTypes() {
		return types;
	}
	
	/**
	 * Gets the number of columns in this log record.
	 * @return number of columns
	 */
	public int getNumberOfColumns() {
		return types.length;
	}
	
	void logging(boolean b) {
		logging = b;
	}
	
	/**
	 * Returns true if the logs of this type (definition) is enabled. 
	 * The following equation holds.<br />
	 * {@link #isEnabled()} = !{@link #isDisabled()}
	 * @return true if the logs of this type (definition) is enabled
	 */
	public boolean isEnabled() {
		return logging;
	}
	
	/**
	 * Returns true if the logs of this type (definition) is disabled.
	 * The following equation holds.<br />
	 * {@link #isDisabled()} = !{@link #isEnabled()}
	 * @return true if the logs of this type (definition) is disabled
	 */
	public boolean isDisabled() {
		return (!logging);
	}
}
