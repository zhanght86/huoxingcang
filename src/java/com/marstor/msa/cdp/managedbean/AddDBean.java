/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.mirrorhost.CDPConstants;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.VolumeGroupInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.util.InterfaceFactory;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "addDBean")
@ViewScoped
public class AddDBean {

    private String dNum = "1";
    private String dSize = "1";
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.add_d";
    private String guid = "";
    private String path = "";
    boolean demand = true;
    boolean specifiedName = false;
    String ds;
    String dg;
    private boolean record = false;
    private long registerd;
    private long used;
    private String DName;
    private CdpDiskGroupInfo group;

    public AddDBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ds = request.getParameter("ds");
        guid = request.getParameter("guid");
        path = request.getParameter("path");
        if (request.getParameter("record") != null) {
            record = true;
            registerd = Long.parseLong(request.getParameter("register"));
            used = Long.parseLong(request.getParameter("used"));
        }
        System.out.println("AddDBean GUID:" + guid);
        System.out.println("AddDBean PATH:" + path);
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        group = cdp.getDiskGroupInfo(guid);
        dg = group.getDiskGroupName();
    }

    public String addD() {
        if (!checkNull() || !checkFormat()) {
            return null;
        }

        if (this.specifiedName) {
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            CdpDiskInfo[] disks = cdp.getDiskInfos(guid, path);
            if (disks != null && disks.length > 0) {
                for (CdpDiskInfo disk : disks) {
                    String[] names = disk.getDiskName().split("_");
                    if (this.DName.equals(names[names.length - 1])) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "nameExists"), global.get("error_mark")));
                        return null;
                    }
                }
            }
        }

        if (record) {
            long registerSize = registerd * 1024L * 1024L * 1024L * 1024L;
            long avialableSize = registerSize - used;
            long creating = Integer.parseInt(dNum) * Integer.parseInt(dSize) * CDPConstants.GB;
            if (creating > avialableSize) {
                String message = res.get(basename, "regSize") + registerSize / (double) (1024L * 1024L * 1024L) + "GB    "
                        + res.get(basename, "aviSize") + avialableSize / (double) (1024L * 1024L * 1024L) + "GB    "
                        + res.get(basename, "gSize") + creating / (double) (1024L * 1024L * 1024L) + "GB";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, global.get("error_mark")));
                return null;
            }
        }

        //是否超出卷组可用容量
        String volumeName = this.path.split("/")[1];
        String availableSize = null;
        VolumeInterface volumeInterface = InterfaceFactory.getVolumeInterfaceInstance();
        VolumeGroupInformation[] volumes = volumeInterface.getAllVolumeGroup();
        for (VolumeGroupInformation v : volumes) {
            if (v.getName().equals(volumeName)) {
                availableSize = v.getAvailableSize();
                break;
            }
        }

        double availableDisk = 0;
        String diskUnit = "G";
        if (availableSize != null && (!availableSize.equalsIgnoreCase("")) && (!availableSize.equalsIgnoreCase("0"))) {
            double available = Double.valueOf(availableSize.substring(0, availableSize.length() - 1));
            diskUnit = availableSize.charAt(availableSize.length() - 1) + "";
            if (diskUnit.equalsIgnoreCase("B")) {
                availableDisk = available / (1024 * 1024 * 1024);
            } else if (diskUnit.equalsIgnoreCase("K")) {
                availableDisk = available / (1024 * 1024);
            } else if (diskUnit.equalsIgnoreCase("M")) {
                availableDisk = available / 1024;
            } else if (diskUnit.equalsIgnoreCase("G")) {
                availableDisk = available;
            } else if (diskUnit.equalsIgnoreCase("T")) {
                availableDisk = available * 1024;
            }

        }
        long creating = Integer.parseInt(dNum) * Integer.parseInt(dSize);
        if(creating > availableDisk){
            RequestContext.getCurrentInstance().execute("create.show()");
            return null;
        }

        for (int i = 0; i < Integer.parseInt(dNum); i++) {
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            String r;
            if (this.specifiedName) {
                r = cdp.createDiskJoinGroup(guid, path, dSize, demand, this.DName);
            } else {
                r = cdp.createDiskJoinGroup(guid, path, dSize, demand, null);
            }

            if (r == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0") + (i + 1)
                                + res.get(basename, "fail1"), global.get("error_mark")));
                return null;
            }
        }
        RequestContext.getCurrentInstance().execute("addd.show()");
        return null;
    }
    
    public String doCreate(){
        for (int i = 0; i < Integer.parseInt(dNum); i++) {
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            String r;
            if (this.specifiedName) {
                r = cdp.createDiskJoinGroup(guid, path, dSize, demand, this.DName);
            } else {
                r = cdp.createDiskJoinGroup(guid, path, dSize, demand, null);
            }

            if (r == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0") + (i + 1)
                                + res.get(basename, "fail1"), global.get("error_mark")));
                return null;
            }
        }
        RequestContext.getCurrentInstance().execute("addd.show()");
        return null;
    }

    public String getDName() {
        return DName;
    }

    public void setDName(String DName) {
        this.DName = DName;
    }

    public boolean isSpecifiedName() {
        return specifiedName;
    }

    public void setSpecifiedName(boolean specifiedName) {
        this.specifiedName = specifiedName;
    }

    public String toLog() {
        String param = "GUID=" + guid;
        return "lun?faces-redirect=true&amp;" + param;
    }

    public void toDgs() {
        RequestContext.getCurrentInstance().execute("window.location.href='dgs.xhtml'");
    }

    public boolean checkNull() {

        if (!this.specifiedName && "".equals(dNum)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null0"), global.get("error_mark")));
            return false;
        }

        if ("".equals(dSize)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null1"), global.get("error_mark")));
            return false;
        }
        if (this.specifiedName && "".equals(DName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null2"), global.get("error_mark")));
            return false;
        }

        return true;
    }

    public boolean checkFormat() {
        if (!this.specifiedName && !DGUtility.checkNum(dNum, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format0"), global.get("error_mark")));
            return false;
        }

        if (!DGUtility.checkNum(dSize, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format1"), global.get("error_mark")));
            return false;
        }

        if (this.specifiedName && !DGUtility.checkEntityName(DName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "nameFormat"), global.get("error_mark")));
            return false;
        }

        if (Integer.parseInt(dNum) + group.getDiskCount() > 10) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format2"), global.get("error_mark")));
            return false;
        }
        if (Integer.parseInt(dSize) > 999999) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format3"), global.get("error_mark")));
            return false;
        }

        return true;
    }

    public String back() {
        if (ds == null) {
            return "dgs.xhtml?faces-redirect=true";
        } else {
            return "ds.xhtml?faces-redirect=true&amp;guid=" + guid + "&amp;path=" + path;
        }
    }

    public String getDNum() {
        return dNum;
    }

    public void setDNum(String dNum) {
        this.dNum = dNum;
    }

    public String getDSize() {
        return dSize;
    }

    public void setDSize(String dSize) {
        this.dSize = dSize;
    }

    public boolean isDemand() {
        return demand;
    }

    public void setDemand(boolean demand) {
        this.demand = demand;
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

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }
}
