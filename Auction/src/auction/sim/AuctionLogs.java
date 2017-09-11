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

import auction.agent.BidderProxy;
import auction.behavior.Bid;

public class AuctionLogs extends SimulationLogs {

	public AuctionLogs(long regionid, String fileName) {
		super(regionid, fileName);
		
		bidlog = new BidLog(regionid);
		logmap.put(bidlog.getFinfo(), bidlog);
	}
	
	/**
	 * Default Constructor for Converter
	 */
	public AuctionLogs() {
		bidlog = new BidLog();
		logmap.put(bidlog.getFinfo(), bidlog);
	}

	/**
	 * logging bid information
	 * @param bid <code>Bid</code> Object for logging
	 * @param time simulation time
	 */
	public void logBid(Bid bid,  long time) {
		bidlog.writelog(bid, time);
	}
}
