/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.javadb.SyncDBUtil;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.bean.SyncStatusInfo;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.managedbean.SnapShotList_vtlBean;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
public class GlobalProtectStatus implements Serializable{

    private MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();
    //快照界面按钮是否可操作
    private boolean saveRollDisabled, cancelRollDisabled, rollDisabled, addSnapDisabled, autoConfigDisabled, createCopyDisabled;
    private boolean saveRollRender, cancelRollRender, rollRender, loadingRender, rollingRender, createCopyRender; //loadingRender用于控制界面是否显示ajaxStatus组件
    private boolean deleteSnapDisabled;
    private boolean isRollbacking;
    private boolean pollAutoStart;
    //复制界面按钮是否可操作
    private boolean startSyncDisable, suspendSyncDisable, stopSyncDisable, resumeSyncDisable, deleteSyncDisable;
    private boolean startSyncRender, suspendSyncRender;
    private boolean syncTargetOrNot,syncLocalTargetOrNot;
    private boolean addLocalSyncDisable, addRemoteSyncDisable;
    private boolean autoSnapOpend;
    private boolean containsCopy;
    private String path;
    private SnapShotListBean snapList;
    private SnapShotList_vtlBean snap_vtlList;

    public GlobalProtectStatus(String path, SnapShotListBean snapList) {
        this.path = path;
        this.snapList = snapList;
        //页面载入时判断一次，如果自动快照开启了，则限制添加本地和源端同步按钮的状态
        FileSystemInfo fileSys = sync.getFileSystemInfo(path);
        if (fileSys == null) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
            return;
        }
        if (fileSys.getiIsOpen() != 1) {
//            this.addLocalSyncDisable = true;
//            this.addRemoteSyncDisable = true;
            this.autoSnapOpend = false;
        } else {
//            this.addLocalSyncDisable = false;
//            this.addRemoteSyncDisable = false;
            this.autoSnapOpend = true;
        }
        //页面载入时判断一次，是否存在此文件系统的副本
        Map<String, String> cloneMap = InterfaceFactory.getSYNCInterfaceInstance().getCloneOriginMap();
        Iterator<String> copys = cloneMap.keySet().iterator();
        this.containsCopy = false;
        Debug.print("path: " + this.path);
        while (copys.hasNext()) {
            String copy = copys.next();
            Debug.print("copy: " + copy);
            //copy.startsWith(this.path + "_snapshotCopy")
            if (copy.contains(this.path+"_")) {
                this.containsCopy = true;
                break;
            }
        }
        this.update();
    }

    public GlobalProtectStatus(String path, SnapShotList_vtlBean snapList) {
        this.path = path;
        this.snap_vtlList = snapList;
        //页面载入时判断一次，如果自动快照开启了，则限制添加本地和源端同步按钮的状态
        FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.path);
        if (fileSys == null) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
            return;
        }
        if (fileSys.getiIsOpen() != 1) {
//            this.addLocalSyncDisable = true;
//            this.addRemoteSyncDisable = true;
            this.autoSnapOpend = false;
        } else {
//            this.addLocalSyncDisable = false;
//            this.addRemoteSyncDisable = false;
            this.autoSnapOpend = true;
        }
        //页面载入时判断一次，是否存在此文件系统的副本
        Map<String, String> cloneMap = InterfaceFactory.getSYNCInterfaceInstance().getCloneOriginMap();
        Iterator<String> copys = cloneMap.keySet().iterator();
        this.containsCopy = false;
        Debug.print("path: " + this.path);
        while (copys.hasNext()) {
            String copy = copys.next();
            Debug.print("copy: " + copy);
            if (copy.startsWith(this.path + "_snapshotCopy")) {
                this.containsCopy = true;
                break;
            }
        }
        this.update();
    }

    public SnapShotListBean getSnapList() {
        return snapList;
    }

    public void setSnapList(SnapShotListBean snapList) {
        this.snapList = snapList;
    }

    public boolean isContainsCopy() {
        return containsCopy;
    }

    public void setContainsCopy(boolean containsCopy) {
        this.containsCopy = containsCopy;
    }

    public boolean isCreateCopyDisabled() {
        return createCopyDisabled;
    }

    public void setCreateCopyDisabled(boolean createCopyDisabled) {
        this.createCopyDisabled = createCopyDisabled;
    }

    public boolean isCreateCopyRender() {
        return createCopyRender;
    }

    public void setCreateCopyRender(boolean createCopyRender) {
        this.createCopyRender = createCopyRender;
    }

    public boolean isSaveRollDisabled() {
        return saveRollDisabled;
    }

    public void setSaveRollDisabled(boolean saveRollDisabled) {
        this.saveRollDisabled = saveRollDisabled;
    }

    public boolean isCancelRollDisabled() {
        return cancelRollDisabled;
    }

    public void setCancelRollDisabled(boolean cancelRollDisabled) {
        this.cancelRollDisabled = cancelRollDisabled;
    }

    public boolean isRollDisabled() {
        return rollDisabled;
    }

    public void setRollDisabled(boolean rollDisabled) {
        this.rollDisabled = rollDisabled;
    }

    public boolean isAddSnapDisabled() {
        return addSnapDisabled;
    }

    public void setAddSnapDisabled(boolean addSnapDisabled) {
        this.addSnapDisabled = addSnapDisabled;
    }

    public boolean isAutoConfigDisabled() {
        return autoConfigDisabled;
    }

    public void setAutoConfigDisabled(boolean autoConfigDisabled) {
        this.autoConfigDisabled = autoConfigDisabled;
    }

    public boolean isSaveRollRender() {
        return saveRollRender;
    }

    public void setSaveRollRender(boolean saveRollRender) {
        this.saveRollRender = saveRollRender;
    }

    public boolean isCancelRollRender() {
        return cancelRollRender;
    }

    public void setCancelRollRender(boolean cancelRollRender) {
        this.cancelRollRender = cancelRollRender;
    }

    public boolean isRollRender() {
        return rollRender;
    }

    public void setRollRender(boolean rollRender) {
        this.rollRender = rollRender;
    }

    public boolean isLoadingRender() {
        return loadingRender;
    }

    public void setLoadingRender(boolean loadingRender) {
        this.loadingRender = loadingRender;
    }

    public boolean isRollingRender() {
        return rollingRender;
    }

    public void setRollingRender(boolean rollingRender) {
        this.rollingRender = rollingRender;
    }

    public MsaSYNCInterface getSync() {
        return sync;
    }

    public void setSync(MsaSYNCInterface sync) {
        this.sync = sync;
    }

    public boolean isDeleteSnapDisabled() {
        return deleteSnapDisabled;
    }

    public void setDeleteSnapDisabled(boolean deleteSnapDisabled) {
        this.deleteSnapDisabled = deleteSnapDisabled;
    }

    public boolean isIsRollbacking() {
        return isRollbacking;
    }

    public void setIsRollbacking(boolean isRollbacking) {
        this.isRollbacking = isRollbacking;
    }

    public boolean isPollAutoStart() {
        return pollAutoStart;
    }

    public void setPollAutoStart(boolean pollAutoStart) {
        this.pollAutoStart = pollAutoStart;
    }

    public boolean isStartSyncDisable() {
        return startSyncDisable;
    }

    public void setStartSyncDisable(boolean startSyncDisable) {
        this.startSyncDisable = startSyncDisable;
    }

    public boolean isSuspendSyncDisable() {
        return suspendSyncDisable;
    }

    public void setSuspendSyncDisable(boolean suspendSyncDisable) {
        this.suspendSyncDisable = suspendSyncDisable;
    }

    public boolean isStopSyncDisable() {
        return stopSyncDisable;
    }

    public void setStopSyncDisable(boolean stopSyncDisable) {
        this.stopSyncDisable = stopSyncDisable;
    }

    public boolean isResumeSyncDisable() {
        return resumeSyncDisable;
    }

    public void setResumeSyncDisable(boolean resumeSyncDisable) {
        this.resumeSyncDisable = resumeSyncDisable;
    }

    public boolean isDeleteSyncDisable() {
        return deleteSyncDisable;
    }

    public void setDeleteSyncDisable(boolean deleteSyncDisable) {
        this.deleteSyncDisable = deleteSyncDisable;
    }

    public boolean isStartSyncRender() {
        return startSyncRender;
    }

    public void setStartSyncRender(boolean startSyncRender) {
        this.startSyncRender = startSyncRender;
    }

    public boolean isSuspendSyncRender() {
        return suspendSyncRender;
    }

    public void setSuspendSyncRender(boolean suspendSyncRender) {
        this.suspendSyncRender = suspendSyncRender;
    }

    public boolean isSyncTargetOrNot() {
        return syncTargetOrNot;
    }

    public void setSyncTargetOrNot(boolean syncTargetOrNot) {
        this.syncTargetOrNot = syncTargetOrNot;
    }

    public boolean isAddLocalSyncDisable() {
        return addLocalSyncDisable;
    }

    public void setAddLocalSyncDisable(boolean addLocalSyncDisable) {
        this.addLocalSyncDisable = addLocalSyncDisable;
    }

    public boolean isAddRemoteSyncDisable() {
        return addRemoteSyncDisable;
    }

    public void setAddRemoteSyncDisable(boolean addRemoteSyncDisable) {
        this.addRemoteSyncDisable = addRemoteSyncDisable;
    }

    public boolean isSyncLocalTargetOrNot() {
        return syncLocalTargetOrNot;
    }

    public void setSyncLocalTargetOrNot(boolean syncLocalTargetOrNot) {
        this.syncLocalTargetOrNot = syncLocalTargetOrNot;
    }

    public void update() {
        SyncStatus status = FileSysSnapSYNCInfo.getLocalSyncStatus(this.path);
        if (status == null) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
            return;
        }
        FileSystemInfo fileSys = sync.getFileSystemInfo(path);
        SyncStatusInfo statusInfo = fileSys.getSyncStatusInfo();
        
        if (sync.isRollbacking(path)) { //回滚中
            this.rollingRender = true;
            this.pollAutoStart = true;
            this.loadingRender = false;
            this.isRollbacking = true;
            //所有按钮都不可操作
            this.createCopyDisabled = true;
            saveRollDisabled = true;
            cancelRollDisabled = true;
            rollDisabled = true;
            addSnapDisabled = true;
            autoConfigDisabled = true;
            this.deleteSnapDisabled = true;
            startSyncDisable = true;
            suspendSyncDisable = true;
            stopSyncDisable = true;
            resumeSyncDisable = true;
            deleteSyncDisable = true;
            addLocalSyncDisable = true;
            addRemoteSyncDisable = true;
            return;
        } else {
            this.isRollbacking = false;
            this.rollingRender = false;
            this.pollAutoStart = false;
            this.loadingRender = true;
            //所有按钮都可操作
            this.createCopyDisabled = false;
            saveRollDisabled = false;
            cancelRollDisabled = false;
            rollDisabled = false;
            addSnapDisabled = false;
            autoConfigDisabled = false;
            this.deleteSnapDisabled = false;
            startSyncDisable = false;
            suspendSyncDisable = false;
            stopSyncDisable = false;
            resumeSyncDisable = false;
            deleteSyncDisable = false;
            addLocalSyncDisable = false;
            addRemoteSyncDisable = false;
        }
        if (status.isRollBackOrNot()) { //如果发生过回滚
            //限制快照界面按钮
            this.addSnapDisabled = true;
            this.autoConfigDisabled = true;
            this.deleteSnapDisabled = true;
            this.createCopyDisabled = true;
            //this.createCopyRender = false;
            //限制复制界面
            this.startSyncDisable = true;
            this.suspendSyncDisable = true;
            this.stopSyncDisable = true;
            this.resumeSyncDisable = true;
            this.deleteSyncDisable = true;
            this.addLocalSyncDisable = true;
            this.addRemoteSyncDisable = true;
            return;
        } else {
            //限制快照界面按钮
            this.addSnapDisabled = false;
            this.autoConfigDisabled = false;
            this.deleteSnapDisabled = false;
            this.createCopyDisabled = false;
            // this.createCopyRender = true;
            //限制复制界面
            this.startSyncDisable = false;
            this.suspendSyncDisable = false;
            this.stopSyncDisable = false;
            this.resumeSyncDisable = false;
            this.deleteSyncDisable = false;
            this.addLocalSyncDisable = false;
            this.addRemoteSyncDisable = false;
        }
        //如果自动快照开启了，则限制添加本地和源端同步按钮的状态
