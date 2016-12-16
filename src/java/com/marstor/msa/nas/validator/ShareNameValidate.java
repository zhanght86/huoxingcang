/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.validator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;
/**
 *
 * @author Administrator
 */
public class ShareNameValidate implements Validator {
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String  shareName = (String)o;
        if(!shareName.matches("^[a-zA-Z0-9]+$") || shareName.length()>32 ) {
            
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "Ŀ¼���Ƹ�ʽ����","Ŀ¼������ĸ��������ɣ��ҳ��Ȳ�����32λ");
            throw new ValidatorException(message); 
        }
        
        
    }
}
