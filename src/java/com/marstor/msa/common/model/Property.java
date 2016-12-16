/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "property")
@RequestScoped
public class Property {  //‘› ±≤ª”√
    
    private int number;
    private String name;

    /**
     * Creates a new instance of Property
     */
    public Property() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
