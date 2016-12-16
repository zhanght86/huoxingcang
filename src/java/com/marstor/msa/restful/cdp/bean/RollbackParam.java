/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public class RollbackParam {

    private String disk_group_guid;
    private Date rollback_time;

    public String getDisk_group_guid() {
        return disk_group_guid;
    }

    public void setDisk_group_guid(String disk_group_guid) {
        this.disk_group_guid = disk_group_guid;
    }

    public Date getRollback_time() {
        return rollback_time;
    }

    public void setRollback_time(Date rollback_time) {
        this.rollback_time = rollback_time;
    }

   
        
}
