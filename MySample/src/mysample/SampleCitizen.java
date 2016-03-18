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

package mysample;

import mysample.message.BroadCastMessage;
import mysample.message.DirectionMessage;
import mysample.message.IndividualMessage;
import mysample.message.MutualMessage;
import mysample.message.SampleMessage;
import mysample.message.SetAttributeMessage;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.citizen.Citizen;
import com.ibm.xasdi_bridge.citizen.CitizenSet;
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
		CitizenSet set = Region.getCitizenSet(0);
		set.add(getID());
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
			reply = individual((IndividualMessage)msg); break;
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
		IndividualMessage reply = new IndividualMessage(1);
		int value = attribute;
		reply.selected = value;
		this.writeLog("I have received an individual message", value);
		return reply;
	}
	
	private BroadCastMessage broadCast(BroadCastMessage msg){
		BroadCastMessage reply = new BroadCastMessage(msg.getGroupID());
		int value = BroadCastMessage.VALUE;
		this.writeLog("I have received a broadcast message", value);
		return reply;
	}
	
	private void attribute(SetAttributeMessage msg){
		this.attribute = msg.attribute;
		this.writeLog("I have received a setattribute message", attribute);
	}
	
	private void direction(DirectionMessage msg){
		int cid = (int)msg.getReceiverID().getLocalID();
		this.writeLog("I have received a direction message", cid);
	}
	
	private DirectionMessage mutual(MutualMessage msg){
		int value = MutualMessage.VALUE;
		this.attribute = value;
		this.writeLog("I have received a mutual message", value);
		CitizenID dest = msg.getSenderID();
		DirectionMessage reply = new DirectionMessage(dest);
		return reply;
	}

	private void writeLog(String str, int value){
		try{
			Logger logger = World.world().getLogger();
			Log log = logger.getFreeLog(0);
			log.setInt(0, value);
			log.setString(1, str);
			log.write();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}

