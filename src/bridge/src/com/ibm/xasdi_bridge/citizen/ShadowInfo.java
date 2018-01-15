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

import java.io.Serializable;

/**
 * Stores information for Shadow Agent.
 */
public interface ShadowInfo extends Serializable {
	
	public Object getInfo();

	/**
	 * Get this agent ID.
	 * @return ID of this agent
	 */
	public long getAgentID();
	
	/**
	 * Get current place ID.
	 * @return ID of current place
	 */
	public long getPlaceID();

}
