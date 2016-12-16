/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.managedbean;
import com.marstor.msa.common.bean.PhysicalDiskInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyConstants;
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
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "diskBean")
@ViewScoped
public class DiskBean extends DiskStates  implements Serializable{

    public Disk disk;
    public List<Disk> diskListInfo;
    private Disk selectedDisk;
    MSAResource resources = new MSAResource();
    public String diskName;
   

    /**
     * Creates a new instance of diskBean
     */
    public DiskBean() {
        getDisks();
    }

    public void getDisks() {
        diskListInfo = new ArrayList();
        VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
        PhysicalDiskInformation[] disks = volume.getAllPhysicalDisk();
        
        if (disks == null) {
            return;
        }
        for (int i = 0; i < disks.length; i++) {
             disk = new Disk();
//            boolean bssd = disks[i].bSSD;
            if (MyConstants.diskModel != PhysicalDiskInformation.DISK_MODEL_SPARE) {
                String pos = disks[i].cardModel == 2 ? (resources.get("volume.disklist", "expand") + disks[i].cardIndex) : resources.get("volume.disklist", "host");
//                if(bssd){
//                    disk.setPosition(pos+"(SSD)");
//                }else{
//                    disk.setPosition(pos);
//                }
                disk.setPosition(pos);
                disk.setNum(disks[i].position);
            }           
            disk.setSize(String.valueOf(disks[i].diskSize));
//            content.add(("".equals(disks[i].poolName)) ? getString("未分配") : disks[i].poolName);
            switch (disks[i].model) {
                case PhysicalDiskInformation.DISK_MODEL_DATA:
                    disk.setType(resources.get("volume.disklist", "disk_model_data"));
                    break;
                case PhysicalDiskInformation.DISK_MODEL_SPARE:
                    disk.setType(resources.get("volume.disklist", "disk_model_spare"));
                    break;
                case PhysicalDiskInformation.DISK_MODEL_UNUSED:
                    disk.setType(resources.get("volume.disklist", "disk_model_unused"));
                    break;
                case PhysicalDiskInformation.DISK_MODEL_CACHE:
                    disk.setType(resources.get("volume.disklist", "disk_model_cache"));
                    break;
                case PhysicalDiskInformation.DISK_MODEL_LOG:
                    disk.setType(resources.get("volume.disklist", "disk_model_log"));
                    break;
                default:
                    disk.setType(resources.get("volume.disklist", "unkonw"));
                    break;
            }
            switch (disks[i].state) {
                case PhysicalDiskInformation.DATA_DISK_STATE_ONLINE:
                    disk.setState(resources.get("volume.disklist", "data_disk_state_online"));
                    disk.setStateIcon("../resources/volume/picture/normal.png");
                    break;
                case PhysicalDiskInformation.DATA_DISK_STATE_OFFLINE:
                    disk.setState(resources.get("volume.disklist", "data_disk_state_offline"));
                    disk.setStateIcon("../resources/volume/picture/normal.png");
                    break;
                case PhysicalDiskInformation.DATA_DISK_CANT_OPEN:
                    disk.setState(resources.get("volume.disklist", "data_disk_cant_open"));
                     disk.setStateIcon("../resources/volume/picture/damage.png");
                    break;
                case PhysicalDiskInformation.SPARE_DISK_STATE_AVAILIBLE:
                    disk.setState(resources.get("volume.disklist", "spare_disk_state_availible"));
                    disk.setStateIcon("../resources/volume/picture/normal.png");
                    break;
                case PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_SELF:
                    disk.setState(resources.get("volume.disklist", "spare_disk_state_inuse_self"));
                    disk.setStateIcon("../resources/volume/picture/normal.png");
                    break;
                case PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_OTHER:
                    disk.setState(resources.get("volume.disklist", "spare_disk_state_inuse_other"));
                     disk.setStateIcon("../resources/volume/picture/normal.png");
                    break;
                case PhysicalDiskInformation.UNUSED_DISK_STATE:
                    disk.setState(resources.get("volume.disklist", "unused_disk_state_STATE"));
                     disk.setStateIcon("../resources/volume/picture/normal.png");
                    break;
                case PhysicalDiskInformation.DATA_DISK_REMOVED:
                    disk.setState(resources.get("volume.disklist", "data_disk_removed"));
                     disk.setStateIcon("../resources/volume/picture/normal.png");
                    break;
                case PhysicalDiskInformation.SMART_BEING_FAILING:
                   disk.setState(resources.get("volume.disklist", "smart_being_failing"));
                    disk.setStateIcon("../resources/volume/picture/warn.png");
                    break;
                default:
                    disk.setState(resources.get("volume.disklist", "unkonw"));
                    disk.setStateIcon("../resources/volume/picture/normal.png");
                    break;
            }
            disk.setDiskName(disks[i].diskName);
            disk.setPoolName(disks[i].poolName);
            if (disks[i].model == PhysicalDiskInformation.DISK_MODEL_UNUSED) {
                disk.setIsUninstall_rendered(true);
            } else {
                disk.setIsUninstall_rendered(false);
            }
//            if ((disks[i].state == PhysicalDiskInformation.DATA_DISK_CANT_OPEN && (disk.getPoolName() == null || disk.getPoolName().equals("")))
//                    || disks[i].state == PhysicalDiskInformation.UNUSED_DISK_STATE) {
//                disk.setIsUninstall_rendered(true);
//            } else {
//                disk.setIsUninstall_rendered(false);
//            }
//          if(disk.getState().equals("未分配") || (disk.getState().equals("损坏") && (disk.getPoolName()==null || disk.getPoolName().equals("")))){
//            disk.setIsUninstall_rendered(true);
//        }else{
//            disk.setIsUninstall_rendered(false);
//        }
            if(disks[i].bSSD){
                disk.setStrType("SSD");
            } else {
                if (disks[i].SATAorSAS ==1) {
                    disk.setStrType("SATA");
                }else if(disks[i].SATAorSAS ==2){
                    disk.setStrType("SAS");
                }else {
                    disk.setStrType(resources.get("volume.disklist", "unkonw"));
                }

            }
        diskListInfo.add(disk);
        }
        

//        disk = new Disk();
//        disk.setPosition("主机");
//        disk.setNum("1");
//        disk.setSize("1863");
//        disk.setType("热备");
//        disk.setState("在线");
//        disk.setDiskName("c3t50014EE002DEB8B2d0");
//        disk.setPoolName("SYSVOL");
//          if(disk.getState().equals("未分配") || (disk.getState().equals("损坏") && (disk.getPoolName()==null || disk.getPoolName().equals("")))){
//            disk.setIsUninstall_rendered(true);
//        }else{
//            disk.setIsUninstall_rendered(false);
//        }
//        diskListInfo.add(disk);
//
//        disk = new Disk();
//        disk.setPosition("主机");
//        disk.setNum("2");
//        disk.setSize("298");
//        disk.setType("高速缓冲");
//        disk.setState("在线");
//        disk.setDiskName("c8t2d0");
//        disk.setPoolName("SYSVOL");
//          if(disk.getState().equals("未分配") || (disk.getState().equals("损坏") && (disk.getPoolName()==null || disk.getPoolName().equals("")))){
//            disk.setIsUninstall_rendered(true);
//        }else{
//            disk.setIsUninstall_rendered(false);
//        }
//        diskListInfo.add(disk);
//
//        disk = new Disk();
//        disk.setPosition("主机");
//        disk.setNum("3");
//        disk.setSize("298");
//        disk.setType("日志");
//        disk.setState("在线");
//        disk.setDiskName("c3t5000C50010D88FF0d0");
//        disk.setPoolName("SYSVOL");
//          if(disk.getState().equals("未分配") || (disk.getState().equals("损坏") && (disk.getPoolName()==null || disk.getPoolName().equals("")))){
//            disk.setIsUninstall_rendered(true);
//        }else{
//            disk.setIsUninstall_rendered(false);
//        }
//        diskListInfo.add(disk);
//
//        disk = new Disk();
//        disk.setPosition("主机");
//        disk.setNum("4");
//        disk.setSize("233");
//        disk.setType("数据");
//        disk.setState("损坏");
//        disk.setDiskName("c4t0d0");
//  
//        if(disk.getState().equals("未分配") || (disk.getState().equals("损坏") && (disk.getPoolName()==null || disk.getPoolName().equals("")))){
//            disk.setIsUninstall_rendered(true);
//        }else{
//            disk.setIsUninstall_rendered(false);
//        }
//        diskListInfo.add(disk);
//
//        disk = new Disk();
//        disk.setPosition("主机");
//        disk.setNum("5");
//        disk.setSize("233");
//        disk.setType("未分配");
//        disk.setState("未分配");
//        disk.setDiskName("c0t7d0");
//        disk.setPoolName("SYSVOL");
//        disk.setPoolName("SYSVOL");
//          if(disk.getState().equals("未分配") || (disk.getState().equals("损坏") && (disk.getPoolName()==null || disk.getPoolName().equals("")))){
//            disk.setIsUninstall_rendered(true);
//        }else{
//            disk.setIsUninstall_rendered(false);
//        }
//        diskListInfo.add(disk);
//
//        disk = new Disk();
//        disk.setPosition("主机");
//        disk.setNum("9");
//        disk.setSize("1863");
//        disk.setType("未分配");
//        disk.setState("未分配");
//        disk.setDiskName("c3t50014EE05833F874d0");
//        disk.setPoolName("SYSVOL");
//        disk.setPoolName("SYSVOL");
//          if(disk.getState().equals("未分配") || (disk.getState().equals("损坏") && (disk.getPoolName()==null || disk.getPoolName().equals("")))){
//            disk.setIsUninstall_rendered(true);
//        }else{
//            disk.setIsUninstall_rendered(false);
//        }
//        diskListInfo.add(disk);
//        
//        disk = new Disk();
//        disk.setPosition("主机");
//        disk.setNum("9");
//        disk.setSize("1863");
//        disk.setType("数据");
//        disk.setState("损坏");
//        disk.setDiskName("c3t50014EE05833F87632");
//        disk.setPoolName("SYSVOL");
//        if(disk.getState().equals("未分配") || (disk.getState().equals("损坏") && (disk.getPoolName()==null || disk.getPoolName().equals("")))){
//            disk.setIsUninstall_rendered(true);
//        }else{
//            disk.setIsUninstall_rendered(false);
//        }
//        diskListInfo.add(disk);
    }

