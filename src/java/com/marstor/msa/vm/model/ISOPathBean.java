/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "iSOPathBean")
@RequestScoped
public class ISOPathBean implements Serializable{

    public String isoPath;
    /**
     * Creates a new instance of ISOPathBean
     */
    public ISOPathBean() {
    }

    public String getIsoPath() {
        return isoPath;
    }

    public void setIsoPath(String isoPath) {
        this.isoPath = isoPath;
    }
    
    
    
}
