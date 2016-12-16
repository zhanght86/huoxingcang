/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "addDGBean")
@ViewScoped
public class AddDGBean {

    private String dgName;
    private List volumes;
    private String volume;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.add_dg";
    private String guid = "";

    public AddDGBean() {
        initVolumes();
    }

    public String addDG() {
        if (!checkNull() || !checkFormat() || isExist()) {
            return null;
        }
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        guid = cdp.createDiskGroup(volume, dgName);
        System.out.println("AddDGBean GUID0:" + guid);
        if (guid != null) {
            RequestContext.getCurrentInstance().execute("adddg.show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
        }
        return null;
    }

    public String toAdd_d() {
        String path = "/" + volume + "/DISK/" + dgName ;
        String param = "guid=" + guid + "&path=" + path;
        System.out.println("AddDGBean GUID:" + guid);
        System.out.println("AddDGBean PATH:" + path);
        return "add_d?faces-redirect=true&amp;" + param;
    }

    public void toDgs() {
        RequestContext.getCurrentInstance().execute("window.location.href='dgs.xhtml'");
    }

    public boolean checkNull() {
        if ("".equals(dgName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "nullError"), global.get("error_mark")));
            return false;
        }
        return true;
    }

    public boolean checkFormat() {
        if (!DGUtility.checkShareName(dgName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "formatError"), global.get("error_mark")));
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
            if (dgName.equalsIgnoreCase(dg.getDiskGroupName())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "existError"), global.get("error_mark")));
                return true;
            }
        }
        return false;
    }

    public final void initVolumes() {
        VolumeInterface common = InterfaceFactory.getVolumeInterfaceInstance();
        VolumeGroupInformation[] vgs = common.getAllVolumeGroup();
        if (vgs == null) {
            return;
        }
        volumes = new ArrayList();
        for (VolumeGroupInformation vg : vgs) {
            volumes.add(vg.name);
        }
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
}
