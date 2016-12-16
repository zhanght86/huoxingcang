/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

/**
 *
 * @author Administrator
 */
public class QuerySnapshotParam {

    private String disk_group_guid;
    private int query_handle;
    private int query_count;
    private boolean forward;
    private boolean reverse;
    private int current_count;

    public String getDisk_group_guid() {
        return disk_group_guid;
    }

    public void setDisk_group_guid(String disk_group_guid) {
        this.disk_group_guid = disk_group_guid;
    }

    public int getQuery_handle() {
        return query_handle;
    }

    public void setQuery_handle(int query_handle) {
        this.query_handle = query_handle;
    }

    public int getQuery_count() {
        return query_count;
    }

    public void setQuery_count(int query_count) {
        this.query_count = query_count;
    }

    

    public int getCurrent_count() {
        return current_count;
    }

    public void setCurrent_count(int current_count) {
        this.current_count = current_count;
    }

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    
    
    
}
