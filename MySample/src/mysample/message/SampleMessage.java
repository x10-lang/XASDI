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

package mysample.message;

import com.ibm.xasdi_bridge.Message;

public class SampleMessage extends Message {
	
	private static final long serialVersionUID = 1L;
	
	public static final int INDIVIDUAL = 1;
	public static final int BROADCAST = 2;
	public static final int ATTRIBUTE = 3;
	public static final int DIRECTION = 4;
	public static final int MUTUAL = 5;

	public SampleMessage(int type) {
		super(type);
	}
}
