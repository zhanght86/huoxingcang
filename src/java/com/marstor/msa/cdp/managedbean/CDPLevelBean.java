/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.backup.managedbean.AgentManager;
import com.marstor.msa.nas.model.GlobalProtectStatus;
import com.marstor.msa.nas.model.GlobalSnapSYNCInfo;
import com.marstor.msa.nas.model.SnapShot;
import com.marstor.msa.nas.model.SyncListBean;
import com.marstor.msa.cdp.model.AgentBean;
import static com.marstor.msa.cdp.model.AgentBean.AGENT_ORACLE;
import static com.marstor.msa.cdp.model.AgentBean.AGENT_SQLSERVER;
import com.marstor.msa.cdp.model.AgentOracleBean;
import com.marstor.msa.cdp.model.AgentSQLServerBean;
import com.marstor.msa.cdp.bean.CdpAgent;
import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpLogPolicy;
import com.marstor.msa.cdp.bean.CdpSnapshotInfo;
import com.marstor.msa.cdp.bean.DataBase;
import com.marstor.msa.cdp.socket.BasicQueryAction;
import com.marstor.msa.cdp.socket.MarsClientSocket;
import com.marstor.msa.cdp.socket.action.GetAgentsAction;
import com.marstor.msa.cdp.util.AgentUtility;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyEncryp;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.xml.XMLConstructor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "cdpLevelBean")
@ViewScoped
public class CDPLevelBean implements Serializable {

    private List<SnapShot> snaps = new ArrayList<SnapShot>();
    private String path;
    private int index;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    public int strategy = 0;
    public boolean mirror, snapshot, RECORD;
    public String guid;
    public CdpAgent agent;
    public DataBase db;
    private String dInterval = "1";
    private String sInterval = "1";
    private String host = "";
    private int port = 40001;
    private boolean isDefault = true;
    private MarsClientSocket clientSock;
    private CdpLogPolicy log;
    private CdpDiskGroupInfo group;
    private String percent;
    private String size = "10";
    private String keepTime = "24";
    private String fullOption = "1";
    private boolean tillFull = false;
    private String basename = "cdp.cdp_level";
    private CdpSnapshotInfo snap;
    private SyncListBean sync;
    private String snapNum = "256";
    private GlobalProtectStatus globalStatus;
    private String dg;
    private int origin;
    private List<AgentOracleBean> oracleAgents = new ArrayList<AgentOracleBean>();
    private List<AgentSQLServerBean> sqlseverAgents = new ArrayList<AgentSQLServerBean>();
    private List<DataBase> listDB = new ArrayList<DataBase>();
    private boolean bRegister = false;
    private long registerSize;
    private long avialableSize;
    private String register;
    private String used;

