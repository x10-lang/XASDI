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

package com.ibm.xasdi_bridge.rt.log;

import java.io.IOException;
import java.io.ObjectOutput;

/**
 * @author yamamoto
 *
 */
class ShortEntry extends Entry {
	short v;
	void clear() {
		v = 0;
	}
	
	void setShort(short bb) {
		v = bb;
	}
	
	short getShort() {
		return v;
	}
	
	void write(ObjectOutput out) throws IOException {
		out.writeShort(v);
	}
	int getLength() {
		return 2;
	}
}
