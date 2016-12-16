/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.ClientDiskDetail;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.model.ClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.ClientPartitionDetail;
import com.marstor.msa.cdp.model.LinuxVolumeDetail;
import com.marstor.msa.cdp.socket.LinuxClientAPI;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import java.util.ArrayList;
import java.util.List;
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
@ManagedBean(name = "vDetailsBean")
@ViewScoped
public class VolumeDetailsBean {

    private ClientInfo client = new ClientInfo();
    private LinuxVolumeDetail details;
    private List<Details> detailsView = new ArrayList();
    public String vgName = "";
    public String vName = "";
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.volume";
    private String ip = "192.168.1.87";
    private int port = 1100;

    public VolumeDetailsBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        port = Integer.parseInt(request.getParameter("port"));
        vgName = request.getParameter("vgName");
        vName = request.getParameter("vName");
        client.setIp(ip);
        client.setPort(port);
        initPartitionDetails();
        initName();
        updateTable();
    }

    public final void initPartitionDetails() {
         details = LinuxClientAPI.GetVolumeDetail(vgName, vName, this.client);
         if(details == null){
            LinuxClientAPI.GetErrorCode();
        }
    }

    public final void initName() {
        if (details.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            for (int i = 0; i < 10; i++) {
                Details g = new Details();
                g.setName(res.get(basename, "name" + i));
                detailsView.add(g);
            }
        } else {
            for (int i = 0; i < 16; i++) {
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
            String strSourcePartition = "卷组 " + details.CDP_VG
                        + ", 卷 " +details.CDP_V;
                String strRestoreStatus = Utility.getRestoreStatusString(details.RestoreStatus);
                String strRestorePercent = details.SyncPercent + "%";
                String strRestoreStartTime = details.SyncStartTime;
                String strRestoreEndTime = "";
                String strSyncTotal = Utility.getByteString(details.SyncTotal);

                if (details.SyncPercent >= 100) {
                    strRestoreStatus = "恢复完成";
                    strRestoreEndTime = details.SyncEndTime;
                }
            detailsView.get(0).setValue(details.vgName);
            detailsView.get(1).setValue(details.vName);

            detailsView.get(2).setValue(Utility.getSizeString(details.vSize) + " GB");
            detailsView.get(3).setValue(details.MountPoint);
            detailsView.get(4).setValue(strRestoreStatus);
            detailsView.get(5).setValue(strSourcePartition);
            detailsView.get(6).setValue(strRestorePercent);
            detailsView.get(7).setValue(strRestoreStartTime);
            detailsView.get(8).setValue(strRestoreEndTime);
            detailsView.get(9).setValue(strSyncTotal);
        } else {
            String strCDPMirror = "";
                String strUseCache = "";
                String strCDPStatus = "";
                String strSyncStatus = "";
                String strSyncPercent = "";
                String strSyncStartTime = "";
                String strSyncEndTime = "";
                String strSyncTotal = "";
                String strHookStartTime = "";
                String strHookEndTime = "";
                String strHookTotal = "";
                if (details.CDPType == CDPConstants.CDP_TYPE_VOLUME) {
                    strCDPMirror = "磁盘" + details.CDP_VG
                            + "，分区 " + details.CDP_V;
                    strUseCache = details.UseCache ? "是" : "否";
                    strCDPStatus = Utility.getCDPStatusString(details.CDPStatus);
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

            detailsView.get(0).setValue(details.vgName);
            detailsView.get(1).setValue(details.vName);

            detailsView.get(2).setValue(Utility.getSizeString(details.vSize) + " GB (" + Utility.getByteString(details.vSize) + " 字节)");
            detailsView.get(3).setValue(details.MountPoint);
            detailsView.get(4).setValue(Utility.getCDPTypeString(details.CDPType));
            detailsView.get(5).setValue(strCDPMirror);
            detailsView.get(6).setValue(strUseCache);
            detailsView.get(7).setValue(strCDPStatus);
            detailsView.get(8).setValue(strSyncStatus);
            detailsView.get(9).setValue(strSyncPercent);
            detailsView.get(10).setValue(strSyncStartTime);
            detailsView.get(11).setValue(strSyncEndTime);
            detailsView.get(12).setValue(strSyncTotal);
            detailsView.get(13).setValue(strHookStartTime);
            detailsView.get(14).setValue(strHookEndTime);
            detailsView.get(15).setValue(strHookTotal);
        }

    }

    public String setting() {
        String param = "ip=" + ip + "&port=" + port;
        return "client?faces-redirect=true&amp;" + param;
    }
    
    public void update(){
        initPartitionDetails();
        updateTable();
    }

    public LinuxVolumeDetail getDetails() {
        return details;
    }

    public void setDetails(LinuxVolumeDetail details) {
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
