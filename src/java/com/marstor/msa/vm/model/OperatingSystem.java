/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.vm.model;

import java.io.Serializable;

/**
 * @introduction ��õ�������ϵͳ������
 * @author Administrator
 */
public class OperatingSystem implements Serializable{
    
    public String ID; //��ʶID
    public String typeName;  //��������
    public int memorySize; //�ڴ��С
    public int diskSize;  //Ӳ�̴�С

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(int diskSize) {
        this.diskSize = diskSize;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    


}
