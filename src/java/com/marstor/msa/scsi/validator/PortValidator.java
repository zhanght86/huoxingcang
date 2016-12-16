/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.validator;

import com.marstor.msa.common.util.MSAResource;
import static com.marstor.msa.scsi.validator.TargetNameValidator.checkIQN;
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
@FacesValidator("portValidator")
public class PortValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String port = (String) o;

        //System.out.println("");
        //resource.get("invalidTarget")
        if (!port.matches("^[0-9]+$")) {
            MSAResource resource = new MSAResource();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("invalidPort"), resource.get("invalidPort"));
            throw new ValidatorException(message);
        }
        long portNum = Long.parseLong(port);

        if ((portNum < 0) || (portNum > 65535)) {
            MSAResource resource = new MSAResource();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("invalidPort"), resource.get("invalidPort"));
            throw new ValidatorException(message);
        }

    }

    public static boolean isValidatePort(String port) {
        if (!port.matches("^[0-9]+$")) {
            return false;
        }
        if(port.length()>5) {
            return false;
        }
        long portNum = Long.parseLong(port);
        if ((portNum < 0) || (portNum > 65535)) {
            return false;
        }
        return true;
    }
}
