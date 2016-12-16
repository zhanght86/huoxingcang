/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.sync.model;

/**
 *
 * @author Administrator
 */
public class TargetHostFileSystem {
    
    String[] pools;
    String[] fileSystems;

    public String[] getPools() {
        return pools;
    }

    public void setPools(String[] pools) {
        this.pools = pools;
    }

    public String[] getFileSystems() {
        return fileSystems;
    }

    public void setFileSystems(String[] fileSystems) {
        this.fileSystems = fileSystems;
    }
    
}
