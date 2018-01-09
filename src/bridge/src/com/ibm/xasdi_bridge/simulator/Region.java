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

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import com.ibm.xasdi_bridge.*;
import com.ibm.xasdi_bridge.citizen.*;
import com.ibm.xasdi_bridge.message.*;
import com.ibm.xasdi_bridge.rt.state.StateImpl;
import com.ibm.xasdi_bridge.rt.state.UpdateableStateImpl;

/**
 * Simulation object manages agents(Citizen, Driver), factories of Citizen agents, 
 */
public abstract class Region {
	
	/**
	 * Repository of CitizenProxyFactory objects.
	 */
	protected Map<Integer, CitizenProxyFactory> cpfMap = new HashMap<Integer, CitizenProxyFactory>();		//	<FactoryID, CPF>

	/**
	 * Repository of CitizenFactory objects.
	 */
	protected Map<Object, CitizenFactory> cfMap = new HashMap<Object, CitizenFactory>();					//	<FactoryID, CF>

	/**
	 * Repository of CitizenProxy objects.
	 */
	protected static HashMap<Long, CitizenProxy> citizenProxies = new HashMap<Long, CitizenProxy>();

	/**
	 * Repository of CitizenSet (set of CitizenID) objects.
	 */
	protected static Map<Long, CitizenSet> citizenSet = new HashMap<Long, CitizenSet>();

	/**
	 * Repository of CitizenID objects.
	 */
	protected ArrayList<Long> citizenIDs = new ArrayList<Long>();						//	Index(0 origin), PhysicalCitizenID(long)
	
	/**
	 * Repository of Group objects.
	 */
	protected static HashMap<Long, Place> places = new HashMap<Long, Place>();			//	PhysicalGroupID, Place

	/**
	 * Repository of Group's IDs.
	 */
	protected ArrayList<Long> placeIDs = new ArrayList<Long>();					//	Index, PhysicalGroupID
	
	/**
	 * Repository of Driver objects.
	 */
	protected static HashMap<Long, Driver> drivers = new HashMap<Long, Driver>();		//	PhysicalDriverID, Driver
	
	/**
	 * Repository of Driver's IDs.
	 */
	protected ArrayList<Long> driverIDs = new ArrayList<Long>();				//	Index, PhysicalDriverID

	/**
	 * Region ID.
	 */
	protected long id;

	/**
	 * MessageRepository object. Some class can access it via getMessageRepository(). 
	 */
	private MessageRepository repository;
	
	/**
	 * Number of threads?
	 */
	private int concurrency = 10;
	
	/**
	 * Array of RegionListener.
	 */
	RegionListener[] regionListeners = null;
	
	/**
	 * The number of phase at each step.
	 */
	int numberOfPhase = 1;
	
	/**
	 * Constructor that should be used.
	 * @param id Region ID
	 */
	public Region(long id) {
		this.id = id;
	}
	
	/**
	 * Constructor without argument (RegionID is X10 PlaceID)
	 */
	public Region(){
		this(World.world().getPlaceID());		//	if RegionID is the same as X10 PlaceID
	}
	
	/**
	 * Get default CitizenFactory object without any arguments.
	 * @return CitizenFactory object
	 */
	protected abstract CitizenFactory getCitizenFactory();

	/**
	 * Bind a CitizenProxy factory to this object.
	 * @param id Factory ID
	 * @param cpf Factory object
	 */
	public void addCitizenProxyFactory(int id, CitizenProxyFactory cpf){
		cpfMap.put(id, cpf);
	}
	
	/**
	 * Bind a Citizen factory to this object.
	 * @param factoryID Factory ID
	 * @param cf Factory object
	 */
	public void addCitizenFactory(Object factoryID,CitizenFactory cf) {
		synchronized(cfMap) {
			cfMap.put(factoryID,cf);
		}
	}

	/**
	 * Get entries of Citizen factories.
	 * @return HashMap about Citizen factories(Key is ID, Agent is Factory)
	 */
	public Map<Object, CitizenFactory> getFactoryMap(){
		return cfMap;
	}
	
	/**
	 * Get a Citizen factory from ID.
	 * @param obj Citizen factory ID
	 * @return Citizen factory object
	 */
	public CitizenFactory getCitizenFactory(Object obj) {
		return cfMap.get(obj);
	}
	
