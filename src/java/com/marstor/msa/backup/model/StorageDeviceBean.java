/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.model;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class StorageDeviceBean {
    
    private String strHostName;
    private String strName;
    private String strType;
    private int iSlotNum;
    private int iDriveNum;
    private List<DriveInfo> listDriveInfo;
    private List<TapeInfo> listTapeInfo;

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public int getiDriveNum() {
        return iDriveNum;
    }

    public void setiDriveNum(int iDriveNum) {
        this.iDriveNum = iDriveNum;
    }

    public int getiSlotNum() {
        return iSlotNum;
    }

    public void setiSlotNum(int iSlotNum) {
        this.iSlotNum = iSlotNum;
    }

    public List<DriveInfo> getListDriveInfo() {
        return listDriveInfo;
    }

    public void setListDriveInfo(List<DriveInfo> listDriveInfo) {
        this.listDriveInfo = listDriveInfo;
    }

    public List<TapeInfo> getListTapeInfo() {
        return listTapeInfo;
    }

    public void setListTapeInfo(List<TapeInfo> listTapeInfo) {
        this.listTapeInfo = listTapeInfo;
    }

    public String getStrHostName() {
        return strHostName;
    }

    public void setStrHostName(String strHostName) {
        this.strHostName = strHostName;
    }
}
