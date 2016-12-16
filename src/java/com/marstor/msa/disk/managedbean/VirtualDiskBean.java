/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.disk.managedbean;

import com.marstor.msa.common.bean.DiskPool;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.bean.VirtualDiskInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.DiskInterface;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.disk.model.DiskPoolInfo;
import com.marstor.msa.disk.model.VirtualDiskInfo;
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
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "virtualDiskBean")
@ViewScoped
public class VirtualDiskBean implements Serializable {

    VirtualDiskInfo disk;
    public VirtualDiskInfo selectDisk;
    public List<VirtualDiskInfo> diskList;
//    public List<VirtualDetail> detail;
//    VirtualDetail diskDetail;
    public List<DiskPoolInfo> poolList;
    DiskPoolInfo diskpool;
    int userType = 3;
    public DiskPoolInfo selectPool;
    MSAResource resources = new MSAResource();

    /**
     * Creates a new instance of VirtualDiskBean
     */
    public VirtualDiskBean() {
        initDiskInfo();
    }

    public void initDiskInfo() {
//        System.out.println("##############init 测试");
        poolList = new ArrayList();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        userType = user.getType();
        //获取所有磁盘池
        DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
        DiskPool[] diskpools = diskIn.getAllDiskPool();
        if (diskpools != null && diskpools.length > 0) {
            for (int i = 0; i < diskpools.length; i++) {
                String status = resources.get("disk.virtualdisklist", "onling");
                diskpool = new DiskPoolInfo();
                diskpool.setPoolName(diskpools[i].poolName);
    
                diskpool.setZfsPath(diskpools[i].zfsPath);
                String zfsStr[] = diskpools[i].zfsPath.split("/");
                diskpool.setDiskarea(zfsStr[0]+"/"+zfsStr[1]);
                diskpool.setIsMounted(diskpools[i].isMounted);
                System.out.println("diskpools[i].poolName="+diskpools[i].poolName);
                System.out.println("diskpools[i].isMounted="+diskpools[i].isMounted);
                   

                if (diskpools[i].isMounted == true) {
                    status = resources.get("disk.virtualdisklist", "onling");
                } else {
                    status = resources.get("disk.virtualdisklist", "offline");
                }

                if (diskpools[i].isMounted == true) {
                    diskpool.setCantOnline(false);
                    diskpool.setCantdel(false);
                    diskpool.setCantSync(false);
                    diskpool.setCantLUN(false);
                    diskpool.setCantDisk(false);
                    VirtualDiskInformation[] disks = diskIn.getDiskFromPool(diskpools[i].zfsPath);
                    if (disks != null && disks.length > 0) {
                         diskpool.setCantLUN(false);
                    }else{
                        diskpool.setCantLUN(true);
                    }
                } else {
                    diskpool.setCantOnline(false);
                    diskpool.setCantdel(false);
                    diskpool.setCantSync(true);
                    diskpool.setCantLUN(true);
                    diskpool.setCantDisk(true);
                }
         
                if (userType != 2) {
                    diskpool.setCantOnline(true);
                    diskpool.setCantdel(true);
                    diskpool.setCantSync(true);
                    diskpool.setCantLUN(true);
                    diskpool.setCantDisk(true);
                }

                if (diskpools[i].isMounted == true) {
                    diskpool.setOnlineState(resources.get("disk.virtualdisklist", "offline"));
                    diskpool.setLineURL("../resources/common/picture/offline.png");
                } else {
                    diskpool.setOnlineState(resources.get("disk.virtualdisklist", "onling"));
                    diskpool.setLineURL("../resources/common/picture/online.png");
                }
                diskpool.setState(status);
                poolList.add(diskpool);
            }
        }

        List<DiskPoolInfo> diList = poolList;
        poolList = new ArrayList();
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        String[] volumeNames = zfs.getVolumeNames();
        if (volumeNames != null && volumeNames.length > 0) {
            for (int i = 0; i < volumeNames.length; i++) {
                if(diList!=null && diList.size()>0){
                    for(int j=0; j<diList.size(); j++){
                        if(volumeNames[i].equals(diList.get(j).getZfsPath().split("/")[0])){
                            poolList.add(diList.get(j));
                        }else{
                            continue;
                        }
                    }
                }
            }
        }

    }

