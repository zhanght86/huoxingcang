/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class SnapshotRes {
    private CdpSnapshot[] snapshots;

    public SnapshotRes() {
    }
    
    

    public SnapshotRes(CdpSnapshot[] snapshots) {
        this.snapshots = snapshots;
    }

    public CdpSnapshot[] getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(CdpSnapshot[] snapshots) {
        this.snapshots = snapshots;
    }
    
}
