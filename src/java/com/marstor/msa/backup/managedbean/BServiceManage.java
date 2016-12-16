/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.managedbean;

import com.marstor.msa.backup.model.BackupAgent;
import com.marstor.msa.backup.jnlp.MBAJobManage;
import com.marstor.msa.backup.jnlp.MBASysManage;
import com.marstor.msa.backup.jnlp.MarsServerConfig;
import com.marstor.msa.backup.util.Util;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "bServiceManager")
@ViewScoped
public class BServiceManage implements Serializable {

    private static final String[] servicesNameView = {
        java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("MasterServerService"),
        java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("DataMoverService"),
        java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("DeviceControllerService"),
        java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("TransmiterService"),
        java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("FileAgentService"),
        java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("SystemDatabaseService"),
        java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("PostgreSQLDatabaseAgentService")
    };
    private static final String[] servicesName = {
        "MBAMasterServer", "MBADataMover", "MBADeviceController", "MBATransmitter", "MBAFileAgent", "PostgreSQL", "MBAPostgreSQLAgent"
    };
    private static final String onLine = "../resources/common/picture/online.png";
    private static final String offLine = "../resources/common/picture/offline.png";
    private final String FILE_PATH = "/usr/local/mba/";
    private List<BackupAgent> list = new ArrayList<BackupAgent>();
    private BackupAgent selectedAgent = null;
    private int userType;
    String ip = "";
    private StreamedContent systemFile;
    private StreamedContent jobFile;
    private StreamedContent configFile;
    private boolean bRestartAll = true;
    private UploadedFile file;
    private MSAResource res = new MSAResource();
    private int iIndex = 0;

    public int getIIndex() {
        return iIndex;
    }

    public void setIIndex(int iIndex) {
        this.iIndex = iIndex;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public BServiceManage() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        userType = user.getType();
        ip = request.getLocalAddr();
        String port="";
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
         
         if(!port.equals(""))
         {
              ip = ip +":"+port;     
         }
        init();
    }

    public void isRestartAll(boolean b) {
        bRestartAll = b;
        System.out.println("Now restart is " + bRestartAll);
    }

    public void setBRestartAll(boolean b) {
        this.bRestartAll = b;
    }

    public boolean getBRestartAll() {

        return bRestartAll;
    }

    public void setSelectedAgent(BackupAgent selectedAgent) {
        this.selectedAgent = selectedAgent;
    }

    public BackupAgent getSelectedAgent() {
        return selectedAgent;
    }

    public void setList(List<BackupAgent> list) {
        this.list = list;
    }

    public List<BackupAgent> getList() {

        return list;
    }

    private void init() {
        list = new ArrayList<BackupAgent>();
        for (int i = 0; i < servicesName.length; i++) {
            String cmdLine = null;
            if (i == 5) {
                cmdLine = "/usr/local/mba/scripts/PostgreSQL status";
            } else {
                cmdLine = "/usr/local/mba/scripts/" + servicesName[i] + ".sh status";
            }

            BackupAgent agent = new BackupAgent();
            String mbaService = InterfaceFactory.getMBAInterfaceInstance().mbaService(cmdLine, servicesName[i], "Get Service Status");
            if (mbaService != null) {
                if (mbaService.contains("running")) {
                    agent.setStatus(true);
                }
            } else {
                agent.setStatus(false);
            }

            agent.setSerial(i);
            agent.setAgentName(servicesNameView[i]);
            if (servicesNameView[i].equals("iSCSIInitiator")) {
                agent.setAgentName("iSCSI Initiator");
            }
            if (agent.getStatus()) {
                agent.setAgentState(java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("Online"));
                agent.setImage(offLine);
                agent.setOperation(java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("StopService"));
                if (userType == 2) {
                    agent.setVisible(false);
                }
            } else {
                agent.setAgentState(java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("Offline"));
                agent.setImage(onLine);
                agent.setOperation(java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString("StartService"));
            }
            list.add(agent);
        }
    }

    public void operation() {
        if (selectedAgent.getStatus()) {
            stopAgent();
        } else {
            startAgent();
        }
        init();
    }

