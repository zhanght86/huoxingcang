/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "channel")
@RequestScoped
public class Channel implements Serializable {
    private String name;
    private String path;
    private String nsName;
    
    
    public Channel(){
        
    }
    
    
    public Channel(String name, String path, String nsName){
        this.name = name;
        this.path = path;
        this.nsName = nsName;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setPath(String path){
        this.path = path;
    }
    
    public String getPath(){
        return path;
    }
    
    public void setNSName(String nsName){
        this.nsName = nsName;
    }
    
    public String getNSName(){
        return this.nsName;
    }
    
}
