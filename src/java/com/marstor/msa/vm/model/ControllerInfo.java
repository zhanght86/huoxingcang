/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.bean.BaseStorage;
import com.marstor.msa.vbox.bean.Controller;
import com.marstor.msa.vbox.bean.VirtualMachine;
import com.marstor.msa.vbox.web.MsaVMInterface;
import com.marstor.msa.vm.managedbean.VMList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 * @introduction ��������ϸ��Ϣ��
 * @author Administrator
 */
@ManagedBean(name = "controllerInfo")
@ViewScoped
public class ControllerInfo implements Serializable{

    public boolean booldel = false;
    String storageAttachMediumStr;
    int storageAttachPortStr;
    int storageAttachDeviceStr;
    String vmName;
    BaseStorage bstorage;
    String controlerName;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_vminfotab";
   

    /**
     * Creates a new instance of ControllerInfo
     */
    public ControllerInfo() {
    }
    public static final int CONTROLLER_TYPE_IDE = 1;  //����������
    public static final int CONTROLLER_TYPE_SCSI = 2;
    public static final int CONTROLLER_TYPE_SAS = 3;
    public static final int CONTROLLER_CHIPSET_TYPE_LSILOGIC = 1;  //������оƬ����
    public static final int CONTROLLER_CHIPSET_TYPE_LSILOGICSAS = 2;
    public static final int CONTROLLER_CHIPSET_TYPE_BUSLOGIC = 3;
    public static final int CONTROLLER_CHIPSET_TYPE_PIIX3 = 4;
    public static final int CONTROLLER_CHIPSET_TYPE_PIIX4 = 5;
    public static final int CONTROLLER_CHIPSET_TYPE_ICH6 = 6;
    private String controllerName;//����������
    private int controllerChipset;//������оƬ��
    private int controllerType;//����������
    private int isAddControler; //������1�����̡�Ӳ��Ϊ0
    private List<BaseStorageInfo> storage = new ArrayList();//���صĴ洢��
    //������ĳ�������µĴ洢�豸���ɾ����
    public BaseStorageInfo selectStorage;
    VMInformation selectVM;
    public List<ControllerInfo> controller;//������
    public ControllerInfo ideController;
    public ControllerInfo scsiController;
    public ControllerInfo sasController;
    public String selectStorageName;

    public boolean isBooldel() {
        return booldel;
    }

    public void setBooldel(boolean booldel) {
        this.booldel = booldel;
    }

    public int getIsAddControler() {
        return isAddControler;
    }

    public void setIsAddControler(int isAddControler) {
        this.isAddControler = isAddControler;
    }

    public int getControllerChipset() {
        return controllerChipset;
    }

