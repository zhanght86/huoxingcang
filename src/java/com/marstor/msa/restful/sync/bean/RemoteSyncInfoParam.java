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
public class RemoteSyncInfoParam {
    private String ip;
    private String port;
    private String username;
    private String password;
    private String root_password;
    private String gzip_level;  //²ÎÊý 6,9,10
    private String src_file_system;
    private String desc_file_system;
    private String ssh_port;
    private String transfer_port;
    private Calendar job_start_time;
    private Calendar job_end_time;
    private boolean begin_timing_job;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoot_password() {
        return root_password;
    }

    public void setRoot_password(String root_password) {
        this.root_password = root_password;
    }

    public String getGzip_level() {
        return gzip_level;
    }

    public void setGzip_level(String gzip_level) {
        this.gzip_level = gzip_level;
    }

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

    public String getSsh_port() {
        return ssh_port;
    }

    public void setSsh_port(String ssh_port) {
        this.ssh_port = ssh_port;
    }

    public String getTransfer_port() {
        return transfer_port;
    }

    public void setTransfer_port(String transfer_port) {
        this.transfer_port = transfer_port;
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
