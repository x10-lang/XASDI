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

/**
 * A data record which keeps a status of
 * a {@link com.ibm.xasdi_bridge.citizen.Citizen} or 
 * a {@link com.ibm.xasdi_bridge.simulator.Place}.
 * @author yamamoto
 */
public interface State {
	/**
	 * Gets the number of columns stored in this object.
	 * @return the number of columns
	 * @throws IllegalArgumentException
	 */
	int getNumberOfColumns() throws IllegalArgumentException;
	
	/**
	 * Gets the value of a column as an int value.
	 * @param i a column number
	 * @return the value of the i-th column
	 * @throws IllegalArgumentException when the column number is invalid
	 */
	int getInt(int i) throws IllegalArgumentException;
	
	/**
	 * Gets the value of a column as a double value.
	 * @param i a column number
	 * @return the value of the i-th column
	 * @throws IllegalArgumentException when the column number is invalid
	 */
	double getDouble(int i) throws IllegalArgumentException;

	/**
	 * Gets the value of a column as a long value.
	 * @param i a column number
	 * @return the value of the i-th column
	 * @throws IllegalArgumentException when the column number is invalid
	 */
	long getLong(int i) throws IllegalArgumentException;

	/**
	 * Gets the value of a column as a short value.
	 * @param i a column number
	 * @return the value of the i-th column
	 * @throws IllegalArgumentException when the column number is invalid
	 */
	short getShort(int i) throws IllegalArgumentException;

	/**
	 * Gets the value of a column as a byte value.
	 * @param i a column number
	 * @return the value of the i-th column
	 * @throws IllegalArgumentException when the column number is invalid
	 */
	byte getByte(int i) throws IllegalArgumentException;

	/**
	 * Gets the value of a column as a float value.
	 * @param i a column number
	 * @return the value of the i-th column
	 * @throws IllegalArgumentException when the column number is invalid
	 */
	float getFloat(int i) throws IllegalArgumentException;

	/**
	 * Gets the value of a column as a boolean value.
	 * @param i a column number
	 * @return the value of the i-th column
	 * @throws IllegalArgumentException when the column number is invalid
	 */
	boolean getBoolean(int i) throws IllegalArgumentException;

	/**
	 * Gets the value of a column as a java.util.Date object.
	 * @param i a column number
	 * @return the value of the i-th column
	 * @throws IllegalArgumentException when the column number is invalid
	 */
	java.util.Date getDate(int i) throws IllegalArgumentException;
	
	/**
	 * Gets the value of a column as a String object.
	 * @param i a column number
	 * @return the value of the i-th column
	 * @throws IllegalArgumentException when the column number is invalid
	 */
	String getString(int i) throws IllegalArgumentException;
	
	/**
	 * Gets the value of a column as an object.
	 * @param i a column number
	 * @return the value of the i-th column
	 * @throws IllegalArgumentException when the column number is invalid
	 */
	Object getObject(int i) throws IllegalArgumentException;
	
	/**
	 * Check if the type of a column is int.
	 * @param i a column number
	 * @return true if the type is int
	 */
	boolean isInt(int i);

	/**
	 * Check if the type of a column is double.
	 * @param i a column number
	 * @return true if the type is double
	 */
	boolean isDouble(int i);

	/**
	 * Check if the type of a column is long.
	 * @param i a column number
	 * @return true if the type is long
	 */
	boolean isLong(int i);

	/**
	 * Check if the type of the a is short.
	 * @param i a column number
	 * @return true if the type is short
	 */
	boolean isShort(int i);

	/**
	 * Check if the type of a column is byte.
	 * @param i a column number
	 * @return true if the type is byte
	 */
	boolean isByte(int i);

	/**
	 * Check if the type of a column is float.
	 * @param i a column number
	 * @return true if the type is float
	 */
	boolean isFloat(int i);

	/**
	 * Check if the type of a i-th column is boolean.
	 * @param i a column number
	 * @return true if the type is boolean
	 */
	boolean isBoolean(int i);

	/**
	 * Check if the type of a i-th column is java.util.Date.
	 * @param i a column number
	 * @return true if the type is java.util.Date
	 */
	boolean isDate(int i);

	/**
	 * Check if the type of a column is String.
	 * @param i a column number
	 * @return true if the type is String
	 */
	boolean isString(int i);

	/**
	 * Check if the type of a column is Object.
	 * @param i a column number
	 * @return true if the type is Object
	 */
	boolean isObject(int i);
}
