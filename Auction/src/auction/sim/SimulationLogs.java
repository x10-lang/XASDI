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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import com.ibm.xasdi_bridge.log.DefaultLogger;
import com.ibm.xasdi_bridge.log.Log;
import com.ibm.xasdi_bridge.log.LogDefinition;
import com.ibm.xasdi_bridge.log.LogSet;
import com.ibm.xasdi_bridge.log.Logger;
import com.ibm.xasdi_bridge.simulator.World;

import auction.agent.BidderProxy;

public class SimulationLogs {
	
	protected static int SAMPLING_INTERVAL = 3600;
	protected HashMap<Integer, LogPack> logmap = new HashMap<Integer, LogPack>();
	
	BidLog bidlog;
	
	/**
	 * Construct Logs with region ID and file name
	 * @param regionid region ID
	 * @param fileName file name
	 */
	public SimulationLogs(long regionid, String fileName) {
		this(regionid, new File(fileName));
	}

	/**
	 * Construct Logs with region ID and File object
	 * @param regionid region ID
	 * @param file File
	 */
	public SimulationLogs(long regionid, File file) {
		Logger logger = World.world().setLogger(new DefaultLogger());
		logger.setFile(file);
	}
	
	/**
	 * Default Constructor for Converter
	 */
	public SimulationLogs() {
		
	}
	
	/**
	 * Set interval of logging
	 * @param SAMPLING_INTERVAL interval of logging
	 */
	public void setSAMPLING_INTERVAL(int sAMPLING_INTERVAL) {
		SAMPLING_INTERVAL = sAMPLING_INTERVAL;
	}

	/**
	 * Return an interval of logging
	 * @return sampling interval
	 */
	public int getSAMPLING_INTERVAL() {
		return SAMPLING_INTERVAL;
	}


	public void setCategory(int c) {
		LogPack logpack = logmap.get(c);
		logpack.enableLog();
	}

	
	public void prepareForConvert(String filename) {
		for (LogPack logpack: logmap.values()) {
			logpack.prepareForConvert(filename);
		}
	}
	
	public void convert(String filename) {
		try {	
			LogSet logSet = LogSet.read(filename);
			while(logSet.hasNext()) {
				Log rec = logSet.next();
				if(rec == null) break;
				LogDefinition def = rec.getLogDefinition();
				int id = def.getID();
				LogPack logpack = logmap.get(id);
				logpack.convert(rec);
			}
			
			for (LogPack logpack: logmap.values()) {
				logpack.close();
			}

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void covertFileset(String[] filenames) {
		List<String> logs = new ArrayList<String>();
		final String suffix = ".log";
		int i = 0;
		for(String fname : filenames) {
			File file = new File(fname);
			if(file.isFile()) {
				if(fname.endsWith(suffix)) {
					logs.add(fname);
					System.out.println((i++) + " converting the file: " + fname);
					convertOne(fname);
				}
			}else if(file.isDirectory()){
				String[] list = file.list();
				for(String e : list) {
					System.out.println(e);
					if(e.endsWith(suffix)){
						System.out.println(suffix);
						String f = fname + '/' + e;
						logs.add(f);
						System.out.println((i++) + " converting the file: " + f);
						convertOne(f);
					}
				}
			}
		}
		
		concatWithoutStream(logs.toArray(new String[0]));
	}
	
	public void convertOne(String filename) {
		try {
			prepareForConvert(filename);
			convert(filename);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void concat(String defFile, String[] files, String suffix){
		BufferedOutputStream bos = null;
		int ret = -1;
		int i = 1;
		int size = files.length;
		
		for(String fileName: files){
			if(bos==null){
				 File file = new File(defFile+suffix);
				 try {
					 file.createNewFile();
					 FileOutputStream fos = new FileOutputStream(file);
					 bos = new BufferedOutputStream(fos);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(i+"/"+size+" concatinating"+suffix+" "+fileName);
			String riFile = fileName+suffix;
			File file = new File(riFile);
			try {
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				while((ret=bis.read())>0){
					bos.write(ret);
				}
				bis.close();
				fis.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		try {
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void concatWithoutStream(String[] files) {
		StringTokenizer str = new StringTokenizer(files[0],"_");
		ArrayList <String> list = new ArrayList<String>(); 
		while(str.hasMoreTokens()) {
			list.add(str.nextToken());
		}
		if(list.size()!=4) {
			System.err.println("Illegal file name: "+str);
			System.exit(1);
		}
		String defFile 	= list.get(0);
		for (LogPack logpack: logmap.values()) {
			concat(defFile,files, logpack.getSuffix());
		}
		
	}

}
