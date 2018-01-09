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

package mysample2;

import mysample2.message.BroadCastMessage;
import mysample2.message.DirectionMessage;
import mysample2.message.IndividualMessage;
import mysample2.message.MutualMessage;
import mysample2.message.SampleMessage;
import mysample2.message.SetAttributeMessage;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.citizen.Citizen;
import com.ibm.xasdi_bridge.log.Log;
import com.ibm.xasdi_bridge.log.Logger;
import com.ibm.xasdi_bridge.simulator.Region;
import com.ibm.xasdi_bridge.simulator.World;

public class SampleCitizen extends Citizen {

	int attribute;
	
	public SampleCitizen(CitizenID cid) {
		super(cid);
	}

	@Override
	public void onCreation(Object arg) {
		attribute = 0;
	}

	@Override
	public void onExecute(long time, int phase) {
		// do nothing
	}

	
	@Override
	public void onActivation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeactivating() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisposing() {
		// TODO Auto-generated method stub

	}

	@Override
	public Message onMessage(Message msg) {
		SampleMessage reply = (SampleMessage)msg;
		switch(msg.getType()){
			case SampleMessage.INDIVIDUAL:
			reply = individual((IndividualMessage)msg); break; // reply will not be sent because message does not have sender ID (see Resolver)
			case SampleMessage.BROADCAST:
			reply = broadCast((BroadCastMessage)msg); break;
			case SampleMessage.ATTRIBUTE:
			reply = null; attribute((SetAttributeMessage)msg); break;
			case SampleMessage.DIRECTION:
			reply = null; direction((DirectionMessage)msg); break;
			case SampleMessage.MUTUAL:
			reply = mutual((MutualMessage)msg); break;
		}
		return reply;
	}
	
	private IndividualMessage individual(IndividualMessage msg){
		long time = msg.getTime();
		IndividualMessage reply = new IndividualMessage(1);
		reply.setTime(time);
		int value = attribute;
		reply.selected = value;
		this.writeLog("individual message "+value, (int) time);
		return reply;
	}
	
	private DirectionMessage broadCast(BroadCastMessage msg){
		DirectionMessage reply = new DirectionMessage(msg.getSenderID());
		reply.setSenderID(cid);
		long time = msg.getTime();
		long sid = msg.getSenderID().getLocalID();
		this.writeLog("broadcast message from "+sid, (int) time);
		reply.setTime(time);
		return reply;
	}
	
	private void attribute(SetAttributeMessage msg){
		long time = msg.getTime();
		this.attribute = msg.attribute;
		this.writeLog("setattribute message "+ attribute, (int) time);
	}
	
	private void direction(DirectionMessage msg){
		long time = msg.getTime();
		int cid = (int)msg.getReceiverID().getLocalID();
		int sid = (int)msg.getSenderID().getLocalID();
		this.writeLog("direction message from "+sid, (int) time);
	}
	
	private DirectionMessage mutual(MutualMessage msg){
		long time = msg.getTime();
		int value = MutualMessage.VALUE;
		this.attribute = value;
		this.writeLog("mutual message "+ value, (int) time);
		CitizenID dest = msg.getSenderID();
		DirectionMessage reply = new DirectionMessage(dest);
		reply.setTime(time);
		return reply;
	}

	private void writeLog(String str, int value){
		if (true)
		{
			try{
				Logger logger = World.world().getLogger();
				Log log = logger.getFreeLog(0);
				log.setInt(0, value);
				log.setString(1, cid.getLocalID()+","+str);
				log.write();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}


}

