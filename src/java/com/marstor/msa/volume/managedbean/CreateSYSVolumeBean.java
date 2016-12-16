/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.managedbean;

import com.marstor.msa.common.bean.PhysicalDiskInformation;
import com.marstor.msa.common.bean.VolumeGroupInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyConstants;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.volume.model.Disk;
import com.marstor.msa.volume.model.DiskStates;
import com.marstor.msa.volume.model.VolumesInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "createSYSVolumeBean")
@ViewScoped
public class CreateSYSVolumeBean extends DiskStates  implements Serializable{

    public Disk diskBean;
    public List<Disk> diskList;
    private DiskModel diskModel;
    private Disk[] selectedDisks;
    public Disk selectedDisk;
    public String volumeName = "SYSVOL";
    public int raidType = 0;
    public String raidLable;
    private long usedDiskCapacity = 0L;
    public int PoolType = 0;
    public String sysType = 0 + "";
    public boolean cantuse = false;
    MSAResource resources = new MSAResource();
    public String waitTip = "";
    public String[] disknames = null;

    /**
     * Creates a new instance of createVolumeBean
     */
    public CreateSYSVolumeBean() {
        raidLable = resources.get("volume.volumegroup_createsysvol", "min_select");
        addList();
        diskModel = new DiskModel(diskList);
    }

    public void addList() {
        diskList = new ArrayList();
usedDiskCapacity = 0L;
        int temp = 0;
        VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
        PhysicalDiskInformation[] diskInformation = volume.getAllPhysicalDisk();
        for (int i = 0; i < diskInformation.length; i++) {


            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_UNUSED) {
                diskBean = new Disk();
                if (MyConstants.diskModel != PhysicalDiskInformation.DISK_MODEL_SPARE) {
                   String postion = diskInformation[i].cardModel == 2 ? (resources.get("volume.volumegroup_createsysvol", "extend") + diskInformation[i].cardIndex) : resources.get("volume.volumegroup_createsysvol", "host");
                   //String postion = diskInformation[i].cardModel == 0 ? "" : ((diskInformation[i].cardModel == 1 ? resources.get("volume.volumegroup_createsysvol", "sas")+" " : resources.get("volume.volumegroup_createsysvol", "extend")+" ") + diskInformation[i].cardIndex);
                   if(postion== null || postion.equals("")){
                       diskBean.setPosition("--");
                   }else{
                        diskBean.setPosition(postion);
                   }
                   String num = diskInformation[i].position;
                   if(num== null || num.equals("")){
                        diskBean.setNum("--");
                   }else{
                       diskBean.setNum(num);
                   }
                    diskBean.setSize(diskInformation[i].diskSize + "");
                    diskBean.setDiskName(diskInformation[i].diskName);
                    diskList.add(diskBean);

                } else {

                    diskBean.setSize(diskInformation[i].diskSize + "");
                    diskBean.setDiskName(diskInformation[i].diskName);
                    diskBean.setPosition("--");
                    diskBean.setNum("--");
                    diskList.add(diskBean);
                }

                temp++;
            } else {
                usedDiskCapacity += diskInformation[i].diskSize;
            }
        }


