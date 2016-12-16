/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.mdu.model;

/**
 *
 * @author Administrator
 */
public class ListFiles {
    String[] dirs;
    String[] files;

    public ListFiles(){
    }
    
    public String[] getDirs() {
        return dirs;
    }

    public void setDirs(String[] dirs) {
        this.dirs = dirs;
    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }
    
    
}
