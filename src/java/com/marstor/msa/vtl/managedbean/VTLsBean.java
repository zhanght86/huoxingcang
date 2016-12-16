/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.DriveInformation;
import com.marstor.msa.bean.TapeInformation;
import com.marstor.msa.bean.TapeLibraryInformation;
import com.marstor.msa.cdp.managedbean.Details;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;


import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "vtlsBean")
@ViewScoped
public class VTLsBean implements Serializable {

    private TapeLibraryInformation[] diskGroups;
    private TapeLibraryInformation selectedDG;
    private TapeInformation selectedD;
    private DriveInformation sDrive;
    private TapeInformation[] disks;
    private Map<Integer, TapeInformation[]> disksMap = new HashMap();
    private List<Details> dgInfo = new ArrayList();
//    private Map dgInfos = new HashMap();
    private DriveInformation[] lun;
    private Map<Integer, DriveInformation[]> luns = new HashMap();
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.vtls";
    public String libName;
    public String seleteDriverName;

    public VTLsBean() {
        initDiskGroups();
//        initName();
    }

    public final void initDiskGroups() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        diskGroups = null;
        this.diskGroups = vtl.getTapeLibrarysInfo();
    }

//    public final void initName() {
//        
//    }
    public void toUnloadDrives() {
        libName = selectedDG.name;
        RequestContext.getCurrentInstance().execute("deleteD.show()");

    }

    public void unloadDrives() {
        getDrives(this.selectedDG);
        if (this.lun == null || this.lun.length < 1) {
            return;
        }
        VtlInterface cdp = InterfaceFactory.getVtlInterfaceInstance();
        for (DriveInformation d : this.lun) {
            if (d.tapeID == 0) {
                continue;
            }
            boolean success = cdp.dismountDrive(this.selectedDG.id, d.number);
            if (success) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success1"), global.get("error_mark")));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), ""));
                return;
            }
        }

        initDiskGroups();
//        initName();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success1"), ""));
    }

    public void toUnloadDrive() {
        seleteDriverName = this.sDrive.name;
        RequestContext.getCurrentInstance().execute("unloadD.show()");

    }

    public void unloadDrive() {

//         if (driveInfo.tapeID == 0) {
//            Constants.showInformationMessage(getString("驱动器中没有磁带"));
//            return;
//        }

        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();

        SystemOutPrintln.print_vtl("this.selectedDG.id=" + this.selectedDG.id + ",this.sDrive.number=" + this.sDrive.number + ",this.sDrive=" + this.sDrive.name);
        boolean success = vtl.dismountDrive(this.selectedDG.id, this.sDrive.number);
        if (success) {
            initDiskGroups();
//        initName();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success1"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), ""));
            return;
        }
//        return "vtls?faces-redirect=true";

    }

    public void getExpansions(TapeLibraryInformation dg) {
        
        getDisks(dg);
       
        dgInfo = new ArrayList();
        for (int i = 0; i < 11; i++) {
            Details g = new Details();
            g.setName(res.get(basename, "name" + i));
            dgInfo.add(g);
        }
//        if (dgInfos.get(dg.id) == null) {
        dgInfo.get(0).setValue(dg.id + "");
        dgInfo.get(1).setValue(dg.name);
        dgInfo.get(2).setValue(dg.vendorID);
        dgInfo.get(3).setValue(dg.productID);
        dgInfo.get(4).setValue(dg.revision);
        dgInfo.get(5).setValue(dg.numberOfSlots + "");
        dgInfo.get(6).setValue(dg.numberOfPorts + "");
        dgInfo.get(7).setValue(dg.numberOfDrives + "");
        dgInfo.get(8).setValue(disksMap.get(dg.id).length + "");
        dgInfo.get(9).setValue(dg.serialNumber);
        dgInfo.get(10).setValue(dg.GUID);
//            this.dgInfos.put(dg.id, dgInfo);
//        }
        
        getDrives(dg);
       
    }

    public void getDrives(TapeLibraryInformation dg) {
//        if (luns.get(dg.id) == null) {
        luns = new HashMap();
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();

        luns.put(dg.id, vtl.getTapeLibraryDrivesInfo(dg.id));
//        }
        lun = null;
        lun = luns.get(dg.id);
    }

    public void getDisks(TapeLibraryInformation dg) {
//        if (disksMap.get(dg.id) == null) {
        disksMap = new HashMap();
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        this.disksMap.put(dg.id, vtl.getTapeLibraryTapesInfo(dg.id));
//        }
        disks = null;
        disks = disksMap.get(dg.id);
    }

    public String tapeIDtoName(int id) {
        if (id <= 0) {
            return "";
        }
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        TapeInformation tape = vtl.getTapeInfo(id);
        String tapename = "";
        if (tape != null) {
            tapename = tape.name;
        }
        return tapename;
    }

    public boolean cantUnloadDriver(int driverid) {
        boolean flag = true;
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        DriveInformation driver = vtl.getDriveInfo(driverid);
        if (driver.full == 1) {
            flag = false;
        }
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        int userType = user.getType();
        if (userType != 2) {
            flag = true;
        }

        return flag;
    }

    public String getType(String lun) {
        if (lun.startsWith("fc")) {
            return "FC";
        } else if (lun.startsWith("iscsi")) {
            return "iSCSI";
        } else if (lun.equalsIgnoreCase("all")) {
            return res.get(basename, "all");
        }
        return "";
    }

    public String getStrLun(String lun) {
        if (lun.equalsIgnoreCase("all")) {
            return res.get(basename, "alll");
        } else {
            return lun;
        }
    }

    public void deleteVTLLib() {
        libName = this.selectedDG.name;
//         SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//        ViewInformation[] views = scsi.getLUNView(this.selectedDG.getGUID());
//        if (views != null && views.length > 0) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "exit_lunmapping"), ""));
//            return ;
//        }

        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        TapeInformation[] tapes = vtl.getTapeLibraryTapesInfo(this.selectedDG.id);
        if (tapes != null && tapes.length > 0) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "exit_tape"), ""));
            RequestContext.getCurrentInstance().execute("outTape.show()");
            return;
        } else {
            RequestContext.getCurrentInstance().execute("deleteDG.show()");
            return;
        }
    }

    public String deleteVTL_real() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        boolean ret = vtl.deleteTapeLibrary(this.selectedDG.id);
        if (ret) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success2"), global.get("error_mark")));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail2"), ""));
            return null;
        }

        return "vtls?faces-redirect=true";
    }

    public String deleteVTL() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        ViewInformation[] views = scsi.getLUNView(this.selectedDG.getGUID());
        boolean deletelibview = true;
        if (views != null && views.length > 0) {
            for (ViewInformation view : views) {
                deletelibview = scsi.removeView(this.selectedDG.getGUID(), view.entry);
                if (!deletelibview) {
                    RequestContext.getCurrentInstance().execute("forceLib.show()");
                    return null;
                }
            }
        }
        DriveInformation[] drivers = vtl.getTapeLibraryDrivesInfo(this.selectedDG.id);
        if (drivers != null && drivers.length > 0) {
            for (DriveInformation driver : drivers) {
                ViewInformation[] driverViews = scsi.getLUNView(driver.getGUID());
                if (driverViews != null && driverViews.length > 0) {
                    for (ViewInformation driverView : driverViews) {
                        deletelibview = scsi.removeView(driver.getGUID(), driverView.entry);
                        if (!deletelibview) {
                            RequestContext.getCurrentInstance().execute("forceLib.show()");
                            return null;
                        }
                    }
                }

            }
        }
        return deleteVTL_real();


