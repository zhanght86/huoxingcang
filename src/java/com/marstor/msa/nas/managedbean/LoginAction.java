/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;



import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@javax.faces.bean.ManagedBean(name = "loginAction")
@javax.faces.bean.RequestScoped
public class LoginAction extends MyAction{

    /**
     * Creates a new instance of LoginAction
     */
    
    private String userInfo;

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
    
    public LoginAction() {
    }
    
//    public void login(ActionEvent event){
//        RequestContext context = RequestContext.getCurrentInstance();
//        ValueBinding binding = context.getApplication().createValueBinding("#{userInfo}");
//        userInfo = (String) binding.getValue(context);
//        RequestContext rc = RequestContext.getCurrentInstance();
//        String p = (String) event.getComponent().getAttributes().get("pri");
//        
//        Debug.print("asdfadfasdfasdfasdfasdfasdfasdfasfdasdfasdasdfasdfdsgdsffasfda" + userInfo + ",       " + p);
//        boolean loggedIn = false;
//
//        if (userInfo != null && userInfo.equals("admin") && userInfo != null && userInfo.equals("admin")) {
//            loggedIn = true;
//            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", userInfo);
//        } else {
//            loggedIn = false;
//            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials");
//        }
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//        loginState(loggedIn);
//    }
    
}
