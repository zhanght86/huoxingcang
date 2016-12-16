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

    private String vmNAME;  //�������
    private String diskName = "NewHardDisk1";
    private String selectDiskName;

    private String vmBaseFolder;//�����·��
    private String osType = "Microsoft Windows";//���������ϵͳ����
    private String vmVersion;//���������ϵͳ�汾
    private int cpuCoreNum;//CPU������
    private String memorySize = 256+"";//�ڴ��С
    private int boot1 = 1;  //����˳����ֵ�ͣ�1�����̡�2��Ӳ�̡�3������
    private int boot2 = 2;  //����˳����ֵ�ͣ�1�����̡�2��Ӳ�̡�3������
    private int boot3 = 3;  //����˳����ֵ�ͣ�1�����̡�2��Ӳ�̡�3������
    private List<ControllerInfo> controller = new ArrayList();  //�������б�
    private String vrdeAddress;  //VRDEԶ�̵�¼IP��ַ
//    private String vrdeAuthType;
    private String vrdePort = 5000 + "";  //Զ�̵�¼�˿ں�
    private boolean vrdeMultipleConnection = false;  //����Զ�̵�¼������,0����رգ�1������
    private String volume ;  //������
  
    VMInformation vmInfo;
    private ControllerInfo ide = null;
    private ControllerInfo scsi = null;
    private ControllerInfo sas = null;
    private int isHaveIDE = 0;
    private int isHaveSCSI = 0;
    private int isHaveSAS = 0;
    List<NetworkInterfaceCardInfo> network;
    private List<BootSequen> bootSequences;  //����˳��
    public List<ControllerType> controllerTypeList;
    ControllerType con;
    private Map<String, String> osTypes = new HashMap<String, String>();
    private Map<String, Map<String, String>> vmVersionsData = new HashMap<String, Map<String, String>>();
    private Map<String, String> vmVersions = new HashMap<String, String>();
    public List<String> vmVersionList;
    private int diskSize = 20;  //��λΪGB
     private String diskSizen = 20 + "";  //��λΪGB
    private String diskAddress = null;
    private int diskType = 0; //0������̬Ӳ�̣�1�����̶���СӲ��
