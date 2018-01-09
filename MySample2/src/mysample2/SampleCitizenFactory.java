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

import com.ibm.xasdi_bridge.CitizenID;
import com.ibm.xasdi_bridge.citizen.Citizen;
import com.ibm.xasdi_bridge.citizen.CitizenFactory;

public class SampleCitizenFactory implements CitizenFactory {

	@Override
	public Citizen newInstance(CitizenID id) {
		return new SampleCitizen(id);
	}
}
