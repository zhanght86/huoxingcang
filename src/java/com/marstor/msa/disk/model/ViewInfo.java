/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.disk.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ViewInfo implements Serializable{
    public String GUID;
    public int entry;
    public String hostGroupName;
    public String targetGroupName;
    public int LUN;
    public String hostGroupNameStr;
    public String type;
    public List<DiskDetail> diskDetail;

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public int getEntry() {
        return entry;
    }

    public void setEntry(int entry) {
        this.entry = entry;
    }

    public String getHostGroupName() {
        return hostGroupName;
    }

    public void setHostGroupName(String hostGroupName) {
        this.hostGroupName = hostGroupName;
    }

    public String getTargetGroupName() {
        return targetGroupName;
    }

    public void setTargetGroupName(String targetGroupName) {
        this.targetGroupName = targetGroupName;
    }

    public int getLUN() {
        return LUN;
    }

    public void setLUN(int LUN) {
        this.LUN = LUN;
    }

    public String getHostGroupNameStr() {
        return hostGroupNameStr;
    }

    public void setHostGroupNameStr(String hostGroupNameStr) {
        this.hostGroupNameStr = hostGroupNameStr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DiskDetail> getDiskDetail() {
        return diskDetail;
    }

    public void setDiskDetail(List<DiskDetail> diskDetail) {
        this.diskDetail = diskDetail;
    }
    
}
