/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.SystemInformation;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.model.SysInfo;
import com.marstor.msa.common.util.MSAOEMResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.common.web.impl.ZFSInterfaceImpl;
import com.marstor.msa.util.InterfaceFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.primefaces.context.RequestContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "system_sysInfoListBean")
@ViewScoped
public class System_sysInfoListBean implements Serializable {

    private List<SysInfo> sysInfoList;
    private MSAResource res = new MSAResource();
    private String basename = "common.system_sysinfo";
    private String activeIndex = "";//页面需要显示的Tab编号，从0开始
    private String strVersion;
    private String strHost;
    private String strPort;
    private String strMemoryControl;
    private boolean isFactorySnapshotExist;

    /**
     * Creates a new instance of System_sysInfoListBean
     */
    public System_sysInfoListBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        activeIndex = request.getParameter("activeIndex");
        if (activeIndex == null || activeIndex.equals("")) {
            activeIndex = "0";
        }
        sysInfoList = new ArrayList();
        this.setSystemInformation();
        this.initHost();
        this.initPort();
        
        ZFSInterface  zfs = ZFSInterfaceImpl.getInstance();
        int arc = zfs.getZfsArcMax();
        if(arc ==0){
            strMemoryControl = res.get(basename, "memory_control");
        }else{
            strMemoryControl = arc+"GB";
        }
 
    }

    public String getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(String activeIndex) {
        this.activeIndex = activeIndex;
    }

    private void setSystemInformation() {
        MSAOEMResource oem = new MSAOEMResource();

        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();

        //twb add
        this.isFactorySnapshotExist = common.isFactorySnapshotExist();

        SystemInformation sys = common.getSystemInfo();
        if (sys != null) {
            SystemOutPrintln.print_common(sys.hostname);
            SysInfo sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "device"));
            sysInfo.setValue(sys.hostname);
            sysInfoList.add(sysInfo);

            ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
            HttpSession session = request.getSession();
            SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
            String userName = user.getName();
            int userType = user.getType();
            SystemOutPrintln.print_common(userName);
            String userTypeStr = res.get(basename, "common");
            if (userType == 2) {
                userTypeStr = res.get(basename, "admin");
            } else if (userType == 3) {
                userTypeStr = res.get(basename, "auditor");
            } else if (userType == 4) {
                userTypeStr = res.get(basename, "authorized");
            }else {
                userTypeStr = res.get(basename, "common");
            }

            sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "user"));
            sysInfo.setValue(userName);
            sysInfoList.add(sysInfo);

            String loginTime = (String) session.getAttribute("time");
            sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "time"));
            sysInfo.setValue(loginTime);
            sysInfoList.add(sysInfo);

