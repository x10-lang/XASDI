/*
 *  This file is part of the XASDI project (https://github.com/x10-lang/xasdi).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2016.
 */

package com.ibm.xasdi_bridge.rt.state;

/**
 * @author yamamoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
interface StateType {
	static final byte BOOLEAN = 1;
	static final byte BYTE = 2;
	static final byte DATE = 3;
	static final byte DOUBLE = 4;
	static final byte FLOAT = 5;
	static final byte INT = 6;
	static final byte LONG = 7;
	static final byte SHORT = 8;
	static final byte STRING = 9;
	static final byte OBJECT = 10;
	static final byte REMOVE = 11;
}
