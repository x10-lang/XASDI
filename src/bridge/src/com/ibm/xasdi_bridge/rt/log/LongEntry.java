/*
 *  This file is part of the XASDI project (https://github.com/x10-lang/XASDI).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2016.
 */

package com.ibm.xasdi_bridge.rt.log;

import java.io.IOException;
import java.io.ObjectOutput;

/**
 * @author yamamoto
 *
 */
class LongEntry extends Entry {
	long v;
	
	void clear() {
		v = 0;
	}
	
	void setLong(long bb) {
		v = bb;
	}
	
	long getLong() {
		return v;
	}
	
	void write(ObjectOutput out) throws IOException {
		out.writeLong(v);
	}
	
	int getLength() {
		return 8;
	}
}
