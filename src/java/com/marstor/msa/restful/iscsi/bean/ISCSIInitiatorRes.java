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
public class ISCSIInitiatorRes implements Serializable{
    private ISCSIInitiator initiator;

    public ISCSIInitiatorRes() {
    }

    public ISCSIInitiatorRes(ISCSIInitiator initiator) {
        this.initiator = initiator;
    }

    public ISCSIInitiator getInitiator() {
        return initiator;
    }

    public void setInitiator(ISCSIInitiator initiator) {
        this.initiator = initiator;
    }
    
}
