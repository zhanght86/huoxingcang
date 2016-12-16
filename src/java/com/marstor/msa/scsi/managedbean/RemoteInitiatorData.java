/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean;

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.scsi.model.RemoteInitiator;
import com.marstor.msa.scsi.model.Chap;
import com.marstor.msa.scsi.model.RemoteInitiatorGroup;
import com.marstor.msa.common.bean.HostGroup;
import com.marstor.msa.common.bean.LuInformation;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.bean.iSCSIInitiator;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import java.util.ArrayList;
import java.util.List;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.Arrays;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
//客户端组
public class RemoteInitiatorData implements Serializable {

    private List<RemoteInitiator> initiators = new ArrayList<RemoteInitiator>();
    private ArrayList<RemoteInitiator> fcInitiators = new ArrayList<RemoteInitiator>();
    private ArrayList<RemoteInitiator> iscsiInitiators = new ArrayList<RemoteInitiator>();
    private RemoteInitiatorGroup groupTemp;
    private RemoteInitiatorGroup selectIscsiGroup;
    private RemoteInitiatorGroup selectFCGroup;
    //private InitiatorGroup  fcGroup;
    //private InitiatorGroup iscsiGroup;
    private RemoteInitiator temp;
    private ArrayList<RemoteInitiatorGroup> groups = new ArrayList<RemoteInitiatorGroup>();
    private ArrayList<RemoteInitiatorGroup> fcGroups = new ArrayList<RemoteInitiatorGroup>();
    private ArrayList<RemoteInitiatorGroup> iscsiGroups = new ArrayList<RemoteInitiatorGroup>();
    private RemoteInitiator[] selectedInitiator;
    private ArrayList<String> fcInitiatorNames = new ArrayList<String>();
    private ArrayList<String> iscsiInitiatorNames = new ArrayList<String>();
    private String allfcInitiatorNames = "", alliscsiInitiatorNames = "";
    private ArrayList<String> fcConnectedInitiators = new ArrayList<String>();
    private ArrayList<String> iscsiConnectedInitiators = new ArrayList<String>();
    private String groupName = "";
    private String activeIndex = "";
    private ViewInformation[] selectGroupView;

