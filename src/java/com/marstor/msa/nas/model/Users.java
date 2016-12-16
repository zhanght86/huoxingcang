/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */

public class Users  implements Serializable{
   
    private ArrayList<CIFSUser>  users = new ArrayList<CIFSUser>();
    private ArrayList<String> userNames = new ArrayList<String>();
    private CIFSUser  selectedUser = new CIFSUser() ;
    public Users() {
        
        CIFSUser u = new CIFSUser("abc", "123");
        users.add(u);
        userNames.add(u.getName());
        u = new CIFSUser("def", "123");
        users.add(u);
        userNames.add(u.getName());
       // selectedUser = new User();
    }
    public void  addUser(String  name,String passwd) {
        CIFSUser u = new CIFSUser(name, passwd);
        users.add(u);
    }

    public CIFSUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(CIFSUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public ArrayList<CIFSUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<CIFSUser> users) {
        this.users = users;
    }

    public ArrayList<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(ArrayList<String> userNames) {
        this.userNames = userNames;
    }
    
}
