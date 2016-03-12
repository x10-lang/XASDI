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

package com.ibm.xasdi_bridge.rt;

import com.ibm.xasdi_bridge.rt.log.LogWriter;
import com.ibm.xasdi_bridge.simulator.Region;

public class SimThread extends Thread{

	protected LogWriter logger = null;
	protected Region region;
	protected Object context = null;
	protected int threadID;
	protected long seqID = 0;
		
	public LogWriter getLogger() {		//	used by LogWriter
		return logger;
	}
	
	public Region getRegion() {		//	used by World
		return region;
	}
	
	public void setContext(Object o) {		//	used by World
		context = o;
	}
	
	public Object getContext() {	//	used by World
		return context;
	}
		
	public void close() {		//	used by World.RegionSimulator.run
		logger.close();
	}

}
