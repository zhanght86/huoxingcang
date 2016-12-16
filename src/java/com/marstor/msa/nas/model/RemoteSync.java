/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class RemoteSync implements Serializable{
    private String  TargetFileSystem;
    private String  LastSnap;
    private String  targetIP;

    public RemoteSync(String TargetFileSystem, String LastSnap, String targetIP) {
        this.TargetFileSystem = TargetFileSystem;
        this.LastSnap = LastSnap;
        this.targetIP = targetIP;
    }

    public String getTargetFileSystem() {
        return TargetFileSystem;
    }

    public void setTargetFileSystem(String TargetFileSystem) {
        this.TargetFileSystem = TargetFileSystem;
    }

    public String getLastSnap() {
        return LastSnap;
    }

    public void setLastSnap(String LastSnap) {
        this.LastSnap = LastSnap;
    }

    public String getTargetIP() {
        return targetIP;
    }

    public void setTargetIP(String targetIP) {
        this.targetIP = targetIP;
    }
    
}
