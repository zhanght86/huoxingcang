/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.oracle.bean.DatabaseName;
import com.marstor.msa.oracle.bean.RMAN;
import com.marstor.msa.oracle.validator.OracleValidator;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "rman")
@ViewScoped
public class RmanBean implements Serializable {

    private MSAResource res = new MSAResource();
    private String dbName = "";
    private int rmanNum = 1;
    private boolean series = false;
    private boolean days = true;
    private boolean isPath = true;
    private boolean path = false;
    private String sPath = "";

    public RmanBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        dbName = request.getParameter("dbName");
        RMAN rman = null;

        List<DatabaseName> dbInfo = InterfaceFactory.getOracleInterfaceInstance().getDBInfo();
        for (DatabaseName db : dbInfo) {
            if (db.databaseName.equals(dbName)) {
                rman = db.rman;
                break;
            }
        }

        if (rman != null) {
            System.out.println("init(rman)");
            init(rman);
        }
    }

    public void setSPath(String sPath) {
        this.sPath = sPath;
    }

    public String getSPath() {
        return sPath;
    }

    public int getRmanNum() {
        return rmanNum;
    }

    public void setRmanNum(int num) {
        this.rmanNum = num;
    }

    public void rmanListener() {
        if (1 == rmanNum) {
            series = false;
            days = true;
        }

        if (2 == rmanNum) {
            series = true;
            days = false;
        }
    }

    public void setDays(boolean days) {
        this.days = days;
    }

    public boolean getSeries() {
        return series;
    }

    public void setSeries(boolean series) {
        this.series = series;
    }

    public boolean getDays() {
        return days;
    }

    public String setRMAN() {
        FacesContext context = FacesContext.getCurrentInstance();  //»°session”Ú
        if (0 == this.rmanNum) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("RMANConfigError"), ""));
            return null;
        }


        RMAN rman = new RMAN();

        rman.rmanBackupSaveType = this.rmanNum;


        if (1 == this.rmanNum) {
            try {
                rman.rmanBackupSaveUnit = Integer.valueOf(this.iUnit);
                if(rman.rmanBackupSaveUnit < 1){
                    context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("SeriesError"), ""));
                    return null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("SeriesError"), ""));
                return null;
            }
        }

        if (2 == this.rmanNum) {
            try {
                rman.rmanBackupSaveUnit = Integer.valueOf(this.idays);
                if(rman.rmanBackupSaveUnit < 1){
                    context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DayError"), ""));
                    return null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("DayError"), ""));
                return null;
            }
        }

        if (this.isPath) {
            if (!OracleValidator.checkRMANPath(this.sPath)) {
                context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ChannelPathError"), ""));
                return null;
            }
            rman.autoBackupControlfile = 1;
           
            rman.controlfileSavePath = this.sPath;
        }
         if(this.sPath==null)
            {
                this.sPath="";
            }

        boolean set = InterfaceFactory.getOracleInterfaceInstance().setRMANConfig(dbName, rman);

        if (set) {
            return "oracle_database?faces-redirect=true";
        } else {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("SetRMANConfigFailed"), ""));
            return null;
        }
    }

    public void pathListener() {
        if (isPath == true) {
            path = false;
        } else {
            path = true;
        }

    }

    public void setIsPath(boolean isPath) {
        this.isPath = isPath;
    }

    public boolean getIsPath() {
        return isPath;
    }

    public void setPath(boolean path) {
        this.path = path;
    }

    public boolean getPath() {
        return path;
    }

    private void init(RMAN rman) {
        if (0 == rman.rmanBackupSaveType) {
            return;
        }

        rmanNum = rman.rmanBackupSaveType;
        if (1 == rmanNum) {
            this.iUnit = String.valueOf(rman.rmanBackupSaveUnit);
            this.series = false;
        }

        if (2 == rmanNum) {
            this.idays = String.valueOf(rman.rmanBackupSaveUnit);
            this.days = false;
        }

        if (1 == rman.autoBackupControlfile) {
            this.isPath = true;
            this.path = false;
        }

        sPath = rman.controlfileSavePath;
    }
    private String idays;
    private String iUnit;

    public String getIdays() {
        return idays;
    }

    public void setIdays(String idays) {
        this.idays = idays;
    }

    public String getIUnit() {
        return iUnit;
    }

    public void setIUnit(String iUnit) {
        this.iUnit = iUnit;
    }

    public static void main(String[] arg) {
        try {
            int iLucy = Integer.valueOf("10.1");
            System.out.println(iLucy);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
