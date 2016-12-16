/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.catalina.connector.Constants;

/**
 *
 * @author Administrator
 */
public class ValidateUtility {
    
    //Language

 
    public static boolean checkDomainName(String domain) {
        if (null == domain || "".equals(domain)) {
            return false;
        }
        return domain.matches("[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+.?");
    }

    public static boolean checkE_Mail(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }
        return email.matches("^[\\w]+(\\.[\\w]+)*@[\\w]+(\\.[\\w]+)+$");
    }

    public static boolean checkTime(String name) {
        boolean flag = false;
        if (name.length() <= 4) {
            flag = name.matches("^[0-9]+$");
        }
        return flag;
    }

    public static boolean checkName(String name) {
        boolean flag = false;
        if (name.length() < 64 && name.length() > 0) {
            flag = name.matches("^[A-Za-z0-9]+$");
        }
        return flag;
    }
    
     public static boolean checkIPMPName(String name) {
        boolean flag = false;
        if (name.length() > 0) {
            flag = name.matches("^[A-Za-z]+$");
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
    public static boolean checkPoolName(String name) {
        boolean flag = false;
        if (name.length() <= 16 && name.length() > 0) {
            flag = name.matches("^[A-Za-z]+[A-Za-z0-9]*+$");
        }
        return flag;
    }

    public static boolean checkNum(String name, boolean canZero) {
        boolean flag = false;
        if (canZero) {
            if (name.length() <= 4) {
                flag = name.matches("^[0-9]+$");
            }
        } else {
            if (name.length() <= 4) {
                flag = name.matches("^[1-9]+[0-9]*$");
            }
        }
        return flag;
    }

    public static boolean checkSize(String name, boolean canZero, long lc) {

        boolean flag = false;
        if (canZero) {
            flag = name.matches("^[0-9]+$") && Long.parseLong(name) < lc;
        } else {
            flag = name.matches("^[1-9]+[0-9]*$") && Long.parseLong(name) < lc;
        }
        return flag;
    }
    
    public static boolean checkNOTLessSize(String name, boolean canZero, long lc) {

        boolean flag = false;
        if (canZero) {
            flag = name.matches("^[0-9]+$") && Long.parseLong(name) >= lc;
        } else {
            flag = name.matches("^[1-9]+[0-9]*$") && Long.parseLong(name) >= lc;
        }
        return flag;
    }

//    public static boolean checkAliasName(String name) {
//        boolean flag = false;
//        if (name.length() <= 64 && name.length() > 0) {
//            flag = name.matches("^" + Constants.STRING_TARGET_ALIAS + "\\d+$");
//        }
//        return flag;
//    }

    public static boolean checkIQN(String name) {
        boolean flag = false;
        if (name.length() <= 64 && name.length() > 0) {
            flag = name.matches("^(iqn.)(\\d){4}(-)(\\d){2}(.)[A-Za-z0-9._:-]+$");
        }
        return flag;
    }

    public static boolean checkTargetNum(String name) {
        boolean flag = false;
        if (name.length() <= 20) {
            flag = name.matches("^[0-9]+$");
        }
        return flag;
    }
    public static final String POOL_LINK_STATUS = "^[0-1]{7}$";
    public static final int POOL_LINK_SYSTEM = 0;
    public static final int POOL_LINK_VDL = 1;
    public static final int POOL_LINK_DISK = 2;
    public static final int POOL_LINK_NAS = 3;
    public static final int POOL_LINK_CDP = 4;
    public static final int POOL_LINK_VM = 5;
    public static final int POOL_LINK_TAPE = 6;

    public static boolean checkIsLinkPoolStatus(String poolLinkStatus) {
        boolean flag = false;
        flag = poolLinkStatus.matches(POOL_LINK_STATUS);
        flag = flag & poolLinkStatus.contains("1");
        return flag;
    }

    public static boolean checkWhichPoolBeNotLink(String poolLinkStatus, int whichPool) {
        boolean flag = false;
        flag = ('0' == poolLinkStatus.charAt(whichPool));
        return flag;
    }

    public static boolean checkSize(String name) {
        boolean flag = false;
        if (name.length() <= 4) {
            flag = name.matches("^[0-9]+$");
        }
        if (flag) {
            flag = (Integer.valueOf(name) > 0);
        }
        return flag;
    }

    public static boolean checkTapeCode(String tapeCode) {
        boolean flag = false;
        if (tapeCode.length() <= 20 && tapeCode.length() >= 6) {
            flag = tapeCode.matches("^[A-Z]{4}[0-9]+$");
        }
        return flag;
    }

    public static boolean checkIPInRouter(String name) {
        boolean flag = false;
        if (null == name || "".equals(name)) {
            return flag;
        }
//        if (!Character.isDigit(name.charAt(name.length() - 1))){
//            return flag;
//        }
        String[] subString = name.split(",");
        flag = subString[0].matches("^(((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?))(/(([1-9])|([1-2]\\d)|(3[0-2])))?$");
        return flag;
    }
//==============================================================================
//                             时间格式转化
//==============================================================================
    private static SimpleDateFormat dateFormat_MMddHHmmyyyy = new SimpleDateFormat("MMddHHmmyyyy.ss");
    private static SimpleDateFormat dataFormatLocalOther = new SimpleDateFormat();
    private static SimpleDateFormat dataFormatUTCOther = new SimpleDateFormat();
    private static SimpleDateFormat dataFormatOther = new SimpleDateFormat();
//  Local String <----> Local Date(或 UTC String <----> UTC Date)
//  UTC   String <----- Local Date
//  UTC   String -----> Local Date

    /**将 本地时间 转换成 "时间"字符串 <br/>或 <br/>将 UTC时间 转换成 UTC"时间"字符串*/
    public static String LocalDate2LocalStringByPattern(Date localDate, String pattern) {
        dataFormatLocalOther.setTimeZone(TimeZone.getDefault());
        if (null == localDate) {
            return "";
        }
        if (null == pattern || "".equals(pattern)) {
            return "";
        }
        dataFormatLocalOther.applyPattern(pattern);
        return dataFormatLocalOther.format(localDate);
    }

    /**将 "时间"字符串 转换成 本地时间 <br/>或 <br/>将 UTC"时间"字符串 转换成 UTC时间*/
    public static Date LocalString2LocalDateByPattern(String strLocalDate, String pattern) {
        dataFormatLocalOther.setTimeZone(TimeZone.getDefault());
        Date date = null;
        if (null == strLocalDate) {
            return date;
        }
        if (null == pattern || "".equals(pattern)) {
            return date;
        }
        dataFormatLocalOther.applyPattern(pattern);
        try {
            date = dataFormatLocalOther.parse(strLocalDate);
        } catch (Exception e) {
             System.out.println(e);
//            Constants.println(e);
        }
        return date;
    }

    /**将 UTC"时间"字符串 转换成 本地时间*/
    public static Date UTCString2LocalDateByPattern(String strUTCDate, String pattern) {
        dataFormatUTCOther.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        if (null == strUTCDate) {
            return date;
        }
        if (null == pattern || "".equals(pattern)) {
            return date;
        }
        dataFormatUTCOther.applyPattern(pattern);
        try {
            date = dataFormatUTCOther.parse(strUTCDate);
        } catch (Exception e) {
             System.out.println(e);
//            Constants.println(e);
        }
        return date;
    }

    /**将 本地时间 转换成 UTC"时间"字符串*/
    public static String LocalDate2UTCStringByPattern(Date localDate, String pattern) {
        dataFormatUTCOther.setTimeZone(TimeZone.getTimeZone("GMT"));
        String utc = "";
        if (null == localDate) {
            return utc;
        }
        if (null == pattern || "".equals(pattern)) {
            return utc;
        }
        dataFormatUTCOther.applyPattern(pattern);
        try {
            utc = dataFormatUTCOther.format(localDate);
        } catch (Exception e) {
             System.out.println(e);
//            Constants.println(e);
        }
        return utc;
    }

    /**将 Local Date 转换成 指定时区String*/
    public static String LocalDate2SpecificTimeZoneStringByPattern(Date localDate, String pattern, String timeZone) {
        String dateString = null;
        if (null == localDate) {
            return null;
        }
        if (null == timeZone || "".equals(timeZone)) {
            return null;
        }
        if (null == pattern || "".equals(pattern)) {
            return null;
        }
        dataFormatOther.setTimeZone(TimeZone.getTimeZone(timeZone));
        dataFormatOther.applyPattern(pattern);
        try {
            dateString = dataFormatOther.format(localDate);
        } catch (Exception e) {
             System.out.println(e);
//            Constants.println(e);
        }
        return dateString;
    }

    /**将 Local Date 转换成 指定时区String*/
    public static Date SpecificTimeZoneString2LocalDateByPattern(String strTimeZoneDate, String pattern, String timeZone) {
        dataFormatOther.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date date = null;
        if (null == strTimeZoneDate) {
            return date;
        }
        if (null == pattern || "".equals(pattern)) {
            return date;
        }
        dataFormatOther.applyPattern(pattern);
        try {
            date = dataFormatOther.parse(strTimeZoneDate);
        } catch (Exception e) {
            System.out.println(e);
//            Constants.println(e);
        }
        return date;
    }

    public static Date UTCString2LocalDate_MMddHHm(String strUTCDate) {
        dateFormat_MMddHHmmyyyy.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = dateFormat_MMddHHmmyyyy.parse(strUTCDate);
        } catch (Exception e) {
             System.out.println(e);
//            Constants.println(e);
        }
        return date;
    }

    public static String localDate2UTCString_MMddHHm(java.util.Date localDate) {
        dateFormat_MMddHHmmyyyy.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat_MMddHHmmyyyy.format(localDate);
    }
//==============================================================================
//                             容量格式转化
//==============================================================================

    public static String sizeToString(long size) {
        
        if (size <= 0) {
            return "0.00B";
        }
        String unitStr;
        long intSize, pointSize;
        long unit;
        if (size >= ((long) 1024 * (long) 1024 * (long) 1024 * (long) 1024)) {
            unitStr = "TB";
            unit = (long) 1024 * (long) 1024 * (long) 1024 * (long) 1024;
        } else if (size >= ((long) 1024 * (long) 1024 * (long) 1024)) {
            unitStr = "GB";
            unit = (long) 1024 * (long) 1024 * (long) 1024;
        } else if (size >= ((long) 1024 * (long) 1024)) {
            unitStr = "MB";
            unit = (long) 1024 * (long) 1024;
        } else if (size >= (long) 1024) {
            unitStr = "KB";
            unit = (long) 1024;
        } else {
            unitStr = "B";
            unit = 1;
        }
        intSize = size / unit;
        pointSize = ((size % unit) * 100) / unit;
        if (intSize >= 100) {
            return intSize + unitStr;
        } else if (intSize >= 10) {
            return intSize + "." + pointSize / 10 + unitStr;
        } else {
            return intSize + "." + pointSize / 10 + pointSize % 10 + unitStr;
        }
    }
    
    public static String byteToGB(long size) {
 
        if (size <= 0) {
            return "0.00B";
        }
        String unitStr;
        long intSize, pointSize;
        long unit;
      
            unitStr = "GB";
            unit = (long) 1024 * (long) 1024 * (long) 1024;
        intSize = size / unit;
        pointSize = ((size % unit) * 100) / unit;
        if (intSize >= 100) {
            return intSize +"";
        } else if (intSize >= 10) {
            return intSize + "." + pointSize / 10 + "";
        } else {
            return intSize + "." + pointSize / 10 + pointSize % 10 + "";
        }
    }

    public static double stringToGBSize(String size) {
        if (null == size || "".equals(size)) {
            return 0;
        }
        size = size.toUpperCase();
        if (!size.matches("^[0-9]+[\\.]{0,1}[0-9]*[M|K|G|T|P|B]{1}[B]{0,1}$")) {
            return 0;
        }
        int length = size.length();
        double num = 0;
        for (int i = length - 1; i >= 0; i--) {
            char temp = size.charAt(i);
            if (!(temp <= '9' && temp >= '0')) {
                continue;
            } else {
                num = Double.valueOf(size.substring(0, i + 1));
                break;
            }
        }
        if (size.endsWith("M") || size.endsWith("MB")) {
            return num / 1024f;
        }
        if (size.endsWith("K") || size.endsWith("KB")) {
            return num / 1024f / 1024f;
        }
        if (size.endsWith("G") || size.endsWith("GB")) {
            return num;
        }
        if (size.endsWith("T") || size.endsWith("TB")) {
            return num * 1024f;
        }
        if (size.endsWith("P") || size.endsWith("PB")) {
            return num * 1024f * 1024f;
        }
        return 0;
    }

    public static String sizeAddCharB(String size) {
        if (size.endsWith("B")) {
            return size;
        } else {
            return size + "B";
        }
    }

  
    
}
