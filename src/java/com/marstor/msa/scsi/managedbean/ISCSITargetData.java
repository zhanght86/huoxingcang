/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.scsi.model.Chap;
import com.marstor.msa.scsi.model.ISCSITarget;
import com.marstor.msa.scsi.model.IPBinding;
import com.marstor.msa.common.bean.TargetInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import java.util.ArrayList;
import com.marstor.msa.util.InterfaceFactory;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
//@SessionScoped
//@RequestScoped
public class ISCSITargetData {

    private ArrayList<ISCSITarget> targets = new ArrayList<ISCSITarget>();
    private ISCSITarget temp;
    private ArrayList<IPBinding> globalBindings = new ArrayList<IPBinding>();
    private String activeIndex="";

    public ISCSITargetData() {
//        IPBinding ip;
//        ip = new IPBinding(true, "192.168.1.1");
//        globalBindings.add(ip);
//        ip = new IPBinding(true, "192.168.1.2");
//        globalBindings.add(ip);
//        ip = new IPBinding(true, "192.168.1.3");
//        globalBindings.add(ip);
//        ArrayList<IPBinding> bindings = new ArrayList<IPBinding>();
//        ip = new IPBinding(true, "192.168.1.1");
//        bindings.add(ip);
//        ip = new IPBinding(false, "192.168.1.2");
//        bindings.add(ip);
//        ip = new IPBinding(false, "192.168.1.3");
//        bindings.add(ip);
//
//        ISCSITarget target;
//        Chap chap = new Chap(true, "test", "1234567890123456", true);
//        target = new ISCSITarget("iqn.2013-04.com.marstor:189", "2013-04-189", "online", chap, true);
//        target.setBindings(bindings);
//        iscsiTargets.add(target);
//        target = new ISCSITarget("iqn.2013-06.com.marstor:1371454760649", "1371454760649", "offline", new Chap(), false);
//        iscsiTargets.add(target);
         ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        activeIndex = request.getParameter("activeIndex");
        if(activeIndex==null || activeIndex.equals("")) {
            activeIndex = "0";
        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        TargetInformation[] allTargetInfo = scsi.getAllTarget();
        if(allTargetInfo==null) {
            return ;
        }
        for (TargetInformation targetInfo : allTargetInfo) {
            if (targetInfo.getProtocal().equals("iscsi")) {
                ISCSITarget target = new ISCSITarget(targetInfo);
                targets.add(target);
                Debug.print("state :" + targetInfo.getState());
                Debug.print(targetInfo.getTargetName());
            }
           // Debug.print("##########@@@@@@@@@@" + targetInfo.getTargetName());
            //  iscsiTargets.add(targetInfo.);
        }
    }
    
    public void init() {
        ArrayList<ISCSITarget> iscsiTargets = new ArrayList<ISCSITarget>();
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        TargetInformation[] allTargetInfo = scsi.getAllTarget();
        if(allTargetInfo==null) {
            return ;
        }
        for (TargetInformation targetInfo : allTargetInfo) {
            if (targetInfo.getProtocal().equals("iscsi")) {
                ISCSITarget target = new ISCSITarget(targetInfo);
                iscsiTargets.add(target);
                Debug.print("state :" + targetInfo.getState());
                Debug.print(targetInfo.getTargetName());
            }
           // Debug.print( targetInfo.getTargetName());
            //  iscsiTargets.add(targetInfo.);
        }
        this.targets = iscsiTargets;
        
    }

    public String getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(String activeIndex) {
        this.activeIndex = activeIndex;
    }
    
    public ArrayList<ISCSITarget> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<ISCSITarget> targets) {
        this.targets = targets;
    }

    public void listenCHAPStart(ISCSITarget target) {
        boolean isStart = target.getChap().isIsStart();
        if (isStart) {
            RequestContext.getCurrentInstance().addCallbackParam("iscsi", "on");
        } else {
            RequestContext.getCurrentInstance().addCallbackParam("iscsi", "off");
        }
    }

    public void addTarget(String name, String alias) {
        ISCSITarget target = new ISCSITarget(name, alias, "online", new Chap(), false);
        this.targets.add(target);
    }

    public void removeTarget() {

        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().deleteiSCSITarget(this.temp.getTargetInfo().getAliasName(), this.temp.getTargetInfo().getTargetName());
        if (!flag) {
            Debug.print(" remove target: " + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delTarget"), res.get("delTarget")));
            return;
        }
        this.init();
        //this.iscsiTargets.remove(temp);
    }

    public void beforeRemoveTarget(ISCSITarget target) {
        //temp = target;
        InterfaceFactory.getSCSIInterfaceInstance().deleteiSCSITarget(target.getTargetInfo().getAliasName(), target.getTargetInfo().getTargetName());
    }

    public ISCSITarget getTemp() {
        return temp;
    }

    public void setTemp(ISCSITarget temp) {
        this.temp = temp;
    }

    public ArrayList<IPBinding> getGlobalBindings() {
        return globalBindings;
    }

    public void setGlobalBindings(ArrayList<IPBinding> globalBindings) {
        this.globalBindings = globalBindings;
    }

    public String doBeforeConfigIPBinding(ISCSITarget target) {
        // this.temp = target;
        String targetName = target.getTargetInfo().getTargetName();
        String startOrNot = target.isIsStartBind() ? "1" : "0";
        ArrayList<String> ipList = target.getTargetInfo().tpgs;
        String bindedIP = "", allIP = "";
        for (int i = 0; i < ipList.size(); i++) {
            if (i + 1 < ipList.size()) {
                bindedIP = bindedIP + ipList.get(i) + ",";
            } else {
                bindedIP = bindedIP + ipList.get(i);
            }
        }
        String allIPList[] = InterfaceFactory.getSCSIInterfaceInstance().getAllIP();
        for (int j = 0; j < allIPList.length; j++) {
            if (j + 1 < allIPList.length) {
                allIP = allIP + allIPList[j] + ",";
            } else {
                allIP = allIP + allIPList[j];
            }
        }
        String param = "targetName=" + targetName + "&" + "allIP=" + allIP + "&" + "bindedIP=" + bindedIP + "&" + "startOrNot=" + startOrNot;
        return "scsi_iscsi_target_bind?faces-redirect=true&amp;" + param;
    }

    public String doBeforeConfigChap(ISCSITarget target) {
        //temp = target;
        String targetName, startOrNot, userName, authType;
        targetName = target.getTargetInfo().getTargetName();
        if (target.getTargetInfo().getChapUser().equals("-")) {
            startOrNot = "0";
        } else {
            startOrNot = "1";
        }
        userName = target.getTargetInfo().getChapUser();
        authType = String.valueOf(target.getTargetInfo().getAuthType());
        //String param = "startOrNot="+target.getTargetInfo().getTargetName();
        String param = "targetName=" + targetName + "&" + "startOrNot=" + startOrNot + "&" + "userName=" + userName + "&" + "authType=" + authType;
        return "target_iscsi_chap?faces-redirect=true&amp;" + param;
    }

    public void saveTemp(ISCSITarget target) {
        this.temp = target;
    }
}
