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
public class ISCSIInitiatorsRes implements Serializable{
    private ISCSIInitiator[] initiators;

    public ISCSIInitiatorsRes() {
    }

    public ISCSIInitiatorsRes(ISCSIInitiator[] initiators) {
        this.initiators = initiators;
    }

    public ISCSIInitiator[] getInitiators() {
        return initiators;
    }

    public void setInitiators(ISCSIInitiator[] initiators) {
        this.initiators = initiators;
    }
    
}
