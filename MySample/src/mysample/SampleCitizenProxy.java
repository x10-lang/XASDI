/*
 *  This file is part of the XASDI project (https://github.com/x10-lang/xasdi).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2016.
 */

package mysample;

import mysample.message.DirectionMessage;
import mysample.message.IndividualMessage;
import mysample.message.MutualMessage;
import mysample.message.SetAttributeMessage;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.simulator.CitizenProxy;
import com.ibm.xasdi_bridge.simulator.Region;

public class SampleCitizenProxy extends CitizenProxy {

	public SampleCitizenProxy(Region region) {
		super(region);
	}
	
	public void individual(CitizenID id){
		IndividualMessage msg =
			new IndividualMessage(0);
		msg.setSenderID(id);
		sendMessage(msg);
	}
	
	public void setAttribute(CitizenID id ,int i){
		SetAttributeMessage msg = new SetAttributeMessage(i);
		msg.setSenderID(id);
		sendMessage(msg);
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
		//	SampleCitizenProxy never migrates
	}
}
