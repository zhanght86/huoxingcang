/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.DiskPool;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.DiskInterface;
import com.marstor.msa.disk.model.DiskPoolInfo;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "storageCreateBean")
@ViewScoped
public class StorageCreateBean implements Serializable {

    public String volumename;  //卷组名
    public String poolname;
    public String fileSystemName;
    public String poolZFSPath;

    /**
     * Creates a new instance of CreateDiskPoolBean
     */
    public StorageCreateBean() {
        initCreateDiskPool();
    }

    public void initCreateDiskPool() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        fileSystemName = request.getParameter("name");
        volumename = fileSystemName.split("/")[0];
    }

    public String getVolumename() {
        return volumename;
    }

    public void setVolumename(String volumename) {
        this.volumename = volumename;
    }

    public String getPoolname() {
        return poolname;
    }

    public void setPoolname(String poolname) {
        this.poolname = poolname;
    }

    public String saveAddPool() {
        MSAResource resources = new MSAResource();
        if ("".equals(poolname)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.creatediskpool", "poolname_no"), ""));
            return null;
        }
        if (!poolname.matches("[a-zA-Z0-9]+") || poolname.length() > 16 || poolname.length() <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.creatediskpool", "poolname_limit"), ""));
            return null;
        }
        String zfsPathStr = fileSystemName + "/" + poolname;
        //获取所有磁盘池
        MsaCDPInterface diskIn = InterfaceFactory.getCDPInterfaceInstance();
        List<CdpDiskGroupInfo> diskpools = diskIn.getDiskGroupInfos();
        if (diskpools != null && diskpools.size() > 0) {
            for (CdpDiskGroupInfo diskpool : diskpools) {
                if (diskpool.getDiskGroupPath().equalsIgnoreCase(zfsPathStr)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.creatediskpool", "poolname_have"), ""));
                    return null;
                }
            }
        }

        String creatpool = diskIn.createDiskGroup(volumename, poolname);
        if (creatpool != null) {
//             RequestContext.getCurrentInstance().execute("addpool.show()");   
            todiskarea();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resources.get("disk.creatediskpool", "creatpoolfail"), ""));
            return null;
        }
        return null;

    }

    public String toCreatDisks() {
        poolZFSPath = fileSystemName + "/" + poolname;
        String param = "poolZFSPath=" + poolZFSPath + "&" + "poolName=" + poolname;
        return "createdisk?faces-redirect=true&amp;" + param;
    }

    public void todiskarea() {
        RequestContext.getCurrentInstance().execute("window.location.href='storage_area.xhtml'");
    }
}
