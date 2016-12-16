/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
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
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.primefaces.context.RequestContext;


/**
 * @introduction 获得单个虚拟机信息类
 * @author Administrator
 */
@ManagedBean(name = "vMInformation")
@ViewScoped
public class VMInformation implements Serializable{

    /**
     * Creates a new instance of VMInformation
     */
    
    public static final int DVD= 1;  //启动顺序，光盘
    public static final int DISK = 2;  //启动顺序，硬盘
    public static final int NET = 3;  //启动顺序，网络
    public static final int VM_RUN = 1;
    public static final int VM_PAUSE = 2;
    public static final int VM_STOP = 3;

    private String vmNAME;
    private String vmUUID;//虚拟机UUID
    private String vmBaseFolderPath;//虚拟机路径
    private int boot1;  //启动顺序，数值型，1：光盘、2：硬盘、3：网络
    private int boot2;  //启动顺序，数值型，1：光盘、2：硬盘、3：网络
    private int boot3;  //启动顺序，数值型，1：光盘、2：硬盘、3：网络
    private String osType;  //虚拟机操作系统类型
    private int cpuCoreNum;  //CPU核心数
    private int memorySize;  //内存大小
    boolean supportIOAPIC;  //是否开启IOACPI
    boolean supportEFI;  //是否开启EFI
    boolean supportUTCTime;  //是否开启UTC时钟
    String remoteAddress;//远程登录ip
    int remotePor;//远程登录端口号
    boolean RemoteIPAddressUsable;//此远程登录ip是否存在
    int remoteTimeOut;//超时时间
    String remoteAuthentication;//认证方式
    boolean remoteMultipleConnections;//是否允许远程连接多连接
    private List<ControllerInfo> controller = new ArrayList();//控制器
    private ControllerInfo ideController = new ControllerInfo();
    private ControllerInfo scsiController = new ControllerInfo();
    private ControllerInfo sasController = new ControllerInfo();
    private List<NetworkInterfaceCardInfo> networkInterfaceCard = new ArrayList();//网卡
    private NetworkInterfaceCardInfo selectNetworkCard;  //选中的网卡
    
    public List<ControllerType> controllerTypeList = new ArrayList(); //控制器类型
    private ControllerType selectedControllerType;
    
    private List<BootSequen> bootSequences;  //启动顺序
    
    private int vmState;  //虚拟机状态
    private String state;  //虚拟机状态
    
    public String m_nicName;
    public boolean m_isCableConnected = false;
    public String m_textMac;
    public String btnMac = "080027E31B6C";
    
    private HtmlCommandButton addCD_IDE;
    private HtmlCommandButton addDisk_IDE;
    private HtmlCommandButton addDisk_SCSI;
    private HtmlCommandButton addDisk_SAS;
    
    private boolean notaddCD_IDE;
    private boolean notaddDisk_IDE;
    private boolean notaddDisk_SCSI;
    private boolean notaddDisk_SAS;
    private boolean notaddNet;
    private int isHave;  //是否可编辑控制器
    String vmname;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_vminfotab";
    public String selectNetworkCardName;
    public String selectedControllerName;

     
    
    private HtmlCommandButton editController;

    public VMInformation() {
       
    }

    public boolean isRemoteIPAddressUsable() {
        return RemoteIPAddressUsable;
    }

    public void setRemoteIPAddressUsable(boolean RemoteIPAddressUsable) {
        this.RemoteIPAddressUsable = RemoteIPAddressUsable;
    }
   

    public int getIsHave() {
        return isHave;
    }

    public void setIsHave(int isHave) {
        this.isHave = isHave;
    }

    
    public List<ControllerInfo> getController() {
        return controller;
    }

    public void setController(List<ControllerInfo> controller) {
        this.controller = controller;
    }

    public List<NetworkInterfaceCardInfo> getNetworkInterfaceCard() {
        return networkInterfaceCard;
    }

