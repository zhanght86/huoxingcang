/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.bean.FileSystemInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.bean.CIFSParametersForSharePath;
import com.marstor.msa.nas.bean.NFSParametersForSharePath;
import com.marstor.msa.nas.bean.SharePath;
import com.marstor.msa.nas.web.NASInterface;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.bean.StatusInfo;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;

/**
 *
 * @author Administrator
 */
@javax.faces.bean.ManagedBean(name = "sharelistbean")
@ViewScoped
public class ShareListBean implements Serializable {

    private List<Share> list;
    //private static boolean flag = true;
    private Share selectedShareBean;
    private Share editBean = new Share();
    private Share editPath = new Share();
    private String path = "";
    private String cifs = "";
    private String shareName = "";
    private String errorInfo;
    private String fileSysList = "";
    private ACL selectedACL;
    private boolean authorityButtonStatus;
    private NFS configNFS = new NFS();
    private Map<String, String> cloneMap;

    public ShareListBean() {
    }

    public void initShareList() {
        //  InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(cifs)
        Map<String, StatusInfo> allStatusInfo = InterfaceFactory.getSYNCInterfaceInstance().getAllFileSystemStatus();
        if (allStatusInfo == null) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysFailed"), res.get("getFileSysFailed")));
            return;
        }
        list = new ArrayList<Share>();
        cloneMap = InterfaceFactory.getSYNCInterfaceInstance().getCloneOriginMap();
        if (cloneMap == null) {
            return;
        }
        NASInterface nas = InterfaceFactory.getNASInterfaceInstance();
        ArrayList<SharePath> paths = nas.getAllSharePaths();
        if (paths == null) {
            // getShareFailed
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getShareFailed"), res.get("getShareFailed")));
            return;
        }
        for (int i = 0; i < paths.size(); i++) {

            SharePath path = paths.get(i);
            FileSystemInformation info = InterfaceFactory.getZFSInterfaceInstance().getFileSystemInformation(path.getPath());
            if (info == null) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysFailed"), res.get("getFileSysFailed")));
                return;
            }
            FileSystem sysInfo = new FileSystem(path.getPath(), info.used, info.available, info.mountpoint, info.quotaValue, info.isSetQuota, info.isSetDedup, info.isMounted, info.Compress, info.isVerify, info.recordsize);
            Share share = new Share(path.getPath(), path.getCifsShareName(), path.getAvailable(), path.getUsed(), path.getMountpoint(), path.isCifsOnOrOff(), path.isNfsOnOrOff(), path.isMountedOrNot(), path.getCasesensitivity(), sysInfo, allStatusInfo, cloneMap);
            list.add(share);
            //aa.substring(aa.lastIndexOf("/")+1)
            if (i + 1 < paths.size()) {
                //fileSysList = fileSysList + path.getPath().substring(path.getPath().lastIndexOf("/") + 1) + ",";
                fileSysList = fileSysList + path.getPath() + ",";
            } else {
                fileSysList = fileSysList + path.getPath();
            }
        }

    }

    public void init() {
        Map<String, StatusInfo> allStatusInfo = InterfaceFactory.getSYNCInterfaceInstance().getAllFileSystemStatus();
        if (allStatusInfo == null) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysFailed"), res.get("getFileSysFailed")));
            return;
        }
        Map<String, String> cloneMap = InterfaceFactory.getSYNCInterfaceInstance().getCloneOriginMap();
        if (cloneMap == null) {
            return;
        }
        List<Share> shareList = new ArrayList<Share>();
        String fileSysStr = "";
        NASInterface nas = InterfaceFactory.getNASInterfaceInstance();
        ArrayList<SharePath> paths = nas.getAllSharePaths();
        if (paths == null) {
            // getShareFailed
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getShareFailed"), res.get("getShareFailed")));
            return;
        }
        for (int i = 0; i < paths.size(); i++) {
            SharePath path = paths.get(i);
            FileSystemInformation info = InterfaceFactory.getZFSInterfaceInstance().getFileSystemInformation(path.getPath());
            if (info == null) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysFailed"), res.get("getFileSysFailed")));
                return;
            }
            FileSystem sysInfo = new FileSystem(path.getPath(), info.used, info.available, info.mountpoint, info.quotaValue, info.isSetQuota, info.isSetDedup, info.isMounted, info.Compress, info.isVerify, info.recordsize);
            Share share = new Share(path.getPath(), path.getCifsShareName(), path.getAvailable(), path.getUsed(), path.getMountpoint(), path.isCifsOnOrOff(), path.isNfsOnOrOff(), path.isMountedOrNot(), path.getCasesensitivity(), sysInfo, allStatusInfo, cloneMap);
            shareList.add(share);
            //aa.substring(aa.lastIndexOf("/")+1)
            if (i + 1 < paths.size()) {
                //fileSysList = fileSysList + path.getPath().substring(path.getPath().lastIndexOf("/") + 1) + ",";
                fileSysStr = fileSysStr + path.getPath() + ",";
            } else {
                fileSysStr = fileSysStr + path.getPath();
            }

        }

        this.fileSysList = fileSysStr;
        this.list = shareList;

    }

    public boolean isAuthorityButtonStatus() {
        return authorityButtonStatus;
    }

    public void setAuthorityButtonStatus(boolean authorityButtonStatus) {
        this.authorityButtonStatus = authorityButtonStatus;
    }

    public Share getEditPath() {
        return editPath;
    }

    public void setEditPath(Share editPath) {
        this.editPath = editPath;
    }

    public ACL getSelectedACL() {
        return selectedACL;
    }

    public void setSelectedACL(ACL selectedACL) {
        this.selectedACL = selectedACL;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public Share getEditBean() {
        return editBean;
    }

    public void setEditBean(Share editBean) {
        this.editBean = editBean;
    }

    public String getCifs() {
        return cifs;
    }

    public void setCifs(String cifs) {
        this.cifs = cifs;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Share getSelectedShareBean() {
        return selectedShareBean;
    }

    public void setSelectedShareBean(Share selectedShareBean) {
        this.selectedShareBean = selectedShareBean;
    }

    public List<Share> getList() {
        if (list == null) {
            initShareList();
        }
        return list;
    }

    public void setList(List<Share> shareList) {
        this.list = shareList;
    }

    public void deletePath() {
        SharePath path = new SharePath();
        path.setPath(this.selectedShareBean.getPath());
        path.setVolumeName(this.selectedShareBean.getVolumeName());
        boolean flag;
        if (this.selectedShareBean.isCopyOrNot()) {
            flag = InterfaceFactory.getSYNCInterfaceInstance().deleteClone(this.selectedShareBean.getPath());
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delCopyFailed"), res.get("delCopyFailed")));
                return;
            }
        } else {
            flag = InterfaceFactory.getNASInterfaceInstance().deleteSharePath(path);
            if (!flag) {
                Debug.print("del path " + flag);
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("deleteFailed"), res.get("deleteFailed")));
                return;
            }
        }

        this.init();
    }

    //author
    public String doBeforeConfigAuthority(Share share) {

        String path = share.getPath();
        String param = "path=" + path;
        return "nas_authority?faces-redirect=true&amp;" + param;
    }
    //nfs

    public String doBeforeConfigNFS(Share share) {

//        NFSParametersForSharePath nfsParameters = InterfaceFactory.getNASInterfaceInstance().getSpecifiedNFSParameters(path);
//        String startOrNot = nfsParameters.isStart() ? "1" : "0";
//        String anonoyVisit = nfsParameters.isAnonymousCanVisit() ? "1" : "0";
//        String anonoyRW = nfsParameters.isAnonymousCanReadWrite() ? "1" : "0";
//        String rwList = nfsParameters.getRwIpList();
//        String roList = nfsParameters.getRoIpList();
//        String rootList = nfsParameters.getCanAsRootVisitIP();
        String params = "path=" + share.getPath();
//                + "&" + "startOrNot=" + startOrNot + "&" + "anonoyVisit=" + anonoyVisit +  "&" + "anonoyRW=" + anonoyRW + "&" + "rwList=" + rwList + "&" + "roList=" + roList + "&" + "rootList=" + rootList  ;

        return "nas_nfs_config?faces-redirect=true&amp;" + params;
    }

    public boolean closeAutoSnap() {
        boolean flag;
        FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.selectedShareBean.getPath());
        if (fileSys == null) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysFailed"), res.get("getFileSysFailed")));
            return false;
        }
        if (fileSys.getiIsOpen() == 1) {//如果自动快照已经开启
            flag = InterfaceFactory.getSYNCInterfaceInstance().cancelTimingSnapshot(this.selectedShareBean.getPath(), false);
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("closeAutoSnapFailed"), res.get("closeAutoSnapFailed")));
                return false;
            }
        }
        return true;
    }

    public boolean closeShare() {
        if (this.selectedShareBean.isCifsOnOrOff()) {
            boolean flag = InterfaceFactory.getNASInterfaceInstance().turnoffCIFSForSharePath(this.selectedShareBean.getPath());
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("configFailed"), res.get("configFailed")));
                Debug.print("config cifs" + flag);
                return false;
            }
        }
        if (this.selectedShareBean.isNfsOnOrOff()) {
            boolean flag = InterfaceFactory.getNASInterfaceInstance().turnOffNFSForSharePath(this.selectedShareBean.getPath());
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("setFailed"), res.get("setFailed")));
                Debug.print("turn off nfs" + flag);
                return false;
            }
        }
        return true;
    }

    public boolean offLine() {
        boolean flag;
        flag = InterfaceFactory.getNASInterfaceInstance().setSharePathStatus(this.selectedShareBean.getPath(), 0);
        if (!flag) {
            Debug.print("offline" + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("offlineFailed"), res.get("offlineFailed")));
            return false;
        }
        this.init();
        return true;
    }

    public void closeSnapBeforeOffline() {
        if (!this.closeAutoSnap()) {
            return;
        }
        if (!this.offLine()) {
            return;
        }
    }

    public void closeShareBeforeOffline() {
        if (!this.closeShare()) {
            return;
        }
        if (!this.offLine()) {
            return;
        }
    }

    public void closeShareBeforeDeletePath() {
        if (!this.closeShare()) {
            return;
        }
        this.deletePath();
    }

    public void closeShareAndDelCopyBeforeDeletePath() {
        if (!this.closeShare()) {
            return;
        }
        if (!this.removeClone(this.selectedShareBean)) {
            return;
        }
        this.deletePath();
    }

    public void closeSnapAndShareBeforeOffline() {
        if (!this.closeShare()) {
            return;
        }
        if (!this.closeAutoSnap()) {
            return;
        }
        if (!this.offLine()) {
            return;
        }
    }

    public void onLine(Share share) {
        String array[] = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(share.getPath());
        if (array == null) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysFailed"), res.get("getFileSysFailed")));
            return;
        }
        if (array[4].equals("1")) {
            if (!array[5].equals("1")) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("syncTargetOnline"), res.get("syncTargetOnline")));
                return;
            }
        }
