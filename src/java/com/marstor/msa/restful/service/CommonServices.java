/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.service;

import com.marstor.msa.common.bean.DiskStatistics;
import com.marstor.msa.common.bean.NTPStatistic;
import com.marstor.msa.common.bean.SystemInformation;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.parameter.EmailParameter;
import com.marstor.msa.restful.cdp.bean.Disk;
import com.marstor.msa.restful.common.bean.Email;
import com.marstor.msa.restful.common.bean.NTP;
import com.marstor.msa.restful.common.bean.SystemInfo;
import com.marstor.msa.restful.common.bean.SystemUser;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author Administrator
 */
public class CommonServices {

    private CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
    private String error;

    public String getError() {
        return error;
    }

    public SystemUser login(String userName, String password, int flag) {
        SystemUserInformation origin = common.login(userName, password, flag);
        SystemUser group = new SystemUser(origin);
        if (origin == null) {
            this.error = "";
        }
        return group;
    }

    public Date getSystemTime() {
        Date date = common.getSystemTime();
        if (date == null) {
            this.error = "";
        }
        return date;
    }

    public boolean setSystemTime(long time) {
        Date date = new Date(time);
        boolean result = common.setSystemTime(date);
        if (!result) {
            this.error = "";
        }
        return result;
    }

    public NTP getNTP() {
        NTPStatistic origin = common.getNTP();
        if (origin == null) {
            this.error = "";
            return null;
        }
        NTP ntp = new NTP(origin);
        return ntp;
    }

    public boolean setNTP(NTP ntp) {
        NTPStatistic data = new NTPStatistic();
        data.bEnable = ntp.isEnabled();
        data.servers = ntp.getServers();
        boolean result = common.setNTP(data);
        if (!result) {
            this.error = "";
        }
        return result;
    }

    public boolean testNTP(String ntpHost){
        boolean result = common.testNTP(ntpHost);        
        if (!result) {
            this.error = "";
        }
        return result;
    }

    public SystemInfo getSystemInfo() {
        SystemInformation origin = common.getSystemInfo();
        if (origin == null) {
            this.error = "";
            return null;
        }
        SystemInfo info = new SystemInfo(origin);
        return info;
    }

    public String getHostName() {
        return common.getHostName();
    }

    public boolean setHostName(String hostname) {        
        boolean result = common.setHostName(hostname);
        if (!result) {
            this.error = "";
        }
        return result;
    }

    public SystemUser[] getUsers() {
        SystemUserInformation[] infos = common.getUsers();
        if (infos == null) {
            this.error = "";
            return null;
        }
        
        SystemUser[] ret = new SystemUser[0];
        if (infos.length != 0) {
            ret = new SystemUser[infos.length];
            for (int i = 0; i < infos.length; i++) {
                ret[i] = new SystemUser(infos[i]);
            }
        }
        return ret;
    }

    public boolean createUser(String username, int userType, String password){
        boolean result = common.createUser(username, userType, password);
        if (!result) {
            this.error = "";
        }
        return result;
    }

    public boolean deleteUser(String username){
        boolean result = common.deleteUser(username);
        if (!result) {
            this.error = "";
        }
        return result;
    }

    public boolean modifyUserPassword(String userName, String password) {
        boolean result = common.modifyUserPassword(userName, password);
        if (!result) {
            this.error = "";
        }
        return result;
    }

    public Email getEmailParameter() {
        EmailParameter origin = common.getEmailParameter();
        if (origin == null) {
            this.error = "";
            return null;
        }
        Email info = new Email(origin);
        return info;
    }

    public boolean setEmailParameter(Email email) {
        EmailParameter param = new EmailParameter();
        param.bNeedAuthorize = email.isAuthorization_required();
        param.enableEmail = email.isEnable_email();
        param.fromAddress = email.getSender_address();
        param.password = email.getPassword();
        param.stmp = email.getStmp();
        param.stmpPort = email.getStmp_port();
        param.toAddress = email.getReceiver_addresses();
        param.userName = email.getUsername();
        
        boolean result = common.setEmailParameter(param);
        if (!result) {
            this.error = "";
        }
        return result; 
    }

    public ArrayList<String> getAvailableTimeZone() {
        ArrayList<String> origin = common.getAvailableTimeZone();
        if (origin == null) {
            this.error = "";
        }
        
        return origin;
    }

    public String getCurrentTimeZone() {
        return common.getCurrentTimeZone();
    }

    public boolean setTimeZone(String timezone) {
        return common.setTimeZone(timezone);
    }

    public boolean powerOff() {
        return common.powerOff();
    }

    public boolean reboot() {
        return common.reboot();
    }
    

