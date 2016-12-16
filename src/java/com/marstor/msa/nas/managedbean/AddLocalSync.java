/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.nas.model.Protect;
import com.marstor.msa.nas.bean.SharePath;
import com.marstor.msa.nas.model.SnapShot;
import com.marstor.msa.nas.model.SyncStatus;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.sync.bean.SyncMapping;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
@ManagedBean
@ViewScoped
public class AddLocalSync implements Serializable {

    private String selectedPath;
    private ArrayList<String> paths = new ArrayList<String>();
    private String sourcePath = "";
    private String pathName = "";
    private String module = "";
    private String returnURL;
    private String title = "";
    private ArrayList<String> startTimeList = new ArrayList<String>();
    private ArrayList<String> endtimeList = new ArrayList<String>();
    private String radioValue;
    private String startTime = "";
    private String endTime = "";
    private boolean timeRendered;
    private boolean isLocal;
    private SyncStatus status;
    private String outputLabel = "";
    private String outputLabel_1 = "";

    public AddLocalSync() {
        this.radioValue = "1";
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        if (request.getParameter("path") != null) {
            sourcePath = request.getParameter("path");
        } else {
            Debug.print("source path null.");
        }
        String module = request.getParameter("module");
        if (module != null) {
            this.module = module;
        } else {
            Debug.print("module null.");
        }
        MSAResource res = new MSAResource();
//        List<String> allSyncTarget = this.getAllSyncTarget(this.sourcePath);
//        if(allSyncTarget==null) {
//            Debug.print("##############!!!!!!!!!!!get local sync failed.");
//            return ;
//        }
        MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();
        
        if (this.module.equals("nas")) {
            title = res.get("nasTitle");
            outputLabel = res.get("targetPath");
            this.outputLabel_1 = res.get("share");
            this.pathName = "/" + this.sourcePath;
            ArrayList<SharePath> shares = InterfaceFactory.getNASInterfaceInstance().getAllSharePaths();
            if (shares != null) {
                for (SharePath share : shares) {
                    if (!share.getPath().equals(sourcePath) && !share.getPath().contains(Protect.copyFlag)) {
                        String status[] = sync.getLocalFileSystemStatus(share.getPath());
                        if(!status[4].equals("1")) {
                            paths.add("/" + share.getPath());
                        }
                    }
                }
            }
        } else if (this.module.equals("vtl")) {
            title = res.get("vtlTitle");
            outputLabel = res.get("targetSpace");
            this.outputLabel_1 = res.get("space");
            this.pathName = this.sourcePath;
            ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
            String[] volumeNames = zfs.getVolumeNames();
            if (volumeNames != null) {
                for (String volumeName : volumeNames) {
                    if (!(volumeName + "/TAPE").equals(sourcePath)) {
                         String status[] = sync.getLocalFileSystemStatus(volumeName + "/TAPE");
                        if(!status[4].equals("1")) {
                            paths.add(volumeName + "/TAPE");
                        }
                        
                    }
                }
            }
        }

        if (paths.size() == 0) {
            this.paths.add("");
        }
        if (paths.size() > 0) {
            this.selectedPath = paths.get(0);
        }
        String returnURL = request.getParameter("returnURL");

        if (returnURL != null) {
            this.returnURL = returnURL;
        }
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                startTimeList.add("0" + String.valueOf(i) + ":00");
                startTimeList.add("0" + String.valueOf(i) + ":30");
            } else {
                startTimeList.add(String.valueOf(i) + ":00");
                startTimeList.add(String.valueOf(i) + ":30");
            }
        }
        endtimeList.add("00:30");
        for (int i = 1; i < 25; i++) {

            if (i == 24) {
                endtimeList.add(String.valueOf(i) + ":00");
            } else if (i < 10) {
                endtimeList.add("0" + String.valueOf(i) + ":00");
                endtimeList.add("0" + String.valueOf(i) + ":30");
            } else {
                endtimeList.add(String.valueOf(i) + ":00");
                endtimeList.add(String.valueOf(i) + ":30");
            }
        }

        this.startTime = "00:00";
        this.endTime = "24:00";

    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getOutputLabel_1() {
        return outputLabel_1;
    }

    public void setOutputLabel_1(String outputLabel_1) {
        this.outputLabel_1 = outputLabel_1;
    }

    public String getOutputLabel() {
        return outputLabel;
    }

    public void setOutputLabel(String outputLabel) {
        this.outputLabel = outputLabel;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public ArrayList<String> getStartTimeList() {
        return startTimeList;
    }

    public void setStartTimeList(ArrayList<String> startTimeList) {
        this.startTimeList = startTimeList;
    }

    public ArrayList<String> getEndtimeList() {
        return endtimeList;
    }

    public void setEndtimeList(ArrayList<String> endtimeList) {
        this.endtimeList = endtimeList;
    }

    public String getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(String radioValue) {
        this.radioValue = radioValue;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isTimeRendered() {
        return timeRendered;
    }

    public void setTimeRendered(boolean timeRendered) {
        this.timeRendered = timeRendered;
    }

    public boolean isIsLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public String getSelectedPath() {
        return selectedPath;
    }

    public void setSelectedPath(String selectedPath) {
        this.selectedPath = selectedPath;
    }

    public ArrayList<String> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<String> paths) {
        this.paths = paths;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void listen() {
        if (this.radioValue.equals(("1"))) {
            this.timeRendered = false;
        } else {
            this.timeRendered = true;
        }
    }

    public String save() {
        MSAResource res = new MSAResource();
        if (this.sourcePath == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getSourceNameFailed"), res.get("getSourceNameFailed")));
            return null;
        }
//        FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.sourcePath);
//        if (fileSys == null) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getLocalFileSystemFailed"), res.get("getLocalFileSystemFailed")));
//            return null;
//        }
//        Debug.print("####################@@@@@@@@@@@@@@@ source filesys path: " + fileSys.getStrName());
//        Debug.print("####################@@@@@@@@@@@@@@@ source filesys isopen: " + fileSys.getiIsOpen());
//        if (fileSys.getiIsOpen() != 1) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("sourceNotOpenSnap"), res.get("sourceNotOpenSnap")));
//            return null;
//        }
//        status = FileSysSnapSYNCInfo.getLocalSyncStatus(this.sourcePath);
//        
//        if(!status.isOpenAutoSnapOrNot()) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "源端共享目录未开启自动快照。", "源端共享目录未开启自动快照。"));
//            return null;
//        }
        if (this.selectedPath == null) {
            if (this.module.equals("nas")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetPathNotNull"), res.get("targetPathNotNull")));
            } else if (this.module.equals("vtl")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetSpaceNotNull"), res.get("targetSpaceNotNull")));
            }
            return null;
        }
//        Boolean  syncExistOrNot = this.isSyncExist();
//        if (syncExistOrNot == null) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getLocalFileSystemFailed"), res.get("getLocalFileSystemFailed")));
//            return null;
//        }
//        if(syncExistOrNot) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetExist"), res.get("targetExist")));
//            return null;
//        }
        status = FileSysSnapSYNCInfo.getLocalSyncStatus(this.selectedPath);
//        if (status.isOnLineOrNot()) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetMustOffLine"), res.get("targetMustOffLine")));
//            return null;
//        }
//        if (status.isExistSnapOrNot()) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetHaveSnap"), res.get("targetHaveSnap")));
//            return null;
//        }
        if (status.isOnLineOrNot() || status.isExistSnapOrNot()) {
            RequestContext.getCurrentInstance().execute("addsync.show()");
            return null;
        }
        return this.addSync();


    }

    public String operateBeforeAddSync() {
        boolean flag;
        MSAResource res = new MSAResource();
        if (status.isOnLineOrNot()) {
            if (module.equals("nas")) {
                flag = InterfaceFactory.getNASInterfaceInstance().setSharePathStatus(this.selectedPath, 0);
                if (!flag) {
                    Debug.print("offline " + flag);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("offLineFailed"), res.get("offLineFailed")));
                    return null;
                }
            } else if (module.equals("vtl")) {
                VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
                flag = vtl.umountZFS(this.selectedPath);
                if (!flag) {
                    Debug.print("offline " + flag);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("offLineFailed"), res.get("offLineFailed")));
                    return null;
                }
            }
        }
        if (status.isExistSnapOrNot()) {
            //删除自动快照
            FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.selectedPath);
            if (fileSys.getiIsOpen() == 1) {//如果目标端开启了自动快照
                flag = InterfaceFactory.getSYNCInterfaceInstance().cancelTimingSnapshot(this.selectedPath, true);
                if (!flag) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
                    return null;
                }
            }
