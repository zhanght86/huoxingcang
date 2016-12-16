/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.bean.FCInitiatorTargetInformation;
import com.marstor.msa.common.bean.InitiatorTargetDevice;
import com.marstor.msa.common.util.MSAResource;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class FCInitiator {

    private String name;
    private boolean isConnected;
    private String CurrentSpeed;
    private String state;
    //private ArrayList<FCTargetMappingIntiator>  targets;
    private ArrayList<FCInitiatorTargetInformation> targetInfos = new ArrayList<FCInitiatorTargetInformation>();
    private MSAResource res = new MSAResource();
    private int targetSize = 0;
    private boolean isContainsTarget;
    //private ArrayList<String> targetItems = new ArrayList<String>();

    public FCInitiator() {
    }

//    public FCInitiator(String name, boolean isConnected, String bandWidth, ArrayList<FCTargetMappingIntiator> targets) {
//        this.name = name;
//        this.isConnected = isConnected;
//        this.CurrentSpeed = bandWidth;
//        this.targets = targets;
//    }
    public FCInitiator(String name, String CurrentSpeed, String state, FCInitiatorTargetInformation infos[]) {
        this.name = name;
        this.CurrentSpeed = CurrentSpeed;
        if (infos == null || infos.length == 0) {
            this.state = res.get("notConnected");
            this.targetSize = -1;
            this.isContainsTarget = false;
            //Debug.print("  fc initiator no target! ");
        } else {
            this.state = res.get("connected");
            // Debug.print("fc initiator target info: ");
            for (int i = 0; i < infos.length; i++) {
                // Debug.print("target name:  "+infos[i].targetName);
                // Debug.print(" target device:");
//                for(int j=0;j<infos[i].devices.size();j++) {
//                    Debug.print(" device name:  "+infos[i].devices.get(j).deviceName);
//                }
                targetInfos.add(infos[i]);
                //targetItems.add(String.valueOf(i));
            }
            this.targetSize = infos.length;
            this.targetSize--;
            this.isContainsTarget = true;
        }
        Debug.print("target num:  " + targetInfos.size());
        for (FCInitiatorTargetInformation info : targetInfos) {
            Debug.print("target name:  " + info.targetName);
            ArrayList<InitiatorTargetDevice> devices = info.devices;
            for (InitiatorTargetDevice device : devices) {
                Debug.print("device name:  " + device.deviceName);
            }
        }
        // this.state = state;


    }

    public boolean isIsContainsTarget() {
        return isContainsTarget;
    }

    public void setIsContainsTarget(boolean isContainsTarget) {
        this.isContainsTarget = isContainsTarget;
    }

    public int getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(int targetSize) {
        this.targetSize = targetSize;
    }

    public ArrayList<FCInitiatorTargetInformation> getTargetInfos() {
        return targetInfos;
    }

    public void setTargetInfos(ArrayList<FCInitiatorTargetInformation> targetInfos) {
        this.targetInfos = targetInfos;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public String getCurrentSpeed() {
        return CurrentSpeed;
    }

    public void setCurrentSpeed(String CurrentSpeed) {
        this.CurrentSpeed = CurrentSpeed;
    }
}
