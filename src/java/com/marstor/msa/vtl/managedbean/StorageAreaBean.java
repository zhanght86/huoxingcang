/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.TapeInformation;
import com.marstor.msa.cdp.managedbean.*;
import com.marstor.msa.common.bean.FileSystemInformation;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.bean.StatusInfo;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.model.FileSystemInfor;
import com.marstor.msa.vtl.model.StorageAreaTapeCount;
import com.marstor.msa.vtl.util.MyConstants;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "vtlSABean")
@ViewScoped
public class StorageAreaBean implements Serializable {

    private String[] volumeNames;
    private List<Details> expansionsView;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.storage_area";
    private Map<String, List<Details>> expansionsMap = new HashMap();
    public List<FileSystemInfor> views;
    public FileSystemInfor selected;
    public Map<String, ArrayList<TapeInformation>> tapesMap = new HashMap();
    public ArrayList<TapeInformation> importedTapes;
    public boolean cantuse;
    public boolean canton_off;
    public boolean cantset;
    public ArrayList<StorageAreaTapeCount> storageAreaTapeCount;
    public String storageAreaName;

    /**
     * Creates a new instance of FileSystemBean
     */
    public StorageAreaBean() {
//        initVolumeNames();
//        initStorageArea();
    }

    public final void initVolumeNames() {
        volumeNames = null;
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        volumeNames = zfs.getVolumeNames();
    }

    public final void initStorageArea() {
        if (volumeNames == null) {
            return;
        }
        views = new ArrayList();
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();
        Map<String, StatusInfo> map = sync.getAllFileSystemStatus();
        for (int i = 0; i < this.volumeNames.length; i++) {
            FileSystemInformation view = zfs.getFileSystemInformation(volumeNames[i] + "/TAPE");
            if (view == null) {
                continue;
            }
            getMenu(view);
            FileSystemInfor file = new FileSystemInfor();
            file.setAvailable(view.available);
            file.setCanton_off(canton_off);
            file.setCantset(cantset);
            file.setCantuse(cantuse);
            file.setCompress(view.Compress);
            file.setIsMounted(view.isMounted);
            file.setIsSetDedup(view.isSetDedup);
            file.setIsSetQuota(view.isSetQuota);
            file.setIsVerify(view.isVerify);
            file.setMountpoint(view.mountpoint);
            file.setName(view.name);
            file.setQuotaValue(view.quotaValue);
            file.setRecordsize(view.recordsize);
            file.setUsed(view.used);
            if (file.isIsMounted()) {
//                String array[] = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(view.name);
//                if (array[4].equals("1")) {//如果是同步目标
//                    if (array[5].equals("1")) { //如果源端暂停同步
//                        file.setOnlineDisabled(false);
//                    } else {
//                        file.setOnlineDisabled(true);
//                    }
//                }
                StatusInfo statusInfo = map.get(volumeNames[i] + "/TAPE");
                if(statusInfo.getIsDescSync() == 1){
                    if (statusInfo.getIsPause() == 1) { //如果源端暂停同步
                        file.setOnlineDisabled(false);
                    } else {
                        file.setOnlineDisabled(true);
                    }
                }
            }
            this.views.add(file);
        }
    }

    public void getMenu(FileSystemInformation view) {
        cantuse = false;
        canton_off = false;
        cantset = false;

        if (view.isMounted) {
            cantuse = false;
        } else {
            cantuse = true;
        }
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        int userType = user.getType();
        if (userType != 2) {
            cantuse = true;
            canton_off = true;
            cantset = true;
        }


    }

    public String OnOffLine(FileSystemInformation fs) {
        if (fs.isMounted) {
            return res.get(basename, "o3_0");//离线
        } else {
            return res.get(basename, "o3_1");//上线
        }
    }

    public String OnOffLineIcon(FileSystemInformation fs) {
        if (fs.isMounted) {
            return "../resources/common/picture/offline.png";
        } else {
            return "../resources/common/picture/online.png";
        }
    }

