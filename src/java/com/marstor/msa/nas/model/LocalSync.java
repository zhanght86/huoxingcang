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
public class LocalSync implements Serializable{
    private String  TargetFileSystem;
    private String  LastSnap;

    public LocalSync(String sharePath, String LastSnap) {
        this.TargetFileSystem = sharePath;
        this.LastSnap = LastSnap;
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

    
    
}
