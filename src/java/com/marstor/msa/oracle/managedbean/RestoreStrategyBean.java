/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.oracle.validator.OracleValidator;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "rStrategyBean")
@ViewScoped
public class RestoreStrategyBean implements Serializable {

    private String jobName = "";
    private String dbName = "";
    private String script = "";
    private boolean restoreControl=false;
    private MSAResource res = new MSAResource();

    public RestoreStrategyBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        dbName = request.getParameter("dbName");
        script = request.getParameter("script");
       this.restoreControl="true".equals(request.getParameter("restoreControl"));
       if(this.restoreControl)
       {
           FacesContext.getCurrentInstance().addMessage("resControlMessage", new FacesMessage(FacesMessage.SEVERITY_INFO, "请将生成的脚本拷贝到备份服务器上执行", ""));
       }
        if (null != script) {
            script = script.replace("<", "=");
        }

        String job = request.getParameter("jobName");
        if (null != job) {
            jobName = job;
        }
    }

    public boolean isRestoreControl()
    {
        return restoreControl;
    }

    public void setRestoreControl(boolean restoreControl)
    {
        this.restoreControl = restoreControl;
    }
    
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public void getDBName(String dbName) {
        this.dbName = dbName;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    public String createScript() {
        String temp=this.restoreControl?"true":"false";
        String params = "dbName=" + dbName + "&jobName=" + jobName+"&restoreControl="+temp;

        return "create_restore_script?faces-redirect=true" + params;
    }

    public String execute() {

        if (!OracleValidator.checkNetServiceName(jobName)) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("RestoreJobNameError"), ""));
            return null;
        }
        if (script.equals("")) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("RestoreJobScriptError"), ""));
            return null;
        }


        boolean executeRestoreJob = InterfaceFactory.getOracleInterfaceInstance().executeRestoreJob(dbName, jobName, script);
        if (executeRestoreJob) {
            String para = "dbName=" + dbName + "&jobName=" + jobName;
            return "real_time_log?faces-redirect=true" + para;
        } else {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("RestoreJobExecuteFailed"), ""));
            return null;
        }
    }
}
