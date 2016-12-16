package com.marstor.msa.scsi.managedbean; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.bean.iSCSIInitiator;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.scsi.model.RemoteInitiator;
import com.marstor.msa.scsi.validator.ISCSIMemberValidate;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.util.SelectOneItem;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@ViewScoped
public class CreateRemoteInitiator {

    private String name;
    private String style;
    private String groupName;
    private String iSCSIConnectedInitiators = "";
    private ArrayList<String> availableInitiators = new ArrayList<String>();
//    public ArrayList<SelectOneItem> itms = new ArrayList<SelectOneItem>();
//    private SelectOneItem selectitem;

    public CreateRemoteInitiator() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        groupName = request.getParameter("groupName");
        Debug.print("groupName :" + groupName);
        this.iSCSIConnectedInitiators = request.getParameter("iscsiConnectedInitiators");
        ArrayList<String> connectedInitiatorList = new ArrayList<String>();
        if (this.iSCSIConnectedInitiators == null) {
            this.iSCSIConnectedInitiators = "";
        } else {
            String connectedInitiatorArray[] = iSCSIConnectedInitiators.trim().split(",");
            if (connectedInitiatorArray != null) {
                for (int i = 0; i < connectedInitiatorArray.length; i++) {
                    connectedInitiatorList.add(connectedInitiatorArray[i]);
                }
            }
        }
        String allMembers[] = InterfaceFactory.getSCSIInterfaceInstance().getAllMembers();
        // Debug.print("allMembers: ");
        ArrayList<String> initiatorMembers = new ArrayList<String>();
        if (allMembers != null) {
            for (int i = 0; i < allMembers.length; i++) {
                String member = allMembers[i];
                if (member.startsWith("iqn")) {
                    initiatorMembers.add(member);
                }
                //Debug.print( member);
            }
        }
        SelectOneItem item;
        for (int i = 0; i < connectedInitiatorList.size(); i++) {
            if (!initiatorMembers.contains(connectedInitiatorList.get(i))) {
                // RemoteInitiator initiator = new RemoteInitiator(connectedInitiatorList.get(i), true);
                this.availableInitiators.add(connectedInitiatorList.get(i));
//                item = new SelectOneItem(connectedInitiatorList.get(i), 45);
//                this.itms.add(item);
            }
        }
//        item= new SelectOneItem("iqn.2013-12.com.marstor:1385970203877.initiator");
//        this.itms.add(item);
        
    }

//    public ArrayList<SelectOneItem> getItms() {
//        return itms;
//    }
//
//    public void setItms(ArrayList<SelectOneItem> itms) {
//        this.itms = itms;
//    }
//
//    public SelectOneItem getSelectitem() {
//        return selectitem;
//    }
//
//    public void setSelectitem(SelectOneItem selectitem) {
//        Debug.print(" setSelectitem() :" + selectitem.toString());
//        this.selectitem = selectitem;
//    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<String> getAvailableInitiators() {
        return availableInitiators;
    }

    public void setAvailableInitiators(ArrayList<String> availableInitiators) {
        this.availableInitiators = availableInitiators;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return this.style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String save() {
//        Debug.print("save()");
//        this.name = this.selectitem.getValue().toString();
//        Debug.print(" this.selectitem.toString() : " + this.selectitem.toString());
//        Debug.print(" this.selectitem.getValue().toString() : " + this.selectitem.getValue().toString());
//        Debug.print(" this.selectitem.getLabel() : " + this.selectitem.getLabel());
//        Debug.print("this.selectitem.getTooltip() : " + this.selectitem.getTooltip());
//        
        MSAResource res = new MSAResource();
        if (this.name == null || this.name.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyName"), res.get("emptyName")));
            return null;
        }
        if (this.name.length() > 64) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("length"), res.get("length")));
            return null;
        }
        if (!(ISCSIMemberValidate.checkIQN(this.name))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("invalidName"), res.get("invalidName")));
            return null;
        }
        //获取所有initiator
        iSCSIInitiator initiators[] = InterfaceFactory.getSCSIInterfaceInstance().getiSCSIInitiator();
        for (iSCSIInitiator initiator : initiators) {
            if (initiator.getName().equals(this.name)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("existName"), res.get("existName")));
                return null;
            }
        }
        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().createiSCSIInitiator(this.name);
        if (!(flag)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addFailed"), res.get("addFailed")));
            Debug.print(" CreateRemoteInitiator " + flag);
            return null;
        }
        String array[] = new String[1];
        array[0] = this.name;
        flag = InterfaceFactory.getSCSIInterfaceInstance().addHostGroupMember(this.groupName, array);
        Debug.print(" add member param:");
        Debug.print("group name: " + this.groupName);
        for (int i = 0; i < array.length; i++) {
            Debug.print(" initiator: " + array[i]);
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addMemberFailed"), res.get("addMemberFailed")));
            Debug.print(" ModifyiSCSIInitiatorGroup add: " + flag);
            return null;
        }
        String params = "groupName=" + groupName + "&" + "iscsiConnectedInitiators=" + this.iSCSIConnectedInitiators;
        return "scsi_update_remote_iscsi_group?faces-redirect=true&amp;" + params;
    }

    public void update() {
        ArrayList<String> connectedInitiatorList = new ArrayList<String>();
        String connectedInitiators[] = InterfaceFactory.getSCSIInterfaceInstance().getConnectedInitiator();
        if (connectedInitiators != null) {
            for (int i = 0; i < connectedInitiators.length; i++) {
                Debug.print("connected Initiator : " + connectedInitiators[i]);
                if (connectedInitiators[i].startsWith("iqn")) {
                    connectedInitiatorList.add(connectedInitiators[i]);
                }
            }
        }
        String allMembers[] = InterfaceFactory.getSCSIInterfaceInstance().getAllMembers();
        // Debug.print("allMembers: ");
        ArrayList<String> initiatorMembers = new ArrayList<String>();
        if (allMembers != null) {
            for (int i = 0; i < allMembers.length; i++) {
                String member = allMembers[i];
                if (member.startsWith("iqn")) {
                    initiatorMembers.add(member);
                }
                //Debug.print(" member);
            }
        }
        ArrayList<String> availableInitiatorList = new ArrayList<String>();
        for (int i = 0; i < connectedInitiatorList.size(); i++) {
            if (!initiatorMembers.contains(connectedInitiatorList.get(i))) {
                // RemoteInitiator initiator = new RemoteInitiator(connectedInitiatorList.get(i), true);
                availableInitiatorList.add(connectedInitiatorList.get(i));
            }
        }
        this.availableInitiators = availableInitiatorList;
    }

    public String cancel() {
        String params = "groupName=" + groupName + "&" + "iscsiConnectedInitiators=" + this.iSCSIConnectedInitiators;
        return "scsi_update_remote_iscsi_group?faces-redirect=true&amp;" + params;
    }
}