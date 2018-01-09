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

package com.ibm.xasdi_bridge.simulator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;	// 20150727HM debug for multi nodes

import com.ibm.xasdi_bridge.fd.engine.Server;
import com.ibm.xasdi_bridge.log.DefaultLogger;
import com.ibm.xasdi_bridge.log.Logger;
import com.ibm.xasdi_bridge.rt.Simulator;
import com.ibm.xasdi_bridge.rt.log.LogWriter;

/**
 * Manages simulation state and logs.
 */
public class World {
	
	static final int NONE = -1;
	static final int STARTED = 0;
	static final int PREPARED = 1;
	static final int EXECUTING = 2;
	static final int EXECUTED = 3;
	static final int FINISHED = 4;
	
	static World world = new World();
	Logger logger = new DefaultLogger();
	private int pid;		//	this world's PlaceID
	static int nPlaces; // The number of X10 Places
	
	Server server;
	Region[] regions = null;
	int size; 
	int deltaTime;
	long time = 0;
	long delta = 1;
	long startTime = 0;
	long endTime = 1000;
	int numPhase = 0;
	
	int numOfThreads = 0;
	int logBufferSize = 1024*1024;
	int regionCounter = 0;
	int simulationCount = 0;
	boolean atWork = false;
	int state = NONE;
	
	private Map<Integer, Region> regionIndex = new HashMap<Integer, Region>();
	HashMap<Region, Simulator> simulators = new HashMap<Region, Simulator>();
	
	/**
	 * Repository stores where X10 Place Citizen object exists.
	 */
	private ConcurrentHashMap<Long, Integer> citizenPlaceMap = new ConcurrentHashMap<Long, Integer>();		//	PhysicalCitizenID, X10 PlaceID

	/**
	 * Repository stores where X10 Place Driver object exists.
	 */
	private HashMap<Long, Integer> driverPlaceMap = new HashMap<Long, Integer>();		//	PhysicalDriverID, X10 PlaceID
	
	LogWriter lw;
	
	/**
	 * Get handling LogWriter object.
	 * @return LogWriter object handled by this World
	 */
	public LogWriter getLogWriter(){
		return this.lw;
	}

	/**
	 * Bind Logger object to this World.
	 * @param logger Logger bound to this World
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	/**
	 * Get this World as static value.
	 * @return Static World object
	 */
	public static World world() {
		return world;
	}
	
	/**
	 * Get X10 Place ID where assigned Citizen agent belongs.
	 * @param cid ID of Citizen agent as long type
	 * @return X10 Place ID where this Citizen agent belongs.
	 */
	public int getCitizenPlaceID(long cid){
		return citizenPlaceMap.get(cid);
	}
	
	/**
	 * Get X10 Place Id where assigned Driver agent belongs.
	 * @param did ID of Driver agent as long type
	 * @return X10 Place ID where this Citizen agent belongs.
	 */
	public int getDriverPlaceID(long did){
		return driverPlaceMap.get(did);
	}
	
	/**
	 * Assign a Citizen agent to X10 Place. Citizen object must be added to proper X10 Place.
	 * @param cid Citizen agent ID
	 * @param pid X10 Place
	 */
	public void setCitizenPlaceID(long cid, int pid){
		citizenPlaceMap.put(cid, pid);
	}
	
	/**
	 * Assign a Driver agent to X10 Place. Driver object must be added to proper X10 Place.
	 * @param did Driver agent ID
	 * @param pid X10 Place
	 */
	public void setDriverPlaceID(long did, int pid){
		driverPlaceMap.put(did, pid);
	}
	
	/**
	 * Get entries of Citizen ID and X10 Place ID where this Citizen agent belongs.
	 * @return HashMap about Citizen ID and X10 Place ID.
	 */
	public ConcurrentHashMap<Long, Integer> getCitizenPlaceMap(){
		return citizenPlaceMap;
	}
	
	/**
	 * Get entries of Driver ID and X10 Place ID where this Driver agent belongs.
	 * @return HashMap about Driver ID and X10 Place ID.
	 */
	public HashMap<Long, Integer> getDriverPlaceMap(){
		return driverPlaceMap;
	}
	
	/**
	 * Return whether a Citizen agent belongs to this X10 Place.
	 * @param citizenID Citizen agent ID
	 * @return If this Citizen agent belongs to this X10 Place, return true.
	 */
	public boolean containsCitizen(long citizenID){
		if(!existsCitizen(citizenID))return false;
		return citizenPlaceMap.get(citizenID) == pid;
	}
	
	/**
	 * Return whether a Driver agent belongs to this X10 Place.
	 * @param driverID Driver agent ID
	 * @return If this Driver agent belongs to this X10 Place, return true.
	 */
	public boolean containsDriver(long driverID){
		if(!existsDriver(driverID))return false;
		return driverPlaceMap.get(driverID) == pid;
	}
	
	/**
	 * Return whether a Citizen agent exists in this simulation.
	 * @param citizenID Citizen agent ID
	 * @return If this Citizen agent exists in this simulation, return true.
	 */
	public boolean existsCitizen(long citizenID){
		return citizenPlaceMap.containsKey(citizenID);
	}
	
	/**
	 * Return whether a Driver agent exists in this simulation.
	 * @param driverID Driver agent ID
	 * @return If this Driver agent exists in this simulation, return true.
	 */
	public boolean existsDriver(long driverID){
		return driverPlaceMap.containsKey(driverID);
	}
	
	/**
	 * Bind this World object to X10 Place.
	 * @param pid X10 Place ID to be bound
	 */
	public void setPlaceID(int pid){
		this.pid = pid;
	}
	
