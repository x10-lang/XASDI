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

package mysample2.model;

import java.io.Serializable;
import java.util.LinkedList;

public class SampleModel implements Serializable
{
	private double a;
	private LinkedList<Integer> numlist;
	private SampleParam param;
	
	public SampleModel()
	{
		numlist = new LinkedList<Integer>();
		param = new SampleParam();
	}
	
	public void setValue(double val, int cnt)
	{
		a = val;
		for (int i = 0; i < cnt; i++)
			numlist.add(i*i);
		param.setValue(val, cnt);
	}
	
	public String toString()
	{
		String str;
		str = "Model val "+a+" "+numlist.toString()+" : "+param.toString();
		return str;
	}

}
