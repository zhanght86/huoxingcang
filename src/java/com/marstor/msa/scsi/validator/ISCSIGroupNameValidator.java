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
@FacesValidator ("iSCSIGroupNameValidator")
public class ISCSIGroupNameValidator implements Validator {
    private static MSAResource resource = new MSAResource();

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String group = (String)o;
        if(!group.startsWith("iscsi_") ) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("startWithiSCSI") , resource.get("startWithiSCSI"));
            throw new ValidatorException(message);
        }else if(!group.matches("^[a-zA-Z0-9_]+$")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("letterAndNumber") , resource.get("letterAndNumber"));
            throw new ValidatorException(message);
        }else if(group.length()>32  ) {
            //lessThan32
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("lessThan32") , resource.get("lessThan32"));
            throw new ValidatorException(message);
        }else if(group.length()<7) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("moreThan6") , resource.get("moreThan6"));
            throw new ValidatorException(message);
        }
        
        
    }
    
}