	/**
	 * Get X10 Place ID bound to this World object.
	 * @return X10 Place ID where this World exists.
	 */
	public int getPlaceID(){
		return pid;
	}
	
	public static int getnPlaces()
	{
		return nPlaces;
	}

	public static void setnPlaces(int nPlaces)
	{
		World.nPlaces = nPlaces;
	}

	/**
	 * Sets the time delta between the simulation cycles.
	 * @param d time delta
	 */
	public void setDeltaTime(long d) {
		delta = d;
	}

	/**
	 * Set a simulation start time and end time. Simulation time is a count of simulation cycle.
	 * @param st Start simulation time
	 * @param ed End simulation time
	 */
	public void setSimulationTime(long st, long ed) {
		startTime = st;
		endTime = ed;
	}
	
	/**
	 * Set the number of phase at each simulation step.
	 * @param phase The number of phase
	 */
	public void setNumberOfPhase(int phase){
		this.numPhase = phase;
	}

	/**
	 * Get a simulation start time
	 * @return Start simulation time
	 */
	public long getSimulationStartTime() {
		return startTime;
	}
	
	/**
	 * Get a simulation end time
	 * @return End simulation time
	 */
	public long getSimulationEndTime() {
		return endTime;
	}
	
	/**
	 * Get the log buffer size. The unit of this size is byte.
	 * @param size Log buffer size(byte)
	 */
	public void setLogBufferSize(int size) {
		this.size = size;
	}

	/**
	 * Get the logger bound to this simulation.
	 * @return Logger object
	 */
	public Logger getLogger() { 
		return logger;
	}

	/**
	 * Set the length of time(second) at each simulation step.
	 * @param i The length of time at each simulation step
	 */
	public void setDeltaTime(int i) {
		this.deltaTime = i;
	}
	
	/**
	 * Return whether this simulation is running.
	 * @return If simulation is running now, return true.
	 */
	public boolean isAtWork() {
		return atWork;
	}
	
	/**
	 * Called when simulation is started.
	 */
	public void start(){
		start(0);
	}

	/**
	 * Start simulation and prepare log writer.
	 * @param regionID ID of region to start
	 */
	public void start(long regionID) {
		if(atWork == false){				//	for plural place
			atWork = true;
			try {
				this.lw = new LogWriter(this.logger, regionID, this.simulationCount, this.size, logger.getFile());
				LogWriter.addLogWriter(regionID, lw);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Bind Logger object to this World.
	 * @param defaultLogger New Logger object bound to this World.
	 * @return New Logger object
	 */
	public Logger setLogger(DefaultLogger defaultLogger) {
		logger = defaultLogger;
		return logger;
	}

	/**
	 * Add region(repository of agents and their factories) to this simulation World with ID.
	 * @param i ID of region
	 * @param region Object of region
	 */
	public void addRegion(int i, Region region) {
		regionIndex.put(i, region);
	}
	
	/**
	 * Get a region object from ID.
	 * @param id ID of region
	 * @return Object of region whose ID is the same as argument
	 */
	public Region getRegion(long id) {
		synchronized(regionIndex) {
			return regionIndex.get(new Long(id));
		}
	}
	
	/**
	 * Run this simulation.
	 * @throws IllegalStateException When all of parameters are not set.
	 */
	void execute() throws IllegalStateException {		//	used by Controller
		if (state != PREPARED) {
			throw new IllegalStateException("not prepared");
		}
		regionCounter = 0;
		synchronized(this) {
			state = EXECUTING;
			notifyAll();
		}
	}
	
	/**
	 * Finish this simulation.
	 */
	void finished() {
		regionCounter = 0;
		state = FINISHED;
	}
	
	/**
	 * Prepare simulation before running.
	 * @throws IOException When some of regions fail to start
	 * @throws IllegalStateException When all of parameters already set
	 */
	void prepare() throws IOException, IllegalStateException {		//	used by Controller
		if (state != STARTED && state != FINISHED) {
			throw new IllegalStateException("already prepared/working");
		}
		simulationCount++;
		LogWriter.removeAllLogWriters();
		regionCounter = 0;
		state = STARTED;
		for(int i=0;i<regions.length;i++) {
			start(regions[i]);
		}
	}
	
	/**
	 * Prepare a region. 
	 * @param region Region object to be prepared
	 */
	void prepare(Region region) {
		region.prepareRegion();
	}
	
	/**
	 * Called when a region is prepared.
	 * @param region Region object that is just prepared
	 */
	void prepared(Region region) {
		try {
			synchronized(this) {
				regionCounter++;
				if (regionCounter == regions.length) {
					state = PREPARED;
				}
			}
			if (state == PREPARED) {
				prepared();
			}
			synchronized(this) {
				if (state != EXECUTING) { 
					wait();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when this simulation is prepared.
	 */
	void prepared() {
		regionCounter = 0;
	}
	
	/**
	 * Stop simulation.
	 * @param region Region object
	 * @param time Current simulation time
	 * @return Always false
	 */
	boolean stop(Region region, long time) {
		return false;
	}
	
	/**
	 * Shutdown simulation program.
	 */
	public void shutdown() {	//	used by Controller
	}
	
	/**
	 * Start simulation at designated region.
	 * @param region Region where simulation will be start
	 * @throws IOException When LogWriter fails to be created
	 */
	void start(Region region) throws IOException {
		LogWriter logWriter = new LogWriter(logger, region.getID(),simulationCount,logBufferSize,logger.getFile());
		LogWriter.addLogWriter(region.getID(),logWriter);
	}
}