    public String OnOffLine() {
        String[] status = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(this.selected.name);
        if (status != null && status.length > 4 && status[4].equals("1")) {
            if (!status[5].equals("1")) {
                SystemOutPrintln.print_vtl("sync target online");
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("syncTargetOnline"), ""));
                return null;
            }

        }
        if (this.selected.isMounted) {
            offlineDisk();
        } else {
            return onlineTape();
        }
        return null;
    }

    public String onlineTape() {
        String fileSystemPath = selected.getMountpoint().substring(1);

//？？？        BasicQueryAction canMountAct = new BasicQueryAction("7067", "Check Can Mount the Directory", this.socket);
//        canMountAct.addParameter("FileSystem", fileSystemPath);
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
        //磁带上线
        SystemOutPrintln.print_vtl("fileSystemPath=" + fileSystemPath);
        VtlInterface zfs = InterfaceFactory.getVtlInterfaceInstance();
        boolean tapeOnline = zfs.mountZFS(fileSystemPath);
        if (tapeOnline == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "o3_1") + selected.name + res.get(basename, "fail"), ""));
            return null;
        } else {
            SystemOutPrintln.print_vtl("online succeed");
        }
        return "storage_area?faces-redirect=true";
//        initVolumeNames();
//        initStorageArea();


    }

    public void offlineDisk() {

        storageAreaName = this.selected.name;
        SystemOutPrintln.print_vtl("path=" + this.selected.name);
        FileSystemInfo fileSys = InterfaceFactory.getSYNCInterfaceInstance().getFileSystemInfo(this.selected.name);
        /// FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.selected.name);
        boolean flag;
        if (fileSys == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "getFileSysFailed"), ""));
            return;
        }
        boolean runauto = false;
        if (fileSys.getiIsOpen() == 1) {//如果自动快照已经开启
            runauto = true;
        }




        //获取磁带区下所有的虚拟磁盘信息
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        TapeInformation[] allTape = vtl.getAllTapesInfo();
        if (allTape == null) {
            return;
        }
        String mediaPath = "/" + this.selected.name;
        List<TapeInformation> tapes = new ArrayList();
        for (int i = 0; i < allTape.length; i++) {
            if (allTape[i].path.equals(mediaPath)) {
                tapes.add(allTape[i]);
            }
        }

        if (tapes != null && tapes.size() > 0) {  //含有磁带
            RequestContext.getCurrentInstance().execute("exporttapetip.show()");
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "exoprt1")+this.selected.name+res.get(basename, "exoprt2"), ""));
            return;
        }


        //离线
        if (runauto) {
            RequestContext.getCurrentInstance().execute("offlineTapeAutoRun.show()");
        } else {
            RequestContext.getCurrentInstance().execute("offlineTape.show()");
        }

    }

    public String offline_auto_real() {
        SystemOutPrintln.print_vtl("path=" + this.selected.name);
        FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.selected.name);
        boolean flag;
        if (fileSys == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "getFileSysFailed"), ""));
            return null;
        }
        if (fileSys.getiIsOpen() == 1) {//如果自动快照已经开启
            flag = InterfaceFactory.getSYNCInterfaceInstance().cancelTimingSnapshot(this.selected.name, false);
            if (!flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "closeAutoSnapFailed"), ""));
                return null;
            }
        }





        VtlInterface zfs = InterfaceFactory.getVtlInterfaceInstance();
        String fileSystemPath = selected.getMountpoint().substring(1);
        SystemOutPrintln.print_vtl("offline fileSystemPath=" + fileSystemPath);

        //离线操作


        boolean tapeOffline = zfs.umountZFS(fileSystemPath);
        if (tapeOffline == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "o3_0") + selected.name + res.get(basename, "fail"), ""));
            return null;
        }
        SystemOutPrintln.print_vtl("offline success");
        return "storage_area?faces-redirect=true";
