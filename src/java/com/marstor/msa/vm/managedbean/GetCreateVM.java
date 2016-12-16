/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.bean.VolumeGroupInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.vm.model.BootSequen;
import com.marstor.msa.vm.model.ControllerInfo;
import com.marstor.msa.vm.model.ControllerType;
import com.marstor.msa.vm.model.NetworkInterfaceCardInfo;
import com.marstor.msa.vm.model.OSName;
import com.marstor.msa.vm.model.OperatingSystem;
import com.marstor.msa.vm.model.VMInformation;
import com.marstor.msa.common.util.GetOperatingSystem;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.bean.BaseStorage;
import com.marstor.msa.vbox.bean.Controller;
import com.marstor.msa.vbox.bean.HDInfo;
import com.marstor.msa.vbox.bean.HardDisk;
import com.marstor.msa.vbox.bean.HostHardware;
import com.marstor.msa.vbox.bean.VirtualMachine;
import com.marstor.msa.vbox.web.MsaVMInterface;
import com.marstor.msa.vm.model.RegisterVMs;
import com.marstor.msa.vm.model.VirtualDiskName;
import com.marstor.msa.vm.model.VirtualDiskNameModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "getCreateVM")
@ViewScoped
public class GetCreateVM implements Serializable{

    private String vmNAME;  //虚拟机名
    private String diskName = "NewHardDisk1";
    private String selectDiskName;

    private String vmBaseFolder;//虚拟机路径
    private String osType = "Microsoft Windows";//虚拟机操作系统类型
    private String vmVersion;//虚拟机操作系统版本
    private int cpuCoreNum;//CPU核心数
    private String memorySize = 256+"";//内存大小
    private int boot1 = 1;  //启动顺序，数值型，1：光盘、2：硬盘、3：网络
    private int boot2 = 2;  //启动顺序，数值型，1：光盘、2：硬盘、3：网络
    private int boot3 = 3;  //启动顺序，数值型，1：光盘、2：硬盘、3：网络
    private List<ControllerInfo> controller = new ArrayList();  //控制器列表
    private String vrdeAddress;  //VRDE远程登录IP地址
//    private String vrdeAuthType;
    private String vrdePort = 5000 + "";  //远程登录端口号
    private boolean vrdeMultipleConnection = false;  //设置远程登录多连接,0代表关闭，1代表开启
    private String volume ;  //卷组名
  
    VMInformation vmInfo;
    private ControllerInfo ide = null;
    private ControllerInfo scsi = null;
    private ControllerInfo sas = null;
    private int isHaveIDE = 0;
    private int isHaveSCSI = 0;
    private int isHaveSAS = 0;
    List<NetworkInterfaceCardInfo> network;
    private List<BootSequen> bootSequences;  //启动顺序
    public List<ControllerType> controllerTypeList;
    ControllerType con;
    private Map<String, String> osTypes = new HashMap<String, String>();
    private Map<String, Map<String, String>> vmVersionsData = new HashMap<String, Map<String, String>>();
    private Map<String, String> vmVersions = new HashMap<String, String>();
    public List<String> vmVersionList;
    private int diskSize = 20;  //单位为GB
     private String diskSizen = 20 + "";  //单位为GB
    private String diskAddress = null;
    private int diskType = 0; //0代表建动态硬盘，1代表建固定大小硬盘
//    private String city;
//    private String suburb;
    List<VMInformation> vmInfoList;
    
    public List<String> volumeList; //卷组列表
    
    
    public boolean getVolumesState = true;  //获得卷组信息是否成功
    private String path = null;
    public int hostCpuCoreNum = 0;
    public int hostMemorySize = 0;
    HostHardware hostHardwareInfo = null;
    public List<String> cpuCoreNumList; //cpul列表
    public String memorySizeTip = "";
    public List<String> ipList; //ip地址列表
    public int vrdeMultipleConnection_int = 0;  //不允许
    public List<VirtualDiskName> virDiskList;
    private VirtualDiskName selectedVirDisk;
    public boolean notFile = false;
    public boolean notName = false;
    public String createDiskType = 0 + "";
    private String diskNameStr = null;
    private int diskSizeInt = 1024;  //单位为MB
    VolumeGroupInformation[] pools;
    public String virDiskSizetip;
    
    public VirtualDiskNameModel vdsModel;
    
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_CreatVMTable";

    /**
     * Creates a new instance of GetCreateVM
     */
    public GetCreateVM() {
        selectDiskName = res.get(basename, "please");
        virDiskSizetip = res.get(basename, "virdisksizetip");
        
        this.getOS();
        this.handleCityChange();
        vmVersion = "Windows 2003";
        
        volumeList = new ArrayList(); //cpu列表
        setvolumeInfoList();
        
        initCPUAndMemorySize();
        initIPList();
        notFile = true;
    notName = false;
    }
    
