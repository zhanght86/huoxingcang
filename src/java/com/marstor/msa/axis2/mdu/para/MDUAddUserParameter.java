/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.mdu.para;

import java.io.Serializable;


/**
 *
 * @author Administrator
 */
public class MDUAddUserParameter implements Serializable{
    
    private String user;
    private String[] groups;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    
    
}
