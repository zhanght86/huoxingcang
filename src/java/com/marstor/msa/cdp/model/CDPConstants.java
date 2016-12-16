package com.marstor.msa.cdp.model;

public class CDPConstants 
{
    //log full option
    public static final int OPTION_AUTO_LOOP = 1;
    public static final int OPTION_STOP_CDP = 2;
    public static final int OPTION_WRITE_PROTECT = 3;
    public static final int KEEP_LOG = 1;
    public static String CDP = "CDP";
    public static final int AGENT_START = 1;
    public static final int AGENT_STOP = 0;
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
    public static final String SHELL_PATH = "/var/msa/";
    public static final String Product_ID = "MSA CDP";
    public static final String DISK_LU_VID = "Marstor";
    public static final int ROLLBACK_STATUS_RUNNING = 1;
    public static final int ROLLBACK_STATUS_COMPLETED = 2;
    public static final int ROLLBACK_STATUS_ERROR = 3;
    public static final int ROLLBACK_STATUS_CANCELED = 4;
    public static final int DISK_CREATED = 0;
    public static final int DISK_CREATING = 1;
    public static final int DISK_FAILED = 2;
    public static final String DEFAULT_AGENT_FILE_PATH = "/usr/msa/CdpAgentCfg.xml";
    public static final int EVENT_FULL_WARNING = 1;
    public static final int EVENT_FULL_ERROR = 2;
    public static final int EVENT_FULL_SNAPSHOT_FINISHED = 3;
    public static final int CDP_STATUS_UNINIT = 1;
    public static final int CDP_STATUS_INITING = 2;
    public static final int CDP_STATUS_INITED = 3;
}
