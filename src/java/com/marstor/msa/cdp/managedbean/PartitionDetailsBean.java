/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.ClientDiskDetail;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.model.ClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.ClientPartitionDetail;
import com.marstor.msa.cdp.socket.ClientAPI;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@ManagedBean(name = "partitionDetailsBean")
@ViewScoped
public class PartitionDetailsBean {

    private ClientInfo client = new ClientInfo();
    private ClientPartitionDetail details;
    private List<Details> detailsView = new ArrayList();
    private int dNum = 0;
    private int pNum = 0;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.partition";
    private String ip = "192.168.1.87";
    private int port = 1100;

    public PartitionDetailsBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        port = Integer.parseInt(request.getParameter("port"));
        dNum = Integer.parseInt(request.getParameter("dNum"));
        pNum = Integer.parseInt(request.getParameter("pNum"));
        client.setIp(ip);
        client.setPort(port);
        initPartitionDetails();
        initName();
        updateTable();
    }
    
    public void update(){
        initPartitionDetails();
        updateTable();
    }

    public final void initPartitionDetails() {
        details = ClientAPI.GetSourcePartitionDetail(dNum, pNum, this.client);
        if(details == null){
            ClientAPI.GetErrorCode();
        }
    }

    public final void initName() {
        if(details == null){
            return;
        }
        if (details.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            for (int i = 0; i < 11; i++) {
                Details g = new Details();
                g.setName(res.get(basename, "name" + i));
                detailsView.add(g);
            }
        } else {
            for (int i = 0; i < 19; i++) {
                Details g = new Details();
                g.setName(res.get(basename, "names" + i));
                detailsView.add(g);
            }
        }
    }

    public final void updateTable() {
        if (details == null) {
            return;
        }
        if (details.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            String strSourcePartition = "磁盘 " + String.valueOf(details.CDPDiskNumber)
                    + ", 分区 " + String.valueOf(details.CDPPartitionNumber);
            String strRestoreStatus = Utility.getRestoreStatusString(details.RestoreStatus);
            String strRestorePercent = details.SyncPercent + "%";
            String strRestoreStartTime = details.SyncStartTime;
            String strRestoreEndTime = "";
            String strSyncTotal = Utility.getByteString(details.SyncTotal);

            if (details.SyncPercent >= 100) {
                strRestoreStatus = "恢复完成";
                strRestoreEndTime = details.SyncEndTime;
            }
            detailsView.get(0).setValue(String.valueOf(details.DiskNumber));
            detailsView.get(1).setValue(String.valueOf(details.PartitionNumber));

            detailsView.get(2).setValue(Utility.getPartitionTypeString(details.PartitionType));
            detailsView.get(3).setValue(Utility.getSizeString(details.PartitionSize) + " GB");
            detailsView.get(4).setValue(details.MountPoint);
            detailsView.get(5).setValue(strRestoreStatus);
            detailsView.get(6).setValue(strSourcePartition);
            detailsView.get(7).setValue(strRestorePercent);
            detailsView.get(8).setValue(strRestoreStartTime);
            detailsView.get(9).setValue(strRestoreEndTime);
            detailsView.get(10).setValue(strSyncTotal);
        } else {
            String strCDPMirror = "";
            String strUseCache = "";
            String strCDPStatus = "";
            String strHookError = "";
            String SyncWarningCode = "";
            String strSyncStatus = "";
            String strSyncPercent = "";
            String strSyncStartTime = "";
            String strSyncEndTime = "";
            String strSyncTotal = "";
            String strHookStartTime = "";
            String strHookEndTime = "";
            String strHookTotal = "";
            if (details.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
                strCDPMirror = "磁盘 " + String.valueOf(details.CDPDiskNumber)
                        + ", 分区 " + String.valueOf(details.CDPPartitionNumber);
                strUseCache = details.UseCache ? "是" : "否";
                strCDPStatus = Utility.getCDPStatusString(details.CDPStatus);
                SyncWarningCode = Utility.getWarningString(details.SyncWarningCode);
//                if (details.HookErrorCode != CDPConstants.ERROR_SUCCESS) {
//                    strHookError = Utility.getSystemErrorString(details.HookErrorCode);
//                }
                if (details.SystemErrorCode != CDPConstants.ERROR_SUCCESS) {
                    strHookError = Utility.getSystemErrorString(details.SystemErrorCode);
                }
                if (details.CDPStatus != CDPConstants.CDP_STATUS_INIT) {
                    strSyncStatus = Utility.getCDPStatusString(details.SyncStatus);
                    if (details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_RUNNING
                            || details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
                            || details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
                        strSyncPercent = details.SyncPercent + "%";
                        strSyncStartTime = details.SyncStartTime;
                        strSyncTotal = Utility.getByteString(details.SyncTotal);
                    }
                    if (details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
                            || details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
                        strSyncEndTime = details.SyncEndTime;
                    }
                    if (details.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING
                            || details.CDPStatus == CDPConstants.CAP_STATUS_HOOK_STOPED
                            || details.CDPStatus == CDPConstants.CDP_STATUS_HOOK_FAILED) {
                        strHookStartTime = details.HookStartTime;
                        strHookTotal = Utility.getByteString(details.HookTotal);
                    }
                    if (details.CDPStatus == CDPConstants.CAP_STATUS_HOOK_STOPED
                            || details.CDPStatus == CDPConstants.CDP_STATUS_HOOK_FAILED) {
                        strHookEndTime = details.HookEndTime;
                    }
                }
            }

            detailsView.get(0).setValue(String.valueOf(details.DiskNumber));
            detailsView.get(1).setValue(String.valueOf(details.PartitionNumber));

            detailsView.get(2).setValue(Utility.getPartitionTypeString(details.PartitionType));
            detailsView.get(3).setValue(Utility.getSizeString(details.PartitionSize) + " GB (" + Utility.getByteString(details.PartitionSize) + " 字节)");
            detailsView.get(4).setValue(details.MountPoint);
            detailsView.get(5).setValue(Utility.getCDPTypeString(details.CDPType));
            detailsView.get(6).setValue(strCDPMirror);
            detailsView.get(7).setValue(strUseCache);
            detailsView.get(8).setValue(strCDPStatus);
            detailsView.get(9).setValue(strHookError);
            detailsView.get(10).setValue(SyncWarningCode);
            detailsView.get(11).setValue(strSyncStatus);
            detailsView.get(12).setValue(strSyncPercent);
            detailsView.get(13).setValue(strSyncStartTime);
            detailsView.get(14).setValue(strSyncEndTime);
            detailsView.get(15).setValue(strSyncTotal);
            detailsView.get(16).setValue(strHookStartTime);
            detailsView.get(17).setValue(strHookEndTime);
            detailsView.get(18).setValue(strHookTotal);
        }

    }

    public String setting() {
        String param = "ip=" + ip + "&port=" + port;
        return "client?faces-redirect=true&amp;" + param;
    }

    public ClientPartitionDetail getDetails() {
        return details;
    }

    public void setDetails(ClientPartitionDetail details) {
        this.details = details;
    }

    public List<Details> getDetailsView() {
        return detailsView;
    }

    public void setDetailsView(List<Details> detailsView) {
        this.detailsView = detailsView;
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
    
    
}
