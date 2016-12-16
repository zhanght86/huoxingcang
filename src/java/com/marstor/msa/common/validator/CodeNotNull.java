/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.validator;

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
public class CodeNotNull implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String code = (String) o;
        System.out.println("ccccccccccccccccccccccccccccccccc" + code);

        if (code.length() <= 0) {
            VerifyCodeBean codeBean = (VerifyCodeBean) ((HttpServletRequest) fc.getExternalContext().getRequest()).getSession().getAttribute("verifyCode");
            codeBean.updateImage();
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "验证码不能为空", "验证码不能为空");
            throw new ValidatorException(message);
        } 

        VerifyCodeBean codeBean = (VerifyCodeBean) ((HttpServletRequest) fc.getExternalContext().getRequest()).getSession().getAttribute("verifyCode");
        codeBean.updateImage();

    }
}
