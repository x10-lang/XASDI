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

import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.simulator.CitizenProxy;
import com.ibm.xasdi_bridge.simulator.Region;

import auction.behavior.Bid;
import auction.behavior.BidStrategy;
import auction.behavior.RandBidStrategy;
import auction.sim.AuctionHouse;

public class BidderProxy extends CitizenProxy {
	
	AuctionHouse house;
	BidStrategy strategy;

	public BidderProxy(Region region) {
		super(region);
		
	}

	@Override
	public void receiveMessage(Message arg0) {
		// TODO Auto-generated method stub

	}

	public AuctionHouse getHouse() {
		return house;
	}

	public void setHouse(AuctionHouse house) {
		this.house = house;
	}

	public void update(long time) {
//		System.out.println("Bidder update time="+time+" cid="+id.getLocalID());		
	}
	
	public Bid getBid(long time) {
		Bid bid = strategy.getBid(time);
		return bid;
	}

	public BidStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(BidStrategy strategy) {
		this.strategy = strategy;
	}

}
