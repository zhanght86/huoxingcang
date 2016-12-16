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
public class FileSystemInfo implements Serializable {
    public String name;
    public String used;
    public String usedStr;
    public String available;
    public String mountpoint;
    public String quotaValue;
    public boolean isSetQuota;
    public boolean isSetDedup;
    public String Compress;
    public boolean isVerify;
    public String recordsize;
    public boolean isMounted; 
    public boolean isCompress;  //∆Ù”√ ˝æ›—πÀı
     public List<VirtualDetail> detail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMountpoint() {
        return mountpoint;
    }

    public void setMountpoint(String mountpoint) {
        this.mountpoint = mountpoint;
    }

    public String getQuotaValue() {
        return quotaValue;
    }

    public void setQuotaValue(String quotaValue) {
        this.quotaValue = quotaValue;
    }

    public boolean isIsSetQuota() {
        return isSetQuota;
    }

    public void setIsSetQuota(boolean isSetQuota) {
        this.isSetQuota = isSetQuota;
    }

    public boolean isIsSetDedup() {
        return isSetDedup;
    }

    public void setIsSetDedup(boolean isSetDedup) {
        this.isSetDedup = isSetDedup;
    }

    public String getCompress() {
        return Compress;
    }

    public void setCompress(String Compress) {
        this.Compress = Compress;
    }

    public boolean isIsVerify() {
        return isVerify;
    }

    public void setIsVerify(boolean isVerify) {
        this.isVerify = isVerify;
    }

    public String getRecordsize() {
        return recordsize;
    }

    public void setRecordsize(String recordsize) {
        this.recordsize = recordsize;
    }

    public boolean isIsMounted() {
        return isMounted;
    }

    public void setIsMounted(boolean isMounted) {
        this.isMounted = isMounted;
    }

    public boolean isIsCompress() {
        return isCompress;
    }

    public void setIsCompress(boolean isCompress) {
        this.isCompress = isCompress;
    }

    public List<VirtualDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<VirtualDetail> detail) {
        this.detail = detail;
    }

    public String getUsedStr() {
        return usedStr;
    }

    public void setUsedStr(String usedStr) {
        this.usedStr = usedStr;
    }
    
}
