/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.sync.impl;

import com.marstor.msa.axis2.cdp.impl.AxisCDPInterfaceImpl;
import com.marstor.msa.sync.bean.Group;
import com.marstor.msa.sync.bean.NasBean;
import com.marstor.msa.sync.bean.SyncMapping;
import com.marstor.msa.sync.bean.SyncStatusInfo;
import com.marstor.msa.sync.bean.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
public class SyncServerUtil {
    
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
            Logger.getLogger(SyncServerUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Calendar convertStringToCalendar(String time){
        if(time == null){
            return null;
        }
        String[] times = time.split(":");
        int endHour = Integer.parseInt(times[0]);
        int endMinite = Integer.parseInt(times[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, endHour);
        calendar.set(Calendar.MINUTE, endMinite);
        return calendar;
    }
    
    public static String convertCalendarToString(Calendar calendar){
        if(calendar == null){
            return null;
        }
        String time = calendar.get(Calendar.HOUR_OF_DAY) +":"+calendar.get(Calendar.MINUTE);
        return time;
    }
    
    public static com.marstor.msa.axis2.sync.model.NasBean convertNasBean(NasBean nasBean) {
        if(nasBean == null){
            return null;
        }
        com.marstor.msa.axis2.sync.model.NasBean nasBean_axis = new com.marstor.msa.axis2.sync.model.NasBean();
        List<Group> groups = nasBean.getGroups();
        List<User> users = nasBean.getUsers();
        Group[] groupArray = new Group[groups.size()];
        User[] userArray = new User[users.size()];
        nasBean_axis.setGroups(groups.toArray(groupArray));
        nasBean_axis.setUsers(users.toArray(userArray));
        return nasBean_axis;
    }
    
    public static com.marstor.msa.axis2.sync.model.SyncMapping convertSyncMapping(SyncMapping syncMapping) {
        if(syncMapping == null){
            return null;
        }
        com.marstor.msa.axis2.sync.model.SyncMapping syncMapping_axis 
                = new com.marstor.msa.axis2.sync.model.SyncMapping();
        Calendar endTime = syncMapping.getJobEndTime();
        syncMapping_axis.setJobEndTime(convertCalendarToString(endTime));
        Calendar startTime = syncMapping.getJobStartTime();
        syncMapping_axis.setJobStartTime(convertCalendarToString(startTime));
        syncMapping_axis.setStrDescFileSystem(syncMapping.getStrDescFileSystem());
        syncMapping_axis.setStrFirstSnapshot(syncMapping.getStrFirstSnapshot());
        syncMapping_axis.setStrGzipLevel(syncMapping.getStrGzipLevel());
        syncMapping_axis.setStrIP(syncMapping.getStrIP());
        syncMapping_axis.setStrPassword(syncMapping.getStrPassword());
        syncMapping_axis.setStrPort(syncMapping.getStrPort());
        syncMapping_axis.setStrRootPassword(syncMapping.getStrRootPassword());
        syncMapping_axis.setStrSSHPort(syncMapping.getStrSSHPort());
        syncMapping_axis.setStrSecondSnapshot(syncMapping.getStrSecondSnapshot());
        syncMapping_axis.setStrSrcFileSystem(syncMapping.getStrSrcFileSystem());
        syncMapping_axis.setStrSyncSizeSpeed(syncMapping.getStrSyncSizeSpeed());
        syncMapping_axis.setStrTransferPort(syncMapping.getStrTransferPort());
        syncMapping_axis.setStrUserName(syncMapping.getStrUserName());
        syncMapping_axis.setbAllSend(syncMapping.isbAllSend());
        syncMapping_axis.setbTimingJob(syncMapping.isbTimingJob());
        if(syncMapping.getdSyncCompleteTime() != null ){
            syncMapping_axis.setdSyncCompleteTime(syncMapping.getdSyncCompleteTime().getTime()); 
        }                                                     
        syncMapping_axis.setiDescFileSystemHashCode(syncMapping.getiDescFileSystemHashCode());                       
        syncMapping_axis.setiIsLocal(syncMapping.getiIsLocal());                       
        syncMapping_axis.setiSyncStatus(syncMapping.getiSyncStatus());               
        return syncMapping_axis;
    }
    
    public static com.marstor.msa.axis2.sync.model.SyncStatusInfo convertSyncStatusInfo(SyncStatusInfo syncStatusInfo) {
        if(syncStatusInfo == null){
            return null;
        }
        com.marstor.msa.axis2.sync.model.SyncStatusInfo syncStatusInfo_axis
                = new com.marstor.msa.axis2.sync.model.SyncStatusInfo();
        syncStatusInfo_axis.setAvailable(syncStatusInfo.getAvailable());
        syncStatusInfo_axis.setExistSnapshot(syncStatusInfo.getExistSnapshot());
        syncStatusInfo_axis.setIsDescSync(syncStatusInfo.getIsDescSync());
        syncStatusInfo_axis.setIsLocalDescSync(syncStatusInfo.getIsLocalDescSync());
        syncStatusInfo_axis.setIsMount(syncStatusInfo.getIsMount());
        syncStatusInfo_axis.setIsPause(syncStatusInfo.getIsPause());
        syncStatusInfo_axis.setIsRollback(syncStatusInfo.getIsRollback());
        syncStatusInfo_axis.setIsSrcSync(syncStatusInfo.getIsSrcSync());
        syncStatusInfo_axis.setUsed(syncStatusInfo.getUsed());
        syncStatusInfo_axis.setbIsRollbacking(syncStatusInfo.isbIsRollbacking());
        return syncStatusInfo_axis;
    }
    
}
