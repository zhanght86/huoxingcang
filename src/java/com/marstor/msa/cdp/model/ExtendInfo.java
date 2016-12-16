/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.model;

/**
 *
 * @author Administrator
 */
public class ExtendInfo {
    private String dg;
    private String dname;
    private String mirror;
    public boolean isLoaded = false;

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getMirror() {
        return mirror;
    }

    public void setMirror(String mirror) {
        this.mirror = mirror;
    }

    public boolean isIsLoaded() {
        return isLoaded;
    }

    public void setIsLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }


    
}