//    private String city;
//    private String suburb;
    List<VMInformation> vmInfoList;
    
    public List<String> volumeList; //�����б�
    
    
    public boolean getVolumesState = true;  //��þ�����Ϣ�Ƿ�ɹ�
    private String path = null;
    public int hostCpuCoreNum = 0;
    public int hostMemorySize = 0;
    HostHardware hostHardwareInfo = null;
    public List<String> cpuCoreNumList; //cpul�б�
    public String memorySizeTip = "";
    public List<String> ipList; //ip��ַ�б�
    public int vrdeMultipleConnection_int = 0;  //������
    public List<VirtualDiskName> virDiskList;
    private VirtualDiskName selectedVirDisk;
    public boolean notFile = false;
    public boolean notName = false;
    public String createDiskType = 0 + "";
    private String diskNameStr = null;
    private int diskSizeInt = 1024;  //��λΪMB
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
        
        volumeList = new ArrayList(); //cpu�б�
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
            hostCpuCoreNum = hostHardwareInfo.getCpuNum();  //��ȡ����������CPU����
            SystemOutPrintln.print_vm("hostCpuCoreNum="+hostCpuCoreNum);
            hostMemorySize = hostHardwareInfo.getMemorySize();  //��ȡ�����������ڴ��С
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
         //����������ƺ�����
        
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
        
        //�����������Ӳ�̡�
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

            diskNameStr = diskName + ".vdi";  //ע�⣡��������������

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
            diskSizeInt = diskSize * 1024;  //ע�⣡��������������
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
            
        
        //Զ������
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
//        String vmUUID = "123";//�����UUID
//        String vmBaseFolderPath = vmBaseFolder;//�����·��
//        int boot1 = this.boot1;
//        int boot2 = this.boot2;
//        int boot3 = this.boot3;
//        String osType = this.vmVersion;//���������ϵͳ����
//        int cpuCoreNum = this.cpuCoreNum;//CPU������
//        int memorySize = this.memorySize;//�ڴ��С
//        int iIOAPIC = 0;
//        boolean supportIOAPIC = false;  //�Ƿ���IOACPI
//        if (iIOAPIC == 0) {  //0����������1��������
//            supportIOAPIC = false;
//        } else {
//            supportIOAPIC = true;
//        }
//        int iEFI = 0;
//        boolean supportEFI = false;  //�Ƿ���EFI
//        if (iEFI == 0) {  //0����������1������
//            supportEFI = false;
//        } else {
//            supportEFI = true;
//        }
//        int iUTCTime = 0;
//        boolean supportUTCTime = false;  //�Ƿ���UTCʱ��
//        if (iUTCTime == 0) {  //0����������1������
//            supportUTCTime = false;
//        } else {
//            supportUTCTime = true;
//        }
//        String remoteAddress = this.vrdeAddress;//Զ�̵�¼ip
//        int remotePort = Integer.valueOf(this.vrdePort);//Զ�̵�¼�˿ں�
//        int RemoteIPAddressUsable = 0;//��Զ�̵�¼ip�Ƿ����
//        int remoteTimeOut = 500;//��ʱʱ��
//        String remoteAuthentication = "ask";//��֤��ʽ
//        int iMultipleConnections = 1;
//        boolean remoteMultipleConnections = vrdeMultipleConnection;//�Ƿ�����Զ�����Ӷ�����
////        iMultipleConnections = vrdeMultipleConnection;
////        if (iMultipleConnections == 0) {
////            remoteMultipleConnections = false;
////        } else {
////            remoteMultipleConnections = true;
////        }
//
//        int controllerCount = 1;
//        controller = new ArrayList();  //���ؿ�����
//
//
////            XMLParser childParser = returnXML.createXMLParser("MSA/ReturnValue/StorageInfo/StorageControler",i);
//        String StorageControlerName = "IDE";
//        int controllerType = 1;  //������洢���������ͣ�1����ide��2����scsi��3����sas
//        int controllerChipset = 1;
//
//        ControllerInfo controllerInfo = new ControllerInfo();
//        controllerInfo.setControllerName(StorageControlerName);
//        controllerInfo.setControllerType(controllerType);
//        controllerInfo.setControllerChipset(controllerChipset);
//
//        int StorageCount = 1;
//        List<BaseStorageInfo> storage = new ArrayList();  //�������¼��ش洢�豸�����̻�Ӳ��
//        for (int j = 0; j < StorageCount; j++) {
////                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
//            String storageAttachMedium = "emptydrive";
//            int StorageAttachMediumIsExists = 1;
//            int storageAttachType = 2;  //�洢�豸����ȡֵ1ΪӲ�̣�2Ϊ����
//            long storageAttachSize = 20;
//            long StorageAttachUsedSize = 20;
//            int StorageAttachIsPhysical = (int) 1;
//            String storageAttachFormat = "Normal(VDI)";  //�洢�豸��ʽ
//            int storageAttachPort = 0;  //�洢�豸�˿ںţ�IDEȡֵ0��1��SCSIȡֵ0��14��SASȡֵ0-7
//            int StorageAttachDevice = 0;  //�洢�豸�豸�ţ�IDE��������ȡֵ0��1��SAS��SCSIȡֵ0
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
//        int controllerType1 = 2;  //������洢���������ͣ�1����ide��2����scsi��3����sas
//        int controllerChipset1 = 1;
//
//        ControllerInfo controllerInfo1 = new ControllerInfo();
//        controllerInfo1.setControllerName(StorageControlerName1);
//        controllerInfo1.setControllerType(controllerType1);
//        controllerInfo1.setControllerChipset(controllerChipset1);
//
//        int StorageCount1 = 1;
//        List<BaseStorageInfo> storage1 = new ArrayList();  //�������¼��ش洢�豸�����̻�Ӳ��
//        for (int j = 0; j < StorageCount1; j++) {
////                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
//            String storageAttachMedium = this.vmNAME;
//            int StorageAttachMediumIsExists = 1;
//            int storageAttachType = 1;  //�洢�豸����ȡֵ1ΪӲ�̣�2Ϊ����
//            long storageAttachSize = 20;
//            long StorageAttachUsedSize = 20;
//            int StorageAttachIsPhysical = (int) 1;
//            String storageAttachFormat = "Normal(VDI)";  //�洢�豸��ʽ
//            int storageAttachPort = 0;  //�洢�豸�˿ںţ�IDEȡֵ0��1��SCSIȡֵ0��14��SASȡֵ0-7
//            int StorageAttachDevice = 0;  //�洢�豸�豸�ţ�IDE��������ȡֵ0��1��SAS��SCSIȡֵ0
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
//        network = new ArrayList();  //��������
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
//       FacesContext context = FacesContext.getCurrentInstance();  //session�򣬸���VMList����vmInfoListֵ
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
    //���ʹ������������
    public String sendMeaaage() {

//            int timeOut =  node.socket.getSocket().getSoTimeout();
//            node.socket.getSocket().setSoTimeout(0);
        if (createDiskType.equals("0")) {
            //���ʹ�������Ӳ�̵�����
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
    
      //��ò���ϵͳ
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
    //���Ҫ�������������Ϣ
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


        List<Controller> controllers = new ArrayList();  //���ؿ�����
        //Ĭ�ϼ���һ��IDE������������IDE�������¼���һ������
        Controller controllerInfo1 = new Controller();
        controllerInfo1.setControllerName("IDE Controller");
        controllerInfo1.setControllerType(1);//������洢���������ͣ�1����ide��2����scsi��3����sas
        controllerInfo1.setControllerChipset(5);
        List<BaseStorage> storage1 = new ArrayList(); //�������¼��ش洢�豸�����̻�Ӳ��
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

        //Ĭ�ϼ���һ��SCSI������������IDE�������¼���һ������
        Controller controllerInfo2 = new Controller();
        controllerInfo2.setControllerName("SCSI Controller"); //SAS Controller
        controllerInfo2.setControllerType(2);//������洢���������ͣ�1����ide��2����scsi��3����sas
        controllerInfo2.setControllerChipset(1);
        if (!getjRadioButton5State()) {
            List<BaseStorage> storage2 = new ArrayList(); //�������¼��ش洢�豸��1ΪӲ�̣�2Ϊ����
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
   //��á�������Ӳ�̡���ѡ��ť�Ƿ�ѡ��
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
      
       //���IP��ַ
    public String getIpAddress(){
       String ip = vrdeAddress;
       if(ip.equalsIgnoreCase(res.get(basename, "any_ip"))){
           return null;
       }
       return ip;
    }
    
       //��ȡ�Ƿ������������
    public int getVRDEMultipleConnection(){
        if(vrdeMultipleConnection){
            vrdeMultipleConnection_int = 1;  //����
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
                        if (disk.getState()!= null && disk.getState().equalsIgnoreCase("inaccessible")) {  //�����𻵵�Ӳ����ʾ����
                            
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
