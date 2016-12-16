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
public class SnapshotIDRes {
    private long snapshot_id;

    public SnapshotIDRes() {
    }
    
    

    public SnapshotIDRes(long snapshot_id) {
        this.snapshot_id = snapshot_id;
    }
    
    

    public long getSnapshot_id() {
        return snapshot_id;
    }

    public void setSnapshot_id(long snapshot_id) {
        this.snapshot_id = snapshot_id;
    }

    
}
