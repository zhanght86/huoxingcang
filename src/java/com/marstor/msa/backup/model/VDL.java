/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.model;

import java.io.File;

/**
 *
 * @author Administrator
 */
public class VDL {

    private String poolName;
    private File[] files;
    private String used;
    private String available;
    private String path;

    public VDL() {
    }

    public VDL(String used, String available,String path,String pool) {
        this.used = used;
        this.available = available;
        this.path = path;
        this.poolName = pool;
    }
    
    public File[] getFiles() {
        return files;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
}
