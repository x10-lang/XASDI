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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mysample2.message.BroadCastMessage;
import mysample2.message.DirectionMessage;
import mysample2.message.IndividualMessage;
import mysample2.message.MutualMessage;
import mysample2.message.SetAttributeMessage;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.citizen.CitizenSet;
import com.ibm.xasdi_bridge.citizen.MessageResolver;
import com.ibm.xasdi_bridge.simulator.Region;

public class SampleResolver implements MessageResolver {

	@Override
	public Collection<CitizenID> resolve(Message msg) {
		if(msg instanceof BroadCastMessage){
			BroadCastMessage bmsg = (BroadCastMessage)msg;
			long pid = bmsg.getGroupID().getLocalID(); // confusion between Place ID (used for Broadcast) and X10 Place ID (used for Citizenset)
			CitizenSet cset = Region.getCitizenSet(pid);
			if(cset == null){
				System.err.println("CitizenSet for BroadCast is null: placeid="+pid);
				return null;
			}else{
				return cset.getCollection();
			}
		}else if(msg instanceof IndividualMessage){
			List<CitizenID> cset = new ArrayList<CitizenID>();
			cset.add(msg.getSenderID());
			return cset;
		}else if(msg instanceof DirectionMessage){
			List<CitizenID> citizenSet = new ArrayList<CitizenID>();
			citizenSet.add(((DirectionMessage)msg).getReceiverID());
			return citizenSet;
		}else if(msg instanceof SetAttributeMessage){
			List<CitizenID> cset = new ArrayList<CitizenID>();
			cset.add(msg.getSenderID());
			return cset;
		}else if(msg instanceof MutualMessage){
			List<CitizenID> citizenSet = new ArrayList<CitizenID>();
			citizenSet.add(((MutualMessage)msg).getReceiverID());
			return citizenSet;
		}else{
			System.err.println("Message is null or unsupported.");
			return null;
		}
	}
}
