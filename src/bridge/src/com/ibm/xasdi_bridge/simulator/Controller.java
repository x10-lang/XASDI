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

import java.io.IOException;



/**
 * @author yamamoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
abstract public class Controller implements Runnable {
	World world;
	boolean done = false;

	void setWorld(World w) {
		world = w;
	}

	/**
	 * Get a world
	 * @return Handling World object
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Prepare a simulation environment
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public void prepare() throws IOException, IllegalStateException{
		try {
			done = false;
			world.prepare();
			synchronized(this) {
				if (!done) {
					wait();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	void prepared() {
		synchronized(this) {
			done = true;
			notify();
		}
	}

	/**
	 * Execute simulation
	 */
	public void execute() {
		try {
			done = false;
			world.execute();				
			synchronized(this) {
				if (!done) {
					wait();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	void executed() {
		synchronized(this) {
			done = true;
			notify();
		}
	}

	/**
	 * Shutdown a simulation environment
	 *
	 */
	public void shutdown() {
		world.shutdown();
	}

	/**
	 * If simulation should be stopped, return true. Otherwise, return false.
	 * @param region a region
	 * @param time simulation time 
	 * @return If simulation should be stopped, return true. Otherwise, return false.
	 */
	abstract public boolean stop(Region region, long time);

	void start() {
	}

}