//        FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.path);
//        if (fileSys == null) {
//            MSAResource res = new MSAResource();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
//            return;
//        }
//        if (fileSys.getiIsOpen() != 1) {
//            this.addLocalSyncDisable = true;
//            this.addRemoteSyncDisable = true;
//        }else {
//            this.addLocalSyncDisable = false;
//            this.addRemoteSyncDisable = false;
//        }
        //如果文件系统是同步源端 
        if (status.isSyncSourceOrNot()) {
            //限制快照界面按钮
            this.saveRollDisabled = true;
            this.cancelRollDisabled = true;
            this.rollDisabled = true;
            this.addSnapDisabled = false;
            this.autoConfigDisabled = true;
            this.deleteSnapDisabled = true;
            return;
        } else {
            this.saveRollDisabled = false;
            this.cancelRollDisabled = false;
            this.rollDisabled = false;
            this.addSnapDisabled = false;
            this.autoConfigDisabled = false;
            this.deleteSnapDisabled = false;
        }
         //如果文件系统是本地同步目标端
        if(statusInfo.getIsLocalDescSync()==1) {
            this.syncLocalTargetOrNot = true;
        }else {
            this.syncLocalTargetOrNot = false;
        }
        //如果文件系统是同步目标端
        if (status.isSyncTargetOrNot()) {
            //限制快照界面按钮
            this.saveRollDisabled = true;
            this.cancelRollDisabled = true;
            this.rollDisabled = true;
            this.addSnapDisabled = true;
            this.autoConfigDisabled = true;
            this.deleteSnapDisabled = true;
            //限制复制界面按钮
            this.addLocalSyncDisable = true;
            this.addRemoteSyncDisable = true;
            this.syncTargetOrNot = true;
            if(status.isSuspendSyncOrNot()) {
                this.createCopyDisabled = false;
            }
            return;
        } else {
            //限制快照界面按钮
            this.saveRollDisabled = false;
            this.cancelRollDisabled = false;
            this.rollDisabled = false;
            this.addSnapDisabled = false;
            this.autoConfigDisabled = false;
            this.deleteSnapDisabled = false;
            //限制复制界面按钮
            this.addLocalSyncDisable = false;
            this.addRemoteSyncDisable = false;
            this.syncTargetOrNot = false;
        }
       
        //如果离线
        if (!status.isOnLineOrNot()) {
            //限制快照界面按钮
            this.autoConfigDisabled = true;
            this.addSnapDisabled = true;
            //限制复制界面按钮
            this.addLocalSyncDisable = true;
            this.addRemoteSyncDisable = true;
        } else {
            //限制快照界面按钮
            this.autoConfigDisabled = false;
            this.addSnapDisabled = false;
            //限制复制界面按钮
            this.addLocalSyncDisable = false;
            this.addRemoteSyncDisable = false;
        }
        if (this.autoSnapOpend) {
            this.addLocalSyncDisable = false;
            this.addRemoteSyncDisable = false;
        } else {
            this.addLocalSyncDisable = true;
            this.addRemoteSyncDisable = true;
        }
        if (this.containsCopy) {
            this.rollDisabled = true;
        } else {
            this.rollDisabled = false;
        }
        
//        if (snapList.getSnaps().size() == 0) {
//            this.deleteSnapDisabled = true;
//        } else {
//            this.deleteSnapDisabled = false;
//        }
        
        SyncDBUtil dbu = new SyncDBUtil();
        int count = dbu.querySnapshotCount(path);
        if (count == 0) {
            this.deleteSnapDisabled = true;
        } else {
            this.deleteSnapDisabled = false;
        }

    }
    public void updateDeleteAllSnapButton(SnapShotListBean snapShotListBean) {
        this.snapList = snapShotListBean;
          SyncDBUtil dbu = new SyncDBUtil();
         int count = dbu.querySnapshotCount(path);
         if (count == 0) {
            this.deleteSnapDisabled = true;
        } else {
            this.deleteSnapDisabled = false;
        }
//        if (snapShotListBean.getSnaps().size() == 0) {
//            this.deleteSnapDisabled = true;
//        } else {
//            this.deleteSnapDisabled = false;
//        }
    }
}