    public void startAgent() {
        if (null == selectedAgent) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("StartServiceFailed"), ""));
            return;
        }

        boolean status = false;
        int i = selectedAgent.getSerial();
        if (i == 5) {
            status = startPostgreSQL();
        } else {
            status = startCommonService(i);
        }
        if (status) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("StartServiceSuccess"), "。"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("StartServiceFailed"), "。"));
        }
    }

    public void stopAgent() {
        if (null == selectedAgent) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("StopServiceFailed"), ""));
            return;
        }

        boolean status = false;
        int i = selectedAgent.getSerial();
        if (i == 5) {
            status = stopPostgreSQL();
        } else {
            status = stopCommonService(i);
        }

        if (status) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("StopServiceSuccess"), "。"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("StopServiceFailed"), "。"));
        }
    }

    public void restartAgent() {
        if (null == selectedAgent) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("RestartServiceFailed"), "。"));
            return;
        }

        boolean status = false;
        int i = selectedAgent.getSerial();
        if (i == 5) {
            status = restartPostgreSQL();
        } else {
            status = restartCommonService(i);
        }

        if (status) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("RestartServiceSuccess"), "。"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("RestartServiceFailed"), "。"));
        }
        init();
    }

    public boolean startPostgreSQL() {
        String cmdLine = "/usr/local/mba/scripts/PostgreSQL start";
        String strRet = InterfaceFactory.getMBAInterfaceInstance().mbaService(cmdLine, "PostgreSQL", "Start");
        System.out.println(strRet);
        if (strRet.contains("Successfully")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean stopPostgreSQL() {
        String cmdLine = "/usr/local/mba/scripts/PostgreSQL stop";
        String strRet = InterfaceFactory.getMBAInterfaceInstance().mbaService(cmdLine, "PostgreSQL", "Stop");
        if (strRet.contains("success")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean restartPostgreSQL() {
        if (stopPostgreSQL() && startPostgreSQL()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean startCommonService(int i) {
        String cmdLine = "/usr/local/mba/scripts/" + servicesName[i] + ".sh start";
        String strRet = InterfaceFactory.getMBAInterfaceInstance().mbaService(cmdLine, servicesName[i], "Start");
        if (strRet.contains("successfully")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean stopCommonService(int i) {
        String cmdLine = "/usr/local/mba/scripts/" + servicesName[i] + ".sh stop";
        String strRet = InterfaceFactory.getMBAInterfaceInstance().mbaService(cmdLine, servicesName[i], "Stop");
        if (strRet.contains("successfully")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean restartCommonService(int i) {
        String cmdLine = "/usr/local/mba/scripts/" + servicesName[i] + ".sh restart";
        String strRet = InterfaceFactory.getMBAInterfaceInstance().mbaService(cmdLine, servicesName[i], "Restart");
        if (strRet.contains("successfully")) {
            return true;
        } else {
            return false;
        }
    }

    public String getFilePath() {
        return FILE_PATH;
    }

    public void handleFileUpload() {
        if (0 == file.getSize()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("NotEmptyFile"), ""));
            setIIndex(2);
            return;
        }
        InputStream inputstream = null;
        try {
            inputstream = file.getInputstream();
        } catch (IOException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
            setIIndex(2);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UploadConfigFileFailed"), ""));
            return;
        }

        boolean bInput = false;
        if (null == inputstream) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UploadConfigFileFailed"), ""));
            setIIndex(2);
            return;
        }
        bInput = Util.upLoadFile(inputstream, FILE_PATH + file.getFileName());
        if (bInput) {
            if (bRestartAll) {
                if (restartAllAgent()) {
                    InterfaceFactory.getMBAInterfaceInstance().configLog(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("UploadConfigFileSuccess"), ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UploadSuccessButInstallFailed"), ""));
                }
            } else {
                InterfaceFactory.getMBAInterfaceInstance().configLog(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("UploadConfigFileSuccess"), ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UploadConfigFileFailed"), ""));
        }
        this.iIndex = 2;
    }

    private boolean restartAllAgent() {
        boolean result = true;
        for (BackupAgent agent : list) {
            if (5 == agent.getSerial()) {
                if (agent.getStatus()) {
                    result = restartPostgreSQL();
                } else {
                    result = startPostgreSQL();
                }
            }
        }

        if (!result) {
            return false;
        }

        for (BackupAgent backup : list) {
            if (0 == backup.getSerial()) {
                if (backup.getStatus()) {
                    result = restartCommonService(0);
                } else {
                    result = startCommonService(0);
                }
            }
        }

        if (!result) {
            return false;
        }

        for (BackupAgent b : list) {
            if (b.getSerial() != 0 && b.getSerial() != 5) {
                if (b.getStatus()) {
                    result = restartCommonService(b.getSerial());
                } else {
                    result = startCommonService(b.getSerial());
                }
            }
            if (!result) {
                return false;
            }
        }
        return true;
    }

    public void restartAll() {
        boolean restartAllAgent = restartAllAgent();
        init();
        if (restartAllAgent) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("RestartAllServiceSuccess"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("RestartAllServiceFailed"), ""));
        }

    }

    public StreamedContent getSystemFile() {
        return systemFile;
    }

    public StreamedContent getJobFile() {
        return jobFile;
    }

    public StreamedContent getConfigFile() {
        return configFile;
    }

    private boolean buildSystemManager() {
        MBASysManage manager = new MBASysManage(ip);
        return manager.writeJNLPFile();
    }

    private boolean buildJobManager() {
        MBAJobManage manager = new MBAJobManage(ip);
        return manager.writeJNLPFile();
    }

    private boolean buildConfigManager() {
        List<String> jarFilePath = InterfaceFactory.getMBAInterfaceInstance().getJarFilePath("/usr/local/mba/configplugins");
        if (null == jarFilePath) {
            return false;
        }

        MarsServerConfig manager = new MarsServerConfig(ip, jarFilePath);
        return manager.writeJNLPFile();
    }

    public void jumpToSys() {
        if (!buildSystemManager()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("CreateSysFileFailed"), ""));
        }
        RequestContext.getCurrentInstance().execute("window.top.location.href='MBASysManage.jnlp'");
    }

    public void jumpToJob() {
        if (!buildJobManager()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("CreateJobFileFailed"), ""));
        }
        RequestContext.getCurrentInstance().execute("window.top.location.href='MBAJobManage.jnlp'");
    }

    public void jumpToConfig() {
        if (!buildConfigManager()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("CreateServerFileFailed"), ""));
        }
        RequestContext.getCurrentInstance().execute("window.top.location.href='MarsServerConfig.jnlp'");
    }

    public void change() {
    }
}
