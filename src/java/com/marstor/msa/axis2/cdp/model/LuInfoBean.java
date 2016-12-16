/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.cdp.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class LuInfoBean implements Serializable{
    
    private String lUName;
    private String operationalStatus;
    private String providerName; 
    private String alias;    
    private String dataFile; 
    private String size;   
    private String vendorID;   
    private String productID;           
    private String serialNum;     
    private String accessState;   

    public LuInfoBean() {
    }

    public LuInfoBean(String lUName, String operationalStatus, String providerName, String alias, String dataFile, String size, String vendorID, String productID, String serialNum, String accessState) {
        this.lUName = lUName;
        this.operationalStatus = operationalStatus;
        this.providerName = providerName;
        this.alias = alias;
        this.dataFile = dataFile;
        this.size = size;
        this.vendorID = vendorID;
        this.productID = productID;
        this.serialNum = serialNum;
        this.accessState = accessState;
    }

    public String getlUName() {
        return lUName;
    }

    public void setlUName(String lUName) {
        this.lUName = lUName;
    }

    public String getOperationalStatus() {
        return operationalStatus;
    }

    public void setOperationalStatus(String operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getAccessState() {
        return accessState;
    }

    public void setAccessState(String accessState) {
        this.accessState = accessState;
    }

}
