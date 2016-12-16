/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.nas.model.Sync;
import com.marstor.msa.nas.model.SyncStatus;
import com.marstor.msa.sync.bean.MarsHost;
import com.marstor.msa.sync.bean.SyncMapping;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.sync.web.impl.MsaSYNCInterfaceImpl;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
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
@ManagedBean(name = "synclist_vtl")
@ViewScoped
public class SyncList_vtlBean  implements Serializable{

    /**
     * Creates a new instance of SyncList_vtlBean
     */
//    private MsaSYNCInterfaceImpl syncInterface = (MsaSYNCInterfaceImpl)InterfaceFactory.getSYNCInterfaceInstance();
    private  MsaSYNCInterface syncInterface = InterfaceFactory.getSYNCInterfaceInstance();
//    private ArrayList<LocalSync> localList = new ArrayList<LocalSync>();
//    private ArrayList<RemoteSync> remoteList = new ArrayList<RemoteSync>();
    private ArrayList<Sync> syncList = new ArrayList<Sync>();
    private Sync selectSync = new Sync();
    private String path;
    private String selectType = "";
    private String module;
    private String returnURL;
    private MSAResource res = new MSAResource();

    public SyncList_vtlBean(String path,String module,String returnURL) {
//        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
//        path = request.getParameter("path");
        if(path==null) {
            return;
        }
        this.path = path;
        if (module != null) {
            this.module = module;
        }
        if (returnURL != null) {
            this.returnURL = returnURL;
        }
        List<SyncMapping> mapps = syncInterface.getSyncMapping(path);
        if (mapps == null) {
            return;
        }
//        Sync sync;
//        for (SyncMapping map : mapps) {
//            MarsHost host = new MarsHost(map.getStrIP(), map.getStrPort(), map.getStrUserName(), map.getStrPassword(), map.getStrSSHPort(), map.getStrRootPassword());
//            if (map.getiIsLocal() == 1) { //本地
//                sync = new Sync(map.getStrDescFileSystem(), map.getStrFirstSnapshot(), Sync.localType, res.get("local"), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host);
//            } else {
//                sync = new Sync(map.getStrDescFileSystem(), map.getStrFirstSnapshot(), Sync.remoteType, map.getStrIP(), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host);
//            }
//            this.syncList.add(sync);
//
//        }

    }
    public void init() {
        ArrayList<Sync> syncs = new ArrayList<Sync>();
         List<SyncMapping> mapps = syncInterface.getSyncMapping(path);
        if (mapps == null) {
            return;
        }
//        Sync sync;
//        for (SyncMapping map : mapps) {
////            if (map.getStrSrcFileSystem().equals(path)) {
//            MarsHost host = new MarsHost(map.getStrIP(), map.getStrPort(), map.getStrUserName(), map.getStrPassword(), map.getStrSSHPort(), map.getStrRootPassword());
//            if (map.getiIsLocal() == 1) { //本地
////                sync = new Sync(map.getStrDescFileSystem(), map.getStrFirstSnapshot(), Sync.localType, res.get("local"), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host);
//                // this.localList.add(local);
//            } else {
////                sync = new Sync(map.getStrDescFileSystem(), map.getStrFirstSnapshot(), Sync.remoteType, map.getStrIP(), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host);
//                //this.remoteList.add(remote);
//            }
//            syncs.add(sync);
//        }
//        this.syncList = syncs;
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
        SystemOutPrintln.print_vtl("select sync target filesys : " + this.selectSync.getTargetFileSystem());
        SystemOutPrintln.print_vtl("select sync type : " + this.selectSync.getType());
        if (this.selectSync.getType() == 1) {
            flag = syncInterface.startSync(path, this.selectSync.getiDescFileSystemHashCode(), true);
        } else {
            flag = syncInterface.startSync(path, this.selectSync.getiDescFileSystemHashCode(), false);
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("startCopyFailed"), res.get("startCopyFailed")));
            SystemOutPrintln.print_vtl("start sync failed: " + flag);
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("pauseCopyFailed"), res.get("pauseCopyFailed")));
            SystemOutPrintln.print_vtl("suspend sync failed: " + flag);
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delCopyFailed"),res.get("delCopyFailed")));
            SystemOutPrintln.print_vtl("delete sync failed: " + flag);
            return;
        }
        this.init();
    }

    public void stopTargetSync() {
        boolean flag = syncInterface.abortSync(path);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCopyFailed"), res.get("stopCopyFailed")));
            SystemOutPrintln.print_vtl("target stop sync failed: " + flag);
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
            SystemOutPrintln.print_vtl("source stop sync failed: " + flag);
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
            SystemOutPrintln.print_vtl("get local file system info failed: ");
            return;
        }
        if (this.selectSync.getType() == 1) {
            flag = syncInterface.resumeSync(path, this.selectSync.getiDescFileSystemHashCode(), true, status.isRollBackOrNot());
        } else {
            flag = syncInterface.resumeSync(path, this.selectSync.getiDescFileSystemHashCode(), false, status.isRollBackOrNot());
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("resumeSyncFailed"),res.get("resumeSyncFailed")));
            SystemOutPrintln.print_vtl("resume sync failed: " + flag);
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
        String param = "path=" + this.path+ "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "vtl_add_local_sync?faces-redirect=true&amp;" + param;
    }

    public String addRemoteSync() {
        String param = "path=" + this.path+ "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "vtl_add_remote_sync?faces-redirect=true&amp;" + param;
    }

    public void checkBeforeStartSync(Sync sync) {
        //  FileSysSnapSYNCInfo.getRemoteSyncStatus(this.selectedPath, this.selectHost.getIp(), this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword());

        SyncStatus status;
        SystemOutPrintln.print_vtl("sync target filesys : " + sync.getTargetFileSystem());
        SystemOutPrintln.print_vtl("sync type : " + sync.getType());
        if (sync.getType() == 1) {
            status = FileSysSnapSYNCInfo.getLocalSyncStatus(sync.getTargetFileSystem());
        } else {
            status = FileSysSnapSYNCInfo.getRemoteSyncStatus(sync.getTargetFileSystem(), sync.getHost().getIp(), sync.getHost().getPort(), sync.getHost().getUsername(), sync.getHost().getPassword());
        }
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
            return;
        }
        if (status.isOnLineOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetFileSysMustOffLine") , res.get("targetFileSysMustOffLine")));
            return;
        }
        if(status.isExistSnapOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetFileSysCanNotExistSnap"), res.get("targetFileSysCanNotExistSnap")));
            return;
        }
        RequestContext.getCurrentInstance().execute("startsync.show()");

    }

    public void checkBeforeResumeSync(Sync sync) {
        //  FileSysSnapSYNCInfo.getRemoteSyncStatus(this.selectedPath, this.selectHost.getIp(), this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword());
        SyncStatus status;
        if (sync.getType() == 1) {
            status = FileSysSnapSYNCInfo.getLocalSyncStatus(sync.getTargetFileSystem());
        } else {
            status = FileSysSnapSYNCInfo.getRemoteSyncStatus(sync.getTargetFileSystem(), sync.getHost().getIp(), sync.getHost().getPort(), sync.getHost().getUsername(), sync.getHost().getPassword());
        }
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("getFileSysInfoFailed") ,res.get("getFileSysInfoFailed")));
            return;
        }
        if (status.isOnLineOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetFileSysMustOffLine"), res.get("targetFileSysMustOffLine")));
            return;
        }
        RequestContext.getCurrentInstance().execute("resumesync.show()");

    }
}
