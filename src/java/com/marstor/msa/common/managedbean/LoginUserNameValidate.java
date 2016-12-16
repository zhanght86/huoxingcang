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
public class LoginUserNameValidate implements Validator  {
      @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {

        String userName = (String) o;
        if (userName==null || userName.equals("") || userName.length() < 5 || !userName.matches("^[a-zA-Z0-9]+$")) {
            //this.errorinfo = "�û�������ĸ��������ɣ�";
            FacesMessage  message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "����", "�û�������ĸ���������,�Ҳ�������5λ��");
            throw new ValidatorException(message); 
           // RequestContext.getCurrentInstance().addCallbackParam("result", errorinfo);

        }
    }
}
