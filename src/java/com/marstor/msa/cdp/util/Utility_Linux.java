/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.util;

import static com.marstor.msa.cdp.util.Utility_Linux.AppPath;
import static com.marstor.msa.cdp.util.Utility_Linux.convertUTF8String;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "util_L")
@ViewScoped
public class Utility_Linux {

    public static String AppPath = "";

    public static String getSysTimeString(long lTime) {
        Date dt = new Date();
        dt.setTime(lTime * 1000);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strTime = df.format(dt);

        return strTime;
    }

    public static boolean checkIP(String name) {
        boolean flag = false;
//        flag = name.matches("^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$");
        flag = name.matches("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        return flag;
    }

    public static String getSizeString(long lSize) {
        float fSize = (float) lSize / CDPConstants.GB;
        String strSize = String.format("%.1f", fSize);

        return strSize;
    }

    public static String getByteString(long lByte) {
        long lKB = 1000;
        long lMB = lKB * 1000;
        long lGB = lMB * 1000;
        long lTB = lGB * 1000;

        long lRemain = lByte;
        String strByte = "";

        strByte += String.format("%03d,", lRemain / lTB);
        lRemain = lRemain % lTB;
        strByte += String.format("%03d,", lRemain / lGB);
        lRemain = lRemain % lGB;
        strByte += String.format("%03d,", lRemain / lMB);
        lRemain = lRemain % lMB;
        strByte += String.format("%03d,", lRemain / lKB);
        lRemain = lRemain % lKB;
        strByte += String.format("%03d", lRemain);

        for (int i = 0; i < strByte.length(); i++) {
            char c = strByte.charAt(i);
            if (c != '0' && c != ',') {
                return strByte.substring(i);
            }
        }

        return "0";
    }

    public static String getHostName() {
        String strHostName = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();

            strHostName = addr.getHostName();
        } catch (Exception e) {
        }

        return strHostName;
    }

    public static String getPartitionTypeString(int nPartitionType) {
        String strPartitionType = "";
        switch (nPartitionType) {
            case CDPConstants.PARTITION_ENTRY_UNUSED:
                strPartitionType = "δʹ�÷���";
                break;
            case CDPConstants.PARTITION_FAT_12:
                strPartitionType = "FAT12 ����";
                break;
            case CDPConstants.PARTITION_XENIX_1:
                strPartitionType = "XENIX ����";
                break;
            case CDPConstants.PARTITION_XENIX_2:
                strPartitionType = "XENIX ����";
                break;
            case CDPConstants.PARTITION_FAT_16:
                strPartitionType = "FAT16 ����";
                break;
            case CDPConstants.PARTITION_EXTENDED:
                strPartitionType = "��չ����";
                break;
            case CDPConstants.PARTITION_HUGE:
                strPartitionType = "�����";
                break;
            case CDPConstants.PARTITION_IFS:
                strPartitionType = "IFS ����";
                break;
            case CDPConstants.PARTITION_OS2BOOTMGR:
                strPartitionType = "OS2 Boot MGR ����";
                break;
            case CDPConstants.PARTITION_FAT32:
                strPartitionType = "FAT32 ����";
                break;
            case CDPConstants.PARTITION_FAT32_XINT13:
                strPartitionType = "FAT32 ����";
                break;
            case CDPConstants.PARTITION_XINT13:
                strPartitionType = "XINT13 ����";
                break;
            case CDPConstants.PARTITION_XINT13_EXTENDED:
                strPartitionType = "XINT13 ��չ����";
                break;
            case CDPConstants.PARTITION_PREP:
                strPartitionType = "PREP ����";
                break;
            case CDPConstants.PARTITION_LDM:
                strPartitionType = "LDM ����";
                break;
            case CDPConstants.PARTITION_UNIX:
                strPartitionType = "UNIX ����";
                break;
            default:
                strPartitionType = "δ֪����";
                break;
        }

        return strPartitionType;
    }

