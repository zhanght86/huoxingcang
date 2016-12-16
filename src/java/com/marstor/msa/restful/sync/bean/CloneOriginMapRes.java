/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.sync.bean;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zeroz
 */
@XmlRootElement
public class CloneOriginMapRes {
    private Map<String, String> map;

    public CloneOriginMapRes() {
    }

    public CloneOriginMapRes(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
    
    
    
}
