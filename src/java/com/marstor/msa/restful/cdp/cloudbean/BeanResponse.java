/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.cloudbean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zeroz
 */

@XmlRootElement
public class BeanResponse {
    
    protected int code = 0;
    protected String message_cn = "";
    protected String message_en = "";
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage_cn() {
        return message_cn;
    }

    public void setMessage_cn(String message_cn) {
        this.message_cn = message_cn;
    }

    public String getMessage_en() {
        return message_en;
    }

    public void setMessage_en(String message_en) {
        this.message_en = message_en;
    }
    
    
    
}
