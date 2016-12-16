/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean( name = "oracleUser")
@RequestScoped
public class OracleUser implements Serializable{
    private String user = "";
    private String password = "";
    
    public OracleUser(){
        
    }
    
    public void setUser(String user){
        this.user = user;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getUser(){
        return user;
    }
    
    public String getPassword(){
        return password;
    }
        
}
