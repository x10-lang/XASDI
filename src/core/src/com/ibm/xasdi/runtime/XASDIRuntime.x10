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

package com.ibm.xasdi.runtime;

import x10.util.Random;
import x10.util.ArrayList;
import x10.util.HashMap;
import x10.io.File;
import x10.util.logging.LogFactory;

import com.ibm.xasdi.util.Properties;
import com.ibm.xasdi.util.FileInputStream;

/***
 * Two agents, agent1 and agent2, exchange request-reply message without any queue
 * Each place has a PlaceLocalHandle object that holds an AgentManager
 * This class does not introduce a messaging queue by directrly 
 */

public class XASDIRuntime {

	public static val NAME = "IBM X10-based Agent Simulation on Distributed Infrastructure (XASDI)";
	public static val VERSION = "0.9.0";

	public static val plhOpt = false;
	private static val logger = LogFactory.getLog("XASDILOG");
	
	public static def main(args: Rail[String]){
		logger.info(NAME + " " + VERSION);
        
		val prop:Properties = new Properties();
		val slash = prop.getSystemProperty("file.separator");
		
		var bootxmlPath:String = prop.getSystemProperty("xasdi.location.bootxml");
		if (null == bootxmlPath) {
			bootxmlPath = ".." + slash + "xasdi" + slash + "xml" + slash + "boot.xml";
		}
		
		try{
			logger.info("Read boot.xml: "+bootxmlPath);
			prop.loadFromXML(new FileInputStream(bootxmlPath));
		} catch (e:CheckedException) {
			logger.error("Error in reading boot.xml");
			logger.error(e.typeName());
		}

		if (checkBootxml(prop)) {
        
			var dir:String = prop.getProperty("directory");
		
			var simTime : Long;
			var simName : String;
			var nThreads : Int;
			var nPlaces : Int;
		
			var total : Int = 0n;
			var syncInterval : Int = 1n;
		
			simTime = Long.parse(prop.getProperty("simTime"));
			simName = prop.getProperty("simName");
			nThreads = Int.parse(prop.getProperty("nThreads"));
			nPlaces = Int.parse(prop.getProperty("nPlaces"));
			val nPhases = Int.parse(prop.getProperty("numPhase"));
		
			var time: long = - System.nanoTime();
		
			logger.info("Initializing.");
		
			val am : AgentManager = new AgentManager(nThreads, nPhases);
		
			logger.info("Copying Agent Simulator to Multiple Places with PlaceLocalHandle ...");
			logger.info("It might take a while if the number of places is many.");
		
			val nThreads_ = nThreads;
			var copyTime:long = System.nanoTime();
			val agentsim : PlaceLocalHandle[AgentSimulator];
			if(XASDIRuntime.plhOpt){
				agentsim = PlaceLocalHandle.makeFlat[AgentSimulator](Place.places(), ()=> new AgentSimulator(am, nThreads_));
			}else{
				agentsim = PlaceLocalHandle.make[AgentSimulator](Place.places(), ()=> new AgentSimulator(am, nThreads_));
			}
			logger.info("Finished Copying Agent Simulator. Elapsed time=" + ((System.nanoTime() - copyTime)/(1000*1000*1000)) + "s" );
		
			agentsim().run(simTime, dir, simName, syncInterval);
		
			logger.info("Application is termimnated successfully");
		
			time += System.nanoTime();
			logger.info("Total (time=" + (time/(1000*1000)) + " ms)");
		}

	}
	
	public static def init(dir:String, simName:String, simTime:Long): XLauncherProxy{
		val prop:Properties = new Properties();
		val slash = prop.getSystemProperty("file.separator");
		try{
			var bootxmlPath:String = prop.getSystemProperty("xasdi.location.bootxml");
			if (null == bootxmlPath) {
				bootxmlPath = ".." + slash + "xasdi" + slash + "xml" + slash + "boot.xml";
			}
			prop.loadFromXML(new FileInputStream(bootxmlPath));
		} catch (e:CheckedException) {
			Console.ERR.print(e);
		}
		prop.setProperty("simulation", simName);			//	use argument
		prop.setProperty("simTime", simTime.toString());
		prop.setProperty("placenum", Place.numPlaces().toString());

		val lp: XLauncherProxy = new XLauncherProxy(prop, here.id as Int);
	        logger.error("Prepare Agent Launcher...");
		lp.prepare(prop);
		
		return lp;
	}

	static def checkBootxml(prop:Properties): Boolean {
		val propertyNames = ["simulation", "directory", "nAgents", "simTime",
			"simName", "nThreads", "nPlaces", "numPhase",
			"syncInterval", "launcherName", "workFile", "driverFile",
			"deltat", "lambda", "updateTime", "smplTime",
			"poisson", "driver", "logFile", "workFolder",
			"logBuffSize", "logPort", "logServer", "logTrip",
			"logVhcl", "logRoad", "logQrho", "seed"];
		val propertyType =  [-1n, 1n, -2n, 3n,
			1n, 2n, 2n, 2n,
			-2n, 1n, 1n, 1n,
			-5n, -5n, -2n, -2n,
			-6n, -1n, 1n, -1n,
			-2n, -2n, -1n, -6n,
			-6n, -6n, -6n, -3n];
		// 1: String, 2: Int, 3: Long, 5: Double, 6: Boolean, 12: initialized Int
		val propertyMust =  [false, true,  false, true,
			true,  true,  true,  true,
			false, true,  true,  true,
			false, false, false, false,
			false, false, true,  false,
			false, false, false, false,
			false, false, false, false];
		for (var i:Int = 0n; i < propertyNames.size; i++) {
			if (propertyType(i) > 0n) {
				val tmpStr = prop.getProperty(propertyNames(i));
				if (tmpStr == null) {
					logger.error(propertyNames(i) + " doesn't exist");
					if (propertyMust(i)) return false;
				} else {
					switch(propertyType(i)) {
						case 1n:
							logger.info(propertyNames(i) + ": " + tmpStr);
							break;
						case 2n:
							try {
								val tmpVal = Int.parse(tmpStr);
								logger.info(propertyNames(i) + ": " + tmpVal);
							} catch (e:Exception) {
								logger.error("Error: Value of " + propertyNames(i) + " should be integer");
							}
							break;
						case 3n:
							try {
								val tmpVal = Long.parse(tmpStr);
								logger.info(propertyNames(i) + ": " + tmpVal);
							} catch (e:Exception) {
								logger.error("Error: Value of " + propertyNames(i) + " should be integer");
							}
							break;
						case 5n:
							try {
								val tmpVal = Double.parse(tmpStr);
								logger.info(propertyNames(i) + ": " + tmpVal);
							} catch (e:Exception) {
								logger.error("Error: Value of " + propertyNames(i) + " should be double");
							}
							break;
						case 6n:
							try {
								val tmpVal = Boolean.parse(tmpStr);
								logger.info(propertyNames(i) + ": " + tmpVal);
							} catch (e:Exception) {
								logger.error("Error: Value of " + propertyNames(i) + " should be boolean");
							}
							break;
						default:
							break;
					}
				}
			}
		}
		return true;
	}

}
