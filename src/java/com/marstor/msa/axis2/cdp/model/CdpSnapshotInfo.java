package com.marstor.msa.axis2.cdp.model;

import java.io.Serializable;
import java.util.*;

public class CdpSnapshotInfo implements Serializable{

    private long SnapshotID;
    private String SnapshotName;
    private long SnapshotTime;
    private long SnapshotSize;
    private String AgentHost;
    private long AgentID;
    private boolean bHasZFSSnapshot;

    public CdpSnapshotInfo() {
    }

    public CdpSnapshotInfo(long SnapshotID, String SnapshotName, long SnapshotTime, long SnapshotSize, String AgentHost, long AgentID, boolean bHasZFSSnapshot) {
        this.SnapshotID = SnapshotID;
        this.SnapshotName = SnapshotName;
        this.SnapshotTime = SnapshotTime;
        this.SnapshotSize = SnapshotSize;
        this.AgentHost = AgentHost;
        this.AgentID = AgentID;
        this.bHasZFSSnapshot = bHasZFSSnapshot;
    }

    public long getSnapshotID() {
        return SnapshotID;
    }

    public void setSnapshotID(long SnapshotID) {
        this.SnapshotID = SnapshotID;
    }

    public String getSnapshotName() {
        return SnapshotName;
    }

    public void setSnapshotName(String SnapshotName) {
        this.SnapshotName = SnapshotName;
    }

    public long getSnapshotTime() {
        return SnapshotTime;
    }

    public void setSnapshotTime(long SnapshotTime) {
        this.SnapshotTime = SnapshotTime;
    }
 
    public long getSnapshotSize() {
        return SnapshotSize;
    }

    public void setSnapshotSize(long SnapshotSize) {
        this.SnapshotSize = SnapshotSize;
    }

    public String getAgentHost() {
        return AgentHost;
    }

    public void setAgentHost(String AgentHost) {
        this.AgentHost = AgentHost;
    }

    public long getAgentID() {
        return AgentID;
    }

    public void setAgentID(long AgentID) {
        this.AgentID = AgentID;
    }

    public boolean isbHasZFSSnapshot() {
        return bHasZFSSnapshot;
    }

    public void setbHasZFSSnapshot(boolean bHasZFSSnapshot) {
        this.bHasZFSSnapshot = bHasZFSSnapshot;
    }
    
}
