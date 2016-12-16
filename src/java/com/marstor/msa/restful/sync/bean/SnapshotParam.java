/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.sync.bean;

/**
 *
 * @author zeroz
 */
public class SnapshotParam {
    
    private String file_system;
    private String snapshot_name;
    private String expiration_date; // 截止时间，保存时间

    public SnapshotParam() {
    }

    public SnapshotParam(String file_system, String snapshot_name, String expiration_date) {
        this.file_system = file_system;
        this.snapshot_name = snapshot_name;
        this.expiration_date = expiration_date;
    }

    public String getFile_system() {
        return file_system;
    }

    public void setFile_system(String file_system) {
        this.file_system = file_system;
    }

    public String getSnapshot_name() {
        return snapshot_name;
    }

    public void setSnapshot_name(String snapshot_name) {
        this.snapshot_name = snapshot_name;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }
    
    
    
}
