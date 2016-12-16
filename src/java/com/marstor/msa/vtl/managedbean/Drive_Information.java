/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.vtl.model.DriveBean;
import com.marstor.msa.vtl.model.VaultBean;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

@ManagedBean(name = "drive_Information")
@SessionScoped
public class Drive_Information  implements Serializable{
    
    public DriveBean driveBean;
    /**
     * Creates a new instance of System_hardWareStateList
     */
    public Drive_Information() {
        
    }

    public DriveBean getDriveBean() {
        return driveBean;
    }

    public void setDriveBean(DriveBean driveBean) {
        this.driveBean = driveBean;
    }    
    
     public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("NIC Edited", ((VaultBean) event.getObject()).getName());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("NIC Cancelled", ((VaultBean) event.getObject()).getName());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
}
