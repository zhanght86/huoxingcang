/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "modifyHostName")
@ViewScoped
public class ModifyHostName implements Serializable{

    private MSAResource res = new MSAResource();
    private String strName;

    public ModifyHostName() {
    }

    public String modify() {
//        if(strName == null || strName.equals("")){
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("hostnamenotnull"), ""));
//             RequestContext.getCurrentInstance().execute("modify.hide();");
//            return null;
//        }
        boolean flag = InterfaceFactory.getCommonInterfaceInstance().setHostName(strName);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("modifyFailed"), ""));
            return null;
        }
        String param = "activeIndex=" + 1;
        return "monitor?faces-redirect=true" + param;
    }

    public void clickModify() {
        if (strName == null || strName.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("hostnamenotnull"), ""));
            return;
        }
        if(strName.length()>=16) {
          //  hostNameLength
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("hostNameLength"), ""));
            return;
        }
        RequestContext.getCurrentInstance().execute("modify.show();");
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }
}
