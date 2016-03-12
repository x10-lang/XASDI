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

package com.ibm.xasdi_bridge;

import com.ibm.xasdi_bridge.simulator.World;

/**
 * ID of agent (Citizen)
 */
public class CitizenID {
	long id ;
	static int pid = World.world().getPlaceID();		//	X10 PlaceID
	static int nPlaces = World.getnPlaces() + 1; // The number of X10 Places
	static long lastcnt = 0;
	
	/**
	 * Constructor using long number.
	 * @param id ID of agent as long value
	 */
	public CitizenID (long id){ // TODO: unique ID among all X10 Places
		this.id = id ;
	}
	
	/**
	 * Constructor generates default id.
	 */
	public CitizenID (){ // TODO: unique ID among all X10 Places
		this.id = (lastcnt++) * nPlaces + pid;
	}
	
	/**
	 * get Citizen ID as long value 
	 * @return Citizen ID as long
	 */
	public long getLocalID(){
		return id ;
	}
	
	/**
	 * get X10 Place ID as int value 
	 * @return X10 Place ID as int
	 */
	public int getPlaceID(){
		return (int) (id % nPlaces);
	}
}
