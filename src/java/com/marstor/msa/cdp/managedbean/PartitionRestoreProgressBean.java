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
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import java.util.ArrayList;
import java.util.List;
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
@ManagedBean(name = "pRPBean")
@ViewScoped
public class PartitionRestoreProgressBean {

    private ClientInfo client = new ClientInfo();
    private ClientPartitionDetail details;
    private List<Details> detailsView = new ArrayList();
    private int dNum = 0;
    private int pNum = 0;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.p_restore_pro";
    private String ip = "192.168.1.87";
    private int port = 1100;
    private String strSize;
    private String strCDPType;
    private boolean disable = true;
    public int dMirror = 10000;
    public int pMirror = 10000;
    public boolean stop = false;

    public PartitionRestoreProgressBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        if (request.getParameter("port") != null) {
            port = Integer.parseInt(request.getParameter("port"));
        }
        if (request.getParameter("dNum") != null) {
            dNum = Integer.parseInt(request.getParameter("dNum"));
        }
        if (request.getParameter("pNum") != null) {
            pNum = Integer.parseInt(request.getParameter("pNum"));
        }

        client.setIp(ip);
        client.setPort(port);
        strSize = request.getParameter("size");
        strCDPType = request.getParameter("cdpType");
        if(request.getParameter("dMirror") != null){
            dMirror = Integer.parseInt(request.getParameter("dMirror"));
        }
        if(request.getParameter("pMirror") != null){
            pMirror = Integer.parseInt(request.getParameter("pMirror"));
        }
        initPartitionDetails();
        initName();
        updateTable();
    }

    public final void initPartitionDetails() {
        details = ClientAPI.GetSourcePartitionDetail(dNum, pNum, this.client);
        if(details == null){
            ClientAPI.GetErrorCode();
        }
    }

    public void refresh() {
        initPartitionDetails();
        updateTable();
    }

    public final void initName() {
        if (details == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "getDetailsFailed"), global.get("error_mark")));
            return;
        }
        if (details.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            for (int i = 0; i < 12; i++) {
                Details g = new Details();
                g.setName(res.get(basename, "name" + i));
                detailsView.add(g);
            }
        } else {
            for (int i = 0; i < 17; i++) {
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
            String strSourcePartition = "´ÅÅÌ " + String.valueOf(details.CDPDiskNumber)
                    + ", ·ÖÇø " + String.valueOf(details.CDPPartitionNumber);
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
            detailsView.get(11).setValue(strHookTotal);

//            if (details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
//                    || details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
//                this.disable = true;
//                if (details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED) {
//                    RequestContext.getCurrentInstance().execute("cancelR.show()");
//                } else {
//                    ClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, dNum, pNum, this.client);
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
                + "&dNum=" + this.dNum
                + "&pNum=" + this.pNum
                + "&pSize=" + this.strSize
                + "&cdpType=" + this.strCDPType
                + "&dMirror=" + this.dMirror
                + "&pMirror=" + this.pMirror;
        Debug.print("PartitonRestoreProgressBean --> PartitionRestoreBean:" + param);
        
        return "p_restore?faces-redirect=true&amp;" + param;
    }
    
    public void cancelR() {
        boolean bRet = ClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, dNum, pNum, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success"), global.get("error_mark")));
    }

    public String cancelRestore() {
        boolean bRet = ClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, dNum, pNum, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            return null;
        }
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

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
}