    public void setNetworkInterfaceCard(List<NetworkInterfaceCardInfo> networkInterfaceCard) {
        this.networkInterfaceCard = networkInterfaceCard;
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

    public int getCpuCoreNum() {
        return cpuCoreNum;
    }

    public void setCpuCoreNum(int cpuCoreNum) {
        this.cpuCoreNum = cpuCoreNum;
    }

    public String getSelectedControllerName() {
        return selectedControllerName;
    }

    public void setSelectedControllerName(String selectedControllerName) {
        this.selectedControllerName = selectedControllerName;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getRemoteAuthentication() {
        return remoteAuthentication;
    }

    public void setRemoteAuthentication(String remoteAuthentication) {
        this.remoteAuthentication = remoteAuthentication;
    }

    public boolean isRemoteMultipleConnections() {
        return remoteMultipleConnections;
    }

    public void setRemoteMultipleConnections(boolean remoteMultipleConnections) {
        this.remoteMultipleConnections = remoteMultipleConnections;
    }

    public int getRemotePor() {
        return remotePor;
    }

    public void setRemotePor(int remotePor) {
        this.remotePor = remotePor;
    }

    public int getRemoteTimeOut() {
        return remoteTimeOut;
    }

    public void setRemoteTimeOut(int remoteTimeOut) {
        this.remoteTimeOut = remoteTimeOut;
    }

    public boolean isSupportEFI() {
        return supportEFI;
    }

    public void setSupportEFI(boolean supportEFI) {
        this.supportEFI = supportEFI;
    }

    public boolean isSupportIOAPIC() {
        return supportIOAPIC;
    }

    public void setSupportIOAPIC(boolean supportIOAPIC) {
        this.supportIOAPIC = supportIOAPIC;
    }

    public boolean isSupportUTCTime() {
        return supportUTCTime;
    }

    public void setSupportUTCTime(boolean supportUTCTime) {
        this.supportUTCTime = supportUTCTime;
    }

    public String getVmBaseFolderPath() {
        return vmBaseFolderPath;
    }

    public void setVmBaseFolderPath(String vmBaseFolderPath) {
        this.vmBaseFolderPath = vmBaseFolderPath;
    }

    public String getVmNAME() {
        return vmNAME;
    }

    public void setVmNAME(String vmNAME) {
        this.vmNAME = vmNAME;
    }

    public String getVmUUID() {
        return vmUUID;
    }

    public void setVmUUID(String vmUUID) {
        this.vmUUID = vmUUID;
    }

    public int getVmState() {
        return vmState;
    }

    public void setVmState(int vmState) {
        this.vmState = vmState;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ControllerInfo getIdeController() {
        return ideController;
    }

    public void setIdeController(ControllerInfo ideController) {
        this.ideController = ideController;
    }

    public ControllerInfo getScsiController() {
        return scsiController;
    }

    public void setScsiController(ControllerInfo scsiController) {
        this.scsiController = scsiController;
    }

    public ControllerInfo getSasController() {
        return sasController;
    }

    public void setSasController(ControllerInfo sasController) {
        this.sasController = sasController;
    }

    public List<ControllerType> getControllerTypeList() {
        return controllerTypeList;
    }

    public void setControllerTypeList(List<ControllerType> controllerTypeList) {
        this.controllerTypeList = controllerTypeList;
    }

    public List<BootSequen> getBootSequences() {
        return bootSequences;
    }

    public void setBootSequences(List<BootSequen> bootSequences) {
        this.bootSequences = bootSequences;
    }
    
    public NetworkInterfaceCardInfo getSelectNetworkCard() {
        return selectNetworkCard;
    }

    public void setSelectNetworkCard(NetworkInterfaceCardInfo selectNetworkCard) {
        this.selectNetworkCard = selectNetworkCard;
    }

    public String getSelectNetworkCardName() {
        return selectNetworkCardName;
    }

    public void setSelectNetworkCardName(String selectNetworkCardName) {
        this.selectNetworkCardName = selectNetworkCardName;
    }
    
    public void toDeleteNetworkCard() {
        selectNetworkCardName = selectNetworkCard.getVmNICName();
         RequestContext.getCurrentInstance().execute("deleteNet.show()");
        
    }
    public String deleteNetworkCard() {
        String vmName = vmNAME;
        int nicNum = selectNetworkCard.getVmNICNum();
        SystemOutPrintln.print_vm("vmName=" + vmName + ",nicNum=" + nicNum);
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        boolean removeNic = vmface.removeNIC(vmNAME, nicNum);
        String returnStr = null;
        if (!removeNic) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "removenic_fail"), ""));
            returnStr = null;
        }else{
            String param = "vmName=" + vmName;
            returnStr = "vm_vminfotab?faces-redirect=true&amp;" + param + "&amp;accordionActive1=1";
          
        }
        return returnStr;
       
