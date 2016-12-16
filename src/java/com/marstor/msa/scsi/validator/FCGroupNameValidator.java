/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.validator;

import com.marstor.msa.common.util.MSAResource;
import static com.marstor.msa.scsi.validator.TargetAliasValidator.checkAliasName;
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
@FacesValidator ("fcGroupNameValidator")
public class FCGroupNameValidator implements Validator{
    private static MSAResource resource = new MSAResource();

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String fcGroup = (String)o;
       // System.out.println(" fcGroup" + fcGroup );
        
//        if(fcGroup.equals("")) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("canNotEmpty") , resource.get("canNotEmpty"));
//            throw new ValidatorException(message);
//        }
        if(!fcGroup.startsWith("fc_") ) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("startWithFC") , resource.get("startWithFC"));
            throw new ValidatorException(message);
        }else if(!fcGroup.matches("^[a-zA-Z0-9_]+$")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("letterAndNumber") , resource.get("letterAndNumber"));
            throw new ValidatorException(message);
        }else if(fcGroup.length()>32  ) {
            //lessThan32
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("lessThan32") , resource.get("lessThan32"));
            throw new ValidatorException(message);
        }else if(fcGroup.length()<4) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("moreThan4") , resource.get("moreThan4"));
            throw new ValidatorException(message);
        }
        
        
    }
    
}
