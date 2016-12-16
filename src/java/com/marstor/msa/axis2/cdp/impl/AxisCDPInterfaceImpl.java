/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.cdp.impl;

import com.marstor.msa.axis2.cdp.para.CdpAgentParameter;
import com.marstor.msa.axis2.cdp.para.CdpAgentSnapParameter;
import com.marstor.msa.axis2.cdp.para.CdpLuParameter;
import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.bean.CdpLogRecord;
import com.marstor.msa.cdp.bean.CdpRollbackTaskInfo;
import com.marstor.msa.cdp.bean.CdpSnapshotInfo;
import com.marstor.msa.cdp.bean.DataBase;
import com.marstor.msa.cdp.bean.Host;
import com.marstor.msa.cdp.bean.LuInfoBean;
import com.marstor.msa.cdp.bean.VmdkDisk;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.axis2.common.impl.AxisLoginServices;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ServiceGroupContext;
import org.apache.axis2.databinding.utils.BeanUtil;

/**
 *
 * @author Administrator
 */
public class AxisCDPInterfaceImpl{

    private final MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
    private static String error;

    private void setError(String s) {
        error = s;
    }

    public String getError() {
        return error;
    }
    
    public int login(String username,String password) {
        System.out.println("AxisCDP login");
        AxisLoginServices loginService = new AxisLoginServices();
        return loginService.login(username, password);
    }
    
    public String createDiskGroup(String volumeName, String diskGroupName) {
        String name = cdp.createDiskGroup(volumeName, diskGroupName);
        if (name == null) {
            this.setError(cdp.getError());
            return null;
        }
        return name;
    }

