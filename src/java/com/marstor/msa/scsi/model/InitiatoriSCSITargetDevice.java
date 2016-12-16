/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

/**
 *
 * @author Administrator
 */
public class InitiatoriSCSITargetDevice {

    private int lun;
    private String deviceName;
    private String product;
    private String vendor; //≥ß…Ã

    public InitiatoriSCSITargetDevice(int LUN, String deviceName, String product, String manufacturer) {
        this.lun = LUN;
        this.deviceName = deviceName;
        this.product = product;
        this.vendor = manufacturer;
    }

    public int getLun() {
        return lun;
    }

    public void setLun(int lun) {
        this.lun = lun;
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

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
}
