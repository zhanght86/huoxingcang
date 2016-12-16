/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.model;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class HostBean {

    private String strHost;
    private String strOSName;
    private int iClienthostid;
    private int iAgentid;
    private List<String> listAgent;
    
    public String getStrOSName() {
        return strOSName;
    }

    public void setStrOSName(String strOSName) {
        this.strOSName = strOSName;
    }
    public int getiAgentid() {
        return iAgentid;
    }

    public void setiAgentid(int iAgentid) {
        this.iAgentid = iAgentid;
    }

    public int getiClienthostid() {
        return iClienthostid;
    }

    public void setiClienthostid(int iClienthostid) {
        this.iClienthostid = iClienthostid;
    }

    public List<String> getListAgent() {
        return listAgent;
    }

    public void setListAgent(List<String> listAgent) {
        this.listAgent = listAgent;
    }

    public String getStrHost() {
        return strHost;
    }

    public void setStrHost(String strHost) {
        this.strHost = strHost;
    }


}
