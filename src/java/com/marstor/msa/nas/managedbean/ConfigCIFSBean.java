/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.bean.CIFSParametersForSharePath;
import com.marstor.msa.nas.bean.ShareName;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class ConfigCIFSBean implements Serializable{

    private boolean startOrNot;
    private boolean previousStartOrNot;
    private String shareName;
    private boolean shareInputEnabled;
    private String path;

    public ConfigCIFSBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        path = request.getParameter("path");
        CIFSParametersForSharePath param = InterfaceFactory.getNASInterfaceInstance().getSpecifiedCIFSParameters(path);

        if (param == null) {
            return;
        }
        if (param.isStart()) {
            this.previousStartOrNot = true;
            this.startOrNot = true;
        } else {
            this.previousStartOrNot = false;
            this.startOrNot = false;
        }
        if (!startOrNot) {
            shareName = "";
            shareInputEnabled = true;
        } else {
            shareName = param.getSharename();
            shareInputEnabled = false;
        }

    }

    public boolean isPreviousStartOrNot() {
        return previousStartOrNot;
    }

    public void setPreviousStartOrNot(boolean previousStartOrNot) {
        this.previousStartOrNot = previousStartOrNot;
    }

    public boolean isShareInputEnabled() {
        return shareInputEnabled;
    }

    public void setShareInputEnabled(boolean shareInputEnabled) {
        this.shareInputEnabled = shareInputEnabled;
    }

    public boolean isStartOrNot() {
        return startOrNot;
    }

    public void setStartOrNot(boolean startOrNot) {
        this.startOrNot = startOrNot;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void listen() {
        if (this.startOrNot) {
            this.shareInputEnabled = false;

        } else {
            this.shareInputEnabled = true;
        }
    }

    public String save() {
        if (startOrNot && this.shareName.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyShare"), res.get("emptyShare")));
            return null;
        }
//        if (startOrNot) {
//            if (!shareName.matches("^[a-zA-Z0-9]+$") || shareName.length() > 32) {

//                MSAResource res = new MSAResource();
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("formatFailed"), res.get("formatFailed")));
//                return null;
//            }
//        }

        if (startOrNot) {
            ShareName share = InterfaceFactory.getNASInterfaceInstance().judgeShareNameOccupiedOrNot(this.shareName);
            if (share.isOccupiedOrNot()) { //如果共享名称已被占用
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("shareOccupied"), res.get("shareOccupied")));
                return null;
            }
            CIFSParametersForSharePath param = new CIFSParametersForSharePath(path, this.shareName);
            boolean flag = InterfaceFactory.getNASInterfaceInstance().configCIFSForSharePath(param);
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("configFailed"), res.get("configFailed")));
                Debug.print("config cifs" + flag);
                return null;
            }
        } else {
            if (this.previousStartOrNot) {
                RequestContext.getCurrentInstance().execute("turnoffcifs.show()");
                return null;
            }

        }

        return "nas_path?faces-redirect=true";
    }

    public String turnOffCIFS() {
        boolean flag = InterfaceFactory.getNASInterfaceInstance().turnoffCIFSForSharePath(this.path);
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("configFailed"), res.get("configFailed")));
            Debug.print("config cifs" + flag);
            return null;
        }
        return "nas_path?faces-redirect=true";
    }
}
