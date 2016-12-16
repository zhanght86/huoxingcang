/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.bean.SharePath;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import com.marstor.msa.nas.model.Constant;
import com.marstor.msa.nas.model.ACL;
import com.marstor.msa.sync.bean.StatusInfo;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

public class Share implements Serializable {

    private String path = "";
    private String volumeName = "";
    private String shareName = "";
    private String cifsStatus = "";
    private String folder = "";
    private boolean editable;
    private ArrayList<ACL> acls = new ArrayList<ACL>();
    private ArrayList<NameValue> nfsProperties = new ArrayList<NameValue>();
    private ArrayList<NameValue> cifsProperties = new ArrayList<NameValue>();
//    private ACL selectedACL = new ACL();
//    private ACL addACL = new ACL();
//    private ACL editACL = new ACL();
    private NFS nfs = new NFS();
    private CIFS cifs = new CIFS();
    private boolean cifsOnOrOff;
    private HtmlInputText shareText;
    private boolean shareNameTextStatus;
    private boolean status;
    private boolean configButtonStatus;
    private boolean nfsOnOrOff;
    //private  boolean cifsStatus;
    private String onOrOff = "";
    private String nfsState = "";
    // private String  cifsOnOrOff="" ;
    private String used = "";
    private String available = "";
    private String mountPoint = "";
    private String type = "";
    private String setStatus;
    private String errorInfo;
    private boolean mountedOrNot;
    private boolean containsCopy, copyOrNot; //是否含有副本
    private boolean buttonDisable, onLineDisable, deleteDisable, offLineDisable, protectDisable, cifsDisable, nfsDisable;
    private FileSystem fileSysInfo;

    public Share() {
//        initBlockSize();
    }

    public Share(String path) {
//        this.path = path;
//        this.mountPoint = "/" + this.path;
//        this.used = "40.0KB";
//        this.available = "1.52TB";
//        this.status = true;
//        this.iconName="offline";
//        
//        initBlockSize();
//
//        ACL acl = new ACL(0, Constant.getOneStyleName(Constant.sys), "所有者");
//
//        acl.setReadAuthor();
//        acl.setCreateAuthor();
//        acl.setExcuteAuthor();
//        acl.setDeleteAuthor();
//        acl.setWriteAuthor();
//        acl.setInheritAuthor();
//        acl.updateGeneralAuthors();
//        acl.updateAllEditAuthors();
//        acl.setDelStr("-");
//        acl.setDelImageRendered(false);
//        acls.add(acl);
//
//        acl = new ACL(1, Constant.getOneStyleName(Constant.sys), "组");
//
//        acl.setReadAuthor();
//        acl.setExcuteAuthor();
//        acl.setInheritAuthor();
//        acl.updateGeneralAuthors();
//        acl.updateAllEditAuthors();
//        acl.setDelStr("-");
//        acl.setDelImageRendered(false);
//        acls.add(acl);
//
//        acl = new ACL(2, Constant.getOneStyleName(Constant.sys), "全部用户");
//
//        acl.setReadAuthor();
//        acl.setExcuteAuthor();
//        acl.setInheritAuthor();
//        acl.updateGeneralAuthors();
//        acl.updateAllEditAuthors();
//        acl.setDelStr("-");
//        acl.setDelImageRendered(false);
//        acls.add(acl);
//
//        acl = new ACL(3, Constant.getOneStyleName(Constant.user), "test");
//
//        acl.setReadAuthor();
//        acl.updateGeneralAuthors();
//        acl.updateAllEditAuthors();
//        acls.add(acl);
//
//        cifs = new CIFS();
    }
//创建目录

