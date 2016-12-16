/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.model;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ClientInfo {
    private String ip = "192.168.2.20";
    private int port = 1100;
    private String name = "ZEROBLAC-26CW3F";
    private int OStype = 0;               //<!--1 windows 2 Linux 3 Solaris 4 AIX 5 HP-UNIX 0 Other-->
    private ClientDiskInfo[] clientDisks;
    private LinuxClientDiskInfo[] linuxClientDisks;
    private LinuxVolumeGroupInfo[] linuxVGs;
    
    public ClientInfo(){
    
    }

    public ClientInfo(String name){
        this.name = name;
    }

    public LinuxVolumeGroupInfo[] getLinuxVGs() {
        return linuxVGs;
    }

    public void setLinuxVGs(LinuxVolumeGroupInfo[] linuxVGs) {
        this.linuxVGs = linuxVGs;
    }

    public LinuxClientDiskInfo[] getLinuxClientDisks() {
        return linuxClientDisks;
    }

    public void setLinuxClientDisks(LinuxClientDiskInfo[] linuxClienDisks) {
        this.linuxClientDisks = linuxClienDisks;
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientDiskInfo[] getClientDisks() {
        return clientDisks;
    }

    public void setClientDisks(ClientDiskInfo[] clientDisks) {
        this.clientDisks = clientDisks;
    }

    public int getOStype() {
        return OStype;
    }

    public void setOStype(int OStype) {
        this.OStype = OStype;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    
}
