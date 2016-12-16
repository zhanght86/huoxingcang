/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.backup.util.Util;
import com.marstor.msa.cdp.model.AgentBean;
import static com.marstor.msa.cdp.model.AgentBean.AGENT_ORACLE;
import static com.marstor.msa.cdp.model.AgentBean.AGENT_SQLSERVER;
import com.marstor.msa.cdp.bean.CdpAgent;
import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpLogPolicy;
import com.marstor.msa.cdp.bean.CdpRollbackTaskInfo;
import com.marstor.msa.cdp.bean.CdpSnapshotInfo;
import com.marstor.msa.cdp.bean.DataBase;
import com.marstor.msa.cdp.model.GlobalProtectStatus;
import com.marstor.msa.cdp.model.SnapShot;
import com.marstor.msa.cdp.model.Sync;
import com.marstor.msa.cdp.socket.MarsClientSocket;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.nas.model.SyncStatus;
import com.marstor.msa.sync.bean.MarsHost;
import com.marstor.msa.sync.bean.SyncMapping;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.sync.web.impl.MsaSYNCInterfaceImpl;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.util.Debug;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SlideEndEvent;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "copyBean")
@ViewScoped
public class CopyBean implements Serializable {

    private List<SnapShot> snaps = new ArrayList<SnapShot>();
    private String path;
    private int index;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    public int strategy = 1;
    public boolean mirror, snapshot, record;
    public String guid;
    public CdpAgent agent;
    public DataBase db;
    private String interval;
    private long defInterval;
    private int port = 40001;
    private boolean isDefault = false;
    private MarsClientSocket clientSock;
    private CdpLogPolicy log;
    private CdpDiskGroupInfo group;
    private String percent;
    private String size;
    private int keepTime = 24;
    private int fullOption = 0;
    private boolean tillFull = false;
    private String basename = "cdp.protect";
    private CdpSnapshotInfo snap;
    private String dg;
    private GlobalProtectStatus globalStatus;
    private ArrayList<Sync> remoteSyncList = new ArrayList<Sync>();
    private MsaSYNCInterface syncInterface = InterfaceFactory.getSYNCInterfaceInstance();
    private ArrayList<Sync> localSyncList = new ArrayList<Sync>();
    private Sync selectSync = new Sync();
    private int level;
    private List<String> groupNames;

    public CopyBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        path = request.getParameter("path");
        guid = request.getParameter("guid");
        dg = request.getParameter("dg");
        if (request.getParameter("level") != null) {
            level = Integer.parseInt(request.getParameter("level"));
        }
        this.groupNames = Arrays.asList(request.getParameterValues("groupName"));
        initGroup();

