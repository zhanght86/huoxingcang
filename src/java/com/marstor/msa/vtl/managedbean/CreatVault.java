/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.VaultInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.util.MyConstants;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "creatVault")
@ViewScoped
public class CreatVault  implements Serializable{

    public String name;

    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.add_vault";

    /**
     * Creates a new instance of CreatDisk
     */
    public CreatVault() {
        name="";
    }

    public String createVTLAction() {
        
        if(name==null || name.equals("")){
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "valut_no"), ""));
            return null;
        }
        if (name.length() > 32) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "valut_limit"), ""));
            return null;
        }
        if (!MyConstants.checkName(name)) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "valut_no"), ""));
            return null;
        }
        if ("offline".equalsIgnoreCase(name)) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "valut_no"), ""));
            return null;
        }

        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        VaultInformation[] vaults = vtl.getVaults();
        if (vaults != null) {
            if (vaults.length != 0) {
                for (int i = 0; i < vaults.length; i++) {
                    VaultInformation vault = vaults[i];
                    if (name.equals(vault.name)) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "valut_exit"), ""));
                        return null;
                    }
                }
            }
        }
        int creatvalut = vtl.createVault(name);
        if (creatvalut == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "createvalut_fail"), ""));
            return null;
        }
        return "vaults?faces-redirect=true";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
