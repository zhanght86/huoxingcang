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
public class WindowsDomainUserValidate implements Validator {
 @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String  name = (String)o;
        if(!isValidName(name)) {
            
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "格式错误", "Windows域用户或用户组名称格式错误！示例：administrator@company.com");
            throw new ValidatorException(message); 
        }
        
        
    }
    public static boolean isValidName(String name) {
//        if(!name.contains("@") || !name.contains(".") ) {
//            return false;
//        }
        
        if (getCharacterCountInStr(name, '@') != 1 || getCharacterCountInStr(name, '.') != 1) {
            return false;
        }
        String nameArray1[] = name.split("@");
        if (nameArray1[0].equals("") || nameArray1[1].equals("") || !nameArray1[0].matches("^[a-zA-Z0-9]+$")) {
            return false;
        }
        if (!nameArray1[1].contains(".")) {
            return false;
        }
        String nameArray2[] = nameArray1[1].split("\\.");
        if (nameArray2[0].equals("") || nameArray2[1].equals("") || !nameArray2[0].matches("^[a-zA-Z0-9]+$") || !nameArray2[1].matches("^[a-zA-Z0-9]+$")) {
            return false;
        }
        return true;
        
    }
    public static int getCharacterCountInStr(String str, char c) {
        char[] chs = str.toCharArray();
        int count = 0;
        for (int i = 0; i < chs.length; i++) {
            if (chs[i] == c) {
                count++;
            }
        }
        return count;
    }
    
    
}
