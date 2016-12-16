/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.disk.managedbean;

import com.marstor.msa.common.bean.FileSystemInformation;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.model.UserInfo;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.model.FileSystemInfor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@ManagedBean(name = "setFileSystemBean")
@ViewScoped
public class SetFileSystemBean  implements Serializable{
    public String fileSystemName;  //磁盘区名
    public String diskGroupName; 
    public String title;
    public String returnSTR;
    public boolean isSetQuota;  //启用配额管理
    public boolean isSetDedup;  //重复数据删除
//    public String compress = 6+"";  //压缩级别
    MSAResource resources = new MSAResource();
    public String compress = resources.get("disk.filesystem_set", "level10");  //压缩级别
    private ArrayList<String> compressLevel = new ArrayList<String>();
    public boolean isVerify = false;  //数据校验
    public String recordsize = "128KB";  //块大小
    public String quotaValue;  //最大分配空间
    public boolean isCompress;  //启用数据压缩
    public boolean notVerify;  //数据校验是否可用
    public boolean notCompress;  //压缩级别是否可用
    public boolean notQuota;  //配额管理是否可用
    private ArrayList<String> blockSize = new ArrayList<String>();
    public int minQuotaValue;  //修改的值最小不能小于多少
    public String name;
    public boolean isAuthorized = false;   //启用授权用户
    public boolean notAuthorized = true;  //授权用户是否可用
    public boolean isAuthorized_old = false;
    public String user_old = "";  //比较用
  
    public String user = "";  //授权用户
    private List<String> userInfoList;

    /**
     * Creates a new instance of SetFileSystemBean
     */
    public SetFileSystemBean() {
        initBlockSize();
        initCompressLevel();
        initSetFileSystem();
    }
    public void initSetFileSystem(){
        name = resources.get("disk.filesystem_set", "vtlname");
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        fileSystemName = request.getParameter("fileSystemName");
        title = request.getParameter("title");
        if(title.equalsIgnoreCase("vtl")){
            title= resources.get("disk.filesystem_set", "vtltitle");
            name = resources.get("disk.filesystem_set", "vtlname");
        }else if(title.equalsIgnoreCase("nas")){
            title= resources.get("disk.filesystem_set", "nastitle");
            name = resources.get("disk.filesystem_set", "nasname");
        }else if(title.equalsIgnoreCase("cdp")){
            title= resources.get("disk.filesystem_set", "cdptitle");
            name = resources.get("disk.filesystem_set", "cdpname");
        }else if(title.equalsIgnoreCase("vdl")) {
            title= resources.get("disk.filesystem_set", "vdltitle");
            name = resources.get("disk.filesystem_set", "vdlname");
        }else{
            title= resources.get("disk.filesystem_set", "title");
        }
        returnSTR = request.getParameter("return");
        initdata();
//        String boolSetDedup = request.getParameter("isSetDedup");
//        if (boolSetDedup.equalsIgnoreCase("true")) {
//            isSetDedup = true;
//        } else {
//            isSetDedup = false;
//        }
//        String boolVerify = request.getParameter("isVerify");
//        if (boolVerify.equalsIgnoreCase("true")) {
//            isVerify = true;
//        } else {
//            isVerify = false;
//        }
//        String boolCompress = request.getParameter("isCompress");
//        if (boolCompress.equalsIgnoreCase("true")) {
//            isCompress = true;
//        } else {
//            isCompress = false;
//        }
//        
//        if(request.getParameter("compress").equals("null")||request.getParameter("compress").equals("") || request.getParameter("compress").equals("0")){
//            compress = 6+"";
//            System.out.println("compress="+request.getParameter("compress"));
//        }else{
//            compress = request.getParameter("compress");
//            System.out.println("12compress="+request.getParameter("compress"));
//        }
////        if(request.getParameter("compress").equals("null")||request.getParameter("compress").equals("") ||request.getParameter("compress").equalsIgnoreCase("off")){
////            compress = 6+"";
////            System.out.println("compress="+request.getParameter("compress"));
////        }else{
////            compress = request.getParameter("compress");
////            System.out.println("12compress="+request.getParameter("compress"));
////        }
//        
//        String boolSetQuota = request.getParameter("isSetQuota");
//        if (boolSetQuota.equalsIgnoreCase("true")) {
//            isSetQuota = true;
//        } else {
//            isSetQuota = false;
//        }
//        if(request.getParameter("quotaValue").equals("null") ||request.getParameter("quotaValue").equals("") ||request.getParameter("quotaValue").equalsIgnoreCase("none")){
//             System.out.println("quotaValue="+request.getParameter("quotaValue"));
//            quotaValue = "";
//        } else {
//            quotaValue = request.getParameter("quotaValue");
//        }
//        if (request.getParameter("recordsize").equals("null")|| request.getParameter("recordsize").equals("")) {
//            System.out.println("recordsize="+request.getParameter("recordsize") );
//            recordsize = "128KB";  //块大小
//        } else {
//             System.out.println("12recordsize="+request.getParameter("recordsize") );
//            recordsize = request.getParameter("recordsize")+"B";
//        }
//        
//        String used = request.getParameter("used");
//        minQuotaValue = minSize(used);
//         
//        if (this.isSetDedup) {
//            this.notVerify = false;
//        } else {
//            this.notVerify = true;
//        }
////        changDuplicate();
//        changeCompress();
//        changeQuota();
             
    }
    
