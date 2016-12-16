/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.oracle.model.Address;
import com.marstor.msa.oracle.model.NSNameInstance;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "SingleNSNameBean")
@SessionScoped
public class SingleNSNameBean {
    private Address selectAdd = null;
    private NSNameInstance nsName = null;
    
    public SingleNSNameBean(){
        nsName = new NSNameInstance();
    }
    
    public NSNameInstance getNSName(){
        return nsName;
    }
    
    
    public void setNetServiceName(String nsName){
        this.nsName.setNetServiceName(nsName);
    }
    
    public void setDBServiceName(String dbName){
        this.nsName.setDBServiceName(dbName);
    }
    
    public String getNetServiceName(){
        return this.nsName.getNetServiceName();
    }
    
    
    public String getDBServiceName(){
        return this.nsName.getDBServiceName();
    }
    
    
}
