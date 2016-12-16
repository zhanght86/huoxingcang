/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.common.bean;

/**
 *
 * @author Administrator
 */
public class LoginParam {
    private String user_name;
    private String user_pwd;
    private int option;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }
    
}
