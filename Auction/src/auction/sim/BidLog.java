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

import com.ibm.xasdi_bridge.log.ColumnType;
import com.ibm.xasdi_bridge.log.Log;
import com.ibm.xasdi_bridge.log.LogDefinition;
import com.ibm.xasdi_bridge.simulator.World;

import auction.agent.BidderProxy;
import auction.behavior.Bid;

public class BidLog extends LogPack {
	
	public static final int BID_INFO = 0x01; // 0000 0000 0000 0001
	public static final String SUFFIX_BI = ".bi.csv";
	
	
	public BidLog(long regionid) {
		super(BID_INFO, SUFFIX_BI, regionid);
		deflog();
	}
	
	/**
	 * Constructor for Converter
	 */
	public BidLog() {
		super(BID_INFO, SUFFIX_BI);
	}

	@Override
	public void deflog() {
		LogDefinition def = prepareDefLog();
		def.addColumn(ColumnType.LONG); // time
		def.addColumn(ColumnType.LONG); // Region ID
		def.addColumn(ColumnType.LONG); // Bidder ID
		def.addColumn(ColumnType.LONG); // House ID
		def.addColumn(ColumnType.INT); // Bid price
		addDefLog();

	}

	@Override
	public void convert(Log rec) {
		long time = rec.getLong(0);
		long regionid = rec.getLong(1);
		long bidderid = rec.getLong(2);
		long houseid = rec.getLong(3);
		int price = rec.getInt(4);
		
		StringBuilder buff = new StringBuilder();
		buff.append("BI,");
		buff.append(time).append(",");
		buff.append(regionid).append(",");
		buff.append(bidderid).append(",");
		buff.append(houseid).append(",");
		buff.append(price);
		
		writeString(buff);

	}
	
	public void writelog(Bid bid,  long time) {
		Log rec = null;
		try {
			if (!useLog) {
				return;
			}
			
			BidderProxy bidder = bid.bidder;
			AuctionHouse house = bidder.getHouse();
			
			rec = World.world().getLogger().getFreeLog(BID_INFO);
			rec.setLong(0,time);
			rec.setLong(1, regionid);
			rec.setLong(2, bidder.getLocalID());
			rec.setLong(3, house.getID().getLocalID());
			rec.setInt(4, bid.price);
			
			rec.write();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
