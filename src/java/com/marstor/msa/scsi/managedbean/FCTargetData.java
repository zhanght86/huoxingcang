/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.managedbean;

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.scsi.model.FCTarget;
import com.marstor.msa.common.bean.TargetInformation;
import com.marstor.msa.common.web.SCSIInterface;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.marstor.msa.util.InterfaceFactory;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "FCTargetData")
@SessionScoped
public class FCTargetData {

    private ArrayList<FCTarget> targets = new ArrayList<FCTarget>();

    public FCTargetData() {
//        FCTarget target;
//        target = new FCTarget("www.10000000C97DAFB2", "www.10000000C97DAFB2", "online");
//        targets.add(target);
//        target = new FCTarget("www.10000000C97DAFB5", "www.10000000C97DAFB5", "offline");
//         targets.add(target);
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        TargetInformation[] allTargetInfo = scsi.getAllTarget();
        if(allTargetInfo==null) {
            return ;
        }
        Debug.print(" get fc targets : " + allTargetInfo );
        for (TargetInformation targetInfo : allTargetInfo) {
             Debug.print(" target : " + targetInfo.getTargetName() );
            if (!targetInfo.getProtocal().equals("iscsi")) {
                 Debug.print(" fc target name : " +  targetInfo.getTargetName() );
                 Debug.print("fc target state : " +  targetInfo.getState() );
                FCTarget target = new FCTarget(targetInfo.targetName, targetInfo.aliasName,targetInfo.state);
                targets.add(target);
            }
            //  targets.add(targetInfo.);
        }
    }

    public ArrayList<FCTarget> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<FCTarget> targets) {
        this.targets = targets;
    }
    public void addTarget(String  name,String  alias) {
        FCTarget  target = new FCTarget(name, alias, "online");
        this.targets.add(target);
    }
}
