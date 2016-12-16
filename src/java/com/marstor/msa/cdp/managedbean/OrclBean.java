/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.AgentBean;
import com.marstor.msa.cdp.model.AgentOracleBean;
import com.marstor.msa.cdp.model.AgentSQLServerBean;
import com.marstor.msa.cdp.socket.BasicQueryAction;
import com.marstor.msa.cdp.socket.MarsClientSocket;
import com.marstor.msa.cdp.socket.action.GetAgentsAction;
import com.marstor.msa.cdp.util.AgentUtility;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyEncryp;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.xml.XMLConstructor;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "orclBean")
@RequestScoped
public class OrclBean {

    private String interval;
    private String ip = "192.168.1.87";
    private int port = 40001;
    private AgentOracleBean agent;
    private boolean isDefault = false;
    private MarsClientSocket clientSock;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.orcl";
    private String guid;
    private String[] ips;

    public OrclBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        guid = request.getParameter("guid");
        ip = request.getParameter("ip");
        getData();
    }

    public final void setDefault() {
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
                agent = (AgentOracleBean) a;
                break;
            }
        }
    }

    public final void getData() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        this.ips = scsi.getAllIP();
    }

    public String setting() {
        if (agent.MSAPort == null || agent.MSAPort.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n0"), global.get("error_mark")));
            return null;
        }
        if (!DGUtility.checkNum(agent.MSAPort)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get("portFormat"), global.get("error_mark")));
            return null;
        }

        if (!AgentUtility.checkPort(agent.MSAPort)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get("portRange"), global.get("error_mark")));
            return null;
        }

        if (agent.ip == null || agent.ip.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n1"), global.get("error_mark")));
            return null;
        }
        if (!DGUtility.checkIP(agent.ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f1"), global.get("error_mark")));
            return null;
        }

        if (agent.port == null || agent.port.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n2"), global.get("error_mark")));
            return null;
        }
        if (!AgentUtility.checkNum(agent.port, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f2"), global.get("error_mark")));
            return null;
        }
        if (!AgentUtility.checkPort(agent.port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get("portRange"), global.get("error_mark")));
            return null;
        }

        if (agent.instanceName == null || agent.instanceName.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n3"), global.get("error_mark")));
            return null;
        }

        if (agent.password == null || agent.password.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n5"), global.get("error_mark")));
            return null;
        }

        if (!AgentUtility.checkName(agent.password)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f5"), global.get("error_mark")));
            return null;
        }

        if (agent.interval == null || agent.interval.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n6"), global.get("error_mark")));
            return null;
        }
        if (!DGUtility.checkNum(agent.interval, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f6"), global.get("error_mark")));
            return null;
        }
        return setOracleAgentAction();
    }

    private String setOracleAgentAction() {
        clientSock = new MarsClientSocket();
        clientSock.Connect(ip, port);

        BasicQueryAction action = new BasicQueryAction("9601", "Add host", clientSock);
        action.addParameter("Group", this.guid);
        action.addParameter("MSAIP", this.agent.MSAIP);
        action.addParameter("MSAPort", this.agent.MSAPort);
        XMLConstructor oracle = new XMLConstructor("Agent");
        oracle.addNode("AgentID", AgentBean.AGENT_ORACLE);
        oracle.addNode("Describe", this.agent.instanceName);
        XMLConstructor parameterName = new XMLConstructor("Parameter");
        parameterName.addNode("Key", "DBAName");
        parameterName.addNode("Value", this.agent.dbaName);
        oracle.addNode(parameterName);

        XMLConstructor parameterPassword = new XMLConstructor("Parameter");
        parameterPassword.addNode("Key", "Password");
        parameterPassword.addNode("Value", this.agent.password);
        oracle.addNode(parameterPassword);

        XMLConstructor parameterIP = new XMLConstructor("Parameter");
        parameterIP.addNode("Key", "IP");
        parameterIP.addNode("Value", this.agent.ip);
        oracle.addNode(parameterIP);

        XMLConstructor parameterPort = new XMLConstructor("Parameter");
        parameterPort.addNode("Key", "Port");
        parameterPort.addNode("Value", this.agent.port);
        oracle.addNode(parameterPort);

        XMLConstructor parameterInstance = new XMLConstructor("Parameter");
        parameterInstance.addNode("Key", "Instance");
        parameterInstance.addNode("Value", this.agent.instanceName);
        oracle.addNode(parameterInstance);

        XMLConstructor parameterInterval = new XMLConstructor("Parameter");
        parameterInterval.addNode("Key", "Interval");
        parameterInterval.addNode("Value", this.agent.interval);
        oracle.addNode(parameterInterval);

        action.addParameter(oracle);
        if (!action.queryAndGetBooleanResult()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return null;
        }
        return "cdp_level?faces-redirect=true&amp;guid=" + this.guid + "&amp;";
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

    public AgentOracleBean getAgent() {
        return agent;
    }

    public void setAgent(AgentOracleBean agent) {
        this.agent = agent;
    }

    public String[] getIps() {
        return ips;
    }

    public void setIps(String[] ips) {
        this.ips = ips;
    }
}
