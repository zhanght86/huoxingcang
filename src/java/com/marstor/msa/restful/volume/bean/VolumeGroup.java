/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.volume.bean;

import com.marstor.msa.bean.TapeInformation;
import com.marstor.msa.common.bean.PhysicalDiskInformation;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class VolumeGroup {
    private String volume_name;
    private String volume_size;
    private String volume_used_size;
    private String volume_available_size;
    private String volume_type;
    private String volume_state;

//    private ArrayList<PhysicalDiskInformation> data_disk;
//    private ArrayList<PhysicalDiskInformation> spare_disk;
//    private ArrayList<PhysicalDiskInformation> cache_disk;
//    private ArrayList<PhysicalDiskInformation> log_disk;
//    private ArrayList<TapeInformation> ti;
//    private boolean bRequireImport;
    public String getVolume_name() {
        return volume_name;
    }

    public void setVolume_name(String volume_name) {
        this.volume_name = volume_name;
    }

    public String getVolume_size() {
        return volume_size;
    }

    public void setVolume_size(String volume_size) {
        this.volume_size = volume_size;
    }

    public String getVolume_used_size() {
        return volume_used_size;
    }

    public void setVolume_used_size(String volume_used_size) {
        this.volume_used_size = volume_used_size;
    }

    public String getVolume_available_size() {
        return volume_available_size;
    }

    public void setVolume_available_size(String volume_available_size) {
        this.volume_available_size = volume_available_size;
    }

    public String getVolume_type() {
        return volume_type;
    }

    public void setVolume_type(String volume_type) {
        this.volume_type = volume_type;
    }

    public String getVolume_state() {
        return volume_state;
    }

    public void setVolume_state(String volume_state) {
        this.volume_state = volume_state;
    }
  
    
}
