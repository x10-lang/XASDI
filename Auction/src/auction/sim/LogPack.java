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

package auction.sim;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.ibm.xasdi_bridge.log.Log;
import com.ibm.xasdi_bridge.log.LogDefinition;
import com.ibm.xasdi_bridge.simulator.World;

public abstract class LogPack {
	protected int finfo = 0x01;
	protected String suffix = ".li.csv";
	protected PrintStream pf;
	protected FileOutputStream fos;
	protected LogDefinition logdef;
	protected long regionid;
	protected boolean useLog = false;
	
	/**
	 * Constructor for Logging
	 */
	public LogPack(int finfo, String suffix, long regionid) {
		this.finfo = finfo;
		this.suffix = suffix;
		this.regionid = regionid;
	}
	
	/**
	 * Constructor for Converter
	 */
	public LogPack(int finfo, String suffix) {
		this.finfo = finfo;
		this.suffix = suffix;
	}
	
	public LogDefinition prepareDefLog() {
		if (logdef == null) {
			logdef = new LogDefinition(finfo, regionid);
		}
		return logdef;
	}
	
	public abstract void deflog();
	
	public void addDefLog() {
		World.world().getLogger().addLogDefinition(logdef);
	}
	
	public void prepareForConvert(String filename) {
		try {
			fos = new FileOutputStream(filename + suffix, true);
			pf = new PrintStream(fos);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeString(StringBuilder line) {
		pf.println(line);
	}
	
	public void close() throws IOException {
		pf.close();
		fos.close();
	}

	public int getFinfo() {
		return finfo;
	}

	public String getSuffix() {
		return suffix;
	}

	public PrintStream getPf() {
		return pf;
	}

	public FileOutputStream getFos() {
		return fos;
	}

	public LogDefinition getLogdef() {
		return logdef;
	}

	public abstract void convert(Log rec);


	public void enableLog() {
		useLog = true;
	}
}
