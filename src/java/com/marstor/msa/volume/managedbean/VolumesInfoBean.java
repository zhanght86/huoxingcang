/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.managedbean;

import com.marstor.msa.common.bean.PhysicalDiskInformation;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.bean.VolumeGroupInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyConstants;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.volume.model.Disk;
import com.marstor.msa.volume.model.VolumesInfo;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "volumesInfoBean")
@ViewScoped
public class VolumesInfoBean implements Serializable {

    VolumesInfo volume;
    public VolumesInfo selectVolume;
    public List<VolumesInfo> volumeList;
    List<Disk> diskList;
    List<Disk> dataList;
    Disk disk;
    MSAResource resources = new MSAResource();
    int userType = 0;
    public String selectVolumeName;
    public String diskname;
//    public String capacity;
//    public String use;
//    public String scale;
    

    /**
     * Creates a new instance of volumesInfoBean
     */
    public VolumesInfoBean() {
        System.out.println("111");
        getVolumes();
    }

    public void getVolumes() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        String userName = user.getName();
        userType = user.getType();

        volumeList = new ArrayList();

        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        VolumeGroupInformation[] pools = volumeIn.getAllVolumeGroup();
        

        if (pools == null) {
            return;
        }
        for (int i = 0; i < pools.length; i++) {
//            if (pools[i].name.equals("rpool")) {
//                continue;
//            }
            volume = new VolumesInfo();
            volume.setName(pools[i].name);

            String poolSta = resources.get("volume.volumegroup", "online");

            if (pools[i].usedSize != null) {
                volume.setUsedSize(pools[i].usedSize + "B");
            }
            if (pools[i].availableSize != null) {
                volume.setUnusedSize(pools[i].availableSize + "B");
            }
            volume.setPoolType(pools[i].poolType);

            String raidType = "RAID5";
            if (pools[i].poolType == VolumeGroupInformation.ZPOOL_TYPE_RAID0) {
                raidType = "RAID0";
            } else if (pools[i].poolType == VolumeGroupInformation.ZPOOL_TYPE_RAID1) {
                raidType = "RAID1";
            } else if (pools[i].poolType == VolumeGroupInformation.ZPOOL_TYPE_RAID5) {
                raidType = "RAID5";
            } else if (pools[i].poolType == VolumeGroupInformation.ZPOOL_TYPE_RAID5_0) {
                raidType = "RAID5+0";
            } else if (pools[i].poolType == VolumeGroupInformation.ZPOOL_TYPE_RAID6) {
                raidType = "RAID6";
            } else if (pools[i].poolType == VolumeGroupInformation.ZPOOL_TYPE_RAID6_0) {
                raidType = "RAID6+0";
            } else if (pools[i].poolType == VolumeGroupInformation.ZPOOL_TYPE_RAIDz3) {
                raidType = resources.get("volume.volumegroup", "raidz3");
            }
            volume.setRaidType(raidType);
            if (pools[i].poolState.equalsIgnoreCase("online")) {
                poolSta = resources.get("volume.volumegroup", "online");
            } else if (pools[i].poolState.equalsIgnoreCase("offline")) {
                poolSta = resources.get("volume.volumegroup", "offline");
            } else if (pools[i].poolState.equalsIgnoreCase("unavail")) {
                poolSta = resources.get("volume.volumegroup", "unavail");
            } else if (pools[i].poolState.equalsIgnoreCase("removed")) {
                poolSta = resources.get("volume.volumegroup", "removed");
            } else if (pools[i].poolState.equalsIgnoreCase("faulted")) {
                poolSta = resources.get("volume.volumegroup", "faulted");
            } else if (pools[i].poolState.equalsIgnoreCase("degraded")) {
                poolSta = resources.get("volume.volumegroup", "degraded");
            } else {
                poolSta = pools[i].poolState;

            }

            volume.setState(poolSta);
            volume.setNotImport(true);
            if (userType != 2) {
                volume.setNotDestroyPool(true);
                volume.setNotClearPool(true);
                volume.setNotfixPool(true);
                volume.setNotaddDisk(true);
                volume.setNotImport(true);
                volume.setNotCapacityPool(true);

            } else {
                volume.setNotDestroyPool(false);
                volume.setNotClearPool(false);
                volume.setNotfixPool(false);
                volume.setNotaddDisk(false);
                volume.setNotImport(true);
                if(pools[i].poolType == VolumeGroupInformation.ZPOOL_TYPE_RAID1){
                    volume.setNotCapacityPool(true);
                }
            }
            if (MyConstants.SYS_VOL.equals(pools[i].name)) {
                volume.setNotDestroyPool(true);
            }
//           if(pools[i].bRequireImport) {
//                volume.setNotDestroyPool(true);
//                volume.setNotClearPool(true);
//                volume.setNotfixPool(true);
//                volume.setNotaddDisk(true);
//           }

//            diskList = new ArrayList();
            ArrayList<PhysicalDiskInformation> dataDisks = pools[i].dataDisk;  //数据盘
            ArrayList<PhysicalDiskInformation> cacheDisks = pools[i].cacheDisk;  //高速缓冲盘
            ArrayList<PhysicalDiskInformation> logDisks = pools[i].logDisk;  //日志盘
            ArrayList<PhysicalDiskInformation> spareDisks = pools[i].spareDisk;  //热备磁盘
//            ArrayList<PhysicalDiskInformation> disks = new ArrayList();  //热备磁盘
//            if (dataDisks != null) {
//                System.out.println("dataDisks="+dataDisks.size());
//                disks.addAll(dataDisks);
//            }
//            if (cacheDisks != null) {
//                System.out.println("cacheDisks="+cacheDisks.size());
//                disks.addAll(cacheDisks);
//            }
//            if (logDisks != null) {
//                System.out.println("logDisks="+logDisks.size());
//                disks.addAll(logDisks);
//            }
//            if (spareDisks != null) {
//                System.out.println("spareDisks="+spareDisks.size());
//                disks.addAll(spareDisks);
//            }
//            
//            if (disks != null &&disks.size()>0) {
//                for (int j = 0; j < disks.size(); j++) {
//
//                    disk = new Disk();
//                    if (MyConstants.diskModel != PhysicalDiskInformation.DISK_MODEL_SPARE) {
//                        disk.setPosition(disks.get(j).cardModel == 2 ? (resources.get("volume.volumegroup", "expansionbox") + disks.get(j).cardIndex) : resources.get("volume.volumegroup", "host"));
//                        disk.setNum(disks.get(j).position);
//                    }
////                    String size ="0.00";
////                    if(disks.get(j).getDiskSize() >0){
////                        size = String.valueOf(disks.get(j).getDiskSize());
////                    }
////                    disk.setSize(size);
//                    switch (disks.get(j).model) {
//                        case PhysicalDiskInformation.DISK_MODEL_DATA:
//                            disk.setType(resources.get("volume.volumegroup", "datadisk"));
//                            break;
//                        case PhysicalDiskInformation.DISK_MODEL_SPARE:
//                            disk.setType(resources.get("volume.volumegroup", "sparedisk"));
//                            break;
//                        case PhysicalDiskInformation.DISK_MODEL_UNUSED:
//                            disk.setType(resources.get("volume.volumegroup", "unuseddisk"));
//                            break;
//                        case PhysicalDiskInformation.DISK_MODEL_CACHE:
//                            disk.setType(resources.get("volume.volumegroup", "cachedisk"));
//                            break;
//                        case PhysicalDiskInformation.DISK_MODEL_LOG:
//                            disk.setType(resources.get("volume.volumegroup", "logdisk"));
//                            break;
//                        default:
//                            disk.setType(resources.get("volume.volumegroup", "unknown"));
//                            break;
//                    }
//                    switch (disks.get(j).state) {
//                        case PhysicalDiskInformation.DATA_DISK_STATE_ONLINE:
//                            disk.setState(resources.get("volume.volumegroup", "online"));
//                            break;
//                        case PhysicalDiskInformation.DATA_DISK_STATE_OFFLINE:
//                            disk.setState(resources.get("volume.volumegroup", "offline"));
//                            break;
//                        case PhysicalDiskInformation.DATA_DISK_CANT_OPEN:
//                            disk.setState(resources.get("volume.volumegroup", "damage"));
//                            break;
//                        case PhysicalDiskInformation.SPARE_DISK_STATE_AVAILIBLE:
//                            disk.setState(resources.get("volume.volumegroup", "availible"));
//                            break;
//                        case PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_SELF:
//                            disk.setState(resources.get("volume.volumegroup", "inuseself"));
//                            break;
//                        case PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_OTHER:
//                            disk.setState(resources.get("volume.volumegroup", "inuseother"));
//                            break;
//                        case PhysicalDiskInformation.UNUSED_DISK_STATE:
//                            disk.setState(resources.get("volume.volumegroup", "unused"));
//                            break;
//                        case PhysicalDiskInformation.DATA_DISK_REMOVED:
//                            disk.setState(resources.get("volume.volumegroup", "removedisk"));
//                            break;
//                        case PhysicalDiskInformation.SMART_BEING_FAILING:
//                            disk.setState(resources.get("volume.volumegroup", "failing"));
//                            break;
//                        default:
//                            disk.setState(resources.get("volume.volumegroup", "unknown"));
//                            break;
//                    }
//                    disk.setDiskName(disks.get(j).diskName);
//
//                    if (!volume.getRaidType().equals("RAID0") && disk.getType().equals(resources.get("volume.volumegroup", "datadisk"))) {
//                        disk.setNotReplace(false);
//                    } else {
//                        disk.setNotReplace(true);
//                    }
//                    if (disk.getType().equals(resources.get("volume.volumegroup", "datadisk"))) {
//                        disk.setNotRemove(true);
//                    } else {
//                        disk.setNotRemove(false);
//                    }
//                    if (disk.getType().equals(resources.get("volume.volumegroup", "datadisk")) && disk.getState().equals(resources.get("volume.volumegroup", "damage"))) {
//                        disk.setNotApart(false);
//                    } else {
//                        disk.setNotApart(true);
//                    }
//                    if(userType != 2){
//                        disk.setNotReplace(true);
//                        disk.setNotRemove(true);
//                        disk.setNotApart(true); 
//                    }
//
//                    diskList.add(disk);
//
//                }
//            }

//            volume.setDiskList(diskList);
            volume.setCacheDisks(cacheDisks);
            volume.setDataDisks(dataDisks);
            volume.setLogDisks(logDisks);
            volume.setSpareDisks(spareDisks);
            volumeList.add(volume);
        }

