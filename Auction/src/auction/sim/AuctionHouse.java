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

package auction.sim;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import com.ibm.xasdi_bridge.simulator.Place;
import com.ibm.xasdi_bridge.simulator.Region;

import auction.agent.BidderProxy;
import auction.behavior.Bid;

public class AuctionHouse extends Place {
	public static final int INITPRICE = 1000;
	Random random;
	
	LinkedList<BidderProxy> bidders = new LinkedList<BidderProxy>();
	int price = INITPRICE; // second highest price
	int topprice = INITPRICE; // highest price
	BidderProxy winner = null; // current winner with highest price

	public AuctionHouse(Region region, long id) {
		super(region, id);
		// TODO Auto-generated constructor stub
	}
	
	public void addBidder(BidderProxy bidder) {
		bidders.add(bidder);
		bidder.setHouse(this);
	}

	public LinkedList<BidderProxy> getBidders() {
		return bidders;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getTopprice() {
		return topprice;
	}

	public void setTopprice(int topprice) {
		this.topprice = topprice;
	}

	public BidderProxy getWinner() {
		return winner;
	}

	public void setWinner(BidderProxy winner) {
		this.winner = winner;
	}

	public void updateAuction(long time, TreeMap<Integer, Bid> bidmap) {
		if (bidmap != null && !bidmap.isEmpty()) {
			Entry<Integer, Bid> ent = bidmap.pollLastEntry();
			int bidprice1 = ent.getKey();
			Bid bid1 = ent.getValue();
			if (bidprice1 > topprice) {
				price = topprice;
				winner = bid1.bidder; // current winner is changed
				topprice = bidprice1;
				
				// auction price is the second largest price
				if (!bidmap.isEmpty()) {
					int bidprice2 = bidmap.lastKey();
					if (bidprice2 > price) {
						price = bidprice2;
					}
				}
			}
			else if (bidprice1 > price) {
				price = bidprice1;
			}
		}
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

}
