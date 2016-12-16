package com.marstor.msa.cdp.util;

/**
 *
 * @author Administrator
 */
public class DGUtility {

    public static boolean checkDomainName(String domain) {
        if (null == domain || "".equals(domain)) {
            return false;
        }
        return domain.matches("^(([^-][a-z0-9A-Z-_]+\\.)*)[^-][a-z0-9A-Z-_]+(\\.[a-zA-Z]{2,4}){1,2}$");
    }

    public static boolean checkHostName(String host) {
        if (null == host || "".equals(host)) {
            return false;
        }
        return host.matches("^(([^-][a-z0-9A-Z-_]+\\.)*)[^-][a-z0-9A-Z-_]+(\\.[a-zA-Z]{2,4}){1,2}|[A-Za-z0-9]+$");
    }

    public static boolean checkE_Mail(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }
//        return email.matches("^[\\w]+(\\.[\\w]+)*@[\\w]+(\\.[\\w]+)+$")
        return email.matches("^([\\w\\d_\\.\\s-]+)@[\\w]+(\\.[\\w]+)+$")
                || email.matches("^[\\*]@[\\w]+(\\.[\\w]+)+$")
                || email.matches("^[\\w]+(\\.[\\w]+)*@[\\*]+$")
                || email.matches("^[\\*]@[\\*]+$");
    }

    public static boolean checkNum(String name) {
        boolean flag = false;
        if (name.length() <= 8) {
            flag = name.matches("^[0-9]+$");
        }
        return flag;
    }
    
    public static boolean checkLength(String name) {
        boolean flag = false;
        if (name.length() <= 8) {
            flag = true;
        }
        return flag;
    }

    public static boolean checkShareName(String name) {
        boolean flag = false;
        if (name.length() <= 8 && name.length() > 0) {
            flag = name.matches("[A-Za-z0-9]+$");
        }
        return flag;
    }

    public static boolean checkEntityName(String name) {
        boolean flag = false;
        if ((name.length() <= 8) && (name.length() > 0)) {
            flag = name.matches("[a-zA-Z0-9]+$");
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

    public static boolean checkIPRange(String name) {
        boolean flag = false;
        if (null == name || "".equals(name)) {
            return flag;
        }
        if (!Character.isDigit(name.charAt(name.length() - 1))){
            return flag;
        }
        flag = name.matches("^((?:(?:[01]\\d?\\d|2(?:[0-4]\\d|5[0-5]))\\.){3}(?:[01]\\d?\\d|2(?:[0-4]\\d|5[0-5])))\\/\\d(\\.\\d)?$");
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
                flag = name.matches("[0-9]|[1-9][0-9]+$");
            }
        }
        return flag;
    }

    public static boolean checkIPInRouter(String name) {
        boolean flag = false;
        if (null == name || "".equals(name)) {
            return flag;
        }        
        flag = name.matches("^(((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?))(/(([1-9])|([1-2]\\d)|(3[0-2])))?$") || name.equals("*");
        return flag;
    }

    public static boolean checkRootIP(String name) {
        boolean flag = false;
        if (null == name || "".equals(name)) {
            return flag;
        }
        flag = name.matches("^(((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?))(/(([1-9])|([1-2]\\d)|(3[0-2])))?$");
        return flag;
    }

    public static String sizeAddCharB(String size) {
        if (size.endsWith("B")) {
            return size;
        } else {
            return size + "B";
        }
    }

}