        //注册容量, 已用容量
//        LicenseInformation capacityLicense = node.serverNode.getXXModuleXXXFunctionLicense(Constants.MODEL_ID_BASIC, Constants.FUNCTIONID_BASIC_DISK_CAPACITY);
//        long licenseCapacity = 1024L * 1024L * 1024L * capacityLicense.getFunctionNumber() * 1024L;
//        long usedCapacity = 1024L * 1024L * 1024L * usedDiskCapacity;
//
//        String tishi = java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/CreatePoolDialog").getString("容量，剩余");
//        tishi = tishi.replaceFirst("%", MyUtility.sizeToString(licenseCapacity));
//        tishi = tishi.replaceFirst("%", MyUtility.sizeToString(licenseCapacity - usedCapacity));
//        jTable1.setToolTipText(tishi);

    }

    public Disk[] getSelectedDisks() {
        return selectedDisks;
    }

    public void setSelectedDisks(Disk[] selectedDisks) {
        this.selectedDisks = selectedDisks;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public int getRaidType() {
        return raidType;
    }

    public void setRaidType(int raidType) {
        this.raidType = raidType;
    }

    public String getRaidLable() {
        return raidLable;
    }

    public void setRaidLable(String raidLable) {
        this.raidLable = raidLable;
    }

    public List<Disk> getDiskList() {
        return diskList;
    }

    public void setDiskList(List<Disk> diskList) {
        this.diskList = diskList;
    }

    public DiskModel getDiskModel() {
        return diskModel;
    }

    public void setDiskModel(DiskModel diskModel) {
        this.diskModel = diskModel;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public boolean isCantuse() {
        return cantuse;
    }

    public void setCantuse(boolean cantuse) {
        this.cantuse = cantuse;
    }
    
    public void tip(){
         if (sysType.equals("0")) {
           waitTip = resources.get("volume.volumegroup_createsysvol", "waitcreat");
         }else{
              waitTip = resources.get("volume.volumegroup_createsysvol", "waitimport");
         }
          RequestContext.getCurrentInstance().execute("waitTip.show()");
    }

    public void handle(String groupName, String type) {
        String returnStr = null;
        if (sysType.equals("0")) {
        
        
/****/
// ????        Object capacityLicense = adaptee.serverNode.getXXModuleXXXFunctionLicense(Constants.MODEL_ID_BASIC, Constants.FUNCTIONID_BASIC_DISK_CAPACITY);
//        if (capacityLicense == null) {
//            Constants.showWarningMessage(MyUtility.getOtherString("没有注册总存储容量"));
//            return;
//        }

//  ****      DefaultTableModel tableModelTemp = (DefaultTableModel) jTable1.getModel();
//        ArrayList<String> selectedDisk = new ArrayList();
////        long selectedSize = 0;
//        for (int i = 0; i < jTable1.getRowCount(); i++) {
//            if (PhysicalDiskInformation.DISK_MODEL != PhysicalDiskInformation.DISK_MODEL_SPARE) {
//                boolean selected = (Boolean) tableModelTemp.getValueAt(i, 0);
//                if (selected) {
//                    selectedDisk.add((String) tableModelTemp.getValueAt(i, 4));
//                    selectedSize += (Long) tableModelTemp.getValueAt(i, 3);
//                }
//            } else {
//                boolean selected = (Boolean) tableModelTemp.getValueAt(i, 0);
//                if (selected) {
//                    selectedDisk.add((String) tableModelTemp.getValueAt(i, 2));
//                    selectedSize += (Long) tableModelTemp.getValueAt(i, 1);
//                }
//            }
//   ***     }

//        FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
//        CreateSYSVolumeBean share = (CreateSYSVolumeBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{createSYSVolumeBean}", CreateSYSVolumeBean.class).getValue(context.getELContext());
/****/         
            
            
            
            Disk[] selDisks = this.getSelectedDisks();
            SystemOutPrintln.print_volume("this.getSelectedDisks().length=" + this.getSelectedDisks().length);
            long selectedSize = 0;
            List<Disk> diskLists = new ArrayList();
            Disk disk;
            disknames = null;
            if (selDisks.length > 0) {
                disknames = new String[selDisks.length];
            }

/****/

//        LicenseInformation licenseInfo = node.serverNode.getXXModuleXXXFunctionLicense(Constants.MODEL_ID_BASIC, Constants.FUNCTIONID_BASIC_DISK_CAPACITY);
//        if ((licenseInfo.getFunctionNumber() * 1024L) < (usedDiskCapacity + selectedSize)) {
//            Constants.showErrorMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/CreatePoolDialog").getString("磁盘容量超过注册最大限制"));
//            return;
//        }


//????        VolumeInterface volumeInt = InterfaceFactory.getVolumeInterfaceInstance();
//        VolumeGroupInformation[] pools = volumeInt.getAllVolumeGroup();
//        if (pools != null && pools.length > 0) {
//            for (int k = 0; k < pools.length; k++) {
//                if (groupName.trim().equalsIgnoreCase(pools[k].getName())) {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "卷组名已存在。", ""));
//                    return null;
//                }
//            }
//        }
/****/            
            
            
            


            if (selDisks.length < 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "no_selectdisk"), ""));
                return;
            }

            for (int i = 0; i < selDisks.length; i++) {
                selectedSize += Long.valueOf(selDisks[i].getSize());
                disknames[i] = selDisks[i].getDiskName();
                disk = new Disk();
                disk.setPosition(selDisks[i].getPosition());
                disk.setNum(selDisks[i].getNum());
                disk.setSize(selDisks[i].getSize());
                disk.setType(resources.get("volume.volumegroup_createsysvol", "datadisk"));
                disk.setState(resources.get("volume.volumegroup_createsysvol", "online"));
                disk.setDiskName(selDisks[i].getDiskName());
                if (type.equals("4")) {
                    disk.setNotReplace(true);
                } else {
                    disk.setNotReplace(false);
                }
                disk.setNotRemove(true);
                if (disk.getState().equals(resources.get("volume.volumegroup_createsysvol", "damage"))) {
                    disk.setNotApart(false);
                } else {
                    disk.setNotApart(true);
                }
                diskLists.add(disk);
            }
        //判断选中的磁盘的总容量是否大于注册码文件中允许的最大容量，如果大于则显示错误信息
        CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
        Reg[] license = commonInterfaceInstance.getLicense();
        int maxSize = 0;
        String unitResourceID = "";
        boolean flag = false;
        for (int i = 0; i < license.length; i++) {
            Reg reg = license[i];
            if (reg.getModuleID() == Module.MODULE_BASIC && reg.getFunctionID() == Module.FUNCTIONID_BASIC_DISK_CAPACITY) {
                maxSize = reg.getFunctionNumber() ;
                unitResourceID = String.valueOf(reg.getUnitResourceID());
                flag = true;
                break;
            }

        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "getTotalDiskSizeFailed"), ""));
            return;
        }
        if (usedDiskCapacity+selectedSize > maxSize*1024) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "totalDiskSize_limit") + maxSize + "TB", ""));
            return;
        }
        
            VolumesInfo volume = new VolumesInfo();
            volume.setName(groupName);
            volume.setUsedSize("5GB");
            volume.setUnusedSize("1.22TB");
            String typeStr = "RAID5";
            if (type.equals("0")) {
                typeStr = "RAID5";
                PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID5;
            } else if (type.equals("1")) {
                typeStr = "RAID6";
                PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID6;
            } else if (type.equals("2")) {
                typeStr = "RAID5+0";
                PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID5_0;
            } else if (type.equals("3")) {
                typeStr = "RAID6+0";
                PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID6_0;
            } else if (type.equals("4")) {
                typeStr = "RAID0";
                PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID0;
            } else if (type.equals("5")) {
                typeStr = "RAID1";
                PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID1;
            } else if (type.equals("6")) {
                typeStr = resources.get("volume.volumegroup_createsysvol", "raidz3");
                PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAIDz3;
            }
            volume.setRaidType(typeStr);
            volume.setState(resources.get("volume.volumegroup_createsysvol", "online"));
            volume.setDiskList(diskLists);



            switch (PoolType) {
                case 1:
                    if (selDisks.length < 3) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "nocountdisks"), ""));
                        return;
                    }
                    break;
                case 2:
                    if (selDisks.length < 4) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "nocountdisks"), ""));
                        return;
                    }
                    break;
                case 3:
                    if (selDisks.length < 6) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "nocountdisks"), ""));
                        return;
                    }
                    if (selDisks.length % 2 != 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,resources.get("volume.volumegroup_createsysvol", "evendisks") , ""));
                        return;
                    }
                    break;
                case 4:
                    if (selDisks.length < 1) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "nocountdisks"), ""));
                        return;
                    }
                    break;
                case 5:
                    if (selDisks.length < 8) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "nocountdisks"), ""));
                        return;
                    }
                    break;
                case 6:
                    if (selDisks.length < 6) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "nocountdisks"), ""));
                        return;
                    }
                    break;
                case 7:
                    if (selDisks.length != 2) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "onlytwodisks"), ""));
                        return;
                    }
                    break;
                default:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "nocountdisks"), ""));
                    break;
            }


