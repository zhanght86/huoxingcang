/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.GlobalProtectStatus;
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
@ManagedBean(name = "protect_vtl")
@ViewScoped
public class Protect_vtl implements Serializable{
    private String path;
    private SnapShotList_vtlBean snap;
    private SyncList_vtlBean sync;
    private GlobalProtectStatus globalStatus;
    private String activeIndex = "";//页面需要显示的Tab编号，从0开始
    private String returnURL = "";
    private String module = "";
    private String title = "";
    /**
     * Creates a new instance of Protect_vtl
     */
    public Protect_vtl() {
        
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
            if (module.equals("vtl")) {
                this.title = res.get("vtlTitle");
            }
        }

        snap = new SnapShotList_vtlBean(path,module,returnURL);
        sync = new SyncList_vtlBean(path,module,returnURL);
        globalStatus = new GlobalProtectStatus(path,snap);
        globalStatus.update();
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

    public SnapShotList_vtlBean getSnap() {
        return snap;
    }

    public void setSnap(SnapShotList_vtlBean snap) {
        this.snap = snap;
    }

    public SyncList_vtlBean getSync() {
        return sync;
    }

    public void setSync(SyncList_vtlBean sync) {
        this.sync = sync;
    }

    public GlobalProtectStatus getGlobalStatus() {
        return globalStatus;
    }

    public void setGlobalStatus(GlobalProtectStatus globalStatus) {
        this.globalStatus = globalStatus;
    }

    public void rollBackSnap() {
        this.snap.rollBackSnap();
        globalStatus.update();
    }

    public void saveRoll() {
        this.snap.saveRoll();
        globalStatus.update();
    }

    public void cancelRoll() {
        this.snap.cancelRoll();
        globalStatus.update();
    }

    public void startSYNC() {
        this.sync.startSYNC();
        globalStatus.update();
    }

    public void suspendSYNC() {
        this.sync.suspendSYNC();
        globalStatus.update();
    }

    public void stopTargetSync() {
        this.sync.stopTargetSync();
        globalStatus.update();
    }

    public void deleteSYNC() {
        this.sync.deleteSYNC();
        globalStatus.update();
    }

    public void stopSourceSync() {
        this.sync.stopSourceSync();
        globalStatus.update();
    }

    public void resumeSync() {
        this.sync.resumeSync();
        globalStatus.update();
    }

    public void deleteSnap() {
        this.snap.deleteSnap();
        globalStatus.update();
    }
    //检测是否是在回滚中

    public void checkRollbacking() {

        boolean isRollbacking = InterfaceFactory.getSYNCInterfaceInstance().isRollbacking(path);
        if (!isRollbacking) {
            MSAResource res = new MSAResource();
            globalStatus.setRollRender(false);
            globalStatus.setLoadingRender(true);
            RequestContext.getCurrentInstance().execute("rollbackpoll.stop();");
            this.snap.doAfterRollBack();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("rollSuccess") , res.get("rollSuccess")));
        } else {
            globalStatus.setRollRender(true);
            globalStatus.setLoadingRender(false);
        }
    }

    public void updateSnapAndSync() {
        this.snap.init();
        this.sync.init();
        globalStatus.update();
    }

    public String goBack() {
        return this.returnURL+"?faces-redirect=true";
    }
}
