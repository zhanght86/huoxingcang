/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.oracle.bean.RestoreStrategyContent;
import com.marstor.msa.oracle.web.OracleInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "restoreBean")
@ViewScoped
public class RestoreBean implements Serializable {

    private int iSpfile = 1;
    private boolean bSpfile = true;
    private String sSpfile = "";
    private int iControlFile = 1;
    private boolean bControlFile = true;
    private String sControlFile = "";
    private String dbName = "";
    private String jobName = "";
    private String activeIndex = "0";
    private boolean restoreControl=false;
    public RestoreBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        

        activeIndex = request.getParameter("active");
        restoreControl="true".equals(request.getParameter("restoreControl"));
        if (activeIndex == null || activeIndex.equals("")) {
            activeIndex = "0";
        }

        dbName = request.getParameter("dbName");

        String job = request.getParameter("jobName");
        if (null != job) {
            jobName = job;
        }
    }

    public String getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(String activeIndex) {
        this.activeIndex = activeIndex;
    }

    public String getDBName() {
        return dbName;
    }

    public String getJobName() {
        return jobName;
    }
    private boolean spfile = false;

    public void setSpfile(boolean spfile) {
        this.spfile = spfile;
    }

    public boolean getSpfile() {
        return spfile;
    }
    private boolean isSpfile = true;

    public void setIsSpfile(boolean isSpfile) {
        this.isSpfile = isSpfile;
    }

    public boolean getIsSpfile() {
        return isSpfile;
    }

    public void isSpfileListener() {
        if (spfile) {
            isSpfile = false;
            if (iSpfile == 2) {
                bSpfile = false;
            }
        } else {
            isSpfile = true;
            bSpfile = true;
        }
    }
    private boolean controlFile = false;

    public void setControlFile(boolean controlFile) {
        this.controlFile = controlFile;
    }

    public boolean getControlFile() {
        return controlFile;
    }
    private boolean isControlFile = true;

    public void setIsControlFile(boolean isControlFile) {
        this.isControlFile = isControlFile;
    }

    public boolean getIsControlFile() {
        return isControlFile;
    }

    public void isControlFileListener() {
        if (controlFile) {
            isControlFile = false;
            if (iControlFile == 2) {
                bControlFile = false;
            }

        } else {
            isControlFile = true;
            bControlFile = true;
        }


    }

    public void setISpfile(int iSpfile) {
        this.iSpfile = iSpfile;
    }

    public int getISpfile() {
        return this.iSpfile;
    }

    public void setBSpfile(boolean bSpfile) {
        this.bSpfile = bSpfile;
    }

    public boolean getBSpfile() {
        return bSpfile;
    }

    public void setSSpfile(String sSpfile) {
        this.sSpfile = sSpfile;
    }

    public String getSSpfile() {
        return sSpfile;
    }

    public void spfileListener() {
        if (2 == iSpfile) {
            bSpfile = false;
        } else {
            bSpfile = true;

        }
    }

    public void setIControlFile(int iControlFile) {
        this.iControlFile = iControlFile;
    }

    public int getIControlFile() {
        return iControlFile;
    }

    public void setBControlFile(boolean bControlFile) {
        this.bControlFile = bControlFile;
    }

    public boolean getBControlFile() {
        return bControlFile;
    }

    public void setSControlFile(String sControlFile) {
        this.sControlFile = sControlFile;
    }

    public String getSControlFile() {
        return sControlFile;
    }

    public void controlFileListener() {
        if (2 == iControlFile) {
            bControlFile = false;
        } else {
            bControlFile = true;
        }
    }
    private boolean bRestore = true;
    private boolean bRestoreType = false;

    public void setBRestore(boolean bRestore) {
        this.bRestore = bRestore;
    }

    public boolean getBRestore() {
        return bRestore;
    }

    public void setBRestoreType(boolean bRestoreType) {
        this.bRestoreType = bRestoreType;
    }

    public boolean getBRestoreType() {
        return bRestoreType;
    }
    private boolean bTables = true;

    public void setBTables(boolean bTables) {
        this.bTables = bTables;
    }

    public boolean getBTables() {
        return bTables;
    }

    public void restoreListener() {
        if (bRestore) {
            bRestoreType = false;
            if (2 == iRestoreType) {
                bLine = false;
                bTables = false;
            }
        } else {
            bRestoreType = true;
            bLine = true;
            bTables = true;
        }
    }
    private int iRestoreType = 1;

    public void setIRestoreType(int iRestoreType) {
        this.iRestoreType = iRestoreType;
    }

    public int getIRestoreType() {
        return iRestoreType;
    }
    private boolean bLine = true;

    public void setBLine(boolean bLine) {
        this.bLine = bLine;
    }

    public boolean getBLine() {
        return bLine;
    }
    private List<String> lLine;

    public void setLLine(List<String> lLine) {
        this.lLine = lLine;
    }

    public List<String> getLLine() {
        return lLine;
    }
    private String sTables = "";

    public void setSTables(String sTables) {
        this.sTables = sTables;
    }

    public String getSTables() {
        return sTables;
    }

    public void restoreTypeListener() {
        if (1 == iRestoreType) {
            this.bLine = true;
            this.bTables = true;
        } else {
            this.bLine = false;
            this.bTables = false;
        }
    }
    private boolean bRecover = true;
    private boolean bRecoverType = false;

    public void setBRecover(boolean bRecover) {
        this.bRecover = bRecover;
    }

    public boolean getBRecover() {
        return bRecover;
    }

    public void setBRecoverType(boolean bRecoverType) {
        this.bRecoverType = bRecoverType;
    }

    public boolean getBRecoverType() {
        return bRecoverType;
    }
    private int sSeq;
    
    public void setSSeq(int sSeq) {
        this.sSeq = sSeq;
    }

    public int getSSeq() {
        return sSeq;
    }
    private int sThread=1;
    