//            VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
//            SystemOutPrintln.print_volume("PoolType=" + PoolType + ",groupName=" + groupName);
//            for (int k = 0; k < disknames.length; k++) {
//                SystemOutPrintln.print_volume("disknames[k]=" + disknames[k]);
//
//            }
//            boolean creat = volumeIn.createVolumeGroup(PoolType, groupName, disknames);
//            if (creat == false) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "creatsysdiskfail"), ""));
//                return null;
//
//            } else {
////            CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
////
////            boolean reboot = common.reboot();
////            if (!reboot) {
////                System.out.println("reboot fail");
////                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "重启火星舱失败。", ""));
////                return null;
////            }
//
//
////                ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
////                HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
////                request.getSession().invalidate();
////                System.out.println(request.getSession().getAttribute("user"));
////                request.getSession().removeAttribute("user");
////                System.out.println(request.getSession().getAttribute("user"));
////                CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
////                boolean reboot = common.reboot();
//
//                RequestContext.getCurrentInstance().execute("window.top.location.href='reboot.xhtml'");
////            returnStr = "reboot?faces-redirect=true";
//                //创建系统卷后重启火星舱
//            }
//
//            SystemOutPrintln.print_volume(")returnStr=" + returnStr);
        } 
//        else {
//            VolumeInterface volumeI = InterfaceFactory.getVolumeInterfaceInstance();
//            boolean importbool = volumeI.importSysvol();
//            if (importbool == false) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "importsysdiskfail"), ""));
//                return null;
//
//            }else{
////                ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
////                HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
////                request.getSession().invalidate();
////                System.out.println(request.getSession().getAttribute("user"));
////                request.getSession().removeAttribute("user");
////                System.out.println(request.getSession().getAttribute("user"));
////                CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
////                boolean reboot = common.reboot();
//                RequestContext.getCurrentInstance().execute("window.top.location.href='reboot.xhtml'");
//                //创建系统卷后重启火星舱
//            }

