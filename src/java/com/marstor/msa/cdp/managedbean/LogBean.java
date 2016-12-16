/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpLogPolicy;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SlideEndEvent;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "logBean")
@ViewScoped
public class LogBean {

    private String percent;
    private String size;
    private int keepTime = 24;
    private int fullOption = 0;
    private boolean tillFull = false;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.log";
    private String guid ="";
    private String path = "";
    private CdpLogPolicy log;
    private CdpDiskGroupInfo group;

    public LogBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        guid = request.getParameter("guid");
        path = request.getParameter("path");
        initLog();
    }
    
    public final void initLog(){
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        group = cdp.getDiskGroupInfo(guid);
        this.log = group.getLogPolicy();
        setDefault();
    }
    
    public String getts(){
        return String.valueOf(group.getTotalDiskSize() / (1024L * 1024L * 1024L)) + "GB";
    }
    
    public void setDefault(){
        percent = String.valueOf((log.getLogSize() * 100) / (group.getTotalDiskSize()));
        size = String.valueOf(log.getLogSize() / (1024L * 1024L * 1024L));
        keepTime = log.getLogKeepTime();
        if (keepTime == 100000) {
            tillFull = true;
            keepTime = 0;
        } else {
            tillFull = false;
        }
        fullOption = log.getLogFullOption();
    }
    
    public String setLog(){
        if (!checkNull() || !checkFormat() ) {
            return null;
        }
        
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        boolean success = cdp.setLogPolicy(path, guid, 
                Long.parseLong(size) * 1024L * 1024L * 1024L, keepTime, fullOption);
        if(!success){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            return null;
        }else{
            return "dgs?faces-redirect=true";
        }        
    }
    
    public boolean checkFormat() {
        if (!DGUtility.checkShareName(size)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format0"), global.get("error_mark")));
            return false;
        }
        if (!DGUtility.checkShareName(String.valueOf(keepTime))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format1"), global.get("error_mark")));
            return false;
        }
        return true;
    }
    
    private boolean checkNull() {
        if (size == null || size.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null0"), global.get("error_mark")));
            return false;
        }
        if (String.valueOf(keepTime).equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null1"), global.get("error_mark")));
            return false;
        }
        return true;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getKeepTime() {
        return keepTime;
    }

    public void setKeepTime(int keepTime) {
        this.keepTime = keepTime;
    }

    public int getFullOption() {
        return fullOption;
    }

    public void setFullOption(int fullOption) {
        this.fullOption = fullOption;
    }

    public boolean isTillFull() {
        return tillFull;
    }

    public void setTillFull(boolean tillFull) {
        this.tillFull = tillFull;
    }

    
    public void onSlideEnd(SlideEndEvent event) {
        FacesMessage msg = new FacesMessage("Slide Ended", "Value: " + event.getValue());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public MSAGlobalResource getGlobal() {
        return global;
    }

    public void setGlobal(MSAGlobalResource global) {
        this.global = global;
    }

    public CdpLogPolicy getLog() {
        return log;
    }

    public void setLog(CdpLogPolicy log) {
        this.log = log;
    }

    public CdpDiskGroupInfo getGroup() {
        return group;
    }

    public void setGroup(CdpDiskGroupInfo group) {
        this.group = group;
    }
    
    
}
