/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class RemoteInitiator implements Serializable {

    private String name;
    private String style;
    private Chap chap;
    private String username = "";
    private String password = "";
    private boolean startCHAPOrNot;
    private boolean isFC;
    private boolean connectedOrNot;
    private boolean belongGroupOrNot;
    private boolean belongAvaliableOrNot;
    private ArrayList<String> attributeGroupNames = new ArrayList<String>();
    private String  groupName ="" ;

    public RemoteInitiator() {
    }

    public RemoteInitiator(String name, Chap chap, boolean connectedOrNot) {
        this.name = name;
        this.style = "iSCSI";
        this.chap = chap;
        this.isFC = false;
        this.connectedOrNot = connectedOrNot;
    }

    public RemoteInitiator(String name, boolean connectedOrNot) {
        this.name = name;
//        this.style = "FC";
//        this.isFC = true;
        this.connectedOrNot = connectedOrNot;
    }

    public RemoteInitiator(String name, String username, String password, boolean connectedOrNot) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.connectedOrNot = connectedOrNot;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Chap getChap() {
        return chap;
    }

    public void setChap(Chap chap) {
        this.chap = chap;
    }

    public boolean isIsFC() {
        return isFC;
    }

    public void setIsFC(boolean isFC) {
        this.isFC = isFC;
    }

    public boolean isConnectedOrNot() {
        return connectedOrNot;
    }

    public void setConnectedOrNot(boolean connectedOrNot) {
        this.connectedOrNot = connectedOrNot;
    }

    public boolean isBelongGroupOrNot() {
        return belongGroupOrNot;
    }

    public void setBelongGroupOrNot(boolean belongGroupOrNot) {
        this.belongGroupOrNot = belongGroupOrNot;
    }

    public boolean isBelongAvaliableOrNot() {
        return belongAvaliableOrNot;
    }

    public void setBelongAvaliableOrNot(boolean belongAvaliableOrNot) {
        this.belongAvaliableOrNot = belongAvaliableOrNot;
    }

    public ArrayList<String> getAttributeGroupNames() {
        return attributeGroupNames;
    }

    public void setAttributeGroupNames(ArrayList<String> attributeGroupNames) {
        this.attributeGroupNames = attributeGroupNames;
    }

    public void addAttributeGroupName(String name) {
        this.attributeGroupNames.add(name);
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

    public boolean isStartCHAPOrNot() {
//        if (this.username != null) {
            if (this.username.equals("<none>")) {
                return false;
            } else {
                return true;
            }
//        }
//        return false;
        //   return startCHAPOrNot;
    }

    public void setStartCHAPOrNot(boolean startCHAPOrNot) {
        this.startCHAPOrNot = startCHAPOrNot;
    }
}
