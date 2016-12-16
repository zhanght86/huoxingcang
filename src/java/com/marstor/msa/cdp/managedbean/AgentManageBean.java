/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.AgentBean;
import static com.marstor.msa.cdp.model.AgentBean.AGENT_ORACLE;
import static com.marstor.msa.cdp.model.AgentBean.AGENT_SQLSERVER;
import com.marstor.msa.cdp.model.AgentOracleBean;
import com.marstor.msa.cdp.model.AgentSQLServerBean;
import com.marstor.msa.cdp.bean.CdpAgent;
import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.DataBase;
import com.marstor.msa.cdp.socket.MarsClientSocket;
import com.marstor.msa.cdp.socket.action.GetAgentsAction;
import com.marstor.msa.cdp.socket.action.MyBasicQueryAction;
import com.marstor.msa.cdp.util.AgentUtility;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import java.util.Vector;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "agentBean")
@ViewScoped
public class AgentManageBean {

    private String interval;
    private String host = "192.168.1.87";
    private int port = 40001;
    private com.marstor.msa.cdp.model.AgentBean[] agents;
    private com.marstor.msa.cdp.model.AgentBean selected;
    private boolean isDefault = false;
    private MarsClientSocket clientSock;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.agent";
    private String guid;
    private CdpDiskGroupInfo group;
    public CdpAgent agent;
    private int formmerID = 100;
    private int latterID;
    public DataBase db;

