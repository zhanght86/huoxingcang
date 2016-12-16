/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.mdu.axis2.client;

import com.marstor.msa.base.axis2.client.MsaBaseClient;
import static com.marstor.msa.base.axis2.client.MsaBaseClient.options;
import static com.marstor.msa.base.axis2.client.MsaBaseClient.sendReceive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;

/**
 *
 * @author Administrator
 */
public class EcdClient extends MsaBaseClient {

    private EndpointReference targetEPR;
    private OMFactory omFactroy;
    private OMNamespace omNamespace;
    private String error;
    
    public EcdClient(String ip, int port) {

        targetEPR = new EndpointReference("http://" + ip + ":" + port + "/ecd/services/ecd?wsdl");

        omFactroy = OMAbstractFactory.getOMFactory();
        omNamespace = omFactroy.createOMNamespace("http://impl.axis2.mdu.msa.marstor.com", "");
    }

    public String getError() {
        return error;
    }

    public void setError() {
        OMElement method = omFactroy.createOMElement("getError", omNamespace);
        OMElement response = sendReceive(options, method);
        error = getResponseString(response);
    }
    
    public boolean modifyLicense(int groupnum, int usernum, String uuid, String secretkey) {
        targetEPR(targetEPR);
        OMElement method = omFactroy.createOMElement("modifyLicense", omNamespace);
        OMElement groupNum = omFactroy.createOMElement("groupnum", omNamespace);
        groupNum.setText(groupnum + "");
        method.addChild(groupNum);
        OMElement response = sendReceive(options, method);
        boolean result = getResponseBoolean(response);
        if (!result) {
            this.setError();
            return false;
        }
        return true;
    }
    
    public String getShareName() {
        targetEPR(targetEPR);
        OMElement method = omFactroy.createOMElement("getShareName", omNamespace);
        OMElement response = sendReceive(options, method);
        String result = getResponseString(response);
        if (result == null) {
            this.setError();
        }
        return result;
    }
    public boolean addGroup(String name) {
        targetEPR(targetEPR);
        OMElement method = omFactroy.createOMElement("addGroup", omNamespace);
        OMElement paraName = omFactroy.createOMElement("name", omNamespace);
        paraName.setText(name);
        method.addChild(paraName);
        OMElement response = sendReceive(options, method);
        boolean result = getResponseBoolean(response);
        if (!result) {
            this.setError();
            return false;
        }
        return true;
    }
    
    public String[] getGroups() {
        targetEPR(targetEPR);
        OMElement method = omFactroy.createOMElement("getGroups", omNamespace);
        OMElement response = sendReceive(options, method);
        String[] groups = getResponseArray(response);
        if (groups == null) {
            setError();
            return null;
        }
        return groups;
    }
    
    public boolean addUser(String name, String[] groups) {
        targetEPR(targetEPR);
//        MDUAddUserParameter para = new MDUAddUserParameter();
//        para.setUser(name);
//        para.setGroups(groups);
//        OMElement element = generateRequestObject(para);
        OMElement method = omFactroy.createOMElement("addUser", omNamespace);
//        method.addChild(element);
        OMElement response = sendReceive(options, method);
        boolean result = getResponseBoolean(response);
        if (!result) {
            this.setError();
            return false;
        }
        return true;
    }
    public String[] getUsers() {
        targetEPR(targetEPR);
        OMElement method = omFactroy.createOMElement("getUsers", omNamespace);
        OMElement response = sendReceive(options, method);
        String[] users = getResponseArray(response);
        if (users == null) {
            setError();
            return null;
        }
        return users;
    }
//    public boolean addSharedDirectory(String owner, String[] dirs, String[] users){
//        targetEPR(targetEPR);
//        MDUAddSharedDirParameter para = new MDUAddSharedDirParameter();
//        para.setOwner(owner);
//        para.setDirs(dirs);
//        para.setUsers(users);
//        OMElement element = generateRequestObject(para);
//        OMElement method = omFactroy.createOMElement("addSharedDirectory", omNamespace);
//        method.addChild(element);
//        OMElement response = sendReceive(options, method);
//        boolean result = getResponseBoolean(response);
//        if (!result) {
//            this.setError();
//            return false;
//        }
//        return true;
//    }
        public boolean addSharedDirectory(String xml){
        targetEPR(targetEPR);
        OMElement method = omFactroy.createOMElement("addSharedDirectory", omNamespace);
        OMElement paraXml = omFactroy.createOMElement("xml", omNamespace);
        paraXml.setText(xml);
        method.addChild(paraXml);
        OMElement response = sendReceive(options, method);
        boolean result = getResponseBoolean(response);
        if (!result) {
            this.setError();
            return false;
        }
        return true;
    }
//    public String[] getUserDirectory(String user){
//        targetEPR(targetEPR);
//        OMElement method = omFactroy.createOMElement("getUserDirectory", omNamespace);
//        OMElement paraUser = omFactroy.createOMElement("user", omNamespace);
//        paraUser.setText(user);
//        method.addChild(paraUser);
//        OMElement response = sendReceive(options, method);
//        String[] userDirectortys = getResponseArray(response);
//        if (userDirectortys == null) {
//            setError();
//            return null;
//        }
//        return userDirectortys;
//    }
    
//    public UserDir[] getUserDirectory(String user){
//        targetEPR(targetEPR);
//        OMElement method = omFactroy.createOMElement("getUserDirectory", omNamespace);
//        OMElement paraUser = omFactroy.createOMElement("user", omNamespace);
//        paraUser.setText(user);
//        method.addChild(paraUser);
//        OMElement response = sendReceive(options, method);
////        UserDir[] userDirectorys = getResponseList(response,UserDir.class);
//        UserDir[] userDirectorys = getResponseObject(response,UserDir.class);
//        if (userDirectorys == null) {
//            setError();
//            return null;
//        }
//        return userDirectorys;
//    }
    public boolean addUserDirectory(String user, String directory){
        targetEPR(targetEPR);
        OMElement method = omFactroy.createOMElement("addUserDirectory", omNamespace);
        OMElement paraUser = omFactroy.createOMElement("user", omNamespace);
        OMElement paraDirectory = omFactroy.createOMElement("directory", omNamespace);
        paraUser.setText(user);
        paraDirectory.setText(directory);
        method.addChild(paraUser);
        method.addChild(paraDirectory);
        OMElement response = sendReceive(options, method);
        boolean result = getResponseBoolean(response);
        if (!result) {
            this.setError();
            return false;
        }
        return true;
    }
}
