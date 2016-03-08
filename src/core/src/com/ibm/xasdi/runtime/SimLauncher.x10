/*
 *  This file is part of the XASDI project (https://github.com/x10-lang/XASDI).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2016.
 */

package com.ibm.xasdi.runtime;

import com.ibm.xasdi.util.Properties;
import x10.compiler.*;

@NativeRep("java", "com.ibm.xasdi_bridge.simulator.Launcher", null, null)
public class SimLauncher {
	
	@Native("java", "new com.ibm.xasdi_bridge.simulator.Launcher(#pid)")
	public native def this(pid:Int);

	@Native("java", "#this.prepare(#prop)")
	public native def prepare(prop:Properties):void;
	
	@Native("java", "#this.prepare(#prop)")
	public native def start(prop:Properties):void;
	
	@Native("java", "#this.complete(#id)")
	public native def complete(id:Long):void;
	
	@Native("java", "#this.getCitizenProxy(#index)")
	public native def getCitizen(index:Long):XCitizenProxy;

}

