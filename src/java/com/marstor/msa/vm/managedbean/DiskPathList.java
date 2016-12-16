/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.bean.HDInfo;
import com.marstor.msa.vbox.web.MsaVMInterface;
import com.marstor.msa.vm.model.DiskPathModel;
import com.marstor.msa.vm.model.ClearHDInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@ManagedBean(name = "diskPathList")
@ViewScoped
public class DiskPathList implements Serializable{

    public ClearHDInfo diskPath;
    public ClearHDInfo diskPa;
    public List<ClearHDInfo> diskList;
    public List<ClearHDInfo> showDiskList;
    private ClearHDInfo selectedDisk;
    private DiskPathModel hdModel;
    private ClearHDInfo[] selectedHDs;
    private boolean cantEdit;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_clearDiskInfoTable";

    /**
     * Creates a new instance of DiskPathList
     */
    public DiskPathList() {
       
        getdisksList();
         showDiskList = new ArrayList();
        getShowList();
        hdModel = new DiskPathModel(showDiskList);
        getMenu();
    }
    
    /**
     * 按钮初始化函数
     */
    public void getMenu() {
        cantEdit = false;
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        int userType = user.getType();
        if (userType != 2) {
            cantEdit = true;
        }
    }

    public List<ClearHDInfo> getDiskList() {
        return diskList;
    }

    public void setDiskList(List<ClearHDInfo> diskList) {
        this.diskList = diskList;
    }

    public List<ClearHDInfo> getShowDiskList() {
        return showDiskList;
    }

    public void setShowDiskList(List<ClearHDInfo> showDiskList) {
        this.showDiskList = showDiskList;
    }

    public ClearHDInfo getSelectedDisk() {
        return selectedDisk;
    }

    public void setSelectedDisk(ClearHDInfo selectedDisk) {
        this.selectedDisk = selectedDisk;
    }

    public DiskPathModel getHdModel() {
        return hdModel;
    }

    public void setHdModel(DiskPathModel hdModel) {
        this.hdModel = hdModel;
    }

    public ClearHDInfo[] getSelectedHDs() {
        return selectedHDs;
    }

    public void setSelectedHDs(ClearHDInfo[] selectedHDs) {
        this.selectedHDs = selectedHDs;
    }

    public void getdisksList() {  //获得所有的磁盘

        diskList = new ArrayList();
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        List<HDInfo> hdList = vmface.getHDInfo();
        if (hdList != null) {
            SystemOutPrintln.print_vm("clean disk is not null");
            for (HDInfo hd : hdList) {
                diskPath = new ClearHDInfo();
                diskPath.setHdName(hd.getHDName());
                diskPath.setState(hd.getState());
                diskPath.setUsage(hd.isUsage());
                SystemOutPrintln.print_vm("hd.getHDName()="+hd.getHDName()+",hd.getUsage()="+hd.isUsage());
                diskList.add(diskPath);
            }
        }else{
            SystemOutPrintln.print_vm("clean disk is null");
        }
        
        
//
//        
//
//        diskPath = new GetHDINFO();
//        diskPath.setHdName("/SYSVOL/VM/test");
//        diskPath.setState("不可用");
//        diskPath.setUsage("test12");
//        diskList.add(diskPath);
//
//        diskPath = new GetHDINFO();
//        diskPath.setHdName("/SYSVOL/VM/her");
//        diskPath.setState("inaccessible");
//        diskPath.setUsage(null);
//        diskList.add(diskPath);
//
//        diskPath = new GetHDINFO();
//        diskPath.setHdName("/SYSVOL/VM/jap");
//        diskPath.setState("可用");
//        diskPath.setUsage(null);
//        diskList.add(diskPath);


    }

    public void getShowList() {  //获得没有用到的磁盘

        if (diskList != null) {
            for (ClearHDInfo disk : diskList) {
                if (disk.usage == false) {
                    diskPa = new ClearHDInfo();
                    diskPa.setHdName(disk.getHdName());
                     SystemOutPrintln.print_vm("disk.getHdName()="+disk.getHdName());
                    SystemOutPrintln.print_vm("disk.getState()="+disk.getState());
                    if (disk.getState()!= null && disk.getState().equalsIgnoreCase("inaccessible")) {
                        diskPa.setState(res.get(basename, "damaged"));
                    } else {
                        diskPa.setState(res.get(basename, "available"));
                    }

                    showDiskList.add(diskPa);

                }
            }
        }
    }

    public String deleteDisk() {
//        diskList.remove(selectedDisk);
        for (int i = 0; i < diskList.size(); i++) {
            if (diskList.get(i).getHdName().equals(selectedDisk.getHdName())) {
                diskList.remove(diskList.get(i));
                break;
            }
        }
        for (int i = 0; i < diskList.size(); i++) {

            SystemOutPrintln.print_vm("磁盘名称=" + diskList.get(i).getHdName());
        }
        showDiskList.remove(selectedDisk);
        return "vm_clearDiskInfoTable?faces-redirect=true";
    }

    public void handleDelete() {
        if (selectedHDs != null && selectedHDs.length>0) {
            RequestContext.getCurrentInstance().execute("deleteDisk.show()");
            return;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "pleaseselete"), ""));
            return;

        } 
    }
    
    public String handleDelete_real() {                 
        if (selectedHDs != null) {
            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            for (int i = 0; i < selectedHDs.length; i++) {
               String path=  selectedHDs[i].getHdName();
               SystemOutPrintln.print_vm("path="+path);
                boolean removeDisk = vmface.deleteDisk(path);
                if (removeDisk) {
                    SystemOutPrintln.print_vm("clear disk success");
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "removedisk_fail"), ""));
                    return null;
                }
            }
        }

        return "vm_clearDiskInfoTable?faces-redirect=true";
    }

//    public String setResource(String resource) {
//        return java.util.ResourceBundle.getBundle("com/marstor/msa/common/resources/messages").getString(resource);
//    }

    public ClearHDInfo getDiskPath() {
        return diskPath;
    }

    public void setDiskPath(ClearHDInfo diskPath) {
        this.diskPath = diskPath;
    }

    public boolean isCantEdit() {
        return cantEdit;
    }

    public void setCantEdit(boolean cantEdit) {
        this.cantEdit = cantEdit;
    }
    
}
