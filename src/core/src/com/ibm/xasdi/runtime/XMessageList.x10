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

package com.ibm.xasdi.runtime;

import x10.compiler.*;

@NativeRep("java", "com.ibm.xasdi_bridge.message.MessageList", null, null)
public class XMessageList {
	
	@Native("java", "new com.ibm.xasdi_bridge.MessageList(#typ)")
	public native def this(typ:Int);
	
	@Native("java", "#this.getType()")
	public final native def getType():Int;
	
	@Native("java", "#this.getCitizenID()")
	public final native def getCID():Long;
	
	@Native("java", "#this.size()")
	public final native def size():Int;
	
	@Native("java", "#this.remove(0)")
	public final native def removeFirst():XMessage;
	
	@Native("java", "#this.addAll(#list)")
	public final native def append(list:XMessageList):Boolean;
	
	@Native("java", "#this.isEmpty()")
	public final native def isEmpty():Boolean;
	
}
