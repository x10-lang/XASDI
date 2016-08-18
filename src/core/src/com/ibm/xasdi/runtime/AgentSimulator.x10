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

package com.ibm.xasdi.runtime;

import x10.util.*;
import x10.compiler.Pragma;
import x10.util.logging.LogFactory;

/****************************************************************
 * Agent Simulator in X10
 * ***************************************************************/
public class AgentSimulator {
	
	static val CITIZEN_ADD = 101n;
	static val CITIZEN_MOVE = 100n;
	static val CITIZEN_REMOVE = 99n;
	static val CONSOLE = 1000n;
	static val ERROR = 9999n;
	
	protected var agentManager:AgentManager;
	protected var nThreads:Int;						//	for constructor of AgentManager
	protected val citizenMap:Map[Long, Int];		//	mapping CitizenID and X10 PlaceID
		
	private static val logger = LogFactory.getLog("XASDILOG");
	
	var managers:PlaceLocalHandle[AgentManager];

	public def this(agentManager:AgentManager, nThreads:Int){
		this.agentManager  = agentManager;
		this.nThreads = nThreads;
		citizenMap = new HashMap[Long, Int]();
	}
	
	public def getAgentManager() : AgentManager{
		return agentManager;
	}
	
	public def stop(){
		//	do nothing
	}

	public def init(){
		//	do nothing
	}
	
	public def assignGraph(){
		//TODO: graph assign 
	}
	
	public def run(num_of_simulations:Long, dir:String, simName:String, syncInterval:Int){
		val team = x10.util.Team.WORLD;
		val useTeam = true;
		
		logger.info("starting ..." + num_of_simulations);
		
		logger.info("Copying Agent Manager to Multiple Places with PlaceLocalHandle ...");
		logger.info("It might take a while if the number of places is many.");
		
		var copyTime:long = System.nanoTime();
		if(XASDIRuntime.plhOpt){
			managers = PlaceLocalHandle.makeFlat[AgentManager](Place.places(), ()=> agentManager);
			logger.info("Using makeFlat method.");
		}else{
			managers = PlaceLocalHandle.make[AgentManager](Place.places(), ()=> agentManager);
			logger.info("Using make method.");
		}
		logger.info("Finished Copying Agent Manager. Elapsed time=" + ((System.nanoTime() - copyTime)/(1000*1000*1000)) + "s" );
		
		var startTime:long = System.nanoTime();

		finish for (h in Place.places()) at(h) async {
			
			val pid = here.id;
			logger.info("AgentSimulator.run#" + pid);
			val lp : XLauncherProxy = XASDIRuntime.init(dir, simName, num_of_simulations);
			this.init();
			
			ag:AgentManager = managers();
			ag.setPID(pid);
			logger.info("run " + pid);
			ag.setPLH(managers);
			ag.createAgents(lp);
			logger.info("everything is set up and the program is ready to run !");
			logger.info("manager will be responding ...");
		}
		
		// Simulation Cycle
		if(Place.numPlaces() > 1){
			finish for(h in Place.places()) at(h) async {
				for(var t_:Int=0n ; t_ < num_of_simulations; t_++){
					if(Debug.debugEnabled) Console.OUT.println("run agents at place id : " + here.id);
					val t = t_;
					val startstepTime:long = System.nanoTime();
					val ag :AgentManager = managers();
					ag.runDrivers(t);
					ag.runCitizens(t);
					
					team.barrier();
						
					val start = System.nanoTime();		//	evaluation
					
					managers().mr.sendToReply(true);	//	for reply Messages
					exchangeMessages();		

					val elapsed = System.nanoTime() - start;
					if(Debug.debugEnabled)Console.OUT.println("Exchange Messages at " + here.id + ": " + elapsed/(1000*1000) + " ms");
						
					team.barrier();

					updateCitizens(managers().mr.getMoveCitizenMessages());
					team.barrier();

					managers().registNewAgents();
					team.barrier();

					managers().mr.sendToReply(false);   //  for reply Messages
	
					if(here.id == 0){
						var simstepTime:long = System.nanoTime() - startstepTime;
						logger.info("step :" + t +"\t(time=" + (simstepTime/(1000*1000)) + " ms)");
					}
				}
			}
		}else{
			for(var t_:Int=0n ; t_ < num_of_simulations; t_++){
				val t = t_;
				var startstepTime:long = System.nanoTime();
				
				finish for (h in Place.places()) at(h) async {
					if(Debug.debugEnabled) Console.OUT.println("run agents at place id : " + here.id);
					val ag :AgentManager = managers();
					ag.runDrivers(t);
					ag.runCitizens(t);
				}
				
				exchangeMessagesAllPlaces();
				
				var simstepTime:long = System.nanoTime() - startstepTime;
				logger.info("step :" + t +"\t(time=" + (simstepTime/(1000*1000)) + " ms)");
			}
		}

		var stepSimTime:long = System.nanoTime() - startTime;
		logger.info("Simulation (time=" + (stepSimTime/(1000*1000)) + " ms)");

		logger.info("shutdown ...");
		
		finish for (h in Place.places()) async at(h) {
			managers().complete();
		}
	}
	
