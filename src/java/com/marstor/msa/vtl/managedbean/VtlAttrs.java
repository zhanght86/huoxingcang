/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;


import com.marstor.msa.bean.DriveInformation;
import com.marstor.msa.bean.TapeLibraryInformation;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "vtlAttrs")
@ViewScoped
public class VtlAttrs  implements Serializable{
    public int tapeLibraryID = -1;
    private TapeLibraryInformation vtl;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.vtl_attrs";
    public String numberOfPorts;
    public String numberOfSlots;
    public String numberOfDrives;
    public String tapeLibraryName;
    public String numberOfTapes;
    public int slotCount;
    public int portCount;
    public int driveCount;
    public int tapeCount;
    public VtlAttrs() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.tapeLibraryID = Integer.valueOf(request.getParameter("id"));
        initVtl();
    }

    public final void initVtl() {
        VtlInterface vtli = InterfaceFactory.getVtlInterfaceInstance();
        this.vtl = vtli.getTapeLibraryInfo(tapeLibraryID);
        tapeLibraryName = vtl.name;
        numberOfPorts = vtl.numberOfPorts + "";
        numberOfSlots = vtl.numberOfSlots + "";
        numberOfDrives = vtl.numberOfDrives + "";
         SystemOutPrintln.print_vtl("numberOfDrives="+numberOfDrives);
        numberOfTapes = vtl.numberOfTapes+"";
//         numberOfTapes = vtl.numberOfTapes + "  " + "(槽位数不可小于磁带数量)";
        slotCount = vtl.numberOfSlots;
        portCount = vtl.numberOfPorts;
        driveCount = vtl.numberOfDrives;
        tapeCount = vtl.numberOfTapes;
    }

    public String modifyVtl() {  
//        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//        ViewInformation[] views = scsi.getLUNView(vtl.getGUID());
//        if (views != null && views.length > 0) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "nomodifyvtl"), ""));
//            return null;
//        }
        
        if (!MyConstants.checkNum(numberOfDrives)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "numberofdrives_false"), ""));
            return null;
        }
         SystemOutPrintln.print_vtl("numberOfDrives="+numberOfDrives);
         int driveC;
         try {
              driveC = Integer.parseInt(numberOfDrives);
         }catch(NumberFormatException e) {
             SystemOutPrintln.print_vtl("e.getMessage()= "+e.getMessage());
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "numberofdrives_false"), ""));
            return null;
         }
        if(driveC > 255){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "numberofdrives_false"), ""));
            return null;
        }
        SystemOutPrintln.print_vtl("driveC="+driveC);
          SystemOutPrintln.print_vtl("driveCount="+driveCount);
        driveCount = driveC - driveCount;
        SystemOutPrintln.print_vtl("driveCount="+(driveC - driveCount));
        if (driveCount < 0) {
            boolean flag = false;
            VtlInterface vtlAPI = InterfaceFactory.getVtlInterfaceInstance();
            DriveInformation[] drives = vtlAPI.getTapeLibraryDrivesInfo(tapeLibraryID);
            if (drives != null && drives.length > 1) {
                for (int i = 0; i < drives.length; i++) {
                    if (drives[i].full == 1) {
                        flag = true;
                        break;
                    }
                }
            }
//        TapeInformation tape = vtlAPI.getTapeInfo(id);
//        String tapename = "";
//        if(tape != null){
//            tapename = tape.name;
//        }
            if (flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "nodelete_drives"), ""));
                return null;
            }

        }

        if (!MyConstants.checkNum(numberOfPorts) || numberOfPorts.length() > 3) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "numberofports_false"), ""));
            return null;
        }
        if (Integer.parseInt(numberOfPorts) > 128) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "numberofports_false"), ""));
            return null;
        }
        if (!MyConstants.checkNum(numberOfSlots) || numberOfSlots.length() > 4) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "numberofslots_false"), ""));
            return null;
        }
        if (Integer.parseInt(numberOfSlots) > 1000) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "numberofslots_false"), ""));
            return null;
        }
        if (Integer.parseInt(numberOfSlots) < tapeCount) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "numberofslots_less"), ""));
            return null;
        }
        portCount = Integer.parseInt(numberOfPorts) - portCount;
        slotCount = Integer.parseInt(numberOfSlots) - slotCount;
        
        boolean ishaveView = false;
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        ViewInformation[] views = scsi.getLUNView(vtl.getGUID());
        if (views != null && views.length > 0) {
            ishaveView = true;
        }
         VtlInterface vtlInteger = InterfaceFactory.getVtlInterfaceInstance();
        DriveInformation[] drivers = vtlInteger.getTapeLibraryDrivesInfo(vtl.id);
        if (drivers != null && drivers.length > 0) {
            for (DriveInformation driver : drivers) {
                ViewInformation[] driverViews = scsi.getLUNView(driver.getGUID());
                if (driverViews != null && driverViews.length > 0) {
                   ishaveView = true;
                }

            }
        }
        
        if(ishaveView){
             RequestContext.getCurrentInstance().execute("delviews.show()"); 
        }else{
             return modifyVtl_real();
        }
        
        return null;
        
    }
     public String modifyVtl_haveViews() { 
         SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        ViewInformation[] views = scsi.getLUNView(vtl.getGUID());
         if (views != null && views.length > 0) {
            for (ViewInformation view : views) {
             boolean   deletelibview = scsi.removeView(vtl.getGUID(), view.entry);
                if (!deletelibview) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "delviews_fail"), ""));
                    return null;
                }
            }
        }
        VtlInterface vtlInteger = InterfaceFactory.getVtlInterfaceInstance();
        DriveInformation[] drivers = vtlInteger.getTapeLibraryDrivesInfo(vtl.id);
        if (drivers != null && drivers.length > 0) {
            for (DriveInformation driver : drivers) {
                ViewInformation[] driverViews = scsi.getLUNView(driver.getGUID());
                if (driverViews != null && driverViews.length > 0) {
                    for (ViewInformation driverView : driverViews) {
                      boolean  deletelibview = scsi.removeView(driver.getGUID(), driverView.entry);
                        if (!deletelibview) {
                             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get(basename, "delviews_fail"), ""));
                            return null;
                        }
                    }
                }

            }
        }
        
        

        
        return modifyVtl_real();
     }
     
      public String modifyVtl_real() { 
          VtlInterface vtli = InterfaceFactory.getVtlInterfaceInstance();
        SystemOutPrintln.print_vtl("tapeLibraryID="+tapeLibraryID+"portCount="+portCount+",slotCount="+slotCount+",driveCount="+driveCount);
        boolean suc = vtli.modifyTapeLibaryElement(tapeLibraryID, portCount,
                slotCount, driveCount);
        if (!suc) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f0"), ""));
            return null;
        }
        return "vtls?faces-redirect=true";
      }
    

    public String getNumberOfPorts() {
        return numberOfPorts;
    }

    public void setNumberOfPorts(String numberOfPorts) {
        this.numberOfPorts = numberOfPorts;
    }

    public String getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setNumberOfSlots(String numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    public String getNumberOfDrives() {
        return numberOfDrives;
    }

    public void setNumberOfDrives(String numberOfDrives) {
        this.numberOfDrives = numberOfDrives;
    }

    public String getTapeLibraryName() {
        return tapeLibraryName;
    }

    public void setTapeLibraryName(String tapeLibraryName) {
        this.tapeLibraryName = tapeLibraryName;
    }

    public String getNumberOfTapes() {
        return numberOfTapes;
    }

    public void setNumberOfTapes(String numberOfTapes) {
        this.numberOfTapes = numberOfTapes;
    }
    
}
