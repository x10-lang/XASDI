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

import java.util.Properties;

import com.ibm.xasdi_bridge.message.MessageRepository;

/**
 * Interface for a simulation launcher that is to be registered
 * as a startup hook of the runtime configuration. 
 * If Caribbean is used as the runtime for the simulation, this class
 * should be specified to the class name of a startup service entry.
 *  
 * @author yamamoto
 */
public interface Launcher {
	/**
	 * Initialize the system using the specified properties.
	 * In most cases, an initialization will need to perform the following things.
	 * <ol>
	 * <li>Define the log formats
	 * <li>Create regions ant add them to the World object. To create a region,
	 *  <ol>
	 *   <li>Add citizen proxy factories to the region (see {@link com.ibm.xasdi_bridge.simulator.CitizenProxyFactory})
	 *   <li>Create places and simulation drivers and add them to the region
	 *   (see {@link com.ibm.xasdi_bridge.simulator.Place}, and {@link com.ibm.xasdi_bridge.simulator.Driver}) 
	 *  </ol>
	 * <li>Initialize environments (citizen containers). To initialize an environment,
	 *  <ol>
	 *   <li>Add citizen factories to the environment
	 *       (see {@link com.ibm.xasdi_bridge.citizen.Environment#addCitizenFactory})
	 *   <li>Set message resolvers if needed
	 *       (see {@link com.ibm.xasdi_bridge.citizen.Environment#setMessageResolver})
	 *   <li>Set environement listeners if needed
	 *       (see {@link com.ibm.xasdi_bridge.citizen.Environment#setEnvironmentListener})
	 *  </ol>
	 * <li>Set the initial simulation time (see {@link com.ibm.xasdi_bridge.simulator.World#setSimulationTime})
	 * </ol>
	 * 
	 * @param prop properties specified in the runtime configuration
	 */
	void prepare(Properties prop);
	
	/**
	 * Called when the simulation should be started.
	 * In most cases, this method can be implemented as below.
	 * <br /><br />
	 * <code>
	 * try {<br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;World.world().start();<br />
	 * } catch(Exception e) {<br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;e.printStackTrace();<br />
	 * }
	 * </code>
	 * 
	 * @param prop properties specified in the runtime configuration
	 * @see com.ibm.xasdi_bridge.simulator.World#start
	 */
	void start(Properties prop);
	
	/**
	 * Get Region object binding this interface.
	 * @return Created main Region object.
	 */
	Region getRegion();
	
	/**
	 * Get message repository binding this interface.
	 * @return Created MessageRepository object.
	 */
	MessageRepository getMessageRepository();
}

