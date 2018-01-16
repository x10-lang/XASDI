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

import java.util.*;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.PlaceID;
import com.ibm.xasdi_bridge.State;
import com.ibm.xasdi_bridge.UpdateableState;
import com.ibm.xasdi_bridge.citizen.ShadowInfo;
import com.ibm.xasdi_bridge.citizen.ShadowList;
import com.ibm.xasdi_bridge.citizen.ShadowMap;
import com.ibm.xasdi_bridge.citizen.Shadowable;
import com.ibm.xasdi_bridge.rt.state.StateImpl;
import com.ibm.xasdi_bridge.rt.state.UpdateableStateImpl;

/**
 * Resource used by some Citizen agents.
 * It does not have Drive agents.
 */
public class Place implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * PlaceID.
	 */
	protected PlaceID id = null;
	
	/**
	 * Region object owns this Group object.
	 */
	protected Region region = null;
	
	/**
	 * Current step time seen from this object .
	 */
	protected long current;
	
	/**
	 * Current resource state. It can be updated.
	 */
	protected UpdateableStateImpl stateForUpdate = null;
	
	/**
	 * Current resource state. It cannot be updated.
	 */
	protected StateImpl state = null;
	
	/**
	 * IDs of Citizens participate in this object.
	 */
	protected Set<CitizenID> citizenSet;
	
	/**
	 * List of Citizen Proxies participate in this object.
	 */
	protected LinkedList<CitizenProxy> citizenList = new LinkedList<CitizenProxy>();
	
	/**
	 * List of ShadowInfos of Citizens at this object
	 */
	protected ShadowList shadowList;
	protected ShadowList originshadowList;
	
	/**
	 * Constructor should be used.
	 * @param region Region object owns this.
	 * @param id Region ID (Long).
	 */
	public Place(Region region, long id) {
		this.region = region;
		this.id = new PlaceID(id);
		state = region.createState();
		stateForUpdate = region.createUpdateableState(state);
	}

	/**
	 * Gets ID of this group.
	 * @return ID of this group.
	 */
	public PlaceID getID() {
		return id;
	}
	
	/**
	 * Gets ID set of Citizen agents in this group.
	 * @return Set of Citizen agent IDs
	 */
	public Set<CitizenID> getCitizenSet() {
		return citizenSet;
	}

	/**
	 * Sets ID set of Citizen agents in this group.
	 * @param citizenSet New set of Citizen agent IDs
	 */
	public void setCitizenSet(Set<CitizenID> citizenSet) {
		this.citizenSet = citizenSet;
	}
	
	/**
	 * Add CitizenProxy into this object.
	 * @param cp CitizenProxy that enter into this object.
	 */
	public void addCitizen(CitizenProxy cp) {
		citizenList.add(cp);
	}
	
	/**
	 * Remove CitizenProxy from this object.
	 * @param cp CitizenProxy that exit from this object.
	 */
	public void removeCitizen(CitizenProxy cp) {
		citizenList.remove(cp);
	}

	/**
	 * Gets the current state of this place.
	 * @return State of this place
	 */
	public State getState() {
		return state;
	}

	/**
	 * Gets the current updatable state of this place.
	 * @return UpdatableState of this place
	 */
	public UpdateableState getStateForUpdate() {
		return stateForUpdate;
	}

	/**
	 * Gets Region object bound to this group.
	 * @return Region object owns this group
	 */
	public Region getRegion() {
		return region;
	}
	
	/**
	 * Sets a simulation time.
	 * This method will be called just before {@link #begin(long)}
	 * @param time a simulation time
	 */
	void setTime(long time) {
		current = time;
	}
	
	/**
	 * Gets the current simulation time.
	 * @return current simulation time
	 */
	long getTime() {
		return current;
	}
	
	/**
	 * Commits the current updatable state of this place.
	 */
	void commitState() {
		stateForUpdate.commit();
	}
	
	/**
	 * Called when the simulation is started.
	 * This method will be called only once at the beginning
	 * of the simulation.
	 */
	public void prepare() {}

	/**
	 * Called when all places are finished to prepare.
	 * This method will be called only once just before the simulation
	 * loop begins.
	 * @see #prepare()
	 */
	public void begin() {}
	
	/**
	 * Called when the simulation is completed.
	 */
	public void complete() {}
	
	/**
	 * Called at the beginning of a simulation cycle
	 * for the specified simulation time.
	 * @param time a simulation cycle (time)
	 */
	public void begin(long time) {}
	
	/**
	 * Called when the simulation cycle for the specified 
	 * simulation time is completed.
	 * @param time a simulation time
	 */
	public void complete(long time) {}
	
	/**
	 * Called to prepare the ShadowList of Citizens at this object.
	 * @return ShadowList of Citizens at this object
	 */
	public ShadowList prepareShadowList() {
		originshadowList = new ShadowList();
		// prepare shadowlist by gathering ShadowInfo from the set of Shadowable Citizens at this Place
		// To be customized
		for (CitizenProxy cp : citizenList) {
			if (cp instanceof Shadowable) {
				ShadowInfo shadowinfo = ((Shadowable) cp).prepareShadowInfo();
				originshadowList.add(shadowinfo);
			}
		}
//		System.out.println("Place.prepareShadowList size = "+originshadowList.size() + " at place "+id.getLocalID());
		return originshadowList;
	}
	
	/**
	 * Called when the Region receive ShadowList from other X10 Places
	 * @param shadows ShadowList from other X10 Places
	 */
	synchronized public void addShadowList(ShadowList shadows){
//		System.out.println("Place.addShadowList "+shadows.toString() + " at place "+id.getLocalID());
		if (shadowList == null) shadowList = new ShadowList();
		shadowList.addAll(shadows);
	}
}
