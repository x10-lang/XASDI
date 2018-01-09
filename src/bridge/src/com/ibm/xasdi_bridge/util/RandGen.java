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

package com.ibm.xasdi_bridge.util;

public class RandGen
{
	int Q1, Q2, S1, S2, P1mS1, P2mS2, P1mP2;
	int I1, I2, b, Mask1, Mask2;
	double Norm;
	long seed2;
	long seed1;
	
	public RandGen(long seed1, long seed2)
	{
		Q1=13; Q2=2; S1=12; S2=17; P1mS1=19; P2mS2=12; P1mP2=2;
		Mask1=2147483647; Mask2=536870911;
		Norm=4.656612873e-10;
		this.seed1 = seed1;
		this.seed2 = seed2;
		
		if( seed1 == 0 || seed2 == 0)
		{
			I1 = (int) System.currentTimeMillis() & Mask1;
			I2 = ((int)System.currentTimeMillis() * 1439) & Mask2;
		}
		else
		{
			I1 = (int) seed1 & Mask1;
			I2 = ((int) seed2 * 1439) & Mask2;
		}
	}
	
	public RandGen()
	{
		this(0,0);
	}
	
	/*
	 * uniform randam number generation by Tezuka
	 */
	public double CombTaus()
	{
		b = ((I1 << Q1) ^ I1) & Mask1;
		I1 = ((I1 << S1) ^ (b >>> P1mS1)) & Mask1;
		b = ((I2 << Q2) ^ I2) & Mask2;
		I2 = ((I2 << S2) ^ (b >>> P2mS2)) & Mask2;
		return ((I1 ^ (I2 << P1mP2)) * Norm);
	}
	
	public double nextDouble()
	{
		return CombTaus();
	}
	
	/*
	 * compute the inverse of cumulative normal distribution,
	 * Risk, Feb 1995
	 */
	public double cndev(double u)
	{

		  double   a[] = {2.50662823884,
		    -18.61500062529,
		    41.39119773534,
		  -25.44106049637};
		  double   b[] = {-8.4735109309,
		    23.08336743743,
		    -21.06224101826,
		  3.13082909833};
		  double   c[] = {0.3374754822726147,
		    0.9761690190917186,
		    0.1607979714918209,
		    0.0276438810333863,
		    0.0038405729373609,
		    0.0003951896511919,
		    .0000321767881768,
		    .0000002888167364,
		  .0000003960315187};
		  double          x, r;
		  x = u - .5;
		    if (Math.abs(x) < .42)
		    {
		      r = x * x;
		      r = x * (((a[3] * r + a[2]) * r + a[1]) * r + a[0]) / ((((b[3] * r + b[2]) * r + b[1]) * r + b[0]) * r + 1.0);
		      u = r;
		    }
		    else
		    {
		      r = u;
		      if (x > .0)
			r = 1.0 - u;
		      r = Math.log(-Math.log(r));
		      r = c[0] + r * (c[1] + r * (c[2] + r * (c[3] + r * (c[4] + r * (c[5] + r * (c[6] + r * (c[7] + r * c[8])))))));
		      if (x < .0)
			r = -r;
		      u = r;
		    }
			return u;
		  }

	public double gauss(double mean, double sigma)
	{
		return cndev(CombTaus()) * sigma + mean;
	}

	public static void main(String args[])
	{
		RandGen rand = new RandGen();
		for (int i = 0; i < 1000; i++)
		{
			double u = rand.gauss(0,1);
			System.out.println(u);
		}
	}

}
