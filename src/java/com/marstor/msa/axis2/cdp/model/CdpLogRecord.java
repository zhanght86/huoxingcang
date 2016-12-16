package com.marstor.msa.axis2.cdp.model;

import java.io.Serializable;
//import org.apache.axis2.databinding.types.xsd.Date;

public class CdpLogRecord implements Serializable{

    private long LogTime;
    private long DiskID;
    private long LogOffset;
    private long LogSize;

    public CdpLogRecord() {
    }

    public CdpLogRecord(long LogTime, long DiskID, long LogOffset, long LogSize) {
        this.LogTime = LogTime;
        this.DiskID = DiskID;
        this.LogOffset = LogOffset;
        this.LogSize = LogSize;
    }

    public long getLogTime() {
        return LogTime;
    }

    public void setLogTime(long LogTime) {
        this.LogTime = LogTime;
    }

    public long getDiskID() {
        return DiskID;
    }

    public void setDiskID(long DiskID) {
        this.DiskID = DiskID;
    }

    public long getLogOffset() {
        return LogOffset;
    }

    public void setLogOffset(long LogOffset) {
        this.LogOffset = LogOffset;
    }

    public long getLogSize() {
        return LogSize;
    }

    public void setLogSize(long LogSize) {
        this.LogSize = LogSize;
    }
}