    public RemoteInitiatorData() {
        //   selectedInitiator = (RemoteInitiator[]) initiators.toArray();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        activeIndex = request.getParameter("activeIndex");
        if (activeIndex == null || activeIndex.equals("")) {
            activeIndex = "0";
        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        HostGroup[] fcHostGroups = scsi.getFCHostGroups();
        if (fcHostGroups != null) {
            for (int i = 0; i < fcHostGroups.length; i++) {
                HostGroup group = fcHostGroups[i];
                ArrayList<String> members = new ArrayList<String>();
                if (group != null) {
                    if (group.getGroupMember() != null) {
                        members = group.getGroupMember();
                    }
                    RemoteInitiatorGroup initiatorGroup = new RemoteInitiatorGroup(group.getGroupName(), members);
                    this.fcGroups.add(initiatorGroup);
                }

            }
        }

        HostGroup[] iscsiHostGroups = scsi.getiSCSIHostGroups();
        if (iscsiHostGroups != null) {
            for (int i = 0; i < iscsiHostGroups.length; i++) {
                HostGroup group = iscsiHostGroups[i];
                ArrayList<String> members = new ArrayList<String>();
                members = group.getGroupMember();
                RemoteInitiatorGroup initiatorGroup = new RemoteInitiatorGroup(group.getGroupName(), members);
                this.iscsiGroups.add(initiatorGroup);
            }
        }

//        String allMembers[] = scsi.getAllMembers();
//        Debug.print("allMembers: ");
//        if (allMembers != null) {
//            for (int i = 0; i < allMembers.length; i++) {
//                String member = allMembers[i];
//                if (member.startsWith("wwn") || member.startsWith("eui")) {
//                    this.fcInitiatorNames.add(member);
//                } else if (member.startsWith("iqn")) {
//                    this.iscsiInitiatorNames.add(member);
//                }
//                Debug.print( member);
//            }
//        }
//        for (int i = 0; i < this.fcInitiatorNames.size(); i++) {
//            if (i + 1 < fcInitiatorNames.size()) {
//                this.allfcInitiatorNames = this.allfcInitiatorNames + fcInitiatorNames.get(i) + ",";
//            } else {
//                this.allfcInitiatorNames = this.allfcInitiatorNames + fcInitiatorNames.get(i);
//            }
//        }
//        for (int i = 0; i < this.iscsiInitiatorNames.size(); i++) {
//            if (i + 1 < iscsiInitiatorNames.size()) {
//                this.alliscsiInitiatorNames = this.alliscsiInitiatorNames + iscsiInitiatorNames.get(i) + ",";
//            } else {
//                this.alliscsiInitiatorNames = this.alliscsiInitiatorNames + iscsiInitiatorNames.get(i);
//            }
//        }
        String connectedInitiators[] = scsi.getConnectedInitiator();
        if (connectedInitiators != null) {
            for (int i = 0; i < connectedInitiators.length; i++) {
                Debug.print(" connected Initiator : " + connectedInitiators[i]);
                if (connectedInitiators[i].startsWith("wwn") || connectedInitiators[i].startsWith("eui")) {
                    this.fcConnectedInitiators.add(connectedInitiators[i]);
                } else if (connectedInitiators[i].startsWith("iqn")) {
                    this.iscsiConnectedInitiators.add(connectedInitiators[i]);
                }
            }
        }

        iSCSIInitiator initiators[] = scsi.getiSCSIInitiator();
        if (initiators != null) {
//            if (initiators.length > 0) {
//                this.alliscsiInitiatorNames = this.alliscsiInitiatorNames + ",";
//            }
            for (int i = 0; i < initiators.length; i++) {
                iSCSIInitiator iscsi = initiators[i];
                RemoteInitiator initiator;
                if (this.iscsiConnectedInitiators.contains(iscsi.getName())) {
                    initiator = new RemoteInitiator(iscsi.getName(), iscsi.getUsername(), iscsi.getPassword(), true);
                } else {
                    initiator = new RemoteInitiator(iscsi.getName(), iscsi.getUsername(), iscsi.getPassword(), false);
                }
                Debug.print( iscsi.getName());
                this.iscsiInitiators.add(initiator);
//                if (!this.alliscsiInitiatorNames.contains(iscsi.getName())) {
//                    if (i + 1 < initiators.length) {
//                        this.alliscsiInitiatorNames = this.alliscsiInitiatorNames + iscsi.getName() + ",";
//                    } else {
//                        this.alliscsiInitiatorNames = this.alliscsiInitiatorNames + iscsi.getName();
//                    }
//                }

            }
        }
        // print();
    }

    public String getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(String activeIndex) {
        this.activeIndex = activeIndex;
    }

    public void print() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        HostGroup[] iscsiHostGroups = scsi.getiSCSIHostGroups();
        if (iscsiHostGroups != null) {
            for (int i = 0; i < iscsiHostGroups.length; i++) {
                HostGroup group = iscsiHostGroups[i];
                //  this.selectedIP = (IPBinding[]) bindedIPList.toArray(new IPBinding[bindedIPList.size()]);
                // ArrayList<String> member = (ArrayList<String>) Arrays.asList(group.getGroupMember());
                ArrayList<String> members = new ArrayList<String>();
                members = group.getGroupMember();
                Debug.print("GroupName:" + group.getGroupName());
                if (members != null) {
                    for (int j = 0; j < members.size(); j++) {
                        Debug.print("member:" + members.get(j));
                    }
                }
            }
        }
        Debug.print("ALLmember:");
        String allMembers[] = scsi.getAllMembers();
        if (allMembers != null) {
            for (int i = 0; i < allMembers.length; i++) {
                String member = allMembers[i];
                Debug.print("member:" + member);
            }
        }

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public RemoteInitiatorGroup getSelectIscsiGroup() {
        return selectIscsiGroup;
    }

    public void setSelectIscsiGroup(RemoteInitiatorGroup selectIscsiGroup) {
        this.selectIscsiGroup = selectIscsiGroup;
    }

    public RemoteInitiatorGroup getSelectFCGroup() {
        return selectFCGroup;
    }

    public void setSelectFCGroup(RemoteInitiatorGroup selectFCGroup) {
        this.selectFCGroup = selectFCGroup;
    }

    public ArrayList<RemoteInitiatorGroup> getFcGroups() {
        return fcGroups;
    }

    public void setFcGroups(ArrayList<RemoteInitiatorGroup> fcGroups) {
        this.fcGroups = fcGroups;
    }

    public ArrayList<RemoteInitiatorGroup> getIscsiGroups() {
        return iscsiGroups;
    }

    public void setIscsiGroups(ArrayList<RemoteInitiatorGroup> iscsiGroups) {
        this.iscsiGroups = iscsiGroups;
    }

    public RemoteInitiatorGroup getGroupTemp() {
        return groupTemp;
    }

    public void setGroupTemp(RemoteInitiatorGroup groupTemp) {
        this.groupTemp = groupTemp;
    }

    public List<RemoteInitiator> getInitiators() {
        return initiators;
    }

    public void setInitiators(ArrayList<RemoteInitiator> initiators) {
        this.initiators = initiators;
    }

    public void beforeRemoveInitiator(RemoteInitiator initiator) {
        //temp = initiator;
        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().deleteiSCSIInitiator(initiator.getName());
        if (!flag) {
            Debug.print(" remove initiator" + flag + " initiatorname=" + initiator.getName());
        }

    }

    public void removeInitiator() {
        // this.initiators.remove(temp);
        //this.iscsiInitiators.remove(temp);
        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().deleteiSCSIInitiator(temp.getName());
        if (!flag) {
            Debug.print("remove initiator" + flag + " initiatorname=" + temp.getName());
            return;
        }

        iSCSIInitiator initiators[] = InterfaceFactory.getSCSIInterfaceInstance().getiSCSIInitiator();
        if (initiators != null) {
            this.iscsiInitiators = new ArrayList<RemoteInitiator>();
            for (int i = 0; i < initiators.length; i++) {
                iSCSIInitiator iscsi = initiators[i];
                RemoteInitiator initiator;
                if (this.iscsiConnectedInitiators.contains(iscsi.getName())) {
                    initiator = new RemoteInitiator(iscsi.getName(), iscsi.getUsername(), iscsi.getPassword(), true);
                } else {
                    initiator = new RemoteInitiator(iscsi.getName(), iscsi.getUsername(), iscsi.getPassword(), false);
                }
                Debug.print( iscsi.getName());
                this.iscsiInitiators.add(initiator);
            }
        }

    }

    public void doBeforeRemoveInitiator(RemoteInitiator initiator) {

        for (RemoteInitiatorGroup group : this.iscsiGroups) {
            if (group.getInitiatorNames().contains(initiator.getName())) {
                //this.groupName = group.getGroupName();
                this.temp.setGroupName(group.getGroupName());
                RequestContext.getCurrentInstance().execute("delgroupinitiator.show()");
                return;
            }
        }
        RequestContext.getCurrentInstance().execute("deleteinitiator.show()");

    }

    public void saveTemp(RemoteInitiator initiator) {
        this.temp = initiator;
    }

    public RemoteInitiator getTemp() {
        return temp;
    }

    public void setTemp(RemoteInitiator temp) {
        this.temp = temp;
    }

    public ArrayList<RemoteInitiator> getFcInitiators() {
        return fcInitiators;
    }

    public void setFcInitiators(ArrayList<RemoteInitiator> fcInitiators) {
        this.fcInitiators = fcInitiators;
    }

    public ArrayList<RemoteInitiator> getIscsiInitiators() {
        return iscsiInitiators;
    }

    public void setIscsiInitiators(ArrayList<RemoteInitiator> iscsiInitiators) {
        this.iscsiInitiators = iscsiInitiators;
    }

    public ArrayList<RemoteInitiatorGroup> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<RemoteInitiatorGroup> groups) {
        this.groups = groups;
    }

