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

public class Message {
	
	val srcAID:AgentID;
	val destAID:AgentID;
	val msgBody:String;
	val msgType:Int;
	
	public static val UNICAST_ONEWAY   = 0;
	public static val UNICAST_TWOWAY   = 1;
	public static val MULTICAST_ONEWAY = 2;
	public static val MULTICAST_TWOWAY = 3;
	
	def this(srcAID:AgentID, destAID:AgentID, msgType:Int, msgBody:String) {
		this.srcAID  = srcAID;
		this.destAID = destAID;
		this.msgType = msgType;
		this.msgBody = msgBody;
	}
	
	public def toString():String {
		return srcAID + "," + destAID + "," + msgBody;
	}
	
	public def getType():Int {
		return msgType;
	}
	
	public def getSrcPlaceID():Long {
		return this.srcAID.pid;
	}
	
	public def getPlaceID():Long {
		return this.destAID.pid;
	}
	public def getDestAgentIDAsInt():Int {
		return destAID.aid as Int;
	}
	public def getSrcAgentID():AgentID{
		return srcAID;
	}
	public def getSrcAgentIDAsInt():Int {
		return srcAID.aid as Int;
	}
	public def getDestAgentID():AgentID{
		return destAID;
	}

}

