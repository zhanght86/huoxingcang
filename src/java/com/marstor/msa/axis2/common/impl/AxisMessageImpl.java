/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.common.impl;

import com.marstor.xml.XMLParser;

/**
 *
 * @author Administrator
 */
public class AxisMessageImpl {
    public boolean sendMsg(String xmlMsg){
        XMLParser parser = new XMLParser(xmlMsg);
        System.out.println(parser.toXmlString());
        return true;
    }
}