    private void initdata() {
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        FileSystemInformation selected = new FileSystemInformation();
        if(fileSystemName.startsWith("/")) {
            selected = zfs.getFileSystemInformation(fileSystemName.substring(fileSystemName.indexOf("/")+1));
        }else {
            selected = zfs.getFileSystemInformation(fileSystemName);
        }
        String quotaValueStr = selected.quotaValue;
        if (selected.isSetQuota) {
            if (quotaValueStr != null && !quotaValueStr.equals("") && !quotaValueStr.equalsIgnoreCase("null") && !quotaValueStr.equalsIgnoreCase("none")) {
                double size = Double.valueOf(selected.getQuotaValue().substring(0, selected.getQuotaValue().length() - 1));
                if (selected.getQuotaValue().endsWith("G")) {
                    quotaValueStr = String.valueOf(size);
                }
                if (selected.getQuotaValue().endsWith("M")) {
                    size = size / 1024;
                    quotaValueStr = String.valueOf(size);
                }
                if (selected.getQuotaValue().endsWith("T")) {
                    size = size * 1024;
                    quotaValueStr = String.valueOf(size);
                }
                if (selected.getQuotaValue().endsWith("P")) {
                    size = size * 1024 * 1024;
                    quotaValueStr = String.valueOf(size);
                }

                if (quotaValueStr.contains(".")) {
                    quotaValueStr = quotaValueStr.split("\\.")[0];
                }
            }
        }
        isSetDedup = selected.isSetDedup;
        isVerify = selected.isVerify;
        isCompress = this.isCompress(selected.Compress);
//        int intcompress = this.compressLevel(selected.Compress);
//        if (intcompress == 0) {
//            compress = 6 + "";
//        } else {
//            compress = intcompress + "";
//        }
        
        compress = this.toCompressLevel(selected.Compress);
        isSetQuota = selected.isSetQuota;
        if (quotaValueStr.equals("null") || quotaValueStr.equals("") || quotaValueStr.equalsIgnoreCase("none")) {
            System.out.println("quotaValue=" + quotaValueStr);
            quotaValue = "";
        } else {
            quotaValue = quotaValueStr;
        }
        
        String recordsizestr = selected.recordsize;
        if (recordsizestr.equals("null") || recordsizestr.equals("")) {
            System.out.println("recordsize=" + recordsizestr);
            recordsize = "128KB";  //块大小
        } else {
            System.out.println("12recordsize=" + recordsizestr);
            recordsize = recordsizestr + "B";
        }

        String used = selected.used;
        minQuotaValue = minSize(used);

        if (this.isSetDedup) {
            this.notVerify = false;
        } else {
            this.notVerify = true;
        }
//        changDuplicate();
        if (this.isCompress) {
            this.notCompress = false;

        } else {
            this.notCompress = true;
        }
//        changeCompress();
        changeQuota();
        MsaSYNCInterface syne = InterfaceFactory.getSYNCInterfaceInstance();

        if(fileSystemName.startsWith("/")) {
            user = syne.getFileSystemAuth(fileSystemName.substring(fileSystemName.indexOf("/")+1));
        }else {
            user = syne.getFileSystemAuth(fileSystemName);
        }
        System.out.println("user="+user);
   
        if(user==null||user.equals("null") || user.equals("") || user.equals("-")){
            isAuthorized = false;
        }else{
            isAuthorized = true;  
        }
        isAuthorized_old = isAuthorized;
        
        changeAuthorized();
       
        if (user==null || user.equals("null") || user.equals("")) {
            user = "";
        }
        user_old = user;
        userInfoList = new ArrayList<String>();
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        SystemUserInformation[] users = common.getUsers();
        boolean flage = false;
        if (users != null && users.length > 0) {
            for (int i = 0; i < users.length; i++) {
                if (users[i].getType()== 4) {  //授权用户
                    userInfoList.add(users[i].getName());
                    flage = true;
                }
            }
        }
        if( !flage){
            userInfoList.add("");
        }
    }
    
