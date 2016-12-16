/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.managedbean.EditUserGroup;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Administrator
 */
@ManagedBean
@SessionScoped
public class UserGroupData implements Serializable{

    //private ArrayList<Group> groups = new ArrayList<Group>();
    //private HashMap<String, Group> groups = new HashMap<String, Group>();
    private ArrayList<String> groupNames = new ArrayList<String>();
    private ArrayList<String> selectedNames = new ArrayList<String>();
    private ArrayList<String> userNames = new ArrayList<String>(); //所有用户名称
    private String selectedName;
    private String selectedType;
    private List<CIFSUser> allUserList = new ArrayList<CIFSUser>();
    private HashMap<String, CIFSUser> allUsers = new HashMap<String, CIFSUser>();
    private CIFSUser[] selectedUsers;
    private UserDataModel userModel;
    private GroupDataModel groupModel = new GroupDataModel();
    private CIFSUser selectedUser = new CIFSUser();
    private Group selectedGroup = new Group();
    private Group selectedRowGroup = new Group();
    private String ss;
    private CIFSUser userForView = new CIFSUser();
    private String selectedUserName;//用在查看用户所属组的selectonelist
    private String selectedGroupName;//用在查看用户所属组的selectonelist
//    private ArrayList<String> groupsSource = new ArrayList<String>();
//    private ArrayList<String> groupsTarget = new ArrayList<String>();
//    private DualListModel<String> modelGroups;
    //private User createUser = new User();
    
    public UserGroupData() {
        //groupModel = new GroupDataModel();
        userModel = new UserDataModel(allUserList);
        addGroup("g1");
        addGroup("g2");
        addGroup("g3");
        addGroup("g4");
        addGroup("g6");
        addUser("abc", "123", "g1");
        addUser("def", "123", "g2");
        addUser("aaa", "123", "g2");
        addUser("eee", "123", "g2");

        //List<User>  users = new ArrayList<User>();
        // updateUserList();
       this.selectedNames = this.userModel.getUserNames();

        //groupModel.initSourceUsersForAllGroup(userModel.getUserNames());

    }

    public String getSelectedGroupName() {
        return selectedGroupName;
    }

    public void setSelectedGroupName(String selectedGroupName) {
        this.selectedGroupName = selectedGroupName;
    }

    public String getSelectedUserName() {
        return selectedUserName;
    }

    public void setSelectedUserName(String selectedUserName) {
        this.selectedUserName = selectedUserName;
    }

    public CIFSUser getUserForView() {
        return userForView;
    }

    public void setUserForView(CIFSUser userForView) {
        this.userForView = userForView;
    }
    
    public Group getSelectedRowGroup() {
        return selectedRowGroup;
    }
    
    public void setSelectedRowGroup(Group selectedRowGroup) {
        this.selectedRowGroup = selectedRowGroup;
    }
    
    public Group getSelectedGroup() {
        return selectedGroup;
    }
    
    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }
    
    public GroupDataModel getGroupModel() {
        return groupModel;
    }
    
    public void setGroupModel(GroupDataModel groupModel) {
        this.groupModel = groupModel;
    }

    public void addGroup(String name) {

        // groups.add(g);
        //groupNames.add(g.getName());
        Group group = new Group(name);
        //group.setSourceUserNames(this.getUserNames());
        //group.getSourceUserNames().addAll(this.userModel.getUserNames());
        if (this.userModel.getUserNames().size() > 0) {
            group.getSourceUserNames().addAll(userModel.getUserNames());
            this.userModel.updateUserSourceGroupForAddGroup(name);
        }
        this.groupModel.addGroup(group);
        
        
    }
    //向指定组添加一个用户

    public void addUser(String name, String passwd, String groupName) {
        // Group g = this.getGroupByID(gid);
        // Group g = this.groupModel.getGroupByName(groupName);
        CIFSUser u = new CIFSUser(name, passwd);
        u.addInGroups(groupName);
        u.setAttributeGroups(groupName);
        u.getSourceGroups().addAll(this.groupModel.getGroupNames());//用户初始化时sourcegroup为空
        u.getSourceGroups().removeAll(u.getInGroups());
        // g.addUser(u);
        this.groupModel.addUserToGroup(u, groupName);
        this.groupModel.addUserToSourceGroup(name);
        this.userModel.addUserName(name);
        this.allUserList.add(u);
        
    }
    //添加一个用户，此用户不属于任何组

    public void addUser(String name, String passwd) {
        // u.setSourceGroups(this.groupNames);
        CIFSUser u = new CIFSUser(name, passwd);
        u.getSourceGroups().addAll(this.groupModel.getGroupNames());
        this.userModel.addUser(u);
        this.groupModel.addUserToSourceGroup(name);
        //userNames.add(name);
    }
    
    public String getSs() {
        return ss;
    }
    
    public void setSs(String ss) {
        this.ss = ss;
    }
    
    public void deleteUsers() {

        //this.allUserList.removeAll(this.selectedUsers.);
        userModel.removeUser(this.selectedUsers);
        for(CIFSUser user : this.selectedUsers) {
            groupModel.updateSourceUsersForDeleteUser(user.getName());
        }
    }
    public void deleteOneUser() {
        userModel.removeOneUser(this.selectedUser);
        groupModel.updateSourceUsersForDeleteUser(this.selectedUser.getName());
    }
    public void deleteGroup() {
        groupModel.removeGroup(selectedGroup);
        userModel.updateUserForDeleteGroup(selectedGroup.getName());
    }
