/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Administrator
 */
public class LogInfo implements Serializable {

    private String type;
    private String time;
    private String module;
    private String user;
    private String detail;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubDetail() {
        return subDetail;
    }

    public void setSubDetail(String subDetail) {
        this.subDetail = subDetail;
    }
    private String subDetail;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