public void setSThread(int sthread) {
        this.sThread = sthread;
    }

    public int getSThread() {
        return sThread;
    }
   
    
    private boolean bSeq = true;

    public void setBSeq(boolean bSeq) {
        this.bSeq = bSeq;
    }

    public boolean getBSeq() {
        return bSeq;
    }
    private int sSCN;

    public void setSSCN(int sSCN) {
        this.sSCN = sSCN;
    }

    public int getSSCN() {
        return sSCN;
    }
    private boolean bSCN = true;

    public void setBSCN(boolean bSCN) {
        this.bSCN = bSCN;
    }

    public boolean getBSCN() {
        return bSCN;
    }
    private String sTime = "";

    public void setSTime(String sTime) {
        this.sTime = sTime;
    }

    public String getSTime() {
        return sTime;
    }
    private boolean bOpenDB = false;

    public void setBOpenDB(boolean bOpenDB) {
        this.bOpenDB = bOpenDB;
    }

    public boolean getBOpenDB() {
        return bOpenDB;
    }
    private boolean bReLog = false;

    public void setBReLog(boolean bReLog) {
        this.bReLog = bReLog;
    }

    public boolean getBReLog() {
        return bReLog;
    }
    private boolean bLog = false;

    public void setBLog(boolean bLog) {
        this.bLog = bLog;
    }

    public boolean getBLog() {
        return bLog;
    }
    private boolean bDB = true;

    public void setBDB(boolean bDB) {
        this.bDB = bDB;
    }

    public boolean getBDB() {
        return bDB;
    }

    public void openDBListener() {
        if (bDB) {
            bReLog = false;
        } else {
            bReLog = true;
        }
    }

    public void recoverListener() {
        if (bRecover) {
            this.bRecoverType = false;
            this.bOpenDB = false;
            if (2 == iRecover) {
                bSeq = false;
            } else {
                bSeq = true;
            }

            if (3 == iRecover) {
                bSCN = false;
            } else {
                bSCN = true;
            }

            if (4 == iRecover) {
                bTime = false;
            } else {
                bTime = true;
            }
            if (bDBID) {
                bReLog = false;
            }
        } else {
            this.bRecoverType = true;
            this.bOpenDB = true;
            bSeq = true;
            bSCN = true;
            bTime = true;
            bReLog = true;
        }
    }
    private int iRecover = 1;

    public void setIRecover(int iRecover) {
        this.iRecover = iRecover;
    }

    public int getIRecover() {
        return iRecover;
    }
    private boolean bTime = true;

    public void setBTime(boolean bTime) {
        this.bTime = bTime;
    }

    public boolean getBTime() {
        return bTime;
    }

    public void recoverTypeListener() {
        if (2 == iRecover) {
            bSeq = false;
        } else {
            bSeq = true;
        }

        if (3 == iRecover) {
            bSCN = false;
        } else {
            bSCN = true;
        }

        if (4 == iRecover) {
            bTime = false;
        } else {
            bTime = true;
        }
    }
    private boolean bDBID = false;

    public void setBDBID(boolean bDBID) {
        this.bDBID = bDBID;
    }

    public boolean getBDBID() {
        return bDBID;
    }

    public void bDBIDListener() {
        if (bDBID) {
            bDBIDNum = false;
        } else {
            bDBIDNum = true;
        }

    }
    private boolean bDBIDNum = true;

    public void setBDBIDNum(boolean bDBIDNum) {
        this.bDBIDNum = bDBIDNum;
    }

    public boolean getBDBIDNum() {
        return bDBIDNum;
    }
    private long sDBID;

    public void setSDBID(long sDBID) {
        this.sDBID = sDBID;
    }

    public long getSDBID() {
        return sDBID;
    }

    public String restoreSpfile() {
        OracleInterface oracle = InterfaceFactory.getOracleInterfaceInstance();
        RestoreStrategyContent res = new RestoreStrategyContent();

        //设置Spfile文件的恢复情况
        res.restoreSpfile = 1;
        res.restoreSpfileFormAuto = iSpfile;
        if (2 == iSpfile) {
            res.restoreSpfileAppointFile = sSpfile;
        }
        String script = oracle.getRestoreScript(res, dbName);

        String params = "script=" + script + "&dbName=" + dbName + "&jobName=" + jobName+"&restoreControl=false";
        return "oracle_restore?faces-redirect=true" + params;
    }

    public String restoreControl() {
       
        OracleInterface oracle = InterfaceFactory.getOracleInterfaceInstance();
        RestoreStrategyContent res = new RestoreStrategyContent();

        //设置控制文件的恢复情况
        res.restoreControlfile = 1;
        res.restoreControlfileFormAuto = iControlFile;
        if (2 == iControlFile) {
            res.restoreControlfileAppointFile = sControlFile;
        }

        String script = oracle.getRestoreScript(res, dbName);
//设置恢复控制文件为真，用于返回后控制恢复脚本显示界面
        String params = "script=" + script + "&dbName=" + dbName + "&jobName=" + jobName+"&restoreControl=true";
        
        
        return "oracle_restore?faces-redirect=true" + params;
    }

    public String restoreDate() {
        String script = getRestoreScript();

        String params = "script=" + script + "&dbName=" + dbName + "&jobName=" + jobName+"&restoreControl=false";
        return "oracle_restore?faces-redirect=true" + params;
    }

    public String getRestoreScript() {
        OracleInterface oracle = InterfaceFactory.getOracleInterfaceInstance();
        RestoreStrategyContent res = new RestoreStrategyContent();

        //设置 Restore的恢复情况
        if (bRestore) {
            res.restore = 1;
            res.restoreType = iRestoreType;
            if (2 == iRestoreType) {
                res.tablespaces = sTables;
                for (String s : lLine) {
                    if (s.equals("1")) {
                        res.restoreTBBeforeOffline = 1;
                    }
                    if (s.equals("2")) {
                        res.restoreTBAfterOnline = 1;
                    }
                }
            }
        }

        //设置 Recover的恢复情况
        if (bRecover) {
            res.recover = 1;
            res.recoverType = iRecover;

            if (1 == iRecover) {
                res.datetime = sTime;
            }
            if (2 == iRecover) {
                res.sequence = sSeq;
                res.thread=this.sThread;
            }
            if (3 == iRecover) {
                res.scn = sSCN;
            }
            if (4 == iRecover) {
                res.datetime = sTime;
            }

            if (bDB) {
                res.restoreAfterOpenDB = 1;
                if (true == bLog) {
                    res.openDBType = 2;
                } else {
                    res.openDBType = 1;
                }
            }
        }
        //设置从DBID恢复情况
        if (bDBID) {
            res.useDBID = 1;
            res.dbID = sDBID;
        }

        //第二个参数是数据库名
        String restoreScript = oracle.getRestoreScript(res, dbName);
        if(null != restoreScript){
            restoreScript = restoreScript.replace("=", "<");
        }
        
        return restoreScript;
    }

    public String modifyChannel() {
        return "oracle_channel?faces-redirect=true";
    }
    
    public String goBack() {
        String temp=restoreControl?"true":"false";
        String param = "dbName=" + dbName+"&jobName="+this.jobName+"&restoreControl="+temp;
        System.out.println("oracle_restore?faces-redirect=true" + param);
        return "oracle_restore?faces-redirect=true"+ param;
    }
}
