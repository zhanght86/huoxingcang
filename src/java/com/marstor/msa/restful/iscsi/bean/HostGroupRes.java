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
public class HostGroupRes implements Serializable{
    private HostGroupInfo host_group;

    public HostGroupRes() {
    }

    public HostGroupRes(HostGroupInfo host_group) {
        this.host_group = host_group;
    }

    public HostGroupInfo getHost_group() {
        return host_group;
    }

    public void setHost_group(HostGroupInfo host_group) {
        this.host_group = host_group;
    }
    
}
