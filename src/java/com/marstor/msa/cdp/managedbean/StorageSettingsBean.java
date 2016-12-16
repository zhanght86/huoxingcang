/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.common.util.MSAResource;
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
@ManagedBean(name = "storageSettingsBean")
@ViewScoped
public class StorageSettingsBean  implements Serializable{
    public String fileSystemName;  //磁盘区名
    public boolean isSetQuota;  //启用配额管理
    public boolean isSetDedup;  //重复数据删除
    public String compress = 6+"";  //压缩级别
    public boolean isVerify = false;  //数据校验
    public String recordsize = "128KB";  //块大小
    public String quotaValue;  //最大分配空间
    public boolean isCompress;  //启用数据压缩
    public boolean notVerify;  //数据校验是否可用
    public boolean notCompress;  //压缩级别是否可用
    public boolean notQuota;  //配额管理是否可用
    private ArrayList<String> blockSize = new ArrayList<String>();
    public int minQuotaValue;  //修改的值最小不能小于多少

    /**
     * Creates a new instance of SetFileSystemBean
     */
    public StorageSettingsBean() {
        this.initBlockSize();
        this.initSetFileSystem();
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
            System.out.println("compress="+request.getParameter("compress"));
        }else{
            compress = request.getParameter("compress");
            System.out.println("12compress="+request.getParameter("compress"));
        }
        
        String boolSetQuota = request.getParameter("isSetQuota");
        if (boolSetQuota.equalsIgnoreCase("true")) {
            isSetQuota = true;
        } else {
            isSetQuota = false;
        }
        if(request.getParameter("quotaValue").equals("null") ||request.getParameter("quotaValue").equals("") ||request.getParameter("quotaValue").equalsIgnoreCase("none")){
             System.out.println("quotaValue="+request.getParameter("quotaValue"));
            quotaValue = "";
        } else {
            quotaValue = request.getParameter("quotaValue");
        }
        if (request.getParameter("recordsize").equals("null")|| request.getParameter("recordsize").equals("")) {
            System.out.println("recordsize="+request.getParameter("recordsize") );
            recordsize = "128KB";  //块大小
        } else {
             System.out.println("12recordsize="+request.getParameter("recordsize") );
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
        System.out.println("isVerify="+isVerify);
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
       System.out.println("this.isVerify="+this.isVerify);
        if (this.isSetDedup) {
            this.notVerify = false;
//                this.isVerify = false; 
             System.out.println("this.isVerify1="+this.isVerify);
        } else {
            this.notVerify = true;                
//                this.isVerify = false;          
             System.out.println("this.isVerify2="+this.isVerify);
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
    
    public String saveFileSet() {
        MSAResource resources = new MSAResource();
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
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.filesystem_set", "value_no"), ""));
                return null;
            }
            if(!ValidateUtility.checkNOTLessSize(fileSize, false, minQuotaValue)){
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.filesystem_set", "value_limit")+minQuotaValue+resources.get("disk.filesystem_set", "gb"),"" ));
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
        boolean dehup = zfs.setDedup(fileSystemName, dedupStr);  //重复数据删除选项 on：开启 off：关闭 verify：数据校验 
       
        int compressInt = 0;
        if (isCompress) {
            compressInt = Integer.valueOf(compress.trim());
        } else {
            compressInt = 0;
        }
        System.out.println("compressInt="+compressInt);
        boolean compress = zfs.setCompress(fileSystemName, compressInt);  //压缩级别 0-9, off即0，0表示关闭 

        int quotaInt = 0;
        if(isSetQuota){
            quotaInt = Integer.valueOf(quotaValue);
        }else{
            quotaInt = 0;
        }
        boolean quota = zfs.setQuota(fileSystemName,quotaInt);  //配额大小，单位G 0表示不启用配额 
        boolean recordSize = zfs.setRecordSize(fileSystemName,recordsize);  //设置块大小 
        String returnStr = null;
        String mess = "";
        if(dehup==true && compress==true && quota== true && recordSize==true){
            returnStr = "diskarea?faces-redirect=true";
        }else{
            if(dehup == false){
                mess =mess+ resources.get("disk.filesystem_set", "dedup_tip")+" ";
            }else if(compress == false){
                mess =mess+ resources.get("disk.filesystem_set", "compress_tip")+" ";
            }else if(quota == false){
                mess =mess+ resources.get("disk.filesystem_set", "quota_tip")+" ";
            }else if(recordSize == false){
                mess =mess+ resources.get("disk.filesystem_set", "recordsize_tip");
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.filesystem_set", "set")+mess+resources.get("disk.filesystem_set", "fail"), ""));

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
    
     public int minSize(String used) {  //单位为G
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
                System.out.println("sizeStr="+sizeStr);
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
