/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.disk.managedbean;

import com.marstor.msa.common.bean.DiskPool;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.bean.VirtualDiskInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.DiskInterface;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.disk.model.DiskDetail;
import com.marstor.msa.disk.model.DiskPoolInfo;
import com.marstor.msa.disk.model.ViewInfo;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
@ManagedBean(name = "lunmappingBean")
@ViewScoped
public class LunmappingBean implements Serializable {

//    public boolean cantnum;
//    public boolean isauto;
    //    public String selectGroupName;
//    private List<String> groupNameList;
//    public String lunnum;
    public String zfsPath;
    public String guid;
    public String poolname;
    public List<ViewInfo> viewList;
    public ViewInfo selectView;
    MSAResource resources = new MSAResource();

    /**
     * Creates a new instance of LunmappingBean
     */
    public LunmappingBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        zfsPath = request.getParameter("zfsPath");
        guid = request.getParameter("GUID");
        poolname = request.getParameter("poolName");
        this.haveGUID();

//        initHostGroupNames();
//        initViewInformation();
//        isauto = true;
//        changeBooleanCheckbox(true);
    }

    public void haveGUID() {
        if (guid != null && !guid.equals("")) {
            //获取所有磁盘池
            DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
            DiskPool[] diskpools = diskIn.getAllDiskPool();
            if (diskpools != null && diskpools.length > 0) {
                for (int i = 0; i < diskpools.length; i++) {
                    //获取磁盘池下所有的虚拟磁盘信息
                    VirtualDiskInformation[] disks = diskIn.getDiskFromPool(diskpools[i].getZfsPath());
                    boolean havaGuid = false;
                    if (disks != null && disks.length > 0) {
//                        System.out.println("disks.length =" + disks.length);
                        for (int j = 0; j < disks.length; j++) {
                            if (disks[j].getGUID().equalsIgnoreCase(guid)) {
                                havaGuid = true;
                                poolname = diskpools[i].poolName;
                                zfsPath = diskpools[i].zfsPath;
                                break;
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }
//
//    public void initHostGroupNames() {
//        groupNameList = new ArrayList();
//        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//        String[] groupNames = scsi.getAllHostGroupNames();
//        if (groupNames != null && groupNames.length > 0) {
//            groupNameList.add("所有客户端组");
//            for (int i = 0; i < groupNames.length; i++) {
//                groupNameList.add(groupNames[i]);
//            }
//        }
//    }

    public void initViewInformation() {
        DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();

        //获取磁盘池下所有的虚拟磁盘信息
        VirtualDiskInformation[] disks = diskIn.getDiskFromPool(zfsPath);
//        boolean havaGuid = false;
        if (disks != null && disks.length > 0) {

            viewList = new ArrayList();
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//            System.out.println("guid[0]=" + disks[0].getGUID());
            ViewInformation[] views = scsi.getLUNView(disks[0].getGUID());
//            System.out.println("views==null=" + (views == null));
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
                        view.setType(resources.get("disk.lunmapping", "all"));
                        view.setHostGroupNameStr(resources.get("disk.lunmapping", "allhostgroup"));
                    } else {
                        String typeStr = views[k].getHostGroupName().split("_")[0];
                        view.setType(typeStr);
                        view.setHostGroupNameStr(views[k].getHostGroupName());
                    }

                    viewList.add(view);
                }
            }

            if (viewList != null && viewList.size() > 0) {
                for (int n = 0; n < viewList.size(); n++) {
                    List<DiskDetail> detailList = new ArrayList();
                    for (int j = 0; j < disks.length; j++) {
//                          System.out.println("123445556disks.length="+disks.length);
//                                SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
                        ViewInformation[] views1 = scsi.getLUNView(disks[j].getGUID());
                        DiskDetail detail = null;
                        if (views1 != null && views1.length > 0) {
                            for (int m = 0; m < views1.length; m++) {
                                if (views1[m].getHostGroupName().equals(viewList.get(n).getHostGroupName())) {
                                    detail = new DiskDetail();
                                     System.out.println("disks[j].getDiskName()="+disks[j].getDiskName());
                                      System.out.println("views1[m].getGUID()="+views1[m].getGUID());
                                      System.out.println("views1[m].getLUN()="+views1[m].getLUN());
                                    detail.setDiskname(disks[j].getDiskName());
                                    detail.setGuid(views1[m].getGUID());
                                    detail.setLun(views1[m].getLUN()+"");
                                    break;
                                } else {
                                    continue;
                                }

                            }
                        }
                        detailList.add(detail);
                       
                    }
                     System.out.println("detailList.size()="+detailList.size());
                    viewList.get(n).setDiskDetail(detailList);
                }
            }

        }


    }

//    public void changeBooleanCheckbox(boolean isSt) {
//        if (isSt == true) {
//            cantnum = true;
//
//        } else {
//            cantnum = false;
//        }
//    }
    public String addView() {
        if(viewList!= null && viewList.size()>0){
            for(int i=0; i<viewList.size(); i++){
                if(viewList.get(i).getHostGroupNameStr().equalsIgnoreCase(resources.get("disk.lunmapping", "allhostgroup"))){
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.lunmapping", "allhostgroup_have"), ""));
                    return null;
                }
            }
        }

        String param = "zfsPath=" + zfsPath + "&" + "poolName=" + poolname;
        return "lunmapping_set?faces-redirect=true&amp;" + param;


    }

    public void removeView() {
        //获取磁盘池下所有的虚拟磁盘信息
        DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
        VirtualDiskInformation[] disks = diskIn.getDiskFromPool(zfsPath);
        if (disks != null && disks.length > 0) {

            for (int k = 0; k < disks.length; k++) {
                String guidStr = disks[k].getGUID();
                System.out.println("disk.GUID=" + guidStr);
                System.out.println("selectView.entry=" + selectView.entry);
                SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
                boolean delete = scsi.removeView(guidStr, selectView.entry);
                System.out.println("delete=" + delete);
                if (delete) {
                    if(k ==(disks.length-1)){
                         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, resources.get("disk.lunmapping", "delluntip_ok"), ""));
                    }     
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.lunmapping", "delluntip_fail"), ""));
                    return;
                }

            }
        }

    }

//    public boolean isCantnum() {
//        return cantnum;
//    }
//
//    public void setCantnum(boolean cantnum) {
//        this.cantnum = cantnum;
//    }
//
//    public boolean isIsauto() {
//        return isauto;
//    }
//
//    public void setIsauto(boolean isauto) {
//        this.isauto = isauto;
//    }
//    public String getDiskname() {
//        return diskname;
//    }
//
//    public void setDiskname(String diskname) {
//        this.diskname = diskname;
//    }
    public String getZfsPath() {
        return zfsPath;
    }

    public void setZfsPath(String zfsPath) {
        this.zfsPath = zfsPath;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPoolname() {
        return poolname;
    }

    public void setPoolname(String poolname) {
        this.poolname = poolname;
    }

//    public String getSelectGroupName() {
//        return selectGroupName;
//    }
//
//    public void setSelectGroupName(String selectGroupName) {
//        this.selectGroupName = selectGroupName;
//    }
//
//    public List<String> getGroupNameList() {
//        return groupNameList;
//    }
//
//    public void setGroupNameList(List<String> groupNameList) {
//        this.groupNameList = groupNameList;
//    }
//
//    public String getLunnum() {
//        return lunnum;
//    }
//    public void setLunnum(String lunnum) {
//        this.lunnum = lunnum;
//    }
    public List<ViewInfo> getViewList() {
        initViewInformation();
        return viewList;
    }

    public void setViewList(List<ViewInfo> viewList) {
        this.viewList = viewList;
    }

    public ViewInfo getSelectView() {
        return selectView;
    }

    public void setSelectView(ViewInfo selectView) {
        this.selectView = selectView;
    }
}
