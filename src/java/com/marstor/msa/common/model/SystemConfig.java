/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class SystemConfig implements Serializable{
    private String name;
    private String user;
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
 
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }   
}
