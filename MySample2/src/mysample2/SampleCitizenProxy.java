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

package mysample2;

import mysample2.message.DirectionMessage;
import mysample2.message.IndividualMessage;
import mysample2.message.MutualMessage;
import mysample2.message.SetAttributeMessage;
import mysample2.model.SampleModel;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.citizen.Movable;
import com.ibm.xasdi_bridge.citizen.MovableFields;
import com.ibm.xasdi_bridge.simulator.CitizenProxy;
import com.ibm.xasdi_bridge.simulator.Region;

public class SampleCitizenProxy extends CitizenProxy implements Movable {

	/**
	 * 
	 */

	/*
	 * By having this class, this fields are serialized into byte stream with deep copy
	 * and then de-serialized it into a vehicle object again with XASDI layer automatically
	 */
	private class MoveFields implements MovableFields
	{	
		/**
		 * 
		 */
		private int attribute;
		private SampleModel model;
	}

	int attribute;
	SampleModel model;
	
	private MovableFields movefields = new MoveFields();
	
	public SampleCitizenProxy(Region region) {
		super(region);
		model = new SampleModel();
	}
	
	public void setModel(double val, int cnt)
	{
		model.setValue(val, cnt);
	}
	
	public SampleModel getModel()
	{
		return model;
	}
	
	public void individual(CitizenID id, long time){
		IndividualMessage msg = new IndividualMessage(0);
		msg.setSenderID(id);
		msg.setTime(time);
		sendMessage(msg);
	}
	
	public void setAttribute(CitizenID id ,int i, long time){
		SetAttributeMessage msg = new SetAttributeMessage(i);
		msg.setSenderID(id);
		msg.setTime(time);
		sendMessage(msg);
		attribute = i;
	}
	
	public void direct(CitizenID dest, DirectionMessage msg){
		msg.setReceiverID(dest);
		sendMessage(msg);
	}

	public void mutual(MutualMessage msg){
		sendMessage(msg);
	}

	@Override
	public void receiveMessage(Message msg) {
		Message reply = citizen.onMessage(msg);
		sendMessage(reply);
	}

	@Override
	public boolean shouldBeSent(long arg0) {
		return false;	
	}

	
	@Override
	public Object getMovableFields() {
		return movefields;
	}

	@Override
	public long getNextDriverID() {
		return 0;
	}

	@Override
	public long getObjectID() {
		return id.getLocalID();
	}
}
