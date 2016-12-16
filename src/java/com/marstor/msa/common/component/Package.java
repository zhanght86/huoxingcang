/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.component;

import com.marstor.msa.bean.IMarsXMLBean;
import com.marstor.xml.XMLConstructor;
import com.marstor.xml.XMLParser;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class Package  implements IMarsXMLBean, Serializable {
    private String name;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
   

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public XMLConstructor toXMLConstrutor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void generateByXMLConstructor(XMLParser xmlp) {
        int id = xmlp.getIntNodeContent("Package/ModuleID");
        name = UpgradePackage.getModuleById(id);
        String path = xmlp.getNodeContent("Package/VersionPath");
        version = UpgradePackage.getVersionFromFile(path);
    }
    
    
}
