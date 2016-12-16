/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.cdp.model;

import java.io.Serializable;

/**
 *
 * @author root
 */
public class Host implements Serializable {

    private String hostname;
    private String ip;
    private String port;
    private int OStype;               //<!--1 windows 2 Linux 3 Solaris 4 AIX 5 HP-UNIX 0 Other-->

    public Host() {
    }

    public Host(String hostname, String ip, String port, int OStype) {
        this.hostname = hostname;
        this.ip = ip;
        this.port = port;
        this.OStype = OStype;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getOStype() {
        return OStype;
    }

    public void setOStype(int OStype) {
        this.OStype = OStype;
    }

}
