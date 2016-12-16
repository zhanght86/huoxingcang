/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.cdp.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class CdpRollbackTaskInfo implements Serializable{
    
    private int TaskStatus;
    private String ErrorMessage;
    private String DiskGroupGuid;
    private long RollbackTime;
    private long StartTime;
    private long EndTime;
    private long TotalRollbackSize;
    private long CurrentRollbackSize;
    private int iRollbackTimeOrder;
    private String error;

    public CdpRollbackTaskInfo() {
    }

    public CdpRollbackTaskInfo(int TaskStatus, String ErrorMessage, String DiskGroupGuid, long RollbackTime, long StartTime, long EndTime, long TotalRollbackSize, long CurrentRollbackSize, int iRollbackTimeOrder, String error) {
        this.TaskStatus = TaskStatus;
        this.ErrorMessage = ErrorMessage;
        this.DiskGroupGuid = DiskGroupGuid;
        this.RollbackTime = RollbackTime;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.TotalRollbackSize = TotalRollbackSize;
        this.CurrentRollbackSize = CurrentRollbackSize;
        this.iRollbackTimeOrder = iRollbackTimeOrder;
        this.error = error;
    }

    public int getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(int TaskStatus) {
        this.TaskStatus = TaskStatus;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String ErrorMessage) {
        this.ErrorMessage = ErrorMessage;
    }

    public String getDiskGroupGuid() {
        return DiskGroupGuid;
    }

    public void setDiskGroupGuid(String DiskGroupGuid) {
        this.DiskGroupGuid = DiskGroupGuid;
    }

    public long getRollbackTime() {
        return RollbackTime;
    }

    public void setRollbackTime(long RollbackTime) {
        this.RollbackTime = RollbackTime;
    }

    public long getStartTime() {
        return StartTime;
    }

    public void setStartTime(long StartTime) {
        this.StartTime = StartTime;
    }

    public long getEndTime() {
        return EndTime;
    }

    public void setEndTime(long EndTime) {
        this.EndTime = EndTime;
    }

    public long getTotalRollbackSize() {
        return TotalRollbackSize;
    }

    public void setTotalRollbackSize(long TotalRollbackSize) {
        this.TotalRollbackSize = TotalRollbackSize;
    }

    public long getCurrentRollbackSize() {
        return CurrentRollbackSize;
    }

    public void setCurrentRollbackSize(long CurrentRollbackSize) {
        this.CurrentRollbackSize = CurrentRollbackSize;
    }

    public int getiRollbackTimeOrder() {
        return iRollbackTimeOrder;
    }

    public void setiRollbackTimeOrder(int iRollbackTimeOrder) {
        this.iRollbackTimeOrder = iRollbackTimeOrder;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
