/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.socket.action;


import com.marstor.msa.cdp.socket.MarsClientSocket;
import com.marstor.msa.cdp.socket.XMLBaseConstructor;
import com.marstor.msa.cdp.socket.XMLBaseParser;
import com.marstor.xml.XMLConstructor;
import com.marstor.xml.XMLParser;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.catalina.connector.Constants;

/**
 *
 * @author Administrator
 */
public class MyBasicQueryAction {

    private MarsClientSocket socket;
    public XMLBaseConstructor sendXML;
    public XMLBaseParser returnXML;
    private String errorInformation;

    public MyBasicQueryAction() {
    }

    private MyBasicQueryAction(String CommmandID, String description) {
        sendXML = new XMLBaseConstructor(CommmandID, description);
        sendXML.addParametersElement();
    }

    public MyBasicQueryAction(String CommmandID, String description, MarsClientSocket socket) {
        this(CommmandID, description);
        this.socket = socket;
        socket.setTimeout(300);
    }

    public void addParameter(String paraName, String value) {
        sendXML.addParameter(paraName, value);
    }

    public void addParameter(String paraName, long value) {
        sendXML.addParameter(paraName, value);
    }

    public void addParameter(String paraName, int value) {
        sendXML.addParameter(paraName, value);
    }

    public void addParameter(XMLConstructor bean) {
        XMLConstructor parameters = new XMLConstructor("Parameters");
        parameters.addNode(bean);
        sendXML.addNode(parameters);
    }

    public boolean sendData() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();

        if (!socket.sendXmlData(sendXML.toByte())) {
            return false;
        }
        return true;
    }

    public boolean readData() {
        if (!socket.onRead()) {
            return false;
        }

        returnXML = new XMLBaseParser(socket.getData());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        if (returnXML.getIntNodeContent("MSA/IsSuccess") == 0) {
            this.errorInformation = returnXML.getNodeContent("MSA/Information");
            if (errorInformation != null && !errorInformation.equals("")) {
            }
            return false;
        }
        return true;
    }

    public XMLParser getReturnValue() {
        return returnXML.createXMLParser("MSA/ReturnValue");
    }

    public boolean doAction() {
        if (!sendData()) {
            return false;
        }
        if (!readData()) {
            return false;
        }
        return true;
    }

    public String[] getMultiStringValue(String name) {
        if (!doAction()) {
            return null;
        }
        int count = returnXML.getNodeCount("MSA/ReturnValue/" + name);
        String[] ret = new String[count];
        for (int i = 0; i < count; i++) {
            ret[i] = returnXML.getNodeContent("MSA/ReturnValue/" + name, i);
        }
        return ret;
    }

    public String getSingleStringValue(String name) {
        if (!doAction()) {
            return null;
        }
        return returnXML.getNodeContent("MSA/ReturnValue/" + name);
    }

    public int getSingleIntValue(String name) {
        if (!doAction()) {
            return -1;
        }
        return returnXML.getIntNodeContent("MSA/ReturnValue/" + name);
    }

    public boolean getSingleBooleanValue(String name) {
        if (!doAction()) {
            return false;
        }
        return Boolean.valueOf(returnXML.getNodeContent("MSA/ReturnValue/" + name));
    }

    public boolean queryAndGetBooleanResult() {
        if (!doAction()) {
            return false;
        }
        return returnXML.getIntNodeContent("MSA/IsSuccess") == 1 ? true : false;
    }

    /**
     * @return the socket
     */
    public MarsClientSocket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(MarsClientSocket socket) {
        this.socket = socket;
    }

    /**
     * @return the errorInformation
     */
    public String getErrorInformation() {
        return errorInformation;
    }
}
