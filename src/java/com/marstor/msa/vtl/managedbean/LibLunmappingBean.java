/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.DriveInformation;
import com.marstor.msa.bean.TapeLibraryInformation;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.disk.model.DiskDetail;
import com.marstor.msa.disk.model.ViewInfo;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.web.VtlInterface;
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
@ManagedBean(name = "libLunmappingBean")
@ViewScoped
public class LibLunmappingBean implements Serializable {

    public int tapeLibraryID;
    public String libraryName;
    public List<ViewInfo> viewList;
    public ViewInfo selectView;
    MSAResource resources = new MSAResource();
    DriveInformation[] drivers;
    TapeLibraryInformation tapelibinfo;
    public String guid;
    public List<DiskDetail> detailList;
    private String libguid;

    /**
     * Creates a new instance of LibLunmappingBean
     */
    public LibLunmappingBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        guid = request.getParameter("GUID");
        SystemOutPrintln.print_vtl("guid="+guid);
        if (guid == null || guid.equalsIgnoreCase("null")) {
            this.tapeLibraryID = Integer.valueOf(request.getParameter("id"));
            this.libraryName = request.getParameter("name");
        }
        this.haveGUID();
        initViewInformation();
    }

    public void haveGUID() {
        if (guid != null && !guid.equals("") && !guid.equalsIgnoreCase("null")) {
            //获取所有磁带库
            SystemOutPrintln.print_vtl("guid="+guid);
            VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
            TapeLibraryInformation[] libs = vtl.getTapeLibrarysInfo();
            boolean havaGuid = false;
            if (libs != null && libs.length > 0) {
                for (int i = 0; i < libs.length; i++) {
                    if (havaGuid) {
                        break;
                    }
                    if (libs[i].getGUID().equalsIgnoreCase(guid)) {
                        havaGuid = true;
                        SystemOutPrintln.print_vtl("guid="+guid);
                        SystemOutPrintln.print_vtl("tapeLibraryID="+tapeLibraryID);
                        SystemOutPrintln.print_vtl("libraryName="+libraryName);
                        this.tapeLibraryID = libs[i].id;
                        this.libraryName = libs[i].name;
                        break;
                    }
                    //获取磁带库下驱动器信息
                    DriveInformation[] dris =  vtl.getTapeLibraryDrivesInfo(libs[i].id);

                    if (dris != null && dris.length > 0) {
//                        System.out.println("disks.length =" + disks.length);
                        for (int j = 0; j < dris.length; j++) {
                            if (dris[j].getGUID().equalsIgnoreCase(guid)) {
                                havaGuid = true;
                                SystemOutPrintln.print_vtl("guid="+guid);
                        SystemOutPrintln.print_vtl("tapeLibraryID="+tapeLibraryID);
                        SystemOutPrintln.print_vtl("libraryName="+libraryName);
                                this.tapeLibraryID = libs[i].id;
                                this.libraryName = libs[i].name;
                                break;
                            } 
                        }
                    }
                }
            }
        }
    }

    public void initViewInformation() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        tapelibinfo = null;
        tapelibinfo = vtl.getTapeLibraryInfo(tapeLibraryID);
        if (tapelibinfo == null) {
            return;
        }
        //获取磁带库下所有的驱动器信息
        drivers = null;
        drivers = vtl.getTapeLibraryDrivesInfo(tapeLibraryID);
        libguid = tapelibinfo.GUID;
