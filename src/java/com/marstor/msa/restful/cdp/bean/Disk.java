/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

/**
 *
 * @author Administrator
 */
public class Disk {
    private int lun;
    private String disk_guid;

    public int getLun() {
        return lun;
    }

    public void setLun(int lun) {
        this.lun = lun;
    }

    public String getDisk_guid() {
        return disk_guid;
    }

    public void setDisk_guid(String disk_guid) {
        this.disk_guid = disk_guid;
    }

    
}
