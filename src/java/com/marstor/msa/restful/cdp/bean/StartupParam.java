/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

/**
 *
 * @author Administrator
 */
public class StartupParam {

    private String disk_group_guid;
    private String cdp_status;

    public String getDisk_group_guid() {
        return disk_group_guid;
    }

    public void setDisk_group_guid(String disk_group_guid) {
        this.disk_group_guid = disk_group_guid;
    }

    public String getCdp_status() {
        return cdp_status;
    }

    public void setCdp_status(String cdp_status) {
        this.cdp_status = cdp_status;
    }

    
}
