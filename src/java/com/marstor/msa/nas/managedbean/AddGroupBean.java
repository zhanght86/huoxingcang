/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.MySession;
import com.marstor.msa.nas.bean.User;
import com.marstor.msa.nas.bean.UserGroup;
import com.marstor.msa.nas.model.UserGroupData;
import com.marstor.msa.nas.bean.UserGroupID;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class AddGroupBean  implements Serializable{

    private String groupName = "";
    private String errorinfo;

    public AddGroupBean() {
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String save() {
        if (this.groupName.equals("")) {
            // this.errorinfo = "用户组名不能为空！";
            //RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyGroup"), res.get("emptyGroup")));
            return null;
        }
        
        //char begin = this.groupName.charAt(0);
        String start ="";
        if(this.groupName.length()>0) {
           start = this.groupName.substring(0,1);
            
        }
        if (!this.groupName.matches("^[a-z0-9]+$") || groupName.length() > 8 || start.matches("[0-9]")) {
            //  this.errorinfo = "用户组名由1-8位小写字母和数字组成！";groupCompose
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("groupCompose"), res.get("groupCompose")));
            return null;
        }
        UserGroup userGroup = InterfaceFactory.getNASInterfaceInstance().isUserGroupExistInSystem(this.groupName);
        if (userGroup  == null) {
            Debug.print("judge group is or not in system failed");
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addGroupFailed"), res.get("addGroupFailed")));
            return null;
        }
        if (userGroup.isExistOrNotInSystem()) {
            Debug.print("group exist in system ");
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("existGroup"), res.get("existGroup")));
            return null;
        }
        int index = UserGroupID.getMin();
        ArrayList<UserGroup> allGroups = InterfaceFactory.getNASInterfaceInstance().getAllSystemUserGroups();
        if(allGroups==null) {
            Debug.print("get all groups failed");
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addGroupFailed"), res.get("addGroupFailed")));
            return null;
        }
        if (allGroups.size() > 0) {
            int temp = allGroups.get(0).getId();
            int max = temp;
            for (int i = 1; i < allGroups.size(); i++) {
                if (temp > allGroups.get(i).getId()) {
                    max = temp;
                } else {
                    max = allGroups.get(i).getId();
                }
                temp = max;
            }
            index = max + 1;
        }
        UserGroup group = new UserGroup(index, groupName);
        boolean flag = InterfaceFactory.getNASInterfaceInstance().addUserGroup(group);
        if(!flag) {
            Debug.print("add group " +flag);
             MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addGroupFailed"), res.get("addGroupFailed")));
            return null;
        }
//        UserGroupData data = MySession.getGroupsFromSession();
//        data.addGroup(groupName);
//        RequestContext.getCurrentInstance().addCallbackParam("result", "success");
        
        return "nas_domain_config?faces-redirect=true&amp;tabViewActive=1&amp;accordionActive=1" ;
    }
}
