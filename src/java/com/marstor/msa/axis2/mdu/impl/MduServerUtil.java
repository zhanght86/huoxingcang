/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.mdu.impl;


import com.marstor.msa.mdu.bean.UserDir;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.util.StreamWrapper;

/**
 *
 * @author Administrator
 */
public class MduServerUtil {
     public static <T> OMElement generateResponseObject(T t) {
        javax.xml.stream.XMLStreamReader reader = BeanUtil.getPullParser(t);
        StreamWrapper parser = new StreamWrapper(reader);
        OMXMLParserWrapper stAXOMBuilder = OMXMLBuilderFactory.createStAXOMBuilder(OMAbstractFactory.getOMFactory(), parser);
        OMElement element = stAXOMBuilder.getDocumentElement();
        return element;
    }

    public static <T> T getResquestObject(OMElement element, Class<T> clazz) {
        try {
            OMNode omNode = (OMNode) element;
            if (omNode.getType() == OMNode.ELEMENT_NODE) {
                OMElement omElement = (OMElement) omNode;
                if (element.getLocalName().toLowerCase().equals(clazz.getSimpleName().toLowerCase())) {
                    T para = (T) BeanUtil.processObject(
                            omElement, clazz, null, false, new DefaultObjectSupplier(), clazz);
                    return para;
                }
            }
        } catch (AxisFault ex) {
            Logger.getLogger(MduServerUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Calendar convertStringToCalendar(String time){
        if(time == null){
            return null;
        }
        String[] times = time.split(":");
        int endHour = Integer.parseInt(times[0]);
        int endMinite = Integer.parseInt(times[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, endHour);
        calendar.set(Calendar.MINUTE, endMinite);
        return calendar;
    }
    
    public static String convertCalendarToString(Calendar calendar){
        if(calendar == null){
            return null;
        }
        String time = calendar.get(Calendar.HOUR_OF_DAY) +":"+calendar.get(Calendar.MINUTE);
        return time;
    }
    
    public static com.marstor.msa.axis2.mdu.model.UserDir convertUserDirInfo(UserDir cdpDiskInfo) {
        if(cdpDiskInfo == null){
            return null;
        }
         com.marstor.msa.axis2.mdu.model.UserDir cdpDiskInfo_axis
                = new com.marstor.msa.axis2.mdu.model.UserDir();
        cdpDiskInfo_axis.setFullPath(cdpDiskInfo.getFullPath());
        cdpDiskInfo_axis.setOwner(cdpDiskInfo.getOwner());
        cdpDiskInfo_axis.setType(cdpDiskInfo.getType());
        cdpDiskInfo_axis.setUserDir(cdpDiskInfo.getUserDir());
        return cdpDiskInfo_axis;
    }    
    
}
