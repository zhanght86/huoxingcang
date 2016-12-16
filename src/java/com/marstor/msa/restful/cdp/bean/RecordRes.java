/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class RecordRes {
    private CdpRecord[] records;

    public RecordRes() {
    }
    
    

    public RecordRes(CdpRecord[] records) {
        this.records = records;
    }

    public CdpRecord[] getRecords() {
        return records;
    }

    public void setRecords(CdpRecord[] records) {
        this.records = records;
    }
    
}