     public void setvolumeInfoList(){      
//         ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
//         String[] volumeNames = zfs.getVolumeNames();
         VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
         pools = volumeIn.getAllVolumeGroup();
         if (pools != null && pools.length > 0) {
             for (int i = 0; i < pools.length; i++) {
                 volumeList.add(pools[i].getName());
             }
         } else {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "getpool_fail"), ""));
             getVolumesState = false;
             return;
         }
            
            
      
    }
    private void getOS(){
        OSName[] osName = GetOperatingSystem.initOSResource();
        int count =osName .length;
        for (int i=0; i<count; i++){
            osTypes.put(osName[i].getOsName(), osName[i].getOsName());
        }
        String name = null;
        String typeName = null;
         Map<String, String> suburbsIstanbul = null;
        for (int i = 0; i < count; i++) {
            name = osName[i].getOsName();
            List<OperatingSystem> opeSystem = osName[i].getOsType();
            int osTypeCount = opeSystem.size();
            suburbsIstanbul = new HashMap<String, String>();
        
            for (int j = 0; j < osTypeCount; j++) {
                typeName = opeSystem.get(j).getTypeName();
                suburbsIstanbul.put(typeName, typeName);
            }
            vmVersionsData.put(name, suburbsIstanbul);
        }
        
        
    }
    
    
    public void initCPUAndMemorySize(){
        cpuCoreNumList = new ArrayList();
         MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        hostHardwareInfo = vmface.getHostHardwareInfo();
        if (hostHardwareInfo != null) {
            hostCpuCoreNum = hostHardwareInfo.getCpuNum();  //获取物理主机的CPU数量
            SystemOutPrintln.print_vm("hostCpuCoreNum="+hostCpuCoreNum);
            hostMemorySize = hostHardwareInfo.getMemorySize();  //获取物理主机的内存大小
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "gethost_fail"), ""));
            return;
        }
        int vmCPUNum = hostCpuCoreNum*2;
        SystemOutPrintln.print_vm("vmCPUNum="+vmCPUNum);
        if(vmCPUNum > 32){
            vmCPUNum = 32;
        }
        for (int i=1;i<=vmCPUNum;i++){
            cpuCoreNumList.add(i+"");
        }       
      
        memorySizeTip = res.get(basename, "rangetip")+ hostMemorySize +"MB)";

    }
    public String createVM_Info() {
         //虚拟电脑名称和类型
        
        path = "/" + volume + "" + "/VM/" + vmNAME;
        if (vmNAME == null || vmNAME.equalsIgnoreCase("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "vmname_null"), ""));
            return null;
        }

        if (vmNAME.length() < 21) {
            if (!vmNAME.matches("[a-zA-Z0-9]+")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "vmname_limit"), ""));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "vmname_limit"), ""));
            return null;
        }

        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        List<VirtualMachine> maps = vmface.getVMList();
        if (maps != null && maps.size() > 0) {
//            Set<String> keys = map.keySet();
//            Iterator<String> iterator = keys.iterator();
            RegisterVMs vm = null;
            for (VirtualMachine map : maps) {
                String key = map.getVmName();
                if (key.equals(vmNAME)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "vmname_exit"), ""));
                    return null;
                }

            }
        }
        
        if (ValidateUtility.checkSize(memorySize.trim(), false, hostMemorySize+1) && Integer.valueOf(memorySize.trim())>3) {
     
        }else{
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "memorysizetip1")+hostMemorySize+res.get(basename, "memorysizetip2"), ""));
            return null;
        }
        
        //设置虚拟电脑硬盘、
        if (createDiskType.equals("0")) {
            if (diskName.length() < 21) {
                if (!diskName.matches("[a-zA-Z0-9]+")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "disk_limit"), ""));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get(basename, "disk_limit"), ""));
                return null;
            }

            diskNameStr = diskName + ".vdi";  //注意！！！！！！！！

