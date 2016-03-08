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

package mysample.report;

import java.io.*;
import com.ibm.xasdi_bridge.log.*;


public class SampleLogConverter {

	public static void main(String[] args) {
		System.out.println("now converting");
		
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("sample.csv"));
			File dir = new File(args[0]);
			File[] logs = dir.listFiles();		
			for(int i=0;i<logs.length;i++) {
				try {
					if(logs[i].isDirectory())continue;
					LogSet set = LogSet.read(logs[i].getAbsolutePath());
					while(set.hasNext()) {
						Log log = set.next();
						LogDefinition def = log.getLogDefinition();
						switch(def.getID()) {
							case 0:
								convert(pw,log);
								break;
							default:
								System.out.println("unknown");
								break;
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			pw.close();
		} catch(Exception ee) {
			ee.printStackTrace();
		}
	}
	
	static void convert(PrintWriter w, Log log) throws Exception {
		int value = log.getInt(0);
		String message = log.getString(1);
		
		w.println(message + "," + value);
	}
}
