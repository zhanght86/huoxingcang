/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.sync.bean.MarsHost;
import com.marstor.msa.sync.bean.SyncMapping;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.sync.web.impl.MsaSYNCInterfaceImpl;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "synclist")
@ViewScoped
public class SyncListBean implements Serializable{

//    private MsaSYNCInterfaceImpl syncInterface = (MsaSYNCInterfaceImpl)InterfaceFactory.getSYNCInterfaceInstance();
    private MsaSYNCInterface syncInterface = InterfaceFactory.getSYNCInterfaceInstance();
//    private ArrayList<LocalSync> localList = new ArrayList<LocalSync>();
//    private ArrayList<RemoteSync> remoteList = new ArrayList<RemoteSync>();
    private ArrayList<Sync> syncList = new ArrayList<Sync>();
    private ArrayList<Sync> localSyncList = new ArrayList<Sync>();
    private ArrayList<Sync> remoteSyncList = new ArrayList<Sync>();
    private Sync selectSync = new Sync();
    private String path;
    private String selectType = "";
    private String module;
    private String returnURL;
    private MSAResource res = new MSAResource();

    public SyncListBean(String path, String modul, String returnURL) {
//        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
//        path = request.getParameter("path");
        if (path == null) {
            return;
        }
        this.path = path;
        if (modul != null) {
            module = modul;
        }
        if (returnURL != null) {
            this.returnURL = returnURL;
        }
        List<SyncMapping> mapps = syncInterface.getSyncMapping(path);
        if (mapps == null) {
            return;
        }
        Sync sync;
        for (SyncMapping map : mapps) {
            MarsHost host = new MarsHost(map.getStrIP(), map.getStrPort(), map.getStrUserName(), map.getStrPassword(), map.getStrSSHPort(), map.getStrRootPassword());
            Debug.print("bTimingJob: " + map.isbTimingJob());
            String targetFileSystem = "";
            if (module.equals("nas")) { //如果进入的NAS模块的页面，目标文件系统前需要加“/”
                targetFileSystem = "/" + map.getStrDescFileSystem();
            } else {
                targetFileSystem = map.getStrDescFileSystem();
            }
            if (map.getiIsLocal() == 1) { //如果是本地复制
                sync = new Sync(targetFileSystem, map.getStrFirstSnapshot(), Sync.localType, res.get("local"), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host, map.getJobStartTime(), map.getJobEndTime(), map.isbTimingJob());
                this.localSyncList.add(sync);
            } else {
                sync = new Sync(targetFileSystem, map.getStrFirstSnapshot(), Sync.remoteType, map.getStrIP(), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host, map.getJobStartTime(), map.getJobEndTime(), map.isbTimingJob());
                this.remoteSyncList.add(sync);
            }
            //  this.syncList.add(sync);
        }

    }

