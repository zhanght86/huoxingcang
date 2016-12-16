/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;
import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.bean.JoinDomainStatus;
import com.marstor.msa.nas.managedbean.JoinDomain;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import java.util.HashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class DomainBean implements Serializable {

    private boolean isInDomain;
    private String domainName = "";
    private String domainController = "";
    private String isOrNot;
    private HashMap map = new HashMap();
    private ArrayList<NameValue> properyies = new ArrayList<NameValue>();
    // private ArrayList<String> properyies = new ArrayList<String>();
    private String test = Constant.domainName;
    private IDMapBean idmap = new IDMapBean();
    private String accordionActive1 = "";

    public DomainBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        accordionActive1 = request.getParameter("accordionActive1");
        if (accordionActive1 == null || accordionActive1.equals("")) {
            accordionActive1 = "0";
        }
        //this.setIsInDomain(false);
//        NameValue  property ;
//        property = new NameValue("已加入域", "否");
//        properyies.add(property);
//        property = new NameValue(Constant.domainName, this.domainName);
//        properyies.add(property);
//        property = new NameValue(Constant.domainController, this.domainController);
//        properyies.add(property);

//        properyies.add(Constant.joinedToDomain);
//        properyies.add(Constant.domainName);
//        properyies.add(Constant.domainController);
//        //获取火星舱所在的域的状态,然后放到map里
//        map.put(Constant.joinedToDomain, Constant.no);
//        map.put(Constant.domainName, "company.com");
//        map.put(Constant.domainController, "192.168.1.50");
        //JoinDomainParameters  parameters =  InterfaceFactory.getNASInterfaceInstance().getJoinDomainParameters();
        JoinDomainStatus status = InterfaceFactory.getNASInterfaceInstance().isInDomain();
        MSAResource res = new MSAResource();
        NameValue property;
        property = new NameValue(res.get("hasJoninDomain"), status.getIsIndomain() ? res.get("yes") : res.get("no"));
        properyies.add(property);
        property = new NameValue(res.get("domainName"), status.getDomainName());
        properyies.add(property);
        property = new NameValue(res.get("domainControl"), status.getDomainControIp());
        properyies.add(property);
        Debug.print("domainControl IP: " + status.getDomainControIp());
        this.isInDomain = status.getIsIndomain();
    }

    public void init() {
        properyies = new ArrayList<NameValue>();
        JoinDomainStatus status = InterfaceFactory.getNASInterfaceInstance().isInDomain();
        MSAResource res = new MSAResource();
        NameValue property;
        property = new NameValue(res.get("hasJoninDomain"), status.getIsIndomain() ? res.get("yes") : res.get("no"));
        properyies.add(property);
        property = new NameValue(res.get("domainName"), status.getDomainName());
        properyies.add(property);
        property = new NameValue(res.get("domainControl"), status.getDomainControIp());
        properyies.add(property);
        Debug.print("domainControl IP: " + status.getDomainControIp());
        this.isInDomain = status.getIsIndomain();
    }

    public String getAccordionActive1() {
        return accordionActive1;
    }

    public void setAccordionActive1(String accordionActive1) {
        this.accordionActive1 = accordionActive1;
    }

    public IDMapBean getIdmap() {
        return idmap;
    }

    public void setIdmap(IDMapBean idmap) {
        this.idmap = idmap;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public HashMap getMap() {
        return map;
    }

    public void setMap(HashMap map) {
        this.map = map;
    }

//    public ArrayList<String> getProperyies() {
//        return properyies;
//    }
//
//    public void setProperyies(ArrayList<String> properyies) {
//        this.properyies = properyies;
//    }
    public ArrayList<NameValue> getProperyies() {
        return properyies;
    }

    public void setProperyies(ArrayList<NameValue> properyies) {
        this.properyies = properyies;
    }

//    public HashMap getNameValue() {
//        return nameValue;
//    }
//
//    public void setNameValue(HashMap nameValue) {
//        this.nameValue = nameValue;
//    }
    public String getDomainController() {
        return domainController;
    }

    public void setDomainController(String domainController) {
        this.domainController = domainController;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getIsOrNot() {
        return isOrNot;
    }

    public void setIsOrNot(String isOrNot) {
        this.isOrNot = isOrNot;
    }

    public boolean isIsInDomain() {
        return isInDomain;
    }

    public void setIsInDomain(boolean isInDomain) {
        this.isInDomain = isInDomain;
        if (isIsInDomain()) {
            this.isOrNot = Constant.yes;
        } else {
            this.isOrNot = Constant.no;
        }
    }

    public void joinDomain(JoinDomain domain) {
        //执行加入域相关操作

        //如果成功
        this.domainName = domain.getADDomainName();
        //this.isInDomain = true;
        this.setIsInDomain(true);
        this.domainController = "192.168.1.50";

        map.put(Constant.joinedToDomain, Constant.yes);
        map.put(Constant.domainName, domain.getADDomainName());
        map.put(Constant.domainController, "192.168.1.50");
    }

    public void onTabChange(TabChangeEvent event) {
//        FacesMessage msg = new FacesMessage("Tab Changed", "Active Tab:" + event.getTab().getId());   
//  
//       FacesContext.getCurrentInstance().addMessage(null, msg); 
        event.getTab().getId();
    }

    public void exitDomain() {
        boolean flag = InterfaceFactory.getNASInterfaceInstance().exitDomain();
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("exitDomainFailed"), ""));
            return;
        }
        this.init();
    }
}
