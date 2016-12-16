/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class CreateFCTarget {
    private String name;
    private String alias;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    public String save() {
        FCTargetData fc = SCSISession.getFCTargetDataFromSession();
        fc.addTarget(name, alias);
        return  "scsi_target?faces-redirect=true";
    }
}
