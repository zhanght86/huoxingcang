/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.validator;

import com.marstor.msa.common.managedbean.ConfirmDialogBean;
import com.marstor.msa.common.managedbean.VerifyCodeBean;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
public class CodeValidator implements Validator
{

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException
    {
        System.out.println("validate");
        if(o ==null)
        {
            return;
        }
        String code = (String) o;
        
        HttpServletRequest s = (HttpServletRequest) fc.getExternalContext().getRequest();
        ConfirmDialogBean confirmbean= (ConfirmDialogBean) fc.getELContext().getELResolver().getValue(fc.getELContext(), null, "confirmDialogBean");
        if (!code.equalsIgnoreCase(confirmbean.getCode()))
        {
            FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "验证码不匹配", "验证码不匹配");
            throw new ValidatorException(message);
        }
    }
}
