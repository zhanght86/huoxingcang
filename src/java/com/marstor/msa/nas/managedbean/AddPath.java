/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.MySession;
import com.marstor.msa.nas.model.ShareListBean;
import com.marstor.msa.nas.bean.SharePath;
import com.marstor.msa.nas.model.SharePathTable;
import com.marstor.msa.nas.bean.Volume;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.List;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
public class AddPath  implements Serializable{

    private String pathName = "";
    private String selectedVolume = "";
    private List<String> volumeNames = new ArrayList<String>();
    private String errorinfo;
    private List<String> fileSysList = new ArrayList<String>();
    private String radioValue = "";

    public AddPath() {
//        volumeNames.add("SYSVOL");
//        volumeNames.add("test");
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        String fileSysStr = request.getParameter("fileSysList");
        Debug.print("param: " + fileSysStr);
        if (fileSysStr != null) {
            String array[] = fileSysStr.trim().split(",");
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    this.fileSysList.add(array[i]);
                }
            }

        }
        ArrayList<Volume> volumes = InterfaceFactory.getNASInterfaceInstance().getVolumes();
        for (Volume v : volumes) {
            volumeNames.add(v.getName());
        }
        this.radioValue = "3";

    }

    public String getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(String radioValue) {
        this.radioValue = radioValue;
    }

    public String getSelectedVolume() {
        return selectedVolume;
    }

    public void setSelectedVolume(String selectedVolume) {
        this.selectedVolume = selectedVolume;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public List<String> getVolumeNames() {
        return volumeNames;
    }

    public void setVolumeNames(List<String> volumeNames) {
        this.volumeNames = volumeNames;
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

//    public String  addPath() {
//        if(this.pathName.equals("")) {
//            
//            RequestContext.getCurrentInstance().addCallbackParam("result", "error");
//        }
//        ShareListBean  share = MySession.getShareFromSession();
//        String  path = this.selectedVolume + "/NAS/" + this.pathName;
//        share.addPath(path);
//        RequestContext.getCurrentInstance().addCallbackParam("result", "success");
//        return "nas_path?faces-redirect=true";
//    }
    public String save() {
        if (this.pathName.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyPath"), res.get("emptyPath")));
            return null;
        }
        String completePath = this.selectedVolume + "/NAS/" + this.pathName;
        if (this.fileSysList.contains(completePath)) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("existPath"), res.get("existPath")));
            return null;
        }
        if (!this.pathName.matches("^[a-zA-Z0-9]+$")) {
//            
//            FacesMessage message = new FacesMessage(
//                    FacesMessage.SEVERITY_WARN, "目录名称格式错误","目录名由字母和数字组成，且长度不超过32位");
//            throw new ValidatorException(message); 
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("pathCompose"), res.get("pathCompose")));
            return null;
        }
        if (this.pathName.length() > 16) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("pathLength"), res.get("pathLength")));
            return null;
        }
        SharePath path = new SharePath(this.selectedVolume + "/NAS/" + this.pathName, this.selectedVolume);
        int casesensitivity;
        try {
            Debug.print("radioValue: " + this.radioValue);
            casesensitivity = Integer.parseInt(this.radioValue);
        } catch (Exception e) {
            Debug.print("Exception: " + e.getMessage());
            return null;
        }
        boolean flag = InterfaceFactory.getNASInterfaceInstance().createSharePath(path, casesensitivity);
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createFailed"), res.get("createFailed")));
            return null;
        }
        return "nas_path?faces-redirect=true";
    }

    public String skipToPathManager() {
        return "nas_path?faces-redirect=true";
    }
}
