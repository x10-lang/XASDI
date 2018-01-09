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

package com.ibm.xasdi_bridge.message;

import com.ibm.xasdi_bridge.*;

/**
 * Represents message request reply.
 * All agents received messages which implement this interface should reply.
 */
public interface RequestResponse {
	
	/**
	 * It represents that reply is necessary.
	 */
	public final boolean needReply = true;
	
	/**
	 * Sets ID of sender agent.
	 * @param cid Sender Citizen ID
	 */
	public void setSenderID(CitizenID cid);
	
	/**
	 * Gets ID of sender agent.
	 * @return Sender Citizen ID
	 */
	public CitizenID getSenderID();
	
	/**
	 * Sets ID of group to which sender agent belongs.
	 * @param pid Group ID which sender agent belongs
	 */
	public void setSenderPlace(PlaceID pid);
	
	/**
	 * Gets ID of group to which sender agent belongs.
	 * @return Group ID which sender agent belongs
	 */
	public PlaceID getSenderPlace();

}
