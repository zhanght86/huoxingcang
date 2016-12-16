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
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.nas.model.SyncStatus;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.sync.web.impl.MsaSYNCInterfaceImpl;
import com.marstor.msa.util.InterfaceFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
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
@ManagedBean(name = "snapshotBean")
@ViewScoped
public class SnapshotBean {

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
    private boolean isLevelS = true;
    private GlobalProtectStatus globalStatus;
    private List<SnapShot> snaps = new ArrayList<SnapShot>();
    private SnapShot selectSnap = new SnapShot();
    private CdpDiskGroupInfo group;
    private String dg;
    private List<String> groupNames;

    public SnapshotBean() {
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
        boolean flag = cdp.cancelRollback(this.group.getDiskGroupPath());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("cancelRollFailed"), res.get("cancelRollFailed")));
            return;
        }
        this.deleteAllDis = false;
        this.init();
        globalStatus.update();
    }

    public void saveRollback() {
        boolean flag = cdp.saveRollback(this.group.getDiskGroupPath());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("saveRollFailed"), res.get("saveRollFailed")));
            return;
        }
        this.deleteAllDis = false;
        this.init();
        globalStatus.update();
    }

    public void updateSnapAndSync() {
        this.deleteAllDis = false;
        init();
        globalStatus.update();
    }

    public void init() {
        initGroup();
        if (level == 1) {
            this.snaps = new ArrayList();
            if(path.startsWith("/")){
                path = path.substring(1);
            }            
            SyncStatus status = FileSysSnapSYNCInfo.getLocalSyncStatus(path);
            if (status == null) {
                return;
            }
            String rollBacked = ""; //已回滚的快照
            if (status.isRollBackOrNot()) { //如果是回滚后快照
                List<String> strSnaps = sync.getSnapshotListAfterRollback(path);
                if (strSnaps == null) {
                    return;
                }
                if (strSnaps.size() > 0) {
                    rollBacked = strSnaps.get(strSnaps.size() - 1);
                }
                for (int i = 0; i < strSnaps.size(); i++) {
                    String strSnap = strSnaps.get(i);
                    String array[] = strSnap.trim().split(";");
                    String type;
                    if (array != null && array.length > 5) {
                        if (array[1].contains("AUTO")) {
                            type = res.get("auto");
                        } else {
                            type = res.get("manual");
                        }
                        SnapShot snapShot = new SnapShot(array[1], array[3], type, array[0]);
                        if (array[1].equals(rollBacked)) {//如果快照名称为已回滚快照
                            snapShot.setSaveRollRender(true);
                            snapShot.setCancelRollRender(true);
                            snapShot.setRollRender(false);
                        } else {
                            snapShot.setSaveRollRender(false);
                            snapShot.setCancelRollRender(false);
                            snapShot.setRollRender(true);
                        }
                        snapShot.setDeleteRender(false);
                        this.setDeleteAllDis(true);
                        this.snaps.add(snapShot);
                    }
                }
            } else {                
                List<Snapshot> autoSnap = sync.getGetAutoSnapshot(path);
                if (autoSnap != null) {
                    for (Snapshot snap : autoSnap) {
                        SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime(), snap.getClones());
                        this.snaps.add(snapShot);
                    }
                }
                List<Snapshot> manualSnap = sync.getGetManualSnapshot(path);
                if (manualSnap != null) {
                    for (Snapshot snap : manualSnap) {
                        SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime(), snap.getClones());

                        this.snaps.add(snapShot);
                    }
                }
                Collections.sort(this.snaps);
            }
        }  else if (level == 2) {
            this.isLevelS = false;
            getQuerySnapshotHandle(-1);
            this.reverse = 0;
            this.forward = true;
            CdpSnapshotInfo[] infos = getSnapshots(this.sHandle, count, 1, 0);
            if (infos != null && infos.length != 0) {
                this.snapshotInfos = new ArrayList();
                for (CdpSnapshotInfo info : infos) {
                    if (!info.isbHasZFSSnapshot() && !dg.contains(CDPConstants.SNAPSHOT_COPY)) {
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
        }
        
    }

    public String getDg() {
        if(dg != null || !dg.equals("")){
            return dg;
        }
        initGroup();
        if (level == 1) {
            this.snaps = new ArrayList();
            if(path.startsWith("/")){
                path = path.substring(1);
            }     
            SyncStatus status = FileSysSnapSYNCInfo.getLocalSyncStatus(path);
            if (status == null) {
                return dg;
            }
            String rollBacked = ""; //已回滚的快照
            if (status.isRollBackOrNot()) { //如果是回滚后快照
                List<String> strSnaps = sync.getSnapshotListAfterRollback(path);
                if (strSnaps == null) {
                    return dg;
                }
                if (strSnaps.size() > 0) {
                    rollBacked = strSnaps.get(strSnaps.size() - 1);
                }
                for (int i = 0; i < strSnaps.size(); i++) {
                    String strSnap = strSnaps.get(i);
                    String array[] = strSnap.trim().split(";");
                    String type;
                    if (array != null && array.length > 5) {
                        if (array[1].contains("AUTO")) {
                            type = res.get("auto");
                        } else {
                            type = res.get("manual");
                        }
                        SnapShot snapShot = new SnapShot(array[1], array[3], type, array[0]);
                        if (array[1].equals(rollBacked)) {//如果快照名称为已回滚快照
                            snapShot.setSaveRollRender(true);
                            snapShot.setCancelRollRender(true);
                            snapShot.setRollRender(false);
                        } else {
                            snapShot.setSaveRollRender(false);
                            snapShot.setCancelRollRender(false);
                            snapShot.setRollRender(true);
                        }
                        snapShot.setDeleteRender(false);
                        this.setDeleteAllDis(true);
                        this.snaps.add(snapShot);
                    }
                }
            } else {                
                List<Snapshot> autoSnap = sync.getGetAutoSnapshot(path);
                if (autoSnap != null) {
                    for (Snapshot snap : autoSnap) {
                        SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime(), snap.getClones());
                        this.snaps.add(snapShot);
                    }
                }
                List<Snapshot> manualSnap = sync.getGetManualSnapshot(path);
                if (manualSnap != null) {
                    for (Snapshot snap : manualSnap) {
                        SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime(), snap.getClones());

                        this.snaps.add(snapShot);
                    }
                }
                Collections.sort(this.snaps);
            }
        } else if (level == 2) {
            this.isLevelS = false;
            getQuerySnapshotHandle(-1);
            CdpSnapshotInfo[] infos = getSnapshots(this.sHandle, count, 1, 0);
            if (infos != null && infos.length != 0) {
                this.snapshotInfos = new ArrayList();
                for (CdpSnapshotInfo info : infos) {
                    if (!info.isbHasZFSSnapshot() && !dg.contains(CDPConstants.SNAPSHOT_COPY)) {
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
        }
        globalStatus = new GlobalProtectStatus(path, level, group, dg, groupNames);
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public String doBeforeAddMSnap() {
        String param = "path=" + this.path + "&guid=" + this.guid;
        return "add_msnap?faces-redirect=true&amp;" + param;
    }

    public String doBeforeAddSnap() {
        String param = "path=" + this.path + "&guid=" + this.guid;
        return "add_snap?faces-redirect=true&amp;" + param;
    }

    public void beforeRollback() {
        CdpDiskInfo[] disks = cdp.getDiskInfos(group.getDiskGroupGuid(), group.getDiskGroupPath());
        ViewInformation[] lun = null;
        if (disks != null && disks.length > 0) {
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            if (disks[0].getLuInfoBean() != null) {
                lun = scsi.getLUNView(disks[0].getLuInfoBean().getlUName());
            }
        }
        if (this.group.getiAutoSnapshotIsOpen() == 1) {
            if (lun != null && lun.length > 0) {
                RequestContext.getCurrentInstance().execute("SdisLUNDelXX.show()");
            } else {
                RequestContext.getCurrentInstance().execute("SdeleteDGBoxXX.show()");
            }
        } else {
            if (lun != null && lun.length > 0) {
                RequestContext.getCurrentInstance().execute("disLUNDelXX.show()");
            } else {
                RequestContext.getCurrentInstance().execute("deleteDGBoxXX.show()");
            }
        }
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
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        if (this.group.isCDPStarted()) {
            cdp.stopCDP(this.group.getDiskGroupGuid(), this.group.getDiskGroupPath(),
                    this.group.getProtectType());
        }
        if (this.group.getiMount() == CDPConstants.TRUE) {
            ViewInformation[] lun = null;
            if (disks.length > 0) {
                if (disks[0].getLuInfoBean() != null) {
                    lun = scsi.getLUNView(disks[0].getLuInfoBean().getlUName());
                }
            }
            if (lun != null && lun.length > 0) {
                for (int i = 0; i < lun.length; i++) {
                    for (int j = 0; j < this.group.getDiskCount(); j++) {
                        scsi.removeView(disks[j].getLuInfoBean().getlUName(), lun[i].entry);
                    }
                }
            }
        }
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
        boolean success = cdp.cdpRollback(path, guid, 0, selected.getSnapshotTime());
        this.rolling = true;
        if (!success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
//        this.init();
//        globalStatus.update();
        RequestContext.getCurrentInstance().execute("PF('pbAjax').start()");
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
        copyName = sync.createClone(para + this.selected.getSnapshotName());
        if (copyName == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "fail"), global.get("error_mark")));
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

    public void rollBackSnap() {
        boolean flag;
        CdpDiskInfo[] disks = cdp.getDiskInfos(guid, "/" + path);
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        if (disks == null) {
            return;
        }
        if (this.group.getiAutoSnapshotIsOpen() == 1) {
            cdp.stopCDP(this.group.getDiskGroupGuid(), this.group.getDiskGroupPath(),
                    this.group.getProtectType());
        }
        if (this.group.getiMount() == CDPConstants.TRUE) {
            ViewInformation[] lun = null;
            if (disks.length > 0) {
                if (disks[0].getLuInfoBean() != null) {
                    lun = scsi.getLUNView(disks[0].getLuInfoBean().getlUName());
                }
            }
            if (lun != null && lun.length > 0) {
                for (int i = 0; i < lun.length; i++) {
                    for (int j = 0; j < this.group.getDiskCount(); j++) {
                        scsi.removeView(disks[j].getLuInfoBean().getlUName(), lun[i].entry);
                    }
                }
            }
        }
        ViewInformation[] views = scsi.getLUNView(disks[0].getLuInfoBean().getlUName());
        int len = disks.length;
        String[] lus;
        lus = new String[len];
        for (int i = 0; i < len; i++) {
            lus[i] = disks[i].getLuInfoBean().getlUName();
        }
        if (views != null && views.length > 0) {
            cdp.offlineLU(this.group.getDiskGroupName(), lus);
        }
        List<String> rollList = this.getBeforeRollSnaps();
        String lastSnap = "";
        if (rollList != null && rollList.size() > 0) {
            lastSnap = rollList.get(0);
        }
        flag = InterfaceFactory.getSYNCInterfaceInstance().rollbackSnapshot(this.path, lastSnap, this.selectSnap.getStrName(), this.getBeforeRollSnaps());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("rollBackFailed"), res.get("rollBackFailed")));
            return;
        }
        this.init();
        globalStatus.update();
    }

    public void updateAfterRoll() {
        List<SnapShot> snapList = new ArrayList<SnapShot>();
        String rollBacked = ""; //已回滚的快照
        //如果回滚过快照
        List<String> afterSnaps = sync.getSnapshotListAfterRollback(path);

        if (afterSnaps != null) {
            if (afterSnaps.size() > 0) {
                rollBacked = afterSnaps.get(afterSnaps.size() - 1);
            }

            for (int i = 0; i < afterSnaps.size(); i++) {
                String strSnap = afterSnaps.get(i);
                String array[] = strSnap.trim().split(";");
                String type;
                if (array != null && array.length > 5) {
                    if (array[1].contains("AUTO")) {
                        type = res.get("auto");
                    } else {
                        type = res.get("manual");
                    }
                    SnapShot snapShot = new SnapShot(array[1], array[3], type, array[0]);
                    if (array[1].equals(rollBacked)) {//如果快照名称为已回滚快照
                        snapShot.setSaveRollRender(true);
                        snapShot.setCancelRollRender(true);
                        snapShot.setRollRender(false);
                    } else {
                        snapShot.setSaveRollRender(false);
                        snapShot.setCancelRollRender(false);
                        snapShot.setRollRender(true);
                    }
                    snapShot.setDeleteRender(false);
                    this.setDeleteAllDis(true);
                    snapList.add(snapShot);
                }
            }
        }

        this.snaps = snapList;
        Collections.sort(this.snaps);
        globalStatus.update();
    }

    public List<String> getBeforeRollSnaps() {
        List<String> strRollSnaps = new ArrayList<String>();
        String status[] = this.getLocalFileSystemStatus(this.path);
        String flag = "";
        if (status != null) {
            flag = status[6];
        }
        if (flag.equals("1")) {
            strRollSnaps = this.getAfterRollSnaps(); //返回上一次回滚后的列表
            return strRollSnaps;
        }
        List<SnapShot> rollSnaps = new ArrayList<SnapShot>();
        //rollSnaps = this.snaps;
        for (SnapShot snap : this.snaps) {
            rollSnaps.add(snap);
        }
        Collections.sort(rollSnaps);
        for (int k = 0; k < rollSnaps.size(); k++) {
            String str = "";
            String fsName = rollSnaps.get(k).getStrName();
            String[] sName = fsName.split("_");
            String name = sName[sName.length - 2];
            if (name.equalsIgnoreCase("AUTO")) {
                name = "";
            }
            String time = sName[sName.length - 1].trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            try {
                str = str + simpleDateFormat.format(sdf.parse(time)) + ";";
            } catch (ParseException ex) {
                return null;
            }
            str = str + fsName + ";";
            if (name.equals("")) {
                str = str + "-2" + ";";
            } else {
                str = str + name + ";";
            }
            str = str + rollSnaps.get(k).getStrUsed() + ";";
            str = str + rollSnaps.get(k).getStrRefer() + ";";
            String fsExpirationdate = String.valueOf(rollSnaps.get(k).getiExpirationdate());
            if (fsExpirationdate != null) {
                if (fsExpirationdate.equalsIgnoreCase("-1")) {
                    fsExpirationdate = res.get("effectForever");
                }
                str = str + fsExpirationdate;
            } else {
                str = str + "-2";
            }
            //getStrRollback[k] = str;
            strRollSnaps.add(str);
        }
        return strRollSnaps;
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
        this.deleteAllDis = false;
        this.init();
        globalStatus.update();
    }

    public void cancelRoll() {
        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().cancelRollback(path);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("cancelRollFailed"), res.get("cancelRollFailed")));
            return;
        }
        this.deleteAllDis = false;
        this.init();
        globalStatus.update();
    }

    public void beforeDeleteMSnap() {
        Collection<String> origins = sync.getCloneOriginMap().values();
        for (String origin : origins) {
            if (origin.equals(this.selectSnap.getStrName())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapsError"), res.get("delSnapsError")));
                return;
            }
        }
        RequestContext.getCurrentInstance().execute("delMSnap.show();");
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

    public void deleteMSnap() {

        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().destroySnapshot(this.selectSnap.getStrName());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
            return;
        }
        this.updateAfterDelete();
    }

    public void beforeDeleteAllMSnap() {
        Collection<String> origins = sync.getCloneOriginMap().values();
        
        for (SnapShot ss : this.snaps) {
            for (String origin : origins) {
                if (origin.equals(ss.getStrName())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delAllSnapsError"), res.get("delAllSnapsError")));
                    return;
                }
            }
        }
        RequestContext.getCurrentInstance().execute("delAllMSnap.show();");
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

    public void deleteAllMSnap() {

        boolean flag = InterfaceFactory.getCDPInterfaceInstance().deleteAllSnapshot(this.group.getDiskGroupPath());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
            return;
        }
        this.updateAfterDelete();
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

    public void updateAfterDelete() {
        List<SnapShot> snapList = new ArrayList<SnapShot>();
        List<Snapshot> autoSnap = sync.getGetAutoSnapshot(path);
        if (autoSnap != null) {
            for (Snapshot snap : autoSnap) {
                SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime(), snap.getClones());

                snapList.add(snapShot);
            }
        }
        List<Snapshot> manualSnap = sync.getGetManualSnapshot(path);
        if (manualSnap != null) {
            for (Snapshot snap : manualSnap) {
                SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime(), snap.getClones());

                snapList.add(snapShot);
            }
        }
        this.snaps = snapList;
        Collections.sort(this.snaps);
        globalStatus.update();
    }
    
    private String copyName;
    
    public void preClone(){
        RequestContext.getCurrentInstance().execute("clone.show()");
    }

    public void createCopy() {
        String str = InterfaceFactory.getSYNCInterfaceInstance().createClone(this.selectSnap.getStrName());
        if (str == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createCopyFailed"), res.get("createCopyFailed")));
        }
        
        copyName = str;
        if (copyName == null) {
            return;
        }
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("createCopy") + global.get("lquote") + copyName + global.get("rquote") + global.get("success") + global.get("error_mark"), res.get("createCopy") + global.get("rquote") + copyName + global.get("lquote") + global.get("success") + global.get("error_mark")));
        
        cdp.setProtectType(copyName, group.getProtectType());
        globalStatus.setContainsCopy(true);//创建副本后设置已包含副本
        this.deleteAllDis = false;
        this.init();
        globalStatus.update();       
        RequestContext.getCurrentInstance().execute("dgs.show()");
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
        if (group != null && group.getSyncStatusInfo() != null && group.getSyncStatusInfo().getIsRollback() == CDPConstants.FALSE) {
            render = false;
        }
        return render;
    }

    public boolean getDisableSave() {
        boolean disable = false;
        if (group != null && group.getSyncStatusInfo() != null && group.getSyncStatusInfo().getIsRollback() == CDPConstants.FALSE) {
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
        if (group != null && group.getSyncStatusInfo() != null && group.getSyncStatusInfo().getIsRollback() == CDPConstants.FALSE) {
            disable = true;
        }
        if (group.getRollbackInfo() != null && group.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_RUNNING) {
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
        this.deleteAllDis = false;
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
        this.sHandle = cdp.getQuerySnapshotHandle(guid, snapShotID, group.getDiskZfsName());
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
    
    public String cancelQ(){
        String param = "guid=" + guid + "&path=" + path + "&level=" + level;
        return "snapshot?faces-redirect=true&amp;" + param;
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

    public String record() {
        String param = "sID=" + this.sID + "&guid=" + this.guid + "&path=" + this.path;
        return "record?faces-redirect=true&amp;" + param;
    }

    public boolean isIsLevelS() {
        return isLevelS;
    }

    public void setIsLevelS(boolean isLevelS) {
        this.isLevelS = isLevelS;
    }

    public GlobalProtectStatus getGlobalStatus() {
        return globalStatus;
    }

    public final void setGlobalStatus(GlobalProtectStatus globalStatus) {
        this.globalStatus = globalStatus;
    }
    private boolean deleteAllDis;

    public boolean getDeleteAllDis() {
        if (!deleteAllDis) {
            if (this.snaps == null || this.snaps.isEmpty()) {
                deleteAllDis = true;
            }
        }
        return deleteAllDis;
    }

    public final void setDeleteAllDis(boolean b) {
        if (this.snaps == null || this.snaps.isEmpty()) {
            deleteAllDis = true;
        } else {
            deleteAllDis = b;
        }
    }

    public List<SnapShot> getSnaps() {
        if(snaps == null || snaps.isEmpty()){    
            this.deleteAllDis = false;
            this.init();
        }
        return snaps;
    }

    public void setSnaps(List<SnapShot> snaps) {
        this.snaps = snaps;
    }

    public SnapShot getSelectSnap() {
        return selectSnap;
    }

    public void setSelectSnap(SnapShot selectSnap) {
        this.selectSnap = selectSnap;
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

    public String getAgentString(int id) {
        String s = "默认代理";
        switch (id) {
            case AgentBean.AGENT_ORACLE:
                s = "Oracle代理";
                break;
            case AgentBean.AGENT_SQLSERVER:
                s = "SQL Server代理";
                break;
            case 999:
                s = "系统快照";
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
