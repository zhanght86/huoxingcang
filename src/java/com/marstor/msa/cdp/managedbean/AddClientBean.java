/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.Host;
import com.marstor.msa.cdp.util.AgentUtility;
import com.marstor.msa.cdp.util.ConvertUtility;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.VolumeGroupInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "addCBean")
@RequestScoped
public class AddClientBean {

    private String ip;
    private String[] oss = {"Windows", "Linux", "Solaris", "AIX", "HP-UNIX", "Other"};
    private String os;
    private String port = "1100";
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.add_client";

    public AddClientBean() {
    }

    public String addClient() {
        if (!validate()) {
            return null;
        }
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        cdp.addHost(ip, port, ConvertUtility.getIntFromOS(os));
        return "clients?faces-redirect=true";
    }

    public boolean validate() {
        if ("".equals(ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "nullError"), global.get("error_mark")));
            return false;
        }
        if (!DGUtility.checkIP(ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "formatError"), global.get("error_mark")));
            return false;
        }

        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        List<Host> clients = cdp.getAllHosts();
        for (Host info : clients) {
            if (ip.equals(info.getIp())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "existError"), global.get("error_mark")));
                return false;
            }
        }
        
        if ("".equals(port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null1"), global.get("error_mark")));
            return false;
        }
        if (!DGUtility.checkNum(port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format1"), global.get("error_mark")));
            return false;
        }
        
        if (!AgentUtility.checkPort(port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "range1"), global.get("error_mark")));
            return false;
        }

        return true;
    }

    public boolean isExist() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        List<CdpDiskGroupInfo> dgs = cdp.getDiskGroupInfos();
        if (dgs == null) {
            return false;
        }

        for (CdpDiskGroupInfo dg : dgs) {
            if (ip.equalsIgnoreCase(dg.getDiskGroupName())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "existError"), global.get("error_mark")));
                return true;
            }
        }
        return false;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String[] getOss() {
        return oss;
    }

    public void setOss(String[] oss) {
        this.oss = oss;
    }
}
