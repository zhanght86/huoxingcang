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
public class DomainNameValidate implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String  domain = (String)o;
        if(!domain.matches("^[a-zA-Z0-9.]+$") ) {
            
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "域名格式错误", "域名格式错误");
            throw new ValidatorException(message); 
        }
        if(counter(domain, '.')<2) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "域名格式错误", "域名格式错误");
            throw new ValidatorException(message); 
        }
        if(!isDomain(domain)) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "域名格式错误", "域名格式错误");
            throw new ValidatorException(message); 
        }
        
    }
    public static int counter(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }
    public static boolean  isDomain(String  str) {
        if(str.startsWith(".")|| str.endsWith(".")) {
             return false;
        }
        String  array[] = str.split("\\.");
        System.out.println(array.length);
        for(int i=0;i<array.length;i++) {
            if(array[i].equals("")) {
                return false;
            }
        }
        return  true;
    }
    public static boolean  validateDomain(String str) {
        if(!str.matches("^[a-zA-Z0-9.]+$") ) {
            return  false;
        }
        if(counter(str, '.')<1) {
            return  false;
        }
        if(!isDomain(str)) {
            return  false;
        }
        return true;
    }
    public  static void main(String  args[]) {
        String  aa = "dsf..hj";
         if(counter(aa, '.')<2) {
            System.out.println("hello");
        }
         if(isDomain(aa)) {
             System.out.println("good");
         }
    }
    
}
