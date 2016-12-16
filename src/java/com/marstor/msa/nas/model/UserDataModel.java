/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.model.ListDataModel; 
import org.primefaces.model.SelectableDataModel; 
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
/**
 *
 * @author Administrator
 */

public class UserDataModel extends ListDataModel<CIFSUser>  implements SelectableDataModel<CIFSUser> {
    
   // private HashMap<String,User>  usersMap = new HashMap<String, User>();
   private  ArrayList<String>  userNames = new ArrayList<String>();//所有用户名称
    public UserDataModel() {
    }

    public UserDataModel(List<CIFSUser> list) {
        super(list);
        //userNames.add("aaa");
    }

    @Override
    public CIFSUser getRowData(String  rowKey) {
        List<CIFSUser>  users = (List<CIFSUser>)  getWrappedData();
        for(CIFSUser u : users) {
            if(u.getName().equals(rowKey)) {
                return  u;
            }
        }
        
        return null;
    }

    @Override
    public Object getRowKey(CIFSUser u) {
        return  u.getName();
    }
    
    public  void  removeUser(CIFSUser[] selectedUsers) {
        List<CIFSUser>  users = (List<CIFSUser>)  getWrappedData();
        for(int i=0;i<selectedUsers.length;i++) {
            users.remove(selectedUsers[i]);
           // usersMap.remove(selectedUsers[i].getName());
        }
        
    }
    public  void  removeOneUser(CIFSUser user) {
        List<CIFSUser>  users = (List<CIFSUser>)  getWrappedData();
//        for(int i=0;i<selectedUsers.length;i++) {
            users.remove(user);
           // usersMap.remove(selectedUsers[i].getName());
//        }
        
    }
    public CIFSUser getFirstUserFromList() {
        List<CIFSUser>  users = (List<CIFSUser>)  getWrappedData();
        CIFSUser user = null;
        if(users.size()>0) {
            user = users.get(0);
        }
        return  user;
    }
    public void  updateUserGroup(String  userName,ArrayList<String> source,ArrayList<String> target,String  groupNames) {
        CIFSUser u = this.getRowData(userName);
        u.setInGroups(source);
        u.setSourceGroups(target);
        u.setAttributeGroups(groupNames);
    }
    public  void  addUser(CIFSUser u) {
        List<CIFSUser>  users = (List<CIFSUser>)  getWrappedData();
        users.add(u);
        this.userNames.add(u.getName());
    }
    public void updatePasswd(String userName,String  passwd) {
        CIFSUser u = this.getRowData(userName);
        u.setPasswd(passwd);
        
    }

    public ArrayList<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(ArrayList<String> userNames) {
        this.userNames = userNames;
    }
    public void  addUserName(String name) {
        this.userNames.add(name);
    }
    public void addGroupToUser(String  groupName,String userName) {
        CIFSUser  user = this.getRowData(userName);
        ArrayList<String>  inGroupNames = user.getInGroups();
        if(!inGroupNames.contains(groupName)) {
            user.addInGroups(groupName);
            user.updateAttributeGroupStr(inGroupNames);
            
        }
    }
    public void updateUserForDeleteGroup(String  groupName) {
        List<CIFSUser>  users = (List<CIFSUser>)  getWrappedData();
        for(CIFSUser user : users) {
            if(user.getInGroups().contains(groupName)) {
               user.getInGroups().remove(groupName);
               user.updateAttributeGroupStr(user.getInGroups());
            }else {
               user.getSourceGroups().remove(groupName);                 
            }
            
        }
    }
    public void updateUserSourceGroupForAddGroup(String  groupName) {
        List<CIFSUser>  users = (List<CIFSUser>)  getWrappedData();
        for(CIFSUser user : users) {
               user.getSourceGroups().add(groupName);
             
        }
    }
    public ArrayList<String>  getGroupNamesByUserName(String  userName) {
        CIFSUser user = this.getRowData(userName);
        ArrayList<String> groupNames = user.getInGroups();
        return groupNames;
    }
    
}
