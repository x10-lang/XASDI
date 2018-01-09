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

@NativeRep("java", "com.ibm.xasdi_bridge.simulator.Driver", null, null)
public class XDriver {
	
	@Native("java", "#this.getID()")
	public final native def getID():Long;
	
	@Native("java", "#this.begin(#arg0)")
	public final native def begin(arg0:Long):void;
	
	@Native("java", "#this.complete(#arg0)")
	public final native def complete(arg0:Long):void;
	
	@Native("java", "#this.execute(#time)")
	public final native def execute(time:Long):void;
	
	@Native("java", "#this.execute(#time,#phase)")
	public final native def execute(time:Long, phase:Int):void;

	@Native("java", "#this.onMessage(#msgs)")
	public final native def onMessage(msgs:XMessageList):void;

}

