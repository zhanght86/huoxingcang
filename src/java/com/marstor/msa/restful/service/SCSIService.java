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
     * 获取所有火星舱提供的Target
	 *
     * @return 失败null
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
     * 创建火星舱iSCSI Target
     *
     * @param aliasName target别名
     * @param targetName target名称
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
     * 删除火星舱iSCSI Target
     *
     * @param aliasName target别名
     * @param targetName target名称
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
     * 获取火星舱可用的iSCSI Target别名
     *
     * @return 失败null
     */
    public String getAvailableAlias(){
        String availableAlias = scsi.getAvailableAlias();
        if(availableAlias == null){
            this.setError(scsi.getLastError());
        }
        return availableAlias;
        
    }
    
    /**
     * 获取火星舱可用的iSCSI Target名称
     *
     * @return 失败null
     */
    public String getAvailableTargetName(){
        String availableTargetName = scsi.getAvailableTargetName();
        if(availableTargetName == null){
             this.setError(scsi.getLastError());
        }
        return availableTargetName;
    }
    
    /**
     * 设置火星舱iSCSI Target绑定IP（目标门户组）
     *
     * @param tpg 不绑定为default，绑定为192.168.1.1,192.168.1.2
     * @param targetName Target名称
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
     * 获取火星舱iSCSI主机组
     *
     * @return 失败null
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
     * 创建火星舱iSCSI主机组
     *
     * @param name 主机组名
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
     * 删除火星舱iSCSI主机组
     *
     * @param name 主机组名
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
     * 获取iSCSI initiator
     *
     * @return 失败null
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
     * 添加iscsi initiator
     *
     * @param name initiator名称
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
     * 添加iscsi initiator
     *
     * @param name initiator名称
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
     * 删除iscsi initiator
     *
     * @param name initiator名称
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
     * 火星舱iSCSI主机组添加成员
     *
     * @param groupname 组名
     * @param member 成员名
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
     * 火星舱iSCSI主机组删除成员
     *
     * @param groupname 组名
     * @param member 成员名
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
     * 设置iSCSI Target CHAP 用户名 密码
     *
     * @param stat_up 是否开启chap认证，ture开启，false关闭
     * @param user_name 用户名
     * @param password 密码
     * @param target_name Target名称
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
     * 设置initiatorCHAP
     *
     * @param stat_up 是否开启chap认证，ture开启，false关闭
     * @param user_name 用户名 "<none>"表示取消设置
     * @param password 密码
     * @param initiator_name initiator名称
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
     * 获取指定Lu的LUN映射
     *
     * @param lu lu名 如600144F0B0927F000000518C4F4F0024
     * @return 失败null hostgroupname为All表示所有客户端组
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
     * 添加LUN映射
     *
     * @param hostgroupName 客户端组名 ""空字符串表示所有客户端组
     * @param lu lu名 如600144F0B0927F000000518C4F4F0024
     * @param LUN LUN号，-1为不指定
     * @return 失败null
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
     * 删除LUN映射
     *
     * @param viewEntry view序号
     * @param lu lu名 如600144F0B0927F000000518C4F4F0024
     * @return 失败null
     */
    public boolean removeView(String lu, int viewEntry){
          boolean isRemove = scsi.removeView(lu, viewEntry);
          if(!isRemove){
               this.setError(scsi.getLastError());
          }
          return isRemove;
    }
    
     /**
     * 获取指定HostGroup绑定可分配的LUN号
     *
     * @param hostgroupName 客户端组名
     * @return null出错
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
