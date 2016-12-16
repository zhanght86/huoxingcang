/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.managedbean;

import com.marstor.msa.common.bean.PhysicalDiskInformation;
import com.marstor.msa.common.bean.VolumeGroupInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyConstants;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.volume.model.Disk;
import com.marstor.msa.volume.model.DiskStates;
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
@ManagedBean(name = "addDiskBean")
@ViewScoped
public class AddDiskBean extends DiskStates  implements Serializable{

    public Disk hotBean;
    public List<Disk> hotList;
    private DiskModel hotModel;
    private Disk[] selectedDisks;
    
    public String diskType = "1";
    public String volumeName;
    private long usedDiskCapacity = 0L;
    MSAResource resources = new MSAResource();

    /**
     * Creates a new instance of addDiskBean
     */
    public AddDiskBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        volumeName = request.getParameter("volumeName");
       
     
        addHotList();
        hotModel = new DiskModel(hotList);
    }

    public void addHotList() {  //热备盘
         SystemOutPrintln.print_volume("跳转到热备盘");
        usedDiskCapacity = 0L;
        hotList = new ArrayList();
        
        VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
        PhysicalDiskInformation[] diskInformation = volume.getAllPhysicalDisk();
        
         VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        VolumeGroupInformation[] pools = volumeIn.getAllVolumeGroup();
        if (pools == null) {
            return;
        }
        ArrayList<PhysicalDiskInformation> spareDisks = null;
         for (int i = 0; i < pools.length; i++) {
             if(pools[i].name.equals(volumeName)){
                  spareDisks = pools[i].spareDisk;  //热备磁盘
                  break;
             }
        }    
        
        PhysicalDiskInformation[] spareDisk = null;
        if(spareDisks!= null){
            spareDisk = new PhysicalDiskInformation[spareDisks.size()];
            for(int j=0; j<spareDisks.size();j++){
                spareDisk[j] = spareDisks.get(j);
            }
        } 

//        ArrayList allDiskSAS = new ArrayList();
//        ArrayList canUsedDisk = new ArrayList();
//        ArrayList canUsedDiskSize = new ArrayList();
//        ArrayList canUsedDiskState = new ArrayList();
//        ArrayList canUsedDiskPosition = new ArrayList();
        for (int i = 0; i < diskInformation.length; i++) {
            if (diskInformation[i].model != PhysicalDiskInformation.DISK_MODEL_UNUSED) {
                usedDiskCapacity += diskInformation[i].diskSize;
            }
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_DATA) {
                continue;
            }
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_CACHE) {
                continue;
            }
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_LOG) {
                continue;
            }
            boolean isUsed = false;
            int diskState = 0;
            for (PhysicalDiskInformation spareDiskName : spareDisk) {
                if (spareDiskName.diskName.equals(diskInformation[i].diskName)) {
                    isUsed = true;
                    diskState = spareDiskName.state;
                    break;
                }
            }
            String status = null;
            if (!isUsed) {
//                if (MyConstants.diskModel != PhysicalDiskInformation.DISK_MODEL_SPARE) {
//                    allDiskSAS.add(diskInformation[i].cardModel == 2 ? ("扩展箱 " + diskInformation[i].cardIndex) : "主机");
//                    canUsedDiskPosition.add(diskInformation[i].position);
//                }
//                canUsedDisk.add(diskInformation[i].diskName);
//                canUsedDiskSize.add(diskInformation[i].diskSize);
                switch (diskInformation[i].state) {
                    case PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_SELF:
                        status = resources.get("volume.volumegroup_adddisk", "spare_disk_state_inuse_self");
                        break;
                    case PhysicalDiskInformation.SPARE_DISK_STATE_INUSE_OTHER:
                        status = resources.get("volume.volumegroup_adddisk", "spare_disk_state_inuse_other");
                        break;
                    case PhysicalDiskInformation.SPARE_DISK_STATE_AVAILIBLE:
                        status = resources.get("volume.volumegroup_adddisk", "spare_disk_state_availible");
                        break;
                    default:
                        status = resources.get("volume.volumegroup_adddisk", "unused_disk_state");
                }
//                canUsedDiskState.add(status);
                
                hotBean = new Disk();
                if (MyConstants.diskModel != PhysicalDiskInformation.DISK_MODEL_SPARE) {

                    //hotBean.setPosition(diskInformation[i].cardModel == 2 ? (resources.get("volume.volumegroup_adddisk", "expand") + diskInformation[i].cardIndex) : resources.get("volume.volumegroup_adddisk", "host"));
                    //hotBean.setNum(diskInformation[i].position);
                    String postion = diskInformation[i].cardModel == 2 ? (resources.get("volume.volumegroup_adddisk", "expand") + diskInformation[i].cardIndex) : resources.get("volume.volumegroup_adddisk", "host");
                     if(postion== null || postion.equals("")){
                       hotBean.setPosition("--");
                   }else{
                        hotBean.setPosition(postion);
                   }
                   String num = diskInformation[i].position;
                   if(num== null || num.equals("")){
                        hotBean.setNum("--");
                   }else{
                       hotBean.setNum(num);
                   }
                }
                hotBean.setSize(diskInformation[i].diskSize + "");
                hotBean.setDiskName(diskInformation[i].diskName);
                hotBean.setState(status);
                hotList.add(hotBean);
            }
        }
       
        
        
        
        