	/**
	 * Create and store Citizen and CitizenProxy objects.
	 * @param factoryID	ID of CitizenFactory and CitizenProxyFactory
	 * @param cid CitizenID to be set
	 * @param arg Any argument application defines
	 * @return Created CitizenProxy object
	 */
	public CitizenProxy createCitizen(Integer factoryID, CitizenID cid, Object arg){
		long lid = cid.getLocalID();
		int pid = World.world().getPlaceID();
		CitizenSet cset = citizenSet.get(id);
		if (cset == null) System.err.println("CitizenSet for id "+id+" is null");
		cset.add(cid);
		CitizenProxy cp = cpfMap.get(factoryID).newInstance(this);
		cp.prepareProxy(cid);
		citizenProxies.put(lid, cp);
		citizenIDs.add(lid);
		repository.addCitizen(lid, pid);
		return cp;
	}
	
	/**
	 * Get all CitizenProxy objects with ID.
	 * @return HashMap of ID and CitizenProxy object.
	 */
	public HashMap<Long, CitizenProxy> getCitizenProxies(){
		return citizenProxies;
	}
	
	/**
	 * Get a Citizen agent from Citizen ID(long)
	 * @param id Citizen agent ID(long)
	 * @return Citizen agent(CitizenProxy object)
	 */
	public static CitizenProxy getCitizenProxy(long id){
		return citizenProxies.get(id);
	}
	
	/**
	 * Remove CitizenProxy and CitizenID from this Region.
	 * @param lid CitizenProxy ID (Long)
	 */
	public synchronized void removeCitizen(long lid){
		if(citizenProxies.containsKey(lid))citizenProxies.remove(lid);
		if(citizenIDs.contains(lid))citizenIDs.remove(lid);
		repository.removeCitizen(lid);
	}
	
	/**
	 * Get all Citizen ID values(Long).
	 * @return ArrayList of Citizen ID values(Long)
	 */
	public ArrayList<Long> getCitizenIDs(){
		return citizenIDs;
	}

	/**
	 * Get a set of Citizen agents from set ID.
	 * @param pid ID of Citizen set
	 * @return Set of Citizen agent IDs
	 */
	public static CitizenSet getCitizenSet(long pid){
		return citizenSet.get(pid);
	}
	
	/**
	 * Get a iterator of Citizen agent sets
	 * @return Citizen agent sets as Iterator
	 */
	public static Iterator<CitizenSet> getCitizenSets(){
		return citizenSet.values().iterator();
	}
	
	/**
	 * Create and add a Citizen agent set using ID
	 * @param id ID of Citizen set
	 */
	public void createCitizenSet(long id){
		citizenSet.put(id, new CitizenSet(id));
	}
	
	/**
	 * Create and add a Citizen agent set using this region's ID
	 */
	public void createCitizenSet(){
		citizenSet.put(id, new CitizenSet(id));
	}

	/**
	 * Set the number of phases at each simulation step.
	 * @param i The number of phases
	 */
	public void setNumberOfPhases(int i){ World.world().setNumberOfPhase(i); }
	
	/**
	 * Add a CitizenProxy factory with ID
	 * @param factoryID Factory ID
	 * @param factory Factory Object
	 */
	public void addCitizenProxyFactory(Integer factoryID, CitizenProxyFactory factory){ 
		cpfMap.put(factoryID, factory);
	}
	
	/**
	 * Add a CitizenProxy factory with submitter name.
	 * @param submitter Submitter name
	 * @param factory CitizenProxy factory object
	 * @throws IllegalStateException Never thrown
	 */
	public void addCitizenProxyFactory(String submitter, CitizenProxyFactory factory) throws IllegalStateException{
		cpfMap.put(0, factory);
	}

	/**
	 * Get a Iterator of registered Driver agents.
	 * @return Registered Driver agents as Iterator
	 */
	public Iterator<Driver> getDrivers() { return drivers.values().iterator();}
	
	/**
	 * Get the number of registered Driver agents.
	 * @return The number of registered Driver agents
	 */
	public int getNumDrivers() { return drivers.size(); }
	
	/**
	 * Get a Driver agent from index.
	 * @param index Index of registered Driver agent
	 * @return Driver agent
	 */
	public Driver getDriver(int index){
		return getDriverFromID(driverIDs.get(index));
	}
	
	/**
	 * Get a Driver agent from agent ID.
	 * @param id Driver agent ID as long
	 * @return Driver agent object
	 */
	public static Driver getDriverFromID(long id){
		return drivers.get(id);
	}

	/**
	 * Get this region ID.
	 * @return Region ID
	 */
	public long getID() { return id;}
	
	public void defined() {}
	
