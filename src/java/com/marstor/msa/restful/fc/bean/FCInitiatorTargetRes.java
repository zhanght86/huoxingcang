/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.fc.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class FCInitiatorTargetRes implements Serializable{
    private FCInitiatorTarget[] initiators;

    public FCInitiatorTargetRes() {
    }

    public FCInitiatorTargetRes(FCInitiatorTarget[] initiators) {
        this.initiators = initiators;
    }

    public FCInitiatorTarget[] getInitiators() {
        return initiators;
    }

    public void setInitiators(FCInitiatorTarget[] initiators) {
        this.initiators = initiators;
    }
    
}