//                 if(jPCreateVMHardDisk_3.getDiskDynamic()){
//                     diskType = 0;
//                 }
//                 if(jPCreateVMHardDisk_3.getDiskStatic()){
//                     diskType = 1;
//                 }
            if (!ValidateUtility.checkSize(diskSizen.trim(), false, 2049)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "disksize_rangetip"), ""));
                return null;
            } else {
      
                diskSize = Integer.valueOf(diskSizen);
                
            }
            diskSizeInt = diskSize * 1024;  //注意！！！！！！！！
            if (diskSizeInt == 0) {
                diskSize = 1;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "disksize_null"), ""));
                return null;
            }
            diskAddress = path + "/" + diskNameStr;
        }

        if (createDiskType.equals("1")) {
            diskAddress = selectDiskName;
            if (diskAddress == null || diskAddress.equalsIgnoreCase("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "seletedisk"), ""));
                return null;
            }
        }

        if (diskType == 1) {
            String availableSize = null;
            for (int i = 0; i < pools.length; i++) {
                if (volume.equalsIgnoreCase(pools[i].getName())) {
                    availableSize = pools[i].getAvailableSize();
//                         System.out.println("availableSize="+availableSize);
                }
            }
            double availableDisk = 0;
            String diskUnit = "G";
            if (availableSize != null && (!availableSize.equalsIgnoreCase("")) && (!availableSize.equalsIgnoreCase("0"))) {
                double available = Double.valueOf(availableSize.substring(0, availableSize.length() - 1));
                diskUnit = availableSize.charAt(availableSize.length() - 1) + "";
                if (diskUnit.equalsIgnoreCase("B")) {
                    availableDisk = available / (1024 * 1024 * 1024);
                } else if (diskUnit.equalsIgnoreCase("K")) {
                    availableDisk = available / (1024 * 1024);
                } else if (diskUnit.equalsIgnoreCase("M")) {
                    availableDisk = available / 1024;
                } else if (diskUnit.equalsIgnoreCase("G")) {
                    availableDisk = available;
                } else if (diskUnit.equalsIgnoreCase("T")) {
                    availableDisk = available * 1024;
                }

            }

            if (availableDisk < diskSize) {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get(basename, "less_disksize"), ""));
                return null;
            }
        }
            
        
        //远程桌面
        String strPort = vrdePort + "";
        if (strPort.matches("\\d{4,5}")) {
            int n = Integer.parseInt(vrdePort);
            if (!(n >= 5000 && n <= 65535)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "port_range"), ""));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "port_range"), ""));
            return null;
        }
        
//         MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
//        Map<String, int[]> map = vmface.getVMList();
        if (maps != null && maps.size() > 0) {
//            Set<String> keys = map.keySet();
//            Iterator<String> iterator = keys.iterator();
            RegisterVMs vm = null;
            for (VirtualMachine map : maps) {
                String key = map.getVmName();
//                int[] values = map.get(key);
                int vmState = map.getVmState();
                int romotePort = map.getRemotePort();
                if (strPort.equalsIgnoreCase(String.valueOf(romotePort))) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "port_exit"), ""));
                    return null;

                }

            }
        }

        String returnStr = sendMeaaage();
      
     
        