//            sysInfo = new SysInfo();
//            sysInfo.setProperty(res.get(basename, "kernel"));
//            sysInfo.setValue(sys.kernel);
//            sysInfoList.add(sysInfo);
            sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "processor"));
            sysInfo.setValue("64-bit CPU, " + sys.cpuCount + " " + "Processors");
            sysInfoList.add(sysInfo);

            sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "grade"));
            sysInfo.setValue(userTypeStr);  //1普通用户，2管理员，3审计员
            sysInfoList.add(sysInfo);

            sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "system_uptime"));
            String strTime = sys.usedtime;
            SystemOutPrintln.print_common("system operate time: " + strTime);
            String[] split = strTime.split(" ");
            String finalTime = "";
            for (String s : split) {
                if (s.contains("year")) {
                    s = s.substring(0, s.indexOf("year")) + res.get(basename, "year");
                }

                if (s.contains("month")) {
                    s = s.substring(0, s.indexOf("month")) + res.get(basename, "month");
                }

                if (s.contains("day")) {
                    s = s.substring(0, s.indexOf("day")) + res.get(basename, "day");
                }
                if (s.contains(":")) {
                    s = s.substring(0, s.indexOf(":")) + res.get(basename, "hour") + s.substring(s.indexOf(":") + 1) + res.get(basename, "mininute");
                }

                finalTime = finalTime + s + " ";
            }
            sysInfo.setValue(finalTime);
            sysInfoList.add(sysInfo);

            sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "version"));
            sysInfo.setValue(oem.get("version"));
            sysInfoList.add(sysInfo);

            sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "manufacturer"));
            sysInfo.setValue(oem.get("manufacturer"));
            sysInfoList.add(sysInfo);

            sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "memory"));
            String strMem = sys.memory;
            sysInfo.setValue(strMem.substring(0, strMem.indexOf("M") + 1) + "B");
            sysInfoList.add(sysInfo);

            sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "networkcard"));
            String net = "";
            if (sys.netConfig != null && sys.netConfig.length > 0) {
                String[] nets = sys.netConfig;
                Arrays.sort(nets);
                for (int i = 0; i < nets.length; i++) {
                    if (nets.length == 1) {
                        net = net + nets[i] + " ";
                    } else {
                        if (i == (nets.length - 1)) {
                            net = net + nets[i] + " ";
                        } else {
                            net = net + nets[i] + ", ";
                        }
                    }
                }
            }

            sysInfo.setValue(net);
            sysInfoList.add(sysInfo);

            String ww = "";
            if (sys.wwpn == null || sys.wwpn.length == 0) {
                ww = res.get(basename, "none");
            } else {
                String[] wwns = sys.wwpn;
                Arrays.sort(wwns);

                for (int i = 0; i < wwns.length; i++) {
                    if (wwns.length == 1) {
                        ww = ww + wwns[i].toUpperCase() + " ";
                    } else {
                        if (i == (wwns.length - 1)) {
                            ww = ww + wwns[i].toUpperCase() + " ";
                        } else {
                            ww = ww + wwns[i].toUpperCase() + ", ";
                        }
                    }
                }
            }
            sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "fibercard"));
            sysInfo.setValue(ww);
            sysInfoList.add(sysInfo);

            sysInfo = new SysInfo();
            sysInfo.setProperty(res.get(basename, "systemcode"));
            sysInfo.setValue(sys.systemKey);
            sysInfoList.add(sysInfo);
        }
    }

    public List<SysInfo> getSysInfoList() {
        return sysInfoList;
    }

    public void setSysInfoList(List<SysInfo> sysInfoList) {
        this.sysInfoList = sysInfoList;
    }

    private void initHost() {
        this.strHost = InterfaceFactory.getCommonInterfaceInstance().getHostName();
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();

        ServletContext application = request.getSession().getServletContext();
        InputStream inputStream = application.getResourceAsStream("/META-INF/MANIFEST.MF");
        try {
            Manifest manifest = new Manifest(inputStream);
            Attributes attr = manifest.getMainAttributes();
            String sVersion = attr.getValue("Version");
            String sBuilt = attr.getValue("Built-Date");
            this.strVersion = sVersion + " Build " + sBuilt.substring(0, sBuilt.length() - 4);
        } catch (IOException ex) {
            Logger.getLogger(System_sysInfoListBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initPort() {
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
                    this.strPort = brandElement.getAttribute("port");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getStrVersion() {
        return strVersion;
    }

    public void setStrVersion(String strVersion) {
        this.strVersion = strVersion;
    }

    public String getStrHost() {
        return strHost;
    }

    public void setStrHost(String strHost) {
        this.strHost = strHost;
    }

    public String getStrPort() {
        return strPort;
    }

    public void setStrPort(String strPort) {
        this.strPort = strPort;
    }

    
    
    public boolean isIsFactorySnapshotExist() {
        return isFactorySnapshotExist;
    }

    public void setIsFactorySnapshotExist(boolean isFactorySnapshotExist) {
        this.isFactorySnapshotExist = isFactorySnapshotExist;
    }
    
    public void autoNetQuestion(){
        RequestContext.getCurrentInstance().execute("autoNet.show()");
    }

    public void resetfactoryConfirm1() {
        this.autoNet = true;
        RequestContext.getCurrentInstance().execute("autoNet.hide()");
        RequestContext.getCurrentInstance().execute("resetfactory1.show()");
    }
    
    public void resetfactoryConfirm0() {
        RequestContext.getCurrentInstance().execute("autoNet.hide()");
        RequestContext.getCurrentInstance().execute("resetfactory0.show()");
    }

    public void resetfactory() {
        String param = "autoNet=" + (autoNet ? 1 : 0);
        RequestContext.getCurrentInstance().execute("window.top.location.href='factory_reset.xhtml?" + param + "'");
    }
    public String toMonitor_memory() {
        String param = "port=" + strPort;
        return "monitor_memory?faces-redirect=true" + param;
    }
    
    
    private boolean autoNet = false;

    public String getStrMemoryControl() {
        return strMemoryControl;
    }

    public void setStrMemoryControl(String strMemoryControl) {
        this.strMemoryControl = strMemoryControl;
    }
    
}
