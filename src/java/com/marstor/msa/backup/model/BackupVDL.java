/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class BackupVDL implements Serializable{
    private String name;
    private List<File> fileList = new ArrayList<File>();
    
    public BackupVDL(){}
    
    public void setFileList(List<File> list){
        this.fileList = list;
    }
    
    public List<File> getFileList(){
        return fileList;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
}
