/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.nas.bean.SharePath;
import com.marstor.msa.nas.model.SnapShot;
import com.marstor.msa.nas.model.SyncStatus;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.sync.util.Debug;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class AddLocalBean implements Serializable {

    private String selectedPath;
    private ArrayList<String> paths = new ArrayList<String>();
    private String sourcePath;
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
    private String guid;
    private String dg;
    private List<CdpDiskGroupInfo> shares;
    private Map<String, Boolean> CS = new HashMap();
    private List<String> groupNames;

    public AddLocalBean() {
        this.radioValue = "1";
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        guid = request.getParameter("guid");
        dg = request.getParameter("dg");
        sourcePath = request.getParameter("path");
        this.groupNames = Arrays.asList(request.getParameterValues("groupName"));
        MSAResource res = new MSAResource();

        title = res.get("nasTitle");
        outputLabel = res.get("share");
        shares = InterfaceFactory.getCDPInterfaceInstance().getDiskGroupInfos();
        if (shares != null) {
            for (CdpDiskGroupInfo share : shares) {
                if (share.getDiskGroupName().contains(CDPConstants.SNAPSHOT_COPY)) {
                    continue;
                }
                if (!share.getDiskGroupName().equals(dg)) {
//                    if (share.getSyncStatusInfo() != null) {
//                        if (share.getSyncStatusInfo().getIsDescSync() != CDPConstants.TRUE) {
//                            paths.add(share.getDiskZfsName());
//                        }
//                    }else{
                    paths.add(share.getDiskZfsName().trim());
                    CS.put(share.getDiskZfsName(), share.isCDPStarted() || (share.getiAutoSnapshotIsOpen() == CDPConstants.TRUE));
//                    }
                }
            }
        }

        if (paths.isEmpty()) {
            this.paths.add("");
        }
        if (paths.size() > 0) {
            this.selectedPath = paths.get(0);
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

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
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
        if (this.selectedPath == null || this.selectedPath.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("choosePlease"), res.get("choosePlease")));
            return null;
        }
        
        status = FileSysSnapSYNCInfo.getLocalSyncStatus(this.selectedPath);        

        if (status.isSyncTargetOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetCopied"), res.get("targetCopied")));
            return null;
        }

        if (CS.get(this.selectedPath)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCDP"), res.get("stopCDP")));
            return null;
        }

        if (status.isOnLineOrNot() || status.isExistSnapOrNot()) {
            RequestContext.getCurrentInstance().execute("addsync.show()");
            return null;
        }



        return this.addSync();
    }

    public String operateBeforeAddSync() {
        RequestContext.getCurrentInstance().execute("PF('block').show()");
        boolean flag = false;
        MSAResource res = new MSAResource();
        if (status.isOnLineOrNot()) {
            for (CdpDiskGroupInfo group : shares) {
                if (group.getDiskZfsName().equals(this.selectedPath)) {
                    flag = InterfaceFactory.getCDPInterfaceInstance().offline(group.getDiskGroupGuid(), group.getDiskGroupPath());
                    break;
                }
            }

            if (!flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("offLineFailed"), res.get("offLineFailed")));
                return null;

            }
        }
        if (status.isExistSnapOrNot()) {            
            flag = InterfaceFactory.getSYNCInterfaceInstance().deleteAllSnapshot(this.selectedPath, true);
            if (!flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
                return null;
            }
        }
        return this.addSync();
    }

    public String addSync() {
        MSAResource res = new MSAResource();
        if (this.selectedPath == null || this.selectedPath.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("choosePlease"), res.get("choosePlease")));
            return null;
        }

        status = FileSysSnapSYNCInfo.getLocalSyncStatus(this.selectedPath);
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getTargetFileSystemFailed"), res.get("getTargetFileSystemFailed")));
            return null;
        }

        if (status.isSyncTargetOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetCopied"), res.get("targetCopied")));
            return null;
        }
        RequestContext.getCurrentInstance().execute("PF('block').show()");
        for (CdpDiskGroupInfo share : shares) {
            if (share.getDiskZfsName().equals(this.selectedPath)) {
                if (share.getProtectType() == CDPConstants.PROTECT_SNAPSHOT) {
                    if (share.getiAutoSnapshotIsOpen() == CDPConstants.TRUE) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCDP"), res.get("stopCDP")));
                        return null;
                    }
                }
                if (share.getProtectType() == CDPConstants.PROTECT_RECORD) {
                    if (share.isCDPStarted()) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCDP"), res.get("stopCDP")));
                        return null;
                    }
                }
            }
        }


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
            System.out.println(ex.getMessage());
        }

        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        CdpDiskGroupInfo group = cdp.getDiskGroupInfo(guid);
        Debug.print(group + "");
        boolean flag = true;
        if (group != null) {            
            flag = cdp.setProtectType(this.selectedPath, group.getProtectType());
            Debug.print(flag + "setProtectType");
            if(group.getProtectType() == CDPConstants.PROTECT_SNAPSHOT){
                flag = flag && cdp.configSnapshotProtect(this.selectedPath, (int)group.getlAutoSnapshotInterval(), group.getiMaxNum());
                Debug.print(flag + "configSnapshotProtect");
                Debug.print("getDiskGroupName()"+group.getDiskGroupName());
                Debug.print( "getlAutoSnapshotInterval()"+group.getlAutoSnapshotInterval() + "");
                Debug.print( "getiMaxNum()" + group.getiMaxNum());
            }            
        }
        
        boolean isFixedTime = this.radioValue.equals("1") ? false : true;
        flag = flag && InterfaceFactory.getSYNCInterfaceInstance().setLocalSyncInfo(this.sourcePath, selectedPath, startCal, endCal, isFixedTime);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setLocalCopyFailed"), res.get("setLocalCopyFailed")));
            return null;
        }
        String param = "path=" + this.sourcePath + "&guid=" + this.guid + "&dg=" + this.dg;
        for(String name : this.groupNames){
            param += "&groupName=" + name;
        }
        return "copy?faces-redirect=true&amp;" + param;
    }

    public String cancel() {
        String param = "path=" + this.sourcePath + "&guid=" + this.guid + "&dg=" + this.dg;
        for(String name : this.groupNames){
            param += "&groupName=" + name;
        }
        return "copy?faces-redirect=true&amp;" + param;
    }
}