        //        String nicName= selectNetworkCard.getBindPhysicsNicIpAddress();
//        boolean nicCableConnected = selectNetworkCard.isBoolCableConnected();
//        String mac = selectNetworkCard.getMacAddress();
//        networkInterfaceCard.remove(selectNetworkCard);
    }

    public ControllerType getSelectedControllerType() {
        return selectedControllerType;
    }

    public void setSelectedControllerType(ControllerType selectedControllerType) {
        this.selectedControllerType = selectedControllerType;
    }

    public HtmlCommandButton getAddCD_IDE() {
        return addCD_IDE;
    }

    public void setAddCD_IDE(HtmlCommandButton addCD_IDE) {
        this.addCD_IDE = addCD_IDE;
    }

    public HtmlCommandButton getAddDisk_IDE() {
        return addDisk_IDE;
    }

    public void setAddDisk_IDE(HtmlCommandButton addDisk_IDE) {
        this.addDisk_IDE = addDisk_IDE;
    }

    public HtmlCommandButton getAddDisk_SCSI() {
        return addDisk_SCSI;
    }

    public void setAddDisk_SCSI(HtmlCommandButton addDisk_SCSI) {
        this.addDisk_SCSI = addDisk_SCSI;
    }

    public HtmlCommandButton getAddDisk_SAS() {
        return addDisk_SAS;
    }

    public void setAddDisk_SAS(HtmlCommandButton addDisk_SAS) {
        this.addDisk_SAS = addDisk_SAS;
    }

    public HtmlCommandButton getEditController() {
        return editController;
    }

    public void setEditController(HtmlCommandButton editController) {
        this.editController = editController;
    }
    
    public void isDisabledController() {  //控制器是否可用
       int isHave = selectedControllerType.getIsHave();
        
        if(isHave==1){  //控制器已存在，删除控制器
            this.addCD_IDE.setDisabled(true);
            this.addDisk_IDE.setDisabled(true);
//            this.editController.setDisabled(true);
        }
    }
    
    public String handleControll(String state,String vmname) {  //添加、删除控制器
        SystemOutPrintln.print_vm("state="+state+",vmname="+vmname);
        this.vmname = vmname;
        String returnStr = null;
        if(state.equalsIgnoreCase(res.get(basename, "add"))){
            returnStr = handleController_add();
            
        }else{
            selectedControllerName = "IDE";
            int type =selectedControllerType.getControllerType();
            if(type == Controller.CONTROLLER_TYPE_IDE){
                selectedControllerName = "IDE";
            }else if(type == Controller.CONTROLLER_TYPE_SCSI){
                selectedControllerName = "SCSI";
            }else{
                selectedControllerName = "SAS";
            }
            
            RequestContext.getCurrentInstance().execute("deleteController.show()");
            return null;
        }
        return returnStr;
        
    }
      
    public String handleController_add() {  //添加控制器
        String vmName = this.vmname;
        int type = selectedControllerType.getControllerType();
        String returnStr = null;
        if (type == Controller.CONTROLLER_TYPE_IDE) {

            String controllerName = "IDE Controller";
            Controller controllerInfo = new Controller();
            controllerInfo.setControllerName(controllerName);
            controllerInfo.setControllerType(1);
            controllerInfo.setControllerChipset(5);
            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            boolean addIDE = vmface.addController(vmName, controllerInfo);

            if (!addIDE) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "addide_fail"), ""));
                return null;
            } else {
                String param = "vmName=" + vmName;
                 SystemOutPrintln.print_vm("add ide success");
                return "vm_vminfotab?faces-redirect=true" + param;
               
            }
        } else if (type == Controller.CONTROLLER_TYPE_SCSI) {

            String controllerName = "SCSI Controller";
            Controller controllerInfo = new Controller();
            controllerInfo.setControllerName(controllerName);
            controllerInfo.setControllerType(2);
            controllerInfo.setControllerChipset(1);
            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            boolean addSCSI = vmface.addController(vmName, controllerInfo);
            if (!addSCSI) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "addscsi_fail"), ""));
                return null;
            } else {
                String param = "vmName=" + vmName;
                 SystemOutPrintln.print_vm("add scsi success");
                return "vm_vminfotab?faces-redirect=true" + param;   
            }

        } else if (type == Controller.CONTROLLER_TYPE_SAS) {
            String controllerName = "SAS Controller";
            Controller controllerInfo = new Controller();
            controllerInfo.setControllerName(controllerName);
            controllerInfo.setControllerType(3);
            controllerInfo.setControllerChipset(2);
            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            boolean addSAS = vmface.addController(vmName, controllerInfo);
            if (!addSAS) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "addsas_fail"), ""));
                return null;
            } else {
                String param = "vmName=" + vmName;
                 SystemOutPrintln.print_vm("add sas success ="+vmName);
               
                return "vm_vminfotab?faces-redirect=true" + param;
                
            }
        }

        return returnStr;
    }

    public String handleController_del(String vmname) {  //删除控制器
        String vmName= vmname;
        int type =selectedControllerType.getControllerType();
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        VirtualMachine vm = vmface.getVMInfo(vmName);
        List<Controller> conList = vm.getController();
        String controName = "IDE";
        if (conList != null && conList.size() > 0) {
            int controllerCount = conList.size();
            SystemOutPrintln.print_vm("controllerCount=" + controllerCount);
            
             for (int i = 0; i < conList.size(); i++) {
                 SystemOutPrintln.print_vm("conList.get(i).getControllerName()=" + conList.get(i).getControllerName());
                int  controlerType = conList.get(i).getControllerType();  //"IDE"
                 if(controlerType == type){
                  controName = conList.get(i).getControllerName();
                  break;
                 }
                

             }
         }
       SystemOutPrintln.print_vm("vmName= "+vmName+",controName="+controName);
       boolean removeCon = vmface.removeController(vmName,controName);
       String returnStr = null;
       if(removeCon){
           System.out.println("delete controller success ");
           String  param = "vmName=" + vmName  ;
        returnStr ="vm_vminfotab?faces-redirect=true" + param ;
       }else{
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "removecon_fail"), ""));
           returnStr = null;
       }
        return returnStr;
