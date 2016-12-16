/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "bJobInfo")
@RequestScoped
public class WebBackupInfo implements Serializable{
    private String dbName = "";
    private String jobName = "";
    private long times = 0;
    private long success = 0;
    private String lastTime = "";
    
    public WebBackupInfo(){
        
    }
    
    public void setDBName(String dbName){
        this.dbName = dbName;
    }
    
    public String getDBName(){
        return dbName;
    }
    
    
    public void setJobName(String jobName){
        this.jobName = jobName;
    }
    
    public String getJobName(){
        return jobName;
    }
    
    public void setTimes(long times){
        this.times = times;
    }
    
    public long getTimes(){
        return times;
    }
    
    public void setSuccess(long success){
        this.success = success;
    }
    
    public long getSuccess(){
        return success;
    }
    
    public void setLastTime(String lastTime){
        this.lastTime = lastTime;
    }
    
    public String getLastTime(){
        return lastTime;
    }
}
