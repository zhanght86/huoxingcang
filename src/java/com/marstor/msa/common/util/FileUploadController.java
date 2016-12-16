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
    private String usrPath;//用户个人子目录名称  
    private String fileUrl;//上传文件的相对路径和文件名   
    private UIInput fileUrlInput;//用于绑定前台的fileurlinput，可用于动态设值，真它妈好用，以后一定要多用！！！！！ 

    /**
     * 初始化类
     */
    public FileUploadController() {
        rootPath = "F:/workspace/msa234";
    }

    /**
     * 读取上传文件，并写入用户专用文件目录
     *
     * @param evnt
     * @throws IOException
     */
    public void handle() {
        
         FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String i =  params.get("file");
        SystemOutPrintln.print_common("i"+i);
      
        //得到上传的文件
//        file = event.getFile(); //找到用户专用文件目录，如果没有就新建一个    
//        + this.getSession().getAttribute("usrid").toString()
        usrPath = "/uploadFiles" ;
        File usrFilePath = new File(rootPath + usrPath);
        if (!usrFilePath.isDirectory()) {
            this.setInfoMessage(null, "你还没有专用文件目录", null);
            if (usrFilePath.mkdirs()) {
                this.setInfoMessage(null, "已为你创建专用文件目录" + usrPath, null);
            }
        }

        //配置输入流，从fileupload中读进来   
        InputStream inputStream = null;
        try {
            inputStream = file.getInputstream();
        } catch (IOException ex) {
            Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //配置输出流，用于写到硬盘   
        String fileType = file.getFileName().substring(file.getFileName().lastIndexOf("."));
        Random r = new Random();
        setFileUrl(usrPath + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + r.nextInt(10) + r.nextInt(10) + r.nextInt(10) + fileType);
        File dir = new File(rootPath + getFileUrl());//上传后写入硬盘的文件     
        OutputStream outputStream = null;//输出流写到硬盘      
        try {
            outputStream = new FileOutputStream(dir);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //读取输入流，并由输出流写入硬盘    
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

        // 检查是否写入成功，成功的话给予提示   
        if (dir.isFile()) {
            this.setInfoMessage(null, "上传成功", file.getFileName() + " \n被上传存储成：\n" + getFileUrl());
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
