/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @introduction 获取物理主机网卡信息
 * @author Administrator
 */
@ManagedBean(name = "hostNICInfo")
@RequestScoped
public class HostNICInfo implements Serializable{
    
    private String NicName;  //网卡名字
    private String NicIpAddress;  //网卡ip地址

    /**
     * Creates a new instance of HostNICInfo
     */
    public HostNICInfo() {
    }
   
    public String getNicIpAddress() {
        return NicIpAddress;
    }

    public void setNicIpAddress(String NicIpAddress) {
        this.NicIpAddress = NicIpAddress;
    }

    public String getNicName() {
        return NicName;
    }

    public void setNicName(String NicName) {
        this.NicName = NicName;
    }
    
}
