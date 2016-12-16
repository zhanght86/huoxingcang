/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.bean.BaseStorage;
import com.marstor.msa.vbox.bean.Controller;
import com.marstor.msa.vbox.bean.HostHardware;
import com.marstor.msa.vbox.bean.NetworkInterfaceCard;
import com.marstor.msa.vbox.bean.VirtualMachine;
import com.marstor.msa.vbox.web.MsaVMInterface;
import com.marstor.msa.vm.model.BaseStorageInfo;
import com.marstor.msa.vm.model.BootSequen;
import com.marstor.msa.vm.model.ControllerInfo;
import com.marstor.msa.vm.model.ControllerType;
import com.marstor.msa.vm.model.NetworkInterfaceCardInfo;
import com.marstor.msa.vm.model.RegisterVMs;
import com.marstor.msa.vm.model.VMInformation;
import com.marstor.msa.vtl.util.MyConstants;
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

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "vMInfoTableBean")
@ViewScoped
public class VMInfoTableBean implements Serializable {

    public VirtualMachine vm;
    String vmNane;
    private ControllerInfo ide = null;
    private ControllerInfo scsi = null;
    private ControllerInfo sas = null;
    private int isHaveIDE = 0;
    private int isHaveSCSI = 0;
    private int isHaveSAS = 0;
    private int isHaveIDE_children = 0;
    private int isHaveSCSI_children = 0;
    private int isHaveSAS_children = 0;
    private int controllerChipSet_IDE = 4;//控制器芯片组
    private int controllerChipSet_SCSI = 2;//控制器芯片组
    private int controllerChipSet_SAS = 1;//控制器芯片组
    List<NetworkInterfaceCardInfo> network;
    List<ControllerInfo> controllers;
    public VMInformation vmInfo;
    private List<BootSequen> bootSequences;  //启动顺序
    public List<ControllerType> controllerTypeList;
    ControllerType con;
    MSAResource resources = new MSAResource();
    public String vmNameStr;
    public String osTypeStr;
    public int cpuCoreNumStr;
    public String memorySizeStr;
    public boolean supportIOAPICStr;
    public List<BootSequen> bootSequencesStr;
    public String remoteAddressStr;
    public boolean remoteMultipleConnectionsStr;
    public String remotePortStr;
    public boolean supportEFIStr;
    public int hostMemorySize;
    public String remotePort_init;
    public String osType_init;
    public boolean cantSys = false;
    
    public boolean haveiso = false;
    public boolean haveejectdisk = false;
    public boolean setting = true;
    public int userType = 1;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_vminfotab";
    private String accordionActive1 = "";