    public static String getCDPTypeString(int nCDPType) {
        String strCDPType = "";
        switch (nCDPType) {
            case CDPConstants.CDP_TYPE_INIT:
                strCDPType = "δ����";
                break;
            case CDPConstants.CDP_TYPE_DISK:
                strCDPType = "���̱���";
                break;
            case CDPConstants.CDP_TYPE_PARTITION:
                strCDPType = "��������";
                break;
            case CDPConstants.CDP_TYPE_DISK_MIRROR:
                strCDPType = "���̾���";
                break;
            case CDPConstants.CDP_TYPE_PARTITION_MIRROR:
                strCDPType = "��������";
                break;
            case CDPConstants.CDP_TYPE_VOLUME:
                strCDPType = "LVM����";
                break;
            case CDPConstants.CDP_TYPE_VOLUME_MIRROR:
                strCDPType = "LVM����";
                break;    
            default:
                strCDPType = "δ֪����";
                break;
        }

        return strCDPType;
    }

    public static String getCDPStatusString(int nCDPStatus) {
        String strCDPStatus = "";
        switch (nCDPStatus) {
            case CDPConstants.CDP_STATUS_INIT:
                strCDPStatus = "δ����";
                break;
            case CDPConstants.CDP_STATUS_HOOK_SYNC_RUNNING:
                strCDPStatus = "��������ͬ��";
                break;
            case CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED:
                strCDPStatus = "����ͬ���ɹ�";
                break;
            case CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED:
                strCDPStatus = "����ͬ��ʧ��";
                break;
            case CDPConstants.CDP_STATUS_HOOK_RUNNING:
                strCDPStatus = "�������ݱ���";
                break;
            case CDPConstants.CDP_STATUS_HOOK_FAILED:
                strCDPStatus = "���ݱ���ʧ��";
                break;
            case CDPConstants.CAP_STATUS_HOOK_STOPED:
                strCDPStatus = "���ݱ���ֹͣ";
                break;
            default:
                strCDPStatus = "δ֪״̬";
                break;
        }

        return strCDPStatus;
    }

    public static String getRestoreStatusString(int nRestoreStatus) {
        String strRestoreStatus = "";
        switch (nRestoreStatus) {
            case CDPConstants.CDP_STATUS_HOOK_SYNC_RUNNING:
                strRestoreStatus = "���ڻָ�";
                break;
            case CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED:
                strRestoreStatus = "�ָ��ɹ�";
                break;
            case CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED:
                strRestoreStatus = "�ָ�ʧ��";
                break;
            default:
                strRestoreStatus = "δ֪״̬";
                break;
        }

        return strRestoreStatus;
    }

