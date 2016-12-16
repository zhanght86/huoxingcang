/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;
import com.marstor.msa.nas.model.UserDataModel;
import com.marstor.msa.nas.model.UserGroupData;
import com.marstor.msa.nas.model.MySession;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.view.facelets.FaceletContext;
import org.primefaces.context.RequestContext;
import javax.faces.context.FacesContext;
/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class EditUserPasswd implements Serializable{
    private String userName="";
    private String passwd="";
    //private ArrayList<Group>  attributeGroups = new ArrayList<Group>();
    //private ArrayList<Group>  sourceGroups = new ArrayList<Group>();
    private String errorinfo="";
    private String confirmPasswd="";

    public String getConfirmPasswd() {
        return confirmPasswd;
    }

    public void setConfirmPasswd(String confirmPasswd) {
        this.confirmPasswd = confirmPasswd;
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void updateUserName() {
              
        FacesContext context = FacesContext.getCurrentInstance();
        String name = context.getExternalContext().getRequestParameterMap().get("username");
        this.userName = name;
        
        
    }
    public void  save() {
        UserGroupData  groups = MySession.getGroupsFromSession();
        UserDataModel model = groups.getUserModel();
        model.updatePasswd(userName, passwd);
        String  uName = this.userName;
        String  uPasswd = this.passwd;
        groups.addUser(uName, passwd);
    }
    
}
