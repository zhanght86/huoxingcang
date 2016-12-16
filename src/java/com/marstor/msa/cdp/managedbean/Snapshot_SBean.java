/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.javadb.SyncDBUtil;
import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.model.GlobalProtectStatus;
import com.marstor.msa.cdp.model.LazySnapModel_cdp;
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.nas.model.SyncStatus;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "snapshot_SBean")
@ViewScoped
public class Snapshot_SBean implements Serializable {

    private String guid;
    private String path;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();
    private MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
    private String basename = "cdp.snapshot";
    private int level;
    private GlobalProtectStatus globalStatus;
    private Snapshot selectSnap = new Snapshot();
    private CdpDiskGroupInfo group = null;
    private String dg;
    private List<String> groupNames;
    private LazyDataModel<Snapshot> snapModel;

    public Snapshot_SBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        guid = request.getParameter("guid");
        path = request.getParameter("path");
        dg = request.getParameter("dg");
        if (request.getParameter("level") != null) {
            level = Integer.parseInt(request.getParameter("level"));
        }
        this.groupNames = Arrays.asList(request.getParameterValues("groupName"));
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        this.snapModel = new LazySnapModel_cdp(path);
    }

    public void cancelRollback() {
        boolean flag = cdp.cancelRollback(this.group.getDiskGroupPath());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("cancelRollFailed"), res.get("cancelRollFailed")));
            return;
        }
        this.init();
        globalStatus.update();
    }

    public void saveRollback() {
        boolean flag = cdp.saveRollback(this.group.getDiskGroupPath());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("saveRollFailed"), res.get("saveRollFailed")));
            return;
        }
        this.init();
        globalStatus.update();
    }

    public void updateSnapAndSync() {
        init();
        globalStatus.update();
    }

    public void init() {
        initGroup();
        this.snapModel = new LazySnapModel_cdp(path);
        List<CdpDiskGroupInfo> groups = cdp.getDiskGroupInfos();
        List<String> names = new ArrayList();
        for (CdpDiskGroupInfo g : groups) {
            names.add(g.getDiskGroupName());
        }
        this.groupNames = names;
        globalStatus = new GlobalProtectStatus(path, level, group, dg, groupNames);
    }

    public String getDg() {
        initGroup();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        globalStatus = new GlobalProtectStatus(path, level, group, dg, groupNames);
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public String doBeforeAddMSnap() {
        String param = "path=" + this.path + "&guid=" + this.guid + "&dg=" + this.dg;
        for (String name : this.groupNames) {
            param += "&groupName=" + name;
        }
        return "add_msnap?faces-redirect=true&amp;" + param;
    }

    public void beforeRollback() {
        CdpDiskInfo[] disks = cdp.getDiskInfos(group.getDiskGroupGuid(), group.getDiskGroupPath());
        ViewInformation[] lun = null;
        if (disks != null && disks.length > 0) {
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            if (disks[0].getLuInfoBean() != null) {
                lun = scsi.getLUNView(disks[0].getLuInfoBean().getlUName());
            }
        }
        if (this.group.getiAutoSnapshotIsOpen() == 1) {
            if (lun != null && lun.length > 0) {
                RequestContext.getCurrentInstance().execute("SdisLUNDelXX.show()");
            } else {
                RequestContext.getCurrentInstance().execute("SdeleteDGBoxXX.show()");
            }
        } else {
            if (lun != null && lun.length > 0) {
                RequestContext.getCurrentInstance().execute("disLUNDelXX.show()");
            } else {
                RequestContext.getCurrentInstance().execute("deleteDGBoxXX.show()");
            }
        }
    }

    public String toDGs() {
        return "dgs?faces-redirect=true";
    }

    public void rollBackSnap() {
        boolean flag;
        CdpDiskInfo[] disks = cdp.getDiskInfos(guid, "/" + path);
        if (disks == null) {
            return;
        }
        if (this.group.getiAutoSnapshotIsOpen() == 1) {
            cdp.stopCDP(this.group.getDiskGroupGuid(), this.group.getDiskGroupPath(),
                    this.group.getProtectType());
        }

        int len = disks.length;
        String[] lus;
        lus = new String[len];
        for (int i = 0; i < len; i++) {
            lus[i] = disks[i].getLuInfoBean().getlUName();
        }

        cdp.offlineLU(this.group.getDiskGroupName(), lus);

        flag = InterfaceFactory.getSYNCInterfaceInstance().rollbackSnapshot(this.path, "", this.selectSnap.getStrName(), null);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("rollBackFailed"), res.get("rollBackFailed")));
            return;
        }
        cdp.onlineLU(this.group.getDiskGroupName(), lus);
        this.init();
        globalStatus.update();
    }

    public void updateAfterRoll() {
        globalStatus.update();
    }

    public List<String> getAfterRollSnaps() {
        List<String> afterSnaps = InterfaceFactory.getSYNCInterfaceInstance().getSnapshotListAfterRollback(path);
        if (afterSnaps == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getRolledSnapFailed"), res.get("getRolledSnapFailed")));
            return null;
        }
        return afterSnaps;
    }

    public String[] getLocalFileSystemStatus(String fileSystem) {
        String[] status = InterfaceFactory.getSYNCInterfaceInstance().getLocalFileSystemStatus(fileSystem);
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
            return null;
        }
        //文件系统状态 int[0] 文件系统是否存在快照 
        //int[1] 文件系统自动快照是否开启 
        //int[2] 文件系统是不是同步源文件系统 
        //int[3] 文件系统是不是同步目标文件系统 
        //int[4] 文件系统是否上线 
        //int[5] 文件系统是否暂停同步
        //int[6] 文件系统是否回滚

        return status;
    }

    public void saveRoll() {
        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().saveCurrentStatus(path);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("saveRollFailed"), res.get("saveRollFailed")));
            return;
        }
        this.init();
        globalStatus.update();
    }

    public void cancelRoll() {
        CdpDiskInfo[] disks = cdp.getDiskInfos(group.getDiskGroupGuid(), group.getDiskGroupPath());
        int len = disks.length;
        String[] lus;
        lus = new String[len];
        for (int i = 0; i < len; i++) {
            lus[i] = disks[i].getLuInfoBean().getlUName();
        }
        cdp.offlineLU(this.group.getDiskGroupName(), lus);

        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().cancelRollback(path);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("cancelRollFailed"), res.get("cancelRollFailed")));
            return;
        }

        cdp.onlineLU(this.group.getDiskGroupName(), lus);

        this.init();
        globalStatus.update();
    }

    public void beforeDeleteMSnap() {
        Collection<String> origins = sync.getCloneOriginMap().values();
        for (String origin : origins) {
            if (origin.equals(this.selectSnap.getStrName())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapsError"), res.get("delSnapsError")));
                return;
            }
        }
        RequestContext.getCurrentInstance().execute("delMSnap.show();");
    }

    public void deleteMSnap() {

        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().destroySnapshot(this.selectSnap.getStrName());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
            return;
        }
        this.updateAfterDelete();
    }

    public void beforeDeleteAllMSnap() {
        Collection<String> origins = sync.getCloneOriginMap().values();

        for (Snapshot ss : this.snapModel) {
            for (String origin : origins) {
                if (origin.equals(ss.getStrName())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delAllSnapsError"), res.get("delAllSnapsError")));
                    return;
                }
            }
        }
        RequestContext.getCurrentInstance().execute("delAllMSnap.show();");
    }

    public void deleteAllMSnap() {

        boolean flag = InterfaceFactory.getCDPInterfaceInstance().deleteAllSnapshot(this.group.getDiskGroupPath());
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
            return;
        }
        this.updateAfterDelete();
    }

    public void updateAfterDelete() {
        init();
        globalStatus.update();
    }
    private String copyName;

    public void createCopy() {
        String str = InterfaceFactory.getSYNCInterfaceInstance().createClone(this.selectSnap.getStrName());
        if (str == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createCopyFailed"), res.get("createCopyFailed")));
        }

        copyName = str;
        if (copyName == null) {
            return;
        }
        if (group == null) {
            this.initGroup();
        }
        cdp.setProtectType(copyName, group.getProtectType());
        globalStatus.setContainsCopy(true);//创建副本后设置已包含副本

        this.init();
        globalStatus.update();
        RequestContext.getCurrentInstance().execute("dgs.show()");
    }

    public String getCopyName() {
        return copyName;
    }

    public void setCopyName(String copyName) {
        this.copyName = copyName;
    }

    //检测是否是在回滚中
    public void checkRollbacking() {
        boolean isRollbacking = InterfaceFactory.getSYNCInterfaceInstance().isRollbacking(path);
        if (!isRollbacking) {
            globalStatus.setRollRender(false);
            globalStatus.setLoadingRender(true);
            RequestContext.getCurrentInstance().execute("rollbackpoll.stop();");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("rollSuccess"), res.get("rollSuccess")));
        } else {
            globalStatus.setRollRender(true);
            globalStatus.setLoadingRender(false);
        }
    }

    public void change() {
    }

    public String cancelQ() {
        String param = "guid=" + guid + "&path=" + path + "&level=" + level;
        for (String name : this.groupNames) {
            param += "&groupName=" + name;
        }
        return "snapshot_s?faces-redirect=true&amp;" + param;
    }

    public GlobalProtectStatus getGlobalStatus() {
        return globalStatus;
    }

    public final void setGlobalStatus(GlobalProtectStatus globalStatus) {
        this.globalStatus = globalStatus;
    }
    private boolean deleteAllDis = false;

    public boolean getDeleteAllDis() {
        SyncDBUtil su = new SyncDBUtil();
        int count = su.querySnapshotCount(path);
        if (count > 0) {
            deleteAllDis = false;
        } else {
            deleteAllDis = true;
        }
        // Debug.print(this.snapModel.iterator() + "------------------------------RowCount()");
        //Debug.print(deleteAllDis + "------------------------------deleteAllDis");
        return deleteAllDis;
    }

    public Snapshot getSelectSnap() {
        return selectSnap;
    }

    public void setSelectSnap(Snapshot selectSnap) {
        this.selectSnap = selectSnap;
    }

    private void initGroup() {
        if (guid != null) {
            group = cdp.getDiskGroupInfo(guid);
        } else {
            List<CdpDiskGroupInfo> groups = cdp.getDiskGroupInfos();
            for (CdpDiskGroupInfo g : groups) {
                if (g.getDiskGroupName().equals(dg)) {
                    group = g;
                    break;
                }
            }
        }

//        if (group != null) {
//            this.dg = group.getDiskGroupName();
//        } else {
//            ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
//            HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
//            dg = request.getParameter("dg");
//        }
    }

    public LazyDataModel<Snapshot> getSnapModel() {
        return snapModel;
    }

    public void setSnapModel(LazyDataModel<Snapshot> snapModel) {
        this.snapModel = snapModel;
    }
}
