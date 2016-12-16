/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.managedbean;

import com.marstor.msa.backup.util.Util;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "backupIP")
@ViewScoped
public class BackupSetIPBean implements Serializable{
    public String ip;
    private MSAResource res = new MSAResource();
    
    public BackupSetIPBean(){
    
    }
    
    public void setIP(String ip){
        this.ip = ip;
    }
    
    public String getIP(){
        return ip;
    }
    
    public void configIP(){
        if(ip.equals("")){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Util.getString("IPNotNull"), ""));
            return;
        }

        if(!ValidateUtility.checkIP(ip)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Util.getString("ErrorIP"), ""));
            return;
        }
                
        boolean configIP = InterfaceFactory.getMBAInterfaceInstance().configIP(ip);
        if(configIP){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("SettingIPAddressSuccess"), ""));
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("SettingIPAddressFailed"), ""));
        }
    }
    
}
