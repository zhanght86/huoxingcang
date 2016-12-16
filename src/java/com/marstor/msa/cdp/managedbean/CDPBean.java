
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.bean.CdpStatusInfo;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.ConvertUtility;
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.FileSystemInformation;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
import com.marstor.msa.sync.bean.SyncStatusInfo;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "cdpBean")
@ViewScoped
public class CDPBean implements Serializable {

    private List<CdpDiskGroupInfo> diskGroups;
    private CdpDiskGroupInfo selectedDG;
    private CdpDiskInfo selectedD;
    private List<CdpDiskInfo> disks = new ArrayList();
    private Map disksMap = new HashMap();
    private List<Details> dgInfo = new ArrayList();
    private Map dgInfos = new HashMap();
    private ViewInformation[] lun;
    private Map<String, ViewInformation[]> luns = new HashMap();
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.cdp";

    public String addDG() {
        return "add_dg?faces-redirect=true";
    }

    public boolean getOffline(int mount) {
        return mount == CDPConstants.OFFLINE;
    }
    
    public boolean getDisableO(CdpDiskGroupInfo group){
//        String[] status = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(group.getDiskZfsName());
//
//        if (status[4].equals("1") && status[5].equals("1")) {
//            return true;
//        }
//        
//        return false;
        SyncStatusInfo syncStatusInfo =  group.getSyncStatusInfo();
        return syncStatusInfo.getIsDescSync()==1 && syncStatusInfo.getIsPause() ==1;
    }
    
    public boolean isRollbacked(CdpDiskGroupInfo group){
        boolean rollbacked = false;
        if (group.getProtectType() == CDPConstants.PROTECT_SNAPSHOT) {
            if (group.getSyncStatusInfo() != null) {
                rollbacked = group.getSyncStatusInfo().getIsRollback() == CDPConstants.TRUE;
            }
        } else {
            rollbacked = false;
        }
        return rollbacked;
    }
    
    public boolean isRollbacking(CdpDiskGroupInfo group){
        boolean rollbacking = false;
        if (group.getProtectType() == CDPConstants.PROTECT_SNAPSHOT) {
            if (group.getSyncStatusInfo() != null) {
                rollbacking = group.getSyncStatusInfo().isbIsRollbacking();
            }
        } else {
            if (group.getRollbackInfo() != null) {
                rollbacking = group.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_RUNNING;
            }
        }
        return rollbacking;
    }

    public boolean isCopy(String name) {
//        return name.contains(CDPConstants.SNAPSHOT_COPY);
        return false;
    }
    
    public boolean isDest(CdpDiskGroupInfo group){
        boolean isDest = false;
        if (group.getSyncStatusInfo() != null) {
            isDest = group.getSyncStatusInfo().getIsDescSync() == CDPConstants.TRUE;
        }
        return isDest;
    }

