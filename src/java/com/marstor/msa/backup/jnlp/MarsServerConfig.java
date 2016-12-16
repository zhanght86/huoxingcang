/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.jnlp;

import com.marstor.msa.backup.util.Util;
import com.marstor.msa.common.util.MSAOEMResource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class MarsServerConfig {

    private String strURL = null;
    private List<String> jarFileList = null;
    private List<URL> urlJarPathList = new ArrayList();
    String strUserHome = Util.getUserHome();
    MSAOEMResource res = new MSAOEMResource();

    public MarsServerConfig(String strIP, List<String> jarFilePath) {
        strURL = "http://" + strIP + "/mba";
        if (jarFilePath != null) {
            jarFileList = jarFilePath;
            for (int i = 0; i < jarFileList.size(); i++) {
                try {
                    urlJarPathList.add(new URL(strURL + jarFileList.get(i).substring(14, jarFileList.get(i).length())));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(MarsServerConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public boolean writeJNLPFile() {
        Util util = new Util();
        String path = util.getAppPath();
        String strFileName = path.substring(0, path.lastIndexOf("WEB-INF"))+ "backups/MarsServerConfig.jnlp";
        System.out.println("writeJNLPFile: "+strFileName);
//        String strFileName = "/var/tomcat6/webapps/ROOT/backup/MarsServerConfig.jnlp";
        try {
//            FileWriter writer = new FileWriter(strFileName);
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(strFileName, false), "UTF-8");
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n");
            writer.write("<jnlp spect=\"1.0+\" codebase=\"" + strURL + "\" spec=\"1.0+\">" + "\n");
            writer.write("<information>" + "\n");
            writer.write("<title>MarsServerConf</title>" + "\n");
            writer.write("<vendor>" + res.get("STRING_MBA_VENDOR_NAME") + "</vendor>" + "\n");
            writer.write("<homepage href=\"" + strURL + "\"/>" + "\n");
            writer.write("<description>MarsServerConf</description>" + "\n");
            writer.write("<description kind=\"short\">MarsServerConf</description>" + "\n");
            writer.write("<icon href=\"MBA_32.gif\"/>" + "\n");
            writer.write("<offline-allowed/>" + "\n");
            writer.write("<shortcut online=\"false\">" + "\n");
            writer.write("<menu submenu=\"" + res.get("STRING_MBA_MENU_NAME") + "\"/>" + "\n");
            writer.write("</shortcut>" + "\n");
            writer.write("</information>" + "\n");
            writer.write("<security>" + "\n");
            writer.write("<all-permissions/>" + "\n");
            writer.write("</security>" + "\n");
            writer.write("<resources>" + "\n");
            writer.write("<j2se version=\"1.4+\"/>" + "\n");
            writer.write("<jar href=\"MARSTORJarLoader.jar\" main=\"true\" download=\"eager\"/>" + "\n");
            writer.write("</resources>" + "\n");
            writer.write("<application-desc main-class=\"com.marstor.jnlp.MARSTOR_JNLP_ClasssLoader\">" + "\n");
            writer.write("<argument>url=" + strURL + "</argument>" + "\n");
            writer.write("<argument>mainclass=com.marstor.server.conf.MarsServerConfFrame</argument>" + "\n");
            writer.write("<argument>jar=MarsServerConfig.jar</argument>" + "\n");
            writer.write("<argument>jar=MBAJobManage.jar</argument>" + "\n");
            writer.write("<argument>opt=" + strURL + "</argument>" + "\n");
            for (URL jarurl : urlJarPathList) {
                writer.write("<argument>opt=" + jarurl.toString() + "</argument>" + "\n");
            }

            writer.write("</application-desc>" + "\n");
            writer.write("</jnlp>" + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void execJNLP() {
        Util.exec(Util.getJavaHome(), "MarsServerConfig.jnlp", strUserHome);
    }
}
