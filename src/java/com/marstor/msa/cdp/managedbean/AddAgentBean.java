/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.AgentBean;
import com.marstor.msa.cdp.model.AgentOracleBean;
import com.marstor.msa.cdp.model.AgentSQLServerBean;
import com.marstor.msa.cdp.bean.DataBase;
import com.marstor.msa.cdp.socket.BasicQueryAction;
import com.marstor.msa.cdp.socket.MarsClientSocket;
import com.marstor.msa.cdp.socket.action.GetAgentsAction;
import com.marstor.msa.cdp.util.AgentUtility;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyEncryp;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.xml.XMLConstructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class AddAgentBean {

    private String interval;
    private String ip = "192.168.1.87";
    private int port = 40001;
    private AgentBean agent = new AgentBean();
    private AgentOracleBean agentOracle = new AgentOracleBean();
    private AgentSQLServerBean agentSQL = new AgentSQLServerBean();
    private String preAgentOracl;
    private String preAgentSQL;
    private boolean isDefault = false;
    private MarsClientSocket clientSock;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.add_agent";
    private String guid;
    private String[] ips;
    public boolean orcl, sql;
    public int type = 1;
    public boolean isEdit = false;
    public String title;
    private String path;
    private boolean disable = false;
    private List<AgentOracleBean> oracleAgents = new ArrayList<AgentOracleBean>();
    private List<AgentSQLServerBean> sqlseverAgents = new ArrayList<AgentSQLServerBean>();
    private String register;
    private String used;
    private String size;
    private String keepTime;
    private boolean tillFull = false;
    private String fullOption;
    private String host;

    public AddAgentBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        path = request.getParameter("path");
        guid = request.getParameter("guid");
        this.register = request.getParameter("register");
        this.used = request.getParameter("used");
        this.size = request.getParameter("rememberSize");
        this.keepTime = request.getParameter("rememberKeepTime");
        this.tillFull = Boolean.valueOf(request.getParameter("rememberTillFull"));
        this.fullOption = request.getParameter("rememberFullOption");
        this.host = request.getParameter("rememberHost");
        this.port = Integer.valueOf(request.getParameter("rememberPort"));
        int para = 0;
        if (request.getParameter("type") != null) {
            para = Integer.parseInt(request.getParameter("type"));
        }

        if (request.getParameterValues("msaIP") != null) {
            int i = 0;
            List<String> dbaNames;
            String[] msaIPs = request.getParameterValues("msaIP");
            String[] msaPorts = request.getParameterValues("msaPort");
            String[] agentIDs = request.getParameterValues("agentID");
            String[] pwds = request.getParameterValues("pwd");
            String[] IPs = request.getParameterValues("ip");
            String[] ports = request.getParameterValues("port");
            String[] instances = request.getParameterValues("instance");
            String[] intervals = request.getParameterValues("interval");
            if (request.getParameterValues("dbaName") != null) {
                dbaNames = Arrays.asList(request.getParameterValues("dbaName"));
                String dba = "";
                if (para == AgentBean.AGENT_ORACLE) {
                    dba = request.getParameter("oracle");
                }
                for (; i < dbaNames.size(); i++) {
                    AgentOracleBean o = new AgentOracleBean();
                    o.MSAIP = msaIPs[i];
                    o.MSAPort = msaPorts[i];
                    o.agentID = Integer.parseInt(agentIDs[i]);
                    o.dbaName = dbaNames.get(i);
                    o.password = pwds[i];
                    o.ip = IPs[i];
                    o.port = ports[i];
                    o.instanceName = instances[i];
                    o.interval = intervals[i];
                    this.oracleAgents.add(o);
                }
            }
            List<String> userNames = null;
            if (request.getParameterValues("db") != null) {
                if (request.getParameterValues("userName") != null) {
                    userNames = Arrays.asList(request.getParameterValues("userName"));
                }

                String[] dbs = request.getParameterValues("db");
                String[] modes = request.getParameterValues("mode");
                String db;
                if (para == AgentBean.AGENT_SQLSERVER) {
                    db = request.getParameter("sql");
                }
                for (int j = 0; j < dbs.length; j++, i++) {
                    AgentSQLServerBean o = new AgentSQLServerBean();
                    o.MSAIP = msaIPs[i];
                    o.MSAPort = msaPorts[i];
                    o.agentID = Integer.parseInt(agentIDs[i]);
                    if (userNames != null) {
                        o.userName = userNames.get(j);
                    }
                    if (pwds != null) {
                        o.password = pwds[i];
                    }

                    o.ip = IPs[i];
                    o.port = Integer.parseInt(ports[i]);
                    o.instanceName = instances[i];
                    o.interval = intervals[i];
                    o.database = dbs[j];
                    o.mode = Integer.parseInt(modes[j]);
                    this.sqlseverAgents.add(o);
                }
            }
        }

        if (para == 0) {
            orcl = true;
            title = res.get(basename, "title0");
        } else {
            title = res.get(basename, "title1");
            this.isEdit = true;
            type = para;
            switchPanel();
            switch (type) {
                case AgentBean.AGENT_ORACLE:
                    String dba = request.getParameter("oracle");
                    AgentOracleBean ob = new AgentOracleBean();
                    for (AgentOracleBean o : this.oracleAgents) {
                        if (o.instanceName.equals(dba)) {
                            this.agent = o;
                            this.agentOracle = o;
                            StringBuilder sb = new StringBuilder();
                            sb.append("&msaIP=");
                            sb.append(o.MSAIP);
                            sb.append("&msaPort=");
                            sb.append(o.MSAPort);
                            sb.append("&agentID=");
                            sb.append(o.agentID);
                            sb.append("&dbaName=");
                            sb.append(o.dbaName);
                            sb.append("&pwd=");
                            sb.append(o.password);
                            sb.append("&ip=");
                            sb.append(o.ip);
                            sb.append("&port=");
                            sb.append(o.port);
                            sb.append("&instance=");
                            sb.append(o.instanceName);
                            sb.append("&interval=");
                            sb.append(o.interval);
                            this.preAgentOracl = sb.toString();
                            ob = o;
                            break;
                        }
                    }
                    this.oracleAgents.remove(ob);
                    break;
                case AgentBean.AGENT_SQLSERVER:
                    String db = request.getParameter("sql");
                    AgentSQLServerBean ssb = new AgentSQLServerBean();
                    for (AgentSQLServerBean sq : this.sqlseverAgents) {
                        if (sq.instanceName.equals(db)) {
                            this.agent = sq;
                            this.agentSQL = sq;
                            StringBuilder sb = new StringBuilder();
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
                            this.preAgentSQL = sb.toString();
                            this.setDis();
                            ssb = sq;
                            break;
                        }
                    }
                    this.sqlseverAgents.remove(ssb);
                    break;
            }
        }
        getData();
    }

    public final void switchPanel() {
        switch (type) {
            case AgentBean.AGENT_ORACLE:
                orcl = true;
                sql = false;
                break;
            case AgentBean.AGENT_SQLSERVER:
                orcl = false;
                sql = true;
                break;
        }
    }

    public final void setDefaultOracle() {
        clientSock = new MarsClientSocket();
        if (!clientSock.Connect(ip, port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "connError"), global.get("error_mark")));
            return;
        }

        GetAgentsAction action = new GetAgentsAction(clientSock, this.guid);
        AgentBean[] agents = action.getAgents();
        clientSock.Close();

        for (AgentBean a : agents) {
            if (a.agentID == AgentBean.AGENT_ORACLE) {
                agent = a;
                agentOracle = (AgentOracleBean) a;
                break;
            }
        }
    }

    public AgentBean getAgent() {
        return agent;
    }

    public void setAgent(AgentBean agent) {
        this.agent = agent;
    }

    public final void getData() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        this.ips = scsi.getAllIP();
    }

    public String settingOracle() {
        agentOracle.MSAPort = agent.MSAPort;
        agentOracle.MSAIP = agent.MSAIP;
        if (agentOracle.MSAPort == null || agentOracle.MSAPort.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n0"), global.get("error_mark")));
            return null;
        }
        if (!DGUtility.checkNum(agentOracle.MSAPort)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get("portFormat"), global.get("error_mark")));
            return null;
        }
        
        if (!AgentUtility.checkPort(agentOracle.MSAPort)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get( "portRange"), global.get("error_mark")));
            return null;
        }

        if (agentOracle.ip == null || agentOracle.ip.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n1"), global.get("error_mark")));
            return null;
        }
        if (!DGUtility.checkIP(agentOracle.ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f1"), global.get("error_mark")));
            return null;
        }

        if (agentOracle.port == null || agentOracle.port.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n2"), global.get("error_mark")));
            return null;
        }
        if (!AgentUtility.checkNum(agentOracle.port, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f2"), global.get("error_mark")));
            return null;
        }
        if (!AgentUtility.checkPort(agentOracle.port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get( "portRange"), global.get("error_mark")));
            return null;
        }

        if (agentOracle.instanceName == null || agentOracle.instanceName.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n3"), global.get("error_mark")));
            return null;
        }

        if (agentOracle.dbaName == null || agentOracle.dbaName.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n4"), global.get("error_mark")));
            return null;
        }

        if (agentOracle.password == null || agentOracle.password.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n5"), global.get("error_mark")));
            return null;
        }

        if (agentOracle.interval == null || agentOracle.interval.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n6"), global.get("error_mark")));
            return null;
        }
        if (!DGUtility.checkNum(agentOracle.interval, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f6"), global.get("error_mark")));
            return null;
        }
        return addOracleAgentAction();
    }

    public String cancel() {
        StringBuilder sb = new StringBuilder();
        sb.append("type=2");
        sb.append("&guid=");
        sb.append(this.guid);
        sb.append("&path=");
        sb.append(this.path);
        sb.append("&fake=1");
        sb.append("&record=1");
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
        if (this.isEdit) {
            if (this.type == AgentBean.AGENT_ORACLE) {
                sb.append(this.preAgentOracl);
            } else {
                sb.append(this.preAgentSQL);
            }
        }
        if (this.oracleAgents != null && !this.oracleAgents.isEmpty()) {
            for (AgentOracleBean o : this.oracleAgents) {
                sb.append("&msaIP=");
                sb.append(o.MSAIP);
                sb.append("&msaPort=");
                sb.append(o.MSAPort);
                sb.append("&agentID=");
                sb.append(o.agentID);
                sb.append("&dbaName=");
                sb.append(o.dbaName);
                sb.append("&pwd=");
                sb.append(o.password);
                sb.append("&ip=");
                sb.append(o.ip);
                sb.append("&port=");
                sb.append(o.port);
                sb.append("&instance=");
                sb.append(o.instanceName);
                sb.append("&interval=");
                sb.append(o.interval);
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

        return "cdp_level?faces-redirect=true&amp;" + sb.toString();
    }

    public String addOracleAgentAction() {
        StringBuilder sb = new StringBuilder();
        sb.append("type=2");
        sb.append("&guid=");
        sb.append(this.guid);
        sb.append("&path=");
        sb.append(this.path);
        sb.append("&fake=1");
        sb.append("&record=1");
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

        this.oracleAgents.add(agentOracle);
        if (this.oracleAgents != null && !this.oracleAgents.isEmpty()) {
            for (AgentOracleBean o : this.oracleAgents) {
                sb.append("&msaIP=");
                sb.append(o.MSAIP);
                sb.append("&msaPort=");
                sb.append(o.MSAPort);
                sb.append("&agentID=");
                sb.append(o.agentID);
                sb.append("&dbaName=");
                sb.append(o.dbaName);
                sb.append("&pwd=");
                sb.append(o.password);
                sb.append("&ip=");
                sb.append(o.ip);
                sb.append("&port=");
                sb.append(o.port);
                sb.append("&instance=");
                sb.append(o.instanceName);
                sb.append("&interval=");
                sb.append(o.interval);
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

        System.out.println(sb.toString());

        return "cdp_level?faces-redirect=true&amp;" + sb.toString();
    }

    public final void setDefaultSQL() {
        clientSock = new MarsClientSocket();
        if (!clientSock.Connect(ip, port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "connError"), global.get("error_mark")));
            return;
        }

        GetAgentsAction action = new GetAgentsAction(clientSock, this.guid);
        AgentBean[] agents = action.getAgents();
        clientSock.Close();

        for (AgentBean a : agents) {
            if (a.agentID == AgentBean.AGENT_SQLSERVER) {
                agent = a;
                agentSQL = (AgentSQLServerBean) a;
                break;
            }
        }
    }

    public String settingSQL() {
        agentSQL.MSAPort = agent.MSAPort;
        agentSQL.MSAIP = agent.MSAIP;
        if (agentSQL.MSAPort == null || agentSQL.MSAPort.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n0"), global.get("error_mark")));
            return null;
        }
        if (!DGUtility.checkNum(agentSQL.MSAPort)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get("portFormat"), global.get("error_mark")));
            return null;
        }
        
        if (!AgentUtility.checkPort(agentSQL.MSAPort)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get( "portRange"), global.get("error_mark")));
            return null;
        }

        if (agentSQL.ip == null || agentSQL.ip.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n1"), global.get("error_mark")));
            return null;
        }
        if (!DGUtility.checkIP(agentSQL.ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f1"), global.get("error_mark")));
            return null;
        }

        if (agentSQL.instanceName == null || agentSQL.instanceName.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n3"), global.get("error_mark")));
            return null;
        }
        if (!AgentUtility.checkInstanceName(agentSQL.instanceName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f3"), global.get("error_mark")));
            return null;
        }

        if (agentSQL.database == null || agentSQL.database.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "ns3"), global.get("error_mark")));
            return null;
        }

        if (!AgentUtility.checkDatabaseName(agentSQL.database)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fs3"), global.get("error_mark")));
            return null;
        }

        if (!disable) {
            if (agentSQL.userName == null || agentSQL.userName.equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n4"), global.get("error_mark")));
                return null;
            }

            if (agentSQL.password == null || agentSQL.password.equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n5"), global.get("error_mark")));
                return null;
            }
        } else {
            agentSQL.userName = "";
            agentSQL.password = "";
        }


        if (agentSQL.interval == null || agentSQL.interval.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n6"), global.get("error_mark")));
            return null;
        }
        if (!DGUtility.checkNum(agentSQL.interval, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f6"), global.get("error_mark")));
            return null;
        }
        return addSQLServerAgentAction();
    }

    public String addSQLServerAgentAction() {
        StringBuilder sb = new StringBuilder();
        sb.append("type=2");
        sb.append("&guid=");
        sb.append(this.guid);
        sb.append("&path=");
        sb.append(this.path);
        sb.append("&fake=1");
        sb.append("&record=1");
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
        this.sqlseverAgents.add(agentSQL);
        if (this.oracleAgents != null && !this.oracleAgents.isEmpty()) {
            for (AgentOracleBean o : this.oracleAgents) {
                sb.append("&msaIP=");
                sb.append(o.MSAIP);
                sb.append("&msaPort=");
                sb.append(o.MSAPort);
                sb.append("&agentID=");
                sb.append(o.agentID);
                sb.append("&dbaName=");
                sb.append(o.dbaName);
                sb.append("&pwd=");
                sb.append(o.password);
                sb.append("&ip=");
                sb.append(o.ip);
                sb.append("&port=");
                sb.append(o.port);
                sb.append("&instance=");
                sb.append(o.instanceName);
                sb.append("&interval=");
                sb.append(o.interval);
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

        System.out.println(sb.toString());

        return "cdp_level?faces-redirect=true&" + sb.toString();
    }

    public final void setDis() {
        if (this.agentSQL.mode == AgentBean.MODE_WINDOWS) {
            this.agentSQL.userName = "";
            this.agentSQL.password = "";
            this.disable = true;
        } else {
            this.disable = false;
        }
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public AgentOracleBean getAgentOracle() {
        return agentOracle;
    }

    public void setAgentOracle(AgentOracleBean agentOracle) {
        this.agentOracle = agentOracle;
    }

    public String[] getIps() {
        return ips;
    }

    public void setIps(String[] ips) {
        this.ips = ips;
    }

    public boolean isOracle() {
        return orcl;
    }

    public void setOracle(boolean oracle) {
        this.orcl = oracle;
    }

    public boolean isSql() {
        return sql;
    }

    public void setSql(boolean sql) {
        this.sql = sql;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGuid() {
        return guid;
    }

    public String getPath() {
        return path;
    }

    public AgentSQLServerBean getAgentSQL() {
        return agentSQL;
    }

    public void setAgentSQL(AgentSQLServerBean agentSQL) {
        this.agentSQL = agentSQL;
    }

    public boolean isOrcl() {
        return orcl;
    }

    public void setOrcl(boolean orcl) {
        this.orcl = orcl;
    }
}
