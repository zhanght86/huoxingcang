/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import com.marstor.msa.cdp.bean.CdpRollbackTaskInfo;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class CdpRollbackTask {

    private int task_status;
    private String error_message;
    private String disk_group_guid;
    private Date rollback_time;
    private Date start_time;
    private Date end_time;
    private long total_rollback_size;
    private long current_rollback_size;
//    private int rollback_time_order;
//    private String error;

    public CdpRollbackTask() {
    }

    public CdpRollbackTask(CdpRollbackTaskInfo origin) {
        task_status = origin.getTaskStatus();
        error_message = origin.getErrorMessage();
        disk_group_guid = origin.getDiskGroupGuid();
        rollback_time = origin.getRollbackTime();
        start_time = origin.getStartTime();
        end_time = origin.getEndTime();
        total_rollback_size = origin.getTotalRollbackSize();
        current_rollback_size = origin.getCurrentRollbackSize();
//        rollback_time_order = origin.getiRollbackTimeOrder();
//        error = origin.getError();
    }

    public int getTask_status() {
        return task_status;
    }

    public void setTask_status(int task_status) {
        this.task_status = task_status;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getDisk_group_guid() {
        return disk_group_guid;
    }

    public void setDisk_group_guid(String disk_group_guid) {
        this.disk_group_guid = disk_group_guid;
    }

    public Date getRollback_time() {
        return rollback_time;
    }

    public void setRollback_time(Date rollback_time) {
        this.rollback_time = rollback_time;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public long getTotal_rollback_size() {
        return total_rollback_size;
    }

    public void setTotal_rollback_size(long total_rollback_size) {
        this.total_rollback_size = total_rollback_size;
    }

    public long getCurrent_rollback_size() {
        return current_rollback_size;
    }

    public void setCurrent_rollback_size(long current_rollback_size) {
        this.current_rollback_size = current_rollback_size;
    }

    
}
