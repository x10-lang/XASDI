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

package com.ibm.xasdi_bridge.citizen;

import com.ibm.xasdi_bridge.CitizenID;

import java.util.*;

/**
 * Represents set of Citizen agents.
 * This class has only IDs of Citizens.
 */
public class CitizenSet {

	long id;
	private Set<CitizenID> citizenIDSet = new HashSet<CitizenID>();
	
	/**
	 * Constructor with ID of this object.
	 * @param id ID of set
	 */
	public CitizenSet(long id) {
		this.id = id;
		this.citizenIDSet = new HashSet<CitizenID>();
	}
	
	/**
	 * Adds Citizen to this set.
	 * @param cid ID(object) of Citizen to be added
	 */
	public void add(CitizenID cid) {
		synchronized(citizenIDSet) {
			citizenIDSet.add(cid);
		}
	}

	/**
	 * Gets IDs of registered Citizen as Collection.
	 * @return ID Collection
	 */
	public Collection<CitizenID> getCollection(){
		return citizenIDSet;
	}

	/**
	 * Removes agent from set.
	 * @param cid ID of Citizen to be removed
	 */
	public void remove(CitizenID cid) {
		synchronized(citizenIDSet) {
			citizenIDSet.remove(cid);
		}
	}
	
	/**
	 * Gets ID of this set.
	 * @return ID of this object
	 */
	public long getID() {
		return id;
	}
	
	/**
	 * Gets the number of Citizen agents registered
	 * @return The number of Citizen agents it has
	 */
	public int size(){
		return citizenIDSet.size();
	}
	
}


