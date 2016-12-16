/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Íø¿¨¾ÛºÏBean
 * @author Administrator
 */
//@ManagedBean(name = "nICAggregation")
//@RequestScoped
public class NICAggregation implements Serializable{
    public String name;
    public String nic;
    public String ip;
    public String subnet;
    public String ipmp;
    public String state;
    public String mode;
    public boolean enable;
    public String gateway;

    /**
     * Creates a new instance of NICAggregation
     */
    public NICAggregation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    public String getIpmp() {
        return ipmp;
    }

    public void setIpmp(String ipmp) {
        this.ipmp = ipmp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

   
    
}
