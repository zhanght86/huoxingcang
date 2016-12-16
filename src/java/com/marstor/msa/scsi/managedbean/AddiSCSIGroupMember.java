/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean;

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.bean.iSCSIInitiator;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.scsi.model.RemoteInitiator;
import com.marstor.msa.scsi.model.RemoteInitiatorDataModel;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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
public class AddiSCSIGroupMember {

    private ArrayList<RemoteInitiator> availableInitiator = new ArrayList<RemoteInitiator>();
    private RemoteInitiatorDataModel data = new RemoteInitiatorDataModel(availableInitiator);
    private SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
    private RemoteInitiator[] selectedInitiator;
    private String iSCSIConnectedInitiators = "";
    private String groupName = "";

    public AddiSCSIGroupMember() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ArrayList<String> connectedInitiatorList = new ArrayList<String>();
        groupName = request.getParameter("groupName");
        this.iSCSIConnectedInitiators = request.getParameter("iscsiConnectedInitiators");
        if (iSCSIConnectedInitiators != null) {
            String iSCSIConnectedInitiatorArray[] = iSCSIConnectedInitiators.trim().split(",");

            if (iSCSIConnectedInitiatorArray != null) {
                for (int i = 0; i < iSCSIConnectedInitiatorArray.length; i++) {
                    connectedInitiatorList.add(iSCSIConnectedInitiatorArray[i]);
                }
            }
        }
        String allMembers[] = scsi.getAllMembers();
        ArrayList<String> allMemberList = new ArrayList<String>();
        for (int i = 0; i < allMembers.length; i++) {
            if (allMembers[i].startsWith("iqn")) {
                allMemberList.add(allMembers[i]);
            }
        }
        iSCSIInitiator initiators[] = scsi.getiSCSIInitiator();
        if (initiators != null) {
            for (int i = 0; i < initiators.length; i++) {
                iSCSIInitiator iscsi = initiators[i];
                if (!allMemberList.contains(iscsi.name)) {
                    RemoteInitiator initiator;
                    if (connectedInitiatorList.contains(iscsi.getName())) {
                        initiator = new RemoteInitiator(iscsi.getName(), iscsi.getUsername(), iscsi.getPassword(), true);
                    } else {
                        initiator = new RemoteInitiator(iscsi.getName(), iscsi.getUsername(), iscsi.getPassword(), false);
                    }
                    this.availableInitiator.add(initiator);
                }
//                Debug.print( iscsi.getName());
            }
        }
        data = new RemoteInitiatorDataModel(availableInitiator);

    }

    public RemoteInitiator[] getSelectedInitiator() {
        return selectedInitiator;
    }

    public void setSelectedInitiator(RemoteInitiator[] selectedInitiator) {
        this.selectedInitiator = selectedInitiator;
    }

    public ArrayList<RemoteInitiator> getAvailableInitiator() {
        return availableInitiator;
    }

    public void setAvailableInitiator(ArrayList<RemoteInitiator> availableInitiator) {
        this.availableInitiator = availableInitiator;
    }

    public RemoteInitiatorDataModel getData() {
        return data;
    }

    public void setData(RemoteInitiatorDataModel data) {
        this.data = data;
    }

    public String save() {
        ArrayList<String> initiators = new ArrayList<String>();
        for (int i = 0; i < this.selectedInitiator.length; i++) {
            initiators.add(selectedInitiator[i].getName());
        }
        if (initiators.size() > 0) {
            boolean flag = scsi.addHostGroupMember(this.groupName, (String[]) initiators.toArray(new String[initiators.size()]));
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addMemberFailed"), res.get("addMemberFailed")));
                Debug.print("ModifyiSCSIInitiatorGroup add: " + flag);
                return null;
            }
        }

        String params = "groupName=" + this.groupName + "&" + "iscsiConnectedInitiators=" + this.iSCSIConnectedInitiators;
//        Debug.print(" param :" + params);
        return "scsi_update_remote_iscsi_group?faces-redirect=true&amp;" + params;
    }

    public String cancel() {
        String params = "groupName=" + this.groupName + "&" + "iscsiConnectedInitiators=" + this.iSCSIConnectedInitiators;
//        Debug.print("param :" + params);
        return "scsi_update_remote_iscsi_group?faces-redirect=true&amp;" + params;
    }
}