	/**
	 * Add a Driver agent to this region using defined ID.
	 * @param d Driver agent ID
	 * @return If this Driver agent should be added(correct ID), return true. Otherwise, return false.
	 */
	public boolean addDriver(Driver d){
		d.setRegion(this);
		long did = d.getID();
		drivers.put(did, d);
		driverIDs.add(did);
		World.world().setDriverPlaceID(did, World.world().getPlaceID());
		return true;
	}
	
	/**
	 * Add a Driver agent to this region with optional ID.
	 * @param id Driver agent ID
	 * @param d Driver object
	 */
	public void addDriver(long id, Driver d){
		d.setRegion(this);
		drivers.put(id, d);
		driverIDs.add(id);
		World.world().setDriverPlaceID(id, World.world().getPlaceID());
	}
	
	/**
	 * Add a group of agents to this region.
	 * @param p Group of agents to be added
	 */
	public void addPlace(Place p){
		long lid = p.getID().getLocalID();
		places.put(lid, p);
		placeIDs.add(lid);
	}
	
	public StateImpl createState() {
		return new StateImpl();
	}
	
	public UpdateableStateImpl createUpdateableState(StateImpl state) {
		return new UpdateableStateImpl(state);
	}
	
	/**
	 * Send a message.
	 * @param msg Message to be sent
	 * @return If sending message is succeeded, return the same message as argument. If failed, return null.
	 */
	public Message sendMessage(Message msg){
		return repository.addMessage(msg);
	}
	
	/**
	 * Send a broadcast message.
	 * @param msg Message to be sent
	 * @return If sending message is succeeded, return the same message as argument. If failed, return null.
	 */
	public Message sendBroadcastMessage(Message msg){
		msg.setRegionID(id);
		return repository.addBroadcastMessage(msg);
	}
	
	/**
	 * Send a message to one CitizenProxy.
	 * @param cid destination CitizenID
	 * @param msg Message to be sent
	 * @return If arguments are null, return null. Otherwise, return message to be sent.
	 */
	public Message sendMessage(CitizenID cid, Message msg){
		return repository.addMessage(cid,msg);
	}

	/**
	 * Bind a repository of messages to this region.
	 * @param repository Message repository bound to this region
	 */
	public void setMessageRepository(MessageRepository repository) {
		this.repository = repository;
		repository.setRegion(this);
	}
	
	/**
	 * Get a repository of messages to this region.
	 * @return Message repository bound to this region
	 */
	public MessageRepository getMessageRepository(){
		return repository;
	}
	
	/**
	 * Get the number of agent groups.
	 * @return The number of groups of agent
	 */
	public int getNumberOfPlaces(){
		return places.size();
	}
	
	/**
	 * Get a group of agents from ID.
	 * @param pid ID of group
	 * @return A group of agents
	 */
	public static Place getPlace(PlaceID pid){
		return places.get(pid.getLocalID());
	}
	
	/**
	 * Get groups of agents as Iterator
	 * @return A groups of agents as Iterator
	 */
	public Iterator<Place> getPlaces(){
		return places.values().iterator();
	}
	
	public void setSimulationConcurrency(Integer concurrency){
		this.concurrency = concurrency;
	}
	
	public int getSimulationConcurrency() {
		return concurrency;
	}
	
	/**
	 * Add listener to this region.
	 * @param listener RegionListener object
	 * @throws IllegalStateException Never thrown
	 */
	public synchronized void addRegionListener(RegionListener listener) throws IllegalStateException {
		if (regionListeners == null) {
			regionListeners = new RegionListener[1];
			regionListeners[0] = listener;
		} else {
			RegionListener[] n = new RegionListener[regionListeners.length+1];
			System.arraycopy(regionListeners, 0, n , 0, regionListeners.length);
			n[regionListeners.length] = listener;
			regionListeners = n;
		}
	}
	
	/**
	 * Add group of agents to this region.
	 * @param ps Array of agents' group
	 * @throws IllegalStateException Never throws
	 */
	public synchronized void addPlaces(Place[] ps) throws IllegalStateException {
		for(int i=0;i<ps.length;i++) {
			long pid = ps[i].getID().getLocalID();
			places.put(pid, ps[i]);
			placeIDs.add(pid);
		}
	}
	
	/**
	 * Add Driver agents to this region.
	 * @param s Array of Driver agents
	 * @throws IllegalStateException Never throws
	 */
	public synchronized void addDrivers(Driver[] s) throws IllegalStateException {
		for(int i=0;i<s.length;i++) {
			s[i].setRegion(this);
			long did = s[i].getID();
			drivers.put(did, s[i]);
			driverIDs.add(did);
			World.world().setDriverPlaceID(did, World.world().getPlaceID());
		}
	}
    
