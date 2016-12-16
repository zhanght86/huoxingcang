/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class GroupDataModel implements Serializable{
    
    private ArrayList<Group>  groupData = new ArrayList<Group>();
    private ArrayList<String>  groupNames = new ArrayList<String>();//所有组的名称

    public GroupDataModel() {
    }

    public GroupDataModel(ArrayList<Group>  groupData) {
        this.groupData = groupData;
    }
    public void  addGroup(String  groupName) {
        Group  g = new Group(groupName);
       
        this.groupData.add(g);
        groupNames.add(groupName);
                
    }
   public void  addGroup(Group group) {
       
        this.groupData.add(group);
        groupNames.add(group.getName());
                
    }
    public Group  getGroupByName(String groupName) {
        for(int i=0;i<this.groupData.size();i++) {
            if(groupData.get(i).getName().equals(groupName)) {
                return  groupData.get(i);
            }
        }
        return null;
    }

    public ArrayList<Group> getGroupData() {
        return groupData;
    }

    public void setGroupData(ArrayList<Group> groupData) {
        this.groupData = groupData;
    }

    public ArrayList<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(ArrayList<String> groupNames) {
        this.groupNames = groupNames;
    }
    public void addUserToGroup(CIFSUser  user,String groupName) {
        Group  g = this.getGroupByName(groupName);
        ArrayList<String>  users = g.getUserNames();
        if(!users.contains(user.getName())) {
            g.addUser(user);
            g.getContainUserNames().add(user.getName());
        }
        
    }
    public void initSourceUsersForAllGroup(ArrayList<String> sourceUserNames) {
        for(Group  group : this.groupData) {
            group.getSourceUserNames().addAll(sourceUserNames);
        }
    }
    public void  addUserToSourceGroup(String  userName) {
        for(Group  group : this.groupData) {
            group.getSourceUserNames().add(userName);
            group.getSourceUserNames().removeAll(group.getContainUserNames());
        }
        
    }
    public void  updateUsersInGroup(String  groupName,ArrayList<String> source,ArrayList<String> target,String  userNames) {
//        User u = this.getRowData(userName);
//        u.setInGroups(target);
//        u.setSourceGroups(source);
//        u.setAttributeGroups(groupNames);
        Group  group = this.getGroupByName(groupName);
        group.setContainUserNames(target);
        group.setSourceUserNames(source);
        group.setUserNamesStr(userNames);
        
    }
    public String  getUserStrByList(List<String> userNames) {
       // List<String> groupNames = this.selectedUser.getModelGroups().getTarget();
        
        StringBuffer names = new StringBuffer();
        for (int i = 0; i < userNames.size(); i++) {
            names.append(userNames.get(i));
            if ((i + 1) < userNames.size()) {
                names.append(",");
            }
        }
        return  names.toString();
    }
    public void removeGroup(Group  group) {
        this.groupData.remove(group);
    }
    public void updateSourceUsersForDeleteUser(String userName) {
        for(Group group: this.groupData) {
            if(group.getContainUserNames().contains(userName)) {
                group.getContainUserNames().remove(userName);
                group.updateUserNamesStr(group.getContainUserNames());
            }else {
                group.getSourceUserNames().remove(userName);
            }
        }
        
    }
    
}