//        //test1
//        ide = null;
//        scsi = null;
//        sas = null;
//
//        isHaveIDE = 0;
//        isHaveSCSI = 0;
//        isHaveSAS = 0;
//
//        String vmNAME = this.vmNAME;
//        String vmUUID = "123";//虚拟机UUID
//        String vmBaseFolderPath = vmBaseFolder;//虚拟机路径
//        int boot1 = this.boot1;
//        int boot2 = this.boot2;
//        int boot3 = this.boot3;
//        String osType = this.vmVersion;//虚拟机操作系统类型
//        int cpuCoreNum = this.cpuCoreNum;//CPU核心数
//        int memorySize = this.memorySize;//内存大小
//        int iIOAPIC = 0;
//        boolean supportIOAPIC = false;  //是否开启IOACPI
//        if (iIOAPIC == 0) {  //0代表不开启，1代表开启，
//            supportIOAPIC = false;
//        } else {
//            supportIOAPIC = true;
//        }
//        int iEFI = 0;
//        boolean supportEFI = false;  //是否开启EFI
//        if (iEFI == 0) {  //0代表不开启，1代表开启
//            supportEFI = false;
//        } else {
//            supportEFI = true;
//        }
//        int iUTCTime = 0;
//        boolean supportUTCTime = false;  //是否开启UTC时钟
//        if (iUTCTime == 0) {  //0代表不开启，1代表开启
//            supportUTCTime = false;
//        } else {
//            supportUTCTime = true;
//        }
//        String remoteAddress = this.vrdeAddress;//远程登录ip
//        int remotePort = Integer.valueOf(this.vrdePort);//远程登录端口号
//        int RemoteIPAddressUsable = 0;//此远程登录ip是否存在
//        int remoteTimeOut = 500;//超时时间
//        String remoteAuthentication = "ask";//认证方式
//        int iMultipleConnections = 1;
//        boolean remoteMultipleConnections = vrdeMultipleConnection;//是否允许远程连接多连接
////        iMultipleConnections = vrdeMultipleConnection;
////        if (iMultipleConnections == 0) {
////            remoteMultipleConnections = false;
////        } else {
////            remoteMultipleConnections = true;
////        }
//
//        int controllerCount = 1;
//        controller = new ArrayList();  //加载控制器
//
//
////            XMLParser childParser = returnXML.createXMLParser("MSA/ReturnValue/StorageInfo/StorageControler",i);
//        String StorageControlerName = "IDE";
//        int controllerType = 1;  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
//        int controllerChipset = 1;
//
//        ControllerInfo controllerInfo = new ControllerInfo();
//        controllerInfo.setControllerName(StorageControlerName);
//        controllerInfo.setControllerType(controllerType);
//        controllerInfo.setControllerChipset(controllerChipset);
//
//        int StorageCount = 1;
//        List<BaseStorageInfo> storage = new ArrayList();  //控制器下加载存储设备，光盘或硬盘
//        for (int j = 0; j < StorageCount; j++) {
////                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
//            String storageAttachMedium = "emptydrive";
//            int StorageAttachMediumIsExists = 1;
//            int storageAttachType = 2;  //存储设备类型取值1为硬盘，2为光盘
//            long storageAttachSize = 20;
//            long StorageAttachUsedSize = 20;
//            int StorageAttachIsPhysical = (int) 1;
//            String storageAttachFormat = "Normal(VDI)";  //存储设备格式
//            int storageAttachPort = 0;  //存储设备端口号，IDE取值0或1，SCSI取值0到14，SAS取值0-7
//            int StorageAttachDevice = 0;  //存储设备设备号，IDE控制器，取值0或1，SAS和SCSI取值0
//
//            BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
//            baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
//            baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
//            baseStorageInfo.setStorageAttachType(storageAttachType);
//            baseStorageInfo.setStorageAttachSize(storageAttachSize);
//            baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
//            baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize);
//            baseStorageInfo.setStorageAttachFormat(storageAttachFormat);
//            baseStorageInfo.setStorageAttachPort(storageAttachPort);
//            baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
//            baseStorageInfo.setStrstate(this.setResource("Storage_deviceState_1"));
//
//            storage.add(baseStorageInfo);
//        }
//
//        controllerInfo.setStorage(storage);
//        controller.add(controllerInfo);
//
//        if (controllerType == 1) {
//            ide = controllerInfo;
//            isHaveIDE = 1;
//
//        }
//
//
//
////            XMLParser childParser = returnXML.createXMLParser("MSA/ReturnValue/StorageInfo/StorageControler",i);
//        String StorageControlerName1 = "SCSI";
//        int controllerType1 = 2;  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
//        int controllerChipset1 = 1;
//
//        ControllerInfo controllerInfo1 = new ControllerInfo();
//        controllerInfo1.setControllerName(StorageControlerName1);
//        controllerInfo1.setControllerType(controllerType1);
//        controllerInfo1.setControllerChipset(controllerChipset1);
//
//        int StorageCount1 = 1;
//        List<BaseStorageInfo> storage1 = new ArrayList();  //控制器下加载存储设备，光盘或硬盘
//        for (int j = 0; j < StorageCount1; j++) {
////                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
//            String storageAttachMedium = this.vmNAME;
//            int StorageAttachMediumIsExists = 1;
//            int storageAttachType = 1;  //存储设备类型取值1为硬盘，2为光盘
//            long storageAttachSize = 20;
//            long StorageAttachUsedSize = 20;
//            int StorageAttachIsPhysical = (int) 1;
//            String storageAttachFormat = "Normal(VDI)";  //存储设备格式
//            int storageAttachPort = 0;  //存储设备端口号，IDE取值0或1，SCSI取值0到14，SAS取值0-7
//            int StorageAttachDevice = 0;  //存储设备设备号，IDE控制器，取值0或1，SAS和SCSI取值0
//
//            BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
//            baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
//            baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
//            baseStorageInfo.setStorageAttachType(storageAttachType);
//            baseStorageInfo.setStorageAttachSize(storageAttachSize);
//            baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
//            baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize);
//            baseStorageInfo.setStorageAttachFormat(storageAttachFormat);
//            baseStorageInfo.setStorageAttachPort(storageAttachPort);
//            baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
//            baseStorageInfo.setStrstate(this.setResource("Storage_deviceState_1"));
//
//            storage1.add(baseStorageInfo);
//        }
//
//        controllerInfo1.setStorage(storage1);
//        controller.add(controllerInfo1);
//        if (controllerType1 == 2) {
//            scsi = controllerInfo1;
//            isHaveSCSI = 1;
//        }
//
//
//
//
//
//        int nicCount = 1;
//        network = new ArrayList();  //加载网卡
//        for (int i = 0; i < nicCount; i++) {
//            int vmNICNum = 1;
//            int vmNICIsUsable = 1;
//            String vmNICName = "network1";
//            String macAddress = "123456";
//            int cableConnected = 1;
//            String bindPhysicsNicIpAddress = "BindPhysicsIPAddress1";
//
//            NetworkInterfaceCardInfo cardInfo = new NetworkInterfaceCardInfo();
//            cardInfo.setVmNICNum(vmNICNum);
//            cardInfo.setVmNICIsUsable(vmNICIsUsable);
//            cardInfo.setVmNICName(vmNICName);
//            cardInfo.setMacAddress(macAddress);
//            cardInfo.setCableConnected(cableConnected);
//            cardInfo.setStrCableConnected(setResource("net_cableConnected_" + cardInfo.getCableConnected()));
//            cardInfo.setBindPhysicsNicIpAddress(bindPhysicsNicIpAddress);
//            network.add(cardInfo);
//        }
//
//
//        vmInfo = new VMInformation();
//        vmInfo.setVmNAME(vmNAME);
//        vmInfo.setVmUUID(vmUUID);
//        vmInfo.setVmBaseFolderPath(vmBaseFolderPath);
//        vmInfo.setBoot1(boot1);
//        vmInfo.setBoot2(boot2);
//        vmInfo.setBoot3(boot3);
//        bootSequences = new ArrayList<BootSequence>();
//        bootSequences.add(new BootSequence(this.setResource("BootSequence_" + boot1), this.setResource("boot_" + boot1)));
//        bootSequences.add(new BootSequence(this.setResource("BootSequence_" + boot2), this.setResource("boot_" + boot2)));
//        bootSequences.add(new BootSequence(this.setResource("BootSequence_" + boot3), this.setResource("boot_" + boot3)));
//        vmInfo.setBootSequences(bootSequences);
//        vmInfo.setOsType(osType);
//        vmInfo.setCpuCoreNum(cpuCoreNum);
//        vmInfo.setMemorySize(memorySize);
//        vmInfo.setSupportIOAPIC(supportIOAPIC);
//        vmInfo.setSupportEFI(supportEFI);
//        vmInfo.setSupportUTCTime(supportUTCTime);
//        vmInfo.setRemoteAddress(remoteAddress);
//        vmInfo.setRemotePor(remotePort);
//        vmInfo.setRemoteIPAddressUsable(RemoteIPAddressUsable);
//        vmInfo.setRemoteTimeOut(remoteTimeOut);
//        vmInfo.setRemoteAuthentication(remoteAuthentication);
//        vmInfo.setRemoteMultipleConnections(remoteMultipleConnections);
//        vmInfo.setController(controller);
//        vmInfo.setNetworkInterfaceCard(network);
//        vmInfo.setVmState(1);
//        vmInfo.setState(setResource("vm_type_" + vmInfo.getVmState()));
//        if (ide != null) {
//            vmInfo.setIdeController(ide);
//        }
//        if (scsi != null) {
//            vmInfo.setScsiController(scsi);
//        }
//        if (sas != null) {
//            vmInfo.setSasController(sas);
//        }
//        this.getControllerTypeList();
//        vmInfo.setControllerTypeList(controllerTypeList);
//
//       FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
//       VMList  share = (VMList) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{vMList}", VMList.class).getValue(context.getELContext());  
//        
//        vmInfoList = share.getVmInfoList();
//        if(vmInfoList == null){
//            vmInfoList = new ArrayList();
//            vmInfoList.add(vmInfo);
//        }else{
//            vmInfoList.add(vmInfo);
//        }
//        for(int i=0;i<vmInfoList.size();i++){
//            System.out.println("***************"+vmInfoList.get(i).getVmNAME());
//        }
        
        
        
        
        
        return returnStr;
        
    }
    //发送创建虚拟机命令
    public String sendMeaaage() {

//            int timeOut =  node.socket.getSocket().getSoTimeout();
//            node.socket.getSocket().setSoTimeout(0);
        if (createDiskType.equals("0")) {
            //发送创建虚拟硬盘的命令
            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            HardDisk hd = new HardDisk();
            hd.setStorageName(diskAddress);
            hd.setSize(diskSizeInt);
            if (diskType == 1) {
                hd.setDiskIsStatic(true);
            } else {
                hd.setDiskIsStatic(false);
            }
            boolean createVMDisk = vmface.createDisk(hd);
            if (!createVMDisk) {
                SystemOutPrintln.print_vm("create hd false ");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get(basename, "creatdisk_fail") , ""));

                return null;
            } else {
                SystemOutPrintln.print_vm("create hd success ");
            }
        }
         SystemOutPrintln.print_vm("create vm start ");
        VirtualMachine createVMInfo = this.getGetCreateVM();

        SystemOutPrintln.print_vm("create vm start 123");
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean createVM = vmface.create(createVMInfo);
        String returnStr = null;
        if (!createVM) {
             SystemOutPrintln.print_vm("create vm false ");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get(basename, "creatvm_fail") , ""));
            returnStr = null;
        }else{
            returnStr = "vm_VMListTable?faces-redirect=true";
            SystemOutPrintln.print_vm("create vm success ");
        }
        return returnStr;
    }
    
      //获得操作系统
    public String getOSType() {
        String typeID = null;
        OSName[] osName = GetOperatingSystem.initOSResource();
        int count =osName .length;
        for (int i = 0; i < count; i++) {
            if (osType.toString().equalsIgnoreCase(osName[i].getOsName())) {
                List<OperatingSystem> opeSystem = osName[i].getOsType();
                int osTypeCount = opeSystem.size();
                for (int j = 0; j < osTypeCount; j++) {
                    if (vmVersion.toString().equals(opeSystem.get(j).getTypeName().toString())) {  
                        typeID = opeSystem.get(j).getID();
                    }
                }
            }
        }
        
        SystemOutPrintln.print_vm("create vm ostype  typeID="+typeID);
        return typeID;
    }
    //获得要创建的虚拟机信息
    public VirtualMachine  getGetCreateVM(){
        VirtualMachine  vmInfo = new VirtualMachine();
   
        vmInfo.setVmName(vmNAME);
        vmInfo.setVmBaseFolderPath(path);
        vmInfo.setOsType(getOSType());
        vmInfo.setCpuCoreNum(cpuCoreNum);
        vmInfo.setMemorySize(Integer.valueOf(memorySize.trim()));
        vmInfo.setBoot1(1);
        vmInfo.setBoot2(2);
        vmInfo.setBoot3(3);


        List<Controller> controllers = new ArrayList();  //加载控制器
        //默认加载一个IDE控制器，且在IDE控制器下加载一个光盘
        Controller controllerInfo1 = new Controller();
        controllerInfo1.setControllerName("IDE Controller");
        controllerInfo1.setControllerType(1);//虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
        controllerInfo1.setControllerChipset(5);
        List<BaseStorage> storage1 = new ArrayList(); //控制器下加载存储设备，光盘或硬盘
        for (int j = 0; j < 1; j++) {
            BaseStorage baseStorageInfo = new BaseStorage();
            baseStorageInfo.setStorageName("emptydrive");
            baseStorageInfo.setStorageType(2);
            baseStorageInfo.setPortNum(0);
            baseStorageInfo.setDeviceNum(0);
            baseStorageInfo.setPhysicsDrive(false);
//            baseStorageInfo.setStorageAttachIsPhysical(0);


            storage1.add(baseStorageInfo);
        }
        controllerInfo1.setStorage(storage1);
        controllers.add(controllerInfo1);

        //默认加载一个SCSI控制器，且在IDE控制器下加载一个光盘
        Controller controllerInfo2 = new Controller();
        controllerInfo2.setControllerName("SCSI Controller"); //SAS Controller
        controllerInfo2.setControllerType(2);//虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
        controllerInfo2.setControllerChipset(1);
        if (!getjRadioButton5State()) {
            List<BaseStorage> storage2 = new ArrayList(); //控制器下加载存储设备，1为硬盘，2为光盘
            for (int j = 0; j < 1; j++) {
                BaseStorage baseStorageInfo = new BaseStorage();
                baseStorageInfo.setStorageName(diskAddress);
                baseStorageInfo.setStorageType(1);
                baseStorageInfo.setPortNum(0);
                baseStorageInfo.setDeviceNum(0);
                baseStorageInfo.setPhysicsDrive(false);

                storage2.add(baseStorageInfo);
            }
            controllerInfo2.setStorage(storage2);
        }
        controllers.add(controllerInfo2);
        

        vmInfo.setController(controllers);   
        vmInfo.setRemoteAddress(getIpAddress());
        vmInfo.setRemotePort(Integer.valueOf(vrdePort));
        vmInfo.setRemoteMultipleConnections(vrdeMultipleConnection);
//        vmInfo.setVrdeAddress(getIpAddress());
//        vmInfo.setVrdePort(Integer.valueOf(jPCreateTelnet.getPort()));
//        vmInfo.setVrdeMultipleConnection(jPCreateTelnet.getVRDEMultipleConnection());
        return vmInfo;
    }
   //获得“不创建硬盘”单选按钮是否选中
    public boolean getjRadioButton5State() {
        if (createDiskType.equals("2")) {
            return true;
        } else {
            return false;
        }
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
        con.setOperation(res.get(basename, "controller_" + con.getIsHave()));
        controllerTypeList.add(con);

        con = new ControllerType();
        con.setControllerName("SCSI");
        con.setControllerType(2);
//        if(scsi != null){
//            isHaveSCSI = 1;
//        }
        con.setIsHave(isHaveSCSI);
        con.setOperation(res.get(basename, "controller_" + con.getIsHave()));
        controllerTypeList.add(con);

        con = new ControllerType();
        con.setControllerName("SAS");
        con.setControllerType(3);
//        if(sas != null){
//            isHaveSAS = 1;
//        }
        con.setIsHave(isHaveSAS);
        con.setOperation(res.get(basename, "controller_" + con.getIsHave()));
        controllerTypeList.add(con);


        return controllerTypeList;
    }

    public String getVmNAME() {
        return vmNAME;
    }

    public void setVmNAME(String vmNAME) {
        this.vmNAME = vmNAME;
    }

    public String getVmBaseFolder() {
        return vmBaseFolder;
    }

    public void setVmBaseFolder(String vmBaseFolder) {
        this.vmBaseFolder = vmBaseFolder;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public int getCpuCoreNum() {
        return cpuCoreNum;
    }

    public void setCpuCoreNum(int cpuCoreNum) {
        this.cpuCoreNum = cpuCoreNum;
    }

    public String getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(String memorySize) {
        this.memorySize = memorySize;
    }

  

    public int getBoot1() {
        return boot1;
    }

    public void setBoot1(int boot1) {
        this.boot1 = boot1;
    }

    public int getBoot2() {
        return boot2;
    }

    public void setBoot2(int boot2) {
        this.boot2 = boot2;
    }

    public int getBoot3() {
        return boot3;
    }

    public void setBoot3(int boot3) {
        this.boot3 = boot3;
    }

    public List<ControllerInfo> getController() {
        return controller;
    }

    public void setController(List<ControllerInfo> controller) {
        this.controller = controller;
    }

    public String getVrdeAddress() {
        return vrdeAddress;
    }

    public void setVrdeAddress(String vrdeAddress) {
        this.vrdeAddress = vrdeAddress;
    }

    public String getVrdePort() {
        return vrdePort;
    }

    public void setVrdePort(String vrdePort) {
        this.vrdePort = vrdePort;
    }



    public boolean isVrdeMultipleConnection() {
        return vrdeMultipleConnection;
    }

    public void setVrdeMultipleConnection(boolean vrdeMultipleConnection) {
        this.vrdeMultipleConnection = vrdeMultipleConnection;
    }



//    public String setResource(String resource) {
//       return java.util.ResourceBundle.getBundle("com/marstor/msa/common/resources/messages").getString(resource);
//    }
    
    public void handleCityChange() {

        if (osType != null && !osType.equals("")) {
            vmVersionList = new ArrayList<String>();

            vmVersions = vmVersionsData.get(osType);
            Iterator<String> keySetIterator = vmVersions.keySet().iterator();
            while (keySetIterator.hasNext()) {
                vmVersionList.add(vmVersions.get(keySetIterator.next()));
            }
            Collections.sort(vmVersionList);
            
        } else {
            vmVersions = new HashMap<String, String>();
             vmVersionList = new ArrayList<String>();
        }
    }
    
    public void handleMemorySizeChange() {
          OSName[] osName = GetOperatingSystem.initOSResource();
            int count1 = osName.length;
            for (int i = 0; i < count1; i++) {
                List<OperatingSystem> opeSystem = osName[i].getOsType();
                int osTypeCount = opeSystem.size();
                for (int j = 0; j < osTypeCount; j++) {
                    if (vmVersion.equalsIgnoreCase(opeSystem.get(j).getTypeName())) {
                        memorySize = opeSystem.get(j).getMemorySize()+"";
                        diskSize = opeSystem.get(j).getDiskSize();
                        break;
                    }
                }
            }  
    }
    
      public void initIPList(){
       ipList = new ArrayList();
       ipList.add(res.get(basename, "any_ip"));
       MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
       List<String> ips = vmface.ifconfig();
       if (ips != null && ips.size() > 0) {
           for (int i = 0; i < ips.size(); i++) {
               ipList.add(ips.get(i));
           }
       } else {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "gethostip_fail"), ""));
           return;
       }
    }
      
       //获得IP地址
    public String getIpAddress(){
       String ip = vrdeAddress;
       if(ip.equalsIgnoreCase(res.get(basename, "any_ip"))){
           return null;
       }
       return ip;
    }
    
       //获取是否允许多重连接
    public int getVRDEMultipleConnection(){
        if(vrdeMultipleConnection){
            vrdeMultipleConnection_int = 1;  //允许
        }else{
            vrdeMultipleConnection_int = 0;
        }
        return vrdeMultipleConnection_int;

    }
   

    public Map<String, String> getOsTypes() {
        return osTypes;
    }

    public void setOsTypes(Map<String, String> osTypes) {
        this.osTypes = osTypes;
    }

    public Map<String, Map<String, String>> getVmVersionsData() {
        return vmVersionsData;
    }

    public void setVmVersionsData(Map<String, Map<String, String>> vmVersionsData) {
        this.vmVersionsData = vmVersionsData;
    }

    public Map<String, String> getVmVersions() {
        return vmVersions;
    }

    public void setVmVersions(Map<String, String> vmVersions) {
        this.vmVersions = vmVersions;
    }

    public String getVmVersion() {
        return vmVersion;
    }

    public void setVmVersion(String vmVersion) {
        this.vmVersion = vmVersion;
    }

    public int getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(int diskSize) {
        this.diskSize = diskSize;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public int getDiskType() {
        return diskType;
    }

    public void setDiskType(int diskType) {
        this.diskType = diskType;
    }

    public List<String> getVolumeList() {
        return volumeList;
    }

    public void setVolumeList(List<String> volumeList) {
        this.volumeList = volumeList;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getSelectDiskName() {
        return selectDiskName;
    }

    public void setSelectDiskName(String selectDiskName) {
        this.selectDiskName = selectDiskName;
    }

    public List<String> getCpuCoreNumList() {
        return cpuCoreNumList;
    }

    public void setCpuCoreNumList(List<String> cpuCoreNumList) {
        this.cpuCoreNumList = cpuCoreNumList;
    }

    public String getMemorySizeTip() {
        return memorySizeTip;
    }

    public void setMemorySizeTip(String memorySizeTip) {
        this.memorySizeTip = memorySizeTip;
    }

    public List<String> getIpList() {
        return ipList;
    }

    public void setIpList(List<String> ipList) {
        this.ipList = ipList;
    }

    public List<VirtualDiskName> getVirDiskList() {
        return virDiskList;
    }

    public void setVirDiskList(List<VirtualDiskName> virDiskList) {
        this.virDiskList = virDiskList;
    }

    public VirtualDiskName getSelectedVirDisk() {
        return selectedVirDisk;
    }

    public void setSelectedVirDisk(VirtualDiskName selectedVirDisk) {
        this.selectedVirDisk = selectedVirDisk;
    }

    public boolean isNotFile() {
        return notFile;
    }

    public void setNotFile(boolean notFile) {
        this.notFile = notFile;
    }

    public boolean isNotName() {
        return notName;
    }

    public void setNotName(boolean notName) {
        this.notName = notName;
    }

    public String getCreateDiskType() {
        return createDiskType;
    }

    public void setCreateDiskType(String createDiskType) {
        this.createDiskType = createDiskType;
    }

    public String getVirDiskSizetip() {
        return virDiskSizetip;
    }

    public void setVirDiskSizetip(String virDiskSizetip) {
        this.virDiskSizetip = virDiskSizetip;
    }

    public VirtualDiskNameModel getVdsModel() {
        return vdsModel;
    }

    public void setVdsModel(VirtualDiskNameModel vdsModel) {
        this.vdsModel = vdsModel;
    }

    public String getDiskSizen() {
        return diskSizen;
    }

    public void setDiskSizen(String diskSizen) {
        this.diskSizen = diskSizen;
    }

    public List<String> getVmVersionList() {
        return vmVersionList;
    }

    public void setVmVersionList(List<String> vmVersionList) {
        this.vmVersionList = vmVersionList;
    }

    
    
    
    public void  clickButton(){
         path = "/" + volume + "" + "/VM/" + vmNAME;
        initVirDiskList();
         vdsModel = new VirtualDiskNameModel(virDiskList);
        RequestContext.getCurrentInstance().execute("dlg.show()");
    }
    public void initVirDiskList(){
        
        virDiskList = new ArrayList();
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        List<HDInfo> disks = vmface.getHDInfo();

        if (disks != null && disks.size() > 0) {
            for (HDInfo disk : disks) {

                String[] fullPath = disk.getHDName().split("/");
                String diskPath = "/" + fullPath[1] + "/" + fullPath[2] + "/" + fullPath[3];
                if (diskPath.endsWith(path)) {
                    if (disk.isUsage() == false) {
                        if (disk.getState()!= null && disk.getState().equalsIgnoreCase("inaccessible")) {  //不是损坏的硬盘显示出来
                            
                        }else{
                            VirtualDiskName data = new VirtualDiskName();
                            data.setName(disk.getHDName());
                            virDiskList.add(data);
                        }
                    }
                }
            }

        }
    }
    
    public void closeDialog() {
        RequestContext.getCurrentInstance().execute("dlg.hide()");
    }
     public void okDialog() {
         if(selectedVirDisk != null){
             selectDiskName = selectedVirDisk.getName();
         }
        
        RequestContext.getCurrentInstance().execute("dlg.hide()");
    }
    
     public void changeBooleanCheckbox() {
        if (createDiskType.equals("0")) {  
            notFile = true;
            notName = false;
        } else if(createDiskType.equals("1")){ 
            notFile = false;
            notName = true;
        }else{
             notFile = true;
            notName = true;
        }
        
    }
    
}
