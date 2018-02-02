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

package com.ibm.xasdi.runtime;

import x10.compiler.Pragma;
import x10.util.*;
import x10.io.File;
import x10.util.logging.LogFactory;

import com.ibm.xasdi.util.*;

public class AgentManager {
	
	protected var pid : Long;						//	Place ID
	protected citizenAgents : AgentRepository;		//	physicalID -> CitizenAgent
	protected citizenIDList : ArrayList[Long];		//	index -> physicalID (for CitizenAgent)
	
	protected var nDrivers:Int = 0n;
	protected driverAgents : AgentRepository;		//	physicalID -> DriverAgent
	protected val id2pMap = new HashMap[Long, Long]();		//	index -> physicalID (for DriverAgent)
	protected val p2idMap = new HashMap[Long, Long]();		//	physicalID -> index (for DriverAgent)

	protected var lp : XLauncherProxy;
	protected var mr : XMessageRepository;
	protected var nThreads:Int;
	var managers:PlaceLocalHandle[AgentManager];
	protected val nPhases:Int;

	private static val logger = LogFactory.getLog("XASDILOG");

	static val BROADCASTID = -1n; // same definition in MessageRepository
	static val REGIONBROADCASTID = -2n; // same definition in MessageRepository

	public def this(nThreads:Int, nPhases:Int){
		this.nThreads = nThreads;
		this.nPhases = nPhases;
		this.citizenAgents = new AgentRepository();
		this.citizenIDList = new ArrayList[Long]();
		this.driverAgents = new AgentRepository();
	}
	
	public def printAgentRepository(){				//	@DEBUG
		Console.OUT.println("citizenAgents : " + here.id);
		for(repset in citizenAgents.entries()){
			Console.OUT.println(repset.getKey() + "<-->" + repset.getValue());
		}
		Console.OUT.println("driverAgents : " + here.id);
		for(repset in driverAgents.entries()){
			Console.OUT.println(repset.getKey() + "<-->" + repset.getValue());
		}		
	}
	
	public def complete(){
		lp.complete();
	}

	public def getAgentRepository():AgentRepository { return this.citizenAgents; }

	public def setPID(pid:Long) { 
		this.pid = pid;
	}

	public def setPLH(managers:PlaceLocalHandle[AgentManager]){
		this.managers = managers;
	}
	
	public def echoTestMessage() {
		Console.OUT.println("Agent Manager at a place : " + pid);
	}

	//	create DriverAgent and CitizenAgent objects
	//	CitizenAgent objects can be also added at "addCitizen" method
	public def createAgents(lp:XLauncherProxy){
		
		this.lp = lp;
		this.mr = lp.getMessageRepository();
		this.nDrivers = lp.getNumDrivers();
		mr.setUp(Place.numPlaces() as Int);
		logger.info("Drivers = " + nDrivers); 
		for (var index:Int = 0n; index<nDrivers; index++){
			val d = lp.getDriver(index);

			if(Debug.debugEnabled) Console.OUT.println("create a driver : index=" + index + " " + d);
			did:Long = d.getID();
			p2idMap.put(did, index);
			id2pMap.put(index, did);
			agent:DriverAgent = new DriverAgent(new AgentID(pid,index), this, d);
			driverAgents.put(did, agent);
		}
		
		val nCitizens = lp.getNumCitizens();		//	first number of citizens
		logger.info("Citizens = " + nCitizens );
		for(var i:Int=0n; i<nCitizens; i++){
			val cp = lp.getCitizenProxy(i);
			val cid = cp.getID();
			val ca = new CitizenAgent(new AgentID(pid, i), this, cp);
			citizenAgents.put(cid, ca);
			citizenIDList.add(cid);
		}
	}


	public def registNewAgents(){
		val nCitizens = mr.getNumNewCitizens();		//	number of new citizens
		logger.info("New Citizens = " + nCitizens );
		for(var i:Int=0n; i<nCitizens; i++){
			val cp = mr.getNewCitizenProxy(i);
			val cid = cp.getID();
			if (!citizenIDList.contains(cid)) {
				val ca = new CitizenAgent(new AgentID(pid, i), this, cp);
				citizenAgents.put(cid, ca);
				citizenIDList.add(cid);
			}
			// TODO: regist this new agent to other places
		}
		mr.clearNewCitizens();
		val ntotal = citizenIDList.size();
		logger.info("Num of Citizens = " + ntotal );
	}
	
