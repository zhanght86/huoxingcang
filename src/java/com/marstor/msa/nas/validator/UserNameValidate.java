/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Administrator
 */
public class UserNameValidate implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {

        String userName = (String) o;
        if (!userName.matches("^[a-zA-Z0-9]+$")) {
            //this.errorinfo = "�û�������ĸ��������ɣ�";
            FacesMessage  message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "����", "�û�������ĸ��������ɣ�");
            throw new ValidatorException(message); 
           // RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);

        }
    }
}
