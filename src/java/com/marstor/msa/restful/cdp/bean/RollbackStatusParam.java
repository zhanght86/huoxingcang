/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

/**
 *
 * @author Administrator
 */
public class RollbackStatusParam {

    private String disk_group_guid;
    private boolean save;

    public String getDisk_group_guid() {
        return disk_group_guid;
    }

    public void setDisk_group_guid(String disk_group_guid) {
        this.disk_group_guid = disk_group_guid;
    }


    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }


    
}
