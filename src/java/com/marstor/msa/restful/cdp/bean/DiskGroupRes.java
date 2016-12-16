/*
 * To change this template, choose Tools | Templates
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
public class DiskGroupRes  implements Serializable{
    private CdpDiskGroup disk_group;

    public DiskGroupRes() {
    }
    
    

    public DiskGroupRes(CdpDiskGroup disk_group) {
        this.disk_group = disk_group;
    }

    public CdpDiskGroup getDisk_group() {
        return disk_group;
    }

    public void setDisk_group(CdpDiskGroup disk_group) {
        this.disk_group = disk_group;
    }
    
    
}
