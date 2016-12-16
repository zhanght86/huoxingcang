/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.managedbean;

import com.marstor.msa.common.bean.PhysicalDiskInformation;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.volume.model.Disk;
import com.marstor.msa.volume.model.DiskStates;
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
@ManagedBean(name = "dataDiskReplaceBean")
@ViewScoped
public class DataDiskReplaceBean extends DiskStates  implements Serializable{

    public String volumeNameStr;
    public String diskNameStr;  //被替换磁盘
    public List<String> replaceDiskList; //替换磁盘列表
    Disk diBean;
    public List<Disk> diList;
    public String replacedDiskName;  //替换磁盘
    public boolean isSameName = false;
    public boolean isPrimaryOnline = false;
    public boolean notUnusedDisk;
    public boolean isprocess = false;
//    public String strProcess ="0%";
    public String newDiskName;
    public String badDiskName;
    public String badDiskState;
    public String volumeName;
    private long usedDiskCapacity = 0L;
    public boolean cantuse = false;
    public boolean cantButtonUse = false;
    MSAResource resources = new MSAResource();

    /**
     * Creates a new instance of dataDiskReplaceBean
     */
    public DataDiskReplaceBean() {
        getReplaceList();
    }

    public void getReplaceList() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        volumeNameStr = request.getParameter("volumeName");
        String state = request.getParameter("state");
        int stateInt = Integer.valueOf(state);
        String poolSta = resources.get("volume.volumegroup_datadiskreplace", "data_disk_state_online");
        switch (stateInt) {
            case PhysicalDiskInformation.DATA_DISK_STATE_ONLINE:
                poolSta = resources.get("volume.volumegroup_datadiskreplace", "data_disk_state_online");
                break;
            case PhysicalDiskInformation.DATA_DISK_STATE_OFFLINE:
                poolSta = resources.get("volume.volumegroup_datadiskreplace", "data_disk_state_offline");
                break;
            case PhysicalDiskInformation.DATA_DISK_CANT_OPEN:
                poolSta = resources.get("volume.volumegroup_datadiskreplace", "data_disk_cant_open");
                break;
            case PhysicalDiskInformation.SPARE_DISK_STATE_AVAILIBLE:
                poolSta = resources.get("volume.volumegroup_datadiskreplace", "spare_disk_state_availible");
                break;
            case PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_SELF:
                poolSta = resources.get("volume.volumegroup_datadiskreplace", "spare_disk_state_inuse_self");
                break;
            case PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_OTHER:
                poolSta = resources.get("volume.volumegroup_datadiskreplace", "spare_disk_state_inuse_other");
                break;
            case PhysicalDiskInformation.UNUSED_DISK_STATE:
                poolSta = resources.get("volume.volumegroup_datadiskreplace", "unused_disk_state");
                break;
            case PhysicalDiskInformation.DATA_DISK_REMOVED:
                poolSta = resources.get("volume.volumegroup_datadiskreplace", "data_disk_remove");
                break;
            case PhysicalDiskInformation.SMART_BEING_FAILING:
                poolSta = resources.get("volume.volumegroup_datadiskreplace", "smart_being_failing");
                break;
            default:
                poolSta = resources.get("volume.volumegroup_datadiskreplace", "unknow");
                break;
        }
        diskNameStr = request.getParameter("diskName") + " " + poolSta;
        SystemOutPrintln.print_volume("volumeNameStr替换磁盘=" + volumeNameStr);


        cantuse = false;
        cantButtonUse = false;
        notUnusedDisk = false;
//        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        int userType = user.getType();
        if (userType == 2) {
            cantButtonUse = false;
        } else {
            cantButtonUse = true;
        }
        replaceDiskList = new ArrayList();
        diList = new ArrayList();

        VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
        PhysicalDiskInformation[] diskInformation = volume.getAllPhysicalDisk();
        boolean ishave = false;
        if (diskInformation != null && diskInformation.length > 0) {

            for (int i = 0; i < diskInformation.length; i++) {
                if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_UNUSED) {
                    ishave = true;
                    diBean = new Disk();
                    diBean.setPosition(diskInformation[i].position);
                    diBean.setState(resources.get("volume.volumegroup_datadiskreplace", "unused_disk"));
                    diBean.setSize(diskInformation[i].diskSize + "");
                    diBean.setDiskName(diskInformation[i].diskName);
                    diList.add(diBean);
                    replaceDiskList.add(diskInformation[i].diskName);


                } else {
                    usedDiskCapacity += diskInformation[i].diskSize;
                }
            }
        }
        if(!ishave){
            replaceDiskList.add(" ");
        }

//        FacesContext context = FacesContext.getCurrentInstance();
//        UnusedDiskBean undisk = (UnusedDiskBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{unusedDiskBean}", UnusedDiskBean.class).getValue(context.getELContext());
//        List<Disk> unDiskList = undisk.getDiskList();
//        for (int i = 0; i < unDiskList.size(); i++) {
//            Disk disk = unDiskList.get(i);
//            if (disk.getState().equals(DISK_MODEL_UNUSED + "")) {
//                diBean = new Disk();
//                diBean.setPosition(disk.getPosition());
//                diBean.setNum(disk.getNum());
//                diBean.setSize(disk.getSize());
//                diBean.setDiskName(disk.getDiskName());
//                diList.add(diBean);
//                replaceDiskList.add(disk.getDiskName());
//            }
//        }
    }

    public boolean isCantButtonUse() {
        return cantButtonUse;
    }

    public void setCantButtonUse(boolean cantButtonUse) {
        this.cantButtonUse = cantButtonUse;
    }

    public boolean isCantuse() {
        return cantuse;
    }

    public void setCantuse(boolean cantuse) {
        this.cantuse = cantuse;
    }

    public String getDiskNameStr() {
        return diskNameStr;
    }

    public void setDiskNameStr(String diskNameStr) {
        this.diskNameStr = diskNameStr;
    }

    public List<String> getReplaceDiskList() {
        return replaceDiskList;
    }

    public void setReplaceDiskList(List<String> replaceDiskList) {
        this.replaceDiskList = replaceDiskList;
    }

//    public String returnDiskName(String volumeName,String diskname,String state,String size) {
//        System.out.println("###################volumeName="+volumeName);
//        
//        
//        
//        
//        volumeNameStr = volumeName;
//        diskNameStr = diskname+" - "+size+"GB "+state;
//         String  param = "volumeName=" + volumeNameStr + "&" + "diskName=" + diskNameStr ;
//
//        return "volumegroup_datadiskreplace?faces-redirect=true";
//    }
    public String getReplacedDiskName() {
        return replacedDiskName;
    }

    public void setReplacedDiskName(String replacedDiskName) {
        this.replacedDiskName = replacedDiskName;
    }

    public boolean isIsSameName() {
        return isSameName;
    }

    public void setIsSameName(boolean isSameName) {
        this.isSameName = isSameName;
    }

    public boolean isIsPrimaryOnline() {
        return isPrimaryOnline;
    }

    public void setIsPrimaryOnline(boolean isPrimaryOnline) {
        this.isPrimaryOnline = isPrimaryOnline;
    }

    public String getVolumeNameStr() {
        return volumeNameStr;
    }

    public void setVolumeNameStr(String volumeNameStr) {
        this.volumeNameStr = volumeNameStr;
    }

    public boolean isNotUnusedDisk() {
        return notUnusedDisk;
    }

    public void setNotUnusedDisk(boolean notUnusedDisk) {
        this.notUnusedDisk = notUnusedDisk;
    }

    public boolean isIsprocess() {
        return isprocess;
    }

    public void setIsprocess(boolean isprocess) {
        this.isprocess = isprocess;
    }

