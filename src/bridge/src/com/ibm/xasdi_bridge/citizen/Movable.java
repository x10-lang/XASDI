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

package com.ibm.xasdi_bridge.citizen;

/**
 * Interface implemented by Objects that is migrated to another X10 Place.
 * Class implements it must have only primitive values.
 */
public interface Movable extends java.io.Serializable {
	
	static final long serialVersionUID = 1L;
		
	public Object getMovableFields();
	
	/**
	 * Get ID of driver that this object will go to.
	 * @return ID of next driver
	 */
	public long getNextDriverID();

	/**
	 * Get this object ID.
	 * @return ID of this object
	 */
	public long getObjectID();

}
