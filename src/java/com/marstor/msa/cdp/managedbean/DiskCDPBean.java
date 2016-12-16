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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "diskCDPBean")
@ViewScoped
public class DiskCDPBean {

    private ClientInfo client = new ClientInfo();
    private List<ClientMirroredDiskInfo> mirrorDisks = new ArrayList();
    private ClientMirroredDiskInfo selected;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.d_cdp";
    private String ip = "192.168.1.87";
    private int port = 1100;
    public List<CdpDiskGroupInfo> cdpGroups;
    public ArrayList<CdpDiskInfo> cdpDisks;
    public long diskNum;
    public int pcount;
    public String size;
    public String cdpType;
    private Map<String, ExtendInfo> extendInfos = new HashMap();
    private final String strSize;

    public DiskCDPBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        if (request.getParameter("port") != null) {
            port = Integer.parseInt(request.getParameter("port"));
        }
        if (request.getParameter("dNum") != null) {
            diskNum = Long.parseLong(request.getParameter("dNum"));
        }

        client.setIp(ip);
        client.setPort(port);
        if (request.getParameter("pCount") != null) {
            pcount = Integer.parseInt(request.getParameter("pCount"));
        }

        strSize = request.getParameter("size");
        if (request.getParameter("size") != null) {
            size = Utility.getSizeString(Long.parseLong(request.getParameter("size"))) + " GB ";
        }

        if (request.getParameter("cdpType") != null) {
            cdpType = Utility.getCDPTypeString(Integer.parseInt(request.getParameter("cdpType")));
        }

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
            if (!DiskDetail.ProductID.equalsIgnoreCase(MyConstants.Product_ID)
                    || DiskDetail.CDPType != 0) {
                DiskInfos[i] = null;
            }
        }

        getCDPData();

        for (int i = 0; i < DiskInfos.length; i++) {
            ClientMirroredDiskInfo DiskInfo = DiskInfos[i];

            if (DiskInfos[i] != null && !DiskInfo.Used
                    && DiskInfo.DiskNumber != this.diskNum) {

                mirrorDisks.add(DiskInfo);

                //  String serialNumber = doubleReverse(DiskInfo.serialNumber);
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
                            extendInfos.put(DiskInfo.serialNumber, e);
                            break;
                        }
                    }
                }
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

    public String getHeader3(ClientMirroredDiskInfo m) {
        if (extendInfos.get(m.serialNumber) == null) {
            return "";
        }
        ExtendInfo e = extendInfos.get(m.serialNumber);
        return e.getDg();
    }

    public String getHeader4(ClientMirroredDiskInfo m) {
        if (extendInfos.get(m.serialNumber) == null) {
            return "";
        }
        ExtendInfo e = extendInfos.get(m.serialNumber);
        return e.getDname();
    }
    boolean bUseCache = true;

    public boolean isBUseCache() {
        return bUseCache;
    }

    public void setBUseCache(boolean bUseCache) {
        this.bUseCache = bUseCache;
    }

    public void change() {
    }

    public String setting() {

        if (this.selected.DiskSize < Long.parseLong(this.strSize)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "error"), global.get("error_mark")));
            return null;
        }

        boolean bRet = ClientAPI.CreateCDP(CDPConstants.CDP_TYPE_DISK,
                (int) diskNum, 0, selected.DiskNumber, 0, bUseCache, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return null;
        }

        String param = "ip=" + ip + "&port=" + port;
        return "client?faces-redirect=true&amp;" + param;
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

    private String doubleReverse(String serial) {
        char[] cs = serial.toCharArray();
        char[] ret = new char[cs.length];
        for (int i = 0; i < cs.length; i++) {
            if (i % 2 == 0) {
                ret[i] = cs[i + 1];
            } else {
                ret[i] = cs[i - 1];
            }
        }
        return new String(ret);
    }

    public List<ClientMirroredDiskInfo> getMirrorDisks() {
        return mirrorDisks;
    }

    public void setMirrorDisks(List<ClientMirroredDiskInfo> mirrorDisks) {
        this.mirrorDisks = mirrorDisks;
    }

    public ClientMirroredDiskInfo getSelected() {
        return selected;
    }

    public void setSelected(ClientMirroredDiskInfo selected) {
        this.selected = selected;
    }

    public long getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(long diskNum) {
        this.diskNum = diskNum;
    }

    public int getPcount() {
        return pcount;
    }

    public void setPcount(int pcount) {
        this.pcount = pcount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCdpType() {
        return cdpType;
    }

    public void setCdpType(String cdpType) {
        this.cdpType = cdpType;
    }
}
