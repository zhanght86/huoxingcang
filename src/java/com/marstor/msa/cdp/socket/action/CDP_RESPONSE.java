package com.marstor.msa.cdp.socket.action;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.io.*;

public class CDP_RESPONSE 
{
    public long nStatus;
    public String strErrorMessage;
    
    protected String m_strXmlCmd = "";
    protected String m_strError = "";
    protected Element m_RootNode = null;
    
    public String getError() {
        return m_strError;
    }
    
    public void setXmlCmd(String strXmlCmd) {
        m_strXmlCmd = strXmlCmd;
    }
    
    public boolean ParseResponse()
    {
        //get root node 
        if(!GetRootNode())
            return false;
        
        //status 
        String strText = GetXmlNodeString(m_RootNode, "Status");
        if (strText == null) {
            m_strError = "Status is invalid!";
            return false;
        }
        nStatus = Integer.parseInt(strText);
        
        //error message
        strText = GetXmlNodeString(m_RootNode, "ErrorMessage");
        if (strText == null) {
            m_strError = "Error message is invalid!";
            return false;
        }
        strErrorMessage = strText;

        return true;
    }
    
    private boolean GetRootNode() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            m_strError = pce.getMessage();
            return false;
        }

        ByteArrayInputStream is = new ByteArrayInputStream(m_strXmlCmd.getBytes());
        Document doc = null;
        try {
            doc = builder.parse(is);
        } catch (IOException ioe) {
            m_strError = ioe.getMessage();
            return false;
        } catch (SAXException saxe) {
            m_strError = saxe.getMessage();
            return false;
        }

        m_RootNode = doc.getDocumentElement();

        return true;
    }
    
    protected String GetXmlNodeString(Element parentNode, String strNode) {
        NodeList Nodes = parentNode.getElementsByTagName(strNode);
        if (Nodes.getLength() < 1) {
            return null;
        }
        Element childNode = (Element) Nodes.item(0);

        String strValue = null;
        try {
            strValue = childNode.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }

        return strValue;
    }

    protected NodeList GetXmlNodeList(Element parentNode, String strNode) {
        NodeList Nodes = parentNode.getElementsByTagName(strNode);

        return Nodes;
    }

    protected Element GetXmlChildNode(Element parentNode, String strNode) {
        NodeList Nodes = parentNode.getElementsByTagName(strNode);
        if (Nodes.getLength() < 1) {
            return null;
        }
        Element childNode = (Element) Nodes.item(0);

        return childNode;
    }
}
