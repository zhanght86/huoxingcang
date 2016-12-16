/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.sync.para;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class RollbackSnapshotParameter implements Serializable{
    
    private String srcFileSystem;
    private String lastSnapshot;
    private String rollbackSnapshot;
    private String[] snapshots;

    public RollbackSnapshotParameter() {
    }

    public String getSrcFileSystem() {
        return srcFileSystem;
    }

    public void setSrcFileSystem(String srcFileSystem) {
        this.srcFileSystem = srcFileSystem;
    }

    public String getLastSnapshot() {
        return lastSnapshot;
    }

    public void setLastSnapshot(String lastSnapshot) {
        this.lastSnapshot = lastSnapshot;
    }

    public String getRollbackSnapshot() {
        return rollbackSnapshot;
    }

    public void setRollbackSnapshot(String rollbackSnapshot) {
        this.rollbackSnapshot = rollbackSnapshot;
    }

    public String[] getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(String[] snapshots) {
        this.snapshots = snapshots;
    }
    
    
}