    public ArrayList<Disk> getPhysDiskStatistics() {
        ArrayList<DiskStatistics> origin = common.getPhysDiskStatistics();
        if (origin == null) {
            this.error = "";
            return null;
        }
//        String diskGroupPath = origin.getDiskGroupPath();
//        result = common.cdpRollback(diskGroupPath, diskGroupGuid, 0, rollbackTime);
//        if (!result) {
//            this.error = common.getError();
//        }
        return null;
    }

//    public CdpRollbackTask getRollbackInfo(String diskGroupGuid) {
//        CdpDiskGroupInfo origin = common.getDiskGroupInfo(diskGroupGuid);
//        if (origin == null) {
//            this.error = common.getError();
//            return null;
//        }
//        String diskGroupPath = origin.getDiskGroupPath();
//        CdpRollbackTaskInfo info = common.getRollbackInfo(diskGroupGuid, diskGroupPath);
//        if (info == null) {
//            this.error = common.getError();
//            return null;
//        }
//        CdpRollbackTask ret = new CdpRollbackTask(info);
//        return ret;
//    }
//
//    public boolean saveRollback(String diskGroupGuid) {
//        CdpDiskGroupInfo origin = common.getDiskGroupInfo(diskGroupGuid);
//        boolean result = false;
//        if (origin == null) {
//            this.error = common.getError();
//            return result;
//        }
//        String diskGroupPath = origin.getDiskGroupPath();
//        result = common.saveRollback(diskGroupPath);
//        if (!result) {
//            this.error = common.getError();
//        }
//        return result;
//    }
//
//    public boolean cancelRollback(String diskGroupGuid) {
//        CdpDiskGroupInfo origin = common.getDiskGroupInfo(diskGroupGuid);
//        boolean result = false;
//        if (origin == null) {
//            this.error = common.getError();
//            return result;
//        }
//        String diskGroupPath = origin.getDiskGroupPath();
//        result = common.cancelRollback(diskGroupPath);
//        if (!result) {
//            this.error = common.getError();
//        }
//        return result;
//    }
//
//    public boolean mappingDiskGroup(String hostGroupName, String diskGroupGuid, List<Disk> disks) {
//        CdpDiskGroupInfo origin = common.getDiskGroupInfo(diskGroupGuid);
//        boolean result = false;
//        if (origin == null) {
//            this.error = common.getError();
//            return result;
//        }
//        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//        if (disks == null) {
//            this.error = "Param disks is null!";
//            return result;
//        }
//        if (disks.isEmpty()) {
//            this.error = "Param disks is empty!";
//            return result;
//        }
//        CdpDiskInfo[] origins = common.getDiskInfos(diskGroupGuid, origin.getDiskGroupPath());
//        if (origins == null) {
//            this.error = common.getError();
//            return result;
//        }
//        if (origins.length != 0) {
//            for (CdpDiskInfo disk : origins) {
//                for (Disk d : disks) {
//                    if (d.getDisk_guid().equals(disk.getDiskGuid())) {
//                        result = scsi.addView(hostGroupName, d.getLun(), disk.getLuInfoBean().getlUName());
//                        if (!result) {
//                            this.error = scsi.getLastError();
//                            return result;
//                        }
//                        break;
//                    }
//                }
//            }
//        }
//        return result;
//    }
//
//    public boolean unmappingDiskGroup(String hostGroupName, String diskGroupGuid, List<Disk> disks) {
//        CdpDiskGroupInfo origin = common.getDiskGroupInfo(diskGroupGuid);
//        boolean result = false;
//        if (origin == null) {
//            this.error = common.getError();
//            return result;
//        }
//        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//        if (disks == null) {
//            this.error = "Param disks is null!";
//            return result;
//        }
//        if (disks.isEmpty()) {
//            this.error = "Param disks is empty!";
//            return result;
//        }
//        CdpDiskInfo[] origins = common.getDiskInfos(diskGroupGuid, origin.getDiskGroupPath());
//        if (origins == null) {
//            this.error = common.getError();
//            return result;
//        }
//
//        for (Disk disk : disks) {
//            for (CdpDiskInfo d : origins) {
//                if (d.getDiskGuid().equals(disk.getDisk_guid())) {
//                    ViewInformation[] views = scsi.getLUNView(d.getLuInfoBean().getlUName());
//                    if (views == null) {
//                        this.error = "views is null!";
//                        return result;
//                    }
//                    if (views.length == 0) {
//                        this.error = "views is empty!";
//                        return result;
//                    }
//                    int entry;
//                    Debug.print("Views Size:" + views.length);
//                    for (ViewInformation view : views) {
//                        Debug.print("View Host Group Name:" + view.getHostGroupName());
//                        if (view.getHostGroupName().equals(hostGroupName)) {
//                            entry = view.getEntry();
//                            result = scsi.removeView(d.getLuInfoBean().getlUName(), entry);
//                            if (!result) {
//                                this.error = scsi.getLastError();
//                                return result;
//                            }
//                            break;
//                        }
//                    }
//                    break;
//                }
//            }
//        }
//        return result;
//    }
//
//    public long getAppointedTimeSnapshotID(String guid, long time) {
//        CdpDiskGroupInfo origin = common.getDiskGroupInfo(guid);
//        if (origin == null) {
//            this.error = common.getError();
//            return -2;
//        }
//        String path = origin.getDiskGroupPath();
//        long id = common.getAppointedTimeSnapshotID(guid, path, time);
//        return id;
//    }
}
