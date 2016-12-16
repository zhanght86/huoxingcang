/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.backup.managedbean.AgentManager;
import com.marstor.msa.cdp.bean.Host;
import com.marstor.msa.cdp.model.CDPAgent;
import com.marstor.msa.cdp.model.CDPAgentPackageXML;
import com.marstor.msa.cdp.model.CDPJre;
import com.marstor.msa.cdp.socket.CDPClientSocket;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.model.IconData;
import com.marstor.msa.common.treeNode.TreeNodeData;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.xml.XMLParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "clientsBean")
@ViewScoped
public class ClientsBean {

    private List<Host> clients;
    private Host selected = new Host();
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.clients";
    private List<CDPAgent> agentList = null;
    private CDPAgent selectedAgent;
    private CDPJre selectedJre;
    private String FILE_NAME = "BuildInfo.xml";
    private final String FILE_PATH = "/usr/msa/cdp/client/";
    private final String JRE_PATH = "/usr/msa/cdp/jre/";
    private StreamedContent agentFile;
    private StreamedContent jreFile;
    private List<CDPJre> jreList = null;

    public ClientsBean() {
        initClients();
        initAgents();
        initJres();
    }

    public final void initClients() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        this.clients = cdp.getAllHosts();
    }

    public CDPJre getSelectedJre() {
        return selectedJre;
    }

    public void setSelectedJre(CDPJre selectedJre) {
        this.selectedJre = selectedJre;
    }
    
    

    private void initAgents() {
        agentList = new ArrayList<CDPAgent>();
        ClassLoader classLoader = this.getClass().getClassLoader();
        List<CDPAgentPackageXML> readMBAAgentPakageXML = Utility.readCDPAgentPakageXML(classLoader);

        List<CDPAgent> tmpAgentList = getAgent();
        if (tmpAgentList == null) {
            return;
        }
        int i = 1;
        for (CDPAgentPackageXML cdpAgentXML : readMBAAgentPakageXML) {            
            if (cdpAgentXML == null) {
                continue;
            }            
            for (CDPAgent c : tmpAgentList) {
                if (c == null) {
                    continue;
                }

                if (c.getStrAgentName().equalsIgnoreCase(cdpAgentXML.getStrAgentName())) {
                    c.setStrAgentOS(cdpAgentXML.getStrAgentOS());
                    c.setStrAgentPlatform(cdpAgentXML.getStrAgentPlatform());
                    c.setStrDescription(cdpAgentXML.getStrDescription());
                    c.setSerial(i);
                    agentList.add(c);
                    i++;
                }                
            }
        }
    }
    
    private void initJres() {
        jreList = new ArrayList<CDPJre>();
        ClassLoader classLoader = this.getClass().getClassLoader();
        List<CDPAgentPackageXML> readMBAAgentPakageXML = Utility.readCDPJrePakageXML(classLoader);
        String[] tmpAgentList = getJre();
        if (tmpAgentList == null) {
            return;
        }
        int i = 1;        
        for (CDPAgentPackageXML cdpAgentXML : readMBAAgentPakageXML) {
            if (cdpAgentXML == null) {
                continue;
            }
            CDPJre jre = new CDPJre();
            jre.setSerial(i);
            jre.setStrAgentName(cdpAgentXML.getStrAgentName());
            jre.setStrAgentOS(cdpAgentXML.getStrAgentOS());
            jre.setStrAgentPlatform(cdpAgentXML.getStrAgentPlatform());
            jre.setStrDescription(cdpAgentXML.getStrDescription());
            jreList.add(jre);
            i++;
        }
    }

    private List<CDPAgent> getAgent() {
        List<CDPAgent> agents = new ArrayList<CDPAgent>();
        File file = new File("/usr/msa/cdp/client/");
        if(!file.exists()){
            return null;
        }
        File[] zips = file.listFiles();
        if (zips == null) {
            return null;
        }
        for (int i = 0; i < zips.length; i++) {
            if (zips[i] == null) {
                continue;
            }
            agents.add(this.readZip(zips[i]));
        }
        return agents;
    }
    
    private String[] getJre() {
        List<CDPJre> agents = new ArrayList<CDPJre>();
        File file = new File("/usr/msa/cdp/jre/");
        if(!file.exists()){
            return null;
        }
        String[] zips = file.list();
        if (zips == null) {
            return null;
        }
        return zips;
    }
    
    private CDPAgent readZip(File agentFile) {
        Debug.print("readZip:" + agentFile.getName());
        try {
            CDPAgent cdpAgent = new CDPAgent();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            cdpAgent.setStrAgentName(agentFile.getName());
            cdpAgent.setStrAgentModifyDate(formatter.format(new Date(agentFile.lastModified())));
            ZipFile zipFile = new ZipFile(agentFile.getAbsolutePath());
            ZipEntry zipEntry = zipFile.getEntry(FILE_NAME);
            Debug.print("zipEntry:" + zipEntry);
            if (zipEntry == null) {
                return null;
            }
            XMLParser xmlParser = new XMLParser(zipFile.getInputStream(zipEntry));
            cdpAgent.setStrAgentDate(formatString(xmlParser.getNodeContent("BuildInfo/Date")));
            cdpAgent.setStrAgentVersion(xmlParser.getNodeContent("BuildInfo/Version"));
            Debug.print("cdpAgent:" + cdpAgent.getStrAgentName());
            return cdpAgent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    } 

    private String formatString(String s) {
        if (s.length() == 8) {
            StringBuilder sb = new StringBuilder();
            sb.append(s.substring(0, 4));
            sb.append("-");
            sb.append(s.substring(4, 6));
            sb.append("-");
            sb.append(s.substring(6, 8));
            return sb.toString();
        } else {
            return s;
        }
    }

    public String connect() {
        if (!connectHost()) {
            return null;
        }
        String param = "ip=" + selected.ip + "&port=" + selected.port;
        if (this.selected.OStype == CDPConstants.OS_TYPE_WINDOWS) {
            return "client?faces-redirect=true&amp;" + param;
        } else if (this.selected.OStype == CDPConstants.OS_TYPE_LINUX) {
            return "client_linux?faces-redirect=true&amp;" + param;
        }
        return param;
    }

    public boolean showClients() {
        CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
        Reg[] license = commonInterfaceInstance.getLicense();
        for (int i = 0; i < license.length; i++) {
            if (Module.MODULE_CDP == license[i].getModuleID() && Module.FUNCTIONID_CDP_CAPACITY == license[i].getFunctionID()) {
                if (0 != Integer.valueOf(license[i].getFunctionNumber())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean connectHost() {
        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(selected.ip, Integer.valueOf(selected.port))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "connectFail"), global.get("error_mark")));
            return false;
        }
        return true;
    }

    public Host getSelected() {
        return selected;
    }

    public void setSelected(Host selected) {
        this.selected = selected;
    }

    public List<Host> getClients() {
        return clients;
    }

    public void setClients(List<Host> clients) {
        this.clients = clients;
    }

    public StreamedContent getAgentFile() {
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(FILE_PATH + selectedAgent.getStrAgentName()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        agentFile = new DefaultStreamedContent(stream, "", selectedAgent.getStrAgentName());
        InterfaceFactory.getMBAInterfaceInstance().downloadAgentLog(selectedAgent.getStrAgentName(), true);
        return agentFile;
    }
    
    public StreamedContent getJreFile() {
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(JRE_PATH + selectedJre.getStrAgentName()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        jreFile = new DefaultStreamedContent(stream, "", selectedJre.getStrAgentName().split("/")[1]);
        InterfaceFactory.getMBAInterfaceInstance().downloadAgentLog(selectedJre.getStrAgentName().split("/")[1], true);
        return jreFile;
    }

    public String modifyClient() {
        System.out.println("hostname-hostname-hostname:" + selected.hostname);
        String param = "hostname=" + selected.getHostname() + "&os=" + selected.OStype
                + "&ip=" + selected.getIp() + "&port=" + selected.getPort();
        return "modify_client?faces-redirect=true&amp;" + param;
    }

    public void deleteClient() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        if (!cdp.deleteHost(this.selected.ip)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "deleteFailed"), global.get("error_mark")));
        } else {
            //       FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "deleteSuccess"), global.get("error_mark")));
            initClients();
        }

    }

    public List<CDPAgent> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<CDPAgent> agentList) {
        this.agentList = agentList;
    }

    public CDPAgent getSelectedAgent() {
        return selectedAgent;
    }

    public void setSelectedAgent(CDPAgent selectedAgent) {
        this.selectedAgent = selectedAgent;
    }

    public List<CDPJre> getJreList() {
        
        return jreList;
    }

    public void setJreList(List<CDPJre> jreList) {
        this.jreList = jreList;
    }
    
}
