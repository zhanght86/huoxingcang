/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.vm.model.ChipTypes;
import com.marstor.msa.vm.model.ControllerInfo;
import com.marstor.msa.vm.model.ControllerType;
import com.marstor.msa.vm.model.VMInformation;
import com.marstor.msa.common.util.GetOperatingSystem;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.bean.Controller;
import com.marstor.msa.vbox.bean.VirtualMachine;
import com.marstor.msa.vbox.web.MsaVMInterface;
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
@ManagedBean(name = "modifyControllerChip")
@ViewScoped
public class ModifyControllerChip implements Serializable{

   

    private int type;  //控制器芯片类型
    private int nameType;  //控制器类型
    private String name;
    public String controllerTrueType;
    
    public List<String> chipList;
    VMInformation selectVM;
    ControllerType selectedControllerType;
    List<ControllerInfo> controller;
    
    
    String vmName;
    public String controName;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_vminfotable_modifyController";

    /**
     * Creates a new instance of ModifyControllerChip
     */
    public ModifyControllerChip() {
        controName = res.get(basename, "ide");
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        vmName = request.getParameter("vmName");
        nameType = Integer.valueOf(request.getParameter("controType"));
        if (nameType == Controller.CONTROLLER_TYPE_IDE) {
            controName = res.get(basename, "ide");
        } else if (nameType == Controller.CONTROLLER_TYPE_SCSI) {
            controName = res.get(basename, "scsi");
        } else if (nameType == Controller.CONTROLLER_TYPE_SAS) {
            controName = res.get(basename, "sas");
        }

        SystemOutPrintln.print_vm("nameType="+nameType);
        type = Integer.valueOf(request.getParameter("controChipset"));
        SystemOutPrintln.print_vm("type="+type);  
        this.setControllerType();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getControllerTrueType() {
        return controllerTrueType;
    }

    public void setControllerTrueType(String controllerTrueType) {
        this.controllerTrueType = controllerTrueType;
    }

    public List<String> getChipList() {
        return chipList;
    }

    public void setChipList(List<String> chipList) {
        this.chipList = chipList;
    }

    //根据控制器类型，设置所对应控制器的芯片类型列表
    public void setControllerType(){
    
        if(nameType == Controller.CONTROLLER_TYPE_IDE){  //name值 1代表"ide"，2代表"scsi",3代表"sas";
            name = res.get(basename, "controller_type_ide");
        }else if(nameType == Controller.CONTROLLER_TYPE_SCSI){
            name = res.get(basename, "controller_type_scsi");
        }else if(nameType == Controller.CONTROLLER_TYPE_SAS){
            name = res.get(basename, "controller_type_sas");
        }
        SystemOutPrintln.print_vm("name="+name);
        
        ChipTypes[] chipTypes = GetOperatingSystem.GetChipType();
        int count =chipTypes .length;
        for (int i = 0; i < count; i++) {
            if (name.equalsIgnoreCase(chipTypes[i].getControllerName())) {
                String[] chips = chipTypes[i].getChipTypes();
                int chipsCount = chips.length;
                chipList = new ArrayList();
                for (int j = 0; j < chipsCount; j++) {
                    chipList.add(chips[j]);
                }
            }
        }

        if(type == Controller.CONTROLLER_CHIPSET_TYPE_LSILOGIC){  //type,控制器芯片类型
            controllerTrueType = "LsiLogic";  //LsiLogic
        }else if(type == Controller.CONTROLLER_CHIPSET_TYPE_LSILOGICSAS){
            controllerTrueType = "LsiLogic SAS";  //LsiLogic SAS
        }else if(type == Controller.CONTROLLER_CHIPSET_TYPE_BUSLOGIC){
            controllerTrueType = "BusLogic";  //BusLogic
        }else if(type == Controller.CONTROLLER_CHIPSET_TYPE_PIIX3){
            controllerTrueType = "PIIX3";  //PIIX3
        }else if(type == Controller.CONTROLLER_CHIPSET_TYPE_PIIX4){
            controllerTrueType = "PIIX4";  //PIIX4
        }else if(type == Controller.CONTROLLER_CHIPSET_TYPE_ICH6){
            controllerTrueType = "ICH6";  //HCH6
        }
    }
    
     //获得某控制器芯片类型
    public String modifyControllerChipset(){
        String controllerChipset = controllerTrueType;
        int getType = 1;
        if(controllerChipset.equalsIgnoreCase("LsiLogic")){
            getType = Controller.CONTROLLER_CHIPSET_TYPE_LSILOGIC; //LsiLogic
        }else if(controllerChipset.equalsIgnoreCase("LsiLogic SAS")){
            getType = Controller.CONTROLLER_CHIPSET_TYPE_LSILOGICSAS;  //LsiLogic SAS
        }else if(controllerChipset.equalsIgnoreCase("BusLogic")){
            getType = Controller.CONTROLLER_CHIPSET_TYPE_BUSLOGIC; //BusLogic
        }else if(controllerChipset.equalsIgnoreCase("PIIX3")){
            getType = Controller.CONTROLLER_CHIPSET_TYPE_PIIX3;  //PIIX3
        }else if(controllerChipset.equalsIgnoreCase("PIIX4")){
            getType = Controller.CONTROLLER_CHIPSET_TYPE_PIIX4;  //PIIX4
        }else if(controllerChipset.equalsIgnoreCase("ICH6")){
            getType = Controller.CONTROLLER_CHIPSET_TYPE_ICH6;  //HCH6
        }
        
           SystemOutPrintln.print_vm("getType="+getType);
           
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        VirtualMachine vm = vmface.getVMInfo(vmName);
        List<Controller> conList = vm.getController();
        if (conList != null && conList.size() > 0) {
            for (int i = 0; i < conList.size(); i++) {
                SystemOutPrintln.print_vm("conList.get(i).getControllerName()=" + conList.get(i).getControllerName());
                String StorageControlerName = conList.get(i).getControllerName();  //"IDE"
                int controllerType = conList.get(i).getControllerType();  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
                if (nameType == controllerType) {
                    Controller selectController = conList.get(i);
                    selectController.setControllerChipset(getType);
                    boolean chipset = vmface.modifyController(vmName, selectController);
                    if (chipset) {
                        String param = "vmName=" + vmName;
                        return "vm_vminfotab?faces-redirect=true&amp;" + param;
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "chipset_fail"), ""));
                        return null;
                    }
                }

            }
        }

         String param1 = "vmName=" + vmName;
        return "vm_vminfotab?faces-redirect=true&amp;" + param1;

    }
    
     //获得某控制器芯片类型
    public String returnVMInfo(){
        String param1 = "vmName=" + vmName;
        return "vm_vminfotab?faces-redirect=true&amp;" + param1;
    }
    
    public String modifyCon(){
        return null;
        
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public String getControName() {
        return controName;
    }

    public void setControName(String controName) {
        this.controName = controName;
    }
    
     

}
