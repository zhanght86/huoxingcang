/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.DriveInformation;
import com.marstor.msa.bean.TapeLibraryInformation;
import com.marstor.msa.cdp.model.LUNExpansion;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
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
@ManagedBean(name = "libLunmapping_setBean")
@ViewScoped
public class LibLunmapping_setBean  implements Serializable{

    public String selectGroupName;
    private List<String> groupNameList;
    private List<String> nameList;
    MSAResource resources = new MSAResource();
    public List<ViewInfo> viewList;
    String tapeLibraryID;
    String tapeLibraryName;
    public boolean cantnum;
    public boolean isauto;
    public String lunnum;
    private int[] defaults;
    TapeLibraryInformation tapelibinfo;
    private List<LUNExpansion> table = new ArrayList();
    int count = 0;
    private boolean enable = true;
    DriveInformation[] drivers;
    private boolean isAll = true;
    /**
     * Creates a new instance of LibLunmapping_setBean
     */
    public LibLunmapping_setBean() {
        enable = false;
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        tapeLibraryID = request.getParameter("tapeLibraryID");
        tapeLibraryName = request.getParameter("libraryName");

        initGroupNames();
//        isauto = true;
//        changeBooleanCheckbox(true);
        initViewInformation();
        initHostGroupNames();
        initDefault();
        initTable();
    }
    public void initGroupNames() {
        nameList = new ArrayList();
//          nameList.add(resources.get("vtl.lib_lunmapping_set", "allhostgroup"));
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        String[] groupNames = scsi.getAllHostGroupNames();
        SystemOutPrintln.print_vtl("no value");
      
        if (groupNames != null && groupNames.length > 0) {
            
            for (int i = 0; i < groupNames.length; i++) {
                nameList.add(groupNames[i]);
            }
        }
        SystemOutPrintln.print_vtl("nameList1="+nameList.size());
    }
    
     public void initViewInformation() {
        
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
       tapelibinfo = vtl.getTapeLibraryInfo(Integer.valueOf(tapeLibraryID));
        if (tapelibinfo == null) {
            return;
        }
//        boolean havaGuid = false;


            viewList = new ArrayList();
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            SystemOutPrintln.print_vtl("tapelibinfo.getGUID()=" + tapelibinfo.getGUID());
            ViewInformation[] views = scsi.getLUNView(tapelibinfo.getGUID());
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
                        view.setType(resources.get("vtl.lib_lunmapping_set", "all"));
                        view.setHostGroupNameStr(resources.get("vtl.lib_lunmapping_set", "allhostgroup"));
                    } else {
                        String typeStr = views[k].getHostGroupName().split("_")[0];
                        view.setType(typeStr);
                        view.setHostGroupNameStr(views[k].getHostGroupName());
                    }