    public static String getErrorString(int nErrorCode) {
        String strError = "";
        switch (nErrorCode) {
            //CDP error
            case CDPConstants.ERROR_SYNC_READSOURCE:
                strError = "��ʼͬ��ʧ�ܣ���Դ����(����)��������";
                break;
            case CDPConstants.ERROR_SYNC_WRITEDEST:
                strError = "��ʼͬ��ʧ�ܣ�дĿ�����(����)��������";
                break;
            case CDPConstants.ERROR_HOOK_WRITE_DEST:
                strError = "���ݱ���ʧ�ܣ�дĿ�����(����)��������";
                break;
            case CDPConstants.ERROR_HOOK_WRITE_LOG_COMPLETED:
                strError = "���ݱ���ʧ�ܣ�дĿ�����(����)��������";
                break;
            case CDPConstants.ERROR_OPEN_SOURCE_PARTITION:
                strError = "��Դ����(����)ʧ��";
                break;
            case CDPConstants.ERROR_OPEN_TARGET_PARTITION:
                strError = "��Ŀ�����(����)ʧ��";
                break;
            case CDPConstants.ERROR_OPEN_TARGET_PARTITION_0:
                strError = "��Ŀ�����ʧ��";
                break;
            case CDPConstants.ERROR_ADDHOOK:
                strError = "��������(����)����ʧ��";
                break;
            case CDPConstants.ERROR_INPUT_TOO_SMALL:
                strError = "�ڴ��쳣";
                break;
            case CDPConstants.ERROR_OBJECT_IS_NOT_HOOK:
                strError = "Ŀ�����(����)���Ǳ���״̬";
                break;
            case CDPConstants.ERROR_TARGET_IS_TOO_SMALL:
                strError = "Ŀ�����(����)�ռ䲻��";
                break;
            case CDPConstants.ERROR_CREATE_SYNC_THREAD:
                strError = "������ʼͬ���̷߳�������";
                break;
            case CDPConstants.ERROR_CREATE_WRITE_THREAD:
                strError = "���������̷߳�������";
                break;
            case CDPConstants.ERROR_INVALID_HOOK_STATUS:
                strError = "Դ��(����)����״̬�쳣";
                break;

            case CDPConstants.ERROR_WRITE_CONFIG_FILE:
                strError = "����������Ϣʧ��";
                break;
            case CDPConstants.ERROR_GET_DISK_INFO:
                strError = "��ȡ������Ϣʧ��";
                break;
            case CDPConstants.ERROR_OPEN_DISK:
                strError = "�򿪴����豸ʧ��";
                break;
            case CDPConstants.ERROR_CONTROL_DRIVER:
                strError = "����CDP��������ʧ��";
                break;
            case CDPConstants.ERROR_CREATE_CDP_SOURCE_CREATED:
                strError = "����CDPʧ�ܣ�CDP�����Ѿ�����";
                break;
            case CDPConstants.ERROR_CREATE_CDP_TARGET_USED:
                strError = "����CDPʧ�ܣ�Ŀ���̻�������ڱ�ʹ��";
                break;
            case CDPConstants.ERROR_DELETE_CDP_SOURCE_RUNING:
                strError = "ɾ��CDPʧ��,Դ�̻�Դ�������ڱ�������";
                break;
            case CDPConstants.ERROR_START_CDP_SOURCE_RUNING:
                strError = "����CDPʧ��,Դ�̻�Դ�������ڱ�������";
                break;
            case CDPConstants.ERROR_START_CDP_RESTORE_RUNING:
                strError = "����CDPʧ��,Դ�̻�Դ�������ڻָ���";
                break;
            case CDPConstants.ERROR_START_CDP_UNMOUNT_FAILED:
                strError = "����CDPʧ��,ж��Ŀ���̻������������ʧ��";
                break;
            case CDPConstants.ERROR_START_CDP_LOCK_FAILED:
                strError = "����CDPʧ��,����Դ�̻�Դ����ʧ��";
                break;
            case CDPConstants.ERROR_STOP_CDP_UNLOCK_FAILED:
                strError = "ֹͣCDPʧ��,���Դ�̻�Դ��������״̬ʧ��";
                break;
            case CDPConstants.ERROR_RESTORE_INVALID_SOURCE_HOOK_STATUS:
                strError = "��CDP�ָ�ʧ�ܣ�Դ�̻�Դ�������ڱ�������";
                break;
            case CDPConstants.ERROR_RESTORE_INVALID_TARGET_HOOK_STATUS:
                strError = "��CDP�ָ�ʧ�ܣ�Ŀ���̻�������ڱ�ʹ����";
                break;
            case CDPConstants.ERROR_RESTORE_UNMOUNT_SOURCE:
                strError = "��CDP�ָ�ʧ�ܣ�ж���̻����������ʧ��";
                break;
            case CDPConstants.ERROR_RESTORE_LOCK_SOURCE:
                strError = "��CDP�ָ�ʧ�ܣ������̻����ʧ��";
                break;
            case CDPConstants.ERROR_STOP_RESTORE_SOURCE_HOOK_STATUS:
                strError = "ֹͣ�ָ�����,Դ��(����)����״̬�쳣";
                break;
            case CDPConstants.ERROR_STOP_RESTORE_UNLOCK_FAILED:
                strError = "ֹͣ�ָ�����,��������(����)ʧ��";
                break;
            case CDPConstants.ERROR_RESTORE_INVALID_TARGET_LOCK_STATUS:
                strError = "��CDP�ָ�ʧ�ܣ�Ŀ���̻������������״̬";
                break;

            //system error
            case CDPConstants.SYSERROR_CONNECT_CLIENT:
                strError = "���ӿͻ���ʧ��";
                break;
            case CDPConstants.SYSERROR_SEND_XML:
                strError = "��ͻ��˷�������ʧ��";
                break;
            case CDPConstants.SYSERROR_PARSE_XML:
                strError = "�����ͻ��˷��ؽ��ʧ��";
                break;

            default:
                strError = "δ֪����";
                break;
        }

        return strError;
    }

