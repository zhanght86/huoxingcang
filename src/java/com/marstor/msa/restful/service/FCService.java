/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.service;

import com.marstor.msa.common.bean.FCInitiatorInformation;
import com.marstor.msa.common.bean.FCInitiatorTargetInformation;
import com.marstor.msa.common.bean.HostGroup;
import com.marstor.msa.common.bean.TargetInformation;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.bean.iSCSIInitiator;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.restful.fc.bean.FCInitiator;
import com.marstor.msa.restful.fc.bean.FCInitiatorTarget;
import com.marstor.msa.restful.iscsi.bean.Constant;
import com.marstor.msa.restful.iscsi.bean.HostGroupInfo;
import com.marstor.msa.restful.iscsi.bean.ISCSIInitiator;
import com.marstor.msa.restful.iscsi.bean.Target;
import com.marstor.msa.restful.iscsi.bean.View;
import com.marstor.msa.util.InterfaceFactory;

/**
 *
 * @author Administrator
 */

public class FCService {
    private SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
    private String error;
        
    
    /**
     * 获取火星舱FC主机组
     *
     * @return 失败null
     */
    public HostGroupInfo[] getFCHostGroups() {
        HostGroup[] hostGroups = scsi.getFCHostGroups();
        HostGroupInfo[] allGroup = null;
        if (hostGroups == null) {
             this.setError(scsi.getLastError());
            return null;
        }
        allGroup = new HostGroupInfo[hostGroups.length];
        HostGroupInfo hostGroup = null;
        for (int i = 0; i < hostGroups.length; i++) {            
            hostGroup = new HostGroupInfo(hostGroups[i]);           
            allGroup[i] = hostGroup;
        }

        return allGroup;
    }
 
    
    /**
     * 获取FC initiator
     *
     * @return 失败null
     */
    public FCInitiator[] getFCInitiator(){
        FCInitiatorInformation[] allISCSIInitiator = scsi.listFCInitiator();
        FCInitiator[] allInitiator = null;
        if (allISCSIInitiator == null) {
            this.setError(scsi.getLastError());
            return null;
        }
        allInitiator = new FCInitiator[allISCSIInitiator.length];
        FCInitiator iSCSIInitiator = null;
        for (int i = 0; i < allISCSIInitiator.length; i++) {
            iSCSIInitiator = new FCInitiator(allISCSIInitiator[i]);
            allInitiator[i] = iSCSIInitiator;
        }
        
        return allInitiator;
    }
    
    /**
     * 获取所有FC Initiator连接的Target信息
     *
     * @param initiator
     * @return
     */
    public FCInitiatorTarget[] getFCInitiatorTarget(String initiator){
        FCInitiatorTargetInformation[] allISCSIInitiator = scsi.listFCInitiatorTarget(initiator);
        FCInitiatorTarget[] allInitiator = null;
        if (allISCSIInitiator == null) {
            this.setError(scsi.getLastError());
            return null;
        }
        allInitiator = new FCInitiatorTarget[allISCSIInitiator.length];
        FCInitiatorTarget iSCSIInitiator = null;
        for (int i = 0; i < allISCSIInitiator.length; i++) {
            iSCSIInitiator = new FCInitiatorTarget(allISCSIInitiator[i]);
            allInitiator[i] = iSCSIInitiator;
        }
        
        return allInitiator;
    }
    
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
}
