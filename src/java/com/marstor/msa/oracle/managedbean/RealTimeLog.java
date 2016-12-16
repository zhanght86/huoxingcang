/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "realTimeLog")
@ViewScoped
public class RealTimeLog implements Serializable {
    private String dbName = "";
    private String jobName = "";
    private String instance = "";
    private String log = "";

    public RealTimeLog() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        dbName = request.getParameter("dbName");
        jobName = request.getParameter("jobName");
        instance = request.getParameter("instance");

        log = InterfaceFactory.getOracleInterfaceInstance().getRMANRealTimeLog(dbName, jobName);
    }

    public void setLog(String log){
        this.log = log;
    }
    
    public void increment(){
        this.log = InterfaceFactory.getOracleInterfaceInstance().getRMANRealTimeLog(dbName, jobName);
    }
    
    public String getLog() {
        return log;
    }
    
}
