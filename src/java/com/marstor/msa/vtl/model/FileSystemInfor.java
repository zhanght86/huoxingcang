/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.model;

import com.marstor.msa.common.bean.FileSystemInformation;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class FileSystemInfor extends FileSystemInformation  implements Serializable{

    public boolean cantuse;
    public boolean canton_off;
    public boolean cantset;
    public boolean onlineDisabled;

    public boolean isCantuse() {
        return cantuse;
    }

    public void setCantuse(boolean cantuse) {
        this.cantuse = cantuse;
    }

    public boolean isCanton_off() {
        return canton_off;
    }

    public void setCanton_off(boolean canton_off) {
        this.canton_off = canton_off;
    }

    public boolean isCantset() {
        return cantset;
    }

    public void setCantset(boolean cantset) {
        this.cantset = cantset;
    }

    public boolean isOnlineDisabled() {
        return onlineDisabled;
    }

    public void setOnlineDisabled(boolean onlineDisabled) {
        this.onlineDisabled = onlineDisabled;
    }
    
}
