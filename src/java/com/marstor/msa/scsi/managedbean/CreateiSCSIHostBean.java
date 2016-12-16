/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.nas.validator.IPValidate;
import com.marstor.msa.scsi.model.InitiatorHostBean;
import com.marstor.msa.scsi.validator.PortValidator;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class CreateiSCSIHostBean {

    private String ip;
    private String port;

    public CreateiSCSIHostBean() {
        port = "3260";
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String save() {
        MSAResource res = new MSAResource();
        if (this.ip.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyIP"), res.get("emptyIP")));
            return null;
        }
        if(!IPValidate.isIp(this.ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("invalidIP"), res.get("invalidIP")));
            return null;
        }
        if (this.port.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyPort"), res.get("emptyPort")));
            return null;
        }
        if (!PortValidator.isValidatePort(port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("invalidPort"), res.get("invalidPort")));
            return null;
        }
        String hosts[] = InterfaceFactory.getSCSIInterfaceInstance().listDiscoveryAddress();
        // ArrayList<String>  address = new ArrayList<String>();
        if(hosts==null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("getAddressFailed"), res.get("getAddressFailed")));
            return null;
        }
        String  address = this.ip + ":" + this.port;
        int num;
        for (num = 0; num < hosts.length; num++) {
            if (hosts[num].equals(address)) {
                break;
            }
        }
        if (num < hosts.length) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("existIP"), res.get("existIP")));
            return null;
        }

        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().addDiscoveryAddress(this.ip + ":" + this.port);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addFailed"), res.get("addFailed")));
            return null;
        }
//        InitiatorHostBean  hosts = SCSISession.getInitiatorHostBeanFromSession();
//        hosts.addHost(ip, port);

        return "scsi_initiator?faces-redirect=true&amp;tabViewActiveIndex=1&amp;accordionPanelActiveIndex=1";
    }
    public String  test1() {
        return "nas_user_group?faces-redirect=true";
    }
}
