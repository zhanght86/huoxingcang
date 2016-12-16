/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.disk.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class DiskRootInfo implements Serializable{
    public String name;
    public String num;
    public String szie;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSzie() {
        return szie;
    }

    public void setSzie(String szie) {
        this.szie = szie;
    }
    
    
}
