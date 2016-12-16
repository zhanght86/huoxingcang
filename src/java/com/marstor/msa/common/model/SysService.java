/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class SysService implements Serializable{
    private String strName;
    private int iID;
    private String strStatus;
    private boolean bVisible = false;
    private String strImage;
    private String strCondition;
    private String strOperation;
    
    public SysService(){}
    

    public boolean getBVisible() {
        return bVisible;
    }

    public void setBVisible(boolean bVisible) {
        this.bVisible = bVisible;
    }


    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public int getiID() {
        return iID;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }  

    public String getStrImage() {
        return strImage;
    }

    public void setStrImage(String strImage) {
        this.strImage = strImage;
    }

    public String getStrCondition() {
        return strCondition;
    }

    public void setStrCondition(String strCondition) {
        this.strCondition = strCondition;
    }

    public String getStrOperation() {
        return strOperation;
    }

    public void setStrOperation(String strOperation) {
        this.strOperation = strOperation;
    }
}
