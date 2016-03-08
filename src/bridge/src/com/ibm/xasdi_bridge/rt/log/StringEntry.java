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
class StringEntry extends Entry {
	static final String CHARSET = "UTF-8";
	String v;
	byte[] b;
	void clear() {
		v = null;
	}
	
	void setString(String bb) {
		v = bb;
		b = null;
		try {
			b = v.getBytes(CHARSET);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	String getString() {
		return v;
	}
	
	void write(ObjectOutput out) throws IOException {
		if (b == null) {
			out.writeInt(0);
		} else {
			out.writeInt(b.length);
			out.write(b);
		}
	}
	
	int getLength() {
		if (b == null) return 0;
		return b.length;
	}
}
