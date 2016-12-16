/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.cloudbean;

/**
 *
 * @author zeroz
 */
public class GroupResponse extends BeanResponse {
    
    private String name;
    private String uuid;
    private String zfsname;
    private int count;
    private int isprotect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getZfsname() {
        return zfsname;
    }

    public void setZfsname(String zfsname) {
        this.zfsname = zfsname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIsprotect() {
        return isprotect;
    }

    public void setIsprotect(int isprotect) {
        this.isprotect = isprotect;
    }
    
    
    
    
    
    
}
