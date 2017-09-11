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

package auction.agent;

import com.ibm.xasdi_bridge.simulator.CitizenProxy;
import com.ibm.xasdi_bridge.simulator.CitizenProxyFactory;
import com.ibm.xasdi_bridge.simulator.Region;

import auction.behavior.BidStrategy;
import auction.behavior.RandBidStrategy;

public class BidderProxyFactory implements CitizenProxyFactory {

	@Override
	public CitizenProxy newInstance(Region region) {
		BidderProxy bidder = new BidderProxy(region);
		BidStrategy strategy = new RandBidStrategy(bidder);
		bidder.setStrategy(strategy);
		return bidder;
	}

}
