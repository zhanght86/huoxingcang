/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.bean.NetConfigInformation;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class NetConfigInformationDetail extends NetConfigInformation implements Serializable{
    public boolean isused;

    /**
     * Creates a new instance of NetConfigInformationDetail
     */
    public NetConfigInformationDetail() {
    }

    public boolean isIsused() {
        return isused;
    }

    public void setIsused(boolean isused) {
        this.isused = isused;
    }
    
}
