package com.marstor.msa.common.util;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
@ManagedBean(name = "oem")
@RequestScoped
public final class MSAOEMResource implements Serializable {

    public MSAOEMResource() {      
    }

    public String get(String key) {
        MSAResource res = new MSAResource();
        return res.get("oem", key);
    } 
}
