/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.cdp.para;



/**
 *
 * @author Administrator
 */
public class CdpAgentSnapParameter {
    private String diskGroupGuid;
    private String ip;
    private int agentID;
    private long lTimeDifference;
    private long[] SnapshotTime;

    public String getDiskGroupGuid() {
        return diskGroupGuid;
    }

    public void setDiskGroupGuid(String diskGroupGuid) {
        this.diskGroupGuid = diskGroupGuid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getAgentID() {
        return agentID;
    }

    public void setAgentID(int agentID) {
        this.agentID = agentID;
    }

    public long getlTimeDifference() {
        return lTimeDifference;
    }

    public void setlTimeDifference(long lTimeDifference) {
        this.lTimeDifference = lTimeDifference;
    }

    public long[] getSnapshotTime() {
        return SnapshotTime;
    }

    public void setSnapshotTime(long[] SnapshotTime) {
        this.SnapshotTime = SnapshotTime;
    }
    
 
}
