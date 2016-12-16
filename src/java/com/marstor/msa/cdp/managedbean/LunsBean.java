/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.model.LUNExpansion;
import com.marstor.msa.cdp.util.Debug;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "lunsBean")
@ViewScoped
public class LunsBean {

    private List<CdpDiskGroupInfo> diskGroups;
    private CdpDiskGroupInfo selectedDG = new CdpDiskGroupInfo();
    private ViewInformation selected;
    private List<CdpDiskInfo> disks;
    private int count = 0;
    private String dg;
    private String[] lun;
    private String lunNum;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.lun";
    private String[] dguids;
    private String[] dnames;
    private String[] dNums;
    private ViewInformation[] views;
    private List<LUNExpansion> expansions;
    private Map<String, List<LUNExpansion>> expansionMap = new HashMap();
    String guid;
    private String lunStatus;
    private String path;
    private String btn;
    private boolean disBtn = true;
    private String icon;
    private CdpDiskGroupInfo group;

    public LunsBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        guid = request.getParameter("GUID");
        System.out.println("GUID:" + guid);
        dg = request.getParameter("dg");
        initData(guid);
        initLunStatus();
    }

    public final void initLunStatus() {
        lunStatus = "";
        for (CdpDiskInfo disk : this.disks) {
            if (disk.getLuInfoBean() != null && disk.getLuInfoBean().getOperationalStatus() != null
                    && disk.getLuInfoBean().getOperationalStatus().equalsIgnoreCase("offlining")) {
                this.lunStatus = "£®’˝‘⁄‘›Õ£”≥…‰£©";
                this.btn = "ª÷∏¥”≥…‰";
                this.icon = "startIcon";
                this.disBtn = true;
            }
        }
        if (this.lunStatus.equals("")) {
            for (CdpDiskInfo disk : this.disks) {
                if (disk.getLuInfoBean() != null && disk.getLuInfoBean().getOperationalStatus() != null
                        && disk.getLuInfoBean().getOperationalStatus().equalsIgnoreCase("offline")) {
                    this.lunStatus = "£®‘›Õ£”≥…‰£©";
                    this.btn = "ª÷∏¥”≥…‰";
                    this.icon = "startIcon";
                    this.disBtn = false;
                }
            }
            if (this.lunStatus.equals("")) {
                if (this.views != null && this.views.length > 0) {
                    this.lunStatus = "£®’˝‘⁄”≥…‰£©";
                    this.disBtn = false;
                } else {
                    this.disBtn = true;
                }
                this.btn = "‘›Õ£”≥…‰";
                this.icon = "stopIcon";
            }
        }
        if (this.lunStatus.equals("")) {
            this.disBtn = true;
        }
    }

    public boolean getRender() {
        boolean render;
        if (this.btn.equals("ª÷∏¥”≥…‰")) {
            render = true;
        } else {
            render = false;
        }
        return render;
    }

    public void LU() {
        int len = this.disks.size();
        lus = new String[len];
        for (int i = 0; i < len; i++) {
            lus[i] = disks.get(i).getLuInfoBean().getlUName();
        }
        if (this.btn.equals("ª÷∏¥”≥…‰")) {
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            cdp.onlineLU(this.group.getDiskGroupName(), lus);
        } else {
            RequestContext.getCurrentInstance().execute("disLUN.show()");
            return;
        }
        initData(guid);
        initLunStatus();
    }
    private String[] lus;

    public void offlineLU() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        cdp.offlineLU(this.group.getDiskGroupName(), lus);
        initData(guid);
        initLunStatus();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLunStatus() {
        return lunStatus;
    }

    public void setLunStatus(String lunStatus) {
        this.lunStatus = lunStatus;
    }

    public String getBtn() {
        return btn;
    }

    public void setBtn(String btn) {
        this.btn = btn;
    }

    public boolean getDisBtn() {
        if (this.views == null) {
            return true;
        }
        if (this.views != null && this.views.length == 0) {
            return true;
        }
        return disBtn;
    }

    public void setDisBtn(boolean disBtn) {
        this.disBtn = disBtn;
    }

    public final void initData(String guid) {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        group = cdp.getDiskGroupInfo(guid);
        count = group.getDiskCount();
        dg = group.getDiskGroupName();
        if (count != 0) {
            dguids = new String[count];
            dnames = new String[count];
            dNums = new String[count];
            CdpDiskInfo[] ds = cdp.getDiskInfos(guid, group.getDiskGroupPath());
            disks = Arrays.asList(ds);
            if (ds == null) {
                return;
            }
            for (int i = 0; i < count; i++) {
                if (ds[i] == null) {
                    return;
                }
                if (ds[i].getLuInfoBean() == null) {
                    return;
                }
                dguids[i] = ds[i].getLuInfoBean().getlUName();
                dnames[i] = ds[i].getDiskName();
                dNums[i] = ds[i].getLuInfoBean().getSerialNum();
            }
            initLunView();
        }

    }

    public final void initLunView() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        views = scsi.getLUNView(dguids[0]);
        initExpansionView();
    }

    public void initExpansionView() {
        for (ViewInformation view : views) {
            expansions = new ArrayList();
            for (int i = 0; i < count; i++) {
                LUNExpansion e = new LUNExpansion();
                e.setGuid(dguids[i]);
                e.setName(dnames[i]);
                e.setSerialNum(dNums[i]);
                e.isLoaded = false;
                expansions.add(e);
            }
            expansionMap.put(view.getHostGroupName(), expansions);
        }
    }

    public void getExpansions(String hostGroupName) {
        Debug.print("hostgroupName:" + hostGroupName );
        expansions = expansionMap.get(hostGroupName);
        if (!(expansions.get(0).isLoaded)) {
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            ViewInformation[] vs;
            for (LUNExpansion e : expansions) {
                vs = scsi.getLUNView(e.getGuid());
                for (ViewInformation v : vs) {
                    Debug.print(v.GUID + v.hostGroupName + v.LUN);
                }
                for (ViewInformation v : vs) {
                    if (v.hostGroupName.equals(hostGroupName)) {
                        e.setLun(v.LUN);
                        e.isLoaded = true;
                        break;
                    }
                }
            }
        }
    }

    public void disLUN() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        for (int i = 0; i < count; i++) {
            boolean success = scsi.removeView(dguids[i], selected.getEntry());
            if (!success) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
                return;
            }
        }
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success"), global.get("error_mark")));
        initData(guid);
        initLunStatus();
    }

    public final String[] getLuns(CdpDiskGroupInfo selectedDG) {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        lun = scsi.getAllHostGroupNames();
        return lun;
    }

    public String getType(String lun) {
        if (lun.startsWith("fc")) {
            return "FC";
        } else if (lun.startsWith("iscsi")) {
            return "iSCSI";
        }
        return "";
    }

    public String addLun() {
        if (views != null && views.length > 0) {
            for (int i = 0; i < views.length; i++) {
                if (views[i].hostGroupName.equalsIgnoreCase("all")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "allhostgroup_have"), ""));
                    return null;
                }
            }
        }

        String param = "GUID=" + guid + "&dg=" + this.dg + "&dc=";
        int dc;
        if (dguids == null) {
            dc = 0;
        } else {
            dc = dguids.length;
        }
        param = param + dc;
        if (dc == 0) {
            return "add_lun?faces-redirect=true&amp;" + param;
        } else {
            for (int i = 0; i < dc; i++) {
                param = param + "&dguid" + i + "=" + this.dguids[i]
                        + "&dname" + i + "=" + this.dnames[i];
            }
            return "add_lun?faces-redirect=true&amp;" + param;
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

    public ViewInformation getSelected() {
        return selected;
    }

    public void setSelected(ViewInformation selected) {
        this.selected = selected;
    }

    public String[] getLun() {
        return lun;
    }

    public void setLun(String[] lun) {
        this.lun = lun;
    }

    public String getLunNum() {
        return lunNum;
    }

    public void setLunNum(String lunNum) {
        this.lunNum = lunNum;
    }

    public ViewInformation[] getViews() {
        return views;
    }

    public void setViews(ViewInformation[] views) {
        this.views = views;
    }

    public List<LUNExpansion> getExpansions() {
        return expansions;
    }

    public void setExpansions(List<LUNExpansion> expansions) {
        this.expansions = expansions;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }
}