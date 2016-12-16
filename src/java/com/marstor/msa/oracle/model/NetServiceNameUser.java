/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "nsnUser")
@ViewScoped
public class NetServiceNameUser implements  Serializable{
    private String user = null;
    private String pass = null;
    
    public NetServiceNameUser(){}
    
    public void setUser(String user){
        this.user = user;
    }
    
    public String getUser(){
        return user;
    }
    
    public void setPass(String pass){
        this.pass = pass;
    }
    
    public String getPass(){
        return pass;
    }
    
    
}
