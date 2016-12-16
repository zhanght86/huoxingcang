/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

/**
 *
 * @author Administrator
 */
public class iSCSIGlobalInitiator {

    private String name;
    private String alias;
    private Chap chap;
    private String authType;
    private String chapName;
    private boolean startCHAPOrNot;

    public iSCSIGlobalInitiator() {
    }

    public iSCSIGlobalInitiator(String name, String alias, Chap chap) {
        this.name = name;
        this.alias = alias;
        this.chap = chap;
    }

    public iSCSIGlobalInitiator(String name, String alias, String authType, String chapName) {
        this.name = name;
        this.alias = alias;
        this.authType = authType;
        this.chapName = chapName;
        if (authType.equals("NONE")) {
            this.startCHAPOrNot = false;
        } else {
            this.startCHAPOrNot = true;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Chap getChap() {
        return chap;
    }

    public void setChap(Chap chap) {
        this.chap = chap;
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

    public boolean isStartCHAPOrNot() {
        return startCHAPOrNot;
    }

    public void setStartCHAPOrNot(boolean startCHAPOrNot) {
        this.startCHAPOrNot = startCHAPOrNot;
    }
}
