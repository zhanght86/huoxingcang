/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.model;

/**
 *
 * @author Administrator
 */
public class MBAAgentPackageXML {
    
    private String strAgentName;
    private String strAgentOS;
    private String strAgentPlatform;
    private String strDescription;

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getStrAgentName() {
        return strAgentName;
    }

    public void setStrAgentName(String strAgentName) {
        this.strAgentName = strAgentName;
    }

    public String getStrAgentOS() {
        return strAgentOS;
    }

    public void setStrAgentOS(String strAgentOS) {
        this.strAgentOS = strAgentOS;
    }

    public String getStrAgentPlatform() {
        return strAgentPlatform;
    }

    public void setStrAgentPlatform(String strAgentPlatform) {
        this.strAgentPlatform = strAgentPlatform;
    }
      
}
