/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.disk.managedbean;

import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.bean.VirtualDiskInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.DiskInterface;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.disk.model.DiskDetail;
import com.marstor.msa.disk.model.ViewInfo;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "lunmapping_setBean")
@ViewScoped
public class Lunmapping_setBean implements Serializable {

    public String diskname;
    public String zfsPath;
    public String poolname;
    public String selectGroupName;
    private List<String> groupNameList;
     private List<String> nameList;
    public String lunnum;
    public boolean cantnum;
    public boolean isauto;
    public List<ViewInfo> viewList;
    MSAResource resources = new MSAResource();
    /**
     * Creates a new instance of Lunmapping_setBean
     */
    public Lunmapping_setBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
//        diskname = request.getParameter("diskName");
        zfsPath = request.getParameter("zfsPath");
        poolname = request.getParameter("poolName");
        System.out.println("zfsPath="+zfsPath+",poolName="+poolname);
        initGroupNames();
        isauto = true;
        changeBooleanCheckbox(true);
        initViewInformation();
        initHostGroupNames();
    }

    public void initGroupNames() {
        nameList = new ArrayList();
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        String[] groupNames = scsi.getAllHostGroupNames();
        System.out.println("no value");
        if (groupNames != null && groupNames.length > 0) {
            nameList.add(resources.get("disk.lunmapping_set", "allhostgroup"));
            for (int i = 0; i < groupNames.length; i++) {
                nameList.add(groupNames[i]);
            }
        }
        System.out.println("nameList1="+nameList.size());
    }
    
    public void initViewInformation() {
        
        DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();

        //获取磁盘池下所有的虚拟磁盘信息
        VirtualDiskInformation[] disks = diskIn.getDiskFromPool(zfsPath);
//        boolean havaGuid = false;
        if (disks != null && disks.length > 0) {

            viewList = new ArrayList();
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            System.out.println("guid[0]=" + disks[0].getGUID());
            ViewInformation[] views = scsi.getLUNView(disks[0].getGUID());
            System.out.println("views==null=" + (views == null));
            ViewInfo view = null;
            if (views != null && views.length > 0) {
                for (int k = 0; k < views.length; k++) {
                    view = new ViewInfo();
                    view.setEntry(views[k].getEntry());
                    view.setGUID(views[k].getGUID());
                    view.setHostGroupName(views[k].getHostGroupName());
                    view.setLUN(views[k].getLUN());
                    view.setTargetGroupName(views[k].getTargetGroupName());
                    if (views[k].getHostGroupName().equalsIgnoreCase("All")) {
                        view.setType(resources.get("disk.lunmapping_set", "all"));
                        view.setHostGroupNameStr(resources.get("disk.lunmapping_set", "allhostgroup"));
                    } else {
                        String typeStr = views[k].getHostGroupName().split("_")[0];
                        view.setType(typeStr);
                        view.setHostGroupNameStr(views[k].getHostGroupName());
                    }

                    viewList.add(view);
                }
            }
 System.out.println("viewList="+viewList.size());
        }
        
        
        
        
        
//        viewList = new ArrayList();
//        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//        ViewInformation[] views = scsi.getLUNView(guid);
//        ViewInfo view = null;
//        if (views != null && views.length > 0) {
//            for (int i = 0; i < views.length; i++) {
//                view = new ViewInfo();
//                view.setEntry(views[i].getLUN());
//                view.setGUID(views[i].getGUID());
//                view.setHostGroupName(views[i].getHostGroupName());
//                view.setLUN(views[i].getLUN());
//                view.setTargetGroupName(views[i].getTargetGroupName());
//                if (views[i].getHostGroupName().equalsIgnoreCase("All")) {
//                    view.setType("所有");
//                    view.setHostGroupNameStr("所有客户端组");
//                } else {
//                    String typeStr = views[i].getHostGroupName().split("_")[0];
//                    view.setType(typeStr);
//                    view.setHostGroupNameStr(views[i].getHostGroupName());
//                }
//
//                viewList.add(view);
//            }
//        }
    }
    
     public void initHostGroupNames() {
        groupNameList = new ArrayList();
//        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//        String[] groupNames = scsi.getAllHostGroupNames();
         if (nameList != null && nameList.size() > 0) {
             if (viewList != null && viewList.size() > 0) {

                 boolean havaAll = false;
                 for (int k = 0; k < viewList.size(); k++) {
                     if (viewList.get(k).getHostGroupNameStr().equalsIgnoreCase(resources.get("disk.lunmapping_set", "allhostgroup"))) {
//                         groupNameList = new ArrayList();
                         havaAll = true;
                         System.out.println("groupNameList=0");
                         break;
                     }
                 }
                 if (havaAll == false) {
                     for (int i = 0; i < nameList.size(); i++) {
                         boolean same = false;
                         for (int j = 0; j < viewList.size(); j++) {
                             if (nameList.get(i).equals(viewList.get(j).getHostGroupNameStr())) {
                                 same = true;
                                 break;
                             }
                         }

                         if (same) {
                             continue;
                         } else {
                             groupNameList.add(nameList.get(i));
                         }

                     }
                     System.out.println("groupNameList="+groupNameList.size());
                 }
             } else {
                groupNameList = nameList;
                 System.out.println("groupNameList="+groupNameList.size());
            }

        }
    }

    public void changeBooleanCheckbox(boolean isSt) {
        if (isSt == true) {
            cantnum = true;

        } else {
            cantnum = false;
        }
    }

    public String addView() {
        if (groupNameList != null && groupNameList.size() > 0) {
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.lunmapping_set", "hostgroup_null"), ""));
            return null;
        }
        String returnStr = null;
        int lunNum = -1;
        if (isauto) {
            lunNum = -1;
            String hostGroup = "";
            if (selectGroupName.equalsIgnoreCase(resources.get("disk.lunmapping_set", "allhostgroup"))) {
                if (viewList != null && viewList.size() > 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.lunmapping_set", "addallhostgroup_no"), ""));
                    return null;
                }
                hostGroup = "";
            } else {
                hostGroup = selectGroupName;
            }
            System.out.println("hostGroup=" + hostGroup);
            System.out.println("lunNum=" + lunNum);

            //获取磁盘池下所有的虚拟磁盘信息
            DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
            VirtualDiskInformation[] disks = diskIn.getDiskFromPool(zfsPath);
            if (disks != null && disks.length > 0) {

                for (int k = 0; k < disks.length; k++) {
                    String guid = disks[k].getGUID();
                    System.out.println("guid=" + guid);
                    SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
                    boolean addVie = scsi.addView(hostGroup, lunNum, guid);
                    if (addVie == false) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.lunmapping_set", "addview_fail"), ""));
                        return null;
                    } else {
//                        String param = "zfsPath=" + zfsPath + "&" + "poolName=" + poolname + "&" + "GUID=" + "";
//                        returnStr = "lunmapping?faces-redirect=true&amp;" + param;
                        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "添加LUN映射成功。", ""));
                    }
                }
                 System.out.println("addview succeed");
                String param = "zfsPath=" + zfsPath + "&" + "poolName=" + poolname + "&" + "GUID=" + "";
                returnStr = "lunmapping?faces-redirect=true&amp;" + param;
                
            }

        } else {
            if (lunnum.equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.lunmapping_set", "lunnum_limit"), ""));
                return null;

            }
            if (ValidateUtility.checkNum(lunnum.trim(), true)) {
                lunNum = Integer.valueOf(lunnum.trim());
                if (lunNum < 0 || lunNum > 255) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.lunmapping_set", "lunnum_limit"), ""));
                    return null;
                } else {
                    String hostGroup = "";
                    if (selectGroupName.equalsIgnoreCase("所有客户端组")) {
                        if (viewList != null && viewList.size() > 0) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.lunmapping_set", "addallhostgroup_no"), ""));
                            return null;
                        }
                        hostGroup = "";
                    } else {
                        hostGroup = selectGroupName;
                    }
                    System.out.println("hostGroup=" + hostGroup);
                    System.out.println("lunNum=" + lunNum);
                    //获取磁盘池下所有的虚拟磁盘信息
                    DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
                    VirtualDiskInformation[] disks = diskIn.getDiskFromPool(zfsPath);
                    if (disks != null && disks.length > 0) {

                        for (int k = 0; k < disks.length; k++) {
                            String guid = disks[k].getGUID();
                            System.out.println("guid=" + guid);
                            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
                            boolean addVie = scsi.addView(hostGroup, lunNum, guid);
                            if (addVie == false) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,resources.get("disk.lunmapping_set", "addview_fail"), ""));
                                return null;
                            } 
                            lunNum = lunNum + 1;  //手动分配，每次LUN号加1
