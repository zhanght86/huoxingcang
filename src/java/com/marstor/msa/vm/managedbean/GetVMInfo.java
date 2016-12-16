/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.vm.model.BaseStorageInfo;
import com.marstor.msa.vm.model.ControllerInfo;
import com.marstor.msa.vm.model.ControllerType;
import com.marstor.msa.vm.model.NetworkInterfaceCardInfo;
import com.marstor.msa.vm.model.VMInformation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "getVMInfo")
@RequestScoped
public class GetVMInfo implements Serializable{

    VMInformation vmInfo;
    public List<ControllerType> controllerTypeList;
    ControllerType con;
    List<NetworkInterfaceCardInfo> network;
    List<ControllerInfo> controllers;
    private ControllerInfo ide = null;
    private ControllerInfo scsi = null;
    private ControllerInfo sas = null;
    List<BaseStorageInfo> ideStorage;
    List<BaseStorageInfo> scsiStorage;
    List<BaseStorageInfo> sasStorage;

    /**
     * Creates a new instance of GetVMInfo
     */
    public GetVMInfo() {
        this.getVM();
        this.getControllerList();
        this.getIDEStorage();
        this.getSCSIStorage();
        this.getSASStorage();
                
    }

    public List getControllerList() {
        controllerTypeList = new ArrayList<ControllerType>();
        con = new ControllerType();
        con.setControllerName("IDE");
        con.setControllerType(1);
        con.setIsHave(1);
        con.setOperation(setResource("controller_" + con.getIsHave()));
        controllerTypeList.add(con);

        con = new ControllerType();
        con.setControllerName("SCSI");
        con.setControllerType(2);
        con.setIsHave(1);
        con.setOperation(setResource("controller_" + con.getIsHave()));
        controllerTypeList.add(con);

        con = new ControllerType();
        con.setControllerName("SAS");
        con.setControllerType(3);
        con.setIsHave(1);
        con.setOperation(setResource("controller_" + con.getIsHave()));
        controllerTypeList.add(con);


        return controllerTypeList;
    }

    public VMInformation getVM() {

        String vmNAME = "test1";
        String vmUUID = "123";//�����UUID
        String vmBaseFolderPath = "/C/test1";//�����·��
        int boot1 = 1;
        int boot2 = 2;
        int boot3 = 3;
        String osType = "Windows 2003";//���������ϵͳ����
        int cpuCoreNum = 1;//CPU������
        int memorySize = 256;//�ڴ��С
        int iIOAPIC = 0;
        boolean supportIOAPIC = false;  //�Ƿ���IOACPI
        if (iIOAPIC == 0) {  //0����������1��������
            supportIOAPIC = false;
        } else {
            supportIOAPIC = true;
        }
        int iEFI = 0;
        boolean supportEFI = false;  //�Ƿ���EFI
        if (iEFI == 0) {  //0����������1������
            supportEFI = false;
        } else {
            supportEFI = true;
        }
        int iUTCTime = 0;
        boolean supportUTCTime = false;  //�Ƿ���UTCʱ��
        if (iUTCTime == 0) {  //0����������1������
            supportUTCTime = false;
        } else {
            supportUTCTime = true;
        }
        String remoteAddress = "192.168.1.88";//Զ�̵�¼ip
        int remotePort = 5000;//Զ�̵�¼�˿ں�
        boolean RemoteIPAddressUsable = false;// ��Զ�̵�¼ip�Ƿ����   0???
        int remoteTimeOut = 500;//��ʱʱ��
        String remoteAuthentication = "ask";//��֤��ʽ
        int iMultipleConnections = 1;
        boolean remoteMultipleConnections = false;//�Ƿ�����Զ�����Ӷ�����
        if (iMultipleConnections == 0) {
            remoteMultipleConnections = false;
        } else {
            remoteMultipleConnections = true;
        }

        int controllerCount = 1;
        controllers = new ArrayList();  //���ؿ�����


//            XMLParser childParser = returnXML.createXMLParser("MSA/ReturnValue/StorageInfo/StorageControler",i);
        String StorageControlerName = "IDE";
        int controllerType = 1;  //������洢���������ͣ�1����ide��2����scsi��3����sas
        int controllerChipset = 1;

        ControllerInfo controllerInfo = new ControllerInfo();
        controllerInfo.setControllerName(StorageControlerName);
        controllerInfo.setControllerType(controllerType);
        controllerInfo.setControllerChipset(controllerChipset);

        int StorageCount = 1;
        List<BaseStorageInfo> storage = new ArrayList();  //�������¼��ش洢�豸�����̻�Ӳ��
        for (int j = 0; j < StorageCount; j++) {
//                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
            String storageAttachMedium = "disk1";
            boolean StorageAttachMediumIsExists = true;  //1??
            int storageAttachType = 1;  //�洢�豸����ȡֵ1ΪӲ�̣�2Ϊ����
            long storageAttachSize = 20;
            long StorageAttachUsedSize = 20;
            boolean StorageAttachIsPhysical = true;//(int) 1;???
            String storageAttachFormat = "Normal(VDI)";  //�洢�豸��ʽ
            int storageAttachPort = 0;  //�洢�豸�˿ںţ�IDEȡֵ0��1��SCSIȡֵ0��14��SASȡֵ0-7
            int StorageAttachDevice = 0;  //�洢�豸�豸�ţ�IDE��������ȡֵ0��1��SAS��SCSIȡֵ0

            BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
            baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
            baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
            baseStorageInfo.setStorageAttachType(storageAttachType);
            baseStorageInfo.setStorageAttachSize(storageAttachSize+"");
            baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
            baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize+"");
            baseStorageInfo.setStorageAttachFormat(storageAttachFormat);
            baseStorageInfo.setStorageAttachPort(storageAttachPort);
            baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
            baseStorageInfo.setStrstate(this.setResource("Storage_deviceState_1"));

            storage.add(baseStorageInfo);
        }