//    public String getStrProcess() {
//        return strProcess;
//    }
//
//    public void setStrProcess(String strProcess) {
//        this.strProcess = strProcess;
//    }
    public String saveDateReplace(String poolName, String diskName, String replaceDisk, boolean isSame, boolean isPrimary) {
         SystemOutPrintln.print_volume("replace disk replaceDisk="+replaceDisk);
        volumeName = poolName;
        newDiskName = null;
        badDiskName = null;
        if (replaceDisk == null || replaceDisk.equalsIgnoreCase("null") || replaceDisk.equalsIgnoreCase("") || replaceDisk.equalsIgnoreCase(" ")) {
            System.out.println("replace disk");
            newDiskName = null;
        } else {
            newDiskName = replaceDisk;
        }
//        if (replaceDisk == null) {
//            newDiskName = null;
//        } else {
//            newDiskName = replaceDisk;
//        }
        if (diskName != null) {
            badDiskName = diskName.split(" ")[0];
            badDiskState = diskName.split(" ")[diskName.split(" ").length - 1];
        }
        if (newDiskName == null && !isSame && !isPrimary) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_datadiskreplace", "unselect"), ""));
            return null;
        }
//判断卷组使用盘的总容量  ????缺
        if (badDiskState.equals(resources.get("volume.volumegroup_datadiskreplace", "data_disk_cant_open"))) {
            //激活所有新添加的磁盘
        }
        if (isSame == true || isPrimary == true) {
            newDiskName = badDiskName;
        }
        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        boolean replace = volumeIn.replaceDisk(poolName, badDiskName, newDiskName, isPrimary);

        String state = volumeIn.queryReplaceState(poolName, badDiskName, newDiskName);
        SystemOutPrintln.print_volume("state=" + state);
        String returnStr = null;
        if (state.equals("0")) {
            returnStr = "volumegroup?faces-redirect=true";
        } else if (state.equals("2")) {  //ReplaceDiskProcess.RET_ERROR
            cantuse = false;
            cantButtonUse = false;
            notUnusedDisk = false;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,resources.get("volume.volumegroup_datadiskreplace", "replacedisk_fail") , ""));
            returnStr = null;
        } else if (state.equals("3")) {  //ReplaceDiskProcess.RET_USER_CLOSE
            cantuse = true;
            cantButtonUse = true;
            notUnusedDisk = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, resources.get("volume.volumegroup_datadiskreplace", "replacedisk_tip"), "。"));
            returnStr = null;
        } else {
            cantuse = true;
            cantButtonUse = true;
            notUnusedDisk = true;

            isprocess = true;
//            strProcess =state;

            returnStr = null;
        }



//        FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
//        VolumesInfoBean vol = (VolumesInfoBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{volumesInfoBean}", VolumesInfoBean.class).getValue(context.getELContext());
//        List<Disk> list = vol.getSelectVolume().getDiskList();
//        for (int j = 0; j < this.diList.size(); j++) {
//            Disk replace = this.diList.get(j);
//            if (replace.getDiskName().equals(replaceDisk)) {
//                
//                for (int i = 0; i < list.size(); i++) {
//                    Disk disk = list.get(i);
//                    if (disk.getDiskName().equals(diskName)) {
//
//                        disk.setPosition(replace.getPosition());
//                        disk.setNum(replace.getNum());
//                        disk.setSize(replace.getSize());
//                        disk.setType("数据磁盘");
//                        disk.setState("在线");
//                        disk.setDiskName(replace.getDiskName());
//                       
//                            disk.setNotReplace(false);
//                       
//                        disk.setNotRemove(true);
//                        if (disk.getState().equals("损毁")) {
//                            disk.setNotApart(false);
//                        } else {
//                            disk.setNotApart(true);
//                        }
//                        break;
//                    }
//                }
//                break;
//            }
//        }



        return returnStr;
    }

    public void changeSameNameCheckbox(boolean isSame) {
        if (isSame == true) {
            isPrimaryOnline = false;
            notUnusedDisk = true;

        } else {
            notUnusedDisk = false;
        }
    }

    public void changePrimaryOnlineCheckbox(boolean isPrimary) {
        if (isPrimary == true) {
            isSameName = false;
            notUnusedDisk = true;

        } else {
            notUnusedDisk = false;
        }
    }
}
