/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.backup.managedbean.AgentManager;
import com.marstor.msa.backup.util.Util;
import com.marstor.msa.common.component.Package;
import com.marstor.msa.common.component.UpgradePackage;
import com.marstor.msa.util.InterfaceFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "sysUpdate")
@ViewScoped
public class SystemUpdate implements Serializable {

//    private String path = "P:/tmp/";
    private String path = "/tmp/";
    private final String CHANGELOG_FILE = "changelog.txt";
    private final String INSTALL_FILE = "Install.sh";
    private UploadedFile file;
    private List<com.marstor.msa.common.component.Package> pkgs = new ArrayList();
    private String changelog = null;
    private static boolean uploaded = false;
    private boolean flag;

    public SystemUpdate() {
        initPackages();
        System.out.println(changelog);
    }

    public boolean isUploaded() {
        flag = uploaded;
        uploaded = false;
        return flag;
    }

    public void setUploaded(boolean uploaded) {
        SystemUpdate.uploaded = uploaded;
    }

    public final void initPackages() {
        pkgs = UpgradePackage.getUpgradePackages();
    }

    public List<Package> getPkgs() {
        return pkgs;
    }

    public String getChangelog() {
        System.out.println("getChangelog:" + uploaded);
        if(!flag){
            return "";
        }
        //获取changelog
        List<String> origin = readFile(path + this.CHANGELOG_FILE);
        StringBuilder sb = new StringBuilder();
        for (String s : origin) {
            sb.append(s);
            sb.append("\n");
        }
        this.changelog = sb.toString();
        
        System.out.println(changelog);
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public void setPkgs(List<Package> pkgs) {
        this.pkgs = pkgs;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    private boolean bInput;

    public boolean isBInput() {
        return bInput;
    }

    public void setBInput(boolean bInput) {
        this.bInput = bInput;
    }

    public void upload() {
        System.out.println(file);
        System.out.println("getContentType:" + file.getContentType());
        System.out.println("getFileName:" + file.getFileName());
        System.out.println("getContents:" + file.getContents());
        System.out.println("getContents:" + file.getSize());

        if (null == file || 0 == file.getSize()) {
            System.out.println("if " + file.getFileName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "不能上传空文件。", ""));
            return;
        }
        InputStream inputstream;
        try {
            inputstream = file.getInputstream();
            System.out.println("getInputstream:" + file.getInputstream());
        } catch (IOException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "生成文件流失败。", ""));
            return;
        }

        bInput = Util.upLoadFile(inputstream, path + file.getFileName());
        if (!bInput) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "升级文件写入失败。", ""));
            return;
        }

        //解压缩升级包，判断相关文件是否存在
        boolean success = InterfaceFactory.getCommonInterfaceInstance().decompressTar(file.getFileName());
        if(!success){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "升级包解压失败。", ""));
            return;
        }
        File tmp = new File(path);
        String[] tmps = tmp.list();

        if (tmps == null || tmps.length < 2) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "升级包已损坏。", ""));
            return;
        }
        System.out.println(tmps.length);
        List<String> fs = Arrays.asList(tmps);
        if (!fs.contains(this.CHANGELOG_FILE) || !fs.contains(this.INSTALL_FILE)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "升级包已损坏。", ""));
            return;
        }

        //自动弹出确认窗口               
        uploaded = true;
        System.out.println("uploaded******************" + uploaded);
        System.out.println("after show dialog******************");
    }

    public String update() {
        System.out.println(file);
        if (null == file || 0 == file.getSize()) {
            System.out.println("if " + file);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "不能上传空文件。", ""));
            return null;
        }
        InputStream inputstream;
        try {
            inputstream = file.getInputstream();
        } catch (IOException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "生成文件流失败。", ""));
            return null;
        }

        bInput = Util.upLoadFile(inputstream, path + file.getFileName());
        if (bInput) {
            if (!InterfaceFactory.getCommonInterfaceInstance().UpdateMSA()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "升级失败。", ""));
                return null;
            }
            return "/volume/reboot?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "升级文件写入失败。", ""));
            return null;
        }
    }

    private List<String> readFile(String file) {
        List<String> fileContext = new ArrayList();
        File f = new File(file);
        BufferedReader reader = null;
        try {
            InputStream in = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            reader = new BufferedReader(isr);
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                fileContext.add(tempString);
            }
            isr.close(); 
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return fileContext;
    }

    public String updateC() {
        if (bInput) {
            if (!InterfaceFactory.getCommonInterfaceInstance().UpdateMSA()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "升级失败。", ""));
                return null;
            }
            return "/volume/reboot?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "升级文件写入失败。", ""));
            return null;
        }
    }
}
