/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.backup.managedbean.AgentManager;
import com.marstor.msa.backup.util.Util;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.util.ConsoleCommandLine;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "sysConf")
@ViewScoped
public class SystemConfig implements Serializable {

    private MSAResource res = new MSAResource();
    private StreamedContent configFile;
    private UploadedFile file;
    private String vmPath = "/tmp/msaback/vm";
    private String exPath = "/tmp/msaback/";
    private String imPath = "/tmp/msaback";
    private String path = "/tmp/";
    private String nasPath = "/tmp/msaback/nas";

    public SystemConfig()
    {
        this.initUser();
    }
    private String exDate;
    private String exUser;

    public String getExDate()
    {
        return exDate;
    }

    public void setExDate(String exDate)
    {
        this.exDate = exDate;
    }

    public String getExUser()
    {
        return exUser;
    }

    public void setExUser(String exUser)
    {
        this.exUser = exUser;
    }

    private void initUser()
    {
        String[] lastExport = InterfaceFactory.getCommonInterfaceInstance().getLastExport();

        if (null == lastExport)
        {
            this.exDate = res.get("noRecord");
            return;
        }

        if (lastExport.length < 2)
        {
            this.exDate = res.get("noRecord");
            return;
        }

        for (String s : lastExport)
        {
            System.out.println(s);
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(Long.valueOf(lastExport[0]));
        this.exDate = f.format(date);
        this.exUser = lastExport[1];
    }

    public StreamedContent getConfigFile()
    {
        File file = new File(exPath);
        if (file.exists())
        {
            String cmd = "rm -rf /tmp/msaback";
            ConsoleCommandLine console = new ConsoleCommandLine();
            console.executeCmdLine(cmd); 
        }
        if (!file.mkdir())
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createExportPathFailed"), ""));
            return null;
        }

        boolean bComm = InterfaceFactory.getCommonInterfaceInstance().exportCommon();
        boolean bNAS = InterfaceFactory.getNASInterfaceInstance().exportConfigFiles();
        boolean bVM = InterfaceFactory.getVMInterfaceInstance().exportConfig(vmPath);
        System.out.println(bComm);
        System.out.println(bNAS);
        System.out.println(bVM);
        if (!bComm || !bNAS || !bVM)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createConfigFileFailed"), ""));
            return null;
        }

        if (!tar())
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createTarFileFailed"), ""));
            return null;
        }

        InputStream stream = null;
        try
        {
            File msaFile = new File("/tmp/msaconfiguration.tar.gz");
            stream = new FileInputStream(msaFile);
        }
        catch (Exception ex)
        {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createInputStreamFailed"), ""));
        }

        configFile = new DefaultStreamedContent(stream, "", "msaconfiguration.tar.gz");
        return configFile;
    }

    public void setConfigFile(StreamedContent agentFile)
    {
        this.configFile = agentFile;
    }

    public UploadedFile getFile()
    {
        return file;
    }

    public void setFile(UploadedFile file)
    {
        this.file = file;
    }

    public void uploadFile()
    {
        if (0 == file.getSize())
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("NotEmptyFile"), ""));
            return;
        }

        InputStream inputstream = null;
        try
        {
            inputstream = file.getInputstream();
        }
        catch (IOException ex)
        {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createStreamFailed"), ""));
            return;
        }

        boolean bInput = false;
        bInput = Util.upLoadFile(inputstream, path + file.getFileName());
        if (bInput)
        {
            if (!unTar())
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("unTarFailed"), ""));
                return;
            }
            if (!InterfaceFactory.getCommonInterfaceInstance().importCommon())
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("commonImportFailed"), ""));
                return;
            }
            if (!InterfaceFactory.getNASInterfaceInstance().importConfigFiles(imPath))
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("nasImportFailed"), ""));
                return;
            }
            if (!InterfaceFactory.getVMInterfaceInstance().importConfig(vmPath))
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("vmImportFailed"), ""));
                return;
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("importConfigSuccess"), ""));
        }
        else
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("configFileWriteFailed"), ""));
        }

    }

    public boolean tar()
    {
        File file = new File("/tmp/msaconfiguration.tar");
        if (file.exists())
        {
            file.delete();
        }

        file = new File("/tmp/msaconfiguration.tar.gz");
        if (file.exists())
        {
            file.delete();
        }

        String cmd = "";
        ConsoleCommandLine console = new ConsoleCommandLine();
        String errorInfo;

        cmd = "/usr/bin/tar cvf /tmp/msaconfiguration.tar /tmp/msaback";
        if (console.executeCmdLine(cmd) != ConsoleCommandLine.RET_OK)
        {
            //log
            errorInfo = console.getErrorString();
            return false;
        }

        cmd = "/usr/bin/gzip -r /tmp/msaconfiguration.tar";
        if (console.executeCmdLine(cmd) != ConsoleCommandLine.RET_OK)
        {
            //log
            errorInfo = console.getErrorString();
            return false;
        }

        cmd = "rm -rf /tmp/msaback";
        if (console.executeCmdLine(cmd) != ConsoleCommandLine.RET_OK)
        {
            //log
            errorInfo = console.getErrorString();
            return false;
        }

        return true;
    }

    public boolean unTar()
    {
        File file = new File("/tmp/msaconfiguration.tar");
        ConsoleCommandLine console = new ConsoleCommandLine();
        if (file.exists())
        {
            file.delete();
        }

        ArrayList<String> cmd = new ArrayList<String>();
        cmd.add("cd /");
        cmd.add("/usr/bin/gunzip /tmp/msaconfiguration.tar.gz");
        cmd.add("/usr/bin/tar -xvf /tmp/msaconfiguration.tar");
        cmd.add("rm -rf /tmp/msaconfiguration.tar");
        if (console.executeByWriteFile(cmd) != ConsoleCommandLine.RET_OK)
        {
            //log
            String errorInfo = console.getErrorString();
            System.out.println(errorInfo);
            return false;
        }
        return true;
    }

    public void exportInfo()
    {
        String[] lastExport = InterfaceFactory.getCommonInterfaceInstance().getLastExport();
        for (String s : lastExport)
        {
            System.out.println(s);
        }
    }

    public void finish()
    {
        System.out.println("...........................................................................");
        configFile = null;
    }
}
