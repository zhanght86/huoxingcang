/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.nas.model.GlobalSnapSYNCInfo;
import com.marstor.msa.nas.model.Protect;
import com.marstor.msa.nas.model.SyncStatus;
import com.marstor.msa.sync.axis2.client.SyncClient;
import com.marstor.msa.sync.bean.MarsHost;
import com.marstor.msa.sync.bean.SyncMapping;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class AddRemoteSync implements Serializable{

    private String ip;
    private String port;
    private String userName;
    private String connectPasswd;
    private String targetPath;
    private String transferPasswd;
    private String transferConfirmPasswd;
    private String sshPort;
    private String compressLevel;
     private ArrayList<String> compress = new ArrayList<String>();
    private ArrayList<String> levels = new ArrayList<String>();
    private ArrayList<String> ipList = new ArrayList<String>();
    private String selectedVolume, selectedIP, selectPath;
    private ArrayList<String> targetVolumes;
    private List<String> targetFileSys;
    private String sourcePath = "";
    private String pathName = "";
    private List<MarsHost> hosts;
    private MarsHost selectHost;
    private String module = "";
    private String returnURL;
    private String title = "", targetTitle, outputLabel;
    private MsaSYNCInterface syncInterface = InterfaceFactory.getSYNCInterfaceInstance();
    private HashMap<String, List<String>> remoteFileSys = new HashMap<String, List<String>>();
    private ArrayList<String> startTimeList = new ArrayList<String>();
    private ArrayList<String> endtimeList = new ArrayList<String>();
    private String radioValue;
    private String startTime = "";
    private String endTime = "";
    private boolean timeRendered;
    private boolean isLocal;
    private boolean connectDisabled;
    
     /*登录添加的属性起始点 PS：记得插入getter和setter方法*/  
    private boolean anony;
    private boolean isrender = false;
    /*登录添加的属性结束点 PS：记得插入getter和setter方法*/

    public AddRemoteSync() {
        initCompress();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        if (request.getParameter("path") != null) {
            sourcePath = request.getParameter("path");
        }
        String returnURL = request.getParameter("returnURL");
        String module = request.getParameter("module");
        if (module != null) {
            this.module = module;
        }
        MSAResource res = new MSAResource();
        if (this.module.equals("nas")) {
            this.title = res.get("nasTitle");
            this.targetTitle = res.get("targetPath");
            this.outputLabel = res.get("sharePath");
            this.pathName = "/" + this.sourcePath;
        } else if (this.module.equals("vtl")) {
            this.title = res.get("vtlTitle");
            this.targetTitle = res.get("targetStorage");
            this.outputLabel = res.get("storageSpace");
            this.pathName = this.sourcePath;
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
                this.connectDisabled = false;
            } else {
                this.ipList.add("");
                this.selectedIP = "";
                this.connectDisabled = true;
            }
        } else {
            this.ipList.add("");
            this.selectedIP = "";
            this.connectDisabled = true;
        }
        this.targetFileSys = new ArrayList<String>();
        this.targetFileSys.add("");
        this.selectPath = "";
        // targetVolumes = new ArrayList<String>();
//        this.compressLevel = "6";
        this.compressLevel = res.get("level10");
        // targetVolumes.add("");
        // this.selectedVolume = "";

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                startTimeList.add("0" + String.valueOf(i) + ":00");
                startTimeList.add("0" + String.valueOf(i) + ":30");
            } else {
                startTimeList.add(String.valueOf(i) + ":00");
                startTimeList.add(String.valueOf(i) + ":30");
            }
        }
        endtimeList.add("00:30");
        for (int i = 1; i < 25; i++) {

            if (i == 24) {
                endtimeList.add(String.valueOf(i) + ":00");
            } else if (i < 10) {
                endtimeList.add("0" + String.valueOf(i) + ":00");
                endtimeList.add("0" + String.valueOf(i) + ":30");
            } else {
                endtimeList.add(String.valueOf(i) + ":00");
                endtimeList.add(String.valueOf(i) + ":30");
            }
        }

        this.startTime = "00:00";
        this.endTime = "24:00";
        this.radioValue = "1";

    }
    
        public void initCompress() {
        MSAResource res = new MSAResource();
//        compress.add(res.get("level0"));
        compress.add(res.get("level6"));
        compress.add(res.get("level9"));
        compress.add(res.get("level10"));
    }
        
         public String toCompressLevel(String Compress) {
              MSAResource res = new MSAResource();
        if (null == Compress || "".equalsIgnoreCase(Compress)) {
//            return 0+"";
            return 6+"";
        }
        if (res.get( "level0").equalsIgnoreCase(Compress)) {
            return 6+"";
//            return 0+"";
        }else if (res.get( "level6").equalsIgnoreCase(Compress)) {
            return 6+"";
        }else if(res.get( "level9").equalsIgnoreCase(Compress)){
             return 9+"";
        }else if(res.get("level10").equalsIgnoreCase(Compress)){
             return 10+"";
        }else{
             return 6+"";
        }
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getOutputLabel() {
        return outputLabel;
    }

    public void setOutputLabel(String outputLabel) {
        this.outputLabel = outputLabel;
    }

    public String getTargetTitle() {
        return targetTitle;
    }

    public void setTargetTitle(String targetTitle) {
        this.targetTitle = targetTitle;
    }

    public String getSelectPath() {
        return selectPath;
    }

    public void setSelectPath(String selectPath) {
        this.selectPath = selectPath;
    }

    public boolean isAnony() {
        return anony;
    }

    public void setAnony(boolean anony) {
        this.anony = anony;
    }

    public boolean isIsrender() {
        return isrender;
    }

    public void setIsrender(boolean isrender) {
        this.isrender = isrender;
    }

    public boolean isConnectDisabled() {
        return connectDisabled;
    }

    public void setConnectDisabled(boolean connectDisabled) {
        this.connectDisabled = connectDisabled;
    }

    public String getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(String radioValue) {
        this.radioValue = radioValue;
    }

    public ArrayList<String> getStartTimeList() {
        return startTimeList;
    }

    public void setStartTimeList(ArrayList<String> startTimeList) {
        this.startTimeList = startTimeList;
    }

    public ArrayList<String> getEndtimeList() {
        return endtimeList;
    }

    public void setEndtimeList(ArrayList<String> endtimeList) {
        this.endtimeList = endtimeList;
    }

    public String getStartTime() {
        return startTime;
    }

    public ArrayList<String> getCompress() {
        return compress;
    }

    public void setCompress(ArrayList<String> compress) {
        this.compress = compress;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isTimeRendered() {
        return timeRendered;
    }

    public void setTimeRendered(boolean timeRendered) {
        this.timeRendered = timeRendered;
    }

    public boolean isIsLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<String> getTargetFileSys() {
        return targetFileSys;
    }

    public void setTargetFileSys(List<String> targetFileSys) {
        this.targetFileSys = targetFileSys;
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

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void listen() {
        if (this.radioValue.equals(("1"))) {
            this.timeRendered = false;
        } else {
            this.timeRendered = true;
        }
    }

    public String save() {
        MSAResource res = new MSAResource();
        if (this.selectHost == null) {
            if (this.module.equals("nas")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("ConnectAndSelect"), res.get("ConnectAndSelect")));
            } else if (this.module.equals("vtl")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("connectAndSelectSpace"), res.get("connectAndSelectSpace")));
            }
            return null;
        }
        Boolean  syncExistOrNot = this.isSyncExist();
        if (syncExistOrNot== null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getCopyListFailed"), res.get("getCopyListFailed")));
            return null;
        }
        if (syncExistOrNot) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetExist"), res.get("targetExist")));
            return null;
        }
        if (this.sourcePath == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getLocalFileSystemFailed"), res.get("getLocalFileSystemFailed")));
            return null;
        }
        if (this.remoteFileSys == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("connectFailed"), res.get("connectFailed")));
            return null;
        }
        if (this.selectPath == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetPathEmpty"), res.get("targetPathEmpty")));
            return null;
        }
        if (this.selectPath.startsWith("/") && this.selectPath.length() > 1) {
            this.selectPath = this.selectPath.substring(1);
        }
        SyncStatus status;
        status = FileSysSnapSYNCInfo.getRemoteSyncStatus(this.selectPath, this.selectHost.getIp(), this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword());
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getTargetFileSystemFailed"), res.get("getTargetFileSystemFailed")));
            return null;
        }

        if (status.isSyncTargetOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetCopied"), res.get("targetCopied")));
            return null;
        }
        if (status.isOnLineOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("remoteMustOffLine"), res.get("remoteMustOffLine")));
            return null;
        }
        if (status.isExistSnapOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("remoteCanNotIncludeSnap"), res.get("remoteCanNotIncludeSnap")));
            return null;
        }
        int transferPort = GlobalSnapSYNCInfo.getInstance().getTransferPort();
        if (transferPort == -1) { //如果获取到的传输端口值为-1，则获取失败
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getModuleConfigInfoFailed"), res.get("getModuleConfigInfoFailed")));
            return null;
        }
        // String targetFileSys = this.selectedVolume + source;
        Debug.print("add remote sync  targetFileSys: " + targetFileSys);
        boolean flag;
        boolean isFixedTime = this.radioValue.equals("1") ? false : true;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date startDate, endDate;
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        try {
            startDate = df.parse(this.startTime);
            endDate = df.parse(this.endTime);
            startCal.setTime(startDate);
            endCal.setTime(endDate);
        } catch (Exception ex) {
            Debug.print(ex.getMessage());
        }
        //syncInterface.setRemoteSyncInfo(target, title, title, title, title, title, title, title, title, title, null, null, flag);
        flag = syncInterface.setRemoteSyncInfo(this.selectHost.getIp(), this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword(), this.selectHost.getSshPassword(), toCompressLevel(this.compressLevel) , this.sourcePath, this.selectPath, this.selectHost.getSshport(), String.valueOf(transferPort), startCal, endCal, isFixedTime);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addRemoteTargetFailed"), res.get("addRemoteTargetFailed")));
            return null;
        }
        // syncInterface.setRemoteSyncInfo(this.selectedIP, this.selectHost.getIp(),this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword() );
        String param = "path=" + this.sourcePath + "&" + "activeIndex=1" + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "nas_snap_sync?faces-redirect=true&amp;" + param;
    }

    public String cancel() {
        String param = "path=" + this.sourcePath + "&" + "activeIndex=1" + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "nas_snap_sync?faces-redirect=true&amp;" + param;
    }
    //判断当前添加的远程同步是否存在

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
    
    //登录通用Action
    private boolean loginAction() {
        SyncClient sync = new SyncClient(this.selectHost.getIp(), Integer.parseInt(this.selectHost.getPort()));
        if(this.userName.equals("") || this.connectPasswd.equals("")){
            this.isrender = true;
            return false;
        }        
        if (sync.login(this.userName, this.connectPasswd) == 4) {
            com.marstor.msa.ba.util.Debug.print("Login Validation success!");
            return true;
        }
        this.isrender = true;
        return false;
    }
    
    //取消按钮Action，另，确定按钮对应的Action末尾需要手动调用此Action。
    public void closeAction(){
        this.userName = "";
        this.connectPasswd = "";
        this.anony = false;
        this.isrender = false;
    }

    public void connect() {

        this.selectHost = new MarsHost();
        for (MarsHost host : hosts) {
            if (host.getIp().equals(this.selectedIP)) {
                this.selectHost = host;
                break;
            }
        }
        
         /*登录代码起始点*/
        String usr;
        String pwd;
        this.isrender = false;
        if(!this.anony){
            if(!loginAction()){
                com.marstor.msa.ba.util.Debug.print("Login Validation failed!");
                this.isrender = true;
                return ;
            }
            usr = this.userName;
            pwd = this.connectPasswd;
        }else{
            usr = "anonymoususer";
            pwd = "anonymoususer";
        }        
        RequestContext.getCurrentInstance().execute("PF('login').hide()");
        /*登录代码结束点*/
        
        this.remoteFileSys = syncInterface.getTargetServerFileSystem(selectHost.getIp(), selectHost.getPort(), usr, pwd);
        if (remoteFileSys == null) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("connectFailed"), res.get("connectFailed")));
            return;
        }
