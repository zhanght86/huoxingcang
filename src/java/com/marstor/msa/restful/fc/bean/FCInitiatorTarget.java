/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.fc.bean;

import com.marstor.msa.common.bean.FCInitiatorTargetInformation;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class FCInitiatorTarget {

    public String target_name = "";
    public ArrayList<InitiatorTargetDevice> devices = new ArrayList<InitiatorTargetDevice>();

    public FCInitiatorTarget() {
    }

    public FCInitiatorTarget(FCInitiatorTargetInformation origin) {
        target_name = origin.targetName;
        for (com.marstor.msa.common.bean.InitiatorTargetDevice device : origin.devices) {
            devices.add(new InitiatorTargetDevice(device));
        }
    }

    public String getTarget_name() {
        return target_name;
    }

    public void setTarget_name(String target_name) {
        this.target_name = target_name;
    }

    public ArrayList<InitiatorTargetDevice> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<InitiatorTargetDevice> devices) {
        this.devices = devices;
    }

}
