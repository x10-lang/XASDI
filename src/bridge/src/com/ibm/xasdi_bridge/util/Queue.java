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
public class Queue {
	static class Entry {
		Entry prev = null;
		Entry next = null;
		Object obj = null;
	}
	
	Entry top = null;
	Entry tail = null;
	Entry free = null;
	
	public void put(Object o) {
		Entry e = null;
		if (free != null) {
			e = free;
			free = free.next;
			if (free != null) {
				free.prev = null;
			}
			e.next = null;
			e.prev = null;
		} else {
			e = new Entry();
		}
		e.obj = o;
		if (tail == null) {
			top = e;
			tail = e;
		} else {
			tail.next = e;
			e.prev = tail;
			tail = e;
		}
	}
	
	public Object getFirst() {
		if (top == null) {
			return null;
		} else {
			Entry e = top;
			top = e.next;
			if (top != null) {
				top.prev = null;
			} else {
				tail = null;
			}
			Object o = e.obj;
			e.obj = null;
			e.prev = null;
			e.next = free;
			if (free != null) {
				free.prev = e;
			}
			free = e;
			return o;
		}
	}
	
	public boolean isEmpty() {
		if (top == null) {
			return true;
		}
		return false;
	}
}
