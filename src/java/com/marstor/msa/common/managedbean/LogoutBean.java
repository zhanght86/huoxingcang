/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "logoutBean")
@RequestScoped
public class LogoutBean implements Serializable {

    /**
     * Creates a new instance of LogoutBean
     */
    public LogoutBean() {
    }
    public void logoutPage() {
//        System.out.println("logout ****************");
//        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
//        request.getSession().invalidate();
//        request.getSession().removeAttribute("user");
//        System.out.println("logout succeed");
//        RequestContext.getCurrentInstance().execute("window.top.location.href='login.xhtml'");

    }
}
