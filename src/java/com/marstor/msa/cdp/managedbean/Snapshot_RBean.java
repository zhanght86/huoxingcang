/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.AgentBean;
import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.bean.CdpRollbackTaskInfo;
import com.marstor.msa.cdp.bean.CdpSnapshotInfo;
import com.marstor.msa.cdp.model.GlobalProtectStatus;
import com.marstor.msa.cdp.model.SnapShot;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.sync.web.impl.MsaSYNCInterfaceImpl;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "snapshot_RBean")
@ViewScoped
public class Snapshot_RBean {

    private String guid;
    private String path;
    private int sHandle;
    private long sID = 0;
    private int count = 50;
    private int reverse = 0;
    private boolean forward = true;
    private List<CdpSnapshotInfo> snapshotInfos = new ArrayList();
    private CdpSnapshotInfo selected;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();
    private MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
    private String basename = "cdp.snapshot";
    private Date date = new Date();
    boolean rolling = false;
    private int level;
    private CdpRollbackTaskInfo rb;
    private GlobalProtectStatus globalStatus;
    private CdpDiskGroupInfo group;
    private String dg = "";
    private List<String> groupNames;

    public Snapshot_RBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        guid = request.getParameter("guid");
        path = request.getParameter("path");
        if (request.getParameter("level") != null) {
            level = Integer.parseInt(request.getParameter("level"));
        }
        
