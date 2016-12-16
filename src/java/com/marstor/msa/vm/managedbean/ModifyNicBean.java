/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.bean.HostNIC;
import com.marstor.msa.vbox.web.MsaVMInterface;
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
@ManagedBean(name = "modifyNicBean")
@ViewScoped
public class ModifyNicBean implements Serializable{

    /**
     * Creates a new instance of ModifyNicBean
     */
    public List<String> nicList;
    public String vmName;
    public int nicMun;
    public String macAddress = "0800273CCDDD";
    public boolean boolCableConnected;
    public String vmNICName;
    public String bindPhysicsNIC;
    List<HostNIC> nic;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_vminfotable_modifyNic";
    public ModifyNicBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        bindPhysicsNIC = request.getParameter("bindPhysicsNIC");
        vmNICName = bindPhysicsNIC.split(" ")[0];
        vmName = request.getParameter("vmName");
        nicMun = Integer.valueOf(request.getParameter("nicMun"));
        macAddress = request.getParameter("macAddress");
        String connected = request.getParameter("cableConnected");
        if (connected.equalsIgnoreCase("true")) {
            boolCableConnected = true;
        }else{
            boolCableConnected = false;
        }
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
                String nicName = nicNames[0];
                nicList.add(nicName);
            }
        }

    }
    public void handleMACRefresh() {

        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        String VMMACAddress = vmface.getMacAddress(vmName, nicMun);

        if (VMMACAddress != null) {
            macAddress = VMMACAddress.trim();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "refreshmac_fail"), ""));
            return;
        }

    }
    public String editNIC() {      
         if (macAddress==null || macAddress.trim().equals("")) {//验证码问题
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "mac_null"), ""));
            return null;
        }
        if (!macAddress.matches("[0-9a-fA-F]{1}[02468aceACE]{1}[0-9a-fA-F]{10}")) {//验证码问题
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "mac_limit"), ""));
            return null;
        }
        String nicName = "";
         String setBindPhysicsNICName = "";
        if(nic != null){
            for (int n = 0; n < nic.size(); n++) {
                String[] nicNames = nic.get(n).getName().split(" ");
                nicName = nicNames[0].toString();
                if (nicName.equals(vmNICName)){
                 setBindPhysicsNICName =  nic.get(n).getName();
                   
                }
            }
        }
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean boolBindNIC = vmface.setBindNIC(vmName, nicMun, setBindPhysicsNICName);  //5025绑定物理网卡 

        boolean boolMacAddress = vmface.setMacAddress(vmName, nicMun, macAddress);  //5025设置虚拟机网卡 

        boolean boolNICConnected = vmface.setNICConnected(vmName, nicMun, boolCableConnected); //5025虚拟机网卡网线是否链接 
        String tip = res.get(basename, "tip");
        String returnStr = "";
        if(boolBindNIC && boolMacAddress && boolNICConnected){
            String param = "vmName=" + vmName;
            returnStr = "vm_vminfotab?faces-redirect=true&amp;" + param+"&amp;accordionActive1=1";    
        }else{
            if(!boolBindNIC){
                tip = tip + res.get(basename, "net")+" ";
            }
            if(!boolMacAddress){
                tip = tip + res.get(basename, "mac")+" ";
            }
            if(!boolNICConnected){
                tip = tip + res.get(basename, "cableconnected");
            }  
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tip+res.get(basename, "fail"), ""));
            return null;
            
        }


        
        return returnStr;
    }
    
    public String cancleNIC() {
        String param = "vmName=" + vmName;
        return "vm_vminfotab?faces-redirect=true&amp;" + param+"&amp;accordionActive1=1"; 
    }

    public List<String> getNicList() {
        return nicList;
    }

    public void setNicList(List<String> nicList) {
        this.nicList = nicList;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getVmNICName() {
        return vmNICName;
    }

    public void setVmNICName(String vmNICName) {
        this.vmNICName = vmNICName;
    }

    public boolean isBoolCableConnected() {
        return boolCableConnected;
    }

    public void setBoolCableConnected(boolean boolCableConnected) {
        this.boolCableConnected = boolCableConnected;
    }
    
}
