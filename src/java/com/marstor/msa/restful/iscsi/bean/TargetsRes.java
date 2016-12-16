/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.iscsi.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class TargetsRes implements Serializable{
    private Target[] targets;

    public TargetsRes() {
    }

    public TargetsRes(Target[] targets) {
        this.targets = targets;
    }

    public Target[] getTargets() {
        return targets;
    }

    public void setTargets(Target[] targets) {
        this.targets = targets;
    }
    
    
}
