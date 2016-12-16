/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @introduction 存储介质详细信息类（光盘、硬盘）
 * @author Administrator
 */
@ManagedBean(name = "baseStorageInfo")
@RequestScoped
public class BaseStorageInfo implements Serializable{

    /**
     * Creates a new instance of BaseStorageInfo
     */
    public BaseStorageInfo() {
    }
    
    
    public static final int VM_DISK = 1;
    public static final int VM_CDROM = 2;
    
    private String StorageName; //文件名
    private String StorageAttachMedium; //硬盘文件名或者光盘镜像全路径名
    private int state;  //介质状态
    private String strstate;
    private int StorageAttachType;  //存储设备类型取值1为硬盘，2为光盘
    private String StorageAttachSize;  //存储设备总容量
    private String StorageAttachUsedSize;  //存储设备已经使用的容量
    private int StorageAttachPort;  //存储设备端口号，IDE取值0或1，SCSI取值0到14，SAS取值0-7
    private int StorageAttachDevice;  //存储设备设备号，适用于IDE控制器，取值0或1，SAS和SCSI取值0
    private String storageAttachFormat;  //存储设备格式
    private boolean storageAttachIsPhysical; //存储设备是否是物理设备（是否使用了物理硬盘），0不是，1是
    private int isDeleleStorageFile;  //是否同时删除存储设备镜像文件
    private boolean storageAttachMediumIsExists;  //存储设备镜像文件、光驱是否存在，1存在，0不存在
    private String attribue;
    
    public boolean haveiso;
    public boolean haveejectdisk;
    public boolean cantiso;
    public boolean cantejectdisk;
    public boolean cantdei_modify;

    public boolean isStorageAttachMediumIsExists() {
        return storageAttachMediumIsExists;
    }

    public void setStorageAttachMediumIsExists(boolean storageAttachMediumIsExists) {
        this.storageAttachMediumIsExists = storageAttachMediumIsExists;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStorageAttachSize() {
        return StorageAttachSize;
    }

    public void setStorageAttachSize(String StorageAttachSize) {
        this.StorageAttachSize = StorageAttachSize;
    }

    public String getStorageAttachUsedSize() {
        return StorageAttachUsedSize;
    }

    public void setStorageAttachUsedSize(String StorageAttachUsedSize) {
        this.StorageAttachUsedSize = StorageAttachUsedSize;
    }

    public String getAttribue() {
        return attribue;
    }

    public void setAttribue(String attribue) {
        this.attribue = attribue;
    }

    
    public int getIsDeleleStorageFile() {
        return isDeleleStorageFile;
    }

    public void setIsDeleleStorageFile(int isDeleleStorageFile) {
        this.isDeleleStorageFile = isDeleleStorageFile;
    }

    public boolean isStorageAttachIsPhysical() {
        return storageAttachIsPhysical;
    }

    public void setStorageAttachIsPhysical(boolean storageAttachIsPhysical) {
        this.storageAttachIsPhysical = storageAttachIsPhysical;
    }

    public String getStorageAttachFormat() {
        return storageAttachFormat;
    }

    public void setStorageAttachFormat(String storageAttachFormat) {
        this.storageAttachFormat = storageAttachFormat;
    }
   
    public int getStorageAttachDevice() {
        return StorageAttachDevice;
    }

    public void setStorageAttachDevice(int StorageAttachDevice) {
        this.StorageAttachDevice = StorageAttachDevice;
    }

    public String getStorageAttachMedium() {
        return StorageAttachMedium;
    }

    public void setStorageAttachMedium(String StorageAttachMedium) {
        this.StorageAttachMedium = StorageAttachMedium;
    }

    public int getStorageAttachPort() {
        return StorageAttachPort;
    }

    public void setStorageAttachPort(int StorageAttachPort) {
        this.StorageAttachPort = StorageAttachPort;
    }

  

    public int getStorageAttachType() {
        return StorageAttachType;
    }

    public void setStorageAttachType(int StorageAttachType) {
        this.StorageAttachType = StorageAttachType;
    }

    public String getStrstate() {
        return strstate;
    }

    public void setStrstate(String strstate) {
        this.strstate = strstate;
    }

    public String getStorageName() {
        return StorageName;
    }

    public void setStorageName(String StorageName) {
        this.StorageName = StorageName;
    }

    public boolean isHaveiso() {
        return haveiso;
    }

    public void setHaveiso(boolean haveiso) {
        this.haveiso = haveiso;
    }

    public boolean isHaveejectdisk() {
        return haveejectdisk;
    }

    public void setHaveejectdisk(boolean haveejectdisk) {
        this.haveejectdisk = haveejectdisk;
    }

    public boolean isCantdei_modify() {
        return cantdei_modify;
    }

    public void setCantdei_modify(boolean cantdei_modify) {
        this.cantdei_modify = cantdei_modify;
    }

    public boolean isCantiso() {
        return cantiso;
    }

    public void setCantiso(boolean cantiso) {
        this.cantiso = cantiso;
    }

    public boolean isCantejectdisk() {
        return cantejectdisk;
    }

    public void setCantejectdisk(boolean cantejectdisk) {
        this.cantejectdisk = cantejectdisk;
    }

}
