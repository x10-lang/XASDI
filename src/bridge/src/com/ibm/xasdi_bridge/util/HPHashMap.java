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

/**
 * @author yamamoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HPHashMap {
	static class Bucket {
		Entry[] values;
	}
	
	static class Entry {
		Object key;
		Object value;
		Entry(Object key, Object value) {
			this.key = key;
			this.value = value;
		}
	}
	
	Bucket[] buckets;
	
	public HPHashMap(int size) {
		buckets = new Bucket[size];
		for(int i=0;i<size;i++) {
			buckets[i] = new Bucket();
		}
	}
	
	public HPHashMap() {
		this(1000);
	}
	
	public void put(Object key, Object value) {
		int h = key.hashCode();
		int i = h%buckets.length;
		if (i<0) i+=buckets.length;
		Bucket b = buckets[i];
		synchronized(b) {
			if (b.values == null) {
				b.values = new Entry[1];
				b.values[0] = new Entry(key,value);
			} else {
				Entry[] e = new Entry[b.values.length+1];
				System.arraycopy(b.values,0,e,0,b.values.length);
				e[b.values.length] = new Entry(key,value);
				b.values = e;
			}
		}
	}
	
	public Object get(Object key) {
		int h = key.hashCode();
		int i = h%buckets.length;
		if (i<0) i+=buckets.length;
		Bucket b = buckets[i];
		synchronized(b) {
			if (b.values == null) {
				return null;
			}
			for(int j=0;j<b.values.length;j++) {
				if (b.values[j].key == key || b.values[j].key.equals(key)) {
					return b.values[j].value;
				}
			}
		}
		return null;
	}
	
	public Object remove(Object key) {
		int h = key.hashCode();
		int i = h%buckets.length;
		Bucket b = buckets[i];
		synchronized(b) {
			for(int j=0;j<b.values.length;j++) {
				if (b.values[j].key == key || b.values[j].key.equals(key)) {
					Object ret = b.values[j].value;
					if (b.values.length > 1) {
						Entry[] e = new Entry[b.values.length-1];
						int c = 0;
						for(int k=0;k<b.values.length;k++) {
							if (j != k) {
								e[c] = b.values[k];
								c++;
							}
						}
						b.values = e;
					} else {
						b.values = null;
					}
					return ret;
				}
			}
		}
		return null;
	}
}
