/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.fc.bean;

import com.marstor.msa.bean.IMarsXMLBean;
import com.marstor.xml.XMLConstructor;
import com.marstor.xml.XMLParser;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class InitiatorTargetDevice
{
    public int device_lun = 0;
    public String device_vendor = "";
    public String product_name = "";
    public String device_name = "";

    public InitiatorTargetDevice(com.marstor.msa.common.bean.InitiatorTargetDevice origin) {
        device_lun = origin.lun;
        device_vendor = origin.vendor;
        product_name = origin.product;
        device_name = origin.deviceName;
    }    

    public int getDevice_lun() {
        return device_lun;
    }

    public void setDevice_lun(int device_lun) {
        this.device_lun = device_lun;
    }

    public String getDevice_vendor() {
        return device_vendor;
    }

    public void setDevice_vendor(String device_vendor) {
        this.device_vendor = device_vendor;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }
    
    

    
    
}
    