    public Share(String pathName, String cifsShareName, String avail, String used, String mountPoint, boolean cifs, boolean nfs, boolean mountedOrNot, int caseSensitivity, FileSystem fileSysInfo, Map<String, StatusInfo> allStatusInfo, Map<String, String> cloneMap) {
        this.path = pathName;
        if (pathName != null && pathName.contains("/")) {
            this.volumeName = pathName.substring(0, pathName.indexOf("/"));
        }
        MSAResource res = new MSAResource();
        //根据文件系统的属性caseSensitivity设置CIFS和NFS按钮的状态
        if (caseSensitivity == SharePath.sensitive) {
            //this.type = res.get("applyNFS");
            this.type = res.get("nas.nas_path","applyNFS");
            this.nfsDisable = false;
            this.cifsDisable = true;
        } else if (caseSensitivity == SharePath.insensitive) {
            //this.type = res.get("applyCIFS");
            this.type = res.get("nas.nas_path", "applyCIFS");
            this.nfsDisable = true;
            this.cifsDisable = false;
        } else if (caseSensitivity == SharePath.mixed) {
            //this.type = res.get("applyNFSOrCIFS");
            this.type = res.get("nas.nas_path", "applyNFSOrCIFS");
            this.nfsDisable = false;
            this.cifsDisable = false;
        }
        this.shareName = cifsShareName;
        this.available = avail + "B";
        this.used = used + "B";
        this.mountPoint = mountPoint;
        this.cifsOnOrOff = cifs;
        this.nfsOnOrOff = nfs;
        this.mountedOrNot = mountedOrNot;
        this.fileSysInfo = fileSysInfo;
        //String array[] = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(pathName);
        StatusInfo status = allStatusInfo.get(pathName);
        if (status != null) {
            if (status.getIsDescSync() == 1) { //如果是同步目标，则不可以上线,不可以删除
                this.onLineDisable = true;
                this.deleteDisable = true;
            } else {
                this.onLineDisable = false;
                this.deleteDisable = false;
            }
        }

//        if (array != null && array.length > 4) {
//            if (array[4].equals("1")) {  //如果是同步目标，则不可以上线,不可以删除
//                this.onLineDisable = true;
//                this.deleteDisable = true;
//            } else {
//                this.onLineDisable = false;
//                this.deleteDisable = false;
//            }
//        }

        if (!this.mountedOrNot) {
            this.buttonDisable = true;
        } else {
            this.buttonDisable = false;
        }
        if (this.path != null) {
            Iterator<String> copys = cloneMap.keySet().iterator();
            this.containsCopy = false;
            while (copys.hasNext()) {
//                "_snapshotCopy"
                if (copys.next().contains(this.path + "_")) {
                    this.containsCopy = true;
                    break;
                }
            }
//            if (cloneMap.containsKey(this.path + "_snapshotCopy")) {
//                this.containsCopy = true;
//            } else {
//                this.containsCopy = false;
//            }
            if (cloneMap.containsKey(this.path)) {
                this.copyOrNot = true;
                this.protectDisable = true;
            } else {
                this.copyOrNot = false;
                this.protectDisable = false;
            }

        }
//        if (this.path.contains("_snapshotCopy_")) {
//                    this.protectDisable = true;
//                } else {
//                    this.protectDisable = false;
//                }

    }

    public boolean isCifsDisable() {
        return cifsDisable;
    }

    public void setCifsDisable(boolean cifsDisable) {
        this.cifsDisable = cifsDisable;
    }

    public boolean isNfsDisable() {
        return nfsDisable;
    }