        controllerInfo.setStorage(storage);
        controllers.add(controllerInfo);

        if (controllerType == 1) {
            ide = controllerInfo;
        }



//            XMLParser childParser = returnXML.createXMLParser("MSA/ReturnValue/StorageInfo/StorageControler",i);
        String StorageControlerName1 = "SCSI";
        int controllerType1 = 2;  //������洢���������ͣ�1����ide��2����scsi��3����sas
        int controllerChipset1 = 1;

        ControllerInfo controllerInfo1 = new ControllerInfo();
        controllerInfo1.setControllerName(StorageControlerName1);
        controllerInfo1.setControllerType(controllerType1);
        controllerInfo1.setControllerChipset(controllerChipset1);

        int StorageCount1 = 1;
        List<BaseStorageInfo> storage1 = new ArrayList();  //�������¼��ش洢�豸�����̻�Ӳ��
        for (int j = 0; j < StorageCount1; j++) {
//                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
            String storageAttachMedium = "disk12";
             boolean StorageAttachMediumIsExists = true;  //1??
            int storageAttachType = 1;  //�洢�豸����ȡֵ1ΪӲ�̣�2Ϊ����
            long storageAttachSize = 20;
            long StorageAttachUsedSize = 20;
            boolean StorageAttachIsPhysical = true;//(int) 1;???
            String storageAttachFormat = "Normal(VDI)";  //�洢�豸��ʽ
            int storageAttachPort = 0;  //�洢�豸�˿ںţ�IDEȡֵ0��1��SCSIȡֵ0��14��SASȡֵ0-7
            int StorageAttachDevice = 0;  //�洢�豸�豸�ţ�IDE��������ȡֵ0��1��SAS��SCSIȡֵ0

            BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
            baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
            baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
            baseStorageInfo.setStorageAttachType(storageAttachType);
            baseStorageInfo.setStorageAttachSize(storageAttachSize+"");
            baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
            baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize+"");
            baseStorageInfo.setStorageAttachFormat(storageAttachFormat);
            baseStorageInfo.setStorageAttachPort(storageAttachPort);
            baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
             baseStorageInfo.setStrstate(this.setResource("Storage_deviceState_1"));