    public void init() {
        ArrayList<Sync> localList = new ArrayList<Sync>();
        ArrayList<Sync> remoteList = new ArrayList<Sync>();
        //ArrayList<Sync> syncs = new ArrayList<Sync>();
        List<SyncMapping> mapps = syncInterface.getSyncMapping(path);
        if (mapps == null) {
            return;
        }
        Sync sync;
        for (SyncMapping map : mapps) {
//            if (map.getStrSrcFileSystem().equals(path)) {
            MarsHost host = new MarsHost(map.getStrIP(), map.getStrPort(), map.getStrUserName(), map.getStrPassword(), map.getStrSSHPort(), map.getStrRootPassword());
            String targetFileSystem = "";
            if (module.equals("nas")) { //如果进入的NAS模块的页面，目标文件系统前需要加“/”
                targetFileSystem = "/" + map.getStrDescFileSystem();
            } else {
                targetFileSystem = map.getStrDescFileSystem();
            }
            if (map.getiIsLocal() == 1) { //本地
                sync = new Sync(targetFileSystem, map.getStrFirstSnapshot(), Sync.localType, res.get("local"), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host, map.getJobStartTime(), map.getJobEndTime(), map.isbTimingJob());
                // this.localList.add(local);
                localList.add(sync);
            } else {
                sync = new Sync(targetFileSystem, map.getStrFirstSnapshot(), Sync.remoteType, map.getStrIP(), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host, map.getJobStartTime(), map.getJobEndTime(), map.isbTimingJob());
                //this.remoteList.add(remote);
                remoteList.add(sync);
            }
        }
        this.localSyncList = localList;
        this.remoteSyncList = remoteList;

    }

//    public boolean isAddLocalSyncDisable() {
//        return addLocalSyncDisable;
//    }
//
//    public void setAddLocalSyncDisable(boolean addLocalSyncDisable) {
//        this.addLocalSyncDisable = addLocalSyncDisable;
//    }
//
//    public boolean isAddRemoteSyncDisable() {
//        return addRemoteSyncDisable;
//    }
//
//    public void setAddRemoteSyncDisable(boolean addRemoteSyncDisable) {
//        this.addRemoteSyncDisable = addRemoteSyncDisable;
//    }
//
//    public boolean isDeleteSyncDisable() {
//        return deleteSyncDisable;
//    }
//
//    public void setDeleteSyncDisable(boolean deleteSyncDisable) {
//        this.deleteSyncDisable = deleteSyncDisable;
//    }
//
//    public boolean isResumeSyncDisable() {
//        return resumeSyncDisable;
//    }
//
//    public void setResumeSyncDisable(boolean resumeSyncDisable) {
//        this.resumeSyncDisable = resumeSyncDisable;
//    }
//
//    public boolean isStartSyncDisable() {
//        return startSyncDisable;
//    }
//
//    public void setStartSyncDisable(boolean startSyncDisable) {
//        this.startSyncDisable = startSyncDisable;
//    }
//
//    public boolean isSuspendSyncDisable() {
//        return suspendSyncDisable;
//    }
//
//    public void setSuspendSyncDisable(boolean suspendSyncDisable) {
//        this.suspendSyncDisable = suspendSyncDisable;
//    }
//
//    public boolean isStopSyncDisable() {
//        return stopSyncDisable;
//    }
//
//    public void setStopSyncDisable(boolean stopSyncDisable) {
//        this.stopSyncDisable = stopSyncDisable;
//    }
    public ArrayList<Sync> getLocalSyncList() {
        return localSyncList;
    }

    public void setLocalSyncList(ArrayList<Sync> localSyncList) {
        this.localSyncList = localSyncList;
    }

    public ArrayList<Sync> getRemoteSyncList() {
        return remoteSyncList;
    }

    public void setRemoteSyncList(ArrayList<Sync> remoteSyncList) {
        this.remoteSyncList = remoteSyncList;
    }

    public Sync getSelectSync() {
        return selectSync;
    }

    public void setSelectSync(Sync selectSync) {
        this.selectSync = selectSync;
    }

//    public boolean isSyncTargetOrNot() {
//        return syncTargetOrNot;
//    }
//
//    public void setSyncTargetOrNot(boolean syncTargetOrNot) {
//        this.syncTargetOrNot = syncTargetOrNot;
//    }
    public MsaSYNCInterface getSyncInterface() {
        return syncInterface;
    }

//    public void setSyncInterface(MsaSYNCInterface syncInterface) {
//        this.syncInterface = syncInterface;
//    }
    public ArrayList<Sync> getSyncList() {
        return syncList;
    }

    public void setSyncList(ArrayList<Sync> syncList) {
        this.syncList = syncList;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void startSYNC() {
        boolean flag;
        Debug.print("select sync target filesys : " + this.selectSync.getTargetFileSystem());
        Debug.print("select sync type : " + this.selectSync.getType());
        if (this.selectSync.getType() == Sync.localType) {
            flag = syncInterface.startSync(path, this.selectSync.getiDescFileSystemHashCode(), true);
        } else {
            flag = syncInterface.startSync(path, this.selectSync.getiDescFileSystemHashCode(), false);
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("startCopyFailed"), res.get("startCopyFailed")));
            Debug.print("start sync failed: " + flag);
            return;
        }
//        this.selectSync.setStartRendered(false);
//        this.selectSync.setStopRendered(true);
//        this.selectSync.setSuspendRendered(true);
//        this.selectSync.setSuspendDisabled(false);
//        this.selectSync.setResumeRendered(false);
//        this.selectSync.setDeleteDisabled(true);
        this.init();
    }

    public void suspendSYNC() {
        boolean flag;
        if (this.selectSync.getType() == 1) {
            flag = syncInterface.suspendSync(path, this.selectSync.getiDescFileSystemHashCode(), true);
        } else {
            flag = syncInterface.suspendSync(path, this.selectSync.getiDescFileSystemHashCode(), false);
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("pauseCopyFailed"), res.get("pauseCopyFailed")));
            Debug.print("suspend sync failed: " + flag);
            return;
        }
