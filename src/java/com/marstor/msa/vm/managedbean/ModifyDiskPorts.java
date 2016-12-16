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
import com.marstor.msa.vm.managedbean.VMList;
import com.marstor.msa.vm.model.BaseStorageInfo;
import com.marstor.msa.vm.model.ControllerInfo;
import com.marstor.msa.vm.model.VMInformation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "modifyDiskPort")
@ViewScoped
public class ModifyDiskPorts implements Serializable{  // 修改硬盘信息

    public static final int Icon_TYPE_IDE = 1;  //IDE控制器图片
    public static final int Icon_TYPE_SCSI = 2;  //SCSI控制器图片
    public static final int Icon_TYPE_SAS = 3;  //SAS控制器图片
    private int port;
    private int devic;
    private String portAttribute;
    public int storageControlerType;
    public List<String> portList;
    VMInformation selectVM;
    BaseStorageInfo selectStorage;
    String storageName;
    public String vmName;
    BaseStorage bstorage;
    String controlerName;
    String storageAttachMediumStr;
    int storageAttachPortStr;
    int storageAttachDeviceStr;
    public int storageAttachType;
    VirtualMachine vm;
    List<BaseStorage> ide_StorageList;
    List<BaseStorage> scsi_StorageList;
    List<BaseStorage> sas_StorageList;
    Controller ctrl;
    public String controlName;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_vminfotable_modifyCD";

    /**
     * Creates a new instance of ModifyDiskPort
     */
    public ModifyDiskPorts() {
        controlName = res.get(basename, "ide");
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        vmName = request.getParameter("vmName");
        storageAttachMediumStr = request.getParameter("storageAttachMedium");
        storageAttachPortStr = Integer.valueOf(request.getParameter("storageAttachPort"));
        storageAttachDeviceStr = Integer.valueOf(request.getParameter("storageAttachDevice"));
        this.getStorageInfo();

        this.setInfo();
    }

    public void getStorageInfo() {

        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        vm = vmface.getVMInfo(vmName);
        List<Controller> conList = vm.getController();
        if (conList != null && conList.size() > 0) {
            int controllerCount = conList.size();
            SystemOutPrintln.print_vm("controllerCount=" + controllerCount);

            boolean flag = false;
            for (int i = 0; i < conList.size(); i++) {
                if (flag) {
                    break;
                }
                SystemOutPrintln.print_vm("conList.get(i).getControllerName()=" + conList.get(i).getControllerName());
                ctrl = conList.get(i);
                controlerName = conList.get(i).getControllerName();  //"IDE"
                int controlerType = conList.get(i).getControllerType();
                SystemOutPrintln.print_vm("controlerType=" + controlerType);
                if (controlerType == Controller.CONTROLLER_TYPE_IDE) {
                    ide_StorageList = new ArrayList();
                    ide_StorageList = conList.get(i).getStorage();

                } else if (controlerType == Controller.CONTROLLER_TYPE_SCSI) {
                    scsi_StorageList = new ArrayList();
                    scsi_StorageList = conList.get(i).getStorage();
                } else if (controlerType == Controller.CONTROLLER_TYPE_SAS) {
                    sas_StorageList = new ArrayList();
                    sas_StorageList = conList.get(i).getStorage();
                }
                List<BaseStorage> bsList = conList.get(i).getStorage();
                if (bsList != null && bsList.size() > 0) {
                    int StorageCount = bsList.size();

                    for (int j = 0; j < StorageCount; j++) {
                        
                        String storageAttachMedium = bsList.get(j).getStorageName();  //"disk1" 
                        int storageAttachport = bsList.get(j).getPortNum();
                        int storageAttachdevice = bsList.get(j).getDeviceNum();
                        
                        
                        if (storageAttachMediumStr.equalsIgnoreCase("--")) {
                            if (storageAttachport ==storageAttachPortStr && storageAttachdevice==storageAttachDeviceStr) {
                                storageControlerType = conList.get(i).getControllerType();
                                if(storageControlerType == Controller.CONTROLLER_TYPE_IDE){
                                     controlName = res.get(basename, "ide");
                                }
                               
                                SystemOutPrintln.print_vm("storageControlerType = " + storageControlerType);
                                bstorage = bsList.get(j);
                                storageAttachType = bsList.get(j).getStorageType();
                                port = bsList.get(j).getPortNum();
                                devic = bsList.get(j).getDeviceNum();
                                flag = true;
                                break;
                            }
                            
                        } else {
                            if (storageAttachMedium.equals(storageAttachMediumStr)) {
                                storageControlerType = conList.get(i).getControllerType();
                                  if(storageControlerType == Controller.CONTROLLER_TYPE_IDE){
                                     controlName = res.get(basename, "ide");
                                }else if(storageControlerType == Controller.CONTROLLER_TYPE_SCSI){
                                     controlName = res.get(basename, "scsi");
                                }else if(storageControlerType == Controller.CONTROLLER_TYPE_SAS){
                                     controlName = res.get(basename, "sas");
                                }
                                SystemOutPrintln.print_vm("storageControlerType = " + storageControlerType);
                                bstorage = bsList.get(j);
                                storageAttachType = bsList.get(j).getStorageType();
                                port = bsList.get(j).getPortNum();
                                devic = bsList.get(j).getDeviceNum();
                                flag = true;
                                break;
                            }
                        }
                        
                    }

                }

            }
        }
    }

