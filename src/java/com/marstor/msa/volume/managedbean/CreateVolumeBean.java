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
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "createVolumeBean")
@ViewScoped
public class CreateVolumeBean extends DiskStates  implements Serializable{

    public Disk diskBean;
    public List<Disk> diskList;
    private DiskModel diskModel;
    private Disk[] selectedDisks;
    public Disk selectedDisk;
    public String volumeName;
    public int raidType = 0;
    public String raidLable;
    private long usedDiskCapacity = 0L;
    public int PoolType = 0;
    MSAResource resources = new MSAResource();

    /**
     * Creates a new instance of createVolumeBean
     */
    public CreateVolumeBean() {
        raidLable = resources.get("volume.volumegroup_createvolume", "min_select");
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
                    String postion = diskInformation[i].cardModel == 2 ? (resources.get("volume.volumegroup_createvolume", "extend") + diskInformation[i].cardIndex) : resources.get("volume.volumegroup_createvolume", "host");
                    //String postion = diskInformation[i].cardModel == 0 ? "" : ((diskInformation[i].cardModel == 1 ? resources.get("volume.volumegroup_createvolume", "sas") : resources.get("volume.volumegroup_createvolume", "extend")) + diskInformation[i].cardIndex);
                    if (postion == null || postion.equals("")) {
                        diskBean.setPosition("--");
                    } else {
                        diskBean.setPosition(postion);
                    }
                    String num = diskInformation[i].position;
                    if (num == null || num.equals("")) {
                        diskBean.setNum("--");
                    } else {
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



//        FacesContext context = FacesContext.getCurrentInstance(); 
//        UnusedDiskBean undisk = (UnusedDiskBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{unusedDiskBean}", UnusedDiskBean.class).getValue(context.getELContext());
//        List<Disk> unDiskList = undisk.getDiskList();
//        for (int i = 0; i < unDiskList.size(); i++) {
//            Disk disk = unDiskList.get(i);
//            if (disk.getState().equals(DISK_MODEL_UNUSED+"")) {
//                diskBean = new Disk();
//                diskBean.setPosition(disk.getPosition());
//                diskBean.setNum(disk.getNum());
//                diskBean.setSize(disk.getSize());
//                diskBean.setDiskName(disk.getDiskName());
//                diskList.add(diskBean);
//            }
//        }

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

    public String handle(String groupName, String type) {
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
//        CreateVolumeBean share = (CreateVolumeBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{createVolumeBean}", CreateVolumeBean.class).getValue(context.getELContext());
        Disk[] selDisks = this.getSelectedDisks();
        SystemOutPrintln.print_volume("selectedDisks length=" + this.selectedDisks.length);
        for(Disk disk: this.selectedDisks) {
            SystemOutPrintln.print_volume("disk: "+ disk.diskName);
        }
        long selectedSize = 0;
        List<Disk> diskLists = new ArrayList();
        Disk disk;
        String[] disknames = null;
        if (selDisks.length > 0) {
            disknames = new String[selDisks.length];
        }



//        LicenseInformation licenseInfo = node.serverNode.getXXModuleXXXFunctionLicense(Constants.MODEL_ID_BASIC, Constants.FUNCTIONID_BASIC_DISK_CAPACITY);
//        if ((licenseInfo.getFunctionNumber() * 1024L) < (usedDiskCapacity + selectedSize)) {
//            Constants.showErrorMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/CreatePoolDialog").getString("磁盘容量超过注册最大限制"));
//            return;
//        }

        if (groupName.trim().equals("")
                || groupName.trim().equals("mbapool")
                || groupName.trim().toLowerCase().startsWith("raid")
                || groupName.trim().toLowerCase().startsWith("mirror")
                || !ValidateUtility.checkPoolName(groupName.trim())) {
            if (!ValidateUtility.checkPoolName(groupName.trim())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "volumename_limit"), ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "volumename_limit"), ""));
            }
//            PoolNamejTextField.requestFocus();
            return null;
        }
        VolumeInterface volumeInt = InterfaceFactory.getVolumeInterfaceInstance();
        VolumeGroupInformation[] pools = volumeInt.getAllVolumeGroup();
        if (pools != null && pools.length > 0) {
            for (int k = 0; k < pools.length; k++) {
                if (groupName.trim().equalsIgnoreCase(pools[k].getName())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "volumename_have"), ""));
                    return null;
                }
            }
        }


        if (selDisks.length < 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "no_selectdisk"), ""));
            return null;
        }

        for (int i = 0; i < selDisks.length; i++) {
            selectedSize += Long.valueOf(selDisks[i].getSize());
            disknames[i] = selDisks[i].getDiskName();
            disk = new Disk();
            disk.setPosition(selDisks[i].getPosition());
            disk.setNum(selDisks[i].getNum());
            disk.setSize(selDisks[i].getSize());
            disk.setType(resources.get("volume.volumegroup_createvolume", "datadisk"));
            disk.setState(resources.get("volume.volumegroup_createvolume", "online"));
            disk.setDiskName(selDisks[i].getDiskName());
            if (type.equals("4")) {
                disk.setNotReplace(true);
            } else {
                disk.setNotReplace(false);
            }
            disk.setNotRemove(true);
            if (disk.getState().equals(resources.get("volume.volumegroup_createvolume", "damage"))) {
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "getTotalDiskSizeFailed"), ""));
            return null;
        }
        if (usedDiskCapacity+selectedSize > maxSize*1024) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "totalDiskSize_limit" )+ maxSize + "TB", ""));
            return null;
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
            typeStr = resources.get("volume.volumegroup_createvolume", "raidz3");
            PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAIDz3;
        }
        volume.setRaidType(typeStr);
        volume.setState(resources.get("volume.volumegroup_createvolume", "online"));
        volume.setDiskList(diskLists);



        switch (PoolType) {
            case 1:
                if (selDisks.length < 3) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "nocountdisks"), ""));
                    return null;
                }
                break;
            case 2:
                if (selDisks.length < 4) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "nocountdisks"), ""));
                    return null;
                }
                break;
            case 3:
                if (selDisks.length < 6) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "nocountdisks"), ""));
                    return null;
                }
                if (selDisks.length % 2 != 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "evendisks"), ""));
                    return null;
                }
                break;
            case 4:
                if (selDisks.length < 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "nocountdisks"), ""));
                    return null;
                }
                break;
            case 5:
                if (selDisks.length < 8) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "nocountdisks"), ""));
                    return null;
                }
                break;
            case 6:
                if (selDisks.length < 6) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "nocountdisks"), ""));
                    return null;
                }
                break;
            case 7:
                if (selDisks.length != 2) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "onlytwodisks"), ""));
                    return null;
                }
                break;
            default:
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "nocountdisks"), ""));
                break;
        }

        String returnStr = null;
        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        SystemOutPrintln.print_volume("PoolType=" + PoolType + ",groupName=" + groupName);
        for (int k = 0; k < disknames.length; k++) {
            SystemOutPrintln.print_volume("disknames[k]=" + disknames[k]);

        }
        boolean creat = volumeIn.createVolumeGroup(PoolType, groupName, disknames);
        if (creat == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "creatdiskfail"), ""));
            returnStr = null;

        } else {
            returnStr = "volumegroup?faces-redirect=true";
        }




