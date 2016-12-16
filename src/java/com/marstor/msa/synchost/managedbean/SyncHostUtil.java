/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.synchost.managedbean;

/**
 *
 * @author Administrator
 */
public class SyncHostUtil {

    public static boolean checkNum(String name, boolean canZero) {
        boolean flag = false;
        if (canZero) {
            if (name.length() <= 6) {
                flag = name.matches("^[0-9]+$");
            }
        } else {
            if (name.length() <= 6) {
                flag = name.matches("^[1-9][0-9]*$") || name.matches("^[1-9]$");
            }
        }
        return flag;
    }

    public static boolean checkPort(String name) {
        boolean flag = false;
        if (Long.parseLong(name) <= 65535 && Long.parseLong(name) > 0) {
            flag = true;
        }
        return flag;
    }

    public static boolean checkIP(String name) {
        boolean flag = false;
        if (null == name || "".equals(name)) {
            return flag;
        }
        if (!Character.isDigit(name.charAt(name.length() - 1))) {
            return flag;
        }
        flag = name.matches("^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$");
        return flag;
    }
}
