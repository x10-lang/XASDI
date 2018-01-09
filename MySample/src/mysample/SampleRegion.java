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

package mysample;

import com.ibm.xasdi_bridge.citizen.CitizenFactory;
import com.ibm.xasdi_bridge.message.MessageQueue;
import com.ibm.xasdi_bridge.simulator.Region;

public class SampleRegion extends Region {

	public static final Integer FACTORYID = new Integer(0);
	
	public SampleRegion(long id) {
		super(id);
	}

	@Override
	protected CitizenFactory getCitizenFactory() {
		return super.getCitizenFactory(SampleRegion.FACTORYID);
	}

	@Override
	public void sendMsgQ(MessageQueue msgq) {
		// do nothing
	}
}

