/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.disk.model.VirtualDetail;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.bean.BaseStorage;
import com.marstor.msa.vbox.bean.Controller;
import com.marstor.msa.vbox.bean.HostHardware;
import com.marstor.msa.vbox.bean.NetworkInterfaceCard;
import com.marstor.msa.vbox.bean.VirtualMachine;
import com.marstor.msa.vbox.web.MsaVMInterface;
import com.marstor.msa.vm.model.BootSequen;
import com.marstor.msa.vm.model.ControllerInfo;
import com.marstor.msa.vm.model.ControllerType;
import com.marstor.msa.vm.model.NetworkInterfaceCardInfo;
import com.marstor.msa.vm.model.RegisterVMs;
import com.marstor.msa.vm.model.VMInformation;
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
@ManagedBean(name = "vMList")
@ViewScoped
public class VMList implements Serializable {

    public List<RegisterVMs> vmList;
    private List<VMInformation> vmInfoList;
    List<NetworkInterfaceCardInfo> network;
    List<ControllerInfo> controllers;
    private VMInformation selectedVM;
    VMInformation vmInfo;
    VirtualMachine vminfo;
    private ControllerInfo ide = null;
    private ControllerInfo scsi = null;
    private ControllerInfo sas = null;
    private int isHaveIDE = 0;
    private int isHaveSCSI = 0;
    private int isHaveSAS = 0;
    private int controllerChipSet_IDE = 4;//控制器芯片组
    private int controllerChipSet_SCSI = 2;//控制器芯片组
    private int controllerChipSet_SAS = 1;//控制器芯片组
    public List<ControllerType> controllerTypeList;
    ControllerType con;
    public List<String> cpuCoreNumList; //cpul列表
    private List<BootSequen> bootSequences;  //启动顺序
//    String boot_1 = "/resources/picture/cd.png";
//    String boot_2 = "/resources/picture/disk.png";
//    String boot_3 = "/resources/picture/network.png";
    public RegisterVMs selectedvm;
    //data
    public int hostCpuCoreNum = 0;
    public int hostMemorySize = 0;
    public long leftHostMemorySize = 0;
    public long defaultMemorySize = 0;
    public String[] cd_ROMDevices;
//    public HostNICInfo[] nic = null;
    HostHardware hostHardwareInfo = null;
    public String vmVolume = null;
    public List<VirtualDetail> detail;
    VirtualDetail vmDetail;
    private boolean isStart;
    private boolean isPause;
    private boolean isResume;
    private boolean notDelete;
    private boolean notCheck;
    private boolean notStart;
    private boolean notPause;
    private boolean notResume;
    private boolean notReboot;
    private boolean notClose;
    public int userType;
    public String vmname;
    
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_VMListTable";

    public VMList() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        userType = user.getType();

        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        hostHardwareInfo = vmface.getHostHardwareInfo();
        if (hostHardwareInfo != null) {
            hostCpuCoreNum = hostHardwareInfo.getCpuNum();  //获取物理主机的CPU数量
            hostMemorySize = hostHardwareInfo.getMemorySize();  //获取物理主机的内存大小
            List<String> dvList = hostHardwareInfo.getDvdroms();  //获取物理驱动
            defaultMemorySize = hostHardwareInfo.getDefaultMemorySize();
            if (dvList != null && dvList.size() > 0) {
                cd_ROMDevices = new String[dvList.size()];
                dvList.toArray(cd_ROMDevices);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "gethostinfo_fail"), ""));
            return;
        }
        leftHostMemorySize = hostMemorySize;
        this.setVMListInfo();


        //
//        vmInfoList = new ArrayList();
//        this.getVMListInfo();
//        
//        cpuCoreNumList = new ArrayList(); //cpu列表
//        setCPUList();

