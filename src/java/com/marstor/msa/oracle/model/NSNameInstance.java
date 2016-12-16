/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "NSName")
@RequestScoped
public class NSNameInstance implements Serializable {

    private String netServiceName = "";
    private String dbServiceName = "";
    private List<Address> addressList = new ArrayList<Address>();
    private String ip;
    private String port;
    private Address sAddress  = null;

    public NSNameInstance() {
    }

    public NSNameInstance(String nsName, String dbServiceName, List<Address> list) {
        this.netServiceName = nsName;
        this.dbServiceName = dbServiceName;
        this.addressList = list;
    }

    public void setNetServiceName(String netServiceName) {
        this.netServiceName = netServiceName;
    }

    public String getNetServiceName() {
        return netServiceName;
    }

    public void setDBServiceName(String dbServiceName) {
        this.dbServiceName = dbServiceName;
    }

    public String getDBServiceName() {
        return dbServiceName;
    }

    public void setList(List<Address> list) {
        this.addressList = list;
    }

    public List<Address> getList() {
        return addressList;
    }

    public String getIP() {
        return this.ip;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void addAddress(String ip, String port) {
        Address add = new Address(ip, port);
        this.addressList.add(add);
        this.ip = null;
        this.port = null;
    }
        
    public void setAddress(Address add){
        this.sAddress = add;
    }
    
    public Address getAddress(){
        return sAddress;
    }
    
    public void deleteAddress(){
        this.addressList.remove(sAddress);
    }
}
