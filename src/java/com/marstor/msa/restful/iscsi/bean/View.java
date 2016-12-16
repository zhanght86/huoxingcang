/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.iscsi.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class View implements Serializable{
    private String disk_lu;
    private int lun_mapping;
    private String host_group_name;
    private String target_group_name;
    private int lun;

    public View() {
    }

    public String getDisk_lu() {
        return disk_lu;
    }

    public void setDisk_lu(String disk_lu) {
        this.disk_lu = disk_lu;
    }

    public int getLun_mapping() {
        return lun_mapping;
    }

    public void setLun_mapping(int lun_mapping) {
        this.lun_mapping = lun_mapping;
    }


    public String getHost_group_name() {
        return host_group_name;
    }

    public void setHost_group_name(String host_group_name) {
        this.host_group_name = host_group_name;
    }

    public String getTarget_group_name() {
        return target_group_name;
    }

    public void setTarget_group_name(String target_group_name) {
        this.target_group_name = target_group_name;
    }

    public int getLun() {
        return lun;
    }

    public void setLun(int lun) {
        this.lun = lun;
    }
    
    
}