//        boolean havaGuid = false;

        viewList = new ArrayList();
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        ViewInformation[] views = scsi.getLUNView(libguid);
        SystemOutPrintln.print_vtl("views==null=" + (views == null));
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
                    view.setType(resources.get("vtl.lib_lunmapping", "all"));
                    view.setHostGroupNameStr(resources.get("vtl.lib_lunmapping", "allhostgroup"));
                } else {
                    String typeStr = views[k].getHostGroupName().split("_")[0];
                    if(typeStr.equalsIgnoreCase("iSCSI")){
                        view.setType("iSCSI");
                    }else{
                        view.setType(typeStr.toUpperCase());
                    }
                    
                    view.setHostGroupNameStr(views[k].getHostGroupName());
                }

                viewList.add(view);
            }
        }

        List<ViewInfo> viewListset = new ArrayList();  //当驱动器属于某个客户端组，而驱动器所在的磁带库却不属于某个客户端组（即某个LUN映射）

        for (int j = 0; j < drivers.length; j++) {

            ViewInformation[] views1 = scsi.getLUNView(drivers[j].getGUID());
            boolean ishave = false;
            if (views1 != null && views1.length > 0) {
                
                for (int m = 0; m < views1.length; m++) {
                    ishave = false;
                    if (views != null && views.length > 0) {
                        for (int k = 0; k < views.length; k++) {
                            if (views1[m].getHostGroupName().equals(views[k].getHostGroupName())) {
                                ishave = true;
                                break;
                            } 
                        }
                       
                        
                    }
                     if (!ishave) {
                            if (viewListset != null && viewListset.size() > 1) {
                                boolean sameflag = false;
                                for (ViewInfo info : viewListset) {
                                    if (info.getHostGroupName().equals(views1[m].getHostGroupName())) {
                                        sameflag = true;
                                        break;
                                    }
                                }
                                if (!sameflag) {
                                    view = new ViewInfo();
                                    view.setEntry(views1[m].getEntry());
                                    view.setGUID(libguid);
                                    view.setHostGroupName(views1[m].getHostGroupName());
                                    view.setLUN(-1);
                                    view.setTargetGroupName(views1[m].getTargetGroupName());
                                    if (views1[m].getHostGroupName().equalsIgnoreCase("All")) {
                                        view.setType(resources.get("vtl.lib_lunmapping", "all"));
                                        view.setHostGroupNameStr(resources.get("vtl.lib_lunmapping", "allhostgroup"));
                                    } else {
                                        String typeStr = views1[m].getHostGroupName().split("_")[0];
                                        view.setType(typeStr);
                                        view.setHostGroupNameStr(views1[m].getHostGroupName());
                                    }

                                    viewListset.add(view);
                                }
                            } else {
                                view = new ViewInfo();
                                view.setEntry(views1[m].getEntry());
                                view.setGUID(libguid);
                                view.setHostGroupName(views1[m].getHostGroupName());
                                view.setLUN(-1);
                                view.setTargetGroupName(views1[m].getTargetGroupName());
                                if (views1[m].getHostGroupName().equalsIgnoreCase("All")) {
                                    view.setType(resources.get("vtl.lib_lunmapping", "all"));
                                    view.setHostGroupNameStr(resources.get("vtl.lib_lunmapping", "allhostgroup"));
                                } else {
                                    String typeStr = views1[m].getHostGroupName().split("_")[0];
                                    view.setType(typeStr);
                                    view.setHostGroupNameStr(views1[m].getHostGroupName());
                                }

                                viewListset.add(view);
                            }


                        }
                }
                
            }

            if (viewListset != null && viewListset.size() > 0) {
                for (ViewInfo info : viewListset) {
                    viewList.add(info);
                }
            }

        }