//        this.selectSync.setStopRendered(true);
//        this.selectSync.setResumeRendered(true);
//        this.selectSync.setStartRendered(false);
//        this.selectSync.setSuspendRendered(false);
//        this.selectSync.setDeleteDisabled(true);
        this.init();

    }

    public void deleteSYNC() {
        boolean flag;
        if (this.selectSync.getType() == 1) {
            flag = syncInterface.deleteSync(path, this.selectSync.getiDescFileSystemHashCode(), true);
        } else {
            flag = syncInterface.deleteSync(path, this.selectSync.getiDescFileSystemHashCode(), false);
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delCopyFailed"), res.get("delCopyFailed")));
            Debug.print("delete sync failed: " + flag);
            return;
        }
        this.init();
    }

    public void stopTargetSync() {
        boolean flag = syncInterface.abortSync(path);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCopyFailed"), res.get("stopCopyFailed")));
            Debug.print("target stop sync failed: " + flag);
            return;
        }

        //this.syncTargetOrNot = false;
    }

    public void stopSourceSync() {
        boolean flag;
        if (this.selectSync.getType() == 1) {
            flag = syncInterface.closeSync(this.path, this.selectSync.getiDescFileSystemHashCode(), true);
        } else {
            flag = syncInterface.closeSync(this.path, this.selectSync.getiDescFileSystemHashCode(), false);
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCopyFailed"), res.get("stopCopyFailed")));
            Debug.print("source stop sync failed: " + flag);
            return;
        }
//        this.selectSync.setStartRendered(true);
//        this.selectSync.setSuspendRendered(true);
//        this.selectSync.setStopRendered(false);
//        this.selectSync.setResumeRendered(false);
//        this.selectSync.setSuspendDisabled(true);
//        this.selectSync.setDeleteDisabled(false);

        this.init();
    }
//
//    public void listen() {
//        if (this.selectType.equals("1")) {
//            this.syncList = this.localList;
//        } else {
//            this.syncList = this.remoteList;
//        }
//    }

    public void resumeSync() {
        boolean flag;
        SyncStatus status = FileSysSnapSYNCInfo.getLocalSyncStatus(path);
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getLocalFileSysFailed"), res.get("getLocalFileSysFailed")));
            Debug.print("get local file system info failed: ");
            return;
        }
        if (this.selectSync.getType() == 1) {
            flag = syncInterface.resumeSync(path, this.selectSync.getiDescFileSystemHashCode(), true, status.isRollBackOrNot());
        } else {
            flag = syncInterface.resumeSync(path, this.selectSync.getiDescFileSystemHashCode(), false, status.isRollBackOrNot());
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("resumeSyncFailed"), res.get("resumeSyncFailed")));
            Debug.print("resume sync failed: " + flag);
            return;
        }
