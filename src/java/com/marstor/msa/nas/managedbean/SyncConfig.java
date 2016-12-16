/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class SyncConfig implements Serializable{
     private String selectType="";
     private boolean  localRendered,remoteRendered;
     
    public SyncConfig() {
//        this.selectType = "2";
//        if(this.selectType.equals("1")) {
//            this.localRendered = true;
//            this.remoteRendered = false;
//        }else {
//            this.localRendered = false;
//            this.remoteRendered = true;
//        }
        this.selectType="1";
        this.localRendered = true;
        this.remoteRendered =false;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public boolean isLocalRendered() {
        return localRendered;
    }

    public void setLocalRendered(boolean localRendered) {
        this.localRendered = localRendered;
    }

    public boolean isRemoteRendered() {
        return remoteRendered;
    }

    public void setRemoteRendered(boolean remoteRendered) {
        this.remoteRendered = remoteRendered;
    }
    public void listen() {
        if(this.selectType==null) {
            return ;
        }
        if(this.selectType.equals("1")) {
            this.localRendered = true;
            this.remoteRendered = false;
        }else {
            this.localRendered = false;
            this.remoteRendered = true;
        }
    } 
}