                    viewList.add(view);
                }
            }
 SystemOutPrintln.print_vtl("viewList="+viewList.size());
        
    }
    
     public void initHostGroupNames() {
        groupNameList = new ArrayList();
//        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//        String[] groupNames = scsi.getAllHostGroupNames();
         if (nameList != null && nameList.size() > 0) {
             if (viewList != null && viewList.size() > 0) {

                 boolean havaAll = false;
                 for (int k = 0; k < viewList.size(); k++) {
                     if (viewList.get(k).getHostGroupNameStr().equalsIgnoreCase(resources.get("vtl.lib_lunmapping_set", "allhostgroup"))) {
//                         groupNameList = new ArrayList();
                         havaAll = true;
                         SystemOutPrintln.print_vtl("groupNameList=0");
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
                     SystemOutPrintln.print_vtl("groupNameList="+groupNameList.size());
                 }
             } else {
                groupNameList = nameList;
                 SystemOutPrintln.print_vtl("groupNameList="+groupNameList.size());
            }

        }
         
        
         if(groupNameList != null && groupNameList.size()>0){
             
         }else{
             groupNameList.add(" ");
         }
              
         
    }
     
     public final void initDefault() {
         if (groupNameList == null || groupNameList.size() < 1) {
             return;
         }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
       SystemOutPrintln.print_vtl("initDefault this.getStrLuns(allhostgroup)="+this.getStrLuns("allhostgroup"));
        
        defaults = scsi.getHostGroupAvailableLuns(this.getStrLuns("allhostgroup"));
       SystemOutPrintln.print_vtl("lun num:");
        for(int lu:defaults){
            System.out.print(lu+" ");
       }
        this.selectGroupName = this.groupNameList.get(0);
        SystemOutPrintln.print_vtl("this.selectGroupName="+this.groupNameList.get(0));
    }
     
     public final void initTable() {
        if (tapelibinfo == null) {
            return;
        }
         LUNExpansion e = null;
         e = new LUNExpansion();
         e.setGuid(tapelibinfo.GUID);
         e.setName(tapelibinfo.name);
         if (defaults != null && defaults.length > 0) {
             e.setLun(defaults[0]);
         } else {
             e.setLun(0);
         }
        e.setType(resources.get("vtl.lib_lunmapping_set", "mediumChanger"));
        this.table.add(e);
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        drivers = vtl.getTapeLibraryDrivesInfo(tapelibinfo.id);
        if (drivers != null && drivers.length > 0) {
            count = drivers.length + 1;
            for (int i = 0; i < drivers.length; i++) {
                e = new LUNExpansion();
                e.setGuid(drivers[i].GUID.toUpperCase());
                e.setName(drivers[i].name);
                if (defaults != null && defaults.length > 0) {
                    if (i >= (defaults.length - 1)) {
                        e.setLun(defaults[defaults.length - 1]);
                    } else {
                        e.setLun(defaults[i + 1]);
                    }
                } else {
                    e.setLun(0);
                }
                e.setType(resources.get("vtl.lib_lunmapping_set", "tapeDriver"));
                this.table.add(e);
            }
        }

    }
     
      public final String getStrLuns(String lun) {
        if (lun.equals("allhostgroup")) {
             SystemOutPrintln.print_vtl("change name= all");
            return "all";
        }
        return lun;
    }
     public void reloadLUN(String name1) {
         String name = name1;
         if (!isAll) {
             if (groupNameList != null && groupNameList.size() > 0 && !groupNameList.get(0).equals(" ")) {
                 name = name1;
             }else{
                 return;
             }
         }
//          String name = this.selectGroupName;
        
        SystemOutPrintln.print_vtl("change name=" + name);
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        SystemOutPrintln.print_vtl("this.getStrLuns(name)=" + this.getStrLuns(name));
        defaults = scsi.getHostGroupAvailableLuns(this.getStrLuns(name));
        SystemOutPrintln.print_vtl("LUN num:");
        for (int lu : defaults) {
            System.out.print(lu + " ");
        }
        SystemOutPrintln.print_vtl("change count=" + count);
        for (int i = 0; i < count; i++) {
            if (i >= defaults.length) {
                this.table.get(i).setLun(defaults[defaults.length - 1]);
            } else {
                this.table.get(i).setLun(defaults[i]);
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
          String hostGroup = "";
//            if (selectGroupName.equalsIgnoreCase(resources.get("disk.lunmapping_set", "allhostgroup"))) {
//                if (viewList != null && viewList.size() > 0) {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.lunmapping_set", "addallhostgroup_no"), ""));
//                    return null;
//                }
//                hostGroup = "";
//            } else {
//                hostGroup = selectGroupName;
//            }
          SystemOutPrintln.print_vtl("isAll="+isAll);
         if (isAll) {
             if (viewList != null && viewList.size() > 0) {
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("vtl.lib_lunmapping_set", "addallhostgroup_no"), ""));
                 return null;
             }
             hostGroup = "";
         } else {
             if(groupNameList.get(0).equals(" ")){
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("vtl.lib_lunmapping_set", "hostgroup_null"), ""));
                 return null;
             }
             hostGroup = selectGroupName;
         }
         if (!enable) {
             List<Integer> temp = new ArrayList();
             for (int i = 0; i < count; i++) {
                 temp.add(table.get(i).getLun());
             }
         
             int num = 0;
             for (int i = 0; i < temp.size() - 1; i++) {
                 List list2 = new ArrayList();
                 for (int j = temp.size() - 1; j > i; j--) {
                     if (temp.get(j).equals(temp.get(i))) {
                         list2.add(temp.get(j));
                         temp.remove(j);
                     }
                 }
                 if ((list2.size() + 1) > 1) {
                     num = (list2.size() + 1);
                 }

             }
             if (num > 1) {
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("vtl.lib_lunmapping_set", "lunnum_exit"), ""));
                     return null;
             }
         }
         if (tapelibinfo == null) {
            return null;
        }
        int lunNum = -1;
          SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
         if (enable) {
             lunNum = -1;
              SystemOutPrintln.print_vtl("hostGroup="+hostGroup);
             SystemOutPrintln.print_vtl("lunNum="+lunNum);
              SystemOutPrintln.print_vtl("tapelibinfo.GUID="+tapelibinfo.GUID);
             boolean successlib = scsi.addView(hostGroup, lunNum, tapelibinfo.GUID);
             if (!successlib) {
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("vtl.lib_lunmapping_set", "addview_fail"), ""));
                 return null;
             }
