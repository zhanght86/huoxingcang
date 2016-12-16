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
import com.marstor.msa.cdp.bean.LuInfoBean;
import com.marstor.msa.cdp.socket.CDPClientSocket;
import com.marstor.msa.cdp.socket.ClientAPI;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import java.text.DecimalFormat;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Administrator
 */
//@ManagedBean(name = "clientBean")
//@ViewScoped
public class ClientBean_bak_new {

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
    private TreeNode root = new DefaultTreeNode();
    private Object selected = new Object();
    private int dNum;
    private int pNum;
    private int sNum;

    public ClientBean_bak_new() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        port = Integer.parseInt(request.getParameter("port"));
        if (request.getParameter("dNum") != null) {
            dNum = Integer.parseInt(request.getParameter("dNum"));
        }
        if (request.getParameter("pNum") != null) {
            pNum = Integer.parseInt(request.getParameter("pNum"));
        }
        if (request.getParameter("sNum") != null) {
            sNum = Integer.parseInt(request.getParameter("sNum"));
        }
        client.setIp(ip);
        client.setPort(port);
        connectHost();
    }

    public final void connectHost() {
        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(ip, port)) {
            return;
        }
        client.setClientDisks(ClientAPI.GetSourceDisksInfo(client));
        if (client.getClientDisks() == null) {
            ClientAPI.GetErrorCode();
            return;
        }
        initObjects();
    }

    public boolean isDRestoring() {
        return dRestoring;
    }

    public void setDRestoring(boolean dRestoring) {
        this.dRestoring = dRestoring;
    }

    public boolean isPRestoring() {
        return pRestoring;
    }

    public void setPRestoring(boolean pRestoring) {
        this.pRestoring = pRestoring;
    }

    public void initObjects() {
        root = new DefaultTreeNode();
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        Map<String, LuInfoBean> VDs = cdp.getAllDiskInfos();

        for (ClientDiskInfo disk : client.getClientDisks()) {
            ClientDiskDetail DiskDetail = ClientAPI.GetSourceDiskDetail(disk.DiskNumber, this.client);

            if (DiskDetail == null) {
                ClientAPI.GetErrorCode();
                return;
            }
            if (DiskDetail.CDPType == CDPConstants.CDP_STATUS_INIT && DiskDetail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
                this.dJumpPara = "d_restore_pro.xhtml?faces-redirect=true&amp;ip=" + ip + "&amp;port=" + port + "&amp;dNum=" + DiskDetail.DiskNumber;
                this.dRestoring = true;

//                if (DiskDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
//                        || DiskDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
//                    boolean bRet = ClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, DiskDetail.DiskNumber, 0, this.client);
//                    if (!bRet) {
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
//                        return;
//                    }
//                }
            }
            disk.setDetail(DiskDetail);
//            String serialNumber = doubleReverse(DiskDetail.SerialNumber);
            String serialNumber = DiskDetail.SerialNumber;
            boolean flag = true;
            if (VDs != null) {
                for (LuInfoBean vd : VDs.values()) {
                    if (vd.getSerialNum().equals("")) {
                        continue;
                    }
                    if (serialNumber.equals(vd.getSerialNum())) {
                        disk.setIsMsa(true);
                        TreeNode d = new DefaultTreeNode(disk, root);
                        if (disk.PartitionInfos == null || disk.PartitionInfos.length == 0) {
                            flag = false;
                            break;
                        }

                        for (ClientPartitionInfo partition : disk.PartitionInfos) {
                            ClientPartitionDetail PartitionDetail = ClientAPI.GetSourcePartitionDetail(disk.DiskNumber, partition.PartitionNumber, this.client);
                            if (PartitionDetail == null) {
                                ClientAPI.GetErrorCode();
                                return;
                            }
                            partition.setDetail(PartitionDetail);
                            partition.setIsMsa(true);
                            partition.setDiskCDPType(disk.CDPType);
                            TreeNode p = new DefaultTreeNode(partition, d);
                            if (PartitionDetail.CDPType == CDPConstants.CDP_STATUS_INIT && PartitionDetail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
                                this.pJumpPara = "p_restore_pro.xhtml?faces-redirect=true&amp;ip=" + ip + "&amp;port=" + port + "&amp;dNum=" + PartitionDetail.DiskNumber + "&amp;pNum=" + PartitionDetail.PartitionNumber;
                                this.pRestoring = true;
//                                if (PartitionDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
//                                        || PartitionDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
//                                    boolean bRet = ClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, PartitionDetail.DiskNumber, pNum, this.client);
//                                    if (!bRet) {
//                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
//                                        return;
//                                    }
//                                }
                            }

                        }
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                TreeNode d = new DefaultTreeNode(disk, root);
                if (disk.PartitionInfos == null || disk.PartitionInfos.length == 0) {
                    continue;
                }
                for (ClientPartitionInfo partition : disk.PartitionInfos) {
                    ClientPartitionDetail PartitionDetail = ClientAPI.GetSourcePartitionDetail(disk.DiskNumber, partition.PartitionNumber, this.client);
                    if (PartitionDetail == null) {
                        ClientAPI.GetErrorCode();
                        return;
                    }
                    partition.setDetail(PartitionDetail);
                    partition.setDiskCDPType(disk.CDPType);
                    TreeNode p = new DefaultTreeNode(partition, d);
                    if (PartitionDetail.CDPType == CDPConstants.CDP_STATUS_INIT && PartitionDetail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
                        this.pJumpPara = "p_restore_pro.xhtml?faces-redirect=true&amp;ip=" + ip + "&amp;port=" + port + "&amp;dNum=" + PartitionDetail.DiskNumber + "&amp;pNum=" + PartitionDetail.PartitionNumber;
                        this.pRestoring = true;
//                                if (PartitionDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
//                                        || PartitionDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
//                                    boolean bRet = ClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, PartitionDetail.DiskNumber, pNum, this.client);
//                                    if (!bRet) {
//                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
//                                        return;
//                                    }
//                                }
                    }
                }
            }
        }
        _buildRowKeys(root);
    }

    public String jumpToRestoring() {
        if (this.dRestoring) {
            return this.dJumpPara;
        } else if (this.pRestoring) {
            return this.pJumpPara;
        } else {
            return null;
        }
    }
    private boolean dRestoring = false;
    private boolean pRestoring = false;
    private String dJumpPara;
    private String pJumpPara;

    private static void _buildRowKeys(TreeNode node) {
        int childCount = node.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                TreeNode childNode = node.getChildren().get(i);

                String childRowKey = (node.getParent() == null)
                        ? String.valueOf(i)
                        : node.getRowKey() + "_" + i;
                childNode.setRowKey(childRowKey);
                _buildRowKeys(childNode);
            }
        }
    }

    public String addStar(Object o) {
        String s;
        if (o instanceof ClientDiskInfo) {
            s = ((ClientDiskInfo) o).isMsa ? "*" : "";
        } else {
            s = ((ClientPartitionInfo) o).isMsa ? "*" : "";
        }
        return s;
    }

    private String doubleReverse(String serial) {
        char[] cs = serial.toCharArray();
        if (cs.length == 0) {
            return "";
        }
        if (serial.length() % 2 != 0) {
            return "";
        }

        char[] ret = new char[cs.length];
        for (int i = 0; i < cs.length; i++) {
            if (i % 2 == 0) {
                ret[i] = cs[i + 1];
            } else {
                ret[i] = cs[i - 1];
            }
        }
        return new String(ret);
    }

    public String getNum(Object o) {
        String s;
        if (o instanceof ClientDiskInfo) {
            s = res.get("disk") + ((ClientDiskInfo) o).DiskNumber;
        } else {
            s = res.get("partition") + ((ClientPartitionInfo) o).PartitionNumber;
        }
        return s;
    }

    public String getSize(Object o) {
        String s;
        if (o instanceof ClientDiskInfo) {
            s = this.getStrSize(((ClientDiskInfo) o).DiskSize);
        } else {
            s = this.getStrSize(((ClientPartitionInfo) o).PartitionSize);
        }
        return s;
    }

    public String getUsedSize(Object o) {
        String s;
        if (o instanceof ClientDiskInfo) {
            s = Integer.toString(((ClientDiskInfo) o).PartitionCount);
        } else {
            ClientPartitionInfo partition = (ClientPartitionInfo) o;
            if (partition.MountPoint.equals("")) {
                s = "";
            } else {
                s = this.getStrSize(partition.PartitionSize - partition.FreeSize, partition.PartitionSize)
                        + "(" + partition.MountPoint + ")";
            }
        }
        return s;
    }

    public String getCDPType(Object o) {
        String s;
        if (o instanceof ClientDiskInfo) {
            s = Utility.getCDPTypeString(((ClientDiskInfo) o).CDPType);
        } else {
            s = Utility.getCDPTypeString(((ClientPartitionInfo) o).CDPType);
        }
        return s;
    }

    public String getMirror(Object o) {
        String s;
        if (o instanceof ClientDiskInfo) {
            s = getDStrMirror((ClientDiskInfo) o);
        } else {
            s = getPStrMirror((ClientPartitionInfo) o);
        }
        return s;
    }

    public String getStatus(Object o) {
        String s;
        if (o instanceof ClientDiskInfo) {
            s = getDStrStatus((ClientDiskInfo) o);
        } else {
            s = getPStrStatus((ClientPartitionInfo) o);
        }
        return s;
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

    public String mirror(Object o) {
        String s;
        if (o instanceof ClientDiskInfo) {
            this.selectedD = (ClientDiskInfo) o;
            s = dMirror();
        } else {
            this.selectedP = (ClientPartitionInfo) o;
            s = pMirror();
        }
        return s;
    }

    public String dMirror() {
        if (this.selectedD.CDPType == CDPConstants.CDP_TYPE_INIT) {
            String param = "ip=" + ip + "&port=" + port
                    + "&dNum=" + this.selectedD.DiskNumber
                    + "&pCount=" + this.selectedD.PartitionCount
                    + "&size=" + this.selectedD.DiskSize
                    + "&cdpType=" + this.selectedD.CDPType;
            return "d_cdp?faces-redirect=true&amp;" + param;
        } else {
            RequestContext.getCurrentInstance().execute("deleteDiskCDP.show()");
            return null;
        }

    }

    public void dDelMirror() {
        this.selectedD = (ClientDiskInfo) this.selected;
        boolean bRet = ClientAPI.DeleteCDP(CDPConstants.CDP_TYPE_DISK, this.selectedD.DiskNumber, 0, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
        }
    }

    public String CDPAction(Object o) {
        String s;
        if (o instanceof ClientDiskInfo) {
            this.selectedD = (ClientDiskInfo) o;
            s = dCDP();
        } else {
            this.selectedP = (ClientPartitionInfo) o;
            s = pCDP();
        }
        return s;
    }

    public String dCDP() {
        if (this.selectedD.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
            RequestContext.getCurrentInstance().execute("stopDiskCDP.show()");
            return null;
        } else {
            String param = "ip=" + ip + "&port=" + port
                    + "&dNum=" + this.selectedD.DiskNumber + "&sNum=" + this.selectedD.CDPDiskNumber;
            return "start_dcdp.xhtml?faces-redirect=true&amp;" + param;
        }
    }

    public String dStartCDP() {
        if (!this.protectAfterRestart) {
            auto = 0;
        }
        int f = this.fast ? 1 : 0;
        boolean bRet = ClientAPI.StartCDPSyn(CDPConstants.CDP_TYPE_DISK, mode, auto, f, client,
                this.dNum, 0);
        if (bRet) {
            connectHost();
            if (this.immediate) {
                MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
                ClientDiskDetail DiskDetail = ClientAPI.GetSourceDiskDetail(sNum, this.client);
                if(DiskDetail == null){
                    ClientAPI.GetErrorCode();
                    return null;
                }
                cdp.createMirrorProtect(ip, String.valueOf(port), this.dNum, 0, true, DiskDetail.SerialNumber);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail21"), global.get("error_mark")));
            return null;
        }
        String param = "ip=" + ip + "&port=" + port;
        return "client.xhtml?faces-redirect=true&amp;" + param;
    }

    public String returnStr() {
        String param = "ip=" + ip + "&port=" + port;
        return "client.xhtml?faces-redirect=true&amp;" + param;
    }

    public void dStopCDP() {
        System.out.println("selectedD.DiskNumber:" + this.selectedD.DiskNumber);
        this.selectedD = (ClientDiskInfo) this.selected;
        System.out.println("selected.DiskNumber:" + this.selectedD.DiskNumber);
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
                    + "&dNum=" + this.selectedP.DiskNumber + "&pNum=" + this.selectedP.PartitionNumber
                    + "&pSize=" + this.selectedP.PartitionSize + "&cdpType=" + this.selectedP.CDPType;
            return "p_cdp?faces-redirect=true&amp;" + param;
        } else {
            RequestContext.getCurrentInstance().execute("deletePartitionCDP.show()");
            return null;
        }

    }

    public void pDelMirror() {
        this.selectedP = (ClientPartitionInfo) this.selected;
        boolean bRet = ClientAPI.DeleteCDP(CDPConstants.CDP_TYPE_DISK, this.selectedP.DiskNumber, selectedP.PartitionNumber, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), global.get("error_mark")));


        }
    }

    public String pCDP() {
        if (this.selectedP.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
            RequestContext.getCurrentInstance().execute("stopPartitionCDP.show()");
            return null;
        } else {
            String param = "ip=" + ip + "&port=" + port + "&dNum=" + this.selectedP.DiskNumber
                    + "&pNum=" + this.selectedP.PartitionNumber + "&sNum=" + this.selectedP.CDPDiskNumber;
            return "start_pcdp.xhtml?faces-redirect=true&amp;" + param;
        }
    }

    public String pStartCDP() {
        if (!this.protectAfterRestart) {
            auto = 0;
        }
        int f = this.fast ? 1 : 0;
        boolean bRet = ClientAPI.StartCDPSyn(CDPConstants.CDP_TYPE_PARTITION, mode, auto, f, client,
                this.dNum, this.pNum);
        if (bRet) {
            connectHost();
            if (this.immediate) {
                MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
                ClientDiskDetail DiskDetail = ClientAPI.GetSourceDiskDetail(sNum, this.client);
                if(DiskDetail == null){
                    ClientAPI.GetErrorCode();
                    return null;
                }
                cdp.createMirrorProtect(ip, String.valueOf(port), this.dNum, this.pNum, false, DiskDetail.SerialNumber);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail21"), global.get("error_mark")));
        }
        String param = "ip=" + ip + "&port=" + port;
        return "client.xhtml?faces-redirect=true&amp;" + param;
    }

    public void pStopCDP() {
        System.out.println("selectedP.PartitionNumber:" + this.selectedP.DiskNumber);
        this.selectedP = (ClientPartitionInfo) this.selected;
        System.out.println("selected.PartitionNumber:" + this.selectedP.DiskNumber);
        boolean bRet = ClientAPI.StopCDP(CDPConstants.CDP_TYPE_PARTITION,
                this.selectedP.DiskNumber, this.selectedP.PartitionNumber, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail3"), global.get("error_mark")));
        }
    }

    public String RestoreAction(Object o) {
        String s;
        if (o instanceof ClientDiskInfo) {
            this.selectedD = (ClientDiskInfo) o;
            s = dRestore();
        } else {
            this.selectedP = (ClientPartitionInfo) o;
            s = pRestore();
        }
        return s;
    }

    public String dRestore() {
        this.selectedD = (ClientDiskInfo) this.selected;
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + this.selectedD.DiskNumber
                + "&pCount=" + this.selectedD.PartitionCount
                + "&size=" + this.selectedD.DiskSize
                + "&cdpType=" + this.selectedD.CDPType;
        return "d_restore?faces-redirect=true&amp;" + param;
    }

    public String pRestore() {
        this.selectedP = (ClientPartitionInfo) this.selected;
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + this.selectedP.DiskNumber + "&pNum=" + this.selectedP.PartitionNumber
                + "&pSize=" + this.selectedP.PartitionSize + "&cdpType=" + this.selectedP.CDPType;
        return "p_restore?faces-redirect=true&amp;" + param;
    }

    public String DetailsAction(Object o) {
        String s;
        if (o instanceof ClientDiskInfo) {
            this.selectedD = (ClientDiskInfo) o;
            s = dDetails();
        } else {
            this.selectedP = (ClientPartitionInfo) o;
            s = pDetails();
        }
        return s;
    }

    public String dDetails() {
        this.selectedD = (ClientDiskInfo) this.selected;
        if (selectedD.CDPType == CDPConstants.CDP_STATUS_INIT && selectedD.detail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            if (selectedD.detail.SyncPercent != 100) {
                String param = "ip=" + ip + "&port=" + port + "&dNum=" + selectedD.DiskNumber;
                return "d_restore_pro?faces-redirect=true&amp;" + param;
            }
        }
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + selectedD.DiskNumber;
        return "disk?faces-redirect=true&amp;" + param;
    }

    public String pDetails() {
        this.selectedP = (ClientPartitionInfo) this.selected;
        if (selectedP.CDPType == CDPConstants.CDP_STATUS_INIT && selectedP.detail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            if (selectedP.detail.SyncPercent != 100) {
                String param = "ip=" + ip + "&port=" + port + "&dNum=" + selectedP.DiskNumber + "&pNum=" + selectedP.PartitionNumber;
                return "p_restore_pro?faces-redirect=true&amp;" + param;
            }
        }
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + selectedP.DiskNumber + "&pNum=" + selectedP.PartitionNumber;
        return "partition?faces-redirect=true&amp;" + param;
    }

    public String getStrSize(long size) {
        return Utility.getSizeString(size) + " GB";
    }

    public String getStrSize(long size, long total) {
        String s = Utility.getSizeString(size) + " GB";
//        DecimalFormat df = new DecimalFormat("0.00");
//
//        s = s + "（" + String.valueOf(df.format((double) size * 100 / (double) total)) + "%）";

        return s;
    }

    public String getStrType(int type) {
        return Utility.getCDPTypeString(type);
    }

    public String getO1(Object o) {
        int type;
        if (o instanceof ClientDiskInfo) {
            type = ((ClientDiskInfo) o).CDPType;
        } else {
            type = ((ClientPartitionInfo) o).CDPType;
        }

        if (type == CDPConstants.CDP_TYPE_INIT) {
            return res.get(basename, "o1_0");
        } else {
            return res.get(basename, "o1_1");
        }
    }

    public String getO2(Object o) {
        int status;
        if (o instanceof ClientDiskInfo) {
            status = ((ClientDiskInfo) o).CDPStatus;
        } else {
            status = ((ClientPartitionInfo) o).CDPStatus;
        }
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

    public boolean isDisable(Object o, int i) {
        boolean dis;
        if (o instanceof ClientDiskInfo) {
            this.selectedD = (ClientDiskInfo) o;
            dis = DiskDisable((ClientDiskInfo) o, i);
        } else {
            this.selectedP = (ClientPartitionInfo) o;
            dis = PartitionDisable((ClientPartitionInfo) o, i);
        }
        return dis;
    }

    public boolean isRendered(Object o) {
        if (o instanceof ClientDiskInfo) {
            this.selectedD = (ClientDiskInfo) o;
            if (this.selectedD.CDPStatus == CDPConstants.CDP_STATUS_INIT) {
                return false;
            }
        } else {
            this.selectedP = (ClientPartitionInfo) o;
            if (this.selectedP.CDPStatus == CDPConstants.CDP_STATUS_INIT) {
                return false;
            }
        }
        return true;
    }

    public boolean diskDisable(Object o) {
        boolean dis;
        if (o instanceof ClientDiskInfo) {
            int i = ((ClientDiskInfo) o).CDPStatus;
            dis = (i == 4 ? true : false);
        } else {
            dis = false;
        }
        return dis;
    }

    public boolean DiskDisable(ClientDiskInfo D, int o) {
        bShowCreateCDP = false;
        bShowDeleteCDP = false;
        bShowStartCDP = false;
        bShowStopCDP = false;
        bShowRestoreData = false;
        bShowRDetail = true;

        if (D.CDPType == CDPConstants.CDP_TYPE_DISK) {
            if (D.CDPStatus != CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowDeleteCDP = true;
            }
            if (D.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowStopCDP = true;
            } else {
                bShowStartCDP = true;
            }
        } else if (D.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
        } else {
            bShowCreateCDP = true;
        }

        if ((D.detail.RestoreStatus != CDPConstants.CDP_RESTORE_STATUS_RUNNING)
                && (D.CDPType == CDPConstants.CDP_TYPE_INIT)) {
            bShowRestoreData = true;
        }
        if (D.detail.SyncPercent == 100 && (D.CDPType == CDPConstants.CDP_TYPE_INIT)) {
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

            case 20:
                return bShowStartCDP;
            case 21:
                return bShowStopCDP;
            case 22:
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

    public boolean PartitionDisable(ClientPartitionInfo P, int o) {
        bShowCreateCDP = false;
        bShowDeleteCDP = false;
        bShowStartCDP = false;
        bShowStopCDP = false;
        bShowRestoreData = false;
        bShowRDetail = true;

        if (P.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
            if (P.CDPStatus != CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowDeleteCDP = true;
            }
            if (P.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowStopCDP = true;
            } else {
                bShowStartCDP = true;
            }
        } else if (P.CDPType == CDPConstants.CDP_TYPE_DISK) {
        } else {
            bShowCreateCDP = true;
        }

        if ((P.detail.RestoreStatus != CDPConstants.CDP_RESTORE_STATUS_RUNNING)
                && (P.CDPType == CDPConstants.CDP_TYPE_INIT)) {
            bShowRestoreData = true;
        }

        if (P.detail.SyncPercent == 100 && (P.CDPType == CDPConstants.CDP_TYPE_INIT)) {
            bShowRestoreData = true;
        }

        //CDPType改为（0:未设置 1:磁盘保护 2:分区保护 3：磁盘镜像 4：分区镜像）
        if (P.CDPType == 3 || P.CDPType == 4) {
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = true;
        }

        //CDPType改为（0:未设置 1:磁盘保护 2:分区保护 3：磁盘镜像 4：分区镜像）
        if (P.diskCDPType == 1 || P.diskCDPType == 3 || P.diskCDPType == 4) {
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = true;
        }

        switch (o) {
            case 1:
                if (P.CDPType == CDPConstants.CDP_TYPE_INIT) {
                    return bShowCreateCDP;
                } else {
                    return bShowDeleteCDP;
                }
            case 20:
                return bShowStartCDP;
            case 21:
                return bShowStopCDP;
            case 22:
                if (P.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
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
    public int mode = 1;
    public int auto = 3;
    public boolean immediate;
    public boolean fast = false;
    public boolean protectAfterRestart;

    public boolean isFast() {
        return fast;
    }

    public void setFast(boolean fast) {
        this.fast = fast;
    }

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

    public TreeNode getRoot() {
        return root;
    }

    public Object getSelected() {
        return selected;
    }

    public void setSelected(Object selected) {
        this.selected = selected;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void change() {
    }
}