    /**
     * Creates a new instance of VMInfoTableBean
     */
    public VMInfoTableBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        accordionActive1 = request.getParameter("accordionActive1");
        if (accordionActive1 == null || accordionActive1.equals("")) {
            accordionActive1 = "0";
        }
        vmNane = request.getParameter("vmName");
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        vm = vmface.getVMInfo(vmNane);
        getvmstate();
        initVM();
        
    }

    public void initVM() {

        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        getTest1_Info();
        HostHardware hostHardwareInfo = vmface.getHostHardwareInfo();
        if (hostHardwareInfo != null) {
            int hostCpuCoreNum = hostHardwareInfo.getCpuNum();  //获取物理主机的CPU数量
            SystemOutPrintln.print_vm("hostCpuCoreNum=" + hostCpuCoreNum);
            hostMemorySize = hostHardwareInfo.getMemorySize();  //获取物理主机的内存大小
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "hosthardwareinfo_fail"), ""));
            return;
        }
    }
    public void getvmstate() {

//        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
//        Map<String, int[]> map = vmface.getVMList();
//        if (map != null && map.size() > 0) {
//            System.out.println("*********map is not null.");
//            Set<String> keys = map.keySet();
//            Iterator<String> iterator = keys.iterator();
//         
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                System.out.println("vmName=" + key);
//                int[] values = map.get(key);
//                int vmState = values[0];
//                int romotePort = values[1];
//                int memorysize = values[2];
//                if (key.equals(vmNane)) {
//                    if (vmState == VirtualMachine.VM_STOP || vmState == VirtualMachine.VM_ABORTED) {
//                        setting = true;
//                    } else {
//                        setting = false;
//                    }
//                }
//
//            }
//        }
        int vmState = vm.getVmState();
        SystemOutPrintln.print_vm("vmState="+vmState);
        if (vmState == VirtualMachine.VM_STOP || vmState == VirtualMachine.VM_ABORTED) {
            setting = true;
        } else {
            setting = false;
        }
        
        SystemOutPrintln.print_vm("setting="+setting);
        
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        userType = user.getType();
        SystemOutPrintln.print_vm("userType="+userType);
        if (setting==false || userType != 2) {
            cantSys = true;
        }
    }

    private void getTest1_Info() {
        boolean notaddCD_IDE = false;
        boolean notaddDisk_IDE = false;
        boolean notaddDisk_SCSI = false;
        boolean notaddDisk_SAS = false;
        int getnicCount = 0;


        //test1
        ide = null;
        scsi = null;
        sas = null;

        isHaveIDE = 0;
        isHaveSCSI = 0;
        isHaveSAS = 0;
        isHaveIDE_children = 0;
        isHaveSCSI_children = 0;
        isHaveSAS_children = 0;
        controllerChipSet_IDE = 4;//控制器芯片组
        controllerChipSet_SCSI = 2;//控制器芯片组
        controllerChipSet_SAS = 1;//控制器芯片组

        String vmNAME = vm.getVmName();
        String vmUUID = vm.getVmUUID();//虚拟机UUID
        String vmBaseFolderPath = vm.getVmBaseFolderPath();//虚拟机路径
        int boot1 = vm.getBoot1();
        int boot2 = vm.getBoot2();
        int boot3 = vm.getBoot3();
        String osType = vm.getOsType();//虚拟机操作系统类型
        SystemOutPrintln.print_vm("虚拟机操作系统类型 osType="+osType);
        int cpuCoreNum = vm.getCpuCoreNum();//CPU核心数
        int memorySize = vm.getMemorySize();//内存大小



        boolean supportIOAPIC = vm.isSupportIOAPIC();  //是否开启IOACPI

        boolean supportEFI = vm.isSupportEFI();  //是否开启EFI

        boolean supportUTCTime = vm.isSupportUTCTime();  //是否开启UTC时钟

        String remoteAddress = vm.getRemoteAddress();//远程登录ip
        int remotePort = vm.getRemotePort();//远程登录端口号

        boolean RemoteIPAddressUsable = vm.isbRemoteIPAddressUsable();//此远程登录ip是否存在
        int remoteTimeOut = vm.getRemoteTimeOut();//超时时间
        String remoteAuthentication = vm.getRemoteAuthentication();//认证方式
//        int iMultipleConnections = 1;
        boolean remoteMultipleConnections = vm.isRemoteMultipleConnections();//是否允许远程连接多连接
//        if (iMultipleConnections == 0) {
//            remoteMultipleConnections = false;
//        } else {
//            remoteMultipleConnections = true;
//        }
        List<Controller> conList = vm.getController();
        if (conList != null && conList.size() > 0) {
            int controllerCount = conList.size();
            SystemOutPrintln.print_vm("controllerCount=" + controllerCount);
            controllers = new ArrayList();  //加载控制器
            for (int i = 0; i < conList.size(); i++) {
                SystemOutPrintln.print_vm("conList.get(i).getControllerName()=" + conList.get(i).getControllerName());
//                if (conList.get(i).getControllerName().equalsIgnoreCase("IDE Controller")) {
                String StorageControlerName = conList.get(i).getControllerName();  //"IDE"
                int controllerType = conList.get(i).getControllerType();  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas


                SystemOutPrintln.print_vm("controllerType=" + controllerType);
                int controllerChipset = conList.get(i).getControllerChipset();

                ControllerInfo controllerInfo = new ControllerInfo();
                controllerInfo.setControllerName(StorageControlerName);
                controllerInfo.setControllerType(controllerType);
                controllerInfo.setControllerChipset(controllerChipset);
                List<BaseStorage> bsList = conList.get(i).getStorage();
                if (bsList != null && bsList.size() > 0) {
                    int StorageCount = bsList.size();
                    List<BaseStorageInfo> storage = new ArrayList();  //控制器下加载存储设备，光盘或硬盘
                    for (int j = 0; j < StorageCount; j++) {
                        String storageAttachMedium = bsList.get(j).getStorageName();  //"disk1"
                        String[] sam = storageAttachMedium.split("/");
                        String storageName = sam[sam.length - 1];

                        boolean StorageAttachMediumIsExists = bsList.get(j).isbStorageAttachMediumIsExists();

                        int storageAttachType = bsList.get(j).getStorageType();  //存储设备类型取值1为硬盘，2为光盘
                        long storageAttachSize = bsList.get(j).getSize();
                        long StorageAttachUsedSize = bsList.get(j).getUsedSize();
                        boolean StorageAttachIsPhysical = bsList.get(j).isPhysicsDrive();
                        String storageAttachFormat = bsList.get(j).getFormat();  //存储设备格式
                        int storageAttachPort = bsList.get(j).getPortNum();  //存储设备端口号，IDE取值0或1，SCSI取值0到14，SAS取值0-7
                        int StorageAttachDevice = bsList.get(j).getDeviceNum();  //存储设备设备号，IDE控制器，取值0或1，SAS和SCSI取值0
                        String attribute = null;

                        if (controllerType == 1) {  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas

                            if (storageAttachPort == 0 && StorageAttachDevice == 0) {
                                attribute = res.get(basename, "common.IDE_PrimaryMaster");
                            } else if (storageAttachPort == 0 && StorageAttachDevice == 1) {
                                attribute = res.get(basename, "common.IDE_PrimarySlave");
                            } else if (storageAttachPort == 1 && StorageAttachDevice == 0) {
                                attribute = res.get(basename, "common.IDE_SecondaryMaster");
                            } else if (storageAttachPort == 1 && StorageAttachDevice == 1) {
                                attribute = res.get(basename, "common.IDE_SecondarySlave");
                            }
                        } else if (controllerType == 2) {
                            attribute = res.get(basename, "common.SCSI_Port")+ " " + storageAttachPort;  //注意：例子“SCSI端口 0”，此处SCSI端口与0之间的空格必须要，因为在其它出需要用空格占一位

                        } else if (controllerType == 3) {
                            attribute = res.get(basename, "common.SAS_Port") + " " + storageAttachPort;  //注意：例子“SCSI端口 0”，此处SCSI端口与0之间的空格必须要，因为在其它出需要用空格占一位
                        }

                        String unit = "MB";
                        String storageType = "";
                        BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
                        if (storageAttachType == BaseStorage.VM_DISK) {  //存储设备类型取值1为硬盘，2为光盘
                            storageType = "Normal (VDI)";
//                            storageAttachSize = storageAttachSize / 1024;
//                            StorageAttachUsedSize = StorageAttachUsedSize / 1024;
//                            unit = "GB";
                           String storageAttachSizestr = MyConstants.sizeToString(storageAttachSize*1024 * 1024);
                           String StorageAttachUsedSizestr = MyConstants.sizeToString(StorageAttachUsedSize*1024 * 1024);
                           

                            SystemOutPrintln.print_vm("storageName=" + storageName);
                            haveiso = false;
                            haveejectdisk = false;

                            baseStorageInfo.setStorageName(storageName);
                            baseStorageInfo.setAttribue(attribute);
                            baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
                            baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
                            baseStorageInfo.setStorageAttachType(storageAttachType);
                            baseStorageInfo.setStorageAttachSize(storageAttachSizestr);
                            baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
                            baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSizestr);
                            baseStorageInfo.setStorageAttachFormat(storageType);
                            baseStorageInfo.setStorageAttachPort(storageAttachPort);
                            baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
                            baseStorageInfo.setHaveiso(haveiso);
                            baseStorageInfo.setHaveejectdisk(haveejectdisk);
                            baseStorageInfo.setCantiso(true);
                            baseStorageInfo.setCantejectdisk(true);
                            baseStorageInfo.setCantdei_modify(false);
                            if (setting==false || userType != 2) {
                                baseStorageInfo.setCantdei_modify(true);
                            }
                            if (StorageAttachMediumIsExists) {
                                baseStorageInfo.setStrstate(res.get(basename, "common.available"));
                            } else {
                                baseStorageInfo.setStrstate(res.get(basename, "common.damaged"));
                            }
                        } else if (storageAttachType == BaseStorage.VM_CDROM) {

                            if (storageName.equalsIgnoreCase("emptydrive")) {
                                storageName = res.get(basename, "common.emptyDisk");
                                haveiso = true;
                                haveejectdisk = false;
                                baseStorageInfo.setStorageName(storageName);
                                baseStorageInfo.setAttribue(attribute);
                                baseStorageInfo.setStorageAttachMedium("--");
                                baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
                                baseStorageInfo.setStorageAttachType(storageAttachType);
                                baseStorageInfo.setStorageAttachSize("--");
                                baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
//                            baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize + unit);
                                baseStorageInfo.setStorageAttachFormat("--");
                                baseStorageInfo.setStorageAttachPort(storageAttachPort);
                                baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
                                if (StorageAttachMediumIsExists) {
                                    baseStorageInfo.setStrstate(res.get(basename, "common.available"));
                                } else {
                                    baseStorageInfo.setStrstate(res.get(basename, "common.damaged"));
                                }
                                baseStorageInfo.setHaveiso(haveiso);
                                baseStorageInfo.setHaveejectdisk(haveejectdisk);
                                baseStorageInfo.setCantiso(false);
                                baseStorageInfo.setCantejectdisk(true);
                                baseStorageInfo.setCantdei_modify(false);
                                 if (setting==false) {
                                    baseStorageInfo.setCantdei_modify(true);
                                }
                                if (userType != 2) {
                                    baseStorageInfo.setCantiso(true);
                                    baseStorageInfo.setCantdei_modify(true);
                                }
                            } else {
                                haveiso = true;
                                haveejectdisk = true;
                                storageType = "Image";
                                storageAttachSize = bsList.get(j).getSize();
                                unit = "MB";
                                baseStorageInfo.setStorageName(storageName);
                                baseStorageInfo.setAttribue(attribute);
                                baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
                                baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
                                baseStorageInfo.setStorageAttachType(storageAttachType);
                                baseStorageInfo.setStorageAttachSize(storageAttachSize + unit);
                                baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
                                baseStorageInfo.setStorageAttachFormat(storageType);
                                baseStorageInfo.setStorageAttachPort(storageAttachPort);
                                baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
                                if (StorageAttachMediumIsExists) {
                                    baseStorageInfo.setStrstate(res.get(basename, "common.available"));
                                } else {
                                    baseStorageInfo.setStrstate(res.get(basename, "common.damaged"));
                                }
                                baseStorageInfo.setHaveiso(haveiso);
                                baseStorageInfo.setHaveejectdisk(haveejectdisk);
                                baseStorageInfo.setCantiso(false);
                                baseStorageInfo.setCantejectdisk(false);
                                baseStorageInfo.setCantdei_modify(false);
                                if (setting==false ) {
                                    baseStorageInfo.setCantdei_modify(true);
                                }
                                if (userType != 2) {
                                    baseStorageInfo.setCantiso(true);
                                    baseStorageInfo.setCantejectdisk(true);
                                    baseStorageInfo.setCantdei_modify(true);
                                }

                            }
                        }

                        storage.add(baseStorageInfo);
                    }
                    controllerInfo.setStorage(storage);
                }
                controllers.add(controllerInfo);

                if (controllerType == Controller.CONTROLLER_TYPE_IDE) {
                    ide = controllerInfo;
                    isHaveIDE = 1;
                    controllerChipSet_IDE = controllerChipset;
                    int storageSize = controllerInfo.getStorage().size();
                    if (storageSize == 4) {
                        notaddCD_IDE = true;
                        notaddDisk_IDE = true;
                    }
                    if (storageSize > 0) {
                        isHaveIDE_children = 1;

                    }

                }
                if (controllerType == Controller.CONTROLLER_TYPE_SCSI) {
                    scsi = controllerInfo;
                    isHaveSCSI = 1;
                      SystemOutPrintln.print_vm("isHaveSCSI="+isHaveSCSI);
                    controllerChipSet_SCSI = controllerChipset;
                    int storageSize = controllerInfo.getStorage().size();
                    if (storageSize == 15) {
                        SystemOutPrintln.print_vm("CONTROLLER_TYPE_SCSI controllerInfo.getStorage().size()="+storageSize+"notaddDisk_SCSI = true;");
                        notaddDisk_SCSI = true;
                    }
                    if (storageSize > 0) {

                        isHaveSCSI_children = 1;

                    }

                }
                if (controllerType == Controller.CONTROLLER_TYPE_SAS) {
                    sas = controllerInfo;
                    isHaveSAS = 1;
                    controllerChipSet_SAS = controllerChipset;
                    int storageSize = controllerInfo.getStorage().size();
                    if (storageSize == 8) {
                        notaddDisk_SAS = true;
                    }
                    if (storageSize > 0) {
                        isHaveSAS_children = 1;
                    }

                }
                
//                }

//                if (conList.get(i).getControllerName().equalsIgnoreCase("SCSI Controller")) {
//                    String StorageControlerName = conList.get(i).getControllerName();  //"SCSI"
//                    int controllerType = conList.get(i).getControllerType();  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
//                    int controllerChipset = conList.get(i).getControllerChipset();
//
//                    ControllerInfo controllerInfo = new ControllerInfo();
//                    controllerInfo.setControllerName(StorageControlerName);
//                    controllerInfo.setControllerType(controllerType);
//                    controllerInfo.setControllerChipset(controllerChipset);
//                    List<BaseStorage> bsList = conList.get(i).getStorage();
//                    if (bsList != null && bsList.size() > 0) {
//                        int StorageCount = bsList.size();
//                        List<BaseStorageInfo> storage = new ArrayList();  //控制器下加载存储设备，光盘或硬盘
//                        for (int j = 0; j < StorageCount; j++) {
//                            String storageAttachMedium = bsList.get(j).getStorageName();  //"disk1"
//                            String[] sam = storageAttachMedium.split("/");
//                            String storageName = sam[sam.length - 1];
//                            boolean StorageAttachMediumIsExists = bsList.get(j).isbStorageAttachMediumIsExists();
//
//                            int storageAttachType = bsList.get(j).getStorageType();  //存储设备类型取值1为硬盘，2为光盘
//                            long storageAttachSize = bsList.get(j).getSize();
//                            long StorageAttachUsedSize = bsList.get(j).getUsedSize();
//                            boolean StorageAttachIsPhysical = bsList.get(j).isPhysicsDrive();
//                            String storageAttachFormat = bsList.get(j).getFormat();  //存储设备格式
//                            int storageAttachPort = bsList.get(j).getPortNum();  //存储设备端口号，IDE取值0或1，SCSI取值0到14，SAS取值0-7
//                            int StorageAttachDevice = bsList.get(j).getDeviceNum();  //存储设备设备号，IDE控制器，取值0或1，SAS和SCSI取值0
//                            String attribute = null;
//                            if (controllerType == 1) {  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
//
//                                if (storageAttachPort == 0 && StorageAttachDevice == 0) {
//                                    attribute = "第一IDE控制器主通道";
//                                } else if (storageAttachPort == 0 && StorageAttachDevice == 1) {
//                                    attribute = "第一IDE控制器从通道";
//                                } else if (storageAttachPort == 1 && StorageAttachDevice == 0) {
//                                    attribute = "第二IDE控制器主通道";
//                                } else if (storageAttachPort == 1 && StorageAttachDevice == 1) {
//                                    attribute = "第二IDE控制器从通道";
//                                }
//                            } else if (controllerType == 2) {
//                                attribute = "SCSI 端口" + " " + storageAttachPort;  //注意：例子“SCSI端口 0”，此处SCSI端口与0之间的空格必须要，因为在其它出需要用空格占一位
//
//                            } else if (controllerType == 3) {
//                                attribute = "SAS 端口" + " " + storageAttachPort;  //注意：例子“SCSI端口 0”，此处SCSI端口与0之间的空格必须要，因为在其它出需要用空格占一位
//                            }
//                            
//                            BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
//                            System.out.println("storageName="+storageName);
//                            baseStorageInfo.setStorageName(storageName);
//                            baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
//                            baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
//                            baseStorageInfo.setStorageAttachType(storageAttachType);
//                            baseStorageInfo.setStorageAttachSize(storageAttachSize);
//                            baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
//                            baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize);
//                            baseStorageInfo.setStorageAttachFormat(storageAttachFormat);
//                            baseStorageInfo.setStorageAttachPort(storageAttachPort);
//                            baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
//                           if (StorageAttachMediumIsExists) {
//                                baseStorageInfo.setStrstate("可用");
//                            } else {
//                                baseStorageInfo.setStrstate("已损坏");
//                            }
//
//                            storage.add(baseStorageInfo);
//                        }
//                        controllerInfo.setStorage(storage);
//                    }
//
//                    controllers.add(controllerInfo);
//                    if (controllerType == 2) {
//                        scsi = controllerInfo;
//                        isHaveSCSI = 1;
//                        controllerChipSet_SCSI = controllerChipset;
//                    }
//                }

//                if (conList.get(i).getControllerName().equalsIgnoreCase("SAS Controller")) {
//                    String StorageControlerName = conList.get(i).getControllerName();  //"SCSI"
//                    int controllerType = conList.get(i).getControllerType();  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
//                    int controllerChipset = conList.get(i).getControllerChipset();
//
//                    ControllerInfo controllerInfo = new ControllerInfo();
//                    controllerInfo.setControllerName(StorageControlerName);
//                    controllerInfo.setControllerType(controllerType);
//                    controllerInfo.setControllerChipset(controllerChipset);
//                    List<BaseStorage> bsList = conList.get(i).getStorage();
//                    if (bsList != null && bsList.size() > 0) {
//                        int StorageCount = bsList.size();
//                        List<BaseStorageInfo> storage = new ArrayList();  //控制器下加载存储设备，光盘或硬盘
//                        for (int j = 0; j < StorageCount; j++) {
//                            String storageAttachMedium = bsList.get(j).getStorageName();  //"disk1"
//                            String[] sam = storageAttachMedium.split("/");
//                            String storageName = sam[sam.length - 1];
//                            
//      
//                            boolean StorageAttachMediumIsExists = bsList.get(j).isbStorageAttachMediumIsExists();
//
//                            int storageAttachType = bsList.get(j).getStorageType();  //存储设备类型取值1为硬盘，2为光盘
//                            long storageAttachSize = bsList.get(j).getSize();
//                            long StorageAttachUsedSize = bsList.get(j).getUsedSize();
//                            boolean StorageAttachIsPhysical = bsList.get(j).isPhysicsDrive();
//                            String storageAttachFormat = bsList.get(j).getFormat();  //存储设备格式
//                            int storageAttachPort = bsList.get(j).getPortNum();  //存储设备端口号，IDE取值0或1，SCSI取值0到14，SAS取值0-7
//                            int StorageAttachDevice = bsList.get(j).getDeviceNum();  //存储设备设备号，IDE控制器，取值0或1，SAS和SCSI取值0
//                            String attribute = null;
//                            if (controllerType == 1) {  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
//
//                                if (storageAttachPort == 0 && StorageAttachDevice == 0) {
//                                    attribute = "第一IDE控制器主通道";
//                                } else if (storageAttachPort == 0 && StorageAttachDevice == 1) {
//                                    attribute = "第一IDE控制器从通道";
//                                } else if (storageAttachPort == 1 && StorageAttachDevice == 0) {
//                                    attribute = "第二IDE控制器主通道";
//                                } else if (storageAttachPort == 1 && StorageAttachDevice == 1) {
//                                    attribute = "第二IDE控制器从通道";
//                                }
//                            } else if (controllerType == 2) {
//                                attribute = "SCSI 端口" + " " + storageAttachPort;  //注意：例子“SCSI端口 0”，此处SCSI端口与0之间的空格必须要，因为在其它出需要用空格占一位
//
//                            } else if (controllerType == 3) {
//                                attribute = "SAS 端口" + " " + storageAttachPort;  //注意：例子“SCSI端口 0”，此处SCSI端口与0之间的空格必须要，因为在其它出需要用空格占一位
//                            }
//                            
//                            BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
//                            System.out.println("storageName="+storageName);
//                            baseStorageInfo.setStorageName(storageName);
//                            baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
//                            baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
//                            baseStorageInfo.setStorageAttachType(storageAttachType);
//                            baseStorageInfo.setStorageAttachSize(storageAttachSize);
//                            baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
//                            baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize);
//                            baseStorageInfo.setStorageAttachFormat(storageAttachFormat);
//                            baseStorageInfo.setStorageAttachPort(storageAttachPort);
//                            baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
//                            if (StorageAttachMediumIsExists) {
//                                baseStorageInfo.setStrstate("可用");
//                            } else {
//                                baseStorageInfo.setStrstate("已损坏");
//                            }
//
//                            storage.add(baseStorageInfo);
//                        }
//                        controllerInfo.setStorage(storage);
//                    }
//
//                    controllers.add(controllerInfo);
//                    if (controllerType == 3) {
//                        sas = controllerInfo;
//                        isHaveSAS = 1;
//                        controllerChipSet_SAS = controllerChipset;
//                    }
//                }

            }
            if (isHaveIDE == 0) {
                     SystemOutPrintln.print_vm("isHaveIDE="+isHaveIDE);
                    notaddCD_IDE = true;
                    notaddDisk_IDE = true;
                }
                if (isHaveSCSI == 0) {
                     SystemOutPrintln.print_vm("CONTROLLER_TYPE_SCSI isHaveSCSI = 0 notaddDisk_SCSI = true;");
                       SystemOutPrintln.print_vm("isHaveSCSI="+isHaveSCSI);
                    notaddDisk_SCSI = true;
                }
                if (isHaveSAS == 0) {
                     SystemOutPrintln.print_vm("isHaveSAS="+isHaveSAS);
                    notaddDisk_SAS = true;
                }
                if (setting==false || userType != 2) {
                    notaddCD_IDE = true;
                    notaddDisk_IDE = true;
                     SystemOutPrintln.print_vm("CONTROLLER_TYPE_SCSI setting==false || userType != 2 notaddDisk_SCSI = true;");
                    notaddDisk_SCSI = true;
                    notaddDisk_SAS = true;
                }
        }else{
            notaddCD_IDE = true;
            notaddDisk_IDE = true;
            notaddDisk_SCSI = true;
            notaddDisk_SAS = true;
        }
        

        List<NetworkInterfaceCard> netList = vm.getNetworkInterfaceCard();
        if (netList != null && netList.size() > 0) {
            int nicCount = netList.size();
            getnicCount =nicCount; 
            network = new ArrayList();  //加载网卡
            for (int k = 0; k < nicCount; k++) {
                int vmNICNum = netList.get(k).getNicNum();
                boolean vmNICIsUsable = netList.get(k).isbVMNICIsUsable();
                String bindPhysicsNIC = netList.get(k).getBindPhysicsNicName();

                String[] nicNames = bindPhysicsNIC.split(" ");
                String vmNICName = nicNames[0];

                String macAddress = netList.get(k).getMacAddress();
//                int cableConnected = 1;
                String bindPhysicsNicIpAddress = netList.get(k).getBindPhysicsNicIpAddress();

                NetworkInterfaceCardInfo cardInfo = new NetworkInterfaceCardInfo();
                cardInfo.setVmNICNum(vmNICNum);
                cardInfo.setVmNICIsUsable(vmNICIsUsable);
                cardInfo.setVmNICName(res.get(basename, "tab1_network") + " " + vmNICNum + ":" + vmNICName);
                cardInfo.setBindPhysicsNIC(bindPhysicsNIC);
                cardInfo.setMacAddress(macAddress);
                cardInfo.setBoolCableConnected(netList.get(k).isCableconnected());
                cardInfo.setBindPhysicsNicIpAddress(bindPhysicsNicIpAddress);
                if (netList.get(k).isCableconnected()) {
                    cardInfo.setStrCableConnected(res.get(basename, "net_cableConnected_1"));
                } else {
                    cardInfo.setStrCableConnected(res.get(basename, "net_cableConnected_0"));
                }
                cardInfo.setBindPhysicsNicIpAddress(bindPhysicsNicIpAddress);
                cardInfo.setDei_modify(false);
                if(setting == false || userType != 2){
                    cardInfo.setDei_modify(true);
                }
                network.add(cardInfo);
            }
        }

        vmInfo = new VMInformation();
        vmInfo.setVmNAME(vmNAME);

        vmInfo.setVmUUID(vmUUID);
        vmInfo.setVmBaseFolderPath(vmBaseFolderPath);
        vmInfo.setBoot1(boot1);
        vmInfo.setBoot2(boot2);
        vmInfo.setBoot3(boot3);
        bootSequences = new ArrayList();
        bootSequences.add(new BootSequen(resources.get("vm.vm_vminfotab", "BootSequence_" + boot1), resources.get("vm.vm_vminfotab", "boot_" + boot1)));
        bootSequences.add(new BootSequen(resources.get("vm.vm_vminfotab", "BootSequence_" + boot2), resources.get("vm.vm_vminfotab", "boot_" + boot2)));
        bootSequences.add(new BootSequen(resources.get("vm.vm_vminfotab", "BootSequence_" + boot3), resources.get("vm.vm_vminfotab", "boot_" + boot3)));
        vmInfo.setBootSequences(bootSequences);
        vmInfo.setOsType(osType);
        vmInfo.setCpuCoreNum(cpuCoreNum);
        vmInfo.setMemorySize(memorySize);
        vmInfo.setSupportIOAPIC(supportIOAPIC);
        vmInfo.setSupportEFI(supportEFI);
        vmInfo.setSupportUTCTime(supportUTCTime);
        vmInfo.setRemoteAddress(remoteAddress);
        vmInfo.setRemotePor(remotePort);
        vmInfo.setRemoteIPAddressUsable(RemoteIPAddressUsable);
        vmInfo.setRemoteTimeOut(remoteTimeOut);
        vmInfo.setRemoteAuthentication(remoteAuthentication);
        vmInfo.setRemoteMultipleConnections(remoteMultipleConnections);
        vmInfo.setController(controllers);
        vmInfo.setNetworkInterfaceCard(network);
        vmInfo.setNotaddNet(false);
         SystemOutPrintln.print_vm("NotaddNet="+vmInfo.isNotaddNet());
         SystemOutPrintln.print_vm("getnicCount="+getnicCount);
        if (getnicCount == 8) { //最多能添加8块网卡，当已经有8快网卡时，则不能再添加
            vmInfo.setNotaddNet(true);
            SystemOutPrintln.print_vm("NotaddNet="+vmInfo.isNotaddNet());
        }else {
             vmInfo.setNotaddNet(false);
        }
        if (setting == false || userType != 2) {
             SystemOutPrintln.print_vm("setting="+setting);
              SystemOutPrintln.print_vm("userType="+userType);
            vmInfo.setNotaddNet(true);
            SystemOutPrintln.print_vm("NotaddNet="+vmInfo.isNotaddNet());
        }
          SystemOutPrintln.print_vm("NotaddNet="+vmInfo.isNotaddNet());
        vmInfo.setVmState(vm.getVmState());
        vmInfo.setNotaddCD_IDE(notaddCD_IDE);
        vmInfo.setNotaddDisk_IDE(notaddDisk_IDE);
         SystemOutPrintln.print_vm("notaddDisk_SCSI="+notaddDisk_SCSI);
        vmInfo.setNotaddDisk_SCSI(notaddDisk_SCSI);
        vmInfo.setNotaddDisk_SAS(notaddDisk_SAS);
