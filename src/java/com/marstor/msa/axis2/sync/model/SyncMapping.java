/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.sync.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class SyncMapping implements Serializable{
    
    private String strSrcFileSystem;
    private String strDescFileSystem;
    private int iDescFileSystemHashCode;
    private int iIsLocal;
    private int iSyncStatus = 9;
    private String strSyncSizeSpeed = "";
    private long dSyncCompleteTime;
    private String strIP = "";
    private String strPort = "";
    private String strSSHPort = "";
    private String strUserName = "";
    private String strPassword = "";
    private String strRootPassword = "";
    private String strGzipLevel = "";
    private String strFirstSnapshot;
    private String strSecondSnapshot = "none";
    private boolean bAllSend = true;
    private String strTransferPort;
    private String jobStartTime;
    private String jobEndTime;
    private boolean bTimingJob = false;
 
    @Override
    public boolean equals(Object o){
        if(this ==o){
            return true;
        }
        if( o != null && o.getClass() == this.getClass()){      
            if(this.getiDescFileSystemHashCode() == ((SyncMapping)o).getiDescFileSystemHashCode()){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return iDescFileSystemHashCode;
    }

    public void setiDescFileSystemHashCode(int iDescFileSystemHashCode) {
        this.iDescFileSystemHashCode = iDescFileSystemHashCode;
    }
    
    public String getStrSrcFileSystem() {
        return strSrcFileSystem;
    }

    public void setStrSrcFileSystem(String strSrcFileSystem) {
        this.strSrcFileSystem = strSrcFileSystem;
    }

    public String getStrDescFileSystem() {
        return strDescFileSystem;
    }

    public void setStrDescFileSystem(String strDescFileSystem) {
        this.strDescFileSystem = strDescFileSystem;
    }

    public int getiIsLocal() {
        return iIsLocal;
    }

    public void setiIsLocal(int iIsLocal) {
        this.iIsLocal = iIsLocal;
    }

    public int getiSyncStatus() {
        return iSyncStatus;
    }

    public void setiSyncStatus(int iSyncStatus) {
        this.iSyncStatus = iSyncStatus;
    }

    public String getStrSyncSizeSpeed() {
        return strSyncSizeSpeed;
    }

    public void setStrSyncSizeSpeed(String strSyncSizeSpeed) {
        this.strSyncSizeSpeed = strSyncSizeSpeed;
    }
    
    public String getStrIP() {
        return strIP;
    }

    public void setStrIP(String strIP) {
        this.strIP = strIP;
    }

    public String getStrPort() {
        return strPort;
    }

    public void setStrPort(String strPort) {
        this.strPort = strPort;
    }

    public String getStrSSHPort() {
        return strSSHPort;
    }

    public void setStrSSHPort(String strSSHPort) {
        this.strSSHPort = strSSHPort;
    }

    public String getStrUserName() {
        return strUserName;
    }

    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public String getStrRootPassword() {
        return strRootPassword;
    }

    public void setStrRootPassword(String strRootPassword) {
        this.strRootPassword = strRootPassword;
    }

    public String getStrGzipLevel() {
        return strGzipLevel;
    }

    public void setStrGzipLevel(String strGzipLevel) {
        this.strGzipLevel = strGzipLevel;
    }

    public int getiDescFileSystemHashCode() {
        return iDescFileSystemHashCode;
    }
    
    public String getStrFirstSnapshot() {
        return strFirstSnapshot;
    }

    public void setStrFirstSnapshot(String strFirstSnapshot) {
        this.strFirstSnapshot = strFirstSnapshot;
    }

    public String getStrSecondSnapshot() {
        return strSecondSnapshot;
    }

    public void setStrSecondSnapshot(String strSecondSnapshot) {
        this.strSecondSnapshot = strSecondSnapshot;
    }

    public boolean isbAllSend() {
        return bAllSend;
    }

    public void setbAllSend(boolean bAllSend) {
        this.bAllSend = bAllSend;
    }

    public String getStrTransferPort() {
        return strTransferPort;
    }

    public void setStrTransferPort(String strTransferPort) {
        this.strTransferPort = strTransferPort;
    }

    public boolean isbTimingJob() {
        return bTimingJob;
    }

    public void setbTimingJob(boolean bTimingJob) {
        this.bTimingJob = bTimingJob;
    }

    public long getdSyncCompleteTime() {
        return dSyncCompleteTime;
    }

    public void setdSyncCompleteTime(long dSyncCompleteTime) {
        this.dSyncCompleteTime = dSyncCompleteTime;
    }

    public String getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(String jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public String getJobEndTime() {
        return jobEndTime;
    }

    public void setJobEndTime(String jobEndTime) {
        this.jobEndTime = jobEndTime;
    }
    
}
