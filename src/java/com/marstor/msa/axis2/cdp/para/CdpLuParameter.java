/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.cdp.para;

/**
 *
 * @author Administrator
 */
public class CdpLuParameter {
    
    private String diskGroupName;
    private String[] luInfos;

    public String getDiskGroupName() {
        return diskGroupName;
    }

    public void setDiskGroupName(String diskGroupName) {
        this.diskGroupName = diskGroupName;
    }

    public String[] getLuInfos() {
        return luInfos;
    }

    public void setLuInfos(String[] luInfos) {
        this.luInfos = luInfos;
    }
    
}