    public RemoteInitiator[] getSelectedInitiator() {
        return selectedInitiator;
    }

    public void setSelectedInitiator(RemoteInitiator[] selectedInitiator) {
        this.selectedInitiator = selectedInitiator;
    }

//    public void  modifyCHAP(String name,Chap chap){
//        for(int i=0;i<this.initiators.size();i++) {
//            
//            if(name.equals(initiators.get(i).getName())) {
//                initiators.get(i).setChap(chap);
//                break;
//            }
//        }
//    }
    public String configCHAP(RemoteInitiator initiator) {
        //this.temp = initiator;
        String initiatorName = initiator.getName();
        String startCHAPOrNot = initiator.isStartCHAPOrNot() ? "1" : "0";
        String userName = initiator.getUsername();
        String param = "initiatorName=" + initiatorName + "&" + "startCHAPOrNot=" + startCHAPOrNot + "&" + "userName=" + userName;

        return "scsi_remote_initiator_chap?faces-redirect=true&amp;" + param;
    }

    public void addInitiator(String name, String style) {
        RemoteInitiator initiator;
        if (style.equals("iSCSI")) {
            Chap chap = new Chap();
            initiator = new RemoteInitiator(name, chap, false);
            this.iscsiInitiators.add(initiator);
        } else {
            initiator = new RemoteInitiator(name, false);
            this.fcInitiators.add(initiator);
        }
        this.initiators.add(initiator);

    }

