/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
public class CopyPlanBean {

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
    private int iFileSystemHashCode;
    private String basename = "cdp.copy_plan";
    public String guid;
    private int level;
    private String dg;
    private List<String> groupNames;

    public CopyPlanBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        String path = request.getParameter("path");
        guid = request.getParameter("guid");
        if(request.getParameter("level") != null){
            level = Integer.parseInt(request.getParameter("level"));
        }
        dg = request.getParameter("dg");
        this.groupNames = Arrays.asList(request.getParameterValues("groupName"));
        String sreturnURL = request.getParameter("returnURL");
        String smodule = request.getParameter("module");
        String siFileSystemHashCode = request.getParameter("iFileSystemHashCode");
        String sisLocal = request.getParameter("iLocal");
        String start = request.getParameter("startTime");
        String end = request.getParameter("endTime");
        //bTimingJob
        String bTimingJob = request.getParameter("bTimingJob");
        if (path != null) {
            this.sourcePath = path;
        }
        if (returnURL != null) {
            this.returnURL = sreturnURL;
        }
        if (smodule != null) {
            this.module = smodule;
        }
        if (siFileSystemHashCode != null) {
            this.iFileSystemHashCode = Integer.parseInt(siFileSystemHashCode);
        }
        if (sisLocal != null) {
            this.isLocal = sisLocal.equals("1") ? true : false;
        }

        MSAResource res = new MSAResource();
        this.title = res.get(basename,"nasTitle");
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

        //this.endTime = "24:00";
//        startTimeList.add("00:00");
//        startTimeList.add("01:30");
//        startTimeList.add("02:00");
//        startTimeList.add("02:30");
    }

    public ArrayList<String> getEndtimeList() {
        return endtimeList;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
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
            System.out.println(ex.getMessage());
        }
        boolean isFixedTime = this.radioValue.equals("1") ? false : true;
        MsaSYNCInterface syncInterface = InterfaceFactory.getSYNCInterfaceInstance();
        boolean flag = syncInterface.moidfySyncTimingTask(this.sourcePath, this.iFileSystemHashCode, startCal, endCal, isFixedTime, this.isLocal);
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setFailed"), res.get("setFailed")));
            return null;
        }
        String param = "path=" + this.sourcePath + "&guid=" + this.guid + "&level=" + this.level + "&dg=" + this.dg;
        for(String name : this.groupNames){
            param += "&groupName=" + name;
        }
        return "copy.xhtml?faces-redirect=true&amp;" + param;
    }

    public String goBack() {
        String param = "path=" + this.sourcePath + "&guid=" + this.guid + "&level=" + this.level + "&dg=" + this.dg;
        for(String name : this.groupNames){
            param += "&groupName=" + name;
        }
        return "copy.xhtml?faces-redirect=true&amp;" + param;
    }
}
