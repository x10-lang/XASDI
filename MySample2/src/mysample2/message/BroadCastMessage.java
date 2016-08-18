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

package mysample2.message;

import com.ibm.xasdi_bridge.PlaceID;

public class BroadCastMessage extends SampleMessage {
	
private static final long serialVersionUID = 1L;
	
	public static final int VALUE = 3141;
	private PlaceID id;

	public BroadCastMessage(long time) {
		super(BROADCAST);
		setTime(time);
	}
	
	public BroadCastMessage(PlaceID id) {
		super(BROADCAST);
		this.id = id;
	}
	
	public PlaceID getGroupID(){
		return id;
	}

}
