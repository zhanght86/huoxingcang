/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.iscsi.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class TargetChap implements Serializable{
    private boolean stat_up;
    private String user_name;
    private String password;
    private String target_name;

    public TargetChap() {
    }

    public boolean isStat_up() {
        return stat_up;
    }

    public void setStat_up(boolean stat_up) {
        this.stat_up = stat_up;
    }

   

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTarget_name() {
        return target_name;
    }

    public void setTarget_name(String target_name) {
        this.target_name = target_name;
    }


    
    
}
