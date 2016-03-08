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

import x10.util.*;

/**
 *	Agent class for CitizenProxy
 *	Note: CitizenAgent object may be removed
 */
public class CitizenAgent extends Agent{		//	one Agent, one CitizenProxy

	var citizen :XCitizenProxy;

	public def this(aid:AgentID, manager:AgentManager, citizen:XCitizenProxy){
		super(aid, manager);
		this.citizen = citizen; 
	}

	public def run(time:Long){
		//	do nothing
	}
	
	public def toString():String{
		return "Agent, aid=" + aid;
	}
	
	public def getCitizen():XCitizenProxy{		//	for message passing
		return citizen;
	}

}