	/**
	 * Prepare simulation.
	 */
	void prepareRegion() {
		prepare();
		if (regionListeners != null) {
			for(int i=0;i<regionListeners.length;i++) {
				regionListeners[i].prepare(id);
			}
		}
		begin();
		beginDriver();
		if (regionListeners != null) {
			for(int i=0;i<regionListeners.length;i++) {
				regionListeners[i].begin(id);
			}
		}
	}
	
	/**
	 * Tell all drivers that simulation started.
	 */
	private void beginDriver() {
		if (drivers == null) {
			return;
		}
		for(Driver d : drivers.values()) {
			d.begin();
		}
	}
	
	/**
	 * Called when simulation finished.
	 */
	void completedRegion() {
		completedDriver();
		completedPlace();
		complete();
	}
	
	/**
	 * Run simulation at one step.
	 * @param time Simulation step
	 * @throws IllegalStateException Never throws
	 * @throws IOException Never throws
	 */
	void execute(long time) throws IllegalStateException, IOException {
		for(int i=0;i<numberOfPhase;i++) {
			executeSimAtPhase(time,i);
		}		
	}
	
	/**
	 * Run simulation at one phase.
	 * @param time Simulation step
	 * @param n Simulation phase
	 */
	private void executeSimAtPhase(long time, int n) {

		begin(time,n);
		
		if (regionListeners != null) {
			for(int i=0;i<regionListeners.length;i++) {
				regionListeners[i].begin(id,time,n);
			}
		}
		complete(time,n);
		if (regionListeners != null) {
			for(int i=0;i<regionListeners.length;i++) {
				regionListeners[i].complete(id,time,n);
			}
		}
	}
	
	/**
	 * Send the same messages to Region, Driver agent, etc.
	 * @param msgq Message repository to be sent
	 */
	public abstract void sendMsgQ(MessageQueue msgq);
	
	/**
	 * Tell all groups of agents that simulation finished.
	 */
	private void completedPlace() {
		if (places == null) {
			return;
		}
		
		for(Driver driver : drivers.values()){
			driver.complete();
		}
		
		for(Place group : places.values()) {
			group.commitState();
			group.complete();
		}
	}

	/**
	 * Tell all Driver agents that simulation finished.
	 */
	private void completedDriver() {
		if (drivers == null) {
			return;
		}
		for(Driver driver : drivers.values()) {
			driver.complete();
		}
	}
	
	public void prepare() {}

	public void begin() {}	
	public void begin(long time) {}
	public void begin(long time, int phase) {}
	
	public void complete() {}
	public void complete(long time) {}
	public void complete(long time, int phase) {}

	/**
	 * Restore CitizenProxy from sent Message.
	 * @param sm	Message object contains CitizenProxy data
	 * @return	Restored CitizenProxy object
	 */
	public CitizenProxy restoreCitizen(Message sm) {
		long lid = sm.getCitizenID();
		CitizenID cid = new CitizenID(lid);
		CitizenProxy cp = createCitizen(LauncherProxy.TEMP_FACTORYID, cid, this);
		
		return cp;
	}
	
	/**
	 * Restore object field values.
	 * @param sm Message contains object to be restored
	 * @param obj Movable object only constructed (not all fields set yet)
	 * @return Restored object
	 */
	public static Movable restore(Message sm, Movable obj){
		try {
			HashMap<String, Object> values = sm.getFields();
			Class<? extends Movable> cls = obj.getClass();
			Field[] fs = cls.getDeclaredFields();
			
			for( Field f : fs){
				f.setAccessible(true);
				if(values.containsKey(f.getName())){
					f.set(obj,values.get(f.getName()));
				}
			}

		} catch (Exception e) {
			System.err.println("Failed to restore an object.");
			e.printStackTrace();
			return null;
		}
		return obj;
	}

	/**
	 * Set all field variables to repository as Message.
	 * @param obj
	 * @return 
	 */
	public static HashMap<String, Object> setFields(MovableFields obj) {		//	for Movable
		HashMap<String, Object> values = new HashMap<String, Object>();
		Class<? extends MovableFields> cls = obj.getClass();
		Field[] fs = cls.getDeclaredFields();

		for(Field f : fs){
			f.setAccessible(true);			//	apply to private field
			String name = f.getName();
			Object value = null;
			try {
				value = f.get(obj);
			} catch (IllegalArgumentException e) {
				System.err.println("Invalid object.");
				return null;
			} catch (IllegalAccessException e) {
				System.err.println("Cannot access field.");
				return null;
			}			
			if(name != "this$0"){ 
				values.put(name,value);
			}
		}
		return values;
	}

}
