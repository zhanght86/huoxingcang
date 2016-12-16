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
//@ManagedBean(name = "infiniBandArea")
//@RequestScoped
public class InfiniBandArea implements Serializable{
    private String areaName;
    private String state;
    private String belongCard;
    private String pkey;
    

    /**
     * Creates a new instance of InfiniBandArea
     */
    public InfiniBandArea() {
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBelongCard() {
        return belongCard;
    }

    public void setBelongCard(String belongCard) {
        this.belongCard = belongCard;
    }

    public String getPkey() {
        return pkey;
    }

    public void setPkey(String pkey) {
        this.pkey = pkey;
    }
    
}
