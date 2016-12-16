/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.oracle.bean.BackupTaskInfo;
import com.marstor.msa.oracle.model.WebBackupTask;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
@ManagedBean(name = "oracleLogBean")
@ViewScoped
public class OracleLogBean implements Serializable {

    private String dbName = "";
    private String jobName = "";
    private WebBackupTask selectedBackup = null;
    private List<WebBackupTask> list;
    private MSAResource res = new MSAResource();

    public OracleLogBean() {

        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        dbName = request.getParameter("dbName");
        jobName = request.getParameter("jobName");
        initBackupTaskList();
    }

    public void setSelectedBackup(WebBackupTask selectedBackup) {
        this.selectedBackup = selectedBackup;
    }

    public WebBackupTask getSelectedBackup() {
        return selectedBackup;
    }

    private void initBackupTaskList() {
        list = new ArrayList<WebBackupTask>();
        List<BackupTaskInfo> backupJobInfo = InterfaceFactory.getOracleInterfaceInstance().getBackupJobInfo(dbName, jobName);
        for (BackupTaskInfo info : backupJobInfo) {
            WebBackupTask task = new WebBackupTask();
            task.setDBName(dbName);
            task.setJobName(info.taskName);
            task.setStartTime(info.taskBeginDatetime);
            task.setEndTime(info.taskEndDatetime);
            task.setData(DatabaseNameBean.getDate(info.totalDataSize));

            if (info.taskBeginDatetime.equals("") || info.taskEndDatetime.equals("") || info.totalDataSize.equals("")) {
                task.setSpeed("0.00MB/s");
            } else {
                Double valueOf = Double.valueOf(info.totalDataSize);
                String speed = DatabaseNameBean.getSpeed(Double.valueOf(info.totalDataSize), info.taskBeginDatetime, info.taskEndDatetime);
                task.setSpeed(speed);
            }

            task.setStatus(getStatus(info.taskStatus));
            if (info.taskStatus == 2) {
                task.setBRun(true);
            }
            task.setInstance(info.instanceName);
            String useTime = DatabaseNameBean.useTime(info.taskBeginDatetime, info.taskEndDatetime);
            if (null != useTime) {
                task.setTimeUsed(useTime);
            }
            list.add(task);
        }
        Collections.reverse(list);
    }

    public List<WebBackupTask> getBackupTaskList() {
        return list;
    }

    public void deleteBackupTask() {
        if (InterfaceFactory.getOracleInterfaceInstance().deleteInstanceLog(selectedBackup.getDBName(), selectedBackup.getJobName(), selectedBackup.getInstance())) {
            System.out.println(selectedBackup.getDBName() + "" + selectedBackup.getJobName() + "" + selectedBackup.getInstance());
            initBackupTaskList();
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("DeleteBackupJobSuccess"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DeleteBackupJobFailed"), ""));
        }
    }

    public String lookRMANLog() {
        String param = "dbName=" + selectedBackup.getDBName() + "&jobName=" + selectedBackup.getJobName() + "&instance=" + selectedBackup.getInstance();

        return "backup_rman_log?faces-redirect=true" + param;
    }

    public static void main(String[] arg) {
        String sd = "";
        try {
            DecimalFormat df = new DecimalFormat(".00");
            System.out.println(df.format(Double.valueOf(sd)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getStatus(int status) {
        if (1 == status) {
            return res.get("Success");
        } else if (2 == status) {
            return res.get("Running");
        } else if (3 == status) {
            return res.get("Failed");
        } else {
            return "";
        }
    }
}
