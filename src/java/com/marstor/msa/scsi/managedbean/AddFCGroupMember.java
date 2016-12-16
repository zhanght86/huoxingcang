/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean; 
import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.bean.FCInitiatorInformation;
import com.marstor.msa.common.bean.iSCSIInitiator;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.scsi.model.RemoteInitiator;
import com.marstor.msa.scsi.model.RemoteInitiatorDataModel;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.HashMap;
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
public class AddFCGroupMember {

    // private ArrayList<RemoteInitiator> availableInitiator = new ArrayList<RemoteInitiator>();
    private ArrayList<String> availableInitiators = new ArrayList<String>();
    // private RemoteInitiatorDataModel data = new RemoteInitiatorDataModel(availableInitiator);
    private SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
    private RemoteInitiator[] selectedInitiator;
    private String fcConnectedInitiators = "";
    private String groupName = "";
    private String selectName = "";
    private HashMap<String,String> initiatorMap = new HashMap<String, String>();
    //private String url = "";

    public AddFCGroupMember() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        // ArrayList<String> connectedInitiatorList = new ArrayList<String>();
        groupName = request.getParameter("groupName");
        //url = request.getParameter("url");
//        this.fcConnectedInitiators = request.getParameter("fcConnectedInitiators");
//        Debug.print(" fcConn:" + this.fcConnectedInitiators);
//        if (fcConnectedInitiators != null) {
//            String fcConnectedInitiatorArray[] = fcConnectedInitiators.trim().split(",");
//            if (fcConnectedInitiatorArray != null) {
//                for (int i = 0; i < fcConnectedInitiatorArray.length; i++) {
//                    connectedInitiatorList.add(fcConnectedInitiatorArray[i]);
//                }
//            }
//        } else {
//            this.fcConnectedInitiators = "";
//        }
        ArrayList<String> connectedInitiatorList = new ArrayList<String>();
        String connectedInitiators[] = scsi.getConnectedInitiator();
        if (connectedInitiators != null) {
            for (int i = 0; i < connectedInitiators.length; i++) {
                Debug.print("connected Initiator : " + connectedInitiators[i]);
                if (connectedInitiators[i].startsWith("wwn") || connectedInitiators[i].startsWith("eui")) {
                    connectedInitiatorList.add(connectedInitiators[i]);
                }
            }
        }

        String allMembers[] = scsi.getAllMembers();
        ArrayList<String> allMemberList = new ArrayList<String>();
        for (int i = 0; i < allMembers.length; i++) {
            // member.startsWith("wwn") || member.startsWith("eui")
            if (allMembers[i].startsWith("wwn") || allMembers[i].startsWith("eui")) {
                allMemberList.add(allMembers[i]);
            }
        }
//        Debug.print("allfcMember: " );
//        for (int i = 0; i < allMemberList.size(); i++) {
//            Debug.print(allMemberList.get(i));
//        }
        for (int i = 0; i < connectedInitiatorList.size(); i++) {
            if (!allMemberList.contains(connectedInitiatorList.get(i))) {
                //RemoteInitiator initiator = new RemoteInitiator(connectedInitiatorList.get(i), true);
                // this.availableInitiator.add(initiator);
                this.initiatorMap.put(connectedInitiatorList.get(i).toUpperCase(), connectedInitiatorList.get(i));
                this.availableInitiators.add(connectedInitiatorList.get(i).toUpperCase());
            }
        }
        if (this.availableInitiators.size() == 0) {
            this.availableInitiators.add("");
        }
        this.selectName = this.availableInitiators.get(0);
//        Debug.print("allfc avail Member: " );
//        for (int i = 0; i < availableInitiator.size(); i++) {
//            Debug.print( availableInitiator.get(i));
//        }
        //data = new RemoteInitiatorDataModel(availableInitiator);

    }