    public void setControllerChipset(int controllerChipset) {
        this.controllerChipset = controllerChipset;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public int getControllerType() {
        return controllerType;
    }

    public void setControllerType(int controllerType) {
        this.controllerType = controllerType;
    }

    public String getSelectStorageName() {
        return selectStorageName;
    }

    public void setSelectStorageName(String selectStorageName) {
        this.selectStorageName = selectStorageName;
    }

    public List<BaseStorageInfo> getStorage() {
        return storage;
    }

    public void setStorage(List<BaseStorageInfo> storage) {
        this.storage = storage;
    }

    public BaseStorageInfo getSelectStorage() {
        return selectStorage;
    }

    public void setSelectStorage(BaseStorageInfo selectStorage) {
        this.selectStorage = selectStorage;
    }

    public void deleteStorage_bySCSI() {
        SystemOutPrintln.print_vm("selectStorage.getStorageAttachMedium()=" + selectStorage.getStorageAttachMedium());
        selectStorage.getStorageAttachMedium();
//        FacesContext context = FacesContext.getCurrentInstance();  //session�򣬸���VMList����vmInfoListֵ
//        VMList share = (VMList) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{vMList}", VMList.class).getValue(context.getELContext());
//
//        selectVM = share.getSelectedVM();
//        if (selectVM != null) {
//            System.out.println("^^^^^^^^^^^^^^^^ѡ�е������=" + selectVM.getVmNAME());
//            controller = selectVM.getController();
//            for(int i=0;i<controller.size();i++){
//               int type = controller.get(i).getControllerType();
//               if(type == 2){  //������洢���������ͣ�1����ide��2����scsi��3����sas
//                   controller.get(i).getStorage().remove(selectStorage);
//                   System.out.println("SCSI�������д洢�豸����="+controller.get(i).getStorage().size());
//                   break;
//               }
//            }
//
////            scsiController = selectVM.getScsiController();
////            scsiController.getStorage().remove(selectStorage);     
//        }

    }

    public void deleteStorage_bySAS() {
        FacesContext context = FacesContext.getCurrentInstance();  //session�򣬸���VMList����vmInfoListֵ
        VMList share = (VMList) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{vMList}", VMList.class).getValue(context.getELContext());

        selectVM = share.getSelectedVM();
        if (selectVM != null) {
            SystemOutPrintln.print_vm("ѡ�е������=" + selectVM.getVmNAME());
            controller = selectVM.getController();
            for (int i = 0; i < controller.size(); i++) {
                int type = controller.get(i).getControllerType();
                if (type == 3) {  //������洢���������ͣ�1����ide��2����scsi��3����sas
                    controller.get(i).getStorage().remove(selectStorage);
                    SystemOutPrintln.print_vm("SAS�������д洢�豸����=" + controller.get(i).getStorage().size());
                    break;
                }
            }

//            sasController = selectVM.getSasController();
//            sasController.getStorage().remove(selectStorage);     
        }
    }

    //������Ƭ
    public String removeDisk(String vmname, int port, int device) {
        SystemOutPrintln.print_vm("vmname=" + vmname+",port="+port+",device="+device);
        String ctrlName = "IDE";
        BaseStorage baseStorage = new BaseStorage();
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        VirtualMachine vm = vmface.getVMInfo(vmname);
        List<Controller> conList = vm.getController();
        if (conList != null && conList.size() > 0) {
            int controllerCount = conList.size();
            SystemOutPrintln.print_vm("controllerCount=" + controllerCount);

            for (int i = 0; i < conList.size(); i++) {
                SystemOutPrintln.print_vm("conList.get(i).getControllerName()=" + conList.get(i).getControllerName());
                int conType = conList.get(i).getControllerType();  //"IDE"
                if (conType == Controller.CONTROLLER_TYPE_IDE) {
                    ctrlName = conList.get(i).getControllerName();
                    List<BaseStorage> bsList = conList.get(i).getStorage();
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
        baseStorage.setStorageName("emptydrive");
        boolean removeDisk = vmface.modifyISO(vmname, ctrlName, baseStorage);
        String returnStr = null;
        if (removeDisk){
            String param = "vmName=" + vmname;
            returnStr = "vm_vminfotab?faces-redirect=true" + param;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "removedisk_fail"), ""));
            returnStr = null;
        }
        return returnStr;
    }
    
    //ISO�ļ�
    public String isoMenu(String vmname, int port, int device) {
        
         String param = "vmName=" + vmname + "&" + "port=" + port + "&" + "device=" + device;
        SystemOutPrintln.print_vm("vmname=" + vmname+",port="+port+",device="+device);
       
            return "vm_vminfotab_selectISO?faces-redirect=true" + param;
    }
    
     //ѡ���������
    public String physicsMenu(String vmname, int port, int device) {
        String param = "vmName=" + vmname + "&" + "port=" + port + "&" + "device=" + device;
        SystemOutPrintln.print_vm("vmname=" + vmname + ",port=" + port + ",device=" + device);

        return "vm_vminfotab_selectDriver?faces-redirect=true" + param;

    }

    public void deleteStorage(String vmname, int type) {
        SystemOutPrintln.print_vm("type ="+type);

        SystemOutPrintln.print_vm("vmName=" + vmname);
        selectStorageName = selectStorage.getStorageName();
        vmName = vmname;
        storageAttachMediumStr = selectStorage.getStorageAttachMedium();
        storageAttachPortStr = selectStorage.getStorageAttachPort();
        storageAttachDeviceStr = selectStorage.getStorageAttachDevice();
        
        SystemOutPrintln.print_vm("storageAttachMediumStr=" + storageAttachMediumStr);
        int storageAttachTypeStr = selectStorage.getStorageAttachType();
        if (storageAttachTypeStr == BaseStorage.VM_DISK) {  //�洢�豸����ȡֵ1ΪӲ�̣�2Ϊ����
            if (type == 1) {
                 SystemOutPrintln.print_vm("deleteIDEStorage.show() ");
                RequestContext.getCurrentInstance().execute("deleteIDEStorage.show()");
                return;
            } else if (type == 2) {
                SystemOutPrintln.print_vm("deleteSCSIStorage.show() ");
                RequestContext.getCurrentInstance().execute("deleteSCSIStorage.show()");
                return;
            } else {
                SystemOutPrintln.print_vm("deleteSASStorage.show() ");
                RequestContext.getCurrentInstance().execute("deleteSASStorage.show()");
                return;
            }

        } else {
            RequestContext.getCurrentInstance().execute("deleteCDStorage.show()");
            return;

        }
    }

    public String deleteStorage_byIDE(boolean del) {
        SystemOutPrintln.print_vm("del=" + del);
        boolean delete = del;
        booldel = false;
        SystemOutPrintln.print_vm("selectStorage.getStorageAttachMedium()=" + selectStorage.getStorageAttachMedium());

        this.getDeleteStorage();
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean removeStorage = vmface.removeStorage(vmName, delete, controlerName, bstorage);
        String returnStr = null;
        if (removeStorage) {
            SystemOutPrintln.print_vm("del ide disk success");
            String param = "vmName=" + vmName;
            return "vm_vminfotab?faces-redirect=true" + param;
        } else {
            SystemOutPrintln.print_vm("del ide disk fail");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "removestorage_fail"), ""));
            returnStr = null;
        }
        return returnStr;




    }

    public String deleteCD_Storage_byIDE() {


        SystemOutPrintln.print_vm("selectStorage.getStorageAttachMedium()=" + selectStorage.getStorageAttachMedium());

        this.getDeleteStorage();
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        SystemOutPrintln.print_vm("vmName="+vmName+",false=false"+",controlerName="+controlerName+"=bstorage"+bstorage.getStorageName());
        boolean removeStorage = vmface.removeStorage(vmName, false, controlerName, bstorage);
        String returnStr = null;
        if (removeStorage) {
              SystemOutPrintln.print_vm("del ide cd success");
            String param = "vmName=" + vmName;
            return "vm_vminfotab?faces-redirect=true" + param;
        } else {
             SystemOutPrintln.print_vm("del ide cd fail");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "removestorage_fail"), ""));
            returnStr = null;
        }
        return returnStr;

    }

    public void getDeleteStorage() {

        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        VirtualMachine vm = vmface.getVMInfo(vmName);
        List<Controller> conList = vm.getController();
        if (conList != null && conList.size() > 0) {
            int controllerCount = conList.size();
            SystemOutPrintln.print_vm("controllerCount=" + controllerCount);
            boolean flag = false;
            for (int i = 0; i < conList.size(); i++) {
//                System.out.println("conList.get(i).getControllerName()=" + conList.get(i).getControllerName());
//                controlerName = conList.get(i).getControllerName();  //"IDE"
                if (flag) {
                    break;
                }
                List<BaseStorage> bsList = conList.get(i).getStorage();
                if (bsList != null && bsList.size() > 0) {
                    int StorageCount = bsList.size();

                    for (int j = 0; j < StorageCount; j++) {
                        if (storageAttachMediumStr.equalsIgnoreCase("--")) { //��Ϊ��emptydrive������Ƭ
                            int storageAttachPort = bsList.get(j).getPortNum();  //"disk1"
                            int storageAttachDevice = bsList.get(j).getDeviceNum();  //"disk1"
                            if (storageAttachPort == storageAttachPortStr && storageAttachDevice == storageAttachDeviceStr) {
                                bstorage = bsList.get(j);
                                SystemOutPrintln.print_vm("conList.get(i).getControllerName()=" + conList.get(i).getControllerName());
                                controlerName = conList.get(i).getControllerName();  //"IDE"
                                flag = true;
                                break;
                            }
                        } else {
                            String storageAttachMedium = bsList.get(j).getStorageName();  //"disk1"  
                            if (storageAttachMedium.equals(storageAttachMediumStr)) {
                                bstorage = bsList.get(j);
                                SystemOutPrintln.print_vm("conList.get(i).getControllerName()=" + conList.get(i).getControllerName());
                                controlerName = conList.get(i).getControllerName();  //"IDE"
                                flag = true;
                                break;
                            }
                        }

                    }

                }

            }
        }
    }

    public String setName(String vmname, String storageAttachMedium, int storageAttachType,int port,int device) {

        SystemOutPrintln.print_vm("vmname = " + vmname + ",storageAttachMedium=" + storageAttachMedium + ",storageAttachType=" + storageAttachType);
        String param = "vmName=" + vmname + "&" + "storageAttachMedium=" + storageAttachMedium+ "&" + "storageAttachPort=" + port+ "&" + "storageAttachDevice=" + device;

        String returnStr = "vm_vminfotable_modifyDisk?faces-redirect=true";
        if (storageAttachType == BaseStorage.VM_DISK) {  //�洢�豸����ȡֵ1ΪӲ�̣�2Ϊ����
            returnStr = "vm_vminfotable_modifyDisk?faces-redirect=true";
        } else {
            returnStr = "vm_vminfotable_modifyCD?faces-redirect=true";
        }

        return returnStr + param;


    }
}
