/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.nas.model.GlobalSnapSYNCInfo;
import com.marstor.msa.nas.model.SyncStatus;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.bean.MarsHost;
import com.marstor.msa.sync.bean.SyncMapping;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
@ManagedBean(name = "addRemoteSync_vtl")
@ViewScoped
public class AddRemoteSync_vtl  implements Serializable{

    /**
     * Creates a new instance of AddRemoteSync_vtl
     */
    private String ip;
    private String port;
    private String userName;
    private String connectPasswd;
    private String targetPath;
    private String transferPasswd;
    private String transferConfirmPasswd;
    private String sshPort;
    private String compressLevel;
    private ArrayList<String> levels = new ArrayList<String>();
    private ArrayList<String> ipList = new ArrayList<String>();
    private String selectedVolume, selectedIP;
    private ArrayList<String> targetVolumes;
    private String sourcePath = "";
    private List<MarsHost> hosts;
    private MarsHost selectHost;
    private String module;
    private String returnURL;
    private MsaSYNCInterface syncInterface = InterfaceFactory.getSYNCInterfaceInstance();
    private HashMap<String, List<String>> remoteFileSys = new HashMap<String, List<String>>();

    public AddRemoteSync_vtl() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        sourcePath = request.getParameter("path");
        String returnURL = request.getParameter("returnURL");
        String module = request.getParameter("module");
        if (module != null) {
            this.module = module;
        }
        if (returnURL != null) {
            this.returnURL = returnURL;
        }
        hosts = syncInterface.getMarsHost();
        if (hosts != null) {
            for (MarsHost host : hosts) {
                this.ipList.add(host.getIp());
            }
            if (ipList.size() > 0) {
                this.selectedIP = ipList.get(0);
            } else {
                this.ipList.add("");
                this.selectedIP = "";
            }
        } else {
            this.ipList.add("");
            this.selectedIP = "";
        }
        targetVolumes = new ArrayList<String>();
        this.compressLevel = "6";
        targetVolumes.add("");
        this.selectedVolume = "";

    }

    public ArrayList<String> getIpList() {
        return ipList;
    }

    public void setIpList(ArrayList<String> ipList) {
        this.ipList = ipList;
    }

    public String getSelectedIP() {
        return selectedIP;
    }

    public void setSelectedIP(String selectedIP) {
        this.selectedIP = selectedIP;
    }

    public String getSelectedVolume() {
        return selectedVolume;
    }

    public void setSelectedVolume(String selectedVolume) {
        this.selectedVolume = selectedVolume;
    }

    public ArrayList<String> getTargetVolumes() {
        return targetVolumes;
    }

    public void setTargetVolumes(ArrayList<String> targetVolumes) {
        this.targetVolumes = targetVolumes;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getConnectPasswd() {
        return connectPasswd;
    }

    public void setConnectPasswd(String connectPasswd) {
        this.connectPasswd = connectPasswd;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getTransferPasswd() {
        return transferPasswd;
    }

    public void setTransferPasswd(String transferPasswd) {
        this.transferPasswd = transferPasswd;
    }

    public String getTransferConfirmPasswd() {
        return transferConfirmPasswd;
    }

    public void setTransferConfirmPasswd(String transferConfirmPasswd) {
        this.transferConfirmPasswd = transferConfirmPasswd;
    }

    public String getSshPort() {
        return sshPort;
    }

    public void setSshPort(String sshPort) {
        this.sshPort = sshPort;
    }

    public String getCompressLevel() {
        return compressLevel;
    }

    public void setCompressLevel(String compressLevel) {
        this.compressLevel = compressLevel;
    }

    public ArrayList<String> getLevels() {
        return levels;
    }

    public void setLevels(ArrayList<String> levels) {
        this.levels = levels;
    }

    public String save() {
        MSAResource res = new MSAResource();
        if (this.selectHost == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ConnectAndSelect"),res.get("ConnectAndSelect")));
            return null;
        }
        if (this.isSyncExist() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getCopyListFailed"), res.get("getCopyListFailed")));
            return null;
        }
        if (this.isSyncExist()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetExist"), res.get("targetExist")));
            return null;
        }
        if (this.sourcePath == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getLocalFileSystemFailed"), res.get("getLocalFileSystemFailed")));
            return null;
        }
        //判断文件系统状态
        FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.sourcePath);
        if (fileSys == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getLocalInfoFailed"), res.get("getLocalInfoFailed")));
            return null;
        }
        if (fileSys.getiIsOpen() != 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("sourceFileSystemNotOpenSnap"), res.get("sourceFileSystemNotOpenSnap")));
            return null;
        }

        if (this.remoteFileSys == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("connectFailed"), res.get("connectFailed")));
            return null;
        }
        String sourceVolume = this.sourcePath.substring(0, this.sourcePath.indexOf("/"));
        String source = this.sourcePath.substring(this.sourcePath.indexOf("/"));
        List<String> fileSystem = this.remoteFileSys.get("FileSystem");
        String target = "";
        //在所有目标文件系统中查找和选中的卷组下的源端文件系统（第一个"/"后）相同的名称
        for (String str : fileSystem) {
            if (!str.contains("/")) {
                continue;
            }
            String volumeName = str.substring(0, str.indexOf("/"));
            if (volumeName.equals(this.selectedVolume)) {
                if (str.substring(str.indexOf("/")).equals(source)) {
                    target = str.substring(str.indexOf("/"));
                    break;
                }
            }
//            if (str.startsWith(this.selectedVolume)) {
//                if (str.contains("/")) {
//                    if(str.substring(str.indexOf("/")).equals(source)) {
//                        
//                    }
//                    target = str.substring(str.indexOf("/"));
//                    break;
//                }
//            }
        }
        SystemOutPrintln.print_vtl("target  : " + target);
        SystemOutPrintln.print_vtl("source  : " + source);
        if (target != null && !target.equals("")) {
            if (source.equals(target)) { // 判断源和目标目录中第一个“/”后的字符串是否相等，即判断目标是否有源端的同名文件系统，如果是则判断状态。
                SyncStatus status;
                String path = this.selectedVolume + target;
                SystemOutPrintln.print_vtl("remote path : " + path);
                status = FileSysSnapSYNCInfo.getRemoteSyncStatus(path, this.selectHost.getIp(), this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword());
                if (status == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getTargetFileSystemFailed"),res.get("getTargetFileSystemFailed")));
                    return null;
                }

                if (status.isSyncTargetOrNot()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetCopied"),res.get("targetCopied")));
                    return null;
                }
                if (status.isOnLineOrNot()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("remoteMustOffLine"),res.get("remoteMustOffLine")));
                    return null;
                }
                if (status.isExistSnapOrNot()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("remoteCanNotIncludeSnap"), res.get("remoteCanNotIncludeSnap")));
                    return null;
                }
            }
        }

