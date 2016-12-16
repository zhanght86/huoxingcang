/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "iPMPAddress")
@ViewScoped
public class IPMPAddress  implements Serializable{
    public String nic;
    public String ip;
    public String netmask;
    public String gateway;
    public boolean defaultgateway = true;
    public boolean cantuse = true;
     public boolean cantgateway = true;
    /**
     * Creates a new instance of IPMPAddress
     */
    public IPMPAddress() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        nic = request.getParameter("nicname");
        ip = request.getParameter("ip");
        netmask = request.getParameter("netmask");
        
        gateway = request.getParameter("gateway");
        SystemOutPrintln.print_common("defaultgateway="+defaultgateway+",netmask="+netmask+",gateway="+gateway);
        if(request.getParameter("defaultgateway")!=null && request.getParameter("defaultgateway").equalsIgnoreCase("true")){
            defaultgateway = true;
        }else{
             defaultgateway = false;
        }
        if(defaultgateway){
            cantgateway = true;
        }else{
             cantgateway = false;
        }
        String cantusestr = request.getParameter("cantuse");
        if(cantusestr!=null &&  cantusestr.equalsIgnoreCase("false")){
            cantuse = false;
        }else{
            cantuse = true;
        }
        this.initAddAddress();
    }
    
    private void initAddAddress(){
        if(cantuse){
            cantgateway = true;
        }
        
    }
     public IPMPAddress(String ip, String netmask,String gateway){
        this.ip = ip;
        this.netmask = netmask;
        this.gateway = gateway;
    }
     
    public void change() {
        if (defaultgateway) {
            cantgateway = true;
        } else {
            cantgateway = false;
        }
    }
     
    public IPMPAddress(String nic, String ip, String netmask, String gateway) {
        this.nic = nic;
        this.ip = ip;
        this.netmask = netmask;

    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public boolean isDefaultgateway() {
        return defaultgateway;
    }

    public void setDefaultgateway(boolean defaultgateway) {
        this.defaultgateway = defaultgateway;
    }

    public boolean isCantuse() {
        return cantuse;
    }

    public void setCantuse(boolean cantuse) {
        this.cantuse = cantuse;
    }

    public boolean isCantgateway() {
        return cantgateway;
    }

    public void setCantgateway(boolean cantgateway) {
        this.cantgateway = cantgateway;
    }
    
}
