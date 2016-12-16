/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.managedbean;

import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "rebootBean")
@ViewScoped
public final class RebootBean implements Serializable {

    public String name;
    public String port;
    public boolean init;
    /**
     * Creates a new instance of RebootBean
     */
    public RebootBean() {
        name = new MSAResource().get("tip");
       
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession sess=request.getSession();
        if(sess == null || sess.isNew())
        {

            System.out.println("跳转初始化：用户未登录");
            return;
        }
        else
        {
            SystemUserInformation user = (SystemUserInformation) sess.getAttribute("user");
            if(user==null)
            {
                 System.out.println("跳转初始化：用户未登录");
                 return ;
            }
        }
        
        String flag = request.getParameter("init");
        request.getSession().invalidate();   
        initPort();
        if(request.getParameter("port") != null){
            this.port = request.getParameter("port");
        }   
        
        if(flag != null){
            init = flag.equals("1");
            reboot2();
        }else{
            reboot1();
        }
    }
    
    private void initPort(){
        try {
// 1.得到DOM解析器的工厂实例
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
// 2.从DOM工厂里获取DOM解析器
            DocumentBuilder db = dbf.newDocumentBuilder();
// 3.解析XML文档，得到document，即DOM树
            Document doc = db.parse("/var/tomcat6/conf/server.xml");

            NodeList list = doc.getElementsByTagName("Connector");
            for (int i = 0; i < list.getLength(); i++) {
                Element brandElement = (Element) list.item(i);
                String protocol = brandElement.getAttribute("protocol");
                if(protocol.contains("HTTP")){
                    port = brandElement.getAttribute("port");
                }
            } 
            

        } catch (Exception ex) {            
            System.out.println(ex.getMessage());
        }
    }
    
    public void reboot1() {
        SystemOutPrintln.print_volume(" reboot1()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                reboot3();
            }
        });
        thread.start();
    }

    public void reboot3() {
        try {
            Thread.sleep(30000);

            CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
            SystemOutPrintln.print_volume("is reboot");
            boolean reboot = common.reboot();
            ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
            HttpSession sess=request.getSession();
            SystemUserInformation user = (SystemUserInformation) sess.getAttribute("user");
            if(null==user)
            {
                common.setUser("System");
            }
            else
            {
                 common.setUser(user.getName());
            }
           
            if (!reboot) {
                SystemOutPrintln.print_volume("reboot false");
            } else {
//                          RequestContext.getCurrentInstance().execute("window.top.location.href='/template/login.xhtml'");   
                SystemOutPrintln.print_volume("reboot success");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(RebootBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void reboot2() {
        SystemOutPrintln.print_volume(" reboot2()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                reboot();
            }
        });
        thread.start();
    }

    public void reboot() {
        try {
            Thread.sleep(30000);

            CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
            SystemOutPrintln.print_volume("is reboot");
            boolean reboot = common.reboot(init);
            if (!reboot) {
                SystemOutPrintln.print_volume("reboot false");
            } else {
//                          RequestContext.getCurrentInstance().execute("window.top.location.href='/template/login.xhtml'");   
                SystemOutPrintln.print_volume("reboot success");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(RebootBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean testURL() {
        try {
            URL url = new URL("");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            /**
             * public int getResponseCode()throws IOException 从 HTTP 响应消息获取状态码。
             * 例如，就以下状态行来说： HTTP/1.0 200 OK HTTP/1.0 401 Unauthorized 将分别返回 200
             * 和 401。 如果无法从响应中识别任何代码（即响应不是有效的 HTTP），则返回 -1。
             *
             * 返回 HTTP 状态码或 -1
             */
            int state = conn.getResponseCode();
            SystemOutPrintln.print_volume(state + "");
            if (state == 200) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    
}
