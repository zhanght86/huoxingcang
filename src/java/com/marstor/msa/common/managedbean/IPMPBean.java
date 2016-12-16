/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.model.IPMP;
import com.marstor.msa.common.model.IPMPDetail;
import com.marstor.msa.common.bean.IpmpAddressInformation;
import com.marstor.msa.common.bean.IpmpGroupInformation;
import com.marstor.msa.common.bean.IpmpTargetInformation;
import com.marstor.msa.common.bean.NetConfigInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "iPMPBean")
@ViewScoped
public class IPMPBean implements Serializable {

    public List<IPMP> ipmpList;
    public IPMP ipmp;
    List<IPMPDetail> detailList;
    List<IpmpTargetInformation> targetList;
    IPMPDetail detail;
    IpmpTargetInformation target;
    public IPMP selectIPMP;
    private MSAResource res = new MSAResource();
    private String basename = "common.system_network";
    public String deleteipmptip;
    public String deleteipmp_ip;

    /**
     * Creates a new instance of IPMPImpl
     */
    public IPMPBean() {
        deleteipmptip = res.get(basename, "deleteipmptip");
        deleteipmp_ip = res.get(basename, "deleteipmp_ip");
        addIPMPList();
    }

    public void addIPMPList() {
        ipmpList = new ArrayList();

        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        IpmpGroupInformation[] ipmpDroups = common.getIPMPGroupInformation();
        IpmpAddressInformation[] ipmpAddresses = common.getIPMPAddessInformation();
       
        String stateStr = "";
        if (ipmpDroups != null && ipmpDroups.length > 0) {
            for (int i = 0; i < ipmpDroups.length; i++) {
                ipmp = new IPMP();
                ipmp.setIPMPGroup(ipmpDroups[i].getGROUP());
                String group = ipmpDroups[i].getGROUP();
                ipmp.setGroupName(ipmpDroups[i].getGroupName());
                if (ipmpDroups[i].getState().equalsIgnoreCase("ok")) {
                    stateStr = res.get(basename, "ok");
                } else if (ipmpDroups[i].getState().equalsIgnoreCase("failed")) {
                    stateStr = res.get(basename, "failed");
                } else if (ipmpDroups[i].getState().equalsIgnoreCase("degraded")) {
                    stateStr = res.get(basename, "degraded");
                } else {
                    stateStr = ipmpDroups[i].getState();
                }
                ipmp.setState(stateStr);
                
                String typeStr = ipmpDroups[i].getInterfaces();
                 SystemOutPrintln.print_common("name="+typeStr+":");
                 String[] names = typeStr.split(" ");
                 typeStr = "";
                for (String na : names) {
                    if (na.startsWith("[")) {
                        na = na.substring(1, na.length());
                    }else if (na.startsWith("(")) {
                        na = na.substring(1, na.length());
                    }else{
                        na = na;
                    }
                    if (na.endsWith("]")) {
                        na = na.substring(0, na.length() - 1);
                    }else if(na.endsWith(")")) {
                        na = na.substring(0, na.length() - 1);
                    }else{
                        na = na;
                    }
                    typeStr = typeStr + na +" ";
                }
                ipmp.setType(typeStr);
                detailList = new ArrayList();

                if (ipmpAddresses != null && ipmpAddresses.length > 0) {
                    for (int j = 0; j < ipmpAddresses.length; j++) {
                        if (ipmpAddresses[j].getGROUP().equals(group)) {
                            detail = new IPMPDetail();
                            detail.setIPMPGroup(ipmpAddresses[j].getGROUP());
                            detail.setIp(ipmpAddresses[j].getAddress());
                            detail.setState(ipmpAddresses[j].getState());
                            detail.setEntry(ipmpAddresses[j].getINBOUND());
                            detail.setExit(ipmpAddresses[j].getOUTBOUND());
                            detailList.add(detail);
                        } else {
                            continue;
                        }
                    }

                }
                
                targetList = new ArrayList();
                IpmpTargetInformation[] targets = common.getGroupTargetState(group);
                if (targets != null && targets.length > 0) {
                    for (int k = 0; k < targets.length; k++) {

                        target = new IpmpTargetInformation();
                        String model = res.get(basename, "disabled");
                        if(targets[k].mode.equalsIgnoreCase("disabled")){
                            model = res.get(basename, "disabled");
                        }else if(targets[k].mode.equalsIgnoreCase("multicast")){
                            model = res.get(basename, "multicast");
                        }else if(targets[k].mode.equalsIgnoreCase("routes")){
                            model = res.get(basename, "routes");
                        }
                        target.setMode(model);
                        target.setNic(targets[k].nic);
                         String state = res.get(basename, "state_ok");
                          SystemOutPrintln.print_common("state="+targets[k].state+":");;
                        if(targets[k].state.equalsIgnoreCase("ok")){
                            state = res.get(basename, "state_ok");
                        }else if(targets[k].state.equalsIgnoreCase("failed")){
                            state = res.get(basename, "state_failed");
                        }else if(targets[k].state.equalsIgnoreCase("disabled")){
                            state = res.get(basename, "disabled");
                        }else if(targets[k].state.equalsIgnoreCase("unknown")){
                            state = res.get(basename, "state_failed");
                        }else{
                            state = targets[k].state;
                        }
                        target.setState(state);
                        target.setTarget(targets[k].target);
                        target.setTestIP(targets[k].testIP);
                        
                   
                        targetList.add(target);


                    }

                }
             
                ipmp.setDetailList(detailList);
                ipmp.setTargetList(targetList);
                ipmpList.add(ipmp);
            }

        }

    }