//        FacesContext context = FacesContext.getCurrentInstance();
//        UnusedDiskBean undisk = (UnusedDiskBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{unusedDiskBean}", UnusedDiskBean.class).getValue(context.getELContext());
//        List<Disk> unDiskList = undisk.getDiskList();
//        for (int i = 0; i < unDiskList.size(); i++) {
//            Disk disk = unDiskList.get(i);
//            if (disk.getState().equals(DISK_MODEL_UNUSED+"")||disk.getState().equals(SPARE_DISK_STATE_AVAILIBLE+"")) {
//                hotBean = new Disk();
//                hotBean.setPosition(disk.getPosition());
//                hotBean.setNum(disk.getNum());
//                hotBean.setSize(disk.getSize());
//                hotBean.setDiskName(disk.getDiskName());
//                if (disk.getState().equals(DISK_MODEL_UNUSED+"")){
//                      hotBean.setState("未分配");
//                }else{
//                     hotBean.setState("已分配");
//                }
//                
//              
//                hotList.add(hotBean);
//            }
//        }

    }
    
     public void addCacheList() {  //高速缓冲盘

        SystemOutPrintln.print_volume("跳转到高速缓冲盘");
        hotList = new ArrayList();
        
          usedDiskCapacity = 0L;
        //添加高速缓冲盘
        VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
        PhysicalDiskInformation[] diskInformation = volume.getAllPhysicalDisk();
        
         VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        VolumeGroupInformation[] pools = volumeIn.getAllVolumeGroup();
        if (pools == null) {
            return;
        }
        ArrayList<PhysicalDiskInformation> cacheDisks = null;
         for (int i = 0; i < pools.length; i++) {
             if(pools[i].name.equals(volumeName)){
                  cacheDisks = pools[i].cacheDisk;  //热备磁盘
                  break;
             }
        }    
        
        PhysicalDiskInformation[] cacheDisk = null;
        if(cacheDisks!= null){
            cacheDisk = new PhysicalDiskInformation[cacheDisks.size()];
            for(int j=0; j<cacheDisks.size();j++){
                cacheDisk[j] = cacheDisks.get(j);
            }
        } 
        if(diskInformation == null){
            return;
        }

//        ArrayList allDiskSAS = new ArrayList();
//        ArrayList canUsedDisk = new ArrayList();
//        ArrayList canUsedDiskSize = new ArrayList();
//        ArrayList canUsedDiskState = new ArrayList();
//        ArrayList canUsedDiskPosition = new ArrayList();        
        for (int i = 0; i < diskInformation.length; i++) {
            if (diskInformation[i].model != PhysicalDiskInformation.DISK_MODEL_UNUSED) {
                usedDiskCapacity += diskInformation[i].diskSize;
            }
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_DATA) {
                continue;
            }
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_SPARE) {
                continue;
            }
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_LOG) {
                continue;
            }
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_CACHE) {
                continue;
            }


            boolean isUsed = false;
            for (PhysicalDiskInformation disk : cacheDisk) {
                if (disk.diskName.equals(diskInformation[i].diskName)) {
                    isUsed = true;
                }
            }
            String status = null;
            if (!isUsed) {
                if(diskInformation[i].bSSD){
                
//                if (MyConstants.diskModel  != PhysicalDiskInformation.DISK_MODEL_SPARE) {
//                    allDiskSAS.add(diskInformation[i].cardModel == 2 ? ("扩展箱 " + diskInformation[i].cardIndex) : "主机");
//                    canUsedDiskPosition.add(diskInformation[i].position);
//                }
                
                
//                canUsedDiskSize.add(diskInformation[i].diskSize);
                switch (diskInformation[i].state) {
                    case PhysicalDiskInformation.DATA_DISK_STATE_ONLINE:
                        status = resources.get("volume.volumegroup_adddisk", "data_disk_state_online");
                        break;
                    case PhysicalDiskInformation.DATA_DISK_STATE_OFFLINE:
                        status = resources.get("volume.volumegroup_adddisk", "data_disk_state_offline");
                        break;
                    case PhysicalDiskInformation.UNUSED_DISK_STATE:
                        status = resources.get("volume.volumegroup_adddisk", "unused_disk_state");
                        break;
                        case PhysicalDiskInformation.DATA_DISK_CANT_OPEN:
                        status = resources.get("volume.volumegroup_adddisk", "data_disk_cant_open");
                        break;
                    case PhysicalDiskInformation.DATA_DISK_REMOVED:
                        status = resources.get("volume.volumegroup_adddisk", "data_disk_removed");
                        break;case PhysicalDiskInformation.SMART_BEING_FAILING:
                    status = resources.get("volume.volumegroup_adddisk", "smart_being_failing");
                    break;
                    default:
                        status = resources.get("volume.volumegroup_adddisk", "unknown");
                }
//                canUsedDiskState.add(status);
//                canUsedDisk.add(diskInformation[i].diskName);
                
                hotBean = new Disk();
           
                if (MyConstants.diskModel  != PhysicalDiskInformation.DISK_MODEL_SPARE) {
                    hotBean.setPosition((diskInformation[i].cardModel == 2 ? (resources.get("volume.volumegroup_adddisk", "expand") + diskInformation[i].cardIndex) : resources.get("volume.volumegroup_adddisk", "host"))+"(SSD)");
                    hotBean.setNum(diskInformation[i].position);
                }
                hotBean.setSize(diskInformation[i].diskSize + "");
                hotBean.setDiskName(diskInformation[i].diskName);
                hotBean.setState(status);
                hotList.add(hotBean);
                }
            }

        }

    }
     
     public void addLogList() {  //日志盘

        SystemOutPrintln.print_volume("跳转到日志磁盘");
        hotList = new ArrayList();
        
          usedDiskCapacity = 0L;
        //添加高速缓冲盘
        VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
        PhysicalDiskInformation[] diskInformation = volume.getAllPhysicalDisk();
        
         VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        VolumeGroupInformation[] pools = volumeIn.getAllVolumeGroup();
        if (pools == null) {
            return;
        }
        ArrayList<PhysicalDiskInformation> logDiskList = null;
         for (int i = 0; i < pools.length; i++) {
             if(pools[i].name.equals(volumeName)){
                  logDiskList = pools[i].logDisk;  //热备磁盘
                  break;
             }
        }    
        
        PhysicalDiskInformation[] logDisks = null;
        if(logDiskList!= null){
            logDisks = new PhysicalDiskInformation[logDiskList.size()];
            for(int j=0; j<logDiskList.size();j++){
                logDisks[j] = logDiskList.get(j);
            }
        } 
        if(diskInformation == null){
            return;
        }
       
        for (int i = 0; i < diskInformation.length; i++) {
             if (diskInformation[i].model != PhysicalDiskInformation.DISK_MODEL_UNUSED) {
                usedDiskCapacity += diskInformation[i].diskSize;
            }
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_DATA) {
                continue;
            }
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_SPARE) {
                continue;
            }
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_CACHE) {
                continue;
            }
            if (diskInformation[i].model == PhysicalDiskInformation.DISK_MODEL_LOG) {
                continue;
            }

            boolean isUsed = false;
            for (PhysicalDiskInformation disk : logDisks) {
                if (disk.diskName.equals(diskInformation[i].diskName)) {
                    isUsed = true;
                }
            }
            String status = null;
            if (!isUsed) {
                if(diskInformation[i].bSSD){
                switch (diskInformation[i].state) {
                    case PhysicalDiskInformation.DATA_DISK_STATE_ONLINE:
                        status = resources.get("volume.volumegroup_adddisk", "data_disk_state_online");
                        break;
                    case PhysicalDiskInformation.DATA_DISK_STATE_OFFLINE:
                        status = resources.get("volume.volumegroup_adddisk", "data_disk_state_offline");
                        break;
                    case PhysicalDiskInformation.UNUSED_DISK_STATE:
                        status = resources.get("volume.volumegroup_adddisk", "unused_disk_state");
                        break;
                    case PhysicalDiskInformation.DATA_DISK_REMOVED:
                        status = resources.get("volume.volumegroup_adddisk", "data_disk_removed");
                        break;
                    case PhysicalDiskInformation.DATA_DISK_CANT_OPEN:
                        status = resources.get("volume.volumegroup_adddisk", "data_disk_cant_open");
                        break;
                    case PhysicalDiskInformation.SMART_BEING_FAILING:
                        status = resources.get("volume.volumegroup_adddisk", "smart_being_failing");
                        break;
                    default:
                        status = resources.get("volume.volumegroup_adddisk", "unknown");
                }
                
                hotBean = new Disk();
                if (MyConstants.diskModel != PhysicalDiskInformation.DISK_MODEL_SPARE) {
                    hotBean.setPosition((diskInformation[i].cardModel == 2 ? (resources.get("volume.volumegroup_adddisk", "expand") + diskInformation[i].cardIndex) : resources.get("volume.volumegroup_adddisk", "host"))+"(SSD)");
                    hotBean.setNum(diskInformation[i].position);
                }
                hotBean.setSize(diskInformation[i].diskSize + "");
                hotBean.setDiskName(diskInformation[i].diskName);
                hotBean.setState(status);
                hotList.add(hotBean);
                }
            }

        }
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public DiskModel getHotModel() {
        return hotModel;
    }

    public void setHotModel(DiskModel hotModel) {
        this.hotModel = hotModel;
    }

    public Disk[] getSelectedDisks() {
        return selectedDisks;
    }

    public void setSelectedDisks(Disk[] selectedDisks) {
        this.selectedDisks = selectedDisks;
    }

    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
        this.diskType = diskType;
    }
    
    public void handleChange() {
        if (diskType.equals("1")) {
            addHotList();
        } else if (diskType.equals("2")) {
            addCacheList();
        }else if(diskType.equals("3")){
            addLogList();
        }
        hotModel = new DiskModel(hotList);
    }
    
    public String saveAddDisk(String type) {

        
        ArrayList<String> diskNames = new ArrayList();
        String status = resources.get("volume.volumegroup_adddisk", "unused_disk_state");
        long capacityOfNewSpareDiskAdded = 0L;
        for (int i = 0; i < selectedDisks.length; i++) {
            diskNames.add(selectedDisks[i].getDiskName());
             capacityOfNewSpareDiskAdded += Long.valueOf(selectedDisks[i].getSize());
           
        }
        if (diskNames.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_adddisk", "noselectdisk"), ""));
            return null;
        }
        String retrurnStr = null;
        if (type.equalsIgnoreCase("1")) {
            //判断卷组使用盘的总容量
//        LicenseInformation diskCapacityInformation = node.serverNode.getXXModuleXXXFunctionLicense(Constants.MODEL_ID_BASIC, Constants.FUNCTIONID_BASIC_DISK_CAPACITY);
//        if (diskCapacityInformation == null) {
//            Constants.showWarningMessage(MyUtility.getOtherString("没有注册总存储容量"));
//            return null;
//        }
//        if ((1024L * diskCapacityInformation.getFunctionNumber()) < (usedDiskCapacity + capacityOfNewSpareDiskAdded)) {
//            Constants.showErrorMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/HotSpareDialog").getString("磁盘容量超过注册最大限制"));
//            return null;
//        }
            //发送命令添加热备盘
            VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
            boolean spare = volume.addSpare(volumeName, diskNames.toArray(new String[]{}));
            if (spare == false) {  
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,resources.get("volume.volumegroup_adddisk", "addspare_fail") , ""));
                retrurnStr = null;
            }else{
                retrurnStr = "volumegroup?faces-redirect=true";
            }
        }
        
         if (type.equalsIgnoreCase("2")) {
              //判断卷组使用盘的总容量
//            LicenseInformation diskCapacityInformation = node.serverNode.getXXModuleXXXFunctionLicense(Constants.MODEL_ID_BASIC, Constants.FUNCTIONID_BASIC_DISK_CAPACITY);
//            if (diskCapacityInformation == null) {
//                Constants.showWarningMessage(MyUtility.getOtherString("没有注册总存储容量"));
//                return;
//            }
//            if ((1024L * diskCapacityInformation.getFunctionNumber()) < (usedDiskCapacity + capacityOfNewSpareDiskAdded)) {
//                Constants.showErrorMessage(java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/CacheDialog").getString("磁盘容量超过注册最大限制"));
//                return;
//            }
            //发送命令添加高速缓冲盘盘
              VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
            boolean cache = volume.addCache(volumeName, diskNames.toArray(new String[]{}));
       
            if (cache == false) {  
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_adddisk", "addcache_fail"), ""));
                retrurnStr = null;
            }else{
                retrurnStr = "volumegroup?faces-redirect=true";
            }
           
        }
         
         if (type.equalsIgnoreCase("3")) {
              //发送命令添加日志盘
             VolumeInterface volume = InterfaceFactory.getVolumeInterfaceInstance();
            boolean log = volume.addLog(volumeName, diskNames.toArray(new String[]{}));
       
            if (log == false) {  
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("volume.volumegroup_adddisk", "addlog_fail"), ""));
                retrurnStr = null;
            }else{
                retrurnStr = "volumegroup?faces-redirect=true";
            }  
        }

    
        
