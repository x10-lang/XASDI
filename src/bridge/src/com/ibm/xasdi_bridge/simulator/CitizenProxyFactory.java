/*
 *  This file is part of the XASDI project (http://x10-lang.org/xasdi/).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2016.
 */

package com.ibm.xasdi_bridge.simulator;

/**
 * Only class to create CitizenProxy objects. 
 */
public interface CitizenProxyFactory {
	
	/**
	 * Create CitizenProxy object and bind it to Region.
	 * @param region Region binding to created CitizenProxy.
	 * @return Created CitizenProxy object.
	 */
	public CitizenProxy newInstance(Region region);

}
