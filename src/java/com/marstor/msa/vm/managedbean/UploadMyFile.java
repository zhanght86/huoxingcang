/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.vm.model.ISOPathBean;
import com.marstor.msa.vm.model.ISOPathDataModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
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
@ManagedBean(name = "uploadMyFile")
@RequestScoped
public class UploadMyFile implements Serializable{

    private UploadedFile uploadedFile;
    private String usrPath;//用户个人子目录名称 
    private String fileUrl;//上传文件的相对路径和文件名 
    private String rootPath; //这里应用程序路径是可以改的 
    private UIInput fileUrlInput;//用于绑定前台的fileurlinput，可用于动态设值，真好用，以后一定要多用！！！！！ 

    /**
     * 初始化类
     */
    public UploadMyFile() {
        rootPath = "F:/testt123/";//放到我电脑目录下面 
    }

    /**
     * 读取上传文件，并写入用户专用文件目录
     *
     * @param evnt
     * @throws IOException
     */
    public void saveFile(FileUploadEvent event) {
        //得到上传的文件 
        uploadedFile = event.getFile();
      
        //找到用户专用文件目录，如果没有就新建一个  
        usrPath = "uploadFiles/" + "aaa";
        File usrFilePath = new File(rootPath + usrPath);
        if (!usrFilePath.isDirectory()) {
            usrFilePath.mkdirs();
        }
        SystemOutPrintln.print_vm("usrPath=" + usrPath);

        //配置输入流，从fileupload中读进来       
        InputStream inputStream = null;
        try {
            inputStream = uploadedFile.getInputstream();
         
        } catch (IOException ex) {
            Logger.getLogger(UploadMyFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        //配置输出流，用于写到硬盘  
        String fileType = uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf("."));
        String fileName = uploadedFile.getFileName();

        Random r = new Random();
        //        setFileUrl(usrPath + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + r.nextInt(10) + r.nextInt(10) + r.nextInt(10) + fileType);
        setFileUrl(usrPath + "/" + fileName);
        File dir = new File(rootPath + getFileUrl());//上传后写入硬盘的文件 
        String path =rootPath + getFileUrl();
//        System.out.println("55555fileUrl=" + rootPath + getFileUrl());

        OutputStream outputStream = null;//输出流写到硬盘 
        try {
            outputStream = new FileOutputStream(dir);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UploadMyFile.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(UploadMyFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        // 检查是否写入成功，成功的话给予提示    
        if (dir.isFile()) {
            FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
            ISOPathList iso = (ISOPathList) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{iSOPathList}", ISOPathList.class).getValue(context.getELContext());
            List<ISOPathBean> isoList = iso.getIsoList();
            ISOPathBean isoPath = new ISOPathBean();
            isoPath.setIsoPath(path);
            iso.getIsoList().add(isoPath);
            for (int i = 0; i < iso.getIsoList().size(); i++) {
                SystemOutPrintln.print_vm("值=" + iso.getIsoList().get(i).getIsoPath());
            }
            ISOPathDataModel mediumISOsModel = iso.getMediumISOsModel();
            mediumISOsModel = new ISOPathDataModel(isoList);
//          FacesMessage message = new FacesMessage("上传成功");
//        FacesContext.getCurrentInstance().addMessage(null, message);
//            this.setInfoMessage(null, "上传成功", uploadedFile.getFileName() + " \n被上传存储成：\n" + getFileUrl()); 
        }
        //        this.fileUrlInput.setValue(this.fileUrl); 
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
     *
     * @return
     */
    public UploadedFile getFile() {
        return uploadedFile;
    }

    /**
     *
     * @param file
     */
    public void setFile(UploadedFile file) {
        this.uploadedFile = file;
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
    
}
