/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.scsi.model.iSCSIGlobalInitiator;
import com.marstor.msa.common.util.MSAResource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.marstor.msa.util.InterfaceFactory;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class ModifyGlobalInitiatorAlias {

    private String name;
    private String alias;
    private iSCSIGlobalInitiator initiator;

    public ModifyGlobalInitiatorAlias() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        String initiatorName = request.getParameter("initiatorName");
        this.name = initiatorName;
//        iSCSIGlobalInitiatorData data =  SCSISession.getiSCSIGlobalInitiatorDataFromSession();
//        initiator = data.getTemp();
//        this.name = initiator.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String save() {
        MSAResource res = new MSAResource();
        if (this.alias.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("canNotEmpty"), res.get("canNotEmpty")));
//            Debug.print("Createiscsitarget " + flag);
            return null;
        }
        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().ModifyInitiatorName(this.name, this.alias);
        if (!flag) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setAliasFailed"), res.get("setAliasFailed")));
//            Debug.print(" Createiscsitarget " + flag);
            return null;
        }
        //initiator.setAlias(this.alias);
        return "scsi_initiator?faces-redirect=true&amp;tabViewActiveIndex=1&accordionPanelActiveIndex=0";
    }
}