    public CDPLevelBean() {
        mirror = true;
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        path = request.getParameter("path");
        guid = request.getParameter("guid");
        if (request.getParameter("record") != null) {
            this.bRegister = true;

            if (request.getParameter("register") != null && request.getParameter("used") != null) {
                this.register = request.getParameter("register");
                this.used = request.getParameter("used");
                this.registerSize = Long.parseLong(request.getParameter("register")) * 1024L * 1024L * 1024L * 1024L;
                this.avialableSize = this.registerSize - Long.parseLong(request.getParameter("used"));
            }
        }
        if (guid != null) {
            initGroup();
        } else {
            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
            List<CdpDiskGroupInfo> dgs = cdp.getDiskGroupInfos();
            for (CdpDiskGroupInfo g : dgs) {
                if (g.getDiskGroupPath().equals(path)) {
                    group = g;
                    this.guid = g.getDiskGroupGuid();
                    break;
                }
            }
        }

        dg = group.getDiskGroupName();
        strategy = group.getProtectType();
        this.origin = group.getProtectType();

        if (request.getParameter("fake") != null) {
            this.size = request.getParameter("rememberSize");
            this.keepTime = request.getParameter("rememberKeepTime");
            this.tillFull = Boolean.valueOf(request.getParameter("rememberTillFull"));
            this.fullOption = request.getParameter("rememberFullOption");
            if (request.getParameter("rememberHost") != null) {
                this.host = request.getParameter("rememberHost");
            }
            if (request.getParameter("rememberPort") != null) {
                this.port = Integer.valueOf(request.getParameter("rememberPort"));
            }

            if (request.getParameterValues("msaIP") != null) {
                int i = 0;
                List<String> dbaNames;
                String[] msaIPs = request.getParameterValues("msaIP");
                String[] msaPorts = request.getParameterValues("msaPort");
                String[] pwds = request.getParameterValues("pwd");
                String[] IPs = request.getParameterValues("ip");
                String[] ports = request.getParameterValues("port");
                String[] instances = request.getParameterValues("instance");
                String[] intervals = request.getParameterValues("interval");
                if (request.getParameterValues("dbaName") != null) {
                    dbaNames = Arrays.asList(request.getParameterValues("dbaName"));
                    for (; i < dbaNames.size(); i++) {
                        AgentOracleBean o = new AgentOracleBean();
                        o.MSAIP = msaIPs[i];
                        o.MSAPort = msaPorts[i];
                        o.agentID = AgentBean.AGENT_ORACLE;
                        o.dbaName = dbaNames.get(i);
                        o.password = pwds[i];
                        o.ip = IPs[i];
                        o.port = ports[i];
                        o.instanceName = instances[i];
                        o.interval = intervals[i];
                        this.oracleAgents.add(o);
                        DataBase d = new DataBase(o.MSAIP, Integer.valueOf(o.MSAPort), "", o.agentID);
                        d.setAgentType(o.agentID);
                        d.setDbInstance(o.instanceName);
                        listDB.add(d);
                    }
                }
                List<String> dbs;
                if (request.getParameterValues("db") != null) {
                    dbs = Arrays.asList(request.getParameterValues("db"));
                    String[] userNames = request.getParameterValues("userName");
                    String[] modes = request.getParameterValues("mode");
                    for (int j = 0; j < dbs.size(); j++, i++) {
                        AgentSQLServerBean o = new AgentSQLServerBean();
                        o.MSAIP = msaIPs[i];
                        o.MSAPort = msaPorts[i];
                        o.agentID = AgentBean.AGENT_SQLSERVER;
                        if (userNames != null) {
                            o.userName = userNames[j];
                        }
                        if (pwds != null) {
                            o.password = pwds[i];
                        }
                        o.ip = IPs[i];
                        o.port = Integer.parseInt(ports[i]);
                        o.instanceName = instances[i];
                        o.interval = intervals[i];
                        o.database = dbs.get(j);
                        o.mode = Integer.parseInt(modes[j]);
                        this.sqlseverAgents.add(o);
                        DataBase d = new DataBase(o.MSAIP, Integer.valueOf(o.MSAPort), "", o.agentID);
                        d.setDbInstance(o.instanceName);
                        d.setAgentType(o.agentID);
                        listDB.add(d);
                    }
                }
            }
        } else {
            if (this.strategy == CDPConstants.PROTECT_SNAPSHOT) {
                this.sInterval = String.valueOf(group.getlAutoSnapshotInterval() / 60);
                this.snapNum = String.valueOf(group.getiMaxNum());
            } else if (this.strategy == CDPConstants.PROTECT_RECORD) {
                initAgents();
                initLog();
            }
        }


        System.out.println(request.getParameter("type"));
        if (request.getParameter("type") != null) {
            this.strategy = Integer.valueOf(request.getParameter("type"));
            this.isDefault = false;
        }
        this.switchPanel();
    }