    public void setInfo() {  //设置页面显示的属性
        portList = new ArrayList();
        SystemOutPrintln.print_vm("storageControlerType = " + storageControlerType);
        if (storageControlerType == Controller.CONTROLLER_TYPE_IDE) {  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
            int storageAttachPort = port;
            int storageAttachDevice = devic;
            String attribute = res.get(basename, "IDE_PrimaryMaster");
            SystemOutPrintln.print_vm("common.IDE_PrimaryMaster = " + res.get(basename, "IDE_PrimaryMaster"));
            if (storageAttachPort == 0 && storageAttachDevice == 0) {
                attribute = res.get(basename, "IDE_PrimaryMaster");
            } else if (storageAttachPort == 0 && storageAttachDevice == 1) {
                attribute = res.get(basename, "IDE_PrimarySlave");
            } else if (storageAttachPort == 1 && storageAttachDevice == 0) {
                attribute = res.get(basename, "IDE_SecondaryMaster");
            } else if (storageAttachPort == 1 && storageAttachDevice == 1) {
                attribute = res.get(basename, "IDE_SecondarySlave");
            }
            List<String> attribute3 = this.getNOUsePorts();
            for (int i = 0; i < attribute3.size(); i++) {
                portList.add(attribute3.get(i));

            }
//            portList.add(attribute);
            portAttribute = attribute;
            SystemOutPrintln.print_vm("storageControlerType = " + storageControlerType);
            SystemOutPrintln.print_vm("portList = " + portList.size());
            SystemOutPrintln.print_vm("portAttribute = " + portAttribute);

        } else if (storageControlerType == Controller.CONTROLLER_TYPE_SCSI) {
            int storageAttachPort = port;
            List<Integer> attribute2 = this.getSCSINOUsePorts();
            for (int i = 0; i < attribute2.size(); i++) {
                portList.add(res.get(basename, "SCSI_Port") + " " + attribute2.get(i));  //注意：例子“SCSI端口 0”，此处SCSI端口与0之间的空格必须要，以为在其它出需要用空格判断分离开
            }
//            portList.add(this.setResource("common.SCSI_Port") + " " + storageAttachPort);
            portAttribute = res.get(basename, "SCSI_Port") + " " + storageAttachPort; //注意：例子“SCSI端口 0”，此处SCSI端口与0之间的空格必须要，以为在其它出需要用空格判断分离开
        } else if (storageControlerType == Controller.CONTROLLER_TYPE_SAS) {
            int storageAttachPort = port;
            List<Integer> attribute2 = this.getSASNOUsePorts();
            for (int i = 0; i < attribute2.size(); i++) {
                portList.add(res.get(basename, "SAS_Port") + " " + attribute2.get(i));
            }
//            portList.add(this.setResource("common.SAS_Port") + " " + storageAttachPort);
            portAttribute = res.get(basename, "SAS_Port") + " " + storageAttachPort;
        }

        SystemOutPrintln.print_vm("portAttribute = " + portAttribute + ",portList.size()=" + portList.size());
    }

