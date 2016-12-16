/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.service;

import com.marstor.msa.common.bean.HostGroup;
import com.marstor.msa.common.bean.TargetInformation;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.bean.iSCSIInitiator;
import com.marstor.msa.common.web.SCSIInterface;
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

public class SCSIService {
    private SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
    private String error;
    
    /**
     * ��ȡ���л��ǲ��ṩ��Target
	 *
     * @return ʧ��null
     */
    public Target[] getAllTarget(){
        TargetInformation[] allTargetInfo = scsi.getAllTarget();
        Target[] allTarget = null;
        if(allTargetInfo==null) {
            this.setError(scsi.getLastError());
            return null;
        }
        allTarget= new Target[allTargetInfo.length];
        Target target = null;
        for(int i=0;i<allTargetInfo.length;i++){
            target = new Target();
            target.setAlias_name(allTargetInfo[i].getAliasName());
            if(allTargetInfo[i].getAuthType() == Constant.CHAP){
                target.setChap_state("on");
            }else{
                 target.setChap_state("off");
            }
            
            target.setChap_secret(allTargetInfo[i].getChapSecret());
            target.setChap_user(allTargetInfo[i].getChapUser());
            target.setInitiator_name(allTargetInfo[i].getInitiatorName());
            target.setTarget_name(allTargetInfo[i].getTargetName());
            target.setBind_ip(allTargetInfo[i].getTpgs());
            allTarget[i] = target;
        }
        
        return allTarget;
    }
    
    /**
     * �������ǲ�iSCSI Target
     *
     * @param aliasName target����
     * @param targetName target����
     * @return
     */
    public Target createiSCSITarget(String aliasName, String targetName) {
        Target target = null;
        boolean isCreate = scsi.createiSCSITarget(aliasName, targetName);
        if (isCreate) {
            Target[] allTarget = getAllTarget();

            if (allTarget != null && allTarget.length > 0) {
                for (Target targetObj : allTarget) {
                    if (targetObj.getTarget_name().equals(targetName)) {
                        target = targetObj;
                        break;
                    }
                }
            }
        }else{
             this.setError(scsi.getLastError());
        }
        return target;
    }
    
    /**
     * ɾ�����ǲ�iSCSI Target
     *
     * @param aliasName target����
     * @param targetName target����
     * @return
     */
    public boolean deleteiSCSITarget(String aliasName, String targetName){
//        Target target = null;
//        Target[] allTarget = getAllTarget();
//
//        if (allTarget != null && allTarget.length > 0) {
//            for (Target targetObj : allTarget) {
//                if (targetObj.targetName.equals(targetName)) {
//                    target = targetObj;
//                    break;
//                }
//            }
//        }
//         if(target == null){
//            return false;
//        }
        boolean isDelete = scsi.deleteiSCSITarget(aliasName, targetName);
        if(!isDelete){
             this.setError(scsi.getLastError());
        }
       
        return isDelete;
    }
    
    /**
     * ��ȡ���ǲտ��õ�iSCSI Target����
     *
     * @return ʧ��null
     */
    public String getAvailableAlias(){
        String availableAlias = scsi.getAvailableAlias();
        if(availableAlias == null){
            this.setError(scsi.getLastError());
        }
        return availableAlias;
        
    }
    
    /**
     * ��ȡ���ǲտ��õ�iSCSI Target����
     *
     * @return ʧ��null
     */
    public String getAvailableTargetName(){
        String availableTargetName = scsi.getAvailableTargetName();
        if(availableTargetName == null){
             this.setError(scsi.getLastError());
        }
        return availableTargetName;
    }
    
    /**
     * ���û��ǲ�iSCSI Target��IP��Ŀ���Ż��飩
     *
     * @param tpg ����Ϊdefault����Ϊ192.168.1.1,192.168.1.2
     * @param targetName Target����
     * @return
     */
    public Target bindTargetTPG(String tpg, String targetName){
         Target target = null;
        boolean isBind = scsi.ModifyTargetTPG(tpg, targetName);
        if (isBind) {
            Target[] allTarget = getAllTarget();

            if (allTarget != null && allTarget.length > 0) {
                for (Target targetObj : allTarget) {
                    if (targetObj.getTarget_name().equals(targetName)) {
                        target = targetObj;
                        break;
                    }
                }
            }
        }else{
             this.setError(scsi.getLastError());
        }
        return target;
    }
    
