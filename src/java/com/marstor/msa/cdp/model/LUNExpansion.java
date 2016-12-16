/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.model;

/**
 *
 * @author Administrator
 */
public class LUNExpansion {
    private String guid;
    private String name;
    private int lun;
    private String serialNum;
    public boolean isLoaded = false;
    private String type;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLun() {
        return lun;
    }

    public void setLun(int lun) {
        this.lun = lun;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    
}
