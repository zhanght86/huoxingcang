/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @introduction 网卡信息
 * @author Administrator
 */
@ManagedBean(name = "networkInterfaceCardInfo")
@RequestScoped
public class NetworkInterfaceCardInfo implements Serializable{
    private int vmNICNum;  //网卡编号
    private boolean vmNICIsUsable;  //是否可用，1为可用，0为不可用
    private String vmNICName;  //网卡名
    private String bindPhysicsNIC;
    private String macAddress;  //网卡mac地址
    private String edit_macAddress;  //网卡mac地址
    private int cableConnected;  //是否插入网线,0代表未插入，1代表插入
    private boolean boolCableConnected;
    private String strCableConnected;  //是否插入网线,根据cableConnected确定，0代表未插入，1代表插入
    private String bindPhysicsNicIpAddress;  //绑定的物理网卡ip
    public boolean dei_modify;
    /**
     * Creates a new instance of NetworkInterfaceCardInfo
     */
    public NetworkInterfaceCardInfo() {
       
    }
 
    public String btnMac = "080027E31B6C";

    public boolean isVmNICIsUsable() {
        return vmNICIsUsable;
    }

    public void setVmNICIsUsable(boolean vmNICIsUsable) {
        this.vmNICIsUsable = vmNICIsUsable;
    }

   

    public String getVmNICName() {
        return vmNICName;
    }

    public void setVmNICName(String vmNICName) {
        this.vmNICName = vmNICName;
    }

    public String getBindPhysicsNicIpAddress() {
        return bindPhysicsNicIpAddress;
    }

    public void setBindPhysicsNicIpAddress(String bindPhysicsNicIpAddress) {
        this.bindPhysicsNicIpAddress = bindPhysicsNicIpAddress;
    }

    public int getCableConnected() {
        return cableConnected;
    }

    public void setCableConnected(int cableConnected) {
        this.cableConnected = cableConnected;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void handleMACRefresh() {
        
        edit_macAddress = macAddress;
        macAddress = btnMac;
    }
    public int getVmNICNum() {
        return vmNICNum;
    }

    public void setVmNICNum(int vmNICNum) {
        this.vmNICNum = vmNICNum;
    }

    public String getStrCableConnected() {
        return strCableConnected;
    }

    public void setStrCableConnected(String strCableConnected) {
        this.strCableConnected = strCableConnected;
    }

    public boolean isBoolCableConnected() {
        return boolCableConnected;
    }

    public void setBoolCableConnected(boolean boolCableConnected) {
        this.boolCableConnected = boolCableConnected;
    }

    public String getEdit_macAddress() {
        return edit_macAddress;
    }

    public void setEdit_macAddress(String edit_macAddress) {
        this.edit_macAddress = edit_macAddress;
    }
    
    public String editUser() {
        if(boolCableConnected==true){
            cableConnected = 1;
        }else{
             cableConnected = 0;        
        }
        strCableConnected = setResource("net_cableConnected_" + cableConnected);
        
        return "vm_vminfotab?faces-redirect=true";
    }
    
    public String cancleUser() {
         macAddress = edit_macAddress;
        
        return "vm_vminfotab?faces-redirect=true";
    }
    
    public String setResource(String resource) {
        return java.util.ResourceBundle.getBundle("com/marstor/msa/common/resources/messages").getString(resource);
    }

    public String getBindPhysicsNIC() {
        return bindPhysicsNIC;
    }

    public void setBindPhysicsNIC(String bindPhysicsNIC) {
        this.bindPhysicsNIC = bindPhysicsNIC;
    }

    public boolean isDei_modify() {
        return dei_modify;
    }

    public void setDei_modify(boolean dei_modify) {
        this.dei_modify = dei_modify;
    }
    
}
