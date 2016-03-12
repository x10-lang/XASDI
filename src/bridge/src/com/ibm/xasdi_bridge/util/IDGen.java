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

package com.ibm.xasdi_bridge.util;

public class IDGen {
	int itsCounter = 0;
	long itsCounterResetTime = 0;
	long itsBaseID = 0L;
	boolean itsBaseExist = false;

	public IDGen() {
	}

	public IDGen(int base) {
		itsBaseID = (long)base;
		itsBaseExist = true;
	}
	synchronized public long getID() {
		if (itsBaseExist) {
			return getID0();
		} else {
			return getID1();
		}
	}

	private long getID0() {
		long time = System.currentTimeMillis();

		if (itsCounter == 0 && itsCounterResetTime == time) {
			while(itsCounterResetTime == time) {
				try {
					Thread.sleep(1);
				} catch(Exception e) {
				}
				time = System.currentTimeMillis();
			}
		}

		long id = (time << 16) >> 8;
		id = id | ((itsBaseID & 0xFF) << 56) | (itsCounter & 0xFF);

		itsCounter++;
		if (itsCounter > 0xFF) {
			itsCounter = 0;
			itsCounterResetTime = time;
		}
		return id;
	}

	private long getID1() {
		long time = System.currentTimeMillis();

		if (itsCounter == 0 && itsCounterResetTime == time) {
			while(itsCounterResetTime == time) {
				try {
					Thread.sleep(1);
				} catch(Exception e) {
				}
				time = System.currentTimeMillis();
			}
		}

		long id = time << 16;
		id = id | (itsCounter & 0xFFFF);

		itsCounter++;
		if (itsCounter > 0xFFFF) {
			itsCounter = 0;
			itsCounterResetTime = time;
		}
		return id;
	}
}