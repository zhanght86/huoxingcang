/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.cdp.util.SnapUtil;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.sync.bean.MarsHost;
import com.marstor.msa.sync.bean.SyncConstants;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class Sync implements Serializable{

    private String sourceFileSystem = "";
    private String TargetFileSystem = "";
    private String LastSnap = "";
    private int type;
    private String targetIP = "";
    public static final int localType = 1;
    public static final int remoteType = 0;
    private int iDescFileSystemHashCode;
    private String status = "";
    private String strSyncSizeSpeed;
    private MarsHost host = new MarsHost();
    private boolean startRendered = false, suspendRendered = true, resumeRendered = false, stopRendered = true, startDisabled = false, stopDisabled = false, suspendDisabled = false, deleteDisabled = true, resumeDisabled = false;//操作按钮的显示和隐藏
    private Calendar jobStartTime;//任务开始时间
    private Calendar jobEndTime;//任务结束时间
    private boolean bTimingJob;//是否开启指定时间同步
    private String lastSnapTime = "";
    private MSAResource res = new MSAResource();

    public Sync() {
    }

    public Sync(String TargetFileSystem, String LastSnap, int type, String targetIP, int iDescFileSystemHashCode, int syncStatus, MarsHost host, Calendar jobStartTime, Calendar jobEndTime, boolean bTimingJob) {

        this.TargetFileSystem = TargetFileSystem;
        Debug.print("last Snap:" + LastSnap);
        if (LastSnap == null || LastSnap.equals("") || LastSnap.equals("null")) {
            //  this.LastSnap = " ";
            lastSnapTime = "";
            //Debug.print("##############@@@@@@@@@@@ 1 this LastSnap:" + this.LastSnap);
        } else {
            String array[] = LastSnap.split("_");
            String time = "";
            if (array != null && array.length > 1) {
                //time = array[4];
                time = array[array.length - 1].trim();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getDefault());
                try {
                    //time = simpleDateFormat.format(sdf.parse(time));
                    this.lastSnapTime = simpleDateFormat.format(sdf.parse(time));
                } catch (ParseException ex) {
                    Logger.getLogger(SnapUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
                //this.LastSnap = LastSnap.substring(0, LastSnap.lastIndexOf("_")) + "_" + time;
                Debug.print("lastSnapTime:" + this.lastSnapTime);
            }
            //this.LastSnap = LastSnap;
            //  Debug.print(" this LastSnap:" + this.LastSnap);
        }
        this.type = type;
        this.targetIP = targetIP;
        this.iDescFileSystemHashCode = iDescFileSystemHashCode;
        this.jobStartTime = jobStartTime;
        this.jobEndTime = jobEndTime;
        this.bTimingJob = bTimingJob;
        if (syncStatus == SyncConstants.WAITING) {
            this.status = res.get("wait");
            this.stopRendered = true;
            this.stopDisabled = false;
            this.suspendRendered = true;
            this.suspendDisabled = false;
            this.startRendered = false;
            this.resumeRendered = false;
            this.deleteDisabled = true;
        } else if (syncStatus == SyncConstants.EXECUTING) {
            this.status = res.get("copying");
            this.stopRendered = true;
            this.stopDisabled = false;
            this.suspendRendered = true;
            this.suspendDisabled = false;
            this.startRendered = false;
            this.resumeRendered = false;
            this.deleteDisabled = true;

        } else if (syncStatus == SyncConstants.SUSPEND) {
            this.status = res.get("pause");
            this.stopRendered = true;
            this.stopDisabled = false;
            this.resumeRendered = true;
            this.resumeDisabled = false;
            startRendered = false;
            this.suspendRendered = false;
            this.deleteDisabled = true;
        } else if (syncStatus == SyncConstants.SUCCESSED) {
            this.status = res.get("finish");
            this.startRendered = false;
            this.suspendRendered = true;
            this.suspendDisabled = false;
            this.stopRendered = true;
            this.stopDisabled = false;
            this.resumeRendered = false;
            this.deleteDisabled = true;
        } else if (syncStatus == SyncConstants.FAILED) {
            this.status = res.get("failed");
            this.startRendered = false;
            this.suspendRendered = true;
            this.suspendDisabled = true;
            this.stopRendered = true;
            this.stopDisabled = false;
            this.resumeRendered = false;
            this.deleteDisabled = true;
        } else if (syncStatus == SyncConstants.SUSPENDING) {
            this.status = res.get("pausing");
            this.stopRendered = true;
            this.suspendRendered = true;
            this.suspendDisabled = true;
            this.stopDisabled = true;
            this.startRendered = false;
            this.resumeRendered = false;
            this.deleteDisabled = true;

        } else if (syncStatus == SyncConstants.CLOSEING) {
            this.status = res.get("closing");
            this.startRendered = false;
            this.startDisabled = true;
            this.suspendRendered = true;
            this.suspendDisabled = true;
            this.stopRendered = true;
            this.resumeRendered = false;
            this.deleteDisabled = true;
            this.stopDisabled = true;
        } else if (syncStatus == SyncConstants.DELETEING) {
            this.status = res.get("deleting");
            this.startRendered = true;
            this.suspendRendered = true;
            this.startDisabled = true;
            this.suspendDisabled = true;
            this.stopRendered = false;
            this.resumeRendered = false;
            this.deleteDisabled = true;

        } else if (syncStatus == SyncConstants.DELETE) {
            this.status = res.get("delete");
            this.startRendered = true;
            this.suspendRendered = true;
            this.startDisabled = true;
            this.suspendDisabled = true;
            this.stopRendered = false;
            this.resumeRendered = false;
            this.deleteDisabled = true;
        } else if (syncStatus == SyncConstants.CLOSE) {
            this.status = res.get("close");
            this.startRendered = true;
            this.startDisabled = true;
            this.suspendRendered = true;
            this.suspendDisabled = true;
            this.stopRendered = false;
            this.resumeRendered = false;
            this.deleteDisabled = false;
        }
        this.host = host;
    }

    public Sync(String sourceFileSystem, String TargetFileSystem, String targetIP, int syncStatus, String strSyncSizeSpeed) {
        this.sourceFileSystem = sourceFileSystem;
        this.TargetFileSystem = TargetFileSystem;
        this.targetIP = targetIP;
        if (this.targetIP == null || this.targetIP.equals("")) {
            this.targetIP = res.get("local");
        }
        if (syncStatus == SyncConstants.WAITING) {
            this.status = res.get("wait");
        } else if (syncStatus == SyncConstants.EXECUTING) {
            this.status = res.get("copying");
        } else if (syncStatus == SyncConstants.SUSPEND) {
            this.status = res.get("pause");
        } else if (syncStatus == SyncConstants.SUCCESSED) {
            this.status = res.get("finish");
        } else if (syncStatus == SyncConstants.FAILED) {
            this.status = res.get("failed");
        } else if (syncStatus == SyncConstants.SUSPENDING) {
            this.status = res.get("pausing");
        } else if (syncStatus == SyncConstants.CLOSEING) {
            this.status = res.get("closing");
        } else if (syncStatus == SyncConstants.DELETEING) {
            this.status = res.get("deleting");
        } else if (syncStatus == SyncConstants.DELETE) {
            this.status = res.get("delete");
        } else if (syncStatus == SyncConstants.CLOSE) {
            this.status = res.get("close");
        }

        this.strSyncSizeSpeed = strSyncSizeSpeed.replace("k", "K");
    }

    public String getLastSnapTime() {
        return lastSnapTime;
    }

    public void setLastSnapTime(String lastSnapTime) {
        this.lastSnapTime = lastSnapTime;
    }

    public Calendar getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(Calendar jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public Calendar getJobEndTime() {
        return jobEndTime;
    }

    public void setJobEndTime(Calendar jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

    public boolean isbTimingJob() {
        return bTimingJob;
    }

    public void setbTimingJob(boolean bTimingJob) {
        this.bTimingJob = bTimingJob;
    }

    public Sync(String TargetFileSystem, String targetIP) {
        this.TargetFileSystem = TargetFileSystem;
        this.targetIP = targetIP;
    }

    public boolean isResumeDisabled() {
        return resumeDisabled;
    }

    public void setResumeDisabled(boolean resumeDisabled) {
        this.resumeDisabled = resumeDisabled;
    }

    public boolean isStartDisabled() {
        return startDisabled;
    }

    public void setStartDisabled(boolean startDisabled) {
        this.startDisabled = startDisabled;
    }

    public boolean isStopDisabled() {
        return stopDisabled;
    }

    public void setStopDisabled(boolean stopDisabled) {
        this.stopDisabled = stopDisabled;
    }

    public String getStrSyncSizeSpeed() {
        return strSyncSizeSpeed;
    }

    public void setStrSyncSizeSpeed(String strSyncSizeSpeed) {
        this.strSyncSizeSpeed = strSyncSizeSpeed;
    }

    public String getSourceFileSystem() {
        return sourceFileSystem;
    }

    public void setSourceFileSystem(String sourceFileSystem) {
        this.sourceFileSystem = sourceFileSystem;
    }

    public MarsHost getHost() {
        return host;
    }

    public boolean isSuspendDisabled() {
        return suspendDisabled;
    }

    public void setSuspendDisabled(boolean suspendDisabled) {
        this.suspendDisabled = suspendDisabled;
    }

    public boolean isDeleteDisabled() {
        return deleteDisabled;
    }

    public void setDeleteDisabled(boolean deleteDisabled) {
        this.deleteDisabled = deleteDisabled;
    }

    public boolean isStop() {
        return stopRendered;
    }

    public void setStopRendered(boolean stopRendered) {
        this.stopRendered = stopRendered;
    }

    public boolean isStart() {
        return startRendered;
    }

    public void setStartRendered(boolean startRendered) {
        this.startRendered = startRendered;
    }

    public boolean isSuspend() {
        return suspendRendered;
    }

    public void setSuspendRendered(boolean suspendRendered) {
        this.suspendRendered = suspendRendered;
    }

    public boolean isResume() {
        return resumeRendered;
    }

    public void setResumeRendered(boolean resumeRendered) {
        this.resumeRendered = resumeRendered;
    }

    public void setHost(MarsHost host) {
        this.host = host;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getiDescFileSystemHashCode() {
        return iDescFileSystemHashCode;
    }

    public void setiDescFileSystemHashCode(int iDescFileSystemHashCode) {
        this.iDescFileSystemHashCode = iDescFileSystemHashCode;
    }

    public String getTargetFileSystem() {
        return TargetFileSystem;
    }

    public void setTargetFileSystem(String TargetFileSystem) {
        this.TargetFileSystem = TargetFileSystem;
    }

    public String getLastSnap() {
        return LastSnap;
    }

    public void setLastSnap(String LastSnap) {
        this.LastSnap = LastSnap;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTargetIP() {
        return targetIP;
    }

    public void setTargetIP(String targetIP) {
        this.targetIP = targetIP;
    }
}
