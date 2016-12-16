/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyEncryp;
import com.marstor.msa.oracle.bean.BackupJob;
import com.marstor.msa.oracle.model.WebBackupInfo;
import com.marstor.msa.oracle.model.Channel;
import com.marstor.msa.oracle.model.DBInstance;
import com.marstor.msa.oracle.bean.DatabaseName;
import com.marstor.msa.oracle.bean.Node;
import com.marstor.msa.oracle.bean.RestoreTaskInfo;
import com.marstor.msa.oracle.model.WebRestoreTask;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "DatabaseBean")
@ViewScoped
public class DatabaseNameBean implements Serializable {

    private DBInstance selectDBName = null;
    private List<DBInstance> list;
    private WebBackupInfo selectedBackup = null;
    private WebRestoreTask selectedRestore = null;
    private MSAResource res = new MSAResource();

    public DatabaseNameBean() {
        init();
    }

    public void setSelectedRestore(WebRestoreTask selectedRestore) {
        this.selectedRestore = selectedRestore;
    }

    public WebRestoreTask getSelectedRestore() {
        return selectedRestore;
    }

    public List<DBInstance> getList() {
        
        return list;
    }

    public void setSelectDBName(DBInstance select) {
        this.selectDBName = select;
    }

    public DBInstance getSelectDBName() {
        return selectDBName;
    }

    public void setSelectedBackup(WebBackupInfo selectedBackup) {
        this.selectedBackup = selectedBackup;
    }

    public WebBackupInfo getSelectedBackup() {
        return selectedBackup;
    }

    public void init() {
        list = new ArrayList<DBInstance>();
        List<DatabaseName> dbNameList = InterfaceFactory.getOracleInterfaceInstance().getDBInfo();
        if (0 == dbNameList.size()) {
            return;
        }

        for (int i = 0; i < dbNameList.size(); i++) {
            ArrayList<Node> nodeList = dbNameList.get(i).list;
            List<Channel> channelList = new ArrayList<Channel>();

            if (0 != nodeList.size()) {
                for (int y = 0; y < nodeList.size(); y++) {
                    HashMap<String, String> map = nodeList.get(y).nodeMap;
                    Iterator iterator = map.keySet().iterator();
                    while (iterator.hasNext()) {
                        String name = (String) iterator.next();
                        String path = (String) map.get(name);
                        channelList.add(new Channel(name, path, nodeList.get(y).nodeNetServiceName));
                    }
                }
            }

            DBInstance db = new DBInstance(dbNameList.get(i).databaseName, dbNameList.get(i).user,
                    String.valueOf(MyEncryp.Decode(dbNameList.get(i).password.toCharArray())),
                    channelList, dbNameList.get(i).netServiceName);
            list.add(db);
        }
    }

