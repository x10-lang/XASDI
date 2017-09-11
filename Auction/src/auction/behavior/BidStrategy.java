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

package auction.behavior;

import auction.behavior.Bid;
import auction.agent.BidderProxy;

public abstract class BidStrategy {
	protected String name;
	protected BidderProxy bidder;
	
	public BidStrategy(String name, BidderProxy bidder) {
		this.name = name;
		this.bidder = bidder;
	}
	
	public BidStrategy(BidderProxy bidder) {
		this("BidStrategy", bidder);
	}
	
	
	public String getName()
	{
		return name;
	}

	public abstract Bid getBid(long time);
}
