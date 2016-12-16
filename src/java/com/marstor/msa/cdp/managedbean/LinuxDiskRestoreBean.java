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
import com.marstor.msa.cdp.model.ExtendInfo;
import com.marstor.msa.cdp.model.LinuxClientDiskDetail;
import com.marstor.msa.cdp.model.LinuxClientMirroredDiskInfo;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "linuxDiskRestoreBean")
@ViewScoped
public class LinuxDiskRestoreBean {

    private ClientInfo client = new ClientInfo();
    private List<LinuxClientMirroredDiskInfo> mirrorDisks = new ArrayList();
    private LinuxClientMirroredDiskInfo selected;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.d_restore_linux";
    private String ip = "192.168.1.87";
    private int port = 1100;
    public String device;
    private String strSize;
    public int pcount;
    public String size;
    public String cdpType;
    public int iCDPType;
    private Map<String, ExtendInfo> extendInfos = new HashMap();
    public List<CdpDiskGroupInfo> cdpGroups;
    public ArrayList<CdpDiskInfo> cdpDisks;
    public String mirrorNum = ""; 
    private String strCDPType;

    public LinuxDiskRestoreBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        port = Integer.parseInt(request.getParameter("port"));
        pcount = Integer.parseInt(request.getParameter("pCount"));
        device = request.getParameter("device");
        strSize = request.getParameter("size");
        iCDPType = Integer.parseInt(request.getParameter("cdpType"));
        size = Utility.getSizeString(Long.parseLong(request.getParameter("size"))) + " GB ";
        cdpType = Utility.getCDPTypeString(Integer.parseInt(request.getParameter("cdpType")));        
        client.setIp(ip);
        client.setPort(port);
        strCDPType = request.getParameter("cdpType");
        if(request.getParameter("cdpType") != null && !strCDPType.equals("null")){
            cdpType = Utility.getCDPTypeString(Integer.parseInt(request.getParameter("cdpType")));
        }        
        if(request.getParameter("mirror") != null){
            mirrorNum = request.getParameter("mirror");
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
            if (!DiskDetail.ProductID.equalsIgnoreCase(MyConstants.Product_ID)
                    || DiskDetail.CDPType != 0) {
                DiskInfos[i] = null;
            }
        }

        getCDPData();

        for (int i = 0; i < DiskInfos.length; i++) {
            LinuxClientMirroredDiskInfo DiskInfo = DiskInfos[i];
            if (DiskInfos[i] != null && !DiskInfo.Used && !DiskInfo.device.equals(device)) {
                mirrorDisks.add(DiskInfo);
                //String serialNumber = doubleReverse(DiskInfo.serialNumber);
                String serialNumber = DiskInfo.serialNumber;
                for (CdpDiskInfo disk : this.cdpDisks) {
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

    public String getHeader3(LinuxClientMirroredDiskInfo m) {
        if (extendInfos.get(m.serialNumber) == null) {
            return "";
        }
        ExtendInfo e = extendInfos.get(m.serialNumber);
        return e.getDg();
    }

    public String getHeader4(LinuxClientMirroredDiskInfo m) {
        if (extendInfos.get(m.serialNumber) == null) {
            return "";
        }
        ExtendInfo e = extendInfos.get(m.serialNumber);
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

    public String setting() {
        int f = this.fast ? 1 : 0;
        if (selected.DiskSize > Long.parseLong(this.strSize)) {
            RequestContext.getCurrentInstance().execute("restore.show()");
            return null;
        }
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                this.device, "", selected.device, "", this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return null;
        }

        String param = "ip=" + ip + "&port=" + port + "&device=" + this.device;
        return "d_restore_pro_LinuxX?faces-redirect=true&amp;" + param;
    }
    
    public void setting0() {
        int f = 0;
        if (selected.DiskSize > Long.parseLong(this.strSize)) {
            RequestContext.getCurrentInstance().execute("restore0.show()");
            return;
        }
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                this.device, "", selected.device, "", this.client);
        
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.mirrorNum = selected.device;
    }
    
    public void setting1() {
        int f = 1;
        if (selected.DiskSize > Long.parseLong(this.strSize)) {
            RequestContext.getCurrentInstance().execute("restore1.show()");
            return;
        }
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                this.device, "", selected.device, "", this.client);
        
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.mirrorNum = selected.device;
    }
    
    public boolean noMirror(){
        if(this.mirrorNum.equals("")){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean getStopRendered(String num){
        if(num.equals(this.mirrorNum)){
            return true;
        }
        return false;
    }
    
    public String process(){
        String param = "ip=" + ip + "&port=" + port
                + "&device=" + this.device
                + "&pCount=" + this.pcount
                + "&size=" + this.strSize
                + "&cdpType=" + this.strCDPType
                + "&mirror=" + this.mirrorNum;
        Debug.print("LinuxDiskRestoreBean --> LinuxDiskRestoreProgressBean:" + param);
        return "d_restore_pro_LinuxX?faces-redirect=true&amp;" + param;
    }

    public void restore() {
        int f = this.fast ? 1 : 0;
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                this.device, "", selected.device, "", this.client);
        RequestContext.getCurrentInstance().execute("restore.hide()");
        if (!bRet) {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }

        this.mirrorNum = selected.device;
    }
    
    public void restore0() {
        int f = 0;
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                this.device, "", selected.device, "", this.client);
        RequestContext.getCurrentInstance().execute("restore0.hide()");
        if (!bRet) {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }

        this.mirrorNum = selected.device;
    }
    
    public void restore1() {
        int f = 1;
        boolean bRet = LinuxClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                this.device, "", selected.device, "", this.client);
        RequestContext.getCurrentInstance().execute("restore1.hide()");
        if (!bRet) {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }

        this.mirrorNum = selected.device;
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
    
    public void cancelRestore() {        
        boolean bRet = LinuxClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, this.device, "", this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            return;
        }
        this.mirrorNum = "";
    }

    public List<LinuxClientMirroredDiskInfo> getMirrorDisks() {
        return mirrorDisks;
    }

    public void setMirrorDisks(List<LinuxClientMirroredDiskInfo> mirrorDisks) {
        this.mirrorDisks = mirrorDisks;
    }

    public LinuxClientMirroredDiskInfo getSelected() {
        return selected;
    }

    public void setSelected(LinuxClientMirroredDiskInfo selected) {
        this.selected = selected;
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
    boolean fast = false;

    public boolean isFast() {
        return fast;
    }

    public void setFast(boolean fast) {
        this.fast = fast;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
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
        if(this.mirrorNum.equals("")){
            return "Œ¥…Ë÷√";
        }else {
            return "¥≈≈Ãª÷∏¥";
        }
    }

    public void setCdpType(String cdpType) {
        this.cdpType = cdpType;
    }
}
