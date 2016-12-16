/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

/**
 *
 * @author Administrator
 */
public class SetLogPolicyParam {

    private String disk_group_guid;
    private long log_size;
    private int log_retention_time;
    private int log_after_full_strategy;
    private long snapshot_interval;

    public String getDisk_group_guid() {
        return disk_group_guid;
    }

    public void setDisk_group_guid(String disk_group_guid) {
        this.disk_group_guid = disk_group_guid;
    }

    public long getLog_size() {
        return log_size;
    }

    public void setLog_size(long log_size) {
        this.log_size = log_size;
    }

    public int getLog_retention_time() {
        return log_retention_time;
    }

    public void setLog_retention_time(int log_retention_time) {
        this.log_retention_time = log_retention_time;
    }

    public int getLog_after_full_strategy() {
        return log_after_full_strategy;
    }

    public void setLog_after_full_strategy(int log_after_full_strategy) {
        this.log_after_full_strategy = log_after_full_strategy;
    }

    public long getSnapshot_interval() {
        return snapshot_interval;
    }

    public void setSnapshot_interval(long snapshot_interval) {
        this.snapshot_interval = snapshot_interval;
    }

    
    
    
}
