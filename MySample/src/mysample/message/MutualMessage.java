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

package mysample.message;

import com.ibm.xasdi_bridge.CitizenID;

public class MutualMessage extends SampleMessage {
	
	private static final long serialVersionUID = 1L;
	
	public static final int VALUE = 1732;
	
	public MutualMessage(CitizenID orig, CitizenID dest) {
		super(MUTUAL);
		this.setSenderID(orig);
		this.setReceiverID(dest);
	}
}
