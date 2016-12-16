/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Administrator
 */
public class LoginPasswordValidate implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {

        String passwd = (String) o;
        if (passwd==null || passwd.equals("") || passwd.length() < 5 || passwd.length() > 10 || !passwd.matches("^[a-zA-Z0-9]+$")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "错误", "密码由5-10位字母和数字组成！");
            throw new ValidatorException(message);
        }
    }
}
