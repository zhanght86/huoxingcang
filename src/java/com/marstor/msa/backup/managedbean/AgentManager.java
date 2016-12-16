/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.managedbean;

import com.marstor.msa.ba.bean.MBAAgent;
import com.marstor.msa.backup.model.BackupAgent;
import com.marstor.msa.backup.model.ClientAgent;
import com.marstor.msa.backup.model.MBAAgentPackageXML;
import com.marstor.msa.backup.util.Util;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "bAgentManager")
@ViewScoped
public class AgentManager implements Serializable {
    private MSAResource res = new MSAResource();
    private StreamedContent agentFile;
    private final String FILE_PATH = "/usr/local/mba/SetupPackage/";
    private List<BackupAgent> agents;
    private List<ClientAgent> clientList = null;
    private ClientAgent selected;
    private InputStream inputstream = null;
    private String fileName = null;
    private UploadedFile file;
    private boolean bCover = true;
    private int iIndex = 0;

    public int getIIndex() {
        return iIndex;
    }

    public void setIIndex(int iIndex) {
        this.iIndex = iIndex;
    }
    public boolean getBCover() {
        return bCover;
    }

    public void setBCover(boolean bCover) {
        this.bCover = bCover;
    }

    public AgentManager() {
        init();
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getFilePath() {
        return FILE_PATH;
    }

    public void setSelected(ClientAgent s) {
        this.selected = s;
    }

    public ClientAgent getSelected() {
        return selected;
    }

    public StreamedContent getAgentFile() {
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(FILE_PATH + selected.getStrAgentName()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        agentFile = new DefaultStreamedContent(stream, "", selected.getStrAgentName());
        InterfaceFactory.getMBAInterfaceInstance().downloadAgentLog(selected.getStrAgentName(), true);
        return agentFile;
    }

    public void setClientList(List<ClientAgent> list) {
        this.clientList = list;
    }

    public List<ClientAgent> getClientList() {
        return clientList;
    }

    private void init() {
        clientList = new ArrayList<ClientAgent>();
        ClassLoader classLoader = this.getClass().getClassLoader();
        List<MBAAgentPackageXML> readMBAAgentPakageXML = Util.readMBAAgentPakageXML(classLoader);

        List<MBAAgent> mbaAgentList = InterfaceFactory.getMBAInterfaceInstance().getAgent();
        if (mbaAgentList == null) {
            return;
        }

        int i = 1;
        for (MBAAgent mbaAgent : mbaAgentList) {
            ClientAgent client = new ClientAgent();
            client.setSerial(i);
            client.setStrAgentName(mbaAgent.getStrAgentName());
            client.setStrAgentVersion(mbaAgent.getStrAgentVersion());
            client.setStrAgentDate(mbaAgent.getStrAgentDate());
            client.setStrAgentModifyDate(mbaAgent.getStrAgentModifyDate());
            clientList.add(client);
            i++;
        }

        for (ClientAgent c : clientList) {
            for (MBAAgentPackageXML mbaAgentXML : readMBAAgentPakageXML) {
                if (c.getStrAgentName().equalsIgnoreCase(mbaAgentXML.getStrAgentName())) {
                    c.setStrAgentOS(mbaAgentXML.getStrAgentOS());
                    c.setStrAgentPlatform(mbaAgentXML.getStrAgentPlatform());
                    c.setStrDescription(mbaAgentXML.getStrDescription());
                }
            }
        }
    }

    public void setAgents(List<BackupAgent> agents) {
        this.agents = agents;
    }

    public List<BackupAgent> getAgents() {
        return agents;
    }

    public void handleFileUpload() {
        if (0 == file.getSize()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("NotEmptyFile"), ""));
            setIIndex(1);
            return;
        }

        fileName = file.getFileName();
        try {
            inputstream = file.getInputstream();
        } catch (IOException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
            setIIndex(1);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getUploadFileFailed"), ""));
            return;
        }

        ClassLoader classLoader = this.getClass().getClassLoader();
        List<MBAAgentPackageXML> readMBAAgentPakageXML = Util.readMBAAgentPakageXML(classLoader);

        boolean bContain = false;
        for (MBAAgentPackageXML agent : readMBAAgentPakageXML) {
            if (fileName.contains(agent.getStrAgentName())) {
                bContain = true;
            }
        }

        if (!bContain) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("PleaseUploadRightAgent"), ""));
            return;
        }


        File f = new File(FILE_PATH + fileName);
        if (f.exists()) {
            if (!bCover) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("fileExist"), ""));
                return;
            }
        }

        boolean bInput = false;
        bInput = Util.upLoadFile(inputstream, FILE_PATH + fileName);
        if (bInput) {
            InterfaceFactory.getMBAInterfaceInstance().uploadAgentLog(fileName, true);
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("UploadAgentSuccess"), ""));
        } else {
            InterfaceFactory.getMBAInterfaceInstance().uploadAgentLog(fileName, false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UploadAgentFailed"), ""));
        }
    }

    public void uploadFile() {
        RequestContext.getCurrentInstance().execute("confirm.hide();");
        boolean bInput = false;
        bInput = Util.upLoadFile(inputstream, FILE_PATH + fileName);
        if (bInput) {
            InterfaceFactory.getMBAInterfaceInstance().uploadAgentLog(fileName, true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("UploadAgentSuccess"), ""));
        } else {
            InterfaceFactory.getMBAInterfaceInstance().uploadAgentLog(fileName, false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UploadAgentFailed"), ""));
        }
    }
    
    public void test(){
        ClientAgent c = new ClientAgent();
        c.setStrAgentDate("123123123");
        clientList.add(c);
        
    }
}
