/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.bean.VolumeGroupInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.bean.BaseStorage;
import com.marstor.msa.vbox.bean.Controller;
import com.marstor.msa.vbox.bean.HDInfo;
import com.marstor.msa.vbox.bean.HardDisk;
import com.marstor.msa.vbox.bean.VirtualMachine;
import com.marstor.msa.vbox.web.MsaVMInterface;
import com.marstor.msa.vm.model.ControllerInfo;
import com.marstor.msa.vm.model.VMInformation;
import com.marstor.msa.vm.model.VirtualDiskName;
import com.marstor.msa.vm.model.VirtualDiskNameModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "creatDisk")
@ViewScoped
public class CreatDisk implements Serializable{

    public String controllerType = "IDE";
    private String selectDiskName;
    private String diskName = "NewHardDisk1";
    private int diskType = 0; //0������̬Ӳ�̣�1�����̶���СӲ��
    private int diskSize = 20;  //��λΪGB
    private String diskSizen = 20 + "";  //��λΪGB
    VMInformation selectVM;
    public List<ControllerInfo> controller;//������
//    public ControllerInfo ideController;
//    public ControllerInfo scsiController;
//    public ControllerInfo sasController;
    Controller ideController;
    public boolean notFile = false;
    public boolean notName = false;
    public String createDiskType = 0 + "";
    public List<VirtualDiskName> virDiskList;
    public String vmName;
    int controType = 1;
    public String controName;
    private String path = null;  //??????????
    public String virDiskSizetip;
    private VirtualDiskName selectedVirDisk;
    String vmVolume = null; //������
    VolumeGroupInformation[] volumeInfos;
    private String diskNameStr = null;
    private int diskSizeInt = 1024;  //��λΪMB
    private String diskAddress = null;
    Controller contro;
    List<BaseStorage> baseInfo = null;
    public VirtualDiskNameModel vdsModel;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_vminfotable_addDisk";

    /**
     * Creates a new instance of CreatDisk
     */
    public CreatDisk() {
        selectDiskName = res.get(basename, "select");
        controName = res.get(basename, "controller_type_ide");
        virDiskSizetip = res.get(basename, "virdisksizetip");
        
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        vmName = request.getParameter("vmName");
        SystemOutPrintln.print_vm("vmName=" + vmName);
        if (vmName != null) {
            SystemOutPrintln.print_vm("��������ֲ�Ϊ��");
            String conT = request.getParameter("controType");
            if (conT != null) {
                controType = Integer.valueOf(conT);
                if (controType == Controller.CONTROLLER_TYPE_IDE) {
                    controName = res.get(basename, "controller_type_ide");
                } else if (controType == Controller.CONTROLLER_TYPE_SCSI) {
                    controName = res.get(basename, "controller_type_scsi");
                } else if (controType == Controller.CONTROLLER_TYPE_SAS) {
                    controName = res.get(basename, "controller_type_sas");
                }
            }
            SystemOutPrintln.print_vm("controType=" + controType);

            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            VirtualMachine vm = vmface.getVMInfo(vmName);
            String vmBaseFolderPath = vm.getVmBaseFolderPath();
            String[] folderPath = vmBaseFolderPath.split("/");
            vmVolume = folderPath[1];
            path = "/" + vmVolume + "/VM/" + vmName;
            SystemOutPrintln.print_vm("path=" + path);
            notFile = true;
            notName = false;

            List<Controller> conList = vm.getController();
            if (conList != null && conList.size() > 0) {
                int controllerCount = conList.size();
                SystemOutPrintln.print_vm("controllerCount=" + controllerCount);

                for (int i = 0; i < conList.size(); i++) {
                    SystemOutPrintln.print_vm("conList.get(i).getControllerName()=" + conList.get(i).getControllerName());
                    int conType = conList.get(i).getControllerType();  //"IDE"
                    if (controType == conType) {
                        contro = conList.get(i);
                    }



                }
            }

            VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
            volumeInfos = volumeIn.getAllVolumeGroup();

            //��ʼ��ʹ������Ӳ��
            initVirDiskList();
            vdsModel = new VirtualDiskNameModel(virDiskList);
        }


        
    }

