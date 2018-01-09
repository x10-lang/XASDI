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

public class Bid {
	public int bidtype;
	public int price;
	public BidderProxy bidder;

	public static final int NOBID = 0;
	public static final int BUY = 1;

	/** default constructor */
	public Bid() {
		this(NOBID, 0, null);
	}


	/** constructor with bid type, price and sender*/
	public Bid(int bidtype, int price, BidderProxy bidder) {
		this.bidtype = bidtype;
		this.price = price;
		this.bidder = bidder;
	}

}
