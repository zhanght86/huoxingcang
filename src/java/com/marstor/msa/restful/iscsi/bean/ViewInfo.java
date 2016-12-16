/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.iscsi.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class ViewInfo implements Serializable{
    private String host_group_name;
    private int lun;
    private String lu;

    public ViewInfo() {
    }

    public String getHost_group_name() {
        return host_group_name;
    }

    public void setHost_group_name(String host_group_name) {
        this.host_group_name = host_group_name;
    }

    public int getLun() {
        return lun;
    }

    public void setLun(int lun) {
        this.lun = lun;
    }
    
    public String getLu() {
        return lu;
    }

    public void setLu(String lu) {
        this.lu = lu;
    }
    
    
}
