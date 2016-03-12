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

package com.ibm.xasdi.runtime;

import x10.compiler.*;

@NativeRep("java", "com.ibm.xasdi_bridge.message.MessageRepository", null, null)
public class XMessageRepository {
	
	@Native("java", "#this.getCitizenMsgQ(#pid)")
	public final native def getCitizenMsgQ(pid:Int):XMessageQueue;

	@Native("java", "#this.getDriverMsgQ(#pid)")
	public final native def getDriverMsgQ(pid:Int):XMessageQueue;	
	
	@Native("java", "#this.getMoveCitizenMessages()")
	public final native def getMoveCitizenMessages():XMessageList;

	@Native("java", "#this.addCitizen(#cid, #pid)")
	public final native def addCitizen(cid:Long, pid:Int):void;		//	only id infomation
	
	@Native("java", "#this.removeCitizen(#cid)")
	public final native def removeCitizen(cid:Long):void;				//	only id infomation
	
	@Native("java", "#this.setUp(#placenum)")
	public final native def setUp(placenum:Int):void;
	
	@Native("java", "#this.clearMoveCitizenMessages()")
	public final native def clearMoveCitizenMessages():void;

	@Native("java", "#this.sendToReply(#sendReply)")
	public final native def sendToReply(sendReply:Boolean):void;		//	reply Messages

	@Native("java", "#this.getNumNewCitizens()")
	public final native def getNumNewCitizens():Int;
	
	@Native("java", "#this.getNewCitizenProxy(#index)")
	public final native def getNewCitizenProxy(index:Int):XCitizenProxy;

	@Native("java", "#this.clearNewCitizens()")
	public final native def clearNewCitizens():void;
	
	@Native("java", "#this.checkRemoteMessages(#msgs, #remotepid)")
	public final native def checkRemoteMessages(msgs:XMessageQueue, remotepid:Int):void;
}
