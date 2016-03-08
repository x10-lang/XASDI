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

package com.ibm.xasdi_bridge.fd.engine;

public class Log {
       public static boolean DEBUG = false;
      
       static {
            String d = System.getProperty( "DEBUG","false" );
             if (d.equalsIgnoreCase("true" )) {
                  DEBUG = true;
            } else {
                  DEBUG = false;
            }
      }
      
       public static void info(String component, String msg) {
//           System.out.println( "XASDI_BRIDGE FD INFO[" + component + "]:" + msg);
      }
      
       public static void warning(String component, String msg) {
//            System.out.println( "XASDI_BRIDGE FD WARNING[" + component + "]:" + msg);
      }
      
       public static void error(String component, String msg) {
//            System.out.println( "XASDI_BRIDGE FD ERROR[" + component + "]:" + msg);
      }
      
       public static void error(String component, Exception e) {
//            System.out.println( "XASDI_BRIDGE FD ERROR[" + component + "]:" + e.getMessage());
      }
      
       public static void error(String component, String msg, Exception e) {
//            System.out.println( "XASDI_BRIDGE FD ERROR[" + component + "]:" + msg + " " + e.getMessage());
            e.printStackTrace();
      }
      
       public static void debug(String component, Object o) {
             if (DEBUG) {
                  System.out.println( "XASDI_BRIDGE FD DEBUG[" + component + "]:" + o);
            }
      }
}