//        boolean ret = vtl.deleteTapeLibrary(this.selectedDG.id);
//        if (ret) {
////            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success2"), global.get("error_mark")));
//        } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail2"), ""));
//            return null;
//        }
//
//        return "vtls?faces-redirect=true";
    }

    public String toCancle() {
        return "vtls?faces-redirect=true";
    }

    public String lunMapping() {
        String param = "id=" + selectedDG.id + "&name=" + selectedDG.name;
        return "lib_lunmapping?faces-redirect=true&amp;" + param;
    }

    public String tapePutin() {
        int size = selectedDG.numberOfSlots - selectedDG.numberOfTapes;
        String param = "id=" + selectedDG.id + "&name=" + selectedDG.name + "&size=" + size;
        return "tape_putin?faces-redirect=true&amp;" + param;
    }

    public String tapeGetout() {
        String param = "id=" + selectedDG.id + "&name=" + selectedDG.name;
        return "tape_getout?faces-redirect=true&amp;" + param;
    }

    public String vtlAttrs() {
        String param = "id=" + selectedDG.id;
        return "vtl_attrs?faces-redirect=true&amp;" + param;
    }

    public String renameDrive() {
        String param = "id=" + sDrive.id + "&name=" + sDrive.name + "&vid=" + this.selectedDG.id;
        return "drive_rename?faces-redirect=true&amp;" + param;
    }

    public String addVtl() {
        return "add_vtl?faces-redirect=true";
    }

    public TapeLibraryInformation[] getDiskGroups() {
        return diskGroups;
    }

    public void setDiskGroups(TapeLibraryInformation[] diskGroups) {
        this.diskGroups = diskGroups;
    }

    public List<Details> getDgInfo() {
        return dgInfo;
    }

    public void setDgInfo(List<Details> dgInfo) {
        this.dgInfo = dgInfo;
    }

    public TapeLibraryInformation getSelectedDG() {
        return selectedDG;
    }

    public void setSelectedDG(TapeLibraryInformation selectedDG) {
        this.selectedDG = selectedDG;
    }

    public TapeInformation getSelectedD() {
        return selectedD;
    }

    public void setSelectedD(TapeInformation selectedD) {
        this.selectedD = selectedD;
    }

    public TapeInformation[] getDisks() {
        return disks;
    }

    public void setDisks(TapeInformation[] disks) {
        this.disks = disks;
    }

    public Map<Integer, TapeInformation[]> getDisksMap() {
        return disksMap;
    }

    public void setDisksMap(Map<Integer, TapeInformation[]> disksMap) {
        this.disksMap = disksMap;
    }

    public DriveInformation[] getLun() {
        return lun;
    }

    public void setLun(DriveInformation[] lun) {
        this.lun = lun;
    }

    public Map<Integer, DriveInformation[]> getLuns() {
        return luns;
    }

    public void setLuns(Map<Integer, DriveInformation[]> luns) {
        this.luns = luns;
    }

    public MSAResource getRes() {
        return res;
    }

    public void setRes(MSAResource res) {
        this.res = res;
    }

    public MSAGlobalResource getGlobal() {
        return global;
    }

    public void setGlobal(MSAGlobalResource global) {
        this.global = global;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public DriveInformation getSDrive() {
        return sDrive;
    }

    public void setSDrive(DriveInformation sDrive) {
        this.sDrive = sDrive;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public String getSeleteDriverName() {
        return seleteDriverName;
    }

    public void setSeleteDriverName(String seleteDriverName) {
        this.seleteDriverName = seleteDriverName;
    }
}