//            else {
//                List<Snapshot> autoSnap = InterfaceFactory.getSYNCInterfaceInstance().getGetAutoSnapshot(this.selectedPath);
//                if (autoSnap != null) {
//                    for (Snapshot snap : autoSnap) {
//                        Debug.print("###########**********!!!!!!!!!!! auto snap strname: " + snap.getStrName());
//                        flag = InterfaceFactory.getSYNCInterfaceInstance().destroySnapshot(snap.getStrName());
//                        if (!flag) {
//                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
//                            return null;
//                        }
//                    }
//                }
//            }
            flag = InterfaceFactory.getSYNCInterfaceInstance().deleteAllSnapshot(this.selectedPath, true);
            if (!flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
                return null;
            }
//            List<Snapshot> manualSnap = InterfaceFactory.getSYNCInterfaceInstance().getGetManualSnapshot(this.selectedPath);
//            if (manualSnap != null) {
//                for (Snapshot snap : manualSnap) {
//                    Debug.print("###########**********!!!!!!!!!!! manual snap strname: " + snap.getStrName());
//                    flag = InterfaceFactory.getSYNCInterfaceInstance().destroySnapshot(snap.getStrName());
//                    if (!flag) {
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
//                        return null;
//                    }
//                }
//            }

        }
        return this.addSync();
    }

    public String addSync() {
        MSAResource res = new MSAResource();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date startDate, endDate;
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        try {
            startDate = df.parse(this.startTime);
            endDate = df.parse(this.endTime);
            startCal.setTime(startDate);
            endCal.setTime(endDate);
        } catch (Exception ex) {
            Debug.print(ex.getMessage());
        }
        boolean flag;
        //boolean flag = InterfaceFactory.getSYNCInterfaceInstance().setLocalSyncInfo(this.sourcePath, selectedPath);
        boolean isFixedTime = this.radioValue.equals("1") ? false : true;
        Debug.print("add sync param: ");
        Debug.print("sourcePath: " + this.sourcePath);
        Debug.print("selected Path: " + this.selectedPath);
        Debug.print("startCal: " + startCal.getTime().toString());
        Debug.print("endCal: " + endCal.getTime().toString());
        Debug.print("isFixedTime: " + isFixedTime);
        if (this.selectedPath.startsWith("/") && this.selectedPath.length() > 1) {
            this.selectedPath = this.selectedPath.substring(1);
        }
        flag = InterfaceFactory.getSYNCInterfaceInstance().setLocalSyncInfo(this.sourcePath, selectedPath, startCal, endCal, isFixedTime);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setLocalCopyFailed"), res.get("setLocalCopyFailed")));
            return null;
        }
        String param = "path=" + this.sourcePath + "&" + "activeIndex=1" + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "nas_snap_sync?faces-redirect=true&amp;" + param;
    }

    public String cancel() {
        String param = "path=" + this.sourcePath + "&" + "activeIndex=1" + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "nas_snap_sync?faces-redirect=true&amp;" + param;
    }

    public Boolean isSyncExist() {
        List<SyncMapping> mapps = InterfaceFactory.getSYNCInterfaceInstance().getSyncMapping(this.sourcePath);
        if (mapps == null) {
            return null;
        }
         this.selectedPath = this.selectedPath.trim();
        if (this.selectedPath.startsWith("/")) {
            this.selectedPath = this.selectedPath.substring(this.selectedPath.indexOf("/") + 1);
        }
        Debug.print("select path: " + this.selectedPath);
        for (SyncMapping map : mapps) {
            if (map.getiIsLocal() == 1) {
                  Debug.print("map path: " + map.getStrDescFileSystem());
                if (this.selectedPath.equals(map.getStrDescFileSystem())) {
                    return true;
                }
            }
        }
        return false;
    }
    public List<String> getAllSyncTarget(String path) {
         List<SyncMapping> mapps = InterfaceFactory.getSYNCInterfaceInstance().getSyncMapping(path);
         if (mapps == null) {
            return null;
        }
        List<String> targetPaths = new ArrayList<String>();
        for(SyncMapping map : mapps) {
             if (map.getiIsLocal() == 1) {
                 targetPaths.add(map.getStrDescFileSystem());
             }
        }
        return targetPaths;
    }
}