    public boolean deleteDiskGroup(String diskGroupGuid, String diskGroupPath) {
        boolean result = cdp.deleteDiskGroup(diskGroupPath, diskGroupPath);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public OMElement getDiskGroupInfos() {
        List<CdpDiskGroupInfo> cdpDiskGroupInfos = cdp.getDiskGroupInfos();
        if (cdpDiskGroupInfos == null) {
            this.setError(cdp.getError());
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpDiskGroupInfo[] cdpDiskGroupInfos_axis
                = new com.marstor.msa.axis2.cdp.model.CdpDiskGroupInfo[cdpDiskGroupInfos.size()];
        for (int i = 0; i < cdpDiskGroupInfos.size(); i++) {
            CdpDiskGroupInfo cdpDiskGroupInfo = cdpDiskGroupInfos.get(i);
            com.marstor.msa.axis2.cdp.model.CdpDiskGroupInfo cdpDiskGroupInfo_axis
                    = CdpServerUtil.convertCdpDiskGroupInfo(cdpDiskGroupInfo);
            cdpDiskGroupInfos_axis[i] = cdpDiskGroupInfo_axis;
        }
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                cdpDiskGroupInfos_axis, new QName("CdpDiskGroupInfo"), false, null);
        return element;
    }

    public OMElement getDiskGroupInfo(String diskGroupGuid) {
        CdpDiskGroupInfo cdpDiskGroupInfo = cdp.getDiskGroupInfo(diskGroupGuid);
        if (cdpDiskGroupInfo == null) {
            this.setError(cdp.getError());
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpDiskGroupInfo cdpDiskGroupInfo_axis
                = CdpServerUtil.convertCdpDiskGroupInfo(cdpDiskGroupInfo);
        OMElement element = CdpServerUtil.generateResponseObject(cdpDiskGroupInfo_axis);
        return element;
    }

    public String createDiskJoinGroup(String diskGroupGuid, String diskGroupPath, String size, boolean demand) {
        String name = cdp.createDiskJoinGroup(diskGroupGuid, diskGroupPath, size, demand, null);
        if (name == null) {
            this.setError(cdp.getError());
            return null;
        }
        return name;
    }

    public boolean deleteDiskFromGroup(String diskGuid, String diskName, String diskGroupPath) {
        boolean result = cdp.deleteDiskFromGroup(diskGuid, diskName, diskGroupPath);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public OMElement getDiskInfos(String diskGroupGuid, String diskGroupPath) {
        CdpDiskInfo[] cdpDiskInfos = cdp.getDiskInfos(diskGroupGuid, diskGroupPath);
        if (cdpDiskInfos == null) {
            this.setError(cdp.getError());
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpDiskInfo[] cdpDiskInfos_axis
                = new com.marstor.msa.axis2.cdp.model.CdpDiskInfo[cdpDiskInfos.length];
        for (int i = 0; i < cdpDiskInfos.length; i++) {
            com.marstor.msa.axis2.cdp.model.CdpDiskInfo cdpDiskInfo_axis
                    = CdpServerUtil.convertCdpDiskInfo(cdpDiskInfos[i]);
            cdpDiskInfos_axis[i] = cdpDiskInfo_axis;
        }
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                cdpDiskInfos_axis, new QName("CdpDiskInfo"), false, null);
        return element;
    }

    public boolean startCDP(String diskGroupGuid, String diskGroupPath, int logFullOption) {
        boolean result = cdp.startCDP(diskGroupGuid, diskGroupPath, logFullOption);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean stopCDP(String diskGroupGuid, String diskGroupPath, int iProtectType) {
        boolean result = cdp.stopCDP(diskGroupGuid, diskGroupPath, iProtectType);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public int getQuerySnapshotHandle(String diskGroupGuid, long snapshotID, String zfsName) {
        int handle = cdp.getQuerySnapshotHandle(diskGroupGuid, snapshotID, zfsName);
        if (handle == -1) {
            this.setError(cdp.getError());
            return -1;
        }
        return handle;
    }

    public OMElement querySnapshotInfos(String diskGroupPath, int queryHandle, int queryCount, int forwardSearch, int reverse, int currentCount) {
        CdpSnapshotInfo[] cdpSnapshotInfos = cdp.querySnapshotInfos(diskGroupPath, queryHandle, queryCount, forwardSearch, reverse, currentCount);
        if (cdpSnapshotInfos == null) {
            this.setError(cdp.getError());
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpSnapshotInfo[] cdpSnapshotInfos_axis
                = new com.marstor.msa.axis2.cdp.model.CdpSnapshotInfo[cdpSnapshotInfos.length];
        for (int i = 0; i < cdpSnapshotInfos.length; i++) {
            com.marstor.msa.axis2.cdp.model.CdpSnapshotInfo cdpSnapshotInfo_axis
                    = CdpServerUtil.convertCdpSnapshotInfo(cdpSnapshotInfos[i]);
            cdpSnapshotInfos_axis[i] = cdpSnapshotInfo_axis;
        }
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                cdpSnapshotInfos_axis, new QName("SnapshotInfo"), false, null);
        return element;
    }

    public boolean closeQuerySnapshotHandle(int queryHandle) {
        boolean result = cdp.closeQuerySnapshotHandle(queryHandle);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public int getQueryLogRecordHandle(String diskGroupGuid, long snapshotID) {
        int handle = cdp.getQueryLogRecordHandle(diskGroupGuid, snapshotID);
        if (handle == -1) {
            this.setError(cdp.getError());
            return -1;
        }
        return handle;
    }

    public OMElement queryLogRecordInfos(String diskGroupPath, int queryHandle, int queryCount, int forwardSearch, int reverse, int currentCount) {
        CdpLogRecord[] cdpLogRecords = cdp.queryLogRecordInfos(diskGroupPath, queryHandle, queryCount, forwardSearch, reverse, currentCount);
        if (cdpLogRecords == null) {
            this.setError(cdp.getError());
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpLogRecord[] cdpLogRecords_axis
                = new com.marstor.msa.axis2.cdp.model.CdpLogRecord[cdpLogRecords.length];
        for (int i = 0; i < cdpLogRecords.length; i++) {
            com.marstor.msa.axis2.cdp.model.CdpLogRecord cdpLogRecord_axis
                    = CdpServerUtil.convertCdpLogRecord(cdpLogRecords[i]);
            cdpLogRecords_axis[i] = cdpLogRecord_axis;
        }
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                cdpLogRecords_axis, new QName("CdpLogRecord"), false, null);
        return element;
    }

    public boolean closeQueryLogRecordHandle(int queryHandle) {
        boolean result = cdp.closeQueryLogRecordHandle(queryHandle);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean setLogPolicy(String diskGroupPath, String diskGroupGuid, long logSize, int logKeepTime, int logFullOption) {
        boolean result = cdp.setLogPolicy(diskGroupPath, diskGroupGuid, logSize, logKeepTime, logFullOption);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public long getAppointedTimeSnapshotID(String diskGroupGuid, String diskGroupPath, long snapshotTime) {
        long id = cdp.getAppointedTimeSnapshotID(diskGroupGuid, diskGroupPath, snapshotTime);
        if (id == -1) {
            this.setError(cdp.getError());
            return -1;
        }
        return id;
    }

    public boolean online(String diskGroupPath) {
        boolean result = cdp.online(diskGroupPath);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean offline(String diskGroupGuid, String diskGroupPath) {
        boolean result = cdp.offline(diskGroupGuid, diskGroupPath);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean setDefaultAgent(OMElement element) {
        element.build();
        element.detach();
        CdpAgentParameter para = CdpServerUtil.getResquestObject(element, CdpAgentParameter.class);
//        try {
//            OMNode omNode = (OMNode) element;
//            if (omNode.getType() == OMNode.ELEMENT_NODE) {
//                OMElement omElement = (OMElement) omNode;
//                para = (CdpAgentParameter) BeanUtil.processObject(
//                        omElement, CdpAgentParameter.class, null, true, new DefaultObjectSupplier(), CdpAgentParameter.class);
//            }
//        } catch (AxisFault ex) {
//            Logger.getLogger(AxisCDPInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
        if (para == null) {
            this.setError("parser object failed");
            return false;
        }
        com.marstor.msa.axis2.cdp.model.DataBase[] dbs_axis = para.getDataBases();
        List<DataBase> listDB = new ArrayList();
        for (com.marstor.msa.axis2.cdp.model.DataBase db_axis : dbs_axis) {
            DataBase db = new DataBase(db_axis.getIp(), db_axis.getPort(), db_axis.getDbInstance(), db_axis.getAgentType());
            listDB.add(db);
        }
        boolean result = cdp.setDefaultAgent(para.getDiskGroupGuid(), para.getSnapshotInterval(), listDB);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean onlineLU(OMElement element) {
        element.build();
        element.detach();
        CdpLuParameter para = CdpServerUtil.getResquestObject(element, CdpLuParameter.class);
//        try {
//            OMNode omNode = (OMNode) element;
//            if (omNode.getType() == OMNode.ELEMENT_NODE) {
//                OMElement omElement = (OMElement) omNode;
//                para = (CdpLuParameter) BeanUtil.processObject(
//                        omElement, CdpLuParameter.class, null, true, new DefaultObjectSupplier(), CdpLuParameter.class);
//            }
//        } catch (AxisFault ex) {
//            Logger.getLogger(AxisCDPInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
        if (para == null) {
            this.setError("parser object failed");
            return false;
        }
        boolean result = cdp.onlineLU(para.getDiskGroupName(), para.getLuInfos());
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean offlineLU(OMElement element) {
        element.build();
        element.detach();
        CdpLuParameter para = CdpServerUtil.getResquestObject(element, CdpLuParameter.class);
        if (para == null) {
            this.setError("parser object failed");
            return false;
        }
        boolean result = cdp.offlineLU(para.getDiskGroupName(), para.getLuInfos());
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public OMElement getRollbackInfo(String diskGroupGuid, String diskGroupPath) {
        CdpRollbackTaskInfo cdpRollbackTaskInfo = cdp.getRollbackInfo(diskGroupPath, diskGroupPath);
        if (cdpRollbackTaskInfo == null) {
            this.setError(cdp.getError());
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpRollbackTaskInfo cdpRollbackTaskInfo_axis
                = CdpServerUtil.convertCdpRollbackTaskInfo(cdpRollbackTaskInfo);
        OMElement element = CdpServerUtil.generateResponseObject(cdpRollbackTaskInfo_axis);
        return element;
    }

    public boolean cdpRollback(String fileSystem, String diskGroupGuid, int rollbackTimeOrder, long rollbackTime) {
        boolean result = cdp.cdpRollback(fileSystem, diskGroupGuid, rollbackTimeOrder, new Date(rollbackTime));
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean saveRollback(String srcFileSystem) {
        boolean result = cdp.saveRollback(srcFileSystem);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean cancelRollback(String srcFileSystem) {
//        MessageContext context = MessageContext.getCurrentMessageContext();
//        ServiceContext serviceContext = context.getServiceContext();
//        System.out.println("cdp serviceContext1="+serviceContext.getProperty("user"));
//        ServiceGroupContext groupContext = context.getServiceGroupContext();
//        System.out.println("cdp groupContext2="+groupContext.getProperty("user"));
        boolean result = cdp.cancelRollback(srcFileSystem);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean addHost(String ip, String port, int ostype) {
        boolean result = cdp.addHost(ip, port, ostype);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean deleteHost(String ip) {
        boolean result = cdp.deleteHost(ip);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean modifyHost(String ip, String port, int ostype, String hostname) {
        boolean result = cdp.modifyHost(ip, port, ostype, hostname);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public OMElement getAllHosts() {
        List<Host> hosts = cdp.getAllHosts();
        if (hosts == null) {
            this.setError(cdp.getError());
            return null;
        }
        com.marstor.msa.axis2.cdp.model.Host[] hosts_axis
                = new com.marstor.msa.axis2.cdp.model.Host[hosts.size()];
        for (int i = 0; i < hosts.size(); i++) {
            Host host = hosts.get(i);
            com.marstor.msa.axis2.cdp.model.Host host_axis
                    = CdpServerUtil.convertHost(host);
            hosts_axis[i] = host_axis;
        }
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                hosts_axis, new QName("Host"), false, null);
        return element;
    }

    public boolean setProtectType(String diskGroupName, int type) {
        boolean result = cdp.setProtectType(diskGroupName, type);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean restoreDiskSignature(String strDiskGuid) {
        boolean result = cdp.restoreDiskSignature(strDiskGuid);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public String createSnapshot(String strDiskGroupGuid, String strSnapshotName, String strAgentHost, long lAgentID) {
        String name = cdp.createSnapshot(strDiskGroupGuid, strSnapshotName, strAgentHost, lAgentID);
        if (name == null) {
            this.setError(cdp.getError());
            return null;
        }
        return name;
    }

    public boolean destroySnapshot(String snapshotName) {
        boolean result = cdp.destroySnapshot(snapshotName);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean configSnapshotProtect(String srcFileSystem, int timeInterval, int maxNum) {
        boolean result = cdp.configSnapshotProtect(srcFileSystem, timeInterval, maxNum);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean cloneReplicate(String diskGroupPath) {
        boolean result = cdp.cloneReplicate(diskGroupPath);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean startAutoSnapshot(String srcFileSystem, int timeInterval, int maxNum) {
        boolean result = cdp.startAutoSnapshot(srcFileSystem, timeInterval, maxNum);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public void createMirrorProtect(String hostIP, String hostPort, int nDiskNumber, int nPartitionNumber, boolean bDiskProtect, String serialNum) {
        cdp.createMirrorProtect(hostIP, hostPort, nDiskNumber, nPartitionNumber, bDiskProtect, serialNum);
    }

    public void createMirrorProtectLinuxV(String hostIP, String hostPort, String vg, String lv, String serialNum) {
        cdp.createMirrorProtectLinuxV(hostIP, hostPort, vg, lv, serialNum);
    }

    public void createMirrorProtectLinux(String hostIP, String hostPort, String device, String pDevice, boolean bDeviceProtect, String serialNum) {
        cdp.createMirrorProtectLinux(hostIP, hostPort, device, pDevice, bDeviceProtect, serialNum);
    }

    public OMElement getAllDiskInfos() {
        Map<String, LuInfoBean> map = cdp.getAllDiskInfos();
        if (map == null) {
            this.setError(cdp.getError());
            return null;
        }
        Collection<LuInfoBean> luInfoBeans = map.values();
        com.marstor.msa.axis2.cdp.model.LuInfoBean[] luInfoBeans_axis
                = new com.marstor.msa.axis2.cdp.model.LuInfoBean[luInfoBeans.size()];
        Iterator<LuInfoBean> iter = luInfoBeans.iterator();
        int index = 0;
        while (iter.hasNext()) {
            LuInfoBean luInfoBean = iter.next();
            com.marstor.msa.axis2.cdp.model.LuInfoBean luInfoBean_axis = CdpServerUtil.convertLuInfoBean(luInfoBean);
            luInfoBeans_axis[index] = luInfoBean_axis;
            index++;
        }
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                luInfoBeans_axis, new QName("Host"), false, null);
        return element;
    }

    public boolean deleteAllSnapshot(String diskGroupPath) {
        boolean result = cdp.deleteAllSnapshot(diskGroupPath);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean setRemoteProtectType(String ip, int port,String username,String password,String diskGroupName, int type) {
        boolean result = cdp.setRemoteProtectType(ip, port, username,password, diskGroupName, type);
        if (!result) {
            this.setError(cdp.getError());
            return false;
        }
        return true;
    }

    public boolean createOracleSnapshot(String diskGroupGuid, String ip, int agentID, long lTimeDifference, long lCreateTime) {
        System.out.println("CDP_AGENT_INFO: createOracleSnapshot  diskGroupGuid=" + diskGroupGuid);
        System.out.println("CDP_AGENT_INFO: createOracleSnapshot  ip=" + ip);
        System.out.println("CDP_AGENT_INFO: createOracleSnapshot  agentID=" + agentID);
        System.out.println("CDP_AGENT_INFO: createOracleSnapshot  lTimeDifference=" + lTimeDifference);
        System.out.println("CDP_AGENT_INFO: createOracleSnapshot  lCreateTime=" + lCreateTime);
        CdpDiskGroupInfo diskGroup = cdp.getDiskGroupInfo(diskGroupGuid);
        if (diskGroup != null && diskGroup.isCDPStarted()) {

            boolean result = cdp.createAgentSnapshot(diskGroupGuid, ip, agentID, lTimeDifference, lCreateTime);
            if (!result) {
                this.setError(cdp.getError());
                return false;
            }

        } else {
            if (diskGroup == null) {
                System.out.println("CDP_AGENT_INFO: createOracleSnapshot , Not found disk group error:" + cdp.getError());
                this.setError("Not found disk group error:" + cdp.getError());
                return false;
            } else if (!diskGroup.isCDPStarted()) {
                System.out.println("CDP_AGENT_INFO: createOracleSnapshot , CDP is not start.");
                this.setError("CDP is not start");
                return false;
            }
        }

        return true;
    } 
    
       
    public boolean createAgentSnapshot(OMElement element) {
        element.build();
        element.detach();
        CdpAgentSnapParameter para = CdpServerUtil.getResquestObject(element, CdpAgentSnapParameter.class);
        if (para == null) {
            this.setError("parser object failed");
            return false;
        }
        String diskGroupGuid = para.getDiskGroupGuid();
        String ip = para.getIp();
        int agentID = para.getAgentID();
        long lTimeDifference = para.getlTimeDifference();

        int iCount = para.getSnapshotTime().length;
        System.out.println("CDP_AGENT_INFO: createAgentSnapshot  diskGroupGuid=" + diskGroupGuid);
        System.out.println("CDP_AGENT_INFO: createAgentSnapshot  ip=" + ip);
        System.out.println("CDP_AGENT_INFO: createAgentSnapshot  agentID=" + agentID);
        System.out.println("CDP_AGENT_INFO: createAgentSnapshot  lTimeDifference=" + lTimeDifference);
        System.out.println("CDP_AGENT_INFO: createAgentSnapshot  lCreateTime iCount=" + iCount);
        CdpDiskGroupInfo diskGroup = cdp.getDiskGroupInfo(diskGroupGuid);
        if (diskGroup != null && diskGroup.isCDPStarted()) {
            for (int i = 0; i < iCount; i++) {
                long lCreateTime = para.getSnapshotTime()[i];    
                System.out.println("CDP_AGENT_INFO: createAgentSnapshot  lCreateTime=" + lCreateTime);
                boolean result = cdp.createAgentSnapshot(diskGroupGuid, ip, agentID, lTimeDifference, lCreateTime);
                if (!result) {
                    this.setError(cdp.getError());
                    return false;
                }
            }
        } else {
            if (diskGroup == null) {
                 System.out.println("CDP_AGENT_INFO: createAgentSnapshot , Not found disk group error:" + cdp.getError());
                this.setError("Not found disk group error:" + cdp.getError());
                return false;
            } else if (!diskGroup.isCDPStarted()) {
                System.out.println("CDP_AGENT_INFO: createAgentSnapshot , CDP is not start." );
                this.setError("CDP is not start");
                return false;
            }
        }
        return true;
    }
    
        
    public long getServerTime(){
        return new Date().getTime();
    }
//    public boolean convertRawToVmdk(String srcDiskPath, String descDiskPath,long diskSize) {
//        boolean result = cdp.convertRawToVmdk(srcDiskPath, descDiskPath, diskSize);
//        if (!result) {
//            this.setError(cdp.getError());
//            return false;
//        }
//        return true;
//    }
    
//    public OMElement getVmdkConvertDisks(){
//        List<VmdkDisk> listdisk = cdp.getVmdkConvertDisks();
//        VmdkDisk[] disks = new VmdkDisk[listdisk.size()];
//        listdisk.toArray(disks);
//        OMElement element = BeanUtil.getOMElement(new QName("root"),
//            disks, new QName("VmdkDisk"), false, null);
//        return element;
//    }
    
//    public String retrunNULL() {
//        System.out.println("public String retrunNULL()");
//        setError("Test NULL and ERROR");
//        return null;
//    }

}
