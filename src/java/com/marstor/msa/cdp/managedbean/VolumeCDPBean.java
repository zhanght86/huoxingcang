/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.ClientDiskDetail;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.model.ClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.ClientMirroredPartitionInfo;
import com.marstor.msa.cdp.model.ClientPartitionDetail;
import com.marstor.msa.cdp.model.LinuxClientDiskDetail;
import com.marstor.msa.cdp.model.LinuxClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.LinuxClientMirroredPartitionInfo;
import com.marstor.msa.cdp.model.LinuxClientPartitionDetail;
import com.marstor.msa.cdp.socket.LinuxClientAPI;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "vCDPBean")
@ViewScoped
public class VolumeCDPBean {

    private ClientInfo client = new ClientInfo();
    private List<LinuxClientMirroredPartitionInfo> mirrorPartitions = new ArrayList();
    private LinuxClientMirroredPartitionInfo selected;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.v_cdp";
    private String ip = "192.168.1.87";
    private int port = 1100;
    public String vgName = "";
    public String vName = "";
    public String volumeSize;
    public String cdpType;
    private final String strSize;

    public VolumeCDPBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        port = Integer.parseInt(request.getParameter("port"));
        vgName = request.getParameter("vgName");
        vName = request.getParameter("vName");
        strSize = request.getParameter("vSize");
        client.setIp(ip);
        client.setPort(port);
        volumeSize = Utility.getSizeString(Long.parseLong(request.getParameter("vSize"))) + " GB ";
        cdpType = Utility.getCDPTypeString(Integer.parseInt(request.getParameter("cdpType")));
        initMirrorDisks();
    }

    public final void initMirrorDisks() {
        LinuxClientMirroredDiskInfo[] DiskInfos = LinuxClientAPI.GetMirrorDisksInfo(this.client);
        if (DiskInfos == null) {
            LinuxClientAPI.GetErrorCode();
            return;
        }
        for (int i = 0; i < DiskInfos.length; i++) {
            LinuxClientDiskDetail DiskDetail = LinuxClientAPI.GetSourceDiskDetail(DiskInfos[i].device, this.client);
            if (DiskDetail == null) {
                LinuxClientAPI.GetErrorCode();
                return;
            }
            if (!DiskDetail.ProductID.equalsIgnoreCase(MyConstants.Product_ID)) {
                DiskInfos[i] = null;
            }
        }
        for (int i = 0; i < DiskInfos.length; i++) {
            LinuxClientMirroredDiskInfo DiskInfo = DiskInfos[i];
            if (DiskInfo == null) {
                continue;
            }
            for (int j = 0; j < DiskInfo.PartitionCount; j++) {
                LinuxClientMirroredPartitionInfo PartitionInfo = DiskInfo.PartitionInfos[j];
                LinuxClientPartitionDetail PartitionDetail = LinuxClientAPI.GetSourcePartitionDetail(DiskInfo.device, PartitionInfo.pDevice, this.client);
                if (PartitionDetail == null) {
                    LinuxClientAPI.GetErrorCode();
                    return;
                }

                if (!PartitionInfo.Used && PartitionDetail.CDPType == 0) {
                    this.mirrorPartitions.add(PartitionInfo);
                }
            }
        }
    }

    public String setting() {
        if(this.selected.PartitionSize < Long.parseLong(this.strSize)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "error"), global.get("error_mark")));
            return null;
        }
        boolean bRet = LinuxClientAPI.CreateVCDP(CDPConstants.CDP_TYPE_VOLUME,
                this.vgName, this.vName, selected.device, selected.pDevice, bUseCache, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return null;
        }
        String param = "ip=" + ip + "&port=" + port;
        return "client_linux?faces-redirect=true&amp;" + param;
    }

    public List<LinuxClientMirroredPartitionInfo> getMirrorPartitions() {
        return mirrorPartitions;
    }

    public void setMirrorPartitions(List<LinuxClientMirroredPartitionInfo> mirrorPartitions) {
        this.mirrorPartitions = mirrorPartitions;
    }

    public LinuxClientMirroredPartitionInfo getSelected() {
        return selected;
    }

    public void setSelected(LinuxClientMirroredPartitionInfo selected) {
        this.selected = selected;
    }
    
    boolean bUseCache = true;

    public boolean getUseCache() {
        return bUseCache;
    }

    public void change(){
    
    }
    
    public void setUseCache(boolean bUseCache) {
        this.bUseCache = bUseCache;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getVgName() {
        return vgName;
    }

    public void setVgName(String vgName) {
        this.vgName = vgName;
    }

    public String getVName() {
        return vName;
    }

    public void setVName(String vName) {
        this.vName = vName;
    }

    public String getVolumeSize() {
        return volumeSize;
    }

    public void setVolumeSize(String volumeSize) {
        this.volumeSize = volumeSize;
    }

    public String getCdpType() {
        return cdpType;
    }

    public void setCdpType(String cdpType) {
        this.cdpType = cdpType;
    }
    
    

}
