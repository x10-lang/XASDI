/*
 *  This file is part of the XASDI project (https://github.com/x10-lang/XASDI).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2016.
 */

package com.ibm.xasdi_bridge.simulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.citizen.*;
import com.ibm.xasdi_bridge.message.*;
import com.ibm.xasdi_bridge.rt.log.LogWriter;

/**
 * Proxy between XASDI(X10) and xasdi_bridge(Java).
 */
public class LauncherProxy {
	
	private Integer placeid;
	private int nPlaces;						//	The number of X10 Places
	protected Launcher launcher;
	protected Region region;
	protected MessageRepository repository;
	
	/**
	 * Default value for CitizenFactory ID and CitizenProxyFacory ID
	 */
	public static final int TEMP_FACTORYID = -1;
	
	/**
	 * Create this object and initial agents using Properties
	 * @param prop Properties from boot.xml
	 * @param placeid X10 Place ID
	 */
	public LauncherProxy(Properties prop, int placeid){
		
		nPlaces = Integer.parseInt(prop.getProperty("nPlaces"));		//	The number of X10 Places
		
		World.world().setPlaceID(placeid);
		World.setnPlaces(nPlaces);
		String launcherName = prop.getProperty("launcherName");
		try{
			this.placeid = placeid;
			launcher = (Launcher)Class.forName(launcherName).newInstance();
		}catch(Exception e){
			System.err.println("Agent launcher: " + launcherName + " is not found");
			e.printStackTrace();
		}
		
		String slash = System.getProperty("file.separator");
		String citizencsv = prop.getProperty("workFile", "citizenPlaceMap.csv");	//	set path
		String citizenFile = System.getProperty("user.dir") + slash + citizencsv;
		String line;
		try{
			BufferedReader br = new BufferedReader(new FileReader(citizenFile));
			while((line = br.readLine()) != null){
				String[] data = line.split(",");
				long aid = Long.parseLong(data[0]);
				int pid = Integer.parseInt(data[1]);
				World.world().setCitizenPlaceID(aid, pid);
			}
			br.close();
		}catch(IOException e){
			System.err.println("Citizen PlaceMap file: " + citizenFile + " is not found.");
		}
		
		//	mapping DriverID and PlaceID (If PlaceID is missing, add it as self PlaceID)
		String drivercsv = prop.getProperty("driverFile", "driverPlaceMap.csv");	//	set path
		String driverFile = System.getProperty("user.dir") + slash + drivercsv;
		try{
			BufferedReader br = new BufferedReader(new FileReader(driverFile));
			while((line = br.readLine()) != null){
				String[] data = line.split(",");
				long aid = Long.parseLong(data[0]);
				if(nPlaces == 1){		//	if the number of Places is 1, ignore PlaceID
					World.world().setDriverPlaceID(aid, 0);
				}else{
					if(data.length == 2){
						int pid = Integer.parseInt(data[1]);
						World.world().setDriverPlaceID(aid, pid);
					}else{
						World.world().setDriverPlaceID(aid, placeid);
					}
				}
			}
			br.close();
		}catch(IOException e){
			System.err.println("Driver PlaceMap file: " + driverFile + " is not found.");
			// TODO: when DriverPlaceMap is not found, use default
		}
	}
	
	/**
	 * Create and bind Region and Launcher to this interface.
	 * @param prop Properties from boot.xml
	 */
	public void prepare(Properties prop){

		prop.setProperty("regionID", placeid.toString());
		
		launcher.prepare(prop);
		launcher.start(prop);
		repository = launcher.getMessageRepository();
		repository.setUp(nPlaces);							//	setUp(The number of X10 Places)
		region = launcher.getRegion();
		region.setMessageRepository(repository);
		region.prepareRegion();
	}
	
	/**
	 * Called when simulation is completed.
	 */
	public void complete(){
		region.completedRegion();
		LogWriter lw = World.world().getLogWriter();
		lw.close();
	}
	
	/**
	 * Get set of Citizen agents' IDs in a X10 Place. 
	 * @param pid X10 Place ID
	 * @return Set of Citizen agent's IDs
	 */
	public CitizenSet getCitizenSet(int pid){
		return Region.getCitizenSet(pid);
	}
	
	/**
	 * Get CitizenProxy object from index.
	 * @param index Index of registered CitizenProxy
	 * @return A Citizen agent(CitizenProxy object) bound this index
	 */
	public CitizenProxy getCitizenProxy(int index){
		return region.getCitizenProxies().get(getCitizenIDs().get(index));
	}
	
	/**
	 * Get Driver object from index.
	 * @param index Index of registered Driver
	 * @return A Driver agent(Driver object) bound this index
	 */
	public Driver getDriver(int index){
		return region.getDriver(index);
	}
	
	/**
	 * Transfer repository of messages to Region
	 * @param msgQ message repository to be transferred
	 */
	public void sendMsgQ(MessageQueue msgQ){		
		region.sendMsgQ(msgQ);
	}
	
	/**
	 * Get message repository bound to this object.
	 * @return MessageRepository at this X10 Place.
	 */
	public MessageRepository getMessageRepository(){
		return repository;
	}
	
	/**
	 * Create a Citizen agent from Message. It is used for migration.
	 * @param sm message contains Citizen agent data
	 * @return Created Citizen agent(CitizenProxy)
	 */
	public CitizenProxy restoreCitizen(Message sm){
		return region.restoreCitizen(sm);
	}
	
	/**
	 * Remove a Citizen agent from this X10 Place.
	 * @param lid Citizen agent ID
	 */
	public void removeCitizen(long lid){
		World.world().getCitizenPlaceMap().remove(lid);
		region.removeCitizen(lid);
	}
	
	/**
	 * Get the number of Citizen agents in this X10 Place.
	 * @return The number of Citizen agents registered
	 */
	public int getNumCitizens(){
		return region.getCitizenProxies().size();
	}
	
	/**
	 * Get the number of Driver agents in this X10 Place.
	 * @return The number of Driver agents registered.
	 */
	public int getNumDrivers(){
		return region.getNumDrivers();
	}
	
	/**
	 * Get set of registered Citizens as list of ID.
	 * @return List of registered CitizenID as long type
	 */
	private ArrayList<Long> getCitizenIDs(){
		return region.getCitizenIDs();
	}
	
}
