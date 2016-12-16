/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.util;

import com.marstor.msa.common.util.MSAResource;
import java.net.URL;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "ccc")
@ViewScoped
public class CDPConstants {
        //log full option
    public static final int OPTION_AUTO_LOOP = 1;
    public static final int OPTION_STOP_CDP = 2;
    public static final int OPTION_STOP_WRITE = 3;
    public static String CDP = "CDP";
    public static final int TRUE = 1;
    public static final int FALSE= 0;
    public static final int ONLINE = 1;
    public static final int OFFLINE= 0;
    public static final int LOCAL = 1;
    public static final int REMOTE= 0;
    public static final int WAITING= 0;
    public static final int EXECUTING = 1;
    public static final int SUSPEND= 2;
    public static final int SUCCESSED = 3;
    public static final int FAILED= 4;
    public static final int SUSPENDING= 5;
    public static final int DELETEING= 6;
    public static final int DELETE= 7;
    public static final int CLOSEING= 8;
    public static final int CLOSE= 9;
    public static final int ROLLBACK_STATUS_RUNNING = 1;
    public static final int ROLLBACK_STATUS_COMPLETED = 2;
    public static final int ROLLBACK_STATUS_ERROR = 3;
    public static final int ROLLBACK_STATUS_CANCELED = 4;
    public static final int DISK_CREATED = 0;
    public static final int DISK_CREATING = 1;
    public static final int DISK_FAILED = 2;
    
    public static final String bundleFile = "com/marstor/msa/cdp/resource/MSACDPModuleClient";
    public static final String resource = "/com/marstor/msa/cdp/resource";
    
    
    public static final String SNAPSHOT_COPY = "_SC";
    
    public static URL IMG_URL_CDP_ROOT;
    public static URL IMG_URL_CDP_CLIENTS;
    public static URL IMG_URL_CDP_CLIENT;
    public static URL IMG_URL_CDP_CLIENT_DISK;
    public static URL IMG_URL_CDP_CLIENT_PARTITION;
    public static URL IMG_URL_CDP_DISKS;
    public static URL IMG_URL_CDP_DISKS_CONSISTENCYGROUPS;
    public static URL IMG_URL_CDP_DISKS_CONSISTENCYGROUP;
    public static URL IMG_URL_CDP_DISK;
    private static MSAResource res = new MSAResource();
    
    
    public static void initImgUrl() {
        
        IMG_URL_CDP_ROOT = CDPConstants.class.getResource(resource + "/CDP.png");
        IMG_URL_CDP_CLIENTS = CDPConstants.class.getResource(resource + "/clients.png");
        IMG_URL_CDP_CLIENT = CDPConstants.class.getResource(resource + "/client.png");
        IMG_URL_CDP_CLIENT_DISK = CDPConstants.class.getResource(resource + "/client_disk.png");
        IMG_URL_CDP_CLIENT_PARTITION = CDPConstants.class.getResource(resource + "/client_partition.png");
        IMG_URL_CDP_DISKS = CDPConstants.class.getResource(resource + "/disks.png");
        IMG_URL_CDP_DISKS_CONSISTENCYGROUPS = CDPConstants.class.getResource(resource + "/disks_consistant_groups.png");
        IMG_URL_CDP_DISKS_CONSISTENCYGROUP = CDPConstants.class.getResource(resource + "/disks_consistant_group.png");
        IMG_URL_CDP_DISK = CDPConstants.class.getResource(resource + "/cdp_disk.png");
        
    }
    
    public static final int OS_TYPE_WINDOWS = 1;
    public static final int OS_TYPE_LINUX = 2;
    public static final int OS_TYPE_SOLARIS = 3;
    public static final int OS_TYPE_AIX = 4;
    public static final int OS_TYPE_HPUNIX = 5;
    
    public static final int PROTECT_NO = 0;
    public static final int PROTECT_SNAPSHOT = 1;
    public static final int PROTECT_RECORD = 2;
    
    public static String getCDPLevelStr(int level){
        switch(level){
            case PROTECT_NO : return res.get("cdp.cdp_level", "mirror");
            case PROTECT_SNAPSHOT : return res.get("cdp.cdp_level", "snapshot");
            case PROTECT_RECORD : return res.get("cdp.cdp_level", "record");
            default : return "";
        }
    }
    
