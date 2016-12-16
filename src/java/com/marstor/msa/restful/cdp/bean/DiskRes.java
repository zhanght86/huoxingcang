/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class DiskRes {
    private CdpDisk[] disks;

    public DiskRes() {
    }
    
    

    public DiskRes(CdpDisk[] disks) {
        this.disks = disks;
    }

    public CdpDisk[] getDisks() {
        return disks;
    }

    public void setDisks(CdpDisk[] disks) {
        this.disks = disks;
    }
    
}
