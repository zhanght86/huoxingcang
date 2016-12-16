/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.fc.bean;

import com.marstor.msa.restful.iscsi.bean.*;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class FCInitiatorsRes implements Serializable{
    private FCInitiator[] fc_initiators;

    public FCInitiatorsRes() {
    }

    public FCInitiatorsRes(FCInitiator[] initiators) {
        this.fc_initiators = initiators;
    }

    public FCInitiator[] getFc_initiators() {
        return fc_initiators;
    }

    public void setFc_initiators(FCInitiator[] fc_initiators) {
        this.fc_initiators = fc_initiators;
    }    
    
}