    public void addInitiator(RemoteInitiator initiator) {
        String style = initiator.getStyle();
        if (style.equals("iSCSI")) {
            this.iscsiInitiators.add(initiator);
            for (int i = 0; i < this.iscsiGroups.size(); i++) {
                iscsiGroups.get(i).addToAvaileble(initiator);
            }
        } else {
            this.fcInitiators.add(initiator);
            for (int i = 0; i < this.fcGroups.size(); i++) {
                this.fcGroups.get(i).addToAvaileble(initiator);
            }
        }
        this.initiators.add(initiator);
    }

    public void addFCGroup(String groupName) {
        RemoteInitiatorGroup group = new RemoteInitiatorGroup(groupName, "FC");
        this.fcGroups.add(group);
        for (int i = 0; i < this.fcInitiators.size(); i++) {
            group.addToAvaileble(fcInitiators.get(i));
        }
    }

    public void addiSCSIGroup(String groupName) {
        RemoteInitiatorGroup group = new RemoteInitiatorGroup(groupName, "iSCSI");
        this.iscsiGroups.add(group);
        for (int i = 0; i < this.iscsiInitiators.size(); i++) {
            group.addToAvaileble(iscsiInitiators.get(i));
        }
    }

    public String updateFCInitiatorGroup(RemoteInitiatorGroup group) {
        //members是组内所有成员，allfcInitiatorNames是所有fc Initiator，fcConnectedInitiators是所有已连接的fc Initiator
        String groupName = group.getGroupName();
//        String members = "";
//        ArrayList<String> initiatorNames = group.getInitiatorNames();
        String initiatorNames = "";
        for (int i = 0; i < fcConnectedInitiators.size(); i++) {
            if (i + 1 < fcConnectedInitiators.size()) {
                initiatorNames = initiatorNames + fcConnectedInitiators.get(i) + ",";
            } else {
                initiatorNames = initiatorNames + fcConnectedInitiators.get(i);
            }

        }
        String params = "groupName=" + groupName + "&" + "fcConnectedInitiators=" + initiatorNames;
//        ArrayList<RemoteInitiator> initiators = group.getAvalibleInitiators();
//        RemoteInitiatorDataModel dataModel = new RemoteInitiatorDataModel(initiators);
//        group.setDataModel(dataModel);
//        ArrayList<RemoteInitiator> includeInitiators = group.getInitiators();
//        RemoteInitiator[] selectedInitiator = (RemoteInitiator[]) includeInitiators.toArray(new RemoteInitiator[includeInitiators.size()]);
//        group.setSelectedInitiator(selectedInitiator);

        //this.selectFCGroup = group;
        return "scsi_update_remote_fc_group?faces-redirect=true&amp;" + params;
    }