    public static String getOSTypeFromInt(int type){
        //<!--1 windows 2 Linux 3 Solaris 4 AIX 5 HP-UNIX 0 Other-->
        
        switch(type){
            case 1: return "Windows";
            case 2: return "Linux";
            case 3: return "Solaris";
            case 4: return "AIX";
            case 5: return "HP-UNIX";  
            default: return "Other";
        }
    }
    
    public static final long KB = 1024;
    public static final long MB = 1024 * KB;
    public static final long GB = 1024 * MB;
    public static final long TB = 1024 * GB;
    
    public static final int MAX_SOURCE_DISK = 100;
    public static final int MAX_SOURCE_PARTITION = 1000;
    public static final int MAX_MIRROR_DISK = 100;
    public static final int MAX_MIRROR_PARTITION = 1000;
        
    //default system parameters
    public static String CDP_CLIENT_IP = "192.168.2.20";
    public static int CDP_CLIENT_PORT = 1100;
    
    //node type
    public static final int NODE_TYPE_DEFAULT = 0;
    public static final int NODE_TYPE_CLIENTS = 1;
    public static final int NODE_TYPE_CLIENT = 2;
    public static final int NODE_TYPE_DISK = 3;
    public static final int NODE_TYPE_PARTITION = 4;
    
    //partition type
    public static final int PARTITION_ENTRY_UNUSED = 0x00;      // Entry unused
    public static final int PARTITION_FAT_12 = 0x01;     // 12-bit FAT entries
    public static final int PARTITION_XENIX_1 = 0x02;    // Xenix
    public static final int PARTITION_XENIX_2 = 0x03;    // Xenix
    public static final int PARTITION_FAT_16 = 0x04;   // 16-bit FAT entries
    public static final int PARTITION_EXTENDED = 0x05;    // Extended partition entry
    public static final int PARTITION_HUGE = 0x06;  // Huge partition MS-DOS V4
    public static final int PARTITION_IFS = 0x07;  // IFS Partition
    public static final int PARTITION_OS2BOOTMGR = 0x0A;   // OS/2 Boot Manager/OPUS/Coherent swap
    public static final int PARTITION_FAT32 = 0x0B;   // FAT32
    public static final int PARTITION_FAT32_XINT13 = 0x0C;    // FAT32 using extended int13 services
    public static final int PARTITION_XINT13 = 0x0E;  // Win95 partition using extended int13 services
    public static final int PARTITION_XINT13_EXTENDED = 0x0F;  // Same as type 5 but uses extended int13 services
    public static final int PARTITION_PREP = 0x41;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int PARTITION_LDM = 0x42;  // Logical Disk Manager partition
    public static final int PARTITION_UNIX = 0x63;   // Unix
    
