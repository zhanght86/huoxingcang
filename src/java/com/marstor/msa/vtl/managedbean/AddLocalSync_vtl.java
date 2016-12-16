/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.nas.bean.SharePath;
import com.marstor.msa.nas.model.SyncStatus;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
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
@ManagedBean(name = "addLocalSync_vtl")
@ViewScoped
public class AddLocalSync_vtl implements Serializable{

    /**
     * Creates a new instance of AddLocalSync_vtl
     */
    private String selectedPath;
    private ArrayList<String> paths = new ArrayList<String>();
    private String sourcePath;
    private String module;
    private String returnURL;
    
    public AddLocalSync_vtl() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        sourcePath = request.getParameter("path");
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        String[] volumeNames = zfs.getVolumeNames();
//        ArrayList<SharePath> shares = InterfaceFactory.getNASInterfaceInstance().getAllSharePaths();
        //cc
        if (volumeNames != null) {
            for (String volumeName : volumeNames) {
                if (!(volumeName + "/TAPE").equals(sourcePath)) {
                    paths.add(volumeName + "/TAPE");
                }
            }
        }
        if (paths.size() == 0) {
            this.paths.add("");
        }
        if (paths.size() > 0) {
            this.selectedPath = paths.get(0);
        }
        String returnURL = request.getParameter("returnURL");
        String module = request.getParameter("module");
        if (module != null) {
            this.module = module;
        }
        if (returnURL != null) {
            this.returnURL = returnURL;
        }
        
    }
    
    public String getSelectedPath() {
        return selectedPath;
    }
    
    public void setSelectedPath(String selectedPath) {
        this.selectedPath = selectedPath;
    }
    
    public ArrayList<String> getPaths() {
        return paths;
    }
    
    public void setPaths(ArrayList<String> paths) {
        this.paths = paths;
    }
    
    public String save() {
        MSAResource res = new MSAResource();
         if (this.sourcePath == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getSourceNameFailed"),  res.get("getSourceNameFailed")));
            return null;
        }
        FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.sourcePath);
        if (fileSys == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getLocalFileSystemFailed"), res.get("getLocalFileSystemFailed")));
            return null;
        }
        SystemOutPrintln.print_vtl("source filesys path: " + fileSys.getStrName());
        SystemOutPrintln.print_vtl("source filesys isopen: " + fileSys.getiIsOpen());
        if (fileSys.getiIsOpen() != 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("sourceNotOpenSnap"), res.get("sourceNotOpenSnap")));
            return null;
        }
        SyncStatus status;
//        status = FileSysSnapSYNCInfo.getLocalSyncStatus(this.sourcePath);
//        
//        if(!status.isOpenAutoSnapOrNot()) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "源端共享目录未开启自动快照。", "源端共享目录未开启自动快照。"));
//            return null;
//        }
        status = FileSysSnapSYNCInfo.getLocalSyncStatus(this.selectedPath);
        if (status.isOnLineOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetMustOffLine"), res.get("targetMustOffLine")));
            return null;
        }
        if (status.isExistSnapOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetHaveSnap"),res.get("targetHaveSnap")));
            return null;
        }
       
//        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().setLocalSyncInfo(this.sourcePath, selectedPath);
        
//        if (!flag) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setLocalCopyFailed"), res.get("setLocalCopyFailed")));
//            return null;
//        }
        String param = "path=" + this.sourcePath + "&" + "activeIndex=1" + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "vtl_snap_sync?faces-redirect=true&amp;" + param;
    }
    
    public String cancel() {
        String param = "path=" + this.sourcePath + "&" + "activeIndex=1" + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "vtl_snap_sync?faces-redirect=true&amp;" + param;
    }
}
