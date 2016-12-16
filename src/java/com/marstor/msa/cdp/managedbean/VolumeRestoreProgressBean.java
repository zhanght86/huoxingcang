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
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import java.util.ArrayList;
import java.util.List;
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
@ManagedBean(name = "vRPBean")
@ViewScoped
public class VolumeRestoreProgressBean {

    private ClientInfo client = new ClientInfo();
    private LinuxVolumeDetail details;
    private List<Details> detailsView = new ArrayList();
    public String vgName = "";
    public String vName = "";
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.v_restore_pro";
    private String ip = "192.168.1.87";
    private int port = 1100;
    private String strSize;
    private String strCDPType;
    private boolean disable = true;
    public String dMirror = "";
    public String pMirror = "";

    public VolumeRestoreProgressBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        if (request.getParameter("port") != null) {
            port = Integer.parseInt(request.getParameter("port"));
        }
        vgName = request.getParameter("vgName");
        vName = request.getParameter("vName");
        client.setIp(ip);
        client.setPort(port);
        strSize = request.getParameter("size");
        strCDPType = request.getParameter("cdpType");
        if(request.getParameter("dMirror") != null){
            dMirror = request.getParameter("dMirror");
        }
        if(request.getParameter("pMirror") != null){
            pMirror = request.getParameter("pMirror");
        }
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

    public void refresh() {
        initPartitionDetails();
        updateTable();
    }

    public final void initName() {
        if (details == null) {
            return;
        }
        if (details.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            for (int i = 0; i < 11; i++) {
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
            String strSourcePartition = "´ÅÅÌ " + details.CDP_VG
                    + ", ·ÖÇø " + details.CDP_V;
            String strRestoreStatus = Utility.getRestoreStatusString(details.SyncStatus);
            String strRestorePercent = details.SyncPercent + "%";
            String strRestoreStartTime = details.SyncStartTime;
            String strRestoreEndTime = "";
            String strSyncTotal = Utility.getByteString(details.SyncTotal);
            String strHookTotal = Utility.getByteString(details.HookTotal);

            if (details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
                    || details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
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
            detailsView.get(10).setValue(strHookTotal);

//            if (details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
//                    || details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
//                this.disable = true;
//
//                if (details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED) {
//                    boolean bRet = LinuxClientAPI.CancelVRestore(CDPConstants.CDP_RESTORE_TYPE_VOLUME, this.vgName, this.vName, this.client);
//                    if (!bRet) {
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
//                        return;
//                    }
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success"), global.get("error_mark")));
//                } else {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), global.get("error_mark")));
//                }
//            }
        }
    }

    public String setting() {
        //cancel restore 
        if (!this.disable) {
            RequestContext.getCurrentInstance().execute("cancelRestore.show();");
            return null;
        }
        String param = "ip=" + ip + "&port=" + port
                + "&vgName=" + this.vgName
                + "&vName=" + this.vName
                + "&vSize=" + this.strSize
                + "&cdpType=" + this.strCDPType
                + "&dMirror=" + this.dMirror
                + "&pMirror=" + this.pMirror;
        Debug.print("VolumeRestoreProgressBean --> VolumeRestoreBean:" + param);
        
        return "v_restore?faces-redirect=true&amp;" + param;
    }

    public String cancelRestore() {
        boolean bRet = LinuxClientAPI.CancelVRestore(CDPConstants.CDP_RESTORE_TYPE_VOLUME, this.vgName, this.vName, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            return null;
        }
        String param = "ip=" + ip + "&port=" + port;
        return "client_linux?faces-redirect=true&amp;" + param;
    }

    public String getBtn() {
        if (this.disable) {
            return global.get("return");
        } else {
            return res.get(basename, "btn");
        }
    }

    public String getIcon() {
        if (this.disable) {
            return "cancleIcon";
        } else {
            return "rollbackIcon";
        }
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

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
}
