/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.vm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @introduction 获得每个操作系统
 * @author Administrator
 */
public class OSName implements Serializable{
    public String osName; //操作系统名称
    private List<OperatingSystem> osType = new ArrayList();//每个操作系统下对应的各个版本

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public List<OperatingSystem> getOsType() {
        return osType;
    }

    public void setOsType(List<OperatingSystem> osType) {
        this.osType = osType;
    }

}