//        if (share.isOnLineDisable()) {
//            Debug.print("###############$$$$$$$$$$$ sync target online");
//            MSAResource res = new MSAResource();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("syncTargetOnline"), res.get("syncTargetOnline")));
//            return;
//        }
        boolean flag = InterfaceFactory.getNASInterfaceInstance().setSharePathStatus(share.getPath(), 1);
        if (!flag) {
            Debug.print("online" + flag);
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("onlineFailed"), res.get("onlineFailed")));
            return;
        }
        this.init();
    }

    public String doBeforeConfigCIFS(Share share) {
//        FacesContext context = FacesContext.getCurrentInstance();
//        String pathName = context.getExternalContext().getRequestParameterMap().get("path");
//        Share share = this.getShareByPath(pathName);
//        this.editPath = new Share();
//        editPath.setPath(share.getPath());
//        editPath.getCifs().setShareName(share.getCifs().getShareName());
        String path = share.getPath();
        String param = "path=" + path;
        return "nas_cifs_config?faces-redirect=true&amp;" + param;
    }

    public void display() {
        //RequestContext.getCurrentInstance().
    }

    public String skipToCreateShare() {
        String param = "fileSysList=" + this.fileSysList;
        Debug.print("param: " + param);
        return "nas_create_share?faces-redirect=true&amp;" + param;
    }

    public void removeSharePath(Share share) {
        List<String> targets = InterfaceFactory.getCDPInterfaceInstance().getExistJobTargetPath();
        for (String target : targets) {
            if (target.equals("/" + this.selectedShareBean.getPath())) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("existTarget"), res.get("existTarget")));
                return;
            }
        }
        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().getFileSystemSyncStatus(share.getPath());
        if (flag) {
            RequestContext.getCurrentInstance().execute("syncing.show()");
            return;
        }
        if (share.isCifsOnOrOff() || share.isNfsOnOrOff()) { //如果共享开启
            // RequestContext.getCurrentInstance().addCallbackParam("result", "opened");
            if (share.isContainsCopy()) {
                RequestContext.getCurrentInstance().execute("delPathConfirmCloseShareDelCopy.show()");
            } else {
                RequestContext.getCurrentInstance().execute("deletePathConfirmCloseShare.show()");
            }

            return;
        }
        if (share.isContainsCopy()) {
            RequestContext.getCurrentInstance().execute("deleteCopy.show()");
            return;
        }
        RequestContext.getCurrentInstance().execute("deletePath.show()");



    }

    public void doBeforeOffLine(Share share) {
        boolean shareOpenOrNot = false, snapExistOrNot = false;
        if (share.isCifsOnOrOff() || share.isNfsOnOrOff()) {
            // RequestContext.getCurrentInstance().addCallbackParam("result", "opened");
            shareOpenOrNot = true;
            //RequestContext.getCurrentInstance().execute("closeShare.show()");
        }
        // else {
        FileSystemInfo fileSys = InterfaceFactory.getSYNCInterfaceInstance().getFileSystemInfo(share.getPath());
        // FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(share.getPath());
        if (fileSys == null) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysFailed"), res.get("getFileSysFailed")));
            return;
        }
        if (fileSys.getiIsOpen() == 1) {
            //confirmCloseSnap
            snapExistOrNot = true;
            // RequestContext.getCurrentInstance().execute("offlineConfirmCloseSnap.show()");
        }
