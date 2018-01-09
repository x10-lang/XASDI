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

import x10.util.*;

/**
 * 	Agent class for Driver
 * 	Note: Driver object is constant (not removed or added dinamically)
 */
public class DriverAgent extends Agent{		//	one Agent, one Driver

	var driver :XDriver;
	
	public def this(aid:AgentID, manager:AgentManager, driver:XDriver){
		super(aid, manager);
		this.driver = driver; 
	}
	
	public def run(time:Long){		//	call Driver.execute directly
		driver.execute(time);
	}
	
	public def run(time:Long, phase:Int){		//	call Driver.execute directly
		driver.execute(time, phase);
	}

	public def toString():String{
		return "Agent, aid=" + aid;
	}
	
	public def getDriver():XDriver{		//	for message passing
		return driver;
	}

}
