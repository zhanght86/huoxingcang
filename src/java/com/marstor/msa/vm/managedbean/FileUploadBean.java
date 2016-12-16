/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.backup.util.Util;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.web.MsaVMInterface;
import com.marstor.msa.vm.model.ISOPathBean;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "fileUploadBean")
@ViewScoped
public class FileUploadBean implements Serializable{

    private final String FILE_PATH = "/SYSVOL/SYSTEM/ISO/";
//   private final String FILE_PATH = "F:\\";
    String fileName;
    private MSAResource res = new MSAResource();
    private String basename = "vm.fileUpload";
    private UploadedFile file;
    public boolean bCover = true;
    private MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();

    /**
     * Creates a new instance of FileUploadBean
     */
    public FileUploadBean() {
    }

    public String getFilePath() {
        return FILE_PATH;
    }

    //������Ч�ַ�
    private boolean existInvalidation(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        int pos = -1;
        if (str.startsWith("//") || str.startsWith("\\\\")) {
            str = str.substring(1);
        }
        if (str.indexOf("//") >= 0
                || str.indexOf("\\\\") >= 0
                || ((pos = str.indexOf(":")) >= 0 && pos != 1)
                || str.indexOf("*") >= 0
                || str.indexOf("?") >= 0
                || str.indexOf("\"") >= 0
                || str.indexOf("<") >= 0
                || str.indexOf(">") >= 0
                || str.indexOf("|") >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public void handleFileUpload() {
        if (0 == file.getSize()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null"), ""));
            return;
        }

        boolean mk = vmface.mkdirUploadDirectory();
        if (!mk) {
            SystemOutPrintln.print_vm("mkdirUploadDirectory false");
        } else {
            SystemOutPrintln.print_vm("mkdirUploadDirectory false");
        }

        fileName = file.getFileName();

//        if ("".equals(fileName)) {
//             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Դ·������Ϊ�գ�", ""));
//            return;
//        }
//  
//        if (existInvalidation(fileName)) {
//             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Դ·���к��зǷ��ַ���", ""));
//            return;
//        }
//        if(!fileName.endsWith(".iso")){
//             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Դ·���зǾ����ļ�����׺��Ϊ��.iso������", ""));
//            return;
//        }
        List<String> getISOInfos = vmface.getISOInfo();
//        boolean flag = false;
        SystemOutPrintln.print_vm("�ϴ� FILE_PATH+fileName=" + FILE_PATH + fileName);
        if (getISOInfos != null && getISOInfos.size() > 0) {
            for (String iso : getISOInfos) {
                SystemOutPrintln.print_vm("�ϴ� iso=" + iso);

                if ((FILE_PATH + fileName).equals(iso)) {
                    if (!bCover) {
                        /////
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "replacetip"), ""));
                        return;
                    }
                }

            }
        }

        handleFileUpload_real();

//        File sourceDir = new File(fileName);
//
//
//        if (Constants.stringContainsCharNotAscii(sourceDir.getName())) {
//            Constants.showErrorMessage(ResourceBundle.getBundle("com/marstor/msa/dialog/resources/UploadDirectDialog").getString("Դ�ļ����к��з�ASCII�ַ�"));
//            return;
//        }
//        InputStream inputstream = null;
//        try {
//            inputstream = event.getFile().getInputstream();
//        } catch (IOException ex) {
//         
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "�ϴ�����ʧ�ܡ�", ""));
//            return;
//        }
//
//        boolean bInput = false;
//        if (null == inputstream) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "�ϴ�����ʧ�ܡ�", ""));
//            return;
//        }
//        bInput = Util.upLoadFile(inputstream, FILE_PATH + fileName);
//        if (bInput) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "�ϴ����óɹ���", ""));
//        } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "�ϴ�����ʧ�ܡ�", ""));
//        }
    }

    public void handleFileUpload_real() {
        InputStream inputstream = null;
        try {
            inputstream = file.getInputstream();
        } catch (IOException ex) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), ""));
            return;
        }

        boolean bInput = false;
        if (null == inputstream) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), ""));
            return;
        }
        bInput = Util.upLoadFile(inputstream, FILE_PATH + fileName);
        this.vmface.uploadLog(fileName, bInput);
        if (bInput) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "succeed"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), ""));
        }

    }

    public String cancleButton() {
        return "vm_isoInfoTable?faces-redirect=true";
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public boolean isbCover() {
        return bCover;
    }

    public void setbCover(boolean bCover) {
        this.bCover = bCover;
    }

}
