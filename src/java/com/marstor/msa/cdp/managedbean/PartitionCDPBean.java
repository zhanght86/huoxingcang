/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.model.ClientDiskDetail;
import com.marstor.msa.cdp.model.ClientDiskInfo;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.model.ClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.ClientMirroredPartitionInfo;
import com.marstor.msa.cdp.model.ClientPartitionDetail;
import com.marstor.msa.cdp.model.ClientPartitionInfo;
import com.marstor.msa.cdp.model.ExtendInfo;
import com.marstor.msa.cdp.socket.ClientAPI;
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
@ManagedBean(name = "partitionCDPBean")
@ViewScoped
public class PartitionCDPBean {

    private ClientInfo client = new ClientInfo();
    private List<ClientMirroredPartitionInfo> mirrorPartitions = new ArrayList();
    private ClientMirroredPartitionInfo selected;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.p_cdp";
    private String ip = "192.168.1.87";
    private int port = 1100;
    public long diskNum = -1;
    public long partitionNum = -1;
    public String partitionSize;
    public String cdpType;
    private final String strSize;
    public List<CdpDiskGroupInfo> cdpGroups;
    public ArrayList<CdpDiskInfo> cdpDisks;
    private Map<String, ExtendInfo> extendInfos = new HashMap();

    public PartitionCDPBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        port = Integer.parseInt(request.getParameter("port"));
        diskNum = Long.parseLong(request.getParameter("dNum"));
        partitionNum = Long.parseLong(request.getParameter("pNum"));
        client.setIp(ip);
        client.setPort(port);
        strSize = request.getParameter("pSize");
        partitionSize = Utility.getSizeString(Long.parseLong(request.getParameter("pSize"))) + " GB ";
        cdpType = Utility.getCDPTypeString(Integer.parseInt(request.getParameter("cdpType")));
        initMirrorDisks();
    }

    public final void initMirrorDisks() {
        ClientMirroredDiskInfo[] DiskInfos = ClientAPI.GetMirrorDisksInfo(this.client);
        if (DiskInfos == null) {
            ClientAPI.GetErrorCode();
            return;
        }
        for (int i = 0; i < DiskInfos.length; i++) {
            ClientDiskDetail DiskDetail = ClientAPI.GetSourceDiskDetail(DiskInfos[i].DiskNumber, this.client);
            if (DiskDetail == null) {
                ClientAPI.GetErrorCode();
                return;
            }
            if (!DiskDetail.ProductID.equalsIgnoreCase(MyConstants.Product_ID)) {
                DiskInfos[i] = null;
            }
        }

        getCDPData();
        for (int i = 0; i < DiskInfos.length; i++) {
            ClientMirroredDiskInfo DiskInfo = DiskInfos[i];
            if (DiskInfo == null || DiskInfo.Used
                    || DiskInfo.DiskNumber == this.diskNum) {
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
                        extendInfos.put(Integer.toString(DiskInfo.DiskNumber), e);
                        break;
                    }
                }
            }
            for (int j = 0; j < DiskInfo.PartitionCount; j++) {
                ClientMirroredPartitionInfo PartitionInfo = DiskInfo.PartitionInfos[j];
                ClientPartitionDetail PartitionDetail = ClientAPI.GetSourcePartitionDetail(DiskInfo.DiskNumber, PartitionInfo.PartitionNumber, this.client);
                if (PartitionDetail == null) {
                    ClientAPI.GetErrorCode();
                    return;
                }

                if (!PartitionInfo.Used && PartitionInfo.DiskNumber != this.diskNum
                        && PartitionDetail.CDPType == 0) {
                    this.mirrorPartitions.add(PartitionInfo);
                }
            }
        }

    }

    public String getHeader1(ClientMirroredPartitionInfo m) {
        if (extendInfos.get(Integer.toString(m.DiskNumber)) == null) {
            return "";
        }
        ExtendInfo e = extendInfos.get(Integer.toString(m.DiskNumber));
        return e.getDg();
    }

    public String getHeader2(ClientMirroredPartitionInfo m) {
        if (extendInfos.get(Integer.toString(m.DiskNumber)) == null) {
            return "";
        }
        ExtendInfo e = extendInfos.get(Integer.toString(m.DiskNumber));
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
    boolean bUseCache = true;

    public boolean getUseCache() {
        return bUseCache;
    }

    public void change() {
    }

    public void setUseCache(boolean bUseCache) {
        this.bUseCache = bUseCache;
    }

    public String setting() {

        if (this.selected.PartitionSize < Long.parseLong(this.strSize)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "error"), global.get("error_mark")));
            return null;
        }
        boolean bRet = ClientAPI.CreateCDP(CDPConstants.CDP_TYPE_PARTITION,
                (int) diskNum, (int) partitionNum, selected.DiskNumber, selected.PartitionNumber, bUseCache, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return null;
        }
        String param = "ip=" + ip + "&port=" + port;
        return "client?faces-redirect=true&amp;" + param;
    }

    public List<ClientMirroredPartitionInfo> getMirrorPartitions() {
        return mirrorPartitions;
    }

    public void setMirrorPartitions(List<ClientMirroredPartitionInfo> mirrorPartitions) {
        this.mirrorPartitions = mirrorPartitions;
    }

    public ClientMirroredPartitionInfo getSelected() {
        return selected;
    }

    public void setSelected(ClientMirroredPartitionInfo selected) {
        this.selected = selected;
    }

    public long getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(long diskNum) {
        this.diskNum = diskNum;
    }

    public long getPartitionNum() {
        return partitionNum;
    }

    public void setPartitionNum(long partitionNum) {
        this.partitionNum = partitionNum;
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
