/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.AgentBean;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "sqlBean")
@ViewScoped
public class SQLBean {

    private String interval;
    private String ip = "192.168.1.87";
    private int port = 40001;
    private AgentSQLServerBean agentSQL = new AgentSQLServerBean();
    private boolean isDefault = false;
    private MarsClientSocket clientSock;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.sql";
    private String guid;
    private String[] ips;
    private boolean disable = false;

    public SQLBean() {
//        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
//        guid = request.getParameter("guid");
//        ip = request.getParameter("ip");
//        getData();
        this.agentSQL.setMode(1);
    }

    public final void getData() {
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        this.ips = scsi.getAllIP();
    }
    
    public final void setDefaultSQL(){
        clientSock = new MarsClientSocket();
        if (!clientSock.Connect(ip, port)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "connError"), global.get("error_mark")));
            return;
        }

        GetAgentsAction action = new GetAgentsAction(clientSock, this.guid);
        AgentBean[] agents = action.getAgents();
        clientSock.Close();
        
        for(AgentBean a : agents){
            if(a.agentID == AgentBean.AGENT_SQLSERVER){
                agentSQL = (AgentSQLServerBean)a;
                break;
            }
        }
    }

    public void settingSQL() {
        if (agentSQL.MSAPort == null || agentSQL.MSAPort.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null0"), global.get("error_mark")));
            return;
        }
        if (!DGUtility.checkNum(agentSQL.MSAPort)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get("portFormat"), global.get("error_mark")));
            return;
        }
        
        if (!AgentUtility.checkPort(agentSQL.MSAPort)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, global.get( "portRange"), global.get("error_mark")));
            return;
        }

        if (agentSQL.ip == null || agentSQL.ip.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n1"), global.get("error_mark")));
            return;
        }
        if (!DGUtility.checkIP(agentSQL.ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f1"), global.get("error_mark")));
            return;
        }

        if (agentSQL.instanceName == null || agentSQL.instanceName.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n2"), global.get("error_mark")));
            return;
        }
        if (!AgentUtility.checkInstanceName(agentSQL.instanceName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f2"), global.get("error_mark")));
            return;
        }

        if (agentSQL.database == null || agentSQL.database.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n3"), global.get("error_mark")));
            return;
        }

        if (!AgentUtility.checkDatabaseName(agentSQL.database)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f3"), global.get("error_mark")));
            return;
        }

        if (!disable) {
            if (agentSQL.userName == null || agentSQL.userName.equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n4"), global.get("error_mark")));
                return;
            }

            if (agentSQL.password == null || agentSQL.password.equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n5"), global.get("error_mark")));
                return;
            }
        }


        if (agentSQL.interval == null || agentSQL.interval.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "n6"), global.get("error_mark")));
            return;
        }
        if (!DGUtility.checkNum(agentSQL.interval, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f6"), global.get("error_mark")));
            return;
        }
        setSQLServerAgentAction();
    }

    public void setDis() {
        if (this.agentSQL.mode == 1) {
            this.agentSQL.userName = "";
            this.agentSQL.password = "";
            this.disable = false;
        } else {
            this.disable = true;
        }
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    private void setSQLServerAgentAction() {
        clientSock = new MarsClientSocket();
        clientSock.Connect(ip, port);
        BasicQueryAction action = new BasicQueryAction("9601", "Add host", clientSock);

        action.addParameter("Group", this.guid);
        action.addParameter("MSAIP", this.agentSQL.MSAIP);
        action.addParameter("MSAPort", this.agentSQL.MSAPort);
        XMLConstructor oracle = new XMLConstructor("Agent");
        oracle.addNode("AgentID", AgentBean.AGENT_SQLSERVER);
        if (this.agentSQL.mode == 2) {
            XMLConstructor parameterName = new XMLConstructor("Parameter");
            parameterName.addNode("Key", "UserName");
            parameterName.addNode("Value", this.agentSQL.userName);
            oracle.addNode(parameterName);

            XMLConstructor parameterPassword = new XMLConstructor("Parameter");
            parameterPassword.addNode("Key", "Password");
            parameterPassword.addNode("Value", new String(MyEncryp.Encode64(this.agentSQL.password.toCharArray())));
            oracle.addNode(parameterPassword);
        }

        XMLConstructor parameterIP = new XMLConstructor("Parameter");
        parameterIP.addNode("Key", "IP");
        parameterIP.addNode("Value", this.agentSQL.ip);
        oracle.addNode(parameterIP);

        XMLConstructor parameterPort = new XMLConstructor("Parameter");
        parameterPort.addNode("Key", "Instance");
        parameterPort.addNode("Value", this.agentSQL.instanceName);
        oracle.addNode(parameterPort);

        XMLConstructor parameterInstance = new XMLConstructor("Parameter");
        parameterInstance.addNode("Key", "Database");
        parameterInstance.addNode("Value", this.agentSQL.database);
        oracle.addNode(parameterInstance);

        XMLConstructor parameterInterval = new XMLConstructor("Parameter");
        parameterInterval.addNode("Key", "Interval");
        parameterInterval.addNode("Value", this.agentSQL.interval);
        oracle.addNode(parameterInterval);

        XMLConstructor mode = new XMLConstructor("Parameter");
        mode.addNode("Key", "Mode");
        mode.addNode("Value", this.agentSQL.mode);
        oracle.addNode(mode);

        action.addParameter(oracle);
        if (!action.queryAndGetBooleanResult()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
        }

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

    public AgentSQLServerBean getAgent() {
        return agentSQL;
    }

    public void setAgent(AgentSQLServerBean agent) {
        this.agentSQL = agent;
    }

    public String[] getIps() {
        return ips;
    }

    public void setIps(String[] ips) {
        this.ips = ips;
    }
}
