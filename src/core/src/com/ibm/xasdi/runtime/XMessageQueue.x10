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

package com.ibm.xasdi.runtime;

import x10.compiler.*;

@NativeRep("java", "com.ibm.xasdi_bridge.message.MessageQueue", null, null)
public class XMessageQueue {
	
	@Native("java", "#this.size()")
	public final native def size():Int;
	
	@Native("java", "#this.isEmpty()")
	public final native def isEmpty():Boolean;
	
	@Native("java", "#this.append(#msgq)")
	public final native def append(msgq:XMessageQueue):void;
	
	@Native("java", "#this.clear()")
	public final native def clear():void;
	
	@Native("java", "#this.getMessages(#aid)")
	public final native def getMessages(aid:Long):XMessageList;
	
	@Native("java", "#this.getPID()")
	public final native def getPID():Int;
	
}