//        }
       this.tip();
    }
    
    private boolean entred = false;

    public String handle_end(String groupName, String type) {
        if(entred){
            return null;
        }else{
            entred = true;
        }
//        RequestContext.getCurrentInstance().execute("PF('block').block()");
        String returnStr = null;
        if (sysType.equals("0")) {
            VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
            SystemOutPrintln.print_volume("PoolType=" + PoolType + ",groupName=" + groupName);
            for (int k = 0; k < disknames.length; k++) {
                SystemOutPrintln.print_volume("disknames[k]=" + disknames[k]);
            }
            boolean creat = volumeIn.createVolumeGroup(PoolType, groupName, disknames);
//            RequestContext.getCurrentInstance().execute("PF('block').unblock()");
            if (creat == false) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "creatsysdiskfail"), ""));
                return null;

            } else {
                RequestContext.getCurrentInstance().execute("window.top.location.href='reboot.xhtml?init=0'");
                //创建系统卷后重启火星舱
            }

            SystemOutPrintln.print_volume("returnStr=" + returnStr);
        } else {
            VolumeInterface volumeI = InterfaceFactory.getVolumeInterfaceInstance();
            boolean importbool = volumeI.importSysvol();
//            RequestContext.getCurrentInstance().execute("PF('block').unblock()");
            if (importbool == false) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "importsysdiskfail"), ""));
                return null;
            }else{
                RequestContext.getCurrentInstance().execute("window.top.location.href='reboot.xhtml?init=0'");
                //创建系统卷后重启火星舱
            }
        }
        return returnStr;
    }
    
    public void changeBooleanCheckbox() {
        if (sysType.equals("0")) {
            cantuse = false;
        } else {
            cantuse = true;
        }

    }
    
       public void refresh() {
        VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
        boolean ref = volume.refreshCache();
        if (ref == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createsysvol", "refreshcache_fail"), ""));
            return;
        } else {
            addList();
            diskModel = new DiskModel(diskList);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, resources.get("volume.volumegroup_createsysvol", "refreshcache_ok"), ""));

        }
    }

    public String getWaitTip() {
        return waitTip;
    }

    public void setWaitTip(String waitTip) {
        this.waitTip = waitTip;
    }
       
       
}