    public VirtualDiskName getSelectedVirDisk() {
        return selectedVirDisk;
    }

    public void setSelectedVirDisk(VirtualDiskName selectedVirDisk) {
        this.selectedVirDisk = selectedVirDisk;
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

    public int getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(int diskSize) {
        this.diskSize = diskSize;
    }

    

    public String getSelectDiskName() {
        return selectDiskName;
    }

    public void setSelectDiskName(String selectDiskName) {
        this.selectDiskName = selectDiskName;
    }

    public String getControllerType() {
        return controllerType;
    }

    public void setControllerType(String controllerType) {
        this.controllerType = controllerType;
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

    public List<VirtualDiskName> getVirDiskList() {
        return virDiskList;
    }

    public void setVirDiskList(List<VirtualDiskName> virDiskList) {
        this.virDiskList = virDiskList;
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

    public String createDisk_byController() {

        //�����������Ӳ�̡�
        if (createDiskType.equals("0")) {
            if (diskName.length() < 21) {
                if (!diskName.matches("[a-zA-Z0-9]+")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "diskname_limit"), ""));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "diskname_limit"), ""));
                return null;
            }

            diskNameStr = diskName + ".vdi";  //ע�⣡��������������

//                 if(jPCreateVMHardDisk_3.getDiskDynamic()){
//                     diskType = 0;
//                 }
//                 if(jPCreateVMHardDisk_3.getDiskStatic()){
//                     diskType = 1;
//                 }
//            if (!ValidateUtility.checkSize(diskSizen.trim(), false, 2049)) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "������̴�СΪ1GB-2TB�����������롣", ""));
//                return null;
//            } else {
//      
//                diskSize = Integer.valueOf(diskSizen);
//                
//            }
//
//            diskSizeInt = diskSize * 1024;  //ע�⣡��������������
//            if (diskSizeInt == 0) {
//                diskSize = 1;
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "������̴�С����Ϊ��ֵ�Ҳ���С��1GB�����������롣", ""));
//                return null;
//            }
            diskAddress = path + "/" + diskNameStr;
            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            List<String> fileNames = vmface.listFiles(path);
            if (fileNames != null) {
                for (int i = 0; i < fileNames.size(); i++) {
                    if (fileNames.get(i).equals(diskNameStr)) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "diskname_exit"), ""));
                        return null;

                    }
                }
            }
            
            baseInfo = contro.getStorage(); //Ŀǰ�˿���������������Ѿ����ڵĴ洢�豸
            for (int i = 0; i < baseInfo.size(); i++) {
                if (baseInfo.get(i).getStorageName().equals(diskAddress)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "diskname_exit"), ""));
                    return null;
                }
            }
            
            
            if (!ValidateUtility.checkSize(diskSizen.trim(), false, 2049)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "disksizen_limit"), ""));
                return null;
            } else {
      
                diskSize = Integer.valueOf(diskSizen);
                
            }

            diskSizeInt = diskSize * 1024;  //ע�⣡��������������
          

        }

        if (createDiskType.equals("1")) {
            diskAddress = selectDiskName;
            if (diskAddress == null || diskAddress.equalsIgnoreCase("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "selectdisk"), ""));
                return null;
            }
            
        }

//        baseInfo = contro.getStorage(); //Ŀǰ�˿���������������Ѿ����ڵĴ洢�豸
//        for (int i = 0; i < baseInfo.size(); i++) {
//            if (baseInfo.get(i).getStorageName().equals(diskAddress)) {
//                System.out.println("44444");
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ӳ�������Ѵ��ڣ����������롣", ""));
//                return null;
//            }
//        }

        if (diskType == 1) {
            String availableSize = null;
            for (int i = 0; i < volumeInfos.length; i++) {
                if (vmVolume.equalsIgnoreCase(volumeInfos[i].getName())) {
                    availableSize = volumeInfos[i].getAvailableSize();
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

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "diskSize_less"), ""));
                return null;
            }
        }

        if (createDiskType.equals("0")) {
            //���ʹ�������Ӳ�̵�����
            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            HardDisk hd = new HardDisk();
            hd.setStorageName(diskAddress);
            SystemOutPrintln.print_vm("diskSizeInt="+diskSizeInt);
            hd.setSize(diskSizeInt);
            if (diskType == 1) {
                hd.setDiskIsStatic(true);
            } else {
                hd.setDiskIsStatic(false);
            }
            boolean createVMDisk = vmface.createDisk(hd);
            if (!createVMDisk) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "createvmdisk_fail"), ""));

                return null;
            } else {
                SystemOutPrintln.print_vm("create hd success ");
            }
        }

        BaseStorage baseStorageInfo = new BaseStorage();  //??????
        baseStorageInfo.setStorageName(diskAddress);
        baseStorageInfo.setStorageType(1);
        baseStorageInfo.setPhysicsDrive(false);
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        SystemOutPrintln.print_vm("vmName=" + vmName + ",contro.getControllerType()=" + contro.getControllerType() + ",diskAddress=" + diskAddress);
        boolean addStorage = vmface.addStorage(vmName, contro, baseStorageInfo);
        String returnStr;
        if (addStorage) {
            String param = "vmName=" + vmName;
            returnStr = "vm_vminfotab?faces-redirect=true" + param;
            SystemOutPrintln.print_vm("add disk success");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "addstorage_fail"), ""));
            returnStr = null;
        }
        return returnStr;