//        vmInfo.s?????????
        vmNameStr = vmNAME;
//        String typeName = null;
//        OSName[] osName = GetOperatingSystem.initOSResource();
//        int count = osName.length;
//        for (int i = 0; i < count; i++) {
//            List<OperatingSystem> opeSystem = osName[i].getOsType();
//            int osTypeCount = opeSystem.size();
//            for (int j = 0; j < osTypeCount; j++) {
//               
//                if (osType.toString().equals(opeSystem.get(j).getID().toString())) {
//                     System.out.println("opeSystem.get(j).getID().toString()="+opeSystem.get(j).getID().toString());
//                    typeName = opeSystem.get(j).getTypeName();
//                    System.out.println("ostypeName="+typeName);
//                }
//            }
//        }
//        osTypeStr = typeName;
        osTypeStr = osType;
        osType_init = osType;
        cpuCoreNumStr = cpuCoreNum;
        memorySizeStr = memorySize + "";
        supportIOAPICStr = supportIOAPIC;
        bootSequencesStr = bootSequences;
        remoteAddressStr = remoteAddress;
        remoteMultipleConnectionsStr = remoteMultipleConnections;
        remotePort_init = remotePort + "";
        remotePortStr = remotePort + "";
        supportEFIStr = supportEFI;
        SystemOutPrintln.print_vm(vm.getVmName() + "虚拟机状态=" + vm.getVmState());
