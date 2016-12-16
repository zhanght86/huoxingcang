/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.socket.action;

import com.marstor.msa.cdp.model.AgentBean;
import com.marstor.msa.cdp.model.AgentFileBean;
import com.marstor.msa.cdp.model.AgentOracleBean;
import com.marstor.msa.cdp.model.AgentSQLServerBean;
import com.marstor.msa.cdp.socket.MarsClientSocket;
import com.marstor.msa.nas.util.Debug;
import com.marstor.xml.XMLParser;

/**
 *
 * @author Administrator
 */
public class GetAgentsAction extends MyBasicQueryAction {
    
    public GetAgentsAction(MarsClientSocket socket, String guid) {
        super("9603", "Get Specified Agent Parameters", socket);
        this.addParameter("Group", guid);
    }

    public AgentBean[] getAgents() {
        if (!this.queryAndGetBooleanResult()) {
            return null;
        }
        Debug.print(this.sendXML.toXmlString());
        Debug.print(this.returnXML.toXmlString());
        
        int count = this.returnXML.getNodeCount("MSA/ReturnValue/Agent");
        AgentBean[] clients = new AgentBean[count];
        for (int i = 0; i < count; i++) {
            AgentBean c = null;
            XMLParser beanParser = returnXML.createXMLParser("MSA/ReturnValue/Agent", i);
            int agentID = beanParser.getIntNodeContent("Agent/AgentID");
            int state = beanParser.getIntNodeContent("Agent/State");            
            switch (agentID) {
                case AgentBean.AGENT_ORACLE:
                    c = new AgentOracleBean(); 
                    c.generateByXMLConstructor(beanParser);
                    break;
                case AgentBean.AGENT_SQLSERVER : 
                    c = new AgentSQLServerBean();
                    c.generateByXMLConstructor(beanParser);
                    break;
                default : 
                    c = new AgentBean();
                    break;
            }
            c.state = state;
            c.agentID = agentID;
            c.groupID = returnXML.getNodeContent("MSA/ReturnValue/Group");
            c.MSAIP = returnXML.getNodeContent("MSA/ReturnValue/MSAIP");
            c.MSAPort = returnXML.getNodeContent("MSA/ReturnValue/MSAPort");
            clients[i] = c;
        }
        return clients;
    }
}