//                            else {
//                                String param = "zfsPath=" + zfsPath + "&" + "poolName=" + poolname + "&" + "GUID=" + "";
//                                returnStr = "lunmapping?faces-redirect=true&amp;" + param;
//                                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "添加LUN映射成功。", ""));
//                            }
                        }
                        System.out.println("addview succeed");
                        String param = "zfsPath=" + zfsPath + "&" + "poolName=" + poolname + "&" + "GUID=" + "";
                        returnStr = "lunmapping?faces-redirect=true&amp;" + param;

                    }

                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.lunmapping_set", "lunnum_limit"), ""));
                return null;
            }
        }
        return returnStr;

    }
    
    public String returnLUN(){
        String param = "zfsPath=" + zfsPath + "&" + "poolName=" + poolname + "&" + "GUID=" + "" ;
        return "lunmapping?faces-redirect=true&amp;" + param;
    }



    public String getSelectGroupName() {
        return selectGroupName;
    }

    public void setSelectGroupName(String selectGroupName) {
        this.selectGroupName = selectGroupName;
    }

    public List<String> getGroupNameList() {
        return groupNameList;
    }

    public void setGroupNameList(List<String> groupNameList) {
        this.groupNameList = groupNameList;
    }

    public String getLunnum() {
        return lunnum;
    }

    public void setLunnum(String lunnum) {
        this.lunnum = lunnum;
    }

    public boolean isCantnum() {
        return cantnum;
    }

    public void setCantnum(boolean cantnum) {
        this.cantnum = cantnum;
    }

    public boolean isIsauto() {
        return isauto;
    }

    public void setIsauto(boolean isauto) {
        this.isauto = isauto;
    }
}
