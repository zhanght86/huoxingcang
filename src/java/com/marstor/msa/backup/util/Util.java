/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.util;

import com.marstor.msa.backup.model.DatabaseParameter;
import com.marstor.msa.backup.model.MBAAgentPackageXML;
import com.marstor.msa.common.util.FileUploadController;
import com.marstor.xml.XMLParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Administrator
 */
public class Util {

    public static String getMbaVendor() {
        String aaa = "Mars Storage Appliance v5.0";
        System.out.println(aaa);
        return aaa;
    }

    public static String getMbaMenu() {
        String aaa = "北京亚细亚智业科技有限公司";
        System.out.println(aaa);
        return aaa;
    }

    public static String getJavaHome() {
        String strJavaHome = System.getProperty("java.home");
        System.out.println(System.getProperty("java.home"));
        System.out.println(System.getProperty("user.home"));
        System.out.println(System.getProperty("user.dir"));
        return strJavaHome;
    }

    public static String getUserHome() {
        String strUserHome = System.getProperty("user.home");
        return strUserHome;
    }

    public static List<MBAAgentPackageXML> readMBAAgentPakageXML(ClassLoader classLoader) {
        List<MBAAgentPackageXML> mbaAgentList = new ArrayList();
        InputStream mbaInputStream = classLoader.getResourceAsStream("com/marstor/msa/backup/res/package.xml");
        if (mbaInputStream != null) {
            XMLParser xmlPaser = new XMLParser(mbaInputStream);
            int iAgentCount = xmlPaser.getNodeCount("MBA/Setup/SetupItem");
            for (int i = 0; i < iAgentCount; i++) {
                XMLParser agentXMLParser = xmlPaser.createXMLParser("MBA/Setup/SetupItem", i);
                MBAAgentPackageXML mbaAgentPackageXML = new MBAAgentPackageXML();
                mbaAgentPackageXML.setStrAgentOS(agentXMLParser.getNodeContent("SetupItem/OS"));
                mbaAgentPackageXML.setStrAgentPlatform(agentXMLParser.getNodeContent("SetupItem/Platform"));
                mbaAgentPackageXML.setStrAgentName(agentXMLParser.getNodeContent("SetupItem/FileName"));
                mbaAgentPackageXML.setStrDescription(agentXMLParser.getNodeContent("SetupItem/Description"));
                mbaAgentList.add(mbaAgentPackageXML);
            }
        }
        return mbaAgentList;
    }

    public static void exec(String strJavaHome, String strJNLPName, String strUserHome) {
        Process process;
        try {
            String cmd = strJavaHome + File.separator + "bin" + File.separator + "javaws.exe "
                    + "\"" + strUserHome + File.separator + strJNLPName + "\"";
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static InputStream getRemoteXMLSteram() {
        InputStream in = null;
        File file = new File("/usr/local/mba/MarsServer.xml");
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return in;
    }

    public static DatabaseParameter getDBBean(InputStream in) {
        DatabaseParameter dbBean = new DatabaseParameter();;
        XMLParser parser = new XMLParser(in);
        dbBean.setStrServerName(parser.getNodeContent("MBA/MarsMasterParameter/DatabaseParameter/ServerName"));
        dbBean.setStrPort(parser.getNodeContent("MBA/MarsMasterParameter/DatabaseParameter/Port"));
        dbBean.setStrDBName(parser.getNodeContent("MBA/MarsMasterParameter/DatabaseParameter/DBName"));
        dbBean.setStrUser(parser.getNodeContent("MBA/MarsMasterParameter/DatabaseParameter/User"));
        dbBean.setStrPassword(parser.getNodeContent("MBA/MarsMasterParameter/DatabaseParameter/Password"));
        return dbBean;
    }

    public static String convert(Long size) {
        if (size >= 0 && size < 1024) {
            return size.toString() + "MB";
        } else if (size >= 1024 && size < 1024000) {
            size = size / 1024;
            return size.toString() + "GB";
        } else if (size >= 1024000 && size < 1024000000) {
            size = size / 1024000;
            return size.toString() + "TB";
        }
        return size.toString();
    }

    public static String getString(String str) {
        return ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString(str);
    }

    public static boolean upLoadFile(InputStream inputStream, String path) {
        File dir = new File(path);//上传后写入硬盘的文件
        OutputStream outputStream = null;//输出流写到硬盘      
        try {
            outputStream = new FileOutputStream(dir);
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();
            inputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public String getAppPath() {
        URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        String strPath = url.getPath();
        strPath = strPath.replace('\\', '/');
        if (strPath.endsWith("/")) {
            strPath = strPath.substring(0, strPath.length() - 1);
        }
        int nIndex = strPath.lastIndexOf("/");
        strPath = strPath.substring(0, nIndex);
        if (!strPath.endsWith("/")) {
            strPath = strPath + "/";
        }
        System.out.println("getAppPath: strPath = "+strPath);
        return strPath;
    }
}
