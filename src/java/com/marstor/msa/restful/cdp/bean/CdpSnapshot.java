/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import com.marstor.msa.cdp.bean.CdpSnapshotInfo;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class CdpSnapshot {
    private long snapshot_id;
    private String snapshot_name;
    private Date snapshot_time;
    private long snapshot_size;
   // private boolean has_zfs_snapshot;

    public CdpSnapshot() {
    }

    public CdpSnapshot(CdpSnapshotInfo param) {
        this.snapshot_id = param.getAgentID();
        this.snapshot_name = param.getSnapshotName();
        this.snapshot_time = param.getSnapshotTime();
        this.snapshot_size = param.getSnapshotSize();
        //this.has_zfs_snapshot = param.isbHasZFSSnapshot();
    }

    public long getSnapshot_id() {
        return snapshot_id;
    }

    public void setSnapshot_id(long snapshot_id) {
        this.snapshot_id = snapshot_id;
    }

    public String getSnapshot_name() {
        return snapshot_name;
    }

    public void setSnapshot_name(String snapshot_name) {
        this.snapshot_name = snapshot_name;
    }

    public Date getSnapshot_time() {
        return snapshot_time;
    }

    public void setSnapshot_time(Date snapshot_time) {
        this.snapshot_time = snapshot_time;
    }

    public long getSnapshot_size() {
        return snapshot_size;
    }

    public void setSnapshot_size(long snapshot_size) {
        this.snapshot_size = snapshot_size;
    }

//    public boolean isHas_zfs_snapshot() {
//        return has_zfs_snapshot;
//    }
//
//    public void setHas_zfs_snapshot(boolean has_zfs_snapshot) {
//        this.has_zfs_snapshot = has_zfs_snapshot;
//    }

    
    
}
