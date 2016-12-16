/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.nas.cloudbean;

import com.marstor.msa.restful.cdp.cloudbean.BeanResponse;
import java.util.Date;

/**
 *
 * @author zeroz
 */
public class NASSnapshotResponse extends BeanResponse{
    
    private int snapshotcount;
    private String name;
    private String zfsname;
    private int isprotect;
    private String createtime;
    private String snapshotname;
    private String snapshotfullname;
    private String snapshotshotsize;

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

    public String getZfsname() {
        return zfsname;
    }

    public void setZfsname(String zfsname) {
        this.zfsname = zfsname;
    }

    public int getIsprotect() {
        return isprotect;
    }

    public void setIsprotect(int isprotect) {
        this.isprotect = isprotect;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
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

    public String getSnapshotshotsize() {
        return snapshotshotsize;
    }

    public void setSnapshotshotsize(String snapshotshotsize) {
        this.snapshotshotsize = snapshotshotsize;
    }


    
    
    
}
