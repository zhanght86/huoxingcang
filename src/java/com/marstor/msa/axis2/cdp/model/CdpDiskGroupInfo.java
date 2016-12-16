package com.marstor.msa.axis2.cdp.model;

import com.marstor.msa.axis2.sync.model.SyncStatusInfo;
import java.io.Serializable;

public class CdpDiskGroupInfo implements Serializable {

    private String DiskGroupGuid;
    private String DiskGroupName;
    private String DiskGroupPath;
    private String DiskZfsName;
    private int DiskCount;
    private int SnapshotCount;
    private long LogRecordCount;
    private boolean bCDPStarted;
    private boolean bLogFull;
    private long TotalDiskSize;
    private int iMount;
    private String strUsedSize;
    private String strAvailSize;
    private CdpLogPolicy LogPolicy;
    private CdpStatusInfo cdpStatusInfo;
    private CdpRollbackTaskInfo RollbackInfo;
    private long lSnapshotInterval;
    private boolean bRollbacked;
    private CdpAgent cdpAgent;
    private int protectType;
    private int iMaxNum;
    private int iAutoSnapshotIsOpen = -1;
    private long lAutoSnapshotInterval;
    private SyncStatusInfo syncStatusInfo;

    public CdpDiskGroupInfo() {
    }

    public CdpDiskGroupInfo(String DiskGroupGuid, String DiskGroupName, String DiskGroupPath, String DiskZfsName, int DiskCount, int SnapshotCount, int LogRecordCount, boolean bCDPStarted, boolean bLogFull, long TotalDiskSize, int iMount, String strUsedSize, String strAvailSize, CdpLogPolicy LogPolicy, CdpRollbackTaskInfo RollbackInfo, long lSnapshotInterval, boolean bRollbacked, CdpAgent cdpAgent, int protectType, int iMaxNum, long lAutoSnapshotInterval, SyncStatusInfo syncStatusInfo) {
        this.DiskGroupGuid = DiskGroupGuid;
        this.DiskGroupName = DiskGroupName;
        this.DiskGroupPath = DiskGroupPath;
        this.DiskZfsName = DiskZfsName;
        this.DiskCount = DiskCount;
        this.SnapshotCount = SnapshotCount;
        this.LogRecordCount = LogRecordCount;
        this.bCDPStarted = bCDPStarted;
        this.bLogFull = bLogFull;
        this.TotalDiskSize = TotalDiskSize;
        this.iMount = iMount;
        this.strUsedSize = strUsedSize;
        this.strAvailSize = strAvailSize;
        this.LogPolicy = LogPolicy;
        this.RollbackInfo = RollbackInfo;
        this.lSnapshotInterval = lSnapshotInterval;
        this.bRollbacked = bRollbacked;
        this.cdpAgent = cdpAgent;
        this.protectType = protectType;
        this.iMaxNum = iMaxNum;
        this.lAutoSnapshotInterval = lAutoSnapshotInterval;
        this.syncStatusInfo = syncStatusInfo;
    }

    public CdpAgent getCdpAgent() {
        return cdpAgent;
    }

    public void setCdpAgent(CdpAgent cdpAgent) {
        this.cdpAgent = cdpAgent;
    }

    public String getDiskZfsName() {
        return DiskZfsName;
    }

    public void setDiskZfsName(String DiskZfsName) {
        this.DiskZfsName = DiskZfsName;
    }

    public String getStrUsedSize() {
        return strUsedSize;
    }

    public void setStrUsedSize(String strUsedSize) {
        this.strUsedSize = strUsedSize;
    }

    public String getStrAvailSize() {
        return strAvailSize;
    }

    public void setStrAvailSize(String strAvailSize) {
        this.strAvailSize = strAvailSize;
    }

    public long getTotalDiskSize() {
        return TotalDiskSize;
    }

    public void setTotalDiskSize(long TotalDiskSize) {
        this.TotalDiskSize = TotalDiskSize;
    }

    public CdpLogPolicy getLogPolicy() {
        return LogPolicy;
    }

    public void setLogPolicy(CdpLogPolicy LogPolicy) {
        this.LogPolicy = LogPolicy;
    }

