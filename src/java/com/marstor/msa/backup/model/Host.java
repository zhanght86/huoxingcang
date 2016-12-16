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
public class Host implements Serializable {
    private String name = "";
    private String agent = "";
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setAgent(String agent){
        this.agent = agent;
    }
    
    public String getAgent(){
        return agent;
    }
}
