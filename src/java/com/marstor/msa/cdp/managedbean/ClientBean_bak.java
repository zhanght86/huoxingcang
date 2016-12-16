/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.ClientDiskDetail;
import com.marstor.msa.cdp.model.ClientDiskInfo;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.model.ClientPartitionDetail;
import com.marstor.msa.cdp.model.ClientPartitionInfo;
import com.marstor.msa.cdp.socket.CDPClientSocket;
import com.marstor.msa.cdp.socket.ClientAPI;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
//@ManagedBean(name = "clientBean")
//@ViewScoped
public class ClientBean_bak {

    private ClientInfo client = new ClientInfo();
    private ClientDiskInfo selectedD = new ClientDiskInfo();
    private ClientPartitionInfo selectedP = new ClientPartitionInfo();
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.client";
    private String ip = "192.168.1.87";
    private int port = 1100;
    boolean bShowCreateCDP = false;
    boolean bShowDeleteCDP = false;
    boolean bShowStartCDP = false;
    boolean bShowStopCDP = false;
    boolean bShowRestoreData = false;
    boolean bShowRDetail = false;

    public ClientBean_bak() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        port = Integer.parseInt(request.getParameter("port"));
        client.setIp(ip);
        client.setPort(port);
        connectHost();
    }

    public final void connectHost() {
        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(ip, port)) {
        }
        client.setClientDisks(ClientAPI.GetSourceDisksInfo(client));
    }

    public String getDStrMirror(ClientDiskInfo d) {
        if (d.CDPType == CDPConstants.CDP_TYPE_DISK) {
            return res.get(basename, "disk") + String.valueOf(d.CDPDiskNumber);
        } else {
            return "";
        }
    }

    public String getDStrStatus(ClientDiskInfo d) {
        if (d.CDPType == CDPConstants.CDP_TYPE_DISK) {
            return Utility.getCDPStatusString(d.CDPStatus);
        } else {
            return "";
        }
    }

    public String getPStrMirror(ClientPartitionInfo p) {
        if (p.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
            return res.get(basename, "disk") + String.valueOf(p.CDPDiskNumber)
                    + res.get(basename, "p") + String.valueOf(p.CDPPartitionNumber);
        } else {
            return "";
        }
    }

    public String getPStrStatus(ClientPartitionInfo p) {
        if (p.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
            return Utility.getCDPStatusString(p.CDPStatus);
        } else {
            return "";
        }
    }

    public String dMirror() {
        if (this.selectedD.CDPType == CDPConstants.CDP_TYPE_INIT) {
            String param = "ip=" + ip + "&port=" + port
                    + "&dNum=" + this.selectedD.DiskNumber;
            return "d_cdp?faces-redirect=true&amp;" + param;
        } else {
            RequestContext.getCurrentInstance().execute("deleteDiskCDP.show()");
            return null;
        }

    }

    public void dDelMirror() {
        boolean bRet = ClientAPI.DeleteCDP(CDPConstants.CDP_TYPE_DISK, this.selectedD.DiskNumber, 0, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));

        }
    }

    public String dCDP() {
        if (this.selectedD.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
            RequestContext.getCurrentInstance().execute("dStopCDP.show()");
            return null;
        } else {
            String param = "ip=" + ip + "&port=" + port;
            return "start_cdp.xhtml?faces-redirect=true&amp;" + param;
        }
    }

    public String dStartCDP() {
        if (!this.protectAfterRestart) {
            auto = 0;
        }
        boolean bRet = ClientAPI.StartCDPSyn(CDPConstants.CDP_TYPE_DISK, 0, mode, auto, client,
                this.selectedD.DiskNumber, 0);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail21"), global.get("error_mark")));
        }
        String param = "ip=" + ip + "&port=" + port;
        return "client.xhtml?faces-redirect=true&amp;" + param;
    }
    
    public String returnStr(){
        String param = "ip=" + ip + "&port=" + port;
        return "client.xhtml?faces-redirect=true&amp;" + param;
    }

    public void dStopCDP() {
        boolean bRet = ClientAPI.StopCDP(CDPConstants.CDP_TYPE_DISK, this.selectedD.DiskNumber, 0, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail2"), global.get("error_mark")));
        }
    }

    public String pMirror() {
        if (this.selectedP.CDPType == CDPConstants.CDP_TYPE_INIT) {
            String param = "ip=" + ip + "&port=" + port
                    + "&dNum=" + this.selectedP.DiskNumber + "&pNum=" + this.selectedP.PartitionNumber;
            return "p_cdp?faces-redirect=true&amp;" + param;
        } else {
            RequestContext.getCurrentInstance().execute("deletePartitionCDP.show()");
            return null;
        }

    }

    public void pDelMirror() {
        boolean bRet = ClientAPI.DeleteCDP(CDPConstants.CDP_TYPE_DISK, this.selectedP.DiskNumber, selectedP.PartitionNumber, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), global.get("error_mark")));


        }
    }

    public void pCDP() {
        if (this.selectedP.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
            RequestContext.getCurrentInstance().execute("pStopCDP.show()");
        } else {
            boolean bRet = ClientAPI.StartCDPSyn(CDPConstants.CDP_TYPE_PARTITION, 0, mode, auto, client,
                    this.selectedP.DiskNumber, this.selectedP.PartitionNumber);
            if (bRet) {
                connectHost();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail31"), global.get("error_mark")));
            }
        }
    }

    public void pStopCDP() {
        boolean bRet = ClientAPI.StopCDP(CDPConstants.CDP_TYPE_DISK,
                this.selectedP.DiskNumber, this.selectedP.PartitionNumber, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail3"), global.get("error_mark")));
        }
    }

    public String dRestore() {
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + this.selectedD.DiskNumber;
        return "d_restore?faces-redirect=true&amp;" + param;
    }

    public String pRestore() {
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + this.selectedP.DiskNumber + "&pNum=" + this.selectedP.PartitionNumber;
        return "p_restore?faces-redirect=true&amp;" + param;
    }

    public String dDetails() {
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + selectedD.DiskNumber;
        return "disk?faces-redirect=true&amp;" + param;
    }

    public String pDetails() {
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + selectedP.DiskNumber + "&pNum=" + selectedP.PartitionNumber;
        return "partition?faces-redirect=true&amp;" + param;
    }

    public String getStrSize(long size) {
        return Utility.getSizeString(size) + " GB";
    }

    public String getStrType(int type) {
        return Utility.getCDPTypeString(type);
    }

    public String getO1(int type) {
        if (type == CDPConstants.CDP_TYPE_INIT) {
            return res.get(basename, "o1_0");
        } else {
            return res.get(basename, "o1_1");
        }
    }

    public String getO2(int status) {
        if (status == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
            return res.get(basename, "o2_1");
        } else {
            return res.get(basename, "o2_0");
        }
    }

    public ClientDiskInfo getSelectedD() {
        return selectedD;
    }

    public void setSelectedD(ClientDiskInfo selectedD) {
        this.selectedD = selectedD;
    }

    public ClientPartitionInfo getSelectedP() {
        return selectedP;
    }

    public void setSelectedP(ClientPartitionInfo selectedP) {
        this.selectedP = selectedP;
    }

    public ClientInfo getClient() {
        return client;
    }

    public void setClient(ClientInfo client) {
        this.client = client;
    }

    public boolean DiskDisable(ClientDiskInfo D, int o) {
        bShowCreateCDP = false;
        bShowDeleteCDP = false;
        bShowStartCDP = false;
        bShowStopCDP = false;
        bShowRestoreData = false;
        bShowRDetail = true;
        long lDiskNum = D.DiskNumber;

        ClientDiskInfo[] DiskInfos = ClientAPI.GetSourceDisksInfo(this.client);
        if (DiskInfos == null) {
            Logger.getLogger(ClientBean_bak.class.getName()).log(Level.SEVERE, "Call CDPClientAPI.GetSourceDisksInfo Failed! Error: {0}", ClientAPI.GetErrorString());
            return false;
        }

        ClientDiskInfo DiskInfo = ClientAPI.GetSourceDiskInfoByNumber((int) lDiskNum, DiskInfos);
        if (DiskInfo == null) {
            Logger.getLogger(ClientBean_bak.class.getName()).log(Level.SEVERE, "Call CDPClientAPI.GetSourceDiskInfoByNumber Failed. DiskNumber={0}", lDiskNum);
            return false;
        }

        ClientDiskDetail DiskDetail = ClientAPI.GetSourceDiskDetail((int) lDiskNum, ClientBean_bak.this.client);
        if (DiskDetail == null) {
            Logger.getLogger(ClientBean_bak.class.getName()).log(Level.SEVERE, "Call CDPClientAPI.GetSourceDiskDetail Failed! Error: {0}", ClientAPI.GetErrorString());
            return false;
        }

        if (DiskInfo.CDPType == CDPConstants.CDP_TYPE_DISK) {
            if (DiskInfo.CDPStatus != CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowDeleteCDP = true;
            }
            if (DiskInfo.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowStopCDP = true;
            } else {
                bShowStartCDP = true;
            }
        } else if (DiskInfo.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
        } else {
            bShowCreateCDP = true;
        }

        if ((DiskDetail.RestoreStatus != CDPConstants.CDP_RESTORE_STATUS_RUNNING)
                && (DiskDetail.CDPType == CDPConstants.CDP_TYPE_INIT)) {
            bShowRestoreData = true;
        }


        //CDPType改为（0:未设置 1:磁盘保护 2:分区保护 3：磁盘镜像 4：分区镜像）
        if (D.CDPType == 3 || D.CDPType == 4) {
            return false;
        }

        switch (o) {
            case 1:
                if (D.CDPType == CDPConstants.CDP_TYPE_INIT) {
                    return bShowCreateCDP;
                } else {
                    return bShowDeleteCDP;
                }

            case 2:
                if (D.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                    return bShowStopCDP;
                } else {
                    return bShowStartCDP;
                }
            case 3:
                return bShowRestoreData;
            case 4:
                return bShowRDetail;
            default:
                return false;
        }


    }

    public boolean PartitionDisable(ClientPartitionInfo D, int o) {
        bShowCreateCDP = false;
        bShowDeleteCDP = false;
        bShowStartCDP = false;
        bShowStopCDP = false;
        bShowRestoreData = false;
        bShowRDetail = true;
        long lDiskNum = D.DiskNumber;
        long lPartitionNum = D.PartitionNumber;

        ClientDiskInfo[] DiskInfos = ClientAPI.GetSourceDisksInfo(this.client);
        if (DiskInfos == null) {
            Logger.getLogger(ClientBean_bak.class.getName()).log(Level.SEVERE, "Call CDPClientAPI.GetSourceDisksInfo Failed! Error: {0}", ClientAPI.GetErrorString());
            return false;
        }

        ClientDiskInfo DiskInfo = ClientAPI.GetSourceDiskInfoByNumber((int) lDiskNum, DiskInfos);
        if (DiskInfo == null) {
            Logger.getLogger(ClientBean_bak.class.getName()).log(Level.SEVERE,
                    "Call CDPClientAPI.GetSourceDiskInfoByNumber Failed. DiskNumber=", lDiskNum);
            return false;
        }

        ClientPartitionInfo PartitionInfo = ClientAPI.GetSourcePartitionInfoByNumber((int) lDiskNum, (int) lPartitionNum, DiskInfos);
        if (PartitionInfo == null) {
            Logger.getLogger(ClientBean_bak.class.getName()).log(Level.SEVERE, "Call CDPClientAPI.GetSourcePartitionInfoByNumber Failed. DiskNumber={0} PartitionNumber={1}", new Object[]{lDiskNum, lPartitionNum});
            return false;
        }

        ClientPartitionDetail PartitionDetail = ClientAPI.GetSourcePartitionDetail((int) lDiskNum, (int) lPartitionNum, this.client);

        if (PartitionInfo.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
            if (PartitionInfo.CDPStatus != CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowDeleteCDP = true;
            }
            if (PartitionInfo.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowStopCDP = true;
            } else {
                bShowStartCDP = true;
            }
        } else if (PartitionInfo.CDPType == CDPConstants.CDP_TYPE_DISK) {
        } else {
            bShowCreateCDP = true;
        }

        if ((PartitionDetail.RestoreStatus != CDPConstants.CDP_RESTORE_STATUS_RUNNING)
                && (PartitionDetail.CDPType == CDPConstants.CDP_TYPE_INIT)) {
            bShowRestoreData = true;
        }

        //CDPType改为（0:未设置 1:磁盘保护 2:分区保护 3：磁盘镜像 4：分区镜像）
        if (D.CDPType == 3 || D.CDPType == 4) {
            return false;
        }

        //CDPType改为（0:未设置 1:磁盘保护 2:分区保护 3：磁盘镜像 4：分区镜像）
        if (D.CDPType == 3 || D.CDPType == 4 || DiskInfo.CDPType == 3 || DiskInfo.CDPType == 4) {
            return false;
        }

        switch (o) {
            case 1:
                if (D.CDPType == CDPConstants.CDP_TYPE_INIT) {
                    return bShowCreateCDP;
                } else {
                    return bShowDeleteCDP;
                }
            case 2:
                if (D.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                    return bShowStopCDP;
                } else {
                    return bShowStartCDP;
                }
            case 3:
                return bShowRestoreData;
            case 4:
                return bShowRDetail;
            default:
                return false;
        }


    }
    public int mode = 0;
    public int auto = 0;
    public boolean immediate;
    public boolean protectAfterRestart;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public boolean isProtectAfterRestart() {
        return protectAfterRestart;
    }

    public void setProtectAfterRestart(boolean protectAfterRestart) {
        this.protectAfterRestart = protectAfterRestart;
    }
}
