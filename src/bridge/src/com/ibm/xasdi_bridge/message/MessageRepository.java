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

package com.ibm.xasdi_bridge.message;

import java.util.*;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.citizen.MessageResolver;
import com.ibm.xasdi_bridge.simulator.CitizenProxy;
import com.ibm.xasdi_bridge.simulator.Region;
import com.ibm.xasdi_bridge.simulator.World;

/**
 * Large unit of Message repository.
 */
public class MessageRepository {

	protected Region region;
	protected MessageResolver resolver;			//	field variable for MessageResolver
	private boolean sendReply = false;			//	whether sent messages should go to replyMsgQSet  
	
	protected Map<Integer, MessageQueue> citizenMsgQSet;	//	X10 Place, MessageQueue for CitizenProxy
	protected Map<Integer, MessageQueue> driverMsgQSet;		//	X10 Place, MessageQueue for Driver
	protected Map<Integer, MessageQueue> replyMsgQSet;		//	X10 Place, MessageQueue of reply
	protected MessageList moveCitizenList;					//	List of Message objects for moving agent
	
	protected ArrayList<Long> newCitizenSet;
	
	public static final int BROADCCASTCID = -1; // used for Broadcast message in X10 Agent Manager
	

	
	/**
	 * Create Repository object.
	 */
	public MessageRepository(){
		citizenMsgQSet = new HashMap<Integer, MessageQueue>();
		driverMsgQSet = new HashMap<Integer, MessageQueue>();
		replyMsgQSet = new HashMap<Integer, MessageQueue>();
		moveCitizenList = new MessageList();
		newCitizenSet = new ArrayList<Long>();
	}
	
	/**
	 * Create small unit of repository.
	 * This method should be called just after constructor.
	 * @param placenum The number of X10 Place
	 */
	public void setUp(int placenum){
		for(int i=0; i<placenum; i++){
			citizenMsgQSet.put(i, new MessageQueue(new HashSet<Long>(), i));
			driverMsgQSet.put(i, new MessageQueue(new HashSet<Long>(), i));
			replyMsgQSet.put(i, new MessageQueue(new HashSet<Long>(), i));
		}
	}
	
	/**
	 * Set the destination of Message. This method should be called only XASDI runtime.
	 * @param sendReply If true, Message will be sent to MessageQueue of reply. If false, for CitizenProxy.
	 */
	public void sendToReply(boolean sendReply){		//	for replyMsgQSet
		this.sendReply = sendReply;
		if(!sendReply){		//	Next received Messages will be sent to citizenMsgQSet
			for(Map.Entry<Integer, MessageQueue> msgq : replyMsgQSet.entrySet()){
				for(Map.Entry<Long, MessageList> msglst : msgq.getValue().getMsgs().entrySet()){
					for(Message msg : msglst.getValue()){
						citizenMsgQSet.get(msgq.getKey()).addMessage(msglst.getKey(), msg);
					}
					msglst.getValue().clear();
				}
			}
		}
	}
	
	/**
	 * Bind Region object to this repository.
	 * @param region Region owns this repository.
	 */
	public void setRegion(Region region){		//	for AuctionService
		this.region = region;
	}
	
	/**
	 * Bind MessageResolver to this repository.
	 * @param resolver MessageResolver bound to this repository.
	 */
	public void setResolver(MessageResolver resolver){
		this.resolver = resolver;
	}
	
	/**
	 * Get a MessageQueue for Citizen agents sent to a X10 Place.
	 * @param pid Destination X10 PlaceID
	 * @return MessageQueue sent to selected X10 Place
	 */
	public MessageQueue getCitizenMsgQ(int pid){
		return citizenMsgQSet.get(pid);
	}

	/**
	 * Get a MessageQueue for Driver agents sent to a X10 Place.
	 * @param pid Destination X10 PlaceID
	 * @return MessageQueue sent to selected X10 Place
	 */
	public MessageQueue getDriverMsgQ(int pid){
		return driverMsgQSet.get(pid);
	}
	
	/**
	 * Get a list of messages contain Movable data.
	 * @return MessageList contain Movable data
	 */
	public MessageList getMoveCitizenMessages(){
		MessageList tmp = (MessageList) moveCitizenList.clone() ;
		moveCitizenList.clear();
		return tmp;
	}
	