//        initVolumeNames();
//        initStorageArea();
    }

    public String offline_real() {

        VtlInterface zfs = InterfaceFactory.getVtlInterfaceInstance();
        String fileSystemPath = selected.getMountpoint().substring(1);
        SystemOutPrintln.print_vtl("offline fileSystemPath=" + fileSystemPath);

        //离线操作


        boolean tapeOffline = zfs.umountZFS(fileSystemPath);
        if (tapeOffline == false) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "o3_0") + selected.name + res.get(basename, "fail"), ""));
            return null;
        }
        SystemOutPrintln.print_vtl("offline success");
        return "storage_area?faces-redirect=true";
//        initVolumeNames();
//        initStorageArea();
    }

    public void initExpansion(FileSystemInformation info) {
        //扩展 存储空间信息
        expansionsView = new ArrayList();
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        FileSystemInformation expansion = zfs.getFileSystemInformation(info.name);
        Details g = new Details();
        g.setName(res.get(basename, "name0"));
        g.setValue(expansion.isSetDedup ? res.get(basename, "true") : res.get(basename, "false"));
        expansionsView.add(g);
        g = new Details();
        g.setName(res.get(basename, "name1"));
        g.setValue(expansion.isVerify ? res.get(basename, "true") : res.get(basename, "false"));
        expansionsView.add(g);
        g = new Details();
        g.setName(res.get(basename, "name2"));
        boolean c = expansion.getCompress().equalsIgnoreCase("null") || expansion.getCompress().equalsIgnoreCase("") || expansion.getCompress().equalsIgnoreCase("off");
        g.setValue(c ? res.get(basename, "false") : res.get(basename, "true"));
        expansionsView.add(g);
        if (!c) {
            //启用数据压缩时
            g = new Details();
            g.setName(res.get(basename, "compresslevel"));
            g.setValue(this.compressLevel(expansion.Compress) + "");
            expansionsView.add(g);
        }
        g = new Details();
        g.setName(res.get(basename, "name4"));
        g.setValue(expansion.isSetQuota ? res.get(basename, "true") : res.get(basename, "false"));
        expansionsView.add(g);
        String quotaValueStr = "";
        if (expansion.isSetQuota) {
            double size = Double.valueOf(expansion.quotaValue.substring(0, expansion.quotaValue.length() - 1));
            if (expansion.quotaValue.endsWith("G")) {
                quotaValueStr = String.valueOf(size);
            }
            if (expansion.quotaValue.endsWith("M")) {
                size = size / 1024;
                quotaValueStr = String.valueOf(size);
            }
            if (expansion.quotaValue.endsWith("T")) {
                size = size * 1024;
                quotaValueStr = String.valueOf(size);
            }
            if (expansion.quotaValue.endsWith("P")) {
                size = size * 1024 * 1024;
                quotaValueStr = String.valueOf(size);
            }
            g = new Details();
            g.setName(res.get(basename, "name5"));
            g.setValue(quotaValueStr + "GB");
            expansionsView.add(g);
        }
        g = new Details();
        g.setName(res.get(basename, "name6"));
        g.setValue(expansion.getRecordsize() != null && !expansion.getRecordsize().equals("")
                ? expansion.recordsize + "B" : "");
        expansionsView.add(g);


        //扩展 磁带信息
//        importedTapes = tapesMap.get(info.name);
//        if (importedTapes == null) {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        TapeInformation[] allTape = vtl.getAllTapesInfo();
        if (allTape == null) {
            return;
        }
        String mediaPath = "/" + info.name;
        this.importedTapes = new ArrayList();
        List<String> importedTapesName = new ArrayList();
        for (int i = 0; i < allTape.length; i++) {
            if (allTape[i].path.equals(mediaPath)) {
                this.importedTapes.add(allTape[i]);
                importedTapesName.add(allTape[i].name);
            }
        }


//            String[] volTapes = vtl.ListTapeFile(mediaPath);
//            if (importedTapes != null && importedTapes.size() > 0) {
//
//                if (volTapes != null && volTapes.length > 0) {
//                    for (String tapeName : volTapes) {
//                        if (!importedTapesName.contains(tapeName)) {
//                            TapeInformation tape = new TapeInformation();
//                            tape.setBarcode("--");
//                            tape.setName(tapeName);
//                            tape.setTapeSize(0);
//                            tape.setLocationTypeID(-2);
//                            this.importedTapes.add(tape);
//                        }
//                    }
//                }
//            } else {
//                if (volTapes != null && volTapes.length > 0) {
//                    for (String tapeName : volTapes) {
//                        TapeInformation tape = new TapeInformation();
//                        tape.setBarcode("--");
//                        tape.setName(tapeName);
//                        tape.setTapeSize(0);
//                         tape.setLocationTypeID(-2);
//                        this.importedTapes.add(tape);
//                    }
//                }
//            }
//            tapesMap.put(info.name, importedTapes);
//        }

        int count = 0;
        long totalspacer = 0;
        long totalcapacity = 0;
        storageAreaTapeCount = new ArrayList();
        if (importedTapes != null && importedTapes.size() > 0) {
            for (TapeInformation tape : importedTapes) {
                count = count + 1;
                totalspacer = totalspacer + tape.fileSize;
                totalcapacity = totalcapacity + tape.tapeSize;
            }
            StorageAreaTapeCount storageTapeCount = new StorageAreaTapeCount();
            storageTapeCount.setCount(count + "");
            storageTapeCount.setTotalspace(MyConstants.sizeToString(totalspacer));
            storageTapeCount.setTotalcapacity(MyConstants.sizeToString(totalcapacity));
            storageAreaTapeCount.add(storageTapeCount);

        }

    }

    public String settings() {

        SystemOutPrintln.print_vtl("selected.name=" + selected.name);
        String title = "vtl";
        String fileSystemName = selected.name; //例如：SYSVOL/TAPE 或SYSVOL/XX/XX
        String returnStr = "/vtl/storage_area";
        String param = "fileSystemName=" + fileSystemName + "&" + "title=" + title + "&" + "return=" + returnStr;
        return "/disk/filesystem_set?faces-redirect=true&amp;" + param;
    }

