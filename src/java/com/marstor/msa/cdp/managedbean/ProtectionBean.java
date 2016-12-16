/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.nas.model.SyncListBean;
import com.marstor.msa.nas.model.GlobalProtectStatus;
import com.marstor.msa.nas.model.SnapShot;
import com.marstor.msa.cdp.model.AgentBean;
import static com.marstor.msa.cdp.model.AgentBean.AGENT_ORACLE;
import static com.marstor.msa.cdp.model.AgentBean.AGENT_SQLSERVER;
import com.marstor.msa.cdp.bean.CdpAgent;
import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpLogPolicy;
import com.marstor.msa.cdp.bean.CdpSnapshotInfo;
import com.marstor.msa.cdp.bean.DataBase;
import com.marstor.msa.cdp.socket.MarsClientSocket;
import com.marstor.msa.cdp.socket.action.GetAgentsAction;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SlideEndEvent;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class ProtectionBean implements Serializable {

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
    private String host = "192.168.1.87";
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
    private SyncListBean sync;
    
    private GlobalProtectStatus globalStatus;

    public ProtectionBean() {
        mirror = true;
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        path = request.getParameter("path");
        guid = request.getParameter("guid");
        if (request.getParameter("type") != null && Integer.valueOf(request.getParameter("type")) == 3) {
            this.strategy = 3;
        }
        initAgents();
        initLog();
        strategy = group.getProtectType();
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

    public final void initAgents() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        group = cdp.getDiskGroupInfo(guid);
        agent = group.getCdpAgent();
        if (agent.getListDB().size() > 0) {
            this.host = agent.getListDB().get(0).getIp();
            if (host == null) {
                host = "";
            }
            this.port = agent.getListDB().get(0).getPort();
            if (port == 0) {
                port = 1100;
            }
            if (group.getlSnapshotInterval() != 0) {
                this.isDefault = true;
                this.interval = String.valueOf(group.getlSnapshotInterval() / 1000);
            }
            clientSock = new MarsClientSocket();
            if (!clientSock.Connect(host, port)) {
                return;
            }

            GetAgentsAction action = new GetAgentsAction(clientSock, this.guid);
            AgentBean[] ags = action.getAgents();
            clientSock.Close();
            List<DataBase> list = new ArrayList();
            for (int i = 0; i < ags.length; i++) {
                DataBase d = new DataBase(ags[i].MSAIP, Integer.valueOf(ags[i].MSAPort), "", ags[i].agentID);
                list.add(d);
            }
            this.agent.setListDB(list);
            cdp.setDefaultAgent(guid, defInterval, list);
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
    
    public String snapshot(){
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        cdp.setProtectType(group.getDiskGroupName(), 1);
        //设置间隔和保留数量
        return "dgs?faces-redirect=true";
    }
    
    public String record(){
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        cdp.setProtectType(group.getDiskGroupName(), 2);
        //设置记录
        cdp.setLogPolicy(group.getDiskGroupPath(), group.getDiskGroupGuid(), 
                Long.valueOf(size), keepTime, tillFull?1:0);
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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
                s = "系统快照";
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

    public SyncListBean getSync() {
        return sync;
    }

    public void setSync(SyncListBean sync) {
        this.sync = sync;
    }

    public GlobalProtectStatus getGlobalStatus() {
        return globalStatus;
    }

    public void setGlobalStatus(GlobalProtectStatus globalStatus) {
        this.globalStatus = globalStatus;
    }
    
    
}
