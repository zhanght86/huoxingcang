/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

/**
 *
 * @author Administrator
 */
public class InitiatoriSCSITargetConnection {
    
    private String  localAddress;
    private String  peerAddress;

    public InitiatoriSCSITargetConnection(String localAddress, String targetAddress) {
        this.localAddress = localAddress;
        this.peerAddress = targetAddress;
    }

    
    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getPeerAddress() {
        return peerAddress;
    }

    public void setPeerAddress(String peerAddress) {
        this.peerAddress = peerAddress;
    }
    
    
    
}
