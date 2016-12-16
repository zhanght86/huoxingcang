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
public class VolumeGroupRes implements Serializable{
    private VolumeGroup volume;

    public VolumeGroupRes() {
    }

    public VolumeGroupRes(VolumeGroup volume) {
        this.volume = volume;
    }

    public VolumeGroup getVolume() {
        return volume;
    }

    public void setVolume(VolumeGroup volume) {
        this.volume = volume;
    }

  

  
    
    
}
