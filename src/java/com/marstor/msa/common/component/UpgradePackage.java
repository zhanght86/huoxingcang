/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.component;

import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.xml.XMLParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class UpgradePackage {

    private static final String FILEPATH = "/usr/msa/";
    private static final String FILENAME = "package.xml";
    private static final int MODULE_COMMON = 0;
    private static final int MODULE_BACKUP = 1;
    private static boolean flag = false;

    public static List<Package> getUpgradePackages() {
        CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
        Reg[] license = commonInterfaceInstance.getLicense();
        for (int i = 0; i < license.length; i++) {
            if ((Module.MODULE_VMware == license[i].getModuleID() && Module.FUNCTIONID_VMware == license[i].getFunctionID())){
                flag = true;
            }
        }
        List<Package> pkgs = new ArrayList();
        File file = new File(FILEPATH + FILENAME);
        if (!file.exists()) {
            return pkgs;
        }
        XMLParser parser = new XMLParser(file);
        int iPackage = parser.getNodeCount("MSA/Package");
        for (int i = 0; i < iPackage; i++) {
            XMLParser parseShared = parser.createXMLParser("MSA/Package", i);
            Package pkg = new Package();
            pkg.generateByXMLConstructor(parseShared);
            if(!flag && pkg.getName().equals(UpgradePackage.getModuleById(MODULE_BACKUP))){
                continue;
            }
            pkgs.add(pkg);
        }
        flag = false;
        return pkgs;
    }

    public static String getModuleById(int id) {
        String name;
        switch (id) {
            case MODULE_COMMON:
                name = "基本系统";
                break;
            case MODULE_BACKUP:
                name = "高级备份";
                break;
            default:
                name = "";
        }
        return name;
    }

    public static String getVersionFromFile(String file) {
        List<String> strs = readFile(file);
        if (strs == null || strs.isEmpty()) {
            return "";
        }
        return strs.get(0);
    }

    private static List<String> readFile(String file) {
        List<String> fileContext = new ArrayList();
        File f = new File(file);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(f));
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                fileContext.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return fileContext;
    }
}
