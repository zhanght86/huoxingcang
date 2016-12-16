/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @introduction 获得虚拟硬盘信息
 * @author Administrator
 */
@ManagedBean(name = "clearHDInfo")
@RequestScoped
public class ClearHDInfo implements Serializable{

    public String hdName;  //硬盘全路径名称
    public boolean usage;  //true 代表有人使用，false代表没用用 被哪个虚拟机使用了，虚拟机路径，如果为空代表该硬盘未被使用
    public String state;  //硬盘状态,inaccessible不可用
    
    /**
     * Creates a new instance of GetHDINFO
     */
    public ClearHDInfo() {
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public String getHdName() {
        return hdName;
    }

    public void setHdName(String hdName) {
        this.hdName = hdName;
    }

    public boolean isUsage() {
        return usage;
    }

    public void setUsage(boolean usage) {
        this.usage = usage;
    }



   

}
