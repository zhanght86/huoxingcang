/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.bean.CIFSParametersForSharePath;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.nas.bean.NFSParametersForSharePath;
import com.marstor.msa.nas.model.SnapShot;
import com.marstor.msa.nas.model.SyncStatus;
import com.marstor.msa.nas.web.NASInterface;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.sync.web.impl.MsaSYNCInterfaceImpl;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "snapshotlist_vtl")
@ViewScoped
public class SnapShotList_vtlBean implements Serializable  {

    /**
     * Creates a new instance of SnapShotList_vtlBean
     */
       private List<SnapShot> snaps = new ArrayList<SnapShot>();
    private String path;
    private String module = "";
    private String returnURL = "";
    private SnapShot selectSnap = new SnapShot();
    private MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();
    private NASInterface nas = InterfaceFactory.getNASInterfaceInstance();
    private VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
    private MSAResource res = new MSAResource();
    private CIFSParametersForSharePath cifsParam = new CIFSParametersForSharePath();
    private NFSParametersForSharePath nfsParam = new NFSParametersForSharePath();
//    private boolean beforerollback_isonling = false;
    public SnapShotList_vtlBean(String path, String module, String returnURL) {
        
        if (path == null) {
            return;
        }
        this.path = path;
        if (module != null) {
            this.module = module;
        }
        if (returnURL != null) {
            this.returnURL = returnURL;
        }
        SyncStatus status = FileSysSnapSYNCInfo.getLocalSyncStatus(path);
        if (status == null) {
            return;
        }
        String rollBacked = ""; //已回滚的快照
        if (status.isRollBackOrNot()) { //如果回滚过快照
            List<String> snaps = sync.getSnapshotListAfterRollback(path);
            SystemOutPrintln.print_vtl("snap List size : " + snaps.size());
            if (snaps == null) {
                return;
            }
            if (snaps.size() > 0) {
                rollBacked = snaps.get(snaps.size() - 1);
            }
            SystemOutPrintln.print_vtl("rolled snap :" + rollBacked);
            for (int i = 0; i < snaps.size(); i++) {
                String strSnap = snaps.get(i);
                SystemOutPrintln.print_vtl("snap str: " + strSnap);
                String array[] = strSnap.trim().split(";");
                String type = "";
                if (array != null && array.length > 5) {
                    if (array[1].contains("AUTO")) {
                        type = res.get("auto");
                    } else {
                        type = res.get("manual");
                    }
                    SnapShot snapShot = new SnapShot(array[1], array[3], type, array[0],array[2]);
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
                    this.snaps.add(snapShot);
                }
            }
        } else {
            List<Snapshot> autoSnap = sync.getGetAutoSnapshot(path);
            if (autoSnap != null) {
                for (Snapshot snap : autoSnap) {
//                    SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime());
//                    this.snaps.add(snapShot);
                }
            }
            List<Snapshot> manualSnap = sync.getGetManualSnapshot(path);
            if (manualSnap != null) {
                for (Snapshot snap : manualSnap) {
//                    SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime());
//
//                    this.snaps.add(snapShot);
                }
            }
            Collections.sort(this.snaps);
        }
        if (this.module.equals("nas")) {
            this.initCIFSAndNFSParam();
        }


    }


