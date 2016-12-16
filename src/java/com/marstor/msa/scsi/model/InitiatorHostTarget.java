/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

/**
 *
 * @author Administrator
 */
public class InitiatorHostTarget {
    private String targetName;
    private String targetAddress;
    private String status;
    private boolean  addedOrNot;

    public InitiatorHostTarget(String name, String ip, String status) {
        this.targetName = name;
        this.targetAddress = ip;
        this.status = status;
    }

    public InitiatorHostTarget(String targetName, String targetAddress, boolean addedOrNot) {
        this.targetName = targetName;
        this.targetAddress = targetAddress;
        this.addedOrNot = addedOrNot;
    }


    public InitiatorHostTarget(String targetName, String targetAddress) {
        this.targetName = targetName;
        this.targetAddress = targetAddress;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAddedOrNot() {
        return addedOrNot;
    }

    public void setAddedOrNot(boolean addedOrNot) {
        this.addedOrNot = addedOrNot;
    }

   
    
}
