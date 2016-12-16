package com.marstor.msa.cdp.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Administrator
 */
public class AgentUtility {
    public static String bundle = "com/marstor/msa/cdp/resource/MSACDPModuleClient";
    
    public static boolean checkSize(String name, boolean canZero, long lc) {

        boolean flag = false;
        if (canZero) {
            flag = name.matches("^[0-9]+$") && Long.parseLong(name) < lc;
        } else {
            flag = name.matches("^[1-9]+[0-9]*$") && Long.parseLong(name) < lc;
        }
        return flag;
    }

    public static boolean checkName(String name) {
        boolean flag = false;
        if (name.length() <= 64 && name.length() > 0) {
            flag = name.matches("^[A-Za-z0-9]+$");
        }
        return flag;
    }
    
    public static boolean checkDatabaseName(String name) {
        boolean flag = false;
        if (name.length() <= 128 && name.length() > 0) {
            flag = name.matches("^[A-Za-z0-9_]+$");
        }
        return flag;
    }
    
    public static boolean checkInstanceName(String name) {
        boolean flag = false;
        if (name.length() <= 64 && name.length() > 0) {
            flag = true;
        }
        return flag;
    }

    public static boolean checkIP(String name) {
        boolean flag = false;
        if (null == name || "".equals(name)) {
            return flag;
        }
        if (!Character.isDigit(name.charAt(name.length() - 1))){
            return flag;
        }
        flag = name.matches("^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$");
        return flag;
    }

    public static boolean checkNum(String name, boolean canZero) {
        boolean flag = false;
        if (canZero) {
            if (name.length() <= 6) {
                flag = name.matches("^[0-9]+$");                
            }
        } else {
            if (name.length() <= 6) {
                flag = name.matches("^[1-9][0-9]*$")||name.matches("^[1-9]$");
            }
        }
        return flag;
    }
    
    public static boolean checkPort(String name) {
        boolean flag = false;
        if(Long.parseLong(name) <= 65535 && Long.parseLong(name) > 0){
            flag = true;
        }
        return flag;
    }
  
    public static String sizeAddCharB(String size) {
        if (size.endsWith("B")) {
            return size;
        } else {
            return size + "B";
        }
    }

    public static String getOtherString(String key) {
        try {
            return ResourceBundle.getBundle(bundle, Locale.getDefault()).getString(key);
        } catch (Exception ex) {
            return key;
        }
    }

}
