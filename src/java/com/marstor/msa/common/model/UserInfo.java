/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import java.io.Serializable;
//
//@ManagedBean(name = "userInfo")
//@RequestScoped
public class UserInfo implements Serializable{

    private String userName;
    private String userType;
    private String userTypeStr;
    private String userPassword;
    private String userSecPassword;
    public boolean notdelete;
    public boolean notmodify;
    public boolean notadd;
    /**
     * Creates a new instance of User
     */
    public UserInfo() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserTypeStr() {
        return userTypeStr;
    }

    public void setUserTypeStr(String userTypeStr) {
        this.userTypeStr = userTypeStr;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isNotdelete() {
        return notdelete;
    }

    public void setNotdelete(boolean notdelete) {
        this.notdelete = notdelete;
    }

    public boolean isNotmodify() {
        return notmodify;
    }

    public void setNotmodify(boolean notmodify) {
        this.notmodify = notmodify;
    }

    public boolean isNotadd() {
        return notadd;
    }

    public void setNotadd(boolean notadd) {
        this.notadd = notadd;
    }

    public String getUserSecPassword() {
        return userSecPassword;
    }

    public void setUserSecPassword(String userSecPassword) {
        this.userSecPassword = userSecPassword;
    }
    
}
