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

package com.ibm.xasdi_bridge;

import com.ibm.xasdi_bridge.simulator.World;

/**
 * Represents ID of Citizen group.
 * It does not mean X10 Place ID.
 */
public class PlaceID implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	long id;
	int pid = World.world().getPlaceID();		//	X10 PlaceID
	int nPlaces = World.getnPlaces(); // The number of X10 Places
	
	/**
	 * Constructor must be used.
	 * @param id ID of Citizen group
	 */
	public PlaceID(long id) { // TODO: unique ID among all X10 Places
		this.id = id;
	}
	
	/**
	 * get Place ID as long value 
	 * @return Place ID as long
	 */
	public long getLocalID() {
		return id;
	}
}
