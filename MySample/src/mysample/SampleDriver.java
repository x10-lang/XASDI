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

package mysample;

import java.util.HashMap;

import mysample.message.BroadCastMessage;
import mysample.message.MutualMessage;

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.message.MessageList;
import com.ibm.xasdi_bridge.simulator.CitizenProxy;
import com.ibm.xasdi_bridge.simulator.Driver;
import com.ibm.xasdi_bridge.simulator.Region;

public class SampleDriver implements Driver {

	SamplePlace place;
	SampleRegion region ;
	
	public SampleDriver(SamplePlace place, SampleRegion region) {
		this.place = place;
		this.region = region;
		for(int i=0; i<10; i++){
			int args = 0;
			CitizenID cid = new CitizenID(i);
			SampleCitizenProxy proxy = (SampleCitizenProxy)region.createCitizen(SampleRegion.FACTORYID, cid, args);
			if(proxy != null){
				Region.getCitizenSet(0).add(cid);
			}
		}
	}
	
	@Override
	public void begin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void begin(long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void complete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void complete(long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(long time) {
		HashMap<Long, CitizenProxy> proxies = region.getCitizenProxies();
		if(time == 0){
			for(long i=0; i<proxies.size(); i++){
				SampleCitizenProxy proxy = ((SampleCitizenProxy)proxies.get(i));
				proxy.setAttribute(proxy.getID() , (int) i);
			}
		}else if(time == 1){
			for(long i=0; i<proxies.size(); i++){
				SampleCitizenProxy proxy = (SampleCitizenProxy)proxies.get(i);
				proxy.individual(proxy.getID());
			}
		}else if(time == 2){
			BroadCastMessage msg = new BroadCastMessage(place.getID());
			region.sendMessage(msg);
		}else if(time == 3){
			for(long i=0; i<proxies.size(); i++){
				SampleCitizenProxy from = (SampleCitizenProxy)proxies.get(i);
				for(long j=0; j<proxies.size(); j++){
					SampleCitizenProxy to = (SampleCitizenProxy)proxies.get(j);
					MutualMessage msg = new MutualMessage(from.getID(), to.getID());
					from.mutual(msg);
				}
			}
		}
	}


	@Override
	public void execute(long arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onMessage(MessageList arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void prepare() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRegion(Region arg0) {
		// TODO Auto-generated method stub

	}

}
