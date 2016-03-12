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

package com.ibm.xasdi_bridge.message;

import java.util.*;

import com.ibm.xasdi_bridge.Message;

/**
 *	Set of Message objects for an agent.
 *	It is used when agents are sent to another X10 Place.
 */
@SuppressWarnings("serial")
public class MessageList extends ArrayList<Message> implements java.io.Serializable{

	/**
	 * Constructor using ArrayList's one
	 */
	public MessageList(){
		super();
	}
}
