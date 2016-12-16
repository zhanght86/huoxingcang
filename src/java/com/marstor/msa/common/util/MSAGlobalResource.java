package com.marstor.msa.common.util;

import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
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
@ManagedBean(name = "global")
@RequestScoped
public final class MSAGlobalResource implements Serializable{

    public MSAGlobalResource() {

    }

    public String get(String key) {
        MSAResource res = new MSAResource();
        return res.get("global", key);
    } 
}
