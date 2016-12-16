/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.model;

/**
 *
 * @author Administrator
 */
public class DriveInfo {
    
    private int iStorageDeviceID;
    private int iDriveNumber;
    private int iIsLocked;

    public int getiStorageDeviceID() {
        return iStorageDeviceID;
    }

    public void setiStorageDeviceID(int iStorageDeviceID) {
        this.iStorageDeviceID = iStorageDeviceID;
    }

    public int getiDriveNumber() {
        return iDriveNumber;
    }

    public void setiDriveNumber(int iDriveNumber) {
        this.iDriveNumber = iDriveNumber;
    }

    public int getiIsLocked() {
        return iIsLocked;
    }

    public void setiIsLocked(int iIsLocked) {
        this.iIsLocked = iIsLocked;
    }
    
}
