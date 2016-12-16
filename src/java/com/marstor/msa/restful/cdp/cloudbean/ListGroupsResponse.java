/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.cloudbean;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zeroz
 */
@XmlRootElement
public class ListGroupsResponse extends BeanResponse{
    
    private String volume_name;
    private List<NameInfo> groups;

    public String getVolume_name() {
        return volume_name;
    }

    public void setVolume_name(String volume_name) {
        this.volume_name = volume_name;
    }

    public List<NameInfo> getGroups() {
        return groups;
    }

    public void setGroups(List<NameInfo> groups) {
        this.groups = groups;
    }
    
    
    
}
