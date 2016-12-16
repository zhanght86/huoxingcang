/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.util;

import com.marstor.msa.cdp.model.CDPAgentPackageXML;
import static com.marstor.msa.cdp.util.Utility.AppPath;
import static com.marstor.msa.cdp.util.Utility.convertUTF8String;
import com.marstor.xml.XMLParser;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "util")
@ViewScoped
public class Utility {

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
        // 除数
        BigDecimal bd = new BigDecimal(lSize);
// 被除数
        BigDecimal bd2 = new BigDecimal(CDPConstants.GB);
// 进行除法运算,保留200位小数,末位使用四舍五入方式,返回结果
        BigDecimal result = bd.divide(bd2, 10, BigDecimal.ROUND_HALF_DOWN);
        System.out.println("---------------------Utility--result-------------" + result);
        String strSize = String.format("%.1f", result);

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
        Debug.print("After T:" + strByte);
        lRemain = lRemain % lTB;
        strByte += String.format("%03d,", lRemain / lGB);
        Debug.print("After G:" + strByte);
        lRemain = lRemain % lGB;
        strByte += String.format("%03d,", lRemain / lMB);
        Debug.print("After M:" + strByte);
        lRemain = lRemain % lMB;
        strByte += String.format("%03d,", lRemain / lKB);
        Debug.print("After K:" + strByte);
        lRemain = lRemain % lKB;
        strByte += String.format("%03d", lRemain);
        Debug.print("After 1:" + strByte);

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
                strPartitionType = "MBR分区";
                break;
            case CDPConstants.PARTITION_FAT_12:
                strPartitionType = "GPT分区";
                break;
            case CDPConstants.PARTITION_XENIX_1:
                strPartitionType = "RAW分区";
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

