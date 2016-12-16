/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @introduction �洢������ϸ��Ϣ�ࣨ���̡�Ӳ�̣�
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
    
    private String StorageName; //�ļ���
    private String StorageAttachMedium; //Ӳ���ļ������߹��̾���ȫ·����
    private int state;  //����״̬
    private String strstate;
    private int StorageAttachType;  //�洢�豸����ȡֵ1ΪӲ�̣�2Ϊ����
    private String StorageAttachSize;  //�洢�豸������
    private String StorageAttachUsedSize;  //�洢�豸�Ѿ�ʹ�õ�����
    private int StorageAttachPort;  //�洢�豸�˿ںţ�IDEȡֵ0��1��SCSIȡֵ0��14��SASȡֵ0-7
    private int StorageAttachDevice;  //�洢�豸�豸�ţ�������IDE��������ȡֵ0��1��SAS��SCSIȡֵ0
    private String storageAttachFormat;  //�洢�豸��ʽ
    private boolean storageAttachIsPhysical; //�洢�豸�Ƿ��������豸���Ƿ�ʹ��������Ӳ�̣���0���ǣ�1��
    private int isDeleleStorageFile;  //�Ƿ�ͬʱɾ���洢�豸�����ļ�
    private boolean storageAttachMediumIsExists;  //�洢�豸�����ļ��������Ƿ���ڣ�1���ڣ�0������
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