//        List<String> volumes = remoteFileSys.get("Volume");
//        if (volumes != null) {
//            targetVolumes = new ArrayList<String>();
//            for (String volume : volumes) {
//                this.targetVolumes.add(volume);
//            }
//        }
        List<String> fileSystem = remoteFileSys.get("FileSystem");
        if (fileSystem != null) {
            this.targetFileSys = new ArrayList<String>();
            if (this.module.equals("nas")) { //如果当前页面为NAS模块的页面
                for (String path : fileSystem) {
                    if (path.contains("/NAS/") && !path.contains(Protect.copyFlag)) {
                        this.targetFileSys.add("/" + path);
                    }
                }
            } else if (this.module.equals("vtl")) { //如果当前页面为VTL模块的页面
                for (String path : fileSystem) {
                    if (path.contains("/TAPE")) {
                        this.targetFileSys.add(path);
                    }
                }
            }

            if (this.targetFileSys.size() == 0) {
                this.targetFileSys.add("");
                this.selectPath = "";
            } else {
                this.selectPath = this.targetFileSys.get(0);
            }
        }
           //确定按钮Action末尾手动调用closeAction()
        closeAction();
    }

    public String addRemoteDevice() {
        //return "/sync/sync_add_mars_host.xhtml";
        String param = "url=/nas/nas_add_remote_sync" + "&" + "path=" + this.sourcePath + "&" + "returnURL=" + this.returnURL + "&" + "module=" + this.module;
        return "/sync/sync_add_mars_host?faces-redirect=true&amp;" + param;
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
//        String sourceVolume = this.sourcePath.substring(0, this.sourcePath.indexOf("/"));
//        String source = this.sourcePath.substring(this.sourcePath.indexOf("/"));
//        List<String> fileSystem = this.remoteFileSys.get("FileSystem");
//        String target = "";
//        //在所有目标文件系统中查找和选中的卷组下的源端文件系统（第一个"/"后）相同的名称
//        for (String str : fileSystem) {
//            if (!str.contains("/")) {
//                continue;
//            }
//            String volumeName = str.substring(0, str.indexOf("/"));
//            if (volumeName.equals(this.selectedVolume)) {
//                if (str.substring(str.indexOf("/")).equals(source)) {
//                    target = str.substring(str.indexOf("/"));
//                    break;
//                }
//            }
//        }
//        Debug.print("target  : " + target);
//        Debug.print("source  : " + source);
//        if (target != null && !target.equals("")) {
//            if (source.equals(target)) { // 判断源和目标目录中第一个“/”后的字符串是否相等，即判断目标是否有源端的同名文件系统，如果是则判断状态。
//                SyncStatus status;
//                String path = this.selectedVolume + target;
//                Debug.print("remote path : " + path);
//                status = FileSysSnapSYNCInfo.getRemoteSyncStatus(path, this.selectHost.getIp(), this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword());
//                if (status == null) {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getTargetFileSystemFailed"), res.get("getTargetFileSystemFailed")));
//                    return null;
//                }
//
//                if (status.isSyncTargetOrNot()) {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetCopied"), res.get("targetCopied")));
//                    return null;
//                }
//                if (status.isOnLineOrNot()) {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("remoteMustOffLine"), res.get("remoteMustOffLine")));
//                    return null;
//                }
//                if (status.isExistSnapOrNot()) {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("remoteCanNotIncludeSnap"), res.get("remoteCanNotIncludeSnap")));
//                    return null;
//                }
//            }
//        }
//判断文件系统状态
//        FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.sourcePath);
//        if (fileSys == null) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getLocalInfoFailed"), res.get("getLocalInfoFailed")));
//            return null;
//        }
//        if (fileSys.getiIsOpen() != 1) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("sourceFileSystemNotOpenSnap"), res.get("sourceFileSystemNotOpenSnap")));
//            return null;
//        }
