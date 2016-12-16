
package com.marstor.msa.axis2.cdp.model;

import java.io.Serializable;


public class CdpAgent implements Serializable{
    
    private String strDiskGroupGuid;
    private long lSnapshotInterval;
    private int iStatus;
    private DataBase[] listDB;

    public CdpAgent() {
    }

    public CdpAgent(String strDiskGroupGuid, long lSnapshotInterval, int iStatus, DataBase[] listDB) {
        this.strDiskGroupGuid = strDiskGroupGuid;
        this.lSnapshotInterval = lSnapshotInterval;
        this.iStatus = iStatus;
        this.listDB = listDB;
    }

    public String getStrDiskGroupGuid() {
        return strDiskGroupGuid;
    }

    public void setStrDiskGroupGuid(String strDiskGroupGuid) {
        this.strDiskGroupGuid = strDiskGroupGuid;
    }

    public long getlSnapshotInterval() {
        return lSnapshotInterval;
    }

    public void setlSnapshotInterval(long lSnapshotInterval) {
        this.lSnapshotInterval = lSnapshotInterval;
    }

    public int getiStatus() {
        return iStatus;
    }

    public void setiStatus(int iStatus) {
        this.iStatus = iStatus;
    }

    public DataBase[] getListDB() {
        return listDB;
    }

    public void setListDB(DataBase[] listDB) {
        this.listDB = listDB;
    }

 


}
