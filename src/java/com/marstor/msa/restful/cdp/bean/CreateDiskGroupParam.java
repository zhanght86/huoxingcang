/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

/**
 *
 * @author Administrator
 */
public class CreateDiskGroupParam {
    private String volume_name;
    private String disk_group_name;

    public String getVolume_name() {
        return volume_name;
    }

    public void setVolume_name(String volume_name) {
        this.volume_name = volume_name;
    }

    public String getDisk_group_name() {
        return disk_group_name;
    }

    public void setDisk_group_name(String disk_group_name) {
        this.disk_group_name = disk_group_name;
    }

    
}
