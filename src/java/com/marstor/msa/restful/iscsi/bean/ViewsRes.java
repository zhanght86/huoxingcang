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
public class ViewsRes implements Serializable{
    private View[] views;

    public ViewsRes() {
    }

    public ViewsRes(View[] views) {
        this.views = views;
    }

    public View[] getViews() {
        return views;
    }

    public void setViews(View[] views) {
        this.views = views;
    }
    
    
    
}
