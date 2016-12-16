/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.model.LUNExpansion;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
@ManagedBean(name = "addlunBean")
@ViewScoped
public class AddLunBean {

    private boolean enable = false;
    private boolean isAll = true;
    private List<CdpDiskGroupInfo> diskGroups;
    private CdpDiskGroupInfo selectedDG = new CdpDiskGroupInfo();
    private String selected = "";
    private List<CdpDiskInfo> disks;
    private int count = 0;
    private ArrayList<String> lun = new ArrayList();
    private int lunNum;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.add_lun";
    private String[] dguids;
    private String[] dnames;
    private String[] dNums;
    private ArrayList<String> views = new ArrayList();
    private List<LUNExpansion> table = new ArrayList();
    private int[] defaults;
    private String dg;
    private String guid;
    private boolean gone = false;

    public AddLunBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        guid = request.getParameter("GUID");
        System.out.println("GUID:" + guid);
        if(request.getParameter("dc") != null){
            count = Integer.parseInt(request.getParameter("dc"));
        }
        
        dg = request.getParameter("dg");
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        CdpDiskGroupInfo group = cdp.getDiskGroupInfo(guid);
        if (count != 0) {
            dguids = new String[count];
            dnames = new String[count];
            dNums = new String[count];
            CdpDiskInfo[] ds = cdp.getDiskInfos(guid, group.getDiskGroupPath());
            if (ds == null) {
                return;
            }
            for (int i = 0; i < count; i++) {
                if (ds[i] != null && ds[i].getLuInfoBean() != null) {
                    dguids[i] = ds[i].getLuInfoBean().getlUName();
                    dnames[i] = ds[i].getDiskName();
                    dNums[i] = ds[i].getLuInfoBean().getSerialNum();
                }
            }
            initLunView();
            getLuns();
            initDefault();
            initTable();
        }
    }

    public void reloadLUN(String name) {
        if (!isAll) {
            if (lun != null && lun.size() > 0 && !lun.get(0).equals("")) {
                name = this.selected;
            }
        }

        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        defaults = scsi.getHostGroupAvailableLuns(name);
        for (int i = 0; i < count; i++) {
            if (defaults != null && defaults.length > i) {
                this.table.get(i).setLun(defaults[i]);
            } else {
                this.table.get(i).setLun(0);
            }
        }
    }

    public final void initDefault() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        defaults = scsi.getHostGroupAvailableLuns("all");
        if (lun != null && !lun.isEmpty()) {
            this.selected = this.lun.get(0);
        }

    }

    public final void initTable() {
        for (int i = 0; i < count; i++) {
            LUNExpansion e = new LUNExpansion();
            e.setGuid(this.dguids[i]);
            e.setName(this.dnames[i]);
            e.setSerialNum(this.dNums[i]);
            if (defaults != null && defaults.length > i) {
                e.setLun(defaults[i]);
            } else {
                e.setLun(0);
            }
            this.table.add(e);
        }
    }

    public final void initLunView() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        ViewInformation[] vs = scsi.getLUNView(dguids[0]);
        int i = 0;
        for (ViewInformation v : vs) {
            views.add(v.hostGroupName);
        }
    }

    public String addLun() {
        RequestContext.getCurrentInstance().execute("PF('block').show()");
        if (this.isAll) {
            this.selected = "";
            if (this.views.size() > 0) {
                RequestContext.getCurrentInstance().execute("PF('block').hide()");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v0"), global.get("error_mark")));
                return null;
            }
        } else {
            if (selected == null || selected.equals("")) {
                RequestContext.getCurrentInstance().execute("PF('block').hide()");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("vtl.lib_lunmapping_set", "hostgroup_null"), ""));
                return null;
            }
        }
        int LUN = -1;
        //手动LUN号情况下，两两不能相同
        if (!enable) {
            ArrayList<Integer> temp = new ArrayList();
            for (int i = 0; i < count; i++) {
                temp.add(table.get(i).getLun());
            }
            ArrayList<Integer> rest;
            for (int i = 0; i < temp.size(); i++) {
                int u = temp.get(i);
                rest = temp;
                rest.remove(i);
                for (int n : rest) {
                    if (rest.contains(u)) {
                        RequestContext.getCurrentInstance().execute("PF('block').hide()");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v2"), global.get("error_mark")));
                        return null;
                    }
                }
            }
        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        ViewInformation[] vs;
        Debug.bDebug = true;
        for (int i = 0; i < count; i++) {
            vs = scsi.getLUNView(table.get(i).getGuid());
            for(ViewInformation info : vs){
                Debug.print(info.GUID + info.hostGroupName + "LUN:" + info.LUN);
            }
            if (!enable) {
                LUN = table.get(i).getLun();
            }
            boolean success = scsi.addView(selected, LUN, table.get(i).getGuid());
            if (!success) {
                RequestContext.getCurrentInstance().execute("PF('block').hide()");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
                return null;
            }
            vs = scsi.getLUNView(table.get(i).getGuid());
            for(ViewInformation info : vs){
                Debug.print(info.GUID + info.hostGroupName + "LUN:" + info.LUN);
            }
            
        }

        return returnLUN();
    }

    public final ArrayList getLuns() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        List<String> all = Arrays.asList(scsi.getAllHostGroupNames());
        //      lun.add(res.get(basename, "all"));
        for (String v : all) {
            System.out.println(v);
            if (!views.contains(v)) {
                lun.add(v);
            }
        }
        return lun;
    }

    public final String getStrLuns(String lun) {
        if (lun.equals(res.get(basename, "all"))) {
            return "all";
        }
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

    public final String returnLUN() {
        this.gone = true;
        String param = "GUID=" + guid + "&dg=" + this.dg + "&dc=";
        int dc = count;
        param = param + dc;
        if (dc == 0) {
            return "lun?faces-redirect=true&amp;" + param;
        } else {
            for (int i = 0; i < dc; i++) {
                param = param + "&dguid" + i + "=" + this.dguids[i]
                        + "&dname" + i + "=" + this.dnames[i];
            }
            return "lun?faces-redirect=true&amp;" + param;
        }
    }

    public boolean checkFormat() {
        if (!DGUtility.checkShareName(String.valueOf(lunNum))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format"), global.get("error_mark")));
            return false;
        }
        return true;
    }

    private boolean checkNull() {
        if (String.valueOf(lunNum).equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null"), global.get("error_mark")));
            return false;
        }
        return true;
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

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getLunNum() {
        return lunNum;
    }

    public void setLunNum(int lunNum) {
        this.lunNum = lunNum;
    }

    public ArrayList getLun() {
        if (lun == null || lun.isEmpty()) {
            lun = new ArrayList();
            lun.add("");
        }
        return lun;
    }

    public void setLun(ArrayList lun) {
        this.lun = lun;
    }

    public String[] getDguids() {
        return dguids;
    }

    public void setDguids(String[] dguids) {
        this.dguids = dguids;
    }

    public String[] getDnames() {
        return dnames;
    }

    public void setDnames(String[] dnames) {
        this.dnames = dnames;
    }

    public List<LUNExpansion> getTable() {
        return table;
    }

    public void setTable(List<LUNExpansion> table) {
        this.table = table;
    }

    public int[] getDefaults() {
        if (defaults == null) {
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            defaults = scsi.getHostGroupAvailableLuns("all");
        }
        return defaults;
    }

    public void setDefaults(int[] defaults) {
        this.defaults = defaults;
    }

    public boolean isIsAll() {
        return isAll;
    }

    public void setIsAll(boolean isAll) {
        this.isAll = isAll;
    }
}
