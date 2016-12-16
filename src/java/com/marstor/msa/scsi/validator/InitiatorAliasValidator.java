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
@FacesValidator ("initiatorAliasValidator")
public class InitiatorAliasValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        MSAResource res = new MSAResource();
        String  name = (String) value;
        String  start ="";
        if(!name.matches("^[a-zA-Z0-9_]+$")  ) {
          FacesMessage  message = new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("compose"), res.get("compose"));
        throw new ValidatorException(message); 
        }
        if(name.length()>1) {
            start = name.substring(0, 1);
        }
        if(!start.matches("[a-zA-Z]")){
            FacesMessage  message = new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("letter"), res.get("letter"));
        throw new ValidatorException(message);
        }
        if(name.length()>32  ) {
          FacesMessage  message = new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("aliasLength"), res.get("aliasLength"));
        throw new ValidatorException(message); 
        }
        
    }
    
}
