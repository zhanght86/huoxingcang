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
public class MDUAddSharedDirParameter implements Serializable{
    
    private String owner;
    private String[] dirs;
    private String[] users;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String[] getDirs() {
        return dirs;
    }

    public void setDirs(String[] dirs) {
        this.dirs = dirs;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    
    
    
}