       //linux patition type
    public static final int LINUX_PARTITION_EMPTY = 0x00;      // Entry unused
    public static final int LINUX_PARTITION_FAT_12 = 0x01;     // 12-bit FAT entries
    public static final int LINUX_PARTITION_XENIX_1 = 0x02;    // Xenix
    public static final int LINUX_PARTITION_XENIX_2 = 0x03;    // Xenix
    public static final int LINUX_PARTITION_FAT_16_1 = 0x04;   // 16-bit FAT entries
    public static final int LINUX_PARTITION_EXTENDED = 0x05;    // Extended partition entry
    public static final int LINUX_PARTITION_FAT_16_2 = 0x06;  // Huge partition MS-DOS V4
    public static final int LINUX_PARTITION_HPFS_NTFS = 0x07;  // IFS Partition
    public static final int LINUX_PARTITION_AIX_1 = 0x08;      // Entry unused
    public static final int LINUX_PARTITION_AIX_2 = 0x09;     // 12-bit FAT entries
    public static final int LINUX_PARTITION_0x0A = 0x0A;   // OS/2 Boot Manager/OPUS/Coherent swap
    public static final int LINUX_PARTITION_0x0B = 0x0B;   // FAT32
    public static final int LINUX_PARTITION_0x0C = 0x0C;    // FAT32 using extended int13 services
    public static final int LINUX_PARTITION_0x0E = 0x0E;  // Win95 partition using extended int13 services
    public static final int LINUX_PARTITION_0x0F = 0x0F;  // Same as type 5 but uses extended int13 services
    public static final int LINUX_PARTITION_0x10 = 0x10;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x11 = 0x11;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x12 = 0x12;   // Unix
    public static final int LINUX_PARTITION_0x14 = 0x14;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x16 = 0x16;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x17 = 0x17;   // Unix
    public static final int LINUX_PARTITION_0x18 = 0x18;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x1B = 0x1B;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x1C = 0x1C;   // Unix
    public static final int LINUX_PARTITION_0x1E = 0x1E;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x24 = 0x24;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x39 = 0x39;   // Unix
    public static final int LINUX_PARTITION_0x3C = 0x3C;   // OS/2 Boot Manager/OPUS/Coherent swap
    public static final int LINUX_PARTITION_0x40 = 0x40;   // FAT32
    public static final int LINUX_PARTITION_0x41 = 0x41;    // FAT32 using extended int13 services
    public static final int LINUX_PARTITION_0x42 = 0x42;  // Win95 partition using extended int13 services
    public static final int LINUX_PARTITION_0x4D = 0x4D;  // Same as type 5 but uses extended int13 services
    public static final int LINUX_PARTITION_0x4E = 0x4E;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x4F = 0x4F;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x50 = 0x50;   // Unix
    public static final int LINUX_PARTITION_0x51 = 0x51;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x52 = 0x52;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x53 = 0x53;   // Unix
    public static final int LINUX_PARTITION_0x54 = 0x54;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x55 = 0x55;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x56 = 0x56;   // Unix
    public static final int LINUX_PARTITION_0x5C = 0x5C;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x61 = 0x61;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x63 = 0x63;   // Unix
    public static final int LINUX_PARTITION_0x64 = 0x64;   // OS/2 Boot Manager/OPUS/Coherent swap
    public static final int LINUX_PARTITION_0x65 = 0x65;   // FAT32
    public static final int LINUX_PARTITION_0x70 = 0x70;    // FAT32 using extended int13 services
    public static final int LINUX_PARTITION_0x75 = 0x75;  // Win95 partition using extended int13 services
    public static final int LINUX_PARTITION_0x80 = 0x80;  // Same as type 5 but uses extended int13 services
    public static final int LINUX_PARTITION_0x81 = 0x81;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x82 = 0x82;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x83 = 0x83;   // Unix
    public static final int LINUX_PARTITION_0x84 = 0x84;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x85 = 0x85;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x86 = 0x86;   // Unix
    public static final int LINUX_PARTITION_0x87 = 0x87;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x88 = 0x88;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x8E = 0x8E;   // Unix
    public static final int LINUX_PARTITION_0x93 = 0x93;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0x94 = 0x94;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0x9F = 0x9F;   // Unix
    public static final int LINUX_PARTITION_0xA0 = 0xA0;   // OS/2 Boot Manager/OPUS/Coherent swap
    public static final int LINUX_PARTITION_0xA5 = 0xA5;   // FAT32
    public static final int LINUX_PARTITION_0xA6 = 0xA6;    // FAT32 using extended int13 services
    public static final int LINUX_PARTITION_0xA7 = 0xA7;  // Win95 partition using extended int13 services
    public static final int LINUX_PARTITION_0xA8 = 0xA8;  // Same as type 5 but uses extended int13 services
    public static final int LINUX_PARTITION_0xA9 = 0xA9;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0xAB = 0xAB;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0xB7 = 0xB7;   // Unix
    public static final int LINUX_PARTITION_0xB8 = 0xB8;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0xBB = 0xBB;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0xBE = 0xBE;   // Unix
    public static final int LINUX_PARTITION_0xBF = 0xBF;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0xC1 = 0xC1;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0xC4 = 0xC4;   // Unix
    public static final int LINUX_PARTITION_0xC6 = 0xC6;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0xC7 = 0xC7;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0xDA = 0xDA;   // Unix
    public static final int LINUX_PARTITION_0xDB = 0xDB;   // Unix
    public static final int LINUX_PARTITION_0xDE = 0xDE;   // OS/2 Boot Manager/OPUS/Coherent swap
    public static final int LINUX_PARTITION_0xDF = 0xDF;   // FAT32
    public static final int LINUX_PARTITION_0xE1 = 0xE1;    // FAT32 using extended int13 services
    public static final int LINUX_PARTITION_0xE3 = 0xE3;  // Win95 partition using extended int13 services
    public static final int LINUX_PARTITION_0xE4 = 0xE4;  // Same as type 5 but uses extended int13 services
    public static final int LINUX_PARTITION_0xEB = 0xEB;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0xEE = 0xEE;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0xEF = 0xEF;   // Unix
    public static final int LINUX_PARTITION_0xF0 = 0xF0;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0xF1 = 0xF1;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0xF4 = 0xF4;   // Unix
    public static final int LINUX_PARTITION_0xF2 = 0xF2;   // PowerPC Reference Platform (PReP) Boot Partition
    public static final int LINUX_PARTITION_0xFD = 0xFD;  // Logical Disk Manager partition
    public static final int LINUX_PARTITION_0xFE = 0xFE;   // Unix
    public static final int LINUX_PARTITION_0xFF = 0xFF;   // PowerPC Reference Platform (PReP) Boot Partition
    
    
    //CDP type
    public static final int CDP_TYPE_INIT = 0;
    public static final int CDP_TYPE_DISK = 1;
    public static final int CDP_TYPE_PARTITION = 2;
    public static final int CDP_TYPE_DISK_MIRROR = 3;
    public static final int CDP_TYPE_PARTITION_MIRROR = 4;
    public static final int CDP_TYPE_VOLUME = 5;
    public static final int CDP_TYPE_VOLUME_MIRROR = 6;
    
    
    //CDP status
    public static final int CDP_STATUS_INIT = 0;
    public static final int CDP_STATUS_HOOK_SYNC_RUNNING = 1;
    public static final int CDP_STATUS_HOOK_SYNC_COMPLETED = 2;
    public static final int CDP_STATUS_HOOK_SYNC_FAILED = 3;
    public static final int CDP_STATUS_HOOK_RUNNING = 4;
    public static final int CDP_STATUS_HOOK_FAILED = 5;
    public static final int CAP_STATUS_HOOK_STOPED =6;
    
