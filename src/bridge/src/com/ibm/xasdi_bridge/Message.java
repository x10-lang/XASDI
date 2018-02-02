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

package com.ibm.xasdi_bridge;

import java.io.*;
import java.util.HashMap;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.citizen.CopyFields;
import com.ibm.xasdi_bridge.citizen.Movable;
import com.ibm.xasdi_bridge.citizen.MovableFields;
import com.ibm.xasdi_bridge.citizen.ShadowMap;
import com.ibm.xasdi_bridge.simulator.Region;
import com.ibm.xasdi_bridge.simulator.World;

/**
 * Unit of data transferred between Citizens and Region objects.
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Sent to one agent and not expect reply.
	 */
	public static final int ONEWAY = 0;
	
	/**
	 * Sent to one agent and expect reply.
	 */
	public static final int REQUEST_RESPONSE = 1;
	
	/**
	 * Sent to several agents and not expect reply.
	 */
	public static final int MULTICAST_ONEWAY = 2;
    
	/**
	 * Sent to several agents and expect replies.
	 */
	public static final int MULTICAST_REQUEST_RESPONSE = 3;

	/**
	 * Used by XASDI when citizen object will be added.
	 */
	public static final int CITIZEN_ADD = 101;
	
	/**
	 * Used by XASDI when citizen object will be moved to another X10 Place.
	 */
	public static final int CITIZEN_MOVE = 100;
	
	/**
	 * Used by XASDI when citizen object will be removed.
	 */
	public static final int CITIZEN_REMOVE = 99;
	
	/**
	 * Used by XASDI when shadow agent information is set.
	 */
	public static final int SHADOW = 200;
	
	/**
	 * Represents this message is used for console output.
	 */
	public static final int CONSOLE = 1000;
	
	/**
	 * Represents error.
	 */
	public static final int ERROR = 9999;

	private int comm = -1;						//	type of the communication (ONEWAY, REQUEST_RESPONSE...)
	int type;									//	type of the message (CITIZEN_ADD, CONSOLE...)
	private long time = -1;						//	time when this message is sent 
	private long regionId = -1;					//	ID of the region to which the sender object belongs
	private long id = -1;						//	ID of the message
	private CitizenID senderID = null;			//	CitizenID of the sender Citizen
	private CitizenID receiverID = null;		//	CitizenID of the receiver Citizen
	private boolean packed = false;				//	whether all parameter are set

	private long destRegionID;		//	ID of the region to which the receiver object belongs
	private long citizenID;			//	Object ID (mainly CitizenID)
	private String text = null;		//	String for console output, or class name implements Movable
	private HashMap<String, Object> fields = new HashMap<String, Object>();		//	Field values of Movable object
	private ShadowMap shadow = null;  // Shadow  Agent Information
	
	/**
	 * Whether this message needs reply message.
	 */
	public boolean needReply;
	

	/**
	 * Default constructor especially used as sending to Region.
	 */
	public Message(){
	}
	

	/**
	 * Constructor used generally.
	 * @param type Type of the message.
	 */
	public Message(int type){
		this.type = type;
	}
	
	/**
	 * Set object for migration.
	 * @param m object to migrate
	 */
	public void setMovable(Movable m){
		type = CITIZEN_MOVE;
		citizenID = m.getObjectID();
		regionId = World.world().getCitizenPlaceID(citizenID);
		destRegionID = World.world().getDriverPlaceID(m.getNextDriverID());
		text = m.getClass().getName();
		fields = Region.setFields((MovableFields)CopyFields.setFields(m,m.getMovableFields()));	
	}
	
	/**
	 * Set object for migration.
	 * @param m object to migrate
	 * @param destID X10 Place ID of destination
	 */
	public void setMovable(Movable m, long destID){
		type = CITIZEN_MOVE;
		citizenID = m.getObjectID();
		regionId = World.world().getPlaceID();
		destRegionID = destID;
		text = m.getClass().getName();
		fields = Region.setFields((MovableFields)CopyFields.setFields(m,m.getMovableFields()));	
	}
	
	/**
	 * Get field variables in a agent to migrate.
	 * @return HashMap about field variables (Key is name, Value is value)
	 */
	public HashMap<String, Object> getFields(){
		return fields;
	}
	
	/**
	 * Get text data. If this message is used for agent migration, it represents Class name.
	 * @return Text data bound this message
	 */
	public String getText(){
		return text;
	}

	/**
	 * Get shadow agent data.
	 * @return Shadow data map
	 */
	public ShadowMap getShadow() {
		return shadow;
	}

	/**
	 * Set shadow agent data.
	 * @param shadow shadow data map
	 */
	public void setShadow(ShadowMap shadow) {
		type = SHADOW;
		this.shadow = shadow;
	}

	/**
	 * Get Region ID of message destination.
	 * @return If this message is used for agent removing, return -1. Otherwise, Region ID of destination.
	 */
	public long getDestRegionID(){
		if(type == CITIZEN_REMOVE){
			return -1;
		}else{
			return destRegionID;
		}
	}
	
	/**
	 * Get Region ID of message origin.
	 * @return If this message is used for agent adding, return -1. Otherwise, Region ID of origin.
	 */
	public long getOrigRegionID(){
		if(type == CITIZEN_ADD){
			return -1;
		}else{
			return regionId;
		}
	}
	
	/**
	 * Get ID of agent to migrate.
	 * @return Citizen ID as long type
	 */
	public long getCitizenID(){
		return citizenID;
	}

	/**
	 * Set ID of agent to migrate.
	 * @param cid Citizen ID as long type
	 */
	public void setCitizenID(long cid){
		this.citizenID = cid;
	}
	
	/**
	 * Get message type.
	 * @return Message type as int type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Get whether this message is sent from agent.
	 * @return If this message is sent from agent, return true.
	 */
	public boolean fromCitizen(){
		return senderID != null;		//	if senderID is null, this Message is from Region
	}
	
	/**
	 * Copy this message to a message 
	 * @param dest Copied message object
	 */
	public void copyTo(Message dest) {
		dest.comm = comm;
		dest.type = type;
		dest.time = time;
		dest.regionId = regionId;
		dest.id = id;
		dest.senderID = senderID;
	}

	/**
	 * Sets the simulation time as of the message is sent.
	 * @param time a simulation time
	 */
	public void setTime(long time) throws IllegalStateException {
		if (packed) {
			throw new IllegalStateException("already packed");
		}
		this.time =time;
	}

	
	/**
	 * Gets the simulation time when this message is sent.
	 * @return time
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Sets the communication type of this message.
	 * @param t Message communication type
	 * @throws IllegalStateException When all of parameters are already set
	 */
	public void setCommType(int t) throws IllegalStateException {
		if (packed) {
			throw new IllegalStateException("already packed");
		}
		comm = t;
	}
	
	/**
	 * Gets ID of the citizen which replied to the request message.
	 * Null will be returned when this message is not a reply message.
	 * @return citizen ID of the sender or null if this message is not a reply message.
	 */
	public CitizenID getSenderID() {
		return senderID;
	}

	/**
	 * If this message is reply message, set sender citizenID.
	 * @param cid sender CitizenID
	 */
	public void setSenderID(CitizenID cid){
		this.senderID = cid;
	}
	
	/**
	 * Gets ID of receiver Citizen.
	 * @return Receiver Citizen ID object
	 */
	public CitizenID getReceiverID(){
		return receiverID;
	}
	
	/**
	 * Sets ID of receiver Citizen.
	 * @param cid Receiver Citizen ID object
	 */
	public void setReceiverID(CitizenID cid){
		this.receiverID = cid;
	}
	
	/**
	 * Set number of this message.
	 * @param id ID of this object
	 * @throws IllegalStateException When all of parameters are already set
	 */
	public void setMessageID(long id) throws IllegalStateException {
		if (packed) {
			throw new IllegalStateException("already packed");
		}
		this.id = id;
	}
	
	/**
	 * Sets ID of the region to which the sender object belongs. 
	 * @param id a region ID
	 */
	public void setRegionID(long id) throws IllegalStateException {
		if (packed) {
			throw new IllegalStateException("already packed");
		}
		regionId = id;
	}

	/**
	 * Gets ID of Region to which sender object belongs.
	 * @return Region ID to which sender object belongs.
	 */
	public long getRegionID() {
		return regionId;
	}
	
	/**
	 * Called when all parameter are set.
	 */
	public void pack() {
		packed = true;
	}
	
	/**
	 * Reuse this object to be sent.
	 */
	public void reuse() {
		packed = false;
	}

}
