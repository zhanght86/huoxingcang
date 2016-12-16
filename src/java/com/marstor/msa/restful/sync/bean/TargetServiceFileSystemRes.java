/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.sync.bean;

import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zeroz
 */

@XmlRootElement
public class TargetServiceFileSystemRes {
    private Map<String, List<String>> maps;

    public TargetServiceFileSystemRes() {
    }
    
    public TargetServiceFileSystemRes(Map<String, List<String>> maps) {
        this.maps = maps;
    }

    public Map<String, List<String>> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, List<String>> maps) {
        this.maps = maps;
    }
    
    
    
}
