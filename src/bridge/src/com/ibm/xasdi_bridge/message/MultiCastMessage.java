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

package com.ibm.xasdi_bridge.message;

import com.ibm.xasdi_bridge.*;

import java.util.*;

/**
 * Message sent to several groups of agents.
 * If you want to send message for all agents, use BroadCastMessage.
 */
@SuppressWarnings("serial")
public abstract class MultiCastMessage extends Message {

	Set<PlaceID> places = new HashSet<PlaceID>();
	Set<CitizenID> citizens = new HashSet<CitizenID>();
	
	/**
	 * Add group of Citizens receive message.
	 * @param p Group ID of Citizens
	 */
	public void addPlace(PlaceID p){
		places.add(p);
	}
	
	/**
	 * Add a Citizen receiving message.
	 * @param c ID of Citizen to be receive message
	 */
	public void addCitizens(CitizenID c){
		citizens.add(c);
	}
}
