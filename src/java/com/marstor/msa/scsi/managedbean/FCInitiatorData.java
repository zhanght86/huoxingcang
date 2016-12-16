/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean;

import com.marstor.msa.common.bean.FCInitiatorInformation;
import com.marstor.msa.common.bean.FCInitiatorTargetInformation;
import com.marstor.msa.common.bean.InitiatorTargetDevice;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.scsi.model.FCInitiator;
import com.marstor.msa.scsi.model.FCTargetMappingIntiator;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.ToggleEvent;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class FCInitiatorData {

    private ArrayList<FCInitiator> initiators = new ArrayList<FCInitiator>();
    private int targetNum=0;
    public FCInitiatorData() {

        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        FCInitiatorInformation infos[] = scsi.listFCInitiator();
//        infos = new FCInitiatorInformation[1];
//        infos[0] = new FCInitiatorInformation();
//        infos[0].name ="wwn.12345EFD79932JKL";
//        infos[0].CurrentSpeed ="2Gb";
//        FCInitiatorTargetInformation fctargetInfos[]  = new FCInitiatorTargetInformation[3];
//        fctargetInfos[0] = new FCInitiatorTargetInformation();
//        fctargetInfos[0].targetName = "12345678";
//        
//        ArrayList<InitiatorTargetDevice> devices = new ArrayList<InitiatorTargetDevice>();
//        InitiatorTargetDevice device = new InitiatorTargetDevice();
//        device.setDeviceName("device123456");
//        device.setProduct("product123456");
//        devices.add(device);
//        fctargetInfos[0].setDevices(devices);
//        
//        fctargetInfos[1] = new FCInitiatorTargetInformation();
//        fctargetInfos[1].targetName = "12345678";
//        
//        //ArrayList<InitiatorTargetDevice> devices = new ArrayList<InitiatorTargetDevice>();
//        ArrayList<InitiatorTargetDevice> devices1 = new ArrayList<InitiatorTargetDevice>();
//        InitiatorTargetDevice device1 = new InitiatorTargetDevice();
//        device1.setDeviceName("device123456");
//        device1.setProduct("product123456");
//        devices1.add(device1);
//        fctargetInfos[1].setDevices(devices1);
//        
//         fctargetInfos[2] = new FCInitiatorTargetInformation();
//        fctargetInfos[2].targetName = "00039090678";
//
//        ArrayList<InitiatorTargetDevice> devices2 = new ArrayList<InitiatorTargetDevice>();
//        InitiatorTargetDevice device2 = new InitiatorTargetDevice();
//        device2.setDeviceName("device123456");
//        device2.setProduct("product123456");
//        devices2.add(device2);
//        fctargetInfos[2].setDevices(devices2);
//         FCInitiator initiator = new FCInitiator(infos[0].getName(),infos[0].getCurrentSpeed(),"已连接",fctargetInfos);
//         this.initiators.add(initiator);
         //FCInitiator initiator1 = new FCInitiator("wwn.5678EFD79932JKL","1Gb","已连接",fctargetInfos);
        
        if (infos == null) {
            return;
        }
        for (int i = 0; i < infos.length; i++) {
            FCInitiatorInformation fc = infos[i];
            FCInitiatorTargetInformation targetInfos[] = scsi.listFCInitiatorTarget(fc.getName());
            FCInitiator initiator = new FCInitiator(fc.getName(), fc.getCurrentSpeed(), fc.getState(),targetInfos);
            initiators.add(initiator);
        }

    }

    public int getTargetNum() {
        return targetNum;
    }

    public void setTargetNum(int targetNum) {
        this.targetNum = targetNum;
    }

    public ArrayList<FCInitiator> getInitiators() {
        return initiators;
    }

    public void setInitiators(ArrayList<FCInitiator> initiators) {
        this.initiators = initiators;
    }

    public void onRowToggle(ToggleEvent event) {
        FCInitiator initiator = (FCInitiator) event.getData();
//        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//        FCInitiatorTargetInformation infos[] = scsi.listFCInitiatorTarget(initiator.getName());
//        ArrayList<FCInitiatorTargetInformation> targetInfos = new ArrayList<FCInitiatorTargetInformation>();
//        for (int i = 0; i < infos.length; i++) {
//            targetInfos.add(infos[i]);
//        }
//        initiator.setTargetInfos(targetInfos);
        this.targetNum= initiator.getTargetSize();
    }
}