//    public ArrayList<String> getAllUserNames() {
//        ArrayList<String>  names = new ArrayList<String>();
//        for(int i=0;i<groups.size();i++) {
//            for(int j=) {
//                
//            }
//            names.addAll(groups.get(i).getUsers());
//        }
//        
//    }

    public void updateUserList() {
        this.allUserList.removeAll(this.allUserList);
        this.allUserList.addAll(allUsers.values());
    }
    
    public CIFSUser getSelectedUser() {
//        User[] users = this.selectedUsers;
//        User u = null;
//        if (users.length > 0) {
//            u = users[0];
//        }
        return this.selectedUser;
    }
    
    public void setSelectedUser(CIFSUser selectedUser) {
        this.selectedUser = selectedUser;
    }
    
    public UserDataModel getUserModel() {
        return userModel;
    }
    
    public void setUserModel(UserDataModel userModel) {
        this.userModel = userModel;
    }
    
    public CIFSUser[] getSelectedUsers() {
        return selectedUsers;
    }
    
    public void setSelectedUsers(CIFSUser[] selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

//    public Group getGroupByID(String id) {
//        int i;
//        for (i = 0; i < groups.size(); i++) {
//            if (groups.get(i).getId().equals(id)) {
//                break;
//            }
//        }
//        if (i >= groups.size()) {
//            return null;
//        }
//        return groups.get(i);
//    }
    public HashMap<String, CIFSUser> getAllUsers() {
        return allUsers;
    }
    
    public void setAllUsers(HashMap<String, CIFSUser> allUsers) {
        this.allUsers = allUsers;
    }
    
    public String getSelectedType() {
        return selectedType;
    }
    
    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

//    public ArrayList<String> getGroupNames() {
////        for(int i=0;i<groups.size();i++) {
////            groupNames.add(groups.get(i).getName());
////        }
//        return groupNames;
//    }
//    
//    public void setGroupNames(ArrayList<String> groupNames) {
//        this.groupNames = groupNames;
//    }
//    
//    public HashMap<String, Group> getGroups() {
//        return groups;
//    }
//    
//    public void setGroups(HashMap<String, Group> groups) {
//        this.groups = groups;
//    }
    public ArrayList<String> getSelectedNames() {
        return selectedNames;
    }
    
    public void setSelectedNames(ArrayList<String> selectedNames) {
        this.selectedNames = selectedNames;
    }
    
    public String getSelectedName() {
        return selectedName;
    }
    
    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }
    
    public CIFSUser getUserByName(String name) {
        for (int i = 0; i < allUserList.size(); i++) {
            if (allUserList.get(i).getName().equals(name)) {
                return allUserList.get(i);
            }
        }
        return null;
    }
    
    public List<CIFSUser> getAllUserList() {
        return allUserList;
    }
    
    public void setAllUserList(List<CIFSUser> allUserList) {
        this.allUserList = allUserList;
    }
    
    public String updateSelectedUserForModifyGroup() {
        FacesContext context = FacesContext.getCurrentInstance();
        String name = context.getExternalContext().getRequestParameterMap().get("username");
        //String  userPasswd = context.getExternalContext().getRequestParameterMap().get("userpasswd");
        //this.userName = name;
//        selectedUser.getInGroups().removeAll(selectedUser.getInGroups());
//        selectedUser.getSourceGroups().removeAll(selectedUser.getSourceGroups());
        selectedUser = new CIFSUser();
        CIFSUser u = userModel.getRowData(name);
        selectedUser.getInGroups().addAll(u.getInGroups());
        selectedUser.getSourceGroups().addAll(u.getSourceGroups());
        DualListModel<String> modelGroups = new DualListModel<String>(u.getInGroups(), u.getSourceGroups());
        selectedUser.setModelGroups(modelGroups);
        //this.selectedName = name;
        selectedUser.setName(name);
        return  "nas_modify_groupUserBelong?faces-redirect=true";
        
    }

    public String updateSelectedGroupForEdit() {
        FacesContext context = FacesContext.getCurrentInstance();
        String name = context.getExternalContext().getRequestParameterMap().get("groupname");
        Group group = groupModel.getGroupByName(name);
        selectedRowGroup = new Group();
        selectedRowGroup.setName(name);
       // selectedRowGroup.getContainUserNames().removeAll(selectedRowGroup.getContainUserNames());
       // selectedRowGroup.getSourceUserNames().removeAll(selectedRowGroup.getSourceUserNames());
        selectedRowGroup.getContainUserNames().addAll(group.getContainUserNames());
        selectedRowGroup.getSourceUserNames().addAll(group.getSourceUserNames());
        
        DualListModel<String> modelUsers = new DualListModel<String>(group.getSourceUserNames(), group.getContainUserNames());
        selectedRowGroup.setModelUsers(modelUsers);
        return  "nas_modify_withinTheGroupOfUsers?faces-redirect=true";
        
        
    }
    public void updateSelectedUserForDel() {
        FacesContext context = FacesContext.getCurrentInstance();
        String name = context.getExternalContext().getRequestParameterMap().get("username");
        this.selectedUser = userModel.getRowData(name);
        
    }
    public void updateSelectedGroupForDel(Group group) {
        this.selectedGroup = group;
        
    }

    public String editUserGroup() {
        List<String> groupNames = this.selectedUser.getModelGroups().getSource();
        StringBuffer names = new StringBuffer();
        for (int i = 0; i < groupNames.size(); i++) {
            names.append(groupNames.get(i));
            if ((i + 1) < groupNames.size()) {
                names.append(",");
            }
        }
        //this.selectedUser.setAttributeGroups(names.toString());
        this.userModel.updateUserGroup(this.selectedUser.getName(), (ArrayList<String>) this.selectedUser.getModelGroups().getSource(), (ArrayList<String>) this.selectedUser.getModelGroups().getTarget(), names.toString());
        CIFSUser user = this.userModel.getRowData(this.selectedUser.getName());
        for (String groupName : this.selectedUser.getModelGroups().getSource()) {
            this.groupModel.addUserToGroup(user, groupName);
        }
        return  "nas_user_group?faces-redirect=true";
    }
    
    public String editGroupContainedUser() {
        List<String> userNames = this.selectedRowGroup.getModelUsers().getTarget();
        String userStr = this.groupModel.getUserStrByList(userNames);
        this.groupModel.updateUsersInGroup(this.selectedRowGroup.getName(), (ArrayList<String>) this.selectedRowGroup.getModelUsers().getSource(), (ArrayList<String>) this.selectedRowGroup.getModelUsers().getTarget(), userStr);
        for (String userName : this.selectedRowGroup.getModelUsers().getTarget()) {
            this.userModel.addGroupToUser(this.selectedRowGroup.getName(), userName);
        }
        return  "nas_user_group?faces-redirect=true";
    }
    
    public String updateSelectedUserForModifyPasswd() {
        FacesContext context = FacesContext.getCurrentInstance();
        String name = context.getExternalContext().getRequestParameterMap().get("username");
        //this.selectedName = name;
        selectedUser = new CIFSUser();
        this.selectedUser.setName(name);
        CIFSUser user = this.getUserModel().getRowData(name);
        this.selectedUser.setPasswd(user.getPasswd());
        return  "nas_modify_passwd?faces-redirect=true";
    }
    
    public String editUserPasswd() {
        String passwd = this.selectedUser.getPasswd();
        String confirmPasswd = this.selectedUser.getConfirmPasswd();
        String errorinfo = "";
//        if (passwd.length() < 6 || passwd.length() > 10 || confirmPasswd.length() < 6 || confirmPasswd.length() > 10 || !passwd.matches("^[a-zA-Z0-9]+$") || !confirmPasswd.matches("^[a-zA-Z0-9]+$")) {
//            errorinfo = "密码由6-10位字母和数字组成！";
//            RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);
//            
//            return;
//        }
        if (!passwd.equals(confirmPasswd)) {
           // errorinfo = "确认密码和密码不一致！";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "错误", "确认密码和密码不一致！"));
            RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);
            return  null;
        }
        this.userModel.updatePasswd(this.selectedUser.getName(), this.selectedUser.getPasswd());
       // RequestContext.getCurrentInstance().addCallbackParam("result", "success");
        return  "nas_user_group?faces-redirect=true";
    }

    public void changeValue() {
        
        if (this.selectedType.equals(Constant.getOneStyleName(Constant.group))) {
            //this.names = this.getGroupsFromSession().getGroupNames();
            this.selectedNames = this.groupModel.getGroupNames();
        }
        if (this.selectedType.equals(Constant.getOneStyleName(Constant.user))) {
            //this.names = this.getUserFromSession().getUserNames();
            this.selectedNames = this.userModel.getUserNames();
        }
        
    }
    
    public ArrayList<String> getGroupNames() {
        return groupNames;
    }
    
    public void setGroupNames(ArrayList<String> groupNames) {
        this.groupNames = groupNames;
    }
    
    public ArrayList<String> getUserNames() {
        return userNames;
    }
    
    public void setUserNames(ArrayList<String> userNames) {
        this.userNames = userNames;
    }
    public void viewUserListen() { //用在查看用户所属组的selectonelist
        this.userForView = new CIFSUser();
        CIFSUser user = this.userModel.getRowData(this.selectedUserName);
        userForView.getInGroups().addAll(user.getInGroups());
       // userForView.getInGroups().add("anm");
    }
    
}
