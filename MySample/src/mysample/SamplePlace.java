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

package mysample;

import com.ibm.xasdi_bridge.simulator.Place;
import com.ibm.xasdi_bridge.simulator.Region;

public class SamplePlace extends Place {

	private static final long serialVersionUID = 1L;

	public SamplePlace(Region region, long id) {
		super(region, id);
	}
}