            storage1.add(baseStorageInfo);
        }

        controllerInfo1.setStorage(storage1);
        controllers.add(controllerInfo1);
        if (controllerType1 == 2) {
            scsi = controllerInfo1;
        }



//            XMLParser childParser = returnXML.createXMLParser("MSA/ReturnValue/StorageInfo/StorageControler",i);
        String StorageControlerName3 = "SAS";
        int controllerType3 = 3;  //������洢���������ͣ�1����ide��2����scsi��3����sas
        int controllerChipset3 = 1;

        ControllerInfo controllerInfo3 = new ControllerInfo();
        controllerInfo3.setControllerName(StorageControlerName3);
        controllerInfo3.setControllerType(controllerType3);
        controllerInfo3.setControllerChipset(controllerChipset3);

        int StorageCount3 = 1;
        List<BaseStorageInfo> storage3 = new ArrayList();  //�������¼��ش洢�豸�����̻�Ӳ��
        for (int j = 0; j < StorageCount3; j++) {
//                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
            String storageAttachMedium = "disk13";
            boolean StorageAttachMediumIsExists = true;  //1??
            int storageAttachType = 1;  //�洢�豸����ȡֵ1ΪӲ�̣�2Ϊ����
            long storageAttachSize = 20;
            long StorageAttachUsedSize = 20;
            boolean StorageAttachIsPhysical = true;//(int) 1;???
            String storageAttachFormat = "Normal(VDI)";  //�洢�豸��ʽ
            int storageAttachPort = 0;  //�洢�豸�˿ںţ�IDEȡֵ0��1��SCSIȡֵ0��14��SASȡֵ0-7
            int StorageAttachDevice = 0;  //�洢�豸�豸�ţ�IDE��������ȡֵ0��1��SAS��SCSIȡֵ0

            BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
            baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
            baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
            baseStorageInfo.setStorageAttachType(storageAttachType);
            baseStorageInfo.setStorageAttachSize(storageAttachSize+"");
            baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
            baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize+"");
            baseStorageInfo.setStorageAttachFormat(storageAttachFormat);
            baseStorageInfo.setStorageAttachPort(storageAttachPort);
            baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
             baseStorageInfo.setStrstate(this.setResource("Storage_deviceState_1"));

            storage3.add(baseStorageInfo);
        }

        controllerInfo3.setStorage(storage3);
        controllers.add(controllerInfo3);
        if (controllerType3 == 3) {
            sas = controllerInfo3;
        }

