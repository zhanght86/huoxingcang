/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.common.bean;

import com.marstor.msa.parameter.EmailParameter;

/**
 *
 * @author Li Mengyang <li.mengyang@marstor.com>
 */
public class Email {
    private boolean enable_email;
    private String[] receiver_addresses;
    private String sender_address;
    private String username;
    private String password;
    private String stmp;
    private String stmp_port;
    private boolean authorization_required;

    public Email(EmailParameter data) {
        this.enable_email = data.enableEmail;
        this.receiver_addresses = data.toAddress;
        this.sender_address = data.fromAddress;
        this.username = data.userName;
        this.password = data.password;
        this.stmp = data.stmp;
        this.stmp_port = data.stmpPort;
        this.authorization_required = data.bNeedAuthorize;
    }   

    public boolean isEnable_email() {
        return enable_email;
    }

    public void setEnable_email(boolean enable_email) {
        this.enable_email = enable_email;
    }

    public String[] getReceiver_addresses() {
        return receiver_addresses;
    }

    public void setReceiver_addresses(String[] receiver_addresses) {
        this.receiver_addresses = receiver_addresses;
    }

    public String getSender_address() {
        return sender_address;
    }

    public void setSender_address(String sender_address) {
        this.sender_address = sender_address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStmp() {
        return stmp;
    }

    public void setStmp(String stmp) {
        this.stmp = stmp;
    }

    public String getStmp_port() {
        return stmp_port;
    }

    public void setStmp_port(String stmp_port) {
        this.stmp_port = stmp_port;
    }

    public boolean isAuthorization_required() {
        return authorization_required;
    }

    public void setAuthorization_required(boolean authorization_required) {
        this.authorization_required = authorization_required;
    }

    
    
}
