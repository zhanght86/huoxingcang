/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.model;

/**
 *
 * @author Administrator
 */
public class DatabaseParameter {

    private String strServerName;
    private String strPort;
    private String strDBName;
    private String strUser;
    private String strPassword;

    public String getStrDBName() {
        return strDBName;
    }

    public void setStrDBName(String strDBName) {
        this.strDBName = strDBName;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public String getStrPort() {
        return strPort;
    }

    public void setStrPort(String strPort) {
        this.strPort = strPort;
    }

    public String getStrServerName() {
        return strServerName;
    }

    public void setStrServerName(String strServerName) {
        this.strServerName = strServerName;
    }

    public String getStrUser() {
        return strUser;
    }

    public void setStrUser(String strUser) {
        this.strUser = strUser;
    }
}