    public void getExpansions(CdpDiskGroupInfo dg) {
        dgInfo = new ArrayList();
        if (dg.getProtectType() == CDPConstants.PROTECT_NO) {
            for (int i = 0; i < 3; i++) {
                Details g = new Details();
                g.setName(res.get(basename, "name" + i));
                dgInfo.add(g);
            }
        } else if (dg.getProtectType() == CDPConstants.PROTECT_SNAPSHOT) {
            for (int i = 0; i < 5; i++) {
                Details g = new Details();
                if (i < 3) {
                    g.setName(res.get(basename, "name" + i));
                }
                if (i >= 3) {
                    g.setName(res.get(basename, "name1" + i));
                }
                dgInfo.add(g);
            }
            if (dg.getiMaxNum() == 0) {
                dgInfo.get(3).setValue(256 + "");
            } else {
                dgInfo.get(3).setValue(dg.getiMaxNum() + "");
            }
            if (dg.getlAutoSnapshotInterval() == 0) {
                dgInfo.get(4).setValue(String.valueOf(1));
            } else {
                dgInfo.get(4).setValue(String.valueOf(dg.getlAutoSnapshotInterval() / 60));
            }
            
        } else if (dg.getProtectType() == CDPConstants.PROTECT_RECORD) {
            for (int i = 0; i < 7; i++) {
                Details g = new Details();
                if (i < 3) {
                    g.setName(res.get(basename, "name" + i));
                }
                if (i >= 3) {
                    g.setName(res.get(basename, "name2" + i));
                }
                dgInfo.add(g);
            }
            long diskSize = dg.getTotalDiskSize();
            long logSize;
            if (diskSize == 0) {
                logSize = 0;
            } else {
                logSize = dg.getLogPolicy().getLogSize() / (1024L * 1024L * 1024L);
            }
            int loopTime = dg.getLogPolicy().getLogKeepTime();
            String loop;
            if (loopTime == 100000) {
                loop = res.get(basename, "chk");
            } else {
                loop = String.valueOf(dg.getLogPolicy().getLogKeepTime()) + global.get("hour");
            }
            String state = "";
            long perc = 0;
            if (dg.getSyncStatusInfo() != null && dg.getRollbackInfo() != null 
                    && (dg.getSyncStatusInfo().getIsRollback() == CDPConstants.TRUE || 
                    dg.getSyncStatusInfo().getIsCdpRollback() == CDPConstants.TRUE)
                    && dg.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_RUNNING) {
                perc = dg.getRollbackInfo().getCurrentRollbackSize() * 100
                        / dg.getRollbackInfo().getTotalRollbackSize();
            }

            if (dg.getiMount() == CDPConstants.ONLINE && dg.isCDPStarted()) {
                state = res.get(basename, "state1");
            }
            if (dg.getiMount() == CDPConstants.ONLINE && !dg.isCDPStarted()) {
                state = res.get(basename, "state2");
            }
            if (dg.getSyncStatusInfo() != null && dg.getRollbackInfo() != null 
                    && dg.getiMount() == CDPConstants.ONLINE
                    && (dg.getSyncStatusInfo().getIsRollback() == CDPConstants.TRUE || 
                    dg.getSyncStatusInfo().getIsCdpRollback() == CDPConstants.TRUE)
                    && dg.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_RUNNING) {
                state = res.get(basename, "state3") + perc + "%）";
            }
            
            if (dg.getSyncStatusInfo() != null && dg.getRollbackInfo() != null 
                    && dg.getiMount() == CDPConstants.ONLINE
                    && (dg.getSyncStatusInfo().getIsRollback() == CDPConstants.TRUE || 
                    dg.getSyncStatusInfo().getIsCdpRollback() == CDPConstants.TRUE)
                    && dg.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_ERROR) {
                state = res.get(basename, "state4") + dg.getRollbackInfo().getErrorMessage();
            }
            if (dg.getiMount() == CDPConstants.OFFLINE) {
                state = res.get(basename, "offline");
            }
            dgInfo.get(3).setValue(String.valueOf(logSize) + global.get("GB"));
            dgInfo.get(4).setValue(loop);
            dgInfo.get(5).setValue(dg.isbLogFull() ? global.get("yes") : global.get("no"));
            dgInfo.get(6).setValue(ConvertUtility.getStringLogFullOption(dg.getLogPolicy().getLogFullOption()));

        }
        dgInfo.get(0).setValue(dg.getDiskGroupName());
        dgInfo.get(1).setValue(getsCDPLevel(dg));
        dgInfo.get(2).setValue(getsMountCDP(dg));
    }

    public void getData() {

        if (selectedDG.getiMount() == CDPConstants.OFFLINE) {
            return;
        }

        getDisks(selectedDG);

        if (luns.get(selectedDG.getDiskGroupName()) == null) {
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            if (disks.size() > 0) {
                luns.put(selectedDG.getDiskGroupName(), scsi.getLUNView(disks.get(0).getLuInfoBean().getlUName()));
            } else {
                luns.put(selectedDG.getDiskGroupName(), null);
            }

        }
        lun = luns.get(selectedDG.getDiskGroupName());
    }

    public void getDisks(CdpDiskGroupInfo dg) {
        if (disksMap.get(dg.getDiskGroupName()) == null) {
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            this.disksMap.put(dg.getDiskGroupName(), cdp.getDiskInfos(dg.getDiskGroupGuid(), dg.getDiskGroupPath()));
        }
        if (disksMap.get(dg.getDiskGroupName()) == null) {
            return;
        }
        disks = Arrays.asList((CdpDiskInfo[]) disksMap.get(dg.getDiskGroupName()));
    }

    public String getType(String lun) {
        if (lun.startsWith("fc")) {
            return "FC";
        } else if (lun.startsWith("iscsi")) {
            return "iSCSI";
        } else if (lun.equalsIgnoreCase("all")) {
            return res.get(basename, "all");
        }
        return "";
    }

    public String getStrLun(String lun) {
        if (lun.equalsIgnoreCase("all")) {
            return res.get(basename, "alll");
        } else {
            return lun;
        }
    }

    public CDPBean() {
        initDiskGroups();
    }