//        selectedControllerType.getControllerName();
//        
//        int isHave = selectedControllerType.getIsHave();
//        System.out.println("############isHave="+isHave);
//        if(isHave==1 && type ==1){  //删除控制器
//            ideController.setStorage(null);
//            System.out.println("############删除");
//            this.addCD_IDE.setDisabled(true);
//            this.addDisk_IDE.setDisabled(true);
//             selectedControllerType.setEdit(true);
////            this.editController.setDisabled(true);
//        }
//        if(isHave==1 && type ==2){
//            scsiController.setStorage(null);
//            this.addDisk_SCSI.setDisabled(true);
//              selectedControllerType.setEdit(true);
////            this.editController.setDisabled(true);
//        }
//        if(isHave==1 && type ==3){
//            sasController.setStorage(null);
//            this.addDisk_SAS.setDisabled(true);
//              selectedControllerType.setEdit(true);
////            this.editController.setDisabled(true);
//        }
//         
//        if(isHave==0 && type ==1){  //添加控制器
//             System.out.println("############添加");
////            ideController = new ControllerInfo();
//            List<BaseStorageInfo> storage = new ArrayList();
//            ideController.setStorage(storage);
//            this.addCD_IDE.setDisabled(false);
//            this.addDisk_IDE.setDisabled(false);
//            selectedControllerType.setEdit(false);
////            this.editController.setDisabled(false);
//            
//        }
//        if(isHave==0 && type ==2){
////            scsiController = new ControllerInfo();
//            List<BaseStorageInfo> storage = new ArrayList();
//            scsiController.setStorage(storage);
//            this.addDisk_SCSI.setDisabled(false);
//            selectedControllerType.setEdit(false);
////            this.editController.setDisabled(false);
//        }
//        if(isHave==0 && type ==3){
////            sasController = new ControllerInfo();
//            List<BaseStorageInfo> storage = new ArrayList();
//            sasController.setStorage(storage);
//            this.addDisk_SAS.setDisabled(false);
//            selectedControllerType.setEdit(false);
////            this.editController.setDisabled(false);
//        }
//       
//        if(isHave == 0){
//             selectedControllerType.setIsHave(1);
//             selectedControllerType.setOperation(setResource("controller_" + selectedControllerType.getIsHave()));
//        }else{
//             selectedControllerType.setIsHave(0);
//             selectedControllerType.setOperation(setResource("controller_" + selectedControllerType.getIsHave()));
//        }

    }
    
    public String isCanEditController(){
//        isHave = 0;
//        isHave = selectedControllerType.getIsHave();
//        int conType = selectedControllerType.getControllerType();
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.addCallbackParam("isEdit", isHave);
//        context.addCallbackParam("conType", conType);
        
        String vmName = vmNAME;
        int controType = selectedControllerType.getControllerType();
        int controChipset=selectedControllerType.getControllerChipset();
        SystemOutPrintln.print_vm("vmName="+vmName+",controType="+controType+",controChipset="+controChipset);
        String  param = "vmName=" + vmName + "&" + "controType=" + controType+ "&" + "controChipset=" + controChipset ;

        return "vm_vminfotable_modifyController?faces-redirect=true" + param ;
        
 
    }

     public String toMdifyNIC(){
        String vmName = vmNAME;
        int nicMun = selectNetworkCard.getVmNICNum();
        String macAddress=selectNetworkCard.getMacAddress();
        boolean cableConnected = selectNetworkCard.isBoolCableConnected();
        String bindPhysicsNIC = selectNetworkCard.getBindPhysicsNIC();
        SystemOutPrintln.print_vm("vmName="+vmName+",nicMun="+nicMun+",macAddress="+macAddress+",cableConnected="+cableConnected);
        String  param = "vmName=" + vmName + "&" + "nicMun=" + nicMun+ "&" + "macAddress=" + macAddress+ "&" + "cableConnected=" + cableConnected+ "&" + "bindPhysicsNIC=" + bindPhysicsNIC ;
        return "vm_vminfotable_modifyNic?faces-redirect=true" + param ;
        
 
    }
     
      public String toAddNIC(){
        String vmName = vmNAME;
        
        SystemOutPrintln.print_vm("vmName="+vmName);
        String  param = "vmName=" + vmName  ;
        return "vm_vminfotable_addnic?faces-redirect=true" + param ;
        
 
    }
    
