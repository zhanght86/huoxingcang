/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.common.util.MSAResource;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class IDMap  implements Serializable{

    private int index;
    private  String winType = "";
    private String unixType = "";
    private String winName = "";
    private String unixName = "";
    private String  winNameStr= "";
    private String  unixNameStr= "";
    

    public IDMap() {
    }

    public IDMap(int index,String winType,String unixType,String winName,String unixName) {
       
        this.index = index;
        this.winType = winType;
       this.winName = winName;
       this.unixType = unixType;
       this.unixName = unixName;
        MSAResource res = new MSAResource();
       if(this.winType.equals("user")) {
           this.winNameStr = res.get("user") + this.winName;
       }else if(this.winType.equals("group")) {
           this.winNameStr = res.get("group") + this.winName;
       }
       if(this.unixType.equals("user")) {
           this.unixNameStr = res.get("user") + this.unixName;
       }else if(this.unixType.equals("group")) {
           this.unixNameStr = res.get("group") + this.unixName;
       }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public void setUnixName(String unixName) {
        this.unixName = unixName;
    }

    public void setUnixType(String unixType) {
        this.unixType = unixType;
    }

    public void setWinName(String winName) {
        this.winName = winName;
    }

    public void setWinType(String winType) {
        this.winType = winType;
    }

    public String getUnixName() {
        return unixName;
    }

    public String getUnixType() {
        return unixType;
    }

    public String getWinName() {
        return winName;
    }

    public String getWinType() {
        return winType;
    }

    public String getUnixNameStr() {
        return unixNameStr;
    }

    public void setUnixNameStr(String unixNameStr) {
        this.unixNameStr = unixNameStr;
    }

    public String getWinNameStr() {
        return winNameStr;
    }

    public void setWinNameStr(String winNameStr) {
        this.winNameStr = winNameStr;
    }
    
}