    public AgentManageBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        guid = request.getParameter("guid");
        initData();
    }

    public final void initData() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        group = cdp.getDiskGroupInfo(guid);
        if (group.getCdpAgent() == null || group.getCdpAgent().getListDB().isEmpty()) {
            host = "";
        } else {
            this.host = group.getCdpAgent().getListDB().get(0).getIp();
        }

        if (host == null) {
            host = "";
        }

        if (group.getlSnapshotInterval() != 0) {
            this.isDefault = true;
            this.interval = String.valueOf(group.getlSnapshotInterval() / 1000);
        }

        agent = group.getCdpAgent();
        if( group.getCdpAgent() == null || group.getCdpAgent().getListDB() == null
                || group.getCdpAgent().getListDB().isEmpty()){
            return;
        }
        this.port = group.getCdpAgent().getListDB().get(0).getPort();
        if (port == 0) {
            port = 1100;
        }
    }

    public void connect() {
        if (host.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n0"), global.get("error_mark")));
            return;
        }
        if (!DGUtility.checkIP(host)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f0"), global.get("error_mark")));
            return;
        }

        if (String.valueOf(port).equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n1"), global.get("error_mark")));
            return;
        }
        if (!AgentUtility.checkNum(String.valueOf(port), false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f1"), global.get("error_mark")));
            return;
        }
        
        if (!AgentUtility.checkPort(String.valueOf(port))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get( "portRange"), global.get("error_mark")));
            return;
        }

        clientSock = new MarsClientSocket();
        if (!clientSock.Connect(host, port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "connError"), global.get("error_mark")));
            return;
        }

        GetAgentsAction action = new GetAgentsAction(clientSock, this.guid);
        this.agents = action.getAgents();
        clientSock.Close();

        if (null != this.agents) {
            for (AgentBean a : agents) {
                if (a.state == 1) {
                    this.formmerID = a.agentID;
                    break;
                }
            }
        }
    }

    public String setting() {
        String param = "guid=" + this.guid + "&ip=" + host;
        if (this.selected.agentID == com.marstor.msa.cdp.model.AgentBean.AGENT_SQLSERVER) {
            return "sql?faces-redirect=true&amp;" + param;
        }

        if (this.selected.agentID == com.marstor.msa.cdp.model.AgentBean.AGENT_ORACLE) {
            return "orcl?faces-redirect=true&amp;" + param;
        }

        return "";
    }

    public String save() {
        if (this.isDefault) {
            if (!AgentUtility.checkNum(this.interval, false)
                    || Integer.parseInt(this.interval) > 86400) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sf0"), global.get("error_mark")));
                return null;
            }

            if (!host.equals("")) {
                clientSock = new MarsClientSocket();
                clientSock.Connect(host, port);

                MyBasicQueryAction stopAgent = new MyBasicQueryAction("9611", "Turn Off The Agent", clientSock);
                stopAgent.addParameter("Group", guid);
                stopAgent.doAction();
            }

//            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
//            boolean success = cdp.setDefaultAgent(guid, Long.valueOf(interval), host, port, this.group.isCDPStarted() ? 1 : 0);
//            if (!success) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sfail0"), global.get("error_mark")));
//                return null;
//            }
        } else {
//            MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
//            boolean success = cdp.setDefaultAgent(guid, Long.valueOf(interval), host, port, this.group.isCDPStarted() ? 1 : 0);
//            if (!success) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sfail1"), global.get("error_mark")));
//                return null;
//            }

            if (agents.length != 0) {
                this.latterID = 100;
                for (int i = 0; i < agents.length; i++) {
                    if (agents[i].state != 1) {
                        if (agents[i].agentID == this.formmerID) {
                            if (!clientSock.Connect(host, port)) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sfail2"), global.get("error_mark")));
                                return null;
                            }
                            MyBasicQueryAction act = new MyBasicQueryAction("9607", "Turn Off The Agent", this.clientSock);
                            act.addParameter("Group", this.group.getDiskGroupGuid());
                            act.addParameter("AgentID", this.formmerID);

                            if (!act.queryAndGetBooleanResult()) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sfail3"), global.get("error_mark")));
                                return null;
                            }
                        }
                    } else {
                        this.latterID = agents[i].agentID;
                        if (this.latterID != this.formmerID && this.formmerID != 100) {
                            if (!clientSock.Connect(host, port)) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sfail2"), global.get("error_mark")));
                                return null;
                            }
                            MyBasicQueryAction act = new MyBasicQueryAction("9607", "Turn Off The Agent", this.clientSock);
                            act.addParameter("Group", this.group.getDiskGroupGuid());
                            act.addParameter("AgentID", this.formmerID);

                            if (!act.queryAndGetBooleanResult()) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sfail3"), global.get("error_mark")));
                                return null;
                            }
                        }
                        switch (agents[i].agentID) {
                            case AgentBean.AGENT_ORACLE:
                                if (((AgentOracleBean) agents[i]).MSAIP.equals("")) {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sn0"), global.get("error_mark")));
                                    String param = "guid=" + this.guid;
                                    return "orcl?faces-redirect=true&amp;" + param;
                                }
                                break;
                            case AgentBean.AGENT_SQLSERVER:
                                if (((AgentSQLServerBean) agents[i]).MSAIP.equals("")) {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sn0"), global.get("error_mark")));
                                    String param = "guid=" + this.guid;
                                    return "sql?faces-redirect=true&amp;" + param;
                                }
                                break;
                        }
                        if (!clientSock.Connect(host, port)) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sfail2"), global.get("error_mark")));
                            return null;
                        }
                        MyBasicQueryAction act = new MyBasicQueryAction("9605", "Turn On The Agent", this.clientSock);
                        act.addParameter("Group", this.group.getDiskGroupGuid());
                        act.addParameter("AgentID", this.latterID);

                        if (!act.queryAndGetBooleanResult()) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "sfail4"), global.get("error_mark")));
                            return null;
                        }
                    }

                }
                this.formmerID = this.latterID;
            }
        }
        return "dgs?faces-redirect=true";
    }

    public void changeState() {
        if (this.selected.state == 0) {
            this.selected.state = 1;
        } else {
            this.selected.state = 0;
        }
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getIp() {
        return host;
    }

    public void setIp(String ip) {
        this.host = ip;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public com.marstor.msa.cdp.model.AgentBean[] getAgents() {
        return agents;
    }

    public void setAgents(com.marstor.msa.cdp.model.AgentBean[] agents) {
        this.agents = agents;
    }

    public com.marstor.msa.cdp.model.AgentBean getSelected() {
        return selected;
    }

    public void setSelected(com.marstor.msa.cdp.model.AgentBean selected) {
        this.selected = selected;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
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
}
