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

import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.citizen.CitizenFactory;
import com.ibm.xasdi_bridge.message.MessageQueue;
import com.ibm.xasdi_bridge.simulator.Region;
import com.ibm.xasdi_bridge.simulator.World;

public class SampleRegion extends Region {

	public static final Integer FACTORYID = new Integer(0);
	
	public SampleRegion() {
		super();
	}

	@Override
	protected CitizenFactory getCitizenFactory() {
		return super.getCitizenFactory(SampleRegion.FACTORYID);
	}

	@Override
	public void sendMsgQ(MessageQueue msgq) {
		// do nothing
	}
	
	@Override
	public SampleCitizenProxy restoreCitizen(Message sm)
	{
		SampleCitizenProxy scp = (SampleCitizenProxy)super.restoreCitizen(sm);
		System.out.println("Restore citizen "+scp.getLocalID()+" at "+World.world().getPlaceID());
	    scp = (SampleCitizenProxy)Region.restore(sm, scp);
	    System.out.println("RegionRestore citizen "+scp.getLocalID()+" at "+World.world().getPlaceID()+" with "+scp.getModel().toString());
	    return scp;
	}
}

