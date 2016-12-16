/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import com.marstor.msa.cdp.bean.CdpLogPolicy;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class LogPolicy {
    private long log_size;
    private int log_retention_time;
    private int log_after_full_strategy;

    public LogPolicy() {
    }

    public LogPolicy(CdpLogPolicy origin) {
        this.log_size = origin.getLogSize();
        this.log_retention_time = origin.getLogKeepTime();
        this.log_after_full_strategy = origin.getLogFullOption();
    }

    public long getLog_size() {
        return log_size;
    }

    public void setLog_size(long log_size) {
        this.log_size = log_size;
    }

    public int getLog_retention_time() {
        return log_retention_time;
    }

    public void setLog_retention_time(int log_retention_time) {
        this.log_retention_time = log_retention_time;
    }

    public int getLog_after_full_strategy() {
        return log_after_full_strategy;
    }

    public void setLog_after_full_strategy(int log_after_full_strategy) {
        this.log_after_full_strategy = log_after_full_strategy;
    }


    
    
}
