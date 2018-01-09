/*
 *  This file is part of the XASDI project (http://x10-lang.org/xasdi/).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014-2018.
 */

package mysample2;

import com.ibm.xasdi_bridge.simulator.CitizenProxy;
import com.ibm.xasdi_bridge.simulator.CitizenProxyFactory;
import com.ibm.xasdi_bridge.simulator.Region;

public class SampleCitizenProxyFactory implements CitizenProxyFactory {

	@Override
	public CitizenProxy newInstance(Region region) {
		return new SampleCitizenProxy(region);
	}
}
