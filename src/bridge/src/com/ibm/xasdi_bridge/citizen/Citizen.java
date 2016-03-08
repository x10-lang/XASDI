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

package com.ibm.xasdi_bridge.citizen;

import com.ibm.xasdi_bridge.Aggregator;
import com.ibm.xasdi_bridge.AsyncReply;
import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.rt.state.StateImpl;
import com.ibm.xasdi_bridge.rt.state.UpdateableStateImpl;

/**
 * Unit of simulation agent.
 * It behaves according to messages.
 */
public abstract class Citizen { 
	
	protected CitizenID cid = null;
	protected long time = -1;
	boolean updated = false;
	UpdateableStateImpl stateForUpdate = null;
	StateImpl state = null;

	
	/**
	 * Constructor with CitizenID.
	 * @param cid Citizen ID
	 */
	public Citizen(CitizenID cid){
		this.cid = cid;
		this.state = new StateImpl();
		this.stateForUpdate = new UpdateableStateImpl(state);
	}

	/**
	 * @deprecated CitizenID cannot be bound to this agent. Use constructor with CitizenID.
	 * Constructor without CitizenID.
	 */
	public Citizen() {
		this.state = new StateImpl();
		this.stateForUpdate = new UpdateableStateImpl(state);
	}
	
	/**
	 * Called when this agent executes.
	 * @param time Simulation step
	 * @param phase Simulation phase at each step
	 */
	public void onExecute(long time, int phase){};
	
	/**
	 * Gets Citizen ID bound to this agent.
	 * @return ID (as object) of this agent
	 */
	public CitizenID getID(){
		return cid;
	}
	
	/**
	 * Gets Citizen ID bound to this agent.
	 * @return ID (as long type) of this agent
	 */
	public long getLongID(){
		return cid.getLocalID();
	}
		
	/**
	 * Gets the current simulation time for this Citizen.
	 * @return the current simulation time for this Citizen
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Gets reply message.
	 * @return null (dummy method)
	 */
	public AsyncReply getAsyncReply(){
		return null;
	}
	
	/**
	 * Sends message to its family.
	 * @param msg Message to be sent
	 * @param ag Aggregator
	 */
	public void postToFamily(Message msg, Aggregator ag){
	}
	
	/**
	 * Gets its family (Group of agents)
	 * @return Group of agents
	 */
	public Family getFamily(){
		return null;
	}
	
	/**
	 * Called when this citizen is created.
	 * @param arg parameter object for the creation
	 */
	public abstract void onCreation(Object arg);

	/**
	 * Called when this citizen is disposed of.
	 */
	public abstract void onDisposing();

	/**
	 * Called after this citizen is loaded from a storage
	 */
	public abstract void onActivation();

	/**
	 * Called befor this citizen is stored into a storage
	 */
	public abstract void onDeactivating();

	/**
	 * Called when a message is delivered to this Citizen. 
	 * @param msg a Message
	 * @return a response message, or null if the message was one-way 
	 */
	public abstract Message onMessage(Message msg);

	
}
