/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.bean.HostGroup;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class CreateFCInitiatorGroup {

    private String groupName;
   // private String url="";

    public CreateFCInitiatorGroup() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
//        url = request.getParameter("url");
//        if(url==null || url.equals("")) {
//            url= "scsi_host";
//        }
        groupName = "fc_";
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String save() {
        MSAResource res = new MSAResource();
        Debug.print("group: " + groupName);
        if (this.groupName.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("canNotEmpty"), res.get("canNotEmpty")));
            return null;
        }
        HostGroup[] fcHostGroups = InterfaceFactory.getSCSIInterfaceInstance().getFCHostGroups();
        for (HostGroup group : fcHostGroups) {
            if (group.getGroupName().equals(this.groupName)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("existGroup"), res.get("existGroup")));
                return null;
            }
        }
        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().createHostGroup(this.groupName);
        if (!flag) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addFailed"), res.get("addFailed")));
            return null;
        }
//       RemoteInitiatorData data =  SCSISession.getRemoteInitiatorDataFromSession();
//       data.addFCGroup(groupName);
      //  return "scsi_host?faces-redirect=true";
        String params = "groupName=" + this.groupName ;
          return "scsi_add_fc_group_member?faces-redirect=true&amp;" + params;
    }

    public void changeValue() {
        if (!this.groupName.startsWith("fc_")) {
            this.groupName = "fc_";
        }
    }

    public void validatorGroupName() {
        MSAResource resource = new MSAResource();
        if (this.groupName.equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, resource.get("canNotEmpty"), resource.get("canNotEmpty"));
            throw new ValidatorException(message);
        }
    }
//    public String goBack() {
//        return this.url + "?faces-redirect=true";
//    }
}
