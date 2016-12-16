/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.sync.model;

import com.marstor.msa.sync.bean.Group;
import com.marstor.msa.sync.bean.User;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class NasBean implements Serializable{
    
    private User[] users;
    private Group[] groups;

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Group[] getGroups() {
        return groups;
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
    }
    
   
    
}

