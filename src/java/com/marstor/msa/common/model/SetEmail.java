/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.managedbean.WarnEmaiBean;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
//@ManagedBean(name = "setEmail")
//@RequestScoped
public class SetEmail implements Serializable{

    public String emailAddress;
    public String smtp;
    public String port;
    public boolean isEnable;
    public String account;
    public String password;

    /**
     * Creates a new instance of SetEmail
     */
    public SetEmail() {
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void save(){
        SystemOutPrintln.print_common("emailAddress="+emailAddress);
        if(isEnable == true){
        }else{
             account = null;
             password = null;
        }
         SystemOutPrintln.print_common("password="+password);
         FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
        WarnEmaiBean share = (WarnEmaiBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{warnEmaiBean}", WarnEmaiBean.class).getValue(context.getELContext());
        share.sendEmail = emailAddress;
    }
}
