package com.marstor.msa.axis2.cdp.model;

import java.io.Serializable;

public class CdpLogPolicy implements Serializable{

    private long LogSize;
    private int LogKeepTime;     //hours
    private int LogFullOption;   //1: auto loop;  2: stop CDP

    public CdpLogPolicy() {
    }

    public CdpLogPolicy(long LogSize, int LogKeepTime, int LogFullOption) {
        this.LogSize = LogSize;
        this.LogKeepTime = LogKeepTime;
        this.LogFullOption = LogFullOption;
    }

    public long getLogSize() {
        return LogSize;
    }

    public void setLogSize(long LogSize) {
        this.LogSize = LogSize;
    }

    public int getLogKeepTime() {
        return LogKeepTime;
    }

    public void setLogKeepTime(int LogKeepTime) {
        this.LogKeepTime = LogKeepTime;
    }

    public int getLogFullOption() {
        return LogFullOption;
    }

    public void setLogFullOption(int LogFullOption) {
        this.LogFullOption = LogFullOption;
    }
    
}
