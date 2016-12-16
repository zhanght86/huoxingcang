/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.iscsi.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class LunsRes implements Serializable{
    private int[] luns;

    public LunsRes() {
    }

    public LunsRes(int[] luns) {
        this.luns = luns;
    }

    public int[] getLuns() {
        return luns;
    }

    public void setLuns(int[] luns) {
        this.luns = luns;
    }

  
    
    
    
}
