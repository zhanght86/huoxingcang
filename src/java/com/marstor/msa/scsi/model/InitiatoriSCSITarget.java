/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

import com.marstor.msa.common.bean.InitiatorTargetConnection;
import com.marstor.msa.common.bean.InitiatorTargetDevice;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class InitiatoriSCSITarget {

    private String targetName;
    private String targetAlias;
    private int  connectionCount;
    private Chap  chap;
    private String authType ;
    private String chapName ;
    //private ArrayList<InitiatoriSCSITargetDevice> devices = new ArrayList<InitiatoriSCSITargetDevice>();
   // private ArrayList<InitiatoriSCSITargetConnection> connections = new ArrayList<InitiatoriSCSITargetConnection>();
    private ArrayList<InitiatoriSCSITargetSummary> summary = new ArrayList<InitiatoriSCSITargetSummary>();
    private ArrayList<InitiatorTargetDevice> devices = new ArrayList<InitiatorTargetDevice>();
    private ArrayList<InitiatorTargetConnection> connections = new ArrayList<InitiatorTargetConnection>();
    public InitiatoriSCSITarget(String name, String alias, int connectNum, Chap chap) {
        this.targetName = name;
        this.targetAlias = alias;
        this.connectionCount = connectNum;
        this.chap = chap;
    }

    public InitiatoriSCSITarget(String targetName, String targetAlias, int connectionCount, String authType, String chapName) {
        this.targetName = targetName;
        this.targetAlias = targetAlias;
        this.connectionCount = connectionCount;
        this.authType = authType;
        this.chapName = chapName;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getChapName() {
        return chapName;
    }

    public void setChapName(String chapName) {
        this.chapName = chapName;
    }

    public ArrayList<InitiatoriSCSITargetSummary> getSummary() {
        return summary;
    }

    public void setSummary(ArrayList<InitiatoriSCSITargetSummary> summary) {
        this.summary = summary;
    }

    public InitiatoriSCSITarget(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetAlias() {
        return targetAlias;
    }

    public void setTargetAlias(String targetAlias) {
        this.targetAlias = targetAlias;
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }

    public Chap getChap() {
        return chap;
    }

    public void setChap(Chap chap) {
        this.chap = chap;
    }

    public ArrayList<InitiatorTargetDevice> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<InitiatorTargetDevice> devices) {
        this.devices = devices;
    }

    //
    //    public ArrayList<InitiatoriSCSITargetDevice> getDevices() {
    //        return devices;
    //    }
    //
    //    public void setDevices(ArrayList<InitiatoriSCSITargetDevice> devices) {
    //    }
    //    }
    //    public ArrayList<InitiatoriSCSITargetConnection> getConnections() {
    //        return connections;
    //    }
    //
    //    public void setConnections(ArrayList<InitiatoriSCSITargetConnection> connections) {
    //        this.connections = connections;
    //    }
    public ArrayList<InitiatorTargetConnection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<InitiatorTargetConnection> connections) {
        this.connections = connections;
    }
    
}