    public final void initLog() {
        if (group == null) {
            return;
        }
        this.log = group.getLogPolicy();
        Debug.print("group.getlSnapshotInterval():" + group.getlSnapshotInterval());
        this.sInterval = String.valueOf(group.getlSnapshotInterval() / 60);
        this.snapNum = String.valueOf(group.getiMaxNum());
        setDefault();
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public void setDefault() {
        if (group.getTotalDiskSize() != 0) {
            percent = String.valueOf((log.getLogSize() * 100) / (group.getTotalDiskSize()));
        } else {
            percent = "0";
        }

        size = String.valueOf(log.getLogSize() / (1024L * 1024L * 1024L));
        keepTime = String.valueOf(log.getLogKeepTime());
        if (keepTime.equals("100000")) {
            tillFull = true;
            keepTime = "0";
        } else {
            tillFull = false;
        }
        fullOption = String.valueOf(log.getLogFullOption());
    }

    public String addAgent() {
        StringBuilder sb = new StringBuilder();
        sb.append("type=0&path=");
        sb.append(this.path);
        sb.append("&guid=");
        sb.append(this.guid);
        sb.append("&register=");
        sb.append(this.register);
        sb.append("&used=");
        sb.append(this.used);
        // remember the user's input
        sb.append("&rememberSize=");
        sb.append(this.size);
        sb.append("&rememberKeepTime=");
        sb.append(this.keepTime);
        sb.append("&rememberTillFull=");
        sb.append(this.tillFull);
        sb.append("&rememberFullOption=");
        sb.append(this.fullOption);
        sb.append("&rememberHost=");
        sb.append(this.host);
        sb.append("&rememberPort=");
        sb.append(this.port);
        if (this.oracleAgents != null && !this.oracleAgents.isEmpty()) {
            for (AgentOracleBean orcl : this.oracleAgents) {
                sb.append("&msaIP=");
                sb.append(orcl.MSAIP);
                sb.append("&msaPort=");
                sb.append(orcl.MSAPort);
                sb.append("&agentID=");
                sb.append(orcl.agentID);
                sb.append("&dbaName=");
                sb.append(orcl.dbaName);
                sb.append("&pwd=");
                sb.append(orcl.password);
                sb.append("&ip=");
                sb.append(orcl.ip);
                sb.append("&port=");
                sb.append(orcl.port);
                sb.append("&instance=");
                sb.append(orcl.instanceName);
                sb.append("&interval=");
                sb.append(orcl.interval);
            }
        }
        if (this.sqlseverAgents != null && !this.sqlseverAgents.isEmpty()) {
            for (AgentSQLServerBean sq : this.sqlseverAgents) {
                sb.append("&msaIP=");
                sb.append(sq.MSAIP);
                sb.append("&msaPort=");
                sb.append(sq.MSAPort);
                sb.append("&agentID=");
                sb.append(sq.agentID);
                sb.append("&userName=");
                sb.append(sq.userName);
                sb.append("&pwd=");
                sb.append(sq.password);
                sb.append("&ip=");
                sb.append(sq.ip);
                sb.append("&port=");
                sb.append(sq.port);
                sb.append("&instance=");
                sb.append(sq.instanceName);
                sb.append("&interval=");
                sb.append(sq.interval);
                sb.append("&db=");
                sb.append(sq.database);
                sb.append("&mode=");
                sb.append(sq.mode);
            }
        }
        return "add_agent?faces-redirect=true&amp;" + sb.toString();
    }

    public String editAgent() {
        StringBuilder sb = new StringBuilder();
        sb.append("&path=");
        sb.append(this.path);
        sb.append("&guid=");
        sb.append(this.guid);
        sb.append("&type=");
        sb.append(this.db.getAgentType());
        sb.append("&register=");
        sb.append(this.register);
        sb.append("&used=");
        sb.append(this.used);
        // remember the user's input
        sb.append("&rememberSize=");
        sb.append(this.size);
        sb.append("&rememberKeepTime=");
        sb.append(this.keepTime);
        sb.append("&rememberTillFull=");
        sb.append(this.tillFull);
        sb.append("&rememberFullOption=");
        sb.append(this.fullOption);
        sb.append("&rememberHost=");
        sb.append(this.host);
        sb.append("&rememberPort=");
        sb.append(this.port);
        if (this.oracleAgents != null && !this.oracleAgents.isEmpty()) {
            for (AgentOracleBean orcl : this.oracleAgents) {
                if (db.getAgentType() == AgentBean.AGENT_ORACLE && db.getDbInstance().equals(orcl.getInstanceName())) {
                    sb.append("&oracle=");
                    sb.append(orcl.instanceName);
                }
                sb.append("&msaIP=");
                sb.append(orcl.MSAIP);
                sb.append("&msaPort=");
                sb.append(orcl.MSAPort);
                sb.append("&agentID=");
                sb.append(orcl.agentID);
                sb.append("&dbaName=");
                sb.append(orcl.dbaName);
                sb.append("&pwd=");
                sb.append(orcl.password);
                sb.append("&ip=");
                sb.append(orcl.ip);
                sb.append("&port=");
                sb.append(orcl.port);
                sb.append("&instance=");
                sb.append(orcl.instanceName);
                sb.append("&interval=");
                sb.append(orcl.interval);
            }
        }

        if (this.sqlseverAgents != null && !this.sqlseverAgents.isEmpty()) {
            for (AgentSQLServerBean sq : this.sqlseverAgents) {
                if (db.getAgentType() == AgentBean.AGENT_SQLSERVER && db.getDbInstance().equals(sq.getInstanceName())) {
                    sb.append("&sql=");
                    sb.append(sq.instanceName);
                }
                sb.append("&msaIP=");
                sb.append(sq.MSAIP);
                sb.append("&msaPort=");
                sb.append(sq.MSAPort);
                sb.append("&agentID=");
                sb.append(sq.agentID);
                sb.append("&userName=");
                sb.append(sq.userName);
                sb.append("&pwd=");
                sb.append(sq.password);
                sb.append("&ip=");
                sb.append(sq.ip);
                sb.append("&port=");
                sb.append(sq.port);
                sb.append("&instance=");
                sb.append(sq.instanceName);
                sb.append("&interval=");
                sb.append(sq.interval);
                sb.append("&db=");
                sb.append(sq.database);
                sb.append("&mode=");
                sb.append(sq.mode);
            }
        }

        return "add_agent?faces-redirect=true&amp;" + sb.toString();
    }

    public void deleteAgent() {
        int del = -1;
        if (this.db.getAgentType() == AgentBean.AGENT_ORACLE) {
            for (int i = 0; i < this.oracleAgents.size(); i++) {
                if (this.oracleAgents.get(i).instanceName.equals(db.getDbInstance())) {
                    del = i;
                    break;
                }
            }
            if (del != -1) {
                this.oracleAgents.remove(del);
            }
        }

        if (this.db.getAgentType() == AgentBean.AGENT_SQLSERVER) {
            for (int i = 0; i < this.sqlseverAgents.size(); i++) {
                if (this.sqlseverAgents.get(i).instanceName.equals(db.getDbInstance())) {
                    del = i;
                    break;
                }
            }
            if (del != -1) {
                this.sqlseverAgents.remove(del);
            }
        }

        this.listDB = new ArrayList();
        for (AgentOracleBean o : this.oracleAgents) {
            if (o.instanceName.equals("")) {
                continue;
            }
            DataBase d = new DataBase(o.MSAIP, Integer.valueOf(o.MSAPort), "", o.agentID);
            d.setAgentType(o.agentID);
            d.setDbInstance(o.instanceName);
            listDB.add(d);
        }

        for (AgentSQLServerBean o : this.sqlseverAgents) {
            if (o.instanceName.equals("")) {
                continue;
            }
            DataBase d = new DataBase(o.MSAIP, Integer.valueOf(o.MSAPort), "", o.agentID);
            d.setDbInstance(o.instanceName);
            d.setAgentType(o.agentID);
            listDB.add(d);
        }
    }

    public void cloneSnap() {
        MsaSYNCInterface cdp = InterfaceFactory.getSYNCInterfaceInstance();
        String ret = cdp.createClone(this.snap.getSnapshotName());
        if (ret == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "fail"), global.get("error_mark")));
        }
    }

    public final void initGroup() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        group = cdp.getDiskGroupInfo(guid);
    }

    public final void initAgents() {
        if (group == null) {
            return;
        }
        if (group.getCdpAgent() == null) {
            return;
        }

        agent = group.getCdpAgent();

        if (agent.getListDB() == null) {
            return;
        }

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
                this.dInterval = String.valueOf(group.getlSnapshotInterval() / 60);
            }
        }
        if (host.equals("")) {
            return;
        }
        clientSock = new MarsClientSocket();
        if (!clientSock.Connect(host, port)) {
            return;
        }

        GetAgentsAction action = new GetAgentsAction(clientSock, this.guid);
        AgentBean[] ags = action.getAgents();
        clientSock.Close();

        for (int i = 0; i < ags.length; i++) {
            DataBase d;
            if (ags[i] != null && !ags[i].MSAPort.equals("")) {
                d = new DataBase(ags[i].MSAIP, Integer.valueOf(ags[i].MSAPort), "", ags[i].agentID);
            } else {
                d = new DataBase(ags[i].MSAIP, 1099, "", ags[i].agentID);
            }

            if (ags[i] instanceof AgentOracleBean) {
                AgentOracleBean o = (AgentOracleBean) ags[i];
                if (o.instanceName.equals("")) {
                    continue;
                }
                d.setDbInstance(o.instanceName);
                this.oracleAgents.add((AgentOracleBean) ags[i]);
            } else if (ags[i] instanceof AgentSQLServerBean) {
                AgentSQLServerBean o = (AgentSQLServerBean) ags[i];
                if (o.instanceName.equals("")) {
                    continue;
                }
                d.setDbInstance(o.instanceName);
                this.sqlseverAgents.add((AgentSQLServerBean) ags[i]);
            }
            listDB.add(d);
        }

        this.isDefault = false;
    }

    public void change() {
    }

    public List<DataBase> getListDB() {
        return listDB;
    }

    public void setListDB(List<DataBase> listDB) {
        this.listDB = listDB;
    }

    public final void switchPanel() {
        switch (strategy) {
            case 0:
                mirror = true;
                snapshot = false;
                RECORD = false;
                break;
            case 1:
                mirror = false;
                snapshot = true;
                RECORD = false;
                break;
            case 2:
                mirror = false;
                snapshot = false;
                RECORD = true;
                break;
        }
    }

    public boolean isRegister() {
        return bRegister;
    }

    public void setRegister(boolean register) {
        this.bRegister = register;
    }

    public String preMirror() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();

        if (this.origin != this.strategy && group.getiAutoSnapshotIsOpen() == CDPConstants.TRUE) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCDPplease"), res.get("stopCDPplease")));
            return null;
        }
        if (this.origin != this.strategy && group.isCDPStarted()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCDPplease"), res.get("stopCDPplease")));
            return null;
        }
        if (this.origin != this.strategy) {
            RequestContext.getCurrentInstance().execute("mirror.show()");
            return null;
        } else {
            return mirror();
        }

    }

    public String mirror() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();

        if (this.origin != this.strategy) {
            List<CdpDiskGroupInfo> groups = cdp.getDiskGroupInfos();
            List<CdpDiskGroupInfo> copies = new ArrayList();
            for (CdpDiskGroupInfo g : groups) {
                if (g.getDiskGroupName().contains(dg + "_")) {
                    copies.add(g);
                }
            }

            for (CdpDiskGroupInfo del : copies) {
                cdp.deleteDiskGroup(del.getDiskGroupGuid(), del.getDiskGroupPath());
            }
            InterfaceFactory.getSYNCInterfaceInstance().deleteAllSnapshot(group.getDiskZfsName(), true);
        }

        cdp.setProtectType(group.getDiskZfsName(), 0);
        return "cdp?faces-redirect=true";
    }

    public String preSnapshot() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        if (this.origin == this.strategy && group.getiAutoSnapshotIsOpen() == CDPConstants.TRUE) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCDPplease"), res.get("stopCDPplease")));
            return null;
        }
        if (this.origin != this.strategy && group.isCDPStarted()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCDPplease"), res.get("stopCDPplease")));
            return null;
        }

        String snapshotIntervalUnit;
        int exactInterval = GlobalSnapSYNCInfo.getInstance().getAutoSnapInterval();
        int minSnap = GlobalSnapSYNCInfo.getInstance().getMinAutoSnapNum();
        int maxSnap = GlobalSnapSYNCInfo.getInstance().getMaxAutoSnapNum();
        //验证自动快照时间间隔

        if (this.sInterval == null || this.sInterval.equalsIgnoreCase("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalCanNotEmpty"), res.get("intervalCanNotEmpty")));
            return null;
        }

        if (sInterval.matches("^[0-9]*[1-9][0-9]*$")) {
            int n = -1;
            if (exactInterval < 60) {
                snapshotIntervalUnit = res.get("second");
            } else {
                snapshotIntervalUnit = res.get("minute");
                exactInterval = exactInterval / 60;
            }
            if (snapshotIntervalUnit.equals(res.get("minute"))) {
                if (this.sInterval.length() > 3) {
                    // 时间间隔为1—999分钟。
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalLimit"), res.get("intervalLimit")));
                    return null;
                } else {
                    n = Integer.parseInt(this.sInterval);
                    if (n < 1 || n > 999) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalLimit"), res.get("intervalLimit")));
                        return null;
                    }
                }

            } else {
                if (this.sInterval.length() > 5) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalLimit"), res.get("intervalLimit")));
                    return null;
                } else {
                    n = Integer.parseInt(this.sInterval);
                    if (n < 1 || n > 59940) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalLimit"), res.get("intervalLimit")));
                        return null;
                    }
                }
            }

        } else {
            //请在时间间隔栏输入有效数字。
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("inputValidNum"), res.get("inputValidNum")));
            return null;
        }
        int trueSnapshotInterval = 0;
        if (snapshotIntervalUnit.equals(res.get("minute"))) {
            trueSnapshotInterval = Integer.valueOf(this.sInterval) * 60;  //单位转化为秒
        }
        if (trueSnapshotInterval < exactInterval) {
            if (snapshotIntervalUnit.equals(res.get("minute"))) {
                //时间间隔不能小于
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalCanNotLess") + exactInterval / 60 + res.get("minute") + "。", res.get("intervalCanNotLess") + exactInterval / 60 + res.get("minute") + "。"));
                return null;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalCanNotLess") + exactInterval + res.get("second") + "。", res.get("intervalCanNotLess") + exactInterval + res.get("second") + "。"));
                return null;
            }
        }

        //验证快照保留数量
        if (this.snapNum == null || this.snapNum.equalsIgnoreCase("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("reserveNumNotEmpty"), res.get("reserveNumNotEmpty")));
            return null;
        }
        if (snapNum.matches("^[0-9]*[1-9][0-9]*$")) {
            if (snapNum.length() < 11) {
                long n = Long.parseLong(snapNum);
//                    System.out.println("n=" + n);
                if (n < 1) {
                    //请在时间间隔栏输入有效数字。
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("inputValidNum"), res.get("inputValidNum")));
                    return null;
                }
                if (Long.valueOf(this.snapNum) < minSnap || Long.valueOf(this.snapNum) > maxSnap) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("reserveNumIs") + minSnap + "-" + maxSnap + "。", res.get("reserveNumIs") + minSnap + "-" + maxSnap + "。"));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("reserveNumIs") + minSnap + "-" + maxSnap + "。", res.get("reserveNumIs") + minSnap + "-" + maxSnap + "。"));
                return null;
            }

        } else {
            //请在快照保留数量栏输入有效数字。
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("inputValidSnapNum"), res.get("inputValidSnapNum")));
            return null;
        }
        if (this.origin != this.strategy && this.origin != 0) {
            RequestContext.getCurrentInstance().execute("snapshot.show()");
            return null;
        } else {
            return snapshot();
        }
    }

    public String snapshot() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        cdp.setProtectType(group.getDiskZfsName(), 1);
        cdp.configSnapshotProtect(group.getDiskZfsName(), Integer.valueOf(this.sInterval) * 60, Integer.parseInt(this.snapNum));
        if (this.origin != this.strategy) {

            List<CdpDiskGroupInfo> groups = cdp.getDiskGroupInfos();
            List<CdpDiskGroupInfo> copies = new ArrayList();
            for (CdpDiskGroupInfo g : groups) {
                if (g.getDiskGroupName().contains(dg + "_")) {
                    copies.add(g);
                }
            }

            for (CdpDiskGroupInfo del : copies) {
                cdp.deleteDiskGroup(del.getDiskGroupGuid(), del.getDiskGroupPath());
            }

            InterfaceFactory.getSYNCInterfaceInstance().deleteAllSnapshot(group.getDiskZfsName(), true);

        }
        return "cdp?faces-redirect=true";
    }

    public String preRecord() {

        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        if (this.origin == this.strategy && group.isCDPStarted()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCDPplease"), res.get("stopCDPplease")));
            return null;
        }
        if (this.origin != this.strategy && group.getiAutoSnapshotIsOpen() == CDPConstants.TRUE) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopCDPplease"), res.get("stopCDPplease")));
            return null;
        }

        if (size == null || size.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null0"), global.get("error_mark")));
            return null;
        }
        if (!DGUtility.checkNum(size, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format0"), global.get("error_mark")));
            return null;
        }

        if (keepTime == null || String.valueOf(keepTime).equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null1"), global.get("error_mark")));
            return null;
        }
        if (!DGUtility.checkNum(keepTime, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format1"), global.get("error_mark")));
            return null;
        }

        if (this.isDefault) {
            if (dInterval == null || this.dInterval.equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null00"), global.get("error_mark")));
                return null;
            }
            if (!DGUtility.checkNum(dInterval, false)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format00"), global.get("error_mark")));
                return null;
            }
        }
        if (!this.isDefault) {
            if (host == null || host.equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null000"), global.get("error_mark")));
                return null;
            }
            if (String.valueOf(port).equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null111"), global.get("error_mark")));
                return null;
            }
            if (!DGUtility.checkIP(host)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f1"), global.get("error_mark")));
                return null;
            }
            if (!AgentUtility.checkNum(String.valueOf(port), false)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f2"), global.get("error_mark")));
                return null;
            }
            if (!AgentUtility.checkPort(String.valueOf(port))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get("portRange"), global.get("error_mark")));
                return null;
            }

            if (this.listDB == null || this.listDB.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "Addplease"), global.get("error_mark")));
                return null;
            }
        }
        long total = group.getTotalDiskSize() + Long.parseLong(size) * 1024L * 1024L * 1024L;

        if (total > this.avialableSize) {
            String message = res.get(basename, "regSize") + this.registerSize / (double) (1024L * 1024L * 1024L) + "GB    "
                    + res.get(basename, "aviSize") + this.avialableSize / (double) (1024L * 1024L * 1024L) + "GB    "
                    + res.get(basename, "gSize") + total / (double) (1024L * 1024L * 1024L) + "GB";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, global.get("error_mark")));
            return null;
        }

        if (this.origin != this.strategy && this.origin != 0) {
            RequestContext.getCurrentInstance().execute("record.show()");
            return null;
        } else {
            return record();
        }

    }

    public String record() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();

        cdp.setProtectType(group.getDiskZfsName(), 2);

        //设置记录
        this.setLog();

        //设置代理        
        List<DataBase> dbList = new ArrayList();
        if (!isDefault) {
            dInterval = "0";
            if (!this.checkVersion()) {
                return null;
            }
            if (this.oracleAgents != null && !this.oracleAgents.isEmpty()) {
                for (AgentOracleBean o : this.oracleAgents) {
                    if (!setOracleAgentAction(o)) {
                        return null;
                    }
                    dbList.add(new DataBase(host, port, o.instanceName, o.agentID));
                }
            }
            if (this.sqlseverAgents != null && !this.sqlseverAgents.isEmpty()) {
                for (AgentSQLServerBean o : this.sqlseverAgents) {
                    if (!setSQLServerAgentAction(o)) {
                        return null;
                    }
                    dbList.add(new DataBase(host, port, o.instanceName, o.agentID));
                }
            }
        }
        cdp.setDefaultAgent(group.getDiskGroupGuid(), Long.valueOf(dInterval) * 60, dbList);

        if (this.origin != this.strategy) {


            List<CdpDiskGroupInfo> groups = cdp.getDiskGroupInfos();
            List<CdpDiskGroupInfo> copies = new ArrayList();
            for (CdpDiskGroupInfo g : groups) {
                if (g.getDiskGroupName().contains(dg + "_")) {
                    copies.add(g);
                }
            }

            for (CdpDiskGroupInfo del : copies) {
                cdp.deleteDiskGroup(del.getDiskGroupGuid(), del.getDiskGroupPath());
            }

            boolean flag = InterfaceFactory.getSYNCInterfaceInstance().deleteAllSnapshot(group.getDiskZfsName(), true);
            if (!flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("delSnapFailed"), res.get("delSnapFailed")));
                return null;
            }
        }
        return "cdp?faces-redirect=true";
    }

    private boolean setOracleAgentAction(AgentOracleBean agentOracle) {
        clientSock = new MarsClientSocket();
        if (!clientSock.Connect(host, port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("connectFailed"), res.get("connectFailed")));
            return false;
        }

        BasicQueryAction action = new BasicQueryAction("9601", "Add host", clientSock);
        action.addParameter("Group", this.guid);
        action.addParameter("MSAIP", agentOracle.MSAIP);
        action.addParameter("MSAPort", agentOracle.MSAPort);
        XMLConstructor oracle = new XMLConstructor("Agent");
        oracle.addNode("AgentID", AgentBean.AGENT_ORACLE);
        oracle.addNode("Describe", agentOracle.instanceName);
        XMLConstructor parameterName = new XMLConstructor("Parameter");
        parameterName.addNode("Key", "DBAName");
        parameterName.addNode("Value", agentOracle.dbaName);
        oracle.addNode(parameterName);

        XMLConstructor parameterPassword = new XMLConstructor("Parameter");
        parameterPassword.addNode("Key", "Password");
        parameterPassword.addNode("Value", new String(MyEncryp.Encode64(agentOracle.password.toCharArray())));
        oracle.addNode(parameterPassword);

        XMLConstructor parameterIP = new XMLConstructor("Parameter");
        parameterIP.addNode("Key", "IP");
        parameterIP.addNode("Value", agentOracle.ip);
        oracle.addNode(parameterIP);

        XMLConstructor parameterPort = new XMLConstructor("Parameter");
        parameterPort.addNode("Key", "Port");
        parameterPort.addNode("Value", agentOracle.port);
        oracle.addNode(parameterPort);

        XMLConstructor parameterInstance = new XMLConstructor("Parameter");
        parameterInstance.addNode("Key", "Instance");
        parameterInstance.addNode("Value", agentOracle.instanceName);
        oracle.addNode(parameterInstance);

        XMLConstructor parameterInterval = new XMLConstructor("Parameter");
        parameterInterval.addNode("Key", "Interval");
        parameterInterval.addNode("Value", agentOracle.interval);
        oracle.addNode(parameterInterval);

        action.addParameter(oracle);
        Debug.bDebug = true;
        Debug.print(action.sendXML.toXmlString());

        if (!action.queryAndGetBooleanResult()) {
            action.returnXML.toXmlString();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return false;
        }
        action.returnXML.toXmlString();
        return true;
    }

    private boolean checkVersion() {
        clientSock = new MarsClientSocket();
        if (!clientSock.Connect(host, port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("connectFailed"), res.get("connectFailed")));
            return false;
        }
        BasicQueryAction action = new BasicQueryAction("9613", "Check Version", clientSock);

        if (!action.queryAndGetBooleanResult()) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "updateplease"), global.get("error_mark")));
            RequestContext.getCurrentInstance().execute("update.show();");
            return false;
        }

        return true;
    }
        
    

    public StreamedContent getUpdateFile(){
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File("/usr/msa/cdp/client/AdvancedMirrorAgentSetup_Patch.zip"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        StreamedContent agentFile = new DefaultStreamedContent(stream, "", "AdvancedMirrorAgentSetup_Patch.zip");
        InterfaceFactory.getMBAInterfaceInstance().downloadAgentLog("AdvancedMirrorAgentSetup_Patch.zip", true);
        return agentFile;
    }

    private boolean setSQLServerAgentAction(AgentSQLServerBean agentSQL) {
        clientSock = new MarsClientSocket();
        if (!clientSock.Connect(host, port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("connectFailed"), res.get("connectFailed")));
            return false;
        }
        BasicQueryAction action = new BasicQueryAction("9601", "Add host", clientSock);
        action.addParameter("Group", this.guid);
        action.addParameter("MSAIP", agentSQL.MSAIP);
        action.addParameter("MSAPort", agentSQL.MSAPort);
        XMLConstructor oracle = new XMLConstructor("Agent");
        oracle.addNode("AgentID", AgentBean.AGENT_SQLSERVER);
        if (agentSQL.mode == AgentBean.MODE_MIX) {
            XMLConstructor parameterName = new XMLConstructor("Parameter");
            parameterName.addNode("Key", "UserName");
            parameterName.addNode("Value", agentSQL.userName);
            oracle.addNode(parameterName);

            XMLConstructor parameterPassword = new XMLConstructor("Parameter");
            parameterPassword.addNode("Key", "Password");
            parameterPassword.addNode("Value", new String(MyEncryp.Encode64(agentSQL.password.toCharArray())));
            oracle.addNode(parameterPassword);
        }

        XMLConstructor parameterIP = new XMLConstructor("Parameter");
        parameterIP.addNode("Key", "IP");
        parameterIP.addNode("Value", agentSQL.ip);
        oracle.addNode(parameterIP);

        XMLConstructor parameterPort = new XMLConstructor("Parameter");
        parameterPort.addNode("Key", "Instance");
        parameterPort.addNode("Value", agentSQL.instanceName);
        oracle.addNode(parameterPort);

        XMLConstructor parameterInstance = new XMLConstructor("Parameter");
        parameterInstance.addNode("Key", "Database");
        parameterInstance.addNode("Value", agentSQL.database);
        oracle.addNode(parameterInstance);

        XMLConstructor parameterInterval = new XMLConstructor("Parameter");
        parameterInterval.addNode("Key", "Interval");
        parameterInterval.addNode("Value", agentSQL.interval);
        oracle.addNode(parameterInterval);

        XMLConstructor mode = new XMLConstructor("Parameter");
        mode.addNode("Key", "Mode");
        mode.addNode("Value", agentSQL.mode);
        oracle.addNode(mode);

        action.addParameter(oracle);
        if (!action.queryAndGetBooleanResult()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return false;
        }

        return true;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSnapNum() {
        if (snapNum == null || snapNum.equals("0")) {
            return "256";
        }
        return snapNum;
    }

    public void setSnapNum(String snapNum) {
        this.snapNum = snapNum;
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

    public boolean isRECORD() {
        return RECORD;
    }

    public void setRECORD(boolean RECORD) {
        this.RECORD = RECORD;
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
        if (dInterval.equals("0")) {
            dInterval = "1";
        }
        return dInterval;
    }

    public void setInterval(String interval) {
        this.dInterval = interval;
    }

    public String getSInterval() {
        if (sInterval == null || sInterval.equals("0")) {
            return "1";
        }
        return sInterval;
    }

    public void setSInterval(String sInterval) {
        this.sInterval = sInterval;
    }

    public String getHost() {
        if (host == null || host.equals("null")) {
            return "";
        }
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

    public boolean exist() {
        if (agent == null) {
            return false;
        }
        if (this.agent.getListDB() == null || this.agent.getListDB().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public String getts() {
        if (group == null) {
            return "0GB";
        }
        return String.valueOf(group.getTotalDiskSize() / (1024L * 1024L * 1024L)) + "GB";
    }

    public void setLog() {

        if (this.tillFull) {
            keepTime = "100000";
        }
        long kt = Long.parseLong(keepTime);
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        boolean success = cdp.setLogPolicy(path, guid,
                Long.parseLong(size) * 1024L * 1024L * 1024L, Integer.parseInt(keepTime), Integer.valueOf(this.fullOption));
        if (!success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
        }
    }

    public boolean checkFormat() {
        if (!DGUtility.checkNum(size, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format0"), global.get("error_mark")));
            return false;
        }
        if (!DGUtility.checkNum(keepTime, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "format1"), global.get("error_mark")));
            return false;
        }
        if (this.isDefault) {
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
        if (this.size != null && this.size.equals("0")) {
            this.size = "1";
        }
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getKeepTime() {
        return keepTime;
    }

    public void setKeepTime(String keepTime) {
        this.keepTime = keepTime;
    }

    public String getFullOption() {
        return fullOption;
    }

    public void setFullOption(String fullOption) {
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

    public String getGuid() {
        return guid;
    }

    public String cancel() {
        return "cdp.xhtml?faces-redirect=true";
    }
    private boolean disableAdd = false;

    public boolean isDisableAdd() {
        if (this.listDB != null && this.listDB.size() > 0) {
            disableAdd = true;
        } else {
            disableAdd = false;
        }
        return disableAdd;
    }

    public void setDisableAdd(boolean disableAdd) {
        this.disableAdd = disableAdd;
    }
}