    public void deleteDBName() {
        FacesContext context = FacesContext.getCurrentInstance();  //»°session”Ú
        if (InterfaceFactory.getOracleInterfaceInstance().deleteSingleDB(selectDBName.getDBName())) {
            //list.remove(this.selectDBName);
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("DeleteDBSuccess"), ""));
        } else {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DeleteDBFailed"), ""));
        }
         init();
    }

    public String addChannelInSDB(String name, String nsName, String path) {
        Channel channel = new Channel();
        channel.setName(name);
        channel.setNSName(nsName);
        channel.setPath(path);
        selectDBName.getList().add(channel);
        return "oracle_dbproperty?faces-redirect=true";
    }

    public String backupJob(String db) {
        String params = "dbName=" + db;
        return "oracle_backup?faces-redirect=true" + params;
    }

    public String restoreJob(String db) {
        String params = "dbName=" + db;
        return "oracle_restore?faces-redirect=true" + params;
    }

    public String restoreTask() {
        String param = "dbName=" + selectedRestore.getDBName() + "&jobName=" + selectedRestore.getJobName() + "&instance=" + selectedRestore.getInstance();

        return "restore_rman_log?faces-redirect=true" + param;
    }

    public String setRMAN(String db) {
        String params = "dbName=" + db;
        return "oracle_setrman?faces-redirect=true" + params;
    }

    public List<WebBackupInfo> getJobInfo(String dbName) {
        List<WebBackupInfo> list = new ArrayList<WebBackupInfo>();
        List<BackupJob> allJobInfo = InterfaceFactory.getOracleInterfaceInstance().getAllJobInfo(dbName);

        if (allJobInfo.size() > 0) {
            for (BackupJob job : allJobInfo) {
                WebBackupInfo jobInfo = new WebBackupInfo();
                jobInfo.setDBName(job.databaseName);
                jobInfo.setJobName(job.taskName);
                jobInfo.setTimes(job.job.executeTimes);
                jobInfo.setSuccess(job.job.successTimes);
                jobInfo.setLastTime(job.job.lastExecuteTime);
                list.add(jobInfo);
            }
        }

        return list;
    }

    public void executeBackupJob() {
        if (InterfaceFactory.getOracleInterfaceInstance().executeJob(selectedBackup.getDBName(), selectedBackup.getJobName())) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("ExecuteJobImmSuccess"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ExecuteJobImmFailed"), ""));
        }
         init();
        RequestContext.getCurrentInstance().execute("executeimm.hide()");
    }

    public void deleteBackupJob() {

        if (!InterfaceFactory.getOracleInterfaceInstance().deleteJobResult(1, selectedBackup.getDBName(), selectedBackup.getJobName())) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DeleteBackupJobFailed"), ""));
        }
         init();
    }

    public String backupJobLog() {

        String param = "dbName=" + selectedBackup.getDBName() + "&jobName=" + selectedBackup.getJobName();
        return "oracle_logs?faces-redirect=true" + param;

    }

    public String backupAttribute() {
        String param = "dbName=" + selectedBackup.getDBName() + "&jobName=" + selectedBackup.getJobName();
        return "backup_property?faces-redirect=true" + param;
    }

    public List<WebRestoreTask> getRestoreTask(String dbName) {
        List<WebRestoreTask> list = new ArrayList<WebRestoreTask>();
        List<RestoreTaskInfo> restoreInfo = InterfaceFactory.getOracleInterfaceInstance().getRestoreInfo(dbName);
        for (RestoreTaskInfo info : restoreInfo) {
            WebRestoreTask task = new WebRestoreTask();
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
            task.setInstance(info.instanceName);

            if (info.taskStatus == 2) {
                task.setBRun(true);
            }

            String useTime = useTime(info.taskBeginDatetime, info.taskEndDatetime);
            if (null != useTime) {
                task.setTimeUsed(useTime);
            }
            list.add(task);
        }
        return list;
    }

    public void deleteRestoreTask() {
        if (!InterfaceFactory.getOracleInterfaceInstance().deleteJobResult(2, selectedRestore.getDBName(), selectedRestore.getJobName())) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DeleteRestoreJobFailed"), ""));
        }
         init();
    }

    public static String getSpeed(double totalSize, String startTime, String endTime) {
        try {
            if (startTime.equals("") || endTime.equals("") || 0 == totalSize) {
                return "0.00MB/s";
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long start = format.parse(startTime).getTime();
            long end = format.parse(endTime).getTime();
            long useTime = (end - start) / 1000;

            if (0 == useTime) {
                return "0.00MB/s";
            }
            DecimalFormat decimalFormat = new DecimalFormat("###.00");
            if (totalSize / useTime < 1) {
                String str = "0" + String.valueOf(decimalFormat.format(totalSize / useTime)) + "MB/s";
                return str;
            } else {
                String str = String.valueOf(decimalFormat.format(totalSize / useTime)) + "MB/s";
                return str;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00MB/s";
        }
    }

    public static String useTime(String startTime, String endTime) {
        try {
            if (startTime.equals("") || endTime.equals("")) {
                return "";
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long start = format.parse(startTime).getTime();
            long end = format.parse(endTime).getTime();
            long hour = (int) ((end - start) / 1000 / 60 / 60);
            long minute = ((end - start) - hour * 60 * 60 * 1000) / 1000 / 60;
            long second = ((end - start) - (hour * 60 * 60 * 1000) - (minute * 60 * 1000)) / 1000;
            String strHour = String.valueOf(hour);
            String strMinute = String.valueOf(minute);
            String strSecond = String.valueOf(second);
            if (strHour.length() < 2) {
                strHour = "0" + String.valueOf(hour);
            }
            if (strMinute.length() < 2) {
                strMinute = "0" + String.valueOf(minute);
            }
            if (strSecond.length() < 2) {
                strSecond = "0" + String.valueOf(second);
            }
            return strHour + ":" + strMinute + ":" + strSecond;
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static String getDate(String total) {
        try {
            String date = total;
            if (null == date || date.equals("")) {
                date = "0";
            }
            Double valueOf = Double.valueOf(date);
            DecimalFormat df = new DecimalFormat("###.00");
            if (valueOf < 1) {
                return "0" + df.format(valueOf) + "MB";
            } else {
                return df.format(valueOf) + "MB";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getStatus(int status) {
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

    public static void main(String[] arg) {
        System.out.println(0 == 0.00);
    }

    public void confirmDeleteDB() {
        RequestContext.getCurrentInstance().execute("deletedb.show()");
    }

    public void confirmExecuteBA() {
        RequestContext.getCurrentInstance().execute("executeimm.show()");
    }

    public void confirmDeleteBA() {
        RequestContext.getCurrentInstance().execute("deletebackup.show()");
    }

    public void confirmDeleteRestore() {
        RequestContext.getCurrentInstance().execute("deleterestore.show()");
    }
}
