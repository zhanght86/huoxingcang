/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import com.marstor.msa.common.util.MSAResource;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 *
 * @author Administrator
 */
public class BootSequenConverter implements Converter{
    public static List<BootSequen> playerDB;
    MSAResource resources = new MSAResource();
    public BootSequenConverter() {
        playerDB = new ArrayList<BootSequen>();
        playerDB.add(new BootSequen(resources.get("vm.vm_vminfotab", "BootSequence_" + 1), resources.get("vm.vm_vminfotab", "boot_" + 1)));
        playerDB.add(new BootSequen(resources.get("vm.vm_vminfotab", "BootSequence_" + 2), resources.get("vm.vm_vminfotab", "boot_" + 2)));
        playerDB.add(new BootSequen(resources.get("vm.vm_vminfotab", "BootSequence_" + 3), resources.get("vm.vm_vminfotab", "boot_" + 3)));

    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String submittedValue) {
         if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
//                int number = Integer.parseInt(submittedValue);

                for (BootSequen p : playerDB) {
                    if (p.getType().equalsIgnoreCase(submittedValue)) {
                        return p;
                    }
                }
                
            } catch(NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
            }
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((BootSequen) value).getType());
        }
    }
    
}
