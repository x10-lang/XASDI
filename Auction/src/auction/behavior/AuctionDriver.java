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

import java.util.LinkedList;
import java.util.TreeMap;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.message.MessageList;
import com.ibm.xasdi_bridge.simulator.Driver;
import com.ibm.xasdi_bridge.simulator.Region;

import auction.agent.BidderProxy;
import auction.sim.AuctionHouse;
import auction.sim.AuctionLogs;
import auction.sim.Auctioneer;

public class AuctionDriver implements Driver {
	long driverid;
	AuctionHouse house;
	Auctioneer auctioneer;
	int numBidder;
	AuctionLogs log;

	public AuctionDriver(long driverid, AuctionHouse house, Auctioneer auctioneer) {
		this.driverid = driverid;
		this.house = house;
		this.auctioneer = auctioneer;
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void begin(long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void complete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void complete(long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(long time, int phase) {
 		switch (phase) {
		case 0: 
			if (time == 0) createBidder(time);
			else updateBidder(time);
			break;
		case 1: updateAuction(time);
			break;
		}

	}
	
	private void createBidder(long time) {
		for (int i = 0; i < numBidder; i++) {
			BidderProxy bidder = (BidderProxy) auctioneer.createCitizen(Auctioneer.FACTORYID, new CitizenID(), null);
			house.addBidder(bidder);
		}
		
	}
	
	private void updateBidder(long time) {
		LinkedList<BidderProxy> bidders = house.getBidders();
		for (BidderProxy bidder: bidders) {
			bidder.update(time);
		}
	}


	private void updateAuction(long time) {
		LinkedList<BidderProxy> bidders = house.getBidders();
		int price = house.getPrice();
		TreeMap<Integer, Bid> bidmap = new TreeMap<Integer, Bid>();
		for (BidderProxy bidder: bidders) {
			Bid bid = bidder.getBid(time);
			if (bid != null && bid.bidtype != Bid.NOBID) {
				int bprice = bid.price;
				if (bprice > price) bidmap.put(bid.price, bid);
				log.logBid(bid, time);
			}
		}
		house.updateAuction(time, bidmap);
			
		int newprice = house.getPrice();
		BidderProxy winner = house.getWinner();
		long winnerid = -1;
		if (winner != null) winnerid = winner.getLocalID();
		System.out.println("House "+house.getID().getLocalID()+", time "+time+", price "+newprice+", winner "+winnerid);
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return driverid;
	}

	@Override
	public void onMessage(MessageList arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void prepare() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRegion(Region auctioneer) {
		this.auctioneer = (Auctioneer) auctioneer;

	}

	public int getNumBidder() {
		return numBidder;
	}

	public void setNumBidder(int numBidder) {
		this.numBidder = numBidder;
	}

	public void setLog(AuctionLogs log) {
		this.log = log;
	}

}
