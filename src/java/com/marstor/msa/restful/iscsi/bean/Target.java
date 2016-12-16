/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.iscsi.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class Target implements Serializable {

    private String target_name;
    private String alias_name;
    private List<String> initiator_name = new ArrayList<String>(); //当前连接的Initiator
//    public int auth_type = -1; 
    private String chap_state = "off";  //是否开启chap认证，on开启，off关闭,对应int auth_type = -1（-1时为关闭，非-1为开启）
    private String chap_user = "";
    private String chap_secret = "";
    private List<String> bind_ip = new ArrayList<String>();

    public Target() {
    }

    public String getTarget_name() {
        return target_name;
    }

    public void setTarget_name(String target_name) {
        this.target_name = target_name;
    }

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }

    public List<String> getInitiator_name() {
        return initiator_name;
    }

    public void setInitiator_name(List<String> initiator_name) {
        this.initiator_name = initiator_name;
    }

    public String getChap_state() {
        return chap_state;
    }

    public void setChap_state(String chap_state) {
        this.chap_state = chap_state;
    }

    public List<String> getBind_ip() {
        return bind_ip;
    }

    public void setBind_ip(List<String> bind_ip) {
        this.bind_ip = bind_ip;
    }

    public String getChap_user() {
        return chap_user;
    }

    public void setChap_user(String chap_user) {
        this.chap_user = chap_user;
    }

    public String getChap_secret() {
        return chap_secret;
    }

    public void setChap_secret(String chap_secret) {
        this.chap_secret = chap_secret;
    }

}