    public boolean isCompress(String Compress) {
        boolean returnStr = false;
        if (Compress.equalsIgnoreCase("null") || Compress.equalsIgnoreCase("") || Compress.equals("off")) {
            returnStr = false;
        } else {
            returnStr = true;

        }
        return returnStr;
        
        
        
//        if (null == Compress || "".equalsIgnoreCase(Compress)) {
//            return false;
//        }
//        return (!"off".equalsIgnoreCase(Compress));
    }
    
//    public int compressLevel(String Compress) {
//        if (null == Compress || "".equalsIgnoreCase(Compress)) {
//            return 0;
//        }
//        if ("off".equalsIgnoreCase(Compress)) {
//            return 0;
//        }
//        if ("gzip".equalsIgnoreCase(Compress)) {
//            return 6;
//        }
//        char num = Compress.charAt(Compress.length() - 1);
//        try {
//            return Integer.valueOf(num + "");
//        } catch (Exception e) {
//            return 0;
//        }
//    }
    
     public String toCompressLevel(String Compress) {
         
        if (null == Compress || "".equalsIgnoreCase(Compress)) {
//            return resources.get("disk.filesystem_set", "level0");
            return resources.get("disk.filesystem_set", "level10");
        }
        if ("off".equalsIgnoreCase(Compress)) {
            return resources.get("disk.filesystem_set", "level10");
//            return resources.get("disk.filesystem_set", "level0");
        }else if ("gzip".equalsIgnoreCase(Compress)) {
            return resources.get("disk.filesystem_set", "level6");
        }else if("gzip-9".equalsIgnoreCase(Compress)){
             return resources.get("disk.filesystem_set", "level9");
        }else if("lz4".equalsIgnoreCase(Compress)){
             return resources.get("disk.filesystem_set", "level10");
        }else{
             return resources.get("disk.filesystem_set", "level10");
        }
    }

    public ArrayList<String> getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(ArrayList<String> blockSize) {
        this.blockSize = blockSize;
    }

    public ArrayList<String> getCompressLevel() {
        return compressLevel;
    }

