/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.DriveInformation;
import com.marstor.msa.bean.TapeLibraryInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.util.MyConstants;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class DriveRename  implements Serializable{

    public int tapeLibraryID = -1;
    private TapeLibraryInformation vtl;
    private String name;
    private int driveID;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.drive_rename";

    public DriveRename() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.tapeLibraryID = Integer.valueOf(request.getParameter("vid"));
        this.driveID =  Integer.valueOf(request.getParameter("id"));
        this.name = request.getParameter("name");
        initVtl();
    }

    public final void initVtl() {
        VtlInterface vtli = InterfaceFactory.getVtlInterfaceInstance();
        this.vtl = vtli.getTapeLibraryInfo(tapeLibraryID);
    }
    
    
    public String renameDrive(){
         if (!MyConstants.checkDriveName(name)) {
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get(basename, "nameno"), ""));
            return null;
        }
         VtlInterface vtli = InterfaceFactory.getVtlInterfaceInstance();
        DriveInformation[] drives = vtli.getTapeLibraryDrivesInfo(tapeLibraryID);
        for (int i = 0; i < drives.length; i++) {
            DriveInformation tempDrive = drives[i];
            if (tempDrive == null) {
                continue;
            }
            if (tempDrive.name.equals(name)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "nameexit"), ""));
                return null;
            }
        }
        
        boolean suc = vtli.renameDrive(tapeLibraryID, driveID, name);
        if (!suc) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f0"), ""));
            return null;
        }
        return "vtls?faces-redirect=true";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