    public void initCIFSAndNFSParam() {
        //获取NFS和CIFS参数
        this.cifsParam = nas.getSpecifiedCIFSParameters(path);
        this.cifsParam.setSharepath(path);
        this.nfsParam = nas.getSpecifiedNFSParameters(path);
        this.nfsParam.setSharepath(path);
    }

//    public void initButton(SyncStatus status) {
//        if (status.isRollBackOrNot()) {
//            this.deleteSnapDisabled = true;
//        } else {
//            this.deleteSnapDisabled = false;
//        }
//        //如果文件系统是同步源端 
//        if (status.isSyncSourceOrNot()) {
//            this.saveRollDisabled = true;
//            this.cancelRollDisabled = true;
//            this.rollDisabled = true;
//            this.addSnapDisabled = false;
//            this.autoConfigDisabled = true;
//            this.deleteSnapDisabled = true;
//        }
//        //如果文件系统是同步目标端
//        if (status.isSyncTargetOrNot()) {
//            this.saveRollDisabled = true;
//            this.cancelRollDisabled = true;
//            this.rollDisabled = true;
//            this.addSnapDisabled = true;
//            this.autoConfigDisabled = true;
//            this.deleteSnapDisabled = true;
//        }
//        if (!status.isOnLineOrNot()) {
//            this.autoConfigDisabled = true;
//            this.addSnapDisabled = true;
//        }
//
//    }
    public void init() {
        List<SnapShot> snapList = new ArrayList<SnapShot>();
        SyncStatus status = FileSysSnapSYNCInfo.getLocalSyncStatus(path);
        if (status == null) {
            return;
        }
        String rollBacked = ""; //已回滚的快照
        if (status.isRollBackOrNot()) { //如果回滚过快照
            List<String> snaps = sync.getSnapshotListAfterRollback(path);
            SystemOutPrintln.print_vtl("snap List size : " + snaps.size());
            if (snaps == null) {
                return;
            }
            if (snaps.size() > 0) {
                rollBacked = snaps.get(snaps.size() - 1);
            }
            SystemOutPrintln.print_vtl("rolled snap :" + rollBacked);
            for (int i = 0; i < snaps.size(); i++) {
                String strSnap = snaps.get(i);
                SystemOutPrintln.print_vtl("snap str: " + strSnap);
                String array[] = strSnap.trim().split(";");
                String type = "";
                if (array != null && array.length > 5) {
                    if (array[1].contains("AUTO")) {
                        type = res.get("auto");
                    } else {
                        type = res.get("manual");
                    }
                    SnapShot snapShot = new SnapShot(array[1], array[3], type, array[0],array[2]);
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
                    snapList.add(snapShot);
                }
            }
        } else {
            List<Snapshot> autoSnap = sync.getGetAutoSnapshot(path);
            if (autoSnap != null) {
                for (Snapshot snap : autoSnap) {
//                    SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime());
//                    snapList.add(snapShot);
                }
            }
            List<Snapshot> manualSnap = sync.getGetManualSnapshot(path);
            if (manualSnap != null) {
                for (Snapshot snap : manualSnap) {
//                    SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime());
//
//                    snapList.add(snapShot);
                }
            }
            Collections.sort(snapList);
        }
        this.snaps = snapList;
    }
//回滚后刷新界面

    public void updateAfterRoll() {
        List<SnapShot> snapList = new ArrayList<SnapShot>();
        String rollBacked = ""; //已回滚的快照
        //如果回滚过快照
        List<String> snaps = sync.getSnapshotListAfterRollback(path);
        SystemOutPrintln.print_vtl("snap List size : " + snaps.size());
        for (String snap : snaps) {
            SystemOutPrintln.print_vtl("snap name : " + snap);
        }

        if (snaps != null) {
            if (snaps.size() > 0) {
                rollBacked = snaps.get(snaps.size() - 1);
            }
        }
        SystemOutPrintln.print_vtl("rolled snap :" + rollBacked);
        for (int i = 0; i < snaps.size(); i++) {
            String strSnap = snaps.get(i);
            SystemOutPrintln.print_vtl("snap str: " + strSnap);
            String array[] = strSnap.trim().split(";");
            String type = "";
            if (array != null && array.length > 5) {
                SystemOutPrintln.print_vtl("create time array[0]: " + array[0]);
                SystemOutPrintln.print_vtl("strname array[1]: " + array[1]);
                SystemOutPrintln.print_vtl("struserd array[3]: " + array[3]);
                if (array[1].contains("AUTO")) {
                    type = res.get("auto");
                } else {
                    type = res.get("manual");
                }
                SnapShot snapShot = new SnapShot(array[1], array[3], type, array[0],array[2]);
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
                snapList.add(snapShot);
            }
        }
        this.snaps = snapList;
    }
    //保存或取消回滚后刷新界面

    public void updateAfterSaveOrCancelRoll() {
        List<SnapShot> snapList = new ArrayList<SnapShot>();
        List<Snapshot> autoSnap = sync.getGetAutoSnapshot(path);
        if (autoSnap != null) {
            for (Snapshot snap : autoSnap) {
//                SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime());
//
//                snapList.add(snapShot);
            }
        }
        List<Snapshot> manualSnap = sync.getGetManualSnapshot(path);
        if (manualSnap != null) {
            for (Snapshot snap : manualSnap) {
//                SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime());
//
//                snapList.add(snapShot);
            }
        }
        this.snaps = snapList;
//        this.addSnapDisabled = false;
//        this.autoConfigDisabled = false;
//        this.deleteSnapDisabled = false;
    }
//删除快照后刷新界面

