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
public class TargetRes implements Serializable{
    private Target target;

    public TargetRes() {
    }

    public TargetRes(Target tar) {
        this.target = tar;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
    
    
}