    public String updateiSCSIInitiatorGroup(RemoteInitiatorGroup group) {
        String members = "";
        String groupName = group.getGroupName();
        String initiatorNames = "";
//        ArrayList<String> initiatorNames = group.getInitiatorNames();
//        for (int i = 0; i < initiatorNames.size(); i++) {
//            if (i + 1 < initiatorNames.size()) {
//                members = members + initiatorNames.get(i) + ",";
//            } else {
//                members = members + initiatorNames.get(i);
//            }
//
//        }
        for (int i = 0; i < iscsiConnectedInitiators.size(); i++) {
            if (i + 1 < iscsiConnectedInitiators.size()) {
                initiatorNames = initiatorNames + iscsiConnectedInitiators.get(i) + ",";
            } else {
                initiatorNames = initiatorNames + iscsiConnectedInitiators.get(i);
            }

        }
//        String params = "groupName=" + groupName + "&" + "members=" + members + "&" + "alliscsiInitiatorNames=" + this.alliscsiInitiatorNames + "&" + "iscsiConnectedInitiators=" + this.iscsiConnectedInitiators;
//        String params = "groupName=" + groupName + "&" + "members=" + members + "&" + "iscsiConnectedInitiators=" + this.iscsiConnectedInitiators;
        String params = "groupName=" + groupName + "&" + "iscsiConnectedInitiators=" + initiatorNames;
        Debug.print("param :" + params);
        return "scsi_update_remote_iscsi_group?faces-redirect=true&amp;" + params;
    }

    public void removeInitiorGroup() {
//        if(this.groupTemp.getStyle().equals("iSCSI")) {
//            
//        }
//        ArrayList<RemoteInitiator> initiators = this.groupTemp.getInitiators();
//        for (int i = 0; i < initiators.size(); i++) {
//            RemoteInitiator initiator = initiators.get(i);
//            initiator.setBelongGroupOrNot(false);
//            //initiator.setBelongAvaliableOrNot(false);
//        }
        this.groups.remove(this.groupTemp);

    }

