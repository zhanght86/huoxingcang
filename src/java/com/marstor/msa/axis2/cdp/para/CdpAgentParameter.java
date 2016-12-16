/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.cdp.para;

import com.marstor.msa.axis2.cdp.model.DataBase;

/**
 *
 * @author Administrator
 */
public class CdpAgentParameter {
    
    private String diskGroupGuid;
    private Long snapshotInterval;
    private DataBase[] dataBases;

    public String getDiskGroupGuid() {
        return diskGroupGuid;
    }

    public void setDiskGroupGuid(String diskGroupGuid) {
        this.diskGroupGuid = diskGroupGuid;
    }

    public Long getSnapshotInterval() {
        return snapshotInterval;
    }

    public void setSnapshotInterval(Long snapshotInterval) {
        this.snapshotInterval = snapshotInterval;
    }

    public DataBase[] getDataBases() {
        return dataBases;
    }

    public void setDataBases(DataBase[] dataBases) {
        this.dataBases = dataBases;
    }
    
}
