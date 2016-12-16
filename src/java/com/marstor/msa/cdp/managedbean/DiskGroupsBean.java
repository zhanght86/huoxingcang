/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpAgent;
import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.socket.BasicQueryAction;
import com.marstor.msa.cdp.socket.MarsClientSocket;
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
@ManagedBean(name = "diskGroupsBean")
@ViewScoped
public class DiskGroupsBean implements Serializable {

    private List<CdpDiskGroupInfo> diskGroups;
    private CdpDiskGroupInfo selectedDG;
    private CdpDiskInfo selectedD;
    private List<CdpDiskInfo> disks;
    private Map disksMap = new HashMap();
    private List<Details> dgInfo;
    private ViewInformation[] lun;
    private Map<String, ViewInformation[]> luns = new HashMap();
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.dgs";

    public DiskGroupsBean() {
    }

    public String addDG() {
        return "add_dg?faces-redirect=true";
    }

    public boolean getOffline(int mount) {
        return mount == CDPConstants.OFFLINE;
    }

    public void getExpansions(CdpDiskGroupInfo dg) {
        dgInfo = new ArrayList();
        for (int i = 1; i < 6; i++) {
            Details g = new Details();
            g.setName(res.get(basename, "name" + i));
            dgInfo.add(g);
        }
//        if (dgInfos.get(dg.getDiskGroupName()) == null) {
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
//        long perc = 0;
//        if (dg.getStatusInfo().getIsRollback() == CDPConstants.TRUE
//                && dg.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_RUNNING) {
//            perc = dg.getRollbackInfo().getCurrentRollbackSize() * 100
//                    / dg.getRollbackInfo().getTotalRollbackSize();
//        }

//        if (dg.getiMount() == CDPConstants.ONLINE && dg.isCDPStarted()) {
//            state = res.get(basename, "state1");
//        }
//        if (dg.getiMount() == CDPConstants.ONLINE && !dg.isCDPStarted()) {
//            state = res.get(basename, "state2");
//        }
//        if (dg.getiMount() == CDPConstants.ONLINE
//                && dg.getStatusInfo().getIsRollback() == CDPConstants.TRUE
//                && dg.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_RUNNING) {
//            state = res.get(basename, "state3") + perc + "%）";
//        }
//        if (dg.getiMount() == CDPConstants.ONLINE
//                && dg.getStatusInfo().getIsRollback() == CDPConstants.TRUE
//                && dg.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_ERROR) {
//            state = res.get(basename, "state4") + dg.getRollbackInfo().getErrorMessage();
//        }

        if (dg.getiMount() == CDPConstants.ONLINE) {
            state = res.get(basename, "online");
        }
        if (dg.getiMount() == CDPConstants.OFFLINE) {
            state = res.get(basename, "offline");
        }
        dgInfo.get(0).setValue(dg.getDiskGroupName());
        dgInfo.get(1).setValue(String.valueOf(dg.getDiskCount()));
        dgInfo.get(2).setValue(String.valueOf(dg.getTotalDiskSize() / (1024L * 1024L * 1024L)) + global.get("GB"));
        dgInfo.get(3).setValue(state);
        dgInfo.get(4).setValue(dg.getDiskGroupPath());
        //  this.dgInfos.put(dg.getDiskGroupName(), dgInfo);
//        }
//        
//        dgInfo = (List<Details>)dgInfos.get(dg.getDiskGroupName());

        if (dg.getiMount() == CDPConstants.OFFLINE) {
            return;
        }

        getDisks(dg);

        if (luns.get(dg.getDiskGroupName()) == null) {
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();

            if (disks != null && disks.size() > 0) {
                if (disks.get(0).getLuInfoBean() != null) {
                    luns.put(dg.getDiskGroupName(), scsi.getLUNView(disks.get(0).getLuInfoBean().getlUName()));
                }
            } else {
                luns.put(dg.getDiskGroupName(), null);
            }

        }
        lun = luns.get(dg.getDiskGroupName());
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

    public boolean disDisks(CdpDiskGroupInfo group) {
        getExpansions(group);
        if (lun != null && lun.length > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void preonoffLine() {
        if (this.selectedDG.getiMount() == com.marstor.msa.cdp.model.CDPConstants.ONLINE) {
            if (this.selectedDG.isCDPStarted() || this.selectedDG.getiAutoSnapshotIsOpen() == CDPConstants.TRUE) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "stopCDPplease"), global.get("error_mark")));
                return;
            }
            getExpansions(this.selectedDG);
            if (lun != null && lun.length > 0) {
                RequestContext.getCurrentInstance().execute("disLUN.show()");
                return;
            }
            RequestContext.getCurrentInstance().execute("offlineDG.show()");
        } else {
            String[] status = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(this.selectedDG.getDiskZfsName());

            if (this.selectedDG.getSyncStatusInfo() != null) {
                if ((status[4].equals("1") && !status[5].equals("1"))
                        || this.selectedDG.getSyncStatusInfo().getIsDescSync() == CDPConstants.TRUE
                        && this.selectedDG.getSyncStatusInfo().getIsPause() == CDPConstants.FALSE) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "hasCopies"), global.get("error_mark")));
                    return;
                }
            }
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            boolean ret = cdp.online(this.selectedDG.getDiskGroupPath());
            if (ret) {
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
        RequestContext.getCurrentInstance().execute("windows.parent.closedlg()");
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

    public void preDelDG(CdpDiskGroupInfo group) {
        List<String> targets = InterfaceFactory.getCDPInterfaceInstance().getExistJobTargetPath();
        for (String target : targets) {
            if (target.equals(this.selectedDG.getDiskGroupPath())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("existTarget"), res.get("existTarget")));
                return;
            }
        }
        String[] status = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(group.getDiskZfsName());
        //是否为目标  同步是否暂停
        if (status[4].equals("1") || status[5].equals("1")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "isDest"), global.get("error_mark")));
            return;
        }

        if (group.getSyncStatusInfo() != null && group.getSyncStatusInfo().getIsDescSync() == CDPConstants.TRUE) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "isDest"), global.get("error_mark")));
            return;
        }
        if ((group.getDiskGroupName()).contains(CDPConstants.SNAPSHOT_COPY)) {
            if (this.selectedDG.isCDPStarted()) {
                getExpansions(this.selectedDG);
                if (lun != null && lun.length > 0) {
                    RequestContext.getCurrentInstance().execute("SdisLUNDel.show()");
                } else {
                    RequestContext.getCurrentInstance().execute("SdeleteDGBox.show()");
                }
            } else {
                getExpansions(this.selectedDG);
                if (lun != null && lun.length > 0) {
                    RequestContext.getCurrentInstance().execute("disLUNDel.show()");
                } else {
                    RequestContext.getCurrentInstance().execute("deleteDGBox.show()");
                }
            }

        } else {
            if (this.selectedDG.isCDPStarted()) {
                getExpansions(this.selectedDG);
                if (lun != null && lun.length > 0) {
                    RequestContext.getCurrentInstance().execute("SdisLUNDelPlus.show()");
                } else {
                    RequestContext.getCurrentInstance().execute("SdeleteDGBoxPlus.show()");
                }
            } else {
                getExpansions(this.selectedDG);
                if (lun != null && lun.length > 0) {
                    RequestContext.getCurrentInstance().execute("disLUNDelPlus.show()");
                } else {
                    RequestContext.getCurrentInstance().execute("deleteDGBoxPlus.show()");
                }
            }

        }
    }

    public void deleteDG() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        if (!this.selectedDG.getDiskGroupName().contains(CDPConstants.SNAPSHOT_COPY)) {
            for (CdpDiskGroupInfo g : diskGroups) {
                if (g.getDiskGroupName().startsWith(this.selectedDG.getDiskGroupName()) && g.getDiskGroupName().contains(CDPConstants.SNAPSHOT_COPY)) {
                    InterfaceFactory.getSYNCInterfaceInstance().deleteClone(g.getDiskGroupName());
                }
            }
        }

        if (this.selectedDG.isCDPStarted()) {
            cdp.stopCDP(this.selectedDG.getDiskGroupGuid(), this.selectedDG.getDiskGroupPath(),
                    this.selectedDG.getProtectType());
        }

        if (this.selectedDG.getiMount() == CDPConstants.TRUE) {
            getDisks(this.selectedDG);

            if (luns.get(selectedDG.getDiskGroupName()) == null) {
                SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
                if (disks.size() > 0 && disks.get(0).getLuInfoBean() != null) {
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
        }
        CdpAgent agent = this.selectedDG.getCdpAgent();
        if (agent != null && agent.getListDB() != null && agent.getListDB().size() > 0) {
            MarsClientSocket clientSock = new MarsClientSocket();
            clientSock.Connect(agent.getListDB().get(0).getIp(), agent.getListDB().get(0).getPort());

            BasicQueryAction action = new BasicQueryAction("9609", "Delete Group", clientSock);
            action.addParameter("GroupID", this.selectedDG.getDiskGroupGuid());
        }
        boolean ret;
        if (this.selectedDG.getDiskGroupName().contains(CDPConstants.SNAPSHOT_COPY)) {
            ret = cdp.deleteDiskGroup(this.selectedDG.getDiskGroupGuid(), this.selectedDG.getDiskGroupPath());
            ret = ret && InterfaceFactory.getSYNCInterfaceInstance().deleteCDPClone(this.selectedDG.getDiskZfsName());

        } else {
            ret = cdp.deleteDiskGroup(this.selectedDG.getDiskGroupGuid(), this.selectedDG.getDiskGroupPath());
        }

        if (ret) {
            initDiskGroups();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail2"), global.get("error_mark")));
        }

    }

    public final void initName() {
        for (int i = 1; i < 6; i++) {
            Details g = new Details();
            g.setName(res.get(basename, "name" + i));
            dgInfo.add(g);
        }
    }

    public final void initDiskGroups() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        this.diskGroups = cdp.getDiskGroupInfos();
        if (diskGroups != null) {
            for (CdpDiskGroupInfo g : diskGroups) {
                Debug.print("DiskGroupGuid" + g.getDiskGroupGuid());
                Debug.print("DiskGroupName" + g.getDiskGroupName());
                Debug.print("DiskGroupPath" + g.getDiskGroupPath());
                Debug.print("DiskCount=" + g.getDiskCount());
                Debug.print("ProtectType=" + g.getProtectType());
                Debug.print("IsCDPStarted=" + g.isCDPStarted());
                Debug.print("iAutoSnapshotIsOpen=" + g.getiAutoSnapshotIsOpen());
                Debug.print("Disabled= IsCDPStarted || iAutoSnapshotIsOpen");
            }
        }
        this.disksMap.clear();
        this.disks = null;
        this.lun = null;
    }

    public boolean disDelete(String name) {
        boolean dis = false;
        for (CdpDiskGroupInfo g : diskGroups) {
            if (g.getDiskGroupName().startsWith(name) && g.getDiskGroupName().contains(CDPConstants.SNAPSHOT_COPY)) {
                dis = true;
                break;
            }
        }
        return dis;
    }

    public boolean disableAll(CdpDiskGroupInfo group) {
        if (group.isCDPStarted() || group.getiAutoSnapshotIsOpen() == CDPConstants.TRUE) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getDisableDel(CdpDiskGroupInfo group) {
        String[] status = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(group.getDiskZfsName());

        if (status[4].equals("1") || status[5].equals("1")) {
            return true;
        }

        return false;
    }

    public List<CdpDiskGroupInfo> getDiskGroups() {
        if (this.diskGroups == null) {
            initDiskGroups();
        }

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
        String param = "GUID=" + this.selectedDG.getDiskGroupGuid() + "&dg=" + this.selectedDG.getDiskGroupName() + "&dc=";
        System.out.println("GUID:" + this.selectedDG.getDiskGroupGuid());
        getDisks(this.selectedDG);
        int dc = disks.size();
        param = param + dc;
        if (dc == 0) {
            return "lun?faces-redirect=true&amp;" + param;
        } else {
            for (int i = 0; i < dc; i++) {
                if (disks.get(i).getLuInfoBean() != null) {
                    param = param + "&dguid" + i + "=" + disks.get(i).getLuInfoBean().getlUName()
                            + "&dname" + i + "=" + disks.get(i).getDiskName();
                }
            }
            return "lun?faces-redirect=true&amp;" + param;
        }
    }

    public String agent() {
        String param = "guid=" + selectedDG.getDiskGroupGuid();
        return "agent?faces-redirect=true&amp;" + param;
    }

    public String snapshot() {
        String param = "guid=" + selectedDG.getDiskGroupGuid() + "&path=" + selectedDG.getDiskGroupPath();

        return "ss?faces-redirect=true&amp;" + param;
    }

    public String disk() {
        String param = "guid=" + selectedDG.getDiskGroupGuid() + "&path=" + selectedDG.getDiskGroupPath();
        if (this.selectedDG.getProtectType() == CDPConstants.PROTECT_RECORD) {
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
        }
        return "ds?faces-redirect=true&amp;" + param;
    }

    public String log() {
        String param = "guid=" + selectedDG.getDiskGroupGuid() + "&path=" + selectedDG.getDiskGroupPath();
        return "log?faces-redirect=true&amp;" + param;
    }

    public String protect() {
        String param = "path=" + selectedDG.getDiskGroupPath() + "&guid=" + selectedDG.getDiskGroupGuid()
                + "&index=0";
        return "protect?faces-redirect=true&amp;" + param;
    }

    public String getsMount(CdpDiskGroupInfo group) {
        return group.getiMount() == com.marstor.msa.cdp.model.CDPConstants.ONLINE ? res.get("cdp.dgs", "online") : res.get("cdp.dgs", "offline");
    }

    public String getONOFFLine(CdpDiskGroupInfo group) {
        return group.getiMount() == com.marstor.msa.cdp.model.CDPConstants.ONLINE ? res.get("cdp.dgs", "off_line") : res.get("cdp.dgs", "on_line");
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
        return group.isCDPStarted() ? res.get("cdp.dgs", "stopCDP") : res.get("cdp.dgs", "startCDP");
    }

    public String settings() {
        System.out.println("selectFile.getUsed()=" + path);
        String volume = path.split("/")[1];
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        selected = zfs.getFileSystemInformation(volume + "/DISK");
        System.out.println("selectFile.getUsed()=" + volume);
        String title = "cdp";
        String returnStr = "/cdp/dgs";
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
