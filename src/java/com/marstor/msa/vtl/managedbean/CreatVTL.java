/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.DriveTypeInformation;
import com.marstor.msa.bean.TapeLibraryInformation;
import com.marstor.msa.bean.TapeLibraryTypeInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.model.VTLBean;
import com.marstor.msa.vtl.util.MyConstants;
import static com.marstor.msa.vtl.util.MyConstants.driveType;
import static com.marstor.msa.vtl.util.MyConstants.tapeLibraryType;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "creatVTL")
@ViewScoped
public class CreatVTL  implements Serializable{

    public String name;
    public String vendorID;
    public String productID;
    public String numberOfDrives = 2 +"";
    public String numberOfSlots = 20 +"";
    public String numberOfPorts = 2 +"";
//    public String numberOfTapes;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.add_vtl";
    private TapeLibraryInformation[] tapeLibs;
    public TapeLibraryTypeInformation[] tapeLibraryType;
    public DriveTypeInformation[] driveType;
    public List<String> libTypeList;
    public List<String> driveTypeList;
    /**
     * Creates a new instance of CreatDisk
     */
    public CreatVTL() {          
        initLibTypes_DriveTypes();
    }
    
    public void initLibTypes_DriveTypes(){
        //获取带库 驱动 磁带类型    
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        tapeLibraryType = vtl.getTapeLibraryTypes();
        libTypeList = new ArrayList();
        if(tapeLibraryType!=null && tapeLibraryType.length>1){
            for(TapeLibraryTypeInformation tapeLibType : tapeLibraryType ){
                libTypeList.add(tapeLibType.toString());
            }
            
        }
        
        driveType = vtl.getDriveTypes();
        driveTypeList = new ArrayList();
        if(driveType!=null && driveType.length>1){
            for(DriveTypeInformation drivetype : driveType ){
                driveTypeList.add(drivetype.toString());
            }
            
        }
    }

    public TapeLibraryTypeInformation[] getTapeLibraryType() {
        return tapeLibraryType;
    }

    public void setTapeLibraryType(TapeLibraryTypeInformation[] tapeLibraryType) {
        MyConstants.tapeLibraryType = tapeLibraryType;
    }

    public DriveTypeInformation[] getDriveType() {
        return driveType;
    }

    public void setDriveType(DriveTypeInformation[] driveType) {
        MyConstants.driveType = driveType;
    }

    public String createVTLAction() {
        
        
        int tapeLibraryType_Int =0;
        int driveType_Int =0;
        int ports = 0;
        int slots = 0;
        int drives = 0;
        if (!MyConstants.checkName(name)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v0"), ""));
            return null;
        }
        
       initTapeLibs();

        if (tapeLibs != null) {
            for (int i = 0; i < tapeLibs.length; i++) {
                TapeLibraryInformation tapeLibrary = tapeLibs[i];
                if (tapeLibrary.name.equals(name)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v4"), ""));
                    return null;
                }
            }
        }
        
        if (!MyConstants.checkNum(numberOfDrives.trim()) || numberOfDrives.length() > 3) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v3"), ""));
            return null;
        } else {
            drives = Integer.parseInt(numberOfDrives);
            if (drives > 255) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v3"), ""));
                return null;
            }
        }
        
        if (!MyConstants.checkNum(numberOfPorts ) || numberOfPorts.length() > 3) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v1"), ""));
            return null;
        } else {
            ports  = Integer.parseInt(numberOfPorts);
            if (ports > 128) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v1"), ""));
                return null;

            }
        }
        if (!MyConstants.checkNum(numberOfSlots) || numberOfSlots.length() > 4) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v2"), ""));
            return null;
        } else {
            slots = Integer.parseInt(numberOfSlots);
            if (slots > 1000) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v2"), ""));
                return null;
            }
        }
        
          if(tapeLibraryType!=null && tapeLibraryType.length>1){
            for(TapeLibraryTypeInformation tapeLibType : tapeLibraryType ){
                if(tapeLibType.toString().equals(vendorID)){
                    tapeLibraryType_Int = tapeLibType.id;
                     break;
                }
            }
            
        }
 
        if(driveType!=null && driveType.length>1){
            for(DriveTypeInformation drivetype : driveType ){
                if(drivetype.toString().equals(productID)){
                    driveType_Int =  drivetype.id;
                    break;
                }
            }
            
        }

        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        SystemOutPrintln.print_vtl("add TapeLib name="+name+",tapeLibraryType_Int="+tapeLibraryType_Int+",driveType_Int="+driveType_Int+",slots="+slots+"ports="+ports+",drives="+drives);
       boolean createvtl =vtl.createTapeLibrary(name, tapeLibraryType_Int, driveType_Int, slots, ports, drives);
        if(!createvtl){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "create_fail"), ""));
            return null;
        }
        
        int numberOfSlots = 0;
        int numberOfTapes = 0;
        int id = -1;
       TapeLibraryInformation[] tapeLibrarys = vtl.getTapeLibrarysInfo();
       if(tapeLibrarys==null || tapeLibrarys.length<1){
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "getlibs_fail"), ""));
            return null;
       }else{
           for(TapeLibraryInformation lib :tapeLibrarys){
               if(lib.name.equals(name)){
                   numberOfSlots = lib.numberOfSlots;
                   numberOfTapes = lib.numberOfTapes;
                   id = lib.id;
                   break;
               }
           }
       }
        
         return tapePutin(numberOfSlots,numberOfTapes,id,name);

//        return "vtls?faces-redirect=true";
    }
    
      public String tapePutin(int numberOfSlots,int numberOfTapes,int id,String name) {
        int size =numberOfSlots -numberOfTapes;
        String param = "id=" + id + "&name=" + name+ "&size=" + size;
        return "tape_putin?faces-redirect=true&amp;" + param;
    }

    public void initTapeLibs() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        this.tapeLibs = vtl.getTapeLibrarysInfo();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   

    public List<String> getLibTypeList() {
        return libTypeList;
    }

    public void setLibTypeList(List<String> libTypeList) {
        this.libTypeList = libTypeList;
    }

    public List<String> getDriveTypeList() {
        return driveTypeList;
    }

    public void setDriveTypeList(List<String> driveTypeList) {
        this.driveTypeList = driveTypeList;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getNumberOfDrives() {
        return numberOfDrives;
    }

    public void setNumberOfDrives(String numberOfDrives) {
        this.numberOfDrives = numberOfDrives;
    }

    public String getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setNumberOfSlots(String numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    public String getNumberOfPorts() {
        return numberOfPorts;
    }

    public void setNumberOfPorts(String numberOfPorts) {
        this.numberOfPorts = numberOfPorts;
    }

//    public String getNumberOfTapes() {
//        return numberOfTapes;
//    }
//
//    public void setNumberOfTapes(String numberOfTapes) {
//        this.numberOfTapes = numberOfTapes;
//    }
    
}
