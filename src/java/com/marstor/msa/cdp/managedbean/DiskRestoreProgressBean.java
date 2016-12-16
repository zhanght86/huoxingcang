/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.ClientDiskDetail;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.socket.ClientAPI;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "dRPBean")
@ViewScoped
public class DiskRestoreProgressBean {

    private ClientInfo client = new ClientInfo();
    private ClientDiskDetail details;
    private List<Details> detailsView = new ArrayList();
    private int dNum = -1;
    public int pcount;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.d_restore_pro";
    private String ip = "192.168.1.87";
    private int port = 1100;
    private String strSize;
    private String strCDPType;
    private boolean disable = true;
    public int mirrorNum = 10000;
    public boolean stop = false;

    public DiskRestoreProgressBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        if (request.getParameter("port") != null) {
            port = Integer.parseInt(request.getParameter("port"));
        }
        if (request.getParameter("dNum") != null) {
            dNum = Integer.parseInt(request.getParameter("dNum"));
        }
        if (request.getParameter("pCount") != null) {
            pcount = Integer.parseInt(request.getParameter("pCount"));
        }

        client.setIp(ip);
        client.setPort(port);

        strSize = request.getParameter("size");
        strCDPType = request.getParameter("cdpType");
        if(request.getParameter("mirror") != null){
            mirrorNum = Integer.parseInt(request.getParameter("mirror"));
        }
        initDiskDetails();
        initName();
        updateTable();
    }

    public final void initDiskDetails() {
        details = ClientAPI.GetSourceDiskDetail(dNum, this.client);
        if (details == null) {
            ClientAPI.GetErrorCode();
        }
    }

    public void refresh() {
        initDiskDetails();
        updateTable();
    }

    public final void updateTable() {
        if (details == null) {
            return;
        }
        if (details.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            String strSourceDisk = "╢еел " + String.valueOf(details.CDPDiskNumber);
            String strRestoreStatus = Utility.getRestoreStatusString(details.SyncStatus);
            String strRestorePercent = details.SyncPercent + "%";
            String strRestoreStartTime = details.SyncStartTime;
            String strRestoreEndTime = "";
            String strSyncTotal = Utility.getByteString(details.SyncTotal);
            String strHookTotal = Utility.getByteString(details.HookTotal);
            Debug.print("HookTotal:" + details.HookTotal);
            Debug.print("strHookTotal:" + strHookTotal);

            if (details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
                    || details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
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
            detailsView.get(12).setValue(strHookTotal);

            if (details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
                    || details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
                this.stop = true;
//                if (details.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED) {
//                    RequestContext.getCurrentInstance().execute("cancelR.show()");
//                } else {
//                    ClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, (int) dNum, 0, this.client);
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), global.get("error_mark")));
//                }
            }
        }
    }

    public void cancelR() {
        boolean bRet = ClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, (int) dNum, 0, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success"), global.get("error_mark")));
    }

    public final void initName() {
        if (details == null) {
            return;
        }
        if (details.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            for (int i = 0; i < 13; i++) {
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

    public String setting() {
        //cancel restore 
        if (!this.disable) {
            RequestContext.getCurrentInstance().execute("cancelRestore.show();");
            return null;
        }
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + this.dNum
                + "&pCount=" + this.pcount
                + "&size=" + this.strSize
                + "&cdpType=" + this.strCDPType
                + "&mirror=" + this.mirrorNum;
        Debug.print("DiskRestoreProgressBean --> DiskRestoreBean:" + param);
        
        return "d_restore?faces-redirect=true&amp;" + param;
    }

    public String cancelRestore() {
        this.stop = true;
        boolean bRet = ClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, (int) dNum, 0, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            return null;
        }
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

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
    
}
