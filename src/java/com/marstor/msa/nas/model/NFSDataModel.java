/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class NFSDataModel implements Serializable{
    private ArrayList<NFS>  nfsData = new ArrayList<NFS>();

    public NFSDataModel() {
    }

    public ArrayList<NFS> getNfsData() {
        return nfsData;
    }

    public void setNfsData(ArrayList<NFS> nfsData) {
        this.nfsData = nfsData;
    }
    public void addNfsData(NFS  nfs) {
        this.nfsData.add(nfs);
    }
    public NFS  getNFSByPath(String path) {
        for(NFS  nfs : this.nfsData) {
            if(nfs.getPath().equals(path)) {
                return nfs;
            }
        }
        return null;
    }
    public void  updateNFS(NFS configNFS) {
        NFS  nfs = this.getNFSByPath(configNFS.getPath());
        nfs.setStatus(configNFS.isStatus());
        nfs.setRw(configNFS.getRw());
        nfs.setReadOnly(configNFS.getReadOnly());
        nfs.setRoot(configNFS.getRoot());
    }
    
}
