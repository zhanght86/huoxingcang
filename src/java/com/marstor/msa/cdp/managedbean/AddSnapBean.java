/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@ViewScoped
public class AddSnapBean {

    private String dgName;
    private List volumes;
    private String volume;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.add_snap";
    private String guid = "";
    private String path;
    private String snapAlias;
    private String dg;
    private List<String> groupNames;

    public AddSnapBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        path = request.getParameter("path");
        guid = request.getParameter("guid");
        dg = request.getParameter("dg");
        this.groupNames = Arrays.asList(request.getParameterValues("groupName"));
    }

    public String addDG() {
        if (this.snapAlias == null || snapAlias.equalsIgnoreCase("")) {
            //Constants.showWarningMessage(this.setResource("manual.snapshot.anotherName.null"));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("aliasNotEmpty"), ""));
            return null;
        }
        if (snapAlias.length() < 21) {
            if (!snapAlias.matches("[a-zA-Z0-9]+")) {
                //快照名称由字母、数字或两者组合而成，且最长不超过20个字符(名称不能为“AUTO”)。
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("snapComposed"), ""));
                return null;
            }
            if (this.snapAlias.equalsIgnoreCase("AUTO")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("aliasAuto"), ""));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("aliasMaxLength"), ""));
            return null;
        }

        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        boolean bRet = cdp.createCdpManualSnapshot(path, guid, snapAlias);
        System.out.println("AddDGBean GUID0:" + guid);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return null;
        } else {
            String param = "path=" + this.path + "&guid=" + this.guid + "&level=2";
            for (String name : this.groupNames) {
                param += "&groupName=" + name;
            }
            return "snapshot_r?faces-redirect=true&amp;" + param;
        }
    }

    public boolean isExist() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        List<CdpDiskGroupInfo> dgs = cdp.getDiskGroupInfos();
        if (dgs == null) {
            return false;
        }

        for (CdpDiskGroupInfo dg : dgs) {
            if (dgName.equalsIgnoreCase(dg.getDiskGroupName())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "existError"), global.get("error_mark")));
                return true;
            }
        }
        return false;
    }

    public String getDgName() {
        return dgName;
    }

    public void setDgName(String dgName) {
        this.dgName = dgName;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public List getVolumes() {
        return volumes;
    }

    public void setVolumes(List volumes) {
        this.volumes = volumes;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSnapAlias() {
        return snapAlias;
    }

    public void setSnapAlias(String snapAlias) {
        this.snapAlias = snapAlias;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }
    
    public String back() {
        String param = "guid=" + this.guid + "&path=" + this.path + "&level=2";
        for(String name : this.groupNames){
            param += "&groupName=" + name;
        }
        return "snapshot_r?faces-redirect=true&amp;" + param;
    }
}