        List<String> exportVolumes = volumeIn.getExportVolume();

        if (exportVolumes == null) {
            return;
        }
        for (int k = 0; k < exportVolumes.size(); k++) {

            volume = new VolumesInfo();
            volume.setName(exportVolumes.get(k));
            volume.setUsedSize("--");
            volume.setUnusedSize("--");
            volume.setRaidType("--");
            volume.setPoolType(100);
            volume.setState(resources.get("volume.volumegroup", "unimport"));

            volume.setNotImport(false);
            volume.setNotCapacityPool(true);
            if (userType != 2) {
                volume.setNotDestroyPool(true);
                volume.setNotClearPool(true);
                volume.setNotfixPool(true);
                volume.setNotaddDisk(true);
                volume.setNotImport(true);

            } else {
                volume.setNotDestroyPool(true);
                volume.setNotClearPool(true);
                volume.setNotfixPool(true);
                volume.setNotaddDisk(true);
                volume.setNotImport(false);
            }

            volumeList.add(volume);
        }

    }

    public void getExpansions(VolumesInfo pool) {
      
        SystemOutPrintln.print_volume("pool=" + (pool == null));
        SystemOutPrintln.print_volume("pool.notImport=" + pool.notImport);

        if (!pool.notImport) {
            return;
        }
        SystemOutPrintln.print_volume("pool name=" + pool.name);
         VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        String[] datas= volumeIn.getDedupInfo(pool.name);
        dataList =  new ArrayList();
       
        if (datas != null && datas.length > 0) {
            disk = new Disk();
            disk.setSize(datas[0]);
            disk.setState(datas[1]);
            disk.setNum(datas[2].substring(0, datas[2].length() - 1));
            dataList.add(disk);
        }
        
        
        diskList = new ArrayList();
        List<PhysicalDiskInformation> dataDisks = pool.getDataDisks();  //数据盘
        List<PhysicalDiskInformation> cacheDisks = pool.getCacheDisks();  //高速缓冲盘
        List<PhysicalDiskInformation> logDisks = pool.getLogDisks();  //日志盘
        List<PhysicalDiskInformation> spareDisks = pool.getSpareDisks();  //热备磁盘
        ArrayList<PhysicalDiskInformation> disks = new ArrayList();  //热备磁盘
        if (dataDisks != null) {
            SystemOutPrintln.print_volume("dataDisks=" + dataDisks.size());
            disks.addAll(dataDisks);
        }
        if (cacheDisks != null) {
            SystemOutPrintln.print_volume("cacheDisks=" + cacheDisks.size());
            disks.addAll(cacheDisks);
        }
        if (logDisks != null) {
            SystemOutPrintln.print_volume("logDisks=" + logDisks.size());
            disks.addAll(logDisks);
        }
        if (spareDisks != null) {
            SystemOutPrintln.print_volume("spareDisks=" + spareDisks.size());
            disks.addAll(spareDisks);
        }

        if (disks != null && disks.size() > 0) {
            for (int j = 0; j < disks.size(); j++) {

                disk = new Disk();
                if (MyConstants.diskModel != PhysicalDiskInformation.DISK_MODEL_SPARE) {
                    disk.setPosition(disks.get(j).cardModel == 2 ? (resources.get("volume.volumegroup", "expansionbox") + disks.get(j).cardIndex) : resources.get("volume.volumegroup", "host"));
                    disk.setNum(disks.get(j).position);
                }
//                    String size ="0.00";
//                    if(disks.get(j).getDiskSize() >0){
//                        size = String.valueOf(disks.get(j).getDiskSize());
//                    }
//                    disk.setSize(size);
                switch (disks.get(j).model) {
                    case PhysicalDiskInformation.DISK_MODEL_DATA:
                        disk.setType(resources.get("volume.volumegroup", "datadisk"));
                        break;
                    case PhysicalDiskInformation.DISK_MODEL_SPARE:
                        disk.setType(resources.get("volume.volumegroup", "sparedisk"));
                        break;
                    case PhysicalDiskInformation.DISK_MODEL_UNUSED:
                        disk.setType(resources.get("volume.volumegroup", "unuseddisk"));
                        break;
                    case PhysicalDiskInformation.DISK_MODEL_CACHE:
                        disk.setType(resources.get("volume.volumegroup", "cachedisk"));
                        break;
                    case PhysicalDiskInformation.DISK_MODEL_LOG:
                        disk.setType(resources.get("volume.volumegroup", "logdisk"));
                        break;
                    default:
                        disk.setType(resources.get("volume.volumegroup", "unknown"));
                        break;
                }
                switch (disks.get(j).state) {
                    case PhysicalDiskInformation.DATA_DISK_STATE_ONLINE:
                        disk.setState(resources.get("volume.volumegroup", "online"));
                        disk.setStateIcon("../resources/volume/picture/normal.png");
                        break;
                    case PhysicalDiskInformation.DATA_DISK_STATE_OFFLINE:
                        disk.setState(resources.get("volume.volumegroup", "offline"));
                        disk.setStateIcon("../resources/volume/picture/normal.png");
                        break;
                    case PhysicalDiskInformation.DATA_DISK_CANT_OPEN:
                        disk.setState(resources.get("volume.volumegroup", "damage"));
                        disk.setStateIcon("../resources/volume/picture/damage.png");
                        break;
                    case PhysicalDiskInformation.SPARE_DISK_STATE_AVAILIBLE:
                        disk.setState(resources.get("volume.volumegroup", "availible"));
                        disk.setStateIcon("../resources/volume/picture/normal.png");
                        break;
                    case PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_SELF:
                        disk.setState(resources.get("volume.volumegroup", "inuseself"));
                        disk.setStateIcon("../resources/volume/picture/normal.png");
                        break;
                    case PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_OTHER:
                        disk.setState(resources.get("volume.volumegroup", "inuseother"));
                        disk.setStateIcon("../resources/volume/picture/normal.png");
                        break;
                    case PhysicalDiskInformation.UNUSED_DISK_STATE:
                        disk.setState(resources.get("volume.volumegroup", "unused"));
                        disk.setStateIcon("../resources/volume/picture/normal.png");
                        break;
                    case PhysicalDiskInformation.DATA_DISK_REMOVED:
                        disk.setState(resources.get("volume.volumegroup", "removedisk"));
                        disk.setStateIcon("../resources/volume/picture/normal.png");
                        break;
                    case PhysicalDiskInformation.SMART_BEING_FAILING:
                        disk.setState(resources.get("volume.volumegroup", "failing"));
                        disk.setStateIcon("../resources/volume/picture/warn.png");
                        break;
                    default:
                        disk.setState(resources.get("volume.volumegroup", "unknown"));
                        disk.setStateIcon("../resources/volume/picture/normal.png");
                        break;
                }
                disk.setDiskName(disks.get(j).diskName);

                if (!pool.getRaidType().equals("RAID0") && disk.getType().equals(resources.get("volume.volumegroup", "datadisk"))) {
                    disk.setNotReplace(false);
                } else {
                    disk.setNotReplace(true);
                }
                if (disk.getType().equals(resources.get("volume.volumegroup", "datadisk"))) {
                    disk.setNotRemove(true);
                } else {
                    disk.setNotRemove(false);
                }
                if (disk.getType().equals(resources.get("volume.volumegroup", "datadisk")) && disk.getState().equals(resources.get("volume.volumegroup", "damage"))) {
                    disk.setNotApart(false);
                } else {
                    disk.setNotApart(true);
                }
                if (userType != 2) {
                    disk.setNotReplace(true);
                    disk.setNotRemove(true);
                    disk.setNotApart(true);
                }

                diskList.add(disk);

            }
        }
    }

    public VolumesInfo getSelectVolume() {
        return selectVolume;
    }

    public void setSelectVolume(VolumesInfo selectVolume) {
        this.selectVolume = selectVolume;
    }

    public List<VolumesInfo> getVolumeList() {
//        getVolumes();
        return volumeList;
    }

    public void setVolumeList(List<VolumesInfo> volumeList) {
        this.volumeList = volumeList;
    }

    public String returnDiskName(String volumeName, String diskname, String state, String size) {
        int statestr = PhysicalDiskInformation.DATA_DISK_STATE_ONLINE;
        if (state.equalsIgnoreCase(resources.get("volume.volumegroup", "online"))) {
            statestr = PhysicalDiskInformation.DATA_DISK_STATE_ONLINE;
        } else if (state.equalsIgnoreCase(resources.get("volume.volumegroup", "offline"))) {
            statestr = PhysicalDiskInformation.DATA_DISK_STATE_OFFLINE;
        } else if (state.equalsIgnoreCase(resources.get("volume.volumegroup", "damage"))) {
            statestr = PhysicalDiskInformation.DATA_DISK_CANT_OPEN;
        } else if (state.equalsIgnoreCase(resources.get("volume.volumegroup", "availible"))) {
            statestr = PhysicalDiskInformation.SPARE_DISK_STATE_AVAILIBLE;
        } else if (state.equalsIgnoreCase(resources.get("volume.volumegroup", "inuseself"))) {
            statestr = PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_SELF;
        } else if (state.equalsIgnoreCase(resources.get("volume.volumegroup", "inuseother"))) {
            statestr = PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_OTHER;
        } else if (state.equalsIgnoreCase(resources.get("volume.volumegroup", "unused"))) {
            statestr = PhysicalDiskInformation.UNUSED_DISK_STATE;
        } else if (state.equalsIgnoreCase(resources.get("volume.volumegroup", "removedisk"))) {
            statestr = PhysicalDiskInformation.DATA_DISK_REMOVED;
        } else if (state.equalsIgnoreCase(resources.get("volume.volumegroup", "failing"))) {
            statestr = PhysicalDiskInformation.SMART_BEING_FAILING;
        } else {
            statestr = 0;  //未知状态
        }
        SystemOutPrintln.print_volume("volumeName=" + volumeName);
        String volumeNameStr = volumeName;
        String diskNameStr = diskname;
        String param = "volumeName=" + volumeNameStr + "&" + "diskName=" + diskNameStr + "&" + "state=" + statestr;

        return "volumegroup_datadiskreplace?faces-redirect=true" + param;
    }

    public String returnAddDiskPage(String volumeName) {
        String param = "volumeName=" + volumeName;
        return "volumegroup_adddisk?faces-redirect=true" + param;
    }
      public String returnCapacityVolumePage(String volumeName,String type) {
        String param = "volumeName=" + volumeName + "&" + "type=" + type;
        return "volumegroup_capacityvolume?faces-redirect=true" + param;
    }

    public String returnAddSysvol() {

        return "volumegroup_createsysvol?faces-redirect=true";
    }

    public void toApartDisk() {
        diskname = selectVolume.selectDisk.getDiskName();
        RequestContext.getCurrentInstance().execute("apartDisk.show()");
    }

    public void toremoveDisk() {
        diskname = selectVolume.selectDisk.getDiskName();
        RequestContext.getCurrentInstance().execute("removeDisk.show()");
    }

    public void deleteVolune() {
        //判断是否可以删除
        selectVolumeName = selectVolume.name;
        SystemOutPrintln.print_volume("deleted pool =" + selectVolume.name);
        if (selectVolume.name.equals("mbapool")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup", "no_mbapool"), "。"));
            return;
        }

        VolumeInterface vol = InterfaceFactory.getVolumeInterfaceInstance();
        String isLinkPool = vol.queryVolumeCanDelelte(selectVolume.name);

        if (null == isLinkPool || "".equalsIgnoreCase(isLinkPool)) {
            return;
        }
        if (ValidateUtility.checkIsLinkPoolStatus(isLinkPool)) {
            boolean diskFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_DISK);
            boolean tapeFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_TAPE);
            boolean vdlFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_VDL);