    public List<Disk> getDiskListInfo() {
//        getDisks();
        return diskListInfo;
    }

    public void setDiskListInfo(List<Disk> diskListInfo) {
        this.diskListInfo = diskListInfo;
    }



    public Disk getSelectedDisk() {
        return selectedDisk;
    }

    public void setSelectedDisk(Disk selectedDisk) {
        this.selectedDisk = selectedDisk;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }
    
    public void toUninstallDisk(){
//        diskName = selectedDisk.getDiskName();
        if(selectedDisk.position!=null && !selectedDisk.equals("")){
            diskName = selectedDisk.position+resources.get("volume.disklist", "deletedisktip_right1")+selectedDisk.num+resources.get("volume.disklist", "deletedisktip_right2")+resources.get("volume.disklist", "deletedisktip_right3")+selectedDisk.getDiskName()+resources.get("volume.disklist", "deletedisktip_right4");
        }else{
            diskName = resources.get("volume.disklist", "deletedisktip_right3")+diskName+resources.get("volume.disklist", "deletedisktip_right4");
        }
         RequestContext.getCurrentInstance().execute("deleteDisk.show()");
         
    }
    public void uninstallDisk(){
//          if (0 != Constants.showQuestionMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/RemoveDiskDialog").getString("该操作将从系统中卸载此磁盘，确认操作？"))) {
//            return;
//        }
        SystemOutPrintln.print_volume("uninstallDisk disk.getDiskName()="+selectedDisk.getDiskName());
        VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
       boolean unconf = volume.unconfigureDisk(selectedDisk.getDiskName());
       if(unconf == false){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.disklist", "deletedisk_fail"), ""));
          SystemOutPrintln.print_volume("uninstallDisk fail");
            return;
       }else{
            getDisks();
            SystemOutPrintln.print_volume("uninstallDisk succeed");
       }
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "拔出磁盘后点确定", "。"));
//        
//          BasicQueryAction replace = new BasicQueryAction("2227", "preReplaceDisk", node.socket);
//        replace.addParameter("DiskName", diskname);
//        if (!replace.queryAndGetBooleanResult()) {
//            Constants.showErrorMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/RemoveDiskDialog").getString("卸载磁盘失败"));
//            return;
//        }
//        Constants.showInformationMessage("拔出磁盘后点确定");
//        BasicQueryAction action = new BasicQueryAction("2229", "server do devfsadm -vC", node.socket);
//        if (action.queryAndGetBooleanResult()) {
//        }
//        diskListInfo.remove(selectedDisk);
    }
    
    public void refresh(){
        VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
        boolean ref = volume.refreshCache();
        if (ref == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.disklist", "refreshcache_fail"), ""));
            return;
        } else {
             getDisks();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,resources.get("volume.disklist", "refreshcache_ok") , ""));

        }
    }
    

}
