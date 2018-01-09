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

package com.ibm.xasdi_bridge.message;

import java.util.*;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;

/**
 * Small unit of Message repository.
 */
@SuppressWarnings("serial")
public class MessageQueue implements java.io.Serializable{
	
	private int pid;								//	destination X10 PlaceID	
	private HashMap<Long, MessageList> msgs;		//	hashmap of PhysicalCitizenID and  Message
		
	/**
	 * Constructor using CitizenID
	 * @param cids	Set of all CitizenID in this X10 Place
	 * @param pid	X10 Place ID
	 */
	public MessageQueue(Set<Long> cids, int pid){
		this.pid = pid;
		msgs = new HashMap<Long, MessageList>();
	}

	/**
	 * Add a message to repository. 
	 * @param dest	destination CitizenID
	 * @param msg	Message object to be sent
	 */
	synchronized public void addMessage(long dest, Message msg){
		if(msg != null){
			if(msgs.get(dest) == null){		//	CitizenProxy might not be assigned
				msgs.put(dest, new MessageList());
			}
			msgs.get(dest).add(msg);
		}
	}
	
	/**
	 * Get size of the MessageQueue (the number of destination).
	 * @return size of the MessageQueue
	 */
	public int size(){
		return msgs.size();
	}
	
	/**
	 * Get whether this repository is empty.
	 * @return If this repository has no messages, return true.
	 */
	public boolean isEmpty(){
		return msgs.isEmpty();
	}
	
	/**
	 * Get entries of destination agent ID and list of messages.
	 * @return HashMap about messages (Key is destination agent ID, Value is list of messages)
	 */
	public HashMap<Long, MessageList> getMsgs(){
		return msgs;
	}
	
	/**
	 * Append another MessageQueue content to this.
	 * @param msgq	MessageQueue from another X10 Place
	 */
	public void append(MessageQueue msgq){
		if(msgq == null)return;		//	argument might be null
		for(Map.Entry<Long, MessageList> amsg : msgq.getMsgs().entrySet()){
			long cid = amsg.getKey();
			if(msgs.containsKey(cid)){
				msgs.get(cid).addAll(amsg.getValue());
			}
		}
	}
	
	/**
	 * Add one Message to be sent to some Citizens.  
	 * @param dests	Set of the destinations sent this message
	 * @param msg	Message object to be sent
	 */
	public void addMessage(Set<CitizenID> dests, Message msg){
		for(CitizenID cid : dests){
			msgs.get(cid.getLocalID()).add(msg);
		}
	}
	
	/**
	 * Add one Message to be sent to all Citizens.
	 * @param msg	Message object to be sent
	 */
	public void addBroadcastMessage(Message msg){
		for(MessageList msglist : msgs.values()){
			msglist.add(msg);
		}
	}
	
	/**
	 * Clear whole of this repository.
	 */
	public void clear(){
		synchronized(msgs){
			msgs.clear();
		}
	}
	
	/**
	 * Get a list of messages sent to an agent.
	 * @param aid ID of agent
	 * @return list of messages sent to agent
	 */
	public MessageList getMessages(long aid){
		return msgs.get(aid);
	}
	
	/**
	 * Get destination X10 Place ID bound to this repository.
	 * @return X10 Place ID
	 */
	public int getPID(){
		return pid;
	}
}

