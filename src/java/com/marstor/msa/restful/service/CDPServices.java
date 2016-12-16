/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.service;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.bean.CdpLogRecord;
import com.marstor.msa.cdp.bean.CdpRollbackTaskInfo;
import com.marstor.msa.cdp.bean.CdpSnapshotInfo;
import com.marstor.msa.cdp.bean.DataBase;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
import com.marstor.msa.restful.cdp.bean.CdpDisk;
import com.marstor.msa.restful.cdp.bean.CdpDiskGroup;
import com.marstor.msa.restful.cdp.bean.CdpRecord;
import com.marstor.msa.restful.cdp.bean.CdpRollbackTask;
import com.marstor.msa.restful.cdp.bean.CdpSnapshot;
import com.marstor.msa.restful.cdp.bean.Disk;
import com.marstor.msa.scsi.util.Debug;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
public class CDPServices {

    private MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
    private String error;

    public String getError() {
        return error;
    }

    public CdpDiskGroup createDiskGroup(String volumeName, String diskGroupName) {
        String guid = cdp.createDiskGroup(volumeName, diskGroupName);
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(guid);
        CdpDiskGroup group = new CdpDiskGroup(origin);
        if (origin == null) {
            this.error = cdp.getError();
        }
        return group;
    }

    public boolean deleteDiskGroup(String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        result = cdp.deleteDiskGroup(diskGroupGuid, origin.getDiskGroupPath());
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public List<CdpDiskGroup> getDiskGroups() {
        List<CdpDiskGroupInfo> origins = cdp.getDiskGroupInfos();
        List<CdpDiskGroup> groups = new ArrayList();
        if (origins == null) {
            this.error = cdp.getError();
            return null;
        }
        if (!origins.isEmpty()) {
            for (CdpDiskGroupInfo origin : origins) {
                CdpDiskGroup group = new CdpDiskGroup(origin);
                groups.add(group);
            }
        }
        return groups;
    }

    public CdpDiskGroup getDiskGroup(String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        if (origin == null) {
            this.error = cdp.getError();
            return null;
        }
        CdpDiskGroup group = new CdpDiskGroup(origin);
        return group;
    }

    public CdpDiskGroup createDiskJoinGroup(String diskGroupGuid,
            String size, boolean demand, String name) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        if (origin == null) {
            this.error = cdp.getError();
            return null;
        }

        CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
        Reg[] license = commonInterfaceInstance.getLicense();
        List<CdpDiskGroupInfo> diskGroups = cdp.getDiskGroupInfos();
        if (diskGroups == null) {
            this.error = cdp.getError();
            return null;
        }
        long registered = 0;
        long used = 0;
        for (Reg license1 : license) {
            if (Module.MODULE_CDP == license1.getModuleID() && Module.FUNCTIONID_CDP_CAPACITY == license1.getFunctionID()) {
                for (CdpDiskGroupInfo g : diskGroups) {
                    if (g.getDiskGroupGuid().equals(origin.getDiskGroupGuid())) {
                        continue;
                    }
                    if (g.getProtectType() == CDPConstants.PROTECT_RECORD) {
                        used = used + g.getTotalDiskSize() + g.getLogPolicy().getLogSize();
                    }
                }
                registered = license1.getFunctionNumber();
                break;
            }
        }

        long registerSize = registered * 1024L * 1024L * 1024L * 1024L;
        long avialableSize = registerSize - used;
        long creating = Integer.parseInt(size) * com.marstor.msa.cdp.mirrorhost.CDPConstants.GB;
        if (creating > avialableSize) {
            this.error = "capacity exceeds limit";
            return null;
        }

        String diskName = cdp.createDiskJoinGroup(diskGroupGuid, origin.getDiskGroupPath(), size, demand, name);
        if(diskName == null){
            this.error = cdp.getError();
            return null;
        }
        origin = cdp.getDiskGroupInfo(diskGroupGuid);
        if (origin == null) {
            this.error = cdp.getError();
            return null;
        }
        CdpDiskGroup group = new CdpDiskGroup(origin);
        return group;
    }

