/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class ConfigFileSystem implements Serializable{
     public String fileSystemName;  //��������
    public boolean isSetQuota;  //����������
    public boolean isSetDedup;  //�ظ�����ɾ��
    public String compress = 6+"";  //ѹ������
    public boolean isVerify = false;  //����У��
    public String recordsize = "128KB";  //���С
    public String quotaValue;  //������ռ�
    public boolean isCompress;  //��������ѹ��
    public boolean notVerify;  //����У���Ƿ����
    public boolean notCompress;  //ѹ�������Ƿ����
    public boolean notQuota;  //�������Ƿ����
    private ArrayList<String> blockSize = new ArrayList<String>();
    public int minQuotaValue;  //�޸ĵ�ֵ��С����С�ڶ���

    /**
     * Creates a new instance of SetFileSystemBean
     */
    public ConfigFileSystem() {
        initBlockSize();
        initSetFileSystem();
    }
    public void initSetFileSystem(){
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        fileSystemName = request.getParameter("fileSystemName");
        String boolSetDedup = request.getParameter("isSetDedup");
        if (boolSetDedup.equalsIgnoreCase("true")) {
            isSetDedup = true;
        } else {
            isSetDedup = false;
        }
        String boolVerify = request.getParameter("isVerify");
        if (boolVerify.equalsIgnoreCase("true")) {
            isVerify = true;
        } else {
            isVerify = false;
        }
        String boolCompress = request.getParameter("isCompress");
        if (boolCompress.equalsIgnoreCase("true")) {
            isCompress = true;
        } else {
            isCompress = false;
        }
        
        if(request.getParameter("compress").equals("null")||request.getParameter("compress").equals("") || request.getParameter("compress").equals("0")){
            compress = 6+"";
            Debug.print("compress="+request.getParameter("compress"));
        }else{
            compress = request.getParameter("compress");
            Debug.print("12compress="+request.getParameter("compress"));
        }
//        if(request.getParameter("compress").equals("null")||request.getParameter("compress").equals("") ||request.getParameter("compress").equalsIgnoreCase("off")){
//            compress = 6+"";
//            Debug.print("compress="+request.getParameter("compress"));
//        }else{
//            compress = request.getParameter("compress");
//            Debug.print("12compress="+request.getParameter("compress"));
//        }
        
        String boolSetQuota = request.getParameter("isSetQuota");
        if (boolSetQuota.equalsIgnoreCase("true")) {
            isSetQuota = true;
        } else {
            isSetQuota = false;
        }
        if(request.getParameter("quotaValue").equals("null") ||request.getParameter("quotaValue").equals("") ||request.getParameter("quotaValue").equalsIgnoreCase("none")){
             Debug.print("quotaValue="+request.getParameter("quotaValue"));
            quotaValue = "";
        } else {
            quotaValue = request.getParameter("quotaValue");
        }
        if (request.getParameter("recordsize").equals("null")|| request.getParameter("recordsize").equals("")) {
            Debug.print("recordsize="+request.getParameter("recordsize") );
            recordsize = "128KB";  //���С
        } else {
             Debug.print("12recordsize="+request.getParameter("recordsize") );
            recordsize = request.getParameter("recordsize")+"B";
        }
        
        String used = request.getParameter("used");
        minQuotaValue = minSize(used);
         
        if (this.isSetDedup) {
            this.notVerify = false;
        } else {
            this.notVerify = true;
        }
//        changDuplicate();
        changeCompress();
        changeQuota();
             
    }

    public ArrayList<String> getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(ArrayList<String> blockSize) {
        this.blockSize = blockSize;
    }

    public String getFileSystemName() {
        return fileSystemName;
    }

    public void setFileSystemName(String fileSystemName) {
        this.fileSystemName = fileSystemName;
    }

    public boolean isIsSetQuota() {
        return isSetQuota;
    }

    public void setIsSetQuota(boolean isSetQuota) {
        this.isSetQuota = isSetQuota;
    }

    public boolean isIsSetDedup() {
        return isSetDedup;
    }

    public void setIsSetDedup(boolean isSetDedup) {
        this.isSetDedup = isSetDedup;
    }

    public String getCompress() {
        return compress;
    }

    public void setCompress(String compress) {
        this.compress = compress;
    }

    
    public boolean getIsVerify() {
        Debug.print("isVerify="+isVerify);
        return isVerify;
    }

    public void setIsVerify(boolean isVerify) {
        this.isVerify = isVerify;
    }

    public String getRecordsize() {
        return recordsize;
    }

    public void setRecordsize(String recordsize) {
        this.recordsize = recordsize;
    }

    public int getMinQuotaValue() {
        return minQuotaValue;
    }

    public void setMinQuotaValue(int minQuotaValue) {
        this.minQuotaValue = minQuotaValue;
    }

    public String getQuotaValue() {
        return quotaValue;
    }

    public void setQuotaValue(String quotaValue) {
        this.quotaValue = quotaValue;
    }

    public boolean isIsCompress() {
        return isCompress;
    }

    public void setIsCompress(boolean isCompress) {
        this.isCompress = isCompress;
    }

    public boolean isNotVerify() {
        return notVerify;
    }

    public void setNotVerify(boolean notVerify) {
        this.notVerify = notVerify;
    }

    public boolean isNotCompress() {
        return notCompress;
    }

    public void setNotCompress(boolean notCompress) {
        this.notCompress = notCompress;
    }

    public boolean isNotQuota() {
        return notQuota;
    }

    public void setNotQuota(boolean notQuota) {
        this.notQuota = notQuota;
    }
    
    public void changDuplicate() {
       boolean isV = this.isVerify;
       Debug.print("this.isVerify="+this.isVerify);
        if (this.isSetDedup) {
            this.notVerify = false;
           
//                this.isVerify = false;
            
             Debug.print("this.isVerify1="+this.isVerify);
        } else {
            this.notVerify = true;
            
         
//                this.isVerify = false;
            
             Debug.print("this.isVerify2="+this.isVerify);
        }
    }

    public void changeCompress() {
        if (this.isCompress) {
            this.notCompress = false;
        } else {
            this.notCompress = true;
        }
    }

    public void changeQuota() {
        if (this.isSetQuota) {
            this.notQuota = false;
        } else {
            this.notQuota = true;
        }
    }

    public void initBlockSize() {
        blockSize.add("4KB");
        blockSize.add("8KB");
        blockSize.add("16KB");
        blockSize.add("32KB");
        blockSize.add("64KB");
        blockSize.add("128KB");
    }
    
    public String save() {
        boolean flag = false;
        double size = 0;
        if (isSetQuota) {
            String fileSize = quotaValue;
            try {
                size = Double.valueOf(fileSize);
                flag = true;
            } catch (Exception e) {
                flag = false;
            }
            if (!flag || size <= 0 || size > 999999) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "��Ч�Ŀռ��С��", ""));
                return null;
            }
            Debug.print("fileSize="+fileSize);
            Debug.print("minQuotaValue="+minQuotaValue);
            if(!ValidateUtility.checkNOTLessSize(fileSize, false, minQuotaValue)){
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "������ռ�ֵ��Ϊ�������Ҳ���С��"+minQuotaValue+"GB��","" ));
                return null;
            }
          
        }
        
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        String dedupStr = "off";
        if (isSetDedup) {
            if(isVerify){
                dedupStr = "verify";
            }else{
                dedupStr = "on";
            }
        } else {
            dedupStr = "off";
        }
        boolean dehup = zfs.setDedup(fileSystemName, dedupStr);  //�ظ�����ɾ��ѡ�� on������ off���ر� verify������У�� 
       
        int compressInt = 0;
        if (isCompress) {
            compressInt = Integer.valueOf(compress.trim());
        } else {
            compressInt = 0;
        }
        Debug.print("compressInt="+compressInt);
        boolean compress = zfs.setCompress(fileSystemName, compressInt);  //ѹ������ 0-9, off��0��0��ʾ�ر� 

        int quotaInt = 0;
        if(isSetQuota){
            quotaInt = Integer.valueOf(quotaValue);
        }else{
            quotaInt = 0;
        }
        boolean quota = zfs.setQuota(fileSystemName,quotaInt);  //����С����λG 0��ʾ��������� 
        Debug.print("recordsize1="+recordsize);
           recordsize = recordsize.substring(0, recordsize.length() - 1);  //ȥ��B
        Debug.print("recordsize11="+recordsize);
        boolean recordSize = zfs.setRecordSize(fileSystemName,recordsize);  //���ÿ��С 
        String returnStr = null;
        String mess = "";
        if(dehup==true && compress==true && quota== true && recordSize==true){
            returnStr = "nas_storage_space?faces-redirect=true";
        }else{
            if(dehup == false){
                mess =mess+ "�ظ�����ɾ��������У�� ";
            }else if(compress == false){
                mess =mess+ "����ѹ�� ";
            }else if(quota == false){
                mess =mess+ "������ ";
            }else if(recordSize == false){
                mess =mess+ "���С";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "����"+mess, "ʧ�ܡ�"));

           returnStr = null;
        }
        return returnStr;


    }
    
    
    public int compressLevel(String Compress){
        if (null == Compress || "".equalsIgnoreCase(Compress)) {
            return 0;
        }
        if ("off".equalsIgnoreCase(Compress)) {
            return 0;
        }
        if ("gzip".equalsIgnoreCase(Compress)) {
            return 6;
        }
        char num = Compress.charAt(Compress.length()-1);
        try {
            return Integer.valueOf(num + "");
        } catch (Exception e) {
            return 0;
        }
    }
    
     public int minSize(String used) {  //��λΪG
        int trueSize = 2;
        double size = Double.valueOf(used.substring(0, used.length() - 1));
        if (used.endsWith("G")) {
            size = size;
        }
        if (used.endsWith("M")) {
            size = size / 1024;
        }
        if (used.endsWith("K")) {
            size = size / (1024 * 1024);
        }
        if (used.endsWith("B")) {
            size = size / (1024 * 1024 * 1024);
        }
        if (used.endsWith("T")) {
            size = size * 1024;
        }
        if (used.endsWith("P")) {
            size = size * 1024 * 1024;
        }
        if (size < 1) {
            trueSize = 2;
        } else {
            String sizeStr = size + "";
            if (sizeStr.contains(".")) {
                Debug.print("sizeStr="+sizeStr);
                String preSzie = sizeStr.split("\\.")[0];
                String suffSzie = sizeStr.split("\\.")[1];
                if (suffSzie.equalsIgnoreCase("0")) {
                    trueSize = Integer.valueOf(preSzie) + 1;
                } else {
                    trueSize = Integer.valueOf(preSzie) + 2;
                }
            } else {
                size = size + 1;
                trueSize = Integer.valueOf(size+"") ;
            }
        }

        return trueSize;
    }
    
}
