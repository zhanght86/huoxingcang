/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "dNSBean")
@ViewScoped
public class DNSBean implements Serializable{

    public String addStr;
    public List<String> dnsList;
    public String selectDNS;
    public String gateway;
    private MSAResource res = new MSAResource();
    private String basename = "common.system_network";

    /**
     * Creates a new instance of DNSBean
     */
    public DNSBean() {
        addDNSList();
    }

    private void addDNSList() {
        dnsList = null;
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        String[] ga = common.getGateway();
        if (ga != null && ga.length > 0) {
            gateway = ga[0];
        }
        String[] dns = common.getDNS();
        dnsList = new ArrayList();
        if (dns != null && dns.length > 0) {
            for (int i = 0; i < dns.length; i++) {
                dnsList.add(dns[i]);
            }
        }
       
//        dnsList.add("192.168.1.224");
//        dnsList.add("192.168.1.5");
//        dnsList.add("192.168.1.22");
//        dnsList.add("192.168.200.121");

    }

    public List<String> getDnsList() {
        return dnsList;
    }

    public void setDnsList(List<String> dnsList) {
        this.dnsList = dnsList;
    }

    public String getSelectDNS() {
        return selectDNS;
    }

    public void setSelectDNS(String selectDNS) {
        this.selectDNS = selectDNS;
    }

    public String getAddStr() {
        return addStr;
    }

    public void setAddStr(String addStr) {
        this.addStr = addStr;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
    
    public void addDNS(String nameStr) {
        if ("".equals(nameStr)) {
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "dns_no"),  ""));
            
            return;
        }
        if (!ValidateUtility.checkIP(nameStr)) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "dns_no"),  ""));
           
            return;
        }

        for (int i = 0; i < dnsList.size(); i++) {
            if (nameStr.trim().equals(dnsList.get(i).trim())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "dns_exit"), ""));
                return;
            }
        }
        
        SystemOutPrintln.print_common("addNTP=" + nameStr);
        if (nameStr != null) {
            dnsList.add(nameStr);
        }
        addStr = "";
        SystemOutPrintln.print_common("长度=" + dnsList.size());
    }
    
     public void pingDNS(String nameStr) {
          if ("".equals(nameStr)) {
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "dns_no"),  ""));
            
            return;
        }
        if (!ValidateUtility.checkIP(nameStr)) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "dns_no"),  ""));
           
            return;
        }
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        boolean ping= common.ping(nameStr);
        if(ping == true){
            SystemOutPrintln.print_common("ping succeed");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "dns_ping"),  ""));
        }else{
            SystemOutPrintln.print_common("ping fail");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "dns_pingfail"),  ""));
        }
     }

    public void deleteDNS(String nameStr) {
         if (nameStr == null || nameStr.equals("")){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "selectdns"), ""));
            return;
        }
        SystemOutPrintln.print_common("addNTP=" + nameStr);
        if (dnsList != null) {
            dnsList.remove(nameStr);
        }
        SystemOutPrintln.print_common("长度=" + dnsList.size());

    }
    
    public void saveDNS() {

        SystemOutPrintln.print_common("长度 gateway=" + gateway);
        SystemOutPrintln.print_common("长度 dnsList=" + dnsList.size());

        String[] gata = null;
        if (gateway != null && !gateway.equals("")) {
            gata = new String[1];
            gata[0] = gateway;

        }
 
        int dnsCount = dnsList.size();
        if (dnsCount <= 0) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "min_onedns"),  ""));
            return;
        }
        if(gateway == null || gateway.trim().equals("")){
            gata = new String[1];
            gata[0] = "";
        }else{
             if (!ValidateUtility.checkIP(gateway)) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "gateway_no"),  ""));
            
            return;
        }
        }
       
        
        
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        boolean setgate= common.setGateway(gata);
        SystemOutPrintln.print_common("setgate="+setgate);
        
         String[] dnsStr = null;
         if(dnsList != null){
             dnsStr = new String[dnsList.size()];
             for(int i=0; i<dnsList.size(); i++){
                 dnsStr[i] = dnsList.get(i);
             }  
         }
        boolean setdns = common.setDNS(dnsStr);
        if(setgate ==true && setdns ==true){
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "gateway_dns_ok"),  ""));
        }else if(setgate ==false && setdns ==false){
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "gateway_dns_fail"),  ""));
        }else{
            if(setgate ==true){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "gateway_ok"),  ""));
            }else{
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "gateway_fail"),  ""));
            }
            if(setdns == true){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "dns_ok"),  ""));
            }else{
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "dns_fail"),  ""));
            }
        }
        
        this.addDNSList();
        
 
     
//              System.out.println("长度gateway=" + gateway);
//        System.out.println("长度123=" + dnsList.size());

    }
}
