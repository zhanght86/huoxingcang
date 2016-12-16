/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

/**
 *
 * @author Administrator
 */
public class GetQueryHandleParam {
    private String disk_group_guid;
    private long snapshot_id;

    public String getDisk_group_guid() {
        return disk_group_guid;
    }

    public void setDisk_group_guid(String disk_group_guid) {
        this.disk_group_guid = disk_group_guid;
    }

    public long getSnapshot_id() {
        return snapshot_id;
    }

    public void setSnapshot_id(long snapshot_id) {
        this.snapshot_id = snapshot_id;
    }

    
    
}
