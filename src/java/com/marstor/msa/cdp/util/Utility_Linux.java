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
                strPartitionType = "未使用分区";
                break;
            case CDPConstants.PARTITION_FAT_12:
                strPartitionType = "FAT12 分区";
                break;
            case CDPConstants.PARTITION_XENIX_1:
                strPartitionType = "XENIX 分区";
                break;
            case CDPConstants.PARTITION_XENIX_2:
                strPartitionType = "XENIX 分区";
                break;
            case CDPConstants.PARTITION_FAT_16:
                strPartitionType = "FAT16 分区";
                break;
            case CDPConstants.PARTITION_EXTENDED:
                strPartitionType = "扩展分区";
                break;
            case CDPConstants.PARTITION_HUGE:
                strPartitionType = "大分区";
                break;
            case CDPConstants.PARTITION_IFS:
                strPartitionType = "IFS 分区";
                break;
            case CDPConstants.PARTITION_OS2BOOTMGR:
                strPartitionType = "OS2 Boot MGR 分区";
                break;
            case CDPConstants.PARTITION_FAT32:
                strPartitionType = "FAT32 分区";
                break;
            case CDPConstants.PARTITION_FAT32_XINT13:
                strPartitionType = "FAT32 分区";
                break;
            case CDPConstants.PARTITION_XINT13:
                strPartitionType = "XINT13 分区";
                break;
            case CDPConstants.PARTITION_XINT13_EXTENDED:
                strPartitionType = "XINT13 扩展分区";
                break;
            case CDPConstants.PARTITION_PREP:
                strPartitionType = "PREP 分区";
                break;
            case CDPConstants.PARTITION_LDM:
                strPartitionType = "LDM 分区";
                break;
            case CDPConstants.PARTITION_UNIX:
                strPartitionType = "UNIX 分区";
                break;
            default:
                strPartitionType = "未知分区";
                break;
        }

        return strPartitionType;
    }

    public static String getCDPTypeString(int nCDPType) {
        String strCDPType = "";
        switch (nCDPType) {
            case CDPConstants.CDP_TYPE_INIT:
                strCDPType = "未设置";
                break;
            case CDPConstants.CDP_TYPE_DISK:
                strCDPType = "磁盘保护";
                break;
            case CDPConstants.CDP_TYPE_PARTITION:
                strCDPType = "分区保护";
                break;
            case CDPConstants.CDP_TYPE_DISK_MIRROR:
                strCDPType = "磁盘镜像";
                break;
            case CDPConstants.CDP_TYPE_PARTITION_MIRROR:
                strCDPType = "分区镜像";
                break;
            case CDPConstants.CDP_TYPE_VOLUME:
                strCDPType = "LVM卷保护";
                break;
            case CDPConstants.CDP_TYPE_VOLUME_MIRROR:
                strCDPType = "LVM卷镜像";
                break;    
            default:
                strCDPType = "未知分区";
                break;
        }

        return strCDPType;
    }

    public static String getCDPStatusString(int nCDPStatus) {
        String strCDPStatus = "";
        switch (nCDPStatus) {
            case CDPConstants.CDP_STATUS_INIT:
                strCDPStatus = "未启动";
                break;
            case CDPConstants.CDP_STATUS_HOOK_SYNC_RUNNING:
                strCDPStatus = "正在数据同步";
                break;
            case CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED:
                strCDPStatus = "数据同步成功";
                break;
            case CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED:
                strCDPStatus = "数据同步失败";
                break;
            case CDPConstants.CDP_STATUS_HOOK_RUNNING:
                strCDPStatus = "正在数据保护";
                break;
            case CDPConstants.CDP_STATUS_HOOK_FAILED:
                strCDPStatus = "数据保护失败";
                break;
            case CDPConstants.CAP_STATUS_HOOK_STOPED:
                strCDPStatus = "数据保护停止";
                break;
            default:
                strCDPStatus = "未知状态";
                break;
        }

        return strCDPStatus;
    }

    public static String getRestoreStatusString(int nRestoreStatus) {
        String strRestoreStatus = "";
        switch (nRestoreStatus) {
            case CDPConstants.CDP_STATUS_HOOK_SYNC_RUNNING:
                strRestoreStatus = "正在恢复";
                break;
            case CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED:
                strRestoreStatus = "恢复成功";
                break;
            case CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED:
                strRestoreStatus = "恢复失败";
                break;
            default:
                strRestoreStatus = "未知状态";
                break;
        }

        return strRestoreStatus;
    }

    public static String getErrorString(int nErrorCode) {
        String strError = "";
        switch (nErrorCode) {
            //CDP error
            case CDPConstants.ERROR_SYNC_READSOURCE:
                strError = "初始同步失败，读源磁盘(分区)发生错误";
                break;
            case CDPConstants.ERROR_SYNC_WRITEDEST:
                strError = "初始同步失败，写目标磁盘(分区)发生错误";
                break;
            case CDPConstants.ERROR_HOOK_WRITE_DEST:
                strError = "数据保护失败，写目标磁盘(分区)发生错误";
                break;
            case CDPConstants.ERROR_HOOK_WRITE_LOG_COMPLETED:
                strError = "数据保护失败，写目标磁盘(分区)发生错误";
                break;
            case CDPConstants.ERROR_OPEN_SOURCE_PARTITION:
                strError = "打开源磁盘(分区)失败";
                break;
            case CDPConstants.ERROR_OPEN_TARGET_PARTITION:
                strError = "打开目标磁盘(分区)失败";
                break;
            case CDPConstants.ERROR_OPEN_TARGET_PARTITION_0:
                strError = "打开目标磁盘失败";
                break;
            case CDPConstants.ERROR_ADDHOOK:
                strError = "启动磁盘(分区)保护失败";
                break;
            case CDPConstants.ERROR_INPUT_TOO_SMALL:
                strError = "内存异常";
                break;
            case CDPConstants.ERROR_OBJECT_IS_NOT_HOOK:
                strError = "目标磁盘(分区)不是保护状态";
                break;
            case CDPConstants.ERROR_TARGET_IS_TOO_SMALL:
                strError = "目标磁盘(分区)空间不足";
                break;
            case CDPConstants.ERROR_CREATE_SYNC_THREAD:
                strError = "创建初始同步线程发生错误";
                break;
            case CDPConstants.ERROR_CREATE_WRITE_THREAD:
                strError = "创建保护线程发生错误";
                break;
            case CDPConstants.ERROR_INVALID_HOOK_STATUS:
                strError = "源盘(分区)保护状态异常";
                break;

            case CDPConstants.ERROR_WRITE_CONFIG_FILE:
                strError = "保存配置信息失败";
                break;
            case CDPConstants.ERROR_GET_DISK_INFO:
                strError = "获取磁盘信息失败";
                break;
            case CDPConstants.ERROR_OPEN_DISK:
                strError = "打开磁盘设备失败";
                break;
            case CDPConstants.ERROR_CONTROL_DRIVER:
                strError = "调用CDP驱动程序失败";
                break;
            case CDPConstants.ERROR_CREATE_CDP_SOURCE_CREATED:
                strError = "创建CDP失败，CDP保护已经存在";
                break;
            case CDPConstants.ERROR_CREATE_CDP_TARGET_USED:
                strError = "创建CDP失败，目标盘或分区正在被使用";
                break;
            case CDPConstants.ERROR_DELETE_CDP_SOURCE_RUNING:
                strError = "删除CDP失败,源盘或源分区正在被保护中";
                break;
            case CDPConstants.ERROR_START_CDP_SOURCE_RUNING:
                strError = "启动CDP失败,源盘或源分区正在被保护中";
                break;
            case CDPConstants.ERROR_START_CDP_RESTORE_RUNING:
                strError = "启动CDP失败,源盘或源分区正在恢复中";
                break;
            case CDPConstants.ERROR_START_CDP_UNMOUNT_FAILED:
                strError = "启动CDP失败,卸载目标盘或分区驱动器号失败";
                break;
            case CDPConstants.ERROR_START_CDP_LOCK_FAILED:
                strError = "启动CDP失败,锁定源盘或源分区失败";
                break;
            case CDPConstants.ERROR_STOP_CDP_UNLOCK_FAILED:
                strError = "停止CDP失败,解除源盘或源分区锁定状态失败";
                break;
            case CDPConstants.ERROR_RESTORE_INVALID_SOURCE_HOOK_STATUS:
                strError = "从CDP恢复失败，源盘或源分区正在被保护中";
                break;
            case CDPConstants.ERROR_RESTORE_INVALID_TARGET_HOOK_STATUS:
                strError = "从CDP恢复失败，目标盘或分区正在被使用中";
                break;
            case CDPConstants.ERROR_RESTORE_UNMOUNT_SOURCE:
                strError = "从CDP恢复失败，卸载盘或分区驱动号失败";
                break;
            case CDPConstants.ERROR_RESTORE_LOCK_SOURCE:
                strError = "从CDP恢复失败，锁定盘或分区失败";
                break;
            case CDPConstants.ERROR_STOP_RESTORE_SOURCE_HOOK_STATUS:
                strError = "停止恢复出错,源盘(分区)保护状态异常";
                break;
            case CDPConstants.ERROR_STOP_RESTORE_UNLOCK_FAILED:
                strError = "停止恢复出错,解锁磁盘(分区)失败";
                break;
            case CDPConstants.ERROR_RESTORE_INVALID_TARGET_LOCK_STATUS:
                strError = "从CDP恢复失败，目标盘或分区处于锁定状态";
                break;

            //system error
            case CDPConstants.SYSERROR_CONNECT_CLIENT:
                strError = "连接客户端失败";
                break;
            case CDPConstants.SYSERROR_SEND_XML:
                strError = "向客户端发送命令失败";
                break;
            case CDPConstants.SYSERROR_PARSE_XML:
                strError = "解析客户端返回结果失败";
                break;

            default:
                strError = "未知错误";
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