//             VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
//             DriveInformation[] drivers =  vtl.getTapeLibraryDrivesInfo(tapelibinfo.id);
             if (drivers != null && drivers.length > 0) {
                 for (int i = 0; i < drivers.length; i++) {
                     boolean success = scsi.addView(hostGroup, lunNum, drivers[i].GUID);
                     if (!success) {
                         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("vtl.lib_lunmapping_set", "addview_fail"), ""));
                         return null;
                     }
                 }
             }

         }else{
             SystemOutPrintln.print_vtl("hostGroup="+hostGroup);
             SystemOutPrintln.print_vtl("table.get(0).getLun()="+table.get(0).getLun());
              SystemOutPrintln.print_vtl("tapelibinfo.GUID="+tapelibinfo.GUID);
             boolean successlib = scsi.addView(hostGroup, table.get(0).getLun(), tapelibinfo.GUID);
             if (!successlib) {
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("vtl.lib_lunmapping_set", "addview_fail"), ""));
                 return null;
             }
//             VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
//             DriveInformation[] drivers = vtl.getTapeLibraryDrivesInfo(tapelibinfo.id);
             if (drivers != null && drivers.length > 0) {
                 for (int i = 0; i < drivers.length; i++) {
                     SystemOutPrintln.print_vtl("hostGroup="+hostGroup);
                     SystemOutPrintln.print_vtl("table.get(i+1).getLun()"+(i+1)+"="+table.get(i+1).getLun());
                      SystemOutPrintln.print_vtl("drivers[i].GUID="+drivers[i].GUID);
                     boolean success = scsi.addView(hostGroup, table.get(i+1).getLun(), drivers[i].GUID);
                     if (!success) {
                         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("vtl.lib_lunmapping_set", "addview_fail"), ""));
                         return null;
                     }
                 }
             }
         }

        
      
         String param = "id=" + tapeLibraryID + "&name=" + tapeLibraryName;
         return "lib_lunmapping?faces-redirect=true&amp;" + param;
    }

    public String returnLUN() {

        String param = "id=" + tapeLibraryID + "&name=" + tapeLibraryName;
        return "lib_lunmapping?faces-redirect=true&amp;" + param;

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

    public String getLunnum() {
        return lunnum;
    }

    public void setLunnum(String lunnum) {
        this.lunnum = lunnum;
    }

    public int[] getDefaults() {
        return defaults;
    }

    public void setDefaults(int[] defaults) {
        this.defaults = defaults;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<LUNExpansion> getTable() {
        return table;
    }

    public void setTable(List<LUNExpansion> table) {
        this.table = table;
    }

    public boolean isIsAll() {
        return isAll;
    }

    public void setIsAll(boolean isAll) {
        this.isAll = isAll;
    }
    
}