    public static void GetAppPath(Object obj) {
        //class file name
        String strClassName = obj.getClass().getName();
        int nIndex = strClassName.lastIndexOf(".");
        if (nIndex != -1) {
            strClassName = strClassName.substring(nIndex + 1) + ".class";
        }

        System.out.println("ClassName=" + strClassName);

        String strClassPath = obj.getClass().getResource(strClassName).getPath();
        if (strClassPath.endsWith("/")) {
            strClassPath = strClassPath.substring(0, strClassPath.length() - 1);
        }

        System.out.println("ClassPath=" + strClassPath);

        if (strClassPath.startsWith("file:")) {
            nIndex = strClassPath.lastIndexOf(".jar");
            if (nIndex != -1) {
                strClassPath = strClassPath.substring(5, nIndex);
            }

            nIndex = strClassPath.lastIndexOf("/");
            if (nIndex != -1) {
                strClassPath = strClassPath.substring(0, nIndex);
            }

            AppPath = strClassPath;

            /*nIndex = strClassPath.lastIndexOf("/");
            if (nIndex != -1)
            AppPath = strClassPath.substring(0, nIndex);*/
        } else {
            strClassPath = obj.getClass().getResource("/").getPath();
            if (strClassPath.endsWith("/")) {
                strClassPath = strClassPath.substring(0, strClassPath.length() - 1);
            }

            nIndex = strClassPath.lastIndexOf("/");
            if (nIndex != -1) {
                AppPath = strClassPath.substring(0, nIndex);
            }
        }

        AppPath = convertUTF8String(AppPath);

        System.out.println("AppPath=" + AppPath);
    }

    public static String convertUTF8String(String strUTF8) {
        String strUnicode = "";

        int nIndex = 0;
        boolean bFind = false;
        byte[] UTF8Values = new byte[256];
        int nValueCnt = 0;
        while (nIndex < strUTF8.length()) {
            char c = strUTF8.charAt(nIndex);
            if (c == '%') {
                String strValue = String.valueOf(strUTF8.charAt(nIndex + 1)) + String.valueOf(strUTF8.charAt(nIndex + 2));
                int nValue = Integer.parseInt(strValue, 16);
                byte b = nValue < 128 ? (byte) nValue : (byte) (nValue - 256);

                UTF8Values[nValueCnt] = b;
                nValueCnt++;

                nIndex += 3;
                continue;
            }

            if (nValueCnt > 0) {
                byte[] Values = new byte[nValueCnt];
                for (int i = 0; i < nValueCnt; i++) {
                    Values[i] = UTF8Values[i];
                }
                try {
                    String strTmp = new String(Values, "UTF-8");

                    strUnicode += strTmp;
                } catch (Exception e) {
                }

                nValueCnt = 0;
            }

            strUnicode += String.valueOf(c);
            nIndex++;
        }

        return strUnicode;
    }
    
    public String numToStr(Object num){
        return String.valueOf(num);
    }
}
