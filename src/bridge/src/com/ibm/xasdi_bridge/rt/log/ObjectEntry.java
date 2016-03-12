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

package com.ibm.xasdi_bridge.rt.log;

import java.io.IOException;
import java.io.ObjectOutput;

/**
 * @author yamamoto
 *
 */
class ObjectEntry extends Entry {
	Object v;
	
	void clear() {
		v = null;
	}
	
	void setObject(Object bb) {
		v = bb;
	}
	
	Object getObject() {
		return v;
	}
	
	void write(ObjectOutput out) throws IOException  {
		
	}
	
	int getLength() {
		return 0;
	}
}