    public void removeFCInitiorGroup() {

        // this.fcGroups.remove(this.groupTemp);
        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().deleteHostGroup(this.selectFCGroup.getGroupName());
        if (!flag) {
            Debug.print("del fc group " + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delFCFailed"), res.get("delFCFailed")));
            return;
        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        HostGroup[] fcHostGroups = scsi.getFCHostGroups();
        if (fcHostGroups != null) {
            this.fcGroups = new ArrayList<RemoteInitiatorGroup>();
            for (int i = 0; i < fcHostGroups.length; i++) {
                HostGroup group = fcHostGroups[i];
                //  this.selectedIP = (IPBinding[]) bindedIPList.toArray(new IPBinding[bindedIPList.size()]);
                // ArrayList<String> member = (ArrayList<String>) Arrays.asList(group.getGroupMember());
                ArrayList<String> members = new ArrayList<String>();
                if (group != null) {
                    members = group.getGroupMember();
                    RemoteInitiatorGroup initiatorGroups = new RemoteInitiatorGroup(group.getGroupName(), members);
                    this.fcGroups.add(initiatorGroups);
                }

            }
        }


    }

    public void doBeforeRemoveFCInitiorGroup(RemoteInitiatorGroup group) {
        this.selectFCGroup = group;
        this.selectGroupView = InterfaceFactory.getSCSIInterfaceInstance().getHostGroupLinkView(this.selectFCGroup.getGroupName());
        if (this.selectGroupView != null) {
            RequestContext.getCurrentInstance().execute("fcquestion.show()");
        } else {
            RequestContext.getCurrentInstance().execute("deleteFCGroup.show()");
        }
//        LuInformation info = InterfaceFactory.getSCSIInterfaceInstance().getHostGroupLinkLu(this.selectFCGroup.getGroupName());
//        if (info != null) {
//            if (info.luMode == LuInformation.LU_MODE_DISK || info.luMode == LuInformation.LU_MODE_CDP ) {
//                // RequestContext.getCurrentInstance().addCallbackParam("result", "disk");
//                this.selectFCGroup.setBinding(RemoteInitiatorGroup.disk);
//            } else if (info.luMode == LuInformation.LU_MODE_DRIVE || info.luMode == LuInformation.LU_MODE_TAPElIB) {
//                this.selectFCGroup.setBinding(RemoteInitiatorGroup.vtl);
//                //  RequestContext.getCurrentInstance().addCallbackParam("result", "drive");
//            }else {
//                this.selectFCGroup.setBinding("");
//            }
//            this.selectFCGroup.setGuid(info.luName);
//            RequestContext.getCurrentInstance().execute("fcquestion.show()");
//
//        } else {
//            RequestContext.getCurrentInstance().execute("deleteFCGroup.show()");
//        }

    }

    public void doBeforeRemoveiSCSIInitiorGroup(RemoteInitiatorGroup group) {
        this.selectIscsiGroup = group;
        this.selectGroupView = InterfaceFactory.getSCSIInterfaceInstance().getHostGroupLinkView(this.selectIscsiGroup.getGroupName());
        if (this.selectGroupView != null) {
            RequestContext.getCurrentInstance().execute("iscsiquestion.show()");
        } else {
            RequestContext.getCurrentInstance().execute("deleteIscsiGroup.show()");
        }

//        LuInformation info = InterfaceFactory.getSCSIInterfaceInstance().getHostGroupLinkLu(this.selectIscsiGroup.getGroupName());
//        //info为null表示没有绑定信息
//        if (info != null) {
//            if (info.luMode == LuInformation.LU_MODE_DISK || info.luMode == LuInformation.LU_MODE_CDP) {
//                this.selectIscsiGroup.setBinding(RemoteInitiatorGroup.disk);
//            } else if (info.luMode == LuInformation.LU_MODE_DRIVE || info.luMode == LuInformation.LU_MODE_TAPElIB) {
//                this.selectIscsiGroup.setBinding(RemoteInitiatorGroup.vtl);
//            } else {
//                this.selectFCGroup.setBinding("");
//            }
//            this.selectIscsiGroup.setGuid(info.luName);
//            RequestContext.getCurrentInstance().execute("iscsiquestion.show()");
//
//        } else {
//            RequestContext.getCurrentInstance().execute("deleteIscsiGroup.show()");
//        }
    }

    public void removeiSCSIInitiorGroup() {

        //this.iscsiGroups.remove(this.groupTemp);
        boolean flag;
        for (int i = 0; i < this.selectIscsiGroup.getInitiatorNames().size(); i++) {
            flag = InterfaceFactory.getSCSIInterfaceInstance().deleteiSCSIInitiator(this.selectIscsiGroup.getInitiatorNames().get(i));
            if (!flag) {
                Debug.print(" del iscsi initiator " + this.selectIscsiGroup.getInitiatorNames().get(i)  + "  " + flag);
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("deliSCSIInitiatorFailed"), res.get("deliSCSIInitiatorFailed")));
                return;
            }
        }
        flag = InterfaceFactory.getSCSIInterfaceInstance().deleteHostGroup(this.selectIscsiGroup.getGroupName());
        if (!flag) {
            Debug.print("del iscsi group " + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("deliSCSIFailed"), res.get("deliSCSIFailed")));
            return;
        }
        //刷新界面
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        HostGroup[] iscsiHostGroups = scsi.getiSCSIHostGroups();
        if (iscsiHostGroups != null) {
            this.iscsiGroups = new ArrayList<RemoteInitiatorGroup>();
            for (int i = 0; i < iscsiHostGroups.length; i++) {
                HostGroup group = iscsiHostGroups[i];
                //  this.selectedIP = (IPBinding[]) bindedIPList.toArray(new IPBinding[bindedIPList.size()]);
                // ArrayList<String> member = (ArrayList<String>) Arrays.asList(group.getGroupMember());
                ArrayList<String> members = new ArrayList<String>();
                members = group.getGroupMember();
                RemoteInitiatorGroup initiatorGroup = new RemoteInitiatorGroup(group.getGroupName(), members);
                this.iscsiGroups.add(initiatorGroup);
            }
        }
    }

    public void doBeforeRemoveGroup(RemoteInitiatorGroup group) {
        //this.groupTemp = group;

        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().deleteHostGroup(group.getGroupName());
        if (!flag) {
            Debug.print("RemoveGroup" + flag);
        }
    }

    public void updateALLAvalibleInitiatorsInFCGroup() {
        for (int j = 0; j < this.fcInitiators.size(); j++) {
            for (int i = 0; i < this.fcGroups.size(); i++) {
                fcGroups.get(i).addToAvaileble(fcInitiators.get(j));
            }
        }

    }

    public void updateALLAvalibleInitiatorsIniSCSIGroup(RemoteInitiator initiator) {
        for (int j = 0; j < this.iscsiInitiators.size(); j++) {
            for (int i = 0; i < this.iscsiGroups.size(); i++) {
                iscsiGroups.get(i).addToAvaileble(initiator);
            }
        }

    }

    public String test() {
        return "scsi_host?faces-redirect=true;";
    }