//        for(int i = 0; i < controllerCount; i++)
//        {
//
////            XMLParser childParser = returnXML.createXMLParser("MSA/ReturnValue/StorageInfo/StorageControler",i);
//            String StorageControlerName = "IDE";
//            int controllerType = 1;  //������洢���������ͣ�1����ide��2����scsi��3����sas
//            int controllerChipset = 1;
//
//            ControllerInfo controllerInfo = new ControllerInfo();
//            controllerInfo.setControllerName(StorageControlerName);
//            controllerInfo.setControllerType(controllerType);
//            controllerInfo.setControllerChipset(controllerChipset);
//            
//            int StorageCount = 1;
//            List<BaseStorageInfo> storage = new ArrayList();  //�������¼��ش洢�豸�����̻�Ӳ��
//            for(int j = 0; j < StorageCount; j++){
////                XMLParser storageChildParser = childParser.createXMLParser("StorageControler/StorageAttach", j);
//                String storageAttachMedium = "disk1";
//                int StorageAttachMediumIsExists = 1;
//                int storageAttachType = 1;  //�洢�豸����ȡֵ1ΪӲ�̣�2Ϊ����
//                long storageAttachSize = 20;
//                long StorageAttachUsedSize = 20;
//                int StorageAttachIsPhysical = (int) 1;
//                String storageAttachFormat = "ask2";  //�洢�豸��ʽ
//                int storageAttachPort = 0;  //�洢�豸�˿ںţ�IDEȡֵ0��1��SCSIȡֵ0��14��SASȡֵ0-7
//                int StorageAttachDevice = 0;  //�洢�豸�豸�ţ�IDE��������ȡֵ0��1��SAS��SCSIȡֵ0
//
//                BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
//                baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
//                baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
//                baseStorageInfo.setStorageAttachType(storageAttachType);
//                baseStorageInfo.setStorageAttachSize(storageAttachSize);
//                baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
//                baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize);
//                baseStorageInfo.setStorageAttachFormat(storageAttachFormat);
//                baseStorageInfo.setStorageAttachPort(storageAttachPort);
//                baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
//
//                storage.add(baseStorageInfo);
//            }
//
//            controllerInfo.setStorage(storage);
//            controllers.add(controllerInfo);
//        }

        int nicCount = 1;
        network = new ArrayList();  //��������
        for (int i = 0; i < nicCount; i++) {
            int vmNICNum = 1;
            boolean vmNICIsUsable = true;//1???
            String vmNICName = "network1";
            String macAddress = "01252456";
            int cableConnected = 1;
            String bindPhysicsNicIpAddress = "BindPhysicsIPAddress1";

            NetworkInterfaceCardInfo cardInfo = new NetworkInterfaceCardInfo();
            cardInfo.setVmNICNum(vmNICNum);
            cardInfo.setVmNICIsUsable(vmNICIsUsable);
            cardInfo.setVmNICName(vmNICName);
            cardInfo.setMacAddress(macAddress);
            cardInfo.setCableConnected(cableConnected);
            cardInfo.setStrCableConnected(setResource("net_cableConnected_" + cardInfo.getCableConnected()));
            cardInfo.setBindPhysicsNicIpAddress(bindPhysicsNicIpAddress);
            network.add(cardInfo);
        }


        vmInfo = new VMInformation();
        vmInfo.setVmNAME(vmNAME);
        vmInfo.setVmUUID(vmUUID);
        vmInfo.setVmBaseFolderPath(vmBaseFolderPath);
        vmInfo.setBoot1(boot1);
        vmInfo.setBoot2(boot2);
        vmInfo.setBoot3(boot3);
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
        return vmInfo;
    }

    public List<BaseStorageInfo> getIDEStorage() {

        if (ide != null) {
            ideStorage = ide.getStorage();
        }
        return ideStorage;
    }

    public List<BaseStorageInfo> getSCSIStorage() {

        if (scsi != null) {
            scsiStorage = scsi.getStorage();
        }
        return scsiStorage;
    }

    public List<BaseStorageInfo> getSASStorage() {

        if (sas != null) {
            sasStorage = sas.getStorage();
        }
        return sasStorage;
    }

    public VMInformation getVmInfo() {
        return vmInfo;
    }

    public void setVmInfo(VMInformation vmInfo) {
        this.vmInfo = vmInfo;
    }

    public List<ControllerType> getControllerTypeList() {
        return controllerTypeList;
    }

    public void setControllerTypeList(List<ControllerType> controllerTypeList) {
        this.controllerTypeList = controllerTypeList;
    }

    public String setResource(String resource) {
        return java.util.ResourceBundle.getBundle("com/marstor/msa/common/resources/messages").getString(resource);
    }

    public List<NetworkInterfaceCardInfo> getNetwork() {
        return network;
    }

    public void setNetwork(List<NetworkInterfaceCardInfo> network) {
        this.network = network;
    }

    public List<BaseStorageInfo> getIdeStorage() {
        return ideStorage;
    }

    public void setIdeStorage(List<BaseStorageInfo> ideStorage) {
        this.ideStorage = ideStorage;
    }

    public List<BaseStorageInfo> getScsiStorage() {
        return scsiStorage;
    }

    public void setScsiStorage(List<BaseStorageInfo> scsiStorage) {
        this.scsiStorage = scsiStorage;
    }

    public List<BaseStorageInfo> getSasStorage() {
        return sasStorage;
    }

    public void setSasStorage(List<BaseStorageInfo> sasStorage) {
        this.sasStorage = sasStorage;
    }

    public List<ControllerInfo> getControllers() {
        return controllers;
    }

    public void setControllers(List<ControllerInfo> controllers) {
        this.controllers = controllers;
    }
    
    
}
