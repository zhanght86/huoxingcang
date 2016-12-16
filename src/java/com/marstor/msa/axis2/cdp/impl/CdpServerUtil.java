/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.cdp.impl;

import com.marstor.msa.cdp.bean.CdpAgent;
import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.bean.CdpLogPolicy;
import com.marstor.msa.cdp.bean.CdpLogRecord;
import com.marstor.msa.cdp.bean.CdpRollbackTaskInfo;
import com.marstor.msa.cdp.bean.CdpSnapshotInfo;
import com.marstor.msa.cdp.bean.CdpStatusInfo;
import com.marstor.msa.cdp.bean.DataBase;
import com.marstor.msa.cdp.bean.Host;
import com.marstor.msa.cdp.bean.LuInfoBean;
import com.marstor.msa.axis2.sync.impl.SyncServerUtil;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.util.StreamWrapper;

/**
 *
 * @author Administrator
 */
public class CdpServerUtil {

    public static <T> OMElement generateResponseObject(T t) {
        javax.xml.stream.XMLStreamReader reader = BeanUtil.getPullParser(t);
        StreamWrapper parser = new StreamWrapper(reader);
        OMXMLParserWrapper stAXOMBuilder = OMXMLBuilderFactory.createStAXOMBuilder(OMAbstractFactory.getOMFactory(), parser);
        OMElement element = stAXOMBuilder.getDocumentElement();
        return element;
    }

