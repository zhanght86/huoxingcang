/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.model;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import com.marstor.msa.cdp.util.CDPConstants;

/**
 *
 * @author Administrator
 */
public class GlobalProtectStatus {

    private MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();
    private MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
    //快照界面按钮是否可操作
    private boolean saveRollDisabled, cancelRollDisabled, rollDisabled, addSnapDisabled, autoConfigDisabled, createCopyDisabled;
    private boolean saveRollRender, cancelRollRender, rollRender, loadingRender, rollingRender, createCopyRender;
    private boolean deleteSnapDisabled;
    private boolean isRollbacking;
    private boolean pollAutoStart;
    //复制界面按钮是否可操作
    private boolean startSyncDisable, suspendSyncDisable, stopSyncDisable, resumeSyncDisable, deleteSyncDisable;
    private boolean startSyncRender, suspendSyncRender;
    private boolean syncTargetOrNot;
    private boolean addLocalSyncDisable, addRemoteSyncDisable;
    private boolean containsCopy;
    private String path;
    private int level;
    private String guid;
    private String dg;
    private boolean isLocalDest;
    private CdpDiskGroupInfo group;

    public GlobalProtectStatus(String path, int level, String dg) {
        this.path = path;
        this.level = level;
        this.dg = dg;
        this.update();
        addLocalSyncDisable = true;
        addRemoteSyncDisable = true;
    }

    public GlobalProtectStatus(String path, int level, CdpDiskGroupInfo group, String dg, List<String> names) {
        this.path = path;
        this.level = level;
        this.group = group;
        this.dg = dg;
        //页面载入时判断一次，是否存在此文件系统的副本
        this.containsCopy = false;
        for (String name : names) {
            String dgName = dg;
            if (name.contains(CDPConstants.SNAPSHOT_COPY)) {
                String[] sname = name.split("_");
                if (sname[0].equals(dgName)) {
                    this.containsCopy = true;
                    break;
                }
            }
        }
//        if (cloneMap.containsKey(this.path + "_snapshotCopy")) {
//            this.containsCopy = true;
//        } else {
//            this.containsCopy = false;
//        }
        this.update();
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

    public final void update() {
//        List<CdpDiskGroupInfo> groups = cdp.getDiskGroupInfos();
//        CdpDiskGroupInfo group = null;
//        List<String> names = new ArrayList();
//        for (CdpDiskGroupInfo g : groups) {
//            names.add(g.getDiskGroupName());
//            if(g.getDiskGroupName().equals(dg)){
//                group = g;
//            }
//        }
//        this.containsCopy = false;
//        System.out.println("##########!!!!!! path: " + this.path);
//        CdpDiskGroupInfo g = cdp.getDiskGroupInfo(guid);
//        for (String name : names) {
//            System.out.println("###########@@@@@@@@@@@@  copy: " + name);
//            if (name.startsWith(dg) && name.contains(CDPConstants.SNAPSHOT_COPY)) {
//                this.containsCopy = true;
//                break;
//            }
//        }
        boolean rollbacking = false;
        boolean rollbacked = false;
        boolean isSource = false;
        boolean isDest = false;
        boolean offline;



        if (group == null) {
            createCopyDisabled = false;
            addSnapDisabled = true;
            deleteSnapDisabled = true;
            rollDisabled = true;
            return;
        }

        if (level == CDPConstants.PROTECT_SNAPSHOT) {
            if (group.getSyncStatusInfo() != null) {
                rollbacking = group.getSyncStatusInfo().isbIsRollbacking();
                rollbacked = group.getSyncStatusInfo().getIsRollback() == CDPConstants.TRUE;
            }
        } else {
            rollbacked = group.getRollbackInfo() != null;
            if (group.getRollbackInfo() != null) {
                rollbacking = group.getRollbackInfo().getTaskStatus() == CDPConstants.ROLLBACK_STATUS_RUNNING;
            }
        }
        if (group.getSyncStatusInfo() != null) {
            isSource = group.getSyncStatusInfo().getIsSrcSync() == CDPConstants.TRUE;
            System.out.println("99999999999999999999isSource="+isSource);
            isDest = group.getSyncStatusInfo().getIsDescSync() == CDPConstants.TRUE;
             System.out.println("99999999999999999999isDest="+isDest);
            this.isLocalDest = group.getSyncStatusInfo().getIsLocalDescSync() == CDPConstants.TRUE;
        }

        offline = group.getiMount() == CDPConstants.FALSE;

        if (rollbacking) { //回滚中
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

        if (rollbacked) { //如果发生过回滚
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
//??           this.addLocalSyncDisable = true;
//??            this.addRemoteSyncDisable = true;
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
// ??           this.addLocalSyncDisable = false;
// ??           this.addRemoteSyncDisable = false;
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
        if (isSource) {
            //限制快照界面按钮
            this.saveRollDisabled = true;
            this.cancelRollDisabled = true;
            this.rollDisabled = true;
            this.addSnapDisabled = false;
            this.autoConfigDisabled = true;
            this.deleteSnapDisabled = true;
            if (isDest) {
                 this.syncTargetOrNot = true;
            }
                  
            return;
        } else {
            this.saveRollDisabled = false;
            this.cancelRollDisabled = false;
            this.rollDisabled = false;
            this.addSnapDisabled = false;
            this.autoConfigDisabled = false;
            this.deleteSnapDisabled = false;
        }
        //如果文件系统是同步目标端
        if (isDest) {
            //限制快照界面按钮
            this.saveRollDisabled = true;
            this.cancelRollDisabled = true;
            this.rollDisabled = true;
            this.addSnapDisabled = true;
            this.autoConfigDisabled = true;
            this.deleteSnapDisabled = true;
            //限制复制界面按钮
// ??           this.addLocalSyncDisable = true;
// ??           this.addRemoteSyncDisable = true;
            this.syncTargetOrNot = true;
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
// ??           this.addLocalSyncDisable = false;
// ??           this.addRemoteSyncDisable = false;
            this.syncTargetOrNot = false;
        }
        //如果离线
        if (offline) {
            //限制快照界面按钮
            this.autoConfigDisabled = true;
            this.addSnapDisabled = true;
            //限制复制界面按钮
// ??           this.addLocalSyncDisable = true;
// ??           this.addRemoteSyncDisable = true;
        } else {
            //限制快照界面按钮
            this.autoConfigDisabled = false;
            this.addSnapDisabled = false;
            //限制复制界面按钮
//??            this.addLocalSyncDisable = false;
//??            this.addRemoteSyncDisable = false;
        }
        if (group.isCDPStarted() || group.getiAutoSnapshotIsOpen() == CDPConstants.TRUE) {
            this.rollDisabled = true;
            this.addLocalSyncDisable = false;
            this.addRemoteSyncDisable = false;
        } else {
            this.rollDisabled = false;
            this.addLocalSyncDisable = true;
            this.addRemoteSyncDisable = true;
        }
        if (this.containsCopy) {
            this.rollDisabled = true;
        } else {
            this.rollDisabled = false;
        }

        if (this.dg.contains(CDPConstants.SNAPSHOT_COPY)) {
            this.createCopyDisabled = true;
            this.deleteSnapDisabled = true;
        }

        if (this.dg.contains(CDPConstants.SNAPSHOT_COPY) && !rollbacking) {
            this.rollDisabled = false;
        }

    }

    public boolean isIsLocalDest() {
        return isLocalDest;
    }

    public void setIsLocalDest(boolean isLocalDest) {
        this.isLocalDest = isLocalDest;
    }
}
