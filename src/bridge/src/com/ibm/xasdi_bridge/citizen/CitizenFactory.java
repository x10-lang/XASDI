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

package com.ibm.xasdi_bridge.citizen;

import com.ibm.xasdi_bridge.CitizenID;

/**
 * The only class to create Citizen object.
 */
public interface CitizenFactory {

	/**
	 * Create Citizen object with ID.
	 * @param id ID bound to created Citizen
	 * @return Created Citizen object
	 */
	public Citizen newInstance(CitizenID id);
}