	//	below 2 methods are called by AgentSimulator to exchange CitizenProxy Agents
	//	put CitizenProxy(from CitizenProxyMessage) to this AgentManager
	public def addCitizen(sm:XMessage){
		val cp = lp.restoreCitizen(sm);
		val cid = cp.getID();
		val aid = new AgentID(here.id, cid);
		val ca = new CitizenAgent(aid, this, cp);
		
		citizenAgents.put(cid, ca);
		citizenIDList.add(cid);
	}
	
	public def removeCitizen(cid:Long):Boolean{		//	remove CitizenProxy from this AgentManager
		if(citizenIDList.contains(cid)){
			citizenIDList.remove(cid);
			citizenAgents.remove(cid);
			return true;
		}else{
			return false;
		}
	}
	
	/** 
	 * A method for running all the citizens' method 
	 */
	public def runCitizens(time:Long){
		val nCitizens = citizenIDList.size();
		val nAgentsPerThread:Long = nCitizens / this.nThreads;
		
		if(nAgentsPerThread == 0){		//	nCitizens < nThreads
			@Pragma(Pragma.FINISH_LOCAL) finish for(i in 0..(nCitizens-1)) async{
				val cid = citizenIDList(i);
				val ag = citizenAgents.getAgent(cid) as CitizenAgent;
				ag.run(time);
			}
		}else{
			@Pragma(Pragma.FINISH_LOCAL) finish for (i in 0..(this.nThreads-1)) async {
				val startIndex = (nAgentsPerThread != 1 || i==0) ? (i*nAgentsPerThread) : (i*nAgentsPerThread+1);
				
				val tmp = (nAgentsPerThread != 1) ? (i+1)*nAgentsPerThread-1 : (i+1)*nAgentsPerThread;
				
				var endIndex:Long = tmp;
				if(tmp >= nCitizens) endIndex  = nCitizens -1;
				if(i == this.nThreads-1)endIndex = nCitizens -1;
				if (Debug.debugEnabled) Console.OUT.println(here.id + " Handling citizens from " + startIndex + " to " + endIndex);
				
				for(var j:Long = startIndex; j<=endIndex; j++){
					val cid = citizenIDList(j);
					if(citizenAgents.existAgent(cid) == false ) continue;
					val ag = citizenAgents.getAgent(cid) as CitizenAgent;
					ag.run(time);
				}
			}
		}	
	}
	
	/**
	 *  A method for running all the drives' method 
	 */
	public def runDrivers(time:Long){
		val NPHASES = this.nPhases;
		val NTHREADS = this.nThreads;

		if (NPHASES > 1) {
			for (phaseId in 0n..(NPHASES-1n)) {
				@Pragma(Pragma.FINISH_LOCAL) finish for (i in 0n..(NTHREADS-1n)) async {
					for(var j:Int = i; j < nDrivers; j += NTHREADS) {
						val did = id2pMap.get(j);
						val ag = driverAgents.getAgent(did) as DriverAgent;
						ag.run(time, phaseId);
					}
				}
			}
		} else {
			@Pragma(Pragma.FINISH_LOCAL) finish for (i in 0n..(NTHREADS-1n)) async {
				for(var j:Int = i; j < nDrivers; j += NTHREADS) {
					val did = id2pMap.get(j);
					val ag = driverAgents.getAgent(did) as DriverAgent;
					ag.run(time);
				}
			}
		}
	}
	
	public def handleMessages(totalMsgQ:XMessageQueue){		//	totalMsgQ is a message queue for storing all the received messages
		val bmsgs = totalMsgQ.getMessages(BROADCASTID);
		for(aid:Long in citizenAgents.keySet()){ // TODO: not loop all citizen
			val cp = (citizenAgents.getAgent(aid) as CitizenAgent).getCitizen();
			if(bmsgs != null){
				if(Debug.debugEnabled)Console.OUT.println("Broadcast onMessages " +  bmsgs.size() + " at " + aid + " " + cp.getID());
				cp.onMessages(bmsgs);
			}
			
			val msgs = totalMsgQ.getMessages(aid); // TODO: concat 2 messagelist
			if(msgs != null){
				if(Debug.debugEnabled)Console.OUT.println("onMessages " +  msgs.size() + " at " + aid + " " + cp.getID());
				cp.onMessages(msgs);
			}
		}
		lp.sendMsgQ(totalMsgQ);		// send the same messages to LauncherProxy, Region, Driver, etc.
	}

	public def showP2idMap(){
		for(set in p2idMap.entries()){
			Console.ERR.println("Phys:" + set.getKey() + " -> Index:" + set.getValue() + " Place:" + here.id);
		}
	}
	
	public def showID2PMap(){
		for(set in id2pMap.entries()){
			Console.ERR.println("Index:" + set.getKey() + " -> Phys:" + set.getValue() + " Place:" + here.id);
		}
	}
}
