/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.service;

import com.marstor.msa.common.bean.PhysicalDiskInformation;

import com.marstor.msa.common.bean.VolumeGroupInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyConstants;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
import com.marstor.msa.restful.volume.bean.VolumeDisk;
import com.marstor.msa.restful.cdp.bean.DisksRes;
import com.marstor.msa.restful.volume.bean.VolumeGroup;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class VolumeService {

    private VolumeInterface volumeSer = InterfaceFactory.getVolumeInterfaceInstance();
    private String error;
    private MSAResource resources = new MSAResource();

    /**
     * 获取火星舱提供的未用磁盘
     *
     * @return 失败null
     */
    public VolumeDisk[] getUnusedDisk() {

        List<VolumeDisk> diskList = new ArrayList();

        PhysicalDiskInformation[] diskInformation = volumeSer.getAllPhysicalDisk();
        if (diskInformation == null || diskInformation.length == 0) {
            this.setError("Not have disks.");
            return null;
        }
        VolumeDisk diskBean = null;
        for (int i = 0; i < diskInformation.length; i++) {
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_UNUSED) {
                diskBean = new VolumeDisk();
                if (MyConstants.diskModel != PhysicalDiskInformation.DISK_MODEL_SPARE) {
                   // String postion = diskInformation[i].cardModel == 2 ? (resources.get("volume.volumegroup_createvolume", "extend") + diskInformation[i].cardIndex) : resources.get("volume.volumegroup_createvolume", "host");
                   
//                    if (postion == null || postion.equals("")) {
//                        diskBean.setDisk_position("--");
//                    } else {
//                        diskBean.setDisk_position(postion);
//                    }
                    String num = diskInformation[i].position;
                    if (num == null || num.equals("")) {
                        diskBean.setDisk_num("--");
                    } else {
                        diskBean.setDisk_num(num);
                    }
                    diskBean.setDisk_size(diskInformation[i].diskSize + "");
                    diskBean.setDisk_name(diskInformation[i].diskName);
                    diskList.add(diskBean);

                } else {

                    diskBean.setDisk_size(diskInformation[i].diskSize + "");
                    diskBean.setDisk_name(diskInformation[i].diskName);
//                    diskBean.setDisk_position("--");
                    diskBean.setDisk_num("--");
                    diskList.add(diskBean);
                }

            }
        }
        VolumeDisk[] unusedDisks = null;
        if (diskList != null && diskList.size() > 0) {
            unusedDisks = new VolumeDisk[diskList.size()];
            for (int j = 0; j < diskList.size(); j++) {
                unusedDisks[j] = diskList.get(j);
            }
        }else{
            unusedDisks = new VolumeDisk[1];
            
        }

        return unusedDisks;

    }

    /**
     * 创建火星舱卷组
     * @param groupName 卷组名称,卷组名须由字母为首的字母和数字组合而成且不能超过16位。
     * @param type 卷组类型,RAID5、RAID6、RAID5+0、RAID6+0、RAID0、RAID1、三倍校验RAIDz3
     * @param selDisks 选择的磁盘
     * @return
     */
    public VolumeGroup createVolume(String groupName, String type, List<DisksRes> selDisks) {

        long usedDiskCapacity = 0L;
        PhysicalDiskInformation[] diskInformation = volumeSer.getAllPhysicalDisk();
        if (diskInformation == null || diskInformation.length == 0) {
            this.setError("Not have system disks.");
            return null;
        }
        for (int i = 0; i < diskInformation.length; i++) {
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_UNUSED) {
            } else {
                usedDiskCapacity += diskInformation[i].diskSize;
            }
        }

        long selectedSize = 0;
        String[] disknames = null;
        if (selDisks!=null && selDisks.size() > 0) {
            disknames = new String[selDisks.size()];
        }

        if (groupName.trim().equals("")
                || groupName.trim().equals("mbapool")
                || groupName.trim().toLowerCase().startsWith("raid")
                || groupName.trim().toLowerCase().startsWith("mirror")
                || !ValidateUtility.checkPoolName(groupName.trim())) {
            if (!ValidateUtility.checkPoolName(groupName.trim())) {
                this.setError("Invalid format of volume group name."); //卷组名须由字母为首的字母和数字组合而成且不能超过16位。
            } else {
                this.setError("Invalid format of volume group name.");
            }
            return null;
        }
        VolumeGroupInformation[] pools = volumeSer.getAllVolumeGroup();
        if (pools != null && pools.length > 0) {
            for (int k = 0; k < pools.length; k++) {
                if (groupName.trim().equalsIgnoreCase(pools[k].getName())) {
                    this.setError("The volume group name already exists.");
                    return null;
                }
            }
        }

        if (selDisks==null || selDisks.size() < 1) {
            this.setError("Not select disk.");
            return null;
        }

        for (int i = 0; i < selDisks.size(); i++) {
            for (PhysicalDiskInformation diskInformation1 : diskInformation) {
                if (diskInformation1.model == PhysicalDiskInformation.DISK_MODEL_UNUSED) {
                    if (diskInformation1.getDiskName().equals(selDisks.get(i).getDisk().trim())) {
                        selectedSize += Long.valueOf(diskInformation1.getDiskSize());
                        disknames[i] = selDisks.get(i).getDisk();
                        break;
                    }
                }
            }
        }
        if(disknames == null || disknames.length==0){
            this.setError("Select the disk errors.");
            return null;
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
                maxSize = reg.getFunctionNumber();
                unitResourceID = String.valueOf(reg.getUnitResourceID());
                flag = true;
                break;
            }

        }
        if (!flag) {
            this.setError("Failed to get the total storage capacity.");
            return null;
        }
        if (usedDiskCapacity + selectedSize > maxSize * 1024) {
            this.setError("The selected disk capacity must be less than " + maxSize + "TB.");
            return null;
        }
        int PoolType = 0;
        String typeStr = "RAID5";
        if (type.equals("RAID5")) {
            typeStr = "RAID5";
            PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID5;
        } else if (type.equals("RAID6")) {
            typeStr = "RAID6";
            PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID6;
        } else if (type.equals("RAID5+0")) {
            typeStr = "RAID5+0";
            PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID5_0;
        } else if (type.equals("RAID6+0")) {
            typeStr = "RAID6+0";
            PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID6_0;
        } else if (type.equals("RAID0")) {
            typeStr = "RAID0";
            PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID0;
        } else if (type.equals("RAID1")) {
            typeStr = "RAID1";
            PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAID1;
        } else if (type.equals("RAIDz3")) {
            typeStr = "三倍校验RAIDz3";
            PoolType = VolumeGroupInformation.ZPOOL_TYPE_RAIDz3;
        }

        switch (PoolType) {
            case 1:
                if (selDisks.size() < 3) {
                    this.setError("Choose at least three disks.");
                    return null;
                }
                break;
            case 2:
                if (selDisks.size() < 4) {
                    this.setError("Choose at least four disks.");
                    return null;
                }
                break;
            case 3:
                if (selDisks.size() < 6) {
                    this.setError("Choose at least six disks.");
                    return null;
                }
                if (selDisks.size() % 2 != 0) {
                    this.setError("Need an even number of disks.");
                    return null;
                }
                break;
            case 4:
                if (selDisks.size() < 1) {
                    this.setError("Select at least one disk");
                    return null;
                }
                break;
            case 5:
                if (selDisks.size() < 8) {
                    this.setError("Choose at least eight disks");
                    return null;
                }
                break;
            case 6:
                if (selDisks.size() < 6) {
                    this.setError("Choose at least six disks");
                    return null;
                }
                break;
            case 7:
                if (selDisks.size() != 2) {
                    this.setError("You must select two disks.");
                    return null;
                }
                break;
            default:
                this.setError("Number of disks is not enough.");

                break;
        }

        VolumeGroupInformation pool = null;
        boolean creat = volumeSer.createVolumeGroup(PoolType, groupName.trim(), disknames);
        if (creat == false) {
            this.setError("Create a volume group failed.");
        } else {
            VolumeGroupInformation[] allPool = volumeSer.getAllVolumeGroup();
            if (allPool != null && allPool.length > 0) {
                for (VolumeGroupInformation volumeGroup : allPool) {
                    if (groupName.trim().equalsIgnoreCase(volumeGroup.getName())) {
                        pool = volumeGroup;
                        break;
                    }
                }
            }

        }
        VolumeGroup group = null;
        if (pool != null) {
            group = new VolumeGroup();
            group.setVolume_name(pool.getName());
            int groupType = pool.getPoolType();
            String typeStr1 = "";
            if (groupType ==VolumeGroupInformation.ZPOOL_TYPE_RAID5) {
                typeStr1 = "RAID5";
                //VolumeGroupInformation.ZPOOL_TYPE_RAID5;
            } else if (groupType ==VolumeGroupInformation.ZPOOL_TYPE_RAID6) {
                typeStr1 = "RAID6";
                //VolumeGroupInformation.ZPOOL_TYPE_RAID6;
            } else if (groupType ==VolumeGroupInformation.ZPOOL_TYPE_RAID5_0) {
                typeStr1 = "RAID5+0";
                //VolumeGroupInformation.ZPOOL_TYPE_RAID5_0;
            } else if (groupType ==VolumeGroupInformation.ZPOOL_TYPE_RAID6_0) {
                typeStr1 = "RAID6+0";
                //VolumeGroupInformation.ZPOOL_TYPE_RAID6_0;
            } else if (groupType ==VolumeGroupInformation.ZPOOL_TYPE_RAID0) {
                typeStr1 = "RAID0";
                //VolumeGroupInformation.ZPOOL_TYPE_RAID0;
            } else if (groupType ==VolumeGroupInformation.ZPOOL_TYPE_RAID1) {
                typeStr1 = "RAID1";
                //VolumeGroupInformation.ZPOOL_TYPE_RAID1;
            } else if (groupType ==VolumeGroupInformation.ZPOOL_TYPE_RAIDz3) {
                typeStr1 = "RAIDz3";
                //VolumeGroupInformation.ZPOOL_TYPE_RAIDz3;
            }

            group.setVolume_type(typeStr1);
            group.setVolume_state(pool.getPoolState());
            group.setVolume_size(pool.getSize());
            group.setVolume_used_size(pool.getUsedSize());
            group.setVolume_available_size(pool.getAvailableSize());
        }

        return group;

    }
    
     /**
     * 删除火星舱卷组
     *
     * @param groupName 卷组名
     * @return
     */
    public boolean deleteVolune(String groupName) {
        //判断是否可以删除
        String volumeName = null;
        if (groupName == null || groupName.trim().equals("")) {
            this.setError("volume name false.");
            return false;
        }
        VolumeGroupInformation[] pools = volumeSer.getAllVolumeGroup();

        if (pools == null) {
            return false;
        }
        for (int i = 0; i < pools.length; i++) {
            if (groupName.equals(pools[i].name)) {
                volumeName = groupName;
                if (MyConstants.SYS_VOL.equalsIgnoreCase(volumeName)) {
                    this.setError("The SYSVOL volume can not delete.");
                    return false;
                }
                break;
            }

        }

        List<String> exportVolumes = volumeSer.getExportVolume();

        if (exportVolumes != null) {
            for (int k = 0; k < exportVolumes.size(); k++) {
                if (groupName.equals(exportVolumes.get(k))) {
                    this.setError("The volume can not delete.");
                    return false;
                }

            }
        }

        if (volumeName == null) {
            this.setError("This volume is not exit.");
            return false;
        }


        VolumeInterface vol = InterfaceFactory.getVolumeInterfaceInstance();
        String isLinkPool = vol.queryVolumeCanDelelte(groupName);

        if (null == isLinkPool || "".equalsIgnoreCase(isLinkPool)) {
            this.setError("The volume can not delete.");
            return false;
        }
        if (ValidateUtility.checkIsLinkPoolStatus(isLinkPool)) {
            boolean diskFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_DISK);
            boolean tapeFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_TAPE);
            boolean vdlFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_VDL);

            boolean vmFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_VM);
            boolean cdpFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_CDP);
            boolean nasFlag = !ValidateUtility.checkWhichPoolBeNotLink(isLinkPool, ValidateUtility.POOL_LINK_NAS);

            if (diskFlag == true || tapeFlag == true || vdlFlag == true || vmFlag == true || cdpFlag == true || nasFlag == true) {
                String flag = "";
                if (diskFlag || cdpFlag) {
                    flag = flag + "CDP";
                    if (tapeFlag || vdlFlag || vmFlag || nasFlag) {
                        flag = flag + "、";
                    }
                }
                if (tapeFlag) {
                    flag = flag + "VTL";
                    if (vdlFlag || vmFlag || nasFlag) {
                        flag = flag + "、";
                    }
                }
                if (vdlFlag) {
                    flag = flag + "Backup";
                    if (vmFlag || nasFlag) {
                        flag = flag + "、";
                    }
                }
                if (vmFlag) {
                    flag = flag + "VM";
                    if (nasFlag) {
                        flag = flag + "、";
                    }
                }
                if (nasFlag) {
                    flag = flag + "NAS";
                }
                this.setError("The volume group on the function of " + flag + "data, cannot be deleted, please delete again after clearing in each function.");

                return false;
            }

        }

        boolean des = volumeSer.destroyVolumGroup(groupName);
        if (des == false) {
            this.setError("Delete volume fail.");
            return false;
        }
        if (groupName.equalsIgnoreCase("syspool")) {
            CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
            boolean reboot = common.reboot();
            if (reboot == false) {
                this.setError("Reboot fail.");
                return false;
            }
        }
        return true;

    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
