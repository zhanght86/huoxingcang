package com.marstor.msa.vtl.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javax.swing.ImageIcon;

/**
 *
 * @author Administrator
 */
public class MyUtility {
    //Language

    public static String bundle = "com/marstor/msa/common/util/resources/MyUtility";
    //Image
    public final static byte LOG_TYPE_ALL = 0x0F;
    public final static byte LOG_TYPE_NORMAL = 0x01;
    public final static byte LOG_TYPE_WARNING = 0x02;
    public final static byte LOG_TYPE_ERROR = 0x04;
    public final static byte LOG_TYPE_DEBUG = 0x08;
    public static final int MAX_SYSTEM_USERS = 32;
    //LUType
    public static final int LU_TYPE_DISK = 1;
    public static final int LU_TYPE_DRIVE = 2;
    public static final int LU_TYPE_TAPELIBRARY = 3;
    public static final int VAULT_TYPE_FREE = 0;//带架
    public static final int VAULT_TYPE_IMPORT = 2;
    public static final int VAULT_TYPE_FOREIGN = 1;
    public static int VAULT_TYPE_OFFLINE = -1;//离线带架
//    public static final int VAULT_TYPE_USER = 3;
    //location
    public static final int LOCATION_TYPE_VAULT = 0;
    public static final int LOCATION_TYPE_TAPELIBRARY = 1;
    //pool
    public static final int NODE_TYPE_POOL_DISK = 81; //磁盘池
    public static final int NODE_TYPE_POOL_TAPE = 82; //磁带池
    public static final int NODE_TYPE_POOL_NAS = 83; //文件池
    public static final int NODE_TYPE_POOL_VDL = 84;
    public static final int NODE_TYPE_POOL_CDP = 85;
    public static final int NODE_TYPE_POOL_VM = 86;
    public static final String ZFS_POOL_DISK = "DISK";
    public static final String ZFS_POOL_TAPE = "TAPE";
    public static final String ZFS_POOL_NAS = "NAS";
    public static final String ZFS_POOL_VDL = "VDL";
    public static final String ZFS_POOL_CDP = "CDP";
    public static final String ZFS_POOL_VM = "VM";
    //target
    public static final int TARGET_TYPE_FC = 1;
    public static final int TARGET_TYPE_FCoE = 2;
    public static final int TARGET_TYPE_iSCSI = 3;
    public final static int LU_MODE_CDP = 4;
    public static final int TARGET_NODE_TYPE_LIBRARY = 1;
    public static final int TARGET_NODE_TYPE_DISK = 2;
    public static final int TARGET_NODE_TYPE_CDP = 3;
    //System volume status
    public static final int SYSVOL_STATUS_OK = 1;
//    public static final int SYSVOL_STATUS_NON = 0;
//    public static final int SYSVOL_STATUS_NEED_TO_CREATE = 2;
//    public static final int SYSVOL_STATUS_NEED_TO_IMPORT = 3;
    public static final String SYSVOL_NAME = "SYSVOL";
//==============================================================================
//                            用于检查的方法
//==============================================================================

    public static boolean checkDomainName(String domain) {
        if (null == domain || "".equals(domain)) {
            return false;
        }
        return domain.matches("^(([^-][a-z0-9A-Z-_]+\\.)*)[^-][a-z0-9A-Z-_]+(\\.[a-zA-Z]{2,4}){1,2}$");
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
        if (name.length() <= 64 && name.length() > 0) {
            flag = name.matches("^[A-Za-z]+[A-Za-z0-9]+$");
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
        }
        return date;
    }

    public static Date UTCString2LocalDate_MMddHHm(String strUTCDate) {
        dateFormat_MMddHHmmyyyy.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = dateFormat_MMddHHmmyyyy.parse(strUTCDate);
        } catch (Exception e) {
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

    public static String getOtherString(String key) {
        try {
            return ResourceBundle.getBundle(bundle, Locale.getDefault()).getString(key);
        } catch (Exception ex) {
            return key;
        }
    }

    public static void main(String[] args) {
//        Date date = MyUtility.localString2LocalDate_yyyy_MM("2000-02-04 09:20:30");
//        System.out.println(MyUtility.localDate2LocalString_yyyy_MM(date));
//        System.out.println(MyUtility.localDate2UTCString_yyyy_MM(date));
//        System.out.println(MyUtility.checkPoolLinkDevice("1000010"));
//        Date date = MyUtility.LocalString2LocalDateByPattern("2011-02-02 02:02:02", "yyyy-MM-dd HH:mm:ss");
//        System.out.println(date);
//        String local =MyUtility.LocalDate2LocalStringByPattern(date, "yyyy-MM-dd HH:mm:ss");
//        System.out.println(local);
//        System.out.println("");
//        String utcString = MyUtility.LocalDate2UTCStringByPattern(date,"yyyy-MM-dd HH:mm:ss");
//        System.out.println(utcString);
//        date = MyUtility.UTCString2LocalDateByPattern(utcString, "yyyy-MM-dd HH:mm:ss");
//        System.out.println(date);
//        System.out.println("");
//        date = MyUtility.LocalString2LocalDateByPattern("2011-02-02 02:02:02", "yyyy-MM-dd HH:mm:ss");
//        System.out.println(date);
//        System.out.println(MyUtility.LocalDate2LocalStringByPattern(date, "yyyy-MM-dd HH:mm:ss"));
//        Date date = MyUtility.UTCString2LocalDateByPattern("2011-02-02 02:02:02", "yyyy-MM-dd HH:mm:ss");
//        System.out.println(date);
//        System.out.println(MyUtility.LocalDate2SpecificTimeZoneStringByPattern(date, "yyyy-MM-dd HH:mm:ss", "GMT+9"));
//        String aa = MyUtility.LocalDate2SpecificTimeZoneStringByPattern(date, "yyyy-MM-dd HH:mm:ss", "GMT+9");
//        System.out.println(MyUtility.SpecificTimeZoneString2LocalDateByPattern(aa, "yyyy-MM-dd HH:mm:ss", "GMT+9"));
//        Calendar c = Calendar.getInstance();
//        c.setTimeZone(TimeZone.getDefault());
//        System.out.println(c.getTimeInMillis());
//        System.out.println(c.getTime());
//        System.out.println("");
//        c.setTimeZone(TimeZone.getTimeZone("GMT"));
//        System.out.println(c.getTimeInMillis());
//        System.out.println(c.getTime());
//        System.out.println("192.18.1.2202".matches("(\\d{1,3}\\.){3}\\d{1,3}"));
//        System.out.println(MyUtility.stringToGBSize("512.564MB"));
//        System.out.println(MyUtility.stringToGBSize("123.564kb"));
//        System.out.println(MyUtility.stringToGBSize("1GB"));
//        System.out.println(MyUtility.stringToGBSize("1tb"));
//        System.out.println(MyUtility.stringToGBSize("123.564P"));
//        System.out.println(MyUtility.checkDomainName("fdskfdswa.d16.com"));
        System.out.println(MyUtility.checkE_Mail("zhao.lisong@company.com"));
    }
}
