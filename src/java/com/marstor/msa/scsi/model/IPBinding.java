/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

/**
 *
 * @author Administrator
 */
public class IPBinding {
    //private boolean isStart;
    private String  ip;

    public IPBinding() {
    }

    public IPBinding(String ip) {
        this.ip = ip;
    }

//    public IPBinding(boolean isStart, String ip) {
//        this.isStart = isStart;
//        this.ip = ip;
//    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
//
//    public boolean isIsStart() {
//        return isStart;
//    }
//
//    public void setIsStart(boolean isStart) {
//        this.isStart = isStart;
//    }
    
}
