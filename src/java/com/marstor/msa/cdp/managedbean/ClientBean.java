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
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.List;
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
@ManagedBean(name = "clientBean")
@ViewScoped
public class ClientBean {

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
    private List<Integer> disksDest_Restore = new ArrayList(); //磁盘恢复类型的磁盘号
    private List<Integer> disksSrc_Restore = new ArrayList(); //磁盘镜像(恢复数据)类型的磁盘号
    private List<Integer> pDisksDest_Restore = new ArrayList(); //分区恢复类型的分区的磁盘号
    private List<Integer> pDisksSrc_Restore = new ArrayList();  //分区镜像(恢复数据)类型的磁盘号
    private List<String> partitionsDest_Restore = new ArrayList();//分区恢复类型的分区的磁盘号和分区号的组合
    private List<String> partitionsSrc_Restore = new ArrayList(); //分区镜像类型的分区的磁盘号和分区号的组合

    public ClientBean() {
        Debug.print("ClientBean()");
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
        Debug.print("connectHost()");
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
        Debug.print("isDRestoring()");
        return dRestoring;
    }

    public void setDRestoring(boolean dRestoring) {
        Debug.print("setDRestoring()");
        this.dRestoring = dRestoring;
    }

    public boolean isPRestoring() {
        Debug.print("isPRestoring()");
        return pRestoring;
    }

    public void setPRestoring(boolean pRestoring) {
        Debug.print("setPRestoring()");
        this.pRestoring = pRestoring;
    }