//     public String fileSysSet(){
//     String param = "fileSystemName=" + fileSystemName + "&" + "isSetDedup=" + selected.isSetDedup + "&" + "isVerify=" + selected.isVerify + "&" + "isCompress=" + this.isCompress(selected.Compress) + "&" + "compress=" +  this.compressLevel(selected.Compress)  + "&" + "isSetQuota=" + selected.isSetQuota + "&" + "quotaValue=" + quotaValueStr + "&" + "recordsize=" + selected.recordsize + "&" + "used=" + selected.used+ "&" + "title=" + title +"&" + "return=" + returnStr;
//        System.out.println("selectFile.getUsed()="+selectFile.getUsed());
//        String quotaValueStr = selectFile.quotaValue;
//        if (quotaValueStr != null && !quotaValueStr.equals("") && !quotaValueStr.equalsIgnoreCase("null") && !quotaValueStr.equalsIgnoreCase("none")) {
//            if (quotaValueStr.contains(".")) {
//                quotaValueStr = quotaValueStr.split("\\.")[0];
//            }
//        }
//        String title="vtl";
//        String returnStr="diskarea";
//        
//        String param = "fileSystemName=" + selectFile.name + "&" + "isSetDedup=" + selectFile.isSetDedup + "&" + "isVerify=" + selectFile.isVerify + "&" + "isCompress=" + selectFile.isCompress+ "&" + "compress=" + selectFile.Compress+ "&" + "isSetQuota=" + selectFile.isSetQuota+ "&" + "quotaValue=" + quotaValueStr+ "&" + "recordsize=" + selectFile.recordsize+ "&" + "used=" + selectFile.used+ "&" + "title=" + title +"&" + "return=" + returnStr;
//        return "filesystem_set?faces-redirect=true&amp;" + param;
//    }
    public String storageCreate() {
        String param = "name=" + selected.name;
        return "add_tape?faces-redirect=true&amp;" + param;
    }

    public String deleteTape() {
        String param = "name=" + selected.name;
        return "del_tape?faces-redirect=true&amp;" + param;
    }

    public String exportTape() {
        String param = "name=" + selected.name;
        return "export_tape?faces-redirect=true&amp;" + param;
    }

    public String toCancle() {
        return "storage_area?faces-redirect=true";
    }

    public String importTape() {
        String param = "name=" + selected.name;
        return "import_tape?faces-redirect=true&amp;" + param;
    }

    public String totapes() {
        String param = "name=" + selected.name;
        return "tapes?faces-redirect=true&amp;" + param;
    }

    public int compressLevel(String Compress) {
        if (null == Compress || "".equalsIgnoreCase(Compress)) {
            return 0;
        }
        if ("off".equalsIgnoreCase(Compress)) {
            return 0;
        }
        if ("gzip".equalsIgnoreCase(Compress)) {
            return 6;
        }
        char num = Compress.charAt(Compress.length() - 1);
        try {
            return Integer.valueOf(num + "");
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isCompress(String Compress) {
        boolean returnStr = false;
        if (Compress.equalsIgnoreCase("null") || Compress.equalsIgnoreCase("") || Compress.equals("off")) {
            returnStr = false;
        } else {
            returnStr = true;

        }
        return returnStr;



//        if (null == Compress || "".equalsIgnoreCase(Compress)) {
//            return false;
//        }
//        return (!"off".equalsIgnoreCase(Compress));
    }

    public String toTapes() {
        String param = "name=" + "";
        return "tapes?faces-redirect=true&amp;" + param;
    }

    public String configSnapSync(FileSystemInformation share) {

        String param = "path=" + share.getName() + "&" + "module=vtl" + "&" + "returnURL=/vtl/storage_area";
        SystemOutPrintln.print_vtl("share.getName()=" + share.getName());
        //path为文件系统的路径，开头不带“/”
        //module为所属模块，例如disk,vtl
        //returnURL为需要返回的完整URL，包括结尾参数。
//        return "vtl_snap_sync?faces-redirect=true&amp;" + param;
        return "/nas/nas_snap_sync?faces-redirect=true&amp;" + param;
    }

    public List<FileSystemInfor> getViews() {
        if (views == null) {
            initVolumeNames();
            initStorageArea();
        }
        return views;
    }

    public void setViews(List<FileSystemInfor> views) {
        this.views = views;
    }

    public List<Details> getExpansionsView() {
        return expansionsView;
    }

    public void setExpansionsView(List<Details> expansionsView) {
        this.expansionsView = expansionsView;
    }

    public FileSystemInfor getSelected() {
        return selected;
    }

    public void setSelected(FileSystemInfor selected) {
        this.selected = selected;
    }

    public ArrayList<TapeInformation> getImportedTapes() {
        return importedTapes;
    }

    public void setImportedTapes(ArrayList<TapeInformation> importedTapes) {
        this.importedTapes = importedTapes;
    }

    public ArrayList<StorageAreaTapeCount> getStorageAreaTapeCount() {
        return storageAreaTapeCount;
    }

    public void setStorageAreaTapeCount(ArrayList<StorageAreaTapeCount> storageAreaTapeCount) {
        this.storageAreaTapeCount = storageAreaTapeCount;
    }

    public String getStorageAreaName() {
        return storageAreaName;
    }

    public void setStorageAreaName(String storageAreaName) {
        this.storageAreaName = storageAreaName;
    }
}
