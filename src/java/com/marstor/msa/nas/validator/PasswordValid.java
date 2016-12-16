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
public class PasswordValid implements  Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        
        String  passwd = (String)o;
        if (passwd.length() < 6 || passwd.length() > 10 || !passwd.matches("^[a-zA-Z0-9]+$") ) {
           FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "错误", "密码由6-10位字母和数字组成！"); 
           throw new ValidatorException(message); 
        }
    }
    public static boolean  validatePasswd(String  passwd) {
        if (passwd.length() < 6 || passwd.length() > 10 || !passwd.matches("^[a-zA-Z0-9]+$") ) {
          return  false;
        }
        return true;
    }
    
    
}
