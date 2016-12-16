
package com.marstor.msa.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


public class SelectOneItemConverter implements Converter {
   

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) { 
        System.out.println("getAsObject"+submittedValue);
        return new SelectOneItem(submittedValue);
//        return submittedValue;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        System.out.println("getAsString"+value);
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }
}
