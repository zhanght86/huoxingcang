/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.common.bean;

import com.marstor.msa.common.bean.SystemUserInformation;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Li Mengyang <li.mengyang@marstor.com>
 */
@XmlRootElement
public class SystemUser implements Serializable{    
    private int user_id;
    private String user_name;
    private int user_type;
    
    public SystemUser(SystemUserInformation user){
        user_id = user.id;
        user_name = user.name;
        user_type = user.type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
    
    
}
