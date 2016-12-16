/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.model;

import com.marstor.msa.common.util.MyEncryp;
import com.marstor.xml.XMLConstructor;
import com.marstor.xml.XMLParser;

/**
 *
 * @author Douli
 */
public class AgentOracleBean extends AgentBean {

    public String dbaName = "";
    public String password = "";
    public String ip = "";
    public String instanceName = "";
    public String port = "1521";
    public String interval = "60";

    public AgentOracleBean() {
    }

    public AgentOracleBean(String groupID,
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
        this.dbaName = dbaName;
        this.password = password;
        this.ip = ip;
        this.instanceName = instanceName;
    }

    @Override
    public XMLConstructor toXMLConstrutor() {
        XMLConstructor oracle = new XMLConstructor("Parameters");
        oracle.addNode("Group", this.groupID);
        oracle.addNode("MSAIP", this.MSAIP);
        oracle.addNode("MSAPort", this.MSAPort);

        XMLConstructor parameterName = new XMLConstructor("Parameter");
        parameterName.addNode("Key", "DBAName");
        parameterName.addNode("Value", this.dbaName);
        oracle.addNode(parameterName);

        XMLConstructor parameterPassword = new XMLConstructor("Parameter");
        parameterPassword.addNode("Key", "Password");
        parameterPassword.addNode("Value", MyEncryp.Encode64(this.password.toCharArray()).toString());
        oracle.addNode(parameterPassword);

        XMLConstructor parameterIP = new XMLConstructor("Parameter");
        parameterIP.addNode("Key", "IP");
        parameterIP.addNode("Value", this.ip);
        oracle.addNode(parameterIP);

        XMLConstructor parameterPort = new XMLConstructor("Parameter");
        parameterPort.addNode("Key", "Port");
        parameterPort.addNode("Value", this.port);
        oracle.addNode(parameterPort);

        XMLConstructor parameterInstance = new XMLConstructor("Parameter");
        parameterInstance.addNode("Key", "Instance");
        parameterInstance.addNode("Value", this.instanceName);
        oracle.addNode(parameterInstance);

        XMLConstructor parameterInterval = new XMLConstructor("Parameter");
        parameterInterval.addNode("Key", "Interval");
        parameterInterval.addNode("Value", this.interval);
        oracle.addNode(parameterInterval);
        return oracle;
    }

    @Override
    public void generateByXMLConstructor(XMLParser parser) {
        this.groupID = parser.getNodeContent("MSA/Parameters/Group");
        this.MSAIP = parser.getNodeContent("MSA/Parameters/MSAIP");
        this.MSAPort = parser.getNodeContent("MSA/Parameters/MSAPort");
        
        int nodeCount = parser.getNodeCount("Agent/Parameter");
        if(nodeCount == 0){
            return;
        }
        for (int i = 0; i < nodeCount; i++) {
            String key = parser.getNodeContent("Agent/Parameter/Key", i);
            String value = parser.getNodeContent("Agent/Parameter/Value", i);
            if (key.equals("IP")) {
                this.ip = value;
            } else if (key.equals("Port")) {
                if (value.equals("0")) {
                    this.port = "1099";
                } else {
                    this.port = value;
                }

            } else if (key.equals("DBAName")) {
                this.dbaName = value;
            } else if (key.equals("Password")) {
                this.password = new String(MyEncryp.Decode(value.toCharArray()));
            } else if (key.equals("Interval")) {
                if (value.equals("0")) {
                    this.interval = "";
                } else {
                    this.interval = value;
                }
            } else if (key.equals("Instance")) {
                this.instanceName = value;
            }
        }
    }

    public String getDbaName() {
        return dbaName;
    }

    public void setDbaName(String dbaName) {
        this.dbaName = dbaName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
    
    
}
