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
public class BackupHost implements Serializable{
    private String host = "";
    private String agent = "";
    
    public BackupHost(){}
    
    public void setHost(String host){
        this.host = host;
    }
    
    public String getHost(){
        return host;
    }
    
    public void setAgent(String agent){
        this.agent = agent;
    }
    
    public String getAgent(){
        return agent;
    }
}
