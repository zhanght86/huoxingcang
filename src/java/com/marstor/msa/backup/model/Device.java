/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.model;

/**
 *
 * @author Administrator
 */
public class Device {
    private String name = "";
    private String type = "";
    
     public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public String getType(){
        return type;
    }
    
}
