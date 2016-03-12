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

package com.ibm.xasdi.runtime;

import x10.compiler.*;
import com.ibm.xasdi.util.*;

@NativeRep("java", "com.ibm.xasdi_bridge.simulator.LauncherProxy", null, null)
public class XLauncherProxy {
	
	@Native("java", "new com.ibm.xasdi_bridge.simulator.LauncherProxy(#prop, #pid)")
	public native def this(prop:Properties, pid:Int);
	
	@Native("java", "#this.prepare(#prop)")
	public final native def prepare(prop:Properties):void;
		
	@Native("java", "#this.complete()")
	public final native def complete():void;

	@Native("java", "#this.getDriver(#index)")
	public final native def getDriver(index:Int):XDriver;
	
	@Native("java", "#this.getCitizenProxy(#index)")
	public final native def getCitizenProxy(index:Int):XCitizenProxy;
	
	@Native("java", "#this.sendMsgQ(#msgq)")
	public final native def sendMsgQ(msgq:XMessageQueue):void;
	
	@Native("java", "#this.getMessageRepository()")
	public final native def getMessageRepository():XMessageRepository;
	
	@Native("java", "#this.restoreCitizen(#sm)")
	public final native def restoreCitizen(sm:XMessage):XCitizenProxy;
	
	@Native("java", "#this.getNumCitizens()")
	public final native def getNumCitizens():Int;
	
	@Native("java", "#this.getNumDrivers()")
	public final native def getNumDrivers():Int;
	
}
