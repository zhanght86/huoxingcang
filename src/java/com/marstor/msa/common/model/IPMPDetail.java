/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
//@ManagedBean(name = "iPMPDetail")
//@RequestScoped
public class IPMPDetail implements Serializable{
    public String IPMPGroup;
    public String ip;
    public String entry;
    public String exit;
    public String state;
    /**
     * Creates a new instance of IPMPDetail
     */
    public IPMPDetail() {
    }

    public String getIPMPGroup() {
        return IPMPGroup;
    }

    public void setIPMPGroup(String IPMPGroup) {
        this.IPMPGroup = IPMPGroup;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getExit() {
        return exit;
    }

    public void setExit(String exit) {
        this.exit = exit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    
}