    public void setCompressLevel(ArrayList<String> compressLevel) {
        this.compressLevel = compressLevel;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void changDuplicate() {
//       boolean isV = this.isVerify;
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
            compress = resources.get("disk.filesystem_set", "level10");
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
    
    public void changeAuthorized() {
        if (this.isAuthorized) {
            this.notAuthorized = false;
        } else {
            this.notAuthorized = true;
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
    
     public void initCompressLevel() {
//        compressLevel.add(resources.get("disk.filesystem_set", "level0"));
        compressLevel.add(resources.get("disk.filesystem_set", "level6"));
        compressLevel.add(resources.get("disk.filesystem_set", "level9"));
        compressLevel.add(resources.get("disk.filesystem_set", "level10"));
    }
    
    public String saveFileSet() {
       
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
            System.out.println("fileSize="+fileSize);
            System.out.println("minQuotaValue="+minQuotaValue);
            if(!ValidateUtility.checkNOTLessSize(fileSize, false, minQuotaValue)){
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.filesystem_set", "value_limit")+minQuotaValue+resources.get("disk.filesystem_set", "gb"),"" ));
                return null;
            }
          
        }
        
        if (fileSystemName != null && fileSystemName.startsWith("/") && fileSystemName.length() > 1) {
            fileSystemName = fileSystemName.substring(1);
        }
        
        MsaSYNCInterface syne = InterfaceFactory.getSYNCInterfaceInstance();
       List<String>  descs = syne.getDescFileSystem();
        if (descs != null) {
            String file = "";
            if (fileSystemName.startsWith("/")) {
                file = fileSystemName.substring(fileSystemName.indexOf("/") + 1);
            } else {
                file = fileSystemName;
            }
            boolean isdesc = false; //是否为目标端
            for(String desc : descs){
                if(desc.equals(file)){
                    isdesc = true;
                    break;
                }
            }
            if (isdesc) {
                if (isAuthorized_old != isAuthorized || !user.equals(user_old)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.filesystem_set", "isdesc"), ""));
                    return null;
                }
            }
        }
       
         boolean authorized = false;
        if(isAuthorized){
            if(user==null ||user.equals("null") || user.trim().equals("")){
//                System.out.println("1111111111111111111");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.filesystem_set", "authorized_no"), ""));
                return null;
            }else{
//                  System.out.println("22222222222222221111111111111111111");
                authorized = syne.setFileSystemAuth(fileSystemName, user);
            }
        }else{
            
//              System.out.println("33333333333333333331111111111111111111");
          authorized = syne.setFileSystemAuth(fileSystemName, "-");    
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
//        if(fileSystemName!=null && fileSystemName.startsWith("/") && fileSystemName.length()>1) {
//            fileSystemName = fileSystemName.substring(1);
//        }
        boolean dehup = zfs.setDedup(fileSystemName, dedupStr);  //重复数据删除选项 on：开启 off：关闭 verify：数据校验 
       
//        int compressInt = 0;
//        if (isCompress) {
//            compressInt = Integer.valueOf(compress.trim());
//        } else {
//            compressInt = 0;
//        }
//        System.out.println("compressInt="+compressInt);
//        boolean compress = zfs.setCompress(fileSystemName, compressInt);  //压缩级别 0-9, off即0，0表示关闭 
        
        int compressInt = 0;
        if (isCompress) {
            compress = compress.trim();
//            if(compress.equals(resources.get("disk.filesystem_set", "level0"))){
//                compressInt = 0;
//            }else
            if (compress.equals(resources.get("disk.filesystem_set", "level6"))) {
                compressInt = 6;
            } else if (compress.equals(resources.get("disk.filesystem_set", "level9"))) {
                compressInt = 9;
            } else if (compress.equals(resources.get("disk.filesystem_set", "level10"))) {
                compressInt = 10;
            } else {
                 compressInt = 6;
//                compressInt = 0;
            }
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
        System.out.println("recordsize1="+recordsize);
           recordsize = recordsize.substring(0, recordsize.length() - 1);  //去掉B
        System.out.println("recordsize11="+recordsize);
        boolean recordSize = zfs.setRecordSize(fileSystemName,recordsize);  //设置块大小 
        String returnStr = null;
        String mess = "";
        if(dehup==true && compress==true && quota== true && recordSize==true && authorized==true){
//            returnStr = "diskarea?faces-redirect=true";
            System.out.println("returnStr="+returnSTR.trim());
            returnStr = returnSTR.trim()+"?faces-redirect=true";
        }else{
            if(dehup == false){
                mess =mess+ resources.get("disk.filesystem_set", "dedup_tip")+" ";
            }else if(compress == false){
                mess =mess+ resources.get("disk.filesystem_set", "compress_tip")+" ";
            }else if(quota == false){
                mess =mess+ resources.get("disk.filesystem_set", "quota_tip")+" ";
            }else if(recordSize == false){
                mess =mess+ resources.get("disk.filesystem_set", "recordsize_tip");
            }else if(authorized == false){
                mess =mess+ resources.get("disk.filesystem_set", "authorized_tip");
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.filesystem_set", "set")+mess+resources.get("disk.filesystem_set", "fail"), ""));

           returnStr = null;
        }
        return returnStr;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsAuthorized() {
        return isAuthorized;
    }

    public void setIsAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    public boolean isNotAuthorized() {
        return notAuthorized;
    }

    public void setNotAuthorized(boolean notAuthorized) {
        this.notAuthorized = notAuthorized;
    }

    public List<String> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<String> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
     
}
