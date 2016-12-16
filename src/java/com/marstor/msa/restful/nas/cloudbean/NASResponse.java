/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.nas.cloudbean;

import com.marstor.msa.restful.cdp.cloudbean.BeanResponse;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zeroz
 */
@XmlRootElement
public class NASResponse extends BeanResponse {

    private String name;
    private String zfsname;
    private int isprotect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZfsname() {
        return zfsname;
    }

    public void setZfsname(String zfsname) {
        this.zfsname = zfsname;
    }

    public int getIsprotect() {
        return isprotect;
    }

    public void setIsprotect(int isprotect) {
        this.isprotect = isprotect;
    }
    
    
}