	/**
	 * Clear all messages for Citizen agent.
	 */
	public void clearMoveCitizenMessages(){
		moveCitizenList.clear();
	}
	
	/**
	 * Get a list of new citizen set created in this X10 Place.
	 * @return newCitizenSet
	 */
	public ArrayList<Long> getNewCitizenSet() {
		ArrayList<Long> tmp = (ArrayList<Long>) newCitizenSet.clone();
		newCitizenSet.clear();
		return tmp;
	}
	
	public int getNumNewCitizens() {
		return newCitizenSet.size();
	}
	
	public CitizenProxy getNewCitizenProxy(int index) {
		return region.getCitizenProxies().get(newCitizenSet.get(index));
	}
	
	public void clearNewCitizens() {
		newCitizenSet.clear();
	}
	

	/**
	 * Add a Message to this repository.
	 * @param msg Message to be added
	 * @return If this message is null or destination is null, return null. Otherwise, return Message to be added.
	 */
	public Message addMessage(Message msg) {		//	boolean -> Message (xasdi_bridge)
		if(msg == null) return null;
		
		//	for moving CitizenProxy
		int type = msg.getType();
		if(type == Message.CITIZEN_ADD || type == Message.CITIZEN_MOVE || type == Message.CITIZEN_REMOVE){
			if(msg.getDestRegionID() != World.world().getPlaceID()){
				moveCitizenList.add(msg);
				return msg;
			}
			return null;
		}

		Collection<CitizenID> citizens = resolver.resolve(msg);
		if(citizens == null) return null; //TODO: manage special message without resolver
		for(CitizenID cid : citizens){
			if(cid == null) continue;
			long lid = cid.getLocalID();
			if(sendReply){				//	MessageQueue for reply
				replyMsgQSet.get(World.world().getCitizenPlaceID(lid)).addMessage(lid, msg);
			}else{
				citizenMsgQSet.get(World.world().getCitizenPlaceID(lid)).addMessage(lid, msg);
			}
		}
		return msg;
	}

	/**
	 * add CitizenID to World.
	 * @param cid ID of Citizen to be added
	 * @param pid X10 Place ID
	 */
	public synchronized void addCitizen(long cid, int pid){
		World.world().getCitizenPlaceMap().put(cid, pid);
		newCitizenSet.add(cid);
	}
	/**
	 * remove CitizenID from World.
	 * @param cid ID of Citizen to be removed
	 */
	public synchronized void removeCitizen(long cid){
		if(World.world().containsCitizen(cid))World.world().getCitizenPlaceMap().remove(cid);
	}

	/**
	 * Add message to the repository. Destination CitizenID is set manually (without MessageResolver)
	 * @param cid destination CitizenID
	 * @param msg Message object to be sent
	 * @return the same Message object
	 */
	public Message addMessage(CitizenID cid, Message msg) {				//	CitizenID should be set here (from xasdi_bridge)
		if(cid == null || msg == null)return null;
		long lid = cid.getLocalID();
		citizenMsgQSet.get(World.world().getCitizenPlaceID(lid)).addMessage(lid, msg);
		return msg;
	}
	
	/**
	 * Add message to the repository for all X10 Places. Destination is set for broadcat (without MessageResolver)
	 * @param msg Message object to be sent
	 * @return the same Message object
	 */
	public Message addBroadcastMessage(Message msg) {
		
		for (MessageQueue msgq: citizenMsgQSet.values())
		{
			msgq.addMessage(BROADCCASTCID, msg);
		}
		return msg;
	}
	
	/**
	 * Called when receiving list of messages from a different X10 Place to register remote citizen ID.
	 * @param msgs List of messages
	 */
	public void checkRemoteMessages(MessageQueue msgq, int remotepid){
		if (msgq != null){
			HashMap<Long, MessageList> msgmap = msgq.getMsgs();
			for (MessageList msgs : msgmap.values()){
				for(Message msg : msgs){
					CitizenID cid = msg.getSenderID();
					World.world().setCitizenPlaceID(cid.getLocalID(), remotepid);
				}
			}
		}
	}
}