//        ipList = new ArrayList(); //IP地址列表
//        setIPList();


    }

    public String getIcon(int type) {
        return "boot_" + type;
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
        con.setOperation( res.get(basename, "controller_" + con.getIsHave()));
        if (isHaveIDE == 0) {
            con.setEdit(true);
        } else {
            con.setEdit(false);
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
        if (isHaveSCSI == 0) {
            con.setEdit(true);
        } else {
            con.setEdit(false);
        }
        con.setOperation( res.get(basename, "controller_" + con.getIsHave()));
        con.setControllerChipset(controllerChipSet_SCSI);
        controllerTypeList.add(con);

        con = new ControllerType();
        con.setControllerName("SAS");
        con.setControllerType(3);
//        if(sas != null){
//            isHaveSAS = 1;
//        }
        con.setIsHave(isHaveSAS);
        if (isHaveSAS == 0) {
            con.setEdit(true);
        } else {
            con.setEdit(false);
        }
        con.setOperation( res.get(basename, "controller_" + con.getIsHave()));
        con.setControllerChipset(controllerChipSet_SAS);
        controllerTypeList.add(con);


        return controllerTypeList;
    }

    private void getVMListInfo() {
//        this.getTest1_Info();
//        this.getTest2_Info();
    }

//    private void getTest1_Info() {
//        //test1
//        ide = null;
//        scsi = null;
//        sas = null;
//        
//        isHaveIDE = 0;
//        isHaveSCSI = 0;
//        isHaveSAS = 0;
//        controllerChipSet_IDE =4;//控制器芯片组
//        controllerChipSet_SCSI =2;//控制器芯片组
//        controllerChipSet_SAS =1;//控制器芯片组
//
//        String vmNAME = "test1";
//        String vmUUID = "123";//虚拟机UUID
//        String vmBaseFolderPath = "/C/test1";//虚拟机路径
//        int boot1 = 1;
//        int boot2 = 2;
//        int boot3 = 3;
//        String osType = "Windows 2003";//虚拟机操作系统类型
//        int cpuCoreNum = 1;//CPU核心数
//        int memorySize = 256;//内存大小
//        int iIOAPIC = 1;
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
//        String remoteAddress = "192.168.1.88";//远程登录ip
//        int remotePort = 5000;//远程登录端口号
//        int RemoteIPAddressUsable = 0;//此远程登录ip是否存在
//        int remoteTimeOut = 500;//超时时间
//        String remoteAuthentication = "ask";//认证方式
//        int iMultipleConnections = 1;
//        boolean remoteMultipleConnections = false;//是否允许远程连接多连接
//        if (iMultipleConnections == 0) {
//            remoteMultipleConnections = false;
//        } else {
//            remoteMultipleConnections = true;
//        }
//
//        int controllerCount = 1;
//        controllers = new ArrayList();  //加载控制器
//
//
////            XMLParser childParser = returnXML.createXMLParser("MSA/ReturnValue/StorageInfo/StorageControler",i);
//        String StorageControlerName = "IDE";
//        int controllerType = 1;  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
//        int controllerChipset = 4;
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
//            String storageAttachMedium = "disk1";
//            int StorageAttachMediumIsExists = 1;
//            int storageAttachType = 1;  //存储设备类型取值1为硬盘，2为光盘
//            long storageAttachSize = 20;
//            long StorageAttachUsedSize = 20;
//            int StorageAttachIsPhysical = (int) 1;
//            String storageAttachFormat = "Normal(VDI)";  //存储设备格式
//            int storageAttachPort = 0;  //存储设备端口号，IDE取值0或1，SCSI取值0到14，SAS取值0-7
//            int StorageAttachDevice = 1;  //存储设备设备号，IDE控制器，取值0或1，SAS和SCSI取值0
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
////        controllerInfo.setStorage(storage);
//         int StorageCount2 = 1;
//        List<BaseStorageInfo> storage2 = new ArrayList();  //控制器下加载存储设备，光盘或硬盘
//        for (int j = 0; j < StorageCount2; j++) {
////                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
//            String storageAttachMedium = "empty";
//            int StorageAttachMediumIsExists = 1;
//            int storageAttachType = 2;  //存储设备类型取值1为硬盘，2为光盘
//            long storageAttachSize = 20;
//            long StorageAttachUsedSize = 20;
//            int StorageAttachIsPhysical = (int) 1;
//            String storageAttachFormat = "Normal(VDI)";  //存储设备格式
//            int storageAttachPort = 1;  //存储设备端口号，IDE取值0或1，SCSI取值0到14，SAS取值0-7
//            int StorageAttachDevice = 1;  //存储设备设备号，IDE控制器，取值0或1，SAS和SCSI取值0
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
//        controllerInfo.setStorage(storage);
//        
//        
//        controllers.add(controllerInfo);
//
//        if (controllerType == 1) { 
//            ide = controllerInfo;
//            isHaveIDE =1;
//            controllerChipSet_IDE =controllerChipset;   
//        }
//
//
//
////            XMLParser childParser = returnXML.createXMLParser("MSA/ReturnValue/StorageInfo/StorageControler",i);
//        String StorageControlerName1 = "SCSI";
//        int controllerType1 = 2;  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
//        int controllerChipset1 = 2;
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
//            String storageAttachMedium = "disk12";
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
//        controllers.add(controllerInfo1);
//        if (controllerType1 == 2) {
//            scsi = controllerInfo1;
//            isHaveSCSI =1;
//             controllerChipSet_SCSI =controllerChipset1;   
//        }
//
//
//
////            XMLParser childParser = returnXML.createXMLParser("MSA/ReturnValue/StorageInfo/StorageControler",i);
//        String StorageControlerName3 = "SAS";
//        int controllerType3 = 3;  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
//        int controllerChipset3 = 1;
//
//        ControllerInfo controllerInfo3 = new ControllerInfo();
//        controllerInfo3.setControllerName(StorageControlerName3);
//        controllerInfo3.setControllerType(controllerType3);
//        controllerInfo3.setControllerChipset(controllerChipset3);
//
//        int StorageCount3 = 1;
//        List<BaseStorageInfo> storage3 = new ArrayList();  //控制器下加载存储设备，光盘或硬盘
//        for (int j = 0; j < StorageCount3; j++) {
////                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
//            String storageAttachMedium = "disk13";
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
//             baseStorageInfo.setStrstate(this.setResource("Storage_deviceState_1"));
//
//            storage3.add(baseStorageInfo);
//        }
//
//        controllerInfo3.setStorage(storage3);
//        controllers.add(controllerInfo3);
//        if (controllerType3 == 3) {
//            sas = controllerInfo3;
//            isHaveSAS =1;
//            controllerChipSet_SAS =controllerChipset3;  
//        }
//
//        int nicCount = 1;
//        network = new ArrayList();  //加载网卡
////        for (int i = 0; i < nicCount; i++) {
//            int vmNICNum = 1;
//            int vmNICIsUsable = 1;
//            String vmNICName = "e100g1";
//            String macAddress = "0125245611";
//            int cableConnected = 1;
//            String bindPhysicsNicIpAddress = "BindPhysicsIPAddress1";
//
//            NetworkInterfaceCardInfo cardInfo = new NetworkInterfaceCardInfo();
//            cardInfo.setVmNICNum(vmNICNum);
//            cardInfo.setVmNICIsUsable(vmNICIsUsable);
//            cardInfo.setVmNICName(vmNICName);
//            cardInfo.setMacAddress(macAddress);
//            cardInfo.setCableConnected(cableConnected);
//            if (cableConnected == 1) {
//                cardInfo.setBoolCableConnected(true);
//            } else {
//                cardInfo.setBoolCableConnected(false);
//            }
//            cardInfo.setStrCableConnected(setResource("net_cableConnected_" + cardInfo.getCableConnected()));
//            cardInfo.setBindPhysicsNicIpAddress(bindPhysicsNicIpAddress);
//            network.add(cardInfo);
////        }
//            
//             int vmNICNum1 = 1;
//            int vmNICIsUsable1 = 1;
//            String vmNICName1 = "e100g0";
//            String macAddress1 = "0125245622";
//            int cableConnected1 = 1;
//            String bindPhysicsNicIpAddress1 = "BindPhysicsIPAddress1";
//
//            NetworkInterfaceCardInfo cardInfo1 = new NetworkInterfaceCardInfo();
//            cardInfo1.setVmNICNum(vmNICNum1);
//            cardInfo1.setVmNICIsUsable(vmNICIsUsable1);
//            cardInfo1.setVmNICName(vmNICName1);
//            cardInfo1.setMacAddress(macAddress1);
//            cardInfo1.setCableConnected(cableConnected1);
//            if (cableConnected1 == 1) {
//                cardInfo1.setBoolCableConnected(true);
//            } else {
//                cardInfo1.setBoolCableConnected(false);
//            }
//            cardInfo1.setStrCableConnected(setResource("net_cableConnected_" + cardInfo1.getCableConnected()));
//            cardInfo1.setBindPhysicsNicIpAddress(bindPhysicsNicIpAddress1);
//            network.add(cardInfo1);
//
//
//        vmInfo = new VMInformation();
//        vmInfo.setVmNAME(vmNAME);
//        vmInfo.setVmUUID(vmUUID);
//        vmInfo.setVmBaseFolderPath(vmBaseFolderPath);
//        vmInfo.setBoot1(boot1);
//        vmInfo.setBoot2(boot2);
//        vmInfo.setBoot3(boot3);
//        bootSequences= new ArrayList<BootSequence>();
//        bootSequences.add(new BootSequence(this.setResource("BootSequence_"+boot1),this.setResource("boot_"+boot1)));
//        bootSequences.add(new BootSequence(this.setResource("BootSequence_"+boot2),this.setResource("boot_"+boot2)));
//        bootSequences.add(new BootSequence(this.setResource("BootSequence_"+boot3),this.setResource("boot_"+boot3)));
//	vmInfo.setBootSequences(bootSequences);	
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
//        vmInfo.setController(controllers);
//        vmInfo.setNetworkInterfaceCard(network);
//        vmInfo.setVmState(1);
//        vmInfo.setState(setResource("vm_type_" + vmInfo.getVmState()));
//        if(ide != null){
//            vmInfo.setIdeController(ide);
//        }
//        if(scsi != null){
//            vmInfo.setScsiController(scsi);
//        }
//        if(sas != null){
//            vmInfo.setSasController(sas);
//        }
//        this.getControllerTypeList();
//        vmInfo.setControllerTypeList(controllerTypeList);
//        vmInfoList.add(vmInfo);
//
//    }
//    private void getTest2_Info() {
//        //test2
//        ide = null;
//        scsi = null;
//        sas = null;
//        
//        isHaveIDE = 0;
//        isHaveSCSI = 0;
//        isHaveSAS = 0;
//        
//        String vmNAME = "test2";
//        String vmUUID = "456";//虚拟机UUID
//        String vmBaseFolderPath = "/C/test2";//虚拟机路径
//        int boot1 = 2;
//        int boot2 = 3;
//        int boot3 = 1;
//        String osType = "Windows 2008";//虚拟机操作系统类型
//        int cpuCoreNum = 1;//CPU核心数
//        int memorySize = 256;//内存大小
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
//        String remoteAddress = "192.168.1.68";//远程登录ip
//        int remotePort = 5000;//远程登录端口号
//        int RemoteIPAddressUsable = 0;//此远程登录ip是否存在
//        int remoteTimeOut = 500;//超时时间
//        String remoteAuthentication = "ask";//认证方式
//        int iMultipleConnections = 1;
//        boolean remoteMultipleConnections = false;//是否允许远程连接多连接
//        if (iMultipleConnections == 0) {
//            remoteMultipleConnections = false;
//        } else {
//            remoteMultipleConnections = true;
//        }
//
//        int controllerCount = 1;
//        controllers = new ArrayList();  //加载控制器
//
//
//
////        String StorageControlerName = "IDE";
////        int controllerType = 1;  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
////        int controllerChipset = 1;
////
////        ControllerInfo controllerInfo = new ControllerInfo();
////        controllerInfo.setControllerName(StorageControlerName);
////        controllerInfo.setControllerType(controllerType);
////        controllerInfo.setControllerChipset(controllerChipset);
////
////        int StorageCount = 1;
////        List<BaseStorageInfo> storage = new ArrayList();  //控制器下加载存储设备，光盘或硬盘
////        for (int j = 0; j < StorageCount; j++) {
//////                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
////            String storageAttachMedium = "disk2";
////            int StorageAttachMediumIsExists = 1;
////            int storageAttachType = 1;  //存储设备类型取值1为硬盘，2为光盘
////            long storageAttachSize = 20;
////            long StorageAttachUsedSize = 20;
////            int StorageAttachIsPhysical = (int) 1;
////            String storageAttachFormat = "Normal(VDI)";  //存储设备格式
////            int storageAttachPort = 0;  //存储设备端口号，IDE取值0或1，SCSI取值0到14，SAS取值0-7
////            int StorageAttachDevice = 0;  //存储设备设备号，IDE控制器，取值0或1，SAS和SCSI取值0
////
////            BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
////            baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
////            baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
////            baseStorageInfo.setStorageAttachType(storageAttachType);
////            baseStorageInfo.setStorageAttachSize(storageAttachSize);
////            baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
////            baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize);
////            baseStorageInfo.setStorageAttachFormat(storageAttachFormat);
////            baseStorageInfo.setStorageAttachPort(storageAttachPort);
////            baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
////            baseStorageInfo.setStrstate(this.setResource("Storage_deviceState_1"));
////
////            storage.add(baseStorageInfo);
////        }
////
////        controllerInfo.setStorage(storage);
////        controllers.add(controllerInfo);
////
////        System.out.println("isHaveIDE11111="+isHaveIDE);
////        if (controllerType == 1) {
////            ide = controllerInfo;
//////            isHaveIDE =1;
////        }
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
//            String storageAttachMedium = "disk5";
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
//             baseStorageInfo.setStrstate(this.setResource("Storage_deviceState_1"));
//
//            storage1.add(baseStorageInfo);
//        }
//
//        controllerInfo1.setStorage(storage1);
//        controllers.add(controllerInfo1);
//        if (controllerType1 == 2) {
//            scsi = controllerInfo1;
//            isHaveSCSI =1;
//        }
//
//
//
////            XMLParser childParser = returnXML.createXMLParser("MSA/ReturnValue/StorageInfo/StorageControler",i);
//        String StorageControlerName3 = "SAS";
//        int controllerType3 = 3;  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
//        int controllerChipset3 = 1;
//
//        ControllerInfo controllerInfo3 = new ControllerInfo();
//        controllerInfo3.setControllerName(StorageControlerName3);
//        controllerInfo3.setControllerType(controllerType3);
//        controllerInfo3.setControllerChipset(controllerChipset3);
//
//        int StorageCount3 = 1;
//        List<BaseStorageInfo> storage3 = new ArrayList();  //控制器下加载存储设备，光盘或硬盘
//        for (int j = 0; j < StorageCount3; j++) {
////                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
//            String storageAttachMedium = "disk6";
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
//             baseStorageInfo.setStrstate(this.setResource("Storage_deviceState_1"));
//
//            storage3.add(baseStorageInfo);
//        }
//
//        controllerInfo3.setStorage(storage3);
//        controllers.add(controllerInfo3);
//        if (controllerType3 == 3) {
//            sas = controllerInfo3;
//            isHaveSAS =1;
//        }
//
//        int nicCount = 1;
//        network = new ArrayList();  //加载网卡
//        for (int i = 0; i < nicCount; i++) {
//            int vmNICNum = 1;
//            int vmNICIsUsable = 1;
//            String vmNICName = "network4";
//            String macAddress = "564564";
//            int cableConnected = 1;
//            String bindPhysicsNicIpAddress = "BindPhysicsIPAddress1";
//
//            NetworkInterfaceCardInfo cardInfo = new NetworkInterfaceCardInfo();
//            cardInfo.setVmNICNum(vmNICNum);
//            cardInfo.setVmNICIsUsable(vmNICIsUsable);
//            cardInfo.setVmNICName(vmNICName);
//            cardInfo.setMacAddress(macAddress);
//            cardInfo.setCableConnected(cableConnected);
//            if (cableConnected == 1) {
//                cardInfo.setBoolCableConnected(true);
//            } else {
//                cardInfo.setBoolCableConnected(false);
//            }
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
//        bootSequences= new ArrayList<BootSequence>();
//        bootSequences.add(new BootSequence(this.setResource("BootSequence_"+boot1),this.setResource("boot_"+boot1)));
//        bootSequences.add(new BootSequence(this.setResource("BootSequence_"+boot2),this.setResource("boot_"+boot2)));
//        bootSequences.add(new BootSequence(this.setResource("BootSequence_"+boot3),this.setResource("boot_"+boot3)));
//	vmInfo.setBootSequences(bootSequences);	
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
//        vmInfo.setController(controllers);
//        vmInfo.setNetworkInterfaceCard(network);
//        
//        vmInfo.setVmState(2);
//        vmInfo.setState(setResource("vm_type_" + vmInfo.getVmState()));
//        if(ide != null){
//            vmInfo.setIdeController(ide);
//        }
//        if(scsi != null){
//            vmInfo.setScsiController(scsi);
//        }
//        if(sas != null){
//            vmInfo.setSasController(sas);
//        }
//        this. getControllerTypeList();
//        vmInfo.setControllerTypeList(controllerTypeList);
//        vmInfoList.add(vmInfo);
//
//
//    }
    private void setVMListInfo() {
        vmList = new ArrayList();
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        List<VirtualMachine> maps = vmface.getVMList();
        if (maps != null && maps.size() > 0) {
            SystemOutPrintln.print_vm("map is not null.");
//            Set<String> keys = map.keySet();
//            Iterator<String> iterator = keys.iterator();
            RegisterVMs vm = null;
            for (VirtualMachine map : maps) {
                String key = map.getVmName();
                SystemOutPrintln.print_vm("vmName=" + key);
//                int[] values = map.get(key);
                String volume = map.getVmBaseFolderPath().split("/")[1];
                SystemOutPrintln.print_vm("volume=" + volume);
                int vmState = map.getVmState();
                System.out.print("vmState=" + vmState);
                int romotePort = map.getRemotePort();
                int memorysize = map.getMemorySize();
                if (vmState == VirtualMachine.VM_RUN || vmState == VirtualMachine.VM_PAUSE || vmState == VirtualMachine.VM_STOPPING) {  //当虚拟机为运行状态时，需要算剩余内存
                    leftHostMemorySize = leftHostMemorySize - memorysize;
                }
                String state = res.get(basename, "vm_stop");
                if (vmState == VirtualMachine.VM_RUN) {
                    state = res.get(basename, "vm_run");
                } else if (vmState == VirtualMachine.VM_PAUSE) {
                    state = res.get(basename, "vm_pause");
                } else if (vmState == VirtualMachine.VM_STOP) {
                    state = res.get(basename, "vm_stop");
                } else if (vmState == VirtualMachine.VM_INACCESSIBLE) {
                    state = res.get(basename, "vm_inaccessible");
                } else if (vmState == VirtualMachine.VM_STOPPING) {
                    state = res.get(basename, "vm_stopping");
                } else if (vmState == VirtualMachine.VM_ABORTED) {
                    state = res.get(basename, "vm_aborted");
                }
                detail = new ArrayList();
                vmDetail = new VirtualDetail();
                vmDetail.setProperty(res.get(basename, "ostype"));
                vmDetail.setName(map.getOsType());
                detail.add(vmDetail);

                vmDetail = new VirtualDetail();
                vmDetail.setProperty(res.get(basename, "cpucorenum"));
                vmDetail.setName(map.getCpuCoreNum() + "");
                detail.add(vmDetail);

                vmDetail = new VirtualDetail();
                vmDetail.setProperty(res.get(basename, "memorysize"));
                vmDetail.setName(map.getMemorySize() + "MB");
                detail.add(vmDetail);

                vmDetail = new VirtualDetail();
                vmDetail.setProperty(res.get(basename, "remoteaddress"));
                //获得IP地址
                String ip = map.getRemoteAddress();
                if (ip == null) {
                    ip = res.get(basename, "anyip");
                }
                vmDetail.setName(ip);
                detail.add(vmDetail);

                vmDetail = new VirtualDetail();
                vmDetail.setProperty(res.get(basename, "remoteport"));
                vmDetail.setName(map.getRemotePort() + "");
                detail.add(vmDetail);

                this.getMenu(vmState);
                vm = new RegisterVMs();
                vm.setVmName(key);
                vm.setUuid(map.getVmUUID());
                vm.setPath(map.getVmBaseFolderPath());
                vm.setBelongVol(volume);
                vm.setVmState(vmState);
                vm.setState(state);
                vm.setRemotePort(romotePort);
                vm.setMemorySize(memorysize);
                vm.setIsStart(isStart);
                vm.setNotStart(notStart);
                vm.setIsPause(isPause);
                vm.setNotPause(notPause);
                vm.setIsResume(isResume);
                vm.setNotResume(notResume);
                vm.setNotReboot(notReboot);
                vm.setNotClose(notClose);
                vm.setNotDelete(notDelete);
                vm.setNotCheck(notCheck);
                vm.setDetail(detail);
                vmList.add(vm);

            }
        }

    }

    public void getMenu(int vmState) //根据选择按钮情况设置哪些成灰的（不能操作）
    {
        SystemOutPrintln.print_vm("vmState=" + vmState);
        SystemOutPrintln.print_vm("userType=" + userType);
        if (vmState == VirtualMachine.VM_RUN) {
            isStart = false;
            isPause = true;
            isResume = false;
            notStart = true;
            notPause = false;
            notResume = true;

            notReboot = false;
            notClose = false;
            notDelete = true;
            SystemOutPrintln.print_vm("VirtualMachine.VM_RUN notDelete = true;");
            notCheck = false; //设置有限制，只能查看

            if (userType != 2) {
                isStart = false;
                isPause = true;
                isResume = false;
                notStart = true;
                notPause = true;
                notResume = true;

                notReboot = true;
                notClose = true;
                notDelete = true;

                notCheck = false; //设置有限制，只能查看


            }

        } else if (vmState == VirtualMachine.VM_PAUSE) {
            isStart = false;
            isPause = false;
            isResume = true;
            notStart = true;
            notPause = true;
            notResume = false;

            notReboot = true;
            notClose = false;
            notDelete = true;
            notCheck = false; //设置有限制，只能查看
            SystemOutPrintln.print_vm("VirtualMachine.VM_PAUSE notDelete = true;");

            if (userType != 2) {
                isStart = false;
                isPause = false;
                isResume = true;
                notStart = true;
                notPause = true;
                notResume = true;

                notReboot = true;
                notClose = true;
                notDelete = true;
                notCheck = false; //设置有限制，只能查看


            }


        } else if (vmState == VirtualMachine.VM_STOP || vmState == VirtualMachine.VM_ABORTED) {
            isStart = true;
            isPause = false;
            isResume = false;
            notStart = false;
            notPause = true;
            notResume = true;

            notReboot = true;
            notClose = true;
            notDelete = false;  //关闭状态可以删除虚拟机
            notCheck = false;  //可以修改虚拟机
            SystemOutPrintln.print_vm("VirtualMachine.VM_STOP VM_ABORTED notDelete = false;");

            if (userType != 2) {
                isStart = true;
                isPause = false;
                isResume = false;
                notStart = true;
                notPause = true;
                notResume = true;

                notReboot = true;
                notClose = true;
                notDelete = true;
                notCheck = false; //设置有限制，只能查看


            }

        } else if (vmState == VirtualMachine.VM_INACCESSIBLE) {

            isStart = true;
            isPause = false;
            isResume = false;
            notStart = true;
            notPause = true;
            notResume = true;

            notReboot = true;
            notClose = true;
            notDelete = false;  //关闭状态可以删除虚拟机
            notCheck = true;  //可以修改虚拟机
            SystemOutPrintln.print_vm("VirtualMachine.VM_INACCESSIBLE notDelete = false;");
            if (userType != 2) {
                isStart = true;
                isPause = false;
                isResume = false;
                notStart = true;
                notPause = true;
                notResume = true;

                notReboot = true;
                notClose = true;
                notDelete = true;
                notCheck = true;


            }

        } else if (vmState == VirtualMachine.VM_STOPPING) {
            isStart = true;
            isPause = false;
            isResume = false;
            notStart = true;
            notPause = true;
            notResume = true;

            notReboot = true;
            notClose = true;
            notDelete = true;  //关闭状态可以删除虚拟机
            SystemOutPrintln.print_vm("VirtualMachine.VM_STOPPING notDelete = true;");
            notCheck = false;  //仅可查看

            if (userType != 2) {
                isStart = true;
                isPause = false;
                isResume = false;
                notStart = true;
                notPause = true;
                notResume = true;

                notReboot = true;
                notClose = true;
                notDelete = true;
                notCheck = false; //仅可查看


            }
        }

    }

    public List<RegisterVMs> getVmList() {
        return vmList;
    }

    public void setVmList(List<RegisterVMs> vmList) {
        this.vmList = vmList;
    }

    public String getVmname() {
        return vmname;
    }

    public void setVmname(String vmname) {
        this.vmname = vmname;
    }

//    public String setResource(String resource) {
//        return java.util.ResourceBundle.getBundle("com/marstor/msa/common/resources/messages").getString(resource);
//    }

    public VMInformation getSelectedVM() {
        return selectedVM;
    }

    public void setSelectedVM(VMInformation selectedVM) {
        this.selectedVM = selectedVM;
    }

    public List<VMInformation> getVmInfoList() {
        return vmInfoList;
    }

    public void setVmInfoList(List<VMInformation> vmInfoList) {
        this.vmInfoList = vmInfoList;
    }

    public List<String> getCpuCoreNumList() {
        return cpuCoreNumList;
    }

    public void setCpuCoreNumList(List<String> cpuCoreNumList) {
        this.cpuCoreNumList = cpuCoreNumList;
    }

    public void setCPUList() {
        for (int i = 0; i < 9; i++) {
            cpuCoreNumList.add(i + 1 + "");
        }
    }

//    public List<String> getIpList() {
//        return ipList;
//    }
//
//    public void setIpList(List<String> ipList) {
//        this.ipList = ipList;
//    }
//   public void setIPList(){
//
//       MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
//       List<String> ips = vmface.ifconfig();
//       ipList.add("任意IP");
//       if (ips != null && ips.size() > 0) {
//           for (int i = 0; i < ips.size(); i++) {
//               ipList.add(ips.get(i));
//           }
//       } else {
//           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "获取主机IP地址信息失败。", ""));
//           return;
//       }
//    }
    public VMInformation getVMByName(String name) {
        VMInformation vm = null;
//       vmInfoList.size();
        for (int i = 0; i < vmInfoList.size(); i++) {
            if (vmInfoList.get(i).getVmNAME().equals(name)) {
                vm = vmInfoList.get(i);
                break;
            }
        }
        return vm;
    }

    public String editSystem() {
        VMList vm = new VMList();
        String name = selectedVM.getVmNAME();
        int coreNum = selectedVM.getCpuCoreNum();
        int memorySize = selectedVM.getMemorySize();
        SystemOutPrintln.print_vm("RemoteAddress=" + selectedVM.getRemoteAddress());

//        VMInformation vmi = vm.getVMByName(name);
//        vmi.setCpuCoreNum(coreNum);
//        vmi.setMemorySize(memorySize);
        SystemOutPrintln.print_vm("vmNAME=" + name);
        SystemOutPrintln.print_vm("cpuCoreNum=" + coreNum);
        SystemOutPrintln.print_vm("memorySize=" + memorySize);
//        for(int i=0; i<vmInfoList.size();i++){
//           if(vmInfoList.get(i).getVmNAME().equals(name)){
//                vmInfoList.get(i) = vmi;
//               break;
//           }
//       }
        for (int i = 0; i < this.getVmInfoList().size(); i++) {
            SystemOutPrintln.print_vm("vmName=" + this.getVmInfoList().get(i).getVmNAME());
            SystemOutPrintln.print_vm("cpu=" + this.getVmInfoList().get(i).getCpuCoreNum());
            SystemOutPrintln.print_vm("memorySize=" + this.getVmInfoList().get(i).getMemorySize());
        }

        return "vm_vminfotab?faces-redirect=true";
    }

    public RegisterVMs getSelectedvm() {
        return selectedvm;
    }

    public void setSelectedvm(RegisterVMs selectedvm) {
        this.selectedvm = selectedvm;
    }

    public String toVmInfo() {
        String param = "vmName=" + selectedvm.getVmName();
        return "vm_vminfotab?faces-redirect=true&amp;" + param;
    }

    //在启动虚拟机或重启虚拟机时的限制信息
    public boolean limitedInformation() {
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        vminfo = vmface.getVMInfo(selectedvm.getVmName());

        if (vminfo != null) {
            vmVolume = vminfo.getVmBaseFolderPath();
            leftHostMemorySize = leftHostMemorySize - vminfo.getMemorySize();
        }



        //提示信息：将不存在的网卡重新绑定才能启动。
        List<NetworkInterfaceCard> networkInterfaceCard = vminfo.getNetworkInterfaceCard();
        boolean isUsable = true;
        String nic_tips = "";
        int cardCount = networkInterfaceCard.size();
        for (int i = 0; i < cardCount; i++) {
            int vmNICNum = networkInterfaceCard.get(i).getNicNum();
            String[] nicNames = networkInterfaceCard.get(i).getBindPhysicsNicName().split(" ");

            String vmNICName = nicNames[0];
            isUsable = networkInterfaceCard.get(i).isbVMNICIsUsable();
            if (isUsable == false) {
                String nic_tip = res.get(basename, "network") + " " + vmNICNum + ":" + vmNICName;
                nic_tips = nic_tips + "\n" + nic_tip;
            }

        }
        if (isUsable == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "pleasemodifycard") + nic_tips, ""));
            return false;
        }

        //提示信息：远程ip地址是否存在
        if (!vminfo.isbRemoteIPAddressUsable()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "pleasemodifyip"), ""));
            return false;
        }

        //提示信息：将损坏的文件卸载才能启动。
        List<Controller> controller = vminfo.getController();
        int controllerCount = controller.size();
        boolean isExists = true;
        String tips = "";
        for (int i = 0; i < controllerCount; i++) {

            List<BaseStorage> storage = controller.get(i).getStorage();  //控制器下加载存储设备，光盘或硬盘
            int storageCount = storage.size();
            String storageControlerName = controller.get(i).getControllerName();
            int storageControlerType = controller.get(i).getControllerType();
            String controlerName = "";
            if (storageControlerType == 1) {  //虚拟机存储控制器类型，1代表ide，2代表scsi，3代表sas
                controlerName = res.get(basename, "ide");
            } else if (storageControlerType == 2) {
                controlerName = res.get(basename, "scsi");
            } else if (storageControlerType == 3) {
                controlerName = res.get(basename, "sas");
            }
            for (int j = 0; j < storageCount; j++) {  //如果有硬盘或光盘在该控制器下
                String storageAttachMedium = storage.get(j).getStorageName();
                String[] sam = storageAttachMedium.split("/");
                String storageName = sam[sam.length - 1];

                int storageAttachType = storage.get(j).getStorageType();
                isExists = storage.get(j).isbStorageAttachMediumIsExists();

                if (isExists == false) {

                    String tip = "";
                    if (storageAttachType == 1) {  //存储设备类型取值1为硬盘，2为光盘
                        tip = res.get(basename, "pleaseuninstall") + controlerName + res.get(basename, "of") + storageName + res.get(basename, "disk");
                    } else if (storageAttachType == 2) {
                        tip = res.get(basename, "pleasepop") + controlerName + res.get(basename, "of") + storageName + res.get(basename, "cd");

                    }
                    tips = tips + "\n" + tip;
                }
            }

        }
        if (isExists == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "damagetip") + tips, ""));
            return false;
        }

        return true;
    }

    public void toDeleteVM() {
        vmname = selectedvm.getVmName();
        String vmpath = selectedvm.getPath();
        System.out.println("vmname=" + vmname);
        System.out.println("vmpath=" + vmpath);
        MsaCDPInterface vmface = InterfaceFactory.getCDPInterfaceInstance();
        List<String> targetpaths = vmface.getExistJobTargetPath();
        if (targetpaths == null && targetpaths.size() == 0) {
            RequestContext.getCurrentInstance().execute("deletevm.show()");
        } else {
            boolean flag = false;
            for (String targetpath : targetpaths) {
                System.out.println("targetpath" + targetpath);
                if (vmpath.startsWith(targetpath)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "notdelvm"), ""));
            } else {
                RequestContext.getCurrentInstance().execute("deletevm.show()");
            }
        }
         
        
      
    }
    public void deleteVM() {
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        String vmnameStr = selectedvm.getVmName();
        String state = selectedvm.getState();
        if (state.equals(res.get(basename, "vm_inaccessible"))) {
            VirtualMachine vmdamage = new VirtualMachine();
            vmdamage.setVmName(vmnameStr);
            SystemOutPrintln.print_vm("vmnameStr=" + vmnameStr + "damage  selectedvm.getUuid()=" + selectedvm.getUuid()+",selectedvm.getPath()="+selectedvm.getPath());
            vmdamage.setVmUUID(selectedvm.getUuid());
            vmdamage.setVmBaseFolderPath(selectedvm.getPath());
            boolean delvm = vmface.delete(vmdamage,true);
            if (!delvm) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "deletevmin") + selectedvm.getVmName() + res.get(basename, "fail"), ""));
            } else {
                SystemOutPrintln.print_vm("delete vm succeed");
                this.setVMListInfo();
            }

        } else {
            VirtualMachine vmStr = vmface.getVMInfo(vmnameStr);
            if (vmStr != null) {
                boolean delvm = vmface.delete(vmStr,false);
                if (delvm == false) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "deletevmin") + selectedvm.getVmName() + res.get(basename, "fail"), ""));
                } else {
                    SystemOutPrintln.print_vm("delete vm succeed");
                    this.setVMListInfo();
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "getvm") + selectedvm.getVmName() + res.get(basename, "infofail"), ""));
            }
        }



