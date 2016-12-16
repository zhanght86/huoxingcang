/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.SystemGroup;
import com.marstor.msa.nas.model.SystemUser;
import com.marstor.msa.nas.bean.User;
import com.marstor.msa.nas.bean.UserGroup;
import com.marstor.msa.nas.web.NASInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class ModifyGroupUser implements Serializable{

    private ArrayList<String> includeUsers = new ArrayList<String>();
    private ArrayList<String> allUsers = new ArrayList<String>();
    private String groupName;
//    private DualListModel<String> modelGroups = new DualListModel<String>(sourceGroups, inGroups);
    private DualListModel<String> model = new DualListModel<String>(allUsers, includeUsers);

    public ModifyGroupUser() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.groupName = request.getParameter("groupName");
        String source = request.getParameter("allUsers");
        String[] array;
        if (source != null) {
            array = source.trim().split(",");
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    this.allUsers.add(array[i]);
                }
            }
        }
        String target = request.getParameter("includeUsers");
        if (target != null) {
            array = target.trim().split(",");
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    this.includeUsers.add(array[i]);
                }
            }

        }
        model = new DualListModel<String>(allUsers, includeUsers);
    }

    public ArrayList<String> getIncludeUsers() {
        return includeUsers;
    }

    public void setIncludeUsers(ArrayList<String> includeUsers) {
        this.includeUsers = includeUsers;
    }

    public ArrayList<String> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(ArrayList<String> allUsers) {
        this.allUsers = allUsers;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public DualListModel<String> getModel() {
        return model;
    }

    public void setModel(DualListModel<String> model) {
        this.model = model;
    }

    public ArrayList<String> getGroupsByUser(String userName) {
        ArrayList<User> users = InterfaceFactory.getNASInterfaceInstance().getAllSystemUsersInfo();
        ArrayList<UserGroup> groups = InterfaceFactory.getNASInterfaceInstance().getAllSystemUserGroups();
        if (users == null || groups == null) {
            return null;
        }
        List<SystemGroup> allGroupList = new ArrayList<SystemGroup>();
        for (UserGroup group : groups) {
            SystemGroup sysGroup = new SystemGroup(group.getId(), group.getName(), group.getInfo(), group.getUsers());
            allGroupList.add(sysGroup);
        }
        ArrayList<String> groupNames = new ArrayList<String>();
        for (User user : users) {
            if (user.getName().equals(userName)) {
                for (SystemGroup group : allGroupList) {
                    if (group.getUserNames().contains(user.getName())) {
                        groupNames.add(group.getName());
                    }
                }
                break;
            }

        }
        return groupNames;
    }

    public String save() {
        //获取用户和组
        List<SystemUser> allUserList = new ArrayList<SystemUser>();
        List<SystemGroup> allGroupList = new ArrayList<SystemGroup>();
        ArrayList<User> users = InterfaceFactory.getNASInterfaceInstance().getAllSystemUsersInfo();
        if (users == null) {
            return null;
        }
        ArrayList<UserGroup> groups = InterfaceFactory.getNASInterfaceInstance().getAllSystemUserGroups();
        if (groups == null) {
            return null;
        }
        for (UserGroup group : groups) {
            SystemGroup sysGroup = new SystemGroup(group.getId(), group.getName(), group.getInfo(), group.getUsers());
            allGroupList.add(sysGroup);
        }
        for (User user : users) {
            ArrayList<String> strs = new ArrayList<String>();
            for (SystemGroup group : allGroupList) {
                if (group.getUserNames().contains(user.getName())) {
                    strs.add(group.getName());
                }
            }
            SystemUser sys = new SystemUser(user.getId(), user.getName(), user.getPassword(), user.getSmbPasswd(), user.getInfo(), strs);
            allUserList.add(sys);
        }
        //修改组内用户
        this.allUsers = (ArrayList<String>) this.model.getSource();
        this.includeUsers = (ArrayList<String>) this.model.getTarget();
        NASInterface nas = InterfaceFactory.getNASInterfaceInstance();
        ArrayList<String> groupNames = new ArrayList<String>();
        for (int i = 0; i < allUserList.size(); i++) {
            SystemUser sys = allUserList.get(i);
            //如果此用户修改前不属于当前组，现在要设置为属于当前组
            if (!sys.getGroupNames().contains(this.groupName) && this.includeUsers.contains(sys.getName())) {
                groupNames = sys.getGroupNames();
                groupNames.add(this.groupName);
                boolean flag = nas.modifyUser(sys.getName(), groupNames);
                if (!flag) {
                    MSAResource res = new MSAResource();
                    Debug.print("modify  group user" + flag);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setFailed"), res.get("setFailed")));
                    return null;
                }

            }
            //如果此用户修改前属于当前组，现在要设置为不属于当前组
            if (sys.getGroupNames().contains(this.groupName) && !this.includeUsers.contains(sys.getName())) {
                groupNames = sys.getGroupNames();
                groupNames.remove(this.groupName);
                boolean flag = nas.modifyUser(sys.getName(), groupNames);
                if (!flag) {
                    MSAResource res = new MSAResource();
                    Debug.print("modify group user " + flag);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setFailed"), res.get("setFailed")));
                    return null;
                }
            }
        }

//       boolean flag =  InterfaceFactory.getNASInterfaceInstance().modifyUser(userName, inGroups);
//       if(!flag) {
//           MSAResource res  = new MSAResource();
//           Debug.print("################$$$$$$$$ modify user group" + flag);
//           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setFailed"), res.get("setFailed")));
//           return null;
//       }
        return "nas_domain_config?faces-redirect=true&amp;tabViewActive=1&amp;accordionActive=1" ;
    }
}
