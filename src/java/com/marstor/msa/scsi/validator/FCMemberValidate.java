/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.validator;

import com.marstor.msa.common.util.MSAResource;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Administrator
 */
@FacesValidator ("fCMemberValidator")
public class FCMemberValidate implements Validator {
      @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String  member = (String)o ;
        MSAResource resource = new MSAResource();
        if(member.equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("canNotEmpty"),resource.get("canNotEmpty") );
            throw new ValidatorException(message); 
        }
        String  array[] = member.split("\\.");
        if(array==null || array.length!=2  || array[1].length()!=16 || !array[1].matches("^[a-fA-F0-9]+$")) {
            //"无效的IP","无效的IP"
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("invaildName"),resource.get("invaildName") );
            throw new ValidatorException(message); 

        }
        if(!array[0].equals("eui")&&!array[0].equals("wwn")) {
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("invaildName"),resource.get("invaildName") );
            throw new ValidatorException(message);
        }
        
        
    }
    
}
