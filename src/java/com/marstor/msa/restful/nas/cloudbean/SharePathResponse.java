/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.nas.cloudbean;

import com.marstor.msa.restful.cdp.cloudbean.BeanResponse;
import com.marstor.msa.restful.cdp.cloudbean.NameInfo;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zeroz
 */

@XmlRootElement
public class SharePathResponse extends BeanResponse {
    
    private String volumename;
    private List<NameInfo> sharedirs;

    public String getVolumename() {
        return volumename;
    }

    public void setVolumename(String volumename) {
        this.volumename = volumename;
    }

    public List<NameInfo> getSharedirs() {
        return sharedirs;
    }

    public void setSharedirs(List<NameInfo> sharedirs) {
        this.sharedirs = sharedirs;
    }
    
    
    
}
