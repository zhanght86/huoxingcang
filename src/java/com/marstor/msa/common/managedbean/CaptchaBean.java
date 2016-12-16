/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import java.io.Serializable;
import javax.faces.application.FacesMessage;  
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;  
import javax.faces.event.ActionEvent;  

@ManagedBean(name = "captchaBean")
@RequestScoped
public class CaptchaBean implements Serializable{  
  
    public void submit(ActionEvent event) {  
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correct", "Correct");  
          
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
}  