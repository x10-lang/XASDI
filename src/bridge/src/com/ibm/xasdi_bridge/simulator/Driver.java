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

package com.ibm.xasdi_bridge.simulator;

import com.ibm.xasdi_bridge.message.MessageList;

public interface Driver {
	
	/**
	 * Called when all drivers are finished to prepare.
	 * This method will be called only once just before the simulation
	 * loop begins.
	 * @see #prepare()
	 */
	public void begin();
	
	/**
	 * Called when the simulation is completed.
	 */
	public void complete();
	
	/**
	 * Called at the beginning of a simulation cycle
	 * for the specified simulation time.
	 * {@link #execute(long, int)} will be called 
	 * for each simulation phases
	 * @param time a simulation cycle (time)
	 */
	public abstract void begin(long time);

	/**
	 * Called when the simulation cycle for the specified 
	 * simulation time is completed.
	 * @param time a simulation time
	 */
	public abstract void complete(long time);

	/**
	 * Called when this object run and make agents run.
	 * @param time Simulation step
	 * @param phase Simulation phase at each step
	 */
	public abstract void execute(long time, int phase);
	
	/**
	 * Called when this object run and make agents run.
	 * @param time Simulation step
	 */
	public void execute(long time);

	/**
	 * Get ID of this Driver agent.
	 * @return ID of this agent
	 */
	public long getID();

	/**
	 * Called when parameters are set to this object.
	 */
	public abstract void prepare();

	/**
	 * Bind this object to Region.
	 * @param region Region possesses this Driver agent
	 */
	public void setRegion(Region region);
	
	/**
	 * Called when some messages received.
	 * @param ml List of messages to be received
	 */
	public void onMessage(MessageList ml);
	
}
