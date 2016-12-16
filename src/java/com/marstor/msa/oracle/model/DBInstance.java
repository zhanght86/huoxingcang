/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DBInstance implements Serializable {

    private String dbName = "";
    private String userName = "";
    private String password = "";
    private String netServiceName = "";
    private List<Channel> channelList = new ArrayList<Channel>();
    private Channel sChannel = null;
    private int channelNum = 0;

    public DBInstance() {
    }

    public DBInstance(String dbName, String user, String pass, List<Channel> list, String netServiceName) {
        this.dbName = dbName;
        this.userName = user;
        this.password = pass;
        this.channelList = list;
        this.channelNum = list.size();
        this.netServiceName = netServiceName;     
    }

    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

    public String getDBName() {
        return this.dbName;
    }

    public void setUser(String user) {
        this.userName = user;
    }

    public String getUser() {
        return this.userName;
    }
    
    public void setNetServiceName(String netServiceName){
        this.netServiceName = netServiceName;        
    }
    
    public String getNetServiceName(){
        return this.netServiceName;
    }

    public void setPass(String pass) {      
        this.password = pass;
    }

    public String getPass() {
        return this.password;
    }
    
    
    public void setList(List<Channel> list){
        this.channelList = list;
        this.channelNum = list.size();
    }
    
    public List<Channel> getList(){
        return channelList;
    }
    
    public int getNum(){
        return channelNum;
    }
    
    public void setSChannel(Channel channel){
        this.sChannel = channel;
    }
    
    public Channel getSChannel(){
        return this.sChannel;
    }
    
    public void deleteChannel(){
        this.channelList.remove(sChannel);
    }
}