    public static final int WARNING_SYNC_READSRC_DATAERROR = 23001;
    
    //mirror CDP status
    public static final int CDP_MIRROR_STATUS_INIT = 0;
    public static final int CDP_MIRROR_STATUS_USED = 1;
    
    //restore type
    public static final int CDP_RESTORE_TYPE_INIT = 0;
    public static final int CDP_RESTORE_TYPE_DISK = 1;
    public static final int CDP_RESTORE_TYPE_PARTITION = 2;
    public static final int CDP_RESTORE_TYPE_VOLUME = 5;
    
    //restore status
    public static final int CDP_RESTORE_STATUS_INIT = 0;
    public static final int CDP_RESTORE_STATUS_RUNNING = 1;
    
    //CDP error
    public static final int ERROR_SUCCESS = 0;      //成功
    
    public static final int ERROR_SYNC_READSOURCE = 13001;      //初始同步失败，读源磁盘(分区)发生错误。
    public static final int ERROR_SYNC_WRITEDEST = 13002;       //初始同步失败，写目标磁盘(分区)发生错误。
    public static final int ERROR_HOOK_WRITE_DEST = 13003;      //数据保护失败，写目标磁盘(分区)发生错误。
    public static final int ERROR_HOOK_WRITE_LOG_COMPLETED = 13004; //数据保护失败，写目标磁盘(分区)发生错误。
    public static final int ERROR_OPEN_SOURCE_PARTITION = 13005;    //打开源磁盘(分区)失败
    public static final int ERROR_OPEN_TARGET_PARTITION = 13006;    //打开目标磁盘(分区)失败
    public static final int ERROR_OPEN_TARGET_PARTITION_0 = 13007;  //打开目标磁盘失败
    public static final int ERROR_ADDHOOK = 13008;              //启动磁盘(分区)保护失败
    public static final int ERROR_INPUT_TOO_SMALL = 13009;      //内存异常
    public static final int ERROR_OBJECT_IS_NOT_HOOK = 13010;   //目标磁盘(分区)不是保护状态
    public static final int ERROR_TARGET_IS_TOO_SMALL = 13011;  //目标磁盘(分区)空间不足
    public static final int ERROR_CREATE_SYNC_THREAD = 13012;   //创建初始同步线程发生错误
    public static final int ERROR_CREATE_WRITE_THREAD = 13013;  //创建保护线程发生错误
    public static final int ERROR_INVALID_HOOK_STATUS = 13014;  //源盘(分区)保护状态异常
    
