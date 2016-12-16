/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.error.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class ErrorInfo {
    
    private int http_code;
    private int error_code;
    private String error_message;

    public ErrorInfo() {
    }

    public ErrorInfo(int http_code, int error_code, String error_message) {
        this.http_code = http_code;
        this.error_code = error_code;
        this.error_message = error_message;
    }

    public int getHttp_code() {
        return http_code;
    }

    public void setHttp_code(int http_code) {
        this.http_code = http_code;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
    
}
