/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.validator;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
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
@FacesValidator ("targetNameValidator")
public class TargetNameValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String targetName = (String) o;
        //System.out.println("");
        //resource.get("invalidTarget")
        MSAResource resource = new MSAResource();
        if (targetName.equals("") || !checkIQN(targetName)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("invalidTarget") , resource.get("invalidTarget"));
            throw new ValidatorException(message);
        }
        
    }

    public static boolean checkIQN(String name) {
        boolean flag = false;
        if (name.length() <= 64 && name.length() > 0) {
            flag = name.matches("^(iqn.)(\\d){4}(-)(\\d){2}(.)[A-Za-z0-9._:-]+$");
        }
        return flag;
    }
}
