/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.ConvertUtility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "disksBean")
@ViewScoped
public class DisksBean {

    private List<CdpDiskGroupInfo> diskGroups;
    private CdpDiskGroupInfo selectedDG = new CdpDiskGroupInfo();
    private CdpDiskInfo selectedD;
    private List<CdpDiskInfo> disks;
    private Map disksMap = new HashMap();
    private List<Details> dgInfo = new ArrayList();
    private Map dgInfos = new HashMap();
    private Map luns = new HashMap();
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.ds";
    private String guid = "";
    private String path = "";
    private boolean disable = false;
    String dg;
    private boolean record = false;
    private long registerd;
    private long used;

    public DisksBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        guid = request.getParameter("guid");
        path = request.getParameter("path");
        if (request.getParameter("record") != null) {
            record = true;
            registerd = Long.parseLong(request.getParameter("register"));
            used = Long.parseLong(request.getParameter("used"));
        }
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        if(cdp.getDiskGroupInfo(guid) != null){
            dg = cdp.getDiskGroupInfo(guid).getDiskGroupName();
        }
        initGroup();
        initDisks();

    }

    public String addDisk() {
        String param = "ds=true&guid=" + guid + "&path=" + path;
        if (record) {
            param = param + "&record=1" + "&register=" + registerd + "&used=" + used;
        }
        return "add_d?faces-redirect=true&amp;" + param;
    }

    public String toVD() {
        String srcPath = this.selectedD.getDiskFileName_Path();
        String param = "guid=" + selectedDG.getDiskGroupGuid() + "&path=" + selectedDG.getDiskGroupPath() 
                + "&srcPath=" + srcPath + "&dSize=" + this.selectedD.getDiskSize() 
                + "&dLun=" + this.selectedD.getLuInfoBean().getlUName()
                + "&gName=" + this.selectedDG.getDiskGroupName();
        if(record){
            param +="&record=1" + "&register=" + registerd
                            + "&used=" + used;
        }
        return "to_vd?faces-redirect=true&amp;" + param;

    }
    
    public String toExtendD(){
        String srcPath = this.selectedD.getDiskFileName_Path();
        double dSize = this.selectedD.getDiskSize()/(double) (1024L * 1024L * 1024L);
        String param = "guid=" + selectedDG.getDiskGroupGuid() + "&path=" + selectedDG.getDiskGroupPath() 
                + "&dName=" + this.selectedD.getDiskName()
                + "&srcPath=" + srcPath + "&dSize=" + (int)dSize 
                + "&dLun=" + this.selectedD.getLuInfoBean().getlUName();
        if(record){
            param +="&record=1" + "&register=" + registerd
                            + "&used=" + used;
        }
        return "extend_d?faces-redirect=true&amp;" + param;
    }

    public String disLUN() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        ViewInformation[] lun = scsi.getLUNView(disks.get(0).getLuInfoBean().getlUName());
        if (lun != null && lun.length > 0) {
            for (int i = 0; i < lun.length; i++) {
                for (int j = 0; j < this.disks.size(); j++) {
                    boolean success = scsi.removeView(this.disks.get(j).getLuInfoBean().getlUName(), lun[i].entry);
                    if (!success) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
                        return null;
                    }
                }
            }
        }
        return addDisk();
    }

    public String cancel() {
        String param = "ds=true&guid=" + guid + "&path=" + path;
        return "ds?faces-redirect=true&amp;" + param;
    }

    public String preAddDisk() {
        if (disks.isEmpty()) {
            return addDisk();
        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        ViewInformation[] lun = scsi.getLUNView(disks.get(0).getLuInfoBean().getlUName());
        if (lun != null && lun.length > 0) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail5"), global.get("error_mark")));
            RequestContext.getCurrentInstance().execute("disLUN.show()");
            return null;
        } else {
            return addDisk();
        }
    }

    public void preDeleteDisk() {
        if (disks.isEmpty()) {
            RequestContext.getCurrentInstance().execute("deleteDisk.show()");
            return;
        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        ViewInformation[] lun = scsi.getLUNView(disks.get(0).getLuInfoBean().getlUName());
        if (lun != null && lun.length > 0) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail5"), global.get("error_mark")));
            RequestContext.getCurrentInstance().execute("deleteLUNDisk.show()");
        } else {
            RequestContext.getCurrentInstance().execute("deleteDisk.show()");
        }
    }

    public void deleteLUNDisk() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        ViewInformation[] lun = scsi.getLUNView(disks.get(0).getLuInfoBean().getlUName());
        if (lun != null && lun.length > 0) {
            scsi = InterfaceFactory.getSCSIInterfaceInstance();
            for (int i = 0; i < lun.length; i++) {
                for (int j = 0; j < this.selectedDG.getDiskCount(); j++) {
                    scsi.removeView(this.disks.get(j).getLuInfoBean().getlUName(), lun[i].entry);
                }
            }
        }
        deleteDisk();
    }

    public void deleteDisk() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        boolean b = cdp.deleteDiskFromGroup(selectedD.getDiskGuid(), selectedD.getDiskName(), path);
        if (!b) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
        } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success0"), global.get("error_mark")));
            initDisks();
