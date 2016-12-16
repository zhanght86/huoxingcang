/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.validator;

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
@FacesValidator("IPValidate")
public class IPValidate implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String ip = (String) o;
        MSAResource resource = new MSAResource();
        if (!isIp(ip)) {
            //"无效的IP","无效的IP"
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("invalidIP"), resource.get("invalidIP"));
            throw new ValidatorException(message);

        }

    }

    public static boolean isIp(String IP) {//判断是否是一个IP

        boolean mask = false;
        String array[] = IP.trim().split("/");
        if (array != null && array.length == 2) {

            if (array[1].matches("\\d{1,2}")) {
                if (Integer.parseInt(array[1]) > 0 && Integer.parseInt(array[1]) < 25) {
                    mask = true;
                }
            }
            if (isIPFormat(array[0]) && mask) {
                return true;
            } else {
                return false;
            }
        }else if(array != null && array.length == 1)  {
             if (isIPFormat(IP) ) {
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }


    }

    public static boolean isIPFormat(String IP) {
        boolean b = false;
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = IP.split("\\.");
            if (Integer.parseInt(s[0]) < 255) {
                if (Integer.parseInt(s[1]) < 255) {
                    if (Integer.parseInt(s[2]) < 255) {
                        if (Integer.parseInt(s[3]) < 255) {
                            b = true;
                        }
                    }
                }
            }

        }
        return b;
    }
    public static boolean checkIP(String name) {
        boolean flag = false;
        if (null == name || "".equals(name)) {
            return flag;
        }
        if (!Character.isDigit(name.charAt(name.length() - 1))){
            return flag;
        }
        flag = name.matches("^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$");
        return flag;
    }
}
