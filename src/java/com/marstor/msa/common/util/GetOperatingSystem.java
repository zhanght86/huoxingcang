/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.common.util;


import com.marstor.msa.vm.model.ChipTypes;
import com.marstor.msa.vm.model.OSName;
import com.marstor.msa.vm.model.OperatingSystem;
import com.marstor.xml.XMLParser;
import java.util.ArrayList;
import java.util.List;

/**
 * @introduction ��ø�����ϵͳ�汾
 * @author Administrator
 */
public class GetOperatingSystem {
    public static XMLParser os = new XMLParser(GetOperatingSystem.class.getResourceAsStream("OS.xml"));  //����ϵͳXML�ļ�
    public static OSName[] osNames;  //����ϵͳ��
    public static ChipTypes[] chips;  //оƬ��


    //��ø�����ϵͳ�µİ汾
    public static OSName[] initOSResource(){
        int count = os.getNodeCount("MarsVM/OS/OSItem");
        osNames = new OSName[count];
        for (int i = 0; i < count; i++)
        {
            XMLParser childParser = os.createXMLParser("MarsVM/OS/OSItem",i);
            osNames[i] = new OSName();
            osNames[i].setOsName(childParser.getNodeContent("OSItem/OSName"));

            int osTypeCount = childParser.getNodeCount("OSItem/OSType");
            List<OperatingSystem> opeSystem = new ArrayList();
            for(int j = 0; j < osTypeCount; j++){
                 XMLParser osTypeChildParser = childParser.createXMLParser("OSItem/OSType", j);
                 String ID = osTypeChildParser.getNodeContent("OSType/ID");
                 String typeName = osTypeChildParser.getNodeContent("OSType/TypeName");
                 int memorySize = osTypeChildParser.getIntNodeContent("OSType/MemorySize");
                 int diskSize = osTypeChildParser.getIntNodeContent("OSType/DiskSize");

                  OperatingSystem operatingSystem = new OperatingSystem();
                  operatingSystem.setID(ID);
                  operatingSystem.setTypeName(typeName);
                  operatingSystem.setMemorySize(memorySize);
                  operatingSystem.setDiskSize(diskSize);

                  opeSystem.add(operatingSystem);
                
            }
            osNames[i].setOsType(opeSystem);           
        }
        return osNames;
    }

    //��ȡĳ�������µ�оƬ����
    public static ChipTypes[] GetChipType() {

        int count = os.getNodeCount("MarsVM/Controller/ControllerItem");
        chips = new ChipTypes[count];
        for (int i = 0; i < count; i++) {
            XMLParser childParser = os.createXMLParser("MarsVM/Controller/ControllerItem", i);
            chips[i] = new ChipTypes();
            chips[i].setControllerName(childParser.getNodeContent("ControllerItem/ControllerName"));

            XMLParser chipChildParser = childParser.createXMLParser("ControllerItem/ChipType");
            int chipItemcount = chipChildParser.getNodeCount("ChipType/ChipItem");
            String[] chipItem = new String[chipItemcount];
            for (int j = 0; j < chipItemcount; j++) {
                chipItem[j] = chipChildParser.getNodeContent("ChipType/ChipItem", j);
            }
            chips[i].setChipTypes(chipItem);
        }
        return chips;
    }
}
