/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.cdp.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class RemoveView implements Serializable{
    private String disk_lu;
    private int lun_mapping;

    public RemoveView() {
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

 

    
    
    
}