//    public String setResource(String resource) {
//        return java.util.ResourceBundle.getBundle("com/marstor/msa/common/resources/messages").getString(resource);
//    }

    public String getM_nicName() {
        return m_nicName;
    }

    public void setM_nicName(String m_nicName) {
        this.m_nicName = m_nicName;
    }

    public boolean isM_isCableConnected() {
        return m_isCableConnected;
    }

    public void setM_isCableConnected(boolean m_isCableConnected) {
        this.m_isCableConnected = m_isCableConnected;
    }

    public String getM_textMac() {
        return m_textMac;
    }

    public void setM_textMac(String m_textMac) {
        this.m_textMac = m_textMac;
    }

    public boolean isNotaddCD_IDE() {
        return notaddCD_IDE;
    }

    public void setNotaddCD_IDE(boolean notaddCD_IDE) {
        this.notaddCD_IDE = notaddCD_IDE;
    }

    public boolean isNotaddDisk_IDE() {
        return notaddDisk_IDE;
    }

    public void setNotaddDisk_IDE(boolean notaddDisk_IDE) {
        this.notaddDisk_IDE = notaddDisk_IDE;
    }

    public boolean isNotaddDisk_SCSI() {
        return notaddDisk_SCSI;
    }

    public void setNotaddDisk_SCSI(boolean notaddDisk_SCSI) {
        this.notaddDisk_SCSI = notaddDisk_SCSI;
    }

    public boolean isNotaddDisk_SAS() {
        return notaddDisk_SAS;
    }

    public void setNotaddDisk_SAS(boolean notaddDisk_SAS) {
        this.notaddDisk_SAS = notaddDisk_SAS;
    }
    
    public void modifyNetworkCard() {

        m_nicName = selectNetworkCard.getVmNICName();
        m_textMac = selectNetworkCard.getMacAddress();
        int isCable = selectNetworkCard.getCableConnected();
        if( isCable== 1){
            m_isCableConnected = true;
        }else{
            m_isCableConnected = false;
        }
        
        SystemOutPrintln.print_vm("m_nicName="+m_nicName);

    }
    
    public void handleMACRefresh() {
        m_textMac = btnMac;
    }

    public boolean isNotaddNet() {
        return notaddNet;
    }

    public void setNotaddNet(boolean notaddNet) {
        this.notaddNet = notaddNet;
    }

    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String setControType(String name, int conType){
        String vmName = name;
        int controType = conType;
        SystemOutPrintln.print_vm("vmName="+name+",controType="+conType);
         String param = "vmName=" + vmName + "&" + "controType=" + controType ;

         return "vm_vminfotable_addDisk?faces-redirect=true"+param;
    }
    
    
}
