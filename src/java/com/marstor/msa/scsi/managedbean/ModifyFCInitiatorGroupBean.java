/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.scsi.model.RemoteInitiator;
import com.marstor.msa.scsi.model.RemoteInitiatorGroup;
import com.marstor.msa.common.bean.HostGroup;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.marstor.msa.util.InterfaceFactory;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class ModifyFCInitiatorGroupBean {

    private RemoteInitiatorGroup group;
    private RemoteInitiatorGroup newGroup;
    private RemoteInitiatorGroup iscsiGroup;
    private RemoteInitiatorGroup fcGroup;
    private String initiatorName;
    private String groupName = "";
    private String fcConnectedInitiators = "";
    private String selectInitiatorName ="jkl";
    // private RemoteInitiator[] selectedInitiator;
    private RemoteInitiator selectedInitiator = new RemoteInitiator();
    private ArrayList<RemoteInitiator> members = new ArrayList<RemoteInitiator>();
    private HashMap<String,String> initiatorMap = new HashMap<String, String>();
    // private ArrayList<RemoteInitiator> allInitiatorList = new ArrayList<RemoteInitiator>();
    // private ArrayList<RemoteInitiator> includeInitiatorList = new ArrayList<RemoteInitiator>();
    //  private ArrayList<String> memberList = new ArrayList<String>();
    private SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
    // private RemoteInitiatorDataModel dataModel = new RemoteInitiatorDataModel(allInitiatorList);

    public ModifyFCInitiatorGroupBean() {
        //group = SCSISession.getRemoteInitiatorDataFromSession().getGroupTemp();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        groupName = request.getParameter("groupName");
        HostGroup[] fcHostGroups = scsi.getFCHostGroups();
        ArrayList<String> memberList = new ArrayList<String>();
        if (fcHostGroups != null) {
            for (int i = 0; i < fcHostGroups.length; i++) {
                HostGroup group = fcHostGroups[i];
                if (group.getGroupName().equals(this.groupName)) {
                    memberList = group.getGroupMember();
                    break;
                }

            }
        }
        ArrayList<String> connectedInitiatorList = new ArrayList<String>();
        String connectedInitiators[] = scsi.getConnectedInitiator();
        if (connectedInitiators != null) {
            for (int i = 0; i < connectedInitiators.length; i++) {
                Debug.print(" connected Initiator : " + connectedInitiators[i]);
                if (connectedInitiators[i].startsWith("wwn") || connectedInitiators[i].startsWith("eui")) {
                    connectedInitiatorList.add(connectedInitiators[i]);
                }
            }
        }
        if (memberList != null) {
            for (int i = 0; i < memberList.size(); i++) {
                String name = memberList.get(i);
                RemoteInitiator initiator;
                if (connectedInitiatorList.contains(name)) {
                    initiator = new RemoteInitiator(name.toUpperCase(), true);
                } else {
                    initiator = new RemoteInitiator(name.toUpperCase(), false);
                }
                initiatorMap.put(name.toUpperCase(), name);//将FC Initiator名称和转换成大写后的名称存入map中
                members.add(initiator);
            }
        }

//        String members = request.getParameter("members");
//        if (members != null) {
//            String memberArray[] = members.trim().split(",");
//            if (memberArray != null) {
//                for (int i = 0; i < memberArray.length; i++) {
//                    memberList.add(memberArray[i]);
//                }
//
//            }
//        }
//        String allFCInitiators = request.getParameter("allfcInitiatorNames");
//        if (allFCInitiators != null) {
//            String fcInitiatorArray[] = allFCInitiators.trim().split(",");
//            if (fcInitiatorArray != null) {
//                for (int i = 0; i < fcInitiatorArray.length; i++) {
//                    String name = fcInitiatorArray[i];
//                    RemoteInitiator initiator;
//                    if (connectedInitiatorList.contains(name)) {
//                        initiator = new RemoteInitiator(name, true);
//                    } else {
//                        initiator = new RemoteInitiator(name, false);
//                    }
//                    if (memberList.contains(name)) {
//                        includeInitiatorList.add(initiator);
//                    }
//                    allInitiatorList.add(initiator);
//                }
//            }
//        }

        //ArrayList<String> memberList = new ArrayList<String>();

        //ArrayList<RemoteInitiator> allInitiatorList = new ArrayList<RemoteInitiator>();
        //ArrayList<RemoteInitiator> includeInitiatorList = new ArrayList<RemoteInitiator>();



        //   dataModel = new RemoteInitiatorDataModel(allInitiatorList);


        // this.selectedInitiator = (RemoteInitiator[]) includeInitiatorList.toArray(new RemoteInitiator[includeInitiatorList.size()]);
//        iscsiGroup = SCSISession.getRemoteInitiatorDataFromSession().getIscsiTemp();
//        fcGroup = SCSISession.getRemoteInitiatorDataFromSession().getFcTemp();
//      
    }

    public void init() {
        HashMap<String,String> initiatorMap = new HashMap<String, String>();
        ArrayList<RemoteInitiator> initiatorMembers = new ArrayList<RemoteInitiator>();
        HostGroup[] fcHostGroups = scsi.getFCHostGroups();
        ArrayList<String> memberList = new ArrayList<String>();
        if (fcHostGroups != null) {
            for (int i = 0; i < fcHostGroups.length; i++) {
                HostGroup group = fcHostGroups[i];
                if (group.getGroupName().equals(this.groupName)) {
                    memberList = group.getGroupMember();
                    break;
                }

            }
        }
        ArrayList<String> connectedInitiatorList = new ArrayList<String>();
        String connectedInitiators[] = scsi.getConnectedInitiator();
        if (connectedInitiators != null) {
            for (int i = 0; i < connectedInitiators.length; i++) {
                Debug.print(" connected Initiator : " + connectedInitiators[i]);
                if (connectedInitiators[i].startsWith("wwn") || connectedInitiators[i].startsWith("eui")) {
                    connectedInitiatorList.add(connectedInitiators[i]);
                }
            }
        }
        if (memberList != null) {
            for (int i = 0; i < memberList.size(); i++) {
                String name = memberList.get(i);
                RemoteInitiator initiator;
                if (connectedInitiatorList.contains(name)) {
                    initiator = new RemoteInitiator(name.toUpperCase(), true);
                } else {
                    initiator = new RemoteInitiator(name.toUpperCase(), false);
                }
                initiatorMap.put(name.toUpperCase(), name);
                initiatorMembers.add(initiator);
            }
        }
        this.members = initiatorMembers;
        this.initiatorMap = initiatorMap;
    }

    public String getSelectInitiatorName() {
        return selectInitiatorName;
    }

    public void setSelectInitiatorName(String selectInitiatorName) {
        this.selectInitiatorName = selectInitiatorName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public RemoteInitiator getSelectedInitiator() {
        return selectedInitiator;
    }

    public void setSelectedInitiator(RemoteInitiator selectedInitiator) {
        this.selectedInitiator = selectedInitiator;
    }

    public ArrayList<RemoteInitiator> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<RemoteInitiator> members) {
        this.members = members;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public RemoteInitiatorGroup getGroup() {
        return group;
    }

    public void setGroup(RemoteInitiatorGroup group) {
        this.group = group;
    }

    public RemoteInitiatorGroup getNewGroup() {
        return newGroup;
    }

    public void setNewGroup(RemoteInitiatorGroup newGroup) {
        this.newGroup = newGroup;
    }

    public RemoteInitiatorGroup getIscsiGroup() {
        return iscsiGroup;
    }

    public void setIscsiGroup(RemoteInitiatorGroup iscsiGroup) {
        this.iscsiGroup = iscsiGroup;
    }

    public RemoteInitiatorGroup getFcGroup() {
        return fcGroup;
    }

    public void setFcGroup(RemoteInitiatorGroup fcGroup) {
        this.fcGroup = fcGroup;
    }

//    public RemoteInitiatorDataModel getDataModel() {
//        return dataModel;
//    }
//
//    public void setDataModel(RemoteInitiatorDataModel dataModel) {
//        this.dataModel = dataModel;
//    }
//
//    public RemoteInitiator[] getSelectedInitiator() {
//        return selectedInitiator;
//    }
//
//    public void setSelectedInitiator(RemoteInitiator[] selectedInitiator) {
//        this.selectedInitiator = selectedInitiator;
//    }
    public String save() {
//        ArrayList<String> includeInitiators = new ArrayList<String>();
//        for (int i = 0; i < this.selectedInitiator.length; i++) {
//            includeInitiators.add(selectedInitiator[i].getName().toLowerCase());
//        }
//        ArrayList<String> addInitiators = new ArrayList<String>();
//        for (int i = 0; i < includeInitiators.size(); i++) {
//            String initiator = includeInitiators.get(i);
//            if (!this.memberList.contains(initiator)) {
//                addInitiators.add(initiator);
//            }
//        }
//        ArrayList<String> deleteInitiators = new ArrayList<String>();
//        for (int j = 0; j < this.memberList.size(); j++) {
//            if (!includeInitiators.contains(this.memberList.get(j))) {
//                deleteInitiators.add(this.memberList.get(j));
//            }
//        }
//        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//        MSAResource res = new MSAResource();
//        boolean flag;
//        if (addInitiators.size() > 0) {
//            Debug.print(" add ");
//            for (String initiator : addInitiators) {
//                Debug.print(initiator);
//            }
//            flag = scsi.addHostGroupMember(this.groupName, (String[]) addInitiators.toArray(new String[addInitiators.size()]));
//            if (!flag) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("configFailed"), res.get("configFailed")));
//                Debug.print(" modify_fc_group add HostGroup Member " + flag);
//                return null;
//            }
//
//        }
//        if (deleteInitiators.size() > 0) {
//            Debug.print(" del ");
//            for (String initiator : deleteInitiators) {
//                Debug.print(initiator);
//            }
//            flag = scsi.removeHostGroupMember(this.groupName, (String[]) deleteInitiators.toArray(new String[deleteInitiators.size()]));
//            if (!flag) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("configFailed"), res.get("configFailed")));
//                Debug.print(" modify_fc_group remove host group member" + flag);
//                return null;
//            }
//
//        }

        return "scsi_host?faces-redirect=true";
    }

    public String addInitiatorToFCGroup() {
//        if (this.initiatorName.equals("")) {
//            MSAResource res = new MSAResource();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("canNotEmpty"), res.get("canNotEmpty")));
//            return;
//        }
//
//        RemoteInitiator initiator = new RemoteInitiator(this.initiatorName, false);
//        this.allInitiatorList.add(initiator);
//        this.dataModel.setWrappedData(this.allInitiatorList);
//
//        //this.includeInitiatorList.add(initiator);
//        ArrayList<RemoteInitiator> initiators = new ArrayList<RemoteInitiator>();
//        for (int i = 0; i < this.selectedInitiator.length; i++) {
//            initiators.add(this.selectedInitiator[i]);
//        }
//        initiators.add(initiator);
//        this.selectedInitiator = (RemoteInitiator[]) initiators.toArray(new RemoteInitiator[initiators.size()]);
        // String params = "groupName=" + groupName + "&" + "fcConnectedInitiators=" + this.fcConnectedInitiators;
        String params = "groupName=" + groupName;
        Debug.print(" param :" + params);
        return "scsi_add_fc_group_member?faces-redirect=true&amp;" + params;

    }

    public void addInitiatorToiSCSIGroup() {
//        RemoteInitiator initiator = new RemoteInitiator(this.initiatorName, true);
//        initiator.setBelongGroupOrNot(true);
//        this.iscsiGroup.addToAvaileble(initiator);
//        RemoteInitiatorDataModel dataModel = new RemoteInitiatorDataModel(iscsiGroup.getAvalibleInitiators());
//        iscsiGroup.setDataModel(dataModel);
//        List<RemoteInitiator> initiators = new ArrayList<RemoteInitiator>();
//        RemoteInitiator[] selected = iscsiGroup.getSelectedInitiator();
//        for (int i = 0; i < selected.length; i++) {
//            initiators.add(selected[i]);
//        }
//        initiators.add(initiator);
//        iscsiGroup.setSelectedInitiator(initiators.toArray(new RemoteInitiator[initiators.size()]));
    }

    public void deleteInitiator() {
        String array[] = new String[1];
        array[0] = this.initiatorMap.get(this.selectedInitiator.getName()) ;
        boolean flag = scsi.removeHostGroupMember(this.groupName, array);
        MSAResource res = new MSAResource();
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delFailed"), res.get("delFailed")));
            Debug.print(" ModifyfcInitiatorGroup remove: " + flag);
            return;
        }
        this.init();
    }
    public void  confirmDeleteInitiator(RemoteInitiator initiator) {
        this.selectedInitiator = initiator;
        this.selectInitiatorName = initiator.getName();
        //this.selectInitiatorName = "new";
       // RequestContext.getCurrentInstance().update("deleteinitiator");
        RequestContext.getCurrentInstance().execute("deleteinitiator.show();");
        
    }
}