//        vmInfo.setState(resources.get("vm.vm_vminfotable", "vm_type_" + vm.getVmState()));
        if (ide != null) {
            vmInfo.setIdeController(ide);
        }
        if (scsi != null) {
            vmInfo.setScsiController(scsi);
        }
        if (sas != null) {
            vmInfo.setSasController(sas);
        }
        this.getControllerTypeList();
        vmInfo.setControllerTypeList(controllerTypeList);


    }

    public List getControllerTypeList() {
        controllerTypeList = new ArrayList<ControllerType>();
        con = new ControllerType();
        con.setControllerName("IDE");
        con.setControllerType(1);
//        if(ide != null){
//            isHaveIDE = 1;
//        }
        SystemOutPrintln.print_vm("isHaveIDE=" + isHaveIDE);
        con.setIsHave(isHaveIDE);
        con.setAdd_del(false);
        if (con.getIsHave() == 0) {
            con.setOperation(res.get(basename, "controller_0"));
            con.setIcon("../resources/common/picture/add.png");
            
        } else {
            con.setOperation(res.get(basename, "controller_1"));
            con.setIcon("../resources/common/picture/delete.png");
            if (isHaveIDE_children == 1) {  //有子结点
                con.setAdd_del(true);
            }
        }
//        con.setOperation(setResource("controller_" + con.getIsHave()));
        if (isHaveIDE == 0) {
            con.setEdit(true);
        } else {
            con.setEdit(false);
        }
        if (setting==false || userType != 2) {
            con.setAdd_del(true);
            con.setEdit(true);
        }
        con.setControllerChipset(controllerChipSet_IDE);
        controllerTypeList.add(con);

        con = new ControllerType();
        con.setControllerName("SCSI");
        con.setControllerType(2);
//        if(scsi != null){
//            isHaveSCSI = 1;
//        }
        con.setIsHave(isHaveSCSI);
        con.setAdd_del(false);
        if (isHaveSCSI == 0) {
            con.setEdit(true);
        } else {
            con.setEdit(false);
        }
        if (con.getIsHave() == 0) {
            con.setOperation(res.get(basename, "controller_0"));
             con.setIcon("../resources/common/picture/add.png");
        } else {
            con.setOperation(res.get(basename, "controller_1"));
             con.setIcon("../resources/common/picture/delete.png");
            if (isHaveSCSI_children == 1) {  //有子结点
                con.setAdd_del(true);
            }

        }
        if (setting==false || userType != 2) {
            con.setAdd_del(true);
            con.setEdit(true);
        }
//        con.setOperation(setResource("controller_" + con.getIsHave()));
        con.setControllerChipset(controllerChipSet_SCSI);
        controllerTypeList.add(con);

        con = new ControllerType();
        con.setControllerName("SAS");
        con.setControllerType(3);
//        if(sas != null){
//            isHaveSAS = 1;
//        }
        con.setIsHave(isHaveSAS);
        con.setAdd_del(false);
        if (isHaveSAS == 0) {
            con.setEdit(true);
        } else {
            con.setEdit(false);
        }
        if (con.getIsHave() == 0) {
            con.setOperation(res.get(basename, "controller_0"));
             con.setIcon("../resources/common/picture/add.png");
        } else {
            con.setOperation(res.get(basename, "controller_1"));
             con.setIcon("../resources/common/picture/delete.png");
            if (isHaveSAS_children == 1) {  //有子结点
                con.setAdd_del(true);
            }

        }
        if (setting==false || userType != 2) {
            con.setAdd_del(true);
            con.setEdit(true);
        }
//        con.setOperation(setResource("controller_" + con.getIsHave()));
        con.setControllerChipset(controllerChipSet_SAS);
        controllerTypeList.add(con);


        return controllerTypeList;
    }

    public VMInformation getVmInfo() {
        return vmInfo;
    }

    public void setVmInfo(VMInformation vmInfo) {
        this.vmInfo = vmInfo;
    }

    public String getVmNameStr() {
        return vmNameStr;
    }

    public void setVmNameStr(String vmNameStr) {
        this.vmNameStr = vmNameStr;
    }

    public String getOsTypeStr() {
        return osTypeStr;
    }

    public void setOsTypeStr(String osTypeStr) {
        this.osTypeStr = osTypeStr;
    }

    public int getCpuCoreNumStr() {
        return cpuCoreNumStr;
    }

    public void setCpuCoreNumStr(int cpuCoreNumStr) {
        this.cpuCoreNumStr = cpuCoreNumStr;
    }

    public boolean isSupportIOAPICStr() {
        return supportIOAPICStr;
    }

    public void setSupportIOAPICStr(boolean supportIOAPICStr) {
        this.supportIOAPICStr = supportIOAPICStr;
    }

    public boolean isRemoteMultipleConnectionsStr() {
        return remoteMultipleConnectionsStr;
    }

    public void setRemoteMultipleConnectionsStr(boolean remoteMultipleConnectionsStr) {
        this.remoteMultipleConnectionsStr = remoteMultipleConnectionsStr;
    }

    public String getMemorySizeStr() {
        return memorySizeStr;
    }

    public void setMemorySizeStr(String memorySizeStr) {
        this.memorySizeStr = memorySizeStr;
    }

    public List<BootSequen> getBootSequencesStr() {
        return bootSequencesStr;
    }

    public void setBootSequencesStr(List<BootSequen> bootSequencesStr) {
        SystemOutPrintln.print_vm(bootSequencesStr.get(0).getType());
        this.bootSequencesStr = bootSequencesStr;
    }

    public String getRemoteAddressStr() {
        return remoteAddressStr;
    }

    public void setRemoteAddressStr(String remoteAddressStr) {
        this.remoteAddressStr = remoteAddressStr;
    }

    public String getRemotePortStr() {
        return remotePortStr;
    }

    public void setRemotePortStr(String remotePortStr) {
        this.remotePortStr = remotePortStr;
    }

    public boolean isCantSys() {
        return cantSys;
    }

    public void setCantSys(boolean cantSys) {
        this.cantSys = cantSys;
    }

    public String editSystem() {

        SystemOutPrintln.print_vm("配置虚拟机");
        int boot1 = 1;
        int boot2 = 2;
        int boot3 = 3;
        int size = bootSequencesStr.size();

        SystemOutPrintln.print_vm("size=" + size);
        for (int i = 0; i < bootSequencesStr.size(); i++) {
            SystemOutPrintln.print_vm("bootSequencesStr.get(i)=" + bootSequencesStr.get(i).getType());

        }

        String boot1_String = bootSequencesStr.get(0).getType();
        if (boot1_String.equalsIgnoreCase(res.get(basename, "BootSequence_1"))) {
            boot1 = 1;
        } else if (boot1_String.equalsIgnoreCase(res.get(basename, "BootSequence_2"))) {
            boot1 = 2;
        } else if (boot1_String.equalsIgnoreCase(res.get(basename, "BootSequence_3"))) {
            boot1 = 3;
        }

        String boot2_String = bootSequencesStr.get(1).getType();
        if (boot2_String.equalsIgnoreCase(res.get(basename, "BootSequence_1"))) {
            boot2 = 1;
        } else if (boot2_String.equalsIgnoreCase(res.get(basename, "BootSequence_2"))) {
            boot2 = 2;
        } else if (boot2_String.equalsIgnoreCase(res.get(basename, "BootSequence_3"))) {
            boot2 = 3;
        }

        String boot3_String = bootSequencesStr.get(2).getType();
        if (boot3_String.equalsIgnoreCase(res.get(basename, "BootSequence_1"))) {
            boot3 = 1;
        } else if (boot3_String.equalsIgnoreCase(res.get(basename, "BootSequence_2"))) {
            boot3 = 2;
        } else if (boot3_String.equalsIgnoreCase(res.get(basename, "BootSequence_3"))) {
            boot3 = 3;
        }
        SystemOutPrintln.print_vm("boot1=" + boot1 + ",boot2=" + boot2 + ",boot3=" + boot3);

        SystemOutPrintln.print_vm("memorySizeStr=" + memorySizeStr);
        int memorySize = hostMemorySize;
        if (memorySizeStr.matches("\\d{1,8}")) {
            int n = Integer.parseInt(memorySizeStr);
            SystemOutPrintln.print_vm("n=" + n);
            if (!(n >= 4 && n <= memorySize)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "memorysize_tip1") + memorySize + res.get(basename, "memorysize_tip2"), ""));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "memorysize_tip1") + memorySize + res.get(basename, "memorysize_tip2"), ""));
            return null;
        }


