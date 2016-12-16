/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
/**
 *
 * @author Administrator
 */
@ManagedBean(name = "fileBean")
@RequestScoped
public class FileBean implements Serializable{
    private UploadedFile file;

    /**
     * Creates a new instance of FileBean
     */
    public FileBean() {
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file1 = event.getFile();
        SystemOutPrintln.print_vm("ÉÏ´«Êä³ö¡£¡£¡£¡£¡£");
//application code
    }
}