    //获得IDE类型控制器下的端口号，类型为光盘(只有在IDE控制器下有)，硬盘（IDE类型控制器下的）
    public List<String> getNOUsePorts() {

        List<String> attribute3 = new ArrayList<String>();
        int count = ide_StorageList.size();
        String[] attribute1 = new String[count];  //获得已经用掉的端口
        String this_attribute = null;
        int port = this.port;
        int devic = this.devic;
        if (port == 0 && devic == 0) {
            this_attribute = res.get(basename, "IDE_PrimaryMaster");
        } else if (port == 0 && devic == 1) {
            this_attribute = res.get(basename, "IDE_PrimarySlave");
        } else if (port == 1 && devic == 0) {
            this_attribute = res.get(basename, "IDE_SecondaryMaster");
        } else if (port == 1 && devic == 1) {
            this_attribute = res.get(basename, "IDE_SecondarySlave");
        }
        String attribute = null;
        for (int i = 0; i < count; i++) {
            int storageAttachPort = ide_StorageList.get(i).getPortNum();  //存储设备端口号，IDE取值0或1，SCSI取值0到15，SAS取值0-7
            int storageAttachDevice = ide_StorageList.get(i).getDeviceNum();  //存储设备设备号，IDE控制器，取值0或1，SAS和SCSI取值0
            if (storageAttachPort == 0 && storageAttachDevice == 0) {
                attribute = res.get(basename, "IDE_PrimaryMaster");
            } else if (storageAttachPort == 0 && storageAttachDevice == 1) {
                attribute = res.get(basename, "IDE_PrimarySlave");
            } else if (storageAttachPort == 1 && storageAttachDevice == 0) {
                attribute = res.get(basename, "IDE_SecondaryMaster");
            } else if (storageAttachPort == 1 && storageAttachDevice == 1) {
                attribute = res.get(basename, "IDE_SecondarySlave");
            }

            if (attribute.equalsIgnoreCase(this_attribute)) {
                continue;
            }
            attribute1[i] = attribute;
        }
        String[] attribute2 = {res.get(basename, "IDE_PrimaryMaster"), res.get(basename, "IDE_PrimarySlave"), res.get(basename, "IDE_SecondaryMaster"), res.get(basename, "IDE_SecondarySlave")};
        for (int i = 0; i < attribute2.length; i++) {
            boolean isHave = false;
            for (int j = 0; j < attribute1.length; j++) {
                if (attribute2[i].equalsIgnoreCase(attribute1[j])) {
                    isHave = true;
                    break;
                } else {
                    isHave = false;
                }
            }
            if (isHave == false) {
                attribute3.add(attribute2[i]);
            }
        }
        return attribute3;
    }

    //获得SCSI下的硬盘端口号
    public List<Integer> getSCSINOUsePorts() {
        List<Integer> attribute2 = new ArrayList<Integer>();
        List<Integer> attribute1 = new ArrayList<Integer>();//获得已经用掉的端口
        int count = scsi_StorageList.size();
        int this_port = this.port;

        for (int i = 0; i < count; i++) {
            int storageAttachPort = scsi_StorageList.get(i).getPortNum();   //存储设备端口号，IDE取值0或1，SCSI取值0到15，SAS取值0-7
            if (this_port == storageAttachPort) {
                continue;
            }
            attribute1.add(storageAttachPort);
        }
        for (int i = 0; i < 16; i++) {
            boolean isHave = false;
            for (int j = 0; j < attribute1.size(); j++) {
                if (i == attribute1.get(j)) {
                    isHave = true;
                    break;
                } else {
                    isHave = false;
                }
            }

            if (isHave == false && i != 7) {
                attribute2.add(i);
            }
        }

        return attribute2;
    }