    public void onRowToggle(ToggleEvent event) {
        System.out.println("****************");
        //获取磁盘池下所有的虚拟磁盘信息
        DiskPoolInfo pool = (DiskPoolInfo) event.getData();
          System.out.println("pool.getPoolName()="+pool.getPoolName());
        diskList = new ArrayList();
        if (pool.isIsMounted()) {
            DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
            VirtualDiskInformation[] disks = diskIn.getDiskFromPool(pool.getZfsPath());
            if (disks != null && disks.length > 0) {
                  System.out.println("disks.length="+disks.length);
                for (int i = 0; i < disks.length; i++) {
                    String status = resources.get("disk.virtualdisklist", "onling");
                    disk = new VirtualDiskInfo();
                    disk.setDiskName(disks[i].getDiskName());
                    disk.setPoolName(disks[i].getPoolName());
                    disk.setGUID(disks[i].getGUID());
                    try {
                        disk.setDiskSize(ValidateUtility.sizeToString(Long.valueOf(disks[i].getDiskSize())));
                    } catch (Exception e) {
                        disk.setDiskSize(disks[i].getDiskSize());
                    }
                    disk.setFileSize(ValidateUtility.sizeToString(disks[i].getFileSize()));

                    int statein = disks[i].getState();
                    boolean ismount = disks[i].isIsMounted();

                    if (ismount == true) {
                        if (statein == 0) {
                            status = resources.get("disk.virtualdisklist", "onling");
                        } else if (statein == 1) {
                            status = resources.get("disk.virtualdisklist", "creating");
                        } else if (statein == 2) {
                            status = resources.get("disk.virtualdisklist", "creatfail");
                        } else {
                            status = statein + "";
                        }
                    } 

                    if (status.equalsIgnoreCase(resources.get("disk.virtualdisklist", "onling"))) {
                        disk.setCantdel(false);
                    } else if (status.equalsIgnoreCase(resources.get("disk.virtualdisklist", "creating"))) {
                        disk.setCantdel(true);
                    } else if (status.equalsIgnoreCase(resources.get("disk.virtualdisklist", "creatfail"))) {
                        disk.setCantdel(false);

                    } else {
                        disk.setCantdel(true);
                    }

                    if (userType != 2) {
                        disk.setCantdel(true);
                    }
                    disk.setState(status);
                    disk.setPath(disks[i].getPath());
                    disk.setIsMounted(disks[i].isIsMounted());
                    diskList.add(disk);
                }
            }
        }
        pool.setDiskList(diskList);

    }

    public VirtualDiskInfo getSelectDisk() {
        return selectDisk;
    }

    public void setSelectDisk(VirtualDiskInfo selectDisk) {
        this.selectDisk = selectDisk;
    }

    public List<VirtualDiskInfo> getDiskList() {
//        initDiskInfo();
        return diskList;
    }

    public void setDiskList(List<VirtualDiskInfo> diskList) {
        this.diskList = diskList;
    }

    public List<DiskPoolInfo> getPoolList() {
        return poolList;
    }

    public void setPoolList(List<DiskPoolInfo> poolList) {
        this.poolList = poolList;
    }

    public DiskPoolInfo getSelectPool() {
        return selectPool;
    }

    public void setSelectPool(DiskPoolInfo selectPool) {
        this.selectPool = selectPool;
    }

    public void linePool() {
        boolean mount = selectPool.isIsMounted();
        if (mount) {
            offlineDisk();
        } else {
            this.onlineDisk();
        }
    }

    public void onlineDisk() {
        String path = selectPool.getZfsPath();
        System.out.println("path1=" + path);
//        BasicQueryAction canMountAct = new BasicQueryAction("7067", "Check Can Mount the Directory", this.socket);
//        canMountAct.addParameter("FileSystem", path);
//        if(!canMountAct.doAction()){
//            Constants.showInformationMessage(this.getString("获取目录同步状态失败，请重试"));
//            return;
//        }
//        boolean canMount = true;
//        if(canMountAct.returnXML.getIntNodeContent("MSA/ReturnValue/CanMount") == 0){
//            canMount = false;
//        }
//        if(!canMount){
//            Constants.showWarningMessage(this.getString("同步进行中，不能上线！"));
//            return;
//        }
        //磁盘池上线
        DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
        boolean diskOnline = diskIn.onlineDiskPool(path);
        if (diskOnline == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.virtualdisklist", "onlinepool") + selectPool.getPoolName() + resources.get("disk.virtualdisklist", "fail"), ""));
            return;
        }else{
            System.out.println("online succeed" );
        }
        initDiskInfo();


    }

    public void offlineDisk() {
         //获取磁盘池下所有的虚拟磁盘信息
            DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
            VirtualDiskInformation[] disks = diskIn.getDiskFromPool(selectPool.getZfsPath());
            boolean extend = false;
           if (disks != null && disks.length > 0) {
            System.out.println("disks.length off=" + disks.length);
            for (int i = 0; i < disks.length; i++) {
                int statein = disks[i].getState();
                boolean ismount = disks[i].isIsMounted();

                if (ismount == true) {
                    if (statein == 1) {  //"扩大磁盘文件中"
                        extend = true;
                        break;
                    } else {
                        continue;
                    }
                }
            }

            if (extend) {  //扩大磁盘文件中
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.virtualdisklist", "offling_creat"), ""));
                return;
            }

            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            System.out.println("disks[0].getGUID()2=" + disks[0].getGUID());
            ViewInformation[] view = scsi.getLUNView(disks[0].getGUID());

            if (view != null && view.length != 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.virtualdisklist", "offling_assign"), ""));
                return;
            }

        }

        //磁盘池离线
        RequestContext.getCurrentInstance().execute("offlineDisk.show()");
    }

    public void offline_real() {

        //磁盘池离线
        String path = selectPool.getZfsPath();
        System.out.println("path2=" + path);
        DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
        boolean diskOffline = diskIn.offlineDiskPool(path);
        if (diskOffline == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.virtualdisklist", "offling_pool") + selectPool.getPoolName() + resources.get("disk.virtualdisklist", "fail"), ""));
            return;
        }
        initDiskInfo();
    }

