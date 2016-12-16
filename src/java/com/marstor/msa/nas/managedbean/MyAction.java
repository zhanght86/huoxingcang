/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
public class MyAction {

    /**
     * Creates a new instance of MyAction
     */
    public FacesContext context = FacesContext.getCurrentInstance();
    public FacesMessage msg = null;
    
    public MyAction() {
    }
    
    public void loginState(boolean loggined){
        RequestContext.getCurrentInstance().addCallbackParam("loggedIn", loggined);
        Debug.print("Login State is " + loggined);
    }
    
}
