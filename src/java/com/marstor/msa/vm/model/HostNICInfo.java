/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @introduction ��ȡ��������������Ϣ
 * @author Administrator
 */
@ManagedBean(name = "hostNICInfo")
@RequestScoped
public class HostNICInfo implements Serializable{
    
    private String NicName;  //��������
    private String NicIpAddress;  //����ip��ַ

    /**
     * Creates a new instance of HostNICInfo
     */
    public HostNICInfo() {
    }
   
    public String getNicIpAddress() {
        return NicIpAddress;
    }

    public void setNicIpAddress(String NicIpAddress) {
        this.NicIpAddress = NicIpAddress;
    }

    public String getNicName() {
        return NicName;
    }

    public void setNicName(String NicName) {
        this.NicName = NicName;
    }
    
}
