package com.marstor.msa.axis2.cdp.model;

import java.io.Serializable;

public class CdpDiskInfo implements Serializable{

    private String DiskGuid;
    private String DiskFileName_Path;
    private String DiskName;
    private long DiskID;
    private long DiskSize;
    private String DiskGroupGuid;
    private long LogRecordCount;
    private int DiskCreateStatus;
    private LuInfoBean luInfoBean;

    public CdpDiskInfo() {
    }

    public CdpDiskInfo(String DiskGuid, String DiskFileName_Path, String DiskName, long DiskID, long DiskSize, String DiskGroupGuid, int LogRecordCount, int DiskCreateStatus, LuInfoBean luInfoBean) {
        this.DiskGuid = DiskGuid;
        this.DiskFileName_Path = DiskFileName_Path;
        this.DiskName = DiskName;
        this.DiskID = DiskID;
        this.DiskSize = DiskSize;
        this.DiskGroupGuid = DiskGroupGuid;
        this.LogRecordCount = LogRecordCount;
        this.DiskCreateStatus = DiskCreateStatus;
        this.luInfoBean = luInfoBean;
    }

    public String getDiskGuid() {
        return DiskGuid;
    }

    public void setDiskGuid(String DiskGuid) {
        this.DiskGuid = DiskGuid;
    }

    public String getDiskFileName_Path() {
        return DiskFileName_Path;
    }

    public void setDiskFileName_Path(String DiskFileName_Path) {
        this.DiskFileName_Path = DiskFileName_Path;
    }

    public String getDiskName() {
        return DiskName;
    }

    public void setDiskName(String DiskName) {
        this.DiskName = DiskName;
    }

    public long getDiskID() {
        return DiskID;
    }

    public void setDiskID(long DiskID) {
        this.DiskID = DiskID;
    }

    public long getDiskSize() {
        return DiskSize;
    }

    public void setDiskSize(long DiskSize) {
        this.DiskSize = DiskSize;
    }

    public String getDiskGroupGuid() {
        return DiskGroupGuid;
    }

    public void setDiskGroupGuid(String DiskGroupGuid) {
        this.DiskGroupGuid = DiskGroupGuid;
    }

    public long getLogRecordCount() {
        return LogRecordCount;
    }

    public void setLogRecordCount(long LogRecordCount) {
        this.LogRecordCount = LogRecordCount;
    }

    public int getDiskCreateStatus() {
        return DiskCreateStatus;
    }

    public void setDiskCreateStatus(int DiskCreateStatus) {
        this.DiskCreateStatus = DiskCreateStatus;
    }

    public LuInfoBean getLuInfoBean() {
        return luInfoBean;
    }

    public void setLuInfoBean(LuInfoBean luInfoBean) {
        this.luInfoBean = luInfoBean;
    }


}
