/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.UserGroupData;
import com.marstor.msa.nas.model.MySession;
import com.marstor.msa.nas.bean.User;
import com.marstor.msa.nas.bean.UserID;
import com.marstor.msa.nas.validator.PasswordValid;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class AddUserBean implements Serializable{

    private int number;
    private String name = "";
    private String passwd = "";
    //private ArrayList<Group>  attributeGroups = new ArrayList<Group>();
    //private ArrayList<Group>  sourceGroups = new ArrayList<Group>();
    private String errorinfo;
    private String confirmPasswd = "";

    public AddUserBean() {
      //  this.errorinfo = "用户名为字母和下划线组成！";
    }

    public String getConfirmPasswd() {
        return confirmPasswd;
    }

    public void setConfirmPasswd(String confirmPasswd) {
        this.confirmPasswd = confirmPasswd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String save() {

        if (this.name.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyUser"), res.get("emptyUser")));
            return null;
        }
        if (this.name.length() > 8) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("userCompose"), res.get("userCompose")));
            return null;
        }
        if (!this.name.matches("^[a-z0-9]+$")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("userCompose"), res.get("userCompose")));
            return null;
        }
        String start = "";
        if (this.name.length() > 0) {
            start = name.substring(0, 1);
        }
        if (!start.matches("[a-z]")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("userCompose"), res.get("userCompose")));
            return null;
        }
//        if (this.passwd.length() < 6 || this.passwd.length() > 10 || this.confirmPasswd.length() < 6 || this.confirmPasswd.length() > 10 || !passwd.matches("^[a-zA-Z0-9]+$") || !confirmPasswd.matches("^[a-zA-Z0-9]+$")) {
//            this.errorinfo = "密码由6-10位字母和数字组成！";
//            RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);
//            return;
//        }
        if (this.passwd.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyPassd"), res.get("emptyPassd")));
            return null;
        }
        if (!PasswordValid.validatePasswd(this.passwd)) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("passwdCompose"), res.get("passwdCompose")));
            return null;
        }
        if (this.confirmPasswd.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyConfirm"), res.get("emptyConfirm")));
            return null;
        }
        if (!this.confirmPasswd.equals(this.passwd)) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("notSame"), res.get("notSame")));
            return null;
        }
//        UserGroupData gs = MySession.getGroupsFromSession();
//        String uName = this.name;
//        String uPasswd = this.passwd;
//        gs.addUser(uName, uPasswd);
//        name = "";
//        passwd = "";
        User user = InterfaceFactory.getNASInterfaceInstance().isUserExistOrNotInSystem(this.name);
        if (user == null) {
            Debug.print("judge user is or not in system failed");
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addUserFailed"), res.get("addUserFailed")));
            return null;
        }
        if (user.isExistOrNotInSystem()) {
            Debug.print("user exist in system ");
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("existUser"), res.get("existUser")));
            return null;
        }
        int index = UserID.getMin();
        ArrayList<User> allUsers = InterfaceFactory.getNASInterfaceInstance().getAllSystemUsersInfo();
        if (allUsers.size() > 0) {
            int temp = allUsers.get(0).getId();
            int max = temp;
            for (int i = 1; i < allUsers.size(); i++) {
                if (temp > allUsers.get(i).getId()) {
                    max = temp;
                } else {
                    max = allUsers.get(i).getId();
                }
                temp = max;
            }
            index = max + 1;
        }


        boolean flag1 = InterfaceFactory.getNASInterfaceInstance().addUser(String.valueOf(index), this.name, new ArrayList<String>());
        if (!flag1) {
            Debug.print("add user" + flag1);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addUserFailed"), res.get("addUserFailed")));
            return null;
        }
        boolean flag2 = InterfaceFactory.getCommonInterfaceInstance().modifyUserPassword(this.name, this.confirmPasswd);
        //RequestContext.getCurrentInstance().addCallbackParam("result", "success");
        if (!flag2) {
            Debug.print("add user" + flag2);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addUserFailed"), res.get("addUserFailed")));
            return null;
        }
        return "nas_domain_config?faces-redirect=true&amp;tabViewActive=1";
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

//    public void mylisten() {
//        if (this.name.length() > 3) {
//            this.errorinfo = "cuowu";
//        }
//    }
}
