/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.sync.bean;

import com.marstor.msa.sync.bean.MarsHost;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zeroz
 */

@XmlRootElement
public class MarsHostRes {
    private List<MarsHost> MarsHosts;

    public MarsHostRes() {
    }
    
    public MarsHostRes(List<MarsHost> MarsHosts) {
        this.MarsHosts = MarsHosts;
    }

    public List<MarsHost> getMarsHosts() {
        return MarsHosts;
    }

    public void setMarsHosts(List<MarsHost> MarsHosts) {
        this.MarsHosts = MarsHosts;
    }
    
    
    
}
