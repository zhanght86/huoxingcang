/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.base.axis2.client;


import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;

/**
 *
 * @author Administrator
 */
public class LoginClient extends MsaBaseClient {

//    private EndpointReference targetEPR;
//    private OMFactory omFactroy;
//    private OMNamespace omNamespace;
//    private String error;

    public LoginClient() {
    }
    

//    public LoginClient(String ip, int port) {
//
//        targetEPR = new EndpointReference("http://" + ip + ":" + port + "/services/loginService?wsdl");
//
//
//
////        serviceClient.setOptions(options);
//
//        omFactroy = OMAbstractFactory.getOMFactory();
//        omNamespace = omFactroy.createOMNamespace("http://impl.axis2.common.msa.marstor.com", "");
//    }
//
//    public String getError() {
//
//        return error;
//    }

    public int login(String user,String password,EndpointReference targetEPR,OMFactory omFactroy,OMNamespace omNamespace) {
        targetEPR(targetEPR) ;
        OMElement method = omFactroy.createOMElement("login", omNamespace);
        OMElement paraUserName = omFactroy.createOMElement("username", omNamespace);
        paraUserName.setText(user);
        OMElement paraPass = omFactroy.createOMElement("password", omNamespace);
        paraPass.setText(password);
        method.addChild(paraUserName);
        method.addChild(paraPass);
        sendReceive(options, method);
                OMElement response = sendReceive(options, method);
        int result = getResponseInt(response);
        return result;
    }

    public void checklogin(EndpointReference targetEPR,OMFactory omFactroy,OMNamespace omNamespace) {
        targetEPR(targetEPR) ;
        OMElement method = omFactroy.createOMElement("checklogin", omNamespace);
        serviceClient.setOptions(options);
        sendReceive(options, method);
    }
   
}