    //删除磁盘池
    public void delDiskPool() {
        //获取磁盘池下所有的虚拟磁盘信息
            DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
            VirtualDiskInformation[] disks = diskIn.getDiskFromPool(selectPool.getZfsPath());
            boolean extend = false;
           if (disks != null && disks.length > 0) {
            System.out.println("disks.length off=" + disks.length);
            for (int i = 0; i < disks.length; i++) {
                int statein = disks[i].getState();
                boolean ismount = disks[i].isIsMounted();

                if (ismount == true) {
                    if (statein == 1) {  //"扩大磁盘文件中"
                        extend = true;
                        break;
                    } else {
                        continue;
                    }
                }
            }

            if (extend) {  //扩大磁盘文件中
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.virtualdisklist", "delete_creat"), ""));
                return;
            }

            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            System.out.println("disks[0].getGUID()2=" + disks[0].getGUID());
            ViewInformation[] view = scsi.getLUNView(disks[0].getGUID());

            if (view != null && view.length != 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.virtualdisklist", "delete_assign"), ""));
                return;
            }

        }

//
//        String[] paths= this.diskInfo.path.split("/");
//        String path = paths[1] + "/" + paths[2] + "/" + paths[3];
//
//        //判断是否同步
//        if (serverNode.hasXXModuleBasicLicense(Constants.MODEL_ID_SNAP)) {
//            BasicQueryAction synchronize = new BasicQueryAction("7051", "Get target server file system info(CommonBasicModule)", socket);
//            synchronize.addParameter("FileSystem", path);
//            if (!synchronize.doAction()) {
//                return;
//            }
//            if (synchronize.getReturnValue().getIntNodeContent("ReturnValue/CanDelete") != 1) {
//                Constants.showErrorMessage(getString("正在同步，不能删除"));
//                return;
//            }
//        }

        //删除磁盘
        RequestContext.getCurrentInstance().execute("delDiskPool.show()");
    }

    public void delDiskPool_real() {

        //删除磁盘池
        String path = selectPool.getZfsPath();
        System.out.println("selectPool.getZfsPath()=" + selectPool.getZfsPath());
        DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
        boolean diskdel = diskIn.deleteDiskPool(path);
        if (diskdel == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.virtualdisklist", "delete_pool") + selectPool.getPoolName() + resources.get("disk.virtualdisklist", "fail"), ""));
            return;
        }else{
             System.out.println("delete pool succeed" );
        }
        
        initDiskInfo();
    }
    
     //删除磁盘
    public void delDisk() {
        if (selectDisk.getState().equalsIgnoreCase(resources.get("disk.virtualdisklist", "creating"))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.virtualdisklist", "deldisk_creat"), ""));
            return;
        }

        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        System.out.println("selectDisk.getGUID()=" + selectDisk.getGUID());
        ViewInformation[] view = scsi.getLUNView(selectDisk.getGUID());

        if (view != null && view.length != 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.virtualdisklist", "deldisk_assign"), ""));
            return;
        }


//
//        String[] paths= this.diskInfo.path.split("/");
//        String path = paths[1] + "/" + paths[2] + "/" + paths[3];
//
//        //判断是否同步
//        if (serverNode.hasXXModuleBasicLicense(Constants.MODEL_ID_SNAP)) {
//            BasicQueryAction synchronize = new BasicQueryAction("7051", "Get target server file system info(CommonBasicModule)", socket);
//            synchronize.addParameter("FileSystem", path);
//            if (!synchronize.doAction()) {
//                return;
//            }
//            if (synchronize.getReturnValue().getIntNodeContent("ReturnValue/CanDelete") != 1) {
//                Constants.showErrorMessage(getString("正在同步，不能删除"));
//                return;
//            }
//        }

        //删除磁盘
        RequestContext.getCurrentInstance().execute("delDisk.show()");
    }
    
      public void delDisk_real() {

        //删除磁盘区
        String path = selectDisk.getPath();
        System.out.println("path3=" + path);
        DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
        boolean diskdel = diskIn.deleteDisk(path,selectDisk.getGUID());
        if (diskdel == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.virtualdisklist", "deldisk") + selectDisk.getDiskName() + resources.get("disk.virtualdisklist", "fail"), ""));
            return;
        }
        initDiskInfo();
    }

    public String lunMapping() {
        System.out.println("selectDisk.poolName()=" + selectPool.poolName);
//        String param = "diskName=" + selectDisk.diskName + "&" + "GUID=" + selectPool.getGUID() + "&" + "poolName=" + selectPool.poolName;
         String param = "zfsPath=" + selectPool.zfsPath + "&" + "poolName=" + selectPool.poolName+ "&" + "GUID=" + "";
        return "lunmapping?faces-redirect=true&amp;" + param;
    }

    public String createDisk() {

        String param = "poolZFSPath=" + selectPool.zfsPath + "&" + "poolName=" + selectPool.poolName;
        return "createdisk?faces-redirect=true&amp;" + param;
    }
}