    //获得SAS控制器下硬盘
    public List<Integer> getSASNOUsePorts() {
        List<Integer> attribute2 = new ArrayList<Integer>();
        List<Integer> attribute1 = new ArrayList<Integer>();//获得已经用掉的端口
        int count = sas_StorageList.size();
        int this_port = this.port;

        for (int i = 0; i < count; i++) {
            int storageAttachPort = sas_StorageList.get(i).getPortNum();  //存储设备端口号，IDE取值0或1，SCSI取值0到15，SAS取值0-7
            if (this_port == storageAttachPort) {
                continue;
            }
            attribute1.add(storageAttachPort);
        }
        for (int i = 0; i < 8; i++) {
            boolean isHave = false;
            for (int j = 0; j < attribute1.size(); j++) {
                if (i == attribute1.get(j)) {
                    isHave = true;
                    break;
                } else {
                    isHave = false;
                }
            }
            if (isHave == false) {
                attribute2.add(i);
            }
        }

        return attribute2;
    }

//    public String setResource(String resource) {
//        return java.util.ResourceBundle.getBundle("com/marstor/msa/common/resources/messages").getString(resource);
//    }

    public String modifyButton() {
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean removeStorage = vmface.removeStorage(vmName, false, controlerName, bstorage);
        String returnStr = null;
        if (removeStorage) {
            SystemOutPrintln.print_vm("del storage success");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "removestorage_fail"), ""));
            return null;
        }
        String attribute = portAttribute;
        if (storageControlerType == 1) {  //IDE控制器

            int storageAttachPort = 0;
            int storageAttachDevice = 0;
            if (attribute.equalsIgnoreCase(res.get(basename, "IDE_PrimaryMaster"))) {
                storageAttachPort = 0;
                storageAttachDevice = 0;
            } else if (attribute.equalsIgnoreCase(res.get(basename, "IDE_PrimarySlave"))) {
                //attribute = "第一IDE控制器从通道";
                storageAttachPort = 0;
                storageAttachDevice = 1;
            } else if (attribute.equalsIgnoreCase(res.get(basename, "IDE_SecondaryMaster"))) {
                storageAttachPort = 1;
                storageAttachDevice = 0;
            } else if (attribute.equalsIgnoreCase(res.get(basename, "IDE_SecondarySlave"))) {
                storageAttachPort = 1;
                storageAttachDevice = 1;
            }
            bstorage.setPortNum(storageAttachPort);
            bstorage.setDeviceNum(storageAttachDevice);



        } else if (storageControlerType == 2 || storageControlerType == 3) {  //SCSI控制器、SAS控制器
            int portInt = Integer.valueOf(attribute.substring(attribute.length() - 2).trim());  //注意：例子“SCSI端口 0”，此处SCSI端口与0之间的空格必须要，因为在其它出需要用空格占一位

            bstorage.setPortNum(portInt);

        }

        boolean addStorage = vmface.addStorage(vmName, ctrl, bstorage);
        if (addStorage) {
            String param = "vmName=" + vmName;
            returnStr = "vm_vminfotab?faces-redirect=true" + param;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "removestorage_fail"), ""));
            returnStr = null;
        }

        return returnStr;
    }

    public String cancleButton() {
        String param = "vmName=" + vmName;
        return "vm_vminfotab?faces-redirect=true" + param;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDevic() {
        return devic;
    }

    public void setDevic(int devic) {
        this.devic = devic;
    }

    public String getPortAttribute() {
        return portAttribute;
    }

    public void setPortAttribute(String portAttribute) {
        this.portAttribute = portAttribute;
    }

    public List<String> getPortList() {
        return portList;
    }

    public void setPortList(List<String> portList) {
        this.portList = portList;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public String getControlName() {
        return controlName;
    }

    public void setControlName(String controlName) {
        this.controlName = controlName;
    }
    
}
