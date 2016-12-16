/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.model;

import com.marstor.msa.backup.model.*;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class CDPAgent implements Comparable , Serializable{
    private int serial;
    private String strAgentName;
    private String strAgentVersion;
    private String strAgentDate;
    private String strAgentModifyDate;
    private String strAgentOS;
    private String strAgentPlatform;
    private String strDescription;

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public void setSerial(int s){
        this.serial = s;
    }
    
    public int getSerial(){
        return serial;
    }
    
    public String getStrAgentDate() {
        return strAgentDate;
    }

    public void setStrAgentDate(String strAgentDate) {
        this.strAgentDate = strAgentDate;
    }

    public String getStrAgentName() {
        return strAgentName;
    }

    public void setStrAgentName(String strAgentName) {
        this.strAgentName = strAgentName;
    }

    public String getStrAgentVersion() {
        return strAgentVersion;
    }

    public void setStrAgentVersion(String strAgentVersion) {
        this.strAgentVersion = strAgentVersion;
    }

    public String getStrAgentModifyDate() {
        return strAgentModifyDate;
    }

    public void setStrAgentModifyDate(String strAgentModifyDate) {
        this.strAgentModifyDate = strAgentModifyDate;
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

    public int compareTo(Object o) {
        String str1 = this.getStrAgentOS();
        String str2 = ((ClientAgent) o).getStrAgentOS();
        return str1.compareTo(str2);
    }
}
