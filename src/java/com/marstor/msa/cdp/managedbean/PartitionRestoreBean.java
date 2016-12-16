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
import com.marstor.msa.cdp.model.ClientPartitionInfo;
import com.marstor.msa.cdp.model.ExtendInfo;
import com.marstor.msa.cdp.socket.ClientAPI;
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
@ManagedBean(name = "partitionRestoreBean")
@ViewScoped
public class PartitionRestoreBean {

    private ClientInfo client = new ClientInfo();
    private List<ClientMirroredPartitionInfo> mirrorPartitions = new ArrayList();
    private ClientMirroredPartitionInfo selected;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.p_restore";
    private String ip = "192.168.1.87";
    private int port = 1100;
    public long diskNum = -1;
    public long partitionNum = -1;
    public String partitionSize;
    public String cdpType;
    private String strCDPType;
    private ClientPartitionInfo PartitionInfo;
    private String strSize;
    public List<CdpDiskGroupInfo> cdpGroups;
    public ArrayList<CdpDiskInfo> cdpDisks;
    private Map<String, ExtendInfo> extendInfos = new HashMap();
    public int dMirror = 10000; 
    public int pMirror = 10000; 

    public PartitionRestoreBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        if (request.getParameter("ip") != null) {
            ip = request.getParameter("ip");
        }
        if (request.getParameter("port") != null) {
            port = Integer.parseInt(request.getParameter("port"));
        }
        if (request.getParameter("dNum") != null) {
            diskNum = Long.parseLong(request.getParameter("dNum"));
        }
        if (request.getParameter("pNum") != null) {
            partitionNum = Long.parseLong(request.getParameter("pNum"));
        }

        client.setIp(ip);
        client.setPort(port);
        strSize = request.getParameter("pSize");
        if (strSize != null) {
            partitionSize = Utility.getSizeString(Long.parseLong(strSize)) + " GB ";
        }
        strCDPType = request.getParameter("cdpType");
        if (strCDPType != null) {
            cdpType = Utility.getCDPTypeString(Integer.parseInt(strCDPType));
        }
        if(request.getParameter("dMirror") != null){
            dMirror = Integer.parseInt(request.getParameter("dMirror"));
        }
        if(request.getParameter("pMirror") != null){
            pMirror = Integer.parseInt(request.getParameter("pMirror"));
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
            if (!DiskDetail.ProductID.equalsIgnoreCase(MyConstants.Product_ID)) {
                DiskInfos[i] = null;
            }
        }

        getCDPData();


        for (int i = 0; i < DiskInfos.length; i++) {
            ClientMirroredDiskInfo DiskInfo = DiskInfos[i];
            if (DiskInfo == null) {
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
                ClientMirroredPartitionInfo MirrorPartition = DiskInfo.PartitionInfos[j];
                if (MirrorPartition != null && !MirrorPartition.Used && MirrorPartition.DiskNumber != this.diskNum) {
                    mirrorPartitions.add(MirrorPartition);
                }
            }
        }
    }
    
    public boolean getStopRendered(int dMirror, int pMirror){
        if(dMirror == this.dMirror && pMirror == this.pMirror){
            return true;
        }
        return false;
    }
    
    public String process(){
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + this.diskNum
                + "&pNum=" + this.partitionNum
                + "&size=" + this.strSize
                + "&cdpType=" + this.strCDPType
                + "&dMirror=" + this.dMirror
                + "&pMirror=" + this.pMirror;
        Debug.print("PartitionRestoreBean --> PartitionRestoreProgressBean:" + param);
        return "p_restore_pro?faces-redirect=true&amp;" + param;
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

    public void setting() {
        if (this.strSize != null && selected.PartitionSize > Long.parseLong(this.strSize)) {
            RequestContext.getCurrentInstance().execute("restore.show()");
            return;
        }

        int f = this.fast ? 1 : 0;
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                (int) diskNum, (int) partitionNum, selected.DiskNumber, selected.PartitionNumber, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.dMirror = selected.DiskNumber;
        this.pMirror = selected.PartitionNumber;
    }
    
    public void setting0() {
        if (this.strSize != null && selected.PartitionSize > Long.parseLong(this.strSize)) {
            RequestContext.getCurrentInstance().execute("restore0.show()");
            return;
        }

        int f = 0;
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                (int) diskNum, (int) partitionNum, selected.DiskNumber, selected.PartitionNumber, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.dMirror = selected.DiskNumber;
        this.pMirror = selected.PartitionNumber;
    }
    
    public void setting1() {
        if (this.strSize != null && selected.PartitionSize > Long.parseLong(this.strSize)) {
            RequestContext.getCurrentInstance().execute("restore1.show()");
            return;
        }

        int f = 1;
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                (int) diskNum, (int) partitionNum, selected.DiskNumber, selected.PartitionNumber, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.dMirror = selected.DiskNumber;
        this.pMirror = selected.PartitionNumber;
    }
    
    public void cancelRestore() {        
        boolean bRet = ClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_PARTITION, (int)this.diskNum, (int)this.partitionNum, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            return;
        }
        this.dMirror = 10000;
        this.pMirror = 10000;
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
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                (int) diskNum, (int) partitionNum, selected.DiskNumber, selected.PartitionNumber, this.client);
        RequestContext.getCurrentInstance().execute("restore.hide()");
        if (!bRet) {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.dMirror = selected.DiskNumber;
        this.pMirror = selected.PartitionNumber;        
    }
    
    public void restore0() {
        int f = 0;
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                (int) diskNum, (int) partitionNum, selected.DiskNumber, selected.PartitionNumber, this.client);
        RequestContext.getCurrentInstance().execute("restore0.hide()");
        if (!bRet) {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.dMirror = selected.DiskNumber;
        this.pMirror = selected.PartitionNumber;        
    }
    
    public void restore1() {
        int f = 1;
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_PARTITION, f,
                (int) diskNum, (int) partitionNum, selected.DiskNumber, selected.PartitionNumber, this.client);
        RequestContext.getCurrentInstance().execute("restore1.hide()");
        if (!bRet) {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.dMirror = selected.DiskNumber;
        this.pMirror = selected.PartitionNumber;        
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
        if(this.dMirror == 10000){
            return "Œ¥…Ë÷√";
        }else{
            return "∑÷«¯ª÷∏¥";
        }
    }

    public void setCdpType(String cdpType) {
        this.cdpType = cdpType;
    }
    boolean fast = false;

    public boolean isFast() {
        return fast;
    }

    public void setFast(boolean fast) {
        this.fast = fast;
    }

    public void change() {
    }

    public int getDMirror() {
        return dMirror;
    }

    public void setDMirror(int dMirror) {
        this.dMirror = dMirror;
    }
    
}
