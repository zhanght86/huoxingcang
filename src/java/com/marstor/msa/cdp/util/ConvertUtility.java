/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.util;

import com.marstor.msa.cdp.bean.SyncMapping;
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
@ManagedBean(name = "cu")
@ViewScoped
public class ConvertUtility {

    
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
        if(date == null){
            return "";
        }
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

    //><!--0�ȴ�ͬ����1����ͬ����2��ͣͬ����3���ͬ����4ͬ��ʧ��-->
    public static String getStringSyncStatus(int status, SyncMapping obj) {
        switch (status) {
            case 0:
                if (obj.getdSyncCompleteTime() == null) {
                    return WAIT + " �ϴ�ͬ����";
                }
                return WAIT + " �ϴ�ͬ����" + ConvertUtility.dateToStringLocal(obj.getdSyncCompleteTime());
            case 1:
                String s = obj.getStrSyncSizeSpeed().replaceAll("S", "s");
                return EXECUTING + s;
            case 2:
                if (obj.getdSyncCompleteTime() == null) {
                    return WAIT + " �ϴ�ͬ����";
                }
                return HALT + " �ϴ�ͬ����" + ConvertUtility.dateToStringLocal(obj.getdSyncCompleteTime());
            case 3:
                return COMPLETE;
            case 4:
                return FAILED;
            case 5:
                return SUSPENDING;
            case 6:
                return DELETING;
            case 7:
                return DELETED;
            case 8:
                return CLOSING;
            case 9:
                return CLOSED;
            default:
                return "";
        }
    }

    public static String getStringDiskStatus(int status) {
        switch (status) {
            case 0:
                return "��ʹ��";
            case 1:
                return "���ڴ���";
            case 2:
                return "����ʧ��";

            default:
                return "";
        }
    }
    
    public static String getStringLogFullOption(int status) {
        switch (status) {
            case CDPConstants.OPTION_AUTO_LOOP:
                return "�Զ�ѭ��";
            case CDPConstants.OPTION_STOP_CDP:
                return "ֹͣ��¼";
            case CDPConstants.OPTION_STOP_WRITE:
                return "������д����";

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
    
    public static final String WAIT = "�ȴ�ͬ��";
    public static final String EXECUTING = "����ͬ��(��ͬ��[����])��";
    public static final String HALT = "��ͣͬ��";
    public static final String COMPLETE = "���ͬ��";
    public static final String FAILED = "ͬ��ʧ��";
    public static final String SUSPENDING = "������ͣ";
    public static final String DELETING = "����ɾ��";
    public static final String DELETED = "��ɾ��";
    public static final String CLOSING = "����ֹͣ";
    public static final String CLOSED = "δ����";
}
