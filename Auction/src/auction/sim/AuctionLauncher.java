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

import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

import com.ibm.xasdi_bridge.PlaceID;
import com.ibm.xasdi_bridge.message.MessageRepository;
import com.ibm.xasdi_bridge.simulator.Launcher;
import com.ibm.xasdi_bridge.simulator.LauncherProxy;
import com.ibm.xasdi_bridge.simulator.Region;
import com.ibm.xasdi_bridge.simulator.World;

import auction.agent.BidderFactory;
import auction.agent.BidderProxyFactory;
import auction.behavior.AuctionDriver;
import auction.message.AuctionResolver;

public class AuctionLauncher implements Launcher {
	
	private Auctioneer auctioneer = new Auctioneer();
	private long auctioneerid;
	private long numPlace;
	private long houseid;
	
	private AuctionLogs logs;
	private String logFile;
	private final String slash = System.getProperty("file.separator");

	static private Logger logger = Logger.getLogger(AuctionLauncher.class.getName());

	@Override
	public MessageRepository getMessageRepository() {
		return auctioneer.getMessageRepository();
	}

	@Override
	public Region getRegion() {
		return auctioneer;
	}

	@Override
	public void prepare(Properties prop) {
		BidderProxyFactory bidderProxyFactory = new BidderProxyFactory();
		auctioneer.addCitizenProxyFactory(Auctioneer.FACTORYID, bidderProxyFactory);
		auctioneer.setNumberOfPhases(1);
		auctioneer.setMessageRepository(new MessageRepository());
		auctioneer.getMessageRepository().setResolver(new AuctionResolver());

		BidderFactory bidderFactory = new BidderFactory();		
		auctioneer.addCitizenFactory(Auctioneer.FACTORYID, bidderFactory);
		auctioneer.createCitizenSet();
		
		auctioneerid = auctioneer.getID();
		numPlace = World.getnPlaces();
		houseid = auctioneerid;
		
		long duration = Long.parseLong(prop.getProperty("simTime", "100"));
		long seed = Integer.parseInt(prop.getProperty("seed", "1"));
		int numBidder = Integer.parseInt(prop.getProperty("numBidder", "10"));
		int numHouse = Integer.parseInt(prop.getProperty("numHouse", "1"));
		
		logger.info("numBidder is set to " + numBidder);
		logger.info("numHouse is set to " + numHouse);
		
		logFile = prop.getProperty("logFile") + slash + prop.getProperty("simName");
		logs = new AuctionLogs(auctioneerid, logFile);
		
		String s = prop.getProperty("logBuffSize");
		if (s != null) {
			int size = Integer.parseInt(s) * 1024 * 1024;
			World.world().setLogBufferSize(size);
			logger.info("log buff size is set to " + s + " MB");
		}
		
		if (prop.getProperty("logBid", "false").equalsIgnoreCase("true")) {
			logs.setCategory(BidLog.BID_INFO);
		}
		
		for (int i = 0; i < numHouse; i++) {
			AuctionHouse house = new AuctionHouse(auctioneer, houseid);
			Random rand = new Random(seed + i);
			house.setRandom(rand);
			auctioneer.addPlace(house);
			AuctionDriver auctionDriver = new AuctionDriver(houseid, house, auctioneer);
			auctionDriver.setNumBidder(numBidder);
			auctionDriver.setLog(logs);
			auctioneer.addDriver(auctionDriver);
			
			houseid += numPlace;
		}
		
		World.world().setDeltaTime(1);
		World.world().setSimulationTime(0, duration);
		World.world().addRegion((int) auctioneerid, auctioneer);
	}

	@Override
	public void start(Properties arg0) {
		World.world().start(auctioneer.getID());
	}

}
