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
//@ManagedBean(name = "bootSequence")
//@RequestScoped
public class BootSequen implements Serializable  {
   private String type;
   private String strIcon;
   

    /**
     * Creates a new instance of BootSequence
     */
    public BootSequen() {
    }
    
    public BootSequen(String type, String icon) {
        this.type = type;
        this.strIcon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStrIcon() {
        return strIcon;
    }

    public void setStrIcon(String strIcon) {
        this.strIcon = strIcon;
    }

  
    
}
