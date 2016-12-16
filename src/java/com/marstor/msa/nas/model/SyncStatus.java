/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class SyncStatus implements Serializable{

    private boolean existSnapOrNot;
    private boolean openAutoSnapOrNot;
    private boolean syncSourceOrNot;
    private boolean syncTargetOrNot;
    private boolean onLineOrNot;
    private boolean suspendSyncOrNot;
    private boolean rollBackOrNot;
    private boolean fileSysExistOrNot;

    public SyncStatus() {
    }

    public boolean isFileSysExistOrNot() {
        return fileSysExistOrNot;
    }

    public void setFileSysExistOrNot(boolean fileSysExistOrNot) {
        this.fileSysExistOrNot = fileSysExistOrNot;
    }

    public boolean isExistSnapOrNot() {
        return existSnapOrNot;
    }

    public void setExistSnapOrNot(boolean existSnapOrNot) {
        this.existSnapOrNot = existSnapOrNot;
    }

    public boolean isOpenAutoSnapOrNot() {
        return openAutoSnapOrNot;
    }

    public void setOpenAutoSnapOrNot(boolean openAutoSnapOrNot) {
        this.openAutoSnapOrNot = openAutoSnapOrNot;
    }

    public boolean isSyncSourceOrNot() {
        return syncSourceOrNot;
    }

    public void setSyncSourceOrNot(boolean syncSourceOrNot) {
        this.syncSourceOrNot = syncSourceOrNot;
    }

    public boolean isSyncTargetOrNot() {
        return syncTargetOrNot;
    }

    public void setSyncTargetOrNot(boolean syncTargetOrNot) {
        this.syncTargetOrNot = syncTargetOrNot;
    }

    public boolean isOnLineOrNot() {
        return onLineOrNot;
    }

    public void setOnLineOrNot(boolean onLineOrNot) {
        this.onLineOrNot = onLineOrNot;
    }

    public boolean isSuspendSyncOrNot() {
        return suspendSyncOrNot;
    }

    public void setSuspendSyncOrNot(boolean suspendSyncOrNot) {
        this.suspendSyncOrNot = suspendSyncOrNot;
    }

    public boolean isRollBackOrNot() {
        return rollBackOrNot;
    }

    public void setRollBackOrNot(boolean rollBackOrNot) {
        this.rollBackOrNot = rollBackOrNot;
    }

    public static double convertSizeToByte(String strSize) {
        if (strSize == null || strSize.equals("")) {
            return 0;
        }
        int iLength = strSize.length();
        String strNumber;
        char c;
        if (iLength > 1) {
            c = strSize.charAt(iLength - 1);
            strNumber = strSize.substring(0, iLength - 1);
        } else {
            c = 'a';
            strNumber = strSize;
        }
        if (true) {
            switch (c) {
                case 'T':
                    return Double.parseDouble(strNumber) * 1024 * 1024 * 1024 * 1024;
                case 'G':
                    return Double.parseDouble(strNumber) * 1024 * 1024 * 1024;
                case 'M':
                    return Double.parseDouble(strNumber) * 1024 * 1024;
                case 'K':
                    return Double.parseDouble(strNumber) * 1024;
                case 'B':
                    return Double.parseDouble(strNumber);
                default:
                    return Double.parseDouble(strNumber);
            }
        }
        return 0;
    }
}
