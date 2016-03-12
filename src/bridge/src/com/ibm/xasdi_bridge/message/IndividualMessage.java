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

package com.ibm.xasdi_bridge.message;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;

/**
 * Message sent to one agent.
 */
@SuppressWarnings("serial")
public abstract class IndividualMessage extends Message {

	CitizenID dest;
	
	/**
	 * Set ID of destination agent
	 * @param dest Destination agent ID
	 */
	public void setDest(CitizenID dest){
		this.dest = dest;
	}
	
	/**
	 * Get ID(Object) of destination agent
	 * @return Destination agent ID as object
	 */
	public CitizenID getDest(){
		return dest;
	}
	
	/**
	 * Get ID(long) of destination agent
	 * @return Destination agent ID as long type
	 */
	public long getDestID(){
		return dest.getLocalID();
	}
}
