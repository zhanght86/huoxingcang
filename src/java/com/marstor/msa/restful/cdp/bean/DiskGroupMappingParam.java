/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class DiskGroupMappingParam {
    private String host_group_name;
    private String disk_group_guid;
    private List<Disk> disks;

    public String getHost_group_name() {
        return host_group_name;
    }

    public void setHost_group_name(String host_group_name) {
        this.host_group_name = host_group_name;
    }

    public List<Disk> getDisks() {
        return disks;
    }

    public void setDisks(List<Disk> disks) {
        this.disks = disks;
    }

    public String getDisk_group_guid() {
        return disk_group_guid;
    }

    public void setDisk_group_guid(String disk_group_guid) {
        this.disk_group_guid = disk_group_guid;
    }
    
    
    
}
