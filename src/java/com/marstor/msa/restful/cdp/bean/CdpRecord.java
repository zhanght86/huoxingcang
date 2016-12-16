/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import com.marstor.msa.cdp.bean.CdpLogRecord;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class CdpRecord {
    private Date log_time;
    private long disk_id;
    private long log_offset;
    private long log_size;

    public CdpRecord() {
        
    }
    
    public CdpRecord(CdpLogRecord param) {
        this.log_time = param.getLogTime();
        this.disk_id = param.getDiskID();
        this.log_offset = param.getLogOffset();
        this.log_size = param.getLogSize();
    }    

    public Date getLog_time() {
        return log_time;
    }

    public void setLog_time(Date log_time) {
        this.log_time = log_time;
    }

    public long getDisk_id() {
        return disk_id;
    }

    public void setDisk_id(long disk_id) {
        this.disk_id = disk_id;
    }

    public long getLog_offset() {
        return log_offset;
    }

    public void setLog_offset(long log_offset) {
        this.log_offset = log_offset;
    }

    public long getLog_size() {
        return log_size;
    }

    public void setLog_size(long log_size) {
        this.log_size = log_size;
    }

    
}
