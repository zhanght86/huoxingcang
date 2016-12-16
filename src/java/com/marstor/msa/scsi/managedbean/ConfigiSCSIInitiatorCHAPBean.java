/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.scsi.model.Chap;
import com.marstor.msa.scsi.model.iSCSIGlobalInitiator;
import com.marstor.msa.util.InterfaceFactory;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
public class ConfigiSCSIInitiatorCHAPBean {

    private boolean isStart;
    private String name;
    private String passwd;
    private boolean nameInput;
    private boolean passwdInput;

    public ConfigiSCSIInitiatorCHAPBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        String authType = request.getParameter("authType");
        String chapName = request.getParameter("chapName");

        if (authType.equals("NONE")) {
            this.isStart = false;
            this.name = "";
        } else {
            this.isStart = true;
            this.name = chapName;
        }
//        iSCSIGlobalInitiatorData  data = SCSISession.getiSCSIGlobalInitiatorDataFromSession();
//        iSCSIGlobalInitiator temp = data.getTempChap();
//        
//        isStart = temp.getChap().isIsStart();
//       name = temp.getChap().getuName();
//       passwd = temp.getChap().getuPasswd();
        this.changeBooleanCheckbox();
    }

    public boolean isIsStart() {
        return isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public boolean isNameInput() {
        return nameInput;
    }

    public void setNameInput(boolean nameInput) {
        this.nameInput = nameInput;
    }

    public boolean isPasswdInput() {
        return passwdInput;
    }

    public void setPasswdInput(boolean passwdInput) {
        this.passwdInput = passwdInput;
    }

    public void changeBooleanCheckbox() {
        if (this.isStart) {
            this.nameInput = false;
            this.passwdInput = false;
        } else {
            this.nameInput = true;
            this.passwdInput = true;
        }
    }

    public String save() {
        boolean flag;

        if (!this.isStart) {

            flag = InterfaceFactory.getSCSIInterfaceInstance().ModifyInitiatorChap("NONE", "", "");
            Debug.print("set chap off.");

        } else {
            if (this.name.equals("")) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("emptyUser"), res.get("emptyUser")));
                return null;
            }
            if (this.passwd.equals("")) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyPasswd"), res.get("emptyPasswd")));
                return null;
            }
            flag = InterfaceFactory.getSCSIInterfaceInstance().ModifyInitiatorChap("CHAP", this.name, this.passwd);
            Debug.print(" set chap on.");
            Debug.print(" name: " + this.name);
            Debug.print(" passwd: " + this.passwd);
        }
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  res.get("setCHAPFailed"), res.get("setCHAPFailed")));
            Debug.print("set chap " + flag);
            return null;
        }

//        Chap chap = new Chap(isStart, name, passwd);
//        iSCSIGlobalInitiatorData data = SCSISession.getiSCSIGlobalInitiatorDataFromSession();
//        iSCSIGlobalInitiator temp = data.getTempChap();
//        temp.setChap(chap);
        return "scsi_initiator?faces-redirect=true&amp;tabViewActiveIndex=1&amp;accordionPanelActiveIndex=0";
    }
}
