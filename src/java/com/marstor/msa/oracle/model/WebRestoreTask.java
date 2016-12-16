/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class WebRestoreTask implements Serializable {

    private String dbName = "";
    private String jobName = "";
    private String status = "";
    private String startTime = "";
    private String endTime = "";
    private String speed = "";
    private String timeUsed = "";
    private String data = "";
    private String instance = "";
    private boolean bRun = false;

    public WebRestoreTask() {
    }

    public boolean getBRun() {
        return bRun;
    }

    public void setBRun(boolean bRun) {
        this.bRun = bRun;
    }

    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

    public String getDBName() {
        return dbName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSpeed() {
        return speed;
    }

    public void setTimeUsed(String time) {
        this.timeUsed = time;
    }

    public String getTimeUsed() {
        return timeUsed;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getInstance() {
        return instance;
    }
}
