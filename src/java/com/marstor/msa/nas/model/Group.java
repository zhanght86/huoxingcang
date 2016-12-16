/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import java.util.ArrayList;
import org.primefaces.model.DualListModel;
/**
 *
 * @author Administrator
 */
public class Group implements Serializable{
    private String  name;
    private String  id;
    private ArrayList<CIFSUser>  users = new ArrayList<CIFSUser>();
    private ArrayList<String>  userNames = new ArrayList<String>();
    private ArrayList<String> containUserNames = new ArrayList<String>();
    private ArrayList<String> sourceUserNames = new ArrayList<String>();
    private DualListModel<String> modelUsers = new DualListModel<String>(sourceUserNames, containUserNames);
    private String userNamesStr = new String();
    public Group(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public Group(String name) {
        this.name = name;
    }
    

    public Group() {
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CIFSUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<CIFSUser> users) {
        this.users = users;
    }
    public void  addUser(String  name,String  passwd) {
        CIFSUser u = new CIFSUser(name, passwd);
        this.users.add(u);
        if(userNames.size()>0) {
           // userNamesStr.(","+name);
            userNamesStr = userNamesStr + "," + name ;
        }else {
            userNamesStr = name;
        }
        this.userNames.add(u.getName());
        
    }
    public void addUser(CIFSUser u) {
        this.users.add(u);
         if(userNames.size()>0) {
           // userNamesStr.(","+name);
            userNamesStr = userNamesStr + "," + u.getName() ;
        }else {
            userNamesStr = u.getName();
        }
        this.userNames.add(u.getName());
       
    }
    

    public ArrayList<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(ArrayList<String> userNames) {
        this.userNames = userNames;
    }

    public String getUserNamesStr() {
        return userNamesStr;
    }

    public void setUserNamesStr(String userNamesStr) {
        this.userNamesStr = userNamesStr;
    }

    public ArrayList<String> getSourceUserNames() {
        return sourceUserNames;
    }

    public void setSourceUserNames(ArrayList<String> sourceUserNames) {
        this.sourceUserNames = sourceUserNames;
    }

    

    public ArrayList<String> getContainUserNames() {
        return containUserNames;
    }

    public void setContainUserNames(ArrayList<String> containUserNames) {
        this.containUserNames = containUserNames;
    }

    public DualListModel<String> getModelUsers() {
        return modelUsers;
    }

    public void setModelUsers(DualListModel<String> modelUsers) {
        this.modelUsers = modelUsers;
    }
    public void  updateUserNamesStr(ArrayList<String> userNames) {
        StringBuffer names = new StringBuffer();
        for (int i = 0; i < userNames.size(); i++) {
            names.append(userNames.get(i));
            if ((i + 1) < userNames.size()) {
                names.append(",");
            }
        }
        this.userNamesStr = names.toString();

    }
   
    
}
