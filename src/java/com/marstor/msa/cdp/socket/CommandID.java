/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.socket;

/**
 *
 * @author Administrator
 */
public class CommandID {

    public final static int CLIENT_COMMAND_BASE = 1000000;
    public final static int GET_SOURCE_DISKS_ID = CLIENT_COMMAND_BASE + 0;
    public final static String GET_SOURCE_DISKS_STR = "Get Source Disks Information";
    public final static int GET_SOURCE_DISKS_RETURN_ID = CLIENT_COMMAND_BASE + 1;
    public final static String GET_SOURCE_DISKS_RETURN_STR = "Source Disks Information";
    public final static int GET_MIRROR_DISKS_ID = CLIENT_COMMAND_BASE + 2;
    public final static String GET_MIRROR_DISKS_STR = "Get Mirror Disks Information";
    public final static int GET_MIRROR_DISKS_RETURN_ID = CLIENT_COMMAND_BASE + 3;
    public final static String GET_MIRROR_DISKS_RETURN_STR = "Mirror Disks Information";
    public final static int CREATE_CDP_ID = CLIENT_COMMAND_BASE + 4;
    public final static String CREATE_CDP_STR = "Create CDP";
    public final static int CREATE_CDP_RETURN_ID = CLIENT_COMMAND_BASE + 5;
    public final static String CREATE_CDP_RETURN_STR = "Create CDP Return";
    public final static int DELETE_CDP_ID = CLIENT_COMMAND_BASE + 6;
    public final static String DELETE_CDP_STR = "Delete CDP";
    public final static int DELETE_CDP_RETURN_ID = CLIENT_COMMAND_BASE + 7;
    public final static String DELETE_CDP_RETURN_STR = "Delete CDP Return";
    public final static int START_CDP_ID = CLIENT_COMMAND_BASE + 8;
    public final static String START_CDP_STR = "Start CDP";
    public final static int START_CDP_SYN_ID = 1000008;
    public final static String START_CDP_SYN_STR = "Start CDP Syn";
    public final static int START_CDP_RETURN_ID = CLIENT_COMMAND_BASE + 9;
    public final static String START_CDP_RETURN_STR = "Start CDP Return";
    public final static int STOP_CDP_ID = CLIENT_COMMAND_BASE + 10;
    public final static String STOP_CDP_STR = "Stop CDP";
    public final static int STOP_CDP_RETURN_ID = CLIENT_COMMAND_BASE + 11;
    public final static String STOP_CDP_RETURN_STR = "Stop CDP Return";
    public final static int GET_SOURCE_DISK_DETAIL_ID = CLIENT_COMMAND_BASE + 12;
    public final static String GET_SOURCE_DISK_DETAIL_STR = "Get Source Disk Detail";
    public final static int GET_SOURCE_DISK_DETAIL_RETURN_ID = CLIENT_COMMAND_BASE + 13;
    public final static String GET_SOURCE_DISK_DETAIL_RETURN_STR = "Source Disk Detail";
    public final static int GET_SOURCE_PARTITION_DETAIL_ID = CLIENT_COMMAND_BASE + 14;
    public final static String GET_SOURCE_PARTITION_DETAIL_STR = "Get Source Partition Detail";
    public final static int GET_SOURCE_PARTITION_DETAIL_RETURN_ID = CLIENT_COMMAND_BASE + 15;
    public final static String GET_SOURCE_PARTITION_DETAIL_RETURN_STR = "Source Partition Detail";
    public final static int RESTORE_DATA_ID = CLIENT_COMMAND_BASE + 16;
    public final static String RESTORE_DATA_STR = "Restore Data";
    public final static int RESTORE_DATA_RETURN_ID = CLIENT_COMMAND_BASE + 17;
    public final static String RESTORE_DATA_RETURN_STR = "Restore Data Return";
    public final static int CANCEL_RESTORE_ID = CLIENT_COMMAND_BASE + 18;
    public final static String CANCEL_RESTORE_STR = "Cancel Restore Data";
    public final static int CANCEL_RESTORE_RETURN_ID = CLIENT_COMMAND_BASE + 19;
    public final static String CANCEL_RESTORE_RETURN_STR = "Cancel Restore Data Return";
    public final static int GET_VGS_ID = CLIENT_COMMAND_BASE + 20;
    public final static String GET_VGS_STR = "Get Source Volume Groups Information";
    public final static int GET_MIRROR_VGS_ID = CLIENT_COMMAND_BASE + 22;
    public final static String GET_MIRROR_VGS_STR = "Get Mirrot Volume Groups Information";
    public final static int GET_VOLUME_DETAIL_ID = CLIENT_COMMAND_BASE + 24;
    public final static String GET_VOLUME_DETAIL_STR = "Get Source Volume Detail";
    public final static int SCAN_DISKS_ID = CLIENT_COMMAND_BASE + 26;
    public final static String SCAN_DISKS_STR = "Scan Disks Information";
    public final static int SCAN_VOLUMES_ID = CLIENT_COMMAND_BASE + 28;
    public final static String SCAN_VOLUMES_STR = "Scan Volume Groups Information";
}
