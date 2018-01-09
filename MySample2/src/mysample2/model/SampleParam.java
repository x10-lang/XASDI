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

package mysample2.model;

import java.io.Serializable;
import java.util.LinkedList;

public class SampleParam implements Serializable
{
	/**
	 * 
	 */
	private double a;
	private LinkedList<Integer> numlist;
	
	public SampleParam()
	{
		numlist = new LinkedList<Integer>();
	}
	
	public void setValue(double val, int cnt)
	{
		a = val;
		for (int i = 0; i < cnt; i++)
			numlist.add(i);
	}
	
	public String toString()
	{
		String str;
		str = "Param val "+a+" "+numlist.toString();
		return str;
	}

}
