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

package com.ibm.xasdi_bridge.simulator;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.citizen.Citizen;
import com.ibm.xasdi_bridge.message.*;

/**
 * Part of Citizen agent. It mainly sends and receives messages.
 */
public abstract class CitizenProxy {
	
	protected Region region = null;
	protected MessageRepository repository = null;
	protected CitizenID id;
	protected Citizen citizen;
	
	/**
	 * Constructor with Region to be bound.
	 * @param region Region bound to CitizenProxy.
	 */
	public CitizenProxy(Region region) {
		this.region = region;
		this.repository = region.getMessageRepository();
	}
	
	/**
	 * Set values to CitizenProxy.
	 * @param cid ID of Citizen agent
	 */
	public void prepareProxy(CitizenID cid){ 
		this.id = cid;
		this.citizen = region.getCitizenFactory().newInstance(cid);
	}
	
	/**
	 * send message to MessageResolver
	 * @param msg
	 * @return the same Message object if any (if destination is another X10 Place, return the same Message)
	 */
	public Message sendMessage(Message msg){
		return repository.addMessage(msg);
	}
	
	/**
	 * Called when receiving list of messages.
	 * @param msgs List of messages
	 */
	public void onMessages(MessageList msgs){
		if(msgs == null){
			System.out.println("MessageList is null");
			return;
		}
		for(Message msg : msgs){
			receiveMessage(msg);
		}
	}
	
	/**
	 * Called when receiving a message.
	 * @param msg Message to be received
	 */
	public abstract void receiveMessage(Message msg);
	
	/**
	 * Get ID(Object) of Citizen agent.
	 * @return Citizen ID as object
	 */
	public CitizenID getID(){
		return id;
	}
	
	/**
	 * Get ID(long) of Citizen agent.
	 * @return Citizen ID as long type
	 */
	public long getLocalID(){
		return id.getLocalID();
	}
	
	/**
	 * Get ID(long) of Citizen agent.
	 * @return Citizen ID as long type
	 */
	public long getObjectID() {				//	for Movable at SystemMessage
		return getLocalID();
	}
	
	/**
	 * Get whether this citizen agent should migrate to other X10 Place ID
	 * @param dest Next Driver ID to move
	 * @return If this agent has to migrate, return true.
	 */
	public boolean shouldBeSent(long dest){
		return !World.world().containsDriver(dest);
	}
	
	/**
	 * Called when this agent is activated
	 */
	public void onActivation() {}
	
	/**
	 * Called when this agent is not activated
	 */
	public void onDeactivation() {}
}