    public void updateAfterDelete() {
        List<SnapShot> snapList = new ArrayList<SnapShot>();
        List<Snapshot> autoSnap = sync.getGetAutoSnapshot(path);
        if (autoSnap != null) {
            for (Snapshot snap : autoSnap) {
//                SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime());
//                    if (snap.getStrName().equals(rollBacked)) {
//                        snapShot.setSaveRollRender(true);
//                        snapShot.setCancelRollRender(true);
//                    } else {
//                        snapShot.setSaveRollRender(false);
//                        snapShot.setCancelRollRender(false);
//                    }
//                snapList.add(snapShot);
            }
        }
        List<Snapshot> manualSnap = sync.getGetManualSnapshot(path);
        if (manualSnap != null) {
            for (Snapshot snap : manualSnap) {
//                SnapShot snapShot = new SnapShot(snap.getStrName(), snap.getStrUsed(), snap.getStrRefer(), snap.getiExpirationdate(), snap.getSyncSizeSpeed(), snap.getSyncStartTime(), snap.getSyncCompleteTime());
//                    if (snap.getStrName().equals(rollBacked)) {
//                        snapShot.setSaveRollRender(true);
//                        snapShot.setCancelRollRender(true);
//                    } else {
//                        snapShot.setSaveRollRender(false);
//                        snapShot.setCancelRollRender(false);
//                    }
//                snapList.add(snapShot);
            }
        }
        this.snaps = snapList;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SnapShot getSelectSnap() {
        return selectSnap;
    }

    public void setSelectSnap(SnapShot selectSnap) {
        this.selectSnap = selectSnap;
    }

    public List<SnapShot> getSnaps() {
        return snaps;
    }

    public void setSnaps(List<SnapShot> snaps) {
        this.snaps = snaps;
    }

    public String doBeforeAddSnap() {
        String param = "path=" + this.path + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "vtl_create_snap?faces-redirect=true&amp;" + param;
    }

    public String doBeforeConfigAutoSnap() {
        String param = "path=" + this.path + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "vtl_auto_snap_config?faces-redirect=true&amp;" + param;
    }

    public void deleteSnap() {
        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().destroySnapshot(this.selectSnap.getStrName());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
            return;
        }
        this.updateAfterDelete();
    }

    public void rollBackSnap() {
        //FileSysSnapSYNCInfo info = new FileSysSnapSYNCInfo(path);
        SyncStatus status = FileSysSnapSYNCInfo.getLocalSyncStatus(this.path);
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("getFileSysFailed"), res.get("getFileSysFailed")));
            return;
        }
//        beforerollback_isonling = status.isOnLineOrNot();
        if (status.isOnLineOrNot()) {
             FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.path);
            boolean flag;
            if (fileSys == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysFailed"), res.get("getFileSysFailed")));
                return;
            }
            if (fileSys.getiIsOpen() == 1) {//如果自动快照已经开启
                flag = InterfaceFactory.getSYNCInterfaceInstance().cancelTimingSnapshot(this.path, false);
                if (!flag) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("closeAutoSnapFailed"), res.get("closeAutoSnapFailed")));
                    return;
                }
            }

            if (this.module.equals("nas")) {
                flag = InterfaceFactory.getNASInterfaceInstance().turnoffCIFSForSharePath(path);
                if (!flag) {
                    SystemOutPrintln.print_vtl("close cifs " + flag);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("closeCIFSFailed"),  res.get("closeCIFSFailed")));
                    return;
                }
                flag = InterfaceFactory.getNASInterfaceInstance().turnOffNFSForSharePath(path);
                if (!flag) {
                    SystemOutPrintln.print_vtl("close nfs " + flag);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("closeNFSFailed"), res.get("closeNFSFailed")));
                    return;
                }
            }


