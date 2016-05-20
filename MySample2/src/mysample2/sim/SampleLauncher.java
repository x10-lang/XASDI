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

package mysample2.sim;

import java.io.File;
import java.util.Properties;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

import mysample2.SampleCitizenFactory;
import mysample2.SampleCitizenProxyFactory;
import mysample2.SampleDriver;
import mysample2.SamplePlace;
import mysample2.SampleRegion;
import mysample2.SampleResolver;

import com.ibm.xasdi_bridge.PlaceID;
import com.ibm.xasdi_bridge.log.ColumnType;
import com.ibm.xasdi_bridge.log.DefaultLogger;
import com.ibm.xasdi_bridge.log.LogDefinition;
import com.ibm.xasdi_bridge.log.Logger;
import com.ibm.xasdi_bridge.message.MessageRepository;
import com.ibm.xasdi_bridge.simulator.Launcher;
import com.ibm.xasdi_bridge.simulator.LauncherProxy;
import com.ibm.xasdi_bridge.simulator.Region;
import com.ibm.xasdi_bridge.simulator.World;

public class SampleLauncher implements Launcher {

	static final String LOGFILE = "logFile";
	private SampleRegion sampleRegion = new SampleRegion();
	private SampleResolver sampleResolver =	new SampleResolver();

	@Override
	public void prepare(Properties prop) {
		String filename = prop.getProperty(LOGFILE, "sample");
		Logger samplelogger = World.world().setLogger(new DefaultLogger());
		samplelogger.setFile(new File(filename));
		LogDefinition def = new LogDefinition(0,sampleRegion.getID());
		def.addColumn(ColumnType.INT);
		def.addColumn(ColumnType.STRING);
		samplelogger.addLogDefinition(def);
		samplelogger.enableLog(0);
		
		String s = prop.getProperty("logBuffSize");
		if (s != null)
		{
			int size = Integer.parseInt(s) * 1024 * 1024;
			World.world().setLogBufferSize(size);
			System.out.println("log buff size is set to " + s + " MB");
		}
		
		SampleCitizenProxyFactory sampleCitizenProxyFactory = new SampleCitizenProxyFactory();
		sampleRegion.addCitizenProxyFactory(SampleRegion.FACTORYID, sampleCitizenProxyFactory);
		sampleRegion.addCitizenProxyFactory(LauncherProxy.TEMP_FACTORYID, sampleCitizenProxyFactory); // to restore after move
		sampleRegion.setNumberOfPhases(1);
		sampleRegion.setMessageRepository(new MessageRepository());
		sampleRegion.getMessageRepository().setResolver(sampleResolver);

		SampleCitizenFactory sampleCitizenFactory = new SampleCitizenFactory();		
		sampleRegion.addCitizenFactory(SampleRegion.FACTORYID, sampleCitizenFactory);
		PlaceID placeID = new PlaceID(sampleRegion.getID());
		sampleRegion.createCitizenSet();
		SamplePlace samplePlace = new SamplePlace(sampleRegion, placeID.getLocalID());
		sampleRegion.addPlace(samplePlace);
		long driverid = 0L;
		SampleDriver sampleDriver = new SampleDriver(driverid, samplePlace, sampleRegion);
		long numCitizen = Long.parseLong(prop.getProperty("numCitizen"));
		System.out.println("Num Citizen = "+numCitizen);
		sampleDriver.setNumCitizen(numCitizen);

		if (sampleDriver != null) {
			sampleRegion.addDriver(sampleDriver); // TODO: notify new driver info into X10 AgentManager
		}
		World.world().setDeltaTime(1);
		World.world().setSimulationTime(0, 4);
		World.world().addRegion((int) sampleRegion.getID(), sampleRegion); // TODO: mismatch between World's region ID (int) and Region ID (long)
	}

	@Override
	public void start(Properties prop) {
		System.out.println("start...");
		World.world().start(sampleRegion.getID());
	}

//	@Override
	public void shutdown() {
	}

	@Override
	public Region getRegion() {
		return sampleRegion;
	}
	
	@Override
	public MessageRepository getMessageRepository() {
		return sampleRegion.getMessageRepository();
	}

}
