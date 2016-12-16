/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.Host;
import com.marstor.msa.cdp.util.AgentUtility;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;
import com.marstor.msa.util.InterfaceFactory;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;;

@ManagedBean(name = "modifyCBean")
@ViewScoped
public class ModifyClientBean {
    private String ip;
    private String oIP;
    private int os;
    private String port;
    private String hostname;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.add_client";
    
    public ModifyClientBean(){
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        hostname = request.getParameter("hostname");
        os = Integer.valueOf(request.getParameter("os"));
        oIP = request.getParameter("ip");
        ip = oIP;
        port = request.getParameter("port");
    }
    
    public String modifyClient(){
        if (!validate()) {
            return null;
        }
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        cdp.modifyHost(ip, port, os, oIP);
        
        return "clients?faces-redirect=true";
    }
    
    public void change(){
        
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
            if(info.ip.equals(oIP)){
                continue;
            }
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
        
        if (!DGUtility.checkLength(port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format2"), global.get("error_mark")));
            return false;
        }
        
        if (!AgentUtility.checkPort(port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "range1"), global.get("error_mark")));
            return false;
        }

        return true;
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


    public int getOs() {
        return os;
    }

    public void setOs(int os) {
        this.os = os;
    }


}
