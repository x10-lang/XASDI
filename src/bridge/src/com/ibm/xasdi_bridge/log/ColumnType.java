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

/**
 * Column type of a log record.
 * 
 * @author yamamoto
 */
public class ColumnType {
	/**
	 * Constant representing "boolean" column type 
	 */
	public static final ColumnType BOOLEAN = new ColumnType(1);

	/**
	 * Constant representing "byte" column type 
	 */
	public static final ColumnType BYTE = new ColumnType(2);;

	/**
	 * Constant representing "double" column type 
	 */
	public static final ColumnType DOUBLE = new ColumnType(3);;

	/**
	 * Constant representing "float" column type 
	 */
	public static final ColumnType FLOAT = new ColumnType(4);;

	/**
	 * Constant representing "int" column type 
	 */
	public static final ColumnType INT = new ColumnType(5);;

	/**
	 * Constant representing "long" column type 
	 */
	public static final ColumnType LONG = new ColumnType(6);;

	/**
	 * Constant representing "short" column type 
	 */
	public static final ColumnType SHORT = new ColumnType(7);;

	/**
	 * Constant representing "String" column type 
	 */
	public static final ColumnType STRING = new ColumnType(8);;

	/**
	 * Constant representing "Object" (i.e. any) column type 
	 */
	public static final ColumnType OBJECT = new ColumnType(9);;
	
	/**
	 * Get a ColumnType from an int value
	 * 1: ColumnType.BOOLEAN<br>
	 * 2: ColumnType.BYTE<br>
	 * 3: ColumnType.DOUBLE<br>
	 * 4: ColumnType.FLOAT<br>
	 * 5: ColumnType.INT<br>
	 * 6: ColumnType.LONG<br>
	 * 7: ColumnType.SHORT<br>
	 * 8: ColumnType.STRING<br>
	 * 9: ColumnType.OBJECT<br>
	 * @param t an int value<br>
	 * @return a ColumnType<br>
	 */
	public static ColumnType getFromInt(int t) {
		switch(t) {
		case 1: return BOOLEAN;
		case 2: return BYTE;
		case 3: return DOUBLE;
		case 4: return FLOAT;
		case 5: return INT;
		case 6: return LONG;
		case 7: return SHORT;
		case 8: return STRING;
		case 9: return OBJECT;
		}
		return null;
	}
	
	int type;
	
	ColumnType(int t) {
		type = t;
	}
	
	/**
	 * Get an int value of this ColumnType
	 * @return an int value
	 */
	public int intValue() {
		return type;
	}
	
	public String toString() {
		String t = "unknown";
		switch(type) {
		case 1: t = "BOOLEAN";break;
		case 2: t = "BYTE";break;
		case 3: t = "DOUBLE";break;
		case 4: t = "FLOAT";break;
		case 5: t = "INT";break;
		case 6: t = "LONG";break;
		case 7: t = "SHORT";break;
		case 8: t = "STRING";break;
		case 9: t = "OBJECT";break;
		}
		return "ColumnType:" + t + "(" + type + ")";
	}
}
