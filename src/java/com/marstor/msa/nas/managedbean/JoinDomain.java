/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.Constant;
import com.marstor.msa.nas.model.DomainBean;
import com.marstor.msa.nas.bean.JoinDomainParameters;
import com.marstor.msa.nas.model.MySession;
import com.marstor.msa.nas.validator.DomainNameValidate;
import com.marstor.msa.nas.validator.IPValidate;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class JoinDomain  implements Serializable{
    private String dnsIP;
    private String ADDomainName;
    private String ADUserName;
    private String ADPasswd;
    private boolean  suport2008OrNot;

    public JoinDomain(String dnsIP, String ADDomainName, String ADUserName, String ADPasswd) {
        this.dnsIP = dnsIP;
        this.ADDomainName = ADDomainName;
        this.ADUserName = ADUserName;
        this.ADPasswd = ADPasswd;
    }

    public JoinDomain() {
    }

    public String getADDomainName() {
        return ADDomainName;
    }

    public void setADDomainName(String ADDomainName) {
        this.ADDomainName = ADDomainName;
    }

    public String getADPasswd() {
        return ADPasswd;
    }

    public void setADPasswd(String ADPasswd) {
        this.ADPasswd = ADPasswd;
    }

    public String getADUserName() {
        return ADUserName;
    }

    public void setADUserName(String ADUserName) {
        this.ADUserName = ADUserName;
    }

    public String getDnsIP() {
        return dnsIP;
    }

    public void setDnsIP(String dnsIP) {
        this.dnsIP = dnsIP;
    }

    public boolean isSuport2008OrNot() {
        return suport2008OrNot;
    }

    public void setSuport2008OrNot(boolean suport2008OrNot) {
        this.suport2008OrNot = suport2008OrNot;
    }
    
    public String save() {
//        if(!IPValidate.isIp(dnsIP)) {
//            RequestContext.getCurrentInstance().addCallbackParam("result", "failed");
//            return ;
//        }
//        if(!DomainNameValidate.validateDomain(this.ADDomainName)) {
//           
//            RequestContext.getCurrentInstance().addCallbackParam("result", "failed");
//            return ;
//        }
//        //String  error = "failed";
//        if(this.ADUserName.equals("")) {
//           // RequestContext.getCurrentInstance().addCallbackParam("result", error);
//            return ;
//        }
        if(this.dnsIP.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("emptyDNS") , res.get("emptyDNS")));
            return null;
        }
        if(!IPValidate.isIp(dnsIP)) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("vailedIP") , res.get("vailedIP")));
            return null;
        }
        if(this.ADDomainName.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("emptyAD") , res.get("emptyAD")));
            return null;
        }
        if(!DomainNameValidate.validateDomain(this.ADDomainName)) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("vailedAD") , res.get("vailedAD")));
            return null;
        }
        if(this.ADUserName.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("emptyUser") , res.get("emptyUser")));
            return null;
        }
         if(this.ADPasswd.equals("")) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("emptyPasswd") , res.get("emptyPasswd")));
            return null;
        }
        JoinDomain  domain = new JoinDomain(dnsIP, ADDomainName, ADUserName, ADPasswd);
//        DomainBean  data = MySession.getDomainData();
//        data.joinDomain(domain);
//        RequestContext.getCurrentInstance().addCallbackParam("result", Constant.configSuccess);
       JoinDomainParameters param = new JoinDomainParameters(ADUserName, ADPasswd, dnsIP, ADDomainName,this.suport2008OrNot);
       boolean flag = InterfaceFactory.getNASInterfaceInstance().joinDomain(param);
       if(!flag) {
           Debug.print("join domain " + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("joinFailed") , res.get("joinFailed")));
            return null;
       }
        return  "nas_domain_config?faces-redirect=true&amp;accordionActive=1";
        
    }
    

}