    public boolean deleteDiskFromGroup(String diskGuid, String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        CdpDiskInfo[] disks = cdp.getDiskInfos(diskGroupGuid, origin.getDiskGroupPath());
        if (disks == null) {
            this.error = cdp.getError();
            return result;
        }
        String diskName = "";
        if (disks.length != 0) {
            for (CdpDiskInfo disk : disks) {
                if (disk.getDiskGuid().equals(diskGuid)) {
                    diskName = disk.getDiskName();
                }
            }
        }
        result = cdp.deleteDiskFromGroup(diskGuid, diskName, diskName);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public CdpDisk[] getDisks(String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        if (origin == null) {
            this.error = cdp.getError();
            return null;
        }
        CdpDiskInfo[] origins = cdp.getDiskInfos(diskGroupGuid, origin.getDiskGroupPath());
        if (origins == null) {
            this.error = cdp.getError();
            return null;
        }
        CdpDisk[] disks = new CdpDisk[0];
        if (origins.length != 0) {
            disks = new CdpDisk[origins.length];
            for (int i = 0; i < origins.length; i++) {
                disks[i] = new CdpDisk(origins[i]);
            }
        }
        return disks;
    }

    public boolean startCDP(String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        String diskGroupPath = origin.getDiskGroupPath();
        if (origin.getLogPolicy() == null) {
            this.error = "Log Policy is NULL!";
            return result;
        }
        int logFullOption = origin.getLogPolicy().getLogFullOption();


        result = cdp.startCDP(diskGroupGuid, diskGroupPath, logFullOption);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public boolean stopCDP(String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        String diskGroupPath = origin.getDiskGroupPath();
        int iProtectType = origin.getProtectType();
        result = cdp.stopCDP(diskGroupGuid, diskGroupPath, iProtectType);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public int getQuerySnapshotHandle(String diskGroupGuid, long snapshotID) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        int ret = -1;
        if (origin == null) {
            this.error = cdp.getError();
            return ret;
        }
        String zfsName = origin.getDiskZfsName();
        ret = cdp.getQuerySnapshotHandle(diskGroupGuid, snapshotID, zfsName);
        if (ret == -1) {
            this.error = cdp.getError();
        }
        return ret;
    }

    public boolean closeQuerySnapshotHandle(int queryHandle) {
        boolean result = cdp.closeQuerySnapshotHandle(queryHandle);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public CdpSnapshot[] querySnapshotInfos(String diskGroupGuid,
            int queryHandle, int queryCount, boolean forwardSearch, boolean reverse, int currentCount) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        if (origin == null) {
            this.error = cdp.getError();
            return null;
        }
        String diskGroupPath = origin.getDiskGroupPath();
        int forward = forwardSearch ? 1 : 0;
        int rever = reverse ? 1 : 0;
        CdpSnapshotInfo[] infos = cdp.querySnapshotInfos(diskGroupPath, queryHandle,
                queryCount, forward, rever, currentCount);
        if (infos == null) {
            this.error = cdp.getError();
            return null;
        }
        CdpSnapshot[] ret = new CdpSnapshot[0];
        if (infos.length != 0) {
            ret = new CdpSnapshot[infos.length];
            for (int i = 0; i < infos.length; i++) {
                ret[i] = new CdpSnapshot(infos[i]);
            }
        }
        return ret;
    }

    public int getQueryLogRecordHandle(String diskGroupGuid, long snapshotID) {
        int ret = cdp.getQueryLogRecordHandle(diskGroupGuid, snapshotID);
        if (ret == -1) {
            this.error = cdp.getError();
        }
        return ret;
    }

    public boolean closeQueryLogRecordHandle(int queryHandle) {
        boolean result = cdp.closeQueryLogRecordHandle(queryHandle);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public CdpRecord[] queryLogRecordInfos(String diskGroupGuid, int queryHandle,
            int queryCount, boolean forwardSearch, boolean reverse, int currentCount) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        if (origin == null) {
            this.error = cdp.getError();
            return null;
        }
        String diskGroupPath = origin.getDiskGroupPath();
        int forward = forwardSearch ? 1 : 0;
        int rever = reverse ? 1 : 0;
        CdpLogRecord[] infos = cdp.queryLogRecordInfos(diskGroupPath, queryHandle,
                queryCount, forward, rever, currentCount);
        if (infos == null) {
            this.error = cdp.getError();
            return null;
        }
        CdpRecord[] ret = new CdpRecord[0];
        if (infos.length != 0) {
            ret = new CdpRecord[infos.length];
            for (int i = 0; i < infos.length; i++) {
                ret[i] = new CdpRecord(infos[i]);
            }
        }
        return ret;
    }

    public boolean setLogPolicy(String diskGroupGuid,
            long logSize, int logKeepTime, int logFullOption, long snapshotInterval) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        result = cdp.setProtectType(origin.getDiskZfsName(), 2);
        if (!result) {
            this.error = cdp.getError();
            return result;
        }
        List<DataBase> dbList = new ArrayList();
        result = cdp.setDefaultAgent(diskGroupGuid, snapshotInterval, dbList);
        if (!result) {
            this.error = cdp.getError();
            return result;
        }

        String diskGroupPath = origin.getDiskGroupPath();
        result = cdp.setLogPolicy(diskGroupPath, diskGroupGuid, logSize * 1024L * 1024L * 1024L, logKeepTime, logFullOption);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public boolean online(String diskGroupPath) {
        boolean result = cdp.online(diskGroupPath);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public boolean offline(String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        String diskGroupPath = origin.getDiskGroupPath();
        result = cdp.offline(diskGroupGuid, diskGroupPath);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public boolean onlineLU(String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        String diskGroupName = origin.getDiskGroupName();
        CdpDiskInfo[] origins = cdp.getDiskInfos(diskGroupGuid, origin.getDiskGroupPath());
        if (origins == null) {
            this.error = cdp.getError();
            return result;
        }
        String[] lu = new String[0];
        if (origins.length != 0) {
            lu = new String[origins.length];
            for (int i = 0; i < origins.length; i++) {
                lu[i] = origins[i].getLuInfoBean().getlUName();
            }
        }
        result = cdp.onlineLU(diskGroupName, lu);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public boolean offlineLU(String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        String diskGroupName = origin.getDiskGroupName();
        CdpDiskInfo[] origins = cdp.getDiskInfos(diskGroupGuid, origin.getDiskGroupPath());
        if (origins == null) {
            this.error = cdp.getError();
            return result;
        }
        String[] lu = new String[0];
        if (origins.length != 0) {
            lu = new String[origins.length];
            for (int i = 0; i < origins.length; i++) {
                lu[i] = origins[i].getLuInfoBean().getlUName();
            }
        }
        result = cdp.offlineLU(diskGroupName, lu);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public boolean cdpRollback(String diskGroupGuid, Date rollbackTime) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        String diskGroupPath = origin.getDiskGroupPath();
        result = cdp.cdpRollback(diskGroupPath, diskGroupGuid, 0, rollbackTime);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public CdpRollbackTask getRollbackInfo(String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        if (origin == null) {
            this.error = cdp.getError();
            return null;
        }
        String diskGroupPath = origin.getDiskGroupPath();
        CdpRollbackTaskInfo info = cdp.getRollbackInfo(diskGroupGuid, diskGroupPath);
        if (info == null) {
            this.error = cdp.getError();
            return null;
        }
        CdpRollbackTask ret = new CdpRollbackTask(info);
        return ret;
    }

    public boolean saveRollback(String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        String diskGroupPath = origin.getDiskGroupPath();
        result = cdp.saveRollback(diskGroupPath);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public boolean cancelRollback(String diskGroupGuid) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        String diskGroupPath = origin.getDiskGroupPath();
        result = cdp.cancelRollback(diskGroupPath);
        if (!result) {
            this.error = cdp.getError();
        }
        return result;
    }

    public boolean mappingDiskGroup(String hostGroupName, String diskGroupGuid, List<Disk> disks) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        if (disks == null) {
            this.error = "Param disks is null!";
            return result;
        }
        if (disks.isEmpty()) {
            this.error = "Param disks is empty!";
            return result;
        }
        CdpDiskInfo[] origins = cdp.getDiskInfos(diskGroupGuid, origin.getDiskGroupPath());
        if (origins == null) {
            this.error = cdp.getError();
            return result;
        }
        if (origins.length != 0) {
            for (CdpDiskInfo disk : origins) {
                for (Disk d : disks) {
                    if (d.getDisk_guid().equals(disk.getDiskGuid())) {
                        result = scsi.addView(hostGroupName, d.getLun(), disk.getLuInfoBean().getlUName());
                        if (!result) {
                            this.error = scsi.getLastError();
                            return result;
                        }
                        break;
                    }
                }
            }
        }
        return result;
    }

    public boolean unmappingDiskGroup(String hostGroupName, String diskGroupGuid, List<Disk> disks) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(diskGroupGuid);
        boolean result = false;
        if (origin == null) {
            this.error = cdp.getError();
            return result;
        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        if (disks == null) {
            this.error = "Param disks is null!";
            return result;
        }
        if (disks.isEmpty()) {
            this.error = "Param disks is empty!";
            return result;
        }
        CdpDiskInfo[] origins = cdp.getDiskInfos(diskGroupGuid, origin.getDiskGroupPath());
        if (origins == null) {
            this.error = cdp.getError();
            return result;
        }

        for (Disk disk : disks) {
            for (CdpDiskInfo d : origins) {
                if (d.getDiskGuid().equals(disk.getDisk_guid())) {
                    ViewInformation[] views = scsi.getLUNView(d.getLuInfoBean().getlUName());
                    if (views == null) {
                        this.error = "views is null!";
                        return result;
                    }
                    if (views.length == 0) {
                        this.error = "views is empty!";
                        return result;
                    }
                    int entry;
                    Debug.print("Views Size:" + views.length);
                    for (ViewInformation view : views) {
                        Debug.print("View Host Group Name:" + view.getHostGroupName());
                        if (view.getHostGroupName().equals(hostGroupName)) {
                            entry = view.getEntry();
                            result = scsi.removeView(d.getLuInfoBean().getlUName(), entry);
                            if (!result) {
                                this.error = scsi.getLastError();
                                return result;
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return result;
    }

    public long getAppointedTimeSnapshotID(String guid, long time) {
        CdpDiskGroupInfo origin = cdp.getDiskGroupInfo(guid);
        if (origin == null) {
            this.error = cdp.getError();
            return -2;
        }
        String path = origin.getDiskGroupPath();
        long id = cdp.getAppointedTimeSnapshotID(guid, path, time);
        if(id == -1){
            this.error = cdp.getError();
            return -2;
        }
        return id;
    }
}
