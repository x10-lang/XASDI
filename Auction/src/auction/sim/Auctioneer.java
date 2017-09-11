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

package auction.sim;

import com.ibm.xasdi_bridge.citizen.CitizenFactory;
import com.ibm.xasdi_bridge.message.MessageQueue;
import com.ibm.xasdi_bridge.simulator.Region;


public class Auctioneer extends Region {
	
	public static final Integer FACTORYID = new Integer(0);

	@Override
	protected CitizenFactory getCitizenFactory() {
		return super.getCitizenFactory(Auctioneer.FACTORYID);
	}

	@Override
	public void sendMsgQ(MessageQueue arg0) {
		// do nothing
	}
	


}
