/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class ConfigSyncPlan implements Serializable{

    private ArrayList<String> startTimeList = new ArrayList<String>();
    private ArrayList<String> endtimeList = new ArrayList<String>();
    private String radioValue;
    private String startTime = "";
    private String endTime = "";
    private String title = "";
    private String module = "";
    private String returnURL;
    private boolean timeRendered;
    private boolean isLocal;
    private String sourcePath = "";
    private String pathName = "";
    private int iFileSystemHashCode;
    private String sourceLabel; //界面上的Label组件，其值可能是“共享目录”，或者“存储空间”

    public ConfigSyncPlan() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        String path = request.getParameter("path");
        String returnURL = request.getParameter("returnURL");
        String module = request.getParameter("module");
        String iFileSystemHashCode = request.getParameter("iFileSystemHashCode");
        String isLocal = request.getParameter("iLocal");
        String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
        //bTimingJob
        String bTimingJob = request.getParameter("bTimingJob");
        if (path != null) {
            this.sourcePath = path;
        }
        if (returnURL != null) {
            this.returnURL = returnURL;
        }
        if (module != null) {
            this.module = module;
        }
        if (iFileSystemHashCode != null) {
            this.iFileSystemHashCode = Integer.parseInt(iFileSystemHashCode);
        }
        if (isLocal != null) {
            this.isLocal = isLocal.equals("1") ? true : false;
        }

        MSAResource res = new MSAResource();
        if (this.module.equals("nas")) {
            this.title = res.get("nasTitle");
            this.sourceLabel = res.get("sharePath");
            if (!this.sourcePath.equals("")) {
                this.pathName = "/" + this.sourcePath;
            }
        } else if (this.module.equals("vtl")) {
            this.title = res.get("vtlTitle");
            this.sourceLabel = res.get("storageSpace");
            if (!this.sourcePath.equals("")) {
                this.pathName = this.sourcePath;
            }
        }
        this.radioValue = bTimingJob.equals("0") ? "1" : "2";
        if (this.radioValue.equals(("1"))) {
            this.timeRendered = false;
        } else {
            this.timeRendered = true;
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
        if (start != null) {
            this.startTime = start;
        } else {
            this.startTime = "00:00";
        }
        if (end != null) {
            if (end.equals("00:00")) {
                this.endTime = "24:00";
            }else {
                this.endTime = end;
            }
        } else {
            this.endTime = "24:00";
        }
        Debug.print("end: " + end);
        //this.endTime = "24:00";
//        startTimeList.add("00:00");
//        startTimeList.add("01:30");
//        startTimeList.add("02:00");
//        startTimeList.add("02:30");
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getSourceLabel() {
        return sourceLabel;
    }

    public void setSourceLabel(String sourceLabel) {
        this.sourceLabel = sourceLabel;
    }

    public ArrayList<String> getEndtimeList() {
        return endtimeList;
    }

    public void setEndtimeList(ArrayList<String> endtimeList) {
        this.endtimeList = endtimeList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTimeRendered() {
        return timeRendered;
    }

    public void setTimeRendered(boolean timeRendered) {
        this.timeRendered = timeRendered;
    }

    public String getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(String radioValue) {
        this.radioValue = radioValue;
    }

    public ArrayList<String> getStartTimeList() {
        return startTimeList;
    }

    public void setStartTimeList(ArrayList<String> startTimeList) {
        this.startTimeList = startTimeList;
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

    public void listen() {
        if (this.radioValue.equals(("1"))) {
            this.timeRendered = false;
        } else {
            this.timeRendered = true;
        }
    }

    public String save() {
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
        boolean isFixedTime = this.radioValue.equals("1") ? false : true;
        MsaSYNCInterface syncInterface = InterfaceFactory.getSYNCInterfaceInstance();
        boolean flag = syncInterface.moidfySyncTimingTask(this.sourcePath, this.iFileSystemHashCode, startCal, endCal, isFixedTime, this.isLocal);
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setFailed"), res.get("setFailed")));
            return null;
        }
        String param = "path=" + this.sourcePath + "&" + "activeIndex=1" + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "nas_snap_sync?faces-redirect=true&amp;" + param;
    }

    public String goBack() {
        String param = "path=" + this.sourcePath + "&" + "activeIndex=1" + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "nas_snap_sync?faces-redirect=true&amp;" + param;
    }
}