//        if(jTextField6.getText()!=null &&(!jTextField6.getText().equalsIgnoreCase("")) ){
//            if (!jTextField6.getText().matches("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$")) {
//                Constants.showWarningMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/vbox/treenode/resources/VM").getString("IP地址格式错误。"));
//                return;
//
//            }
//        }


        if (remotePortStr.matches("\\d{4,5}")) {
            int n = Integer.parseInt(remotePortStr);
            if (!(n >= 5000 && n <= 65535)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "remoteportlimit"), ""));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "remoteportlimit"), ""));
            return null;
        }

        if (!remotePortStr.equalsIgnoreCase(remotePort_init)) {

            boolean flag = false;
            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            List<VirtualMachine> maps = vmface.getVMList();
            if (maps != null && maps.size() > 0) {
                SystemOutPrintln.print_vm("map is not null.");
//                Set<String> keys = map.keySet();
//                Iterator<String> iterator = keys.iterator();
                RegisterVMs vm = null;
                for (VirtualMachine map : maps) {
                    String key = map.getVmName();
                    SystemOutPrintln.print_vm("vmName=" + key);
//                    int[] values = map.get(key);
                    int vmState = map.getVmState();
                    int romotePort =map.getRemotePort();
                    int memorysize =map.getMemorySize();
                    if (remotePortStr.trim().equalsIgnoreCase(String.valueOf(romotePort))) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "remoteportexit"), ""));
                        return null;
                    }
                }
            }
        }


        //发送系统设置的命令
        String vrdeConnection = 0 + "";
        if (remoteMultipleConnectionsStr) {
            vrdeConnection = 1 + "";
        } else {
            vrdeConnection = 0 + "";
        }
        String vrdeAddress = remoteAddressStr;
        if (vrdeAddress.equalsIgnoreCase(res.get(basename, "anyip"))) {
            vrdeAddress = null;
        }

        String ioacpiIsOn = 0 + "";
        if (supportIOAPICStr) {
            ioacpiIsOn = 1 + "";
        }

        boolean isSupportEFI = supportEFIStr;
        String efi = 0 + "";
        if (isSupportEFI) {
            efi = 1 + "";
        }
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean systemConfig = vmface.systemConfig(vmNane, cpuCoreNumStr + "", memorySizeStr, boot1, boot2, boot3, ioacpiIsOn, efi, vrdeAddress, remotePortStr, vrdeConnection);
        String returnStr = null;
        if (!systemConfig) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "systemconfig_fail"), ""));
            returnStr = null;
        } else {
            String param = "vmName=" + vmNane;
            returnStr = "vm_vminfotab?faces-redirect=true" + param;
        }


        return returnStr;

    }

    public String getAccordionActive1() {
        return accordionActive1;
    }

    public void setAccordionActive1(String accordionActive1) {
        this.accordionActive1 = accordionActive1;
    }
    
}
