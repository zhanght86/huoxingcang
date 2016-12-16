/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.bean.BaseStorage;
import com.marstor.msa.vbox.bean.Controller;
import com.marstor.msa.vbox.bean.HostHardware;
import com.marstor.msa.vbox.bean.VirtualMachine;
import com.marstor.msa.vbox.web.MsaVMInterface;
import com.marstor.msa.vm.model.ISOPathBean;
import com.marstor.msa.vm.model.ISOPathDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@ManagedBean(name = "selectDriverBean")
@ViewScoped
public class SelectDriverBean implements Serializable{
    

    public ISOPathBean drivePath;
    public List<ISOPathBean> driverList;

    private ISOPathDataModel mediumDriversModel;
    private ISOPathBean selectedDriver;
    String vmName;
    int port;
    int device;
    Controller ideCon = new Controller();
    String ctrlName;
    BaseStorage baseStorage = new BaseStorage();
    List<BaseStorage> bsList;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_vminfotab_selectDriver";

    /**
     * Creates a new instance of ISOPathList
     */
    public SelectDriverBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        vmName = request.getParameter("vmName");
        port = Integer.valueOf(request.getParameter("port"));
        device = Integer.valueOf(request.getParameter("device"));
        getVM();
        getDriveList();
        mediumDriversModel = new ISOPathDataModel(driverList);
    }
    public void getVM(){
       
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        VirtualMachine vm = vmface.getVMInfo(vmName);
        List<Controller> conList = vm.getController();
        if (conList != null && conList.size() > 0) {
            int controllerCount = conList.size();
            for (int i = 0; i < conList.size(); i++) {
                SystemOutPrintln.print_vm("conList.get(i).getControllerName()=" + conList.get(i).getControllerName());
                int conType = conList.get(i).getControllerType();  //"IDE"
                if (conType == Controller.CONTROLLER_TYPE_IDE) {
                    ideCon = conList.get(i);
                    ctrlName = conList.get(i).getControllerName();
                    bsList = conList.get(i).getStorage();
                    if (bsList != null && bsList.size() > 0) {
                        int StorageCount = bsList.size();
                        for (int j = 0; j < StorageCount; j++) {
                            int storageAttachPort = bsList.get(j).getPortNum();
                            int storageAttachDevice = bsList.get(j).getDeviceNum();
                            if ((port == storageAttachPort) && (device == storageAttachDevice)) {
                                baseStorage = bsList.get(j);
                                break;
                            }
                        }

                    }
                }


            }
        }
    }

    public ISOPathBean getDrivePath() {
        return drivePath;
    }

    public void setDrivePath(ISOPathBean drivePath) {
        this.drivePath = drivePath;
    }

   

    public List<ISOPathBean> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<ISOPathBean> driverList) {
        this.driverList = driverList;
    }

   

    public void getDriveList() {
        driverList = new ArrayList();
        List<String> cd_ROMDevices = new ArrayList();

        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        HostHardware hostHardwareInfo = vmface.getHostHardwareInfo();
        if (hostHardwareInfo != null) {
            cd_ROMDevices = hostHardwareInfo.getDvdroms();  //获取物理驱动
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "hosthardwareInfo_fail"), ""));

            return;
        }
        if (cd_ROMDevices != null) {
            SystemOutPrintln.print_vm("cd_ROMDevices not null");
            int storageCount = bsList.size();
            boolean isExist = true;
            for (String device : cd_ROMDevices) {
                for (int j = 0; j < storageCount; j++) {
                    if (bsList.get(j).getStorageType() == BaseStorage.VM_CDROM) {
                        String storageAttachMedium = bsList.get(j).getStorageName();
                        if (storageAttachMedium.endsWith(device)) {
                            isExist = true;
                            break;
                        } else {
                            isExist = false;
                            continue;
                        }
                    }
                }
                if (isExist) {
                    continue;
                }
                drivePath = new ISOPathBean();
                drivePath.setIsoPath(device);
                driverList.add(drivePath);
            }
        }
        
    }
    
    public String okButton() {
        if(selectedDriver == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "selecteddriver_null"), ""));
            return null;
        }
        SystemOutPrintln.print_vm("selectedDriver.getIsoPath()=" + selectedDriver.getIsoPath());
        baseStorage.setStorageName("host:" + selectedDriver.getIsoPath());
        baseStorage.setPhysicsDrive(true);
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean removeDisk = vmface.modifyISO(vmName, ctrlName, baseStorage);
        String returnStr = null;
        if (removeDisk) {
            String param = "vmName=" + vmName;
            returnStr = "vm_vminfotab?faces-redirect=true" + param;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "adddriver_fail"), ""));
            returnStr = null;
        }
        return returnStr;

    }
    
    public String cancleButton() {
        String param = "vmName=" + vmName;
        return "vm_vminfotab?faces-redirect=true" + param;
    }

    public ISOPathDataModel getMediumDriversModel() {
        return mediumDriversModel;
    }

    public void setMediumDriversModel(ISOPathDataModel mediumDriversModel) {
        this.mediumDriversModel = mediumDriversModel;
    }


    public ISOPathBean getSelectedDriver() {
        return selectedDriver;
    }

    public void setSelectedDriver(ISOPathBean selectedDriver) {
        this.selectedDriver = selectedDriver;
    }
  
}
