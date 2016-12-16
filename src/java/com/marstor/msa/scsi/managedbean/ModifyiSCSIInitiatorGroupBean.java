/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.bean.HostGroup;
import com.marstor.msa.common.bean.iSCSIInitiator;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.scsi.model.RemoteInitiator;
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
public class ModifyiSCSIInitiatorGroupBean {

    private String initiatorName;
    private String groupName = "";
    //  private RemoteInitiator[] selectedInitiator;
    //  private ArrayList<String> memberList = new ArrayList<String>();
    private RemoteInitiator selectedInitiator = new RemoteInitiator();
    private ArrayList<RemoteInitiator> members = new ArrayList<RemoteInitiator>();
    private String iSCSIConnectedInitiators = "";
    private ArrayList<String> connectedInitiatorList = new ArrayList<String>();
    //  private ArrayList<RemoteInitiator> allInitiatorList = new ArrayList<RemoteInitiator>();
    //   private ArrayList<RemoteInitiator> includeInitiatorList = new ArrayList<RemoteInitiator>();
    //   private RemoteInitiatorDataModel data = new RemoteInitiatorDataModel(allInitiatorList);
    private SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();

    public ModifyiSCSIInitiatorGroupBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        groupName = request.getParameter("groupName");

        HostGroup[] iscsiHostGroups = scsi.getiSCSIHostGroups();
        ArrayList<String> memberList = new ArrayList<String>();
        //获取组中成员
        if (iscsiHostGroups != null) {
            for (int i = 0; i < iscsiHostGroups.length; i++) {
                HostGroup group = iscsiHostGroups[i];
                if (group.getGroupName().equals(this.groupName)) {
                    memberList = group.getGroupMember();
                    break;
                }

            }
        }
        //获取已连接的Initiator
        connectedInitiatorList = new ArrayList<String>();
        this.iSCSIConnectedInitiators = request.getParameter("iscsiConnectedInitiators");
        if (iSCSIConnectedInitiators != null) {
            String iSCSIConnectedInitiatorArray[] = iSCSIConnectedInitiators.trim().split(",");

            if (iSCSIConnectedInitiatorArray != null) {

                for (int i = 0; i < iSCSIConnectedInitiatorArray.length; i++) {
                    connectedInitiatorList.add(iSCSIConnectedInitiatorArray[i]);
                }
            }
        } else {
            this.iSCSIConnectedInitiators = "";
        }
        //创建表格中的每一行
        iSCSIInitiator initiators[] = scsi.getiSCSIInitiator();
        HashMap<String, iSCSIInitiator> allInitiators = new HashMap<String, iSCSIInitiator>();
        // ArrayList<String> allInitiators = new ArrayList<String>();
        if (initiators != null) {
            for (iSCSIInitiator initiator : initiators) {
                // allInitiators.add(initiator.name);
                allInitiators.put(initiator.name, initiator);
            }
            if (memberList != null) {
                for (String member : memberList) {
                    RemoteInitiator initiator;
                    if (allInitiators.containsKey(member)) {
                        if (connectedInitiatorList.contains(member)) {
                            //  initiator = new RemoteInitiator(initiators[i].getName(), initiators[i].getUsername(), initiators[i].getPassword(), true);
                            initiator = new RemoteInitiator(allInitiators.get(member).name, allInitiators.get(member).getUsername(), allInitiators.get(member).getPassword(), true);
                        } else {
                            //   initiator = new RemoteInitiator(initiators[i].getName(), initiators[i].getUsername(), initiators[i].getPassword(), false);
                            initiator = new RemoteInitiator(allInitiators.get(member).name, allInitiators.get(member).getUsername(), allInitiators.get(member).getPassword(), false);
                        }

                    } else {
                        //如果组成员在allInitiators中不存在，则创建一个新的initiator.
                        initiator = new RemoteInitiator(member, false);
                        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().createiSCSIInitiator(member);
                        if (!(flag)) {
                            MSAResource res = new MSAResource();
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addFailed"), res.get("addFailed")));
                            Debug.print("CreateRemoteInitiator " + flag);
                            return ;
                        }

                    }
                    Debug.print("initiator name : " + initiator.getName());
                    this.members.add(initiator);
                }
            }
        }

//            for (int i = 0; i < initiators.length; i++) {
//                if (memberList.contains(initiators[i].getName())) {
//                    RemoteInitiator initiator;
//                    if (connectedInitiatorList.contains(initiators[i].getName())) {
//                        initiator = new RemoteInitiator(initiators[i].getName(), initiators[i].getUsername(),initiators[i].getPassword(), true);
//                    } else {
//                         initiator = new RemoteInitiator(initiators[i].getName(), initiators[i].getUsername(),initiators[i].getPassword(), false);
//                    }
//                    this.members.add(initiator);
//                }
//            }

//        if (memberList != null) {
//            for (int i = 0; i < memberList.size(); i++) {
//                String name = memberList.get(i);
//                RemoteInitiator initiator;
//                if (connectedInitiatorList.contains(name)) {
//                    initiator = new RemoteInitiator(name, true);
//                } else {
//                    initiator = new RemoteInitiator(name, false);
//                }
//                members.add(initiator);
//            }
//        }

//        String alliSCSIInitiators = request.getParameter("alliscsiInitiatorNames");
//        if (alliSCSIInitiators != null) {
//            String iSCSIInitiatorArray[] = alliSCSIInitiators.trim().split(",");
//            if (iSCSIInitiatorArray != null) {
//                for (int i = 0; i < iSCSIInitiatorArray.length; i++) {
//                    String name = iSCSIInitiatorArray[i];
//                    Debug.print( name);
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
//
//        }

//        data = new RemoteInitiatorDataModel(allInitiatorList);
//        Debug.print("success");
//
//        this.selectedInitiator = (RemoteInitiator[]) includeInitiatorList.toArray(new RemoteInitiator[includeInitiatorList.size()]);
    }

    public void init() {
        ArrayList<RemoteInitiator> initiatorMembers = new ArrayList<RemoteInitiator>();
        HostGroup[] iscsiHostGroups = scsi.getiSCSIHostGroups();
        ArrayList<String> memberList = new ArrayList<String>();
        if (iscsiHostGroups != null) {
            for (int i = 0; i < iscsiHostGroups.length; i++) {
                HostGroup group = iscsiHostGroups[i];
                if (group.getGroupName().equals(this.groupName)) {
                    memberList = group.getGroupMember();
                    break;
                }

            }
        }
        //创建表格中的每一行
        iSCSIInitiator initiators[] = scsi.getiSCSIInitiator();
        HashMap<String, iSCSIInitiator> allInitiators = new HashMap<String, iSCSIInitiator>();
        // ArrayList<String> allInitiators = new ArrayList<String>();
        if (initiators != null) {
            for (iSCSIInitiator initiator : initiators) {
                // allInitiators.add(initiator.name);
                allInitiators.put(initiator.name, initiator);
            }
            if (memberList != null) {
                for (String member : memberList) {
                    RemoteInitiator initiator;
                    if (allInitiators.containsKey(member)) {
                        if (connectedInitiatorList.contains(member)) {
                            //  initiator = new RemoteInitiator(initiators[i].getName(), initiators[i].getUsername(), initiators[i].getPassword(), true);
                            initiator = new RemoteInitiator(allInitiators.get(member).name, allInitiators.get(member).getUsername(), allInitiators.get(member).getPassword(), true);
                        } else {
                            //   initiator = new RemoteInitiator(initiators[i].getName(), initiators[i].getUsername(), initiators[i].getPassword(), false);
                            initiator = new RemoteInitiator(allInitiators.get(member).name, allInitiators.get(member).getUsername(), allInitiators.get(member).getPassword(), false);
                        }

                    } else {
                        initiator = new RemoteInitiator(member, false);
                    }
                    initiatorMembers.add(initiator);
                }
            }
        }
        this.members = initiatorMembers;

    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
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

    //    public ArrayList<String> getMemberList() {
    //        return memberList;
    //    }
    //
    //    public void setMemberList(ArrayList<String> memberList) {
    //        this.memberList = memberList;
    //    }
    public ArrayList<RemoteInitiator> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<RemoteInitiator> members) {
        this.members = members;
    }

    public String addInitiatorToiSCSIGroup() {
//        if (this.initiatorName.equals("")) {
//            MSAResource res = new MSAResource();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("canNotEmpty"), res.get("canNotEmpty")));
//            return;
//        }
//        RemoteInitiator initiator = new RemoteInitiator(this.initiatorName, false);
//        this.allInitiatorList.add(initiator);
//        this.data.setWrappedData(this.allInitiatorList);
//        //this.data.addInitiator(initiator);
//       // this.includeInitiatorList.add(initiator);
//        ArrayList<RemoteInitiator> initiators = new ArrayList<RemoteInitiator>();
//        for(int i=0;i<this.selectedInitiator.length;i++) {
//            initiators.add(this.selectedInitiator[i]);
//        }
//        initiators.add(initiator);
//        this.selectedInitiator = (RemoteInitiator[]) initiators.toArray(new RemoteInitiator[initiators.size()]);
        String params = "groupName=" + groupName + "&" + "iscsiConnectedInitiators=" + this.iSCSIConnectedInitiators;
        Debug.print(" param :" + params);
        return "scsi_create_remote_initiator?faces-redirect=true&amp;" + params;

    }

    public String configCHAP(RemoteInitiator initiator) {
        //this.temp = initiator;
        String initiatorName = initiator.getName();
        String startCHAPOrNot = initiator.isStartCHAPOrNot() ? "1" : "0";
        String userName = initiator.getUsername();
        String param = "groupName=" + groupName + "&" + "iscsiConnectedInitiators=" + this.iSCSIConnectedInitiators + "&" + "initiatorName=" + initiatorName + "&" + "startCHAPOrNot=" + startCHAPOrNot + "&" + "userName=" + userName;

        return "scsi_remote_initiator_chap?faces-redirect=true&amp;" + param;
    }

    public void deleteInitiator() {
        String array[] = new String[1];
        array[0] = this.selectedInitiator.getName();
        boolean flag = scsi.removeHostGroupMember(this.groupName, array);
        MSAResource res = new MSAResource();
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delFailed"), res.get("delFailed")));
            Debug.print(" ModifyiSCSIInitiatorGroup remove： " + flag);
            return;
        }
        flag = InterfaceFactory.getSCSIInterfaceInstance().deleteiSCSIInitiator(this.selectedInitiator.getName());
        if (!flag) {
            //delInitiator
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delInitiator"), res.get("delInitiator")));
            Debug.print(" remove initiator " + flag + " initiatorname=" + this.selectedInitiator.getName());
            return;
        }
        this.init();
    }

    public String save() {
//        ArrayList<String> includeInitiators = new ArrayList<String>();
//        for(int i=0;i<this.memberList.size();i++) {
//           Debug.print(" initiator member " + memberList.get(i)); 
//        }
//         for(int i=0;i<this.selectedInitiator.length;i++) {
//             Debug.print("select initiator " + selectedInitiator[i].getName());
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
//        if (addInitiators.size() > 0) {
//            Debug.print("add ");
//            Debug.print(" group " + this.groupName);
//            Debug.print(" initiator: ");
//            for(String  initiator : addInitiators) {
//                 Debug.print(initiator);
//            }
//            boolean flag = scsi.addHostGroupMember(this.groupName, (String[]) addInitiators.toArray(new String[addInitiators.size()]));
//           // print();
//            if (!flag) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("configFailed"), res.get("configFailed")));
//                Debug.print(" ModifyiSCSIInitiatorGroup add" + flag);
//                return null;
//            }
//
//        }
//        if (deleteInitiators.size() > 0) {
//            Debug.print(" del ");
//            Debug.print(" group " + this.groupName);
//            Debug.print(" initiator: ");
//            for(String  initiator : deleteInitiators) {
//                 Debug.print(initiator);
//            }
//            boolean flag = scsi.removeHostGroupMember(this.groupName, (String[]) deleteInitiators.toArray(new String[deleteInitiators.size()]));
//            if (!flag) {
//              //  print();
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("configFailed"), res.get("configFailed")));
//                Debug.print(" ModifyiSCSIInitiatorGroup remove" + flag);
//                return null;
//            }
//
//        }
        return "scsi_host?faces-redirect=true";
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
//                if (groupMember != null) {
//                    for (int j = 0; j < groupMember.length; j++) {
//                        Debug.print("member:" + groupMember[j]);
//                    }
//                }
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
}
