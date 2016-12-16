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
@FacesValidator ("chapPasswd")
public class CHAPPasswdValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      String passwd = (String) value;
      
        if (passwd.length() < 12 || passwd.length() > 16) {
            MSAResource  res = new MSAResource();
            FacesMessage  mess = new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("invalidPasswd"),res.get("invalidPasswd"));
            throw new ValidatorException(mess);
                //Constants.showErrorMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/InitiatorAccessControlDialog").getString("密码应该为12到16位"));
                  
        }
        if(!checkName(passwd)) {
            MSAResource  res = new MSAResource();
            FacesMessage  mess = new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("passwdNumLetter"),res.get("passwdNumLetter"));
            throw new ValidatorException(mess);
        }
    
    }
    public  boolean checkName(String name) {
        boolean flag = false;
        if (name.length() < 64 && name.length() > 0) {
            flag = name.matches("^[A-Za-z0-9]+$");
        }
        return flag;
    }
    
}