//            flag = this.adaptee.poolInfo.ti.isEmpty() ? true : false;
//            if(!flag){
//                Constants.showErrorMessage(MyUtility.getString(resourcesPath, "该卷组不为空，不能删除；请先清空该卷组，再删除该卷组", adaptee.serverNode).replaceAll("%", MyUtility.getString(resourcesPath, "VDL区", adaptee.serverNode)));
//                return;
//            }
            boolean vmFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_VM);
            boolean cdpFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_CDP);
            boolean nasFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_NAS);

            if (diskFlag == true || tapeFlag == true || vdlFlag == true || vmFlag == true || cdpFlag == true || nasFlag == true) {
                String flag = "";
                if (diskFlag || cdpFlag) {
                    flag = flag + resources.get("volume.volumegroup", "disk");
                    if (tapeFlag || vdlFlag || vmFlag || nasFlag) {
                        flag = flag + "、";
                    }
                }
                if (tapeFlag) {
                    flag = flag + resources.get("volume.volumegroup", "tape");
                    if (vdlFlag || vmFlag || nasFlag) {
                        flag = flag + "、";
                    }
                }
                if (vdlFlag) {
                    flag = flag + resources.get("volume.volumegroup", "vdl");
                    if (vmFlag || nasFlag) {
                        flag = flag + "、";
                    }
                }
                if (vmFlag) {
                    flag = flag + resources.get("volume.volumegroup", "vm");
                    if (nasFlag) {
                        flag = flag + "、";
                    }
                }
//                if(cdpFlag){
//                    flag= flag+resources.get("volume.volumegroup", "cdp")+" ";
//                }
                if (nasFlag) {
                    flag = flag + resources.get("volume.volumegroup", "nas");
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup", "volumetip1") + flag + resources.get("volume.volumegroup", "volumetip2"), ""));
                return;
            }

        }
        int iRet = 0;
        if (selectVolume.name.equalsIgnoreCase("SYSVOL")) {
            RequestContext.getCurrentInstance().execute("deletepool1.show()");
            return;
            // iRet = Constants.showQuestionMessage(MyUtility.getString(resourcesPath, "该操作可能造成数据丢失，且需要重启服务器，非专业人员不建议操作，是否继续？", adaptee.serverNode));
        } else {
            RequestContext.getCurrentInstance().execute("deletepool2.show()");
            return;
            // iRet = Constants.showQuestionMessage(MyUtility.getString(resourcesPath, "确定要删除卷组吗？", adaptee.serverNode));
        }

