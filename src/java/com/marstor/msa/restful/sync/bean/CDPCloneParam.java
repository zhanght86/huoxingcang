/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.sync.bean;

import java.util.Date;

/**
 *
 * @author zeroz
 */
public class CDPCloneParam {
    private String snapshot_name;
    private Date date;

    public String getSnapshot_name() {
        return snapshot_name;
    }

    public void setSnapshot_name(String snapshot_name) {
        this.snapshot_name = snapshot_name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
    
    
}
