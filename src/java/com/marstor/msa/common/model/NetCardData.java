/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class NetCardData implements Serializable{
    private String currentTime;
    private Double speed;
    

    public NetCardData(String currentTime,Double speed ) {
        this.speed = speed;
        this.currentTime = currentTime;
    }

    public NetCardData() {
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
    
    
    
}
