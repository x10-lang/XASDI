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


public class AgentID {
	val pid:Long;			//	X10 Place ID
	val aid:Long;			//	Physical Agent ID

	public def this(pid:Long, aid:Long){
		this.pid = pid;
		this.aid = aid;
	}

	public def toString():String{
		return "(place ID, agentID)= (" + pid + "," + aid + ")";
	}
	
	public def getPID():Long{	
		return pid;
	}
	
	public def getAID():Long{	
		return aid;
	}
}

