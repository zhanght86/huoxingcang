/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class DeviceDetail implements Serializable{
    private String name = "";
    private String value = "";
    
     public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setValue(String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
    
}
