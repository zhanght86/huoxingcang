/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.bean.InitiatorStaticConfig;
import com.marstor.msa.common.bean.InitiatorTargetConnection;
import com.marstor.msa.common.bean.InitiatorTargetDevice;
import com.marstor.msa.common.bean.InitiatorTargetInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.util.InterfaceFactory;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.ToggleEvent;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class InitiatoriSCSITargetsBean {

    private ArrayList<InitiatoriSCSITarget> targets = new ArrayList<InitiatoriSCSITarget>();
    private InitiatoriSCSITarget temp;
    private InitiatoriSCSITarget selectTarget;

    public InitiatoriSCSITargetsBean() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        InitiatorStaticConfig connectedConfig[] = InterfaceFactory.getSCSIInterfaceInstance().listStaticConfig();
        ArrayList<String> connectedTargets = new ArrayList<String>();
        if (connectedConfig != null) {
            for (int i = 0; i < connectedConfig.length; i++) {
                connectedTargets.add(connectedConfig[i].getTargetName());
            }
        }

        String targetNames[] = scsi.listTargets();
        if (targetNames == null) {
            return;
        }
        for (int i = 0; i < targetNames.length; i++) {
            if (connectedTargets.contains(targetNames[i])) { //如果此Target为已连接的Target
                String name = targetNames[i];
                // scsi.listTargetDetail(name);
                InitiatoriSCSITarget target = new InitiatoriSCSITarget(name);
                targets.add(target);
            }

        }

//        ArrayList<InitiatoriSCSITargetDevice> devices;
//        ArrayList<InitiatoriSCSITargetConnection> connects;
//        InitiatoriSCSITargetDevice device;
//        InitiatoriSCSITargetConnection connect;
//        
//        devices = new ArrayList<InitiatoriSCSITargetDevice>();
//        device = new InitiatoriSCSITargetDevice(0, "/dev/rmt/1n", "ULTRIUM-TD1", "IBM");
//       // 1, "/dev/rmt/2n", "ULTRIUM-TD1", "IBM"
//        
//        devices.add(device);
//        
//        device = new InitiatoriSCSITargetDevice(1, "/dev/rmt/2n", "ULTRIUM-TD1", "IBM");
//        devices.add(device);
//        //192.168.10.232:37506	192.168.10.232:3260
//        connects = new ArrayList<InitiatoriSCSITargetConnection>();
//        connect = new InitiatoriSCSITargetConnection("192.168.10.232:37506", "192.168.10.232:3260");
//        connects.add(connect);
//       // iqn.2013-01.com.marstor:1357608243219 别名	Mars-iSCSI-T-2
//        InitiatoriSCSITarget target;
//        target = new InitiatoriSCSITarget("iqn.2013-01.com.marstor:1357608243219", "Mars-iSCSI-T-2", 1, new Chap());
//        target.setDevices(devices);
//        target.setConnections(connects);
//        targets.add(target);



    }

    public void init() {
        InitiatorStaticConfig connectedConfig[] = InterfaceFactory.getSCSIInterfaceInstance().listStaticConfig();
        ArrayList<String> connectedTargets = new ArrayList<String>();
        if (connectedConfig != null) {
            for (int i = 0; i < connectedConfig.length; i++) {
                connectedTargets.add(connectedConfig[i].getTargetName());
            }
        }
        ArrayList<InitiatoriSCSITarget> targets = new ArrayList<InitiatoriSCSITarget>();
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        String targetNames[] = scsi.listTargets();
        if (targetNames == null) {
            return;
        }
        for (int i = 0; i < targetNames.length; i++) {
            if (connectedTargets.contains(targetNames[i])) { 
                String name = targetNames[i];
                InitiatoriSCSITarget target = new InitiatoriSCSITarget(name);
                targets.add(target);
            }

        }
        this.targets = targets;
    }

    public InitiatoriSCSITarget getSelectTarget() {
        return selectTarget;
    }

    public void setSelectTarget(InitiatoriSCSITarget selectTarget) {
        this.selectTarget = selectTarget;
    }

    public ArrayList<InitiatoriSCSITarget> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<InitiatoriSCSITarget> targets) {
        this.targets = targets;
    }

    public InitiatoriSCSITarget getTemp() {
        return temp;
    }

    public void setTemp(InitiatoriSCSITarget temp) {
        this.temp = temp;
    }

    public String doBeforeConfigChap(InitiatoriSCSITarget target) {

        this.temp = target;
        String param = "targetName=" + target.getTargetName();
        return "scsi_iscsi_target_chap?faces-redirect=true&amp;" + param;
    }

    public void onRowToggle(ToggleEvent event) {
        MSAResource res = new MSAResource();
        InitiatoriSCSITarget target = (InitiatoriSCSITarget) event.getData();
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();

        InitiatorTargetInformation targetInfo = scsi.listTargetDetail(target.getTargetName());
        if (targetInfo == null) {
            return;
        }
        ArrayList<InitiatoriSCSITargetSummary> summaryList = new ArrayList<InitiatoriSCSITargetSummary>();
        InitiatoriSCSITargetSummary summary;
        summary = new InitiatoriSCSITargetSummary(res.get("name"), targetInfo.getTargetName());
        summaryList.add(summary);
        summary = new InitiatoriSCSITargetSummary(res.get("alias"), targetInfo.getTargetAlias());
        summaryList.add(summary);
        //Target Portal Group	1
        summary = new InitiatoriSCSITargetSummary("Target Portal Group", targetInfo.getTPGT());
        summaryList.add(summary);
        summary = new InitiatoriSCSITargetSummary(res.get("connectNum"), String.valueOf(targetInfo.getConnectionCount()));
        summaryList.add(summary);
        String startCHAPOrNot = "", chapName = "";
        if (targetInfo.getAuthType().equals("CHAP")) {
            startCHAPOrNot = "CHAP";
            chapName = targetInfo.getChapName();
        } else {
            startCHAPOrNot = res.get("nothing");
            chapName = res.get("nothing");
        }
        summary = new InitiatoriSCSITargetSummary(res.get("authorityStyle"), startCHAPOrNot);
        summaryList.add(summary);
        summary = new InitiatoriSCSITargetSummary(res.get("authorityName"), chapName);
        summaryList.add(summary);
        Debug.print("target name: " + targetInfo.getTargetName());
        Debug.print("authority name: " + targetInfo.getChapName());
        target.setSummary(summaryList);
        ArrayList<InitiatorTargetDevice> devices = targetInfo.getDevices();
        target.setDevices(devices);
        ArrayList<InitiatorTargetConnection> connects = targetInfo.getConnections();
        target.setConnections(connects);

    }

    public void reConnecedTarget() {
        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().reconnectTarget();
        if (!flag) {
            Debug.print("reConneced Target " + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("reConnectFailed"), res.get("reConnectFailed")));
            return;
        }
        init();
    }

    public void removeTarget() {

        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().removeStaticConfig(this.selectTarget.getTargetName());
        if (!flag) {
            Debug.print("remove Target: " + this.selectTarget.getTargetName() + " " + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("deleteTargetFailed"), res.get("deleteTargetFailed")));
            return;
        }
        this.init();
    }
}
