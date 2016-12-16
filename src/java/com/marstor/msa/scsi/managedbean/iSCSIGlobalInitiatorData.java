/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean;

import com.marstor.msa.scsi.model.iSCSIGlobalInitiator;
import com.marstor.msa.common.bean.InitiatorNodeInformation;
import java.util.ArrayList;
import com.marstor.msa.util.InterfaceFactory;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class iSCSIGlobalInitiatorData {
    //initiators数组只包含一条记录
    private ArrayList<iSCSIGlobalInitiator>  initiators = new ArrayList<iSCSIGlobalInitiator>();
    private iSCSIGlobalInitiator temp;
    private iSCSIGlobalInitiator tempChap;
    private String  tabViewActiveIndex="";
    private String  accordionPanelActiveIndex="";
    //private int num=2;
    public iSCSIGlobalInitiatorData() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        tabViewActiveIndex = request.getParameter("tabViewActiveIndex");
        if(tabViewActiveIndex==null || tabViewActiveIndex.equals("")) {
            tabViewActiveIndex = "0";
        }
        accordionPanelActiveIndex = request.getParameter("accordionPanelActiveIndex");
        if(accordionPanelActiveIndex==null || accordionPanelActiveIndex.equals("")) {
            accordionPanelActiveIndex = "0";
        }
        InitiatorNodeInformation  info =  InterfaceFactory.getSCSIInterfaceInstance().listInitiatorNode();
        if(info==null) {
            return ;
        }
        iSCSIGlobalInitiator initiator = new iSCSIGlobalInitiator(info.getName(), info.getAlias(), info.getAuthType(), info.getChapName());
//        iSCSIGlobalInitiator initiator = new iSCSIGlobalInitiator("iqn.2013-02.com.marstor:1359679026925.initiator","msaserver", new Chap());
        initiators.add(initiator);
       
    }

    

    public String getTabViewActiveIndex() {
        return tabViewActiveIndex;
    }

    public void setTabViewActiveIndex(String tabViewActiveIndex) {
        this.tabViewActiveIndex = tabViewActiveIndex;
    }

    public String getAccordionPanelActiveIndex() {
        return accordionPanelActiveIndex;
    }

    public void setAccordionPanelActiveIndex(String accordionPanelActiveIndex) {
        this.accordionPanelActiveIndex = accordionPanelActiveIndex;
    }

    public ArrayList<iSCSIGlobalInitiator> getInitiators() {
        return initiators;
    }

    public void setInitiators(ArrayList<iSCSIGlobalInitiator> initiators) {
        this.initiators = initiators;
    }

    public iSCSIGlobalInitiator getTempChap() {
        return tempChap;
    }

    public void setTempChap(iSCSIGlobalInitiator tempChap) {
        this.tempChap = tempChap;
    }
    
    public String doBeforeModifyAlias(iSCSIGlobalInitiator initiator) {
        //this.temp = initiator;
        String  initiatorName = initiator.getName();
        String  param = "initiatorName=" + initiatorName ;
        return  "scsi_modify_initiator_alias?faces-redirect=true&amp;" + param ;
    }

    public iSCSIGlobalInitiator getTemp() {
        return temp;
    }

    public void setTemp(iSCSIGlobalInitiator temp) {
        this.temp = temp;
    }
    public String doBeforeModifyCHAP(iSCSIGlobalInitiator initiator) {
        //this.tempChap = initiator;
        String  authType = initiator.getAuthType();
        String  chapName = initiator.getChapName();
        String  param = "authType=" + authType + "&" + "chapName=" + chapName ;
        return  "scsi_iscsi_initiator_chap?faces-redirect=true" + param ;
    }
    
}