//            String param = "guid=" + guid + "&path=" + path;
//            return "ds?faces-redirect=true&amp;" + param;
        }
    }

    public void preRestore() {
        System.out.println(this.selectedD.getDiskName());
        RequestContext.getCurrentInstance().execute("restoreName.show();");
    }

    public void restoreName() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        boolean b = cdp.restoreDiskSignature(this.selectedD.getDiskGuid());
        if (!b) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), global.get("error_mark")));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success1"), global.get("error_mark")));
            initDisks();
        }
    }

    public List<CdpDiskInfo> getDs() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        disks = Arrays.asList(cdp.getDiskInfos(selectedDG.getDiskGroupGuid(), selectedDG.getDiskGroupPath()));
        return disks;
    }

    public final List<CdpDiskInfo> initDisks() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        CdpDiskInfo[] ds = cdp.getDiskInfos(guid, path);
        if (ds != null) {
            disks = Arrays.asList(ds);
        }
        return disks;
    }

    public final void initGroup() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        CdpDiskGroupInfo group = cdp.getDiskGroupInfo(guid);
        this.selectedDG = group;
        String[] status = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(group.getDiskZfsName());

        if ((status[4].equals("1") && status[5].equals("1")) || group.getDiskGroupName().contains(CDPConstants.SNAPSHOT_COPY)) {
            this.disable = true;
        }

    }

    public String getType(String lun) {
        if (lun.startsWith("fc")) {
            return "FC";
        } else if (lun.startsWith("iscsi")) {
            return "iSCSI";
        }
        return "";
    }

    public final void initName() {
        for (int i = 0; i < 10; i++) {
            Details g = new Details();
            g.setName(res.get(basename, "name" + i));
            dgInfo.add(g);
        }
    }

    public final void initDiskGroups() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        this.diskGroups = cdp.getDiskGroupInfos();
    }

    public List<CdpDiskGroupInfo> getDiskGroups() {
        return diskGroups;
    }

    public void setDiskGroups(List<CdpDiskGroupInfo> diskGroups) {
        this.diskGroups = diskGroups;
    }

    public List<CdpDiskInfo> getDisks() {
        return disks;
    }

    public void setDisks(List<CdpDiskInfo> disks) {
        this.disks = disks;
    }

    public CdpDiskGroupInfo getSelectedDG() {
        return selectedDG;
    }

    public void setSelectedDG(CdpDiskGroupInfo selectedDG) {
        this.selectedDG = selectedDG;
    }

    public CdpDiskInfo getSelectedD() {
        return selectedD;
    }

    public void setSelectedD(CdpDiskInfo selectedD) {
        this.selectedD = selectedD;
    }

    public List<Details> getDgInfo() {
        return dgInfo;
    }

    public void setDgInfo(List<Details> dgInfo) {
        this.dgInfo = dgInfo;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }
}
