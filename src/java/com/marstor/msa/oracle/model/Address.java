/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


@ManagedBean(name = "address")
@RequestScoped
public class Address implements Serializable{
    public String ip = "";
    public String port = "1521";
    
    public Address(){
        
    }

    public Address(String ip, String port){
        this.ip = ip;
        this.port = port;
    }
        
    public void setIP(String ip){
        this.ip = ip;
    }
    
    public void setPort(String port){
        this.port = port;
    }
    
    public String getIP(){
        return ip;
    }
    
    public String getPort(){
        return port;
    }
}
