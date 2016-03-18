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

package com.ibm.xasdi_bridge.rt;

import com.ibm.xasdi_bridge.rt.log.LogWriter;
import com.ibm.xasdi_bridge.simulator.Region;

public interface Simulator extends Runnable {		//	used by World

	LogWriter getLogger();
	
	Region getRegion();
}
