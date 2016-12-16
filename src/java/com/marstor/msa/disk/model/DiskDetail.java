/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.disk.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class DiskDetail implements Serializable{
    public String diskname;
    public String guid;
    public String lun;
    public String type;

    public String getDiskname() {
        return diskname;
    }

    public void setDiskname(String diskname) {
        this.diskname = diskname;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLun() {
        return lun;
    }

    public void setLun(String lun) {
        this.lun = lun;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    
    
}
