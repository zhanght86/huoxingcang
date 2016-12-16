/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "netWorkCard")
@RequestScoped
public class NetWorkCard implements Serializable{
    public String cardName;
    public String ip;
    public String subnet;
    public String aggregation;
    public String ipmpGroupName;
    public String state;
    public boolean PCoECard;
    public boolean enable;
    private boolean notEdit;  //ÊÇ·ñ¿ÉÐÞ¸Ä
    public String gateway;


    /**
     * Creates a new instance of NetWorkCard
     */
    public NetWorkCard() {
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    public String getAggregation() {
        return aggregation;
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }

    public String getIpmpGroupName() {
        return ipmpGroupName;
    }

    public void setIpmpGroupName(String ipmpGroupName) {
        this.ipmpGroupName = ipmpGroupName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isPCoECard() {
        return PCoECard;
    }

    public void setPCoECard(boolean PCoECard) {
        this.PCoECard = PCoECard;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }



    public boolean isNotEdit() {
        return notEdit;
    }

    public void setNotEdit(boolean notEdit) {
        this.notEdit = notEdit;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    
    
    
    
}
