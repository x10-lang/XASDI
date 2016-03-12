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

package com.ibm.xasdi_bridge.citizen;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Copy object field variables using deep-copy.
 */
public class CopyFields {
	
	/**
	 * Copy from object to another object using deep-copy.
	 * @param in Object to be read
	 * @param out Copied object
	 * @return Same as copied object
	 */
	public static Object setFields(Object in , Object out){
		Class<? extends Object> Incls = in.getClass();
		Class<? extends Object> Outcls = out.getClass();
		Field[] InFields = Incls.getDeclaredFields();
		Field[] OutFields = Outcls.getDeclaredFields();
		HashMap<String,Field> InFieldsMap = new HashMap<String,Field>();
		for( int i = 0 ; i < InFields.length ; i ++ ){
			InFields[i].setAccessible(true);
			InFieldsMap.put(InFields[i].getName(),InFields[i]);
		}
		
		for( Field f : OutFields ){
			f.setAccessible(true);
			if(InFieldsMap.containsKey(f.getName()) || f.getName() == "this$0" ){
				if(f.getName() == "this$0") continue;
				try {
					f.set(out,InFieldsMap.get(f.getName()).get(in));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}else{
				System.err.println("Fields mismatched");
				System.exit(0);
			}
		}
		return out;
	}
}
