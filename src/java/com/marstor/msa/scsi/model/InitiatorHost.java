/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class InitiatorHost {
    
    private String address;
    private ArrayList<InitiatorHostTarget>  targets = new ArrayList<InitiatorHostTarget>();

    public InitiatorHost(String address) {
        this.address = address;
    }

    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<InitiatorHostTarget> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<InitiatorHostTarget> targets) {
        this.targets = targets;
    }
    
    
    
}
