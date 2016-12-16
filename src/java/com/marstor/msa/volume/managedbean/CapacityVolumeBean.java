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
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "capacityVolumeBean")
@ViewScoped
public class CapacityVolumeBean extends DiskStates  implements Serializable{

    private Disk diskBean;
    private List<Disk> diskList;
    private DiskModel diskModel;
    private Disk[] selectedDisks;
    public Disk selectedDisk;
    private String volumeName;
    private int raidType = 0;
    private String raidLable;
    private long usedDiskCapacity = 0L;
    private String raidLableTip;
    private MSAResource resources = new MSAResource();
    private String tip;
    private String[] disknames = null;

    /**
     * Creates a new instance of createVolumeBean
     */
    public CapacityVolumeBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        volumeName = request.getParameter("volumeName");
        raidType = Integer.valueOf(request.getParameter("type"));
        raidLable = "RAID5";
        if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID0) {
            raidLable = "RAID0";
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_one");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID1) {
            raidLable = "RAID1";
             raidLableTip = resources.get("volume.volumegroup_capacityvolume", "onlytwodisks");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID5) {
            raidLable = "RAID5";
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_select");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID5_0) {
            raidLable = "RAID5+0";
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_six");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID6) {
            raidLable = "RAID6";
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_four");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID6_0) {
            raidLable = "RAID6+0";
             raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_eight");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAIDz3) {
            raidLable = resources.get("volume.volumegroup_capacityvolume", "raidz3");
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_six");
        } 
        this.addList();
        diskModel = new DiskModel(diskList);
    }
    public String checkType() {
        if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID0) {
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_one");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID1) {
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "onlytwodisks");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID5) {
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_select");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID5_0) {
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_six");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID6) {
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_four");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAID6_0) {
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_eight");
        } else if (raidType == VolumeGroupInformation.ZPOOL_TYPE_RAIDz3) {
            raidLableTip = resources.get("volume.volumegroup_capacityvolume", "min_six");
        }
        return raidLableTip;
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
                    String postion = diskInformation[i].cardModel == 2 ? (resources.get("volume.volumegroup_capacityvolume", "extend") + diskInformation[i].cardIndex) : resources.get("volume.volumegroup_capacityvolume", "host");              
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

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void handle(String groupName, int type) {
        if (type==VolumeGroupInformation.ZPOOL_TYPE_RAID1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "onlytwodisks"), ""));
            return ;
        }

        Disk[] selDisks = this.getSelectedDisks();
        if (selDisks.length < 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "no_selectdisk"), ""));
            return ;
        }
        SystemOutPrintln.print_volume("selectedDisks length=" + this.selectedDisks.length);
        for(Disk disk: this.selectedDisks) {
            SystemOutPrintln.print_volume("disk: "+ disk.diskName);
        }
        long selectedSize = 0;
        List<Disk> diskLists = new ArrayList();
        Disk disk;
        disknames = null;
        if (selDisks.length > 0) {
            disknames = new String[selDisks.length];
        }
        for (int i = 0; i < selDisks.length; i++) {
            selectedSize += Long.valueOf(selDisks[i].getSize());
            disknames[i] = selDisks[i].getDiskName();
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "getTotalDiskSizeFailed"), ""));
            return ;
        }
        if (usedDiskCapacity+selectedSize > maxSize*1024) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "totalDiskSize_limit" )+ maxSize + "TB", ""));
            return ;
        }
        
         switch (type) {
            case 1:
                if (selDisks.length < 3) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "nocountdisks"), ""));
                    return ;
                }
                break;
            case 2:
                if (selDisks.length < 4) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "nocountdisks"), ""));
                    return ;
                }
                break;
            case 3:
                if (selDisks.length < 6) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "nocountdisks"), ""));
                    return ;
                }
                if (selDisks.length % 2 != 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "evendisks"), ""));
                    return ;
                }
                break;
            case 4:
                if (selDisks.length < 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "nocountdisks"), ""));
                    return;
                }
                break;
            case 5:
                if (selDisks.length < 8) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "nocountdisks"), ""));
                    return;
                }
                break;
            case 6:
                if (selDisks.length < 6) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "nocountdisks"), ""));
                    return;
                }
                break;
            case 7:
                if (selDisks.length != 2) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "onlytwodisks"), ""));
                    return;
                }
                break;
            default:
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "nocountdisks"), ""));
                return;
        }
      this.handle_tip();

    }
    
     public String capacityVolume() {
         
        String returnStr = null;
        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        SystemOutPrintln.print_volume("PoolType=" + raidType + ",groupName=" + volumeName);
        for (int k = 0; k < disknames.length; k++) {
            SystemOutPrintln.print_volume("disknames[k]=" + disknames[k]);

        }
        boolean creat = volumeIn.expandVolumeGroup(raidType, volumeName, disknames);
        if (creat == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "capacitdiskfail"), ""));
            returnStr = null;

        } else {
            returnStr = "volumegroup?faces-redirect=true";
        }

        return returnStr;
     }
    
     public void handle_tip() {
   
        tip = resources.get("volume.volumegroup_capacityvolume", "capacity_left") + volumeName + resources.get("volume.volumegroup_capacityvolume", "capacity_right");
         SystemOutPrintln.print_common("tip="+tip);
         RequestContext.getCurrentInstance().execute("capacity.show()");
    }
     
     

    public String cancleButton() {
        return "volumegroup?faces-redirect=true";
    }

    public String getRaidLableTip() {
        return raidLableTip;
    }

    public void setRaidLableTip(String raidLableTip) {
        this.raidLableTip = raidLableTip;
    }

    public void refresh() {
        VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
        boolean ref = volume.refreshCache();
        if (ref == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_capacityvolume", "refreshcache_fail"), ""));
            return;
        } else {
            addList();
            diskModel = new DiskModel(diskList);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, resources.get("volume.volumegroup_capacityvolume", "refreshcache_ok"), ""));

        }
    }
}
