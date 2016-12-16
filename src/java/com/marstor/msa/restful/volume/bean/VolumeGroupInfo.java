/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.volume.bean;

import com.marstor.msa.restful.cdp.bean.DisksRes;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class VolumeGroupInfo  implements Serializable {
    private String volume_name;
    private String volume_type;
    private List<DisksRes> disks;
  

    public VolumeGroupInfo() {
    }

    public String getVolume_name() {
        return volume_name;
    }

    public void setVolume_name(String volume_name) {
        this.volume_name = volume_name;
    }

    public String getVolume_type() {
        return volume_type;
    }

    public void setVolume_type(String volume_type) {
        this.volume_type = volume_type;
    }

    

    public List<DisksRes> getDisks() {
        return disks;
    }

    public void setDisks(List<DisksRes> disks) {
        this.disks = disks;
    }

   

 

 
}