    public static <T> T getResquestObject(OMElement element, Class<T> clazz) {
        try {
            OMNode omNode = (OMNode) element;
            if (omNode.getType() == OMNode.ELEMENT_NODE) {
                OMElement omElement = (OMElement) omNode;
                if (element.getLocalName().toLowerCase().equals(clazz.getSimpleName().toLowerCase())) {
                    T para = (T) BeanUtil.processObject(
                            omElement, clazz, null, false, new DefaultObjectSupplier(), clazz);
                    return para;
                }
            }
        } catch (AxisFault ex) {
            Logger.getLogger(CdpServerUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static com.marstor.msa.axis2.cdp.model.CdpAgent convertCdpAgent(CdpAgent cdpAgent) {
        if(cdpAgent == null){
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpAgent cdpAgent_axis
                = new com.marstor.msa.axis2.cdp.model.CdpAgent();
        cdpAgent_axis.setStrDiskGroupGuid(cdpAgent.getStrDiskGroupGuid());
        cdpAgent_axis.setiStatus(cdpAgent.getiStatus());
        cdpAgent_axis.setlSnapshotInterval(cdpAgent.getlSnapshotInterval());
        List<DataBase> dataBases = cdpAgent.getListDB();
        com.marstor.msa.axis2.cdp.model.DataBase[] dataBases_axis
                = new com.marstor.msa.axis2.cdp.model.DataBase[dataBases.size()];
        for (int i = 0; i < dataBases.size(); i++) {
            DataBase dataBase = dataBases.get(i);
            com.marstor.msa.axis2.cdp.model.DataBase dataBase_axis
                    = convertDataBase(dataBase);
            dataBases_axis[i] = dataBase_axis;
        }
        cdpAgent_axis.setListDB(dataBases_axis);
        return cdpAgent_axis;
    }

    public static com.marstor.msa.axis2.cdp.model.DataBase convertDataBase(DataBase dataBase) {
        if(dataBase == null){
            return null;
        }
        com.marstor.msa.axis2.cdp.model.DataBase dataBase_axis
                = new com.marstor.msa.axis2.cdp.model.DataBase();
        dataBase_axis.setIp(dataBase.getIp());
        dataBase_axis.setPort(dataBase.getPort());
        dataBase_axis.setDbInstance(dataBase.getDbInstance());
        dataBase_axis.setAgentType(dataBase.getAgentType());
        return dataBase_axis;
    }

    public static com.marstor.msa.axis2.cdp.model.CdpLogPolicy convertCdpLogPolicy(CdpLogPolicy cdpLogPolicy) {
        if(cdpLogPolicy ==null){
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpLogPolicy cdpLogPolicy_axis
                = new com.marstor.msa.axis2.cdp.model.CdpLogPolicy();
        cdpLogPolicy_axis.setLogFullOption(cdpLogPolicy.getLogFullOption());
        cdpLogPolicy_axis.setLogKeepTime(cdpLogPolicy.getLogKeepTime());
        cdpLogPolicy_axis.setLogSize(cdpLogPolicy.getLogSize());
        return cdpLogPolicy_axis;
    }

    public static com.marstor.msa.axis2.cdp.model.CdpLogRecord convertCdpLogRecord(CdpLogRecord cdpLogRecord) {
        if(cdpLogRecord == null){
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpLogRecord cdpLogRecord_axis
                = new com.marstor.msa.axis2.cdp.model.CdpLogRecord();
        cdpLogRecord_axis.setDiskID(cdpLogRecord.getDiskID());
        cdpLogRecord_axis.setLogOffset(cdpLogRecord.getLogOffset());
        cdpLogRecord_axis.setLogSize(cdpLogRecord.getLogSize());
        cdpLogRecord_axis.setLogTime(processTime(cdpLogRecord.getLogTime()));
        return cdpLogRecord_axis;
    }

    public static com.marstor.msa.axis2.cdp.model.CdpSnapshotInfo convertCdpSnapshotInfo(CdpSnapshotInfo cdpSnapshotInfo) {
        if(cdpSnapshotInfo == null){
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpSnapshotInfo cdpSnapshotInfo_axis
                = new com.marstor.msa.axis2.cdp.model.CdpSnapshotInfo();
        cdpSnapshotInfo_axis.setAgentHost(cdpSnapshotInfo.getAgentHost());
        cdpSnapshotInfo_axis.setAgentID(cdpSnapshotInfo.getAgentID());
        cdpSnapshotInfo_axis.setSnapshotID(cdpSnapshotInfo.getSnapshotID());
        cdpSnapshotInfo_axis.setSnapshotName(cdpSnapshotInfo.getSnapshotName());
        cdpSnapshotInfo_axis.setSnapshotSize(cdpSnapshotInfo.getSnapshotSize());
        cdpSnapshotInfo_axis.setSnapshotTime(processTime(cdpSnapshotInfo.getSnapshotTime()));
        cdpSnapshotInfo_axis.setbHasZFSSnapshot(cdpSnapshotInfo.isbHasZFSSnapshot());
        return cdpSnapshotInfo_axis;
    }

    public static com.marstor.msa.axis2.cdp.model.CdpRollbackTaskInfo convertCdpRollbackTaskInfo(CdpRollbackTaskInfo cdpRollbackTaskInfo) {
        if(cdpRollbackTaskInfo == null){
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpRollbackTaskInfo cdpRollbackTaskInfo_axis
                = new com.marstor.msa.axis2.cdp.model.CdpRollbackTaskInfo();
        cdpRollbackTaskInfo_axis.setCurrentRollbackSize(cdpRollbackTaskInfo.getCurrentRollbackSize());
        cdpRollbackTaskInfo_axis.setDiskGroupGuid(cdpRollbackTaskInfo.getDiskGroupGuid());
        cdpRollbackTaskInfo_axis.setEndTime(processTime(cdpRollbackTaskInfo.getEndTime()));
        cdpRollbackTaskInfo_axis.setError(cdpRollbackTaskInfo.getError());
        cdpRollbackTaskInfo_axis.setErrorMessage(cdpRollbackTaskInfo.getErrorMessage());
        cdpRollbackTaskInfo_axis.setRollbackTime(processTime(cdpRollbackTaskInfo.getRollbackTime()));
        cdpRollbackTaskInfo_axis.setStartTime(processTime(cdpRollbackTaskInfo.getStartTime()));
        cdpRollbackTaskInfo_axis.setTaskStatus(cdpRollbackTaskInfo.getTaskStatus());
        cdpRollbackTaskInfo_axis.setTotalRollbackSize(cdpRollbackTaskInfo.getTotalRollbackSize());
        cdpRollbackTaskInfo_axis.setiRollbackTimeOrder(cdpRollbackTaskInfo.getiRollbackTimeOrder());
        return cdpRollbackTaskInfo_axis;
    }

    public static com.marstor.msa.axis2.cdp.model.CdpStatusInfo convertCdpStatusInfo(CdpStatusInfo cdpStatusInfo) {
        if(cdpStatusInfo == null){
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpStatusInfo cdpStatusInfo_axis
                = new com.marstor.msa.axis2.cdp.model.CdpStatusInfo();
        cdpStatusInfo_axis.setExistSnapshot(cdpStatusInfo.getExistSnapshot());
        cdpStatusInfo_axis.setIsDescSync(cdpStatusInfo.getIsDescSync());
        cdpStatusInfo_axis.setIsLocalDescSync(cdpStatusInfo.getIsLocalDescSync());
        cdpStatusInfo_axis.setIsMount(cdpStatusInfo.getIsMount());
        cdpStatusInfo_axis.setIsPause(cdpStatusInfo.getIsPause());
        cdpStatusInfo_axis.setIsRollback(cdpStatusInfo.getIsRollback());
        cdpStatusInfo_axis.setIsSrcSync(cdpStatusInfo.getIsSrcSync());
        return cdpStatusInfo_axis;
    }
    
    public static com.marstor.msa.axis2.cdp.model.Host convertHost(Host host) {
        if(host ==null){
            return null;
        }
        com.marstor.msa.axis2.cdp.model.Host host_axis
                = new com.marstor.msa.axis2.cdp.model.Host();
        host_axis.setHostname(host.getHostname());
        host_axis.setIp(host.getIp());
        host_axis.setOStype(host.getOStype());
        host_axis.setPort(host.getPort());
        return host_axis;
    }
    
    public static com.marstor.msa.axis2.cdp.model.LuInfoBean convertLuInfoBean(LuInfoBean luInfoBean) {
        if(luInfoBean ==null){
            return null;
        }
        com.marstor.msa.axis2.cdp.model.LuInfoBean luInfoBean_axis
                = new com.marstor.msa.axis2.cdp.model.LuInfoBean();
        luInfoBean_axis.setAccessState(luInfoBean.getAccessState());
        luInfoBean_axis.setAlias(luInfoBean.getAlias());
        luInfoBean_axis.setDataFile(luInfoBean.getDataFile());
        luInfoBean_axis.setOperationalStatus(luInfoBean.getOperationalStatus());
        luInfoBean_axis.setProductID(luInfoBean.getProductID());
        luInfoBean_axis.setProviderName(luInfoBean.getProviderName());
        luInfoBean_axis.setSerialNum(luInfoBean.getSerialNum());
        luInfoBean_axis.setSize(luInfoBean.getSize());
        luInfoBean_axis.setVendorID(luInfoBean.getVendorID());
        luInfoBean_axis.setlUName(luInfoBean.getlUName());
        return luInfoBean_axis;
    }
    
    public static com.marstor.msa.axis2.cdp.model.CdpDiskInfo convertCdpDiskInfo(CdpDiskInfo cdpDiskInfo) {
        if(cdpDiskInfo == null){
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpDiskInfo cdpDiskInfo_axis
                = new com.marstor.msa.axis2.cdp.model.CdpDiskInfo();
        cdpDiskInfo_axis.setDiskCreateStatus(cdpDiskInfo.getDiskCreateStatus());
        cdpDiskInfo_axis.setDiskFileName_Path(cdpDiskInfo.getDiskFileName_Path());
        cdpDiskInfo_axis.setDiskGroupGuid(cdpDiskInfo.getDiskGroupGuid());
        cdpDiskInfo_axis.setDiskGuid(cdpDiskInfo.getDiskGuid());
        cdpDiskInfo_axis.setDiskID(cdpDiskInfo.getDiskID());
        cdpDiskInfo_axis.setDiskName(cdpDiskInfo.getDiskName());
        cdpDiskInfo_axis.setDiskSize(cdpDiskInfo.getDiskSize());
        cdpDiskInfo_axis.setLogRecordCount(cdpDiskInfo.getLogRecordCount());
        com.marstor.msa.axis2.cdp.model.LuInfoBean luInfoBean_axis = convertLuInfoBean(cdpDiskInfo.getLuInfoBean());                       
        cdpDiskInfo_axis.setLuInfoBean(luInfoBean_axis);
        return cdpDiskInfo_axis;
    }
    
    public static com.marstor.msa.axis2.cdp.model.CdpDiskGroupInfo convertCdpDiskGroupInfo(CdpDiskGroupInfo cdpDiskGroupInfo) {
        if(cdpDiskGroupInfo == null){
            return null;
        }
        com.marstor.msa.axis2.cdp.model.CdpDiskGroupInfo cdpDiskGroupInfo_axis
                = new com.marstor.msa.axis2.cdp.model.CdpDiskGroupInfo();
        cdpDiskGroupInfo_axis.setCDPStarted(cdpDiskGroupInfo.isCDPStarted());
        cdpDiskGroupInfo_axis.setDiskCount(cdpDiskGroupInfo.getDiskCount());
        cdpDiskGroupInfo_axis.setDiskGroupGuid(cdpDiskGroupInfo.getDiskGroupGuid());
        cdpDiskGroupInfo_axis.setDiskGroupName(cdpDiskGroupInfo.getDiskGroupName());
        cdpDiskGroupInfo_axis.setDiskGroupPath(cdpDiskGroupInfo.getDiskGroupPath());
        cdpDiskGroupInfo_axis.setDiskZfsName(cdpDiskGroupInfo.getDiskZfsName());
        cdpDiskGroupInfo_axis.setLogRecordCount(cdpDiskGroupInfo.getLogRecordCount());
        cdpDiskGroupInfo_axis.setProtectType(cdpDiskGroupInfo.getProtectType());
        cdpDiskGroupInfo_axis.setSnapshotCount(cdpDiskGroupInfo.getSnapshotCount());
        cdpDiskGroupInfo_axis.setStrAvailSize(cdpDiskGroupInfo.getStrAvailSize());
        cdpDiskGroupInfo_axis.setStrUsedSize(cdpDiskGroupInfo.getStrUsedSize());
        cdpDiskGroupInfo_axis.setTotalDiskSize(cdpDiskGroupInfo.getTotalDiskSize());
        cdpDiskGroupInfo_axis.setbCDPStarted(cdpDiskGroupInfo.isbCDPStarted());
        cdpDiskGroupInfo_axis.setbLogFull(cdpDiskGroupInfo.isbLogFull());
        cdpDiskGroupInfo_axis.setbRollbacked(cdpDiskGroupInfo.isRollbacked());
        cdpDiskGroupInfo_axis.setiAutoSnapshotIsOpen(cdpDiskGroupInfo.getiAutoSnapshotIsOpen());
        cdpDiskGroupInfo_axis.setiMaxNum(cdpDiskGroupInfo.getiMaxNum());
        cdpDiskGroupInfo_axis.setiMount(cdpDiskGroupInfo.getiMount());
        cdpDiskGroupInfo_axis.setlAutoSnapshotInterval(cdpDiskGroupInfo.getlAutoSnapshotInterval());
        cdpDiskGroupInfo_axis.setlSnapshotInterval(cdpDiskGroupInfo.getlSnapshotInterval());
        
        com.marstor.msa.axis2.cdp.model.CdpLogPolicy CdpLogPolicy_axis
                = convertCdpLogPolicy(cdpDiskGroupInfo.getLogPolicy());
        cdpDiskGroupInfo_axis.setLogPolicy(CdpLogPolicy_axis);
        
//        com.marstor.msa.cdp.axis2.model.CdpStatusInfo CdpStatusInfo_axis
//                = convertCdpStatusInfo(cdpDiskGroupInfo.getCdpStatusInfo());
//        cdpDiskGroupInfo_axis.setCdpStatusInfo(CdpStatusInfo_axis);
        
        com.marstor.msa.axis2.cdp.model.CdpRollbackTaskInfo CdpRollbackTaskInfo_axis
                = convertCdpRollbackTaskInfo(cdpDiskGroupInfo.getRollbackInfo());
        cdpDiskGroupInfo_axis.setRollbackInfo(CdpRollbackTaskInfo_axis);
        
        com.marstor.msa.axis2.cdp.model.CdpAgent cdpAgent_axis
                = convertCdpAgent(cdpDiskGroupInfo.getCdpAgent());
        cdpDiskGroupInfo_axis.setCdpAgent(cdpAgent_axis);
        
        com.marstor.msa.axis2.sync.model.SyncStatusInfo syncStatusInfo_axis
                = SyncServerUtil.convertSyncStatusInfo(cdpDiskGroupInfo.getSyncStatusInfo());
        cdpDiskGroupInfo_axis.setSyncStatusInfo(syncStatusInfo_axis);
        
        return cdpDiskGroupInfo_axis;
    }
    
    private static long processTime(Date date){
        if(date == null){
            return 0;
        }
        return date.getTime();
    }
}
