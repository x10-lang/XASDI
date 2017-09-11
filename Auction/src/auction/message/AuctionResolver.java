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

package auction.message;

import java.util.ArrayList;
import java.util.Collection;
import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.Message;
import com.ibm.xasdi_bridge.citizen.MessageResolver;


public class AuctionResolver implements MessageResolver {

	@Override
	public Collection<CitizenID> resolve(Message msg) {
		Collection<CitizenID> citizenSet = new ArrayList<CitizenID>();
		citizenSet.add(msg.getReceiverID());
		if(citizenSet.isEmpty())
			return null;
			
		return citizenSet;
	}

}