//        if (sourceVolume.equals(this.selectedVolume)) { //如果源卷组和目标卷组相同，则需要进一步判断
//            List<String> fileSystem = this.remoteFileSys.get("FileSystem");
//            System.out.println("###################### source file sys : " + source);
//            for (String str : fileSystem) {
//                if (!str.contains("/")) {
//                    continue;
//                }
//                String target = str.substring(str.indexOf("/"));// 获取目录名称中第一个“/”后的字符串
        // System.out.println("###################### target file sys : " + source);

//            }
//        }


        int transferPort = GlobalSnapSYNCInfo.getInstance().getTransferPort();
        if (transferPort == -1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getModuleConfigInfoFailed"), res.get("getModuleConfigInfoFailed")));
            return null;
        }
        String targetFileSys = this.selectedVolume + source;
        SystemOutPrintln.print_vtl("add remote sync  targetFileSys: " + targetFileSys);
//        boolean flag = syncInterface.setRemoteSyncInfo(this.selectHost.getIp(), this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword(), this.selectHost.getSshPassword(), this.compressLevel, this.sourcePath, targetFileSys, this.selectHost.getSshport(), String.valueOf(transferPort));
//        if (!flag) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addRemoteTargetFailed"),res.get("addRemoteTargetFailed")));
//            return null;
//        }
        // syncInterface.setRemoteSyncInfo(this.selectedIP, this.selectHost.getIp(),this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword() );
        String param = "path=" + this.sourcePath + "&" + "activeIndex=1" + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "vtl_snap_sync?faces-redirect=true&amp;" + param;
    }

    public String cancel() {
        String param = "path=" + this.sourcePath + "&" + "activeIndex=1" + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "vtl_snap_sync?faces-redirect=true&amp;" + param;
    }

    public Boolean isSyncExist() {
        List<SyncMapping> mapps = syncInterface.getSyncMapping(this.sourcePath);
        if (mapps == null) {
            return null;
        }
        String source = this.sourcePath.substring(this.sourcePath.indexOf("/"));
        String targetFileSys = this.selectedVolume + source;
        //    Sync sync = new Sync(this.selectedVolume, this.selectedIP);
        for (SyncMapping map : mapps) {
            if (map.getiIsLocal() != 1) {
                if (this.selectedIP.equals(map.getStrIP()) && targetFileSys.equals(map.getStrDescFileSystem())) {
                    return true;
                }
//                Sync remote = new Sync(map.getStrDescFileSystem(), map.getStrIP());
//                if (sync.equals(remote)) {
//                    return true;
//                }
            }
        }
        return false;
    }

    public void connect() {
        this.selectHost = new MarsHost();
        for (MarsHost host : hosts) {
            if (host.getIp().equals(this.selectedIP)) {
                this.selectHost = host;
                break;
            }
        }
        this.remoteFileSys = syncInterface.getTargetServerFileSystem(selectHost.getIp(), selectHost.getPort(), selectHost.getUsername(), selectHost.getPassword());
        if (remoteFileSys == null) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get("connectFailed") , res.get("connectFailed")));
            return;
        }
        List<String> volumes = remoteFileSys.get("Volume");
        if (volumes != null) {
//            if (targetVolumes.contains("")) {
//                this.targetVolumes.remove("");
//            }
            targetVolumes = new ArrayList<String>();
            for (String volume : volumes) {
                this.targetVolumes.add(volume);
            }
        }

    }

    public static boolean checkNum(String name, boolean canZero) {
        boolean flag = false;
        if (canZero) {
            if (name.length() <= 6) {
                flag = name.matches("^[0-9]+$");
            }
        } else {
            if (name.length() <= 6) {
                flag = name.matches("^[1-9][0-9]*$") || name.matches("^[1-9]$");
            }
        }
        return flag;
    }

    public static boolean checkPort(String name) {
        boolean flag = false;
        if (Long.parseLong(name) <= 65535 && Long.parseLong(name) > 0) {
            flag = true;
        }
        return flag;
    }
}
