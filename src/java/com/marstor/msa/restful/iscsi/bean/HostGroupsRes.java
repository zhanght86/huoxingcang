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
public class HostGroupsRes implements Serializable{
    private HostGroupInfo[] host_groups;

    public HostGroupsRes() {
    }

    public HostGroupsRes(HostGroupInfo[] host_groups) {
        this.host_groups = host_groups;
    }

    public HostGroupInfo[] getHost_groups() {
        return host_groups;
    }

    public void setHost_groups(HostGroupInfo[] host_groups) {
        this.host_groups = host_groups;
    }

   
    
}
