/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.vm.model;

import java.io.Serializable;

/**
 * @introduction 获得单个操作系统的属性
 * @author Administrator
 */
public class OperatingSystem implements Serializable{
    
    public String ID; //标识ID
    public String typeName;  //类型名称
    public int memorySize; //内存大小
    public int diskSize;  //硬盘大小

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
