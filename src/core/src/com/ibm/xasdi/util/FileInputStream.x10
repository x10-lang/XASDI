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

package com.ibm.xasdi.util;

import x10.compiler.Native;
import x10.compiler.NativeRep;

@NativeRep("java", "java.io.FileInputStream", null, null)
public class FileInputStream {
	
	@Native("java", "new java.io.FileInputStream(#name)")
	public native def this(name:String);
}
