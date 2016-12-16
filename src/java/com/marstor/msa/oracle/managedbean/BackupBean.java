/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.oracle.bean.DatabaseName;
import com.marstor.msa.oracle.bean.StrategyContent;
import com.marstor.msa.oracle.model.TablesDataModel;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "backupBean")
@ViewScoped
public class BackupBean implements Serializable {
    private MSAResource res = new MSAResource();
    private boolean isDB = true;
    private boolean dbSelecter = false;
    private boolean isTables = true;
    private int dbType = 1;
    private String dbName = "";
    private String type = "";
    private String jobName = "";

    public BackupBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        String name = request.getParameter("dbName");
        if (null != name) {
            this.dbName = name;
        }

        String job = request.getParameter("jobName");
        if (null != job) {
            this.jobName = job;
        }

        String sType = request.getParameter("type");
        if (null != sType) {
            this.type = sType;
        }

        List<DatabaseName> dbInfo = InterfaceFactory.getOracleInterfaceInstance().getDBInfo();
        for (DatabaseName db : dbInfo) {
            if (db.databaseName.equals(name)) {

                List<String> queryTableSpace = InterfaceFactory.getOracleInterfaceInstance().queryTableSpace(db.user, db.password, db.netServiceName);
                if (0 == queryTableSpace.size()) {
                    FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            res.get("GetTableSpacesInfoFailed"), res.get("PleaseCheckTheDatabaseConfig")));
                }

                if (0 != queryTableSpace.size()) {
                    selectedData = new String[queryTableSpace.size()];
                    for (int i = 0; i < queryTableSpace.size(); i++) {
                        selectedData[i] = queryTableSpace.get(i);
                    }
                }
                dataModel = new TablesDataModel(queryTableSpace);
            }
        }
    }

    public void setIsDB(boolean isDB) {
        this.isDB = isDB;
    }

    public boolean getIsDB() {
        return isDB;
    }

    public void setDBSelecter(boolean dbSelecter) {
        this.dbSelecter = dbSelecter;
    }

    public boolean getDBSelecter() {
        return dbSelecter;
    }

    public void setDBType(int dbType) {
        this.dbType = dbType;
    }

    public int getDBType() {
        return dbType;
    }

    public void isDBListener() {
        if (isDB) {
            this.dbSelecter = false;
        } else {
            this.dbSelecter = true;
            RequestContext.getCurrentInstance().execute("bui.show();");
        }
    }

    public void isTablesListener() {
        if (this.dbType == 2) {
            RequestContext.getCurrentInstance().execute("bui.hide();");
        } else {
            RequestContext.getCurrentInstance().execute("bui.show();");
        }
    }

    private boolean bSPfile = false;

    public void setBSPfile(boolean bSPfile) {
        this.bSPfile = bSPfile;
    }

    public boolean getBSPfile() {
        return bSPfile;
    }
    private boolean bLog = true;

    public void setBLog(boolean bLog) {
        this.bLog = bLog;
    }

    public boolean getBLog() {
        return bLog;
    }

    public void logListener() {
        if (bLog) {
            bDaysType = false;
            if (2 == iDaysType) {
                bDays = false;
            }

        } else {
            bDaysType = true;
            bDays = true;
        }
    }
    private boolean bControlFile = false;

    public void setBControlFile(boolean bControlFile) {
        this.bControlFile = bControlFile;
    }

    public boolean getBControlFile() {
        return bControlFile;
    }
    private boolean bDaysType = false;

    public void setBDaysType(boolean bDays) {
        this.bDaysType = bDays;
    }

    public boolean getBDaysType() {
        return bDaysType;
    }
    private int iDaysType = 1;

    public void setIDaysType(int iDaysType) {
        this.iDaysType = iDaysType;
    }

    public int getIDaysType() {
        return iDaysType;
    }

    public void daysListener() {
        if (2 == iDaysType) {
            bDays = false;
        } else {
            bDays = true;
        }
    }
    private boolean bDays = true;

    public void setBDays(boolean bDays) {
        this.bDays = bDays;
    }

    public boolean getBDays() {
        return bDays;
    }
    private int iDays = 0;

    public void setIDays(int iDays) {
        this.iDays = iDays;
    }

    public int getIDays() {
        return iDays;
    }
    private int iLevel = 1;

    public void setILevel(int iLevel) {
        this.iLevel = iLevel;
    }

    public int getILevel() {
        return iLevel;
    }
    private TablesDataModel dataModel;

    public TablesDataModel getDataModel() {
        return dataModel;
    }
    private String selectedTable;

    public void setSelectedTable(String selectedTable) {
        this.selectedTable = selectedTable;
    }

    public String getSelectedTable() {
        return selectedTable;
    }
    private String[] selectedData;

    public void setSelectedData(String[] selectedData) {
        this.selectedData = selectedData;
    }

    public String[] getSelectedData() {
        return selectedData;
    }

    public String backupScript() {
        String script = getBackupScript();

        if (null == script) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("CreateBackupScriptFailed"), ""));
            return null;
        }

        if (type.equals("backup")) {
            String para = "script=" + script + "&dbName=" + dbName + "&jobName=" + jobName;
            return "oracle_backup?faces-redirect=true" + para;
        }

        if (type.equals("property")) {
            String para = "script=" + script + "&dbName=" + dbName + "&jobName=" + jobName;
            return "backup_property?faces-redirect=true" + para;
        }
        return null;
    }

    private String getBackupScript() {
        StrategyContent str = new StrategyContent();

        //备份数据库部分
        if (isDB) {
            str.backupDatabase = 1;
            if (1 == dbType) {
                str.backupObject = 1;
            }
            if (2 == dbType) {
                str.backupObject = 2;
                ArrayList<String> list = new ArrayList<String>();
                if (null != selectedData) {
                    for (String data : selectedData) {
                        list.add(data);
                    }
                }
                str.list = list;
            }
        }

        if (bSPfile) {
            str.backupSpfile = 1;
        }

        if (bLog) {
            str.backupArchivefile = 1;
            str.deleteArchivefile = 1;
            if (1 == iDaysType) {
                str.deleteArchivefile = 2;
            }
            if (2 == iDaysType) {
                str.deleteArchivefile = 3;
                str.deleteArchivefileDay = iDays;
            }
        }

        if (bControlFile) {
            str.backupControlfile = 1;
        }

        str.backupType = iLevel;

        String script = InterfaceFactory.getOracleInterfaceInstance().getBackupScript(str, dbName);
        return script;
    }

    public String goBack() {
        String param = "dbName=" + dbName;
        return "oracle_backup?faces-redirect=true" + param;
    }
}
