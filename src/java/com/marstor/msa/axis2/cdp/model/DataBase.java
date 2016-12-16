/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.cdp.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class DataBase implements Serializable{
    
    private String ip;
    private int port;
    private String dbInstance;
    private int agentType;

    public DataBase() {
    }

    public DataBase(String ip, int port, String dbInstance, int agentType) {
        this.ip = ip;
        this.port = port;
        this.dbInstance = dbInstance;
        this.agentType = agentType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDbInstance() {
        return dbInstance;
    }

    public void setDbInstance(String dbInstance) {
        this.dbInstance = dbInstance;
    }

    public int getAgentType() {
        return agentType;
    }

    public void setAgentType(int agentType) {
        this.agentType = agentType;
    }

   
}
