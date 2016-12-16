/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.cdp.util.SnapUtil;
import com.marstor.msa.common.util.MSAResource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class SnapShot implements Comparable {

    private String strName;
    private String strUsed;
    private String strRefer;
    private int iExpirationdate;
    private String SyncSizeSpeed;
    private String SyncStartTime;
    private String SyncCompleteTime;
    private String type;
    private String createdTime = "";
    private String strCreatedTime="";
    private String alias = "";
    private boolean saveRollEnabled, cancelRollEnabled, rollEnabled;
    private boolean saveRollRender, cancelRollRender, rollRender = true, deleteRender = true, createCopyRender;
    private boolean permitCreateCopy ,existCopy;
    private MSAResource res = new MSAResource();
    private long SnapshotID;
    private String SnapshotName;
    private Date SnapshotTime;
    private long SnapshotSize;
    private String AgentHost;
    private long AgentID;
    private boolean bHasZFSSnapshot;

    public SnapShot() {
    }
    
  

    public SnapShot(String strName, String strUsed, String strRefer, int iExpirationdate, String SyncSizeSpeed, String SyncStartTime, String SyncCompleteTime, String clones) {
        this.strName = strName;
        this.strUsed = strUsed;
        this.strRefer = strRefer;
        this.iExpirationdate = iExpirationdate;
        this.SyncSizeSpeed = SyncSizeSpeed;
        this.SyncStartTime = SyncStartTime;
        this.SyncCompleteTime = SyncCompleteTime;
        this.rollRender = true;
        this.deleteRender = true;
        if (this.strName != null) {
            String array[] = this.strName.split("_");
            if (array != null && array.length > 1) {
                this.alias = array[array.length - 2];
                String time = array[array.length - 1].trim();
                this.strCreatedTime = time;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getDefault());
                try {
                    this.createdTime = simpleDateFormat.format(sdf.parse(time));
                } catch (ParseException ex) {
                    Logger.getLogger(SnapUtil.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        if (this.strName != null) {
            if (this.strName.contains("AUTO")) {
                this.type = res.get("auto");
            } else {
                this.type = res.get("manual");
            }
        }
//        Debug.print("clones: " + clones);
        if(clones==null || clones.equals("")) {
            this.permitCreateCopy = true;
            this.existCopy = false;
        }else {
            this.permitCreateCopy = false;
            this.existCopy = true;
        }
    }

    public SnapShot(String strName, String strUsed, String type, String strCreatedTime,String alias) {
        this.strName = strName;
        this.strUsed = strUsed;
        this.type = type;
        this.createdTime = strCreatedTime;
        this.alias = alias;
        this.permitCreateCopy = true;
//        if (clones != null) {
//            if (clones.equals("")) {
//                this.createCopyRender = true;
//            } else {
//                this.createCopyRender = false;
//            }
//        }
        //  this.createCopyRender = true;
    }

    public boolean isExistCopy() {
        return existCopy;
    }

    public void setExistCopy(boolean existCopy) {
        this.existCopy = existCopy;
    }


    public boolean isPermitCreateCopy() {
        return permitCreateCopy;
    }

    public void setPermitCreateCopy(boolean permitCreateCopy) {
        this.permitCreateCopy = permitCreateCopy;
    }

    public boolean isCreateCopyRender() {
        return createCopyRender;
    }

    public void setCreateCopyRender(boolean createCopyRender) {
        this.createCopyRender = createCopyRender;
    }

    public boolean isRollRender() {
        return rollRender;
    }

    public void setRollRender(boolean rollRender) {
        this.rollRender = rollRender;
    }

    public boolean isDeleteRender() {
        return deleteRender;
    }

    public void setDeleteRender(boolean deleteRender) {
        this.deleteRender = deleteRender;
    }

    public String getStrCreatedTime() {
        return strCreatedTime;
    }

    public void setStrCreatedTime(String strCreatedTime) {
        this.strCreatedTime = strCreatedTime;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public MSAResource getRes() {
        return res;
    }

    public void setRes(MSAResource res) {
        this.res = res;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrUsed() {
        return strUsed;
    }

    public void setStrUsed(String strUsed) {
        this.strUsed = strUsed;
    }

    public String getStrRefer() {
        return strRefer;
    }

    public void setStrRefer(String strRefer) {
        this.strRefer = strRefer;
    }

    public int getiExpirationdate() {
        return iExpirationdate;
    }

    public void setiExpirationdate(int iExpirationdate) {
        this.iExpirationdate = iExpirationdate;
    }

    public String getSyncSizeSpeed() {
        return SyncSizeSpeed;
    }

    public void setSyncSizeSpeed(String SyncSizeSpeed) {
        this.SyncSizeSpeed = SyncSizeSpeed;
    }

    public String getSyncStartTime() {
        return SyncStartTime;
    }

    public void setSyncStartTime(String SyncStartTime) {
        this.SyncStartTime = SyncStartTime;
    }

    public String getSyncCompleteTime() {
        return SyncCompleteTime;
    }

    public void setSyncCompleteTime(String SyncCompleteTime) {
        this.SyncCompleteTime = SyncCompleteTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSaveRollEnabled() {
        return saveRollEnabled;
    }

    public void setSaveRollEnabled(boolean saveRollEnabled) {
        this.saveRollEnabled = saveRollEnabled;
    }

    public boolean isCancelRollEnabled() {
        return cancelRollEnabled;
    }

    public void setCancelRollEnabled(boolean cancelRollEnabled) {
        this.cancelRollEnabled = cancelRollEnabled;
    }

    public boolean isRollEnabled() {
        return rollEnabled;
    }

    public void setRollEnabled(boolean rollEnabled) {
        this.rollEnabled = rollEnabled;
    }

    public boolean isSaveRollRender() {
        return saveRollRender;
    }

    public void setSaveRollRender(boolean saveRollRender) {
        this.saveRollRender = saveRollRender;
    }

    public boolean isCancelRollRender() {
        return cancelRollRender;
    }

    public void setCancelRollRender(boolean cancelRollRender) {
        this.cancelRollRender = cancelRollRender;
    }

    @Override
    public int compareTo(Object o) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        SnapShot snap = (SnapShot) o;

        return snap.strCreatedTime.compareTo(this.strCreatedTime);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(date);
    }

    public static Date stringToDate(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return formatter.parse(s);
        } catch (ParseException ex) {
            Logger.getLogger(SnapUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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

    public Date getSnapshotTime() {
        return SnapshotTime;
    }

    public void setSnapshotTime(Date SnapshotTime) {
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