//        FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
//        CreateVolumeBean share = (CreateVolumeBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{createVolumeBean}", CreateVolumeBean.class).getValue(context.getELContext());
//        Disk[] selDisks = share.getSelectedDisks();
//        
//        List<Disk> diskList = new ArrayList();
//        Disk disk;
//        for (int i = 0; i < selDisks.length; i++) {
//            System.out.println("#############selDisks[i].getDiskName()="+selDisks[i].getDiskName());
//            disk = new Disk();
//            disk.setPosition(selDisks[i].getPosition());
//            disk.setNum(selDisks[i].getNum());
//            disk.setSize(selDisks[i].getSize());
//            disk.setType("数据磁盘");
//            disk.setState("在线");
//            disk.setDiskName(selDisks[i].getDiskName());
//            if(type.equals("4")){
//                disk.setNotReplace(true);
//            }else{
//                 disk.setNotReplace(false);
//            }
//            disk.setNotRemove(true);
//            if(disk.getState().equals("损毁")){
//                disk.setNotApart(false);
//            }else{
//                 disk.setNotApart(true);
//            }
//            diskList.add(disk);
//        }

//        VolumesInfo volume = new VolumesInfo();
//        volume.setName(groupName);
//        volume.setUsedSize("5GB");
//        volume.setUnusedSize("1.22TB");
//        String typeStr = "RAID5";
//        if(type.equals("0")){
//            typeStr = "RAID5";
//        }else if(type.equals("1")){
//             typeStr = "RAID6";
//        }else if(type.equals("2")){
//             typeStr = "RAID5+0";
//        }else if(type.equals("3")){
//             typeStr = "RAID6+0";
//        }else if(type.equals("4")){
//             typeStr = "RAID0";
//        }else if(type.equals("5")){
//             typeStr = "RAID1";
//        }else if(type.equals("6")){
//             typeStr = "三倍校验";
//        }
//        volume.setRaidType(typeStr);
//        volume.setState("在线");
//        volume.setDiskList(diskList);
//
//        VolumesInfoBean vol = (VolumesInfoBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{volumesInfoBean}", VolumesInfoBean.class).getValue(context.getELContext());
//        vol.getVolumeList().add(volume);
//        
//        //删除用掉的磁盘
//        UnusedDiskBean undisk = (UnusedDiskBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{unusedDiskBean}", UnusedDiskBean.class).getValue(context.getELContext());
////        List<DiskBean> list = undisk.getDiskList();
//        for (int i = 0; i < undisk.getDiskList().size(); i++) {
//            for (int j = 0; j < selDisks.length; j++) {
//                if (undisk.getDiskList().get(i).getNum().equals(selDisks[j].getNum())) {
//                    undisk.getDiskList().get(i).setState(DISK_MODEL_DATA +"");
//                }
//            }
//        }

        return returnStr;
    }

    public String cancleButton() {
        return "volumegroup?faces-redirect=true";
    }

    public void refresh() {
        VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
        boolean ref = volume.refreshCache();
        if (ref == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_createvolume", "refreshcache_fail"), ""));
            return;
        } else {
            addList();
            diskModel = new DiskModel(diskList);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, resources.get("volume.volumegroup_createvolume", "refreshcache_ok"), ""));

        }
    }
}
