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
public class VolumeDiskRes implements Serializable{
    private VolumeDisk[] disks;

    public VolumeDiskRes() {
    }

    public VolumeDiskRes(VolumeDisk[] disks) {
        this.disks = disks;
    }

    public VolumeDisk[] getDisks() {
        return disks;
    }

    public void setDisks(VolumeDisk[] disks) {
        this.disks = disks;
    }




    
    
}
