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

package com.ibm.xasdi.util;

import x10.compiler.Native;
import x10.compiler.NativeRep;

@NativeRep("java", "java.util.Properties", null, null)
public class Properties {
	@Native("java", "new java.util.Properties()")
	public native def this();

	@Native("java", "#this.setProperty(#key, #value)")
	public native def setProperty(key:String, value:String): void;
	
	@Native("java", "#this.getProperty(#key)")
	public native def getProperty(key:String): String;
	
	@Native("java", "#this.getProperty(#key, #value)")
	public native def getProperty(key:String, value:String): String;
	
	@Native("java", "#this.loadFromXML(#input)")
	public native def loadFromXML(input:FileInputStream): void;
	
	@Native("java", "java.lang.System.getProperty(#key)")
	public static native def getSystemProperty(key:String): String;
}
