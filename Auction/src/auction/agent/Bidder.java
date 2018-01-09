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

package auction.agent;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.citizen.Citizen;

public class Bidder extends Citizen {
	
	public Bidder(CitizenID cid) {
		super(cid);
	}

	@Override
	public void onActivation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreation(Object arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeactivating() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisposing() {
		// TODO Auto-generated method stub

	}

	@Override
	public Message onMessage(Message arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
