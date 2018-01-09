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

package auction.report;

import auction.sim.AuctionLogs;

public class AuctionLogConverter {
	
	public static void main(String[] args) {
		
		AuctionLogConverter converter = new AuctionLogConverter();
		converter.convert(args);
	}
	
	public void convert(String[] args) {
		AuctionLogs logs = new AuctionLogs();
		logs.covertFileset(args);
	}

}
