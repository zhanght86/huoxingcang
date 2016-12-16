/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.Utility;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.nas.validator.PasswordValid;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
public class ModifyUserPasswd implements Serializable{

    private String passwd;
    private String confirmPasswd;
    private String userName;

    public ModifyUserPasswd() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.userName = request.getParameter("userName");

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getConfirmPasswd() {
        return confirmPasswd;
    }

    public void setConfirmPasswd(String confirmPasswd) {
        this.confirmPasswd = confirmPasswd;
    }

    public String save() {
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
        boolean flag = InterfaceFactory.getCommonInterfaceInstance().modifyUserPassword(this.userName, this.confirmPasswd);
       Debug.print("username: " + this.userName + "  passwd: " + this.passwd );
        if (!flag) {
            Debug.print("modify passwd" + flag);
            Debug.print("username: " + this.userName + "  passwd: " + this.passwd );
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("modifyFailed"), res.get("modifyFailed")));
            return null;
        }
        return "nas_domain_config?faces-redirect=true&amp;tabViewActive=1";
    }
}
