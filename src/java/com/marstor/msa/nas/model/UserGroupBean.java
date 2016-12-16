/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.nas.bean.User;
import com.marstor.msa.nas.bean.UserGroup;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class UserGroupBean implements Serializable{

    private List<SystemUser> allUserList = new ArrayList<SystemUser>();
    private List<SystemGroup> allGroupList = new ArrayList<SystemGroup>();
    private SystemUser selectedUser;
    private SystemGroup selectedGroup;
    private String tabViewActive="";
    private String accordionActive="";

    public UserGroupBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        tabViewActive = request.getParameter("tabViewActive");
        if(tabViewActive==null || tabViewActive.equals("")) {
            tabViewActive = "0";
        }
        accordionActive = request.getParameter("accordionActive");
        if(accordionActive==null || accordionActive.equals("")) {
            accordionActive = "0";
        }
        ArrayList<User> users = InterfaceFactory.getNASInterfaceInstance().getAllSystemUsersInfo();
        if (users == null) {
            return;
        }
        ArrayList<UserGroup> groups = InterfaceFactory.getNASInterfaceInstance().getAllSystemUserGroups();
        if (groups == null) {
            return;
        }
        for (UserGroup group : groups) {
            SystemGroup sysGroup = new SystemGroup(group.getId(), group.getName(), group.getInfo(), group.getUsers());
            this.allGroupList.add(sysGroup);
        }
        for (User user : users) {
            ArrayList<String> strs = new ArrayList<String>();
            for (SystemGroup group : this.allGroupList) {
                if (group.getUserNames().contains(user.getName())) {
                    strs.add(group.getName());
                }
            }
            SystemUser sys = new SystemUser(user.getId(), user.getName(), user.getPassword(), user.getSmbPasswd(), user.getInfo(), strs);
            this.allUserList.add(sys);
        }

    }

    public String getAccordionActive() {
        return accordionActive;
    }

    public void setAccordionActive(String accordionActive) {
        this.accordionActive = accordionActive;
    }

    public String getTabViewActive() {
        return tabViewActive;
    }

    public void setTabViewActive(String tabViewActive) {
        this.tabViewActive = tabViewActive;
    }

    public SystemGroup getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(SystemGroup selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public List<SystemUser> getAllUserList() {
        Iterator<SystemUser> it = allUserList.iterator();
        List<SystemUser> rmlist=new ArrayList<SystemUser>();
        while(it.hasNext()){
            SystemUser su = it.next();
            if(su.getName().equalsIgnoreCase("ecd")){
                rmlist.add(su);
            }
        }
        allUserList.removeAll(rmlist);
        return allUserList;
    }

    public void setAllUserList(List<SystemUser> allUserList) {
        this.allUserList = allUserList;
    }

    public List<SystemGroup> getAllGroupList() {
        return allGroupList;
    }

    public void setAllGroupList(List<SystemGroup> allGroupList) {
        this.allGroupList = allGroupList;
    }

    public SystemUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(SystemUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String doBeforeModifyPasswd(SystemUser user) {
        // InterfaceFactory.getCommonInterfaceInstance().modifyUserPassword(null, null);
        String param = "userName=" + user.getName();
        return "nas_modify_passwd?faces-redirect=true&amp;" + param;

    }

    public String doBeforeModifyUserGroup(SystemUser user) {
        List<String> groups = user.getGroupNames();
        String inGroups = "";
        for (int i = 0; i < groups.size(); i++) {
            if (i + 1 < groups.size()) {
                inGroups = inGroups + groups.get(i) + ",";
            } else {
                inGroups = inGroups + groups.get(i);
            }
        }
        String allGroups = "";
        for (int i = 0; i < this.allGroupList.size(); i++) {
            SystemGroup group = allGroupList.get(i);
            if (groups.contains(group.getName())) {
                continue;
            }
            if (i + 1 < this.allGroupList.size()) {
                allGroups = allGroups + group.getName() + ",";
            } else {
                allGroups = allGroups + group.getName();
            }
        }

        String param = "userName=" + user.getName() + "&" + "inGroups=" + inGroups + "&" + "allGroups=" + allGroups;
        return "nas_modify_user_group?faces-redirect=true&amp;" + param;
    }

    public String doBeforeModifyGroupUser(SystemGroup group) {
        List<String> users = group.getUserNames();
        String includeUsers = "";
        for (int i = 0; i < users.size(); i++) {
            if (i + 1 < users.size()) {
                includeUsers = includeUsers + users.get(i) + ",";
            } else {
                includeUsers = includeUsers + users.get(i);
            }
        }
        String allUsers = "";
        for (int i = 0; i < this.allUserList.size(); i++) {
            SystemUser user = allUserList.get(i);
            if (users.contains(user.getName())) {
                continue;
            }
            if (i + 1 < this.allUserList.size()) {
                allUsers = allUsers + user.getName() + ",";
            } else {
                allUsers = allUsers + user.getName();
            }
        }

        String param = "groupName=" + group.getName() + "&" + "includeUsers=" + includeUsers + "&" + "allUsers=" + allUsers;
        return "nas_modify_group_user?faces-redirect=true&amp;" + param;
    }

    public void deleteUser() {
        boolean flag = InterfaceFactory.getNASInterfaceInstance().delUser(String.valueOf(this.selectedUser.getId()), this.selectedUser.getName());
        if (!flag) {
            Debug.print("del user" + flag);
            return;
        }
        ArrayList<User> users = InterfaceFactory.getNASInterfaceInstance().getAllSystemUsersInfo();
        if(users==null) {
            return;
        }
        ArrayList<UserGroup> groups = InterfaceFactory.getNASInterfaceInstance().getAllSystemUserGroups();
        if(groups ==null) {
            return;
        }
        this.allGroupList = new ArrayList<SystemGroup>();
        for (UserGroup group : groups) {
            SystemGroup sysGroup = new SystemGroup(group.getId(), group.getName(), group.getInfo(), group.getUsers());
            this.allGroupList.add(sysGroup);
        }
        this.allUserList = new ArrayList<SystemUser>();
        for (User user : users) {
            ArrayList<String> strs = new ArrayList<String>();
            for (SystemGroup group : this.allGroupList) {
                if (group.getUserNames().contains(user.getName())) {
                    strs.add(group.getName());
                }
            }
            SystemUser sys = new SystemUser(user.getId(), user.getName(), user.getPassword(), user.getSmbPasswd(), user.getInfo(), strs);
            this.allUserList.add(sys);
        }
    }

    public void deleteGroup() {
        boolean flag = InterfaceFactory.getNASInterfaceInstance().delGroup(String.valueOf(this.selectedGroup.getId()), this.selectedGroup.getName());
        if (!flag) {
            Debug.print("del group" + flag);
            return;
        }
        ArrayList<User> users = InterfaceFactory.getNASInterfaceInstance().getAllSystemUsersInfo();
        if(users==null) {
            return;
        }
        ArrayList<UserGroup> groups = InterfaceFactory.getNASInterfaceInstance().getAllSystemUserGroups();
        if(groups ==null) {
            return;
        }
        this.allGroupList = new ArrayList<SystemGroup>();
        for (UserGroup group : groups) {
            SystemGroup sysGroup = new SystemGroup(group.getId(), group.getName(), group.getInfo(), group.getUsers());
            this.allGroupList.add(sysGroup);
        }
        this.allUserList = new ArrayList<SystemUser>();
        for (User user : users) {
            ArrayList<String> strs = new ArrayList<String>();
            for (SystemGroup group : this.allGroupList) {
                if (group.getUserNames().contains(user.getName())) {
                    strs.add(group.getName());
                }
            }
            SystemUser sys = new SystemUser(user.getId(), user.getName(), user.getPassword(), user.getSmbPasswd(), user.getInfo(), strs);
            this.allUserList.add(sys);
        }
    }
}
