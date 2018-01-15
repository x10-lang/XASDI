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

import java.util.*;

/**
 *	Map of Shadow List for Place.
 *	It is used when shadow agents are sent to other X10 Places.
 */
@SuppressWarnings("serial")
public class ShadowMap extends TreeMap<Long, ShadowList> implements java.io.Serializable{

	/**
	 * Constructor using TreeMap's one
	 */
	public ShadowMap(){
		super();
	}
}
