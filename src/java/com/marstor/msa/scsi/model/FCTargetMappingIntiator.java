/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

/**
 *
 * @author Administrator
 */
public class FCTargetMappingIntiator {
    
    private int LUN;
    private String deviceName;
    private String product;
    private String manufacturer; //≥ß…Ã

    public FCTargetMappingIntiator(int LUN, String deviceName, String product, String manufacturer) {
        this.LUN = LUN;
        this.deviceName = deviceName;
        this.product = product;
        this.manufacturer = manufacturer;
    }

    public FCTargetMappingIntiator() {
    }

    public int getLUN() {
        return LUN;
    }

    public void setLUN(int LUN) {
        this.LUN = LUN;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    
}