    public List<IPMP> getIpmpList() {
         
        return ipmpList;
    }

    public void setIpmpList(List<IPMP> ipmpList) {
        this.ipmpList = ipmpList;
    }

    public IPMP getSelectIPMP() {
        return selectIPMP;
    }

    public void setSelectIPMP(IPMP selectIPMP) {
        this.selectIPMP = selectIPMP;
    }

    public String getDeleteipmptip() {
        return deleteipmptip;
    }

    public void setDeleteipmptip(String deleteipmptip) {
        this.deleteipmptip = deleteipmptip;
    }

    public String getDeleteipmp_ip() {
        return deleteipmp_ip;
    }

    public void setDeleteipmp_ip(String deleteipmp_ip) {
        this.deleteipmp_ip = deleteipmp_ip;
    }
    
    
    public String cheakIPMP() {
         SystemOutPrintln.print_common("cheak IPMP=" + selectIPMP.groupName);
        String group = selectIPMP.groupName;
        String param = "groupName=" + group;
        return "network_checkipmp?faces-redirect=true&amp;" + param;
    }

    public void deleteIPMP() {
//        selectIPMP = select;
         SystemOutPrintln.print_common("删除IPMP="+selectIPMP.groupName);
         String group = selectIPMP.getIPMPGroup();
        
         CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
         IpmpAddressInformation[] action =common.getIPMPAddessInformation();
         if(action == null || action.length<0){
             return;
         }

        List<IpmpAddressInformation> allIpmpAddress = Arrays.asList(action);
        boolean isNullOrEmpty = (allIpmpAddress == null);
        if (!isNullOrEmpty) {
            isNullOrEmpty = isNullOrEmpty || allIpmpAddress.isEmpty();
        }
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        String ip = request.getLocalAddr();
        boolean bCurrentIP = false;

        if (!isNullOrEmpty) {
            for (int i = 0; i < allIpmpAddress.size(); i++) {
                IpmpAddressInformation object = allIpmpAddress.get(i);
                if (!object.GROUP.equals(group)) {
                    continue;
                }
                if (object.address.equals(ip)) {
                    bCurrentIP = true;
                    break;
                }
            }
            if (bCurrentIP) {
 //             Constants.showWarningMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/NetConfigDialog").getString("选择网卡正在被当前连接使用"));
            }
        }
        
        if (bCurrentIP) {           
            deleteipmp_ip = res.get(basename, "deleteipmp_ip_left") + selectIPMP.groupName + res.get(basename, "delete_right");
            RequestContext.getCurrentInstance().execute("deleteIPMP_IP.show()");
            return;
        } else {
            deleteipmptip = res.get(basename, "delete_left") + selectIPMP.groupName + res.get(basename, "delete_right");
            RequestContext.getCurrentInstance().execute("deleteIPMP.show()");
            return;
        }
        
        
//        String group = selectIPMP.getIPMPGroup();
//        String type = selectIPMP.getType();
        
        
        
        
//        ipmpList.remove(selectIPMP);

    }
    
    public String deleteIPMP_real(){
         String group = selectIPMP.groupName;
//         String type = selectIPMP.getType();
//        NetConfigInformation[] nets = null;
//        if(type!=null && !type.equals("")){
//            String[] nicNames = null;
//            if(type.contains(",")){
//                nicNames=type.split(",");
//            }else{
//                nicNames=type.split(" ");
//            }
//             
//            nets = new  NetConfigInformation[nicNames.length];
//            NetConfigInformation net = null;
//            for(int i=0; i<nicNames.length; i++){
//                net = new NetConfigInformation();
//                net.setName(nicNames[i].trim());
//                nets[i] = net;
//            }
//        }
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        SystemOutPrintln.print_common("delete IPMP group="+group);
//        for(int i=0; i<nets.length;i++){
//            System.out.println("delete IPMP nets[i]="+nets[i]);
//        }
//        boolean deIPMP = common.deleteIPMPGroup(group, nets);
           boolean deIPMP = common.deleteIPMPGroup(group);
        if (deIPMP == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "deipmp_fail"), ""));
            return null;
        }else{
             SystemOutPrintln.print_common("delete IPMP succeed");
        }
        return "system_network?faces-redirect=true&amp;accordionActive1=2";
    }
    
    public String openIPMP(){
 //   Constants.showWarningMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/NetConfigDialog").getString("可能影响其它模块的使用"));
    return "network_createipmp?faces-redirect=true";
    }
    
}
