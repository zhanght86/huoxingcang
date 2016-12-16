/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.model;

import java.io.Serializable;
import java.util.List;
import com.marstor.msa.common.bean.PhysicalDiskInformation;


/**
 *
 * @author Administrator
 */
//@ManagedBean(name = "volumesInfo")
//@RequestScoped
public class VolumesInfo implements Serializable{
    public String name;
    public String usedSize;
    public String unusedSize;
    public String raidType;
    private int poolType;
    public String state;
    public List<Disk> diskList;
    public Disk selectDisk;
    public boolean notDestroyPool;  //删除卷组
    public boolean notClearPool;  //清除卷组错误
    public boolean notfixPool;  //检查修复卷组
    public boolean notaddDisk;  //添加磁盘
    public boolean notCapacityPool;  //卷组扩容
    public boolean notImport;  //导入卷组
    List<PhysicalDiskInformation> dataDisks;  //数据盘
            List<PhysicalDiskInformation> cacheDisks;  //高速缓冲盘
            List<PhysicalDiskInformation> logDisks;  //日志盘
            List<PhysicalDiskInformation> spareDisks;  //热备磁盘

    

    /**
     * Creates a new instance of volumesInfo
     */
    public VolumesInfo() {
    }

    public boolean isNotImport() {
        return notImport;
    }

    public void setNotImport(boolean notImport) {
        this.notImport = notImport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(String usedSize) {
        this.usedSize = usedSize;
    }

    public String getUnusedSize() {
        return unusedSize;
    }

    public void setUnusedSize(String unusedSize) {
        this.unusedSize = unusedSize;
    }

    public String getRaidType() {
        return raidType;
    }

    public void setRaidType(String raidType) {
        this.raidType = raidType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Disk> getDiskList() {
        return diskList;
    }

    public void setDiskList(List<Disk> diskList) {
        this.diskList = diskList;
    }

    public Disk getSelectDisk() {
        return selectDisk;
    }

    public void setSelectDisk(Disk selectDisk) {
        this.selectDisk = selectDisk;
    }

    public boolean isNotDestroyPool() {
        return notDestroyPool;
    }

    public void setNotDestroyPool(boolean notDestroyPool) {
        this.notDestroyPool = notDestroyPool;
    }

    public boolean isNotClearPool() {
        return notClearPool;
    }

    public void setNotClearPool(boolean notClearPool) {
        this.notClearPool = notClearPool;
    }

    public boolean isNotfixPool() {
        return notfixPool;
    }

    public void setNotfixPool(boolean notfixPool) {
        this.notfixPool = notfixPool;
    }

    public boolean isNotaddDisk() {
        return notaddDisk;
    }

    public void setNotaddDisk(boolean notaddDisk) {
        this.notaddDisk = notaddDisk;
    }

    public List<PhysicalDiskInformation> getDataDisks() {
        return dataDisks;
    }

    public void setDataDisks(List<PhysicalDiskInformation> dataDisks) {
        this.dataDisks = dataDisks;
    }

    public List<PhysicalDiskInformation> getCacheDisks() {
        return cacheDisks;
    }

    public void setCacheDisks(List<PhysicalDiskInformation> cacheDisks) {
        this.cacheDisks = cacheDisks;
    }

    public List<PhysicalDiskInformation> getLogDisks() {
        return logDisks;
    }

    public void setLogDisks(List<PhysicalDiskInformation> logDisks) {
        this.logDisks = logDisks;
    }

    public List<PhysicalDiskInformation> getSpareDisks() {
        return spareDisks;
    }

    public void setSpareDisks(List<PhysicalDiskInformation> spareDisks) {
        this.spareDisks = spareDisks;
    }

    public int getPoolType() {
        return poolType;
    }

    public void setPoolType(int poolType) {
        this.poolType = poolType;
    }

    public boolean isNotCapacityPool() {
        return notCapacityPool;
    }

    public void setNotCapacityPool(boolean notCapacityPool) {
        this.notCapacityPool = notCapacityPool;
    }
    
    
    
}
