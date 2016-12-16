/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "diskCount")
@RequestScoped
public class DiskCount  implements Serializable{
    public String property;
    public int count;

    /**
     * Creates a new instance of CountBean
     */
    public DiskCount() {
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

 
}
