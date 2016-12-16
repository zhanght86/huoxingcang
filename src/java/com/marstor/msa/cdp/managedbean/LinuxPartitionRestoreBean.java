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
import com.marstor.msa.cdp.model.ExtendInfo;
import com.marstor.msa.cdp.model.LinuxClientDiskDetail;
import com.marstor.msa.cdp.model.LinuxClientDiskInfo;
import com.marstor.msa.cdp.model.LinuxClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.LinuxClientMirroredPartitionInfo;
import com.marstor.msa.cdp.socket.ClientAPI;
import com.marstor.msa.cdp.socket.LinuxClientAPI;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Debug;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "linuxPRestoreBean")
@ViewScoped
public class LinuxPartitionRestoreBean {

    private ClientInfo client = new ClientInfo();
    private List<LinuxClientMirroredPartitionInfo> mirrorPartitions = new ArrayList();
    private LinuxClientMirroredPartitionInfo selected;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.p_restore_linux";
    private String ip = "192.168.1.87";
    private int port = 1100;
    public String device;
    public String pDevice;
    private String strSize;
    public String pSize;
    public String cdpType;
    private String strCDPType;
    public List<CdpDiskGroupInfo> cdpGroups;
    public ArrayList<CdpDiskInfo> cdpDisks;
    private Map<String, ExtendInfo> extendInfos = new HashMap();
    public String dMirror = ""; 
    public String pMirror = ""; 


    public LinuxPartitionRestoreBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        port = Integer.parseInt(request.getParameter("port"));
        device = request.getParameter("device");
        pDevice = request.getParameter("pDevice");
        strSize = request.getParameter("pSize");
        
        client.setIp(ip);
        client.setPort(port);
        if (strSize != null) {
            pSize = Utility.getSizeString(Long.parseLong(strSize)) + " GB ";
        }
        
        String type = request.getParameter("cdpType");
        strCDPType = request.getParameter("cdpType");
        if (strCDPType != null) {
            cdpType = Utility.getCDPTypeString(Integer.parseInt(strCDPType));
        }
        if(request.getParameter("dMirror") != null){
            dMirror = request.getParameter("dMirror");
        }
        if(request.getParameter("pMirror") != null){
            pMirror = request.getParameter("pMirror");
        }
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
                    if (!PartitionInfo.Used && !PartitionInfo.device.equals(device))  {
                    mirrorPartitions.add(PartitionInfo);
                }
            }
        }
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

    public String setting() {
        if(this.strSize != null && selected.PartitionSize > Long.parseLong(this.strSize)){
            RequestContext.getCurrentInstance().execute("restore.show()");
            return null;
        }
        int f = this.fast? 1 : 0;
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                device, pDevice, this.selected.device, this.selected.pDevice, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return null;
        }

        String param = "ip=" + ip + "&port=" + port + "&device=" + this.device + "&pDevice=" + this.pDevice;
        return "p_restore_pro_linux?faces-redirect=true&amp;" + param;
    }
    
    public void setting0() {
        if(this.strSize != null && selected.PartitionSize > Long.parseLong(this.strSize)){
            RequestContext.getCurrentInstance().execute("restore0.show()");
            return;
        }
        int f = 0;
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                device, pDevice, this.selected.device, this.selected.pDevice, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        
        this.dMirror = selected.device;
        this.pMirror = selected.pDevice;
    }
    
    public void setting1() {
        if(this.strSize != null && selected.PartitionSize > Long.parseLong(this.strSize)){
            RequestContext.getCurrentInstance().execute("restore1.show()");
            return;
        }
        int f = 1;
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                device, pDevice, this.selected.device, this.selected.pDevice, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        
        this.dMirror = selected.device;
        this.pMirror = selected.pDevice;
    }
    
    public boolean noMirror(){
        if(this.dMirror.equals("")){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean getStopRendered(String dMirror, String pMirror){
        if(dMirror.equals(this.dMirror) && pMirror.equals(this.pMirror)){
            return true;
        }
        return false;
    }
    
    public String process(){
        String param = "ip=" + ip + "&port=" + port
                + "&device=" + this.device
                + "&pDevice=" + this.pDevice
                + "&size=" + this.strSize
                + "&cdpType=" + this.strCDPType
                + "&dMirror=" + this.dMirror
                + "&pMirror=" + this.pMirror;
        Debug.print("linuxPartitionRestoreBean --> linuxPartitionRestoreProgressBean:" + param);
        return "p_restore_pro_linux?faces-redirect=true&amp;" + param;
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

    public String cancel() {
        RequestContext.getCurrentInstance().execute("restore.hide()");
        return null;
    }
    
    public String cancel0() {
        RequestContext.getCurrentInstance().execute("restore0.hide()");
        return null;
    }
    
    public String cancel1() {
        RequestContext.getCurrentInstance().execute("restore1.hide()");
        return null;
    }

    public void restore() {
        int f = this.fast ? 1 : 0;
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                this.device, this.pDevice, selected.device, selected.pDevice, this.client);
        RequestContext.getCurrentInstance().execute("restore.hide()");
        if (!bRet) {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.dMirror = selected.device;
        this.pMirror = selected.pDevice;
    }
    
    public void restore0() {
        int f = 0;
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                this.device, this.pDevice, selected.device, selected.pDevice, this.client);
        RequestContext.getCurrentInstance().execute("restore0.hide()");
        if (!bRet) {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.dMirror = selected.device;
        this.pMirror = selected.pDevice;
    }
    
    public void restore1() {
        int f = 1;
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                this.device, this.pDevice, selected.device, selected.pDevice, this.client);
        RequestContext.getCurrentInstance().execute("restore1.hide()");
        if (!bRet) {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.dMirror = selected.device;
        this.pMirror = selected.pDevice;
    }
    
    public void cancelRestore() {        
        boolean bRet = LinuxClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_PARTITION, this.device, this.pDevice, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            return;
        }
        this.dMirror = "";
        this.pMirror = "";
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

     boolean fast = false;

    public boolean isFast() {
        return fast;
    }

    public void setFast(boolean fast) {
        this.fast = fast;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getPSize() {
        return pSize;
    }

    public void setPSize(String pSize) {
        this.pSize = pSize;
    }

    public String getCdpType() {
        if(this.dMirror.equals("")){
            return "Œ¥…Ë÷√";
        }else{
            return "∑÷«¯ª÷∏¥";
        }
    }

    public void setCdpType(String cdpType) {
        this.cdpType = cdpType;
    }
    
    public void change(){
    
    }
}
