/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean;

import com.marstor.msa.common.bean.HostGroup;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
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
public class CreateiSCSIInitiatorGroup {

    private String groupName;

    public CreateiSCSIInitiatorGroup() {
        groupName = "iscsi_";
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String save() {
        
        if (this.groupName.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyName"), res.get("emptyName")));
            return null;
        }
         HostGroup[] iscsiHostGroups = InterfaceFactory.getSCSIInterfaceInstance().getiSCSIHostGroups();
         for (HostGroup group : iscsiHostGroups) {
            if (group.getGroupName().equals(this.groupName)) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("existGroup"), res.get("existGroup")));
                return null;
            }
        }
        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().createHostGroup(this.groupName);
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  res.get("addFailed"), res.get("addFailed")));
            return null;
        }


//       RemoteInitiatorData data =  SCSISession.getRemoteInitiatorDataFromSession();
//       data.addiSCSIGroup(groupName);
       // return "scsi_host?faces-redirect=true&amp;activeIndex=1";
        String param = "groupName=" + this.groupName;
        return "scsi_create_remote_initiator?faces-redirect=true&amp;" + param;
    }
}