    public void initObjects() {
        Debug.print("initObjects()");
        root = new DefaultTreeNode();
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        Map<String, LuInfoBean> VDs = cdp.getAllDiskInfos();

        for (ClientDiskInfo disk : client.getClientDisks()) {
            ClientDiskDetail DiskDetail = ClientAPI.GetSourceDiskDetail(disk.DiskNumber, this.client);

            if (DiskDetail == null) {
                ClientAPI.GetErrorCode();
                return;
            }
            disk.setDetail(DiskDetail);
            if (DiskDetail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING
                    && DiskDetail.CDPType == CDPConstants.CDP_TYPE_INIT) {
                this.disksDest_Restore.add(disk.DiskNumber);
                this.disksSrc_Restore.add(disk.detail.CDPDiskNumber);
            }
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
                            if (PartitionDetail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING
                                    && PartitionDetail.CDPType == CDPConstants.CDP_TYPE_INIT) {
                                this.pDisksDest_Restore.add(PartitionDetail.DiskNumber);
                                this.pDisksSrc_Restore.add(PartitionDetail.CDPDiskNumber);
                                String pDest = String.valueOf(PartitionDetail.DiskNumber)
                                        + String.valueOf(PartitionDetail.PartitionNumber);
                                String pSrc = String.valueOf(PartitionDetail.CDPDiskNumber)
                                        + String.valueOf(PartitionDetail.CDPPartitionNumber);
                                this.partitionsDest_Restore.add(pDest);
                                this.partitionsSrc_Restore.add(pSrc);
                            }
                            partition.setDetail(PartitionDetail);
                            partition.setIsMsa(true);
                            partition.setDiskCDPType(disk.CDPType);
                            TreeNode p = new DefaultTreeNode(partition, d);
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
                    if (PartitionDetail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING
                            && PartitionDetail.CDPType == CDPConstants.CDP_TYPE_INIT) {
                        this.pDisksDest_Restore.add(PartitionDetail.DiskNumber);
                        this.pDisksSrc_Restore.add(PartitionDetail.CDPDiskNumber);
                        String pDest = String.valueOf(PartitionDetail.DiskNumber)
                                + String.valueOf(PartitionDetail.PartitionNumber);
                        String pSrc = String.valueOf(PartitionDetail.CDPDiskNumber)
                                + String.valueOf(PartitionDetail.CDPPartitionNumber);
                        this.partitionsDest_Restore.add(pDest);
                        this.partitionsSrc_Restore.add(pSrc);
                    }
                    partition.setDetail(PartitionDetail);
                    partition.setDiskCDPType(disk.CDPType);
                    TreeNode p = new DefaultTreeNode(partition, d);
                }
            }
        }
        _buildRowKeys(root);
    }

    public String jumpToRestoring() {
        Debug.print("jumpToRestoring()");
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
        Debug.print("_buildRowKeys()");
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
        Debug.print("addStar()");
        String s;
        if (o instanceof ClientDiskInfo) {
            s = ((ClientDiskInfo) o).isMsa ? "*" : "";
        } else {
            s = ((ClientPartitionInfo) o).isMsa ? "*" : "";
        }
        return s;
    }

    private String doubleReverse(String serial) {
        Debug.print("doubleReverse()");
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
        Debug.print("getNum()");
        String s;
        if (o instanceof ClientDiskInfo) {
            s = res.get("disk") + ((ClientDiskInfo) o).DiskNumber;
        } else {
            s = res.get("partition") + ((ClientPartitionInfo) o).PartitionNumber;
        }
        return s;
    }

    public String getSize(Object o) {
        Debug.print("getSize()");
        String s;
        if (o instanceof ClientDiskInfo) {
            s = this.getStrSize(((ClientDiskInfo) o).DiskSize);
        } else {
            s = this.getStrSize(((ClientPartitionInfo) o).PartitionSize);
        }
        return s;
    }

    public String getUsedSize(Object o) {
        Debug.print("getUsedSize()");
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
        Debug.print("getCDPType()");
        String s;
        boolean isDest_Restore;
        boolean isSrc_Restore;

        if (o instanceof ClientDiskInfo) {
            isDest_Restore = this.disksDest_Restore.contains(((ClientDiskInfo) o).DiskNumber);
            isSrc_Restore = this.disksSrc_Restore.contains(((ClientDiskInfo) o).DiskNumber);
            boolean ispdDest_Restore = this.pDisksDest_Restore.contains(((ClientDiskInfo) o).DiskNumber);
            boolean ispdSrc_Restore = this.pDisksSrc_Restore.contains(((ClientDiskInfo) o).DiskNumber);
            if (isDest_Restore) {
                s = "磁盘恢复";
            } else if (isSrc_Restore) {
                s = "磁盘镜像";
            } else if (ispdDest_Restore) {
                s = "分区恢复";
            } else if (ispdSrc_Restore) {
                s = "分区镜像";
            } else {
                s = Utility.getCDPTypeString(((ClientDiskInfo) o).CDPType);
            }

        } else {
            String p = String.valueOf(((ClientPartitionInfo) o).DiskNumber)
                    + String.valueOf(((ClientPartitionInfo) o).PartitionNumber);

            isDest_Restore = this.partitionsDest_Restore.contains(p);
            isSrc_Restore = this.partitionsSrc_Restore.contains(p);

            s = Utility.getCDPTypeString(((ClientPartitionInfo) o).CDPType,
                    isDest_Restore, "分区", isSrc_Restore);
        }
        return s;
    }

    public String getMirror(Object o) {
        Debug.print("getMirror()");
        String s;
        if (o instanceof ClientDiskInfo) {
            s = getDStrMirror((ClientDiskInfo) o);
        } else {
            s = getPStrMirror((ClientPartitionInfo) o);
        }
        return s;
    }

    public String getStatus(Object o) {
        Debug.print("getStatus()");
        String s;
        if (o instanceof ClientDiskInfo) {
            s = getDStrStatus((ClientDiskInfo) o);
        } else {
            s = getPStrStatus((ClientPartitionInfo) o);
        }
        return s;
    }

    public String getDStrMirror(ClientDiskInfo d) {
        Debug.print("getDStrMirror()");
        if (d.CDPType == CDPConstants.CDP_TYPE_DISK) {
            return res.get(basename, "disk") + String.valueOf(d.CDPDiskNumber);
        } else {
            return "";
        }
    }

    public String getDStrStatus(ClientDiskInfo d) {
        Debug.print("getDStrStatus()");
        if (d.CDPType == CDPConstants.CDP_TYPE_DISK) {
            return Utility.getCDPStatusString(d.CDPStatus);
        } else if (d.CDPType == CDPConstants.CDP_TYPE_INIT) {
            return Utility.getCDPStatusString(d.CDPStatus, d.CDPType, d.detail.RestoreStatus);

        } else {
            return "";
        }
    }

    public String getPStrMirror(ClientPartitionInfo p) {
        Debug.print("getPStrMirror()");
        if (p.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
            return res.get(basename, "disk") + String.valueOf(p.CDPDiskNumber)
                    + res.get(basename, "p") + String.valueOf(p.CDPPartitionNumber);
        } else {
            return "";
        }
    }

    public String getPStrStatus(ClientPartitionInfo p) {
        Debug.print("getPStrStatus()");
        if (p.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
            return Utility.getCDPStatusString(p.CDPStatus);
        } else if (p.CDPType == CDPConstants.CDP_TYPE_INIT) {
            return Utility.getCDPStatusString(p.CDPStatus, p.CDPType, p.detail.RestoreStatus);
        } else {
            return "";
        }
    }

    public String mirror(Object o) {
        Debug.print("mirror()");
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
        Debug.print("dMirror()");
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
        Debug.print("dDelMirror()");
        this.selectedD = (ClientDiskInfo) this.selected;
        boolean bRet = ClientAPI.DeleteCDP(CDPConstants.CDP_TYPE_DISK, this.selectedD.DiskNumber, 0, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
        }
    }

    public String CDPAction(Object o) {
        Debug.print("CDPAction()");
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
        Debug.print("dCDP()");
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
        Debug.print("dStartCDP()");
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
                if (DiskDetail == null) {
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
        Debug.print("returnStr()");
        String param = "ip=" + ip + "&port=" + port;
        return "client.xhtml?faces-redirect=true&amp;" + param;
    }

    public void dStopCDP() {
        Debug.print("dStopCDP()");
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
        Debug.print("pMirror()");
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
        Debug.print("pDelMirror()");
        this.selectedP = (ClientPartitionInfo) this.selected;
        boolean bRet = ClientAPI.DeleteCDP(CDPConstants.CDP_TYPE_DISK, this.selectedP.DiskNumber, selectedP.PartitionNumber, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), global.get("error_mark")));


        }
    }

    public String pCDP() {
        Debug.print("pCDP()");
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
        Debug.print("pStartCDP()");
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
                if (DiskDetail == null) {
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
        Debug.print("pStopCDP()");
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
        Debug.print("RestoreAction()");
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
        Debug.print("dRestore()");
        this.selectedD = (ClientDiskInfo) this.selected;
        int mirrorNum = 10000;
        if (selectedD.detail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            mirrorNum = selectedD.detail.CDPDiskNumber;
        }
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + this.selectedD.DiskNumber
                + "&pCount=" + this.selectedD.PartitionCount
                + "&size=" + this.selectedD.DiskSize
                + "&cdpType=" + this.selectedD.CDPType
                + "&mirror=" + mirrorNum;
        Debug.print("ClientBean --> DiskRestoreBean:" + param);
        return "d_restore?faces-redirect=true&amp;" + param;
    }

    public String pRestore() {
        Debug.print("pRestore()");
        this.selectedP = (ClientPartitionInfo) this.selected;
        int dMirror = 10000;
        int pMirror = 10000;
        if (selectedP.detail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            dMirror = selectedP.detail.CDPDiskNumber;
            pMirror = selectedP.detail.CDPPartitionNumber;
        }
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + this.selectedP.DiskNumber
                + "&pNum=" + this.selectedP.PartitionNumber
                + "&pSize=" + this.selectedP.PartitionSize
                + "&cdpType=" + this.selectedP.CDPType
                + "&dMirror=" + dMirror
                + "&pMirror=" + pMirror;
        return "p_restore?faces-redirect=true&amp;" + param;
    }

    public String DetailsAction(Object o) {
        Debug.print("DetailsAction()");
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
        Debug.print("dDetails()");
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
        Debug.print("pDetails()");
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
        Debug.print("getStrSize()");
        return Utility.getSizeString(size) + " GB";
    }

    public String getStrSize(long size, long total) {
        Debug.print("getStrSize()");
        String s = Utility.getSizeString(size) + " GB";
//        DecimalFormat df = new DecimalFormat("0.00");
//
//        s = s + "（" + String.valueOf(df.format((double) size * 100 / (double) total)) + "%）";

        return s;
    }

    public String getStrType(int type) {
        Debug.print("getStrType()");
        return Utility.getCDPTypeString(type);
    }

    public String getO1(Object o) {
        Debug.print("getO1()");
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
        Debug.print("getO2()");
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
        Debug.print("getSelectedD()");
        return selectedD;
    }

    public void setSelectedD(ClientDiskInfo selectedD) {
        Debug.print("setSelectedD()");
        this.selectedD = selectedD;
    }

    public ClientPartitionInfo getSelectedP() {
        Debug.print("getSelectedP()");
        return selectedP;
    }

    public void setSelectedP(ClientPartitionInfo selectedP) {
        Debug.print("setSelectedP()");
        this.selectedP = selectedP;
    }

    public ClientInfo getClient() {
        Debug.print("getClient()");
        return client;
    }

    public void setClient(ClientInfo client) {
        Debug.print("setClient()");
        this.client = client;
    }

    public boolean isDisable(Object o, int i) {
        Debug.print("isDisable()");
        boolean dis;
        if (o instanceof ClientDiskInfo) {
            this.selectedD = (ClientDiskInfo) o;
            dis = _DiskDisable((ClientDiskInfo) o, i);
        } else {
            this.selectedP = (ClientPartitionInfo) o;
            dis = PartitionDisable((ClientPartitionInfo) o, i);
        }
        return dis;
    }

    public boolean isRendered(Object o) {
        Debug.print("isRendered()");
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

    public boolean _DiskDisable(ClientDiskInfo D, int o) {
        Debug.print("_DiskDisable()");
        Debug.print("DiskNumber:" + D.DiskNumber);
        Debug.print("CDPType:" + D.CDPType);
        Debug.print("RestoreStatus:" + D.detail.RestoreStatus);


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
        } else if (D.detail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING
                && (D.CDPType == CDPConstants.CDP_TYPE_INIT)) {
            bShowRDetail = false;
            bShowRestoreData = true;
            bShowCreateCDP = false;
        } else {
            bShowCreateCDP = true;
        }

        if ((D.CDPType == CDPConstants.CDP_TYPE_INIT)) {
            bShowRestoreData = true;
        }
        //CDPType改为（0:未设置 1:磁盘保护 2:分区保护 3：磁盘镜像 4：分区镜像）
        if (D.CDPType == 3 || D.CDPType == 4) {
            return false;
        }

        boolean isDest_Restore = this.disksDest_Restore.contains(D.DiskNumber);
        boolean isSrc_Restore = this.disksSrc_Restore.contains(D.DiskNumber);
        boolean ispdDest_Restore = this.pDisksDest_Restore.contains(D.DiskNumber);
        boolean ispdSrc_Restore = this.pDisksSrc_Restore.contains(D.DiskNumber);

        if (isDest_Restore) {//磁盘恢复
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = true;
            bShowRDetail = false;
        } else if (isSrc_Restore) {//磁盘镜像
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        } else if (ispdDest_Restore) {//分区恢复
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        } else if (ispdSrc_Restore) {//分区镜像
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
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
        Debug.print("PartitionDisable()");
        Debug.print("DiskNumber:" + P.DiskNumber);
        Debug.print("PartitionNumber:" + P.PartitionNumber);
        Debug.print("CDPType:" + P.CDPType);
        Debug.print("RestoreStatus:" + P.detail.RestoreStatus);
        
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
        } else if (P.detail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING
                && (P.CDPType == CDPConstants.CDP_TYPE_INIT)) {
            bShowRDetail = false;
            bShowRestoreData = true;
            bShowCreateCDP = false;
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

        boolean isDiskDest_Restore = this.disksDest_Restore.contains(P.DiskNumber);
        boolean isDiskSrc_Restore = this.disksSrc_Restore.contains(P.DiskNumber);
        
        String p = String.valueOf(P.DiskNumber)
                + String.valueOf(P.PartitionNumber);
        
        boolean isDest_Restore = this.partitionsDest_Restore.contains(p);
        boolean isSrc_Restore = this.partitionsSrc_Restore.contains(p);

        if (isDiskDest_Restore) {//磁盘恢复
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        } else if (isDiskSrc_Restore) {//磁盘镜像
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        } else if (isDest_Restore) {//分区恢复
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = true;
            bShowRDetail = false;
        } else if (isSrc_Restore) {//分区镜像
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
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
        Debug.print("isFast()");
        return fast;
    }

    public void setFast(boolean fast) {
        Debug.print("setFast()");
        this.fast = fast;
    }

    public int getMode() {
        Debug.print("getMode()");
        return mode;
    }

    public void setMode(int mode) {
        Debug.print("setMode()");
        this.mode = mode;
    }

    public int getAuto() {
        Debug.print("getAuto()");
        return auto;
    }

    public void setAuto(int auto) {
        Debug.print("setAuto()");
        this.auto = auto;
    }

    public boolean isImmediate() {
        Debug.print("isImmediate()");
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        Debug.print("setImmediate()");
        this.immediate = immediate;
    }

    public boolean isProtectAfterRestart() {
        Debug.print("isProtectAfterRestart()");
        return protectAfterRestart;
    }

    public void setProtectAfterRestart(boolean protectAfterRestart) {
        Debug.print("setProtectAfterRestart()");
        this.protectAfterRestart = protectAfterRestart;
    }

    public TreeNode getRoot() {
        Debug.print("getRoot()");
        return root;
    }

    public Object getSelected() {
        Debug.print("getSelected()");
        return selected;
    }

    public void setSelected(Object selected) {
        Debug.print("setSelected()");
        this.selected = selected;
    }

    public String getIp() {
        Debug.print("getIp()");
        return ip;
    }

    public void setIp(String ip) {
        Debug.print("setIp()");
        this.ip = ip;
    }

    public void change() {
        Debug.print("change()");
    }
}
