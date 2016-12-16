/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.managedbean;

import com.marstor.msa.backup.util.Util;
import com.marstor.msa.common.treeNode.TreeBean;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.util.ConsoleCommandLine;
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
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "bUpdateProgram")
@ViewScoped
public class UpdateProgram implements Serializable {

    private String FILE_PATH = "/SYSVOL/SYSTEM/tmp/";
    private UploadedFile file;
    String strName = "";
    private boolean bInstall = true;
    private MSAResource res = new MSAResource();

    public boolean getBInstall() {
        return bInstall;
    }

    public void setBInstall(boolean bInstall) {
        this.bInstall = bInstall;
    }

    public UpdateProgram() {
    }

    public String getFilePath() {
        return FILE_PATH;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void handleFileUpload() {
        if (0 == file.getSize()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("NotEmptyFile"), ""));
            return;
        }

        InputStream inputstream = null;
        try {
            inputstream = file.getInputstream();
        } catch (IOException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UpdateFailed"), ""));
            return;
        }

        boolean bInput = false;
        if (null == inputstream) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("UpdateFailed"), ""));
            return;
        }
        bInput = Util.upLoadFile(inputstream, FILE_PATH + file.getFileName());
        if (bInput) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (bInstall) {
                ConsoleCommandLine console = new ConsoleCommandLine();

                int execute = console.executeByWriteFile(installScript());
                if (0 == execute) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Util.getString("InstallSuccess"), ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Util.getString("InstallFailed"), ""));
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Util.getString("UpdateFailed"), ""));
        }
    }

    private List installScript() {
        List list = new ArrayList();
        list.add("cd /SYSVOL/SYSTEM/tmp");
        list.add("tar -xvf setup.tar");
        list.add("chmod 777 /SYSVOL/SYSTEM/tmp/setup/Setup.sh");
        list.add("cd /SYSVOL/SYSTEM/tmp/setup");
        list.add("./Setup.sh");
        return list;
    }

    public void test() {
        System.out.println("lucy is lucy.");
    }
}