    public String getDiskGroupGuid() {
        return DiskGroupGuid;
    }

    public void setDiskGroupGuid(String DiskGroupGuid) {
        this.DiskGroupGuid = DiskGroupGuid;
    }

    public String getDiskGroupName() {
        return DiskGroupName;
    }

    public void setDiskGroupName(String DiskGroupName) {
        this.DiskGroupName = DiskGroupName;
    }

    public int getDiskCount() {
        return DiskCount;
    }

    public void setDiskCount(int DiskCount) {
        this.DiskCount = DiskCount;
    }

    public int getSnapshotCount() {
        return SnapshotCount;
    }

    public void setSnapshotCount(int SnapshotCount) {
        this.SnapshotCount = SnapshotCount;
    }

    public long getLogRecordCount() {
        return LogRecordCount;
    }

    public void setLogRecordCount(long LogRecordCount) {
        this.LogRecordCount = LogRecordCount;
    }

    public boolean isCDPStarted() {
        return bCDPStarted;
    }

    public void setCDPStarted(boolean CDPStarted) {
        this.bCDPStarted = CDPStarted;
    }

    public String getDiskGroupPath() {
        return DiskGroupPath;
    }

    public void setDiskGroupPath(String DiskGroupPath) {
        this.DiskGroupPath = DiskGroupPath;
    }

    public int getiMount() {
        return iMount;
    }

    public void setiMount(int iMount) {
        this.iMount = iMount;
    }

    public boolean isbCDPStarted() {
        return bCDPStarted;
    }

    public boolean isbLogFull() {
        return bLogFull;
    }

    public CdpStatusInfo getStatusInfo() {
        return cdpStatusInfo;
    }

    public CdpRollbackTaskInfo getRollbackInfo() {
        return RollbackInfo;
    }

    public long getlSnapshotInterval() {
        return lSnapshotInterval;
    }

    public void setbCDPStarted(boolean bCDPStarted) {
        this.bCDPStarted = bCDPStarted;
    }

    public void setbLogFull(boolean bLogFull) {
        this.bLogFull = bLogFull;
    }

    public void setStatusInfo(CdpStatusInfo StatusInfo) {
        this.cdpStatusInfo = StatusInfo;
    }

    public void setRollbackInfo(CdpRollbackTaskInfo RollbackInfo) {
        this.RollbackInfo = RollbackInfo;
    }

    public void setlSnapshotInterval(long lSnapshotInterval) {
        this.lSnapshotInterval = lSnapshotInterval;
    }

    public int getProtectType() {
        return protectType;
    }

    public void setProtectType(int protectType) {
        this.protectType = protectType;
    }

    public int getiMaxNum() {
        return iMaxNum;
    }

    public void setiMaxNum(int iMaxNum) {
        this.iMaxNum = iMaxNum;
    }

    public int getiAutoSnapshotIsOpen() {
        return iAutoSnapshotIsOpen;
    }

    public void setiAutoSnapshotIsOpen(int iAutoSnapshotIsOpen) {
        this.iAutoSnapshotIsOpen = iAutoSnapshotIsOpen;
    }

    public SyncStatusInfo getSyncStatusInfo() {
        return syncStatusInfo;
    }

    public void setSyncStatusInfo(SyncStatusInfo syncStatusInfo) {
        this.syncStatusInfo = syncStatusInfo;
    }

    public long getlAutoSnapshotInterval() {
        return lAutoSnapshotInterval;
    }

    public void setlAutoSnapshotInterval(long lAutoSnapshotInterval) {
        this.lAutoSnapshotInterval = lAutoSnapshotInterval;
    }

    public CdpStatusInfo getCdpStatusInfo() {
        return cdpStatusInfo;
    }

    public void setCdpStatusInfo(CdpStatusInfo cdpStatusInfo) {
        this.cdpStatusInfo = cdpStatusInfo;
    }

    public boolean isbRollbacked() {
        return bRollbacked;
    }

    public void setbRollbacked(boolean bRollbacked) {
        this.bRollbacked = bRollbacked;
    }
    
    public boolean isRollbacked() {
        return bRollbacked;
    }
 
}
