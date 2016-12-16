/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.model.ClientDiskDetail;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.model.ClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.ClientMirroredPartitionInfo;
import com.marstor.msa.cdp.model.ClientPartitionDetail;
import com.marstor.msa.cdp.model.ExtendInfo;
import com.marstor.msa.cdp.model.LinuxClientDiskDetail;
import com.marstor.msa.cdp.model.LinuxClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.LinuxClientMirroredPartitionInfo;
import com.marstor.msa.cdp.model.LinuxClientPartitionDetail;
import com.marstor.msa.cdp.socket.LinuxClientAPI;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyConstants;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@ManagedBean(name = "linuxPCDPBean")
@ViewScoped
public class LinuxPartitionCDPBean {

    private ClientInfo client = new ClientInfo();
    private List<LinuxClientMirroredPartitionInfo> mirrorPartitions = new ArrayList();
    private LinuxClientMirroredPartitionInfo selected;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.p_cdp_linux";
    private String ip = "192.168.1.87";
    private int port = 1100;
    public String device;
    public String pDevice;
    public String partitionSize;
    public String cdpType;
    private final String strSize;
    public List<CdpDiskGroupInfo> cdpGroups;
    public ArrayList<CdpDiskInfo> cdpDisks;
    private Map<String, ExtendInfo> extendInfos = new HashMap();

    public LinuxPartitionCDPBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        port = Integer.parseInt(request.getParameter("port"));
        device = request.getParameter("device");
        pDevice = request.getParameter("pDevice");
        strSize = request.getParameter("pSize");
        client.setIp(ip);
        client.setPort(port);
        partitionSize = Utility.getSizeString(Long.parseLong(request.getParameter("pSize"))) + " GB ";
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
        
        getCDPData();

        for (int i = 0; i < DiskInfos.length; i++) {
            LinuxClientMirroredDiskInfo DiskInfo = DiskInfos[i];
            
            if (DiskInfo == null || DiskInfo.Used
                    || DiskInfo.device.equals(this.device)) {
                continue;
            }
            String serialNumber = DiskInfo.serialNumber;
            for (CdpDiskInfo disk : this.cdpDisks) {
                if (disk.getLuInfoBean() == null) {
                    continue;
                }
                if (!serialNumber.equals(disk.getLuInfoBean().getSerialNum())) {
                    continue;
                }
                for (CdpDiskGroupInfo group : this.cdpGroups) {
                    if (!group.getDiskGroupGuid().equals(disk.getDiskGroupGuid())) {
                        continue;
                    } else {
                        ExtendInfo e = new ExtendInfo();
                        e.setDg(group.getDiskGroupName());
                        e.setDname(disk.getDiskName());
                        extendInfos.put(DiskInfo.device, e);
                        break;
                    }
                }
            }
            for (int j = 0; j < DiskInfo.PartitionCount; j++) {
                LinuxClientMirroredPartitionInfo PartitionInfo = DiskInfo.PartitionInfos[j];
                LinuxClientPartitionDetail PartitionDetail = LinuxClientAPI.GetSourcePartitionDetail(DiskInfo.device, PartitionInfo.pDevice, this.client);
                
                if (PartitionDetail == null) {
                    LinuxClientAPI.GetErrorCode();
                    return;
                }
                if (!PartitionInfo.Used && !PartitionInfo.device.equals(device)
                            && PartitionDetail.CDPType == 0) {
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
        boolean bRet = LinuxClientAPI.CreateCDP(CDPConstants.CDP_TYPE_PARTITION,
                this.device, this.pDevice, selected.device, selected.pDevice, bUseCache, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return null;
        }
        String param = "ip=" + ip + "&port=" + port;
        return "client_linux?faces-redirect=true&amp;" + param;
    }
    
    public String getHeader1(LinuxClientMirroredPartitionInfo m) {
        if (extendInfos.get(m.device) == null) {
            return "";
        }
        ExtendInfo e = extendInfos.get(m.device);
        return e.getDg();
    }

    public String getHeader2(LinuxClientMirroredPartitionInfo m) {
        if (extendInfos.get(m.device) == null) {
            return "";
        }
        ExtendInfo e = extendInfos.get(m.device);
        return e.getDname();
    }
    
    private void getCDPData() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        this.cdpGroups = cdp.getDiskGroupInfos();
        if (this.cdpGroups == null) {
            return;
        }
        this.cdpDisks = new ArrayList();
        for (CdpDiskGroupInfo group : this.cdpGroups) {
            if (group.getDiskGroupGuid().equals("")) {
                continue;
            }
            CdpDiskInfo[] infos = cdp.getDiskInfos(group.getDiskGroupGuid(), group.getDiskGroupPath());
            if (infos != null) {
                this.cdpDisks.addAll(Arrays.asList(infos));
            }
        }
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

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPDevice() {
        return pDevice;
    }

    public void setPDevice(String pDevice) {
        this.pDevice = pDevice;
    }

    public String getPartitionSize() {
        return partitionSize;
    }

    public void setPartitionSize(String partitionSize) {
        this.partitionSize = partitionSize;
    }

    public String getCdpType() {
        return cdpType;
    }

    public void setCdpType(String cdpType) {
        this.cdpType = cdpType;
    }

    
}
