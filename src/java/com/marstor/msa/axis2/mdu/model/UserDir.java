/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.mdu.model;

/**
 *
 * @author Administrator
 */
public class UserDir {
    public static final int TYPE_USER = 1;
    public static final int TYPE_GROUP = 2;
    public static final int TYPE_SHARED = 3;
    
    private int type;
    private String owner;
    private String fullPath;
    private String userDir;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getUserDir() {
        return userDir;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }
    
    
}