        this.groupNames = Arrays.asList(request.getParameterValues("groupName"));
    }

    public void cancelRollback() {
        CdpDiskInfo[] disks = cdp.getDiskInfos(guid, path);
        if (disks == null) {
            return;
        }
//        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//        ViewInformation[] views = scsi.getLUNView(disks[0].getDiskGuid());
        int len = disks.length;
        String[] lus;
        lus = new String[len];
        for (int i = 0; i < len; i++) {
            lus[i] = disks[i].getLuInfoBean().getlUName();
        }
//        if (views != null && views.length > 0) {
        cdp.offlineLU(this.group.getDiskGroupName(), lus);
//        }
        boolean flag = cdp.cancelRollback(this.group.getDiskGroupPath());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("cancelRollFailed"), res.get("cancelRollFailed")));
            return;
        }
        cdp.onlineLU(this.group.getDiskGroupName(), lus);
        this.init();
        globalStatus.update();
    }

    public void saveRollback() {
        boolean flag = cdp.saveRollback(this.group.getDiskGroupPath());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("saveRollFailed"), res.get("saveRollFailed")));
            return;
        }
        this.init();
        globalStatus.update();
    }

    public String doBeforeAddMSnap() {
        String param = "path=" + this.path + "&guid=" + this.guid + "&dg=" + this.dg;
        for(String name : this.groupNames){
            param += "&groupName=" + name;
        }
        return "add_snap?faces-redirect=true&amp;" + param;
    }

    public void updateSnapAndSync() {
        init();        
        globalStatus.update();
    }

    public void init() {
        initGroup();
        getQuerySnapshotHandle(-1);
        this.reverse = 0;
        this.forward = true;
        CdpSnapshotInfo[] infos = getSnapshots(this.sHandle, count, 1, 0);
        if (infos != null && infos.length != 0) {
            this.snapshotInfos = new ArrayList();
            for (CdpSnapshotInfo info : infos) {
                if (!info.isbHasZFSSnapshot() && !this.dg.contains(CDPConstants.SNAPSHOT_COPY)) {
                    continue;
                }
                this.snapshotInfos.add(info);
            }
        }

        if (this.snapshotInfos != null && !this.snapshotInfos.isEmpty()) {
            Collections.sort(snapshotInfos);
        }
        if (this.rolling) {
            getRollbackInfo();
        }
        
        List<CdpDiskGroupInfo> groups = cdp.getDiskGroupInfos();
        List<String> names = new ArrayList();
        for(CdpDiskGroupInfo g : groups){
            names.add(g.getDiskGroupName());
        }
        this.groupNames = names;
        globalStatus = new GlobalProtectStatus(path, level, group, dg, groupNames);

    }

    public String getDg() {
        if (!dg.equals("")) {
            return dg;
        }
        initGroup();

        getQuerySnapshotHandle(-1);
        CdpSnapshotInfo[] infos = getSnapshots(this.sHandle, count, 1, 0);
        if (infos != null && infos.length != 0) {
            this.snapshotInfos = new ArrayList();
            for (CdpSnapshotInfo info : infos) {
                if (!info.isbHasZFSSnapshot() && !this.dg.contains(CDPConstants.SNAPSHOT_COPY)) {
                    continue;
                }
                this.snapshotInfos.add(info);
            }
        }

        if (this.snapshotInfos != null && !this.snapshotInfos.isEmpty()) {
            Collections.sort(snapshotInfos);
        }

        if (group.isRollbacked()) {
            rb = group.getRollbackInfo();
            if (rb != null && rb.getCurrentRollbackSize() < rb.getTotalRollbackSize()) {
                this.rolling = true;
                RequestContext.getCurrentInstance().execute("PF('pbAjax').start()");
            }
        }

        globalStatus = new GlobalProtectStatus(path, level, group, dg, groupNames);
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public String doBeforeAddSnap() {
        String param = "path=" + this.path + "&guid=" + this.guid;
        return "add_snap?faces-redirect=true&amp;" + param;
    }

    public void preRollback() {
        CdpDiskInfo[] disks = cdp.getDiskInfos(group.getDiskGroupGuid(), group.getDiskGroupPath());
        ViewInformation[] lun = null;
        if (disks != null && disks.length > 0) {
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            if (disks[0].getLuInfoBean() != null) {
                lun = scsi.getLUNView(disks[0].getLuInfoBean().getlUName());
            }
        }
        if (this.group.isCDPStarted()) {
            if (lun != null && lun.length > 0) {
                RequestContext.getCurrentInstance().execute("SdisLUNDel.show()");
            } else {
                RequestContext.getCurrentInstance().execute("SdeleteDGBox.show()");
            }
        } else {
            if (lun != null && lun.length > 0) {
                RequestContext.getCurrentInstance().execute("disLUNDel.show()");
            } else {
                RequestContext.getCurrentInstance().execute("deleteDGBox.show()");
            }
        }
    }

    public void rollback() {
        CdpDiskInfo[] disks = cdp.getDiskInfos(guid, path);
        if (disks == null) {
            return;
        }
//        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        if (this.group.isCDPStarted()) {
            cdp.stopCDP(this.group.getDiskGroupGuid(), this.group.getDiskGroupPath(),
                    this.group.getProtectType());
        }
//        if (this.group.getiMount() == CDPConstants.TRUE) {
//            ViewInformation[] lun = null;
//            if (disks.length > 0) {
//                if (disks[0].getLuInfoBean() != null) {
//                    lun = scsi.getLUNView(disks[0].getLuInfoBean().getlUName());
//                }
//            }
//            if (lun != null && lun.length > 0) {
//                for (int i = 0; i < lun.length; i++) {
//                    for (int j = 0; j < this.group.getDiskCount(); j++) {
//                        scsi.removeView(disks[j].getLuInfoBean().getlUName(), lun[i].entry);
//                    }
//                }
//            }
//        }
//        ViewInformation[] views = scsi.getLUNView(disks[0].getDiskGuid());
        int len = disks.length;
        String[] lus;
        lus = new String[len];
        for (int i = 0; i < len; i++) {
            lus[i] = disks[i].getLuInfoBean().getlUName();
        }
//        if (views != null && views.length > 0) {
        cdp.offlineLU(this.group.getDiskGroupName(), lus);
//        }
        boolean success = cdp.cdpRollback(path, guid, 0, selected.getSnapshotTime());
        this.rolling = true;
        if (!success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
//        this.init();
//        globalStatus.update();
        RequestContext.getCurrentInstance().execute("PF('pbAjax').start()");
        RequestContext.getCurrentInstance().execute("PF('block').show()");
    }

    public void cloneSnap() {
        sync = InterfaceFactory.getSYNCInterfaceInstance();
        String para;
//        if(this.selected.getAgentID() == 999){
        para = this.group.getDiskZfsName() + "@";
//        }else{
//            String underline = this.group.getDiskZfsName().replace("/", "_");  
//            para = this.group.getDiskZfsName() + "@" + underline + "_";
//        }
        copyName = sync.createCDPClone(para + this.selected.getSnapshotName(), this.selected.getSnapshotTime());
        if (copyName == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "createCopyFailed"), global.get("error_mark")));
            return;
        }

        cdp.setProtectType(copyName, group.getProtectType());
        globalStatus.setContainsCopy(true);//创建副本后设置已包含副本
        this.init();
        globalStatus.update();
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("createCopy") + global.get("lquote") + copyName + global.get("rquote") + global.get("success") + global.get("error_mark"), res.get("createCopy") + global.get("rquote") + para + this.selected.getSnapshotName() + global.get("lquote") + global.get("success") + global.get("error_mark")));
        RequestContext.getCurrentInstance().execute("dgs.show()");
    }

    public String toDGs() {
        return "dgs?faces-redirect=true";
    }

    public List<String> getAfterRollSnaps() {
        List<String> afterSnaps = InterfaceFactory.getSYNCInterfaceInstance().getSnapshotListAfterRollback(path);
        if (afterSnaps == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getRolledSnapFailed"), res.get("getRolledSnapFailed")));
            return null;
        }
        return afterSnaps;
    }

    public String[] getLocalFileSystemStatus(String fileSystem) {
        String[] status = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(fileSystem);
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
            return null;
        }
        //文件系统状态 int[0] 文件系统是否存在快照 
        //int[1] 文件系统自动快照是否开启 
        //int[2] 文件系统是不是同步源文件系统 
        //int[3] 文件系统是不是同步目标文件系统 
        //int[4] 文件系统是否上线 
        //int[5] 文件系统是否暂停同步
        //int[6] 文件系统是否回滚

        return status;
    }

    public void clickCreateCopy(SnapShot snap) {
        if (snap.isPermitCreateCopy()) {
            RequestContext.getCurrentInstance().execute("createCopy.show();");
        } else {
            RequestContext.getCurrentInstance().execute("finishCreateCopy.show();");
        }
    }

    public void preCreateCopy(CdpSnapshotInfo snapshot) {
        SnapShot snap = new SnapShot(snapshot, path);
        if (snap.isPermitCreateCopy()) {
            RequestContext.getCurrentInstance().execute("clone.show();");
        } else {
            RequestContext.getCurrentInstance().execute("finishCreateCopy.show();");
        }
    }

    public void saveRoll() {
        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().saveCurrentStatus(path);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("saveRollFailed"), res.get("saveRollFailed")));
            return;
        }
        this.init();
        globalStatus.update();
    }

    public void cancelRoll() {
        CdpDiskInfo[] disks = cdp.getDiskInfos(guid, path);
        if (disks == null) {
            return;
        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        ViewInformation[] views = scsi.getLUNView(disks[0].getDiskGuid());
        int len = disks.length;
        String[] lus;
        lus = new String[len];
        for (int i = 0; i < len; i++) {
            lus[i] = disks[i].getLuInfoBean().getlUName();
        }
        if (views != null && views.length > 0) {
            cdp.offlineLU(this.group.getDiskGroupName(), lus);
        }
        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().cancelRollback(path);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("cancelRollFailed"), res.get("cancelRollFailed")));
            return;
        }
        if (views != null && views.length > 0) {
            cdp.onlineLU(this.group.getDiskGroupName(), lus);
        }
        this.init();
        globalStatus.update();
    }

    public void preDeleteSnap() {
        Collection<String> origins = sync.getCloneOriginMap().values();
        for (String origin : origins) {
            if (origin.equals(group.getDiskZfsName() + "@" + this.selected.getSnapshotName())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapsError"), res.get("delSnapsError")));
                return;
            }
        }
        RequestContext.getCurrentInstance().execute("delSnap.show();");
    }

    public void preDeleteAllSnap() {
        Collection<String> origins = sync.getCloneOriginMap().values();

        for (CdpSnapshotInfo ss : this.snapshotInfos) {
            for (String origin : origins) {
                if (origin.equals(ss.getSnapshotName())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delAllSnapsError"), res.get("delAllSnapsError")));
                    return;
                }
            }
        }
        RequestContext.getCurrentInstance().execute("delAllSnaps.show();");
    }

    public boolean getRecordDeleteAllDis() {
        if (this.snapshotInfos == null || this.snapshotInfos.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteSnap() {
        String para = this.group.getDiskZfsName() + "@";
        boolean flag = InterfaceFactory.getCDPInterfaceInstance().destroySnapshot(para + this.selected.getSnapshotName());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
            return;
        }
        this.init();
    }
    private String copyName;

    public void preClone() {
        RequestContext.getCurrentInstance().execute("clone.show()");
    }

    public String getCopyName() {
        return copyName;
    }

    public void setCopyName(String copyName) {
        this.copyName = copyName;
    }

    //检测是否是在回滚中
    public void checkRollbacking() {
        boolean isRollbacking = InterfaceFactory.getSYNCInterfaceInstance().isRollbacking(path);
        if (!isRollbacking) {
            globalStatus.setRollRender(false);
            globalStatus.setLoadingRender(true);
            RequestContext.getCurrentInstance().execute("rollbackpoll.stop();");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("rollSuccess"), res.get("rollSuccess")));
        } else {
            globalStatus.setRollRender(true);
            globalStatus.setLoadingRender(false);
        }
    }

    public final void getRollbackInfo() {
        rb = cdp.getRollbackInfo(guid, path);
    }
    private Integer progress = 0;

    public Integer getProgress() {
        getRollbackInfo();
        if (rb == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail11"), global.get("error_mark")));
            RequestContext.getCurrentInstance().execute("pbAjax.cancel();");
            this.rolling = false;
            return null;
        }

        progress = (int) ((double) rb.getCurrentRollbackSize() * 100 / (double) rb.getTotalRollbackSize());
        if (progress > 100 || rb.getCurrentRollbackSize() == rb.getTotalRollbackSize()) {
            progress = 100;
        }

        return progress;
    }

    public boolean getRenderSave() {
        boolean render = true;
        if (group != null && group.getSyncStatusInfo() != null && group.getSyncStatusInfo().getIsCdpRollback() == CDPConstants.FALSE) {
            render = false;
        }
        return render;
    }

    public boolean getDisableSave() {
        boolean disable = false;
        if (group != null && group.getSyncStatusInfo() != null && group.getSyncStatusInfo().getIsCdpRollback() == CDPConstants.FALSE) {
            disable = true;
        }
        if (group != null && group.getRollbackInfo() != null && group.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_ERROR) {
            disable = true;
        }

        if (group != null && group.getRollbackInfo() != null && group.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_CANCELED) {
            disable = true;
        }

        if (group != null && group.getRollbackInfo() != null && group.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_RUNNING) {
            disable = true;
        }
        return disable;
    }

    public boolean getDisableCancel() {
        boolean disable = false;
        if (group != null && group.getSyncStatusInfo() != null && group.getSyncStatusInfo().getIsCdpRollback() == CDPConstants.FALSE) {
            disable = true;
        }
        if (group != null && group.getRollbackInfo() != null && group.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_RUNNING) {
            disable = true;
        }
        return disable;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void onComplete() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "回滚完成。", "Progress Completed"));
        this.rolling = false;
        RequestContext.getCurrentInstance().execute("PF('block').hide()");
        this.init();
        globalStatus.update();
    }

    public void cancel() {
        cdp.cancelRollback(path);
        progress = null;
        rolling = false;
    }

    public void seekRev() {
        if (this.forward) {
            this.reverse = 1;
        } else {
            this.reverse = 0;
        }
        this.forward = false;
        CdpSnapshotInfo[] infos = getSnapshots(this.sHandle, count, 0, this.snapshotInfos.size());
        if (infos == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "none"), null));
            return;
        }
        if (infos.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "first"), null));
            return;
        }
        List<CdpSnapshotInfo> tmp = new ArrayList();
        for (CdpSnapshotInfo info : infos) {
            if (info.isbHasZFSSnapshot()) {
                tmp.add(info);
            }
        }
        
        if (tmp.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "first"), null));
            return;
        }        
        
        this.snapshotInfos = tmp;

        if (this.snapshotInfos != null && !this.snapshotInfos.isEmpty()) {
            Collections.sort(snapshotInfos);
        }
    }

    public void seekNext() {
        if (!this.forward) {
            this.reverse = 1;
        } else {
            this.reverse = 0;
        }
        this.forward = true;

        CdpSnapshotInfo[] infos = getSnapshots(this.sHandle, count, 1, this.snapshotInfos.size());
        if (infos == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "none"), null));
            return;
        }
        if (infos.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "last"), null));
            return;
        }
        List<CdpSnapshotInfo> tmp = new ArrayList();
        for (CdpSnapshotInfo info : infos) {
            if (info.isbHasZFSSnapshot()) {
                tmp.add(info);
            }
        }
        if (tmp.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "last"), null));
            return;
        }
        this.snapshotInfos = tmp;

        if (this.snapshotInfos != null && !this.snapshotInfos.isEmpty()) {
            Collections.sort(snapshotInfos);
        }
    }

    public void seekTime() {
        closeQueryHandle();
        getSnapshotID();
        getQuerySnapshotHandle(this.sID);
        CdpSnapshotInfo[] infos = getSnapshots(this.sHandle, count, 1, 0);
        this.snapshotInfos = new ArrayList();
        for (CdpSnapshotInfo info : infos) {
            if (info.isbHasZFSSnapshot()) {
                this.snapshotInfos.add(info);
            }
        }
        if (this.snapshotInfos != null && !this.snapshotInfos.isEmpty()) {
            Collections.sort(snapshotInfos);
        }
    }

    private void getQuerySnapshotHandle(long snapShotID) {
        if(group != null){
            this.sHandle = cdp.getQuerySnapshotHandle(guid, snapShotID, group.getDiskZfsName());
        }
    }

    private CdpSnapshotInfo[] getSnapshots(int handle, int count, int forwardislast, int currentCount) {
        return cdp.querySnapshotInfos(path, handle, count, forwardislast, reverse, currentCount);
    }

    private void closeQueryHandle() {
        cdp.closeQuerySnapshotHandle(this.sHandle);
    }

    private void getSnapshotID() {
        this.sID = cdp.getAppointedTimeSnapshotID(guid, path, date.getTime());
    }

    public List<CdpSnapshotInfo> getSnapshotInfos() {
        return snapshotInfos;
    }

    public void setSnapshotInfos(List<CdpSnapshotInfo> snapshotInfos) {
        this.snapshotInfos = snapshotInfos;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void change() {
    }

    public String cancelQ() {
        String param = "guid=" + guid + "&path=" + path + "&level=" + level;
        for(String name : this.groupNames){
            param += "&groupName=" + name;
        }
        return "snapshot_r?faces-redirect=true&amp;" + param;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getSID() {
        return sID;
    }

    public void setSID(long sID) {
        this.sID = sID;
    }

    public CdpSnapshotInfo getSelected() {
        return selected;
    }

    public void setSelected(CdpSnapshotInfo selected) {
        this.selected = selected;
    }

    public boolean isRolling() {
        return rolling;
    }

    public void setRolling(boolean rolling) {
        this.rolling = rolling;
    }

    public CdpDiskGroupInfo getGroup() {
        return group;
    }

    public void setGroup(CdpDiskGroupInfo group) {
        this.group = group;
    }

    public String record() {
        String param = "sID=" + this.sID + "&guid=" + this.guid + "&path=" + this.path;
        for(String name : this.groupNames){
            param += "&groupName=" + name;
        }
        return "record?faces-redirect=true&amp;" + param;
    }

    public GlobalProtectStatus getGlobalStatus() {
        return globalStatus;
    }

    public final void setGlobalStatus(GlobalProtectStatus globalStatus) {
        this.globalStatus = globalStatus;
    }

    private void initGroup() {
        group = cdp.getDiskGroupInfo(guid);
        if (group != null) {
            this.dg = group.getDiskGroupName();
        }
    }

    public boolean isCopy() {
        return dg.contains(CDPConstants.SNAPSHOT_COPY);
    }

    public String getAgentString(CdpSnapshotInfo snap) {
        String s = "";
        int id = (int) snap.getAgentID();
        String name = snap.getSnapshotName();
        switch (id) {
            case AgentBean.AGENT_ORACLE:
                s = "Oracle代理";
                break;
            case AgentBean.AGENT_SQLSERVER:
                s = "SQL Server代理";
                break;
            case 999:
                s = "系统快照";
                break;
            default:
                if (name.startsWith("DEFAULT")) {
                    s = "默认代理";
                } else {
                    s = name.split("_")[0];
                }
                break;
        }
        return s;
    }

    public void deleteAllSnap() {
        boolean flag = InterfaceFactory.getCDPInterfaceInstance().deleteAllSnapshot(this.group.getDiskGroupPath());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
            return;
        }
        this.init();
    }
}
