/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.bean.GlobalNFSParameters;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class GlobalNFSDataBean implements Serializable {

    private ArrayList<String> serverVersions = new ArrayList<String>();
    private ArrayList<String> clientVersions = new ArrayList<String>();
    private String selectedServerVersion;
    private String selectedClientVersion;
    private String maxNFSRequestNum = "";
    private String connectQueueLength = "";
    private String maxNFSLockRequestNum = "";
    private String lockQueueLength = "";

    public GlobalNFSDataBean() {
        GlobalNFSParameters params = InterfaceFactory.getNASInterfaceInstance().getGlobalNFSParameters();
        if (params == null) {
            return;
        }
        this.maxNFSRequestNum = String.valueOf(params.getMaxNFSRequestNum());
        this.connectQueueLength = String.valueOf(params.getConnectQueueLength());
        this.maxNFSLockRequestNum = String.valueOf(params.getMaxNFSLockRequestNum());
        this.lockQueueLength = String.valueOf(params.getLockQueueLength());
        serverVersions.add("NFS4");
        serverVersions.add("NFS3");
        clientVersions.add("4");
        clientVersions.add("3");
        clientVersions.add("2");

        // this.selectedServerVersion = String.valueOf(params.getServerVersion());
        int serverVersion = params.getServerVersion();
        if (serverVersion == 4) {
            this.selectedServerVersion = "NFS4";
        } else if (serverVersion == 3) {
            this.selectedServerVersion = "NFS3";
        }
        // this.selectedServerVersion = "NFS3";
        this.selectedClientVersion = String.valueOf(params.getClientVersion());

    }

    public String getSelectedClientVersion() {
        return selectedClientVersion;
    }

    public void setSelectedClientVersion(String selectedClientVersion) {
        this.selectedClientVersion = selectedClientVersion;
    }

    public String getSelectedServerVersion() {
        return selectedServerVersion;
    }

    public void setSelectedServerVersion(String selectedServerVersion) {
        this.selectedServerVersion = selectedServerVersion;
    }

    public ArrayList<String> getClientVersions() {
        return clientVersions;
    }

    public void setClientVersions(ArrayList<String> clientVersions) {
        this.clientVersions = clientVersions;
    }

    public String getConnectQueueLength() {
        return connectQueueLength;
    }

    public void setConnectQueueLength(String connectQueueLength) {
        this.connectQueueLength = connectQueueLength;
    }

    public String getLockQueueLength() {
        return lockQueueLength;
    }

    public void setLockQueueLength(String lockQueueLength) {
        this.lockQueueLength = lockQueueLength;
    }

    public String getMaxNFSLockRequestNum() {
        return maxNFSLockRequestNum;
    }

    public void setMaxNFSLockRequestNum(String maxNFSLockRequestNum) {
        this.maxNFSLockRequestNum = maxNFSLockRequestNum;
    }

    public String getMaxNFSRequestNum() {
        return maxNFSRequestNum;
    }

    public void setMaxNFSRequestNum(String maxNFSRequestNum) {
        this.maxNFSRequestNum = maxNFSRequestNum;
    }

    public ArrayList<String> getServerVersions() {
        return serverVersions;
    }

    public void setServerVersions(ArrayList<String> serverVersions) {
        this.serverVersions = serverVersions;
    }

    public void save() {

        //÷¥––±£¥Ê√¸¡Ó
        Long temp;
        //this.connectQueueLength = connectQueueLength;
        if (this.maxNFSRequestNum.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyMaxReq"), res.get("emptyMaxReq")));
            return;
        }
        if (!this.maxNFSRequestNum.matches("^[0-9]+$")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("limitMaxReq"), res.get("limitMaxReq")));
            return;
        }
        temp = Long.parseLong(maxNFSRequestNum);
        if (temp < 1 || temp > 2100000000) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("limitMaxReq"), res.get("limitMaxReq")));
            return;
        }
        if (this.connectQueueLength.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyConnectQueue"), res.get("emptyConnectQueue")));
            return;
        }
        if (!this.connectQueueLength.matches("^[0-9]+$")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("limitConnectQueue"), res.get("limitConnectQueue")));
            return;
        }
        temp = Long.parseLong(this.connectQueueLength);
        if (temp < 0 || temp > 2100000000) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("limitConnectQueue"), res.get("limitConnectQueue")));
            return;
        }
        if (this.maxNFSLockRequestNum.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyLockedReq"), res.get("emptyLockedReq")));
            return;
        }
        if (!this.maxNFSLockRequestNum.matches("^[0-9]+$")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("limitLockedReq"), res.get("limitLockedReq")));
            return;
        }
        temp = Long.parseLong(this.maxNFSLockRequestNum);
        if (temp < 1 || temp > 2100000000) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("limitLockedReq"), res.get("limitLockedReq")));
            return;
        }
        if (this.lockQueueLength.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyLockedQueue"), res.get("emptyLockedQueue")));
            return;
        }
        if (!this.lockQueueLength.matches("^[0-9]+$")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("limitlockedQueue"), res.get("limitlockedQueue")));
            return;
        }
        temp = Long.parseLong(this.lockQueueLength);
        if (temp < 32 || temp > 2100000000) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("limitlockedQueue"), res.get("limitlockedQueue")));
            return;
        }
        int serverVersion = 0;
        if (this.selectedServerVersion.equals("NFS4")) {
            serverVersion = 4;
        } else if (this.selectedServerVersion.equals("NFS3")) {
            serverVersion = 3;
        }
        GlobalNFSParameters param = new GlobalNFSParameters(serverVersion, Integer.parseInt(this.selectedClientVersion), Integer.parseInt(this.maxNFSRequestNum), Integer.parseInt(this.connectQueueLength), Integer.parseInt(maxNFSLockRequestNum), Integer.parseInt(lockQueueLength));
        boolean flag = InterfaceFactory.getNASInterfaceInstance().configGlobalNFSParameters(param);
        if (!flag) {
            Debug.print("set global nfs" + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setFailed"), res.get("setFailed")));
            return;
        }
        MSAResource res = new MSAResource();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("setSuccess"), res.get("setSuccess")));


    }
}
