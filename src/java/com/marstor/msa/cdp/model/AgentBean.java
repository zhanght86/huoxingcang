/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.model;

import com.marstor.xml.XMLConstructor;
import com.marstor.xml.XMLParser;



/**
 *
 * @author Douli
 */
public class AgentBean {

    public static final int OPEN = 1;
    public static final int CLOSE = 0;
    public static final int AGENT_ORACLE = 2;
    public static final int AGENT_SQLSERVER = 3;
    
    public static final int MODE_MIX = 1;
    public static final int MODE_WINDOWS = 2;
    
    public String groupID = "";
    public String MSAIP = "";
    public String MSAPort = "1099";
    public int agentID;
    public int state;

    public AgentBean(){}
    
    public AgentBean(String groupID,
                       String MSAIP,
                       int MSAPort,
                       String dbaName,
                       String password,
                       String ip,
                       int port,
                       int interval,
                       String instanceName) {
        this.groupID = groupID;
        this.MSAIP = MSAIP;
    }

    public XMLConstructor toXMLConstrutor() {
        XMLConstructor oracle = new XMLConstructor("Parameters");
        oracle.addNode("Group", this.groupID);
        oracle.addNode("MSAIP", this.MSAIP);
        oracle.addNode("MSAPort", this.MSAPort);
        return oracle;
    }

    public void generateByXMLConstructor(XMLParser parser) {
        this.groupID = parser.getNodeContent("MSA/Parameters/Group");
        this.MSAIP = parser.getNodeContent("MSA/Parameters/MSAIP");
        this.MSAPort = parser.getNodeContent("MSA/Parameters/MSAPort");
    }
    
    @Override
    public String toString(){
        String s = "";
        switch(this.agentID){
                case AGENT_ORACLE : s = "Oracle代理";break;
                    case AGENT_SQLSERVER : s = "SQL Server代理";break;
        }
        return s;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getMSAIP() {
        return MSAIP;
    }

    public void setMSAIP(String MSAIP) {
        this.MSAIP = MSAIP;
    }

    public String getMSAPort() {
        return MSAPort;
    }

    public void setMSAPort(String MSAPort) {
        this.MSAPort = MSAPort;
    }

    public int getAgentID() {
        return agentID;
    }

    public void setAgentID(int agentID) {
        this.agentID = agentID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    
    
}
