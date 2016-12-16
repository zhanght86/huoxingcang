/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.bean.FileSystemInformation;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.nas.model.FileSystem;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class StorageSpaceBean implements Serializable{

    private ArrayList<FileSystem> spaces = new ArrayList<FileSystem>();

    public StorageSpaceBean() {
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        String[] volumes = zfs.getVolumeNames();
        for (int i = 0; i < volumes.length; i++) {
            FileSystemInformation info = zfs.getFileSystemInformation(volumes[i] + "/NAS");
            FileSystem sys = new FileSystem(volumes[i] + "/NAS", info.used, info.available, info.mountpoint, info.quotaValue, info.isSetQuota, info.isSetDedup, info.isMounted, info.Compress, info.isVerify, info.recordsize);
            spaces.add(sys);
        }
    }

    public ArrayList<FileSystem> getSpaces() {
        return spaces;
    }

    public void setSpaces(ArrayList<FileSystem> spaces) {
        this.spaces = spaces;
    }

    public String doBeforeConfigFileSys(FileSystem sys) {
        
        String quotaValueStr = sys.getQuotaValue();
        if (quotaValueStr != null && !quotaValueStr.equals("") && !quotaValueStr.equalsIgnoreCase("null") && !quotaValueStr.equalsIgnoreCase("none")) {
            if (quotaValueStr.contains(".")) {
                quotaValueStr = quotaValueStr.split("\\.")[0];
            }
        }
        String title="nas";
        String returnStr="/nas/nas_storage_space";
        String param = "fileSystemName=" + sys.getName() + "&" + "isSetDedup=" + sys.isIsSetDedup()  + "&" + "isVerify=" + sys.isIsVerify()  + "&" + "isCompress=" + sys.isIsCompress()+ "&" + "compress=" + sys.compressLevel(sys.getCompress())   + "&" + "isSetQuota=" + sys.isIsSetQuota() + "&" + "quotaValue=" + quotaValueStr + "&" + "recordsize=" + sys.getRecordsize()  + "&" + "used=" + sys.getUsed() + "&" + "title=" + title +"&" + "return=" + returnStr;
        Debug.print("param: " + param);
        //Debug.print("≤‚ ‘");
        return "/disk/filesystem_set?faces-redirect=true&amp;" + param;
        
//        String quotaValueStr = sys.getQuotaValue();
//        if (quotaValueStr != null && !quotaValueStr.equals("") && !quotaValueStr.equalsIgnoreCase("null") && !quotaValueStr.equalsIgnoreCase("none")) {
//            if (quotaValueStr.contains(".")) {
//                quotaValueStr = quotaValueStr.split("\\.")[0];
//            }
//        }
//        String param = "fileSystemName=" + sys.getName() + "&" + "isSetDedup=" + sys.isIsSetDedup() + "&" + "isVerify=" + sys.isIsVerify() + "&" + "isCompress=" + sys.isIsCompress() + "&" + "compress=" + sys.getCompress() + "&" + "isSetQuota=" + sys.isIsSetQuota() + "&" + "quotaValue=" + quotaValueStr + "&" + "recordsize=" + sys.getRecordsize() + "&" + "used=" + sys.getUsed();
//        return "nas_config_param?faces-redirect=true&amp;" + param;
    }
}
