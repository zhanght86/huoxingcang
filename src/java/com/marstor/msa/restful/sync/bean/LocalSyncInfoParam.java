/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.sync.bean;

import java.util.Calendar;

/**
 *
 * @author zeroz
 */
public class LocalSyncInfoParam {
    
    private String src_file_system;
    private String desc_file_system;
    private Calendar job_start_time;
    private Calendar job_end_time;
    private boolean begin_timing_job;

    public String getSrc_file_system() {
        return src_file_system;
    }

    public void setSrc_file_system(String src_file_system) {
        this.src_file_system = src_file_system;
    }

    public String getDesc_file_system() {
        return desc_file_system;
    }

    public void setDesc_file_system(String desc_file_system) {
        this.desc_file_system = desc_file_system;
    }

    public Calendar getJob_start_time() {
        return job_start_time;
    }

    public void setJob_start_time(Calendar job_start_time) {
        this.job_start_time = job_start_time;
    }

    public Calendar getJob_end_time() {
        return job_end_time;
    }

    public void setJob_end_time(Calendar job_end_time) {
        this.job_end_time = job_end_time;
    }
    
    public boolean isBegin_timing_job() {
        return begin_timing_job;
    }

    public void setBegin_timing_job(boolean begin_timing_job) {
        this.begin_timing_job = begin_timing_job;
    }
    
    
}
