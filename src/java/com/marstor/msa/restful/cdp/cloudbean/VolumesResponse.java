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
public class VolumesResponse extends BeanResponse {
    
    private List<VolumeName> volumes;

    public List<VolumeName> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<VolumeName> volumes) {
        this.volumes = volumes;
    }
    
    
    
}
