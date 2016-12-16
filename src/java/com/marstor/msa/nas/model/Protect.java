/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
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
public class Protect implements Serializable {

    private String path;
    private SnapShotListBean snapShotListBean;
    private SyncListBean syncListBean;
    private GlobalProtectStatus globalStatus;
    private String activeIndex = "";//页面需要显示的Tab编号，从0开始
    private String returnURL = "";
    private String module = "";
    private String title = "";
    private int moduleID;
    private String headText = "";
    private boolean createCopyRender;
    private String mess_1 = "";
    private String mess_2 = "";
    public static  final String copyFlag="_SC"; 

    public Protect() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        path = request.getParameter("path");
        if (path == null) {
            return;
        }
        activeIndex = request.getParameter("activeIndex");
        if (activeIndex == null || activeIndex.equals("")) {
            activeIndex = "0";
        }
        this.returnURL = request.getParameter("returnURL");
        module = request.getParameter("module");
        MSAResource res = new MSAResource();
        if (module != null) {
            if (module.equals("nas")) {
                this.title = res.get("nasTitle");
                this.moduleID = 1;
                this.headText = res.get("targetPath");
                this.createCopyRender = true;
            }
            if (module.equals("vtl")) {
                this.title = res.get("vtlTitle");
                this.moduleID = 2;
                this.headText = res.get("targetStorage");
                this.createCopyRender = false;
            }
        }

