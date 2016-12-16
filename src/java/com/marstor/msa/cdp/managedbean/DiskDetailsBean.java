/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.ClientDiskDetail;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.model.ClientPartitionInfo;
import com.marstor.msa.cdp.socket.ClientAPI;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "diskDetailsBean")
@ViewScoped
public class DiskDetailsBean {

    private ClientInfo client = new ClientInfo();
    private ClientDiskDetail details;
    private List<Details> detailsView = new ArrayList();
    private int dNum = 0;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.disk";
    private String ip = "192.168.1.87";
    private int port = 1100;

    public DiskDetailsBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        if(request.getParameter("port") != null){
            port = Integer.parseInt(request.getParameter("port"));
        }
        if(request.getParameter("dNum") != null){
            dNum = Integer.parseInt(request.getParameter("dNum"));
        }
        
        client.setIp(ip);
        client.setPort(port);
        initDiskDetails();    
        initName();
        updateTable();
    }

    public final void initDiskDetails() {
        details = ClientAPI.GetSourceDiskDetail(dNum, this.client);
        if(details == null){
            ClientAPI.GetErrorCode();
        }
    }
    
    public void update(){
        initDiskDetails();  
        updateTable();
    }
    
    public final void updateTable(){
        if (details == null) {
            return;
        }
        if (details.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            String strSourceDisk = "´ÅÅÌ " + String.valueOf(details.CDPDiskNumber);
            String strRestoreStatus = Utility.getRestoreStatusString(details.RestoreStatus);
            String strRestorePercent = details.SyncPercent + "%";
            String strRestoreStartTime = details.SyncStartTime;
            String strRestoreEndTime = "";
            String strSyncTotal = Utility.getByteString(details.SyncTotal);

            if (details.SyncPercent >= 100) {
                strRestoreStatus = "»Ö¸´Íê³É";
                strRestoreEndTime = details.SyncEndTime;
            }
            detailsView.get(0).setValue(String.valueOf(details.DiskNumber));
            detailsView.get(1).setValue(Utility.getSizeString(details.DiskSize) + " GB");
            detailsView.get(2).setValue(details.VendorID);
            detailsView.get(3).setValue(details.ProductID);
            detailsView.get(4).setValue(details.SerialNumber);
            detailsView.get(5).setValue(details.ProductRevision);
            detailsView.get(6).setValue(strRestoreStatus);
            detailsView.get(7).setValue(strSourceDisk);
            detailsView.get(8).setValue(strRestorePercent);
            detailsView.get(9).setValue(strRestoreStartTime);
            detailsView.get(10).setValue(strRestoreEndTime);
            detailsView.get(11).setValue(strSyncTotal);
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
                        
            if (details.CDPType == CDPConstants.CDP_TYPE_DISK) {
                strCDPMirror = "´ÅÅÌ " + String.valueOf(details.CDPDiskNumber);
                strUseCache = details.UseCache ? "ÊÇ" : "·ñ";
                strCDPStatus = Utility.getCDPStatusString(details.CDPStatus);
                SyncWarningCode = Utility.getWarningString(details.SyncWarningCode);
//                if (details.HookErrorCode != CDPConstants.ERROR_SUCCESS) {
//                    strHookError = Utility.getSystemErrorString(details.HookErrorCode);
//                }
                if (details.HookErrorCode != CDPConstants.ERROR_SUCCESS) {
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
            detailsView.get(1).setValue(Utility.getSizeString(details.DiskSize) + " GB (" + Utility.getByteString(details.DiskSize) + " ×Ö½Ú)");

            detailsView.get(2).setValue(details.VendorID);
            detailsView.get(3).setValue(details.ProductID);
            detailsView.get(4).setValue(details.SerialNumber);
            detailsView.get(5).setValue(details.ProductRevision);
            detailsView.get(6).setValue(Utility.getCDPTypeString(details.CDPType));
            detailsView.get(7).setValue(strCDPMirror);
            detailsView.get(8).setValue(strUseCache);
            detailsView.get(9).setValue(strCDPStatus);
            detailsView.get(10).setValue(strHookError);
            detailsView.get(11).setValue(SyncWarningCode);
            detailsView.get(12).setValue(strSyncStatus);
            detailsView.get(13).setValue(strSyncPercent);
            detailsView.get(14).setValue(strSyncStartTime);
            detailsView.get(15).setValue(strSyncEndTime);
            detailsView.get(16).setValue(strSyncTotal);
            detailsView.get(17).setValue(strHookStartTime);
            detailsView.get(18).setValue(strHookEndTime);
            detailsView.get(19).setValue(strHookTotal);
        }

    }

    public final void initName() {
        if(details == null){
            return;
        }
        if (details.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            for (int i = 0; i < 12; i++) {
                Details g = new Details();
                g.setName(res.get(basename, "name" + i));
                detailsView.add(g);
            }
        } else {
            for (int i = 0; i < 20; i++) {
                Details g = new Details();
                g.setName(res.get(basename, "names" + i));
                detailsView.add(g);
            }
        }

    }

    public String setting() {
        String param = "ip=" + ip + "&port=" + port;
        return "client?faces-redirect=true&amp;" + param;
    }

    public ClientDiskDetail getDetails() {
        return details;
    }

    public void setDetails(ClientDiskDetail details) {
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
