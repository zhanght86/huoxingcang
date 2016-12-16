/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.sync.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class CloneOrigin implements Serializable {
    
    String clone;
    String snapshot;

    public CloneOrigin() {
    }

    public String getClone() {
        return clone;
    }

    public void setClone(String clone) {
        this.clone = clone;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }
    
}
