/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean;

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.bean.TargetInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.scsi.model.Chap;
import com.marstor.msa.scsi.model.ISCSITarget;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
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
//@RequestScoped
//火星舱作为Target时 进行的chap验证
public class ConfigTargetiSCSICHAPBean implements Serializable{

    private boolean isStart;
    private boolean authenInfo;
    private String name;
    private String passwd;
    private boolean nameInput;
    private boolean passwdInput;
    private boolean authenInfoInput;
    private String targetName;

    public ConfigTargetiSCSICHAPBean() {
        //ISCSITargetData data = SCSISession.getISCSITargetDataFromSession();
        //ISCSITarget temp = ISCSITargetData.getTemp();
//        isStart = temp.getChap().isIsStart();
//        authenInfo = temp.getChap().isAuthenInfo();
//        name = temp.getChap().getuName();
//        passwd = temp.getChap().getuPasswd();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        //String targetName = request.getParameter("name");
        if (request.getParameter("targetName") == null || request.getParameter("authType") == null || request.getParameter("startOrNot") == null || request.getParameter("userName") == null) {
            return;
        }
        this.targetName = request.getParameter("targetName");
        if (Integer.parseInt(request.getParameter("authType")) == TargetInformation.NONE) {
            isStart = false;
        } else {
            isStart = true;
        }
        if (isStart && request.getParameter("startOrNot").equals("1")) {
            authenInfo = true;
        } else {
            authenInfo = false;
        }
        //isStart = temp.isIsStartChap();
        //authenInfo = temp.getChap().isAuthenInfo();

        //name = temp.getTargetInfo().getChapUser();
        //passwd = temp.getTargetInfo().getChapSecret();
        if (authenInfo) {
            name = request.getParameter("userName");
        } else {
            name = "";
        }


        if (isStart) {
            if (this.authenInfo) {
                this.nameInput = false;
                this.passwdInput = false;
            } else {
                this.nameInput = true;
                this.passwdInput = true;
            }
            this.authenInfoInput = false;
        } else {
            this.nameInput = true;
            this.passwdInput = true;
            this.authenInfoInput = true;

        }
        // this.changeAuthenInfoCheckbox();
    }

    public boolean isIsStart() {
        return isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public boolean isAuthenInfo() {
        return authenInfo;
    }

    public void setAuthenInfo(boolean authenInfo) {
        this.authenInfo = authenInfo;
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

    public boolean isAuthenInfoInput() {
        return authenInfoInput;
    }

    public void setAuthenInfoInput(boolean authenInfoInput) {
        this.authenInfoInput = authenInfoInput;
    }

    public void changeStartCheckbox() {
        if (this.isStart) {
//            if (this.authenInfo) {
//                this.nameInput = false;
//                this.passwdInput = false;
//            } else {
//                this.passwdInput = true;
//                this.nameInput = true;
//            }
            this.authenInfoInput = false;
        } else {
//            this.nameInput = true;
//            this.passwdInput = true;
            this.authenInfoInput = true;

        }
        this.nameInput = true;
        this.passwdInput = true;

        this.authenInfo = false;
    }

    public void changeAuthenInfoCheckbox() {
        if (this.authenInfo) {
            this.nameInput = false;
            this.passwdInput = false;
        } else {
            this.nameInput = true;
            this.passwdInput = true;
        }
    }

    public String save() {

        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        Debug.print(this.targetName);
        boolean flag;
        String cancelPasswd = "111111111111";
        if (this.isStart) {
            if (this.authenInfo) {
                if (this.name.equals("")) {
                    MSAResource res = new MSAResource();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyUser"), res.get("emptyUser")));
                    return null;
                }
                if (this.passwd.equals("")) {
                    MSAResource res = new MSAResource();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyPasswd"), res.get("emptyPasswd")));
                    return null;
                }
                if (this.passwd.length()<12 || this.passwd.length()>20) {
                    MSAResource res = new MSAResource();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("invalidPasswd"), res.get("invalidPasswd")));
                    return null;
                }
                flag = scsi.setTargetCHAP(TargetInformation.CHAP, this.targetName);
                if (!flag) {
                    MSAResource res = new MSAResource();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setCHAPFailed"), res.get("setCHAPFailed")));
                    Debug.print("setTargetCHAP " + flag);
                    return null;
                }
                flag = scsi.setTargetPassword(name, this.passwd, this.targetName);
                if (!flag) {
                    MSAResource res = new MSAResource();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setCHAPFailed"), res.get("setCHAPFailed")));
                    Debug.print(" setTargetPasswd " + flag);
                    return null;
                }

            } else {
                flag = scsi.setTargetCHAP(TargetInformation.CHAP, this.targetName);
                if (!flag) {
                    MSAResource res = new MSAResource();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setCHAPFailed"), res.get("setCHAPFailed")));
                    Debug.print("setTargetCHAP " + flag);
                    return null;
                }
                flag = scsi.setTargetPassword("none", cancelPasswd, this.targetName);
                if (!flag) {
                    MSAResource res = new MSAResource();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setCHAPFailed"), res.get("setCHAPFailed")));
                    Debug.print("setTargetPasswd " + flag);
                    return null;
                }
            }

        } else {
            flag = scsi.setTargetCHAP(TargetInformation.NONE, this.targetName);
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setCHAPFailed"), res.get("setCHAPFailed")));
                Debug.print("setTargetCHAP " + flag);
                return null;
            }
            flag = scsi.setTargetPassword("none", cancelPasswd, this.targetName);
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setCHAPFailed"), res.get("setCHAPFailed")));
                Debug.print("setTargetPasswd " + flag);
                return null;
            }

        }
//        
//        Chap chap = new Chap(isStart, name, passwd, authenInfo);
//        ISCSITargetData data = SCSISession.getISCSITargetDataFromSession();
//        ISCSITarget temp = data.getTemp();
//        temp.setChap(chap);
        return "scsi_target?faces-redirect=true&amp;activeIndex=1";
    }
}
