/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class CIFS implements Serializable{

    private boolean cifsOnOrOff;
    private String shareName="";
    private boolean shareNameTextStatus;
    private boolean configButtonStatus;
    private boolean firstConfig;
    private String errorInfo;
    private String statusDisplay;
    
    public CIFS() {
        this.cifsOnOrOff = false;
        this.configButtonStatus = true;
    }

    public CIFS(boolean cifsOnOrOff, String shareName) {
        this.cifsOnOrOff = cifsOnOrOff;
        this.shareName = shareName;
        if(cifsOnOrOff) {
            this.shareNameTextStatus = false;
            this.configButtonStatus = false;
        }else {
              this.shareNameTextStatus = true;
              this.configButtonStatus = true;
        }
        
    }

    public String getStatusDisplay() {
        if(this.cifsOnOrOff) {
            this.statusDisplay=Constant.open;
        }else {
            this.statusDisplay=Constant.notOpen;
        }
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public boolean isFirstConfig() {
        return firstConfig;
    }

    public void setFirstConfig(boolean firstConfig) {
        this.firstConfig = firstConfig;
    }

    public boolean isConfigButtonStatus() {
        
        return configButtonStatus;
    }

    public void setConfigButtonStatus(boolean configButtonStatus) {
        this.configButtonStatus = configButtonStatus;
    }

    
    public boolean isCifsOnOrOff() {
        return cifsOnOrOff;
    }

    public void setCifsOnOrOff(boolean cifsOnOrOff) {
        this.cifsOnOrOff = cifsOnOrOff;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public boolean isShareNameTextStatus() {
        return shareNameTextStatus;
    }

    public void setShareNameTextStatus(boolean shareNameTextStatus) {
        this.shareNameTextStatus = shareNameTextStatus;
    }
}
