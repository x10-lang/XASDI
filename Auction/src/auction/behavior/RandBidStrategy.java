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

package auction.behavior;

import auction.agent.BidderProxy;
import auction.sim.AuctionHouse;

public class RandBidStrategy extends BidStrategy {
	double sigma = 100;

	public RandBidStrategy(BidderProxy bidder) {
		super("Rand", bidder);
	}

	@Override
	public Bid getBid(long time) {
		AuctionHouse house = bidder.getHouse();
		int price = house.getPrice();
		BidderProxy winner = house.getWinner();
		if (winner != bidder) {
			int bidprice = (int) (price + sigma * house.getRandom().nextGaussian());
			if (bidprice > price) {
				Bid bid = new Bid(Bid.BUY, bidprice, bidder);
				return bid;
			}
		}
		
		return null;
	}

}
