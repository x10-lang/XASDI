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

import x10.util.*;

public class Agent {

	protected val aid:AgentID;
	protected val manager:AgentManager;

	public def this(aid:AgentID, manager:AgentManager) {
		this.aid = aid;
		this.manager = manager;
	}

	public def getAgentID():AgentID {
		return aid;
	}
	
	public def run(time:Long){
		Console.OUT.println("Agent.run");
		//	do nothing
	}
	
	public def getPlaceID():Long{
		return here.id;
	}

	public def toString():String{
		return "Agent, aid=" + aid;
	}
	
}

