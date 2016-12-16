/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
//@ManagedBean(name = "disk")
//@RequestScoped
public class Disk implements Serializable{
      
    public String position;
    public String num;
    public String size;
    public String type;
    public String state;
    public String diskName;
    public boolean notApart;  //分离
    public boolean notReplace;  //替换
    public boolean notRemove;  //清除
    public String poolName;  //所在卷组
    public boolean isUninstall_rendered;  //是否卸载
    public boolean isUninstall_disabled;  //是否卸载
    public String strType;
    public String stateIcon;
    

    
    /**
     * Creates a new instance of disk
     */
    public Disk() {
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public boolean isNotApart() {
        return notApart;
    }

    public void setNotApart(boolean notApart) {
        this.notApart = notApart;
    }

    public boolean isNotReplace() {
        return notReplace;
    }

    public void setNotReplace(boolean notReplace) {
        this.notReplace = notReplace;
    }

    public boolean isNotRemove() {
        return notRemove;
    }

    public void setNotRemove(boolean notRemove) {
        this.notRemove = notRemove;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public boolean isIsUninstall_rendered() {
        return isUninstall_rendered;
    }

    public void setIsUninstall_rendered(boolean isUninstall_rendered) {
        this.isUninstall_rendered = isUninstall_rendered;
    }

    public boolean isIsUninstall_disabled() {
        return isUninstall_disabled;
    }

    public void setIsUninstall_disabled(boolean isUninstall_disabled) {
        this.isUninstall_disabled = isUninstall_disabled;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public String getStateIcon() {
        return stateIcon;
    }

    public void setStateIcon(String stateIcon) {
        this.stateIcon = stateIcon;
    }

 

  
    
    
    
}
