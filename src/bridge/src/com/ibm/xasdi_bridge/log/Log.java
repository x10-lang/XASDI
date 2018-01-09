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

package com.ibm.xasdi_bridge.log;

import java.io.IOException;

/**
 * A log record for storing a portion of simulation result.
 * @author yamamoto
 */
public interface Log {
	/**
	 * Gets the number of columns in this log record.
	 * @return the number of columns in this log record.
	 * @throws IllegalArgumentException
	 */
	int getNumberOfColumns() throws IllegalArgumentException;
    
	/**
	 * Gets the metadata (schema) of this log record.
	 * @return log definition (metadata) of this log record
	 */
	LogDefinition getLogDefinition();
    
	/**
	 * Gets the value of the i-th column as an "int" value.
	 * @param i column number (starting from 0)
	 * @return value of the i-th column
	 * @throws IllegalArgumentException
	 */
	int getInt(int i) throws IllegalArgumentException;

	/**
	 * Sets an "int" value to the i-th column.
	 * @param i column number (starting from 0)
	 * @param value an "int" value
	 * @throws IllegalArgumentException
	 */
	void setInt(int i, int value) throws IllegalArgumentException;

	/**
	 * Gets the value of the i-th column as a "double" value.
	 * @param i column number (starting from 0)
	 * @return value of the i-th column
	 * @throws IllegalArgumentException
	 */
	double getDouble(int i) throws IllegalArgumentException;

	/**
	 * Sets a "double" value to the i-th column.
	 * @param i column number (starting from 0)
	 * @param value a "double" value
	 * @throws IllegalArgumentException
	 */
	void setDouble(int i, double value) throws IllegalArgumentException;

	/**
	 * Gets the value of the i-th column as a "long" value.
	 * @param i column number (starting from 0)
	 * @return value of the i-th column
	 * @throws IllegalArgumentException
	 */
	long getLong(int i) throws IllegalArgumentException;

	/**
	 * Sets a "long" value to the i-th column.
	 * @param i column number (starting from 0)
	 * @param value a "long" value
	 * @throws IllegalArgumentException
	 */
	void setLong(int i, long value) throws IllegalArgumentException;

	/**
	 * Gets the value of the i-th column as a "short" value.
	 * @param i column number (starting from 0)
	 * @return value of the i-th column
	 * @throws IllegalArgumentException
	 */
	short getShort(int i) throws IllegalArgumentException;

	/**
	 * Sets a "short" value to the i-th column.
	 * @param i column number (starting from 0)
	 * @param value a "short" value
	 * @throws IllegalArgumentException
	 */
	void setShort(int i, short value) throws IllegalArgumentException;

	/**
	 * Gets the value of the i-th column as a "byte" value.
	 * @param i column number (starting from 0)
	 * @return value of the i-th column
	 * @throws IllegalArgumentException
	 */
	byte getByte(int i) throws IllegalArgumentException;

	/**
	 * Sets a "byte" value to the i-th column.
	 * @param i column number (starting from 0)
	 * @param value a "byte" value
	 * @throws IllegalArgumentException
	 */
	void setByte(int i, byte value) throws IllegalArgumentException;

	/**
	 * Gets the value of the i-th column as a "float" value.
	 * @param i column number (starting from 0)
	 * @return value of the i-th column
	 * @throws IllegalArgumentException
	 */
	float getFloat(int i) throws IllegalArgumentException;

	/**
	 * Sets a "float" value to the i-th column.
	 * @param i column number (starting from 0)
	 * @param value a "float" value
	 * @throws IllegalArgumentException
	 */
	void setFloat(int i, float value) throws IllegalArgumentException;

	/**
	 * Gets the value of the i-th column as a "boolean" value.
	 * @param i column number (starting from 0)
	 * @return value of the i-th column
	 * @throws IllegalArgumentException
	 */
	boolean getBoolean(int i) throws IllegalArgumentException;

	/**
	 * Sets a "boolean" value to the i-th column.
	 * @param i column number (starting from 0)
	 * @param value a "boolean" value
	 * @throws IllegalArgumentException
	 */
	void setBoolean(int i, boolean value) throws IllegalArgumentException;

	/**
	 * Gets the value of the i-th column as a "String" value.
	 * @param i column number (starting from 0)
	 * @return value of the i-th column
	 * @throws IllegalArgumentException
	 */
	String getString(int i) throws IllegalArgumentException;

	/**
	 * Sets a "String" value to the i-th column.
	 * @param i column number (starting from 0)
	 * @param value a "String" value
	 * @throws IllegalArgumentException
	 */
	void setString(int i, String value) throws IllegalArgumentException;

	/**
	 * Gets the value of the i-th column as an "Object" value.
	 * @param i column number (starting from 0)
	 * @return value of the i-th column
	 * @throws IllegalArgumentException
	 */
	Object getObject(int i) throws IllegalArgumentException;

	/**
	 * Write this log into an underling log system
	 * @throws IOException
	 */
	void write() throws IOException;

	void setLong(int i, String localID);

}
