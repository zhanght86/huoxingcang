/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.vm.model.ISOPathBean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "deleteMultiISOs")
@SessionScoped
public class DeleteMultiISOs {
 private ISOPathBean[] selectedISOs;
    /**
     * Creates a new instance of DeleteMultiISOs
     */
    public DeleteMultiISOs() {
       
    }
    
    public void delete(){
         FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
        ISOPathList share = (ISOPathList) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{iSOPathList}", ISOPathList.class).getValue(context.getELContext());
        selectedISOs = share.getSelectedISOs();
        SystemOutPrintln.print_vm("selectedISOs="+selectedISOs.length);
        SystemOutPrintln.print_vm("selectedISOs="+selectedISOs.length);
    }
}