        snapShotListBean = new SnapShotListBean(path, module, returnURL);
        syncListBean = new SyncListBean(path, module, returnURL);
        globalStatus = new GlobalProtectStatus(path, snapShotListBean);
//        //页面载入时判断一次，如果自动快照开启了，则限制添加本地和源端同步按钮的状态
//        FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.path);
//        if (fileSys == null) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
//            return;
//        }
//        if (fileSys.getiIsOpen() != 1) {
//            globalStatus.setAddLocalSyncDisable(true);
//            globalStatus.setAddRemoteSyncDisable(true);
//        }else {
//            globalStatus.setAddLocalSyncDisable(false);
//            globalStatus.setAddRemoteSyncDisable(false);
//        }
        //globalStatus.update();//刷新按钮组件的状态（是否Disable）


    }

    public String getMess_1() {
        return mess_1;
    }

    public void setMess_1(String mess_1) {
        this.mess_1 = mess_1;
    }

    public String getMess_2() {
        return mess_2;
    }

    public void setMess_2(String mess_2) {
        this.mess_2 = mess_2;
    }

    public boolean isCreateCopyRender() {
        return createCopyRender;
    }

    public void setCreateCopyRender(boolean createCopyRender) {
        this.createCopyRender = createCopyRender;
    }

    public String getHeadText() {
        return headText;
    }

    public void setHeadText(String headText) {
        this.headText = headText;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(String activeIndex) {
        this.activeIndex = activeIndex;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SnapShotListBean getSnapShotListBean() {
        return snapShotListBean;
    }

    public void setSnapShotListBean(SnapShotListBean snapShotListBean) {
        this.snapShotListBean = snapShotListBean;
    }

    public SyncListBean getSyncListBean() {
        return syncListBean;
    }

    public void setSyncListBean(SyncListBean syncListBean) {
        this.syncListBean = syncListBean;
    }

    public GlobalProtectStatus getGlobalStatus() {
        return globalStatus;
    }

    public void setGlobalStatus(GlobalProtectStatus globalStatus) {
        this.globalStatus = globalStatus;
    }

    public void rollBackSnap() {
        this.snapShotListBean.rollBackSnap();
        globalStatus.update();
    }

    public void saveRoll() {
        this.snapShotListBean.saveRoll();
        globalStatus.update();
    }

    public void cancelRoll() {
        this.snapShotListBean.cancelRoll();
        globalStatus.update();
    }

    public void startSYNC() {
        this.syncListBean.startSYNC();
        globalStatus.update();
    }

    public void suspendSYNC() {
        this.syncListBean.suspendSYNC();
        globalStatus.update();
    }

    public void stopTargetSync() {
        this.syncListBean.stopTargetSync();
        globalStatus.update();
    }

    public void deleteSYNC() {
        this.syncListBean.deleteSYNC();
        globalStatus.update();
    }

    public void stopSourceSync() {
        this.syncListBean.stopSourceSync();
        globalStatus.update();
    }

    public void resumeSync() {
        this.syncListBean.resumeSync();
        globalStatus.update();
    }

    public void deleteSnap() {
        this.snapShotListBean.deleteSnap();
       // globalStatus.setSnapList(snapShotListBean); //修改snap对象后，刷新globalStatus中的snapList对象
//        Debug.print("snap size:" + snapShotListBean.getSnaps().size());
        globalStatus.updateDeleteAllSnapButton(snapShotListBean);
    }

    public void deleteAllSnap() {
        this.snapShotListBean.deleteAllSnaps();
      //  globalStatus.setSnapList(snapShotListBean);//修改snap对象后，刷新globalStatus中的snapList对象
//        Debug.print("snap size:" + snapShotListBean.getSnaps().size());
        globalStatus.updateDeleteAllSnapButton(snapShotListBean);
    }

    public void createCopy() {
        MSAResource res = new MSAResource();
        String copyName = this.snapShotListBean.createCopy();
        if (copyName == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createCopyFailed"), res.get("createCopyFailed")));
            return;
        }
        globalStatus.setContainsCopy(true);//创建副本后设置已包含副本
        globalStatus.update();
        MSAGlobalResource global = new MSAGlobalResource();
        //if (this.module.equals("nas")) {
        this.mess_1 = global.get("lquote") + copyName + global.get("rquote") + res.get("copyCreateSuccess") + global.get("comma") + res.get("isOrNotSkipToNAS");
        RequestContext.getCurrentInstance().execute("skipToNAS.show()");
        //} else if (this.module.equals("vtl")) {
        //    this.mess_2 = global.get("lquote") + copyName + global.get("rquote") + res.get("copyCreateSuccess") + global.get("comma") + res.get("isOrNotSkipToVtl");
        //    RequestContext.getCurrentInstance().execute("skipToVTL.show()");
        //}

        // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("createCopy") + global.get("lquote") + copyName + global.get("rquote") + global.get("success") + global.get("error_mark"), res.get("createCopy") + global.get("rquote") + copyName + global.get("lquote") + global.get("success") + global.get("error_mark")));
    }

    //检测是否是在回滚中
    public void checkRollbacking() {

        boolean isRollbacking = InterfaceFactory.getSYNCInterfaceInstance().isRollbacking(path);
        if (!isRollbacking) {
            MSAResource res = new MSAResource();
            globalStatus.setRollRender(false);
            globalStatus.setLoadingRender(true);
            RequestContext.getCurrentInstance().execute("rollbackpoll.stop();");
            this.snapShotListBean.doAfterRollBack();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("rollSuccess"), res.get("rollSuccess")));
        } else {
            globalStatus.setRollRender(true);
            globalStatus.setLoadingRender(false);
        }
    }

    public void updateSnapAndSync() {
        this.snapShotListBean.init();
        this.syncListBean.init();
        globalStatus.update();
    }

    public String goBack() {
        return this.returnURL + "?faces-redirect=true";
    }

    public void clickCreateCopy(Snapshot snap) {
        //  if (snap.isPermitCreateCopy()) {
        RequestContext.getCurrentInstance().execute("createCopy.show();");
//        } else {
//            RequestContext.getCurrentInstance().execute("finishCreateCopy.show();");
//        }
    }
    public void clickDelSnap(Snapshot snap) {
        MSAResource res = new MSAResource();
        if(snap.isExistCopy()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("forCreatedCopy"), res.get("forCreatedCopy")));
            return;
        }
        RequestContext.getCurrentInstance().execute("delSnap.show();");
    }
}