    /**
     * ��ȡ���ǲ�iSCSI������
     *
     * @return ʧ��null
     */
    public HostGroupInfo[] getiSCSIHostGroups() {
        HostGroup[] hostGroups = scsi.getiSCSIHostGroups();
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
     * �������ǲ�iSCSI������
     *
     * @param name ��������
     * @return
     */
    public HostGroupInfo createHostGroup(String name){
         HostGroupInfo hostGroup = null;
        boolean isCreate = scsi.createHostGroup(name);
        if (isCreate) {
            HostGroupInfo[] allHostGroupt = getiSCSIHostGroups();

            if (allHostGroupt != null && allHostGroupt.length > 0) {
                for (HostGroupInfo hostGroupObj : allHostGroupt) {
                    if (hostGroupObj.getGroup_name().equals(name)) {
                        hostGroup = hostGroupObj;
                        break;
                    }
                }
            }
        }else{
             this.setError(scsi.getLastError());
        }
        return hostGroup;
    }
    
    /**
     * ɾ�����ǲ�iSCSI������
     *
     * @param name ��������
     * @return
     */
    public boolean deleteHostGroup(String name){
        HostGroupInfo hostGroup = null;
        HostGroupInfo[] allHostGroupt = getiSCSIHostGroups();

        if (allHostGroupt != null && allHostGroupt.length > 0) {
            for (HostGroupInfo hostGroupObj : allHostGroupt) {
                if (hostGroupObj.getGroup_name().equals(name)) {
                    hostGroup = hostGroupObj;
                    break;
                }
            }
        }
        if(hostGroup == null){
            this.setError("this hostgroup is not exit.");
            return false;
        }
        ViewInformation[] selectGroupView = scsi.getHostGroupLinkView(name);
        if (selectGroupView != null) {
            this.setError("exit lun_mapping.");

            return false;
        }
        
        if (hostGroup.getGroup_member() != null && hostGroup.getGroup_member().size() > 0) {
            boolean flag;
            for (String group_member : hostGroup.getGroup_member()) {
                flag = this.deleteiSCSIInitiator(group_member);
                if (!flag) {
                    this.setError(scsi.getLastError());

                    return false;
                }
            }
        }
 
        boolean isDelete = scsi.deleteHostGroup(name);
        if(!isDelete){
             this.setError(scsi.getLastError());
        }
        return isDelete;
    }
    
    /**
     * ��ȡiSCSI initiator
     *
     * @return ʧ��null
     */
    public ISCSIInitiator[] getiSCSIInitiator(){
        iSCSIInitiator[] allISCSIInitiator = scsi.getiSCSIInitiator();
        ISCSIInitiator[] allInitiator = null;
        if (allISCSIInitiator == null) {
            this.setError(scsi.getLastError());
            return null;
        }
        allInitiator = new ISCSIInitiator[allISCSIInitiator.length];
        ISCSIInitiator iSCSIInitiator = null;
        for (int i = 0; i < allISCSIInitiator.length; i++) {
            iSCSIInitiator = new ISCSIInitiator();
            iSCSIInitiator.setName(allISCSIInitiator[i].getName());
            iSCSIInitiator.setUser_name(allISCSIInitiator[i].getUsername());
            iSCSIInitiator.setPassword(allISCSIInitiator[i].getPassword());
            allInitiator[i] = iSCSIInitiator;
        }
        
        return allInitiator;
    }
    
     /**
     * ���iscsi initiator
     *
     * @param name initiator����
     * @return
     */
//    public ISCSIInitiator createiSCSIInitiator(String name){
//         ISCSIInitiator initiator = null;
//        boolean isCreate = scsi.createiSCSIInitiator(name);
//        if (isCreate) {
//            ISCSIInitiator[] allInitiator = getiSCSIInitiator();
//
//            if (allInitiator != null && allInitiator.length > 0) {
//                for (ISCSIInitiator initiatorObj : allInitiator) {
//                    if (initiatorObj.getName().equals(name)) {
//                        initiator = initiatorObj;
//                        break;
//                    }
//                }
//            }
//        }else{
//            this.setError(scsi.getLastError());
//        }
//        return initiator;
//    }
    
     /**
     * ���iscsi initiator
     *
     * @param name initiator����
     * @return
     */
    public boolean createiSCSIInitiator(String name){
         
        boolean isCreate = scsi.createiSCSIInitiator(name);
        if (!isCreate) {
         
            this.setError(scsi.getLastError());
        }
        return isCreate;
    }
    
    /**
     * ɾ��iscsi initiator
     *
     * @param name initiator����
     * @return
     */
    public boolean deleteiSCSIInitiator(String name){
//        ISCSIInitiator initiator = null;
//        ISCSIInitiator[] allInitiator = getiSCSIInitiator();
//
//        if (allInitiator != null && allInitiator.length > 0) {
//            for (ISCSIInitiator initiatorObj : allInitiator) {
//                if (initiatorObj.getName().equals(name)) {
//                    initiator = initiatorObj;
//                    break;
//                }
//            }
//        }
//        if(initiator == null){
//            return null;
//        }
        boolean isDelete = scsi.deleteiSCSIInitiator(name);
        if(!isDelete){
            this.setError(scsi.getLastError());
        }
        return isDelete;
    }
    
    /**
     * ���ǲ�iSCSI��������ӳ�Ա
     *
     * @param groupname ����
     * @param member ��Ա��
     * @return
     */
    public HostGroupInfo addHostGroupMember(String groupname, String member) {
        HostGroupInfo hostGroup = null;
        String array[] = new String[1];
        array[0] = member;
        boolean isAddMember = scsi.addHostGroupMember(groupname, array);
        if (isAddMember) {
            HostGroupInfo[] allHostGroupt = getiSCSIHostGroups();

            if (allHostGroupt != null && allHostGroupt.length > 0) {
                for (HostGroupInfo hostGroupObj : allHostGroupt) {
                    if (hostGroupObj.getGroup_member() == null || hostGroupObj.getGroup_member().size()< 1) {
                        continue;
                    }
                    for (String hostMember : hostGroupObj.getGroup_member()) {
                        if (hostGroupObj.getGroup_name().equals(groupname) && hostMember.equals(member)) {
                            hostGroup = hostGroupObj;
                            break;
                        }
                    }

                }
            }
        }else{
            this.setError(scsi.getLastError());
        }
        return hostGroup;
    }
    
    /**
     * ���ǲ�iSCSI������ɾ����Ա
     *
     * @param groupname ����
     * @param member ��Ա��
     * @return
     */
    public boolean removeHostGroupMember(String groupname, String member) {
        String array[] = new String[1];
        array[0] = member;
        boolean isRemove = scsi.removeHostGroupMember(groupname, array);
        if(!isRemove){
            this.setError(scsi.getLastError());
        }
        return isRemove;
    }
    
    /**
     * ����iSCSI Target CHAP �û��� ����
     *
     * @param stat_up �Ƿ���chap��֤��ture������false�ر�
     * @param user_name �û���
     * @param password ����
     * @param target_name Target����
     * @return
     */
    public Target setTargetChap(boolean stat_up, String user_name, String password, String target_name) {

        Target target = null;
        boolean flag = false;
        String cancelPasswd = "111111111111";
        boolean isStart = stat_up;
//        if (stat_up.trim().equalsIgnoreCase("true")) {
//            isStart = true;
//        }
        if (isStart) {
            flag = scsi.setTargetCHAP(Constant.CHAP, target_name);
            if (!flag) {
//                System.out.println("setTargetCHAP " + flag);
                 this.setError(scsi.getLastError());
                return null;
            }

            
            if ((user_name == null && password == null) || (user_name == null && password.trim().equals("")) || ( password==null && user_name.trim().equals(""))|| (user_name.trim().equals("") && password.trim().equals(""))) {
                flag = scsi.setTargetPassword("none", cancelPasswd, target_name);
                if (!flag) {
//                    System.out.println("setTargetPasswd " + flag);
                     this.setError(scsi.getLastError());
                    return null;
                }
            } else {
                if (user_name == null || password == null || user_name.trim().equals("") || password.trim().equals("")) {
                    this.setError("user_name or password can not null.");
                    return null;
                }
                flag = scsi.setTargetPassword(user_name, password, target_name);
                if (!flag) {
//                    System.out.println(" setTargetPasswd " + flag);
                    this.setError(scsi.getLastError());
                    return null;
                }
            }
        } else {
            flag = scsi.setTargetCHAP(Constant.NONE, target_name);
            if (!flag) {
//                System.out.println("setTargetCHAP " + flag);
                 this.setError(scsi.getLastError());
                return null;
            }
            flag = scsi.setTargetPassword("none", cancelPasswd, target_name);
            if (!flag) {
//                System.out.println("setTargetPasswd " + flag);
                 this.setError(scsi.getLastError());
                return null;
            }
        }

        Target[] allTarget = getAllTarget();
        if (allTarget != null && allTarget.length > 0) {
            for (Target targetObj : allTarget) {
                if (targetObj.getTarget_name().equals(target_name)) {
                    target = targetObj;
                    break;
                }
            }
        }

        return target;
    }
    
    /**
     * ����initiatorCHAP
     *
     * @param stat_up �Ƿ���chap��֤��ture������false�ر�
     * @param user_name �û��� "<none>"��ʾȡ������
     * @param password ����
     * @param initiator_name initiator����
     * @return
     */
    public ISCSIInitiator setInitiatorChap(boolean stat_up, String user_name, String password, String initiator_name){
 
        boolean isStart = stat_up;
        
        if (isStart) {
            if (user_name == null || password == null || user_name.trim().equals("") || password.trim().equals("")) {
                this.setError("user_name or password can not null.");
                return null;
            }
        }
      
        if (!isStart) {
            user_name = "<none>";
            password = "";
        }
        boolean flag = scsi.setiSCSIInitiatorChap(user_name, password, initiator_name);
        if (!flag) {
            
//            System.out.println("set initiator chap " + flag);
             this.setError(scsi.getLastError());
            return null;
        }

        ISCSIInitiator initiator = null;
        if (flag) {
             ISCSIInitiator[] allInitiator = getiSCSIInitiator();

            if (allInitiator != null && allInitiator.length > 0) {
                for (ISCSIInitiator initiatorObj : allInitiator) {
                    if (initiatorObj.getName().equals(initiator_name)) {
                        initiator = initiatorObj;
                        break;
                    }
                }
            }
        }
        return initiator;
    }
     
     /**
     * ��ȡָ��Lu��LUNӳ��
     *
     * @param lu lu�� ��600144F0B0927F000000518C4F4F0024
     * @return ʧ��null hostgroupnameΪAll��ʾ���пͻ�����
     */
    public View[] getLUNView(String lu){
        ViewInformation[] viewInformations = scsi.getLUNView(lu);
        View[] allView = null;
        if (viewInformations == null) {
             this.setError(scsi.getLastError());
            return null;
        }
        allView = new View[viewInformations.length];
        View view = null;
        for (int i = 0; i < viewInformations.length; i++) {
            view = new View();
            view.setLun(viewInformations[i].getLUN());
            view.setLun_mapping(viewInformations[i].getEntry());
            view.setDisk_lu(viewInformations[i].getGUID());
            view.setHost_group_name(viewInformations[i].getHostGroupName());
            view.setTarget_group_name(viewInformations[i].getTargetGroupName());

            allView[i] = view;
        }

        return allView;
    }
    
    
     /**
     * ���LUNӳ��
     *
     * @param hostgroupName �ͻ������� ""���ַ�����ʾ���пͻ�����
     * @param lu lu�� ��600144F0B0927F000000518C4F4F0024
     * @param LUN LUN�ţ�-1Ϊ��ָ��
     * @return ʧ��null
     */
    public View[] addView(String hostgroupName, int LUN, String lu){
        View[] allView = null;
        boolean isAdd = scsi.addView(hostgroupName, LUN, lu);
        if (isAdd) {
            allView = getLUNView(lu); 
        }else{
             this.setError(scsi.getLastError());
        }
        return allView;
    }
    
    /**
     * ɾ��LUNӳ��
     *
     * @param viewEntry view���
     * @param lu lu�� ��600144F0B0927F000000518C4F4F0024
     * @return ʧ��null
     */
    public boolean removeView(String lu, int viewEntry){
          boolean isRemove = scsi.removeView(lu, viewEntry);
          if(!isRemove){
               this.setError(scsi.getLastError());
          }
          return isRemove;
    }
    
     /**
     * ��ȡָ��HostGroup�󶨿ɷ����LUN��
     *
     * @param hostgroupName �ͻ�������
     * @return null����
     */
    public int[] getHostGroupAvailableLuns(String hostgroupName){
        int[] lun = scsi.getHostGroupAvailableLuns(hostgroupName);
        if(lun == null){
            this.setError(scsi.getLastError());
        }
        return lun;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
}
