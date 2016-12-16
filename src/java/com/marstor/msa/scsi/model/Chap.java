/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

/**
 *
 * @author Administrator
 */
public class Chap {
    
    private boolean  isStart=false;
    private String  uName;
    private String  uPasswd;
    private boolean authenInfo;
    
    public Chap(boolean  isStart,String uName, String uPasswd) {
        this.isStart = isStart;
        this.uName = uName;
        this.uPasswd = uPasswd;
    }

    public Chap(String uName, String uPasswd) {
        this.uName = uName;
        this.uPasswd = uPasswd;
        this.isStart = true;
    }

    public Chap() {
    }

    public Chap(boolean  isStart,String uName, String uPasswd, boolean authenInfo) {
        this.isStart = isStart;
        this.uName = uName;
        this.uPasswd = uPasswd;
        this.authenInfo = authenInfo;
    }
    

    public boolean isIsStart() {
        return isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPasswd() {
        return uPasswd;
    }

    public void setuPasswd(String uPasswd) {
        this.uPasswd = uPasswd;
    }

    public boolean isAuthenInfo() {
        return authenInfo;
    }

    public void setAuthenInfo(boolean authenInfo) {
        this.authenInfo = authenInfo;
    }
    
}
