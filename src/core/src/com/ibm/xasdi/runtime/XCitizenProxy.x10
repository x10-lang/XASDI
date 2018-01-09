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

@NativeRep("java", "com.ibm.xasdi_bridge.simulator.CitizenProxy", null, null)
public class XCitizenProxy {
	
	@Native("java", "#this.getLocalID()")
	public final native def getID():Long;
		
	@Native("java", "#this.onMessages(#msgs)")
	public final native def onMessages(msgs:XMessageList):void;
	
}
