/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "storageAreaTapeCount")
@RequestScoped
public class StorageAreaTapeCount  implements Serializable{
 public String count;
 public String totalspace;
 public String totalcapacity;
    /**
     * Creates a new instance of StorageAreaTapeCount
     */
    public StorageAreaTapeCount() {
       
        
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTotalspace() {
        return totalspace;
    }

    public void setTotalspace(String totalspace) {
        this.totalspace = totalspace;
    }

    public String getTotalcapacity() {
        return totalcapacity;
    }

    public void setTotalcapacity(String totalcapacity) {
        this.totalcapacity = totalcapacity;
    }
    
}