    public void startstopCDP() {
        if (this.selectedDG.isCDPStarted()) {
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            boolean ret = cdp.stopCDP(this.selectedDG.getDiskGroupGuid(), this.selectedDG.getDiskGroupPath(),
                    this.selectedDG.getProtectType());
            if (ret) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success1"), global.get("error_mark")));
                initDiskGroups();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), global.get("error_mark")));
            }
        } else {
            boolean keepLog = true;
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            boolean ret = cdp.startCDP(this.selectedDG.getDiskGroupGuid(), this.selectedDG.getDiskGroupPath(),
                    this.selectedDG.getLogPolicy().getLogFullOption());
            if (ret) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success0"), global.get("error_mark")));
                initDiskGroups();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            }
        }
    }

    public void preonoffLine() {
        if (this.selectedDG.getiMount() == com.marstor.msa.cdp.model.CDPConstants.ONLINE) {
            getExpansions(this.selectedDG);
            if (lun != null && lun.length > 0) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail5"), global.get("error_mark")));
                RequestContext.getCurrentInstance().execute("disLUN.show()");
                return;
            }
            RequestContext.getCurrentInstance().execute("offlineDG.show()");
        } else {
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            boolean ret = cdp.online(this.selectedDG.getDiskGroupPath());
            if (ret) {
                //    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success4"), global.get("error_mark")));
                initDiskGroups();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail4"), global.get("error_mark")));
            }
        }
    }

    public void showOnOff() {
        RequestContext.getCurrentInstance().execute("offlineDG.show()");
    }

    public void showDel() {
        RequestContext.getCurrentInstance().execute("deleteDGBox.show()");
    }

    public String cancel() {
        return "dgs?faces-redirect=true";
    }

    public void onoffLine() {
        if (this.selectedDG.getiMount() == com.marstor.msa.cdp.model.CDPConstants.ONLINE) {
            getData();
            if (lun != null && lun.length > 0) {
                SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
                for (int i = 0; i < lun.length; i++) {
                    for (int j = 0; j < this.selectedDG.getDiskCount(); j++) {
                        scsi.removeView(this.disks.get(j).getLuInfoBean().getlUName(), lun[i].entry);
                    }
                }
            }
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            boolean ret = cdp.offline(this.selectedDG.getDiskGroupGuid(), this.selectedDG.getDiskGroupPath());
            if (ret) {
                //     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success3"), global.get("error_mark")));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail3"), global.get("error_mark")));
            }
        } else {
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            boolean ret = cdp.online(this.selectedDG.getDiskGroupPath());
            if (ret) {
                //    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success4"), global.get("error_mark")));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail4"), global.get("error_mark")));
            }
        }
        initDiskGroups();
    }

    public void preDelDG() {
        getExpansions(this.selectedDG);
        if (lun != null && lun.length > 0) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail5"), global.get("error_mark")));
            RequestContext.getCurrentInstance().execute("disLUNDel.show()");
        } else {
            RequestContext.getCurrentInstance().execute("deleteDGBox.show()");
        }
    }

    public void deleteDG() {
        getDisks(this.selectedDG);

        if (luns.get(selectedDG.getDiskGroupName()) == null) {
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            if (disks.size() > 0) {
                luns.put(selectedDG.getDiskGroupName(), scsi.getLUNView(disks.get(0).getLuInfoBean().getlUName()));
            } else {
                luns.put(selectedDG.getDiskGroupName(), null);
            }

        }
        lun = luns.get(selectedDG.getDiskGroupName());
        if (lun != null && lun.length > 0) {
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            for (int i = 0; i < lun.length; i++) {
                for (int j = 0; j < this.selectedDG.getDiskCount(); j++) {
                    scsi.removeView(this.disks.get(j).getLuInfoBean().getlUName(), lun[i].entry);
                }
            }
        }
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        boolean ret = cdp.deleteDiskGroup(this.selectedDG.getDiskGroupGuid(), this.selectedDG.getDiskGroupPath());
        if (ret) {
            initDiskGroups();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail2"), global.get("error_mark")));
        }
    }

    public final void initName() {
        for (int i = 3; i < 9; i++) {
            Details g = new Details();
            g.setName(res.get(basename, "name" + i));
            dgInfo.add(g);
        }
    }

    public final void initDiskGroups() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        this.diskGroups = cdp.getDiskGroupInfos();
        for(CdpDiskGroupInfo g : this.diskGroups){
            Debug.print("DiskGroupGuid" + g.getDiskGroupGuid());
            Debug.print("DiskGroupName" + g.getDiskGroupName());
            Debug.print("DiskGroupPath" + g.getDiskGroupPath());
            Debug.print("DiskCount=" + g.getDiskCount());
            Debug.print("IsCopy=" + this.isCopy(g.getDiskGroupName()));
            
            Debug.print("ProtectType=" + g.getProtectType());
            Debug.print("IsRollbacked=" + this.isRollbacked(g));
            Debug.print("IsRollbacking=" + this.isRollbacking(g));
            if(g.getSyncStatusInfo() != null){
                Debug.print("IsLocalDescSync=" + g.getSyncStatusInfo().getIsLocalDescSync());
                Debug.print("IsDestSync=" + g.getSyncStatusInfo().getIsDescSync());
                Debug.print("IsPause=" + g.getSyncStatusInfo().getIsPause());
                Debug.print("Disabled= IsDestSync && IsPause");
            }            
        }
        this.dgInfos.clear();
        this.disksMap.clear();
        this.disks = null;
        this.lun = null;
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

    public ViewInformation[] getLun() {
        return lun;
    }

    public void setLun(ViewInformation[] lun) {
        this.lun = lun;
    }

    public String lun() {
        String param = "GUID=null&dg=" + this.selectedDG.getDiskGroupName() + "&dc=";
        getDisks(this.selectedDG);
        int dc = disks.size();
        param = param + dc;
        if (dc == 0) {
            return "lun?faces-redirect=true&amp;" + param;
        } else {
            for (int i = 0; i < dc; i++) {
                param = param + "&dguid" + i + "=" + disks.get(i).getLuInfoBean().getlUName()
                        + "&dname" + i + "=" + disks.get(i).getDiskName();
            }
            return "lun?faces-redirect=true&amp;" + param;
        }
    }

    public String agent() {
        String param = "guid=" + selectedDG.getDiskGroupGuid();
        return "agent?faces-redirect=true&amp;" + param;
    }

    public String snapshot() {
        
        String param = "guid=" + selectedDG.getDiskGroupGuid() + "&path=" + selectedDG.getDiskGroupPath()
                + "&zfsName=" + this.selectedDG.getDiskZfsName() + "&level=" + this.selectedDG.getProtectType()
                + "&dg=" + this.selectedDG.getDiskGroupName();
        for(CdpDiskGroupInfo g : this.diskGroups){
            param += "&groupName=" + g.getDiskGroupName();
        }
        if(this.selectedDG.getProtectType() == CDPConstants.PROTECT_SNAPSHOT){
            System.out.println("snapshot url=" + "snapshot_s?faces-redirect=true&amp;" + param);
            return "snapshot_s?faces-redirect=true&amp;" + param;
        }else{
            System.out.println("snapshot url=" + "snapshot_r?faces-redirect=true&amp;" + param);
            return "snapshot_r?faces-redirect=true&amp;" + param;
        }
        
    }

    public String cdpLevel() {
        String param = "guid=" + selectedDG.getDiskGroupGuid() + "&path=" + selectedDG.getDiskGroupPath()
                + "&level=" + this.selectedDG.getProtectType();
        CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
        Reg[] license = commonInterfaceInstance.getLicense();
        for (int i = 0; i < license.length; i++) {
            if (Module.MODULE_CDP == license[i].getModuleID() && Module.FUNCTIONID_CDP_CAPACITY == license[i].getFunctionID()) {
                long used = 0;
                for (CdpDiskGroupInfo g : this.diskGroups) {
                    if (g.getDiskGroupGuid().equals(this.selectedDG.getDiskGroupGuid())) {
                        continue;
                    }
                    if (g.getProtectType() == CDPConstants.PROTECT_RECORD) {
                        used = used + g.getTotalDiskSize() + g.getLogPolicy().getLogSize();
                    }
                }
                param = param + "&record=1" + "&register=" + license[i].getFunctionNumber()
                        + "&used=" + used;
                break;
            }
        }

        return "cdp_level?faces-redirect=true&amp;" + param;
    }

    public void CDPAction() {
        boolean flag;
        if (this.selectedDG.getProtectType() == CDPConstants.PROTECT_SNAPSHOT) {
            flag = this.selectedDG.getiAutoSnapshotIsOpen() == 1;
            if (!flag) {
                startCDP();
                return;
            }
        } else {
            flag = this.selectedDG.isCDPStarted();
        }
        if (flag) {
            if (selectedDG.getSyncStatusInfo() != null) {
                boolean isSource = selectedDG.getSyncStatusInfo().getIsSrcSync() == CDPConstants.TRUE;
                Debug.print("selectedDG.getSyncStatusInfo().getIsSrcSync() = " + selectedDG.getSyncStatusInfo().getIsSrcSync() + "");
                if (isSource) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "deleteCopy"), global.get("error_mark")));
                    return;
                }
            }
            RequestContext.getCurrentInstance().execute("stopCDPBox.show()");
        } else {
            RequestContext.getCurrentInstance().execute("startCDPBox.show()");
        }
    }

    public void startCDP() {
        boolean keepLog = true;
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        boolean ret;
        if (this.selectedDG.getProtectType() == 1) {
            ret = cdp.startAutoSnapshot(this.selectedDG.getDiskGroupPath().substring(1), (int) this.selectedDG.getlAutoSnapshotInterval(),
                    this.selectedDG.getiMaxNum());
        } else {
            ret = cdp.startCDP(this.selectedDG.getDiskGroupGuid(), this.selectedDG.getDiskGroupPath(),
                    this.selectedDG.getLogPolicy().getLogFullOption());
        }
        if (ret) {
            initDiskGroups();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
        }
    }

    public void stopCDP() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        boolean ret = cdp.stopCDP(this.selectedDG.getDiskGroupGuid(), this.selectedDG.getDiskGroupPath(),
                this.selectedDG.getProtectType());
        if (ret) {
            initDiskGroups();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), global.get("error_mark")));
        }
    }

    public String log() {
        String param = "guid=" + selectedDG.getDiskGroupGuid() + "&path=" + selectedDG.getDiskGroupPath();
        return "log?faces-redirect=true&amp;" + param;
    }

    public String copy() {
        String param = "path=" + selectedDG.getDiskGroupPath() + "&guid=" + selectedDG.getDiskGroupGuid()
                + "&level=" + this.selectedDG.getProtectType() + "&dg=" + selectedDG.getDiskGroupName();
        for(CdpDiskGroupInfo g : this.diskGroups){
            param += "&groupName=" + g.getDiskGroupName();
        }
        System.out.println("copy paras:" + "copy?faces-redirect=true&amp;" + param);
        return "copy?faces-redirect=true&amp;" + param;
    }

    public String getsMount(CdpDiskGroupInfo group) {
        return group.getiMount() == com.marstor.msa.cdp.model.CDPConstants.ONLINE ? res.get(basename, "online") : res.get(basename, "offline");
    }

    public String getsMountCDP(CdpDiskGroupInfo group) {
        String s;
        if (group.getiMount() == com.marstor.msa.cdp.model.CDPConstants.ONLINE) {
            s = res.get(basename, "online");
            if (group.getProtectType() == CDPConstants.PROTECT_SNAPSHOT) {
                if (group.getiAutoSnapshotIsOpen() == CDPConstants.TRUE) {
                    s = s + "（已开启CDP）";
                } else {
                    s = s + "（未开启CDP）";
                }
            } else if (group.getProtectType() == CDPConstants.PROTECT_RECORD) {
                if (group.isCDPStarted()) {
                    s = s + "（已开启CDP）";
                } else {
                    s = s + "（未开启CDP）";
                }
            } else {
                return s;
            }

        } else {
            s = res.get(basename, "offline");
        }
        return s;
    }

    public String getsCDP(CdpDiskGroupInfo group) {
        if (group.getProtectType() == CDPConstants.PROTECT_NO) {
            return "";
        } else if (group.getProtectType() == CDPConstants.PROTECT_SNAPSHOT) {
            return group.getiAutoSnapshotIsOpen() == CDPConstants.TRUE ? res.get(basename, "on") : res.get(basename, "off");
        } else if (group.getProtectType() == CDPConstants.PROTECT_RECORD) {
            return group.isCDPStarted() ? res.get(basename, "on") : res.get(basename, "off");
        } else {
            return "";
        }
    }

    public String getsCDPLevel(CdpDiskGroupInfo group) {
        String s = "";
        switch (group.getProtectType()) {
            case 0:
                s = res.get("cdp.cdp_level", "mirror");
                break;
            case 1:
                s = res.get("cdp.cdp_level", "snapshot");
                break;
            case 2:
                s = res.get("cdp.cdp_level", "record");
                break;
        }
        return s;
    }

    public String getONOFFLine(CdpDiskGroupInfo group) {
        return group.getiMount() == com.marstor.msa.cdp.model.CDPConstants.ONLINE ? res.get(basename, "off_line") : res.get(basename, "on_line");
    }

    public boolean RONLine(CdpDiskGroupInfo group) {
        return group.getiMount() == com.marstor.msa.cdp.model.CDPConstants.ONLINE ? false : true;
    }

    public boolean ROFFLine(CdpDiskGroupInfo group) {
        return group.getiMount() == com.marstor.msa.cdp.model.CDPConstants.ONLINE ? true : false;
    }

    public boolean isMonted(CdpDiskGroupInfo group) {
        return group.getiMount() == com.marstor.msa.cdp.model.CDPConstants.ONLINE;
    }

    public String getStartStopCDP(CdpDiskGroupInfo group) {
        String s;
        if (group.getProtectType() == CDPConstants.PROTECT_SNAPSHOT) {
            if (group.getiAutoSnapshotIsOpen() == CDPConstants.TRUE) {
                s = res.get(basename, "stopCDP");
            } else {
                s = res.get(basename, "startCDP");
            }
        } else if (group.getProtectType() == CDPConstants.PROTECT_RECORD) {
            if (group.isCDPStarted()) {
                s = res.get(basename, "stopCDP");
            } else {
                s = res.get(basename, "startCDP");
            }
        } else {
            s = res.get(basename, "startCDP");
        }
        return s;
    }

    public boolean getRenderStart(CdpDiskGroupInfo group) {
        boolean s;
        if (group.getProtectType() == CDPConstants.PROTECT_SNAPSHOT) {
            if (group.getiAutoSnapshotIsOpen() == CDPConstants.TRUE) {
                s = false;
            } else {
                s = true;
            }
        } else if (group.getProtectType() == CDPConstants.PROTECT_RECORD) {
            if (group.isCDPStarted()) {
                s = false;
            } else {
                s = true;
            }
        } else {
            s = true;
        }
        return s;

    }

    public String settings() {

        System.out.println("selectFile.getUsed()=" + path);
        String volume = path.split("/")[1];
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        selected = zfs.getFileSystemInformation(volume + "/DISK");
        System.out.println("selectFile.getUsed()=" + volume);
        String quotaValueStr = selected.quotaValue;
        if (selected.isSetQuota) {
            if (quotaValueStr != null && !quotaValueStr.equals("") && !quotaValueStr.equalsIgnoreCase("null") && !quotaValueStr.equalsIgnoreCase("none")) {
                double size = Double.valueOf(selected.getQuotaValue().substring(0, selected.getQuotaValue().length() - 1));
                if (selected.getQuotaValue().endsWith("G")) {
                    quotaValueStr = String.valueOf(size);
                }
                if (selected.getQuotaValue().endsWith("M")) {
                    size = size / 1024;
                    quotaValueStr = String.valueOf(size);
                }
                if (selected.getQuotaValue().endsWith("T")) {
                    size = size * 1024;
                    quotaValueStr = String.valueOf(size);
                }
                if (selected.getQuotaValue().endsWith("P")) {
                    size = size * 1024 * 1024;
                    quotaValueStr = String.valueOf(size);
                }

                if (quotaValueStr.contains(".")) {
                    quotaValueStr = quotaValueStr.split("\\.")[0];
                }
            }

        }

        String title = "cdp";
        String returnStr = "/cdp/dgs";
        System.out.println("quotaValueStr=" + quotaValueStr + ",selected.recordsize=" + selected.recordsize);
        System.out.println("selected.name=" + selected.name);
        String fileSystemName = selected.name + "/" + this.selectedDG.getDiskGroupName(); //例如：SYSVOL/TAPE 或SYSVOL/XX/XX
        String param = "fileSystemName=" + fileSystemName + "&" + "title=" + title + "&" + "return=" + returnStr;
        return "/disk/filesystem_set?faces-redirect=true&amp;" + param;
    }
    public String path;
    public FileSystemInformation selected;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCompress(String Compress) {
        if (null == Compress || "".equalsIgnoreCase(Compress)) {
            return false;
        }
        return (!"off".equalsIgnoreCase(Compress));
    }

    public int compressLevel(String Compress) {
        if (null == Compress || "".equalsIgnoreCase(Compress)) {
            return 0;
        }
        if ("off".equalsIgnoreCase(Compress)) {
            return 0;
        }
        if ("gzip".equalsIgnoreCase(Compress)) {
            return 6;
        }
        char num = Compress.charAt(Compress.length() - 1);
        try {
            return Integer.valueOf(num + "");
        } catch (Exception e) {
            return 0;
        }
    }
}