//        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
//        boolean des = volumeIn.destroyVolumGroup(selectVolume.name);
//        if (des == false) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除卷组失败", "。"));
//            return;
//        }
//        if (selectVolume.name.equalsIgnoreCase("syspool")) {
//            CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
//            boolean reboot = common.reboot();
//            if(reboot == false){
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "重启系统失败", "。"));
//            }
//        }
//        volumeList.remove(selectVolume);
    }

    public String deleteVolune_real() {
        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();

        SystemOutPrintln.print_volume("deleted pool =" + selectVolume.name);
        boolean des = volumeIn.destroyVolumGroup(selectVolume.name);
        if (des == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup", "deletevolumetip"), ""));
            return null;
        }
        SystemOutPrintln.print_volume("deleted pool succeed");
        if (selectVolume.name.equalsIgnoreCase("syspool")) {
            CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
            boolean reboot = common.reboot();
            if (reboot == false) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup", "reboottip"), ""));
            }
        }
        return "volumegroup?faces-redirect=true";
    }

    public void clearVolune() {
        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        boolean des = volumeIn.zpoolClear(selectVolume.name);
        if (des) {
            this.getVolumes();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, resources.get("volume.volumegroup", "clearvolume") + selectVolume.name + resources.get("volume.volumegroup", "succeed"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup", "clearvolume") + selectVolume.name + resources.get("volume.volumegroup", "fail"), ""));

        }
    }

    public void importVolune() {
        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        boolean des = volumeIn.importVolume(selectVolume.name);
        if (des) {
            this.getVolumes();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, resources.get("volume.volumegroup", "importpool") + selectVolume.name + resources.get("volume.volumegroup", "succeed"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup", "importpool") + selectVolume.name + resources.get("volume.volumegroup", "fail"), ""));

        }
    }

    public void fixVolume() {
// 已添加       int iRet = Constants.showQuestionMessage("该操作将尝试修复卷组中的错误数据，时间较长，并且可能影响其他操作，是否继续？");
//                if(iRet != JOptionPane.OK_OPTION){
//                    return;
//                }
        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        boolean fix = volumeIn.zpoolScrub(selectVolume.name);
        if (fix) {
            this.getVolumes();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, resources.get("volume.volumegroup", "fixvolume") + selectVolume.name + resources.get("volume.volumegroup", "succeed"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup", "fixvolume") + selectVolume.name + resources.get("volume.volumegroup", "fail"), ""));
        }
    }

    public String apartDisk() {  //分离磁盘
        SystemOutPrintln.print_volume("detach selectVolume.getName()=" + selectVolume.getName() + ",selectVolume.selectDisk.getDiskName()=" + selectVolume.selectDisk.getDiskName());

//已添加        if (0 != Constants.showQuestionMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/DetachDiskDialog").getString("该操作将从卷组中分离此磁盘，确认操作？"))) {
//            return;
//        }
        String volumeName = selectVolume.getName();
        String diskname = selectVolume.selectDisk.getDiskName();
        SystemOutPrintln.print_volume("detach volumeName=" + volumeName + ",diskname=" + diskname);
        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        boolean detach = volumeIn.detachDisk(volumeName, diskname);
        if (detach == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup", "apartdisktip") + diskname + resources.get("volume.volumegroup", "fail"), ""));
        }
        SystemOutPrintln.print_volume("detach disk");
        return "volumegroup?faces-redirect=true";
    }

    public String removeDisk() {  //移除磁盘
        SystemOutPrintln.print_volume("removeDisk selectVolume.getName()=" + selectVolume.getName() + ",selectVolume.selectDisk.getDiskName()=" + selectVolume.selectDisk.getDiskName() + ",selectVolume.selectDisk.getType()=" + selectVolume.selectDisk.getType());
        String type = selectVolume.selectDisk.getType();
        String volumeName = selectVolume.getName();
        String diskname = selectVolume.selectDisk.getDiskName();
        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        SystemOutPrintln.print_volume("移除磁盘  " + "volumeName=" + volumeName + ",diskname=" + diskname);
        if (type.equalsIgnoreCase(resources.get("volume.volumegroup", "sparedisk"))) {  //移除热备盘
            boolean spareDisk = volumeIn.removeSpare(volumeName, diskname);
            SystemOutPrintln.print_volume("热备盘 = " + spareDisk);
            if (spareDisk == false) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup", "removesparedisk") + diskname + resources.get("volume.volumegroup", "fail"), ""));
            }
        } else if (type.equalsIgnoreCase(resources.get("volume.volumegroup", "cachedisk"))) {  //移除高速缓冲盘
            boolean cacheDisk = volumeIn.removeCache(volumeName, diskname);
            SystemOutPrintln.print_volume("读缓冲固态盘 = " + cacheDisk);
            if (cacheDisk == false) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup", "removecachedisk") + diskname + resources.get("volume.volumegroup", "fail"), ""));
            }
        } else if (type.equalsIgnoreCase(resources.get("volume.volumegroup", "logdisk"))) {  //移除索引盘（即日志盘）
            boolean logDisk = volumeIn.removeLog(volumeName, diskname);
            SystemOutPrintln.print_volume("写缓冲固态盘 = " + logDisk);
            if (logDisk == false) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup", "removelogdisk") + diskname + resources.get("volume.volumegroup", "fail"), ""));
            }
        }

        return "volumegroup?faces-redirect=true";

    }

    public List<Disk> getDiskList() {
        return diskList;
    }

    public void setDiskList(List<Disk> diskList) {
        this.diskList = diskList;
    }

    public String getSelectVolumeName() {
        return selectVolumeName;
    }

    public void setSelectVolumeName(String selectVolumeName) {
        this.selectVolumeName = selectVolumeName;
    }

    public String getDiskname() {
        return diskname;
    }

    public void setDiskname(String diskname) {
        this.diskname = diskname;
    }

    public List<Disk> getDataList() {
        return dataList;
    }

    public void setDataList(List<Disk> dataList) {
        this.dataList = dataList;
    }

   

}
