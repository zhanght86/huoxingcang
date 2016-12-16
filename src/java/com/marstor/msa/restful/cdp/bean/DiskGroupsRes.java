/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class DiskGroupsRes implements Serializable{
    private List<CdpDiskGroup> disk_groups;

    public DiskGroupsRes() {
    }    
    

    public DiskGroupsRes(List<CdpDiskGroup> disk_groups) {
        this.disk_groups = disk_groups;
    }

    public List<CdpDiskGroup> getDisk_groups() {
        return disk_groups;
    }

    public void setDisk_groups(List<CdpDiskGroup> disk_groups) {
        this.disk_groups = disk_groups;
    }
    
}