    public static final int ERROR_WRITE_CONFIG_FILE = 13100;            //保存配置信息失败
    public static final int ERROR_GET_DISK_INFO = 13101;                //获取磁盘信息失败
    public static final int ERROR_OPEN_DISK = 13102;                    //打开磁盘设备失败
    public static final int ERROR_CONTROL_DRIVER = 13103;               //调用CDP驱动程序失败
    public static final int ERROR_CREATE_CDP_SOURCE_CREATED = 13104;    //创建CDP失败，CDP保护已经存在
    public static final int ERROR_CREATE_CDP_TARGET_USED = 13105;       //创建CDP失败，目标盘或分区正在被使用
    public static final int ERROR_DELETE_CDP_SOURCE_RUNING = 13106;     //删除CDP失败,源盘或源分区正在被保护中
    public static final int ERROR_START_CDP_SOURCE_RUNING = 13107;      //启动CDP失败,源盘或源分区正在被保护中
    public static final int ERROR_START_CDP_RESTORE_RUNING = 13108;     //启动CDP失败,源盘或源分区正在恢复中
    public static final int ERROR_START_CDP_UNMOUNT_FAILED = 13109;     //启动CDP失败,卸载目标盘或分区驱动器号失败
    public static final int ERROR_START_CDP_LOCK_FAILED = 13110;        //启动CDP失败,锁定源盘或源分区失败
    public static final int ERROR_STOP_CDP_UNLOCK_FAILED = 13111;       //停止CDP失败,解除源盘或源分区锁定状态失败
    public static final int ERROR_RESTORE_INVALID_SOURCE_HOOK_STATUS = 13112;  //从CDP恢复失败，源盘或源分区正在被保护中
    public static final int ERROR_RESTORE_INVALID_TARGET_HOOK_STATUS = 13113;  //从CDP恢复失败，目标盘或分区正在被使用中
    public static final int ERROR_RESTORE_UNMOUNT_SOURCE = 13114;       //从CDP恢复失败，卸载盘或分区驱动号失败
    public static final int ERROR_RESTORE_LOCK_SOURCE = 13115;          //从CDP恢复失败，锁定盘或分区失败
    public static final int ERROR_STOP_RESTORE_SOURCE_HOOK_STATUS = 13116;      //停止恢复出错,源盘(分区)保护状态异常
    public static final int ERROR_STOP_RESTORE_UNLOCK_FAILED = 13117;           //停止恢复出错,解锁磁盘(分区)失败
    public static final int ERROR_RESTORE_INVALID_TARGET_LOCK_STATUS = 13118;   //从CDP恢复失败，目标盘或分区处于锁定状态
    
    //system error
    public static final int SYSERROR_BASE = 20000;
    
    public static final int SYSERROR_CONNECT_CLIENT = SYSERROR_BASE + 1;    //连接客户端失败
    public static final int SYSERROR_SEND_XML = SYSERROR_BASE + 2;          //向客户端发送命令失败
    public static final int SYSERROR_PARSE_XML = SYSERROR_BASE + 3;         //解析客户端返回结果失败
    
    public static URL IMG_URL_LOGO32;
    public static URL IMG_URL_LOGO16;
    
    public static URL IMG_URL_HELP;
    public static URL IMG_URL_ROOT;
    public static URL IMG_URL_CLIENTS;
    public static URL IMG_URL_CLIENT;
    public static URL IMG_URL_DISK;
    public static URL IMG_URL_PARTITION;

    public static void SetParams(String strClientIP, int nClientPort)
    {
        CDP_CLIENT_IP = strClientIP;
        CDP_CLIENT_PORT = nClientPort;
    }
}
