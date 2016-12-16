/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.nas.model.UserDataModel;
import com.marstor.msa.nas.model.CIFSUser;
import com.marstor.msa.nas.model.UserGroupData;
import com.marstor.msa.nas.model.MySession;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.primefaces.model.DualListModel;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class EditUserGroup implements Serializable{

    private ArrayList<String> groupsSource = new ArrayList<String>();
    private ArrayList<String> groupsTarget = new ArrayList<String>();
    private DualListModel<String> modelGroups = new DualListModel<String>(groupsSource, groupsTarget);
    private String userName;

    public ArrayList<String> getGroupsSource() {
        return groupsSource;
    }

    public void setGroupsSource(ArrayList<String> groupsSource) {
        this.groupsSource = groupsSource;
    }

    public ArrayList<String> getGroupsTarget() {
        return groupsTarget;
    }

    public void setGroupsTarget(ArrayList<String> groupsTarget) {
        this.groupsTarget = groupsTarget;
    }

    public DualListModel<String> getModelGroups() {
        return modelGroups;
    }

    public void setModelGroups(DualListModel<String> modelGroups) {
        this.modelGroups = modelGroups;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public Groups getSessionGroups() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Groups gs = (Groups) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{groups}", Groups.class).getValue(context.getELContext());
//        return  gs;
//    }
    public void updateSourceAndTarget() {

        FacesContext context = FacesContext.getCurrentInstance();
        String name = context.getExternalContext().getRequestParameterMap().get("username");
        //String  userPasswd = context.getExternalContext().getRequestParameterMap().get("userpasswd");
        //this.userName = name;
        //selectedUser  = getUserByName(name);
        this.userName = name;
        UserGroupData gs = MySession.getGroupsFromSession();
        CIFSUser user = gs.getUserModel().getRowData(name);
        this.groupsSource = user.getSourceGroups();
        this.groupsTarget = user.getInGroups();
        this.modelGroups.setSource(groupsSource);
        this.modelGroups.setTarget(groupsTarget);
        
        //gs.setGroupsSource(groupsSource);
        //gs.setGroupsTarget(groupsTarget);

    }

    public void save() {

        UserGroupData gs = MySession.getGroupsFromSession();
//        User user = gs.getUserModel().getRowData(this.userName);
//        user.setInGroups(gs.getGroupsTarget());
//        user.setSourceGroups(gs.getGroupsSource());
        UserDataModel  model = gs.getUserModel();
        //model.updateUserGroup(this.userName, (ArrayList<String>)this.getModelGroups().getSource(), (ArrayList<String>)this.getModelGroups().getTarget());
    }
}
