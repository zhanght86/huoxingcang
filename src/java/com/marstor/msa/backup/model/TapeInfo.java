/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.model;

/**
 *
 * @author Administrator
 */
public class TapeInfo {
    
    private int iStorageDeviceID;
    private String strBarCode;
    private String strLabel;
    private long lUnusedSize;
    private long lCapacity;
    private int iIsWriteProtect;
    private int iUsedCycle;
    private int iIsLocked;
    private int iSrcslot;

    public int getiSrcslot() {
        return iSrcslot;
    }

    public void setiSrcslot(int iSrcslot) {
        this.iSrcslot = iSrcslot;
    }

    public int getiIsLocked() {
        return iIsLocked;
    }

    public void setiIsLocked(int iIsLocked) {
        this.iIsLocked = iIsLocked;
    }

    public int getiIsWriteProtect() {
        return iIsWriteProtect;
    }

    public void setiIsWriteProtect(int iIsWriteProtect) {
        this.iIsWriteProtect = iIsWriteProtect;
    }

    public int getiStorageDeviceID() {
        return iStorageDeviceID;
    }

    public void setiStorageDeviceID(int iStorageDeviceID) {
        this.iStorageDeviceID = iStorageDeviceID;
    }

    public int getiUsedCycle() {
        return iUsedCycle;
    }

    public void setiUsedCycle(int iUsedCycle) {
        this.iUsedCycle = iUsedCycle;
    }

    public long getlCapacity() {
        return lCapacity;
    }

    public void setlCapacity(long lCapacity) {
        this.lCapacity = lCapacity;
    }

    public long getlUsedSize() {
        return lUnusedSize;
    }

    public void setlUsedSize(long lUsedSize) {
        this.lUnusedSize = lUsedSize;
    }

    public String getStrBarCode() {
        return strBarCode;
    }

    public void setStrBarCode(String strBarCode) {
        this.strBarCode = strBarCode;
    }

    public String getStrLabel() {
        return strLabel;
    }

    public void setStrLabel(String strLabel) {
        this.strLabel = strLabel;
    }
    
}