//        this.selectSync.setStartRendered(false);
//        this.selectSync.setStopRendered(true);
//        this.selectSync.setSuspendRendered(true);
//        this.selectSync.setSuspendDisabled(false);
//        this.selectSync.setResumeRendered(false);
//        this.selectSync.setDeleteDisabled(true);
        this.init();
    }

    public String addLocalSync() {
        String param = "path=" + this.path + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "nas_add_local_sync?faces-redirect=true&amp;" + param;
    }

    public String addRemoteSync() {
        String param = "path=" + this.path + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "nas_add_remote_sync?faces-redirect=true&amp;" + param;
    }

    public String configSyncPlan(Sync sync) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Calendar cal;
        Date date;
        cal = sync.getJobStartTime();
        date = cal.getTime();
        String startTime = df.format(date);
        cal = sync.getJobEndTime();
        date = cal.getTime();
        String endTime = df.format(date);
        String bTimingJob = sync.isbTimingJob() ? "1" : "0";
        Debug.print("bTimingJob str: " + bTimingJob);
        Debug.print("start time: " + startTime);
        Debug.print("end time: " + endTime);
        String param = "path=" + this.path + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL + "&" + "iFileSystemHashCode=" + sync.getiDescFileSystemHashCode() + "&"
                + "iLocal=" + sync.getType() + "&" + "startTime=" + startTime + "&" + "endTime=" + endTime + "&" + "bTimingJob=" + bTimingJob;
        Debug.print("param= " + param);
        return "nas_sync_plan?faces-redirect=true&amp;" + param;
    }

    public void checkBeforeStartSync(Sync sync) {
        //  FileSysSnapSYNCInfo.getRemoteSyncStatus(this.selectedPath, this.selectHost.getIp(), this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword());

        SyncStatus status;
        Debug.print("sync target filesys : " + sync.getTargetFileSystem());
        Debug.print("sync type : " + sync.getType());
//        Boolean syncExistOrNot = this.isSyncExist();
//        if (syncExistOrNot == null) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getCopyListFailed"), res.get("getCopyListFailed")));
//            return ;
//        }
//        if (syncExistOrNot) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetExist"), res.get("targetExist")));
//            return;
//        }
        if (sync.getType() == Sync.localType) {
            status = FileSysSnapSYNCInfo.getLocalSyncStatus(sync.getTargetFileSystem());
        } else {
            status = FileSysSnapSYNCInfo.getRemoteSyncStatus(sync.getTargetFileSystem(), sync.getHost().getIp(), sync.getHost().getPort(), sync.getHost().getUsername(), sync.getHost().getPassword());
        }
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
            return;
        }
        if (status.isSyncTargetOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetCopied"), res.get("targetCopied")));
            return;
        }
        if (status.isOnLineOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetFileSysMustOffLine"), res.get("targetFileSysMustOffLine")));
            return;
        }
        if (status.isExistSnapOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetFileSysCanNotExistSnap"), res.get("targetFileSysCanNotExistSnap")));
            return;
        }
        RequestContext.getCurrentInstance().execute("startsync.show()");

    }
//    //判断当前添加的远程同步是否存在
//
//    public Boolean isSyncExist() {
//        List<SyncMapping> mapps = syncInterface.getSyncMapping(this.path);
//        if (mapps == null) {
//            return null;
//        }
//        String source = "";
//        if (this.path.startsWith("/")) {
//            source = this.path.substring(this.path.indexOf("/"));
//        } else {
//            source = this.path;
//        }
//        String targetFileSys = this.selectSync.getTargetFileSystem();
//        //    Sync sync = new Sync(this.selectedVolume, this.selectedIP);
//        for (SyncMapping map : mapps) {
//            if (map.getiIsLocal() != 1) {
//                if (this.selectSync.getTargetIP().equals(map.getStrIP()) && targetFileSys.equals(map.getStrDescFileSystem())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public void checkBeforeResumeSync(Sync sync) {
        //  FileSysSnapSYNCInfo.getRemoteSyncStatus(this.selectedPath, this.selectHost.getIp(), this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword());
        SyncStatus status;
        if (sync.getType() == 1) {
            status = FileSysSnapSYNCInfo.getLocalSyncStatus(sync.getTargetFileSystem());
        } else {
            status = FileSysSnapSYNCInfo.getRemoteSyncStatus(sync.getTargetFileSystem(), sync.getHost().getIp(), sync.getHost().getPort(), sync.getHost().getUsername(), sync.getHost().getPassword());
        }
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
            return;
        }
        if (status.isOnLineOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetFileSysMustOffLine"), res.get("targetFileSysMustOffLine")));
            return;
        }
        RequestContext.getCurrentInstance().execute("resumesync.show()");

    }
}
