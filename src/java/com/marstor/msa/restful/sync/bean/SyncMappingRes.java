/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.sync.bean;

import com.marstor.msa.sync.bean.SyncMapping;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zeroz
 */
@XmlRootElement
public class SyncMappingRes {
    private List<SyncMapping> syn_mappings;

    public SyncMappingRes() {
    }

    public SyncMappingRes(List<SyncMapping> syn_mappings) {
        this.syn_mappings = syn_mappings;
    }

    public List<SyncMapping> getSyn_mappings() {
        return syn_mappings;
    }

    public void setSyn_mappings(List<SyncMapping> syn_mappings) {
        this.syn_mappings = syn_mappings;
    }
    
    
}