    public void setNfsDisable(boolean nfsDisable) {
        this.nfsDisable = nfsDisable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public boolean isCopyOrNot() {
        return copyOrNot;
    }

    public void setCopyOrNot(boolean copyOrNot) {
        this.copyOrNot = copyOrNot;
    }

    public boolean isContainsCopy() {
        return containsCopy;
    }

    public void setContainsCopy(boolean containsCopy) {
        this.containsCopy = containsCopy;
    }

    public boolean isProtectDisable() {
        return protectDisable;
    }

    public void setProtectDisable(boolean protectDisable) {
        this.protectDisable = protectDisable;
    }

    public ArrayList<NameValue> getNfsProperties() {
        return nfsProperties;
    }

    public void setNfsProperties(ArrayList<NameValue> nfsProperties) {
        this.nfsProperties = nfsProperties;
    }

    public ArrayList<NameValue> getCifsProperties() {
        return cifsProperties;
    }

    public void setCifsProperties(ArrayList<NameValue> cifsProperties) {
        this.cifsProperties = cifsProperties;
    }

    public FileSystem getFileSysInfo() {
        return fileSysInfo;
    }

    public void setFileSysInfo(FileSystem fileSysInfo) {
        this.fileSysInfo = fileSysInfo;
    }

    public boolean isOffLineDisable() {
        return offLineDisable;
    }

    public void setOffLineDisable(boolean offLineDisable) {
        this.offLineDisable = offLineDisable;
    }

    public boolean isDeleteDisable() {
        return deleteDisable;
    }

    public void setDeleteDisable(boolean deleteDisable) {
        this.deleteDisable = deleteDisable;
    }

    public boolean isOnLineDisable() {
        return onLineDisable;
    }

    public void setOnLineDisable(boolean onLineDisable) {
        this.onLineDisable = onLineDisable;
    }

    public String getNfsState() {
        return nfsState;
    }

    public void setNfsState(String nfsState) {
        this.nfsState = nfsState;
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    public boolean isMountedOrNot() {
        return mountedOrNot;
    }

    public void setMountedOrNot(boolean mountedOrNot) {
        this.mountedOrNot = mountedOrNot;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public boolean isButtonEnable() {
        return buttonDisable;
    }

    public void setButtonDisable(boolean buttonDisable) {
        this.buttonDisable = buttonDisable;
    }

//
//    public String getIconName() {
//        return iconName;
//    }
//
//    public void setIconName(String iconName) {
//        this.iconName = iconName;
//    }
    public CIFS getCifs() {
        return cifs;
    }

    public void setCifs(CIFS cifs) {
        this.cifs = cifs;
    }

    public NFS getNfs() {
        return nfs;
    }

    public void setNfs(NFS nfs) {
        this.nfs = nfs;
    }

//    public boolean isAuthorityButtonStatus() {
//        return authorityButtonStatus;
//    }
//
//    public void setAuthorityButtonStatus(boolean authorityButtonStatus) {
//        this.authorityButtonStatus = authorityButtonStatus;
//    }
    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

//    public ArrayList<String> getBlockSize() {
//        return blockSize;
//    }
//
//    public void setBlockSize(ArrayList<String> blockSize) {
//        this.blockSize = blockSize;
//    }
//
//    public boolean isLevelSpinnerStatus() {
//        return levelSpinnerStatus;
//    }
//
//    public void setLevelSpinnerStatus(boolean levelSpinnerStatus) {
//        this.levelSpinnerStatus = levelSpinnerStatus;
//    }
//
//    public String getMaxSpace() {
//        return maxSpace;
//    }
//
//    public void setMaxSpace(String maxSpace) {
//        this.maxSpace = maxSpace;
//    }
//
//    public boolean isMaxSpaceStatus() {
//        return maxSpaceStatus;
//    }
//
//    public void setMaxSpaceStatus(boolean maxSpaceStatus) {
//        this.maxSpaceStatus = maxSpaceStatus;
//    }
//
//    public HtmlInputText getMaxSpaceText() {
//        return maxSpaceText;
//    }
//
//    public void setMaxSpaceText(HtmlInputText maxSpaceText) {
//        this.maxSpaceText = maxSpaceText;
//    }
    public String getMountpoint() {
        return mountPoint;
    }

    public void setMountpoint(String mountpoint) {
        this.mountPoint = mountpoint;
    }

    public String getNfsOnOrOff() {
        return nfsState;
    }

    public void setNfsOnOrOff(String nfsOnOrOff) {
        this.nfsState = nfsOnOrOff;
    }

    public boolean isNfsOnOrOff() {
        return nfsOnOrOff;
    }

    public void setNfsOnOrOff(boolean nfsOnOrOff) {
        this.nfsOnOrOff = nfsOnOrOff;
    }

    public String getOnOrOff() {
        if (this.status) {
            this.onOrOff = Constant.on;
        } else {
            this.onOrOff = Constant.off;
        }
        return onOrOff;
    }

    public void setOnOrOff(String onOrOff) {
        this.onOrOff = onOrOff;
    }

//    public String getSelectedBlockSize() {
//        return selectedBlockSize;
//    }
//
//    public void setSelectedBlockSize(String selectedBlockSize) {
//        this.selectedBlockSize = selectedBlockSize;
//    }
//
//    public int getSelectedLevel() {
//        return selectedLevel;
//    }
//
//    public void setSelectedLevel(int selectedLevel) {
//        this.selectedLevel = selectedLevel;
//    }
    public String getSetStatus() {
        if (this.status) {
            this.setStatus = Constant.offLine;
        } else {
            this.setStatus = Constant.onLine;
        }
        return setStatus;
    }

    public void setSetStatus(String setStatus) {
        this.setStatus = setStatus;
    }

//    public boolean isStartCheck() {
//        return startCheck;
//    }
//
//    public void setStartCheck(boolean startCheck) {
//        this.startCheck = startCheck;
//    }
//
//    public boolean isStartCompress() {
//        return startCompress;
//    }
//
//    public void setStartCompress(boolean startCompress) {
//        this.startCompress = startCompress;
//    }
//
//    public boolean isStartDataCheckBoxStatus() {
//        return startDataCheckBoxStatus;
//    }
//
//    public void setStartDataCheckBoxStatus(boolean startDataCheckBoxStatus) {
//        this.startDataCheckBoxStatus = startDataCheckBoxStatus;
//    }
//
//    public boolean isStartDeduplicate() {
//        return startDeduplicate;
//    }
//
//    public void setStartDeduplicate(boolean startDeduplicate) {
//        this.startDeduplicate = startDeduplicate;
//    }
//
//    public boolean isStartQuota() {
//        return startQuota;
//    }
//
//    public void setStartQuota(boolean startQuota) {
//        this.startQuota = startQuota;
//    }
    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public boolean isConfigButtonStatus() {

        return configButtonStatus;
    }

    public void setConfigButtonStatus(boolean configButtonStatus) {
        this.configButtonStatus = configButtonStatus;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isShareNameTextStatus() {
        return shareNameTextStatus;
    }

    public void setShareNameTextStatus(boolean shareNameTextStatus) {
        this.shareNameTextStatus = shareNameTextStatus;
    }

    public HtmlInputText getShareText() {
        return shareText;
    }

    public void setShareText(HtmlInputText shareText) {
        this.shareText = shareText;
    }

    public boolean isCifsOnOrOff() {
        return cifsOnOrOff;
    }

    public void setCifsOnOrOff(boolean cifsOnOrOff) {
        this.cifsOnOrOff = cifsOnOrOff;
    }

    public ArrayList<ACL> getAcls() {
        return acls;
    }

    public void setAcls(ArrayList<ACL> acls) {
        this.acls = acls;
    }

    public String getCifsStatus() {
        if (this.cifsOnOrOff) {
            cifsStatus = Constant.open;
        } else {
            cifsStatus = Constant.notOpen;
        }
        return cifsStatus;
    }

    public void setCifsStatus(String cifsStatus) {
        this.cifsStatus = cifsStatus;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getPath() {
        return path;
    }

    public String getShareName() {
        return shareName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public ACL getACLByID(int index) {
        for (int i = 0; i < acls.size(); i++) {
            if (acls.get(i).getIndex() == index) {
                return acls.get(i);
            }
        }
        return null;
    }

    public ACL getACLByNameAndStyle(String name, String style) {

        for (int i = 0; i < acls.size(); i++) {
            if (acls.get(i).getObjectName().equals(name) && acls.get(i).getObjectStyle().equals(style)) {
                return acls.get(i);
            }
        }
        return null;
    }

    public void deleteACL(ACL acl) {
        this.acls.remove(acl);

    }

    public void initAddACL() {
        // addACL = new ACL();
        this.errorInfo = "";
//        UserGroupData data = MySession.getGroupsFromSession();
//        data.setSelectedType("");
//        data.setSelectedName("");
//        data.setSelectedNames(data.getUserModel().getUserNames());
//        if(acls.size()>0) {
//            this.selectedACL =acls.get(0);
//        }

    }

    public void initEditACL() {

        // UserGroupData data = MySession.getGroupsFromSession();
        this.errorInfo = "";
        FacesContext context = FacesContext.getCurrentInstance();
        String index = context.getExternalContext().getRequestParameterMap().get("index");
        ACL acl = this.getACLByID(Integer.parseInt(index));

//        editACL = new ACL();
//       // ACL acl = MySession.getShareFromSession().getEditBean().getSelectedACL();
//        editACL.setObjectName(acl.getObjectName());
//        editACL.setObjectStyle(acl.getObjectStyle());
//        editACL.setAuthMap(acl.getAuthMap());
//        editACL.updateGeneralAuthors();
//        editACL.updateAllEditAuthors();

    }

    //   public String addACLToList() {
//        // addACL.updateAuthorityFromAllAuthors(addACL.getAuthorsForAdd());
//        UserGroupData data = MySession.getGroupsFromSession();
//        // addACL.setObjectName(data.getSelectedName());
//        //this.objectName = data.getSelectedName();
//        // addACL.setObjectStyle(data.getSelectedType());
//        if (checkSpecifiedACLExit(data.getSelectedType(), data.getSelectedName())) {
//            //RequestContext.getCurrentInstance().addCallbackParam("result", "failed");
//            this.errorInfo = Constant.aclExist;
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"错误信息", Constant.aclExist));
//            return "";
//        }
//        if (!addACL.getAuthorsForAdd().isConfigAuthors()) {
//            //RequestContext.getCurrentInstance().addCallbackParam("result", "failed");
//            this.errorInfo = Constant.configACL;
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"错误信息", Constant.configACL));
//            return "";
//        }
//        ShareListBean share = MySession.getShareFromSession();
//        Share sharePath = share.getShareByPath(share.getEditBean().getPath());
//        ACL acl = new ACL();
//        acl.setObjectName(data.getSelectedName());
//        acl.setObjectStyle(data.getSelectedType());
//        acl.updateAuthorityFromAllAuthors(addACL.getAuthorsForAdd());
//        acl.setIndex(sharePath.getAcls().size() + 1);
//
//        sharePath.getAcls().add(acl);
//        this.getAcls().add(acl);
//        if (this.getAcls().size() > 0) {
//            this.selectedACL = sharePath.getAcls().get(0);
//            //path.selectedACL = this.getAcls().get(0);
//        }
//        return  "nas_authority?faces-redirect=true";
//        // share.getEditBean().getAcls().add(this);
//
//       // RequestContext.getCurrentInstance().addCallbackParam("result", Constant.configSuccess);
//    }
//
//    public String editSpecifiedACL() {
//        if (!this.editACL.getAuthorsForEdit().isConfigAuthors()) {
//            RequestContext.getCurrentInstance().addCallbackParam("result", Constant.configACL);
//            this.errorInfo = Constant.configACL;
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "错误信息", Constant.configACL));
//            return "";
//        }
//        ShareListBean share = MySession.getShareFromSession();
//        Share sharePath = share.getShareByPath(share.getEditBean().getPath());
//        //ACL selectedAcl = sharePath.getSelectedACL();
//        ACL acl = sharePath.getACLByNameAndStyle(this.editACL.getObjectName(), this.editACL.getObjectStyle());
//        //acl.setObjectName(sharePath.getEditACL().getObjectName());
//        // acl.setObjectStyle(data.getSelectedType());
//        acl.updateAuthorityFromAllAuthors(this.editACL.getAuthorsForEdit());
//        if (this.getAcls().size() > 0) {
//            this.selectedACL = sharePath.getAcls().get(0);
//            //path.selectedACL = this.getAcls().get(0);
//        }
//        //RequestContext.getCurrentInstance().addCallbackParam("result", Constant.configSuccess);
//        return  "nas_authority?faces-redirect=true";
//    }
    public boolean checkSpecifiedACLExit(String style, String name) {

        for (int i = 0; i < this.acls.size(); i++) {
            if (acls.get(i).getObjectName().equals(name) && acls.get(i).getObjectStyle().equals(style)) {
                return true; //返回存在
            }
        }
        return false;
    }