        if (path != null && path.startsWith("/")) {
            path = path.substring(1);

        }
        if (path != null) {
            List<SyncMapping> mapps = syncInterface.getSyncMapping(path);
            if (mapps != null) {
                Sync sync;
                for (SyncMapping map : mapps) {
                    MarsHost host = new MarsHost(map.getStrIP(), map.getStrPort(), map.getStrUserName(), map.getStrPassword(), map.getStrSSHPort(), map.getStrRootPassword());
                    String lastSnap = map.getStrFirstSnapshot();
                    String strConvertDate = "";
                    if (lastSnap != null && !lastSnap.equals("null")) {
                        String strDate = lastSnap.substring(lastSnap.length() - 14, lastSnap.length());
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        simpleDateFormat.setTimeZone(TimeZone.getDefault());
                        try {
                            strConvertDate = simpleDateFormat.format(formatter.parse(strDate));
                        } catch (ParseException ex) {
                            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                    if (map.getiIsLocal() == 1) { //如果是本地复制
                        sync = new Sync(map.getStrDescFileSystem(), strConvertDate, Sync.localType, res.get("local"), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host, map.getJobStartTime(), map.getJobEndTime(), map.isbTimingJob());
                        this.localSyncList.add(sync);
                    } else {
                        sync = new Sync(map.getStrDescFileSystem(), strConvertDate, Sync.remoteType, map.getStrIP(), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host, map.getJobStartTime(), map.getJobEndTime(), map.isbTimingJob());
                        this.remoteSyncList.add(sync);
                    }
                }
            }
        }
        Debug.print("Enter GlobalProtectStatus:" + group);
        if (group != null) {
            globalStatus = new GlobalProtectStatus(path, level, group, dg, groupNames);
        } else {
            globalStatus = new GlobalProtectStatus(path, level, dg);
        }
    }

    public void init() {
        ArrayList<Sync> localList = new ArrayList<Sync>();
        ArrayList<Sync> remoteList = new ArrayList<Sync>();
        List<SyncMapping> mapps = syncInterface.getSyncMapping(path);
        if (mapps == null) {
            return;
        }
        Sync sync;
        for (SyncMapping map : mapps) {
            MarsHost host = new MarsHost(map.getStrIP(), map.getStrPort(), map.getStrUserName(), map.getStrPassword(), map.getStrSSHPort(), map.getStrRootPassword());
            String lastSnap = map.getStrFirstSnapshot();
            String strConvertDate = "";
            if (lastSnap != null && !lastSnap.equals("null")) {
                String strDate = lastSnap.substring(lastSnap.length() - 14, lastSnap.length());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getDefault());
                try {
                    strConvertDate = simpleDateFormat.format(formatter.parse(strDate));
                } catch (ParseException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (map.getiIsLocal() == 1) { //本地
                sync = new Sync(map.getStrDescFileSystem(), strConvertDate, Sync.localType, res.get("local"), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host, map.getJobStartTime(), map.getJobEndTime(), map.isbTimingJob());
                localList.add(sync);
            } else {
                sync = new Sync(map.getStrDescFileSystem(), strConvertDate, Sync.remoteType, map.getStrIP(), map.getiDescFileSystemHashCode(), map.getiSyncStatus(), host, map.getJobStartTime(), map.getJobEndTime(), map.isbTimingJob());
                remoteList.add(sync);
            }
        }
        initGroup();
        if (group != null) {
            globalStatus = new GlobalProtectStatus(path, level, group, dg, groupNames);
        } else {
            globalStatus = new GlobalProtectStatus(path, level, dg);
        }
        this.localSyncList = localList;
        this.remoteSyncList = remoteList;

    }

    public final void initGroup() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        group = cdp.getDiskGroupInfo(guid);
        Debug.print(group + "");
        if (group == null) {
            Debug.print(group + "group == null");
            List<CdpDiskGroupInfo> gs = cdp.getDiskGroupInfos();
            for (CdpDiskGroupInfo g : gs) {
                Debug.print(g.getDiskGroupName() + "mid");
                if (g.getDiskGroupName().equals(dg)) {
                    group = g;
                    Debug.print(group + "end");
                    break;
                }
            }
        }

    }

    public String addRemoteSync() {
        long autoSnapshotInterval= 0;
        int iMaxNum= 0;
         if(group !=null){
            autoSnapshotInterval= group.getlAutoSnapshotInterval();
            iMaxNum= group.getiMaxNum();
         }

        String param = "path=" + this.path + "&guid=" + this.guid + "&level=" + this.level+ "&dg=" + this.dg+ "&AutoSnapshotInterval=" + autoSnapshotInterval+ "&iMaxNum=" + group.getiMaxNum();
        for (String name : this.groupNames) {
            param += "&groupName=" + name;
        }
        return "add_remote?faces-redirect=true&amp;" + param;
    }

    public String getDg() {

        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public ArrayList<Sync> getRemoteSyncList() {
        return remoteSyncList;
    }

    public void setRemoteSyncList(ArrayList<Sync> remoteSyncList) {
        this.remoteSyncList = remoteSyncList;
    }

    public String configSyncPlan(Sync sync) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Calendar cal;
        Date date;
        cal = sync.getJobStartTime();
        date = cal.getTime();
        String startTime = df.format(date);
        cal = sync.getJobEndTime();
        date = cal.getTime();
        String endTime = df.format(date);
        String bTimingJob = sync.isbTimingJob() ? "1" : "0";
        String param = "path=" + this.path + "&" + "iFileSystemHashCode=" + sync.getiDescFileSystemHashCode() + "&"
                + "iLocal=" + sync.getType() + "&" + "startTime=" + startTime + "&" + "endTime=" + endTime + "&" + "bTimingJob=" + bTimingJob
                + "&guid=" + this.guid + "&level=" + this.level + "&dg=" + this.dg;
        for (String name : this.groupNames) {
            param += "&groupName=" + name;
        }
        return "copy_plan?faces-redirect=true&amp;" + param;
    }

    public void checkBeforeStartSync(Sync sync) {
        //  FileSysSnapSYNCInfo.getRemoteSyncStatus(this.selectedPath, this.selectHost.getIp(), this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword());

        SyncStatus status;
        if (sync.getType() == 1) {
            status = FileSysSnapSYNCInfo.getLocalSyncStatus(sync.getTargetFileSystem());
        } else {
            status = FileSysSnapSYNCInfo.getRemoteSyncStatus(sync.getTargetFileSystem(), sync.getHost().getIp(), sync.getHost().getPort(), sync.getHost().getUsername(), sync.getHost().getPassword());
        }
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
            return;
        }
        if (status.isOnLineOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetFileSysMustOffLine"), res.get("targetFileSysMustOffLine")));
            return;
        }
        if (status.isExistSnapOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetFileSysCanNotExistSnap"), res.get("targetFileSysCanNotExistSnap")));
            return;
        }
        RequestContext.getCurrentInstance().execute("startsync.show()");

    }

