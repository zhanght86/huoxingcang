/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.validator;

import com.marstor.msa.common.util.MSAResource;
import static com.marstor.msa.nas.validator.IPValidate.isIp;
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
@FacesValidator("iSCSIMemberValidate")
public class ISCSIMemberValidate implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String member = (String) o;
        MSAResource resource = new MSAResource();
//        String  array[] = member.split("\\.");
//        if(array==null || array.length!=2 || !array[0].equals("iqn") || array[1].length()!=16 || !array[1].matches("^[A-F0-9]+$")) {
//            //"无效的IP","无效的IP"
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,resource.get("invalidGroup"),resource.get("invalidGroup") );
//            throw new ValidatorException(message); 
//
//        }
        if (!checkIQN(member)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("invalidName"), resource.get("invalidName"));
            throw new ValidatorException(message);
        }

    }

    public static boolean checkIQN(String name) {
        boolean flag = false;
        if (name.length() <= 64 && name.length() > 0) {
            flag = name.matches("^(iqn)[.](\\d){4}[-](\\d){2}[.][A-Za-z0-9._:-]+$");
        }
        if (flag) {
            String array[] = name.trim().split("\\.");
            if (array != null || array.length > 3) {
                String date = array[1];
                String arrayDate[] = date.split("-");
                if (arrayDate != null || arrayDate.length > 1) {
                    int year = Integer.parseInt(arrayDate[0]);
                    int month = Integer.parseInt(arrayDate[1]);
                    if (year < 1 || month > 12 || month < 1) {
                        flag = false;
                    }
                } else {
                    flag = false;
                }
            }
        }

        return flag;
    }
}
