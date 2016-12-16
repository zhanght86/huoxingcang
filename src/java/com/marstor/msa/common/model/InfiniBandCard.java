/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
//@ManagedBean(name = "infiniBandCard")
//@RequestScoped
public class InfiniBandCard implements Serializable{
    private String storageName;
    private String state;
    private String pkeys;

    /**
     * Creates a new instance of InfiniBandCard
     */
    public InfiniBandCard() {
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPkeys() {
        return pkeys;
    }

    public void setPkeys(String pkeys) {
        this.pkeys = pkeys;
    }
    
}
