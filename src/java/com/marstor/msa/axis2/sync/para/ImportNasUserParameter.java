/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.sync.para;

import com.marstor.msa.sync.bean.NasBean;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class ImportNasUserParameter implements Serializable{
    
    private NasBean nasBean; 
    private String ip;
    private String port;
    private String user;
    private String pwd;

    public ImportNasUserParameter() {
    }

    public NasBean getNasBean() {
        return nasBean;
    }

    public void setNasBean(NasBean nasBean) {
        this.nasBean = nasBean;
    }

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
}
