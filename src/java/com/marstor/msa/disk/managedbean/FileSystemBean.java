/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.disk.managedbean;

import com.marstor.msa.common.bean.FileSystemInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.disk.model.FileSystemInfo;
import com.marstor.msa.disk.model.VirtualDetail;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "fileSystemBean")
@ViewScoped
public class FileSystemBean implements Serializable{
    FileSystemInfo file;
    public FileSystemInfo selectFile;
    public List<FileSystemInfo> fileList;
     public List<VirtualDetail> detail;
    VirtualDetail fileDetail;

    /**
     * Creates a new instance of FileSystemBean
     */
    public FileSystemBean() {
        initDiskPoolInfo();
    }
    public void initDiskPoolInfo(){
        MSAResource resources = new MSAResource();
        fileList = new ArrayList();

        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        String[] volumeNames = zfs.getVolumeNames();
        if(volumeNames != null && volumeNames.length>0){
            for(int i=0; i<volumeNames.length; i++){
                 FileSystemInformation fileinfo = zfs.getFileSystemInformation(volumeNames[i]+"/DISK");
                 if(fileinfo != null){
                     file = new FileSystemInfo();
                     file.setName(volumeNames[i]+"/DISK");
                     file.setMountpoint(fileinfo.getMountpoint());
                     file.setUsed(fileinfo.getUsed());
                     file.setUsedStr(fileinfo.getUsed()+"B");
                     file.setAvailable(fileinfo.getAvailable()+"B");
                     file.setIsSetDedup(fileinfo.isIsSetDedup());
                     file.setIsVerify(fileinfo.isIsVerify());
                     if(fileinfo.getCompress().equalsIgnoreCase("null") || fileinfo.getCompress().equalsIgnoreCase("") || fileinfo.getCompress().equals("off")){
                          file.setIsCompress(false);
                     }else{
                           file.setIsCompress(true);
                            
                     }
                     file.setCompress(this.compressLevel(fileinfo.getCompress())+"");
                    
                   
                     file.setIsMounted(fileinfo.isIsMounted());
                     file.setIsSetQuota(fileinfo.isIsSetQuota());
                     if (fileinfo.isIsSetQuota()) {
                         double size = Double.valueOf(fileinfo.getQuotaValue().substring(0, fileinfo.getQuotaValue().length() - 1));
                         if (fileinfo.getQuotaValue().endsWith("G")) {
                             file.setQuotaValue(String.valueOf(size));      
                         }
                         if (fileinfo.getQuotaValue().endsWith("M")) {
                             size = size / 1024;
                             file.setQuotaValue(String.valueOf(size));
                         }
                         if (fileinfo.getQuotaValue().endsWith("T")) {
                             size = size * 1024;
                             file.setQuotaValue(String.valueOf(size));
                         }
                         if (fileinfo.getQuotaValue().endsWith("P")) {
                             size = size * 1024 * 1024;
                             file.setQuotaValue(String.valueOf(size));
                         }
                     }
                     if ((fileinfo.getRecordsize() != null) && (!"".equalsIgnoreCase(fileinfo.getRecordsize()))) {
                         file.setRecordsize(fileinfo.getRecordsize());
                     }

                     detail = new ArrayList();
                     fileDetail = new VirtualDetail();
                     
                     fileDetail.setProperty(resources.get("disk.diskarea", "issetdedup"));
                     String isuse = resources.get("disk.diskarea", "false");
                     if(fileinfo.isIsSetDedup()){
                         isuse = resources.get("disk.diskarea", "true");
                     }else{
                         isuse = resources.get("disk.diskarea", "false");
                     }
                     fileDetail.setName(isuse);
                     detail.add(fileDetail);
                     fileDetail = new VirtualDetail();
                     fileDetail.setProperty(resources.get("disk.diskarea", "isverify"));
                     if(fileinfo.isIsVerify()){
                         isuse = resources.get("disk.diskarea", "true");
                     }else{
                         isuse = resources.get("disk.diskarea", "false");
                     }
                     fileDetail.setName(isuse);
                     detail.add(fileDetail);
                     fileDetail = new VirtualDetail();
                     fileDetail.setProperty(resources.get("disk.diskarea", "iscompress"));
                      if(fileinfo.getCompress().equalsIgnoreCase("null")||fileinfo.getCompress().equalsIgnoreCase("")||fileinfo.getCompress().equalsIgnoreCase("off")){
                         isuse = resources.get("disk.diskarea", "false");       
                     }else{
                          isuse = resources.get("disk.diskarea", "true");
                     }
                     fileDetail.setName(isuse);
                     detail.add(fileDetail);
                     if (isuse.equalsIgnoreCase(resources.get("disk.diskarea", "true"))) {
                         fileDetail = new VirtualDetail();
                         fileDetail.setProperty(resources.get("disk.diskarea", "compresslevel"));         
                         fileDetail.setName(this.compressLevel(fileinfo.getCompress())+"");
                         detail.add(fileDetail);
                     }
                     if(fileinfo.isSetQuota){
                         isuse = resources.get("disk.diskarea", "true");
                     }else{
                         isuse = resources.get("disk.diskarea", "false");
                     }
                     fileDetail = new VirtualDetail();
                     fileDetail.setProperty(resources.get("disk.diskarea", "isSetQuota"));
                     fileDetail.setName(isuse);
                     detail.add(fileDetail);
                     if (isuse.equalsIgnoreCase(resources.get("disk.diskarea", "true"))) {
                         fileDetail = new VirtualDetail();
                         fileDetail.setProperty(resources.get("disk.diskarea", "quotaValue"));
                         fileDetail.setName(file.getQuotaValue()+"GB");
                         detail.add(fileDetail);
                     }
                     
                     fileDetail = new VirtualDetail();
                     fileDetail.setProperty(resources.get("disk.diskarea", "recordsize"));
                     if(fileinfo.getRecordsize()!=null && !fileinfo.getRecordsize().equals("")){
                          fileDetail.setName(fileinfo.getRecordsize()+"B");
                     }
                     detail.add(fileDetail);
                     file.setDetail(detail);
                     fileList.add(file);
                 }
            }
        }
        
    }

