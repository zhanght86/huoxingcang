package com.marstor.msa.cdp.model;

import org.w3c.dom.*;

public abstract class CommonInfo 
{
    protected String m_strXmlCmd = "";
    protected String m_strError = "";

    public abstract boolean ParseInfo(Element objNode);

    public String getError() {
        return m_strError;
    }

    public String getXmlCmd() {
        return m_strXmlCmd;
    }

    public void setXmlCmd(String strXmlCmd) {
        m_strXmlCmd = strXmlCmd;
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
