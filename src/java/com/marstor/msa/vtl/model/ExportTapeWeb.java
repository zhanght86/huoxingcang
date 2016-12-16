/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "exportTapeWeb")
@RequestScoped
public class ExportTapeWeb  implements Serializable{
    public int tapeID;
    public boolean selected;
    public String name;
    public String state;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTapeID() {
        return tapeID;
    }

    public void setTapeID(int tapeID) {
        this.tapeID = tapeID;
    }
    
}
