/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.bean.HostNIC;
import com.marstor.msa.vbox.bean.NetworkInterfaceCard;
import com.marstor.msa.vbox.bean.VirtualMachine;
import com.marstor.msa.vbox.web.MsaVMInterface;
import com.marstor.msa.vm.model.NetworkInterfaceCardInfo;
import com.marstor.msa.vm.model.VMInformation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@ManagedBean(name = "creatNetWork")
@ViewScoped
public class CreatNetWork implements Serializable{
    List<NetworkInterfaceCard> networkInterfaceCards  = null;
    NetworkInterfaceCardInfo networkInterfaceCardInfo = null;
    int nicCount = 0;
    VirtualMachine vm = new VirtualMachine();
    NetworkInterfaceCardInfo newNet;
    List<HostNIC> nic = null;
    public List<String> nicList;
    public String nicName;
    public boolean isCableConnected = false;
    public VMInformation selectedVM;
    public List<NetworkInterfaceCardInfo> networkInterfaceCard;
    String vmName;
    int vmnicNum = 1;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_vminfotable_addnic";
    /**
     * Creates a new instance of CreatNetWork
     */
    public CreatNetWork() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        vmName = request.getParameter("vmName");
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        vm = vmface.getVMInfo(vmName);
        //网络设置
        networkInterfaceCards = vm.getNetworkInterfaceCard();
        nicCount = networkInterfaceCards.size();
        vmnicNum = getVMNICNum();

        initdata();
    }
    
     //从后台服务器端获取数据
    public void initdata(){
        nicList = new ArrayList();
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        nic = vmface.getHostNICInfo();
        if (nic == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "getnic_fail"), ""));
            return;
        }
        if (nic != null) {
            for (int n = 0; n < nic.size(); n++) {
                String[] nicNames = nic.get(n).getName().split(" ");
                String name = nicNames[0];
                nicList.add(name);
            }
        }

    }
    
    public String addNetWork(){
        String getNic = nicName;
        String nicName = null;
        String bindPhysicsNICName = "";
        if(nic != null){
            for (int n = 0; n < nic.size(); n++) {
               String[] nicNames = nic.get(n).getName().split(" ");
                nicName = nicNames[0].toString();
                if (nicName.equals(getNic)){
                     bindPhysicsNICName = nic.get(n).getName();
                }
            }
        }
        
        
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean boolAddNIC = vmface.addNIC(vmName, vmnicNum);
        String returnStr = "";
        if (boolAddNIC) {
            String vMMACAddress = vmface.getMacAddress(vmName, vmnicNum);
            if (vMMACAddress != null) {
                vMMACAddress = vMMACAddress.trim();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "getmac_fail"), ""));
                return null;
            }

            boolean boolBindNIC = vmface.setBindNIC(vmName, vmnicNum, bindPhysicsNICName);  //5025绑定物理网卡 
            boolean boolMacAddress = vmface.setMacAddress(vmName, vmnicNum, vMMACAddress.trim());  //5025设置虚拟机网卡 
            boolean boolNICConnected = vmface.setNICConnected(vmName, vmnicNum, isCableConnected); //5025虚拟机网卡网线是否链接 
            String tip = res.get(basename, "tip");
            
            if (boolMacAddress && boolBindNIC && boolNICConnected) {
                SystemOutPrintln.print_vm("add nic succeed vmName="+vmName+",vmnicNum="+vmnicNum+",vMMACAddress="+vMMACAddress.trim()+",isCableConnected="+isCableConnected+",bindPhysicsNICName="+bindPhysicsNICName);
                String param = "vmName=" + vmName;
                returnStr = "vm_vminfotab?faces-redirect=true&amp;" + param+"&amp;accordionActive1=1";
            } else {
                if (!boolBindNIC) {
                    tip = tip + res.get(basename, "net")+" ";
                }
                if (!boolMacAddress) {
                    tip = tip + res.get(basename, "mac")+" ";
                }
                if (!boolNICConnected) {
                    tip = tip + res.get(basename, "cableconnected");
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tip + res.get(basename, "fail"), ""));
                return null;

            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "addnic_fail"), ""));
            return null;
        }

        return returnStr;
    }
    
    //获得新建网卡编号
    public int getVMNICNum(){
        ArrayList<Integer> attribute1 = new ArrayList<Integer>();//获得已经用掉的端口
        int cardCount = networkInterfaceCards.size();
        for (int i = 0; i < cardCount; i++) {
            int vmNICNum = networkInterfaceCards.get(i).getNicNum();
            attribute1.add(vmNICNum);
        }
        ArrayList<Integer> attribute2 = new ArrayList<Integer>();//获得未用的端口
        for (int i = 1; i < 9; i++) {  //给赋值
            boolean isHave = false;
            for (int j = 0; j < attribute1.size(); j++) {
                if (i == attribute1.get(j)) {
                    isHave = true;
                    break;
                } else {
                    isHave = false;
                }
            }
            if (isHave == false) {
                attribute2.add(i);
            }
        }

        int nic = attribute2.get(0);
        return nic;
    }
    
    public String cancleButton(){
        String param = "vmName=" + vmName;
        return "vm_vminfotab?faces-redirect=true&amp;" + param+"&amp;accordionActive1=1"; 
    }

    public List<String> getNicList() {
        return nicList;
    }

    public void setNicList(List<String> nicList) {
        this.nicList = nicList;
    }

    public String getNicName() {
        return nicName;
    }

    public void setNicName(String nicName) {
        this.nicName = nicName;
    }

    public boolean isIsCableConnected() {
        return isCableConnected;
    }

    public void setIsCableConnected(boolean isCableConnected) {
        this.isCableConnected = isCableConnected;
    }
    
//    public String setResource(String resource) {
//        return java.util.ResourceBundle.getBundle("com/marstor/msa/common/resources/messages").getString(resource);
//    }
    
}