//        
//         
//        
//        String storageAttachMedium = diskName;
//        boolean StorageAttachMediumIsExists = true; //1  ????
//        int storageAttachType = 1;  //�洢�豸����ȡֵ1ΪӲ�̣�2Ϊ����
//        long storageAttachSize = 20;
//        long StorageAttachUsedSize = diskSize;
//        boolean StorageAttachIsPhysical = true;  //(int) 1 ???
//        String storageAttachFormat = "Normal(VDI)";  //�洢�豸��ʽ
//        int storageAttachPort = 0;  //�洢�豸�˿ںţ�IDEȡֵ0��1��SCSIȡֵ0��14��SASȡֵ0-7
//        int StorageAttachDevice = 0;  //�洢�豸�豸�ţ�IDE��������ȡֵ0��1��SAS��SCSIȡֵ0
//
//        BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
//        baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
//        baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
//        baseStorageInfo.setStorageAttachType(storageAttachType);
//        baseStorageInfo.setStorageAttachSize(storageAttachSize+"");
//        baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
//        baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize+"");
//        baseStorageInfo.setStorageAttachFormat(storageAttachFormat);                  
//        baseStorageInfo.setStorageAttachPort(storageAttachPort);
//        baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
//        baseStorageInfo.setStrstate(this.setResource("Storage_deviceState_1"));
//
//
//        FacesContext context = FacesContext.getCurrentInstance();  //session�򣬸���VMList����vmInfoListֵ
//        VMList share = (VMList) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{vMList}", VMList.class).getValue(context.getELContext());
//
//        selectVM = share.getSelectedVM();
//        if (selectVM != null) {
//            System.out.println("^^^^^^^^^^^^^^^^ѡ�е������=" + selectVM.getVmNAME());
//            controller = selectVM.getController();
//            if (controllerType.equalsIgnoreCase("IDE")) {
//                for (int i = 0; i < controller.size(); i++) {
//                    int type = controller.get(i).getControllerType();
//                    if (type == 1) {  //������洢���������ͣ�1����ide��2����scsi��3����sas
//                        controller.get(i).getStorage().add(baseStorageInfo);
//                        System.out.println("IDE�������д洢�豸����=" + controller.get(i).getStorage().size());
//                        break;
//                    }
//                }
//
////            ideController = selectVM.getIdeController();
////            ideController.getStorage().add(baseStorageInfo);
//            }else if(controllerType.equalsIgnoreCase("SCSI")){
//                for (int i = 0; i < controller.size(); i++) {
//                    int type = controller.get(i).getControllerType();
//                    if (type == 2) {  //������洢���������ͣ�1����ide��2����scsi��3����sas
//                        controller.get(i).getStorage().add(baseStorageInfo);
//                        System.out.println("IDE�������д洢�豸����=" + controller.get(i).getStorage().size());
//                        break;
//                    }
//                }
//            }else if(controllerType.equalsIgnoreCase("SAS")){
//                for (int i = 0; i < controller.size(); i++) {
//                    int type = controller.get(i).getControllerType();
//                    if (type == 3) {  //������洢���������ͣ�1����ide��2����scsi��3����sas
//                        controller.get(i).getStorage().add(baseStorageInfo);
//                        System.out.println("IDE�������д洢�豸����=" + controller.get(i).getStorage().size());
//                        break;
//                    }
//                }
//            }
//
//        }
//
//
//        return "vm_vminfotab?faces-redirect=true";
    }

    public String cancleButton() {
        String param = "vmName=" + vmName;
        return "vm_vminfotab?faces-redirect=true&amp;" + param;
    }

    public void changeBooleanCheckbox() {
        if (createDiskType.equals("0")) {
            notFile = true;
            notName = false;
        } else if (createDiskType.equals("1")) {
            notFile = false;
            notName = true;
        } else {
            notFile = true;
            notName = true;
        }

    }

    public void clickButton() {
//         path = "/" + volume + "" + "/VM/" + vmName;
       
        RequestContext.getCurrentInstance().execute("dlg.show()");
    }

    public void initVirDiskList() {

        virDiskList = new ArrayList();
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        List<HDInfo> disks = vmface.getHDInfo();

        if (disks != null && disks.size() > 0) {
            for (HDInfo disk : disks) {

                String[] fullPath = disk.getHDName().split("/");
                String diskPath = "/" + fullPath[1] + "/" + fullPath[2] + "/" + fullPath[3];
                SystemOutPrintln.print_vm("diskPath="+diskPath);
                SystemOutPrintln.print_vm("path="+path);
                if (diskPath.endsWith(path)) {
                    if (disk.isUsage() == false) {
                        if (disk.getState() != null && disk.getState().equalsIgnoreCase("inaccessible")) {  //�����𻵵�Ӳ����ʾ����
                        } else {
                            VirtualDiskName data = new VirtualDiskName();
                            SystemOutPrintln.print_vm("����disk.getHDName()=" + disk.getHDName());
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
        if (selectedVirDisk != null) {
            selectDiskName = selectedVirDisk.getName();
        }

        RequestContext.getCurrentInstance().execute("dlg.hide()");
    }

    public String setIDEControType() {
        controllerType = "IDE";

        SystemOutPrintln.print_vm("controllerType=" + controllerType);
        return "vm_vminfotable_addDisk?faces-redirect=true";
    }

    public String setSCSIControType() {
        controllerType = "SCSI";

        SystemOutPrintln.print_vm("controllerType=" + controllerType);
        return "vm_vminfotable_addDisk?faces-redirect=true";
    }

    public String setSASControType() {
        controllerType = "SAS";

        SystemOutPrintln.print_vm("controllerType=" + controllerType);
        return "vm_vminfotable_addDisk?faces-redirect=true";
    }

    public void geIDEController() {

        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        VirtualMachine vm = vmface.getVMInfo(vmName);
        List<Controller> conList = vm.getController();
        if (conList != null && conList.size() > 0) {
            int controllerCount = conList.size();
            SystemOutPrintln.print_vm("controllerCount=" + controllerCount);

            for (int i = 0; i < conList.size(); i++) {
                SystemOutPrintln.print_vm("conList.get(i).getControllerName()=" + conList.get(i).getControllerName());
                int controlerType = conList.get(i).getControllerType();  //"IDE"
                if (controlerType == Controller.CONTROLLER_TYPE_IDE) {
                    ideController = conList.get(i);
                }


            }
        }
    }

    public String createCD_byIDE(String name) {
        SystemOutPrintln.print_vm("add cd   vmName=" + name);
        vmName = name;
        //��ӹ��̲���
        this.geIDEController();
        BaseStorage baseStorageInfo = new BaseStorage();
        baseStorageInfo.setStorageName("emptydrive");
        baseStorageInfo.setStorageType(2);
        baseStorageInfo.setPhysicsDrive(false);
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean addCD = vmface.addStorage(vmName, ideController, baseStorageInfo);
        String returnStr = null;
        if (!addCD) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "addcd_fail"), ""));
            returnStr = null;
        } else {
            SystemOutPrintln.print_vm("add cd success");
            String param = "vmName=" + vmName;
            returnStr = "vm_vminfotab?faces-redirect=true" + param;
        }

        return returnStr;

//          
//          
//          
//         
//        String storageAttachMedium = "emptydrive";
//        boolean StorageAttachMediumIsExists = true;  //1  ??/
//        int storageAttachType = 2;  //�洢�豸����ȡֵ1ΪӲ�̣�2Ϊ����
//        long storageAttachSize = 0;
//        long StorageAttachUsedSize = 0;
//        boolean StorageAttachIsPhysical = true; //(int) 1 ???
//        String storageAttachFormat = "Image";  //�洢�豸��ʽ
//        int storageAttachPort = 0;  //�洢�豸�˿ںţ�IDEȡֵ0��1��SCSIȡֵ0��14��SASȡֵ0-7
//        int StorageAttachDevice = 0;  //�洢�豸�豸�ţ�IDE��������ȡֵ0��1��SAS��SCSIȡֵ0
//
//        BaseStorageInfo baseStorageInfo = new BaseStorageInfo();
//        baseStorageInfo.setStorageAttachMedium(storageAttachMedium);
//        baseStorageInfo.setStorageAttachMediumIsExists(StorageAttachMediumIsExists);
//        baseStorageInfo.setStorageAttachType(storageAttachType);
//        baseStorageInfo.setStorageAttachSize(storageAttachSize+"");
//        baseStorageInfo.setStorageAttachIsPhysical(StorageAttachIsPhysical);
//        baseStorageInfo.setStorageAttachUsedSize(StorageAttachUsedSize+"");
//        baseStorageInfo.setStorageAttachFormat(storageAttachFormat);                  
//        baseStorageInfo.setStorageAttachPort(storageAttachPort);
//        baseStorageInfo.setStorageAttachDevice(StorageAttachDevice);
//        baseStorageInfo.setStrstate(this.setResource("Storage_deviceState_1"));
//
//        FacesContext context = FacesContext.getCurrentInstance();  //session�򣬸���VMList����vmInfoListֵ
//        VMList share = (VMList) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{vMList}", VMList.class).getValue(context.getELContext());
//
//        selectVM = share.getSelectedVM();
//        if (selectVM != null) {
//            System.out.println("^^^^^^^^^^^^^^^^ѡ�е������=" + selectVM.getVmNAME());
//            controller = selectVM.getController();
//                for (int i = 0; i < controller.size(); i++) {
//                    int type = controller.get(i).getControllerType();
//                    if (type == 1) {  //������洢���������ͣ�1����ide��2����scsi��3����sas
//                        controller.get(i).getStorage().add(baseStorageInfo);
//                        System.out.println("IDE�������д洢�豸����=" + controller.get(i).getStorage().size());
//                        break;
//                    }
//                }
//
////            ideController = selectVM.getIdeController();
////            ideController.getStorage().add(baseStorageInfo);
//        }
//
//        return "vm_vminfotab?faces-redirect=true";

    }

//    public String setResource(String resource) {
//        return java.util.ResourceBundle.getBundle("com/marstor/msa/common/resources/messages").getString(resource);
//    }

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
    
}
