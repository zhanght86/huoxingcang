/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean; import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.bean.iSCSIInitiator;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.scsi.model.RemoteInitiator;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.marstor.msa.util.InterfaceFactory;
import javax.faces.application.FacesMessage;
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
public class ConfigCHAPForRemoteInitiator {

    private boolean isStart;
    private String name;
    private String passwd;
    private boolean nameInput;
    private boolean passwdInput;
    private String initiatorName;
    private String iSCSIConnectedInitiators = "";
    private String groupName = "";

    public ConfigCHAPForRemoteInitiator() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        String startCHAPOrNot = request.getParameter("startCHAPOrNot");
        initiatorName = request.getParameter("initiatorName");
        this.iSCSIConnectedInitiators = request.getParameter("iscsiConnectedInitiators");
        if(this.iSCSIConnectedInitiators==null) {
            this.iSCSIConnectedInitiators ="";
        }
        groupName = request.getParameter("groupName");
        // Debug.print(startCHAPOrNot);
        //startCHAPOrNot
        if (startCHAPOrNot != null) {
            if (startCHAPOrNot.equals("1")) {
                this.isStart = true;
                this.name = request.getParameter("userName");
            } else {
                this.isStart = false;
                this.name = "";
            }
        }


//       RemoteInitiatorData data =  SCSISession.getRemoteInitiatorDataFromSession();
//
//       RemoteInitiator temp = data.getTemp();
//       isStart = temp.getChap().isIsStart();
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

        if (this.isStart && this.name.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyUser"), res.get("emptyUser")));
            return null;
        }
        if (this.isStart && this.passwd.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyPasswd"), res.get("emptyPasswd")));
            return null;
        }
        Debug.print("username :" + this.name + "  passwd :" + passwd);
        if (!this.isStart) {
            this.name = "<none>";
            this.passwd = "";
        }
        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().setiSCSIInitiatorChap(name, passwd, this.initiatorName);
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setCHAPFailed"), res.get("setCHAPFailed")));
            Debug.print("set chap " + flag);
            return null;
        }
//        iSCSIInitiator initiators[] = InterfaceFactory.getSCSIInterfaceInstance().getiSCSIInitiator();
//        if (initiators != null) {
//            for (int i = 0; i < initiators.length; i++) {
//                iSCSIInitiator iscsi = initiators[i];
//                 Debug.print(iscsi.getName());
//            }
//        }
//        Chap  chap = new Chap(isStart, name, passwd);
//        RemoteInitiatorData data =  SCSISession.getRemoteInitiatorDataFromSession();
//      
//       RemoteInitiator temp = data.getTemp();
//       temp.setChap(chap);
        String param = "groupName=" + groupName + "&" + "iscsiConnectedInitiators=" + this.iSCSIConnectedInitiators;
        return "scsi_update_remote_iscsi_group?faces-redirect=true&amp;" + param;

    }

    public String cancel() {
        String param = "groupName=" + groupName + "&" + "iscsiConnectedInitiators=" + this.iSCSIConnectedInitiators;
        return "scsi_update_remote_iscsi_group?faces-redirect=true&amp;" + param;
    }
}