//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSelectName() {
        return selectName;
    }

    public void setSelectName(String selectName) {
        this.selectName = selectName;
    }

    public ArrayList<String> getAvailableInitiators() {
        return availableInitiators;
    }

    public void setAvailableInitiators(ArrayList<String> availableInitiators) {
        this.availableInitiators = availableInitiators;
    }

    public RemoteInitiator[] getSelectedInitiator() {
        return selectedInitiator;
    }

    public void setSelectedInitiator(RemoteInitiator[] selectedInitiator) {
        this.selectedInitiator = selectedInitiator;
    }

//    public ArrayList<RemoteInitiator> getAvailableInitiator() {
//        return availableInitiator;
//    }
//
//    public void setAvailableInitiator(ArrayList<RemoteInitiator> availableInitiator) {
//        this.availableInitiator = availableInitiator;
//    }
//
//    public RemoteInitiatorDataModel getData() {
//        return data;
//    }
//
//    public void setData(RemoteInitiatorDataModel data) {
//        this.data = data;
//    }
    public String save() {
        if (this.selectName == null || this.selectName.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("nameCannotEmpty"), res.get("nameCannotEmpty")));
            return null;
        }
        ArrayList<String> initiators = new ArrayList<String>();
//        for (int i = 0; i < this.selectedInitiator.length; i++) {
//            initiators.add(selectedInitiator[i].getName());
//        }
        String  initiator = this.initiatorMap.get(this.selectName);
        if(initiator!=null) {
            initiators.add(initiator);
        }
        if (initiators.size() > 0) {
            boolean flag = scsi.addHostGroupMember(this.groupName, (String[]) initiators.toArray(new String[initiators.size()]));
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addMemberFailed"), res.get("addMemberFailed")));
                Debug.print("ModifyFCInitiatorGroup add: " + flag);
                return null;
            }
        }

        //String params = "groupName=" + this.groupName + "&" + "fcConnectedInitiators=" + this.fcConnectedInitiators;
        String params = "groupName=" + this.groupName;
        //        Debug.print("param :" + params);
        return "scsi_update_remote_fc_group?faces-redirect=true&amp;" + params;
    }

    public void update() {
        ArrayList<String> connectedInitiatorList = new ArrayList<String>();
        ArrayList<String> availableInitiatorList = new ArrayList<String>();
        this.initiatorMap = new HashMap<String, String>();
        String connectedInitiators[] = scsi.getConnectedInitiator();
        if (connectedInitiators != null) {
            for (int i = 0; i < connectedInitiators.length; i++) {
                Debug.print("connected Initiator : " + connectedInitiators[i]);
                if (connectedInitiators[i].startsWith("wwn") || connectedInitiators[i].startsWith("eui")) {
                    connectedInitiatorList.add(connectedInitiators[i]);
                }
            }
        }
        String allMembers[] = scsi.getAllMembers();
        ArrayList<String> allMemberList = new ArrayList<String>();
        for (int i = 0; i < allMembers.length; i++) {
            // member.startsWith("wwn") || member.startsWith("eui")
            if (allMembers[i].startsWith("wwn") || allMembers[i].startsWith("eui")) {
                allMemberList.add(allMembers[i]);
            }
        }
        for (int i = 0; i < connectedInitiatorList.size(); i++) {
            if (!allMemberList.contains(connectedInitiatorList.get(i))) {
                //RemoteInitiator initiator = new RemoteInitiator(connectedInitiatorList.get(i), true);
                // this.availableInitiator.add(initiator);
                initiatorMap.put(connectedInitiatorList.get(i).toUpperCase(), connectedInitiatorList.get(i)); //将FC Initiator名称和转换成大写后的名称存入map中
                availableInitiatorList.add(connectedInitiatorList.get(i).toUpperCase());
            }
        }
        this.availableInitiators = availableInitiatorList;
        if (this.availableInitiators.size() == 0) {
            this.availableInitiators.add("");
        }
        this.selectName = this.availableInitiators.get(0);

    }

    public String cancel() {
        //String params = "groupName=" + this.groupName + "&" + "fcConnectedInitiators=" + this.fcConnectedInitiators;
        String params = "groupName=" + this.groupName;
        //        Debug.print("param :" + params);
        return "scsi_update_remote_fc_group?faces-redirect=true&amp;" + params;
    }
}
