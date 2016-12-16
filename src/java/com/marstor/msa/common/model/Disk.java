/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.scsi.util.Debug;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class Disk implements Serializable{

    private String stateIcon="";
    private String tempIcon="";
    //private boolean isHiddenTemp;//是否在界面上隐藏此磁盘的温度图片
    private String   tempDisplay=""; //是否在界面上显示温度图片,如果设置为none,则不显示；如果设置为block,则显示

    public Disk() {
    }

    public Disk(int state, int temp, boolean isExistDisk) {
        Debug.print("Disk Constructor: state===============================" + state);
        if (!isExistDisk) { //如果槽位上无盘
            this.stateIcon = DiskStatus.trayRed;
            this.tempDisplay = "none";
        } else {
            this.tempDisplay = "block";
            if (state > 6) { //磁盘损坏
                this.stateIcon = DiskStatus.trayBlueRed;
            } else if (state <= 6) {//磁盘正常
                this.stateIcon = DiskStatus.trayBlueGreen;
            }
            if (temp < 0 || temp>100) {
                this.tempIcon = DiskStatus.tempMap.get(1);
            } else if (temp > 60&&temp<=100) {
                this.tempIcon = DiskStatus.tempMap.get(60);
            } else {
                this.tempIcon = DiskStatus.tempMap.get(temp);
            }
        }


    }

    //
    //    public boolean isIsHiddenTemp() {
    //        return isHiddenTemp;
    //    }
    //
    //    public void setIsHiddenTemp(boolean isHiddenTemp) {
    //    }
    //    }
    public String getTempDisplay() {
        return tempDisplay;
    }

    public void setTempDisplay(String tempDisplay) {
        this.tempDisplay = tempDisplay;
    }

    public String getStateIcon() {
        return stateIcon;
    }

    public void setStateIcon(String stateIcon) {
        this.stateIcon = stateIcon;
    }

    public String getTempIcon() {
        return tempIcon;
    }

    public void setTempIcon(String tempIcon) {
        this.tempIcon = tempIcon;
    }
}
