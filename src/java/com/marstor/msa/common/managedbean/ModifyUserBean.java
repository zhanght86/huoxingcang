/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "modifyUserBean")
@ViewScoped
public class ModifyUserBean implements Serializable{
    private String userName;
    private String userType;
    private String password;
    private String secPassword;
    private MSAResource res = new MSAResource();
    private String basename = "common.system_user_edit";

    /**
     * Creates a new instance of System_userInfoList_addBean
     */
    public ModifyUserBean() {
         ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        userName = request.getParameter("userName");
    }
    public String editUser(String name,String pass,String secPass) {
         if (pass.length() == 0 || secPass.length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "password_limit"), ""));
            return null;
        }
        if (!pass.matches("[a-zA-Z0-9]+") || pass.length() < 6 || pass.length() > 32) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "password_limit"), ""));
            return null;
        }
        if (!secPass.matches("[a-zA-Z0-9]+") ||  secPass.length() < 6 || secPass.length() > 32) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "password_limit"), ""));
           return null;
        }

        if (!checkPassword(pass, secPass)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "password_notsame"), ""));
            return null;
        }
          SystemOutPrintln.print_common("name=" + name);
            SystemOutPrintln.print_common("pass=" + pass);
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        
        boolean modifyUser = common.modifyUserPassword(name,pass);
        String returnstr = null;
        if (modifyUser) {

              SystemOutPrintln.print_common("edit user succeed");

            returnstr = "system_user?faces-redirect=true";
        } else {
                SystemOutPrintln.print_common("edit user fail");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "modify_fail"), ""));
        }
        
        
        return returnstr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecPassword() {
        return secPassword;
    }

    public void setSecPassword(String secPassword) {
        this.secPassword = secPassword;
    }

    public boolean checkPassword(String inpsw, String inpsw2) {
        boolean flag = false;
        if (inpsw.equals(inpsw2)) {
            flag = true;
        }
        return flag;
    }
    
    public String cancleButton() {
        userName = "";
        return "system_user?faces-redirect=true";
    }
    
}
