/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.cloudbean;

import java.util.Date;

/**
 *
 * @author zeroz
 */
public class CDPSnapshotResponse extends BeanResponse {

    private int snapshotcount;
    private String name;
    private String uuid;
    private String zfsname;
    private int count;
    private int isprotect;
    private Date createtime;
    private String snapshotname;
    private String snapshotfullname;
    private long snapshotshotsize;

    public int getSnapshotcount() {
        return snapshotcount;
    }

    public void setSnapshotcount(int snapshotcount) {
        this.snapshotcount = snapshotcount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getZfsname() {
        return zfsname;
    }

    public void setZfsname(String zfsname) {
        this.zfsname = zfsname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIsprotect() {
        return isprotect;
    }

    public void setIsprotect(int isprotect) {
        this.isprotect = isprotect;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getSnapshotname() {
        return snapshotname;
    }

    public void setSnapshotname(String snapshotname) {
        this.snapshotname = snapshotname;
    }

    public String getSnapshotfullname() {
        return snapshotfullname;
    }

    public void setSnapshotfullname(String snapshotfullname) {
        this.snapshotfullname = snapshotfullname;
    }

    public long getSnapshotshotsize() {
        return snapshotshotsize;
    }

    public void setSnapshotshotsize(long snapshotshotsize) {
        this.snapshotshotsize = snapshotshotsize;
    }

  
    
    
    
}
