/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.sync.bean;

public class SyncMappingParam {
    private String src_file_system;
    private int hash_code; //ÈÎÎñÂí£¬iDescFileSystemHashCode
    private boolean local;

    public String getSrc_file_system() {
        return src_file_system;
    }

    public void setSrc_file_system(String src_file_system) {
        this.src_file_system = src_file_system;
    }

    public int getHash_code() {
        return hash_code;
    }

    public void setHash_code(int hash_code) {
        this.hash_code = hash_code;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
    
    
    
}
