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
public class DiskPoolInfo implements Serializable {

    public String state;
    public boolean isMounted;
    public String poolName;
    public String diskarea;
    public String zfsPath;
    public List<VirtualDiskInfo> diskList;
    public boolean cantOnline;
    public boolean cantdel;
    public boolean cantSync;
    public boolean cantLUN;
    public boolean cantDisk;
    public String onlineState;
    public String lineURL;
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isIsMounted() {
        return isMounted;
    }

    public void setIsMounted(boolean isMounted) {
        this.isMounted = isMounted;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getZfsPath() {
        return zfsPath;
    }

    public void setZfsPath(String zfsPath) {
        this.zfsPath = zfsPath;
    }

    public List<VirtualDiskInfo> getDiskList() {
        return diskList;
    }

    public void setDiskList(List<VirtualDiskInfo> diskList) {
        this.diskList = diskList;
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

    public String getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(String onlineState) {
        this.onlineState = onlineState;
    }

    public String getLineURL() {
        return lineURL;
    }

    public void setLineURL(String lineURL) {
        this.lineURL = lineURL;
    }

    public boolean isCantDisk() {
        return cantDisk;
    }

    public void setCantDisk(boolean cantDisk) {
        this.cantDisk = cantDisk;
    }

    public String getDiskarea() {
        return diskarea;
    }

    public void setDiskarea(String diskarea) {
        this.diskarea = diskarea;
    }
 

    
}
