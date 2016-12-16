/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.common.util.MSAResource;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class FileSystem implements Serializable{

    private String name;
    private String used;
    private String usedStr;
    private String available;
    private String mountpoint;
    private String quotaValue;
    private boolean isSetQuota;
    private boolean isSetDedup;
    private boolean isMounted;
    private String Compress;
    private boolean isVerify;
    private String recordsize;
    private boolean isCompress;
    private ArrayList<NameValue> details = new ArrayList<NameValue>();
    private MSAResource res = new MSAResource();

    public FileSystem(String name, String used, String available, String mountpoint, String quotaValue, boolean isSetQuota, boolean isSetDedup, boolean isMounted, String Compress, boolean isVerify, String recordsize) {
        this.name = name;
        this.used = used;
        this.usedStr = used + "B";
        this.available = available + "B";
        this.mountpoint = mountpoint;
        //this.quotaValue = quotaValue;
        if (isSetQuota) {
            double size = Double.valueOf(quotaValue.substring(0, quotaValue.length() - 1));
            if (quotaValue.endsWith("G")) {
                this.quotaValue = String.valueOf(size);
            }
            if (quotaValue.endsWith("M")) {
                size = size / 1024;
                this.quotaValue = String.valueOf(size);
            }
            if (quotaValue.endsWith("T")) {
                size = size * 1024;
                this.quotaValue = String.valueOf(size);
            }
            if (quotaValue.endsWith("P")) {
                size = size * 1024 * 1024;
                this.quotaValue = String.valueOf(size);
            }
        }
        this.isSetQuota = isSetQuota;
        this.isSetDedup = isSetDedup;
        this.isMounted = isMounted;
        this.Compress = this.compressLevel(Compress) + "";
        this.isVerify = isVerify;

        //this.recordsize = recordsize;
        if ((recordsize != null) && (!"".equalsIgnoreCase(recordsize))) {
            this.recordsize = recordsize;
        }
        if (Compress.equalsIgnoreCase("null") || Compress.equalsIgnoreCase("") || Compress.equals("off")) {
           // this.setIsCompress(false);
            this.isCompress = false;
        } else {
          //  this.setIsCompress(true);
            this.isCompress =true;

        }
        NameValue value;
        if (isSetDedup) {
            value = new NameValue(res.get("dedup"), res.get("yes"));
        } else {
            value = new NameValue(res.get("dedup"), res.get("no"));
        }
        this.details.add(value);
        if (isVerify) {
            value = new NameValue(res.get("verify"), res.get("yes"));
        } else {
            value = new NameValue(res.get("verify"), res.get("no"));
        }
        this.details.add(value);
        if (Compress.equalsIgnoreCase("null") || Compress.equalsIgnoreCase("") || Compress.equalsIgnoreCase("off")) {
            value = new NameValue(res.get("compress"), res.get("no"));
        } else {
            value = new NameValue(res.get("compress"), res.get("yes"));
        }
        this.details.add(value);
        if(this.isCompress) {
             value = new NameValue(res.get("compressLevel"), this.Compress);
             this.details.add(value);
        }
        if(isSetQuota) {
              value = new NameValue(res.get("quota"), res.get("yes"));
        }else {
             value = new NameValue(res.get("quota"), res.get("no"));
        }
        this.details.add(value);
        if(this.isSetQuota) {
            value = new NameValue(res.get("quotaValue"), this.quotaValue+"GB");
            this.details.add(value);
        }
        value = new NameValue(res.get("recordsize"), this.recordsize+"B");
        this.details.add(value);
        //this.setCompress(this.compressLevel(fileinfo.getCompress())+"");

    }

    public String getUsedStr() {
        return usedStr;
    }

    public void setUsedStr(String usedStr) {
        this.usedStr = usedStr;
    }

    public MSAResource getRes() {
        return res;
    }

    public void setRes(MSAResource res) {
        this.res = res;
    }

    public ArrayList<NameValue> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<NameValue> details) {
        this.details = details;
    }

    public boolean isIsCompress() {
        return isCompress;
    }

    public void setIsCompress(boolean isCompress) {
        this.isCompress = isCompress;
    }

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

    public boolean isIsMounted() {
        return isMounted;
    }

    public void setIsMounted(boolean isMounted) {
        this.isMounted = isMounted;
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

    public int compressLevel(String Compress) {
        if (null == Compress || "".equalsIgnoreCase(Compress)) {
            return 0;
        }
        if ("off".equalsIgnoreCase(Compress)) {
            return 0;
        }
        if ("gzip".equalsIgnoreCase(Compress)) {
            return 6;
        }
        char num = Compress.charAt(Compress.length() - 1);
        try {
            return Integer.valueOf(num + "");
        } catch (Exception e) {
            return 0;
        }
    }
}