	//	TODO: use Remote Array Copy
	protected def exchangeMessages(){					//	for using Team
		val pid = here.id;
		
		finish for(h2 in Place.places()) async {
			if(h2.id != pid){							//	here place does not use "at" deep copy
				val temp1 = at(h2){
					managers().mr.getDriverMsgQ(pid as Int)
				};
				
				val temp2 = at(h2){
					managers().mr.getCitizenMsgQ(pid as Int)
				};
				
				managers().handleMessages(temp1);
				managers().mr.checkRemoteMessages(temp2, h2.id as Int);
				managers().handleMessages(temp2);
			}else{										//	handle messages at here place
				val temp1 = managers().mr.getDriverMsgQ(pid as Int);
				val temp2 = managers().mr.getCitizenMsgQ(pid as Int);
				
				managers().handleMessages(temp1);
                                managers().handleMessages(temp2);
			}
		}

		finish for(h2 in Place.places()) async{
			//	TODO: these clear() procedures will be removed (clear at Application level) 
			managers().mr.getDriverMsgQ(h2.id as Int).clear();
			managers().mr.getCitizenMsgQ(h2.id as Int).clear();
		}		
	}
	
	protected def exchangeMessagesRemoteArray(){
		
		val pid = here.id;
		finish for(h2 in Place.places()) async {
			if(h2.id != pid){
				val driverOrigArr = at(h2){
					val arr = new Rail[XMessageQueue](1);
					arr(0) = managers().mr.getDriverMsgQ(pid as Int);
					new GlobalRail(arr)
				};
				val citizenOrigArr = at(h2){
					val arr = new Rail[XMessageQueue](1);
					arr(0) = managers().mr.getCitizenMsgQ(pid as Int);
					new GlobalRail(arr)
				};
				
				logger.info(citizenOrigArr.size + " " + driverOrigArr.size);		//	@DEBUG
				val driverDstArr = new Rail[XMessageQueue](1);
				val citizenDstArr = new Rail[XMessageQueue](1);
				logger.info(citizenDstArr.size + " " + driverDstArr.size);			//	@DEBUG
				
				finish{
					Rail.asyncCopy(driverOrigArr, 0, driverDstArr, 0, 1);
					Rail.asyncCopy(citizenOrigArr, 0, citizenDstArr, 0, 1);
				}
				
				managers().handleMessages(driverDstArr(0));
				managers().handleMessages(citizenDstArr(0));
			}
		}
		
		finish for(h2 in Place.places()) async{
			//	TODO: these clear() procedures will be removed (clear at Application level) 
			managers().mr.getDriverMsgQ(h2.id as Int).clear();
			managers().mr.getCitizenMsgQ(h2.id as Int).clear();
		}
	}

	
	protected def exchangeMessagesAllPlaces(){			//	for not using Team
		finish for(h1 in Place.places()) at(h1) async{
			val pid = here.id;
			for(h2 in Place.places()) async{
				val temp1 = at(h2){
					managers().mr.getDriverMsgQ(pid as Int)
				};

				val temp2 = at(h2){
					managers().mr.getCitizenMsgQ(pid as Int)
				};
			
				managers().handleMessages(temp1);
				managers().handleMessages(temp2);
			}
		}
		finish for(h1 in Place.places()) at(h1) async{
			updateCitizens(managers().mr.getMoveCitizenMessages());
			for(h2 in Place.places()) {
				managers().mr.getDriverMsgQ(h2.id as Int).clear();
				managers().mr.getCitizenMsgQ(h2.id as Int).clear();
			}
		}
	}
	
	protected def updateCitizens(sml: XMessageList){
		while(!sml.isEmpty()){
			sm : XMessage = sml.removeFirst();
			switch(sm.getType()){
				case CITIZEN_ADD:
					addCitizen(sm);
					break;
				case CITIZEN_MOVE:
					removeCitizen(sm.getCitizenID(), sm.getOrigRID() as Int);
					addCitizen(sm);
					break;
				case CITIZEN_REMOVE:
					removeCitizen(sm.getCitizenID(), sm.getOrigRID() as Int);
					break;
			}
		}
	}

	private def addCitizen(sm:XMessage){
		cid:Long = sm.getCitizenID();
		pid:Int = sm.getDestRID() as Int;
		citizenMap.put(cid, pid);
		at(Place(pid as Long)){
			managers().addCitizen(sm);
		}		
	}
	
	private def removeCitizen(cid:Long, pid:Int){
		citizenMap.remove(cid);
		at(Place(pid as Long)){
			managers().removeCitizen(cid);
		}
	}
}
