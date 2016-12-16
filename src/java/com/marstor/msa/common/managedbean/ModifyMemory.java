/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.cdp.util.AgentUtility;
import com.marstor.msa.common.bean.SystemInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.common.web.impl.ZFSInterfaceImpl;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;


/**
 *
 * @author Administrator
 */
@ManagedBean(name = "modifyMemory")
@ViewScoped
public class ModifyMemory implements Serializable{

    private MSAResource res = new MSAResource();
    private String strName;
    private String port;
    private int maxMermory;
    private String tip;

    public ModifyMemory() {
//        int arc = zfs.getZfsArcMax();
//        if(arc ==0){
//            strName = 0 + "";
//        }else{
//            strName = arc + "";
//        }
         ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        port = request.getParameter("port");

        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        SystemInformation sys = common.getSystemInfo();
        String strMem = sys.memory;
        String strMB = strMem.substring(0, strMem.indexOf("M"));
        if (strMB != null && !strMB.equals("")) {
            double test = Double.valueOf(strMB) / 1024L;
            int testInt = (int) test;
            if (test > testInt) {
                maxMermory = testInt + 1;
            } else {
                maxMermory = testInt;
            }
        }
        maxMermory = maxMermory - 4;
        if (maxMermory <= 0) {
            tip = res.get("no_modify");
        } else {
            if (maxMermory == 1) {
                tip = "GB£¨" + res.get("hostNameLength_1") + "£©";
            } else {
                tip = res.get("range1") + maxMermory + res.get("range2");
            }

        }

    }

    public void modify() {
        ZFSInterface  zfs = ZFSInterfaceImpl.getInstance();
        boolean flag = zfs.setZfsArcMax(Integer.valueOf(strName));
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("modifyFailed"), ""));
            return;
        }
        RequestContext.getCurrentInstance().execute("window.top.location.href='../volume/rebootX.xhtml?port="+ port + "'");
    }

    public void clickModify() {
        if (strName == null || strName.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("hostnamenotnull"), ""));
            return;
        } 
        if (!AgentUtility.checkNum(strName, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("hostNameLength") + maxMermory + res.get("unit"), ""));
            return;
        } else {
            Long longName = Long.valueOf(strName);
            if(maxMermory <= 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("nospace"), ""));
                return;
            }
            if (longName < 1 || longName > maxMermory) {
                if (maxMermory == 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("hostNameLength_1"), ""));
                    return;
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("hostNameLength") + maxMermory + res.get("unit"), ""));
                return;
            }
        }
        RequestContext.getCurrentInstance().execute("modify.show();");
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public int getMaxMermory() {
        return maxMermory;
    }

    public void setMaxMermory(int maxMermory) {
        this.maxMermory = maxMermory;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }





   
    
}
