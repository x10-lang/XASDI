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

import java.util.Iterator;

/**
 * @author yamamoto
 *
 */
public class IndexMap {
	static final int GROWSIZE=10;
		
	static class Entry {
		long id;
		Object v;
	}

	static class LocalIterator implements Iterator <Object>{
		IndexMap map;
		int count = 0;
		
		LocalIterator(IndexMap m) {
			map = m;
		}
		
		public boolean hasNext() {
			if (map.elements == null) {
				return false;
			}
			if (count < map.elements.length) {
				return true;
			}
			return false;
		}
		
		public Object next() {
			return map.elements[count++].v;
		}
		
		public void remove() {
			// not support
		}
	}
	
	Entry[][] index = null;
	int size;
	Entry[] elements = null;
	long currentID = -1;
	Object currentValue = null;
	
	public IndexMap(int n) {
		size = n;
		index = new Entry[n][];
	}
	public void put(int id, Object o) {
		put((long)id,o);
	}
	
	public synchronized void put(long id, Object o) {
		currentID = -1;
		currentValue = null;
		
		Entry e = new Entry();
		e.id = id;
		e.v = o;
		
		if (elements != null && elements.length*3/2 > size) {
			grow();
		}
		
		int row = (int)id%size;
		Entry[] array = index[row];
		if (array == null) {
			array = new Entry[1];
			array[0] = e;
			index[row] = array;
			addElement(id,o);
		} else {
			for(int i=0;i<array.length;i++) {
				if (array[i].id == id) {
					replaceElement(array[i].v,o);
					array[i].v = o;
					return;
				}
			}
			Entry[] ne = new Entry[array.length+1];
			System.arraycopy(array,0,ne,0,array.length);
			ne[array.length] = e;
			index[row] = ne;
			addElement(id,o);
		}
	}

	
	public Object get(int id) {
		return get((long)id);
	}
	
	public synchronized Object get(long id) {
		if (id == currentID) {
			return currentValue;
		}
		int row = (int)id%size;
		Entry[] array = index[row];
		if (array == null) {
			return null;
		}
		for(int i=0;i<array.length;i++) {
			if (array[i].id == id) {
				currentID = id;
				currentValue = array[i].v;
				return array[i].v;
			}
		}
		return null;
	}
	
	public Iterator <Object> iterator() {
		return new LocalIterator(this);
	}
	
	public int size() {
		if (elements == null) {
			return 0;
		}
		return elements.length;
	}
	
	public boolean containsKey(int id) {
		return containsKey((long)id);
	}
	
	public boolean containsKey(long id) {
		int row = (int)id%size;
		Entry[] array = index[row];
		if (array == null) {
			return false;
		}
		for(int i=0;i<array.length;i++) {
			if (array[i].id == id) {
				return true;
			}
		}
		return false;
	}
	
	private void addElement(long id, Object o) {
		if (elements == null) {
			elements = new Entry[1];
			Entry e = new Entry();
			e.id = id;
			e.v = o;
			elements[0] = e;
		} else {
			Entry[] a = new Entry[elements.length+1];
			System.arraycopy(elements,0,a,0,elements.length);
			Entry e = new Entry();
			e.id = id;
			e.v = o;
			a[elements.length] = e;
			elements = a;
		}
	}
	
	private void replaceElement(Object o, Object n) {
		for(int i=0;i<elements.length;i++) {
			if (elements[i].v == o) {
				elements[i].v = n;
			}
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i=0;i<elements.length;i++) {
			sb.append(elements[i].v);
			sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}
	
	private void grow() {
		int nsize = index.length*2;
		Entry[][] nindex = new Entry[nsize][];
		for(int i=0;i<elements.length;i++) {
			Entry e = elements[i];
			int j = (int)(e.id%nsize);
			Entry[] array = nindex[j];
			if (array == null) {
				array = new Entry[1];
				array[0] = e;
				nindex[j] = array;
			} else {
				Entry[] narray = new Entry[array.length+1];
				System.arraycopy(array,0,narray,0,array.length);
				narray[array.length] = e;
				nindex[j] = narray;
			}
		}
		index = nindex;
		size = nsize;
	}
}
