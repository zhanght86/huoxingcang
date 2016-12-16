/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.disk.model;

import com.marstor.msa.common.model.Property;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class VirtualDiskInfo implements Serializable{
    public String poolName;
    public String diskName;
    public String path;
    public String diskSize;
    public String fileSize;
    public String GUID;
    public String serial;
    public String state;
    public boolean isMounted;
    public String onlineState;
    public List<VirtualDetail> detail;
    public boolean cantOnline;
    public boolean cantdel;
    public boolean cantSync;
    public boolean cantLUN;
    public String lineURL;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(String diskSize) {
        this.diskSize = diskSize;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public boolean isIsMounted() {
        return isMounted;
    }

    public void setIsMounted(boolean isMounted) {
        this.isMounted = isMounted;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(String onlineState) {
        this.onlineState = onlineState;
    }

    public List<VirtualDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<VirtualDetail> detail) {
        this.detail = detail;
    }

    public boolean isCantOnline() {
        return cantOnline;
    }

    public void setCantOnline(boolean cantOnline) {
        this.cantOnline = cantOnline;
    }

    public boolean isCantdel() {
        return cantdel;
    }

    public void setCantdel(boolean cantdel) {
        this.cantdel = cantdel;
    }

    public boolean isCantSync() {
        return cantSync;
    }

    public void setCantSync(boolean cantSync) {
        this.cantSync = cantSync;
    }

    public boolean isCantLUN() {
        return cantLUN;
    }

    public void setCantLUN(boolean cantLUN) {
        this.cantLUN = cantLUN;
    }

    public String getLineURL() {
        return lineURL;
    }

    public void setLineURL(String lineURL) {
        this.lineURL = lineURL;
    }
    
}
