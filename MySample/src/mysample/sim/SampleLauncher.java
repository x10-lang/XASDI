/*
 *  This file is part of the XASDI project (https://github.com/x10-lang/xasdi).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2016.
 */

package mysample.sim;

import java.io.File;
import java.util.Properties;

import mysample.SampleCitizenFactory;
import mysample.SampleCitizenProxyFactory;
import mysample.SampleDriver;
import mysample.SamplePlace;
import mysample.SampleRegion;
import mysample.SampleResolver;

import com.ibm.xasdi_bridge.log.ColumnType;
import com.ibm.xasdi_bridge.log.DefaultLogger;
import com.ibm.xasdi_bridge.log.LogDefinition;
import com.ibm.xasdi_bridge.log.Logger;
import com.ibm.xasdi_bridge.message.MessageRepository;
import com.ibm.xasdi_bridge.simulator.Launcher;
import com.ibm.xasdi_bridge.simulator.Region;
import com.ibm.xasdi_bridge.simulator.World;

public class SampleLauncher implements Launcher {

	static final String LOGFILE = "logFile";
	private SampleRegion sampleRegion = new SampleRegion(0);
	private SampleResolver sampleResolver =	new SampleResolver();

	@Override
	public void prepare(Properties prop) {
		//	defines Log
		String filename = prop.getProperty(LOGFILE, "sample");
		Logger logger = World.world().setLogger(new DefaultLogger());
		logger.setFile(new File(filename));
		LogDefinition def = new LogDefinition(0,0);
		def.addColumn(ColumnType.INT);
		def.addColumn(ColumnType.STRING);
		logger.addLogDefinition(def);
		logger.enableLog(0);
		
		//	defines SampleCitizen(Proxy)Factory, SampleRegion
		SampleCitizenProxyFactory sampleCitizenProxyFactory = new SampleCitizenProxyFactory();
		sampleRegion.addCitizenProxyFactory(SampleRegion.FACTORYID, sampleCitizenProxyFactory);
		sampleRegion.setNumberOfPhases(1);
		sampleRegion.setMessageRepository(new MessageRepository());
		sampleRegion.getMessageRepository().setResolver(sampleResolver);

		SampleCitizenFactory sampleCitizenFactory = new SampleCitizenFactory();		
		sampleRegion.addCitizenFactory(SampleRegion.FACTORYID, sampleCitizenFactory);
		sampleRegion.createCitizenSet(0);
		
		//	defines a place, a driver, a simulator
		SamplePlace samplePlace = new SamplePlace(sampleRegion, 0);
		sampleRegion.addPlace(samplePlace);
		SampleDriver sampleDriver = new SampleDriver(samplePlace, sampleRegion);
		sampleRegion.addDriver(sampleDriver);
		World.world().setDeltaTime(1);
		World.world().setSimulationTime(0, 4);
		World.world().addRegion(0, sampleRegion);
	}

	@Override
	public void start(Properties prop) {
		System.out.println("start...");
		World.world().start();
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
