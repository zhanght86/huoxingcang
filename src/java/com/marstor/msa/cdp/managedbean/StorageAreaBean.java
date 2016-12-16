/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.common.bean.FileSystemInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.disk.model.FileSystemInfo;
import com.marstor.msa.disk.model.VirtualDetail;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "saBean")
@ViewScoped
public class StorageAreaBean implements Serializable {

    private String[] volumeNames;
    private List<Details> expansionsView;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.storage_area";
    private Map<String, List<Details>> expansionsMap = new HashMap();
    public List<FileSystemInformation> views = new ArrayList();
    public FileSystemInformation selected;
    public String path;

    /**
     * Creates a new instance of FileSystemBean
     */
    public StorageAreaBean() {
        initVolumeNames();
        initNames();
        initStorageArea();
    }

    public final void initVolumeNames() {
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        volumeNames = zfs.getVolumeNames();
    }

    public final void initStorageArea() {
        if (volumeNames == null) {
            return;
        }
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        for (int i = 0; i < this.volumeNames.length; i++) {
            FileSystemInformation view = zfs.getFileSystemInformation(volumeNames[i] + "/DISK");
            this.views.add(view);
        }
    }

    public final void initNames() {
        if (volumeNames == null) {
            return;
        }
        for (int i = 0; i < this.volumeNames.length; i++) {
            expansionsView = new ArrayList();
            for (int j = 0; j < 5; j++) {
                Details g = new Details();
                g.setName(res.get(basename, "name" + j));
                expansionsView.add(g);
            }
            this.expansionsMap.put(volumeNames[i] + "/DISK", expansionsView);
        }

    }

    public void initExpansion(FileSystemInformation info) {
        //扩展 存储空间信息
        expansionsView = new ArrayList();
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        FileSystemInformation expansion = zfs.getFileSystemInformation(info.name);
        Details g = new Details();
        g.setName(res.get(basename, "name0"));
        g.setValue(expansion.isSetDedup ? res.get(basename, "true") : res.get(basename, "false"));
        expansionsView.add(g);
        g = new Details();
        g.setName(res.get(basename, "name1"));
        g.setValue(expansion.isVerify ? res.get(basename, "true") : res.get(basename, "false"));
        expansionsView.add(g);
        g = new Details();
        g.setName(res.get(basename, "name2"));
        boolean c = expansion.getCompress().equalsIgnoreCase("null") || expansion.getCompress().equalsIgnoreCase("") || expansion.getCompress().equalsIgnoreCase("off");
        g.setValue(c ? res.get(basename, "false") : res.get(basename, "true"));
        expansionsView.add(g);
        if (!c) {
            //启用数据压缩时
            g = new Details();
            g.setName(res.get(basename, "compresslevel"));
            g.setValue(this.compressLevel(expansion.Compress) + "");
            expansionsView.add(g);
        }
        g = new Details();
        g.setName(res.get(basename, "name4"));
        g.setValue(expansion.isSetQuota ? res.get(basename, "true") : res.get(basename, "false"));
        expansionsView.add(g);
        String quotaValueStr = "";
        if (expansion.isSetQuota) {
            double size = Double.valueOf(expansion.quotaValue.substring(0, expansion.quotaValue.length() - 1));
            if (expansion.quotaValue.endsWith("G")) {
                quotaValueStr = String.valueOf(size);
            }
            if (expansion.quotaValue.endsWith("M")) {
                size = size / 1024;
                quotaValueStr = String.valueOf(size);
            }
            if (expansion.quotaValue.endsWith("T")) {
                size = size * 1024;
                quotaValueStr = String.valueOf(size);
            }
            if (expansion.quotaValue.endsWith("P")) {
                size = size * 1024 * 1024;
                quotaValueStr = String.valueOf(size);
            }
            g = new Details();
            g.setName(res.get(basename, "name5"));
            g.setValue(quotaValueStr + "GB");
            expansionsView.add(g);
        }
        g = new Details();
        g.setName(res.get(basename, "name6"));
        g.setValue(expansion.getRecordsize() != null && !expansion.getRecordsize().equals("")
                ? expansion.recordsize + "B" : "");
        expansionsView.add(g);
//        expansionsView = this.expansionsMap.get(info.name);
//        if (expansionsView.get(0).getValue() == null) {
//            ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
//            FileSystemInformation expansion = zfs.getFileSystemInformation(info.name);
//            expansionsView.get(0).setValue(expansion.isSetDedup
//                    ? res.get(basename, "true") : res.get(basename, "false"));
//            expansionsView.get(1).setValue(expansion.isVerify
//                    ? res.get(basename, "true") : res.get(basename, "false"));
//            expansionsView.get(2).setValue(expansion.getCompress().equalsIgnoreCase("null") || expansion.getCompress().equalsIgnoreCase("") || expansion.getCompress().equalsIgnoreCase("off")
//                    ? res.get(basename, "false") : res.get(basename, "true"));
//            expansionsView.get(3).setValue(expansion.isSetQuota
//                    ? res.get(basename, "true") : res.get(basename, "false"));
//            expansionsView.get(4).setValue(expansion.getRecordsize() != null && !expansion.getRecordsize().equals("")
//                    ? expansion.recordsize + "B" : "");
//        }
    }

    public String settings() {
        String volume = path.split("/")[1];
        ZFSInterface zfs = InterfaceFactory.getZFSInterfaceInstance();
        selected = zfs.getFileSystemInformation(volume + "/DISK");
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

        String title = "cdp";
        String returnStr = "/cdp/dgs";
        String param = "fileSystemName=" + selected.name + "&" + "isSetDedup=" + selected.isSetDedup + "&" + "isVerify=" + selected.isVerify + "&" + "isCompress=" + this.isCompress(selected.Compress) + "&" + "compress=" + this.compressLevel(selected.Compress) + "&" + "isSetQuota=" + selected.isSetQuota + "&" + "quotaValue=" + quotaValueStr + "&" + "recordsize=" + selected.recordsize + "&" + "used=" + selected.used + "&" + "title=" + title + "&" + "return=" + returnStr;
        return "/disk/filesystem_set?faces-redirect=true&amp;" + param;
    }

    public String storageCreate() {
        String param = "name=" + selected.name;
        return "storage_create?faces-redirect=true&amp;" + param;
    }

    public int compressLevel(String Compress) {
        if (null == Compress || "".equalsIgnoreCase(Compress)) {
            return 0;
        }
        if ("off".equalsIgnoreCase(Compress)) {
            return 0;
        }
        if ("gzip".equalsIgnoreCase(Compress)) {
            return 6;
        }
        char num = Compress.charAt(Compress.length() - 1);
        try {
            return Integer.valueOf(num + "");
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isCompress(String Compress) {
        if (null == Compress || "".equalsIgnoreCase(Compress)) {
            return false;
        }
        return (!"off".equalsIgnoreCase(Compress));
    }

    public List<FileSystemInformation> getViews() {
        return views;
    }

    public void setViews(List<FileSystemInformation> views) {
        this.views = views;
    }

    public List<Details> getExpansionsView() {
        return expansionsView;
    }

    public void setExpansionsView(List<Details> expansionsView) {
        this.expansionsView = expansionsView;
    }

    public FileSystemInformation getSelected() {
        return selected;
    }

    public void setSelected(FileSystemInformation selected) {
        this.selected = selected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
