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

import x10.io.Deserializer;
import x10.io.Serializer;
import x10.util.HashMap;

public class AgentRepository extends HashMap[Long,Agent]{		//	PhysicalID, Agent
	
	public def serialize(s:Serializer) {
		super.serialize(s);
	}
	
	def this() { super(); }

	public def this(ds:Deserializer) {
		super(ds);
	}

	public def existAgent(p:Long):boolean { return super.containsKey(p); }
	public def getAgent(p:Long):Agent { return super.getOrThrow(p) as Agent; }
	
	public def removeAgent(p:Long):Boolean{		// CitizenAgent might be removed
		if(existAgent(p)){
			super.remove(p);
			return true;
		}else{
			return false;
		}
	}
	
}