//        FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
//        AddDiskBean share = (AddDiskBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{addDiskBean}", AddDiskBean.class).getValue(context.getELContext());
//        Disk[] selDisks = share.getSelectedDisks();
//        
//        VolumesInfoBean vol = (VolumesInfoBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{volumesInfoBean}", VolumesInfoBean.class).getValue(context.getELContext());
//        
//        Disk disk;
//        for (int i = 0; i < selDisks.length; i++) {
//            disk = new Disk();
//            disk.setPosition(selDisks[i].getPosition());
//            disk.setNum(selDisks[i].getNum());
//            disk.setSize(selDisks[i].getSize());
//            String typeStr = "热备盘";
//            if(type.equals("1")){
//                typeStr = "热备盘";
//            }else if(type.equals("2")){
//                typeStr = "高速缓冲盘";
//            }else{
//                typeStr = "日志盘";
//            }
//            disk.setType(typeStr);
//            disk.setState("在线");
//            disk.setNotReplace(true);
//            disk.setNotRemove(false);
//            disk.setNotApart(true);
//            disk.setDiskName(selDisks[i].getDiskName());
//            vol.getSelectVolume().getDiskList().add(disk);
//        }
//
//        
//
//        
//        //删除用掉的磁盘
//        UnusedDiskBean undisk = (UnusedDiskBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{unusedDiskBean}", UnusedDiskBean.class).getValue(context.getELContext());
////        List<DiskBean> list = undisk.getDiskList();
//        for (int i = 0; i < undisk.getDiskList().size(); i++) {
//            for (int j = 0; j < selDisks.length; j++) {
//                if (undisk.getDiskList().get(i).getNum().equals(selDisks[j].getNum())) {
//                    String stateStr = SPARE_DISK_STATE_AVAILIBLE + "";
//                    if (type.equals("1")) {
//                        stateStr = SPARE_DISK_STATE_AVAILIBLE + "";
//                    } else if (type.equals("2")) {
//                        stateStr = DISK_MODEL_CACHE + "";
//                    } else {
//                        stateStr = DISK_MODEL_LOG + "";
//                    }
//                    undisk.getDiskList().get(i).setState(stateStr);
//                }
//            }
//        }
        
        return retrurnStr;
    }
    
    
}