//        else {
//            RequestContext.getCurrentInstance().execute("offlineConfirm.show()");
//        }
        if (shareOpenOrNot && !snapExistOrNot) {
            RequestContext.getCurrentInstance().execute("closeShare.show()");
            return;
        }
        if (!shareOpenOrNot && snapExistOrNot) {
            RequestContext.getCurrentInstance().execute("offlineConfirmCloseSnap.show()");
            return;
        }
        if (shareOpenOrNot && snapExistOrNot) {
            RequestContext.getCurrentInstance().execute("confirmCloseSnapAndShare.show()");
            return;
        }
        if (!shareOpenOrNot && !snapExistOrNot) {
            RequestContext.getCurrentInstance().execute("offlineConfirm.show()");

        }
        // RequestContext.getCurrentInstance().addCallbackParam("result", "closed");


        // }
    }

    public String configSnapSync(Share share) {

        String param = "path=" + share.getPath() + "&" + "module=nas" + "&" + "returnURL=/nas/nas_path";
        //path为文件系统的路径，开头不带“/”
        //module为所属模块，例如disk,vtl
        //returnURL为需要返回的完整URL，包括结尾参数。
        return "nas_snap_sync?faces-redirect=true&amp;" + param;
    }

    public String configFileSys(Share share) {
        FileSystem sys = share.getFileSysInfo();
        String quotaValueStr = sys.getQuotaValue();
        if (quotaValueStr != null && !quotaValueStr.equals("") && !quotaValueStr.equalsIgnoreCase("null") && !quotaValueStr.equalsIgnoreCase("none")) {
            if (quotaValueStr.contains(".")) {
                quotaValueStr = quotaValueStr.split("\\.")[0];
            }
        }
        String title = "nas";
        String returnStr = "/nas/nas_path";
        String param = "fileSystemName=" + "/" + sys.getName() + "&" + "isSetDedup=" + sys.isIsSetDedup() + "&" + "isVerify=" + sys.isIsVerify() + "&" + "isCompress=" + sys.isIsCompress() + "&" + "compress=" + sys.compressLevel(sys.getCompress()) + "&" + "isSetQuota=" + sys.isIsSetQuota() + "&" + "quotaValue=" + quotaValueStr + "&" + "recordsize=" + sys.getRecordsize() + "&" + "used=" + sys.getUsed() + "&" + "title=" + title + "&" + "return=" + returnStr;
        Debug.print("param: " + param);
        //Debug.print("*************测试");
        return "/disk/filesystem_set?faces-redirect=true&amp;" + param;

//        String quotaValueStr = sys.getQuotaValue();
//        if (quotaValueStr != null && !quotaValueStr.equals("") && !quotaValueStr.equalsIgnoreCase("null") && !quotaValueStr.equalsIgnoreCase("none")) {
//            if (quotaValueStr.contains(".")) {
//                quotaValueStr = quotaValueStr.split("\\.")[0];
//            }
//        }
//        String param = "fileSystemName=" + sys.getName() + "&" + "isSetDedup=" + sys.isIsSetDedup() + "&" + "isVerify=" + sys.isIsVerify() + "&" + "isCompress=" + sys.isIsCompress() + "&" + "compress=" + sys.getCompress() + "&" + "isSetQuota=" + sys.isIsSetQuota() + "&" + "quotaValue=" + quotaValueStr + "&" + "recordsize=" + sys.getRecordsize() + "&" + "used=" + sys.getUsed();
//        return "nas_config_param?faces-redirect=true&amp;" + param;
    }

    public void onRowToggle(ToggleEvent event) {
        MSAResource res = new MSAResource();
        Share share = (Share) event.getData();
        NFSParametersForSharePath nfsParameters = InterfaceFactory.getNASInterfaceInstance().getSpecifiedNFSParameters(share.getPath());
        if (nfsParameters == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getNFSFailed"), res.get("getNFSFailed")));
            return;
        }
        ArrayList<NameValue> nfsProperties = new ArrayList<NameValue>();

        MSAGlobalResource global = new MSAGlobalResource();
        String anonoyVisit = "", anonoyRW = "", roList = "", rwList = "", rootList = "";
        if (nfsParameters.isAnonymousCanVisit()) {
            anonoyVisit = global.get("yes");
        } else {
            anonoyVisit = global.get("no");
        }
        if (nfsParameters.isAnonymousCanReadWrite()) {
            anonoyRW = global.get("yes");
        } else {
            anonoyRW = global.get("no");
        }
        String array[];
        if (!nfsParameters.getRwIpList().equals("")) {
            array = nfsParameters.getRwIpList().trim().split(":");
            if (array != null && array.length > 0) {
                for (int i = 0; i < array.length; i++) {
                    if (i + 1 < array.length) {
                        rwList = rwList + array[i] + ",";
                    } else {
                        rwList = rwList + array[i];
                    }
                }
            }
        }
        if (!nfsParameters.getRoIpList().equals("")) {
            array = nfsParameters.getRoIpList().trim().split(":");
            if (array != null && array.length > 0) {
                for (int i = 0; i < array.length; i++) {
                    if (i + 1 < array.length) {
                        roList = roList + array[i] + ",";
                    } else {
                        roList = roList + array[i];
                    }
                }
            }
        }
        if (!nfsParameters.getCanAsRootVisitIP().equals("")) {
            array = nfsParameters.getCanAsRootVisitIP().trim().split(":");
            if (array != null && array.length > 0) {
                for (int i = 0; i < array.length; i++) {
                    if (i + 1 < array.length) {
                        rootList = rootList + array[i] + ",";
                    } else {
                        rootList = rootList + array[i];
                    }
                }
            }
        }
        NameValue value;
        value = new NameValue(res.get("anonVisit"), anonoyVisit);
        nfsProperties.add(value);
        value = new NameValue(res.get("anonRW"), anonoyRW);
        nfsProperties.add(value);
        value = new NameValue(res.get("rwAuthor"), rwList);
        nfsProperties.add(value);
        value = new NameValue(res.get("roAuthor"), roList);
        nfsProperties.add(value);
        value = new NameValue(res.get("rootAuthor"), rootList);
        nfsProperties.add(value);
        share.setNfsProperties(nfsProperties);

        CIFSParametersForSharePath cifsParam = InterfaceFactory.getNASInterfaceInstance().getSpecifiedCIFSParameters(share.getPath());
        if (cifsParam == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getCIFSFailed"), res.get("getCIFSFailed")));
            return;
        }
        String shareName = "";
        if (cifsParam.isStart()) {
            shareName = cifsParam.getSharename();
        }
        ArrayList<NameValue> cifsProperties = new ArrayList<NameValue>();
        value = new NameValue(res.get("shareName"), shareName);
        cifsProperties.add(value);
        share.setCifsProperties(cifsProperties);
    }

    public boolean removeClone(Share share) {

        ArrayList<String> copys = new ArrayList<String>();
        ArrayList<String> manualSnaps = new ArrayList<String>();
        Map<String, String> cloneMap = InterfaceFactory.getSYNCInterfaceInstance().getCloneOriginMap();
        if (cloneMap == null) {
            MSAResource res = new MSAResource();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getCopysFailed"), res.get("getCopysFailed")));
            return false;
        }
        boolean flag;
        Iterator<String> copyIterator = cloneMap.keySet().iterator(); //获得所有副本
        while (copyIterator.hasNext()) {
            String copy = copyIterator.next();
            if (copy.startsWith(share.getPath())) {
                copys.add(copy);
            }
        }
//        for (Share path : this.list) {     //获取所有副本
//            if (path.getPath().startsWith(share.getPath() + "_snapshotCopy")) {
//                copys.add(path.getPath());
//            }
//        }
        for (String copy : copys) { //删除目录下的副本
            String snap = cloneMap.get(copy);
            flag = InterfaceFactory.getSYNCInterfaceInstance().deleteClone(copy);
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delCopyFailed"), res.get("delCopyFailed")));
                return false;
            }
            Debug.print("copy: " + copy);
            Debug.print("snap: " + snap);
            if (snap != null && !snap.contains("AUTO")) {
                manualSnaps.add(snap);
            }
        }
        if (!this.closeAutoSnap()) {
            return false;
        }
        for (String snap : manualSnaps) {
            flag = InterfaceFactory.getSYNCInterfaceInstance().destroySnapshot(snap);
            if (!flag) {
                MSAResource res = new MSAResource();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
                return false;
            }
        }

        //InterfaceFactory.getSYNCInterfaceInstance().
        return true;
    }

    public void remveCloneBeforeDeletePath() {

        if (!this.removeClone(this.selectedShareBean)) {
            return;
        }
        this.deletePath();
    }
}
