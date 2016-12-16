/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.synchost.managedbean;

import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.sync.bean.MarsHost;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class ConfigRemoteMarsHost implements Serializable{
    
    private List<MarsHost> marsHosts;
    private MarsHost selected;
    private MsaSYNCInterface syncObj;
    private List<String> config;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String selectedIP;

    public ConfigRemoteMarsHost() {
        syncObj= InterfaceFactory.getSYNCInterfaceInstance();
        marsHosts = syncObj.getMarsHost();
        config = syncObj.getConfigMarsHostIP();
    }
   
    public void judgeCanDelete(){
        System.out.println("judge can delete "+selected.getIp());
        if(config.contains(selected.getIp())){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("notDelete"), global.get("error_mark")));
            return ;
        }
        selectedIP = selected.getIp();
        RequestContext.getCurrentInstance().execute("deleteHost.show()");
        //oncomplete="deleteHost.show();"
    }
    
    public void deleteMarsHost(){
        System.out.println("delete "+selected.getIp());
        if(syncObj.deleteMarsHost(selected)){
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("deleteSuccess"), global.get("error_mark")));
            marsHosts = syncObj.getMarsHost();
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("deleteFailed"), global.get("error_mark")));
        }
    }
    
    public String modifyMarsHost(){
        System.out.println("modify "+selected.getIp());
//        if(config.contains(selected.getIp())){
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("notEdit"), global.get("error_mark")));
//            return null;
//        }
        String param = "ip=" + selected.getIp();
        return "sync_edit_mars_host?faces-redirect=true&amp;" + param;
    }

    public List<MarsHost> getMarsHosts() {
        return marsHosts;
    }

    public void setMarsHosts(List<MarsHost> marsHosts) {
        this.marsHosts = marsHosts;
    }

    public MarsHost getSelected() {
        return selected;
    }

    public void setSelected(MarsHost selected) {
        this.selected = selected;
    }

    public String getSelectedIP() {
        return selectedIP;
    }

    public void setSelectedIP(String selectedIP) {
        this.selectedIP = selectedIP;
    }
   
}
