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
@ManagedBean(name = "selectISOBean")
@ViewScoped
public class SelectISOBean implements Serializable{
    

    public ISOPathBean isoPath;
    public List<ISOPathBean> isoList;
    public String newISOPath;
    private ISOPathDataModel mediumISOsModel;
    private ISOPathBean selectedISO;
    
//    public String[] getISOInfos;
    String vmName;
    int port;
    int device;
    Controller ideCon = new Controller();
    String ctrlName;
    BaseStorage baseStorage = new BaseStorage();
    List<BaseStorage> bsList;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_vminfotab_selectISO";

    /**
     * Creates a new instance of ISOPathList
     */
    public SelectISOBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        vmName = request.getParameter("vmName");
        port = Integer.valueOf(request.getParameter("port"));
        device = Integer.valueOf(request.getParameter("device"));
        getVM();
        getISOList();
        mediumISOsModel = new ISOPathDataModel(isoList);
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

    public ISOPathBean getIsoPath() {
        return isoPath;
    }

    public void setIsoPath(ISOPathBean isoPath) {
        this.isoPath = isoPath;
    }

    public List<ISOPathBean> getIsoList() {
        return isoList;
    }

    public void setIsoList(List<ISOPathBean> isoList) {
        this.isoList = isoList;
    }

    public void getISOList() {
        isoList = new ArrayList();
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        List<String> getISOInfos = vmface.getISOInfo();
        if (getISOInfos != null) {
            int storageCount = bsList.size(); //控制器下加载存储设备，光盘或硬盘
            boolean isExist = true;
            for (String iso : getISOInfos) {
                for (int j = 0; j < storageCount; j++) {
                    if (bsList.get(j).getStorageType() == BaseStorage.VM_CDROM) {
                        String storageAttachMedium = bsList.get(j).getStorageName();
                        if (storageAttachMedium.endsWith(iso)) {
                            isExist = true;  //如果ISO文件正在被使用，则界面不显示
                            break;
                        } else {
                            isExist = false;  //如果ISO文件没有被使用，则界面显示
                            continue;
                        }
                    }
                }
                if (isExist) {
                    continue;
                }

                isoPath = new ISOPathBean();
                isoPath.setIsoPath(iso);
                isoList.add(isoPath);
            }
        }

    }
    
    public String okButton(){
        if(selectedISO == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "selectediso_null"), ""));
            return null;
        }
        SystemOutPrintln.print_vm("selectedISO.getIsoPath()="+selectedISO.getIsoPath());
         baseStorage.setStorageName(selectedISO.getIsoPath());
         baseStorage.setPhysicsDrive(false);
          MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean removeDisk = vmface.modifyISO(vmName, ctrlName, baseStorage);
        String returnStr = null;
        if (removeDisk){
            String param = "vmName=" + vmName;
            returnStr = "vm_vminfotab?faces-redirect=true" + param;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "addiso_fail"), ""));
            returnStr = null;
        }
        return returnStr;
        
    }
    
    public String cancleButton() {
        String param = "vmName=" + vmName;
        return "vm_vminfotab?faces-redirect=true" + param;
    }

    public ISOPathDataModel getMediumISOsModel() {
        return mediumISOsModel;
    }

    public ISOPathBean getSelectedISO() {
        return selectedISO;
    }

    public void setSelectedISO(ISOPathBean selectedISO) {
        this.selectedISO = selectedISO;
    }


    public String getNewISOPath() {
        return newISOPath;
    }

    public void setNewISOPath(String newISOPath) {
        this.newISOPath = newISOPath;
    }

}
