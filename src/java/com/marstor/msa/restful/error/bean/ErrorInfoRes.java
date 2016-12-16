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
public class ErrorInfoRes {
    
    private ErrorInfo error_info;

    public ErrorInfoRes() {
    }

    public ErrorInfoRes(ErrorInfo error_info) {
        this.error_info = error_info;
    }

    public ErrorInfo getError_info() {
        return error_info;
    }

    public void setError_info(ErrorInfo error_info) {
        this.error_info = error_info;
    }
    
}
