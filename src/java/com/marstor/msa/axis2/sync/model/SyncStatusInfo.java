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
public class SyncStatusInfo implements Serializable{

    private int ExistSnapshot;//是否含有快照，1是，0不是
    private int IsSrcSync;//是否是同步源文件系统，1是，0不是
    private int IsDescSync;//是否是同步目标文件系统，1是，0不是
    private int IsMount;//目标文件系统是否挂载，1是，0不是
    private int IsPause;//目标文件系统是否处于暂停状态，1是，0不是
    private int IsLocalDescSync;
    private int IsRollback;
    private boolean bIsRollbacking;
    private String used;
    private String available;

    public SyncStatusInfo() {
    }

    
    public int getIsRollback() {
        return IsRollback;
    }

    public void setIsRollback(int IsRollback) {
        this.IsRollback = IsRollback;
    }

    public int getExistSnapshot() {
        return ExistSnapshot;
    }

    public int getIsSrcSync() {
        return IsSrcSync;
    }

    public int getIsDescSync() {
        return IsDescSync;
    }

    public int getIsMount() {
        return IsMount;
    }

    public int getIsPause() {
        return IsPause;
    }

    public void setExistSnapshot(int ExistSnapshot) {
        this.ExistSnapshot = ExistSnapshot;
    }

    public void setIsSrcSync(int IsSrcSync) {
        this.IsSrcSync = IsSrcSync;
    }

    public void setIsDescSync(int IsDescSync) {
        this.IsDescSync = IsDescSync;
    }

    public void setIsMount(int IsMount) {
        this.IsMount = IsMount;
    }

    public void setIsPause(int IsPause) {
        this.IsPause = IsPause;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public boolean isbIsRollbacking() {
        return bIsRollbacking;
    }

    public void setbIsRollbacking(boolean bIsRollbacking) {
        this.bIsRollbacking = bIsRollbacking;
    }

    public int getIsLocalDescSync() {
        return IsLocalDescSync;
    }

    public void setIsLocalDescSync(int IsLocalDescSync) {
        this.IsLocalDescSync = IsLocalDescSync;
    }
    
}
