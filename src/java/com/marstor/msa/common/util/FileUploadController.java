/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.util;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "fileUploadController")
@RequestScoped
public class FileUploadController {

    private UploadedFile file;
    private String rootPath = "F:/workspace/msa234";

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void upload() {
       
         SystemOutPrintln.print_common("Uploaded: "+file.getFileName());
        if (file != null) {
            FacesMessage msg = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
//    private UploadedFile uploadedFile;
    private String usrPath;//�û�������Ŀ¼����  
    private String fileUrl;//�ϴ��ļ������·�����ļ���   
    private UIInput fileUrlInput;//���ڰ�ǰ̨��fileurlinput�������ڶ�̬��ֵ����������ã��Ժ�һ��Ҫ���ã��������� 

    /**
     * ��ʼ����
     */
    public FileUploadController() {
        rootPath = "F:/workspace/msa234";
    }

    /**
     * ��ȡ�ϴ��ļ�����д���û�ר���ļ�Ŀ¼
     *
     * @param evnt
     * @throws IOException
     */
    public void handle() {
        
         FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String i =  params.get("file");
        SystemOutPrintln.print_common("i"+i);
      
        //�õ��ϴ����ļ�
//        file = event.getFile(); //�ҵ��û�ר���ļ�Ŀ¼�����û�о��½�һ��    
//        + this.getSession().getAttribute("usrid").toString()
        usrPath = "/uploadFiles" ;
        File usrFilePath = new File(rootPath + usrPath);
        if (!usrFilePath.isDirectory()) {
            this.setInfoMessage(null, "�㻹û��ר���ļ�Ŀ¼", null);
            if (usrFilePath.mkdirs()) {
                this.setInfoMessage(null, "��Ϊ�㴴��ר���ļ�Ŀ¼" + usrPath, null);
            }
        }

        //��������������fileupload�ж�����   
        InputStream inputStream = null;
        try {
            inputStream = file.getInputstream();
        } catch (IOException ex) {
            Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //���������������д��Ӳ��   
        String fileType = file.getFileName().substring(file.getFileName().lastIndexOf("."));
        Random r = new Random();
        setFileUrl(usrPath + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + r.nextInt(10) + r.nextInt(10) + r.nextInt(10) + fileType);
        File dir = new File(rootPath + getFileUrl());//�ϴ���д��Ӳ�̵��ļ�     
        OutputStream outputStream = null;//�����д��Ӳ��      
        try {
            outputStream = new FileOutputStream(dir);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //��ȡ�����������������д��Ӳ��    
        try {
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // ����Ƿ�д��ɹ����ɹ��Ļ�������ʾ   
        if (dir.isFile()) {
            this.setInfoMessage(null, "�ϴ��ɹ�", file.getFileName() + " \n���ϴ��洢�ɣ�\n" + getFileUrl());
        }
        this.fileUrlInput.setValue(this.fileUrl);
    }
    
     /**    
      * @return the fileUrl  
      */   
    public String getFileUrl() {    
        return fileUrl;   
    } 
    
    /**   
     * @param fileUrl the fileUrl to set     
     */   
    public void setFileUrl(String fileUrl) {  
        this.fileUrl = fileUrl;   
    } 
    
          
    /**   
     * @return the fileUrlInput  
     */    
    public UIInput getFileUrlInput() {  
    
        return fileUrlInput;   
    } 
     /**  
      * @param fileUrlInput the fileUrlInput to set  
      */  
    public void setFileUrlInput(UIInput fileUrlInput) { 
        this.fileUrlInput = fileUrlInput; 
    } 
    
    public void setInfoMessage( String i,String summary,String detail){
        FacesMessage msg = new FacesMessage(summary,detail);     
        FacesContext.getCurrentInstance().addMessage(i, msg);  
    }
}
