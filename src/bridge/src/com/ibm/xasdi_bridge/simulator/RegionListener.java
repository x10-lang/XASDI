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

package com.ibm.xasdi_bridge.simulator;

/**
 * A RegionListner object is called when state of a region is changed.
 * @author yamamoto
 *
 */
public interface RegionListener {
	/**
	 * Called when a region is prepared.
	 * @param regionID
	 */
	void prepare(long regionID);
	
	/**
	 * Called when a region is prepared at each simulation cycle.
	 * @param regionID
	 * @param time
	 */
	void prepare(long regionID, long time);
	
	/**
	 * Called when a simulation process starts.
	 * @param regionID
	 */
	void begin(long regionID);
	
	/**
	 * Called when a simulation process at each simulation cycle starts.
	 * @param regionID
	 * @param time
	 */
	void begin(long regionID,long time);

	/**
	 * Called when a simulation process at each phase at each simulation cycle starts.
	 * @param regionID
	 * @param time
	 * @param phase
	 */
	void begin(long regionID,long time, int phase);

	/**
	 * Called when a simulation process at each phase at each simulation cycle completes.
	 * @param regionID
	 * @param time
	 * @param phase
	 */
	void complete(long regionID,long time, int phase);

	/**
	 * Called when a simulation process at each simulation cycle completes.
	 * @param regionID
	 * @param time
	 */
	void complete(long regionID,long time);
	
	/**
	 * Called when a simulation completes.
	 * @param regionID
	 */
	void complete(long regionID);
}