    public static String getLinuxPartitionTypeString(int nPartitionType) {
        String strPartitionType;
        switch (nPartitionType) {
            case CDPConstants.LINUX_PARTITION_EMPTY:
                strPartitionType = "Empty";
                break;
            case CDPConstants.LINUX_PARTITION_FAT_12:
                strPartitionType = "FAT12";
                break;
            case CDPConstants.LINUX_PARTITION_XENIX_1:
                strPartitionType = "XENIX root";
                break;
            case CDPConstants.LINUX_PARTITION_XENIX_2:
                strPartitionType = "XENIX usr";
                break;
            case CDPConstants.LINUX_PARTITION_FAT_16_1:
                strPartitionType = "FAT16 <32M";
                break;
            case CDPConstants.LINUX_PARTITION_EXTENDED:
                strPartitionType = "Extended";
                break;
            case CDPConstants.LINUX_PARTITION_FAT_16_2:
                strPartitionType = "FAT16";
                break;
            case CDPConstants.LINUX_PARTITION_HPFS_NTFS:
                strPartitionType = "HPFS/NTFS";
                break;
            case CDPConstants.LINUX_PARTITION_AIX_1:
                strPartitionType = "AIX";
                break;
            case CDPConstants.LINUX_PARTITION_AIX_2:
                strPartitionType = "AIX bootable";
                break;
            case CDPConstants.LINUX_PARTITION_0x0A:
                strPartitionType = "OS/2 Boot Manager";
                break;
            case CDPConstants.LINUX_PARTITION_0x0B:
                strPartitionType = "W95 FAT32";
                break;
            case CDPConstants.LINUX_PARTITION_0x0C:
                strPartitionType = "W95 FAT32 (LBA)";
                break;
            case CDPConstants.LINUX_PARTITION_0x0E:
                strPartitionType = "W95 FAT16 (LBA)";
                break;
            case CDPConstants.LINUX_PARTITION_0x0F:
                strPartitionType = "W95 Ext'd (LBA)";
                break;
            case CDPConstants.LINUX_PARTITION_0x10:
                strPartitionType = "OPUS";
                break;
            case CDPConstants.LINUX_PARTITION_0x11:
                strPartitionType = "Hidden FAT12";
                break;
            case CDPConstants.LINUX_PARTITION_0x12:
                strPartitionType = "Compaq diagnostics";
                break;
            case CDPConstants.LINUX_PARTITION_0x14:
                strPartitionType = "Hidden FAT16 <32M";
                break;
            case CDPConstants.LINUX_PARTITION_0x16:
                strPartitionType = "Hidden FAT16";
                break;
            case CDPConstants.LINUX_PARTITION_0x17:
                strPartitionType = "Hidden HPFS/NTFS";
                break;
            case CDPConstants.LINUX_PARTITION_0x18:
                strPartitionType = "AST SmartSleep";
                break;
            case CDPConstants.LINUX_PARTITION_0x1B:
                strPartitionType = "Hidden W95 FAT32";
                break;
            case CDPConstants.LINUX_PARTITION_0x1C:
                strPartitionType = "Hidden W95 FAT32 (LBA)";
                break;
            case CDPConstants.LINUX_PARTITION_0x1E:
                strPartitionType = "Hidden W95 FAT16 (LBA)";
                break;
            case CDPConstants.LINUX_PARTITION_0x24:
                strPartitionType = "NEC DOS";
                break;
            case CDPConstants.LINUX_PARTITION_0x39:
                strPartitionType = "Plan 9";
                break;
            case CDPConstants.LINUX_PARTITION_0x3C:
                strPartitionType = "PartitionMagic recovery";
                break;
            case CDPConstants.LINUX_PARTITION_0x40:
                strPartitionType = "Venix 80286";
                break;
            case CDPConstants.LINUX_PARTITION_0x41:
                strPartitionType = "PPC PReP Boot";
                break;
            case CDPConstants.LINUX_PARTITION_0x42:
                strPartitionType = "SFS";
                break;
            case CDPConstants.LINUX_PARTITION_0x4D:
                strPartitionType = "QNX4.x";
                break;
            case CDPConstants.LINUX_PARTITION_0x4E:
                strPartitionType = "QNX4.x 2nd part";
                break;
            case CDPConstants.LINUX_PARTITION_0x4F:
                strPartitionType = "QNX4.x 3rd part";
                break;
            case CDPConstants.LINUX_PARTITION_0x50:
                strPartitionType = "OnTrack DM";
                break;
            case CDPConstants.LINUX_PARTITION_0x51:
                strPartitionType = "OnTrack DM6 Aux1";
                break;
            case CDPConstants.LINUX_PARTITION_0x52:
                strPartitionType = "CP/M";
                break;
            case CDPConstants.LINUX_PARTITION_0x53:
                strPartitionType = "OnTrack DM6 Aux3";
                break;
            case CDPConstants.LINUX_PARTITION_0x54:
                strPartitionType = "OnTrackDM6";
                break;
            case CDPConstants.LINUX_PARTITION_0x55:
                strPartitionType = "EZ-Drive";
                break;
            case CDPConstants.LINUX_PARTITION_0x56:
                strPartitionType = "Golden Bow";
                break;
            case CDPConstants.LINUX_PARTITION_0x5C:
                strPartitionType = "Priam Edisk";
                break;
            case CDPConstants.LINUX_PARTITION_0x61:
                strPartitionType = "SpeedStor";
                break;
            case CDPConstants.LINUX_PARTITION_0x63:
                strPartitionType = "GNU HURD or SysV";
                break;
            case CDPConstants.LINUX_PARTITION_0x64:
                strPartitionType = "Novell Netware 286";
                break;
            case CDPConstants.LINUX_PARTITION_0x65:
                strPartitionType = "Novell Netware 386";
                break;
            case CDPConstants.LINUX_PARTITION_0x70:
                strPartitionType = "DiskSecure Multi-Boot";
                break;
            case CDPConstants.LINUX_PARTITION_0x75:
                strPartitionType = "PC/IX";
                break;
            case CDPConstants.LINUX_PARTITION_0x80:
                strPartitionType = "Old Minix";
                break;
            case CDPConstants.LINUX_PARTITION_0x81:
                strPartitionType = "Minix / old Linux";
                break;
            case CDPConstants.LINUX_PARTITION_0x82:
                strPartitionType = "Linux swap / Solaris";
                break;
            case CDPConstants.LINUX_PARTITION_0x83:
                strPartitionType = "Linux";
                break;
            case CDPConstants.LINUX_PARTITION_0x84:
                strPartitionType = "OS/2 hidden C: drive";
                break;
            case CDPConstants.LINUX_PARTITION_0x85:
                strPartitionType = "Linux extended";
                break;
            case CDPConstants.LINUX_PARTITION_0x86:
                strPartitionType = "NTFS volume set";
                break;
            case CDPConstants.LINUX_PARTITION_0x87:
                strPartitionType = "NTFS volume set";
                break;
            case CDPConstants.LINUX_PARTITION_0x88:
                strPartitionType = "Linux plaintext";
                break;
            case CDPConstants.LINUX_PARTITION_0x8E:
                strPartitionType = "Linux LVM";
                break;
            case CDPConstants.LINUX_PARTITION_0x93:
                strPartitionType = "Amoeba";
                break;
            case CDPConstants.LINUX_PARTITION_0x94:
                strPartitionType = "Amoeba BBT";
                break;
            case CDPConstants.LINUX_PARTITION_0x9F:
                strPartitionType = "BSD/OS";
                break;
            case CDPConstants.LINUX_PARTITION_0xA0:
                strPartitionType = "IBM Thinkpad hibernation";
                break;
            case CDPConstants.LINUX_PARTITION_0xA5:
                strPartitionType = "FreeBSD";
                break;
            case CDPConstants.LINUX_PARTITION_0xA6:
                strPartitionType = "OpenBSD";
                break;
            case CDPConstants.LINUX_PARTITION_0xA7:
                strPartitionType = "NeXTSTEP";
                break;
            case CDPConstants.LINUX_PARTITION_0xA8:
                strPartitionType = "Darwin UFS";
                break;
            case CDPConstants.LINUX_PARTITION_0xA9:
                strPartitionType = "NetBSD";
                break;
            case CDPConstants.LINUX_PARTITION_0xAB:
                strPartitionType = "Darwin boot";
                break;
            case CDPConstants.LINUX_PARTITION_0xB7:
                strPartitionType = "BSDI fs";
                break;
            case CDPConstants.LINUX_PARTITION_0xB8:
                strPartitionType = "BSDI swap";
                break;
            case CDPConstants.LINUX_PARTITION_0xBB:
                strPartitionType = "Boot Wizard hidden";
                break;
            case CDPConstants.LINUX_PARTITION_0xBE:
                strPartitionType = "Solaris boot";
                break;
            case CDPConstants.LINUX_PARTITION_0xBF:
                strPartitionType = "Solaris";
                break;
            case CDPConstants.LINUX_PARTITION_0xC1:
                strPartitionType = "DRDOS/sec (FAT-12)";
                break;
            case CDPConstants.LINUX_PARTITION_0xC4:
                strPartitionType = "DRDOS/sec (FAT-16 < 32M)";
                break;
            case CDPConstants.LINUX_PARTITION_0xC6:
                strPartitionType = "DRDOS/sec (FAT-16)";
                break;
            case CDPConstants.LINUX_PARTITION_0xC7:
                strPartitionType = "Syrinx";
                break;
            case CDPConstants.LINUX_PARTITION_0xDA:
                strPartitionType = "Non-FS data";
                break;
            case CDPConstants.LINUX_PARTITION_0xDB:
                strPartitionType = "CP/M / CTOS / ...";
                break;
            case CDPConstants.LINUX_PARTITION_0xDE:
                strPartitionType = "Dell Utility";
                break;
            case CDPConstants.LINUX_PARTITION_0xDF:
                strPartitionType = "BootIt";
                break;
            case CDPConstants.LINUX_PARTITION_0xE1:
                strPartitionType = "DOS access";
                break;
            case CDPConstants.LINUX_PARTITION_0xE3:
                strPartitionType = "DOS R/O";
                break;
            case CDPConstants.LINUX_PARTITION_0xE4:
                strPartitionType = "SpeedStor";
                break;
            case CDPConstants.LINUX_PARTITION_0xEB:
                strPartitionType = "BeOS fs";
                break;
            case CDPConstants.LINUX_PARTITION_0xEE:
                strPartitionType = "EFI GPT";
                break;
            case CDPConstants.LINUX_PARTITION_0xEF:
                strPartitionType = "EFI (FAT-12/16/32)";
                break;
            case CDPConstants.LINUX_PARTITION_0xF0:
                strPartitionType = "Linux/PA-RISC boot";
                break;
            case CDPConstants.LINUX_PARTITION_0xF1:
                strPartitionType = "SpeedStor";
                break;
            case CDPConstants.LINUX_PARTITION_0xF4:
                strPartitionType = "SpeedStor";
                break;
            case CDPConstants.LINUX_PARTITION_0xF2:
                strPartitionType = "DOS secondary";
                break;
            case CDPConstants.LINUX_PARTITION_0xFD:
                strPartitionType = "Linux raid autodetect";
                break;
            case CDPConstants.LINUX_PARTITION_0xFE:
                strPartitionType = "LANstep";
                break;
            case CDPConstants.LINUX_PARTITION_0xFF:
                strPartitionType = "BBT";
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

    public static String getCDPTypeString(int nCDPType, boolean isDest_Restore, String device, boolean isSrc_Restore) {
        String strCDPType = "";
        switch (nCDPType) {
            case CDPConstants.CDP_TYPE_INIT:
                if (isDest_Restore) {
                    strCDPType = device + "恢复";
                } else if (isSrc_Restore) {
                    strCDPType = device + "镜像";
                } else {
                    strCDPType = "未设置";
                }
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

    public static String getCDPStatusString(int nCDPStatus, int cdpType, int restoreStatus) {
        String strCDPStatus = "";
        switch (nCDPStatus) {
            case CDPConstants.CDP_STATUS_INIT:
                strCDPStatus = "";
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

        if (cdpType == CDPConstants.CDP_TYPE_INIT) {
            if (restoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
                strCDPStatus = "正在恢复";
            }
        }

        return strCDPStatus;
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

    public static String getWarningString(long nRestoreStatus) {
        String strRestoreStatus = "";
        if (nRestoreStatus == CDPConstants.WARNING_SYNC_READSRC_DATAERROR) {
            strRestoreStatus = "初始同步发现源磁盘有坏块";
        }


        return strRestoreStatus;
    }
    private static Properties prop = null;

    private static void initErrorMap() {
        try {
            prop = new Properties();
            prop.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("com/marstor/msa/cdp/res/ErrorCode.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static String getSystemErrorString(long nErrorCode) {
        Debug.printpage("Error Code" + nErrorCode);

        if (prop == null) {
            initErrorMap();
        }
        String code = Long.toHexString(nErrorCode);

        String strError = prop.getProperty(code);

        if (strError == null && !code.equals("0")) {
            strError = "系统错误代码" + code + "。具体原因请联系统管理员或咨询厂商。";
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

    public String numToStr(Object num) {
        return String.valueOf(num);
    }
    
    public static List<CDPAgentPackageXML> readCDPAgentPakageXML(ClassLoader classLoader) {
        List<CDPAgentPackageXML> mbaAgentList = new ArrayList();
        InputStream mbaInputStream = classLoader.getResourceAsStream("com/marstor/msa/cdp/res/cdp_package.xml");
        if (mbaInputStream != null) {
            XMLParser xmlPaser = new XMLParser(mbaInputStream);
            int iAgentCount = xmlPaser.getNodeCount("CDP/Setup/SetupItem");
            for (int i = 0; i < iAgentCount; i++) {
                XMLParser agentXMLParser = xmlPaser.createXMLParser("CDP/Setup/SetupItem", i);
                CDPAgentPackageXML mbaAgentPackageXML = new CDPAgentPackageXML();
                mbaAgentPackageXML.setStrAgentOS(agentXMLParser.getNodeContent("SetupItem/OS"));
                mbaAgentPackageXML.setStrAgentPlatform(agentXMLParser.getNodeContent("SetupItem/Platform"));
                mbaAgentPackageXML.setStrAgentName(agentXMLParser.getNodeContent("SetupItem/FileName"));
                mbaAgentPackageXML.setStrDescription(agentXMLParser.getNodeContent("SetupItem/Description"));
                mbaAgentList.add(mbaAgentPackageXML);
            }
        }
        return mbaAgentList;
    }
    
    public static List<CDPAgentPackageXML> readCDPJrePakageXML(ClassLoader classLoader) {
        List<CDPAgentPackageXML> mbaAgentList = new ArrayList();
        InputStream mbaInputStream = classLoader.getResourceAsStream("com/marstor/msa/cdp/res/jre_package.xml");
        if (mbaInputStream != null) {
            XMLParser xmlPaser = new XMLParser(mbaInputStream);
            int iAgentCount = xmlPaser.getNodeCount("JRE/Setup/SetupItem");
            for (int i = 0; i < iAgentCount; i++) {
                XMLParser agentXMLParser = xmlPaser.createXMLParser("JRE/Setup/SetupItem", i);
                CDPAgentPackageXML mbaAgentPackageXML = new CDPAgentPackageXML();
                mbaAgentPackageXML.setStrAgentOS(agentXMLParser.getNodeContent("SetupItem/OS"));
                mbaAgentPackageXML.setStrAgentPlatform(agentXMLParser.getNodeContent("SetupItem/Platform"));
                mbaAgentPackageXML.setStrAgentName(agentXMLParser.getNodeContent("SetupItem/FileName"));
                mbaAgentPackageXML.setStrDescription(agentXMLParser.getNodeContent("SetupItem/Description"));
                mbaAgentList.add(mbaAgentPackageXML);
            }
        }
        return mbaAgentList;
    }
}