    public FileSystemInfo getSelectFile() {
        return selectFile;
    }

    public void setSelectFile(FileSystemInfo selectFile) {
        this.selectFile = selectFile;
    }

    public List<FileSystemInfo> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileSystemInfo> fileList) {
        this.fileList = fileList;
    }
    
    public String fileSysSet(){
        System.out.println("selectFile.getUsed()="+selectFile.getUsed());
        String quotaValueStr = selectFile.quotaValue;
        if (quotaValueStr != null && !quotaValueStr.equals("") && !quotaValueStr.equalsIgnoreCase("null") && !quotaValueStr.equalsIgnoreCase("none")) {
            if (quotaValueStr.contains(".")) {
                quotaValueStr = quotaValueStr.split("\\.")[0];
            }
        }
        String title="vtl";
        String returnStr="diskarea";
        
        String param = "fileSystemName=" + selectFile.name + "&" + "isSetDedup=" + selectFile.isSetDedup + "&" + "isVerify=" + selectFile.isVerify + "&" + "isCompress=" + selectFile.isCompress+ "&" + "compress=" + selectFile.Compress+ "&" + "isSetQuota=" + selectFile.isSetQuota+ "&" + "quotaValue=" + quotaValueStr+ "&" + "recordsize=" + selectFile.recordsize+ "&" + "used=" + selectFile.used+ "&" + "title=" + title +"&" + "return=" + returnStr;
        return "filesystem_set?faces-redirect=true&amp;" + param;
    }
    
//    public String createDisk(){
//        String param = "poolZFSPath=" + selectFile.name;
//        return "createdisk?faces-redirect=true&amp;" + param;
//    }
    
     public String createDiskPool(){

        String param = "fileSystemName=" + selectFile.name;
        return "creatediskpool?faces-redirect=true&amp;" + param;
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
     
       public boolean isCompress(String Compress){
        if (null == Compress || "".equalsIgnoreCase(Compress)) {
            return false;
        }
        return (!"off".equalsIgnoreCase(Compress));
    }
       
   
       
   
    
}
