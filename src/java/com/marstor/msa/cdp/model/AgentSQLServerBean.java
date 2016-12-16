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
public class AgentSQLServerBean extends AgentBean{

    public String userName = "";
    public String password = "";
    public String ip = "";
    public String database = "";    
    public String instanceName = "";
    public int port = 1433;
    public String interval = "60";
    public int mode = 1;

    public AgentSQLServerBean(){}
    
    public AgentSQLServerBean(String groupID, 
                           String MSAIP, 
                           int MSAPort,
                           String userName,
                           String password, 
                           String ip, 
                           int port, 
                           int interval, 
                           String database) {
        this.groupID = groupID;
        this.MSAIP = MSAIP;
        this.userName = userName;
        this.password = password;
        this.ip = ip;
        this.port = port;
        this.database = database;
    }

    @Override
    public XMLConstructor toXMLConstrutor() {
        XMLConstructor sqlServer = new XMLConstructor("Parameters");
        sqlServer.addNode("Group", this.groupID);
        sqlServer.addNode("MSAIP", this.MSAIP);
        sqlServer.addNode("MSAPort", this.MSAPort);

        XMLConstructor parameterName = new XMLConstructor("Parameter");
        parameterName.addNode("Key", "UserName");
        parameterName.addNode("Value", this.userName);
        sqlServer.addNode(parameterName);

        XMLConstructor parameterPassword = new XMLConstructor("Parameter");
        parameterPassword.addNode("Key", "Password");
        parameterPassword.addNode("Value", MyEncryp.Encode64(this.password.toCharArray()).toString());
        sqlServer.addNode(parameterPassword);

        XMLConstructor parameterIP = new XMLConstructor("Parameter");
        parameterIP.addNode("Key", "IP");
        parameterIP.addNode("Value", this.ip);
        sqlServer.addNode(parameterIP);

        XMLConstructor parameterPort = new XMLConstructor("Parameter");
        parameterPort.addNode("Key", "Port");
        parameterPort.addNode("Value", this.port);
        sqlServer.addNode(parameterPort);

        XMLConstructor parameterDatabase = new XMLConstructor("Parameter");
        parameterDatabase.addNode("Key", "Database");
        parameterDatabase.addNode("Value", this.database);
        sqlServer.addNode(parameterDatabase);

        XMLConstructor parameterInterval = new XMLConstructor("Parameter");
        parameterInterval.addNode("Key", "Interval");
        parameterInterval.addNode("Value", this.interval);
        sqlServer.addNode(parameterInterval);
        return sqlServer;
    }

    @Override
    public void generateByXMLConstructor(XMLParser parser) {
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
                if(value.equals("0")){
                    this.port = 1099;
                }else{
                    this.port = Integer.parseInt(value);
                }     
                
            }else if (key.equals("Instance")) {
                this.instanceName = value;
            }else if (key.equals("UserName")) {
                this.userName = value;
            } else if (key.equals("Password")) {
                this.password = new String(MyEncryp.Decode(value.toCharArray()));
            } else if (key.equals("Interval")) {
                if(value.equals("0")){
                    this.interval = "";
                }else{
                    this.interval = value;
                }
            }else if(key.equals("Database")){
                this.database = value;
            }else if(key.equals("Mode")){
                this.mode = Integer.parseInt(value);
            }
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
    
    
    
    public static void main(String [] args){
        AgentSQLServerBean bean = new AgentSQLServerBean("group123", "192.234.1.2", 110, "yuan", "xi", "225.225.0.1", 1234, 1, "database");
        System.out.println(bean.toXMLConstrutor().toXmlString());
    
    }
}
