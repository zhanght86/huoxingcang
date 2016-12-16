/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.bean.InitiatorStaticConfig;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.ToggleEvent;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class InitiatorHostBean {

    private ArrayList<InitiatorHost> hosts = new ArrayList<InitiatorHost>();
    private InitiatorHostTarget tempTarget;
    private InitiatorHost tempHost;
    private InitiatorHost selectHost;
    private InitiatoriSCSITargetsBean   hostTargets = new InitiatoriSCSITargetsBean();

    public InitiatorHostBean() {

        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        String hosts[] = scsi.listDiscoveryAddress();
        if (hosts == null) {
            return;
        }
        for (int i = 0; i < hosts.length; i++) {
            InitiatorHost host = new InitiatorHost(hosts[i]);
//            InitiatorStaticConfig targets[] = InterfaceFactory.getSCSIInterfaceInstance().listTargetFromDiscovery(host.getAddress());
//            for (int j = 0; j < targets.length; j++) {
//                InitiatorStaticConfig staticConfig = targets[j];
//                InitiatorHostTarget target = new InitiatorHostTarget(staticConfig.targetName, staticConfig.targetAddress);
//                host.getTargets().add(target);
//            }
            this.hosts.add(host);
        }
//        InitiatorHost host;
//        ArrayList<InitiatorHostTarget> targets;
//        InitiatorHostTarget target;
//
//        host = new InitiatorHost("192.168.10.232:3260");
//        targets = new ArrayList<InitiatorHostTarget>();
//        target = new InitiatorHostTarget("iqn.2013-01.com.marstor:1357608243219", "192.168.10.232:3260", true);
//        targets.add(target);
//        target = new InitiatorHostTarget("iqn.2013-01.com.marstor:1357891735399", "192.168.10.232:3260", false);
//        targets.add(target);
//        host.setTargets(targets);
//        
//        hosts.add(host);


    }

    public void init() {
        ArrayList<InitiatorHost> initiatorHosts = new ArrayList<InitiatorHost>();
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        String hosts[] = scsi.listDiscoveryAddress();
        if (hosts == null) {
            return;
        }
        for (int i = 0; i < hosts.length; i++) {
            InitiatorHost host = new InitiatorHost(hosts[i]);
//            InitiatorStaticConfig targets[] = InterfaceFactory.getSCSIInterfaceInstance().listTargetFromDiscovery(host.getAddress());
//            for (int j = 0; j < targets.length; j++) {
//                InitiatorStaticConfig staticConfig = targets[j];
//                InitiatorHostTarget target = new InitiatorHostTarget(staticConfig.targetName, staticConfig.targetAddress);
//                host.getTargets().add(target);
//            }
            initiatorHosts.add(host);
        }
        this.hosts = initiatorHosts;
    }

    public InitiatoriSCSITargetsBean getHostTargets() {
        return hostTargets;
    }

    public void setHostTargets(InitiatoriSCSITargetsBean hostTargets) {
        this.hostTargets = hostTargets;
    }

    public InitiatorHost getSelectHost() {
        return selectHost;
    }

    public void setSelectHost(InitiatorHost selectHost) {
        this.selectHost = selectHost;
    }

    public InitiatorHostTarget getTempTarget() {
        return tempTarget;
    }

    public void setTempTarget(InitiatorHostTarget tempTarget) {
        this.tempTarget = tempTarget;
    }

    public InitiatorHost getTempHost() {
        return tempHost;
    }

    public void setTempHost(InitiatorHost tempHost) {
        this.tempHost = tempHost;
    }

    public ArrayList<InitiatorHost> getHosts() {
        return hosts;
    }

    public void setHosts(ArrayList<InitiatorHost> hosts) {
        this.hosts = hosts;
    }

    public void doBeforeModifyTargetStatus(InitiatorHostTarget target) {
        this.tempTarget = target;
    }

    public void addTarget() {
        //this.tempTarget.setAddedOrNot(true);
        //InitiatorStaticConfig staticConfig = new InitiatorStaticConfig(this.tempTarget.getTargetName(), this.tempTarget.getTargetAddress());
        InitiatorStaticConfig staticConfig = new InitiatorStaticConfig(this.tempTarget.getTargetName(), this.tempTarget.getTargetAddress());
        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().addStaticConfig(staticConfig);
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addTargetFailed"), res.get("addTargetFailed")));
            return;
        }
        //this.init();
        updateSelectHost();
        hostTargets.init();
    }

    public void removeTarget() {
        InitiatorStaticConfig staticConfig = new InitiatorStaticConfig(this.tempTarget.getTargetName(), this.tempTarget.getTargetAddress());
        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().removeStaticConfig(staticConfig);
        if (!flag) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("deleteTargetFailed"), res.get("deleteTargetFailed")));
            return;
        }
        //this.init();
        updateSelectHost();
        hostTargets.init();
    }

    public void updateSelectHost() {
        InitiatorStaticConfig targets[] = InterfaceFactory.getSCSIInterfaceInstance().listTargetFromDiscovery(this.selectHost.getAddress());
        if (targets == null) {
            return;
        }
        ArrayList<InitiatorHostTarget> hostTargets = new ArrayList<InitiatorHostTarget>();
        InitiatorStaticConfig connectedConfig[] = InterfaceFactory.getSCSIInterfaceInstance().listStaticConfig();
        //ArrayList<InitiatorStaticConfig> connectedStaticConfig = new ArrayList<InitiatorStaticConfig>();
        ArrayList<String> connectedTargetStr = new ArrayList<String>();//包含每个target的名称、IP地址和端口号
        Debug.print("connected target ");
        Debug.print("connected target size : " + connectedConfig.length);
        for (int i = 0; i < connectedConfig.length; i++) {
            Debug.print("target name : " + connectedConfig[i].getTargetName());
            // connectedStaticConfig.add(connectedConfig[i]);
            String targetName = connectedConfig[i].getTargetName();
            String address = connectedConfig[i].getTargetAddress();
            connectedTargetStr.add(targetName + ":" + address);
        }
        Debug.print("host all target ");
        for (int i = 0; i < targets.length; i++) {
            InitiatorStaticConfig config = targets[i];
            String targetStr = config.getTargetName() + ":" + config.getTargetAddress();
            Debug.print(" target name : " + config.getTargetName());
            InitiatorHostTarget target;
            if (connectedTargetStr.contains(targetStr)) {
                target = new InitiatorHostTarget(config.targetName, config.targetAddress, true);
            } else {
                target = new InitiatorHostTarget(config.targetName, config.targetAddress, false);
            }

            hostTargets.add(target);
        }
        this.selectHost.setTargets(hostTargets);
    }

    public void doBeforeDeleteHost(InitiatorHost host) {
        //this.tempHost = host;
        InterfaceFactory.getSCSIInterfaceInstance().removeDiscoveryAddress(host.getAddress());
    }

    public void deleteHost() {
        //this.hosts.remove(this.tempHost);

        boolean flag = InterfaceFactory.getSCSIInterfaceInstance().removeDiscoveryAddress(this.tempHost.getAddress());
        if (!flag) {
            Debug.print("delete host " + flag);
        }
        this.init();
    }

    public void saveTemp(InitiatorHost host) {
        this.tempHost = host;
    }

    public void saveTempTarget(InitiatorHostTarget target) {
        this.tempTarget = target;
    }

    public void addHost(String ip, String port) {
        String address = ip + ":" + port;
        InitiatorHost host = new InitiatorHost(address);
        this.hosts.add(host);
    }

    public void onRowToggle(ToggleEvent event) {
        InitiatorHost host = (InitiatorHost) event.getData();
        this.selectHost = host;
        InitiatorStaticConfig targets[] = InterfaceFactory.getSCSIInterfaceInstance().listTargetFromDiscovery(host.getAddress());
        if (targets == null) {
            return;
        }
        ArrayList<InitiatorHostTarget> hostTargets = new ArrayList<InitiatorHostTarget>();
        InitiatorStaticConfig connectedConfig[] = InterfaceFactory.getSCSIInterfaceInstance().listStaticConfig();
        //ArrayList<InitiatorStaticConfig> connectedStaticConfig = new ArrayList<InitiatorStaticConfig>();
        ArrayList<String> connectedTargetStr = new ArrayList<String>();//包含每个target的名称、IP地址和端口号
        Debug.print(" connected target ");
        Debug.print("connected target size : " + connectedConfig.length);
        for (int i = 0; i < connectedConfig.length; i++) {
            Debug.print("target name : " + connectedConfig[i].getTargetName());
            // connectedStaticConfig.add(connectedConfig[i]);
            String targetName = connectedConfig[i].getTargetName();
            String address = connectedConfig[i].getTargetAddress();
            connectedTargetStr.add(targetName + ":" + address);
        }
        Debug.print("host all target ");
        ArrayList<String> targetNames = new ArrayList<String>();
        for (int i = 0; i < targets.length; i++) {
            InitiatorStaticConfig config = targets[i];
            if(targetNames.contains(config.getTargetName())) {
                continue;
            }
            targetNames.add(config.getTargetName());
            String targetStr = config.getTargetName() + ":" + config.getTargetAddress();
            Debug.print("target name : " + config.getTargetName());
            InitiatorHostTarget target;
            if (connectedTargetStr.contains(targetStr)) {
                target = new InitiatorHostTarget(config.targetName, config.targetAddress, true);
            } else {
                target = new InitiatorHostTarget(config.targetName, config.targetAddress, false);
            }

            hostTargets.add(target);
        }
        host.setTargets(hostTargets);
    }

    public void doBeforeDeleteTarget(InitiatorHostTarget target) {
        InitiatorStaticConfig config = new InitiatorStaticConfig(target.getTargetName(), target.getTargetAddress());
        InterfaceFactory.getSCSIInterfaceInstance().removeStaticConfig(config);
    }
}
