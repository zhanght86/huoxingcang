/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.util.InterfaceFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "backupRmanLog")
@ViewScoped
public class BackupRmanLog implements Serializable{

    private String dbName = "";
    private String jobName = "";
    private String instance = "";
    private String log = "";
    private StreamedContent logFile;

    public BackupRmanLog() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        dbName = request.getParameter("dbName");
        jobName = request.getParameter("jobName");
        instance = request.getParameter("instance");
        
        log = InterfaceFactory.getOracleInterfaceInstance().getTaskLog(1, dbName, jobName, instance);
        
    }

    public void setLog(String log){
        this.log = log;
    }
    
    public String getLog() {
        return log;
    }
    
    public StreamedContent getLogFile() {
        InputStream stream = new ByteArrayInputStream(log.getBytes());
        logFile = new DefaultStreamedContent(stream, "txt", "backup.log");
        return logFile;
    }
    
    public String goBack() {

        String param = "dbName=" + dbName + "&jobName=" + jobName;

        return "oracle_logs?faces-redirect=true" + param;
    }
}
