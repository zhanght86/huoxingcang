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
public class BackupAgent implements Serializable{
    private int serial = 0;
    private String agentName = "";
    private String agentState = "";
    private String operation = "";
    private String image = "";
    private boolean visible = true;
    private boolean status = false;
    
    public void setStatus(boolean s){
        this.status = s;
    }
    
    public boolean getStatus(){
        return status;
    }
    
    public void setVisible(boolean v){
        this.visible = v;
    }
    
    public boolean getVisible(){
        return visible;
    }
    
    public void setImage(String i){
        this.image = i;
    }
    
    public String getImage(){
        return image;
    }
    
    public void setOperation(String s){
        this.operation = s;
    }
    
    public String getOperation(){
        return operation;
    }
    
    public void setSerial(int s){
        this.serial = s;
    }
    
    public int getSerial(){
        return serial;
    }
    
    public void setAgentName(String name){
        this.agentName = name;
    }
    
    public String getAgentName(){
        return agentName;
    }
    
    
    public void setAgentState(String state){
        this.agentState = state;
    }
    
    public String getAgentState(){
        return agentState;
    }
    
    
}