    public void change() {
        if (this.cifsOnOrOff) {
            //this.shareNameEdit = "true";
            // this.shareText.setReadonly(false);
            //this.shareText.setDisabled(false);
            //this.shareText.setValue(this.shareName);
            this.setShareNameTextStatus(false);
        }
        if (!this.cifsOnOrOff) {
            // this.shareNameEdit = "false";
            //this.shareText.setReadonly(true);
//            this.shareText.setDisabled(true);
//            this.shareText.setValue("");
            this.setShareNameTextStatus(true);
        }
    }

    public void disableShareText() {
        if (this.shareText != null) {
            this.shareText.setDisabled(true);
            this.shareText.setValue("");
        }
    }

    public void enbleShareText() {
        if (this.shareText != null) {
            this.shareText.setDisabled(false);
            // this.shareText.setValue("");
            this.shareText.setValue(this.shareName);
        }

    }

    public void updateShareText() {
        if (this.shareText != null) {
            this.shareText.setValue(this.shareName);
            //this.shareText.setValue(this.shareName);
        }
    }
//
//    public void initBlockSize() {
//        blockSize.add("4K");
//        blockSize.add("8K");
//        blockSize.add("16K");
//        blockSize.add("32K");
//        blockSize.add("64K");
//        blockSize.add("128K");
//    }
//
//    public void changDuplicate() {
//        if (this.startDeduplicate) {
//            // this.checkData.setDisabled(false);
//            this.startDataCheckBoxStatus = false;
//        } else {
//            //this.checkData.setDisabled(true);
//            this.startDataCheckBoxStatus = true;
//        }
//    }
//
//    public void changeCompress() {
//        if (this.startCompress) {
//            this.levelSpinnerStatus = false;
//            //this.level.setDisabled(false);
//        } else {
//            //this.level.setDisabled(true);
//            this.levelSpinnerStatus = true;
//        }
//    }
//
//    public void changeQuota() {
//        if (this.startQuota) {
//            //this.maxSpaceText.setDisabled(false);
//            this.maxSpaceStatus = false;
//        } else {
//            this.maxSpaceStatus = true;
//            //this.maxSpaceText.setDisabled(true);
//        }
//    }
//
//    public void disableMaxSpaceText() {
//        if (this.maxSpaceText != null) {
//            this.maxSpaceText.setDisabled(true);
//        }
//    }
//
//    public void enableMaxSpaceText() {
//        if (this.maxSpaceText != null) {
//            this.maxSpaceText.setDisabled(false);
//        }
//    }

    public void cifsListen() {
        FacesContext context = FacesContext.getCurrentInstance();
        String pathName = context.getExternalContext().getRequestParameterMap().get("path");
        //SharePathBean share = this.getShareByPath(pathName);
        if (this.getCifs().isCifsOnOrOff()) {
            //this.getCifs().setConfigButtonStatus(true);
            RequestContext.getCurrentInstance().addCallbackParam("status", "on");
        } else {
            this.getCifs().setConfigButtonStatus(false);
            RequestContext.getCurrentInstance().addCallbackParam("status", "off");
        }
    }

    public void doBeforeDeleteACL() {
        FacesContext context = FacesContext.getCurrentInstance();
        String index = context.getExternalContext().getRequestParameterMap().get("index");
//        this.editACL = new ACL();
//        this.editACL = this.getACLByID(Integer.parseInt(index));

    }
}
