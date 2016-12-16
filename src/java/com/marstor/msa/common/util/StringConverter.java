/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.util;

import java.io.UnsupportedEncodingException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 *
 * @author taoxy
 */
public class StringConverter implements Converter{

    /**
     *
     * @param context
     * @param component
     * @param newValues
     * @return
     * @throws ConverterException
     */
    @Override
   public Object getAsObject(FacesContext context, UIComponent component,String newValues) throws ConverterException {
        String newstr = "";
        if (newValues == null) {
                newValues = "";
          }
          byte[] byte1 = null;
          try {
           byte1 = newValues.getBytes("ISO-8859-1");
           newstr = new String(byte1, "UTF-8");
           UIInput input=(UIInput)component;//
           input.setSubmittedValue(newstr);
          } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
          }

          return newstr;

 }

 public String getAsString(FacesContext context, UIComponent component,
   Object Values) throws ConverterException { 
  return (String) Values;
 }
    
}