//        vmInfoList.remove(selectedVM);
    }

    public void startupVM() {  //启动虚拟机
        //限制信息
        if (limitedInformation() == false) {
            return;
        }
        long defaultMemorySizeSure = 5120;
        if (defaultMemorySize > 0) {
            defaultMemorySizeSure = defaultMemorySize;
        }
        //提示可用空间是否充足
        if (leftHostMemorySize < defaultMemorySizeSure) { //单位MB
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "cantstart"), ""));
            return;
        } else {
            startupVM_real();
        }
    }

    public void startupVM_real() {  //启动操作
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean startvm = vmface.start(selectedvm.getVmName(), selectedvm.getVmState());
        if (startvm == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "startvm_fail"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "startvm_ok"), ""));
            this.setVMListInfo();
        }
    }

    //暂停虚拟机
    public void pauseVM() {

        if (selectedvm.getVmState() == VirtualMachine.VM_RUN) {  //处于唤醒状态，下一次点击则为暂停
            //暂停操作
            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            boolean pausevm = vmface.suspend(selectedvm.getVmName(), selectedvm.getVmState());
            if (pausevm == false) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "pausevm_fail"), ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "pausevm_ok"), ""));
                this.setVMListInfo();
            }
        }

    }

    //恢复虚拟机
    public void resumeVM() {

        if (selectedvm.getVmState() == VirtualMachine.VM_PAUSE) {  //处于暂停状态，下一次点击则为唤醒
            //唤醒操作  
            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            boolean resumevm = vmface.resume(selectedvm.getVmName(), selectedvm.getVmState());
            if (resumevm == false) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "resumevm_fail"), ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "resumevm_ok"), ""));
                this.setVMListInfo();
            }

        }

    }

    //重启虚拟机
    public void rebootVM() {
        //限制信息
        if (limitedInformation() == false) {
            return;
        }
        RequestContext.getCurrentInstance().execute("rebootvm.show()");
        return;
    }

    public void rebootVM_real() {
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean rebootvm = vmface.reset(selectedvm.getVmName(), selectedvm.getVmState());
        if (rebootvm == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "rebootvm_fail"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "rebootvm_ok"), ""));
            this.setVMListInfo();
        }
    }

    public void closeVM() {
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean closevm = vmface.close(selectedvm.getVmName(), selectedvm.getVmState());
        if (closevm == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "closevm_fail"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "closevm_ok"), ""));
            this.setVMListInfo();
        }
    }
}
