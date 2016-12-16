/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.VmdkDisk;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.DGUtility;
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.bean.SharePath;
import com.marstor.msa.nas.web.NASInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.bean.VirtualMachine;
import com.marstor.msa.vbox.web.MsaVMInterface;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "addTaskBean")
@ViewScoped
public class AddTaskBean {

    private String srcPath;
    private List<String> desPaths = new ArrayList();
    private String dName;
    private String desPath;
    private String dFormat;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.add_task";
    private String guid;
    private String path;
    private boolean record = false;
    private long registerd;
    private long used;
    private long dSize;
    private String dLun;
    private String gName;
    private List<String> formats = new ArrayList();
    private List<VmdkDisk> tasks;
    private Integer progress = 0;

    public AddTaskBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        guid = request.getParameter("guid");
        path = request.getParameter("path");
        if (request.getParameter("record") != null) {
            record = true;
            registerd = Long.parseLong(request.getParameter("register"));
            used = Long.parseLong(request.getParameter("used"));
        }
        srcPath = request.getParameter("srcPath");
        String ds = request.getParameter("dSize");
        if (ds != null) {
            dSize = Long.parseLong(ds);
        }
        dLun = request.getParameter("dLun");
        gName = request.getParameter("gName");
        
        String[] paths = request.getParameterValues("desPath");
        if(paths != null){
           this.desPaths = Arrays.asList(paths);
        }
       
        formats.add("VDI");
        formats.add("VMDK");
        formats.add("VHD");

        //initTasks();
        this.dFormat = "VDI";
        //initPaths();
        
        
        if (this.desPaths.isEmpty()) {
            this.desPath = "";
        } else {
            this.desPath = desPaths.get(0);
        }
    }

    public Integer getProgress(VmdkDisk task) {

        progress = (int) ((double) task.getCurrentSize() * 100 / (double) task.getDiskSize());
        if (progress > 100 || task.getCurrentSize() == task.getDiskSize()) {
            progress = 100;
        }

        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public final void initTasks() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        tasks = cdp.getVmdkConvertDisks(srcPath);
        for (VmdkDisk t : tasks) {
            Debug.print("DestDisk:" + t.getDescDisk());
            Debug.print("getCurrentSize:" + t.getCurrentSize());
            Debug.print("getDiskSize:" + t.getDiskSize());
            Debug.print("D-Percent:" + t.getdPercent());
            Debug.print("Str-Percent:" + t.getPercent());
        }
    }

    public String save() {
        if (!checkNull() || !checkFormat() || isExist()) {
            return null;
        }

        if (this.dFormat.equalsIgnoreCase("vmdk") && dSize >= 2 * 1024L * 1024L * 1024L * 1024L) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "lessThan2T"), global.get("error_mark")));
            return null;
        }

        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        CdpDiskGroupInfo group = cdp.getDiskGroupInfo(guid);
        if (group.isCDPStarted() || group.getiAutoSnapshotIsOpen() == CDPConstants.TRUE) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "stopCDPplease"), global.get("error_mark")));
            return null;
        }

        String[] luns = new String[1];
        luns[0] = dLun;
        cdp.offlineLU(gName, luns);
        Debug.webPrint("MsaCDPInterface convertRawToVmdk(" + dName + ", " + dLun + ", " + srcPath + ", " + desPath + ", " + dSize + ", " + dFormat.toLowerCase() + ")");
        boolean bRet = cdp.convertRawToVmdk(dName, dLun, srcPath, desPath, dSize, dFormat.toLowerCase());
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return null;
        }
        String param = "guid=" + guid + "&path=" + path
                + "&srcPath=" + srcPath + "&dSize=" + dSize
                + "&dLun=" + dLun
                + "&gName=" + gName;
        if (record) {
            param += "&record=1" + "&register=" + registerd
                    + "&used=" + used;
        }
        return "to_vd?faces-redirect=true&amp;" + param;
    }

    public String cancel() {
        String param = "guid=" + guid + "&path=" + path
                + "&srcPath=" + srcPath + "&dSize=" + dSize
                + "&dLun=" + dLun
                + "&gName=" + gName;
        if (record) {
            param += "&record=1" + "&register=" + registerd
                    + "&used=" + used;
        }
        return "to_vd?faces-redirect=true&amp;" + param;
    }

    public void change() {
        Debug.webPrint(this.dFormat);
    }

    public boolean checkNull() {
        if ("".equals(dName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "nullError"), global.get("error_mark")));
            return false;
        }
        
        if (desPath == null || "".equals(desPath)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "nullPath"), global.get("error_mark")));
            return false;
        }
        return true;
    }

    public boolean checkFormat() {
        if (!DGUtility.checkShareName(dName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "formatError"), global.get("error_mark")));
            return false;
        }
        return true;
    }

    public boolean isExist() {
        File file = new File(this.desPath);
        File[] files = file.listFiles();

        if (files == null) {
            return false;
        }

        for (File dg : files) {
            if (dg.getName().equals(dName + "." + dFormat.toLowerCase())
                    || dg.getName().equals("." + dName + "." + dFormat.toLowerCase())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "existError"), global.get("error_mark")));
                return true;
            }
        }
        return false;
    }

    public final void initPaths() {
        MsaVMInterface vm = InterfaceFactory.getVMInterfaceInstance();
        List<VirtualMachine> vms = vm.getVMList();
        NASInterface nas = InterfaceFactory.getNASInterfaceInstance();
        List<SharePath> sps = nas.getAllSharePaths();
        desPaths = new ArrayList();
        if (vms != null) {
            for (VirtualMachine VM : vms) {
                String vp = VM.getVmBaseFolderPath();
                String[] vps = vp.split("/");
                desPaths.add("/" + vps[1] + "/" + vps[2] + "/" + vps[3]);
            }
        }
        if (sps != null) {
            for (SharePath sp : sps) {
                desPaths.add("/" + sp.getPath());
            }
        }
        if (this.desPaths.isEmpty()) {
            this.desPath = "";
        } else {
            this.desPath = desPaths.get(0);
        }

        formats.add("VDI");
        formats.add("VMDK");
        formats.add("VHD");
    }

    public String getSrcPath() {
        
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public List getDesPaths() {
        if (desPaths.isEmpty()) {
            desPaths.add("");
        }
        return desPaths;
    }

    public void setDesPaths(List desPaths) {
        this.desPaths = desPaths;
    }

    public String getDName() {
        return dName;
    }

    public void setDName(String dName) {
        this.dName = dName;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDesPath() {
        return desPath;
    }

    public void setDesPath(String desPath) {
        this.desPath = desPath;
    }

    public String getDFormat() {
        return dFormat;
    }

    public void setDFormat(String dFormat) {
        this.dFormat = dFormat;
    }

    public List<String> getFormats() {
        return formats;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }

    public List<VmdkDisk> getTasks() {
        if (tasks == null) {
            return new ArrayList();
        }
        return tasks;
    }

    public void setTasks(List<VmdkDisk> tasks) {
        this.tasks = tasks;
    }
}
