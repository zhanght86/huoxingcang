/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.volume.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class VolumeDisk implements Serializable{
    private String disk_name;
//    private String disk_position;
    private String disk_num;
    private String disk_size;

    public VolumeDisk() {
    }
    
    public String getDisk_name() {
        return disk_name;
    }

    public void setDisk_name(String disk_name) {
        this.disk_name = disk_name;
    }

    public String getDisk_num() {
        return disk_num;
    }

    public void setDisk_num(String disk_num) {
        this.disk_num = disk_num;
    }

    public String getDisk_size() {
        return disk_size;
    }

    public void setDisk_size(String disk_size) {
        this.disk_size = disk_size;
    }

    

}