    public Sync getSelectSync() {
        return selectSync;
    }

    public void setSelectSync(Sync selectSync) {
        this.selectSync = selectSync;
    }

    public void checkBeforeResumeSync(Sync sync) {
        //  FileSysSnapSYNCInfo.getRemoteSyncStatus(this.selectedPath, this.selectHost.getIp(), this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword());
        SyncStatus status;
        if (sync.getType() == 1) {
            status = FileSysSnapSYNCInfo.getLocalSyncStatus(sync.getTargetFileSystem());
        } else {
            status = FileSysSnapSYNCInfo.getRemoteSyncStatus(sync.getTargetFileSystem(), sync.getHost().getIp(), sync.getHost().getPort(), sync.getHost().getUsername(), sync.getHost().getPassword());
        }
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getFileSysInfoFailed"), res.get("getFileSysInfoFailed")));
            return;
        }
        if (status.isOnLineOrNot()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("targetFileSysMustOffLine"), res.get("targetFileSysMustOffLine")));
            return;
        }
        RequestContext.getCurrentInstance().execute("resumesync.show()");

    }

    public String addLocalSync() {
        String param = "path=" + this.path + "&guid=" + this.guid + "&dg=" + this.dg;
        for (String name : this.groupNames) {
            param += "&groupName=" + name;
        }
        return "add_local?faces-redirect=true&amp;" + param;
    }

    public ArrayList<Sync> getLocalSyncList() {
        return localSyncList;
    }

    public void setLocalSyncList(ArrayList<Sync> localSyncList) {
        this.localSyncList = localSyncList;
    }

    public void updateSnapAndSync() {
        init();
    }

    public void suspendSYNC() {
        boolean flag;
        if (this.selectSync.getType() == 1) {
            flag = syncInterface.suspendSync(path, this.selectSync.getiDescFileSystemHashCode(), true);
        } else {
            flag = syncInterface.suspendSync(path, this.selectSync.getiDescFileSystemHashCode(), false);
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("pauseCopyFailed"), res.get("pauseCopyFailed")));
            return;
        }
        this.init();
        if (guid != null) {
            globalStatus.update();
        }
    }

    public void stopTargetSync() {
        boolean flag = syncInterface.abortSync(path);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCopyFailed"), res.get("stopCopyFailed")));
            return;
        }
        this.init();
    }

    public void deleteSYNC() {
        boolean flag;
        if (this.selectSync.getType() == 1) {
            flag = syncInterface.deleteSync(path, this.selectSync.getiDescFileSystemHashCode(), true);
        } else {
            flag = syncInterface.deleteSync(path, this.selectSync.getiDescFileSystemHashCode(), false);
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delCopyFailed"), res.get("delCopyFailed")));
            return;
        }
        this.init();
        if (guid != null) {
            globalStatus.update();
        }
    }

    public void stopSourceSync() {
        boolean flag;
        if (this.selectSync.getType() == 1) {
            flag = syncInterface.closeSync(this.path, this.selectSync.getiDescFileSystemHashCode(), true);
        } else {
            flag = syncInterface.closeSync(this.path, this.selectSync.getiDescFileSystemHashCode(), false);
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCopyFailed"), res.get("stopCopyFailed")));
            return;
        }
//        this.selectSync.setStartRendered(true);
//        this.selectSync.setSuspendRendered(true);
//        this.selectSync.setStopRendered(false);
//        this.selectSync.setResumeRendered(false);
//        this.selectSync.setSuspendDisabled(true);
//        this.selectSync.setDeleteDisabled(false);

        this.init();
        if (guid != null) {
            globalStatus.update();
        }
    }

    public void startSYNC() {
        boolean flag;
        if (this.selectSync.getType() == 1) {
            flag = syncInterface.startSync(path, this.selectSync.getiDescFileSystemHashCode(), true);
        } else {
            flag = syncInterface.startSync(path, this.selectSync.getiDescFileSystemHashCode(), false);
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("startCopyFailed"), res.get("startCopyFailed")));
            return;
        }
        this.init();
        if (guid != null) {
            globalStatus.update();
        }
    }

    public void resumeSync() {
        boolean flag;
        SyncStatus status = FileSysSnapSYNCInfo.getLocalSyncStatus(path);
        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getLocalFileSysFailed"), res.get("getLocalFileSysFailed")));
            return;
        }
        if (this.selectSync.getType() == 1) {
            flag = syncInterface.resumeSync(path, this.selectSync.getiDescFileSystemHashCode(), true, status.isRollBackOrNot());
        } else {
            flag = syncInterface.resumeSync(path, this.selectSync.getiDescFileSystemHashCode(), false, status.isRollBackOrNot());
        }
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("resumeSyncFailed"), res.get("resumeSyncFailed")));
            return;
        }
        this.init();
        if (guid != null) {
            globalStatus.update();
        }
    }

    public final void initLog() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        group = cdp.getDiskGroupInfo(guid);
        this.log = group.getLogPolicy();
        setDefault();
    }

    public void setDefault() {
        if (group.getTotalDiskSize() != 0) {
            percent = String.valueOf((log.getLogSize() * 100) / (group.getTotalDiskSize()));
        } else {
            percent = "0";
        }

        size = String.valueOf(log.getLogSize() / (1024L * 1024L * 1024L));
        keepTime = log.getLogKeepTime();
        if (keepTime == 100000) {
            tillFull = true;
            keepTime = 0;
        } else {
            tillFull = false;
        }
        fullOption = log.getLogFullOption();
    }

    public String addAgent() {
        String param = "type=0";
        return "add_agent?faces-redirect=true&amp;" + param;
    }

    public String editAgent() {
        String param = "type=" + this.db.getAgentType();
        return "add_agent?faces-redirect=true&amp;" + param;
    }

    public void cloneSnap() {
        MsaSYNCInterface cdp = InterfaceFactory.getSYNCInterfaceInstance();
        String ret = cdp.createClone(this.snap.getSnapshotName());
        if (ret == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "fail"), global.get("error_mark")));
        }
    }

    public void switchPanel() {
        switch (strategy) {
            case 0:
                mirror = true;
                snapshot = false;
                record = false;
                break;
            case 1:
                mirror = false;
                snapshot = true;
                record = false;
                break;
            case 2:
                mirror = false;
                snapshot = false;
                record = true;
                break;
        }
    }

    public String mirror() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        cdp.setProtectType(group.getDiskGroupName(), 0);
        return "dgs?faces-redirect=true";
    }

    public String snapshot() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        cdp.setProtectType(group.getDiskGroupName(), 1);
        //设置间隔和保留数量
        return "dgs?faces-redirect=true";
    }

    public String record() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        cdp.setProtectType(group.getDiskGroupName(), 2);
        //设置记录
        cdp.setLogPolicy(group.getDiskGroupPath(), group.getDiskGroupGuid(),
                Long.valueOf(size), keepTime, tillFull ? 1 : 0);
        //设置代理
        cdp.setDefaultAgent(group.getDiskGroupGuid(), Long.valueOf(interval), null);
        return "dgs?faces-redirect=true";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<SnapShot> getSnaps() {
        return snaps;
    }

    public void setSnaps(List<SnapShot> snaps) {
        this.snaps = snaps;
    }

    public String doBeforeAddSnap() {
        String param = "path=" + this.path + "&guid=" + this.guid;
        return "add_snap?faces-redirect=true&amp;" + param;
    }

    public String doBeforeConfigAutoSnap() {
        String param = "path=" + this.path;
        return "nas_auto_snap_config?faces-redirect=true&amp;" + param;
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public boolean isMirror() {
        return mirror;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public boolean isSnapshot() {
        return snapshot;
    }

    public void setSnapshot(boolean snapshot) {
        this.snapshot = snapshot;
    }

    public boolean isRecord() {
        return record;
    }

    public void setRecord(boolean record) {
        this.record = record;
    }

    public CdpAgent getAgent() {
        return agent;
    }

    public void setAgent(CdpAgent agent) {
        this.agent = agent;
    }

    public DataBase getDb() {
        return db;
    }

    public void setDb(DataBase db) {
        this.db = db;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public long getDefInterval() {
        return defInterval;
    }

    public void setDefInterval(long defInterval) {
        this.defInterval = defInterval;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getts() {
        return String.valueOf(group.getTotalDiskSize() / (1024L * 1024L * 1024L)) + "GB";
    }

    public String setLog() {
        if (!checkNull() || !checkFormat()) {
            return null;
        }

        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        boolean success = cdp.setLogPolicy(path, guid,
                Long.parseLong(size) * 1024L * 1024L * 1024L, keepTime, fullOption);
        if (!success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            return null;
        } else {
            return "dgs?faces-redirect=true";
        }
    }

    public boolean checkFormat() {
        if (!DGUtility.checkShareName(size)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format0"), global.get("error_mark")));
            return false;
        }
        if (!DGUtility.checkShareName(String.valueOf(keepTime))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format1"), global.get("error_mark")));
            return false;
        }
        return true;
    }

    private boolean checkNull() {
        if (size == null || size.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null0"), global.get("error_mark")));
            return false;
        }
        if (String.valueOf(keepTime).equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null1"), global.get("error_mark")));
            return false;
        }
        return true;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getKeepTime() {
        return keepTime;
    }

    public void setKeepTime(int keepTime) {
        this.keepTime = keepTime;
    }

    public int getFullOption() {
        return fullOption;
    }

    public void setFullOption(int fullOption) {
        this.fullOption = fullOption;
    }

    public boolean isTillFull() {
        return tillFull;
    }

    public void setTillFull(boolean tillFull) {
        this.tillFull = tillFull;
    }

    public void onSlideEnd(SlideEndEvent event) {
        FacesMessage msg = new FacesMessage("Slide Ended", "Value: " + event.getValue());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public CdpLogPolicy getLog() {
        return log;
    }

    public void setLog(CdpLogPolicy log) {
        this.log = log;
    }

    public CdpDiskGroupInfo getGroup() {
        return group;
    }

    public void setGroup(CdpDiskGroupInfo group) {
        this.group = group;
    }

    public String strType(int type) {
        String s = "";
        switch (type) {
            case AGENT_ORACLE:
                s = "Oracle代理";
                break;
            case AGENT_SQLSERVER:
                s = "SQL Server代理";
                break;
        }
        return s;
    }

    public String getAgentString(int id) {
        String s = "默认代理";
        switch (id) {
            case AgentBean.AGENT_ORACLE:
                s = "Oracle代理";
                break;
            case AgentBean.AGENT_SQLSERVER:
                s = "SQL Server代理";
                break;
            case 999:
                s = "初始快照";
        }
        return s;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public CdpSnapshotInfo getSnap() {
        return snap;
    }

    public void setSnap(CdpSnapshotInfo snap) {
        this.snap = snap;
    }

    public GlobalProtectStatus getGlobalStatus() {
        return globalStatus;
    }

    public void setGlobalStatus(GlobalProtectStatus globalStatus) {
        this.globalStatus = globalStatus;
    }
}
