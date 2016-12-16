/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class CdpDiskGroup  implements Serializable{

    private String disk_group_guid;
    private String disk_group_name;
    private String disk_group_path;
    //private String disk_zfs_name;
    private int disk_count;
    private int snapshot_count;
    private long record_count;
    private String cdp_status;
    private boolean log_full;
    private long total_disk_size;
    private String mount_status;
//    private String used_size;
//    private String available_size;
    private LogPolicy log_policy;
    
    public CdpDiskGroup(){    
    }

    public CdpDiskGroup(CdpDiskGroupInfo origin) {
        disk_group_guid = origin.getDiskGroupGuid();
        disk_group_name = origin.getDiskGroupName();
        disk_group_path = origin.getDiskGroupPath();
        //disk_zfs_name = origin.getDiskZfsName();
        disk_count = origin.getDiskCount();
        snapshot_count = origin.getSnapshotCount();
        record_count = origin.getLogRecordCount();
        cdp_status = origin.isCDPStarted()? "on" : "off";
        log_full = origin.isbLogFull();
        total_disk_size = origin.getTotalDiskSize();
        mount_status = origin.getiMount() == 1? "online" :"offline";
//        used_size = origin.getStrUsedSize();
//        available_size = origin.getStrAvailSize();
        log_policy = new LogPolicy(origin.getLogPolicy());
    }

    public String getDisk_group_guid() {
        return disk_group_guid;
    }

    public void setDisk_group_guid(String disk_group_guid) {
        this.disk_group_guid = disk_group_guid;
    }

    public String getDisk_group_name() {
        return disk_group_name;
    }

    public void setDisk_group_name(String disk_group_name) {
        this.disk_group_name = disk_group_name;
    }

    public String getDisk_group_path() {
        return disk_group_path;
    }

    public void setDisk_group_path(String disk_group_path) {
        this.disk_group_path = disk_group_path;
    }

//    public String getDisk_zfs_name() {
//        return disk_zfs_name;
//    }
//
//    public void setDisk_zfs_name(String disk_zfs_name) {
//        this.disk_zfs_name = disk_zfs_name;
//    }

    public int getDisk_count() {
        return disk_count;
    }

    public void setDisk_count(int disk_count) {
        this.disk_count = disk_count;
    }

    public int getSnapshot_count() {
        return snapshot_count;
    }

    public void setSnapshot_count(int snapshot_count) {
        this.snapshot_count = snapshot_count;
    }

    public long getRecord_count() {
        return record_count;
    }

    public void setRecord_count(long record_count) {
        this.record_count = record_count;
    }

    

    public boolean isLog_full() {
        return log_full;
    }

    public void setLog_full(boolean log_full) {
        this.log_full = log_full;
    }

    public long getTotal_disk_size() {
        return total_disk_size;
    }

    public void setTotal_disk_size(long total_disk_size) {
        this.total_disk_size = total_disk_size;
    }

    public String getCdp_status() {
        return cdp_status;
    }

    public void setCdp_status(String cdp_status) {
        this.cdp_status = cdp_status;
    }

    public String getMount_status() {
        return mount_status;
    }

    public void setMount_status(String mount_status) {
        this.mount_status = mount_status;
    }

    //    public String getUsed_size() {
    //        return used_size;
    //    }
    //
    //    public void setUsed_size(String used_size) {
    //        this.used_size = used_size;
    //    }
    //
    //    public String getAvailable_size() {
    //        return available_size;
    //    }
    //
    //    public void setAvailable_size(String available_size) {
    //        this.available_size = available_size;
    //    }
    public LogPolicy getLog_policy() {
        return log_policy;
    }

    public void setLog_policy(LogPolicy log_policy) {
        this.log_policy = log_policy;
    }


    
}
