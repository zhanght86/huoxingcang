/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.base.axis2.client;

//import com.marstor.msa.cdp.axis2.client.CdpClientUtil;
import com.marstor.msa.mdu.axis2.client.EcdClient;
import com.marstor.msa.mdu.axis2.client.Ecdguest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.util.StreamWrapper;

/**
 *
 * @author Administrator
 */
public abstract class MsaBaseClient {

    public static ServiceClient serviceClient;
    public static Options options;
    private static final ReentrantLock lock = new ReentrantLock();

    static {
        try {
            serviceClient = new ServiceClient();
            options = serviceClient.getOptions();
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
            options.setManageSession(true);
        } catch (AxisFault ex) {
            Logger.getLogger(MsaBaseClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void targetEPR(EndpointReference targetEPR) {
        options.setTo(targetEPR);
    }

    public static OMElement sendReceive(Options options, OMElement method) {
        lock.lock();
        try {
            printRequest(method);
            method.build();
            serviceClient.setOptions(options);
            System.out.println(method.toString());
            OMElement response = serviceClient.sendReceive(method);
            System.out.println(response.toString());
            printResponse(response);
            return response;
        } catch (AxisFault ex) {
            Logger.getLogger(MsaBaseClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.unlock();
        }
        return null;
    }

    public static void sendRobust(Options options, OMElement method) {
        lock.lock();
        try {
            printRequest(method);
            method.build();
            serviceClient.setOptions(options);
            serviceClient.sendRobust(method);
        } catch (AxisFault ex) {
            Logger.getLogger(MsaBaseClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.unlock();
        }
    }

    private static void printRequest(OMElement request) {
        if (request == null) {
            return;
        }
//        System.out.println("axis2 request:" + request.toString());
    }

    private static void printResponse(OMElement response) {
        if (response == null) {
            return;
        }
//        System.out.println("axis2 response:" + response.toString());
//        System.out.println(response.getFirstElement().getText());
    }

    public static int getResponseInt(OMElement response) {
        if (response == null) {
            return -1;
        }
        return Integer.parseInt(response.getFirstElement().getText());
    }

    public static long getResponseLong(OMElement response) {
        if (response == null) {
            return -1;
        }

        return Long.parseLong(response.getFirstElement().getText());
    }

    public static String getResponseString(OMElement response) {
        if (response == null || response.toString().
                contains("<ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />")) {
            return null;
        }
        return response.getFirstElement().getText();
    }

    public static String[] getResponseArray(OMElement response) {
        String[] stringArray = null;
        ArrayList<String> list = new ArrayList<String>();
        if (response == null || response.toString().contains("<ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />")) {
            return null;
        }
        Iterator iter = response.getChildElements();
        while (iter.hasNext()) {
            OMElement element = (OMElement) iter.next();
            list.add(element.getText());
        }
        stringArray = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            stringArray[i] = list.get(i);
        }
        return stringArray;
    }

    public static boolean getResponseBoolean(OMElement response) {
        if (response == null) {
            return false;
        }
        return Boolean.parseBoolean(response.getFirstElement().getText());
    }
//    public static UserDir[] getResponseList(OMElement response, Class<UserDir> clazz) {
//        List<UserDir> list = new ArrayList();
//        try {
//            if (response == null || response.toString().contains("<ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />")) {
//                return null;
//            }
//            OMNode omNode = (OMNode) response.getFirstElement().getFirstElement();
//            if (omNode != null && omNode.getType() == OMNode.ELEMENT_NODE) {
//                OMElement omElementRoot = (OMElement) omNode;
//                if (omElementRoot.getLocalName().toLowerCase().equals("root")) {
//                    Iterator iter = omElementRoot.getChildElements();
//                    while (iter.hasNext()) {
//                        OMElement element = (OMElement) iter.next();
//                        UserDir obj = (UserDir) BeanUtil.processObject(element, clazz, null, false, new DefaultObjectSupplier(), clazz);
//                        list.add(obj);
//                    }
//                }
//            }
//        } catch (AxisFault ex) {
//            Logger.getLogger(MduClient.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//        UserDir [] userDir = new UserDir[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            userDir[i] = list.get(i);
//        }
//        return userDir;
//    }
    public static <T> T[] getResponseObject(OMElement response, Class<T> clazz) {
        List<T> list = new ArrayList();
        try {
            if (response == null || response.toString().contains("<ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />")) {
                return null;
            }
            OMNode omNode = (OMNode) response.getFirstElement().getFirstElement();
            if (omNode != null && omNode.getType() == OMNode.ELEMENT_NODE) {
                OMElement omElementRoot = (OMElement) omNode;
                if (omElementRoot.getLocalName().toLowerCase().equals("root")) {
                    Iterator iter = omElementRoot.getChildElements();
                    while (iter.hasNext()) {
                        OMElement element = (OMElement) iter.next();
                        T obj = (T) BeanUtil.processObject(element, clazz, null, false, new DefaultObjectSupplier(), clazz);
                        list.add(obj);
                    }
                }
            }
        } catch (AxisFault ex) {
            Logger.getLogger(Ecdguest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        T[] t;
        t = (T[]) Array.newInstance(clazz , list.size());
        for (int i = 0; i < list.size(); i++) {
            t[i] = list.get(i);
        }
        return t;
    }
//    public static <T> List<T> getResponseList(OMElement response, Class<T> clazz) {
//        List<T> list = new ArrayList();
//        try {
//            if (response == null || response.toString().contains("<ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />")) {
//                return null;
//            }
//            OMNode omNode = (OMNode) response.getFirstElement().getFirstElement();
//            if (omNode != null && omNode.getType() == OMNode.ELEMENT_NODE) {
//                OMElement omElementRoot = (OMElement) omNode;
//                if (omElementRoot.getLocalName().toLowerCase().equals("root")) {
//                    Iterator iter = omElementRoot.getChildElements();
//                    while (iter.hasNext()) {
//                        OMElement element = (OMElement) iter.next();
//                        T obj = (T) BeanUtil.processObject(element, clazz, null, false, new DefaultObjectSupplier(), clazz);
//                        list.add(obj);
//                    }
//                }
//            }
//        } catch (AxisFault ex) {
//            Logger.getLogger(CdpClientUtil.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//        return list;
//    }

//    public static <T> T getResponseObject(OMElement response, Class<T> clazz) {
//        try {
//            if (response == null || response.toString().contains("<ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />")) {
//                return null;
//            }
//            OMNode omNode = (OMNode) response.getFirstElement().getFirstElement();
//            if (omNode != null && omNode.getType() == OMNode.ELEMENT_NODE) {
//                OMElement element = (OMElement) omNode;
//                if (element.getLocalName().toLowerCase().equals(clazz.getSimpleName().toLowerCase())) {
//                    T obj = (T) BeanUtil.processObject(element, clazz, null, false, new DefaultObjectSupplier(), clazz);
//                    return obj;
//                }
//            }
//        } catch (AxisFault ex) {
//            Logger.getLogger(CdpClientUtil.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }

    public static <T> OMElement generateRequestObject(T t) {
        javax.xml.stream.XMLStreamReader reader = BeanUtil.getPullParser(t);
        StreamWrapper parser = new StreamWrapper(reader);
        OMXMLParserWrapper stAXOMBuilder = OMXMLBuilderFactory.createStAXOMBuilder(OMAbstractFactory.getOMFactory(), parser);
        OMElement element = stAXOMBuilder.getDocumentElement();
        return element;
    }
}
