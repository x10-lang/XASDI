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

package com.ibm.xasdi_bridge;

public interface UpdateableState extends State {
	/**
	 * Sets an int value to a column.
	 * @param i a column number
	 * @param value an int value
	 * @throws IllegalArgumentException
	 */
	void setInt(int i, int value) throws IllegalArgumentException;

	/**
	 * Sets a double value to a column.
	 * @param i a column number
	 * @param value a double value
	 * @throws IllegalArgumentException
	 */
	void setDouble(int i, double value) throws IllegalArgumentException;
	
	/**
	 * Sets a long value to a column.
	 * @param i a column number
	 * @param value a long value
	 * @throws IllegalArgumentException
	 */
	void setLong(int i, long value) throws IllegalArgumentException;
	
	/**
	 * Sets a short value to a column.
	 * @param i a column number
	 * @param value a short value
	 * @throws IllegalArgumentException
	 */
	void setShort(int i, short value) throws IllegalArgumentException;
	
	/**
	 * Sets a byte value to a column.
	 * @param i a column number
	 * @param value a byte value
	 * @throws IllegalArgumentException
	 */
	void setByte(int i, byte value) throws IllegalArgumentException;
	
	/**
	 * Sets a float value to a column.
	 * @param i a column number
	 * @param value a float value
	 * @throws IllegalArgumentException
	 */
	void setFloat(int i, float value) throws IllegalArgumentException;
	
	/**
	 * Sets a boolean value to a column.
	 * @param i a column number
	 * @param value a boolean value
	 * @throws IllegalArgumentException
	 */
	void setBoolean(int i, boolean value) throws IllegalArgumentException;
	
	/**
	 * Sets a Date value to a column.
	 * @param i a column number
	 * @param value a java.util.Date object
	 * @throws IllegalArgumentException
	 */
	void setDate(int i, java.util.Date value) throws IllegalArgumentException;
	
	/**
	 * Sets a String value to a column.
	 * @param i a column number
	 * @param value a String object
	 * @throws IllegalArgumentException
	 */
	void setString(int i, String value) throws IllegalArgumentException;
	
	/**
	 * Sets an Object to a column.
	 * @param i a column number
	 * @param value an Object
	 * @throws IllegalArgumentException
	 */
	void setObject(int i, Object value) throws IllegalArgumentException;
	
	/**
	 * Unset the value of a column.
	 * @param i a column number
	 * @throws IllegalArgumentException
	 */
	void remove(int i) throws IllegalArgumentException;
    
}

