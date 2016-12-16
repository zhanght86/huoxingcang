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
@FacesValidator ("targetAliasValidator")
public class TargetAliasValidator implements Validator{
    private static MSAResource resource = new MSAResource();

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String alias = (String)o;
        if(alias.equals("") ||!checkAliasName(alias)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("invalidAlias") , resource.get("invalidAlias"));
            throw new ValidatorException(message);
        }
        
        
    }
    public static boolean checkAliasName(String name) {
        boolean flag = false;
        if (name.length() <= 64 && name.length() > 0) {
            flag = name.matches("^[A-Za-z]*" + "("+ resource.get("marsiscsi") + ")" + "\\d+$");
        }
        return flag;
    }
    
}
