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
    private String usrPath;//�û�������Ŀ¼���� 
    private String fileUrl;//�ϴ��ļ������·�����ļ��� 
    private String rootPath; //����Ӧ�ó���·���ǿ��Ըĵ� 
    private UIInput fileUrlInput;//���ڰ�ǰ̨��fileurlinput�������ڶ�̬��ֵ������ã��Ժ�һ��Ҫ���ã��������� 

    /**
     * ��ʼ����
     */
    public UploadMyFile() {
        rootPath = "F:/testt123/";//�ŵ��ҵ���Ŀ¼���� 
    }

    /**
     * ��ȡ�ϴ��ļ�����д���û�ר���ļ�Ŀ¼
     *
     * @param evnt
     * @throws IOException
     */
    public void saveFile(FileUploadEvent event) {
        //�õ��ϴ����ļ� 
        uploadedFile = event.getFile();
      
        //�ҵ��û�ר���ļ�Ŀ¼�����û�о��½�һ��  
        usrPath = "uploadFiles/" + "aaa";
        File usrFilePath = new File(rootPath + usrPath);
        if (!usrFilePath.isDirectory()) {
            usrFilePath.mkdirs();
        }
        SystemOutPrintln.print_vm("usrPath=" + usrPath);

        //��������������fileupload�ж�����       
        InputStream inputStream = null;
        try {
            inputStream = uploadedFile.getInputstream();
         
        } catch (IOException ex) {
            Logger.getLogger(UploadMyFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        //���������������д��Ӳ��  
        String fileType = uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf("."));
        String fileName = uploadedFile.getFileName();

        Random r = new Random();
        //        setFileUrl(usrPath + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + r.nextInt(10) + r.nextInt(10) + r.nextInt(10) + fileType);
        setFileUrl(usrPath + "/" + fileName);
        File dir = new File(rootPath + getFileUrl());//�ϴ���д��Ӳ�̵��ļ� 
        String path =rootPath + getFileUrl();
//        System.out.println("55555fileUrl=" + rootPath + getFileUrl());

        OutputStream outputStream = null;//�����д��Ӳ�� 
        try {
            outputStream = new FileOutputStream(dir);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UploadMyFile.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(UploadMyFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        // ����Ƿ�д��ɹ����ɹ��Ļ�������ʾ    
        if (dir.isFile()) {
            FacesContext context = FacesContext.getCurrentInstance();  //session�򣬸���VMList����vmInfoListֵ
            ISOPathList iso = (ISOPathList) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{iSOPathList}", ISOPathList.class).getValue(context.getELContext());
            List<ISOPathBean> isoList = iso.getIsoList();
            ISOPathBean isoPath = new ISOPathBean();
            isoPath.setIsoPath(path);
            iso.getIsoList().add(isoPath);
            for (int i = 0; i < iso.getIsoList().size(); i++) {
                SystemOutPrintln.print_vm("ֵ=" + iso.getIsoList().get(i).getIsoPath());
            }
            ISOPathDataModel mediumISOsModel = iso.getMediumISOsModel();
            mediumISOsModel = new ISOPathDataModel(isoList);
//          FacesMessage message = new FacesMessage("�ϴ��ɹ�");
//        FacesContext.getCurrentInstance().addMessage(null, message);
//            this.setInfoMessage(null, "�ϴ��ɹ�", uploadedFile.getFileName() + " \n���ϴ��洢�ɣ�\n" + getFileUrl()); 
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