//跳转到解除iSCSI绑定界面

    public String skipToRemoveiSCSIBind() {
        Debug.print(" iscsi bind : " + this.selectIscsiGroup.getBinding());
        String param = "GUID=" + this.selectIscsiGroup.getGuid();
        Debug.print(" iscsi bind guid : " + this.selectIscsiGroup.getGuid());
        if (this.selectIscsiGroup.getBinding().equals(RemoteInitiatorGroup.disk)) {
            return "/cdp/lun?faces-redirect=true&amp;" + param;
        } else if (this.selectIscsiGroup.getBinding().equals(RemoteInitiatorGroup.vtl)) {
            return "/vtl/lib_lunmapping?faces-redirect=true&amp;" + param;
        }
        return null;
        // return "nas_user_group?faces-redirect=true";
    }

    public void removeFCBindedGroup() {
//        Debug.print("fc bind : " + this.selectFCGroup.getBinding());
//        String param = "GUID=" + this.selectFCGroup.getGuid();
//        Debug.print(" fc bind guid : " + this.selectFCGroup.getGuid());
//        if (this.selectFCGroup.getBinding().equals(RemoteInitiatorGroup.disk)) {
//            return "/cdp/lun?faces-redirect=true&amp;" + param;
//        } else if (this.selectFCGroup.getBinding().equals(RemoteInitiatorGroup.vtl)) {
//            return "/vtl/lib_lunmapping?faces-redirect=true&amp;" + param;
//        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        for (int i = 0; i < this.selectGroupView.length; i++) {
            ViewInformation view = this.selectGroupView[i];
            boolean flag = scsi.removeView(view.GUID, view.entry);
            if (!flag) {
                Debug.print(" RemoveView " + flag);
                Debug.print("Guid: " + view.GUID);
                Debug.print(" entry: " + view.entry);
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("removeBindFailed"), res.get("removeBindFailed")));
                return;
            }
        }
        this.removeFCInitiorGroup();

    }

    public void removeISCSIBindedGroup() {
//        Debug.print("fc bind : " + this.selectFCGroup.getBinding());
//        String param = "GUID=" + this.selectFCGroup.getGuid();
//        Debug.print(" fc bind guid : " + this.selectFCGroup.getGuid());
//        if (this.selectFCGroup.getBinding().equals(RemoteInitiatorGroup.disk)) {
//            return "/cdp/lun?faces-redirect=true&amp;" + param;
//        } else if (this.selectFCGroup.getBinding().equals(RemoteInitiatorGroup.vtl)) {
//            return "/vtl/lib_lunmapping?faces-redirect=true&amp;" + param;
//        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        for (int i = 0; i < this.selectGroupView.length; i++) {
            ViewInformation view = this.selectGroupView[i];
            boolean flag = scsi.removeView(view.GUID, view.entry);
            if (!flag) {
                Debug.print("RemoveView " + flag);
                Debug.print(" Guid: " + view.GUID);
                Debug.print("entry: " + view.entry);
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("removeBindFailed"), res.get("removeBindFailed")));
                return;
            }
        }
        this.removeiSCSIInitiorGroup();

    }
//    public void aaa() {
//        RequestContext.getCurrentInstance().execute("bind.show()");
//    }
//
//    public String test1() {
//        return "nas_user_group?faces-redirect=true";
//    }
}
