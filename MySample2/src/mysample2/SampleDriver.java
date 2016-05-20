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

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

import mysample2.message.BroadCastMessage;
import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.message.MessageList;
import com.ibm.xasdi_bridge.simulator.CitizenProxy;
import com.ibm.xasdi_bridge.simulator.Driver;
import com.ibm.xasdi_bridge.simulator.Region;
import com.ibm.xasdi_bridge.simulator.World;

public class SampleDriver implements Driver {

	Long id;
	SamplePlace place;
	SampleRegion region ;
	long numCitizen = 5;
	SampleCitizenProxy market = null;
	SampleCitizenProxy mover = null;
	
	public SampleDriver(Long id, SamplePlace place, SampleRegion region) {
		this.id = id;
		this.place = place;
		this.region = region;
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
	public void execute(long time, int phase)
	{
 		switch (phase) {
		case 0: messagesample(time);
			break;
		case 1: movesample(time);
			break;
		}
	
	}
	
	public void messagesample(long time)
	{
		HashMap<Long, CitizenProxy> proxies = region.getCitizenProxies();
		if (time == 0)
		{
			long rid = region.getID();
			for (long lid = 0; lid < numCitizen; lid++)
			{
				int args = 0;
				CitizenID cid = new CitizenID();
				SampleCitizenProxy proxy = (SampleCitizenProxy) region.createCitizen(SampleRegion.FACTORYID, cid, args);
				mover = proxy;
				if (lid == 0) market = proxy;
				if (lid % 100000 == 0)
				{
					System.out.println("Place "+rid+" citizens "+lid);
					outMemoryStatus();
				}
			}
			int mid = (int) mover.getLocalID();
			mover.setModel(rid * 100.0 + mid/10.0, mid);
			System.out.println("Mover at "+rid+" "+mover.getModel().toString());
		}
		else if (time == 1)
		{
			for (Iterator<Map.Entry<Long, CitizenProxy>> it = proxies.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry<Long, CitizenProxy> entry = it.next();
				SampleCitizenProxy proxy = (SampleCitizenProxy) (entry.getValue());
				proxy.setAttribute(proxy.getID(), (int) (entry.getKey().longValue()), time);
			}
		}
		else if (time == 2)
		{
			for (Iterator<CitizenProxy> it = proxies.values().iterator(); it.hasNext();)
			{
				SampleCitizenProxy proxy = (SampleCitizenProxy) (it.next());
				proxy.individual(proxy.getID(), time);
			}
		}
		else if (time >= 1)
		{
			BroadCastMessage msg = new BroadCastMessage(time); // not use PlaceID
			msg.setSenderID(market.getID());
			region.sendBroadcastMessage(msg); // use addBroadcast in Messagerepository
		}
		
		if (time >= 1)
		{
			System.out.println("RID "+region.getID()+", DriverPlaceMap: "+World.world().getDriverPlaceMap().toString());
			System.out.println("RID "+region.getID()+", CitizenPlaceMap: "+World.world().getCitizenPlaceMap().toString());
		}
	}
	
	public void movesample(long time)
	{
		if (time == 3)
		{
			if (mover != null)
			{
				int origID = World.world().getPlaceID();
				long destID = World.getnPlaces() - 1 - origID; // destination X10PlaceID
				Message mvm = new Message();
				mvm.setMovable(mover, destID);
				System.out.println("Move citizen "+mover.getLocalID()+" from "+origID+" to "+destID);
				if (region.sendMessage(mvm) != null)
				{
					region.removeCitizen(mover.getLocalID());
				}
				
			}
		}
	}
	

	@Override
	public void execute(long time) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return id;
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

	public long getNumCitizen()
	{
		return numCitizen;
	}

	public void setNumCitizen(long numCitizen)
	{
		this.numCitizen = numCitizen;
	}
	
	protected static void outMemoryStatus()
	{
		Runtime runtime = Runtime.getRuntime();
		long max = runtime.maxMemory();
		long total = runtime.totalMemory();
		long free = runtime.freeMemory();
		long used = total - free;
		System.out.println("MaxMemory: " + max);
		System.out.println("TotalMemory: " + total);
		System.out.println("FreeMemory: " + free);
		System.out.println("UsedMemory: " + used);
	}

}
