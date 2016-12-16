/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.util;

import static com.marstor.msa.cdp.util.SnapUtil.CLOSED;
import static com.marstor.msa.cdp.util.SnapUtil.CLOSING;
import static com.marstor.msa.cdp.util.SnapUtil.COMPLETE;
import static com.marstor.msa.cdp.util.SnapUtil.DELETED;
import static com.marstor.msa.cdp.util.SnapUtil.DELETING;
import static com.marstor.msa.cdp.util.SnapUtil.EXECUTING;
import static com.marstor.msa.cdp.util.SnapUtil.FAILED;
import static com.marstor.msa.cdp.util.SnapUtil.HALT;
import static com.marstor.msa.cdp.util.SnapUtil.SUSPENDING;
import static com.marstor.msa.cdp.util.SnapUtil.WAIT;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "u")
@ViewScoped
public class SnapUtil {

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(date);
    }

    public static Date stringToDate(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return formatter.parse(s);
        } catch (ParseException ex) {
            Logger.getLogger(SnapUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static String dateToStringLocal(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(date);
    }

    public static String dateToStringLocalMS(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return formatter.format(date);
    }

    public static Date stringToDateLocal(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return formatter.parse(s);
        } catch (ParseException ex) {
            Logger.getLogger(SnapUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Date stringToDateLocalMS(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        try {
            return formatter.parse(s);
        } catch (ParseException ex) {
            Logger.getLogger(SnapUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
//
//    //><!--0等待同步，1正在同步，2暂停同步，3完成同步，4同步失败-->
//    public static String getStringSyncStatus(int status, CopyTargetMapping obj) {
//        switch (status) {
//            case 0:
//                if (obj.getdSyncCompleteTime() == null) {
//                    return WAIT + " 上次同步：";
//                }
//                return WAIT + " 上次同步：" + Util.dateToStringLocal(obj.getdSyncCompleteTime());
//            case 1:
//                String s = obj.getStrSyncSizeSpeed().replaceAll("S", "s");
//                return EXECUTING + s;
//            case 2:
//                if (obj.getdSyncCompleteTime() == null) {
//                    return WAIT + " 上次同步：";
//                }
//                return HALT + " 上次同步：" + Util.dateToStringLocal(obj.getdSyncCompleteTime());
//            case 3:
//                return COMPLETE;
//            case 4:
//                return FAILED;
//            case 5:
//                return SUSPENDING;
//            case 6:
//                return DELETING;
//            case 7:
//                return DELETED;
//            case 8:
//                return CLOSING;
//            case 9:
//                return CLOSED;
//            default:
//                return "";
//        }
//    }

    public static String getStringDiskStatus(int status) {
        switch (status) {
            case 0:
                return "可使用";
            case 1:
                return "正在创建";
            case 2:
                return "创建失败";

            default:
                return "";
        }
    }
    
    public static String getStringLogFullOption(int status) {
        switch (status) {
            case CDPConstants.OPTION_AUTO_LOOP:
                return "自动循环";
            case CDPConstants.OPTION_STOP_CDP:
                return "停止记录";
            case CDPConstants.OPTION_STOP_WRITE:
                return "磁盘组写保护";

            default:
                return "";
        }
    }
    
     public static double convertSizeToByte(String strSize) {
        if (strSize == null || strSize.equals("")) {
            return 0;
        }
        int iLength = strSize.length();
        String strNumber;
        char c;
        boolean bRet;
        if (iLength > 1) {
            c = strSize.charAt(iLength - 2);
            strNumber = strSize.substring(0, iLength - 2);
        } else {
            c = 'a';
            strNumber = strSize;       
        }
        if (true) {
            switch (c) {
                case 'T':
                    return Double.parseDouble(strNumber) * 1024 * 1024 * 1024 * 1024;
                case 'G':
                    return Double.parseDouble(strNumber) * 1024 * 1024 * 1024;
                case 'M':
                    return Double.parseDouble(strNumber) * 1024 * 1024;
                case 'K':
                    return Double.parseDouble(strNumber) * 1024;
                default:
                    return Double.parseDouble(strSize.substring(0, iLength - 1));
            }
        }
        return 0;
    }

    public static long convert(String size) {
        long s = 0;
        String num = size.substring(0,size.length()-1);
        if (size.endsWith("K")) {
            s = Long.parseLong(num) * 1024;
        }
        if (size.endsWith("M")) {
            s = Long.parseLong(num) * 1024 * 1024;
        }
        if (size.endsWith("G")) {
            s = Long.parseLong(num) * 1024 * 1024 * 1024;
        }
        if (size.endsWith("T")) {
            s = Long.parseLong(num) * 1024 * 1024 * 1024 * 1024;
        }
        return s;
    }
    
    public static int getIntFromOS(String os){
        //<!--1 windows 2 Linux 3 Solaris 4 AIX 5 HP-UNIX 0 Other-->
        int ios = 0;
        if(os.equalsIgnoreCase("windows")){
            ios = 1;
        }else if (os.equalsIgnoreCase("linux")){
            ios = 2;
        }else if (os.equalsIgnoreCase("Solaris")){
            ios = 3;
        }else if (os.equalsIgnoreCase("AIX")){
            ios = 4;
        }else if (os.equalsIgnoreCase("HP-UNIX")){
            ios = 5;
        }
        
        return ios;
    }
    
    public static final String WAIT = "等待同步";
    public static final String EXECUTING = "正在同步(已同步[速率])：";
    public static final String HALT = "暂停同步";
    public static final String COMPLETE = "完成同步";
    public static final String FAILED = "同步失败";
    public static final String SUSPENDING = "正在暂停";
    public static final String DELETING = "正在删除";
    public static final String DELETED = "已删除";
    public static final String CLOSING = "正在停止";
    public static final String CLOSED = "未开启";
}
