/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.managedbean;

import com.marstor.msa.backup.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "bConfigUpload")
@ViewScoped
public class ConfigUpload implements Serializable{

    private final String FILE_PATH = "/usr/local/mba/";

    public ConfigUpload(){}
    
    
    public String getFilePath() {
        return FILE_PATH;
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        
        
//        if(file == null){
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Util.getString("PleaseUploadCalledMarsServer"), ""));
//            return;
//        }
        
        
        String fileName = file.getFileName();
        if(!fileName.substring(fileName.length()-14,fileName.length()).equalsIgnoreCase("MarsServer.xml")){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Util.getString("PleaseUploadCalledMarsServer"), ""));
            return;
        }
        InputStream inputstream = null;
        try {
            inputstream = file.getInputstream();
        } catch (IOException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Util.getString("UploadConfigFileFailed"), ""));
            return;
        }
        boolean bInput = false;
        if (null == inputstream) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Util.getString("UploadConfigFileFailed"), ""));
            return;
        }
        bInput = Util.upLoadFile(inputstream, FILE_PATH + fileName);
        if (bInput) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Util.getString("UploadConfigFileSuccess"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Util.getString("UploadConfigFileFailed"), ""));
        }
    }
}