//        if (viewList != null && viewList.size() > 0) {
//            for (int n = 0; n < viewList.size(); n++) {
//                List<DiskDetail> detailList = new ArrayList();
//                DiskDetail detail = null;
//                detail = new DiskDetail();
//                detail.setDiskname(libraryName);
//                detail.setGuid(viewList.get(n).getGUID());
//                detail.setLun(viewList.get(n).getLUN());
//                detailList.add(detail);
//                for (int j = 0; j < drivers.length; j++) {
////                          System.out.println("123445556disks.length="+disks.length);
////                                SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//                    ViewInformation[] views1 = scsi.getLUNView(drivers[j].getGUID());
//
//                    if (views1 != null && views1.length > 0) {
//                        for (int m = 0; m < views1.length; m++) {
//
//
//                            if (views1[m].getHostGroupName().equals(viewList.get(n).getHostGroupName())) {
//                                detail = new DiskDetail();
//                                System.out.println("drivers[j].getName()=" + drivers[j].getName());
//                                System.out.println("drivers[j].getGUID()=" + drivers[j].getGUID());
//                                System.out.println("views1[m].getLUN()=" + views1[m].getLUN());
//                                detail.setDiskname(drivers[j].getName());
//                                detail.setGuid(drivers[j].getGUID());
//                                detail.setLun(views1[m].getLUN());
//                                break;
//                            } else {
//                                continue;
//                            }
//
//                        }
//                    }
//                    detailList.add(detail);
//
//                }
//                System.out.println("detailList.size()=" + detailList.size());
//                viewList.get(n).setDiskDetail(detailList);
//            }
//        }




    }
    
     public void getExpansions(ViewInfo view) {
    
       
                detailList = new ArrayList();
                DiskDetail detail = null;
                detail = new DiskDetail();
                detail.setDiskname(libraryName);
                detail.setGuid(view.getGUID());
                if(view.getLUN() == -1){
                     detail.setLun("");
                }else{
                    detail.setLun(view.getLUN()+"");
                }
                detail.setType(resources.get("vtl.lib_lunmapping", "mediumChanger"));
                detailList.add(detail);
                 SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
                for (int j = 0; j < drivers.length; j++) {
                               
                    ViewInformation[] views1 = scsi.getLUNView(drivers[j].getGUID());
                    boolean isdriverhaveview = false;
                    if (views1 != null && views1.length > 0) {
                        for (int m = 0; m < views1.length; m++) {

                            if (views1[m].getHostGroupName().equals(view.getHostGroupName())) {
                                detail = new DiskDetail();
                                SystemOutPrintln.print_vtl("drivers[j].getName()=" + drivers[j].getName());
                                SystemOutPrintln.print_vtl("drivers[j].getGUID()=" + drivers[j].getGUID().toUpperCase());
                                SystemOutPrintln.print_vtl("views1[m].getLUN()=" + views1[m].getLUN());
                                detail.setDiskname(drivers[j].getName());
                                detail.setGuid(drivers[j].getGUID().toUpperCase());
                                detail.setLun(views1[m].getLUN()+"");
                                isdriverhaveview = true;
                                break;
                            } else {
                                continue;
                            }

                        }
                    }
                    
                    if(isdriverhaveview == false){  //驱动器在这LUN映射中没有LUN号，即没添加这个LUN成功，则只显示出来
                        detail = new DiskDetail();
                                SystemOutPrintln.print_vtl("drivers[j].getName()=" + drivers[j].getName());
                                SystemOutPrintln.print_vtl("drivers[j].getGUID()=" + drivers[j].getGUID().toUpperCase());
                                SystemOutPrintln.print_vtl("views1[m].getLUN()=" + "driver no have lun");
                                detail.setDiskname(drivers[j].getName());
                                detail.setGuid(drivers[j].getGUID().toUpperCase());
                                detail.setLun("");
                    }
                    detail.setType(resources.get("vtl.lib_lunmapping", "tapeDriver"));
                    detailList.add(detail);

                }
                SystemOutPrintln.print_vtl("detailList.size()=" + detailList.size());
            
        
        
    }

    public String addView() {
        if (viewList != null && viewList.size() > 0) {
            for (int i = 0; i < viewList.size(); i++) {
                if (viewList.get(i).getHostGroupNameStr().equalsIgnoreCase(resources.get("vtl.lib_lunmapping", "allhostgroup"))) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("vtl.lib_lunmapping", "allhostgroup_have"), ""));
                    return null;
                }
            }
        }
//
        String param = "tapeLibraryID=" + tapeLibraryID + "&" + "libraryName=" + libraryName;
        return "lib_lunmapping_set?faces-redirect=true&amp;" + param;


    }

    public void removeView() {


        int entry = selectView.entry;
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        SystemOutPrintln.print_vtl("tapelibinfo.GUID=" + tapelibinfo.GUID);
        SystemOutPrintln.print_vtl("selectView.entry=" + entry);
        if(selectView.getLUN()==-1){  //如果磁带库没有此LUN映射则不做解除
        } else {
            boolean deletelibview = scsi.removeView(tapelibinfo.GUID, entry);
            if (!deletelibview) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("vtl.lib_lunmapping", "delluntip_fail"), ""));
                return;
            }
        }
        
       
        //获取磁带库下所有的驱动器信息
        if (drivers != null && drivers.length > 0) {

            for (int k = 0; k < drivers.length; k++) {
                String guidStr = drivers[k].getGUID(); 
                ViewInformation[] views1 = scsi.getLUNView(guidStr);
                boolean isdriverhaveview = false;
                if (views1 != null && views1.length > 0) {
                    for (int m = 0; m < views1.length; m++) {

                        if (views1[m].getHostGroupName().equals(selectView.getHostGroupName())) {
                            isdriverhaveview = true;
                            break;
                        } else {
                            continue;
                        }
                    }
                }

                if (isdriverhaveview == false) {
                    continue;
                }
                 SystemOutPrintln.print_vtl("disk.GUID=" + guidStr);
                SystemOutPrintln.print_vtl("selectView.entry=" + entry);

                boolean delete = scsi.removeView(guidStr, entry);
                SystemOutPrintln.print_vtl("delete=" + delete);
                if (delete) {
//                    if (k == (drivers.length - 1)) {
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, resources.get("vtl.lib_lunmapping", "delluntip_ok"), ""));
//                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("vtl.lib_lunmapping", "delluntip_fail"), ""));
                    return;
                }

            }
        }
        initViewInformation();
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public List<ViewInfo> getViewList() {
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

    public List<DiskDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<DiskDetail> detailList) {
        this.detailList = detailList;
    }
    
}
