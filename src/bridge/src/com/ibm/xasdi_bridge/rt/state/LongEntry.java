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

package com.ibm.xasdi_bridge.rt.state;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author yamamoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
class LongEntry extends Entry {
	long v;
	byte getType() {return StateImpl.LONG;}
	long getLong() {return v;}
	void setLong(long bb) {v = bb;}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(v);
	}
	
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		v = in.readLong();
	}
}
