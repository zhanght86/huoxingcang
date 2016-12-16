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
public class AgentFileBean extends AgentBean{

    public String dbaName = "";
    public String password = "";
    public String ip = "";
    public String instanceName = "";
    public int port = 0;
    public String interval = "";

    public AgentFileBean(){}
    
    public AgentFileBean(String groupID,
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
        this.port = port;
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

        int nodeCount = parser.getNodeCount("Agent/Parameter");
        for (int i = 0; i < nodeCount; i++) {
            String key = parser.getNodeContent("Agent/Parameter/Key", i);
            String value = parser.getNodeContent("Agent/Parameter/Value", i);
            if (key.equals("Interval")) {
                if(value.equals("0")){
                    this.interval = "";
                }else{
                    this.interval = value;
                }     
            }
        }
    }
}
