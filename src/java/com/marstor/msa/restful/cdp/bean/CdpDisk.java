/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import com.marstor.msa.cdp.bean.CdpDiskInfo;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class CdpDisk {
    private String disk_guid;
    private String disk_path;
    private String disk_name;
    private long disk_id;
    private long disk_size;
    private String disk_group_guid;    
    private String disk_create_status;
    private String disk_status;
    //private String disk_lu;
    private int record_count;
    
    public CdpDisk(){
    
    }
    
    public CdpDisk(CdpDiskInfo origin){
        disk_guid = origin.getDiskGuid();
        disk_path = origin.getDiskFileName_Path();
        disk_name = origin.getDiskName();
        disk_id = origin.getDiskID();
        disk_size = origin.getDiskSize();
        disk_group_guid = origin.getDiskGroupGuid();
        record_count = origin.getLogRecordCount();
        if(origin.getDiskCreateStatus() == 0){
            disk_create_status = "created";
        }
        if(origin.getDiskCreateStatus() == 1){
            disk_create_status = "creating";
        }
        if(origin.getDiskCreateStatus() == 2){
            disk_create_status = "failed";
        }
        disk_status = origin.getLuInfoBean().getOperationalStatus().toLowerCase();
       // disk_lu = origin.getLuInfoBean().getlUName();
        
    }

    public String getDisk_guid() {
        return disk_guid;
    }

    public void setDisk_guid(String disk_guid) {
        this.disk_guid = disk_guid;
    }

    public String getDisk_path() {
        return disk_path;
    }

    public void setDisk_path(String disk_path) {
        this.disk_path = disk_path;
    }

    public String getDisk_name() {
        return disk_name;
    }

    public void setDisk_name(String disk_name) {
        this.disk_name = disk_name;
    }

    public long getDisk_id() {
        return disk_id;
    }

    public void setDisk_id(long disk_id) {
        this.disk_id = disk_id;
    }

    public long getDisk_size() {
        return disk_size;
    }

    public void setDisk_size(long disk_size) {
        this.disk_size = disk_size;
    }

    public String getDisk_group_guid() {
        return disk_group_guid;
    }

    public void setDisk_group_guid(String disk_group_guid) {
        this.disk_group_guid = disk_group_guid;
    }

    public int getRecord_count() {
        return record_count;
    }

    public void setRecord_count(int record_count) {
        this.record_count = record_count;
    }

    public String getDisk_create_status() {
        return disk_create_status;
    }

    public void setDisk_create_status(String disk_create_status) {
        this.disk_create_status = disk_create_status;
    }

    public String getDisk_status() {
        return disk_status;
    }

    public void setDisk_status(String disk_status) {
        this.disk_status = disk_status;
    }

//    public String getDisk_lu() {
//        return disk_lu;
//    }
//
//    public void setDisk_lu(String disk_lu) {
//        this.disk_lu = disk_lu;
//    }

    
    
}