//            flag = InterfaceFactory.getNASInterfaceInstance().setSharePathStatus(this.path, 0);
            SystemOutPrintln.print_vtl("umountZFS this.path=" + this.path);
            flag = vtl.umountZFS(this.path);
            if (!flag) {
                SystemOutPrintln.print_vtl("offline" + flag);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("offLineFailed"), res.get("offLineFailed")));
                return;
            }
        }
        if (status.isOpenAutoSnapOrNot()) {
            boolean flag = InterfaceFactory.getSYNCInterfaceInstance().cancelTimingSnapshot(this.path, false);
            if (!flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("closeAutoSnapFailed"), res.get("closeAutoSnapFailed")));
                return;
            }
        }
        List<String> rollList = this.getBeforeRollSnaps();
        String lastSnap = "";
        if (rollList != null && rollList.size() > 0) {
            lastSnap = rollList.get(0);
        }

        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().rollbackSnapshot(this.path, lastSnap, this.selectSnap.getStrName(), this.getBeforeRollSnaps());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("rollBackFailed"), res.get("rollBackFailed")));
            return;
        }
        updateAfterRoll();
        if (sync.isRollbacking(path)) {
//            this.loadingRender = false;
//            this.rollingRender = true;
            RequestContext.getCurrentInstance().execute("rollbackpoll.start();");
        } else {
             if (status.isOnLineOrNot()) {
                this.doAfterRollBack();
            }
        }
    }

    //回滚操作完成需要做的，上线磁带区，开启共享功能
    public void doAfterRollBack() {
        boolean flag;
//        if (beforerollback_isonling) {
            SystemOutPrintln.print_vtl("doAfterRollBack mountZFS this.path=" + this.path);
            flag = vtl.mountZFS(this.path);

            if (!flag) {
                SystemOutPrintln.print_vtl("online" + flag);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("onLineFailed"), res.get("onLineFailed")));
                return;
            } else {
                SystemOutPrintln.print_vtl("doAfterRollBack online ok");
            }
//        }
        
//        flag = InterfaceFactory.getNASInterfaceInstance().setSharePathStatus(this.path, 1);
      
        if (this.module.equals("nas")) {
            SystemOutPrintln.print_vtl("CIFS param getSharename: " + this.cifsParam.getSharename());
            SystemOutPrintln.print_vtl("CIFS param isStart : " + this.cifsParam.isStart());
            if (this.cifsParam.isStart()) {
                flag = InterfaceFactory.getNASInterfaceInstance().configCIFSForSharePath(this.cifsParam);
                if (!flag) {
                    SystemOutPrintln.print_vtl("config cifs:  " + flag);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("configCIFSFailed"),  res.get("configCIFSFailed")));
                    return;
                }
            }
            if (this.nfsParam.isStart()) {
                flag = InterfaceFactory.getNASInterfaceInstance().configNFSForSharePath(nfsParam);
                if (!flag) {
                    SystemOutPrintln.print_vtl("config nfs:  " + flag);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("configNFSFailed"), res.get("configNFSFailed")));
                    return;
                }
            }
        }



    }
//获取回滚前列表

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
            if (name == null || name.equals("")) {
                str = str + "-2" + ";";
            } else {
                str = str + name + ";";
            }
            str = str + rollSnaps.get(k).getStrUsed() + ";";
            str = str + rollSnaps.get(k).getStrRefer() + ";";
            String fsExpirationdate = String.valueOf(rollSnaps.get(k).getiExpirationdate());
            if (fsExpirationdate != null) {
                if (fsExpirationdate.equalsIgnoreCase("-1")) {
                    fsExpirationdate = res.get("effectForever") ;
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

    public String[] getLocalFileSystemStatus(String fileSystem) {
        String[] status = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(fileSystem);
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"),  res.get("getFileSysInfoFailed")));
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

    public List<String> getAfterRollSnaps() {
        List<String> snaps = InterfaceFactory.getSYNCInterfaceInstance().getSnapshotListAfterRollback(path);
        if (snaps == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getRolledSnapFailed"),  res.get("getRolledSnapFailed")));
            return null;
        }
        return snaps;
    }

    public void saveRoll() {
        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().saveCurrentStatus(path);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("saveRollFailed"), res.get("saveRollFailed")));
            SystemOutPrintln.print_vtl("save roll failed : " + flag);
            return;
        }
        this.updateAfterSaveOrCancelRoll();
    }

    public void cancelRoll() {
        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().cancelRollback(path);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("cancelRollFailed"),res.get("cancelRollFailed")));
            SystemOutPrintln.print_vtl("cancel roll failed : " + flag);
            return;
        }
        this.updateAfterSaveOrCancelRoll();
    }
//    public void checkRollbacking() {
//        this.isRollbacking = sync.isRollbacking(path);
//        if (!this.isRollbacking) {
//            this.rollingRender = false;
//            this.loadingRender = true;
//            RequestContext.getCurrentInstance().execute("rollbackpoll.stop();");
//            // RequestContext.getCurrentInstance().execute("afterRoll.show();");
//            this.doAfterRollBack();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "回滚操作成功。", "回滚操作成功。"));
//        } else {
//            this.rollingRender = true;
//            this.loadingRender = false;
//        }
//    }
}
