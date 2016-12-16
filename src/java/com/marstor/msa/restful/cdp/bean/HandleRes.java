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
public class HandleRes {
    private int handle;

    public HandleRes() {
    }
    
    

    public HandleRes(int handle) {
        this.handle = handle;
    }

    public int getHandle() {
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }
    
}
